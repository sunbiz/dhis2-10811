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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.activityplan.ActivityPlanService;
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
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAttributeService;
import org.hisp.dhis.patient.PatientIdentifier;
import org.hisp.dhis.patient.PatientIdentifierService;
import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.patient.PatientMobileSetting;
import org.hisp.dhis.patientattributevalue.PatientAttributeValue;
import org.hisp.dhis.patientattributevalue.PatientAttributeValueService;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.system.util.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Required;

public class ActivityReportingServiceImpl
    implements ActivityReportingService
{

    private static Log log = LogFactory.getLog( ActivityReportingServiceImpl.class );

    private static final boolean DEBUG = log.isDebugEnabled();

    private ActivityComparator activityComparator = new ActivityComparator();

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramStageInstanceService programStageInstanceService;

    private ActivityPlanService activityPlanService;

    private PatientAttributeValueService patientAttValueService;

    private PatientAttributeService patientAttService;

    private PatientDataValueService dataValueService;

    private PatientMobileSettingService patientMobileSettingService;

    private PatientIdentifierService patientIdentifierService;

    // -------------------------------------------------------------------------
    // MobileDataSetService
    // -------------------------------------------------------------------------

    private PatientMobileSetting setting;

    private org.hisp.dhis.patient.PatientAttribute groupByAttribute;

    @Override
    public ActivityPlan getCurrentActivityPlan( OrganisationUnit unit, String localeString )
    {
        long time = System.currentTimeMillis();

        List<Activity> items = new ArrayList<Activity>();

        this.setGroupByAttribute( patientAttService.getPatientAttributeByGroupBy( true ) );

        Collection<org.hisp.dhis.activityplan.Activity> activities = activityPlanService
            .getCurrentActivitiesByProvider( unit );

        for ( org.hisp.dhis.activityplan.Activity activity : activities )
        {
            items.add( getActivity( activity.getTask(), activity.getDueDate().getTime() < time ) );
        }

        if ( items.isEmpty() )
        {
            return null;
        }

        Collections.sort( items, activityComparator );

        if ( DEBUG )
            log.debug( "Found " + items.size() + " current activities in " + (System.currentTimeMillis() - time)
                + " ms." );

        return new ActivityPlan( items );
    }

    @Override
    public ActivityPlan getAllActivityPlan( OrganisationUnit unit, String localeString )
    {
        long time = System.currentTimeMillis();

        List<Activity> items = new ArrayList<Activity>();

        this.setGroupByAttribute( patientAttService.getPatientAttributeByGroupBy( true ) );

        Collection<org.hisp.dhis.activityplan.Activity> activities = activityPlanService.getActivitiesByProvider( unit );

        for ( org.hisp.dhis.activityplan.Activity activity : activities )
        {
            if ( activity.getDueDate() != null )
            {
                items.add( getActivity( activity.getTask(), activity.getDueDate().getTime() < time ) );
            }

        }

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

                        expiredDate.setTime( DateUtils.getDateAfterAddition( programStageInstance.getDueDate(),
                            programStageInstance.getProgramInstance().getProgram().getMaxDaysAllowedInputData() ) );

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
    public void saveActivityReport( OrganisationUnit unit, ActivityValue activityValue )
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

        for ( ProgramStageDataElement de : programStageInstance.getProgramStage().getProgramStageDataElements() )
        {
            dataElements.add( de.getDataElement() );
        }

        programStageInstance.getProgramStage().getProgramStageDataElements();
        Collection<Integer> dataElementIds = new ArrayList<Integer>( activityValue.getDataValues().size() );

        for ( DataValue dv : activityValue.getDataValues() )
        {
            dataElementIds.add( dv.getId() );
        }

        if ( dataElements.size() != dataElementIds.size() )
        {
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
        programStageInstance.setCompleted( true );
        programStageInstanceService.updateProgramStageInstance( programStageInstance );
        // Everything is fine, hence save
        saveDataValues( activityValue, programStageInstance, dataElementMap );

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
        activity.setExpireDate( DateUtils.getDateAfterAddition( instance.getDueDate(), instance.getProgramInstance()
            .getProgram().getMaxDaysAllowedInputData() ) );

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

    private PatientMobileSetting getSettings()
    {
        PatientMobileSetting setting = null;

        Collection<PatientMobileSetting> currentSetting = patientMobileSettingService.getCurrentSetting();
        if ( currentSetting != null && !currentSetting.isEmpty() )
            setting = currentSetting.iterator().next();
        return setting;
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

    public void setActivityPlanService( org.hisp.dhis.activityplan.ActivityPlanService activityPlanService )
    {
        this.activityPlanService = activityPlanService;
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

    public PatientIdentifierService getPatientIdentifierService()
    {
        return patientIdentifierService;
    }

    @Required
    public void setPatientIdentifierService( PatientIdentifierService patientIdentifierService )
    {
        this.patientIdentifierService = patientIdentifierService;
    }

}
