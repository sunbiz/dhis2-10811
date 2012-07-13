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

import static org.hisp.dhis.program.ProgramValidation.AFTER_CURRENT_DATE;
import static org.hisp.dhis.program.ProgramValidation.AFTER_DUE_DATE;
import static org.hisp.dhis.program.ProgramValidation.AFTER_OR_EQUALS_TO_CURRENT_DATE;
import static org.hisp.dhis.program.ProgramValidation.AFTER_OR_EQUALS_TO_DUE_DATE;
import static org.hisp.dhis.program.ProgramValidation.BEFORE_CURRENT_DATE;
import static org.hisp.dhis.program.ProgramValidation.BEFORE_DUE_DATE;
import static org.hisp.dhis.program.ProgramValidation.BEFORE_DUE_DATE_PLUS_OR_MINUS_MAX_DAYS;
import static org.hisp.dhis.program.ProgramValidation.BEFORE_OR_EQUALS_TO_CURRENT_DATE;
import static org.hisp.dhis.program.ProgramValidation.BEFORE_OR_EQUALS_TO_DUE_DATE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.caseaggregation.CaseAggregationConditionService;
import org.hisp.dhis.caseentry.state.SelectedStateManager;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.ProgramValidation;
import org.hisp.dhis.program.ProgramValidationService;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version $ ValidateProgramInstanceAction.java Apr 28, 2011 10:56:10 AM $
 */
