package org.hisp.dhis.caseentry.state;

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

import java.util.List;

import org.hisp.dhis.period.Period;

import com.opensymphony.xwork2.Action;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: NextPeriodsAction.java 2966 2007-03-03 14:38:20Z torgeilo $ *
 * @modifier Dang Duy Hieu
 * @since 2009-10-14
 */
public class NextPeriodsAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PeriodGenericManager periodGenericManager;

    public void setPeriodGenericManager( PeriodGenericManager periodGenericManager )
    {
        this.periodGenericManager = periodGenericManager;
    }

    // -------------------------------------------------------------------------
    // Input && Output
    // -------------------------------------------------------------------------

    private boolean startField;

    public void setStartField( boolean startField )
    {
        this.startField = startField;
    }

    private List<Period> periods;

    public List<Period> getPeriods()
    {
        return periods;
    }

    private Integer index;

    public void setIndex( Integer index )
    {
        this.index = index;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        String selectedPeriodKey = "";
        String basePeriodKey = "";

        if ( startField )
        {
            selectedPeriodKey = PeriodGenericManager.SESSION_KEY_SELECTED_PERIOD_INDEX_START;
            basePeriodKey = PeriodGenericManager.SESSION_KEY_BASE_PERIOD_START;
        }
        else
        {
            selectedPeriodKey = PeriodGenericManager.SESSION_KEY_SELECTED_PERIOD_INDEX_END;
            basePeriodKey = PeriodGenericManager.SESSION_KEY_BASE_PERIOD_END;
        }

        periodGenericManager.setSelectedPeriodIndex( selectedPeriodKey, index );
        periodGenericManager.nextPeriodSpan( selectedPeriodKey, basePeriodKey );

        periods = periodGenericManager.getPeriodList( selectedPeriodKey, basePeriodKey );

        return SUCCESS;
    }
}
