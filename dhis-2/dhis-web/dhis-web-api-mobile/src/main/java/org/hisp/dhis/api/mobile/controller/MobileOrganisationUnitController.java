package org.hisp.dhis.api.mobile.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.hisp.dhis.api.mobile.ActivityReportingService;
import org.hisp.dhis.api.mobile.FacilityReportingService;
import org.hisp.dhis.api.mobile.IProgramService;
import org.hisp.dhis.api.mobile.NotAllowedException;
import org.hisp.dhis.api.mobile.model.ActivityPlan;
import org.hisp.dhis.api.mobile.model.ActivityValue;
import org.hisp.dhis.api.mobile.model.Contact;
import org.hisp.dhis.api.mobile.model.DataSetList;
import org.hisp.dhis.api.mobile.model.DataSetValue;
import org.hisp.dhis.api.mobile.model.DataStreamSerializable;
import org.hisp.dhis.api.mobile.model.MobileModel;
import org.hisp.dhis.api.mobile.model.ModelList;
import org.hisp.dhis.api.mobile.model.LWUITmodel.Patient;
import org.hisp.dhis.api.mobile.model.SMSCode;
import org.hisp.dhis.api.mobile.model.SMSCommand;
import org.hisp.dhis.i18n.I18nService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.smscommand.SMSCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( value = "/mobile" )
public class MobileOrganisationUnitController
    extends AbstractMobileController
{
    private static final String ACTIVITY_REPORT_UPLOADED = "activity_report_uploaded";

    private static final String DATASET_REPORT_UPLOADED = "dataset_report_uploaded";

    @Autowired
    private ActivityReportingService activityReportingService;

    @Autowired
    private IProgramService programService;

    @Autowired
    private FacilityReportingService facilityReportingService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private I18nService i18nService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private SMSCommandService smsCommandService;

    // For client version 2.8 and lower
    @RequestMapping( method = RequestMethod.GET, value = "orgUnits/{id}/all" )
    @ResponseBody
    public MobileModel getAllDataForOrgUnit2_8( @PathVariable int id, @RequestHeader( "accept-language" ) String locale )
    {
        MobileModel mobileModel = new MobileModel();
        mobileModel.setClientVersion( DataStreamSerializable.TWO_POINT_EIGHT );
        OrganisationUnit unit = getUnit( id );
        mobileModel.setActivityPlan( activityReportingService.getCurrentActivityPlan( unit, locale ) );
        mobileModel.setPrograms( programService.getPrograms( unit, locale ) );
        mobileModel.setDatasets( facilityReportingService.getMobileDataSetsForUnit( unit, locale ) );
        mobileModel.setServerCurrentDate( new Date() );
        mobileModel.setLocales( getLocalStrings( i18nService.getAvailableLocales() ) );
        return mobileModel;
    }

    @RequestMapping( method = RequestMethod.POST, value = "orgUnits/{id}/updateDataSets" )
    @ResponseBody
    public DataSetList checkUpdatedDataSet2_8( @PathVariable int id, @RequestBody DataSetList dataSetList,
        @RequestHeader( "accept-language" ) String locale )
    {
        DataSetList returnList = facilityReportingService.getUpdatedDataSet( dataSetList, getUnit( id ), locale );
        returnList.setClientVersion( DataStreamSerializable.TWO_POINT_EIGHT );
        return returnList;
    }

    /**
     * Save a facility report for unit
     * 
     * @param dataSetValue - the report to save
     * @throws NotAllowedException if the {@link DataSetValue} is invalid
     */
    @RequestMapping( method = RequestMethod.POST, value = "orgUnits/{id}/dataSets" )
    @ResponseBody
    public String saveDataSetValues2_8( @PathVariable int id, @RequestBody DataSetValue dataSetValue )
        throws NotAllowedException
    {
        facilityReportingService.saveDataSetValues( getUnit( id ), dataSetValue );
        return DATASET_REPORT_UPLOADED;
    }

    /**
     * Save activity report for unit
     * 
     * @param activityValue - the report to save
     * @throws NotAllowedException if the {@link ActivityValue activity value}
     *         is invalid
     */
    @RequestMapping( method = RequestMethod.POST, value = "orgUnits/{id}/activities" )
    @ResponseBody
    public String saveActivityReport2_8( @PathVariable int id, @RequestBody ActivityValue activityValue )
        throws NotAllowedException
    {
        // FIXME set the last argument to 0 to fix compilation error
        activityReportingService.saveActivityReport( getUnit( id ), activityValue, 0 );
        return ACTIVITY_REPORT_UPLOADED;
    }

    @RequestMapping( method = RequestMethod.POST, value = "orgUnits/{id}/activitiyplan" )
    @ResponseBody
    public MobileModel updatePrograms2_8( @PathVariable int id, @RequestHeader( "accept-language" ) String locale,
        @RequestBody ModelList programsFromClient )
    {
        MobileModel model = new MobileModel();
        model.setClientVersion( DataStreamSerializable.TWO_POINT_EIGHT );
        model.setPrograms( programService.updateProgram( programsFromClient, locale, getUnit( id ) ) );
        model.setActivityPlan( activityReportingService.getCurrentActivityPlan( getUnit( id ), locale ) );
        model.setServerCurrentDate( new Date() );
        return model;
    }

    @RequestMapping( method = RequestMethod.GET, value = "orgUnits/{id}/search" )
    @ResponseBody
    public ActivityPlan search2_8( @PathVariable int id, @RequestHeader( "identifier" ) String identifier )
        throws NotAllowedException
    {
        ActivityPlan activityPlan = activityReportingService.getActivitiesByIdentifier( identifier );
        ;
        activityPlan.setClientVersion( DataStreamSerializable.TWO_POINT_EIGHT );
        return activityPlan;
    }

    @RequestMapping( method = RequestMethod.GET, value = "orgUnits/{id}/changeLanguageDataSet" )
    @ResponseBody
    public DataSetList changeLanguageDataSet2_8( @PathVariable int id, @RequestHeader( "accept-language" ) String locale )
    {
        return facilityReportingService.getDataSetsForLocale( getUnit( id ), locale );
    }

    // For client version 2.9 and higher

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/orgUnits/{id}/all" )
    @ResponseBody
    public MobileModel getAllDataForOrgUnit( @PathVariable String clientVersion, @PathVariable int id,
        @RequestHeader( "accept-language" ) String locale )
    {
        MobileModel mobileModel = new MobileModel();
        mobileModel.setClientVersion( clientVersion );
        OrganisationUnit unit = getUnit( id );
        mobileModel.setActivityPlan( activityReportingService.getCurrentActivityPlan( unit, locale ) );
        mobileModel.setPrograms( programService.getPrograms( unit, locale ) );
        mobileModel.setDatasets( facilityReportingService.getMobileDataSetsForUnit( unit, locale ) );
        mobileModel.setServerCurrentDate( new Date() );
        mobileModel.setLocales( getLocalStrings( i18nService.getAvailableLocales() ) );
        mobileModel.setSmsCommands( this.getMobileSMSCommands( smsCommandService.getSMSCommands() ) );
        return mobileModel;
    }

    @RequestMapping( method = RequestMethod.POST, value = "{clientVersion}/orgUnits/{id}/updateDataSets" )
    @ResponseBody
    public DataSetList checkUpdatedDataSet( @PathVariable String clientVersion, @PathVariable int id,
        @RequestBody DataSetList dataSetList, @RequestHeader( "accept-language" ) String locale )
    {
        DataSetList returnList = facilityReportingService.getUpdatedDataSet( dataSetList, getUnit( id ), locale );
        returnList.setClientVersion( clientVersion );
        return returnList;
    }

    /**
     * Save a facility report for unit
     * 
     * @param dataSetValue - the report to save
     * @throws NotAllowedException if the {@link DataSetValue} is invalid
     */

    @RequestMapping( method = RequestMethod.POST, value = "{clientVersion}/orgUnits/{id}/dataSets" )
    @ResponseBody
    public String saveDataSetValues( @PathVariable int id, @RequestBody DataSetValue dataSetValue )
        throws NotAllowedException
    {
        facilityReportingService.saveDataSetValues( getUnit( id ), dataSetValue );
        return DATASET_REPORT_UPLOADED;
    }

    @RequestMapping( method = RequestMethod.POST, value = "{clientVersion}/orgUnits/{id}/activitiyplan" )
    @ResponseBody
    public MobileModel updatePrograms( @PathVariable String clientVersion, @PathVariable int id,
        @RequestHeader( "accept-language" ) String locale, @RequestBody ModelList programsFromClient )
    {
        MobileModel model = new MobileModel();
        model.setClientVersion( clientVersion );
        model.setPrograms( programService.updateProgram( programsFromClient, locale, getUnit( id ) ) );
        model.setActivityPlan( activityReportingService.getCurrentActivityPlan( getUnit( id ), locale ) );
        model.setServerCurrentDate( new Date() );
        return model;
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/orgUnits/{id}/search" )
    @ResponseBody
    public ActivityPlan search( @PathVariable String clientVersion, @PathVariable int id,
        @RequestHeader( "identifier" ) String identifier )
        throws NotAllowedException
    {
        ActivityPlan activityPlan = activityReportingService.getActivitiesByIdentifier( identifier );
        activityPlan.setClientVersion( clientVersion );
        return activityPlan;
    }

    /**
     * Save a facility report for unit
     * 
     * @param dataSetValue - the report to save
     * @throws NotAllowedException if the {@link DataSetValue} is invalid
     */

    // @RequestMapping( method = RequestMethod.POST, value =
    // "{clientVersion}/orgUnits/{id}/dataSets" )
    // @ResponseBody
    // public String saveDataSetValues( @PathVariable int id, @RequestBody
    // DataSetValue dataSetValue )
    // throws NotAllowedException
    // {
    // facilityReportingService.saveDataSetValues( getUnit( id ), dataSetValue
    // );
    // return DATASET_REPORT_UPLOADED;
    // }

    /**
     * Save activity report for unit
     * 
     * @param activityValue - the report to save
     * @throws NotAllowedException if the {@link ActivityValue activity value}
     *         is invalid
     */
    @RequestMapping( method = RequestMethod.POST, value = "{clientVersion}/orgUnits/{id}/activities" )
    @ResponseBody
    public String saveActivityReport( @PathVariable int id, @RequestBody ActivityValue activityValue )
        throws NotAllowedException
    {
        // FIXME set the last argument to 0 to fix compilation error
        activityReportingService.saveActivityReport( getUnit( id ), activityValue, 0 );
        return ACTIVITY_REPORT_UPLOADED;
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/orgUnits/{id}/changeLanguageDataSet" )
    @ResponseBody
    public DataSetList changeLanguageDataSet( @PathVariable int id, @RequestHeader( "accept-language" ) String locale )
    {
        return facilityReportingService.getDataSetsForLocale( getUnit( id ), locale );
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/orgUnits/{id}/updateContactForMobile" )
    @ResponseBody
    public Contact updateContactForMobile()
    {
        return facilityReportingService.updateContactForMobile();
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/orgUnits/{id}/findPatient" )
    @ResponseBody
    public Patient findPatientByName( @PathVariable int id, @RequestHeader( "name" ) String fullName)
    throws NotAllowedException
    {
        return activityReportingService.findPatient( fullName );
    }

    // Supportive methods

    private Collection<String> getLocalStrings( Collection<Locale> locales )
    {
        if ( locales == null || locales.isEmpty() )
        {
            return null;
        }
        Collection<String> localeStrings = new ArrayList<String>();

        for ( Locale locale : locales )
        {
            localeStrings.add( locale.getLanguage() + "-" + locale.getCountry() );
        }
        return localeStrings;
    }

    private List<SMSCommand> getMobileSMSCommands( Collection<org.hisp.dhis.smscommand.SMSCommand> normalSMSCommands )
    {
        List<SMSCommand> smsCommands = new ArrayList<SMSCommand>();
        for ( org.hisp.dhis.smscommand.SMSCommand normalSMSCommand : normalSMSCommands )
        {
            SMSCommand mobileSMSCommand = new SMSCommand();
            List<SMSCode> smsCodes = new ArrayList<SMSCode>();
            mobileSMSCommand.setCodeSeparator( normalSMSCommand.getSeparator() );
            mobileSMSCommand.setDataSetId( normalSMSCommand.getDataset().getId() );
            mobileSMSCommand.setCodeSeparator( normalSMSCommand.getCodeSeparator() );
            for ( org.hisp.dhis.smscommand.SMSCode normalSMSCode : normalSMSCommand.getCodes() )
            {
                SMSCode smsCode = new SMSCode();
                smsCode.setCode( normalSMSCode.getCode() );
                smsCode.setDataElementId( normalSMSCode.getDataElement().getId() );
                smsCode.setOptionId( normalSMSCode.getId() );
                smsCodes.add( smsCode );
            }
            mobileSMSCommand.setSmsCodes( smsCodes );
            smsCommands.add( mobileSMSCommand );
        }
        return smsCommands;
    }

    private OrganisationUnit getUnit( int id )
    {
        return organisationUnitService.getOrganisationUnit( id );
    }
}