public class ValidateProgramInstanceAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SelectedStateManager selectedStateManager;

    private ProgramStageInstanceService programStageInstanceService;

    private PatientDataValueService patientDataValueService;

    private ProgramValidationService programValidationService;

    private CaseAggregationConditionService aggregationConditionService;

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private I18n i18n;

    private I18nFormat format;

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Map<DataElement, String> resultDEMultiStages;

    private List<ProgramValidation> programValidations;

    private Map<Integer, String> leftsideFormulaMap;

    private Map<Integer, String> rightsideFormulaMap;

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    public void setAggregationConditionService( CaseAggregationConditionService aggregationConditionService )
    {
        this.aggregationConditionService = aggregationConditionService;
    }

    public Map<Integer, String> getLeftsideFormulaMap()
    {
        return leftsideFormulaMap;
    }

    public Map<Integer, String> getRightsideFormulaMap()
    {
        return rightsideFormulaMap;
    }

    public List<ProgramValidation> getProgramValidations()
    {
        return programValidations;
    }

    public void setProgramValidationService( ProgramValidationService programValidationService )
    {
        this.programValidationService = programValidationService;
    }

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    public Map<DataElement, String> getResultDEMultiStages()
    {
        return resultDEMultiStages;
    }

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        resultDEMultiStages = new HashMap<DataElement, String>();

        programValidations = new ArrayList<ProgramValidation>();

        // ---------------------------------------------------------------------
        // Get selected objects
        // ---------------------------------------------------------------------

        ProgramStageInstance programStageInstance = selectedStateManager.getSelectedProgramStageInstance();

        ProgramStage programStage = programStageInstance.getProgramStage();

        // ---------------------------------------------------------------------
        // Get selected objects
        // ---------------------------------------------------------------------

        Set<ProgramStageDataElement> dataElements = programStage.getProgramStageDataElements();

        for ( ProgramStageDataElement psDataElement : dataElements )
        {
            DataElement dataElement = psDataElement.getDataElement();

            checkDataElementInMultiStage( programStageInstance, dataElement );
        }

        // ---------------------------------------------------------------------
        // Check validations for dataelement into multi-stages
        // ---------------------------------------------------------------------

        runProgramValidation( programValidationService.getProgramValidation( programStageInstance.getProgramStage() ),
            programStageInstance );

        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Support method
    // -------------------------------------------------------------------------

    /**
     * ------------------------------------------------------------------------
     * // Check value of the dataElment into previous. // If the value
     * exists,allow users to enter data of // the dataElement into the
     * programStageInstance // Else, disable Input-field of the dataElement
     * ------------------------------------------------------------------------
     **/

    private void checkDataElementInMultiStage( ProgramStageInstance programStageInstance,
        DataElement dataElement )
    {
        ProgramInstance programInstance = programStageInstance.getProgramInstance();
        List<ProgramStage> stages = new ArrayList<ProgramStage>( programInstance.getProgram().getProgramStages() );

        int index = programStageInstance.getStageInProgram();

        if ( index > 0 )
        {
            ProgramStage prevStage = stages.get( index - 1 );
            ProgramStageInstance prevStageInstance = programStageInstanceService.getProgramStageInstance(
                programInstance, prevStage );
            PatientDataValue prevValue = patientDataValueService.getPatientDataValue( prevStageInstance, dataElement );

            if ( prevValue == null )
            {
                String message = i18n.getString( "selected" ) + " " + i18n.getString( "program_stage" ) + " "
                    + i18n.getString( "should" ) + " " + i18n.getString( "data_value" ) + " "
                    + i18n.getString( "is_null" );

                resultDEMultiStages.put( dataElement, message );
            }
        }

    }

    private void runProgramValidation( Collection<ProgramValidation> validations,
        ProgramStageInstance programStageInstance )
    {
        if ( validations != null )
        {
            for ( ProgramValidation validation : validations )
            {
                boolean valid = programValidationService.runValidation( validation, programStageInstance, format );

                if ( !valid )
                {
                    programValidations.add( validation );
                }
            }
        }

        if ( !programValidations.isEmpty() )
        {
            leftsideFormulaMap = new HashMap<Integer, String>( programValidations.size() );
            rightsideFormulaMap = new HashMap<Integer, String>( programValidations.size() );

            for ( ProgramValidation validation : programValidations )
            {
                leftsideFormulaMap.put( validation.getId(), aggregationConditionService
                    .getConditionDescription( validation.getLeftSide() ) );

                if ( validation.getDateType() )
                {
                    String rightSide = validation.getRightSide();
                    int index = rightSide.indexOf( 'D' );
                    if ( index < 0 )
                    {
                        int rightValidation = Integer.parseInt( rightSide );

                        switch ( rightValidation )
                        {
                        case BEFORE_CURRENT_DATE:
                            rightsideFormulaMap.put( validation.getId(), i18n.getString( "before_current_date" ) );
                            break;
                        case BEFORE_OR_EQUALS_TO_CURRENT_DATE:
                            rightsideFormulaMap.put( validation.getId(), i18n
                                .getString( "before_or_equals_to_current_date" ) );
                            break;
                        case AFTER_CURRENT_DATE:
                            rightsideFormulaMap.put( validation.getId(), i18n.getString( "after_current_date" ) );
                            break;
                        case AFTER_OR_EQUALS_TO_CURRENT_DATE:
                            rightsideFormulaMap.put( validation.getId(), i18n
                                .getString( "after_or_equals_to_current_date" ) );
                            break;
                        case BEFORE_DUE_DATE:
                            rightsideFormulaMap.put( validation.getId(), i18n.getString( "before_due_date" ) );
                            break;
                        case BEFORE_OR_EQUALS_TO_DUE_DATE:
                            rightsideFormulaMap.put( validation.getId(), i18n
                                .getString( "before_or_equals_to_due_date" ) );
                            break;
                        case AFTER_DUE_DATE:
                            rightsideFormulaMap.put( validation.getId(), i18n.getString( "after_due_date" ) );
                            break;
                        case AFTER_OR_EQUALS_TO_DUE_DATE:
                            rightsideFormulaMap
                                .put( validation.getId(), i18n.getString( "after_or_equals_to_due_date" ) );
                            break;
                        default:
                            rightsideFormulaMap.put( validation.getId(), "" );
                            break;

                        }
                    }
                    else
                    {

                        int rightValidation = Integer.parseInt( rightSide.substring( 0, index ) );

                        int daysValue = Integer.parseInt( rightSide.substring( index + 1, rightSide.length() ) );

                        if ( rightValidation == BEFORE_DUE_DATE_PLUS_OR_MINUS_MAX_DAYS )
                        {
                            rightsideFormulaMap.put( validation.getId(), i18n
                                .getString( "in_range_due_date_plus_or_minus" )
                                + " " + daysValue + i18n.getString( "days" ) );
                        }
                    }
                }
                else if ( validation.getRightSide().equals( "1==1" ) )
                {
                    rightsideFormulaMap.put( validation.getId(), "" );
                }
                else
                {
                    rightsideFormulaMap.put( validation.getId(), aggregationConditionService
                        .getConditionDescription( validation.getRightSide() ) );
                }
            }
        }
    }
}
