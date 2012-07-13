package org.hisp.dhis.reporttable;

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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.amplecode.quick.BatchHandler;
import org.amplecode.quick.BatchHandlerFactory;
import org.hisp.dhis.DhisTest;
import org.hisp.dhis.aggregation.AggregatedDataValue;
import org.hisp.dhis.aggregation.AggregatedIndicatorValue;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.completeness.DataSetCompletenessResult;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.jdbc.batchhandler.AggregatedDataValueBatchHandler;
import org.hisp.dhis.jdbc.batchhandler.AggregatedIndicatorValueBatchHandler;
import org.hisp.dhis.jdbc.batchhandler.AggregatedOrgUnitDataValueBatchHandler;
import org.hisp.dhis.jdbc.batchhandler.AggregatedOrgUnitIndicatorValueBatchHandler;
import org.hisp.dhis.jdbc.batchhandler.DataSetCompletenessResultBatchHandler;
import org.hisp.dhis.mock.MockI18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.RelativePeriods;
import org.junit.Test;

/**
 * @author Lars Helge Overland
 */
public class ReportTableGridTest
    extends DhisTest
{
    private ReportTableService reportTableService;
    
    private BatchHandlerFactory batchHandlerFactory;
    
    private List<DataElement> dataElements;
    private List<DataElementCategoryOptionCombo> categoryOptionCombos;
    private List<Indicator> indicators;
    private List<DataSet> dataSets;
    private List<Period> periods;
    private List<Period> relativePeriods;
    private List<OrganisationUnit> units;
    private List<OrganisationUnitGroup> groups;

    private PeriodType montlyPeriodType;

    private DataElement dataElementA;
    private DataElement dataElementB;

    private DataElementCategoryOption categoryOptionA;
    private DataElementCategoryOption categoryOptionB;
    
    private DataElementCategory categoryA;
    
    private DataElementCategoryCombo categoryComboA;
    
    private DataElementCategoryOptionCombo categoryOptionComboA;
    private DataElementCategoryOptionCombo categoryOptionComboB;  

    private IndicatorType indicatorType;
    
    private Indicator indicatorA;
    private Indicator indicatorB;
    
    private DataSet dataSetA;
    private DataSet dataSetB;
    
    private Period periodA;
    private Period periodB;
    
    private OrganisationUnit unitA;
    private OrganisationUnit unitB;
    
    private OrganisationUnitGroup groupA;
    private OrganisationUnitGroup groupB;
        
    private int dataElementIdA;
    private int dataElementIdB;
    
    private int categoryOptionComboIdA;
    private int categoryOptionComboIdB;
    
    private int indicatorIdA;
    private int indicatorIdB;
    
    private int dataSetIdA;
    private int dataSetIdB;
    
    private int periodIdA;
    private int periodIdB;
    
    private int unitIdA;
    private int unitIdB;
    
    private int groupIdA;
    private int groupIdB;
        
    private I18nFormat i18nFormat;
    
    private Date date = new Date();

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
        throws Exception
    {
        reportTableService = (ReportTableService) getBean( ReportTableService.ID );
        
        dataElementService = (DataElementService) getBean( DataElementService.ID );
        
        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );        
        
        indicatorService = (IndicatorService) getBean( IndicatorService.ID );
        dataSetService = (DataSetService) getBean( DataSetService.ID );
        periodService = (PeriodService) getBean( PeriodService.ID );
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );
        organisationUnitGroupService = (OrganisationUnitGroupService) getBean( OrganisationUnitGroupService.ID );
        
        batchHandlerFactory = (BatchHandlerFactory) getBean( "batchHandlerFactory" );
        
        dataElements = new ArrayList<DataElement>();
        categoryOptionCombos = new ArrayList<DataElementCategoryOptionCombo>();
        indicators = new ArrayList<Indicator>();
        dataSets = new ArrayList<DataSet>();
        periods = new ArrayList<Period>();
        relativePeriods = new ArrayList<Period>();
        units = new ArrayList<OrganisationUnit>();
        groups = new ArrayList<OrganisationUnitGroup>();
        
        montlyPeriodType = PeriodType.getPeriodTypeByName( MonthlyPeriodType.NAME );       
        
        // ---------------------------------------------------------------------
        // Setup Dimensions
        // ---------------------------------------------------------------------

        categoryOptionA = new DataElementCategoryOption( "Male" );
        categoryOptionB = new DataElementCategoryOption( "Female" );
        
        categoryService.addDataElementCategoryOption( categoryOptionA );
        categoryService.addDataElementCategoryOption( categoryOptionB );

        categoryA = new DataElementCategory( "Gender" );
        categoryA.getCategoryOptions().add( categoryOptionA );
        categoryA.getCategoryOptions().add( categoryOptionB );

        categoryService.addDataElementCategory( categoryA );

        categoryComboA = new DataElementCategoryCombo( "Gender" );
        categoryComboA.getCategories().add( categoryA );        
        
        categoryService.addDataElementCategoryCombo( categoryComboA );
        
        categoryService.generateOptionCombos( categoryComboA );

        Iterator<DataElementCategoryOptionCombo> iter = categoryComboA.getOptionCombos().iterator();
        categoryOptionComboA = iter.next();
        categoryOptionComboB = iter.next();
        
        categoryOptionComboA.getCategoryOptions().iterator().next().setCategoryOptionCombos( getSet( categoryOptionComboA ) ); // Inverse association not set before transaction is committed
        categoryOptionComboB.getCategoryOptions().iterator().next().setCategoryOptionCombos( getSet( categoryOptionComboB ) );
        
        categoryOptionComboIdA = categoryOptionComboA.getId();
        categoryOptionComboIdB = categoryOptionComboB.getId();

        categoryOptionCombos.add( categoryOptionComboA );        
        categoryOptionCombos.add( categoryOptionComboB );
        
        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        dataElementA = createDataElement( 'A' );
        dataElementB = createDataElement( 'B' );
        
        dataElementIdA = dataElementService.addDataElement( dataElementA );
        dataElementIdB = dataElementService.addDataElement( dataElementB );
                
        dataElements.add( dataElementA );
        dataElements.add( dataElementB );

        // ---------------------------------------------------------------------
        // Setup Indicators
        // ---------------------------------------------------------------------

        indicatorType = createIndicatorType( 'A' );
        
        indicatorService.addIndicatorType( indicatorType );
        
        indicatorA = createIndicator( 'A', indicatorType );
        indicatorB = createIndicator( 'B', indicatorType );
        
        indicatorIdA = indicatorService.addIndicator( indicatorA );
        indicatorIdB = indicatorService.addIndicator( indicatorB );
                
        indicators.add( indicatorA );
        indicators.add( indicatorB );

        // ---------------------------------------------------------------------
        // Setup DataSets
        // ---------------------------------------------------------------------

        dataSetA = createDataSet( 'A', montlyPeriodType );
        dataSetB = createDataSet( 'B', montlyPeriodType );
        
        dataSetIdA = dataSetService.addDataSet( dataSetA );
        dataSetIdB = dataSetService.addDataSet( dataSetB );
        
        dataSets.add( dataSetA );
        dataSets.add( dataSetB );

        // ---------------------------------------------------------------------
        // Setup Periods
        // ---------------------------------------------------------------------

        periodA = createPeriod( montlyPeriodType, getDate( 2008, 1, 1 ), getDate( 2008, 1, 31 ) );
        periodB = createPeriod( montlyPeriodType, getDate( 2008, 2, 1 ), getDate( 2008, 2, 28 ) );
        
        periodIdA = periodService.addPeriod( periodA );
        periodIdB = periodService.addPeriod( periodB );
                
        periods.add( periodA );
        periods.add( periodB );
        
        // ---------------------------------------------------------------------
        // Setup OrganisationUnits
        // ---------------------------------------------------------------------

        unitA = createOrganisationUnit( 'A' );
        unitB = createOrganisationUnit( 'B' );
        
        unitIdA = organisationUnitService.addOrganisationUnit( unitA );
        unitIdB = organisationUnitService.addOrganisationUnit( unitB );
        
        units.add( unitA );
        units.add( unitB );

        // ---------------------------------------------------------------------
        // Setup OrganisationUnitGroups
        // ---------------------------------------------------------------------

        groupA = createOrganisationUnitGroup( 'A' );
        groupB = createOrganisationUnitGroup( 'B' );
        
        groupIdA = organisationUnitGroupService.addOrganisationUnitGroup( groupA );
        groupIdB = organisationUnitGroupService.addOrganisationUnitGroup( groupB );
        
        groups.add( groupA );
        groups.add( groupB );
        
        i18nFormat = new MockI18nFormat();

        BatchHandler<AggregatedDataValue> dataValueBatchHandler = batchHandlerFactory.createBatchHandler( AggregatedDataValueBatchHandler.class ).init();
        
        dataValueBatchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdA, periodIdA, 8, unitIdA, 8, 11 ) );
        dataValueBatchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdA, periodIdA, 8, unitIdB, 8, 12 ) );
        dataValueBatchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdA, periodIdB, 8, unitIdA, 8, 13 ) );
        dataValueBatchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdA, periodIdB, 8, unitIdB, 8, 14 ) );
        dataValueBatchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdA, periodIdA, 8, unitIdA, 8, 15 ) );
        dataValueBatchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdA, periodIdA, 8, unitIdB, 8, 16 ) );
        dataValueBatchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdA, periodIdB, 8, unitIdA, 8, 17 ) );
        dataValueBatchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdA, periodIdB, 8, unitIdB, 8, 18 ) );  
        
        dataValueBatchHandler.flush();
        
        BatchHandler<AggregatedIndicatorValue> indicatorValueBatchHandler = batchHandlerFactory.createBatchHandler( AggregatedIndicatorValueBatchHandler.class ).init();
        
        indicatorValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdA, periodIdA, 8, unitIdA, 8, "", 1, 11, 0, 0 ) );
        indicatorValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdA, periodIdA, 8, unitIdB, 8, "", 1, 12, 0, 0 ) );
        indicatorValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdA, periodIdB, 8, unitIdA, 8, "", 1, 13, 0, 0 ) );
        indicatorValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdA, periodIdB, 8, unitIdB, 8, "", 1, 14, 0, 0 ) );
        indicatorValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdB, periodIdA, 8, unitIdA, 8, "", 1, 15, 0, 0 ) );
        indicatorValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdB, periodIdA, 8, unitIdB, 8, "", 1, 16, 0, 0 ) );
        indicatorValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdB, periodIdB, 8, unitIdA, 8, "", 1, 17, 0, 0 ) );
        indicatorValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdB, periodIdB, 8, unitIdB, 8, "", 1, 18, 0, 0 ) );
        
        indicatorValueBatchHandler.flush();
        
        BatchHandler<DataSetCompletenessResult> completenessBatchHandler = batchHandlerFactory.createBatchHandler( DataSetCompletenessResultBatchHandler.class ).init();
        
        completenessBatchHandler.addObject( new DataSetCompletenessResult( dataSetIdA, periodIdA, null, unitIdA, null, 100, 11, 11 ) );
        completenessBatchHandler.addObject( new DataSetCompletenessResult( dataSetIdA, periodIdA, null, unitIdB, null, 100, 12, 12 ) );
        completenessBatchHandler.addObject( new DataSetCompletenessResult( dataSetIdA, periodIdB, null, unitIdA, null, 100, 13, 13 ) );
        completenessBatchHandler.addObject( new DataSetCompletenessResult( dataSetIdA, periodIdB, null, unitIdB, null, 100, 14, 14 ) );
        completenessBatchHandler.addObject( new DataSetCompletenessResult( dataSetIdB, periodIdA, null, unitIdA, null, 100, 15, 15 ) );
        completenessBatchHandler.addObject( new DataSetCompletenessResult( dataSetIdB, periodIdA, null, unitIdB, null, 100, 16, 16 ) );
        completenessBatchHandler.addObject( new DataSetCompletenessResult( dataSetIdB, periodIdB, null, unitIdA, null, 100, 17, 17 ) );
        completenessBatchHandler.addObject( new DataSetCompletenessResult( dataSetIdB, periodIdB, null, unitIdB, null, 100, 18, 18 ) );
        
        completenessBatchHandler.flush();
        
        BatchHandler<AggregatedDataValue> dataValueOrgUnitBatchHandler = batchHandlerFactory.createBatchHandler( AggregatedOrgUnitDataValueBatchHandler.class ).init();
        
        dataValueOrgUnitBatchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdA, periodIdA, 8, unitIdA, groupIdA, 8, 21 ) );
        dataValueOrgUnitBatchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdA, periodIdA, 8, unitIdA, groupIdB, 8, 22 ) );
        dataValueOrgUnitBatchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdA, periodIdB, 8, unitIdA, groupIdA, 8, 23 ) );
        dataValueOrgUnitBatchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdA, periodIdB, 8, unitIdA, groupIdB, 8, 24 ) );
        dataValueOrgUnitBatchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdA, periodIdA, 8, unitIdA, groupIdA, 8, 25 ) );
        dataValueOrgUnitBatchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdA, periodIdA, 8, unitIdA, groupIdB, 8, 26 ) );
        dataValueOrgUnitBatchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdA, periodIdB, 8, unitIdA, groupIdA, 8, 27 ) );
        dataValueOrgUnitBatchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdA, periodIdB, 8, unitIdA, groupIdB, 8, 28 ) );
        
        dataValueOrgUnitBatchHandler.flush();
        
        BatchHandler<AggregatedIndicatorValue> indicatorOrgUnitValueBatchHandler = batchHandlerFactory.createBatchHandler( AggregatedOrgUnitIndicatorValueBatchHandler.class ).init();
        
        indicatorOrgUnitValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdA, periodIdA, 8, unitIdA, groupIdA, 8, "", 1, 21, 0, 0 ) );
        indicatorOrgUnitValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdA, periodIdA, 8, unitIdA, groupIdB, 8, "", 1, 22, 0, 0 ) );
        indicatorOrgUnitValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdA, periodIdB, 8, unitIdA, groupIdA, 8, "", 1, 23, 0, 0 ) );
        indicatorOrgUnitValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdA, periodIdB, 8, unitIdA, groupIdB, 8, "", 1, 24, 0, 0 ) );
        indicatorOrgUnitValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdB, periodIdA, 8, unitIdA, groupIdA, 8, "", 1, 25, 0, 0 ) );
        indicatorOrgUnitValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdB, periodIdA, 8, unitIdA, groupIdB, 8, "", 1, 26, 0, 0 ) );
        indicatorOrgUnitValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdB, periodIdB, 8, unitIdA, groupIdA, 8, "", 1, 27, 0, 0 ) );
        indicatorOrgUnitValueBatchHandler.addObject( new AggregatedIndicatorValue( indicatorIdB, periodIdB, 8, unitIdA, groupIdB, 8, "", 1, 28, 0, 0 ) );
        
        indicatorOrgUnitValueBatchHandler.flush();
    }
    
    @Override
    public boolean emptyDatabaseAfterTest()
    {
        return true;
    }
    
    private Set<DataElementCategoryOptionCombo> getSet( DataElementCategoryOptionCombo c )
    {
        return new HashSet<DataElementCategoryOptionCombo>( Arrays.asList( c ) );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Test
    public void testGetOrgUnitIndicatorReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Prescriptions",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, new ArrayList<OrganisationUnit>(), new ArrayList<OrganisationUnit>(),
            groups, null, true, true, false, new RelativePeriods(), null, i18nFormat, "january_2000" );

        reportTable.setParentOrganisationUnit( unitA );
        
        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, null, 0 );
        
        assertEquals( 21.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 23.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 25.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 27.0, grid.getRow( 0 ).get( 11 ) );
        
        assertEquals( 22.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 24.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 26.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 28.0, grid.getRow( 1 ).get( 11 ) );
    }

    @Test
    public void testGetIndicatorOrgUnitReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, new ArrayList<OrganisationUnit>(), new ArrayList<OrganisationUnit>(), 
            groups, null, false, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        reportTable.setParentOrganisationUnit( unitA );
        
        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 21.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 22.0, grid.getRow( 0 ).get( 14 ) );
        
        assertEquals( 23.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 24.0, grid.getRow( 1 ).get( 14 ) );
        
        assertEquals( 25.0, grid.getRow( 2 ).get( 13 ) );
        assertEquals( 26.0, grid.getRow( 2 ).get( 14 ) );
        
        assertEquals( 27.0, grid.getRow( 3 ).get( 13 ) );
        assertEquals( 28.0, grid.getRow( 3 ).get( 14 ) );
    }

    @Test
    public void testGetIndicatorOrgUnitReportTableC()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, new ArrayList<OrganisationUnit>(), new ArrayList<OrganisationUnit>(), 
            groups, null, true, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        reportTable.setParentOrganisationUnit( unitA );
        
        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 21.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 22.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 25.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 26.0, grid.getRow( 0 ).get( 11 ) );
        
        assertEquals( 23.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 24.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 27.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 28.0, grid.getRow( 1 ).get( 11 ) );
    }

    @Test
    public void testGetDataElementOrgUnitReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Prescriptions",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, new ArrayList<OrganisationUnit>(), new ArrayList<OrganisationUnit>(),
            groups, null, true, true, false, new RelativePeriods(), null, i18nFormat, "january_2000" );

        reportTable.setParentOrganisationUnit( unitA );
        
        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 21.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 23.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 25.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 27.0, grid.getRow( 0 ).get( 11 ) );
        
        assertEquals( 22.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 24.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 26.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 28.0, grid.getRow( 1 ).get( 11 ) );
    }
    
    @Test
    public void testGetDataElementOrgUnitReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, new ArrayList<OrganisationUnit>(), new ArrayList<OrganisationUnit>(), 
            groups, null, false, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        reportTable.setParentOrganisationUnit( unitA );
        
        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );
        
        assertEquals( 21.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 22.0, grid.getRow( 0 ).get( 14 ) );
        
        assertEquals( 23.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 24.0, grid.getRow( 1 ).get( 14 ) );
        
        assertEquals( 25.0, grid.getRow( 2 ).get( 13 ) );
        assertEquals( 26.0, grid.getRow( 2 ).get( 14 ) );
        
        assertEquals( 27.0, grid.getRow( 3 ).get( 13 ) );
        assertEquals( 28.0, grid.getRow( 3 ).get( 14 ) );
    }

    @Test
    public void testGetDataElementOrgUnitReportTableC()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, new ArrayList<OrganisationUnit>(), new ArrayList<OrganisationUnit>(), 
            groups, null, true, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        reportTable.setParentOrganisationUnit( unitA );
        
        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );
        
        assertEquals( 21.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 22.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 25.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 26.0, grid.getRow( 0 ).get( 11 ) );
        
        assertEquals( 23.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 24.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 27.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 28.0, grid.getRow( 1 ).get( 11 ) );
    }
    
    @Test
    public void testGetIndicatorReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Prescriptions",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(),
            new ArrayList<OrganisationUnitGroup>(), null, true, true, false, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, null, 0 );
        
        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 13.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 17.0, grid.getRow( 0 ).get( 11 ) );
        
        assertEquals( 12.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 16.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 11 ) );
    }
    
    @Test
    public void testGetIndicatorReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, false, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 11.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 14 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 14 ) );
        
        assertEquals( 15.0, grid.getRow( 2 ).get( 13 ) );
        assertEquals( 16.0, grid.getRow( 2 ).get( 14 ) );
        
        assertEquals( 17.0, grid.getRow( 3 ).get( 13 ) );
        assertEquals( 18.0, grid.getRow( 3 ).get( 14 ) );
    }

    @Test
    public void testGetIndicatorReportTableC()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 11 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 17.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 11 ) );
    }
    
    @Test
    public void testGetDataElementReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Prescriptions",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(),
            new ArrayList<OrganisationUnitGroup>(), null, true, true, false, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 13.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 17.0, grid.getRow( 0 ).get( 11 ) );
        
        assertEquals( 12.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 16.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 11 ) );
    }
    
    @Test
    public void testGetDataElementReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, false, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );
        
        assertEquals( 11.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 14 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 14 ) );
        
        assertEquals( 15.0, grid.getRow( 2 ).get( 13 ) );
        assertEquals( 16.0, grid.getRow( 2 ).get( 14 ) );
        
        assertEquals( 17.0, grid.getRow( 3 ).get( 13 ) );
        assertEquals( 18.0, grid.getRow( 3 ).get( 14 ) );
    }

    @Test
    public void testGetDataElementReportTableC()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );
        
        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 11 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 17.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 11 ) );
    }
    
    @Test
    public void testGetDataSetReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Prescriptions",
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, new ArrayList<OrganisationUnit>(),
            new ArrayList<OrganisationUnitGroup>(), null, true, true, false, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );
        
        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 13.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 17.0, grid.getRow( 0 ).get( 11 ) );
        
        assertEquals( 12.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 16.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 11 ) );
    }
    
    @Test
    public void testGetDataSetReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, false, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 11.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 14 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 14 ) );
        
        assertEquals( 15.0, grid.getRow( 2 ).get( 13 ) );
        assertEquals( 16.0, grid.getRow( 2 ).get( 14 ) );
        
        assertEquals( 17.0, grid.getRow( 3 ).get( 13 ) );
        assertEquals( 18.0, grid.getRow( 3 ).get( 14 ) );
    }

    @Test
    public void testGetDataSetReportTableC()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 11 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 17.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 11 ) );
    }

    @Test
    public void testGetCategoryComboReportTableA()
    {
        BatchHandler<AggregatedDataValue> batchHandler = batchHandlerFactory.createBatchHandler( AggregatedDataValueBatchHandler.class ).init();
        
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdA, 8, unitIdA, 8, 11 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdA, 8, unitIdB, 8, 12 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdB, 8, unitIdA, 8, 13 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdB, 8, unitIdB, 8, 14 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdA, 8, unitIdA, 8, 15 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdA, 8, unitIdB, 8, 16 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdB, 8, unitIdA, 8, 17 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdB, 8, unitIdB, 8, 18 ) );  
        
        batchHandler.flush();
        
        ReportTable reportTable = new ReportTable( "Prescriptions",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(),
            new ArrayList<OrganisationUnitGroup>(), categoryComboA, true, true, false, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );
        
        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 11.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 13.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 13.0, grid.getRow( 0 ).get( 11 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 12 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 17.0, grid.getRow( 0 ).get( 14 ) );
        assertEquals( 17.0, grid.getRow( 0 ).get( 15 ) );

        assertEquals( 12.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 12.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 11 ) );
        assertEquals( 16.0, grid.getRow( 1 ).get( 12 ) );
        assertEquals( 16.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 14 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 15 ) );
    }

    @Test
    public void testGetCategoryComboReportTableB()
    {
        BatchHandler<AggregatedDataValue> batchHandler = batchHandlerFactory.createBatchHandler( AggregatedDataValueBatchHandler.class ).init();
        
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdA, 8, unitIdA, 8, 11 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdA, 8, unitIdB, 8, 12 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdB, 8, unitIdA, 8, 13 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdB, 8, unitIdB, 8, 14 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdA, 8, unitIdA, 8, 15 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdA, 8, unitIdB, 8, 16 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdB, 8, unitIdA, 8, 17 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdB, 8, unitIdB, 8, 18 ) );  
        
        batchHandler.flush();
        
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), categoryComboA, false, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );
        
        assertEquals( 11.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 11.0, grid.getRow( 0 ).get( 14 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 15 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 16 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 13.0, grid.getRow( 1 ).get( 14 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 15 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 16 ) );
        
        assertEquals( 15.0, grid.getRow( 2 ).get( 13 ) );
        assertEquals( 15.0, grid.getRow( 2 ).get( 14 ) );
        assertEquals( 16.0, grid.getRow( 2 ).get( 15 ) );
        assertEquals( 16.0, grid.getRow( 2 ).get( 16 ) );

        assertEquals( 17.0, grid.getRow( 3 ).get( 13 ) );
        assertEquals( 17.0, grid.getRow( 3 ).get( 14 ) );
        assertEquals( 18.0, grid.getRow( 3 ).get( 15 ) );
        assertEquals( 18.0, grid.getRow( 3 ).get( 16 ) );
    }

    @Test
    public void testGetCategoryComboReportTableC()
    {
        BatchHandler<AggregatedDataValue> batchHandler = batchHandlerFactory.createBatchHandler( AggregatedDataValueBatchHandler.class ).init();
        
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdA, 8, unitIdA, 8, 11 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdA, 8, unitIdB, 8, 12 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdB, 8, unitIdA, 8, 13 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdB, 8, unitIdB, 8, 14 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdA, 8, unitIdA, 8, 15 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdA, 8, unitIdB, 8, 16 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdB, 8, unitIdA, 8, 17 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdB, 8, unitIdB, 8, 18 ) );  
        
        batchHandler.flush();
        
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), categoryComboA, true, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );
                
        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 11.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 11 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 12 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 14 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 15 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 13.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 11 ) );
        assertEquals( 17.0, grid.getRow( 1 ).get( 12 ) );
        assertEquals( 17.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 14 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 15 ) );
    }

    @Test
    public void testGetCategoryComboReportTableTotal()
    {
        BatchHandler<AggregatedDataValue> batchHandler = batchHandlerFactory.createBatchHandler( AggregatedDataValueBatchHandler.class ).init();
        
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdA, 8, unitIdA, 8, 11 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdA, 8, unitIdB, 8, 12 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdB, 8, unitIdA, 8, 13 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdA, categoryOptionComboIdB, periodIdB, 8, unitIdB, 8, 14 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdA, 8, unitIdA, 8, 15 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdA, 8, unitIdB, 8, 16 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdB, 8, unitIdA, 8, 17 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementIdB, categoryOptionComboIdB, periodIdB, 8, unitIdB, 8, 18 ) );  
        
        batchHandler.flush();
        
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), categoryComboA, false, false, false, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 11.0, grid.getRow( 0 ).get( 18 ) );
        assertEquals( 11.0, grid.getRow( 0 ).get( 19 ) );
        assertEquals( 22.0, grid.getRow( 0 ).get( 20 ) );

        assertEquals( 12.0, grid.getRow( 1 ).get( 18 ) );
        assertEquals( 12.0, grid.getRow( 1 ).get( 19 ) );
        assertEquals( 24.0, grid.getRow( 1 ).get( 20 ) );

        assertEquals( 13.0, grid.getRow( 2 ).get( 18 ) );
        assertEquals( 13.0, grid.getRow( 2 ).get( 19 ) );
        assertEquals( 26.0, grid.getRow( 2 ).get( 20 ) );

        assertEquals( 14.0, grid.getRow( 3 ).get( 18 ) );
        assertEquals( 14.0, grid.getRow( 3 ).get( 19 ) );
        assertEquals( 28.0, grid.getRow( 3 ).get( 20 ) );
        
        assertEquals( 15.0, grid.getRow( 4 ).get( 18 ) );
        assertEquals( 15.0, grid.getRow( 4 ).get( 19 ) );
        assertEquals( 30.0, grid.getRow( 4 ).get( 20 ) );

        assertEquals( 16.0, grid.getRow( 5 ).get( 18 ) );
        assertEquals( 16.0, grid.getRow( 5 ).get( 19 ) );
        assertEquals( 32.0, grid.getRow( 5 ).get( 20 ) );

        assertEquals( 17.0, grid.getRow( 6 ).get( 18 ) );
        assertEquals( 17.0, grid.getRow( 6 ).get( 19 ) );
        assertEquals( 34.0, grid.getRow( 6 ).get( 20 ) );

        assertEquals( 18.0, grid.getRow( 7 ).get( 18 ) );
        assertEquals( 18.0, grid.getRow( 7 ).get( 19 ) );
        assertEquals( 36.0, grid.getRow( 7 ).get( 20 ) );
    }
    
    @Test
    public void testGetMultiReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Prescriptions",
            dataElements, indicators, dataSets, periods, relativePeriods, units, new ArrayList<OrganisationUnit>(),
            new ArrayList<OrganisationUnitGroup>(), null, true, true, false, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 13.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 17.0, grid.getRow( 0 ).get( 11 ) );
        assertEquals( 11.0, grid.getRow( 0 ).get( 12 ) );
        assertEquals( 13.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 14 ) );
        assertEquals( 17.0, grid.getRow( 0 ).get( 15 ) );
        assertEquals( 11.0, grid.getRow( 0 ).get( 16 ) );
        assertEquals( 13.0, grid.getRow( 0 ).get( 17 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 18 ) );
        assertEquals( 17.0, grid.getRow( 0 ).get( 19 ) );
        
        assertEquals( 12.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 16.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 11 ) );
        assertEquals( 12.0, grid.getRow( 1 ).get( 12 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 16.0, grid.getRow( 1 ).get( 14 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 15 ) );
        assertEquals( 12.0, grid.getRow( 1 ).get( 16 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 17 ) );
        assertEquals( 16.0, grid.getRow( 1 ).get( 18 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 19 ) );        
    }
    
    @Test
    public void testGetMultiReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, indicators, dataSets, periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, false, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );
        
        assertEquals( 11.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 14 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 14 ) );
        
        assertEquals( 15.0, grid.getRow( 2 ).get( 13 ) );
        assertEquals( 16.0, grid.getRow( 2 ).get( 14 ) );
        
        assertEquals( 17.0, grid.getRow( 3 ).get( 13 ) );
        assertEquals( 18.0, grid.getRow( 3 ).get( 14 ) );
        
        assertEquals( 11.0, grid.getRow( 4 ).get( 13 ) );
        assertEquals( 12.0, grid.getRow( 4 ).get( 14 ) );
        
        assertEquals( 13.0, grid.getRow( 5 ).get( 13 ) );
        assertEquals( 14.0, grid.getRow( 5 ).get( 14 ) );
        
        assertEquals( 15.0, grid.getRow( 6 ).get( 13 ) );
        assertEquals( 16.0, grid.getRow( 6 ).get( 14 ) );
        
        assertEquals( 17.0, grid.getRow( 7 ).get( 13 ) );
        assertEquals( 18.0, grid.getRow( 7 ).get( 14 ) );
        
        assertEquals( 11.0, grid.getRow( 8 ).get( 13 ) );
        assertEquals( 12.0, grid.getRow( 8 ).get( 14 ) );
        
        assertEquals( 13.0, grid.getRow( 9 ).get( 13 ) );
        assertEquals( 14.0, grid.getRow( 9 ).get( 14 ) );
        
        assertEquals( 15.0, grid.getRow( 10 ).get( 13 ) );
        assertEquals( 16.0, grid.getRow( 10 ).get( 14 ) );
        
        assertEquals( 17.0, grid.getRow( 11 ).get( 13 ) );
        assertEquals( 18.0, grid.getRow( 11 ).get( 14 ) );
    }
    
    @Test
    public void testGetMultiReportTableC()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, indicators, dataSets, periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 11 ) );
        assertEquals( 11.0, grid.getRow( 0 ).get( 12 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 14 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 15 ) );
        assertEquals( 11.0, grid.getRow( 0 ).get( 16 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 17 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 18 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 19 ) );

        assertEquals( 13.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 17.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 11 ) );
        assertEquals( 13.0, grid.getRow( 1 ).get( 12 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 17.0, grid.getRow( 1 ).get( 14 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 15 ) );
        assertEquals( 13.0, grid.getRow( 1 ).get( 16 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 17 ) );
        assertEquals( 17.0, grid.getRow( 1 ).get( 18 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 19 ) );
    }

    @Test
    public void testGetIndicatorReportTableColumnsOnly()
    {
        ReportTable reportTable = new ReportTable( "Prescriptions",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(),
            new ArrayList<OrganisationUnitGroup>(), null, true, true, true, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 11.0, grid.getRow( 0 ).get( 3 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 4 ) );
        assertEquals( 13.0, grid.getRow( 0 ).get( 5 ) );
        assertEquals( 14.0, grid.getRow( 0 ).get( 6 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 7 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 17.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 18.0, grid.getRow( 0 ).get( 10 ) );
    }

    @Test
    public void testGetIndicatorReportTableRowsOnly()
    {
        ReportTable reportTable = new ReportTable( "Prescriptions",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(),
            new ArrayList<OrganisationUnitGroup>(), null, false, false, false, new RelativePeriods(), null, i18nFormat, "january_2000" );

        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 11.0, grid.getRow( 0 ).get( 18 ) );
        assertEquals( 12.0, grid.getRow( 1 ).get( 18 ) );
        assertEquals( 13.0, grid.getRow( 2 ).get( 18 ) );
        assertEquals( 14.0, grid.getRow( 3 ).get( 18 ) );
        assertEquals( 15.0, grid.getRow( 4 ).get( 18 ) );
        assertEquals( 16.0, grid.getRow( 5 ).get( 18 ) );
        assertEquals( 17.0, grid.getRow( 6 ).get( 18 ) );
        assertEquals( 18.0, grid.getRow( 7 ).get( 18 ) );
    }

    @Test
    public void testGetIndicatorReportTableTopLimit()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, false, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );
        reportTable.setTopLimit( 2 );
        
        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 2, grid.getHeight() );
        
        assertEquals( 11.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 14 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 14 ) );
    }

    @Test
    public void testGetIndicatorReportTableSortOrder()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, false, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );
        reportTable.setSortOrder( ReportTable.DESC );
        
        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );

        assertEquals( 17.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 18.0, grid.getRow( 0 ).get( 14 ) );

        assertEquals( 15.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 16.0, grid.getRow( 1 ).get( 14 ) );

        assertEquals( 13.0, grid.getRow( 2 ).get( 13 ) );
        assertEquals( 14.0, grid.getRow( 2 ).get( 14 ) );
        
        assertEquals( 11.0, grid.getRow( 3 ).get( 13 ) );
        assertEquals( 12.0, grid.getRow( 3 ).get( 14 ) );
    }

    @Test
    public void testGetDataElementReportTableRegression()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, false, true, new RelativePeriods(), null, i18nFormat, "january_2000" );
        
        reportTable.setRegression( true );
        
        int id = reportTableService.saveReportTable( reportTable );

        Grid grid = reportTableService.getReportTableGrid( id, i18nFormat, date, 0 );
        
        assertEquals( 11.0, grid.getRow( 0 ).get( 8 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 9 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 10 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 11 ) );

        assertEquals( 11.0, grid.getRow( 0 ).get( 12 ) );
        assertEquals( 12.0, grid.getRow( 0 ).get( 13 ) );
        assertEquals( 15.0, grid.getRow( 0 ).get( 14 ) );
        assertEquals( 16.0, grid.getRow( 0 ).get( 15 ) );
        
        assertEquals( 13.0, grid.getRow( 1 ).get( 8 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 9 ) );
        assertEquals( 17.0, grid.getRow( 1 ).get( 10 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 11 ) );

        assertEquals( 13.0, grid.getRow( 1 ).get( 12 ) );
        assertEquals( 14.0, grid.getRow( 1 ).get( 13 ) );
        assertEquals( 17.0, grid.getRow( 1 ).get( 14 ) );
        assertEquals( 18.0, grid.getRow( 1 ).get( 15 ) );
    }    
}
