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

import static org.hisp.dhis.system.util.ConversionUtils.getIntegerCollection;
import static org.hisp.dhis.system.util.ConversionUtils.getList;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.oust.manager.SelectionTreeManager;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.reporttable.ReportParams;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;

import com.opensymphony.xwork2.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class SaveTableAction
    implements Action
{
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
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer tableId;

    public void setTableId( Integer id )
    {
        this.tableId = id;
    }
    
    private String tableName;

    public void setTableName( String tableName )
    {
        this.tableName = tableName;
    }
        
    private Integer sortOrder;
    
    public void setSortOrder( Integer sortOrder )
    {
        this.sortOrder = sortOrder;
    }
    
    private Integer topLimit;

    public void setTopLimit( Integer topLimit )
    {
        this.topLimit = topLimit;
    }

    private boolean regression;

    public void setRegression( boolean regression )
    {
        this.regression = regression;
    }    
    
    private boolean cumulative;
    
    public void setCumulative( boolean cumulative )
    {
        this.cumulative = cumulative;
    }

    private Integer categoryComboId;

    public void setCategoryComboId( Integer categoryComboId )
    {
        this.categoryComboId = categoryComboId;
    }

    private boolean doIndicators;

    public void setDoIndicators( boolean doIndicators )
    {
        this.doIndicators = doIndicators;
    }
    
    private boolean doPeriods;

    public void setDoPeriods( boolean doPeriods )
    {
        this.doPeriods = doPeriods;
    }

    private boolean doOrganisationUnits;

    public void setDoOrganisationUnits( boolean doUnits )
    {
        this.doOrganisationUnits = doUnits;
    }

    private List<String> selectedDataElements = new ArrayList<String>();

    public void setSelectedDataElements( List<String> selectedDataElements )
    {
        this.selectedDataElements = selectedDataElements;
    }
    
    private List<String> selectedIndicators = new ArrayList<String>();

    public void setSelectedIndicators( List<String> selectedIndicators )
    {
        this.selectedIndicators = selectedIndicators;
    }

    private List<String> selectedDataSets = new ArrayList<String>();

    public void setSelectedDataSets( List<String> selectedDataSets )
    {
        this.selectedDataSets = selectedDataSets;
    }
    
    private List<String> selectedPeriods = new ArrayList<String>();

    public void setSelectedPeriods( List<String> selectedPeriods )
    {
        this.selectedPeriods = selectedPeriods;
    }
    
    private List<String> selectedOrganisationUnitGroups = new ArrayList<String>();

    public void setSelectedOrganisationUnitGroups( List<String> selectedOrganisationUnitGroups )
    {
        this.selectedOrganisationUnitGroups = selectedOrganisationUnitGroups;
    }

    private boolean reportingMonth;

    public void setReportingMonth( boolean reportingMonth )
    {
        this.reportingMonth = reportingMonth;
    }

    private boolean reportingBimonth;
    
    public void setReportingBimonth( boolean reportingBimonth )
    {
        this.reportingBimonth = reportingBimonth;
    }
    
    private boolean reportingQuarter;

    public void setReportingQuarter( boolean reportingQuarter )
    {
        this.reportingQuarter = reportingQuarter;
    }

    private boolean lastSixMonth;

    public void setLastSixMonth( boolean lastSixMonth )
    {
        this.lastSixMonth = lastSixMonth;
    }

    private boolean monthsThisYear;

    public void setMonthsThisYear( boolean monthsThisYear )
    {
        this.monthsThisYear = monthsThisYear;
    }

    private boolean quartersThisYear;

    public void setQuartersThisYear( boolean quartersThisYear )
    {
        this.quartersThisYear = quartersThisYear;
    }

    private boolean thisYear;

    public void setThisYear( boolean thisYear )
    {
        this.thisYear = thisYear;
    }

    private boolean monthsLastYear;

    public void setMonthsLastYear( boolean monthsLastYear )
    {
        this.monthsLastYear = monthsLastYear;
    }

    private boolean quartersLastYear;

    public void setQuartersLastYear( boolean quartersLastYear )
    {
        this.quartersLastYear = quartersLastYear;
    }

    private boolean last5Years;
    
    public void setLast5Years( boolean last5Years )
    {
        this.last5Years = last5Years;
    }

    private boolean lastYear;
    
    public void setLastYear( boolean lastYear )
    {
        this.lastYear = lastYear;
    }
    
    private boolean last12Months;

    public void setLast12Months( boolean last12Months )
    {
        this.last12Months = last12Months;
    }

    private boolean last4Quarters;

    public void setLast4Quarters( boolean last4Quarters )
    {
        this.last4Quarters = last4Quarters;
    }
    
    private boolean last2SixMonths;

    public void setLast2SixMonths( boolean last2SixMonths )
    {
        this.last2SixMonths = last2SixMonths;
    }
    
    private boolean thisFinancialYear;

    public void setThisFinancialYear( boolean thisFinancialYear )
    {
        this.thisFinancialYear = thisFinancialYear;
    }

    private boolean lastFinancialYear;

    public void setLastFinancialYear( boolean lastFinancialYear )
    {
        this.lastFinancialYear = lastFinancialYear;
    }

    private boolean last5FinancialYears;

    public void setLast5FinancialYears( boolean last5FinancialYears )
    {
        this.last5FinancialYears = last5FinancialYears;
    }

    private boolean paramReportingMonth;

    public void setParamReportingMonth( boolean paramReportingMonth )
    {
        this.paramReportingMonth = paramReportingMonth;
    }

    private boolean paramLeafParentOrganisationUnit;
    
    public void setParamLeafParentOrganisationUnit( boolean paramLeafParentOrganisationUnit )
    {
        this.paramLeafParentOrganisationUnit = paramLeafParentOrganisationUnit;
    }

    private boolean paramGrandParentOrganisationUnit;
    
    public void setParamGrandParentOrganisationUnit( boolean paramGrandParentOrganisationUnit )
    {
        this.paramGrandParentOrganisationUnit = paramGrandParentOrganisationUnit;
    }

    private boolean paramParentOrganisationUnit;

    public void setParamParentOrganisationUnit( boolean paramParentOrganisationUnit )
    {
        this.paramParentOrganisationUnit = paramParentOrganisationUnit;
    }
    
    private boolean paramOrganisationUnit;

    public void setParamOrganisationUnit( boolean paramOrganisationUnit )
    {
        this.paramOrganisationUnit = paramOrganisationUnit;
    }
        
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        ReportTable reportTable = getReportTable();
        
        reportTableService.saveReportTable( reportTable );
        
        return SUCCESS;
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private ReportTable getReportTable()
        throws Exception
    {    
        List<Period> periods = getList( periodService.getPeriodsByExternalIds( selectedPeriods ) );
        
        List<DataElement> dataElements = new ArrayList<DataElement>();        
        List<Indicator> indicators = new ArrayList<Indicator>();
        List<DataSet> dataSets = new ArrayList<DataSet>();
        List<OrganisationUnit> units = new ArrayList<OrganisationUnit>( selectionTreeManager.getReloadedSelectedOrganisationUnits() );
        List<OrganisationUnitGroup> organisationUnitGroups = new ArrayList<OrganisationUnitGroup>(); 

        for ( Integer id : getIntegerCollection( selectedDataElements ) )
        {
            dataElements.add( dataElementService.getDataElement( id ) );
        }
        
        for ( Integer id : getIntegerCollection( selectedIndicators ) )
        {
            indicators.add( indicatorService.getIndicator( id ) );
        }
        
        for ( Integer id : getIntegerCollection( selectedDataSets ) )
        {
            dataSets.add( dataSetService.getDataSet( id ) );
        }
                
        for ( Integer id : getIntegerCollection( selectedOrganisationUnitGroups ) )
        {
            organisationUnitGroups.add( organisationUnitGroupService.getOrganisationUnitGroup( id ) );
        }
        
        DataElementCategoryCombo categoryCombo = categoryComboId != null ? categoryService.getDataElementCategoryCombo( categoryComboId ) : null;
        
        RelativePeriods relatives = new RelativePeriods( reportingMonth, reportingBimonth, reportingQuarter, lastSixMonth,
            monthsThisYear, quartersThisYear, thisYear, 
            monthsLastYear, quartersLastYear, lastYear, 
            last5Years, last12Months, false, last4Quarters, last2SixMonths,
            thisFinancialYear, lastFinancialYear, last5FinancialYears, false );
        
        ReportParams reportParams = new ReportParams();
        
        reportParams.setParamReportingMonth( paramReportingMonth );
        reportParams.setParamLeafParentOrganisationUnit( paramLeafParentOrganisationUnit );
        reportParams.setParamGrandParentOrganisationUnit( paramGrandParentOrganisationUnit );
        reportParams.setParamParentOrganisationUnit( paramParentOrganisationUnit );
        reportParams.setParamOrganisationUnit( paramOrganisationUnit );
        
        ReportTable reportTable = null;
        
        if ( tableId == null )
        {
            reportTable = new ReportTable( tableName,
                dataElements, indicators, dataSets, periods, null, units, null, organisationUnitGroups,
                categoryCombo, doIndicators, doPeriods, doOrganisationUnits, relatives, reportParams, null, null );
        }
        else
        {
            reportTable = reportTableService.getReportTable( tableId );
            
            reportTable.setName( tableName );
            reportTable.setRegression( regression );
            reportTable.setDataElements( dataElements );
            reportTable.setIndicators( indicators );
            reportTable.setDataSets( dataSets );
            reportTable.setPeriods( periods );
            reportTable.setUnits( units );
            reportTable.setOrganisationUnitGroups( organisationUnitGroups );
            reportTable.setCategoryCombo( categoryCombo );
            reportTable.setDoIndicators( doIndicators );
            reportTable.setDoPeriods( doPeriods );
            reportTable.setDoUnits( doOrganisationUnits );
            reportTable.setRelatives( relatives );
            reportTable.setReportParams( reportParams );
        }

        reportTable.setRegression( regression );
        reportTable.setCumulative( cumulative );
        reportTable.setSortOrder( sortOrder );
        reportTable.setTopLimit( topLimit );
        
        return reportTable;
    }
}
