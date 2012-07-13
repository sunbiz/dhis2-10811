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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hisp.dhis.reporttable.ReportTable.DATAELEMENT_ID;
import static org.hisp.dhis.reporttable.ReportTable.INDICATOR_ID;
import static org.hisp.dhis.reporttable.ReportTable.PERIOD_ID;
import static org.hisp.dhis.reporttable.ReportTable.ORGANISATIONUNITGROUP_ID;
import static org.hisp.dhis.reporttable.ReportTable.getColumnName;
import static org.hisp.dhis.reporttable.ReportTable.getIdentifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hisp.dhis.DhisTest;
import org.hisp.dhis.common.NameableObject;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.mock.MockI18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.RelativePeriods;
import org.junit.Test;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ReportTableTest
    extends DhisTest
{
    private List<DataElement> dataElements;
    private List<DataElementCategoryOptionCombo> categoryOptionCombos;
    private List<Indicator> indicators;
    private List<DataSet> dataSets;
    private List<Period> periods;
    private List<Period> relativePeriods;
    private List<OrganisationUnit> units;
    private List<OrganisationUnit> relativeUnits;
    private List<OrganisationUnitGroup> groups;

    private PeriodType montlyPeriodType;

    private DataElement dataElementA;
    private DataElement dataElementB;
    
    private DataElementCategoryOptionCombo categoryOptionComboA;
    private DataElementCategoryOptionCombo categoryOptionComboB;

    private DataElementCategoryCombo categoryCombo;
    
    private IndicatorType indicatorType;
    
    private Indicator indicatorA;
    private Indicator indicatorB;

    private DataSet dataSetA;
    private DataSet dataSetB;
    
    private Period periodA;
    private Period periodB;
    private Period periodC;
    private Period periodD;
    
    private OrganisationUnit unitA;
    private OrganisationUnit unitB;
    
    private OrganisationUnitGroup groupA;
    private OrganisationUnitGroup groupB;
    
    private RelativePeriods relatives;

    private I18nFormat i18nFormat;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
        throws Exception
    {
        dataElements = new ArrayList<DataElement>();
        categoryOptionCombos = new ArrayList<DataElementCategoryOptionCombo>();
        indicators = new ArrayList<Indicator>();
        dataSets = new ArrayList<DataSet>();
        periods = new ArrayList<Period>();
        relativePeriods = new ArrayList<Period>();
        units = new ArrayList<OrganisationUnit>();
        relativeUnits = new ArrayList<OrganisationUnit>();
        groups = new ArrayList<OrganisationUnitGroup>();
        
        montlyPeriodType = PeriodType.getPeriodTypeByName( MonthlyPeriodType.NAME );

        dataElementA = createDataElement( 'A' );
        dataElementB = createDataElement( 'B' );
        
        dataElementA.setId( 'A' );
        dataElementB.setId( 'B' );
        
        dataElements.add( dataElementA );
        dataElements.add( dataElementB );
        
        categoryOptionComboA = createCategoryOptionCombo( 'A', 'A', 'B' );
        categoryOptionComboB = createCategoryOptionCombo( 'B', 'C', 'D' );
        
        categoryOptionComboA.setId( 'A' );
        categoryOptionComboB.setId( 'B' );
        
        categoryOptionCombos.add( categoryOptionComboA );
        categoryOptionCombos.add( categoryOptionComboB );

        categoryCombo = new DataElementCategoryCombo( "CategoryComboA" );
        categoryCombo.setId( 'A' );
        categoryCombo.setOptionCombos( new HashSet<DataElementCategoryOptionCombo>( categoryOptionCombos ) );
        
        indicatorType = createIndicatorType( 'A' );

        indicatorA = createIndicator( 'A', indicatorType );
        indicatorB = createIndicator( 'B', indicatorType );
        
        indicatorA.setId( 'A' );
        indicatorB.setId( 'B' );
                
        indicators.add( indicatorA );
        indicators.add( indicatorB );
        
        dataSetA = createDataSet( 'A', montlyPeriodType );
        dataSetB = createDataSet( 'B', montlyPeriodType );
        
        dataSetA.setId( 'A' );
        dataSetB.setId( 'B' );
        
        dataSets.add( dataSetA );
        dataSets.add( dataSetB );        
        
        periodA = createPeriod( montlyPeriodType, getDate( 2008, 1, 1 ), getDate( 2008, 1, 31 ) );
        periodB = createPeriod( montlyPeriodType, getDate( 2008, 2, 1 ), getDate( 2008, 2, 28 ) );
        
        periodA.setId( 'A' );
        periodB.setId( 'B' );
        
        periods.add( periodA );
        periods.add( periodB );
        
        periodC = createPeriod( montlyPeriodType, getDate( 2008, 3, 1 ), getDate( 2008, 3, 31 ) );
        periodD = createPeriod( montlyPeriodType, getDate( 2008, 4, 1 ), getDate( 2008, 4, 30 ) );
        
        periodC.setId( 'C' );
        periodD.setId( 'D' );
        
        periodC.setName( RelativePeriods.REPORTING_MONTH );
        periodD.setName( RelativePeriods.THIS_YEAR );
        
        relativePeriods.add( periodC );
        relativePeriods.add( periodD );
        
        unitA = createOrganisationUnit( 'A' );
        unitB = createOrganisationUnit( 'B' );
        
        unitA.setId( 'A' );
        unitB.setId( 'B' );
        
        units.add( unitA );
        units.add( unitB );
        relativeUnits.add( unitA );
        
        groupA = createOrganisationUnitGroup( 'A' );
        groupB = createOrganisationUnitGroup( 'B' );
        
        groupA.setId( 'A' );
        groupB.setId( 'B' );
        
        groups.add( groupA );
        groups.add( groupB );
        
        relatives = new RelativePeriods();
        
        relatives.setReportingMonth( true );
        relatives.setThisYear( true );

        i18nFormat = new MockI18nFormat();
    }
    
    private static List<NameableObject> getList( NameableObject... objects )
    {
        return Arrays.asList( objects );
    }

    private static List<String> getColumnNames( List<List<NameableObject>> cols )
    {
        List<String> columns = new ArrayList<String>();
        
        for ( List<NameableObject> column : cols )
        {
            columns.add( getColumnName( column ) );
        }
        
        return columns;
    }
    
    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Test
    public void testGetIdentifierA()
    {
        List<NameableObject> a1 = getList( unitA, periodA );
        List<NameableObject> a2 = getList( indicatorA );
        
        List<NameableObject> b1 = getList( periodA );
        List<NameableObject> b2 = getList( indicatorA, unitA );
        
        List<NameableObject> c1 = getList( groupA, periodA );
        List<NameableObject> c2 = getList( indicatorA );
        
        assertNotNull( getIdentifier( a1, a2 ) );
        assertNotNull( getIdentifier( b1, b2 ) );
        assertNotNull( getIdentifier( c1, c2 ) );
        assertEquals( getIdentifier( a1, a2 ), getIdentifier( b1, b2 ) );
        
        String identifier = getIdentifier( getIdentifier( unitA.getClass(), unitA.getId() ), 
            getIdentifier( periodA.getClass(), periodA.getId() ), getIdentifier( indicatorA.getClass(), indicatorA.getId() ) );
        
        assertEquals( getIdentifier( a1, a2 ), identifier );

        identifier = getIdentifier( getIdentifier( periodA.getClass(), periodA.getId() ), 
            getIdentifier( indicatorA.getClass(), indicatorA.getId() ), getIdentifier( unitA.getClass(), unitA.getId() ) );
        
        assertEquals( getIdentifier( b1, b2 ), identifier );
        
        identifier = getIdentifier( getIdentifier( groupA.getClass(), groupA.getId() ),
            getIdentifier( periodA.getClass(), periodA.getId() ), getIdentifier( indicatorA.getClass(), indicatorA.getId() ) );
        
        assertEquals( getIdentifier( c1, c2 ), identifier );
    }

    @Test
    public void testGetIdentifierB()
    {
        String a1 = getIdentifier( Indicator.class, 1 );
        String a2 = getIdentifier( DataElement.class, 2 );
        
        assertEquals( INDICATOR_ID + 1, a1 );
        assertEquals( DATAELEMENT_ID + 2, a2 );
        
        String b1 = getIdentifier( Indicator.class, 2 );
        String b2 = getIdentifier( DataElement.class, 1 );
        
        assertEquals( INDICATOR_ID + 2, b1 );
        assertEquals( DATAELEMENT_ID + 1, b2 );
        
        String c1 = getIdentifier( OrganisationUnitGroup.class, 1 );
        String c2 = getIdentifier( Period.class, 2 );
        
        assertEquals( getIdentifier( ORGANISATIONUNITGROUP_ID + 1 ), c1 );
        assertEquals( getIdentifier( PERIOD_ID + 2 ), c2 );
        
        assertFalse( getIdentifier( a1, a2 ).equals( getIdentifier( b1, b2 ) ) );        
    }
    
    @Test
    public void testGetIdentifierC()
    {
        List<NameableObject> a1 = getList( dataElementA, periodA, categoryOptionComboA );
        List<NameableObject> a2 = getList( unitA );
        
        String b1 = getIdentifier( DataElement.class, 'A' );
        String b2 = getIdentifier( Period.class, 'A' );
        String b3 = getIdentifier( DataElementCategoryOptionCombo.class, 'A' );
        String b4 = getIdentifier( OrganisationUnit.class, 'A' );
        
        String a = getIdentifier( a1, a2 );
        String b = getIdentifier( b1, b2, b3, b4 );
        
        assertEquals( a, b );
    }

    @Test
    public void testGetIdentifierD()
    {
        List<NameableObject> a1 = getList( dataElementA, periodA, categoryOptionComboA );
        List<NameableObject> a2 = getList( groupA );
        
        String b1 = getIdentifier( DataElement.class, 'A' );
        String b2 = getIdentifier( Period.class, 'A' );
        String b3 = getIdentifier( DataElementCategoryOptionCombo.class, 'A' );
        String b4 = getIdentifier( OrganisationUnitGroup.class, 'A' );
        
        String a = getIdentifier( a1, a2 );
        String b = getIdentifier( b1, b2, b3, b4 );
        
        assertEquals( a, b );
    }
    
    @Test
    public void testGetColumnName()
    {
        List<NameableObject> a1 = getList( unitA, periodC );
        
        assertNotNull( getColumnName( a1 ) );
        assertEquals( "organisationunitshorta_reporting_month", getColumnName( a1 ) );
        
        List<NameableObject> a2 = getList( unitB, periodD );

        assertNotNull( getColumnName( a2 ) );
        assertEquals( "organisationunitshortb_year", getColumnName( a2 ) );
        
        List<NameableObject> a3 = getList( groupA, indicatorA );
        
        assertNotNull( getColumnName( a3 ) );
        assertEquals( "organisationunitgroupa_indicatorshorta", getColumnName( a3 ) );
    }

    @Test
    public void testOrganisationUnitGroupReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, new ArrayList<OrganisationUnit>(), relativeUnits, 
            groups, null, true, true, false, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();
        
        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.ORGANISATIONUNIT_NAME ) );
        
        List<List<NameableObject>> columns = reportTable.getColumns();

        assertNotNull( columns ); 
        assertEquals( 8, columns.size() );
        
        Iterator<List<NameableObject>> iterator = columns.iterator();
        
        assertEquals( getList( indicatorA, periodA ), iterator.next() );
        assertEquals( getList( indicatorA, periodB ), iterator.next() );
        assertEquals( getList( indicatorA, periodC ), iterator.next() );
        assertEquals( getList( indicatorA, periodD ), iterator.next() );
        assertEquals( getList( indicatorB, periodA ), iterator.next() );
        assertEquals( getList( indicatorB, periodB ), iterator.next() );
        assertEquals( getList( indicatorB, periodC ), iterator.next() );
        assertEquals( getList( indicatorB, periodD ), iterator.next() );
            
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );        
        assertEquals( 8, columnNames.size() );
        
        assertTrue( columnNames.contains( "indicatorshorta_reporting_month" ) );
        assertTrue( columnNames.contains( "indicatorshorta_year" ) );
        assertTrue( columnNames.contains( "indicatorshortb_reporting_month" ) );
        assertTrue( columnNames.contains( "indicatorshortb_year" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 2, rows.size() );
        
        iterator = rows.iterator();
        
        assertEquals( getList( groupA ), iterator.next() );
        assertEquals( getList( groupB ), iterator.next() );
    }

    @Test
    public void testOrganisationUnitGroupReportTableB()
    {        
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, new ArrayList<OrganisationUnit>(), relativeUnits, 
            groups, null, true, false, true, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 4, columns.size() );

        Iterator<List<NameableObject>> iterator = columns.iterator();
        
        assertEquals( getList( indicatorA, groupA ), iterator.next() );
        assertEquals( getList( indicatorA, groupB ), iterator.next() );
        assertEquals( getList( indicatorB, groupA ), iterator.next() );
        assertEquals( getList( indicatorB, groupB ), iterator.next() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 4, columnNames.size() );
        
        assertTrue( columnNames.contains( "indicatorshorta_organisationunitgroupa" ) );
        assertTrue( columnNames.contains( "indicatorshorta_organisationunitgroupb" ) );
        assertTrue( columnNames.contains( "indicatorshortb_organisationunitgroupa" ) );
        assertTrue( columnNames.contains( "indicatorshortb_organisationunitgroupb" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 4, rows.size() );

        iterator = rows.iterator();
        
        assertEquals( getList( periodA ), iterator.next() );
        assertEquals( getList( periodB ), iterator.next() );
        assertEquals( getList( periodC ), iterator.next() );
        assertEquals( getList( periodD ), iterator.next() );
    }
    
    @Test
    public void testIndicatorReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, true, false, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();
        
        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.ORGANISATIONUNIT_NAME ) );
        
        List<List<NameableObject>> columns = reportTable.getColumns();

        assertNotNull( columns ); 
        assertEquals( 8, columns.size() );
        
        Iterator<List<NameableObject>> iterator = columns.iterator();
        
        assertEquals( getList( indicatorA, periodA ), iterator.next() );
        assertEquals( getList( indicatorA, periodB ), iterator.next() );
        assertEquals( getList( indicatorA, periodC ), iterator.next() );
        assertEquals( getList( indicatorA, periodD ), iterator.next() );
        assertEquals( getList( indicatorB, periodA ), iterator.next() );
        assertEquals( getList( indicatorB, periodB ), iterator.next() );
        assertEquals( getList( indicatorB, periodC ), iterator.next() );
        assertEquals( getList( indicatorB, periodD ), iterator.next() );
            
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );        
        assertEquals( 8, columnNames.size() );
        
        assertTrue( columnNames.contains( "indicatorshorta_reporting_month" ) );
        assertTrue( columnNames.contains( "indicatorshorta_year" ) );
        assertTrue( columnNames.contains( "indicatorshortb_reporting_month" ) );
        assertTrue( columnNames.contains( "indicatorshortb_year" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 2, rows.size() );
        
        iterator = rows.iterator();
        
        assertEquals( getList( unitA ), iterator.next() );
        assertEquals( getList( unitB ), iterator.next() );
    }

    @Test
    public void testIndicatorReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, false, false, true, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 2, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.INDICATOR_ID ) );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );        

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 2, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.INDICATOR_NAME ) );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );        

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 2, columns.size() );

        Iterator<List<NameableObject>> iterator = columns.iterator();
        
        assertEquals( getList( unitA ), iterator.next() );
        assertEquals( getList( unitB ), iterator.next() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 2, columnNames.size() );
        
        assertTrue( columnNames.contains( "organisationunitshorta" ) );
        assertTrue( columnNames.contains( "organisationunitshortb" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 8, rows.size() );
        
        iterator = rows.iterator();
        
        assertEquals( getList( indicatorA, periodA ), iterator.next() );
        assertEquals( getList( indicatorA, periodB ), iterator.next() );
        assertEquals( getList( indicatorA, periodC ), iterator.next() );
        assertEquals( getList( indicatorA, periodD ), iterator.next() );
        assertEquals( getList( indicatorB, periodA ), iterator.next() );
        assertEquals( getList( indicatorB, periodB ), iterator.next() );
        assertEquals( getList( indicatorB, periodC ), iterator.next() );
        assertEquals( getList( indicatorB, periodD ), iterator.next() );            
    }

    @Test
    public void testIndicatorReportTableC()
    {        
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, false, true, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 4, columns.size() );

        Iterator<List<NameableObject>> iterator = columns.iterator();
        
        assertEquals( getList( indicatorA, unitA ), iterator.next() );
        assertEquals( getList( indicatorA, unitB ), iterator.next() );
        assertEquals( getList( indicatorB, unitA ), iterator.next() );
        assertEquals( getList( indicatorB, unitB ), iterator.next() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 4, columnNames.size() );
        
        assertTrue( columnNames.contains( "indicatorshorta_organisationunitshorta" ) );
        assertTrue( columnNames.contains( "indicatorshorta_organisationunitshortb" ) );
        assertTrue( columnNames.contains( "indicatorshortb_organisationunitshorta" ) );
        assertTrue( columnNames.contains( "indicatorshortb_organisationunitshortb" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 4, rows.size() );

        iterator = rows.iterator();
        
        assertEquals( getList( periodA ), iterator.next() );
        assertEquals( getList( periodB ), iterator.next() );
        assertEquals( getList( periodC ), iterator.next() );
        assertEquals( getList( periodD ), iterator.next() );
    }
    
    @Test
    public void testIndicatorReportTableColumnsOnly()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, true, true, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 0, indexColumns.size() );
        
        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 0, indexNameColumns.size() );
        
        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 16, columns.size() );
        
        List<List<NameableObject>> rows = reportTable.getRows();

        assertNotNull( rows );
        assertEquals( 1, rows.size() );
    }

    @Test
    public void testIndicatorReportTableRowsOnly()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), indicators, new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, false, false, false, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 3, indexColumns.size() );
        
        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 3, indexNameColumns.size() );
        
        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 1, columns.size() );
        
        List<List<NameableObject>> rows = reportTable.getRows();

        assertNotNull( rows );
        assertEquals( 16, rows.size() );
    }

    @Test
    public void testDataElementReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, true, false, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();
        
        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.ORGANISATIONUNIT_NAME ) );

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 8, columns.size() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 8, columnNames.size() );
        
        assertTrue( columnNames.contains( "dataelementshorta_year" ) );
        assertTrue( columnNames.contains( "dataelementshorta_reporting_month" ) );
        assertTrue( columnNames.contains( "dataelementshortb_year" ) );
        assertTrue( columnNames.contains( "dataelementshortb_reporting_month" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 2, rows.size() );
    }

    @Test
    public void testDataElementReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, false, false, true, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 2, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.INDICATOR_ID ) );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 2, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.INDICATOR_NAME ) );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 2, columns.size() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 2, columnNames.size() );
        
        assertTrue( columnNames.contains( "organisationunitshorta" ) );
        assertTrue( columnNames.contains( "organisationunitshortb" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 8, rows.size() );        
    }

    @Test
    public void testDataElementReportTableC()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, false, true, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 4, columns.size() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 4, columnNames.size() );
        
        assertTrue( columnNames.contains( "dataelementshorta_organisationunitshorta" ) );
        assertTrue( columnNames.contains( "dataelementshorta_organisationunitshortb" ) );
        assertTrue( columnNames.contains( "dataelementshortb_organisationunitshorta" ) );
        assertTrue( columnNames.contains( "dataelementshortb_organisationunitshortb" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 4, rows.size() );
    }
    
    @Test
    public void testCategoryComboReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), categoryCombo, true, true, false, relatives, null, i18nFormat, "january_2000" );
        
        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> indexNameColumns = reportTable.getIndexNameColumns();
        
        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.ORGANISATIONUNIT_NAME ) );

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 16, columns.size() );
        
        Iterator<List<NameableObject>> iterator = columns.iterator();
        
        assertEquals( getList( dataElementA, periodA, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementA, periodA, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( dataElementA, periodB, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementA, periodB, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( dataElementA, periodC, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementA, periodC, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( dataElementA, periodD, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementA, periodD, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( dataElementB, periodA, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementB, periodA, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( dataElementB, periodB, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementB, periodB, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( dataElementB, periodC, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementB, periodC, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( dataElementB, periodD, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementB, periodD, categoryOptionComboB ), iterator.next() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 16, columnNames.size() );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 2, rows.size() );

        iterator = rows.iterator();
        
        assertEquals( getList( unitA ), iterator.next() );
        assertEquals( getList( unitB ), iterator.next() );
    }
    
    @Test
    public void testCategoryComboReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), categoryCombo, false, false, true, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 2, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.INDICATOR_ID ) );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 2, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.INDICATOR_NAME ) );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );

        List<List<NameableObject>> columns = reportTable.getColumns();

        assertNotNull( columns );
        assertEquals( 4, columns.size() );

        Iterator<List<NameableObject>> iterator = columns.iterator();
        
        assertEquals( getList( unitA, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( unitA, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( unitB, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( unitB, categoryOptionComboB ), iterator.next() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 4, columnNames.size() );

        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 8, rows.size() );

        iterator = rows.iterator();
        
        assertEquals( getList( dataElementA, periodA ), iterator.next() );
        assertEquals( getList( dataElementA, periodB ), iterator.next() );
        assertEquals( getList( dataElementA, periodC ), iterator.next() );
        assertEquals( getList( dataElementA, periodD ), iterator.next() );
        assertEquals( getList( dataElementB, periodA ), iterator.next() );
        assertEquals( getList( dataElementB, periodB ), iterator.next() );
        assertEquals( getList( dataElementB, periodC ), iterator.next() );
        assertEquals( getList( dataElementB, periodD ), iterator.next() );
    }

    @Test
    public void testCategoryComboReportTableC()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            dataElements, new ArrayList<Indicator>(), new ArrayList<DataSet>(), periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), categoryCombo, true, false, true, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 8, columns.size() );
        
        Iterator<List<NameableObject>> iterator = columns.iterator();
        
        assertEquals( getList( dataElementA, unitA, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementA, unitA, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( dataElementA, unitB, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementA, unitB, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( dataElementB, unitA, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementB, unitA, categoryOptionComboB ), iterator.next() );
        assertEquals( getList( dataElementB, unitB, categoryOptionComboA ), iterator.next() );
        assertEquals( getList( dataElementB, unitB, categoryOptionComboB ), iterator.next() );

        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 8, columnNames.size() );

        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 4, rows.size() );

        iterator = rows.iterator();
        
        assertEquals( getList( periodA ), iterator.next() );
        assertEquals( getList( periodB ), iterator.next() );
        assertEquals( getList( periodC ), iterator.next() );
        assertEquals( getList( periodD ), iterator.next() );
    }

    @Test
    public void testDataSetReportTableA()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, true, false, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();
        
        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.ORGANISATIONUNIT_ID ) );
        
        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.ORGANISATIONUNIT_NAME ) );

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 8, columns.size() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 8, columnNames.size() );
        
        assertTrue( columnNames.contains( "datasetshorta_year" ) );
        assertTrue( columnNames.contains( "datasetshorta_reporting_month" ) );
        assertTrue( columnNames.contains( "datasetshortb_year" ) );
        assertTrue( columnNames.contains( "datasetshortb_reporting_month" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 2, rows.size() );
    }

    @Test
    public void testDataSetReportTableB()
    {
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, false, false, true, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 2, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.INDICATOR_ID ) );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );        

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 2, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.INDICATOR_NAME ) );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );        

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 2, columns.size() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 2, columnNames.size() );
        
        assertTrue( columnNames.contains( "organisationunitshorta" ) );
        assertTrue( columnNames.contains( "organisationunitshortb" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 8, rows.size() );
    }

    @Test
    public void testDataSetReportTableC()
    {        
        ReportTable reportTable = new ReportTable( "Embezzlement",
            new ArrayList<DataElement>(), new ArrayList<Indicator>(), dataSets, periods, relativePeriods, units, new ArrayList<OrganisationUnit>(), 
            new ArrayList<OrganisationUnitGroup>(), null, true, false, true, relatives, null, i18nFormat, "january_2000" );

        reportTable.init();
        
        List<String> indexColumns = reportTable.getIndexColumns();

        assertNotNull( indexColumns );
        assertEquals( 1, indexColumns.size() );
        assertTrue( indexColumns.contains( ReportTable.PERIOD_ID ) );

        List<String> indexNameColumns = reportTable.getIndexNameColumns();

        assertNotNull( indexNameColumns );
        assertEquals( 1, indexNameColumns.size() );
        assertTrue( indexNameColumns.contains( ReportTable.PERIOD_NAME ) );

        List<List<NameableObject>> columns = reportTable.getColumns();
        
        assertNotNull( columns );
        assertEquals( 4, columns.size() );
        
        List<String> columnNames = getColumnNames( reportTable.getColumns() );
        
        assertNotNull( columnNames );
        assertEquals( 4, columnNames.size() );
        
        assertTrue( columnNames.contains( "datasetshorta_organisationunitshorta" ) );
        assertTrue( columnNames.contains( "datasetshorta_organisationunitshortb" ) );
        assertTrue( columnNames.contains( "datasetshortb_organisationunitshorta" ) );
        assertTrue( columnNames.contains( "datasetshortb_organisationunitshortb" ) );
        
        List<List<NameableObject>> rows = reportTable.getRows();
        
        assertNotNull( rows );
        assertEquals( 4, rows.size() );
    }
}
