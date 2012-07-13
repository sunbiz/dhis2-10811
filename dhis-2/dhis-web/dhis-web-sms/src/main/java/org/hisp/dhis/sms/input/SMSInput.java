package org.hisp.dhis.sms.input;

import com.opensymphony.xwork2.Action;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.exolab.castor.types.Date;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.CalendarPeriodType;
import org.hisp.dhis.period.Period;

import org.hisp.dhis.sms.incoming.IncomingSms;
import org.hisp.dhis.sms.incoming.IncomingSmsStore;
import org.hisp.dhis.sms.incoming.SmsMessageEncoding;
import org.hisp.dhis.sms.incoming.SmsMessageStatus;
import org.hisp.dhis.smscommand.SMSCode;
import org.hisp.dhis.smscommand.SMSCommand;
import org.hisp.dhis.smscommand.SMSCommandService;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Christian
 */
public class SMSInput implements Action {

    private String msisdn, sender, message, dca, reffering_batch, network_id, concat_reference, concat_num_segments, concat_seq_num, received_time;
    private String source_id; // Probably like message id and should be an int
    private int msg_id; // unique for each sms
    private IncomingSms sms;
    private IncomingSmsStore smsStore;
    
    // Services
    private CurrentUserService currentUserService;
    private DataValueService dataValueService;
    private UserService userService;
    private OrganisationUnitService organisationUnitService;
    private SMSCommandService smsCommandService;
    @Autowired
    private DataElementCategoryService dataElementCategoryService;
    
    public SMSInput() {
    }

    @Override
    public String execute() throws Exception {
        System.out.println("Sender: " + sender + ", Message: " + message);
        IncomingSms sms = new IncomingSms();
        sms.setText(message);
        sms.setOriginator(sender);

        java.util.Date rec = null;
        try {
            Date received = Date.parseDate(received_time);
            rec = received.toDate();
        } catch (ParseException pe) {
            System.out.println("ERROR: No received_time input");
            return ERROR;
        }
        sms.setReceivedDate(rec);
        sms.setSentDate(rec); // This should probably be removed from incoming SMS entirely. Though other gateways may use it?
        sms.setEncoding(SmsMessageEncoding.ENC7BIT);
        sms.setStatus(SmsMessageStatus.INCOMING);
        sms.setId(msg_id);
        sms.setGatewayId("HARDCODEDTESTGATEWAY");

        int result = smsStore.save(sms);
        System.out.println("The result of SMS save is *trommevirvel* " + result);

        Collection<User> users = userService.getUsersByPhoneNumber(sender);

        OrganisationUnit orgunit = null;
        for (Iterator<User> it = users.iterator(); it.hasNext();) {
            User user = it.next();
            OrganisationUnit ou = user.getOrganisationUnit(); // Might be undefined if the user has more than one org.units "attached"
            if (orgunit == null) {
                orgunit = ou;
            } else if (orgunit.getId() == ou.getId()) {
                // same orgunit, no problem...
            } else {
                // orgunit and ou are different, ie. the phone number is registered to users at multiple facilities.
                // Now what should we do?
                System.out.println("user is registered to more than one orgunit, what orgunit should we pick?");
                return ERROR;
            }
        }
        System.out.println("Orgunit: " + orgunit);

        String[] marr = message.trim().split(" ");
        if (marr.length < 1) {
            return ERROR;
        }
        String commandString = marr[0];
        Collection<SMSCommand> commands = smsCommandService.getSMSCommands();
        for (Iterator<SMSCommand> it = commands.iterator(); it.hasNext();) {
            SMSCommand command = it.next();
            if (command.getName().equalsIgnoreCase(commandString)) {
                System.out.println("We got a hit on command name: " + commandString);
                //DataSet ds = command.getDataset();

                IParser p = new SMSParserKeyValue(command.getSeperator(), " ", " ", true, false); // hack, to be changed into message type handler of some sort
                Map<String, String> parsedMessage = p.parse(message);

                Collection<SMSCode> codes = command.getCodes();
                for (Iterator<SMSCode> it1 = codes.iterator(); it1.hasNext();) {
                    SMSCode code = it1.next();

                    if (parsedMessage.containsKey(code.getCode())) { // If SMS Code is in the SMS (parsed sms)

                        String storedBy = currentUserService.getCurrentUsername();

                        if (StringUtils.isBlank(storedBy)) {
                            storedBy = "[unknown] from [" + sender+"]";
                        }
                        System.out.println("Code.getOptionId() == " + code.getOptionId());
                        DataElementCategoryOptionCombo optionCombo = dataElementCategoryService.getDataElementCategoryOptionCombo(code.getDataElement().getCategoryCombo().getCategoryOptions());
                        
                        System.out.println("optionCombo == " + optionCombo);
                        Period period = code.getDataElement().getPeriodType().createPeriod();
                        CalendarPeriodType cpt = (CalendarPeriodType) period.getPeriodType();
                        period = cpt.getPreviousPeriod(period);

                        System.out.println("Found Code (" + code.getCode() + ") in message. " + code.getOptionId() + " & " + code.getDataElement());
                        DataValue dv = dataValueService.getDataValue(orgunit, code.getDataElement(), period, optionCombo);
                        if (dv != null) {
                            System.out.println("Updating dataValue, Code: " + code.getCode() +". Value: " + parsedMessage.get(code.getCode()) + " Period: " + period.getDisplayName() + ". " + period.getStartDateString() + " til " + period.getEndDate().toString());
                            dv.setValue(parsedMessage.get(code.getCode()));
                            dataValueService.updateDataValue(dv);
                        } else {
                            DataValue dataVal = new DataValue();
                            
                            if(optionCombo != null) {
                                dataVal.setOptionCombo(optionCombo);
                            }
                            if(orgunit != null) {
                                dataVal.setSource(orgunit);
                            }
                            if(code.getDataElement() != null) {
                                dataVal.setDataElement(code.getDataElement());
                            }
                            
                            dataVal.setPeriod(period);
                            dataVal.setComment("");
                            dataVal.setTimestamp(new java.util.Date());
                            dataVal.setStoredBy(storedBy);
                            dataVal.setValue(parsedMessage.get(code.getCode()));
                            dataValueService.addDataValue(dataVal);
                            /*
                            dataValueService.addDataValue(new DataValue(code.getDataElement(), period, orgunit, parsedMessage.get(code.getCode()),
                                    storedBy, new java.util.Date(), "", optionCombo));*/
                        }



                    }

                }
            }
        }

        // TODO DataEntry stuff

        return SUCCESS;
    }

