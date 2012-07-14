/*
 * Copyright (c) 2004-2009, University of Oslo
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
package org.hisp.dhis.caseentry.action.patient;

import java.util.Collection;
import java.util.Date;

import org.hisp.dhis.caseentry.state.SelectedStateManager;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.system.util.DateUtils;

import com.opensymphony.xwork2.Action;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
public class SaveProgramEnrollmentAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PatientService patientService;

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
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

    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private Integer patientId;

    public Integer getPatientId()
    {
        return patientId;
    }

    public void setPatientId( Integer patientId )
    {
        this.patientId = patientId;
    }

    private Integer programId;

    public void setProgramId( Integer programId )
    {
        this.programId = programId;
    }

    private String enrollmentDate;

    public void setEnrollmentDate( String enrollmentDate )
    {
        this.enrollmentDate = enrollmentDate;
    }

    private String dateOfIncident;

    public void setDateOfIncident( String dateOfIncident )
    {
        this.dateOfIncident = dateOfIncident;
    }

    private ProgramInstance programInstance;

    public ProgramInstance getProgramInstance()
    {
        return programInstance;
    }

    private ProgramStageInstance activeProgramStageInstance;

    public ProgramStageInstance getActiveProgramStageInstance()
    {
        return activeProgramStageInstance;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        Patient patient = patientService.getPatient( patientId );

        Program program = programService.getProgram( programId );

        if ( dateOfIncident == null || dateOfIncident.isEmpty() )
        {
            dateOfIncident = enrollmentDate;
        }

        Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( patient, program,
            false );

        if ( programInstances.iterator().hasNext() )
        {
            programInstance = programInstances.iterator().next();
        }

        if ( programInstance == null )
        {
            programInstance = new ProgramInstance();
            programInstance.setEnrollmentDate( format.parseDate( enrollmentDate ) );
            programInstance.setDateOfIncident( format.parseDate( dateOfIncident ) );
            programInstance.setProgram( program );
            programInstance.setPatient( patient );
            programInstance.setCompleted( false );

            programInstanceService.addProgramInstance( programInstance );

            patient.getPrograms().add( program );
            patientService.updatePatient( patient );

            Date dateCreatedEvent = format.parseDate( dateOfIncident );
            if ( program.getGeneratedByEnrollmentDate() )
            {
                dateCreatedEvent = format.parseDate( enrollmentDate );
            }

            boolean isFirstStage = false;
            Date currentDate = new Date();
            for ( ProgramStage programStage : program.getProgramStages() )
            {
                if ( programStage.getAutoGenerateEvent() )
                {
                    Date dueDate = DateUtils
                        .getDateAfterAddition( dateCreatedEvent, programStage.getMinDaysFromStart() );

                    if ( ! ( program.getIgnoreOverdueEvents() && dueDate.before( currentDate ) ))
                    {
                        ProgramStageInstance programStageInstance = new ProgramStageInstance();
                        programStageInstance.setProgramInstance( programInstance );
                        programStageInstance.setProgramStage( programStage );
                        programStageInstance.setDueDate( dueDate );

                        if ( program.isSingleEvent() )
                        {
                            programStageInstance.setOrganisationUnit( selectedStateManager
                                .getSelectedOrganisationUnit() );
                            programStageInstance.setExecutionDate( dueDate );
                        }
                        programStageInstanceService.addProgramStageInstance( programStageInstance );

                        if ( !isFirstStage )
                        {
                            activeProgramStageInstance = programStageInstance;
                            isFirstStage = true;
                        }
                    }
                }
            }
        }
        else
        {
            programInstance.setEnrollmentDate( format.parseDate( enrollmentDate ) );
            programInstance.setDateOfIncident( format.parseDate( dateOfIncident ) );

            programInstanceService.updateProgramInstance( programInstance );

            for ( ProgramStageInstance programStageInstance : programInstance.getProgramStageInstances() )
            {
                if ( !programStageInstance.isCompleted()
                    || programStageInstance.getStatus() != ProgramStageInstance.SKIPPED_STATUS )
                {
                    Date dueDate = DateUtils.getDateAfterAddition( format.parseDate( dateOfIncident ),
                        programStageInstance.getProgramStage().getMinDaysFromStart() );

                    programStageInstance.setDueDate( dueDate );

                    programStageInstanceService.updateProgramStageInstance( programStageInstance );
                }
            }
        }

        return SUCCESS;
    }
}
