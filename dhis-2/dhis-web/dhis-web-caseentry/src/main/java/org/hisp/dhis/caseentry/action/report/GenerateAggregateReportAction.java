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

package org.hisp.dhis.caseentry.action.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.common.Grid;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patientreport.PatientAggregateReport;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.period.comparator.AscendingPeriodComparator;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.ProgramStageService;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * 
 * @version GenerateAggregateReportAction.java 9:55:42 AM Jan 9, 2013 $
 */
public class GenerateAggregateReportAction
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

    private ProgramStageService programStageService;

    public void setProgramStageService( ProgramStageService programStageService )
    {
        this.programStageService = programStageService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer programStageId;

    public void setProgramStageId( Integer programStageId )
    {
        this.programStageId = programStageId;
    }

    private String aggregateType;

    public void setAggregateType( String aggregateType )
    {
        this.aggregateType = aggregateType;
    }

    private Collection<Integer> orgunitIds = new HashSet<Integer>();

    public void setOrgunitIds( Collection<Integer> orgunitIds )
    {
        this.orgunitIds = orgunitIds;
    }

    private List<String> deFilters;

    public void setDeFilters( List<String> deFilters )
    {
        this.deFilters = deFilters;
    }

    private Collection<String> fixedPeriods = new HashSet<String>();

    public void setFixedPeriods( Collection<String> fixedPeriods )
    {
        this.fixedPeriods = fixedPeriods;
    }

    private Collection<String> relativePeriods = new HashSet<String>();

    public void setRelativePeriods( Collection<String> relativePeriods )
    {
        this.relativePeriods = relativePeriods;
    }

    private List<String> startDates = new ArrayList<String>();

    public void setStartDates( List<String> startDates )
    {
        this.startDates = startDates;
    }

    private List<String> endDates = new ArrayList<String>();

    public void setEndDates( List<String> endDates )
    {
        this.endDates = endDates;
    }

    private String facilityLB; // All, children, current

    public void setFacilityLB( String facilityLB )
    {
        this.facilityLB = facilityLB;
    }

    private Integer position;

    public void setPosition( Integer position )
    {
        this.position = position;
    }

    private Integer limitRecords;

    public void setLimitRecords( Integer limitRecords )
    {
        this.limitRecords = limitRecords;
    }

    private Integer deGroupBy;

    public void setDeGroupBy( Integer deGroupBy )
    {
        this.deGroupBy = deGroupBy;
    }

    private Boolean useCompletedEvents;

    public void setUseCompletedEvents( Boolean useCompletedEvents )
    {
        this.useCompletedEvents = useCompletedEvents;
    }

    private Boolean userOrganisationUnit;

    public void setUserOrganisationUnit( Boolean userOrganisationUnit )
    {
        this.userOrganisationUnit = userOrganisationUnit;
    }

    private Boolean userOrganisationUnitChildren;

    public void setUserOrganisationUnitChildren( Boolean userOrganisationUnitChildren )
    {
        this.userOrganisationUnitChildren = userOrganisationUnitChildren;
    }

    private Integer deSum;

    public void setDeSum( Integer deSum )
    {
        this.deSum = deSum;
    }

    private String type;

    public void setType( String type )
    {
        this.type = type;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Grid grid;

    public Grid getGrid()
    {
        return grid;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        // ---------------------------------------------------------------------
        // Get user orgunits
        // ---------------------------------------------------------------------

        Collection<OrganisationUnit> userOrgunits = currentUserService.getCurrentUser().getOrganisationUnits();

        if ( userOrganisationUnit || userOrganisationUnitChildren )
        {
            orgunitIds = new HashSet<Integer>();

            if ( userOrganisationUnit )
            {
                for ( OrganisationUnit userOrgunit : userOrgunits )
                {
                    orgunitIds.add( userOrgunit.getId() );
                }
            }

            if ( userOrganisationUnitChildren )
            {
                for ( OrganisationUnit userOrgunit : userOrgunits )
                {
                    if ( userOrgunit.hasChild() )
                    {
                        for ( OrganisationUnit childOrgunit : userOrgunit.getSortedChildren() )
                        {
                            orgunitIds.add( childOrgunit.getId() );
                        }
                    }
                }
            }
        }

        // ---------------------------------------------------------------------
        // Get periods
        // ---------------------------------------------------------------------

        List<Period> periods = new ArrayList<Period>();

        // Create period from start-date and end-date

        for ( int i = 0; i < startDates.size(); i++ )
        {
            Period period = new Period();
            period.setStartDate( format.parseDate( startDates.get( i ) ) );
            period.setEndDate( format.parseDate( endDates.get( i ) ) );
            periods.add( period );
        }

        // Fixed periods
        List<Period> fixedPeriodList = new ArrayList<Period>(); 
        for ( String periodId : fixedPeriods )
        {
            Period p = PeriodType.getPeriodFromIsoString( periodId );
            fixedPeriodList.add( p );
        }
        Collections.sort( fixedPeriodList, new AscendingPeriodComparator() );      
        periods.addAll( fixedPeriodList );
        
        // Relative periods
        periods.addAll( getRelativePeriod() );
        
        // ---------------------------------------------------------------------
        // Generate report
        // ---------------------------------------------------------------------

        ProgramStage programStage = programStageService.getProgramStage( programStageId );
        Map<Integer, Collection<String>> deFilterMap = null;
        if ( deFilters != null )
        {
            deFilterMap = new HashMap<Integer, Collection<String>>();
            for ( String deFilter : deFilters )
            {
                int index = deFilter.indexOf( PatientAggregateReport.SEPARATE_FILTER );
                Integer deId = Integer.parseInt( deFilter.substring( 0, index ) );
                String filter = deFilter.substring( index + 1, deFilter.length() );
                if ( deFilterMap.containsKey( deId ) )
                {
                    Collection<String> filterValues = deFilterMap.get( deId );
                    filterValues.add( filter );
                }
                else
                {
                    Collection<String> filterValues = new HashSet<String>();
                    filterValues.add( filter );
                    deFilterMap.put( deId, filterValues );
                }
            }
        }
        
        grid = programStageInstanceService.getAggregateReport( position, programStage, orgunitIds, facilityLB,
            deGroupBy, deSum, deFilterMap, periods, aggregateType, limitRecords, useCompletedEvents, format, i18n );

        return type == null ? SUCCESS : type;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private List<Period> getRelativePeriod()
    {
        List<Period> periods = new ArrayList<Period>();

        RelativePeriods rp = new RelativePeriods();
        
        if ( relativePeriods.contains( "lastWeek" ) )
        {
            rp.clear().setLastWeek( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }
        
        if ( relativePeriods.contains( "last4Weeks" ) )
        {
            rp.clear().setLast4Weeks( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }

        if ( relativePeriods.contains( "last12Weeks" ) )
        {
            rp.clear().setLast12Weeks( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }

        if ( relativePeriods.contains( "last12Months" ) )
        {
            rp.clear().setLast12Months( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }

        if ( relativePeriods.contains( "reportingQuarter" ) )
        {
            rp.clear().setReportingQuarter( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }

        if ( relativePeriods.contains( "last4Quarters" ) )
        {
            rp.clear().setLast4Quarters( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }

        if ( relativePeriods.contains( "lastSixMonth" ) )
        {
            rp.clear().setLastSixMonth( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }

        if ( relativePeriods.contains( "last2SixMonths" ) )
        {
            rp.clear().setLast2SixMonths( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }

        if ( relativePeriods.contains( "thisYear" ) )
        {
            rp.clear().setThisYear( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }

        if ( relativePeriods.contains( "lastYear" ) )
        {
            rp.clear().setLastYear( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }

        if ( relativePeriods.contains( "last5Years" ) )
        {
            rp.clear().setLast5Years( true );
            periods.addAll( periodService.reloadPeriods( rp.getRelativePeriods() ) );
        }
 
        return setNames(periods);
    }

    private List<Period> setNames( List<Period> periods )
    {
        for ( Period period : periods )
        {
            period.setName( format.formatPeriod( period ) );
        }

        return periods;
    }
}
