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
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.patientattributevalue.PatientAttributeValue;
import org.hisp.dhis.patientattributevalue.PatientAttributeValueService;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.relationship.Relationship;
import org.hisp.dhis.relationship.RelationshipService;
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

    private I18nFormat format;

    // -------------------------------------------------------------------------
    // Input && Output
    // -------------------------------------------------------------------------

    private Integer patientId;

    private Patient patient;

    private Set<PatientIdentifier> identifiers;

    private Collection<PatientAttributeValue> attributeValues;

    private Collection<ProgramInstance> activeProgramInstances;

    private Collection<ProgramInstance> completedProgramInstances;

    private Collection<PatientAudit> patientAudits;

    private Map<PatientAttribute, String> attributeMap = new HashMap<PatientAttribute, String>();

    private Collection<Relationship> relationships = new HashSet<Relationship>();

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public void setPatientAuditService( PatientAuditService patientAuditService )
    {
        this.patientAuditService = patientAuditService;
    }

    public Map<PatientAttribute, String> getAttributeMap()
    {
        return attributeMap;
    }

    public void setFormat( I18nFormat format )
    {
        this.format = format;
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

    public Collection<Relationship> getRelationships()
    {
        return relationships;
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

        Collection<PatientAttribute> calAttributes = patientAttributeService
            .getPatientAttributesByValueType( PatientAttribute.TYPE_CALCULATED );

        for ( PatientAttribute calAttribute : calAttributes )
        {
            Double value = patientAttributeValueService.getCalculatedPatientAttributeValue( patient, calAttribute,
                format );
            if ( value != null )
            {
                attributeMap.put( calAttribute, value + "" );
            }
        }

        // ---------------------------------------------------------------------
        // Get patient-identifiers
        // ---------------------------------------------------------------------

        identifiers = patient.getIdentifiers();

        // ---------------------------------------------------------------------
        // Get relationship
        // ---------------------------------------------------------------------

        relationships = relationshipService.getRelationshipsForPatient( patient );

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
        PatientAudit patientAudit = patientAuditService.getPatientAudit( patientId, visitor, date,
            PatientAudit.MODULE_PATIENT_DASHBOARD );
        if ( patientAudit == null )
        {
            patientAudit = new PatientAudit( patient, visitor, date, PatientAudit.MODULE_PATIENT_DASHBOARD );
            patientAuditService.savePatientAudit( patientAudit );
        }

        return SUCCESS;
    }
}
