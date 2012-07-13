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

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.period.CalendarPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author Chau Thu Tran
 * @version $Id$
 */
public class DefaultPeriodGenericManager
    implements PeriodGenericManager
{
    private static final Log log = LogFactory.getLog( DefaultPeriodGenericManager.class );

    public static final String SESSION_KEY_SELECTED_PERIOD_TYPE = "SESSION_KEY_SELECTED_PERIOD_TYPE";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // Implementation methodss
    // -------------------------------------------------------------------------

    public void setSelectedPeriodIndex( String key, Integer index )
    {
        getSession().put( key, index );
    }

    public Integer getSelectedPeriodIndex( String key )
    {
        return (Integer) getSession().get( key );
    }

    public Period getSelectedPeriod( String key, String baseKey )
    {
        Integer index = getSelectedPeriodIndex( key );

        if ( index == null )
        {
            return null;
        }

        List<Period> periods = getPeriodList( key, baseKey );

        if ( index >= 0 && index < periods.size() )
        {
            return periods.get( index );
        }

        return null;
    }

    public void clearSelectedPeriod()
    {
        getSession().remove( SESSION_KEY_SELECTED_PERIOD_INDEX_START );
        getSession().remove( SESSION_KEY_SELECTED_PERIOD_INDEX_END );
    }

    public List<Period> getPeriodList( String key, String baseKey )
    {
        Period basePeriod = getBasePeriod( baseKey );

        CalendarPeriodType periodType = (CalendarPeriodType) getPeriodType();

        List<Period> periods = periodType.generatePeriods( basePeriod );

        Date now = new Date();

        Iterator<Period> iterator = periods.iterator();

        while ( iterator.hasNext() )
        {
            if ( iterator.next().getStartDate().after( now ) )
            {
                iterator.remove();
            }
        }

        return periods;
    }

    public void nextPeriodSpan( String key, String baseKey )
    {
        List<Period> periods = getPeriodList( key, baseKey );
        CalendarPeriodType periodType = (CalendarPeriodType) getPeriodType();

        Period basePeriod = periods.get( periods.size() - 1 );
        Period newBasePeriod = periodType.getNextPeriod( basePeriod );

        if ( newBasePeriod.getStartDate().before( new Date() ) ) // Future
                                                                 // periods not
                                                                 // allowed
        {
            getSession().put( baseKey, newBasePeriod );
        }
    }

    public void previousPeriodSpan( String key, String baseKey )
    {
        List<Period> periods = getPeriodList( key, baseKey );
        CalendarPeriodType periodType = (CalendarPeriodType) getPeriodType();

        Period basePeriod = periods.get( 0 );
        Period newBasePeriod = periodType.getPreviousPeriod( basePeriod );

        getSession().put( baseKey, newBasePeriod );
    }

    // -------------------------------------------------------------------------
    // Supporting methods
    // -------------------------------------------------------------------------

    private PeriodType getPeriodType()
    {
        return (PeriodType) getSession().get( SESSION_KEY_SELECTED_PERIOD_TYPE );
    }

    private Period getBasePeriod( String baseKey )
    {
        Period basePeriod = (Period) getSession().get( baseKey );

        PeriodType periodType = getPeriodType();

        if ( basePeriod == null )
        {
            log.debug( "Base period is null, creating new" );

            basePeriod = periodType.createPeriod();
            getSession().put( baseKey, basePeriod );
        }
        else if ( !basePeriod.getPeriodType().equals( periodType ) )
        {
            log.debug( "Wrong type of base period, transforming" );

            basePeriod = periodType.createPeriod( basePeriod.getStartDate() );
            getSession().put( baseKey, basePeriod );
        }

        return basePeriod;
    }

    private static final Map<String, Object> getSession()
    {
        return ActionContext.getContext().getSession();
    }

    @Override
    public void setPeriodType( String periodTypeName )
    {
        getSession().put( SESSION_KEY_SELECTED_PERIOD_TYPE, periodService.getPeriodTypeByName( periodTypeName ) );
    }

    public void clearBasePeriod()
    {
        getSession().remove( SESSION_KEY_BASE_PERIOD_START );
        getSession().remove( SESSION_KEY_BASE_PERIOD_END );
    }
}
