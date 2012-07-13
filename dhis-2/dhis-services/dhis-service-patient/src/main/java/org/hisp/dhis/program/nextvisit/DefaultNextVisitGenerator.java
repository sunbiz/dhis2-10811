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
package org.hisp.dhis.program.nextvisit;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class DefaultNextVisitGenerator
    implements NextVisitGenerator
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

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
    // Implementation methods
    // -------------------------------------------------------------------------

    public Map<Patient, Set<ProgramStageInstance>> getNextVisits( Collection<ProgramInstance> programInstances )
    {
        Map<Patient, Set<ProgramStageInstance>> visitsByPatients = new HashMap<Patient, Set<ProgramStageInstance>>();

        Set<ProgramStageInstance> programStageInstances = new HashSet<ProgramStageInstance>();

        // -----------------------------------------------------------------
        // Initially assume to have a first visit for all programInstances
        // -----------------------------------------------------------------

        Map<Integer, Integer> visitsByProgramInstances = new HashMap<Integer, Integer>();

        for ( ProgramInstance programInstance : programInstances )
        {
            programStageInstances.addAll( programInstance.getProgramStageInstances() );

            visitsByProgramInstances.put( programInstance.getId(), 0 );
        }

        // -----------------------------------------------------------------
        // For each of these active instances, see at which stage they are
        // currently (may not necessarily be at the first stage)
        // -----------------------------------------------------------------

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

        // -----------------------------------------------------------------
        // For each of these active instances, based on the current stage
        // determine the next stage
        // -----------------------------------------------------------------

        for ( ProgramInstance programInstance : programInstances )
        {
            Program program = programInstance.getProgram();

            ProgramStage nextStage = program.getProgramStageByStage( visitsByProgramInstances.get( programInstance
                .getId() ) + 1 );

            if ( nextStage != null )
            {
                ProgramStageInstance nextStageInstance = programStageInstanceService.getProgramStageInstance(
                    programInstance, nextStage );

                if ( visitsByPatients.containsKey( programInstance.getPatient() ) )
                {
                    visitsByPatients.get( programInstance.getPatient() ).add( nextStageInstance );
                }
                else
                {
                    Set<ProgramStageInstance> programStageInstancess = new HashSet<ProgramStageInstance>();

                    programStageInstancess.add( nextStageInstance );

                    visitsByPatients.put( programInstance.getPatient(), programStageInstancess );
                }
            }
        }

        return visitsByPatients;
    }
}
