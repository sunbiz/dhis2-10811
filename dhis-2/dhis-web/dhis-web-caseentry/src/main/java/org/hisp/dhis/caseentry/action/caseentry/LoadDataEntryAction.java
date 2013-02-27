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

import static org.hisp.dhis.system.util.ValidationUtils.coordinateIsValid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.caseentry.state.SelectedStateManager;
import org.hisp.dhis.dataentryform.DataEntryForm;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientAttributeService;
import org.hisp.dhis.patientattributevalue.PatientAttributeValueService;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramDataEntryService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.ProgramStageSection;
import org.hisp.dhis.program.ProgramStageSectionService;
import org.hisp.dhis.program.comparator.ProgramStageDataElementSortOrderComparator;
import org.hisp.dhis.program.comparator.ProgramStageSectionSortOrderComparator;
import org.hisp.dhis.system.util.ValidationUtils;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version $ LoadDataEntryAction.java May 7, 2011 2:37:44 PM $
 * 
 */
public class LoadDataEntryAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramDataEntryService programDataEntryService;

    private PatientDataValueService patientDataValueService;

    private ProgramStageInstanceService programStageInstanceService;

    private SelectedStateManager selectedStateManager;

    private PatientAttributeService patientAttributeService;

    private PatientAttributeValueService patientAttributeValueService;

    private ProgramStageSectionService programStageSectionService;

    private I18nFormat format;

    // -------------------------------------------------------------------------
    // Input && Output
    // -------------------------------------------------------------------------

    private Integer programStageInstanceId;

    private ProgramStageInstance programStageInstance;

    private String customDataEntryFormCode;

    private I18n i18n;

    private List<ProgramStageDataElement> programStageDataElements = new ArrayList<ProgramStageDataElement>();

    private Map<Integer, PatientDataValue> patientDataValueMap;

    private OrganisationUnit organisationUnit;

    private Program program;

    private ProgramStage programStage;

    private List<ProgramStageSection> sections = new ArrayList<ProgramStageSection>();

    private Map<String, Double> calAttributeValueMap = new HashMap<String, Double>();

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    public void setProgramStageSectionService( ProgramStageSectionService programStageSectionService )
    {
        this.programStageSectionService = programStageSectionService;
    }

    public void setPatientAttributeService( PatientAttributeService patientAttributeService )
    {
        this.patientAttributeService = patientAttributeService;
    }

    public void setPatientAttributeValueService( PatientAttributeValueService patientAttributeValueService )
    {
        this.patientAttributeValueService = patientAttributeValueService;
    }

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    public void setProgramStageInstanceId( Integer programStageInstanceId )
    {
        this.programStageInstanceId = programStageInstanceId;
    }

    public List<ProgramStageSection> getSections()
    {
        return sections;
    }

    public Program getProgram()
    {
        return program;
    }

    public ProgramStage getProgramStage()
    {
        return programStage;
    }

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    public void setProgramDataEntryService( ProgramDataEntryService programDataEntryService )
    {
        this.programDataEntryService = programDataEntryService;
    }

    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    public ProgramStageInstance getProgramStageInstance()
    {
        return programStageInstance;
    }

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    public String getCustomDataEntryFormCode()
    {
        return customDataEntryFormCode;
    }

    public List<ProgramStageDataElement> getProgramStageDataElements()
    {
        return programStageDataElements;
    }

    public Map<Integer, PatientDataValue> getPatientDataValueMap()
    {
        return patientDataValueMap;
    }

    private String visitor;

    public String getVisitor()
    {
        return visitor;
    }

    private Patient patient;

    public Patient getPatient()
    {
        return patient;
    }

    public Map<String, Double> getCalAttributeValueMap()
    {
        return calAttributeValueMap;
    }
    
    private String longitude;

    public String getLongitude()
    {
        return longitude;
    }

    private String latitude;

    public String getLatitude()
    {
        return latitude;
    }

    // -------------------------------------------------------------------------
    // Implementation Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        organisationUnit = selectedStateManager.getSelectedOrganisationUnit();

        // ---------------------------------------------------------------------
        // Get program-stage-instance
        // ---------------------------------------------------------------------

        if ( programStageInstanceId != null )
        {
            programStageInstance = programStageInstanceService.getProgramStageInstance( programStageInstanceId );

            program = programStageInstance.getProgramStage().getProgram();

            programStage = programStageInstance.getProgramStage();

            selectedStateManager.setSelectedProgramStageInstance( programStageInstance );

            if ( program.isRegistration() )
            {
                patient = programStageInstance.getProgramInstance().getPatient();

                Collection<PatientAttribute> calAttributes = patientAttributeService
                    .getPatientAttributesByValueType( PatientAttribute.TYPE_CALCULATED );

                for ( PatientAttribute calAttribute : calAttributes )
                {
                    Double value = patientAttributeValueService.getCalculatedPatientAttributeValue( patient,
                        calAttribute, format );
                    if ( value != null )
                    {
                        calAttributeValueMap.put( calAttribute.getName(), value );
                    }
                }
            }

            // ---------------------------------------------------------------------
            // Get data values
            // ---------------------------------------------------------------------

            programStageDataElements = new ArrayList<ProgramStageDataElement>( programStageInstance.getProgramStage()
                .getProgramStageDataElements() );

            Collections.sort( programStageDataElements, new ProgramStageDataElementSortOrderComparator() );

            Collection<PatientDataValue> patientDataValues = patientDataValueService
                .getPatientDataValues( programStageInstance );

            patientDataValueMap = new HashMap<Integer, PatientDataValue>( patientDataValues.size() );

            for ( PatientDataValue patientDataValue : patientDataValues )
            {
                int key = patientDataValue.getDataElement().getId();
                patientDataValueMap.put( key, patientDataValue );
            }

            // ---------------------------------------------------------------------
            // Get data-entry-form
            // ---------------------------------------------------------------------

            DataEntryForm dataEntryForm = programStageInstance.getProgramStage().getDataEntryForm();

            if ( programStage.getDataEntryType().equals( ProgramStage.TYPE_CUSTOM ) )
            {
                Boolean disabled = (program.getDisplayProvidedOtherFacility() == null) ? true : !program
                    .getDisplayProvidedOtherFacility();
                customDataEntryFormCode = programDataEntryService.prepareDataEntryFormForEntry(
                    dataEntryForm.getHtmlCode(), patientDataValues, disabled.toString(), i18n,
                    programStageInstance.getProgramStage(), programStageInstance, organisationUnit );
            }
            else if ( programStage.getDataEntryType().equals( ProgramStage.TYPE_SECTION ) )
            {
                sections = new ArrayList<ProgramStageSection>(
                    programStageSectionService.getProgramStages( programStage ) );

                Collections.sort( sections, new ProgramStageSectionSortOrderComparator() );
            }

            // -----------------------------------------------------------------
            // Allow update only if org unit does not have polygon coordinates
            // -----------------------------------------------------------------

            longitude = ValidationUtils.getLongitude( programStageInstance.getCoordinates() );
            latitude = ValidationUtils.getLatitude( programStageInstance.getCoordinates() );
        }

        return SUCCESS;
    }

}
