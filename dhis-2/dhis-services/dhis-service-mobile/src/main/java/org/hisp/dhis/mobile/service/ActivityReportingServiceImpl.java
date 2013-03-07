package org.hisp.dhis.mobile.service;

/*
 * Copyright (c) 2010, University of Oslo
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.api.mobile.ActivityReportingService;
import org.hisp.dhis.api.mobile.NotAllowedException;
import org.hisp.dhis.api.mobile.PatientMobileSettingService;
import org.hisp.dhis.api.mobile.model.Activity;
import org.hisp.dhis.api.mobile.model.ActivityPlan;
import org.hisp.dhis.api.mobile.model.ActivityValue;
import org.hisp.dhis.api.mobile.model.Beneficiary;
import org.hisp.dhis.api.mobile.model.DataValue;
import org.hisp.dhis.api.mobile.model.PatientAttribute;
import org.hisp.dhis.api.mobile.model.Task;
import org.hisp.dhis.api.mobile.model.comparator.ActivityComparator;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAttributeService;
import org.hisp.dhis.patient.PatientIdentifier;
import org.hisp.dhis.patient.PatientIdentifierService;
import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.patient.PatientMobileSetting;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.patientattributevalue.PatientAttributeValue;
import org.hisp.dhis.patientattributevalue.PatientAttributeValueService;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.ProgramStageSection;
import org.hisp.dhis.program.ProgramStageSectionService;
import org.hisp.dhis.relationship.Relationship;
import org.hisp.dhis.relationship.RelationshipService;
import org.hisp.dhis.system.util.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Required;

public class ActivityReportingServiceImpl
    implements ActivityReportingService
{
    private static final String PROGRAM_STAGE_UPLOADED = "program_stage_uploaded";

    private static final String PROGRAM_STAGE_SECTION_UPLOADED = "program_stage_section_uploaded";

    private ActivityComparator activityComparator = new ActivityComparator();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramStageInstanceService programStageInstanceService;

    // private ActivityPlanService activityPlanService;

    private PatientService patientService;

    private PatientAttributeValueService patientAttValueService;

    private PatientAttributeService patientAttService;

    private PatientDataValueService dataValueService;

    private PatientMobileSettingService patientMobileSettingService;

    private PatientIdentifierService patientIdentifierService;

    private ProgramStageSectionService programStageSectionService;

    private ProgramInstanceService programInstanceService;

    private RelationshipService relationshipService;

    private DataElementService dataElementService;

    private PatientDataValueService patientDataValueService;

    private ProgramService programService;

    private OrganisationUnitService organisationUnitService;

    private org.hisp.dhis.mobile.service.ModelMapping modelMapping;

    // -------------------------------------------------------------------------
    // MobileDataSetService
    // -------------------------------------------------------------------------

    private PatientMobileSetting setting;

    private org.hisp.dhis.patient.PatientAttribute groupByAttribute;

    @Override
    public ActivityPlan getCurrentActivityPlan( OrganisationUnit unit, String localeString )
    {
        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.DATE, 30 );

        long upperBound = cal.getTime().getTime();

        cal.add( Calendar.DATE, -60 );
        long lowerBound = cal.getTime().getTime();

        List<Activity> items = new ArrayList<Activity>();
        Collection<Patient> patients = patientService.getPatients( unit, null, null );

        for ( Patient patient : patients )
        {
            for ( ProgramStageInstance programStageInstance : programStageInstanceService.getProgramStageInstances(
                patient, false ) )
            {
                if ( programStageInstance.getDueDate().getTime() >= lowerBound
                    && programStageInstance.getDueDate().getTime() <= upperBound )
                {
                    items.add( getActivity( programStageInstance, false ) );
                }
            }
        }

        this.setGroupByAttribute( patientAttService.getPatientAttributeByGroupBy( true ) );

        if ( items.isEmpty() )
        {
            return null;
        }

        Collections.sort( items, activityComparator );

        return new ActivityPlan( items );
    }

    @Override
    public ActivityPlan getAllActivityPlan( OrganisationUnit unit, String localeString )
    {

        List<Activity> items = new ArrayList<Activity>();
        Collection<Patient> patients = patientService.getPatients( unit, null, null );

        for ( Patient patient : patients )
        {
            for ( ProgramStageInstance programStageInstance : programStageInstanceService.getProgramStageInstances(
                patient, false ) )
            {
                items.add( getActivity( programStageInstance, false ) );
            }
        }

        this.setGroupByAttribute( patientAttService.getPatientAttributeByGroupBy( true ) );

        if ( items.isEmpty() )
        {
            return null;
        }

        Collections.sort( items, activityComparator );
        return new ActivityPlan( items );
    }

    @Override
    public ActivityPlan getActivitiesByIdentifier( String keyword )
        throws NotAllowedException
    {

        long time = PeriodType.createCalendarInstance().getTime().getTime();

        Calendar expiredDate = Calendar.getInstance();

        List<Activity> items = new ArrayList<Activity>();

        Collection<Patient> patients = patientIdentifierService.getPatientsByIdentifier( keyword, 0,
            patientIdentifierService.countGetPatientsByIdentifier( keyword ) );

        // Make sure user input full beneficiary identifier number

        if ( patients.size() > 1 )
        {
            throw NotAllowedException.NEED_MORE_SPECIFIC;
        }
        else if ( patients.size() == 0 )
        {
            throw NotAllowedException.NO_BENEFICIARY_FOUND;
        }
        else
        {
            if ( patients != null )
            {
                Iterator<Patient> iterator = patients.iterator();

                while ( iterator.hasNext() )
                {
                    Patient patient = iterator.next();

                    List<ProgramStageInstance> programStageInstances = programStageInstanceService
                        .getProgramStageInstances( patient, false );

                    for ( int i = 0; i < programStageInstances.size(); i++ )
                    {
                        ProgramStageInstance programStageInstance = programStageInstances.get( i );

                        // expiredDate.setTime( DateUtils.getDateAfterAddition(
                        // programStageInstance.getDueDate(), 0 ) );
                        expiredDate.setTime( DateUtils.getDateAfterAddition( programStageInstance.getDueDate(), 30 ) );

                        if ( programStageInstance.getDueDate().getTime() <= time
                            && expiredDate.getTimeInMillis() > time )
                        {
                            items.add( getActivity( programStageInstance,
                                programStageInstance.getDueDate().getTime() < time ) );
                        }
                    }
                }
            }
            return new ActivityPlan( items );
        }

    }

    // -------------------------------------------------------------------------
    // DataValueService
    // -------------------------------------------------------------------------

    @Override
    public void saveActivityReport( OrganisationUnit unit, ActivityValue activityValue, Integer programStageSectionId )
        throws NotAllowedException
    {

        ProgramStageInstance programStageInstance = programStageInstanceService.getProgramStageInstance( activityValue
            .getProgramInstanceId() );
        if ( programStageInstance == null )
        {
            throw NotAllowedException.INVALID_PROGRAM_STAGE;
        }

        programStageInstance.getProgramStage();
        Collection<org.hisp.dhis.dataelement.DataElement> dataElements = new ArrayList<org.hisp.dhis.dataelement.DataElement>();

        ProgramStageSection programStageSection = programStageSectionService
            .getProgramStageSection( programStageSectionId );

        if ( programStageSectionId != null && programStageSectionId != 0 )
        {
            for ( ProgramStageDataElement de : programStageSection.getProgramStageDataElements() )
            {
                dataElements.add( de.getDataElement() );
            }
        }
        else
        {
            for ( ProgramStageDataElement de : programStageInstance.getProgramStage().getProgramStageDataElements() )
            {
                dataElements.add( de.getDataElement() );
            }
        }

        programStageInstance.getProgramStage().getProgramStageDataElements();
        Collection<Integer> dataElementIds = new ArrayList<Integer>( activityValue.getDataValues().size() );

        for ( DataValue dv : activityValue.getDataValues() )
        {
            dataElementIds.add( dv.getId() );
        }

        if ( dataElements.size() != dataElementIds.size() )
        {
            ;
            throw NotAllowedException.INVALID_PROGRAM_STAGE;
        }

        Map<Integer, org.hisp.dhis.dataelement.DataElement> dataElementMap = new HashMap<Integer, org.hisp.dhis.dataelement.DataElement>();
        for ( org.hisp.dhis.dataelement.DataElement dataElement : dataElements )
        {
            if ( !dataElementIds.contains( dataElement.getId() ) )
            {
                throw NotAllowedException.INVALID_PROGRAM_STAGE;
            }
            dataElementMap.put( dataElement.getId(), dataElement );
        }

        // Set ProgramStageInstance to completed
        if ( programStageSectionId == 0 )
        {
            programStageInstance.setCompleted( true );
            programStageInstanceService.updateProgramStageInstance( programStageInstance );
        }

        // Everything is fine, hence save
        saveDataValues( activityValue, programStageInstance, dataElementMap );

    }

    @Override
    public org.hisp.dhis.api.mobile.model.LWUITmodel.Patient findPatient( String fullName, int orgUnitId )
        throws NotAllowedException
    {
        int startIndex = fullName.indexOf( ' ' );
        int endIndex = fullName.lastIndexOf( ' ' );

        String firstName = fullName.toString();
        String middleName = " ";
        String lastName = " ";

        if ( fullName.indexOf( ' ' ) != -1 )
        {
            firstName = fullName.substring( 0, startIndex );
            if ( startIndex == endIndex )
            {
                middleName = "  ";
                lastName = fullName.substring( startIndex + 1, fullName.length() );
            }
            else
            {
                middleName = " " + fullName.substring( startIndex + 1, endIndex ) + " ";
                lastName = fullName.substring( endIndex + 1, fullName.length() );
            }
        }
        List<Patient> patients = (List<Patient>) this.patientService.getPatientByFullname( firstName + middleName
            + lastName, orgUnitId );

        if ( patients.size() > 1 )
        {
            throw NotAllowedException.NEED_MORE_SPECIFIC;
        }
        else if ( patients.size() == 0 )
        {
            throw NotAllowedException.NO_BENEFICIARY_FOUND;
        }
        else
        {
            org.hisp.dhis.api.mobile.model.LWUITmodel.Patient patient = getPatientModel( orgUnitId, patients.get( 0 ) );

            return patient;
        }
    }

    @Override
    public String saveProgramStage( org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStage programStage )
        throws NotAllowedException
    {
        ProgramStageInstance programStageInstance = programStageInstanceService.getProgramStageInstance( programStage
            .getId() );

        List<org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStageDataElement> dataElements = programStage
            .getDataElements();

        for ( int i = 0; i < dataElements.size(); i++ )
        {
            DataElement dataElement = dataElementService.getDataElement( dataElements.get( i ).getId() );

            PatientDataValue patientDataValue = new PatientDataValue( programStageInstance, dataElement, new Date(),
                dataElements.get( i ).getValue() );

            PatientDataValue previousPatientDataValue = patientDataValueService.getPatientDataValue(
                patientDataValue.getProgramStageInstance(), patientDataValue.getDataElement() );

            if ( previousPatientDataValue == null )
            {
                patientDataValueService.savePatientDataValue( patientDataValue );
            }
            else
            {
                previousPatientDataValue.setValue( patientDataValue.getValue() );
                previousPatientDataValue.setTimestamp( new Date() );
                previousPatientDataValue.setProvidedElsewhere( patientDataValue.getProvidedElsewhere() );
                patientDataValueService.updatePatientDataValue( previousPatientDataValue );
            }

        }

        programStageInstance.setExecutionDate( new Date() );

        if ( programStageInstance.getProgramStage().getProgramStageDataElements().size() > dataElements.size() )
        {
            programStageInstanceService.updateProgramStageInstance( programStageInstance );
            return PROGRAM_STAGE_SECTION_UPLOADED;
        }
        else
        {
            programStageInstance.setCompleted( true );

            // check if any compulsory value is null
            for ( int i = 0; i < dataElements.size(); i++ )
            {
                if ( dataElements.get( i ).isCompulsory() == true )
                {
                    if ( dataElements.get( i ).getValue().equals( "" ) )
                    {
                        programStageInstance.setCompleted( false );
                        // break;
                        throw NotAllowedException.INVALID_PROGRAM_STAGE;
                    }
                }
            }
            programStageInstanceService.updateProgramStageInstance( programStageInstance );
            
            // check if all belonged program stage are completed
            if ( isAllProgramStageFinished( programStageInstance ) == true )
            {
                ProgramInstance programInstance = programStageInstance.getProgramInstance();
                programInstance.setCompleted( true );
                programInstanceService.updateProgramInstance( programInstance );
            }
            
            return PROGRAM_STAGE_UPLOADED;
        }
    }
    
    private boolean isAllProgramStageFinished( ProgramStageInstance programStageInstance )
    {
        ProgramInstance programInstance = programStageInstance.getProgramInstance();
        Collection<ProgramStageInstance> programStageInstances =  programInstance.getProgramStageInstances();
        if ( programStageInstances != null )
        {
            Iterator<ProgramStageInstance> iterator = programStageInstances.iterator();

            while ( iterator.hasNext() )
            {
                ProgramStageInstance each = iterator.next();
                if( !each.isCompleted() )
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public org.hisp.dhis.api.mobile.model.LWUITmodel.Patient enrollProgram( String enrollInfo, int orgUnitId )
        throws NotAllowedException
    {
        String[] enrollProgramInfos = enrollInfo.split( "-" );
        int patientId = Integer.parseInt( enrollProgramInfos[0] );
        int programId = Integer.parseInt( enrollProgramInfos[1] );

        Patient patient = patientService.getPatient( patientId );
        Program program = programService.getProgram( programId );

        ProgramInstance programInstance = new ProgramInstance();
        // programInstance.setEnrollmentDate( sdf.parseDateTime( enrollmentDate
        // ).toDate() );
        // programInstance.setDateOfIncident( sdf.parseDateTime( incidentDate
        // ).toDate() );
        programInstance.setEnrollmentDate( new Date() );
        programInstance.setDateOfIncident( new Date() );
        programInstance.setProgram( program );
        programInstance.setPatient( patient );
        programInstance.setCompleted( false );
        programInstanceService.addProgramInstance( programInstance );
        patient.getPrograms().add( program );
        patientService.updatePatient( patient );
        for ( ProgramStage programStage : program.getProgramStages() )
        {
            if ( programStage.getAutoGenerateEvent() )
            {
                ProgramStageInstance programStageInstance = new ProgramStageInstance();
                programStageInstance.setProgramInstance( programInstance );
                programStageInstance.setProgramStage( programStage );

                // Date dateCreatedEvent = sdf.parseDateTime( incidentDate
                // ).toDate();
                Date dateCreatedEvent = new Date();
                if ( program.getGeneratedByEnrollmentDate() )
                {
                    // dateCreatedEvent = sdf.parseDateTime( enrollmentDate
                    // ).toDate();
                }
                Date dueDate = DateUtils.getDateAfterAddition( dateCreatedEvent, programStage.getMinDaysFromStart() );

                programStageInstance.setDueDate( dueDate );

                if ( program.isSingleEvent() )
                {
                    programStageInstance.setExecutionDate( dueDate );
                }

                programStageInstanceService.addProgramStageInstance( programStageInstance );

            }

        }

        return getPatientModel( orgUnitId, patient );
    }

    // -------------------------------------------------------------------------
    // Supportive method
    // -------------------------------------------------------------------------

    private Activity getActivity( ProgramStageInstance instance, boolean late )
    {
        Activity activity = new Activity();
        Patient patient = instance.getProgramInstance().getPatient();

        activity.setBeneficiary( getBeneficiaryModel( patient ) );
        activity.setDueDate( instance.getDueDate() );
        activity.setTask( getTask( instance ) );
        activity.setLate( late );
        activity.setExpireDate( DateUtils.getDateAfterAddition( instance.getDueDate(), 30 ) );

        return activity;
    }

    private Task getTask( ProgramStageInstance instance )
    {
        if ( instance == null )
            return null;

        Task task = new Task();
        task.setCompleted( instance.isCompleted() );
        task.setId( instance.getId() );
        task.setProgramStageId( instance.getProgramStage().getId() );
        task.setProgramId( instance.getProgramInstance().getProgram().getId() );
        return task;
    }

    private Beneficiary getBeneficiaryModel( Patient patient )
    {
        Beneficiary beneficiary = new Beneficiary();
        List<PatientAttribute> patientAtts = new ArrayList<PatientAttribute>();
        List<org.hisp.dhis.patient.PatientAttribute> atts;

        beneficiary.setId( patient.getId() );
        beneficiary.setFirstName( patient.getFirstName() );
        beneficiary.setLastName( patient.getLastName() );
        beneficiary.setMiddleName( patient.getMiddleName() );

        Period period = new Period( new DateTime( patient.getBirthDate() ), new DateTime() );
        beneficiary.setAge( period.getYears() );

        this.setSetting( getSettings() );

        if ( setting != null )
        {
            if ( setting.getGender() )
            {
                beneficiary.setGender( patient.getGender() );
            }
            if ( setting.getDobtype() )
            {
                beneficiary.setDobType( patient.getDobType() );
            }
            if ( setting.getBirthdate() )
            {
                beneficiary.setBirthDate( patient.getBirthDate() );
            }
            if ( setting.getRegistrationdate() )
            {
                beneficiary.setRegistrationDate( patient.getRegistrationDate() );
            }

            atts = setting.getPatientAttributes();
            for ( org.hisp.dhis.patient.PatientAttribute each : atts )
            {
                PatientAttributeValue value = patientAttValueService.getPatientAttributeValue( patient, each );
                if ( value != null )
                {
                    patientAtts.add( new PatientAttribute( each.getName(), value.getValue() ) );
                }
            }

        }

        // Set attribute which is used to group beneficiary on mobile (only if
        // there is attribute which is set to be group factor)
        PatientAttribute beneficiaryAttribute = null;

        if ( groupByAttribute != null )
        {
            beneficiaryAttribute = new PatientAttribute();
            beneficiaryAttribute.setName( groupByAttribute.getName() );
            PatientAttributeValue value = patientAttValueService.getPatientAttributeValue( patient, groupByAttribute );
            beneficiaryAttribute.setValue( value == null ? "Unknown" : value.getValue() );
            beneficiary.setGroupAttribute( beneficiaryAttribute );
        }

        // Set all identifier
        Set<PatientIdentifier> patientIdentifiers = patient.getIdentifiers();
        List<org.hisp.dhis.api.mobile.model.PatientIdentifier> identifiers = new ArrayList<org.hisp.dhis.api.mobile.model.PatientIdentifier>();
        if ( patientIdentifiers.size() > 0 )
        {

            for ( PatientIdentifier id : patientIdentifiers )
            {

                String idTypeName = "DHIS2 ID";

                // MIGHT BE NULL because of strange design..
                PatientIdentifierType identifierType = id.getIdentifierType();

                if ( identifierType != null )
                {
                    idTypeName = identifierType.getName();
                }

                identifiers
                    .add( new org.hisp.dhis.api.mobile.model.PatientIdentifier( idTypeName, id.getIdentifier() ) );
            }

            beneficiary.setIdentifiers( identifiers );
        }

        beneficiary.setPatientAttValues( patientAtts );
        return beneficiary;
    }

    // get patient model for LWUIT
    private org.hisp.dhis.api.mobile.model.LWUITmodel.Patient getPatientModel( int orgUnitId, Patient patient )
    {
        org.hisp.dhis.api.mobile.model.LWUITmodel.Patient patientModel = new org.hisp.dhis.api.mobile.model.LWUITmodel.Patient();
        List<PatientAttribute> patientAtts = new ArrayList<PatientAttribute>();
        List<org.hisp.dhis.api.mobile.model.LWUITmodel.Program> mobileProgramList = new ArrayList<org.hisp.dhis.api.mobile.model.LWUITmodel.Program>();
        List<org.hisp.dhis.patient.PatientAttribute> atts;

        patientModel.setId( patient.getId() );
        patientModel.setFirstName( patient.getFirstName() );
        patientModel.setLastName( patient.getLastName() );
        patientModel.setMiddleName( patient.getMiddleName() );

        Period period = new Period( new DateTime( patient.getBirthDate() ), new DateTime() );
        patientModel.setAge( period.getYears() );
        patientModel.setPhoneNumber( patient.getPhoneNumber() );

        this.setSetting( getSettings() );

        if ( setting != null )
        {
            if ( setting.getGender() )
            {
                patientModel.setGender( patient.getGender() );
            }
            if ( setting.getDobtype() )
            {
                patientModel.setDobType( patient.getDobType() );
            }
            if ( setting.getBirthdate() )
            {
                patientModel.setBirthDate( patient.getBirthDate() );
            }
            if ( setting.getRegistrationdate() )
            {
                patientModel.setRegistrationDate( patient.getRegistrationDate() );
            }

            atts = setting.getPatientAttributes();
            for ( org.hisp.dhis.patient.PatientAttribute each : atts )
            {
                PatientAttributeValue value = patientAttValueService.getPatientAttributeValue( patient, each );
                if ( value != null )
                {
                    patientAtts.add( new PatientAttribute( each.getName(), value.getValue() ) );
                }
            }

        }

        // Set attribute which is used to group beneficiary on mobile (only if
        // there is attribute which is set to be group factor)
        PatientAttribute patientAttribute = null;

        if ( groupByAttribute != null )
        {
            patientAttribute = new PatientAttribute();
            patientAttribute.setName( groupByAttribute.getName() );
            PatientAttributeValue value = patientAttValueService.getPatientAttributeValue( patient, groupByAttribute );
            patientAttribute.setValue( value == null ? "Unknown" : value.getValue() );
            patientModel.setGroupAttribute( patientAttribute );
        }

        // Set all identifier
        Set<PatientIdentifier> patientIdentifiers = patient.getIdentifiers();
        List<org.hisp.dhis.api.mobile.model.PatientIdentifier> identifiers = new ArrayList<org.hisp.dhis.api.mobile.model.PatientIdentifier>();
        if ( patientIdentifiers.size() > 0 )
        {

            for ( PatientIdentifier id : patientIdentifiers )
            {

                String idTypeName = "DHIS2 ID";

                // MIGHT BE NULL because of strange design..
                PatientIdentifierType identifierType = id.getIdentifierType();

                if ( identifierType != null )
                {
                    idTypeName = identifierType.getName();
                }

                identifiers
                    .add( new org.hisp.dhis.api.mobile.model.PatientIdentifier( idTypeName, id.getIdentifier() ) );
            }

            patientModel.setIdentifiers( identifiers );
        }

        patientModel.setPatientAttValues( patientAtts );

        // Set all programs

        List<ProgramInstance> listOfProgramInstance = new ArrayList<ProgramInstance>(
            programInstanceService.getProgramInstances( patient ) );
        if ( listOfProgramInstance.size() > 0 )
        {
            for ( ProgramInstance each : listOfProgramInstance )
            {
                org.hisp.dhis.api.mobile.model.LWUITmodel.Program mobileProgram = new org.hisp.dhis.api.mobile.model.LWUITmodel.Program();
                mobileProgram.setVersion( each.getProgram().getVersion() );
                mobileProgram.setId( each.getId() );
                mobileProgram.setName( each.getProgram().getName() );
                mobileProgram.setCompleted( each.isCompleted() );
                mobileProgram.setProgramStages( getMobileProgramStages( patient, each, each.getProgram() ) );

                mobileProgramList.add( mobileProgram );
            }
        }

        patientModel.setPrograms( mobileProgramList );

        // Set Relationship
        List<Relationship> relationships = new ArrayList<Relationship>(
            relationshipService.getRelationshipsForPatient( patient ) );
        List<org.hisp.dhis.api.mobile.model.LWUITmodel.Relationship> relationshipList = new ArrayList<org.hisp.dhis.api.mobile.model.LWUITmodel.Relationship>();
        for ( Relationship eachRelationship : relationships )
        {
            org.hisp.dhis.api.mobile.model.LWUITmodel.Relationship relationshipMobile = new org.hisp.dhis.api.mobile.model.LWUITmodel.Relationship();
            relationshipMobile.setId( eachRelationship.getId() );
            if ( eachRelationship.getPatientA().getId() == patient.getId() )
            {
                relationshipMobile.setName( eachRelationship.getRelationshipType().getaIsToB() );
                relationshipMobile.setPersonName( eachRelationship.getPatientB().getFullName() );
            }
            else
            {
                relationshipMobile.setName( eachRelationship.getRelationshipType().getbIsToA() );
                relationshipMobile.setPersonName( eachRelationship.getPatientA().getFullName() );
            }
            relationshipList.add( relationshipMobile );
        }
        patientModel.setRelationships( relationshipList );

        // Set available enrollment programs
        List<Program> enrollmentProgramList = new ArrayList<Program>();
        enrollmentProgramList = generateEnrollmentProgramList( orgUnitId, patient );
        List<org.hisp.dhis.api.mobile.model.LWUITmodel.Program> enrollmentProgramListMobileList = new ArrayList<org.hisp.dhis.api.mobile.model.LWUITmodel.Program>();
        for ( Program enrollmentProgram : enrollmentProgramList )
        {
            org.hisp.dhis.api.mobile.model.LWUITmodel.Program enrollmentProgramMobile = new org.hisp.dhis.api.mobile.model.LWUITmodel.Program();
            enrollmentProgramMobile.setId( enrollmentProgram.getId() );
            enrollmentProgramMobile.setName( enrollmentProgram.getName() );
            enrollmentProgramMobile.setCompleted( false );
            enrollmentProgramMobile.setVersion( enrollmentProgram.getVersion() );
            enrollmentProgramMobile.setProgramStages( null );
            enrollmentProgramListMobileList.add( enrollmentProgramMobile );
        }

        patientModel.setEnrollmentPrograms( enrollmentProgramListMobileList );

        return patientModel;
    }

    private List<org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStage> getMobileProgramStages( Patient patient,
        ProgramInstance programInstance, Program program )
    {

        List<org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStage> mobileProgramStages = new ArrayList<org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStage>();
        for ( ProgramStage eachProgramStage : program.getProgramStages() )
        {
            ProgramStageInstance programStageInstance = programStageInstanceService.getProgramStageInstance(
                programInstance, eachProgramStage );
            org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStage mobileProgramStage = new org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStage();
            List<org.hisp.dhis.api.mobile.model.LWUITmodel.Section> mobileSections = new ArrayList<org.hisp.dhis.api.mobile.model.LWUITmodel.Section>();
            mobileProgramStage.setId( programStageInstance.getId() );
            mobileProgramStage.setName( eachProgramStage.getName() );

            // is repeatable
            mobileProgramStage.setRepeatable( eachProgramStage.getIrregular() );

            // is completed
            mobileProgramStage.setCompleted( checkIfProgramStageCompleted( patient, program, eachProgramStage ) );

            // Set all data elements
            mobileProgramStage.setDataElements( getDataElementsForMobile( eachProgramStage, programStageInstance ) );

            // Set all program sections
            if ( eachProgramStage.getProgramStageSections().size() > 0 )
            {
                for ( ProgramStageSection eachSection : eachProgramStage.getProgramStageSections() )
                {
                    org.hisp.dhis.api.mobile.model.LWUITmodel.Section mobileSection = new org.hisp.dhis.api.mobile.model.LWUITmodel.Section();
                    mobileSection.setId( eachSection.getId() );
                    mobileSection.setName( eachSection.getName() );

                    // Set all data elements' id, then we could have full from
                    // data element list of program stage
                    List<Integer> dataElementIds = new ArrayList<Integer>();
                    for ( ProgramStageDataElement eachPogramStageDataElement : eachSection
                        .getProgramStageDataElements() )
                    {
                        dataElementIds.add( eachPogramStageDataElement.getDataElement().getId() );
                    }
                    mobileSection.setDataElementIds( dataElementIds );
                    mobileSections.add( mobileSection );
                }
            }
            mobileProgramStage.setSections( mobileSections );

            mobileProgramStages.add( mobileProgramStage );

        }
        return mobileProgramStages;
    }

    private boolean checkIfProgramStageCompleted( Patient patient, Program program, ProgramStage programstage )
    {

        Collection<ProgramInstance> programIntances = programInstanceService.getProgramInstances( patient, program,
            false );
        ProgramStageInstance programStageInstance = new ProgramStageInstance();
        if ( programIntances != null && programIntances.size() == 1 )
        {
            for ( ProgramInstance each : programIntances )
            {
                programStageInstance = programStageInstanceService.getProgramStageInstance( each, programstage );
            }
        }
        return programStageInstance.isCompleted();
    }

    private List<org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStageDataElement> getDataElementsForMobile(
        ProgramStage programStage, ProgramStageInstance programStageInstance )
    {
        List<ProgramStageDataElement> programStageDataElements = new ArrayList<ProgramStageDataElement>(
            programStage.getProgramStageDataElements() );
        List<org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStageDataElement> mobileDataElements = new ArrayList<org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStageDataElement>();
        for ( ProgramStageDataElement programStageDataElement : programStageDataElements )
        {
            org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStageDataElement mobileDataElement = new org.hisp.dhis.api.mobile.model.LWUITmodel.ProgramStageDataElement();
            mobileDataElement.setId( programStageDataElement.getDataElement().getId() );
            mobileDataElement.setName( programStageDataElement.getDataElement().getName() );
            mobileDataElement.setType( programStageDataElement.getDataElement().getType() );
            if ( programStageDataElement.getDataElement().getNumberType() != null )
            {
                mobileDataElement.setCompulsory( programStageDataElement.isCompulsory() );
            }
            mobileDataElement.setNumberType( programStageDataElement.getDataElement().getNumberType() );

            PatientDataValue patientDataValue = dataValueService.getPatientDataValue( programStageInstance,
                programStageDataElement.getDataElement() );
            if ( patientDataValue != null )
            {
                mobileDataElement.setValue( patientDataValue.getValue() );
            }
            else
            {
                mobileDataElement.setValue( "" );
            }
            if ( programStageDataElement.getDataElement().getOptionSet() != null )
            {
                mobileDataElement.setOptionSet( modelMapping.getLWUITOptionSet( programStageDataElement
                    .getDataElement() ) );
            }
            else
            {
                mobileDataElement.setOptionSet( null );
            }
            if ( programStageDataElement.getDataElement().getCategoryCombo() != null )
            {
                mobileDataElement.setCategoryOptionCombos( modelMapping
                    .getCategoryOptionCombos( programStageDataElement.getDataElement() ) );
            }
            else
            {
                mobileDataElement.setCategoryOptionCombos( null );
            }
            mobileDataElements.add( mobileDataElement );
        }
        return mobileDataElements;
    }

    private PatientMobileSetting getSettings()
    {
        PatientMobileSetting setting = null;

        Collection<PatientMobileSetting> currentSetting = patientMobileSettingService.getCurrentSetting();
        if ( currentSetting != null && !currentSetting.isEmpty() )
            setting = currentSetting.iterator().next();
        return setting;
    }

    private List<Program> generateEnrollmentProgramList( int orgId, Patient patient )
    {
        List<Program> programs = new ArrayList<Program>();
        for ( Program program : programService.getPrograms( organisationUnitService.getOrganisationUnit( orgId ) ) )

        {
            if ( (program.isSingleEvent() && program.isRegistration()) || !program.isSingleEvent() )
            {
                // wrong here
                if ( programInstanceService.getProgramInstances( patient, program ).size() == 0 )
                {
                    programs.add( program );
                }
            }
        }
        return programs;
    }

    private void saveDataValues( ActivityValue activityValue, ProgramStageInstance programStageInstance,
        Map<Integer, DataElement> dataElementMap )
    {
        org.hisp.dhis.dataelement.DataElement dataElement;
        String value;

        for ( DataValue dv : activityValue.getDataValues() )
        {
            value = dv.getValue();

            if ( value != null && value.trim().length() == 0 )
            {
                value = null;
            }

            if ( value != null )
            {
                value = value.trim();
            }

            dataElement = dataElementMap.get( dv.getId() );
            PatientDataValue dataValue = dataValueService.getPatientDataValue( programStageInstance, dataElement );
            if ( dataValue == null )
            {
                if ( value != null )
                {
                    if ( programStageInstance.getExecutionDate() == null )
                    {
                        programStageInstance.setExecutionDate( new Date() );
                        programStageInstanceService.updateProgramStageInstance( programStageInstance );
                    }

                    dataValue = new PatientDataValue( programStageInstance, dataElement, new Date(), value );

                    dataValueService.savePatientDataValue( dataValue );
                }
            }
            else
            {
                if ( programStageInstance.getExecutionDate() == null )
                {
                    programStageInstance.setExecutionDate( new Date() );
                    programStageInstanceService.updateProgramStageInstance( programStageInstance );
                }

                dataValue.setValue( value );
                dataValue.setTimestamp( new Date() );

                dataValueService.updatePatientDataValue( dataValue );
            }
        }
    }

    // Setters...

    @Required
    public void setProgramStageInstanceService(
        org.hisp.dhis.program.ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    @Required
    public void setPatientAttValueService( PatientAttributeValueService patientAttValueService )
    {
        this.patientAttValueService = patientAttValueService;
    }

    @Required
    public void setPatientAttService( PatientAttributeService patientAttService )
    {
        this.patientAttService = patientAttService;
    }

    @Required
    public void setDataValueService( org.hisp.dhis.patientdatavalue.PatientDataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    @Required
    public void setPatientMobileSettingService( PatientMobileSettingService patientMobileSettingService )
    {
        this.patientMobileSettingService = patientMobileSettingService;
    }

    @Required
    public void setModelMapping( org.hisp.dhis.mobile.service.ModelMapping modelMapping )
    {
        this.modelMapping = modelMapping;
    }

    public PatientMobileSetting getSetting()
    {
        return setting;
    }

    public void setSetting( PatientMobileSetting setting )
    {
        this.setting = setting;
    }

    public org.hisp.dhis.patient.PatientAttribute getGroupByAttribute()
    {
        return groupByAttribute;
    }

    public void setGroupByAttribute( org.hisp.dhis.patient.PatientAttribute groupByAttribute )
    {
        this.groupByAttribute = groupByAttribute;
    }

    @Required
    public void setPatientIdentifierService( PatientIdentifierService patientIdentifierService )
    {
        this.patientIdentifierService = patientIdentifierService;
    }

    @Required
    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    @Required
    public void setProgramInstanceService( ProgramInstanceService programInstanceService )
    {
        this.programInstanceService = programInstanceService;
    }

    @Required
    public void setRelationshipService( RelationshipService relationshipService )
    {
        this.relationshipService = relationshipService;
    }

    @Required
    public void setProgramStageSectionService( ProgramStageSectionService programStageSectionService )
    {
        this.programStageSectionService = programStageSectionService;
    }

    @Required
    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    @Required
    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    @Required
    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    @Required
    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }
}
