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

package org.hisp.dhis.caseentry.action.caseaggregation;

import org.hisp.dhis.caseentry.state.PeriodGenericManager;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.oust.manager.SelectionTreeManager;
import org.hisp.dhis.period.Period;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version ValidationCaseAggregationAction.java Mar 21, 2011 8:50:06 PM $
 */
public class ValidationCaseAggregationAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SelectionTreeManager selectionTreeManager;

    private PeriodGenericManager periodGenericManager;

    private I18n i18n;

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------
    
    private String message;

    private int sDateLB;

    private int eDateLB;
    
    // -------------------------------------------------------------------------
    // Getters/Setters
    // -------------------------------------------------------------------------

    public void setPeriodGenericManager( PeriodGenericManager periodGenericManager )
    {
        this.periodGenericManager = periodGenericManager;
    }

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
    }

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    public void setEDateLB( int dateLB )
    {
        eDateLB = dateLB;
    }

    public void setSDateLB( int dateLB )
    {
        sDateLB = dateLB;
    }

    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Implementation Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        message = "";

        // ---------------------------------------------------------------------
        // Get selected orgunits
        // ---------------------------------------------------------------------

        OrganisationUnit selectedOrgunit = selectionTreeManager.getReloadedSelectedOrganisationUnit();

        if ( selectedOrgunit == null )
        {
            message = i18n.getString("please_specify_an_orgunit");
            return INPUT;
        }

        // ---------------------------------------------------------------------
        // Check start-date and end-date
        // ---------------------------------------------------------------------

        periodGenericManager.setSelectedPeriodIndex( PeriodGenericManager.SESSION_KEY_SELECTED_PERIOD_INDEX_START,
            sDateLB );
        Period startPeriod = periodGenericManager.getSelectedPeriod(
            PeriodGenericManager.SESSION_KEY_SELECTED_PERIOD_INDEX_START,
            PeriodGenericManager.SESSION_KEY_BASE_PERIOD_START );

        periodGenericManager.setSelectedPeriodIndex( PeriodGenericManager.SESSION_KEY_SELECTED_PERIOD_INDEX_END,
            eDateLB );
        Period endPeriod = periodGenericManager.getSelectedPeriod(
            PeriodGenericManager.SESSION_KEY_SELECTED_PERIOD_INDEX_END,
            PeriodGenericManager.SESSION_KEY_BASE_PERIOD_END );

        if ( startPeriod.getEndDate().after( endPeriod.getEndDate()  ) )
        {
            message = i18n.getString("please_select_to_date_greater_or_equals_to_from_date");
            return INPUT;
        }

        return SUCCESS;
    }
}
