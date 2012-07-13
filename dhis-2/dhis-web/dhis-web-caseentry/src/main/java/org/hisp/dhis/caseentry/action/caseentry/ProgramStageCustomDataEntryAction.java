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

package org.hisp.dhis.caseentry.action.caseentry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.caseentry.state.SelectedStateManager;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataentryform.DataEntryForm;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientIdentifier;
import org.hisp.dhis.patient.PatientIdentifierService;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramDataEntryService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.comparator.ProgramStageDataElementSortOrderComparator;

import com.opensymphony.xwork2.Action;

public class ProgramStageCustomDataEntryAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    private PatientIdentifierService patientIdentifierService;

    public void setPatientIdentifierService( PatientIdentifierService patientIdentifierService )
    {
        this.patientIdentifierService = patientIdentifierService;
    }

    private PatientDataValueService patientDataValueService;

    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    private ProgramDataEntryService programDataEntryService;

    public void setProgramDataEntryService( ProgramDataEntryService programDataEntryService )
    {
        this.programDataEntryService = programDataEntryService;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String customDataEntryFormCode = null;

    public String getCustomDataEntryFormCode()
    {
        return this.customDataEntryFormCode;
    }

    // -------------------------------------------------------------------------
    // Input / Output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    private Integer programStageInstanceId;

    public void setProgramStageInstanceId( Integer programStageInstanceId )
    {
        this.programStageInstanceId = programStageInstanceId;
    }

    private Patient patient;

    public Patient getPatient()
    {
        return patient;
    }

    private PatientIdentifier patientIdentifier;

    public PatientIdentifier getPatientIdentifier()
    {
        return patientIdentifier;
    }

    private Program program;

    public Program getProgram()
    {
        return program;
    }

    private ProgramStage programStage;

    public ProgramStage getProgramStage()
    {
        return programStage;
    }

    private Collection<DataElement> dataElements = new ArrayList<DataElement>();

    public Collection<DataElement> getDataElements()
    {
        return dataElements;
    }
    
    private Map<Integer, PatientDataValue> patientDataValueMap;

    public Map<Integer, PatientDataValue> getPatientDataValueMap()
    {
        return patientDataValueMap;
    }

    private OrganisationUnit organisationUnit;

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    private DataEntryForm dataEntryForm;

    public DataEntryForm getDataEntryForm()
    {
        return this.dataEntryForm;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    private boolean customDataEntryFormExists;

    public boolean isCustomDataEntryFormExists()
    {
        return customDataEntryFormExists;
    }

    public void setCustomDataEntryFormExists( boolean customDataEntryFormExists )
    {
        this.customDataEntryFormExists = customDataEntryFormExists;
    }

    public ProgramStageInstance programStageInstance;

    public int getProgramStageInstanceId()
    {
        return programStageInstance.getId();
    }

    private List<ProgramStageDataElement> programStageDataElements;

    public List<ProgramStageDataElement> getProgramStageDataElements()
    {
        return programStageDataElements;
    }

    // -------------------------------------------------------------------------
    // Implementation Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Get Orgunit & Program, ProgramStage
        // ---------------------------------------------------------------------

        organisationUnit = selectedStateManager.getSelectedOrganisationUnit();

        programStageInstance = programStageInstanceService.getProgramStageInstance( programStageInstanceId );

        programStage = programStageInstance.getProgramStage();

        programStageDataElements = new ArrayList<ProgramStageDataElement>( programStage.getProgramStageDataElements() );

        Collections.sort( programStageDataElements, new ProgramStageDataElementSortOrderComparator() );

        program = programStage.getProgram();

        patient = programStageInstance.getProgramInstance().getPatient();

        if ( patient != null )
        {
            patientIdentifier = patientIdentifierService.getPatientIdentifier( patient );
        }

        selectedStateManager.setSelectedProgramStageInstance( programStageInstance );

        selectedStateManager.setSelectedProgramStageInstance( programStageInstance );
        
        // ---------------------------------------------------------------------
        // Get data values
        // ---------------------------------------------------------------------

        Collection<PatientDataValue> patientDataValues = patientDataValueService
            .getPatientDataValues( programStageInstance );

        patientDataValueMap = new HashMap<Integer, PatientDataValue>( patientDataValues.size() );

        for ( PatientDataValue patientDataValue : patientDataValues )
        {
            int key = patientDataValue.getDataElement().getId() ;
            patientDataValueMap.put( key, patientDataValue );
        }

        DataEntryForm dataEntryForm = programStage.getDataEntryForm();

        if ( dataEntryForm != null )
        {
            customDataEntryFormExists = true;

            customDataEntryFormCode = programDataEntryService.prepareDataEntryFormForEntry(
                dataEntryForm.getHtmlCode(), patientDataValues, "", i18n, programStage, programStageInstance,
                organisationUnit );
        }

        return SUCCESS;
    }

}
