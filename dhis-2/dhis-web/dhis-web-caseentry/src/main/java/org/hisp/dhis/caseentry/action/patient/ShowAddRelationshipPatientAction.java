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

package org.hisp.dhis.caseentry.action.patient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientAttributeGroup;
import org.hisp.dhis.patient.PatientAttributeGroupService;
import org.hisp.dhis.patient.PatientAttributeService;
import org.hisp.dhis.patient.PatientIdentifier;
import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.patient.PatientIdentifierTypeService;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.patient.comparator.PatientAttributeGroupSortOrderComparator;
import org.hisp.dhis.patientattributevalue.PatientAttributeValue;
import org.hisp.dhis.patientattributevalue.PatientAttributeValueService;
import org.hisp.dhis.relationship.RelationshipType;
import org.hisp.dhis.relationship.RelationshipTypeService;

import com.opensymphony.xwork2.Action;

/**
 * @author Viet
 * 
 * @version $Id$
 */
public class ShowAddRelationshipPatientAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PatientService patientService;

    private PatientAttributeService patientAttributeService;

    private PatientAttributeGroupService patientAttributeGroupService;

    private PatientIdentifierTypeService patientIdentifierTypeService;

    private RelationshipTypeService relationshipTypeService;

    private PatientAttributeValueService patientAttributeValueService;

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private Integer id;

    private Collection<PatientAttribute> noGroupAttributes;

    private List<PatientAttributeGroup> attributeGroups;

    private Collection<PatientIdentifierType> identifierTypes;

    private Collection<RelationshipType> relationshipTypes;

    private Patient patient;

    private Map<Integer, String> identiferMap;

    private Map<Integer, String> attributeMap;

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        patient = patientService.getPatient( id.intValue() );

        identifierTypes = patientIdentifierTypeService.getAllPatientIdentifierTypes();

        noGroupAttributes = new ArrayList<PatientAttribute>( patientAttributeService.getPatientAttributes( null, null ) );

        relationshipTypes = relationshipTypeService.getAllRelationshipTypes();

        identiferMap = new HashMap<Integer, String>();

        for ( PatientIdentifier identifier : patient.getIdentifiers() )
        {
            if ( identifier.getIdentifierType() != null )
                identiferMap.put( identifier.getIdentifierType().getId(), identifier.getIdentifier() );
        }

        // -------------------------------------------------------------------------
        // Get patient-attribute values
        // -------------------------------------------------------------------------

        attributeGroups = new ArrayList<PatientAttributeGroup>( patientAttributeGroupService
            .getPatientAttributeGroupsWithoutProgram() );
        Collections.sort( attributeGroups, new PatientAttributeGroupSortOrderComparator() );

        noGroupAttributes = patientAttributeService.getPatientAttributes( null, null );

        Collection<PatientAttributeValue> patientAttributeValues = patientAttributeValueService
            .getPatientAttributeValuesWithoutProgram( patient );

        for ( PatientAttributeValue patientAttributeValue : patientAttributeValues )
        {
            if ( PatientAttribute.TYPE_COMBO.equalsIgnoreCase( patientAttributeValue.getPatientAttribute()
                .getValueType() ) )
            {
                attributeMap.put( patientAttributeValue.getPatientAttribute().getId(), patientAttributeValue
                    .getPatientAttributeOption().getName() );
            }
            else
            {
                attributeMap
                    .put( patientAttributeValue.getPatientAttribute().getId(), patientAttributeValue.getValue() );
            }
        }

        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Getter/Setter
    // -------------------------------------------------------------------------

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Collection<PatientIdentifierType> getIdentifierTypes()
    {
        return identifierTypes;
    }

    public Collection<PatientAttributeGroup> getAttributeGroups()
    {
        return attributeGroups;
    }

    public Collection<PatientAttribute> getNoGroupAttributes()
    {
        return noGroupAttributes;
    }

    public void setRelationshipTypeService( RelationshipTypeService relationshipTypeService )
    {
        this.relationshipTypeService = relationshipTypeService;
    }

    public Collection<RelationshipType> getRelationshipTypes()
    {
        return relationshipTypes;
    }

    public Patient getPatient()
    {
        return patient;
    }

    public Map<Integer, String> getIdentiferMap()
    {
        return identiferMap;
    }

    public void setPatientIdentifierTypeService( PatientIdentifierTypeService patientIdentifierTypeService )
    {
        this.patientIdentifierTypeService = patientIdentifierTypeService;
    }

    public void setPatientAttributeService( PatientAttributeService patientAttributeService )
    {
        this.patientAttributeService = patientAttributeService;
    }

    public void setPatientAttributeGroupService( PatientAttributeGroupService patientAttributeGroupService )
    {
        this.patientAttributeGroupService = patientAttributeGroupService;
    }

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    public Map<Integer, String> getAttributeMap()
    {
        return attributeMap;
    }

    public void setPatientAttributeValueService( PatientAttributeValueService patientAttributeValueService )
    {
        this.patientAttributeValueService = patientAttributeValueService;
    }
}
