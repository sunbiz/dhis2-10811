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

package org.hisp.dhis.patient.action.validation;

import org.hisp.dhis.caseaggregation.CaseAggregationConditionService;
import org.hisp.dhis.program.ProgramValidation;
import org.hisp.dhis.program.ProgramValidationService;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version $ GetProgramValidationAction.java Apr 28, 2011 11:17:58 AM $
 */
public class GetProgramValidationAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramValidationService programValidationService;

    private CaseAggregationConditionService aggregationConditionService;

    // -------------------------------------------------------------------------
    // Input && Output
    // -------------------------------------------------------------------------

    private Integer validationId;

    private ProgramValidation validation;

    private String leftDescription;

    public String getLeftDescription()
    {
        return leftDescription;
    }

    private String rightDescription;

    public String getRightDescription()
    {
        return rightDescription;
    }

    // -------------------------------------------------------------------------
    // Getter && Setter
    // -------------------------------------------------------------------------

    public void setProgramValidationService( ProgramValidationService programValidationService )
    {
        this.programValidationService = programValidationService;
    }

    public void setAggregationConditionService( CaseAggregationConditionService aggregationConditionService )
    {
        this.aggregationConditionService = aggregationConditionService;
    }

    public void setValidationId( Integer validationId )
    {
        this.validationId = validationId;
    }

    public ProgramValidation getValidation()
    {
        return validation;
    }

    // -------------------------------------------------------------------------
    // Implementation Action
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        validation = programValidationService.getProgramValidation( validationId );

        leftDescription = aggregationConditionService.getConditionDescription( validation.getLeftSide() );
        rightDescription = aggregationConditionService.getConditionDescription( validation.getRightSide() );

        return SUCCESS;
    }

}
