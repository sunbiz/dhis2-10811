package org.hisp.dhis.sms.parse;

/*
 * Copyright (c) 2004-2012, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.CompleteDataSetRegistrationService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.CalendarPeriodType;
import org.hisp.dhis.period.DailyPeriodType;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.QuarterlyPeriodType;
import org.hisp.dhis.period.WeeklyPeriodType;
import org.hisp.dhis.period.YearlyPeriodType;
import org.hisp.dhis.sms.incoming.IncomingSms;
import org.hisp.dhis.sms.outbound.OutboundSms;
import org.hisp.dhis.sms.outbound.OutboundSmsService;
import org.hisp.dhis.sms.outbound.OutboundSmsTransportService;
import org.hisp.dhis.smscommand.SMSCode;
import org.hisp.dhis.smscommand.SMSCommand;
import org.hisp.dhis.smscommand.SMSCommandService;
import org.hisp.dhis.system.util.ValidationUtils;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Christian and Magnus
 */
public class DefaultParserManager
    implements ParserManager
{

    private static final Log log = LogFactory.getLog( DefaultParserManager.class );

    private CompleteDataSetRegistrationService registrationService;

    private DataValueService dataValueService;

    private UserService userService;

    private SMSCommandService smsCommandService;

    private OutboundSmsService outboundSmsService;

    @Autowired
    private DataElementCategoryService dataElementCategoryService;

    @Autowired
    private OutboundSmsTransportService transportService;

    @Autowired
    private DataSetService dataSetService;

    @Transactional
    public void parse( IncomingSms sms )
    {
        try
        {
            parse( sms.getOriginator(), sms.getText() );
        }
        catch ( SMSParserException e )
        {
            sendSMS( e.getMessage(), sms.getOriginator() );
            return;
        }
    }

    private void sendSMS( String message, String sender )
    {
        String gatewayId = transportService.getDefaultGateway();
        if ( outboundSmsService != null )
        {
            outboundSmsService.sendMessage( new OutboundSms( message, sender ), gatewayId );
        }
    }

    @Transactional
    private void parse( String sender, String message )
        throws SMSParserException
    {

        if ( StringUtils.isEmpty( sender ) )
        {
            return;
        }

        sender = StringUtils.replace( sender, "+", "" );

        Collection<OrganisationUnit> orgUnits = getOrganisationUnitsByPhoneNumber( sender );
        if ( orgUnits == null || orgUnits.size() == 0 )
        {
            log.info( "No user found for phone number: " + sender );
            throw new SMSParserException( "No user associated with this phone number. Please contact your supervisor." );
        }

        if ( StringUtils.isEmpty( message ) )
        {
            throw new SMSParserException( "No command in SMS" );
        }

        String commandString = null;
        if ( message.indexOf( " " ) > 0 )
        {
            commandString = message.substring( 0, message.indexOf( " " ) );
            message = message.substring( commandString.length() );
        }
        else
        {
            commandString = message;
        }

        boolean foundCommand = false;

        for ( SMSCommand command : smsCommandService.getSMSCommands() )
        {
            if ( command.getName().equalsIgnoreCase( commandString ) )
            {
                foundCommand = true;
                if ( ParserType.KEY_VALUE_PARSER.equals( command.getParserType() ) )
                {
                    runKeyValueParser( sender, message, orgUnits, command );
                    break;
                }
                else
                {
                    runJ2MEParser( sender, message, orgUnits, command );
                    break;
                }
            }
        }
        if ( !foundCommand )
        {
            throw new SMSParserException( "Command '" + commandString + "' does not exist" );
        }
    }

    protected Collection<OrganisationUnit> getOrganisationUnitsByPhoneNumber( String sender )
    {
        Collection<OrganisationUnit> orgUnits = new ArrayList<OrganisationUnit>();
        Collection<User> users = userService.getUsersByPhoneNumber( sender );
        for ( User u : users )
        {
            if ( u.getOrganisationUnits() != null )
            {
                orgUnits.addAll( u.getOrganisationUnits() );
            }
        }

        return orgUnits;
    }

    private void runKeyValueParser( String sender, String message, Collection<OrganisationUnit> orgUnits,
        SMSCommand command )
    {
        IParser p = new SMSParserKeyValue();
        if ( !StringUtils.isBlank( command.getSeparator() ) )
        {
            p.setSeparator( command.getSeparator() );
        }

        Map<String, String> parsedMessage = p.parse( message );
        Date date = lookForDate( message );
        OrganisationUnit orgUnit = selectOrganisationUnit( orgUnits, parsedMessage );
        Period period = getPeriod( command, date );

        // Check if Data Set is locked
        if ( dataSetService.isLocked( command.getDataset(), period, orgUnit, null ) )
        {
            throw new SMSParserException( "Dataset is locked for the period " + period.getStartDate() + " - "
                + period.getEndDate() );
        }

        boolean valueStored = false;
        for ( SMSCode code : command.getCodes() )
        {
            if ( parsedMessage.containsKey( code.getCode().toUpperCase() ) )
            {
                valueStored = storeDataValue( sender, orgUnit, parsedMessage, code, command, date,
                    command.getDataset(), formIsComplete( command, parsedMessage ) );
            }
        }

        if ( parsedMessage.isEmpty() )
        {
            if ( StringUtils.isEmpty( command.getDefaultMessage() ) )
            {
                throw new SMSParserException( "No values reported for command '" + command.getName() + "'" );
            }
            else
            {
                throw new SMSParserException( command.getDefaultMessage() );
            }
        }
        else if ( !valueStored )
        {
            throw new SMSParserException( "Wrong format for command '" + command.getName() + "'" );
        }

        markCompleteDataSet( sender, orgUnit, parsedMessage, command, date );
        sendSuccessFeedback( sender, command, parsedMessage, date, orgUnit );

    }

    protected OrganisationUnit selectOrganisationUnit( Collection<OrganisationUnit> orgUnits,
        Map<String, String> parsedMessage )
    {
        OrganisationUnit orgUnit = null;

        for ( OrganisationUnit o : orgUnits )
        {
            if ( orgUnits.size() == 1 )
            {
                orgUnit = o;
            }
            if ( parsedMessage.containsKey( "ORG" ) && o.getCode().equals( parsedMessage.get( "ORG" ) ) )
            {
                orgUnit = o;
                break;
            }
        }

        if ( orgUnit == null && orgUnits.size() > 1 )
        {
            String messageListingOrgUnits = "Found more then one org unit for this number. Please specify one of the following:";
            for ( Iterator<OrganisationUnit> i = orgUnits.iterator(); i.hasNext(); )
            {
                OrganisationUnit o = i.next();
                messageListingOrgUnits += " " + o.getName() + ":" + o.getCode();
                if ( i.hasNext() )
                {
                    messageListingOrgUnits += ",";
                }
            }
            throw new SMSParserException( messageListingOrgUnits );
        }
        return orgUnit;
    }

    protected void sendSuccessFeedback( String sender, SMSCommand command, Map<String, String> parsedMessage,
        Date date, OrganisationUnit orgunit )
    {
        String reportBack = "Thank you! Values entered: ";
        String notInReport = "Missing values for: ";

        Period period = null;

        Map<String, DataValue> codesWithDataValues = new TreeMap<String, DataValue>();
        List<String> codesWithoutDataValues = new ArrayList<String>();

        for ( SMSCode code : command.getCodes() )
        {

            DataElementCategoryOptionCombo optionCombo = dataElementCategoryService
                .getDataElementCategoryOptionCombo( code.getOptionId() );

            period = getPeriod( command, date );

            DataValue dv = dataValueService.getDataValue( orgunit, code.getDataElement(), period, optionCombo );

            if ( dv == null && !StringUtils.isEmpty( code.getCode() ) )
            {
                codesWithoutDataValues.add( code.getCode() );
            }
            else if ( dv != null )
            {
                codesWithDataValues.put( code.getCode(), dv );
            }
        }

        for ( String key : codesWithDataValues.keySet() )
        {
            DataValue dv = codesWithDataValues.get( key );
            String value = dv.getValue();
            if ( StringUtils.equals( dv.getDataElement().getType(), DataElement.VALUE_TYPE_BOOL ) )
            {
                if ( "true".equals( value ) )
                {
                    value = "Yes";
                }
                else if ( "false".equals( value ) )
                {
                    value = "No";
                }
            }
            reportBack += key + "=" + value + " ";
        }

        Collections.sort( codesWithoutDataValues );

        for ( String key : codesWithoutDataValues )
        {
            notInReport += key + ",";
        }
        notInReport = notInReport.substring( 0, notInReport.length() - 1 );

        if ( codesWithoutDataValues.size() > 0 )
        {
            sendSMS( reportBack + notInReport, sender );
        }
        else
        {
            sendSMS( reportBack, sender );
        }
    }

    protected Period getPeriod( SMSCommand command, Date date )
    {

        Period period;
        period = command.getDataset().getPeriodType().createPeriod();
        CalendarPeriodType cpt = (CalendarPeriodType) period.getPeriodType();
        if ( command.isCurrentPeriodUsedForReporting() )
        {
            period = cpt.createPeriod( new Date() );
        }
        else
        {
            period = cpt.getPreviousPeriod( period );
        }

        if ( date != null )
        {
            period = cpt.createPeriod( date );
        }

        return period;
    }

    private Date lookForDate( String message )
    {
        if ( !message.contains( " " ) )
        {
            return null;
        }
        Date date = null;
        String dateString = message.trim().split( " " )[0];
        SimpleDateFormat format = new SimpleDateFormat( "ddMM" );

        try
        {
            Calendar cal = Calendar.getInstance();
            date = format.parse( dateString );
            cal.setTime( date );
            int year = Calendar.getInstance().get( Calendar.YEAR );
            int month = Calendar.getInstance().get( Calendar.MONTH );
            if ( cal.get( Calendar.MONTH ) < month )
            {
                cal.set( Calendar.YEAR, year );
            }
            else
            {
                cal.set( Calendar.YEAR, year - 1 );
            }
            date = cal.getTime();
        }
        catch ( Exception e )
        {
            // no date found
        }
        return date;
    }

    private boolean storeDataValue( String sender, OrganisationUnit orgunit, Map<String, String> parsedMessage,
        SMSCode code, SMSCommand command, Date date, DataSet dataSet, boolean completeForm )
    {
        String upperCaseCode = code.getCode().toUpperCase();

        String storedBy = getUser( sender ).getUsername();

        if ( StringUtils.isBlank( storedBy ) )
        {
            storedBy = "[unknown] from [" + sender + "]";
        }

        DataElementCategoryOptionCombo optionCombo = dataElementCategoryService.getDataElementCategoryOptionCombo( code
            .getOptionId() );

        Period period = getPeriod( command, date );

        DataValue dv = dataValueService.getDataValue( orgunit, code.getDataElement(), period, optionCombo );

        String value = parsedMessage.get( upperCaseCode );
        if ( !StringUtils.isEmpty( value ) )
        {
            boolean newDataValue = false;
            if ( dv == null )
            {
                dv = new DataValue();
                dv.setOptionCombo( optionCombo );
                dv.setSource( orgunit );
                dv.setDataElement( code.getDataElement() );
                dv.setPeriod( period );
                dv.setComment( "" );
                newDataValue = true;
            }

            if ( StringUtils.equals( dv.getDataElement().getType(), DataElement.VALUE_TYPE_BOOL ) )
            {
                if ( "Y".equals( value.toUpperCase() ) || "YES".equals( value.toUpperCase() ) )
                {
                    value = "true";
                }
                else if ( "N".equals( value.toUpperCase() ) || "NO".equals( value.toUpperCase() ) )
                {
                    value = "false";
                }
            }
            else if ( StringUtils.equals( dv.getDataElement().getType(), DataElement.VALUE_TYPE_INT ) )
            {
                try
                {
                    Integer.parseInt( value );
                }
                catch ( NumberFormatException e )
                {
                    return false;
                }

            }

            dv.setValue( value );
            dv.setTimestamp( new java.util.Date() );
            dv.setStoredBy( storedBy );

            if ( newDataValue )
            {
                dataValueService.addDataValue( dv );
            }
            else
            {
                dataValueService.updateDataValue( dv );
            }
        }

        return true;
    }

    /* Checks if all defined data codes have values in the database */
    private void markCompleteDataSet( String sender, OrganisationUnit orgunit, Map<String, String> parsedMessage,
        SMSCommand command, Date date )
    {

        Period period = null;

        for ( SMSCode code : command.getCodes() )
        {

            DataElementCategoryOptionCombo optionCombo = dataElementCategoryService
                .getDataElementCategoryOptionCombo( code.getOptionId() );

            period = getPeriod( command, date );

            DataValue dv = dataValueService.getDataValue( orgunit, code.getDataElement(), period, optionCombo );

            if ( dv == null && !StringUtils.isEmpty( code.getCode() ) )
            {
                return; // not marked as complete
            }
        }

        String storedBy = getUser( sender ).getUsername();

        if ( StringUtils.isBlank( storedBy ) )
        {
            storedBy = "[unknown] from [" + sender + "]";
        }

        // if new values are submitted re-register as complete
        deregisterCompleteDataSet( command.getDataset(), period, orgunit );
        registerCompleteDataSet( command.getDataset(), period, orgunit, storedBy );

    }

    private boolean formIsComplete( SMSCommand command, Map<String, String> parsedMessage )
    {
        for ( SMSCode code : command.getCodes() )
        {
            if ( !parsedMessage.containsKey( code.getCode().toUpperCase() ) )
            {
                return false;
            }
        }
        return true;
    }

    private void registerCompleteDataSet( DataSet dataSet, Period period, OrganisationUnit organisationUnit,
        String storedBy )
    {
        CompleteDataSetRegistration registration = new CompleteDataSetRegistration();

        if ( registrationService.getCompleteDataSetRegistration( dataSet, period, organisationUnit ) == null )
        {
            registration.setDataSet( dataSet );
            registration.setPeriod( period );
            registration.setSource( organisationUnit );
            registration.setDate( new Date() );
            registration.setStoredBy( storedBy );

            registration.setPeriodName( registration.getPeriod().toString() );

            registrationService.saveCompleteDataSetRegistration( registration, true );

            log.info( "DataSet registered as complete: " + registration );
        }
    }

    private void deregisterCompleteDataSet( DataSet dataSet, Period period, OrganisationUnit organisationUnit )
    {
        CompleteDataSetRegistration registration = registrationService.getCompleteDataSetRegistration( dataSet, period,
            organisationUnit );

        if ( registration != null )
        {
            registrationService.deleteCompleteDataSetRegistration( registration );

            log.info( "DataSet un-registered as complete: " + registration );
        }
    }

    private User getUser( String sender )
    {
        OrganisationUnit orgunit = null;
        User user = null;
        for ( User u : userService.getUsersByPhoneNumber( sender ) )
        {
            OrganisationUnit ou = u.getOrganisationUnit();

            // Might be undefined if the user has more than one org.units
            // "attached"
            if ( orgunit == null )
            {
                orgunit = ou;
            }
            else if ( orgunit.getId() == ou.getId() )
            {
                // same orgunit, no problem...
            }
            else
            {
                throw new SMSParserException(
                    "User is associated with more than one orgunit. Please contact your supervisor." );
            }
            user = u;
        }
        return user;
    }

    // Run the J2ME parser for mobile

    private void runJ2MEParser( String sender, String message, Collection<OrganisationUnit> orgUnits, SMSCommand command )
    {
        J2MEDataEntryParser j2meParser = new J2MEDataEntryParser();
        j2meParser.setSmsCommand( command );
        message = message.trim();

        if ( !StringUtils.isBlank( command.getSeparator() ) )
        {
            j2meParser.setSeparator( command.getSeparator() );
        }
        String token[] = message.split( "!" );
        Period period = getPeriod( token[0].trim(), command.getDataset().getPeriodType() );
        Map<String, String> parsedMessage = j2meParser.parse( token[1] );
        OrganisationUnit orgUnit = selectOrganisationUnit( orgUnits, parsedMessage );

        boolean valueStored = false;
        for ( SMSCode code : command.getCodes() )
        {
            if ( parsedMessage.containsKey( code.getCode().toUpperCase() ) )
            {
                storeDataValue( sender, orgUnit, parsedMessage, code, command, period, command.getDataset(),
                    formIsComplete( command, parsedMessage ) );
                valueStored = true;
            }
        }

        if ( parsedMessage.isEmpty() || !valueStored )
        {
            if ( StringUtils.isEmpty( command.getDefaultMessage() ) )
            {
                throw new SMSParserException( "No values reported for command '" + command.getName() + "'" );
            }
            else
            {
                throw new SMSParserException( command.getDefaultMessage() );
            }
        }

        registerCompleteDataSet( command.getDataset(), period, orgUnit, "mobile" );

        sendSuccessFeedback( sender, command, parsedMessage, period, orgUnit );

    }

    private void sendSuccessFeedback( String sender, SMSCommand command, Map<String, String> parsedMessage,
        Period period, OrganisationUnit orgUnit )
    {
        String reportBack = "Thank you! Values entered: ";
        String notInReport = "Missing values for: ";
        boolean missingElements = false;

        for ( SMSCode code : command.getCodes() )
        {

            DataElementCategoryOptionCombo optionCombo = dataElementCategoryService
                .getDataElementCategoryOptionCombo( code.getOptionId() );

            DataValue dv = dataValueService.getDataValue( orgUnit, code.getDataElement(), period, optionCombo );

            if ( dv == null && !StringUtils.isEmpty( code.getCode() ) )
            {
                notInReport += code.getCode() + ",";
                missingElements = true;
            }
            else if ( dv != null )
            {
                String value = dv.getValue();
                if ( StringUtils.equals( dv.getDataElement().getType(), DataElement.VALUE_TYPE_BOOL ) )
                {
                    if ( "true".equals( value ) )
                    {
                        value = "Yes";
                    }
                    else if ( "false".equals( value ) )
                    {
                        value = "No";
                    }
                }
                reportBack += code.getCode() + "=" + value + " ";
            }
        }

        notInReport = notInReport.substring( 0, notInReport.length() - 1 );

        if ( missingElements )
        {
            sendSMS( reportBack + notInReport, sender );
        }
        else
        {
            sendSMS( reportBack, sender );
        }

    }

    private void storeDataValue( String sender, OrganisationUnit orgUnit, Map<String, String> parsedMessage,
        SMSCode code, SMSCommand command, Period period, DataSet dataset, boolean formIsComplete )
    {
        String upperCaseCode = code.getCode().toUpperCase();

        String storedBy = getUser( sender ).getUsername();

        if ( StringUtils.isBlank( storedBy ) )
        {
            storedBy = "[unknown] from [" + sender + "]";
        }

        DataElementCategoryOptionCombo optionCombo = dataElementCategoryService.getDataElementCategoryOptionCombo( code
            .getOptionId() );

        DataValue dv = dataValueService.getDataValue( orgUnit, code.getDataElement(), period, optionCombo );

        String value = parsedMessage.get( upperCaseCode );
        if ( !StringUtils.isEmpty( value ) )
        {
            boolean newDataValue = false;
            if ( dv == null )
            {
                dv = new DataValue();
                dv.setOptionCombo( optionCombo );
                dv.setSource( orgUnit );
                dv.setDataElement( code.getDataElement() );
                dv.setPeriod( period );
                dv.setComment( "" );
                newDataValue = true;
            }

            if ( StringUtils.equals( dv.getDataElement().getType(), DataElement.VALUE_TYPE_BOOL ) )
            {
                if ( "Y".equals( value.toUpperCase() ) || "YES".equals( value.toUpperCase() ) )
                {
                    value = "true";
                }
                else if ( "N".equals( value.toUpperCase() ) || "NO".equals( value.toUpperCase() ) )
                {
                    value = "false";
                }
            }

            dv.setValue( value );
            dv.setTimestamp( new java.util.Date() );
            dv.setStoredBy( storedBy );

            if ( ValidationUtils.dataValueIsValid( value, dv.getDataElement() ) != null )
            {
                return; // not a valid value for data element
            }

            if ( newDataValue )
            {
                dataValueService.addDataValue( dv );
            }
            else
            {
                dataValueService.updateDataValue( dv );
            }
        }

    }

    public static Period getPeriod( String periodName, PeriodType periodType )
        throws IllegalArgumentException
    {

        if ( periodType instanceof DailyPeriodType )
        {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat formatter = new SimpleDateFormat( pattern );
            Date date;
            try
            {
                date = formatter.parse( periodName );
            }
            catch ( ParseException e )
            {
                throw new IllegalArgumentException( "Couldn't make a period of type " + periodType.getName()
                    + " and name " + periodName, e );
            }
            return periodType.createPeriod( date );

        }

        if ( periodType instanceof WeeklyPeriodType )
        {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat formatter = new SimpleDateFormat( pattern );
            Date date;
            try
            {
                date = formatter.parse( periodName );
            }
            catch ( ParseException e )
            {
                throw new IllegalArgumentException( "Couldn't make a period of type " + periodType.getName()
                    + " and name " + periodName, e );
            }
            return periodType.createPeriod( date );
        }

        if ( periodType instanceof MonthlyPeriodType )
        {
            int dashIndex = periodName.indexOf( '-' );

            if ( dashIndex < 0 )
            {
                return null;
            }

            int month = Integer.parseInt( periodName.substring( 0, dashIndex ) );
            int year = Integer.parseInt( periodName.substring( dashIndex + 1, periodName.length() ) );

            Calendar cal = Calendar.getInstance();
            cal.set( Calendar.YEAR, year );
            cal.set( Calendar.MONTH, month );

            return periodType.createPeriod( cal.getTime() );
        }

        if ( periodType instanceof YearlyPeriodType )
        {
            Calendar cal = Calendar.getInstance();
            cal.set( Calendar.YEAR, Integer.parseInt( periodName ) );

            return periodType.createPeriod( cal.getTime() );
        }

        if ( periodType instanceof QuarterlyPeriodType )
        {
            Calendar cal = Calendar.getInstance();

            int month = 0;
            if ( periodName.substring( 0, periodName.indexOf( " " ) ).equals( "Jan" ) )
            {
                month = 1;
            }
            else if ( periodName.substring( 0, periodName.indexOf( " " ) ).equals( "Apr" ) )
            {
                month = 4;
            }
            else if ( periodName.substring( 0, periodName.indexOf( " " ) ).equals( "Jul" ) )
            {
                month = 6;
            }
            else if ( periodName.substring( 0, periodName.indexOf( " " ) ).equals( "Oct" ) )
            {
                month = 10;
            }

            int year = Integer.parseInt( periodName.substring( periodName.lastIndexOf( " " ) + 1 ) );

            cal.set( Calendar.MONTH, month );
            cal.set( Calendar.YEAR, year );

            if ( month != 0 )
            {
                return periodType.createPeriod( cal.getTime() );
            }

        }

        throw new IllegalArgumentException( "Couldn't make a period of type " + periodType.getName() + " and name "
            + periodName );
    }

    public void setDataElementCategoryService( DataElementCategoryService dataElementCategoryService )
    {
        this.dataElementCategoryService = dataElementCategoryService;
    }

    @Required
    public void setSmsCommandService( SMSCommandService smsCommandService )
    {
        this.smsCommandService = smsCommandService;
    }

    @Required
    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    @Required
    public void setUserService( UserService userService )
    {
        this.userService = userService;
    }

    public void setOutboundSmsService( OutboundSmsService outboundSmsService )
    {
        this.outboundSmsService = outboundSmsService;
    }

    public void setRegistrationService( CompleteDataSetRegistrationService registrationService )
    {
        this.registrationService = registrationService;
    }
}
