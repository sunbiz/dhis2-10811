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

package org.hisp.dhis.caseentry.action.caseentry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.dataentryform.DataEntryForm;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientAttributeGroup;
import org.hisp.dhis.patient.PatientAttributeGroupService;
import org.hisp.dhis.patient.PatientAttributeService;
import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.patient.PatientIdentifierTypeService;
import org.hisp.dhis.patient.comparator.PatientAttributeGroupSortOrderComparator;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramDataEntryService;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageDataElement;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * 
 * @version $ShowEventWithRegistrationFormAction.java Jun 26, 2012 4:41:09 PM$
 */
public class ShowEventWithRegistrationFormAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitSelectionManager selectionManager;

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    private PatientAttributeService patientAttributeService;

    public void setPatientAttributeService( PatientAttributeService patientAttributeService )
    {
        this.patientAttributeService = patientAttributeService;
    }

    private PatientAttributeGroupService patientAttributeGroupService;

    public void setPatientAttributeGroupService( PatientAttributeGroupService patientAttributeGroupService )
    {
        this.patientAttributeGroupService = patientAttributeGroupService;
    }

    private PatientIdentifierTypeService patientIdentifierTypeService;

    public void setPatientIdentifierTypeService( PatientIdentifierTypeService patientIdentifierTypeService )
    {
        this.patientIdentifierTypeService = patientIdentifierTypeService;
    }

    private ProgramService programService;

    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }

    private ProgramDataEntryService programDataEntryService;

    public void setProgramDataEntryService( ProgramDataEntryService programDataEntryService )
    {
        this.programDataEntryService = programDataEntryService;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private Integer programId;

    private Collection<PatientAttribute> noGroupAttributes;

    private List<PatientAttributeGroup> attributeGroups;

    private Collection<PatientIdentifierType> identifierTypes;

    private OrganisationUnit organisationUnit;

    private String customDataEntryFormCode;

    private List<ProgramStageDataElement> programStageDataElements = new ArrayList<ProgramStageDataElement>();

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        identifierTypes = patientIdentifierTypeService.getPatientIdentifierTypesWithoutProgram();

        noGroupAttributes = patientAttributeService.getPatientAttributes( null, null );

        attributeGroups = new ArrayList<PatientAttributeGroup>( patientAttributeGroupService
            .getPatientAttributeGroupsWithoutProgram() );
        Collections.sort( attributeGroups, new PatientAttributeGroupSortOrderComparator() );

        organisationUnit = selectionManager.getSelectedOrganisationUnit();

        // Get data entry form

        Program program = programService.getProgram( programId );

        ProgramStage programStage = program.getProgramStages().iterator().next();

        DataEntryForm dataEntryForm = programStage.getDataEntryForm();

        if ( dataEntryForm != null )
        {
            customDataEntryFormCode = programDataEntryService.prepareDataEntryFormForEdit( dataEntryForm.getHtmlCode() );
        }
        else
        {
            programStageDataElements = new ArrayList<ProgramStageDataElement>( programStage
                .getProgramStageDataElements() );
        }

        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Getter/Setter
    // -------------------------------------------------------------------------

    public Collection<PatientIdentifierType> getIdentifierTypes()
    {
        return identifierTypes;
    }

    public List<PatientAttributeGroup> getAttributeGroups()
    {
        return attributeGroups;
    }

    public void setProgramId( Integer programId )
    {
        this.programId = programId;
    }

    public Collection<PatientAttribute> getNoGroupAttributes()
    {
        return noGroupAttributes;
    }

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    public String getCustomDataEntryFormCode()
    {
        return customDataEntryFormCode;
    }

    public List<ProgramStageDataElement> getProgramStageDataElements()
    {
        return programStageDataElements;
    }
}
