package org.hisp.dhis.dataarchive;

/*
 * Copyright (c) 2004-2008, University of Oslo
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

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hisp.dhis.DhisTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;

/**
 * @author Dang Duy Hieu
 * @version $Id$
 */
public abstract class DataArchiveTest
    extends DhisTest
{
    protected DataArchiveService dataArchiveService;

    protected final String T = "true";

    protected final String F = "false";

    protected DataElementCategoryCombo categoryCombo;

    protected DataElementCategoryOptionCombo categoryOptionCombo;

    protected Collection<Integer> dataElementIds;

    protected Collection<Integer> periodIds;

    protected Collection<Integer> organisationUnitIds;

    protected DataSet dataSetA;

    protected DataSet dataSetB;

    protected DataSet dataSetC;

    protected DataElement dataElementA;

    protected DataElement dataElementB;

    protected DataElement dataElementC;

    protected DataElement dataElementD;

    protected DataElement dataElementE;

    protected DataElement dataElementF;

    protected Period periodA;

    protected Period periodB;

    protected Period periodC;

    protected Period periodD;

    protected OrganisationUnit unitA;

    protected OrganisationUnit unitB;

    protected OrganisationUnit unitC;

    protected OrganisationUnit unitD;

    protected OrganisationUnit unitE;

    protected OrganisationUnit unitF;

    protected OrganisationUnit unitG;

    protected OrganisationUnit unitH;

    protected OrganisationUnit unitI;

    public void setUpDataArchiveTest()
        throws Exception
    {
        dataArchiveService = (DataArchiveService) getBean( DataArchiveService.ID );

        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );

        dataSetService = (DataSetService) getBean( DataSetService.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );

        dataValueService = (DataValueService) getBean( DataValueService.ID );

        indicatorService = (IndicatorService) getBean( IndicatorService.ID );

        periodService = (PeriodService) getBean( PeriodService.ID );

        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );

        organisationUnitGroupService = (OrganisationUnitGroupService) getBean( OrganisationUnitGroupService.ID );

        categoryCombo = categoryService
            .getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );

        categoryOptionCombo = categoryService.getDefaultDataElementCategoryOptionCombo();

        // ---------------------------------------------------------------------
        // Setup identifier Collections
        // ---------------------------------------------------------------------

        dataElementIds = new HashSet<Integer>();
        periodIds = new HashSet<Integer>();
        organisationUnitIds = new HashSet<Integer>();

        // ---------------------------------------------------------------------
        // Setup DataElements
        // ---------------------------------------------------------------------

        dataElementA = super.createDataElement( 'A', DataElement.VALUE_TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM,
            categoryCombo );
        dataElementB = super.createDataElement( 'B', DataElement.VALUE_TYPE_BOOL, DataElement.AGGREGATION_OPERATOR_SUM,
            categoryCombo );
        dataElementC = super.createDataElement( 'C', DataElement.VALUE_TYPE_STRING,
            DataElement.AGGREGATION_OPERATOR_SUM, categoryCombo );
        dataElementD = super.createDataElement( 'D', DataElement.VALUE_TYPE_INT, DataElement.AGGREGATION_OPERATOR_SUM,
            categoryCombo );
        dataElementE = super.createDataElement( 'E', DataElement.VALUE_TYPE_BOOL, DataElement.AGGREGATION_OPERATOR_SUM,
            categoryCombo );
        dataElementF = super.createDataElement( 'F', DataElement.VALUE_TYPE_STRING,
            DataElement.AGGREGATION_OPERATOR_SUM, categoryCombo );

        dataElementIds.add( dataElementService.addDataElement( dataElementA ) );
        dataElementIds.add( dataElementService.addDataElement( dataElementB ) );
        dataElementIds.add( dataElementService.addDataElement( dataElementC ) );
        dataElementIds.add( dataElementService.addDataElement( dataElementD ) );
        dataElementIds.add( dataElementService.addDataElement( dataElementE ) );
        dataElementIds.add( dataElementService.addDataElement( dataElementF ) );

        Set<DataElement> dataElementsA = new HashSet<DataElement>();
        Set<DataElement> dataElementsB = new HashSet<DataElement>();
        Set<DataElement> dataElementsC = new HashSet<DataElement>();

        // One dataElement
        dataElementsA.add( dataElementA );

        // Two dataElements
        dataElementsB.add( dataElementB );
        dataElementsB.add( dataElementD );

        // Three dataElements
        dataElementsC.add( dataElementC );
        dataElementsC.add( dataElementE );
        dataElementsC.add( dataElementF );

        // ---------------------------------------------------------------------
        // Setup Periods
        // ---------------------------------------------------------------------

        Iterator<PeriodType> periodTypeIt = periodService.getAllPeriodTypes().iterator();
        PeriodType periodTypeA = periodTypeIt.next(); // Daily
        PeriodType periodTypeB = periodTypeIt.next(); // Weekly

        Date mar01 = super.getDate( 2005, 3, 1 );
        Date mar31 = super.getDate( 2005, 3, 31 );
        Date apr01 = super.getDate( 2005, 4, 1 );
        Date apr30 = super.getDate( 2005, 4, 30 );
        Date may01 = super.getDate( 2005, 5, 1 );
        Date may31 = super.getDate( 2005, 5, 31 );

        periodA = super.createPeriod( periodTypeA, mar01, mar31 );
        periodB = super.createPeriod( periodTypeA, apr01, apr30 );
        periodC = super.createPeriod( periodTypeB, mar01, may31 );
        periodD = super.createPeriod( periodTypeB, may01, may31 );

        periodIds.add( periodService.addPeriod( periodA ) );
        periodIds.add( periodService.addPeriod( periodB ) );
        periodIds.add( periodService.addPeriod( periodC ) );
        periodIds.add( periodService.addPeriod( periodD ) );

        // ---------------------------------------------------------------------
        // Setup DataSets
        // ---------------------------------------------------------------------

        dataSetA = super.createDataSet( 'A', periodTypeA );
        dataSetB = super.createDataSet( 'B', periodTypeB );
        dataSetC = super.createDataSet( 'C', periodTypeB );

        dataSetA.setDataElements( dataElementsA );
        dataSetB.setDataElements( dataElementsB );
        dataSetC.setDataElements( dataElementsC );

        dataSetService.addDataSet( dataSetA );
        dataSetService.addDataSet( dataSetB );
        dataSetService.addDataSet( dataSetC );

        // ---------------------------------------------------------------------
        // Setup OrganisationUnits
        // ---------------------------------------------------------------------

        unitA = super.createOrganisationUnit( 'A' );
        unitB = super.createOrganisationUnit( 'B', unitA );
        unitC = super.createOrganisationUnit( 'C', unitA );
        unitD = super.createOrganisationUnit( 'D', unitB );
        unitE = super.createOrganisationUnit( 'E', unitB );
        unitF = super.createOrganisationUnit( 'F', unitB );
        unitG = super.createOrganisationUnit( 'G', unitF );
        unitH = super.createOrganisationUnit( 'H', unitF );
        unitI = super.createOrganisationUnit( 'I' );

        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitA ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitB ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitC ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitD ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitE ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitF ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitG ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitH ) );
        organisationUnitIds.add( organisationUnitService.addOrganisationUnit( unitI ) );

        // ---------------------------------------------------------------------
        // Setup DataValues
        // ---------------------------------------------------------------------

        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodA, unitC, "90", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodA, unitD, "10", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodA, unitE, "35", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodA, unitF, "25", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodA, unitG, "20", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodA, unitH, "60", categoryOptionCombo ) );

        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodB, unitC, "70", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodB, unitD, "40", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodB, unitE, "65", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodB, unitF, "55", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodB, unitG, "20", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodB, unitH, "15", categoryOptionCombo ) );

        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodC, unitC, "95", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodC, unitD, "40", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodC, unitE, "45", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodC, unitF, "30", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodC, unitG, "50", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementA, periodC, unitH, "70", categoryOptionCombo ) );

        dataValueService.addDataValue( super.createDataValue( dataElementB, periodA, unitC, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodA, unitD, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodA, unitE, F, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodA, unitF, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodA, unitG, F, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodA, unitH, T, categoryOptionCombo ) );

        dataValueService.addDataValue( super.createDataValue( dataElementB, periodB, unitC, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodB, unitD, F, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodB, unitE, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodB, unitF, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodB, unitG, F, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodB, unitH, T, categoryOptionCombo ) );

        dataValueService.addDataValue( super.createDataValue( dataElementB, periodC, unitC, F, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodC, unitD, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodC, unitE, F, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodC, unitF, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodC, unitG, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementB, periodC, unitH, T, categoryOptionCombo ) );

        dataValueService.addDataValue( super.createDataValue( dataElementC, periodD, unitC, "String1",
            categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementC, periodD, unitD, "String2",
            categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementC, periodD, unitE, "String3",
            categoryOptionCombo ) );

        dataValueService
            .addDataValue( super.createDataValue( dataElementD, periodA, unitC, "10", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementD, periodA, unitD, "20", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementD, periodA, unitE, "30", categoryOptionCombo ) );

        dataValueService
            .addDataValue( super.createDataValue( dataElementD, periodB, unitF, "40", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementD, periodB, unitG, "50", categoryOptionCombo ) );
        dataValueService
            .addDataValue( super.createDataValue( dataElementD, periodB, unitH, "60", categoryOptionCombo ) );

        dataValueService.addDataValue( super.createDataValue( dataElementE, periodC, unitC, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementE, periodC, unitD, F, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementE, periodC, unitE, T, categoryOptionCombo ) );

        dataValueService.addDataValue( super.createDataValue( dataElementE, periodD, unitF, T, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementE, periodD, unitG, F, categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementE, periodD, unitH, T, categoryOptionCombo ) );

        dataValueService.addDataValue( super.createDataValue( dataElementF, periodA, unitC, "String4",
            categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementF, periodA, unitD, "String5",
            categoryOptionCombo ) );
        dataValueService.addDataValue( super.createDataValue( dataElementF, periodA, unitE, "String6",
            categoryOptionCombo ) );
    }
}
