package org.hisp.dhis.mobile.action.smscommand;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.option.OptionService;
import org.hisp.dhis.smscommand.SMSCode;
import org.hisp.dhis.smscommand.SMSCommand;
import org.hisp.dhis.smscommand.SMSCommandService;

import com.opensymphony.xwork2.Action;

public class EditSMSCommandForm implements Action {

    // services
    private SMSCommandService smsCommandService;
    private DataSetService dataSetService;
    private DataElementService dataElementService;

    // input fields
    private String name; // for lista
    private int selectedDataSetID; //
    private String codeDataelementOption;
    private String seperator;
    private String codeSeperator;

    private int selectedCommandID = -1;

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception {
        
        Set<SMSCode> codeSet = new HashSet<SMSCode>();
        
        @SuppressWarnings("unchecked")
        List<JSONObject> jsonCodes = (List<JSONObject>) JSONObject.fromObject(codeDataelementOption).get("codes");
        for(JSONObject x : jsonCodes){
            System.out.println(x.get("dataElementId") + " " + x.get("optionId") + " " + x.get("code"));
            SMSCode c = new SMSCode();
            c.setCode(x.getString("code"));
            c.setDataElement(dataElementService.getDataElement(x.getInt("dataElementId")));
            c.setOptionId(x.getInt("optionId"));
            codeSet.add(c);
        }
        
        if(codeSet.size() > 0){
            smsCommandService.save(codeSet);
        }
        
        SMSCommand c = getSMSCommand();
        if (selectedDataSetID > -1 && c != null) {
            c.setDataset(getDataSetService().getDataSet(getSelectedDataSetID()));
            c.setName(name);
            c.setSeperator(seperator);
            c.setCodes(codeSet);
            c.setCodeSeperator(codeSeperator);
            smsCommandService.save(c);
        }
        
        return SUCCESS;
    }

    public Collection<DataSet> getDataSets() {
        return getDataSetService().getAllDataSets();
    }

    public Set<DataElement> getDataSetElements() {
        DataSet d = dataSetService.getDataSet(selectedDataSetID);
        if (d != null) {
            return d.getDataElements();
        }
        return null;
    }

    public SMSCommand getSMSCommand() {
        return smsCommandService.getSMSCommand(selectedCommandID);

    }

    public DataSetService getDataSetService() {
        return dataSetService;
    }

    public void setDataSetService(DataSetService dataSetService) {
        this.dataSetService = dataSetService;
    }

    public int getSelectedDataSetID() {
        return selectedDataSetID;
    }

    public void setSelectedDataSetID(int selectedDataSetID) {
        this.selectedDataSetID = selectedDataSetID;
    }

    public String getCodeDataelementOption() {
        return codeDataelementOption;
    }

    public void setCodeDataelementOption(String codeDataelementOption) {
        this.codeDataelementOption = codeDataelementOption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SMSCommandService getSmsCommandService() {
        return smsCommandService;
    }

    public void setSmsCommandService(SMSCommandService smsCommandService) {
        this.smsCommandService = smsCommandService;
    }

    public int getSelectedCommandID() {
        return selectedCommandID;
    }

    public void setSelectedCommandID(int selectedCommandID) {
        this.selectedCommandID = selectedCommandID;
    }

    public String getSeperator() {
        return seperator;
    }

    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    public DataElementService getDataElementService() {
        return dataElementService;
    }

    public void setDataElementService(DataElementService dataElementService) {
        this.dataElementService = dataElementService;
    }

    public String getCodeSeperator() {
        return codeSeperator;
    }

    public void setCodeSeperator(String codeSeperator) {
        this.codeSeperator = codeSeperator;
    }
}