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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.caseentry.state.SelectedStateManager;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.program.ProgramExpressionService;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramValidation;
import org.hisp.dhis.program.ProgramValidationResult;
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

    private ProgramValidationService programValidationService;

    private ProgramExpressionService programExpressionService;

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private I18nFormat format;

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Collection<ProgramValidationResult> programValidationResults;

    private Map<Integer, String> leftsideFormulaMap = new HashMap<Integer, String>();

    private Map<Integer, String> rightsideFormulaMap = new HashMap<Integer, String>();

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public void setProgramExpressionService( ProgramExpressionService programExpressionService )
    {
        this.programExpressionService = programExpressionService;
    }

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    public Map<Integer, String> getLeftsideFormulaMap()
    {
        return leftsideFormulaMap;
    }

    public Map<Integer, String> getRightsideFormulaMap()
    {
        return rightsideFormulaMap;
    }

    public Collection<ProgramValidationResult> getProgramValidationResults()
    {
        return programValidationResults;
    }

    public void setProgramValidationService( ProgramValidationService programValidationService )
    {
        this.programValidationService = programValidationService;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        programValidationResults = new ArrayList<ProgramValidationResult>();

        // ---------------------------------------------------------------------
        // Get selected objects
        // ---------------------------------------------------------------------

        ProgramStageInstance programStageInstance = selectedStateManager.getSelectedProgramStageInstance();

        // ---------------------------------------------------------------------
        // Check validations for dataelement into multi-stages
        // ---------------------------------------------------------------------

        // runProgramValidation( programValidationService.getProgramValidation(
        // programStageInstance.getProgramStage() ),
        // programStageInstance );

        Collection<ProgramValidation> validation = programValidationService.getProgramValidation( programStageInstance
            .getProgramStage() );
        programValidationResults = programValidationService.validate( validation, programStageInstance );
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Support method
    // -------------------------------------------------------------------------

    private void runProgramValidation( Collection<ProgramValidation> validations,
        ProgramStageInstance programStageInstance )
    {
        // if ( validations != null )
        // {
        // for ( ProgramValidation validation : validations )
        // {
        // ProgramValidationResult validationResult =
        // programValidationService.validate( validation,
        // programStageInstance, format );
        //
        // if ( validationResult != null )
        // {
        // programValidationResults.add( validationResult );
        //
        // leftsideFormulaMap.put(
        // validationResult.getProgramValidation().getId(),
        // programExpressionService.getExpressionDescription(
        // validationResult.getProgramValidation()
        // .getLeftSide().getExpression() ) );
        //
        // rightsideFormulaMap.put(
        // validationResult.getProgramValidation().getId(),
        // programExpressionService.getExpressionDescription(
        // validationResult.getProgramValidation()
        // .getRightSide().getExpression() ) );
        // }
        // }
        // }
    }
}
