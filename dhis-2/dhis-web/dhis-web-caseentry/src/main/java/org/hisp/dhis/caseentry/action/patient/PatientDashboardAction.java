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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientAttributeService;
import org.hisp.dhis.patient.PatientAudit;
import org.hisp.dhis.patient.PatientAuditService;
import org.hisp.dhis.patient.PatientIdentifier;
import org.hisp.dhis.patient.PatientIdentifierTypeService;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.patientattributevalue.PatientAttributeValue;
import org.hisp.dhis.patientattributevalue.PatientAttributeValueService;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.relationship.Relationship;
import org.hisp.dhis.relationship.RelationshipService;
import org.hisp.dhis.relationship.RelationshipTypeService;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * 
 * @version PatientDashboardAction.java 1:30:29 PM Aug 10, 2012 $
 */
public class PatientDashboardAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PatientService patientService;

    private PatientAttributeValueService patientAttributeValueService;

    private RelationshipService relationshipService;

    private ProgramInstanceService programInstanceService;

    private PatientAuditService patientAuditService;

    private CurrentUserService currentUserService;

    private PatientAttributeService patientAttributeService;

    private PatientIdentifierTypeService identifierTypeService;

    private ProgramService programService;

    private RelationshipTypeService relationshipTypeService;

    private I18nFormat format;

    // -------------------------------------------------------------------------
    // Input && Output
    // -------------------------------------------------------------------------

    private Integer patientId;

    private Patient patient;

    private Set<PatientIdentifier> identifiers;

    private Collection<PatientAttributeValue> attributeValues;

    private Collection<Relationship> relationship;

    private Collection<ProgramInstance> activeProgramInstances;

    private Collection<ProgramInstance> completedProgramInstances;

    private Collection<PatientAudit> patientAudits;

    private Map<String, String> attributeMap = new HashMap<String, String>();

    private Map<String, String> identifierMap = new HashMap<String, String>();

    private Map<Integer, String> programMap = new HashMap<Integer, String>();

    private Map<String, Relationship> relationshipMap = new HashMap<String, Relationship>();

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public void setPatientAuditService( PatientAuditService patientAuditService )
    {
        this.patientAuditService = patientAuditService;
    }

    public void setIdentifierTypeService( PatientIdentifierTypeService identifierTypeService )
    {
        this.identifierTypeService = identifierTypeService;
    }

    public Map<String, String> getAttributeMap()
    {
        return attributeMap;
    }

    public Map<String, String> getIdentifierMap()
    {
        return identifierMap;
    }

    public Map<Integer, String> getProgramMap()
    {
        return programMap;
    }

    public Map<String, Relationship> getRelationshipMap()
    {
        return relationshipMap;
    }

    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }

    public void setRelationshipTypeService( RelationshipTypeService relationshipTypeService )
    {
        this.relationshipTypeService = relationshipTypeService;
    }

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    public void setAttributeValues( Collection<PatientAttributeValue> attributeValues )
    {
        this.attributeValues = attributeValues;
    }

    public void setActiveProgramInstances( Collection<ProgramInstance> activeProgramInstances )
    {
        this.activeProgramInstances = activeProgramInstances;
    }

    public void setPatientAttributeService( PatientAttributeService patientAttributeService )
    {
        this.patientAttributeService = patientAttributeService;
    }

    public void setPatientAttributeValueService( PatientAttributeValueService patientAttributeValueService )
    {
        this.patientAttributeValueService = patientAttributeValueService;
    }

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    public Collection<ProgramInstance> getActiveProgramInstances()
    {
        return activeProgramInstances;
    }

    public Collection<PatientAudit> getPatientAudits()
    {
        return patientAudits;
    }

    public Collection<ProgramInstance> getCompletedProgramInstances()
    {
        return completedProgramInstances;
    }

    public void setRelationshipService( RelationshipService relationshipService )
    {
        this.relationshipService = relationshipService;
    }

    public void setProgramInstanceService( ProgramInstanceService programInstanceService )
    {
        this.programInstanceService = programInstanceService;
    }

    public Patient getPatient()
    {
        return patient;
    }

    public Set<PatientIdentifier> getIdentifiers()
    {
        return identifiers;
    }

    public Collection<PatientAttributeValue> getAttributeValues()
    {
        return attributeValues;
    }

    public Collection<Relationship> getRelationship()
    {
        return relationship;
    }

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    public void setPatientId( Integer patientId )
    {
        this.patientId = patientId;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        patient = patientService.getPatient( patientId );

        // ---------------------------------------------------------------------
        // Get patient-attribute-values
        // ---------------------------------------------------------------------

        attributeValues = patientAttributeValueService.getPatientAttributeValues( patient );

        for ( PatientAttributeValue attributeValue : attributeValues )
        {
            Integer id = attributeValue.getPatientAttribute().getId();
            attributeMap.put( patientAttributeService.getPatientAttribute( id ).getDisplayName(),
                attributeValue.getValue() );
        }

        Collection<PatientAttribute> calAttributes = patientAttributeService
            .getPatientAttributesByValueType( PatientAttribute.TYPE_CALCULATED );

        for ( PatientAttribute calAttribute : calAttributes )
        {
            Double value = patientAttributeValueService.getCalculatedPatientAttributeValue( patient, calAttribute,
                format );
            if ( value != null )
            {
                attributeMap.put( calAttribute.getDisplayName(), value + "" );
            }
        }

        // ---------------------------------------------------------------------
        // Get patient-identifiers
        // ---------------------------------------------------------------------

        identifiers = patient.getIdentifiers();

        for ( PatientIdentifier identifier : identifiers )
        {
            if ( identifier.getIdentifierType() != null )
            {
                identifierMap.put(
                    identifierTypeService.getPatientIdentifierType( identifier.getIdentifierType().getId() )
                        .getDisplayName(), identifier.getIdentifier() );
            }
            else
            {
                identifierMap.put( null, identifier.getIdentifier() );
            }
        }

        // ---------------------------------------------------------------------
        // Get relationship
        // ---------------------------------------------------------------------

        Collection<Relationship> relationships = relationshipService.getRelationshipsForPatient( patient );

        for ( Relationship relationship : relationships )
        {
            relationshipMap.put( relationshipTypeService.getRelationshipType( relationship.getId() ).getDisplayName(),
                relationship );
        }

        Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( patient );

        activeProgramInstances = new HashSet<ProgramInstance>();

        completedProgramInstances = new HashSet<ProgramInstance>();

        for ( ProgramInstance programInstance : programInstances )
        {
            if ( programInstance.isCompleted() )
            {
                completedProgramInstances.add( programInstance );
            }
            else
            {
                activeProgramInstances.add( programInstance );
            }

            Integer programId = programInstance.getProgram().getId();
            if ( !programMap.containsKey( programId ) )
            {
                programMap.put( programId, programService.getProgram( programId ).getDisplayName() );
            }
        }

        // ---------------------------------------------------------------------
        // Patient-Audit
        // ---------------------------------------------------------------------

        patientAudits = patientAuditService.getPatientAudits( patient );

        long millisInDay = 60 * 60 * 24 * 1000;
        long currentTime = new Date().getTime();
        long dateOnly = (currentTime / millisInDay) * millisInDay;
        Date date = new Date( dateOnly );
        String visitor = currentUserService.getCurrentUsername();
        PatientAudit patientAudit = patientAuditService.getPatientAudit( patient, visitor, date );
        if ( patientAudit == null )
        {
            patientAudit = new PatientAudit( patient, date, visitor );
            patientAuditService.savePatientAudit( patientAudit );
        }

        return SUCCESS;
    }
}