    public void setDataElementCategoryService(DataElementCategoryService dataElementCategoryService) {
        this.dataElementCategoryService = dataElementCategoryService;
    }

    public void setSmsCommandService(SMSCommandService smsCommandService) {
        this.smsCommandService = smsCommandService;
    }

    public void setCurrentUserService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    public void setDataValueService(DataValueService dataValueService) {
        this.dataValueService = dataValueService;
    }

    public void setSmsStore(IncomingSmsStore smsStore) {
        System.out.println("Setting SMSStore: " + smsStore);
        this.smsStore = smsStore;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setOrganisationUnitService(OrganisationUnitService organisationUnitService) {
        this.organisationUnitService = organisationUnitService;
    }

    public String getConcat_num_segments() {
        return concat_num_segments;
    }

    public void setConcat_num_segments(String concat_num_segments) {
        this.concat_num_segments = concat_num_segments;
    }

    public String getConcat_reference() {
        return concat_reference;
    }

    public void setConcat_reference(String concat_reference) {
        this.concat_reference = concat_reference;
    }

    public String getConcat_seq_num() {
        return concat_seq_num;
    }

    public void setConcat_seq_num(String concat_seq_num) {
        this.concat_seq_num = concat_seq_num;
    }

    public String getDca() {
        return dca;
    }

    public void setDca(String dca) {
        this.dca = dca;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getNetwork_id() {
        return network_id;
    }

    public void setNetwork_id(String network_id) {
        this.network_id = network_id;
    }

    public String getReceived_time() {
        return received_time;
    }

    public void setReceived_time(String received_time) {
        this.received_time = received_time;
    }

    public String getReffering_batch() {
        return reffering_batch;
    }

    public void setReffering_batch(String reffering_batch) {
        this.reffering_batch = reffering_batch;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public IncomingSms getSms() {
        return sms;
    }

    public void setSms(IncomingSms sms) {
        this.sms = sms;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

}
