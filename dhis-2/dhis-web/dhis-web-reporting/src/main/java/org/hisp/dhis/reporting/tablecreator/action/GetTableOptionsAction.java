package org.hisp.dhis.reporting.tablecreator.action;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.oust.manager.SelectionTreeManager;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.system.filter.AggregatableDataElementFilter;
import org.hisp.dhis.system.filter.PastAndCurrentPeriodFilter;
import org.hisp.dhis.system.util.FilterUtils;

import com.opensymphony.xwork2.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GetTableOptionsAction
    implements Action
{
    private static final int AVAILABLE_REPORTING_MONTHS = 12;
    
    private static final int ALL = 0;

    public int getALL()
    {
        return ALL;
    }
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ReportTableService reportTableService;

    public void setReportTableService( ReportTableService reportTableService )
    {
        this.reportTableService = reportTableService;
    }
    
    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataElementCategoryService categoryService;
    
    public void setCategoryService( DataElementCategoryService categoryService )
    {
        this.categoryService = categoryService;
    }

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    private OrganisationUnitGroupService organisationUnitGroupService;
    
    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private SelectionTreeManager selectionTreeManager;

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
    }
    
    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }
        
    private boolean dimension;

    public boolean isDimension()
    {
        return dimension;
    }

    public void setDimension( boolean dimension )
    {
        this.dimension = dimension;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<DataElementGroup> dataElementGroups = new ArrayList<DataElementGroup>();

    public List<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }
    
    private List<DataElementCategoryCombo> categoryCombos = new ArrayList<DataElementCategoryCombo>();
    
    public List<DataElementCategoryCombo> getCategoryCombos()
    {
        return categoryCombos;
    }

    private List<DataElement> dataElements = new ArrayList<DataElement>();

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }
    
    private List<IndicatorGroup> indicatorGroups = new ArrayList<IndicatorGroup>();

    public List<IndicatorGroup> getIndicatorGroups()
    {
        return indicatorGroups;
    }
    
    private List<Indicator> indicators = new ArrayList<Indicator>();

    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    private List<DataSet> dataSets = new ArrayList<DataSet>();

    public List<DataSet> getDataSets()
    {
        return dataSets;
    }
    
    private List<PeriodType> periodTypes = new ArrayList<PeriodType>();

    public List<PeriodType> getPeriodTypes()
    {
        return periodTypes;
    }

    private List<Period> periods = new ArrayList<Period>();

    public List<Period> getPeriods()
    {
        return periods;
    }
            
    private List<OrganisationUnitGroup> organisationUnitGroups = new ArrayList<OrganisationUnitGroup>();

    public List<OrganisationUnitGroup> getOrganisationUnitGroups()
    {
        return organisationUnitGroups;
    }

    private ReportTable reportTable;

    public ReportTable getReportTable()
    {
        return reportTable;
    }
    
    private List<DataElement> selectedDataElements = new ArrayList<DataElement>();

    public List<DataElement> getSelectedDataElements()
    {
        return selectedDataElements;
    }
    
    private List<Indicator> selectedIndicators = new ArrayList<Indicator>();

    public List<Indicator> getSelectedIndicators()
    {
        return selectedIndicators;
    }

    private List<DataSet> selectedDataSets = new ArrayList<DataSet>();

    public List<DataSet> getSelectedDataSets()
    {
        return selectedDataSets;
    }    
    
    private List<Period> selectedPeriods = new ArrayList<Period>();

    public List<Period> getSelectedPeriods()
    {
        return selectedPeriods;
    }
        
    private List<OrganisationUnitGroup> selectedOrganisationUnitGroups = new ArrayList<OrganisationUnitGroup>();

    public List<OrganisationUnitGroup> getSelectedOrganisationUnitGroups()
    {
        return selectedOrganisationUnitGroups;
    }

    private SortedMap<Integer, String> reportingPeriods = new TreeMap<Integer, String>();

    public SortedMap<Integer, String> getReportingPeriods()
    {
        return reportingPeriods;
    }
    
    private Comparator<Indicator> indicatorComparator;
    
    public void setIndicatorComparator( Comparator<Indicator> indicatorComparator )
    {
        this.indicatorComparator = indicatorComparator;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        // ---------------------------------------------------------------------
        // Available metadata
        // ---------------------------------------------------------------------

        if ( dimension )
        {
            categoryCombos = new ArrayList<DataElementCategoryCombo>( categoryService.getAllDataElementCategoryCombos() );            
            Collections.sort( categoryCombos, IdentifiableObjectNameComparator.INSTANCE );            
        }
        else
        {        
            dataElementGroups = new ArrayList<DataElementGroup>( dataElementService.getAllDataElementGroups() );            
            Collections.sort( dataElementGroups, IdentifiableObjectNameComparator.INSTANCE );
            
            dataElements = new ArrayList<DataElement>( dataElementService.getAllDataElements() );            
            Collections.sort( dataElements, IdentifiableObjectNameComparator.INSTANCE );            
            FilterUtils.filter( dataElements, new AggregatableDataElementFilter() );
            
            indicatorGroups = new ArrayList<IndicatorGroup>( indicatorService.getAllIndicatorGroups() );            
            Collections.sort( indicatorGroups, IdentifiableObjectNameComparator.INSTANCE );
            
            indicators = new ArrayList<Indicator>( indicatorService.getAllIndicators() );            
            Collections.sort( indicators, indicatorComparator );
            
            dataSets = new ArrayList<DataSet>( dataSetService.getAllDataSets() );            
            Collections.sort( dataSets, IdentifiableObjectNameComparator.INSTANCE ); 
        }
        
        periodTypes = periodService.getAllPeriodTypes();
        
        periods = new MonthlyPeriodType().generatePeriods( new Date() );
        Collections.reverse( periods );
        FilterUtils.filter( periods, new PastAndCurrentPeriodFilter() );
        
        organisationUnitGroups = new ArrayList<OrganisationUnitGroup>( organisationUnitGroupService.getOrganisationUnitGroupsWithGroupSets() );
        Collections.sort( organisationUnitGroups, new IdentifiableObjectNameComparator() );
        
        // ---------------------------------------------------------------------
        // Reporting periods
        // ---------------------------------------------------------------------

        MonthlyPeriodType periodType = new MonthlyPeriodType();
        
        Calendar cal = PeriodType.createCalendarInstance();
        
        for ( int i = 0; i < AVAILABLE_REPORTING_MONTHS; i++ )
        {
            int month = i + 1;
            cal.add( Calendar.MONTH, -1 );            
            Period period = periodType.createPeriod( cal.getTime() );            
            String periodName = format.formatPeriod( period );
            
            reportingPeriods.put( month, periodName );
        }
        
        selectionTreeManager.clearSelectedOrganisationUnits();
        
        // ---------------------------------------------------------------------
        // Report table
        // ---------------------------------------------------------------------

        if ( id != null )
        {
            reportTable = reportTableService.getReportTable( id );
            
            dataElements.removeAll( reportTable.getDataElements() );
            
            indicators.removeAll( reportTable.getIndicators() );
            
            dataSets.removeAll( reportTable.getDataSets() );
            
            periods.removeAll( reportTable.getPeriods() );
            
            selectedDataElements = reportTable.getDataElements();
            
            selectedIndicators = reportTable.getIndicators();
            
            selectedDataSets = reportTable.getDataSets();
            
            selectedPeriods = reportTable.getPeriods();
            
            selectedOrganisationUnitGroups = reportTable.getOrganisationUnitGroups();

            selectionTreeManager.setSelectedOrganisationUnits( reportTable.getUnits() );
        }
        
        return SUCCESS;
    }
}
