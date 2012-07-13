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

package org.hisp.dhis.patient.action.program;

import java.util.Date;

import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageService;

import com.opensymphony.xwork2.Action;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
public class AddProgramAction
    implements Action
{
    private static String SINGLE_EVENT = "Single-Event";

    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private ProgramService programService;

    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }

    private ProgramStageService programStageService;

    public void setProgramStageService( ProgramStageService programStageService )
    {
        this.programStageService = programStageService;
    }

    private ProgramInstanceService programInstanceService;

    public void setProgramInstanceService( ProgramInstanceService programInstanceService )
    {
        this.programInstanceService = programInstanceService;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }

    private String dateOfEnrollmentDescription;

    public void setDateOfEnrollmentDescription( String dateOfEnrollmentDescription )
    {
        this.dateOfEnrollmentDescription = dateOfEnrollmentDescription;
    }

    private String dateOfIncidentDescription;

    public void setDateOfIncidentDescription( String dateOfIncidentDescription )
    {
        this.dateOfIncidentDescription = dateOfIncidentDescription;
    }

    private Integer maxDaysAllowedInputData;

    public void setMaxDaysAllowedInputData( Integer maxDaysAllowedInputData )
    {
        this.maxDaysAllowedInputData = maxDaysAllowedInputData;
    }

    private Integer type;

    public void setType( Integer type )
    {
        this.type = type;
    }

    private Boolean displayProvidedOtherFacility;

    public void setDisplayProvidedOtherFacility( Boolean displayProvidedOtherFacility )
    {
        this.displayProvidedOtherFacility = displayProvidedOtherFacility;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        displayProvidedOtherFacility = (displayProvidedOtherFacility == null) ? false : displayProvidedOtherFacility;

        Program program = new Program();

        program.setName( name );
        program.setDescription( description );
        program.setVersion( 1 );
        program.setDateOfEnrollmentDescription( dateOfEnrollmentDescription );
        program.setDateOfIncidentDescription( dateOfIncidentDescription );
        program.setMaxDaysAllowedInputData( maxDaysAllowedInputData );
        program.setType( type );
        program.setDisplayProvidedOtherFacility( displayProvidedOtherFacility );
        
        programService.saveProgram( program );

        if ( program.getType().equals( Program.SINGLE_EVENT_WITH_REGISTRATION ) || 
            program.getType().equals( Program.SINGLE_EVENT_WITHOUT_REGISTRATION ))
        {
            ProgramStage programStage = new ProgramStage();

            programStage.setName( SINGLE_EVENT + " " + name );
            programStage.setDescription( description );
            programStage.setStageInProgram( program.getProgramStages().size() + 1 );
            programStage.setProgram( program );
            programStage.setMinDaysFromStart( 0 );

            programStageService.saveProgramStage( programStage );
        }

        // ---------------------------------------------------------------------
        // create program-instance for anonymous program
        // ---------------------------------------------------------------------

        if ( program.getType().equals( Program.SINGLE_EVENT_WITHOUT_REGISTRATION ) )
        {
            // Add a new program-instance
            ProgramInstance programInstance = new ProgramInstance();
            programInstance.setEnrollmentDate( new Date() );
            programInstance.setDateOfIncident( new Date() );
            programInstance.setProgram( program );
            programInstance.setCompleted( false );

            programInstanceService.addProgramInstance( programInstance );
        }

        return SUCCESS;
    }
}