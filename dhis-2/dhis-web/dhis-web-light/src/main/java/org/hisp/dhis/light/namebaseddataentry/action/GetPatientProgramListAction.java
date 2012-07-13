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

package org.hisp.dhis.light.namebaseddataentry.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hisp.dhis.light.utils.NamebasedUtils;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientIdentifier;
import org.hisp.dhis.patient.PatientIdentifierService;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.relationship.Relationship;
import org.hisp.dhis.relationship.RelationshipService;
import org.hisp.dhis.relationship.RelationshipType;
import org.hisp.dhis.relationship.RelationshipTypeService;

import com.opensymphony.xwork2.Action;

public class GetPatientProgramListAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramInstanceService programInstanceService;

    public ProgramInstanceService getProgramInstanceService()
    {
        return programInstanceService;
    }

    public void setProgramInstanceService( ProgramInstanceService programInstanceService )
    {
        this.programInstanceService = programInstanceService;
    }
    
    private PatientIdentifierService patientIdentifierService;
    
    public PatientIdentifierService getPatientIdentifierService()
    {
        return patientIdentifierService;
    }

    public void setPatientIdentifierService( PatientIdentifierService patientIdentifierService )
    {
        this.patientIdentifierService = patientIdentifierService;
    }

    private ProgramService programService;

    public ProgramService getProgramService()
    {
        return programService;
    }

    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }

    private PatientService patientService;

    public PatientService getPatientService()
    {
        return patientService;
    }

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    private NamebasedUtils util;

    public NamebasedUtils getUtil()
    {
        return util;
    }

    public void setUtil( NamebasedUtils util )
    {
        this.util = util;
    }

    private RelationshipService relationshipService;

    public RelationshipService getRelationshipService()
    {
        return relationshipService;
    }

    public void setRelationshipService( RelationshipService relationshipService )
    {
        this.relationshipService = relationshipService;
    }
    
    public RelationshipTypeService relationshipTypeService;
    
    public RelationshipTypeService getRelationshipTypeService()
    {
        return relationshipTypeService;
    }

    public void setRelationshipTypeService( RelationshipTypeService relationshipTypeService )
    {
        this.relationshipTypeService = relationshipTypeService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
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

    private Set<ProgramInstance> programInstances = new HashSet<ProgramInstance>();

    public Set<ProgramInstance> getProgramInstances()
    {
        return programInstances;
    }

    public void setProgramInstances( Set<ProgramInstance> programInstances )
    {
        this.programInstances = programInstances;
    }

    private Patient patient;

    public Patient getPatient()
    {
        return patient;
    }

    public void setPatient( Patient patient )
    {
        this.patient = patient;
    }

    private List<Program> enrollmentProgramList;

    public List<Program> getEnrollmentProgramList()
    {
        return enrollmentProgramList;
    }

    public void setEnrollmentProgramList( List<Program> enrollmentProgramList )
    {
        this.enrollmentProgramList = enrollmentProgramList;
    }
    
    //Use for add relationship
    
    private List<Patient> relatedPeople;

    public List<Patient> getRelatedPeople()
    {
        return relatedPeople;
    }

    public void setRelatedPeople( List<Patient> relatedPeople )
    {
        this.relatedPeople = relatedPeople;
    }
    
    private Collection<RelationshipType> relationshipTypes;
    
    public Collection<RelationshipType> getRelationshipTypes()
    {
        return relationshipTypes;
    }

    public void setRelationshipTypes( Collection<RelationshipType> relationshipTypes )
    {
        this.relationshipTypes = relationshipTypes;
    }
    
    private Boolean validated;
    
    public Boolean getValidated()
    {
        return validated;
    }

    public void setValidated( Boolean validated )
    {
        this.validated = validated;
    }
    
    private Collection<PatientIdentifier> patientIdentifiers;
    
    public Collection<PatientIdentifier> getPatientIdentifiers()
    {
        return patientIdentifiers;
    }

    public void setPatientIdentifiers( Collection<PatientIdentifier> patientIdentifiers )
    {
        this.patientIdentifiers = patientIdentifiers;
    }

    @Override
    public String execute()
        throws Exception
    {
        programInstances.clear();
        relatedPeople = new ArrayList<Patient>();

        patient = patientService.getPatient( patientId );
        for ( ProgramInstance programInstance : programInstanceService.getProgramInstances( patient ) )
        {
            if ( !programInstance.getProgram().isSingleEvent() )
            {
                programInstances.add( programInstance );
            }
        }

        enrollmentProgramList = this.generateEnrollmentProgramList();
        Collection<Relationship> relationships = relationshipService.getRelationshipsForPatient( patient );

        for ( Relationship relationship : relationships )
        {
            if ( relationship.getPatientA().getId() != patient.getId() )
            {
                relatedPeople.add( relationship.getPatientA() );
            }

            if ( relationship.getPatientB().getId() != patient.getId() )
            {
                relatedPeople.add( relationship.getPatientB() );
            }
        }
        
        relationshipTypes = relationshipTypeService.getAllRelationshipTypes();
        patientIdentifiers = patientIdentifierService.getPatientIdentifiers( patient );
        return SUCCESS;
    }

    private List<Program> generateEnrollmentProgramList()
    {
        List<Program> programs = new ArrayList<Program>();
        for ( Program program : programService.getPrograms( patient.getOrganisationUnit() ) )

        {
            if ( !program.isSingleEvent() )
            {
                if ( programInstanceService.getProgramInstances( patient, program ).size() == 0 )
                {
                    programs.add( program );
                }
            }
        }
        return programs;
    }
}
