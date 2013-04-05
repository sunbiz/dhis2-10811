package org.hisp.dhis.api.mobile.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import org.hisp.dhis.api.mobile.model.PatientIdentifier;
import org.hisp.dhis.api.mobile.model.SMSCode;
import org.hisp.dhis.api.mobile.model.SMSCommand;
import org.hisp.dhis.api.mobile.model.LWUITmodel.Patient;
import org.hisp.dhis.api.mobile.model.LWUITmodel.PatientIdentifierAndAttribute;
import org.hisp.dhis.api.mobile.model.LWUITmodel.Program;
import org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStage;
import org.hisp.dhis.api.mobile.model.LWUITmodel.Relationship;
import org.hisp.dhis.i18n.I18nService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientAttributeService;
import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.patient.PatientIdentifierTypeService;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.patientattributevalue.PatientAttributeValue;
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

    private static final String PATIENT_REGISTERED = "patient_registered";

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

    @Autowired
    private PatientIdentifierTypeService patientIdentifierTypeService;

    @Autowired
    private PatientAttributeService patientAttributeService;

    private Integer patientId;

    public Integer getPatientId()
    {
        return patientId;
    }

    public void setPatientId( Integer patientId )
    {
        this.patientId = patientId;
    }

    // For client version 2.8 and lower
    @RequestMapping( method = RequestMethod.GET, value = "orgUnits/{id}/all" )
    @ResponseBody
    public MobileModel getAllDataForOrgUnit2_8( @PathVariable
    int id, @RequestHeader( "accept-language" )
    String locale )
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
    public DataSetList checkUpdatedDataSet2_8( @PathVariable
    int id, @RequestBody
    DataSetList dataSetList, @RequestHeader( "accept-language" )
    String locale )
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
    public String saveDataSetValues2_8( @PathVariable
    int id, @RequestBody
    DataSetValue dataSetValue )
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
    public String saveActivityReport2_8( @PathVariable
    int id, @RequestBody
    ActivityValue activityValue )
        throws NotAllowedException
    {
        // FIXME set the last argument to 0 to fix compilation error
        activityReportingService.saveActivityReport( getUnit( id ), activityValue, 0 );
        return ACTIVITY_REPORT_UPLOADED;
    }

    @RequestMapping( method = RequestMethod.POST, value = "orgUnits/{id}/activitiyplan" )
    @ResponseBody
    public MobileModel updatePrograms2_8( @PathVariable
    int id, @RequestHeader( "accept-language" )
    String locale, @RequestBody
    ModelList programsFromClient )
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
    public ActivityPlan search2_8( @PathVariable
    int id, @RequestHeader( "identifier" )
    String identifier )
        throws NotAllowedException
    {
        ActivityPlan activityPlan = activityReportingService.getActivitiesByIdentifier( identifier );
        activityPlan.setClientVersion( DataStreamSerializable.TWO_POINT_EIGHT );
        return activityPlan;
    }

    @RequestMapping( method = RequestMethod.GET, value = "orgUnits/{id}/changeLanguageDataSet" )
    @ResponseBody
    public DataSetList changeLanguageDataSet2_8( @PathVariable
    int id, @RequestHeader( "accept-language" )
    String locale )
    {
        return facilityReportingService.getDataSetsForLocale( getUnit( id ), locale );
    }

    // For client version 2.9 and higher

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/orgUnits/{id}/all" )
    @ResponseBody
    public MobileModel getAllDataForOrgUnit( @PathVariable
    String clientVersion, @PathVariable
    int id, @RequestHeader( "accept-language" )
    String locale )
    {
        MobileModel mobileModel = new MobileModel();
        mobileModel.setClientVersion( clientVersion );
        OrganisationUnit unit = getUnit( id );
        mobileModel.setActivityPlan( activityReportingService.getCurrentActivityPlan( unit, locale ) );
        mobileModel.setPrograms( programService.getPrograms( unit, locale ) );
        mobileModel.setDatasets( facilityReportingService.getMobileDataSetsForUnit( unit, locale ) );
        mobileModel.setServerCurrentDate( new Date() );
        mobileModel.setLocales( getLocalStrings( i18nService.getAvailableLocales() ) );
        mobileModel.setSmsCommands( this.getMobileSMSCommands( smsCommandService.getJ2MESMSCommands() ) );
        return mobileModel;
    }

    @RequestMapping( method = RequestMethod.POST, value = "{clientVersion}/orgUnits/{id}/updateDataSets" )
    @ResponseBody
    public DataSetList checkUpdatedDataSet( @PathVariable
    String clientVersion, @PathVariable
    int id, @RequestBody
    DataSetList dataSetList, @RequestHeader( "accept-language" )
    String locale )
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
    public String saveDataSetValues( @PathVariable
    int id, @RequestBody
    DataSetValue dataSetValue )
        throws NotAllowedException
    {
        facilityReportingService.saveDataSetValues( getUnit( id ), dataSetValue );
        return DATASET_REPORT_UPLOADED;
    }

    @RequestMapping( method = RequestMethod.POST, value = "{clientVersion}/orgUnits/{id}/activitiyplan" )
    @ResponseBody
    public MobileModel updatePrograms( @PathVariable
    String clientVersion, @PathVariable
    int id, @RequestHeader( "accept-language" )
    String locale, @RequestBody
    ModelList programsFromClient )
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
    public ActivityPlan search( @PathVariable
    String clientVersion, @PathVariable
    int id, @RequestHeader( "identifier" )
    String identifier )
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
    public String saveActivityReport( @PathVariable
    int id, @RequestBody
    ActivityValue activityValue )
        throws NotAllowedException
    {
        // FIXME set the last argument to 0 to fix compilation error
        activityReportingService.saveActivityReport( getUnit( id ), activityValue, 0 );
        return ACTIVITY_REPORT_UPLOADED;
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/orgUnits/{id}/changeLanguageDataSet" )
    @ResponseBody
    public DataSetList changeLanguageDataSet( @PathVariable
    int id, @RequestHeader( "accept-language" )
    String locale )
    {
        return facilityReportingService.getDataSetsForLocale( getUnit( id ), locale );
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/orgUnits/{id}/updateContactForMobile" )
    @ResponseBody
    public Contact updateContactForMobile()
    {
        return facilityReportingService.updateContactForMobile();
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/LWUIT/orgUnits/{id}/updateContactForMobile" )
    @ResponseBody
    public Contact updateContactForMobileLWUIT()
    {
        return facilityReportingService.updateContactForMobile();
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/LWUIT/orgUnits/{id}/findPatient" )
    @ResponseBody
    public Patient findPatientByName( @PathVariable
    int id, @RequestHeader( "name" )
    String keyword )
        throws NotAllowedException
    {
        return activityReportingService.findPatient( keyword, id );
    }

    @RequestMapping( method = RequestMethod.POST, value = "{clientVersion}/LWUIT/orgUnits/{id}/uploadProgramStage" )
    @ResponseBody
    public String saveProgramStage( @PathVariable
    int id, @RequestBody
    ProgramStage programStage )
        throws NotAllowedException
    {
        return activityReportingService.saveProgramStage( programStage, id );
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/LWUIT/orgUnits/{id}/enrollProgram" )
    @ResponseBody
    public Patient enrollProgram( @PathVariable
    int id, @RequestHeader( "enrollInfo" )
    String enrollInfo )
        throws NotAllowedException
    {
        return activityReportingService.enrollProgram( enrollInfo, id );
    }

    @RequestMapping( method = RequestMethod.POST, value = "{clientVersion}/LWUIT/orgUnits/{id}/addRelationship" )
    @ResponseBody
    public Patient addRelationship( @PathVariable
    int id, @RequestBody
    Relationship enrollmentRelationship )
        throws NotAllowedException
    {
        return activityReportingService.addRelationship( enrollmentRelationship, id );
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/LWUIT/orgUnits/{id}/downloadAnonymousProgramUrl" )
    @ResponseBody
    public Program getAnonymousProgram( @PathVariable
    int id, @RequestHeader( "useless-part" )
    String enrollInfo )
        throws NotAllowedException
    {
        return activityReportingService.getAllAnonymousProgram( id );
    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/LWUIT/orgUnits/{id}/findProgram" )
    @ResponseBody
    public Program findProgram( @PathVariable
    int id, @RequestHeader( "info" )
    String programInfo )
        throws NotAllowedException
    {
        return activityReportingService.findProgram( programInfo );
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

            mobileSMSCommand.setName( normalSMSCommand.getName() );
            mobileSMSCommand.setCodeSeparator( normalSMSCommand.getCodeSeparator() );
            mobileSMSCommand.setDataSetId( normalSMSCommand.getDataset().getId() );
            mobileSMSCommand.setSeparator( normalSMSCommand.getSeparator() );

            for ( org.hisp.dhis.smscommand.SMSCode normalSMSCode : normalSMSCommand.getCodes() )
            {
                SMSCode smsCode = new SMSCode();

                smsCode.setCode( normalSMSCode.getCode() );
                smsCode.setDataElementId( normalSMSCode.getDataElement().getId() );
                smsCode.setOptionId( normalSMSCode.getOptionId() );
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

    @RequestMapping( method = RequestMethod.POST, value = "{clientVersion}/LWUIT/orgUnits/{id}/registerPerson" )
    @ResponseBody
    public String savePatient( @PathVariable
    int id, @RequestBody
    Patient patient )
    {

        org.hisp.dhis.patient.Patient patientWeb = new org.hisp.dhis.patient.Patient();

        int startIndex = patient.getFirstName().indexOf( ' ' );
        int endIndex = patient.getFirstName().lastIndexOf( ' ' );

        String firstName = patient.getFirstName().toString();
        String middleName = "";
        String lastName = "";

        if ( patient.getFirstName().indexOf( ' ' ) != -1 )
        {
            firstName = patient.getFirstName().substring( 0, startIndex );
            if ( startIndex == endIndex )
            {
                middleName = "";
                lastName = patient.getFirstName().substring( startIndex + 1, patient.getFirstName().length() );
            }
            else
            {
                middleName = patient.getFirstName().substring( startIndex + 1, endIndex );
                lastName = patient.getFirstName().substring( endIndex + 1, patient.getFirstName().length() );
            }
        }

        patientWeb.setFirstName( firstName );
        patientWeb.setMiddleName( middleName );
        patientWeb.setLastName( lastName );
        patientWeb.setGender( patient.getGender() );
        patientWeb.setDobType( patient.getDobType() );
        patientWeb.setPhoneNumber( patient.getPhoneNumber() );
        patientWeb.setBirthDate( patient.getBirthDate() );
        patientWeb.setOrganisationUnit( organisationUnitService.getOrganisationUnit( id ) );
        patientWeb.setRegistrationDate( new Date() );

        Set<org.hisp.dhis.patient.PatientIdentifier> patientIdentifierSet = new HashSet<org.hisp.dhis.patient.PatientIdentifier>();
        Set<PatientAttribute> patientAttributeSet = new HashSet<PatientAttribute>();
        List<PatientAttributeValue> patientAttributeValues = new ArrayList<PatientAttributeValue>();

        Collection<PatientIdentifier> identifiers = patient.getIdentifiers();

        Collection<org.hisp.dhis.api.mobile.model.PatientAttribute> patientAttributesMobile = patient
            .getPatientAttValues();
        for ( PatientIdentifier identifier : identifiers )
        {
            PatientIdentifierType patientIdentifierType = patientIdentifierTypeService
                .getPatientIdentifierType( identifier.getIdentifierType() );

            org.hisp.dhis.patient.PatientIdentifier patientIdentifier = new org.hisp.dhis.patient.PatientIdentifier();
            patientIdentifier.setIdentifierType( patientIdentifierType );
            patientIdentifier.setPatient( patientWeb );
            patientIdentifier.setIdentifier( identifier.getIdentifier() );
            patientIdentifierSet.add( patientIdentifier );
        }

        for ( org.hisp.dhis.api.mobile.model.PatientAttribute paAtt : patientAttributesMobile )
        {

            PatientAttribute patientAttribute = patientAttributeService.getPatientAttributeByName( paAtt.getName() );

            patientAttributeSet.add( patientAttribute );

            PatientAttributeValue patientAttributeValue = new PatientAttributeValue();

            patientAttributeValue.setPatient( patientWeb );
            patientAttributeValue.setPatientAttribute( patientAttribute );
            patientAttributeValue.setValue( paAtt.getValue() );

            patientAttributeValues.add( patientAttributeValue );

        }

        patientWeb.setIdentifiers( patientIdentifierSet );
        patientWeb.setAttributes( patientAttributeSet );

        patientId = patientService.savePatient( patientWeb );

        return PATIENT_REGISTERED;

    }

    @RequestMapping( method = RequestMethod.GET, value = "{clientVersion}/LWUIT/orgUnits/{id}/getVariesInfo" )
    @ResponseBody
    public PatientIdentifierAndAttribute getVariesInfo( @PathVariable
    String clientVersion, @PathVariable
    int id, @RequestHeader( "accept-language" )
    String locale )
    {
        PatientIdentifierAndAttribute patientIdentifierAndAttribute = new PatientIdentifierAndAttribute();
        patientIdentifierAndAttribute.setClientVersion( clientVersion );
        patientIdentifierAndAttribute.setPatientIdentifiers( activityReportingService.getIdentifiersForMobile() );
        patientIdentifierAndAttribute.setPatientAttributes( activityReportingService.getPatientAttributesForMobile() );

        return patientIdentifierAndAttribute;

    }
}
