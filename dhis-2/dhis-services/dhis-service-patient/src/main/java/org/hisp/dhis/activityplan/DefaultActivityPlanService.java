package org.hisp.dhis.activityplan;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.system.util.DateUtils;

/**
 * @author abyotag_adm
 * 
 */
public class DefaultActivityPlanService
    implements ActivityPlanService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ActivityPlanStore activityPlanStore;

    public void setActivityPlanStore( ActivityPlanStore activityPlanStore )
    {
        this.activityPlanStore = activityPlanStore;
    }

    private ProgramService programService;

    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }

    private ProgramInstanceService programInstanceService;

    public void setProgramInstanceService( ProgramInstanceService programInstanceService )
    {
        this.programInstanceService = programInstanceService;
    }

    private PatientDataValueService patientDataValueService;

    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    // -------------------------------------------------------------------------
    // Activity Plan
    // -------------------------------------------------------------------------

    @Override
    public Collection<Activity> getActivitiesByBeneficiary( Patient beneficiary )
    {
        Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( beneficiary, false );

        return getActivties( programInstances );
    }

    public Collection<Activity> getCurrentActivitiesByProvider( OrganisationUnit organisationUnit )
    {
        long time = PeriodType.createCalendarInstance().getTime().getTime();

        List<Activity> items = new ArrayList<Activity>();

        List<ProgramInstance> programInstances = new ArrayList<ProgramInstance>();

        Collection<Program> programs = programService.getPrograms( Program.MULTIPLE_EVENTS_WITH_REGISTRATION,
            organisationUnit );

        for ( Program program : programs )
        {
            programInstances.addAll( programInstanceService.getProgramInstances( program, organisationUnit ) );
        }

        Calendar expiredDate = Calendar.getInstance();

        for ( ProgramInstance programInstance : programInstances )
        {
            Set<ProgramStageInstance> programStageInstances = programInstance.getProgramStageInstances();
            for ( ProgramStageInstance programStageInstance : programStageInstances )
            {
                if ( !programStageInstance.isCompleted() )
                {
                    expiredDate.setTime( DateUtils.getDateAfterAddition( programStageInstance.getDueDate(),
                        programStageInstance.getProgramInstance().getProgram().getMaxDaysAllowedInputData() ) );

                    // TODO compare with date.before

                    if ( programStageInstance.getDueDate().getTime() <= time && expiredDate.getTimeInMillis() > time )
                    {
                        Activity activity = new Activity();
                        activity.setBeneficiary( programInstance.getPatient() );
                        activity.setTask( programStageInstance );
                        activity.setDueDate( programStageInstance.getDueDate() );
                        items.add( activity );
                    }
                }
            }
        }
        return items;
    }

    public Collection<Activity> getActivitiesByProvider( OrganisationUnit organisationUnit )
    {
        Collection<Activity> activities = new ArrayList<Activity>();

        Collection<Program> programs = programService.getPrograms( Program.MULTIPLE_EVENTS_WITH_REGISTRATION,
            organisationUnit );

        if ( programs.size() > 0 )
        {
            // -----------------------------------------------------------------
            // For all the programs a facility is servicing get active programs,
            // those with active instances (completed = false)
            // -----------------------------------------------------------------

            Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( programs, false );

            // -----------------------------------------------------------------
            // Get next activities for the active programInstances
            // -----------------------------------------------------------------

            activities = getActivties( programInstances );
        }

        return activities;
    }

    public Collection<Activity> getActivitiesByProvider( OrganisationUnit organisationUnit, Collection<Program> programs )
    {

        Collection<Activity> activities = new ArrayList<Activity>();

        if ( programService.getPrograms( Program.MULTIPLE_EVENTS_WITH_REGISTRATION, organisationUnit ).containsAll(
            programs ) )
        {
            Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( programs, false );

            // -----------------------------------------------------------------
            // Get next activities for the active programInstances
            // -----------------------------------------------------------------

            activities = getActivties( programInstances );
        }

        return activities;
    }

    public Collection<Activity> getActivitiesByProgram( Collection<Program> programs )
    {

        Collection<Activity> activities = new ArrayList<Activity>();

        Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( programs, false );

        // -----------------------------------------------------------------
        // Get next activities for the active programInstances
        // -----------------------------------------------------------------

        activities = getActivties( programInstances );

        return activities;
    }

    @Override
    public Collection<Activity> getActivitiesByTask( ProgramStageInstance task )
    {
        // ---------------------------------------------------------------------
        // Get the parent program for the given program stage
        // ---------------------------------------------------------------------

        Program program = task.getProgramInstance().getProgram();

        // ---------------------------------------------------------------------
        // Pick only those active instances for the identified program
        // ---------------------------------------------------------------------

        Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( program, false );

        // ---------------------------------------------------------------------
        // Get next activities for the active programInstances
        // ---------------------------------------------------------------------

        return getActivties( programInstances );
    }

    @Override
    public Collection<Activity> getActivitiesByDueDate( Date dueDate )
    {
        // ---------------------------------------------------------------------
        // Get all active stageInstances within the given due date
        // ---------------------------------------------------------------------

        Collection<ProgramStageInstance> programStageInstances = programStageInstanceService.getProgramStageInstances(
            dueDate, false );

        Collection<Activity> activities = new ArrayList<Activity>();

        for ( ProgramStageInstance programStageInstance : programStageInstances )
        {

            Activity activity = new Activity();
            activity.setBeneficiary( programStageInstance.getProgramInstance().getPatient() );
            activity.setTask( programStageInstance );
            activity.setDueDate( programStageInstance.getDueDate() );
            activities.add( activity );
        }

        return activities;
    }

    @Override
    public Collection<Activity> getActivitiesWithInDate( Date startDate, Date endDate )
    {
        // ---------------------------------------------------------------------
        // Get all active stageInstances within the given time frame
        // ---------------------------------------------------------------------

        Collection<ProgramStageInstance> programStageInstances = programStageInstanceService.getProgramStageInstances(
            startDate, endDate, false );

        Collection<Activity> activities = new ArrayList<Activity>();

        for ( ProgramStageInstance programStageInstance : programStageInstances )
        {

            Activity activity = new Activity();
            activity.setBeneficiary( programStageInstance.getProgramInstance().getPatient() );
            activity.setTask( programStageInstance );
            activity.setDueDate( programStageInstance.getDueDate() );
            activities.add( activity );
        }

        return activities;

    }

    public Collection<Activity> getActivitiesByProvider( OrganisationUnit organisationUnit, int min, int max )
    {
        Collection<Integer> programStageInstanceIds = activityPlanStore.getActivitiesByProvider( organisationUnit
            .getId(), min, max );

        Collection<Activity> activities = new ArrayList<Activity>();

        if ( programStageInstanceIds != null )
        {
            for ( Integer id : programStageInstanceIds )
            {
                ProgramStageInstance instance = programStageInstanceService.getProgramStageInstance( id );
                Activity activity = new Activity();
                activity.setBeneficiary( instance.getProgramInstance().getPatient() );
                activity.setTask( instance );
                activity.setDueDate( instance.getDueDate() );
                activities.add( activity );
            }
        }

        return activities;
    }

    public int countActivitiesByProvider( OrganisationUnit organisationUnit )
    {
        return activityPlanStore.countActivitiesByProvider( organisationUnit.getId() );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Collection<Activity> getActivties( Collection<ProgramInstance> programInstances )
    {
        Collection<Activity> activities = new ArrayList<Activity>();

        // ---------------------------------------------------------------------
        // Get all stageInstances for the give programInstances
        // ---------------------------------------------------------------------

        Collection<ProgramStageInstance> programStageInstances = programStageInstanceService
            .getProgramStageInstances( programInstances );

        Map<String, ProgramStageInstance> mappedStageInstance = new HashMap<String, ProgramStageInstance>();

        for ( ProgramStageInstance programStageInstance : programStageInstances )
        {
            mappedStageInstance.put( programStageInstance.getProgramInstance().getId() + "_"
                + programStageInstance.getProgramStage().getId(), programStageInstance );

        }

        // -----------------------------------------------------------------
        // Initially assume to have a first visit for all programInstances
        // -----------------------------------------------------------------

        Map<Integer, Integer> visitsByProgramInstances = new HashMap<Integer, Integer>();

        for ( ProgramInstance programInstance : programInstances )
        {
            programStageInstances.addAll( programInstance.getProgramStageInstances() );

            visitsByProgramInstances.put( programInstance.getId(), 0 );
        }

        // ---------------------------------------------------------------------
        // For each of these active instances, see at which stage they are
        // actually (may not necessarily be at the first stage)
        // ---------------------------------------------------------------------

        Collection<PatientDataValue> patientDataValues = patientDataValueService
            .getPatientDataValues( programStageInstances );

        for ( PatientDataValue patientDataValue : patientDataValues )
        {
            if ( visitsByProgramInstances.get( patientDataValue.getProgramStageInstance().getProgramInstance().getId() ) < patientDataValue
                .getProgramStageInstance().getProgramStage().getStageInProgram() )
            {
                visitsByProgramInstances.put( patientDataValue.getProgramStageInstance().getProgramInstance().getId(),
                    patientDataValue.getProgramStageInstance().getProgramStage().getStageInProgram() );
            }
        }

        // ---------------------------------------------------------------------
        // For each of these active instances, based on the current stage
        // determine the next stage
        // ---------------------------------------------------------------------

        for ( ProgramInstance programInstance : programInstances )
        {
            Program program = programInstance.getProgram();

            ProgramStage nextStage = program.getProgramStageByStage( visitsByProgramInstances.get( programInstance
                .getId() ) + 1 );

            if ( nextStage != null )
            {

                ProgramStageInstance nextStageInstance = mappedStageInstance.get( programInstance.getId() + "_"
                    + nextStage.getId() );

                Activity activity = new Activity();
                activity.setBeneficiary( programInstance.getPatient() );
                activity.setTask( nextStageInstance );

                if ( nextStageInstance != null )
                {
                    activity.setDueDate( nextStageInstance.getDueDate() );
                }

                activities.add( activity );
            }
        }

        return activities;
    }

}
