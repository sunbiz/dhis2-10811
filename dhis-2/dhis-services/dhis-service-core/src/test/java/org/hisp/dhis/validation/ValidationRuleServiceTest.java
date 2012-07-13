package org.hisp.dhis.validation;

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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hisp.dhis.expression.Expression.SEPARATOR;
import static org.hisp.dhis.expression.Operator.equal_to;
import static org.hisp.dhis.expression.Operator.greater_than;
import static org.hisp.dhis.expression.Operator.less_than;
import static org.hisp.dhis.expression.Operator.less_than_or_equal_to;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.amplecode.quick.BatchHandler;
import org.amplecode.quick.BatchHandlerFactory;
import org.hisp.dhis.DhisTest;
import org.hisp.dhis.aggregation.AggregatedDataValue;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.jdbc.batchhandler.AggregatedDataValueBatchHandler;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.system.util.MathUtils;
import org.junit.Test;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ValidationRuleServiceTest
    extends DhisTest
{
    private BatchHandlerFactory batchHandlerFactory;

    private DataElement dataElementA;

    private DataElement dataElementB;

    private DataElement dataElementC;

    private DataElement dataElementD;

    private int dataElementIdA;

    private int dataElementIdB;

    private int dataElementIdC;

    private int dataElementIdD;

    private Set<DataElement> dataElementsA = new HashSet<DataElement>();

    private Set<DataElement> dataElementsB = new HashSet<DataElement>();

    private Set<DataElement> dataElementsC = new HashSet<DataElement>();

    private Set<DataElementCategoryOptionCombo> optionCombos;

    private DataElementCategoryCombo categoryCombo;

    private DataElementCategoryOptionCombo categoryOptionCombo;

    private Expression expressionA;

    private Expression expressionB;

    private Expression expressionC;

    private DataSet dataSet;

    private Period periodA;

    private Period periodB;

    private OrganisationUnit sourceA;

    private OrganisationUnit sourceB;

    private Set<OrganisationUnit> sourcesA = new HashSet<OrganisationUnit>();

    private ValidationRule validationRuleA;

    private ValidationRule validationRuleB;

    private ValidationRule validationRuleC;

    private ValidationRule validationRuleD;

    private ValidationRuleGroup group;

    private PeriodType periodType;

    // ----------------------------------------------------------------------
    // Fixture
    // ----------------------------------------------------------------------

    @Override
    public void setUpTest()
        throws Exception
    {
        batchHandlerFactory = (BatchHandlerFactory) getBean( "batchHandlerFactory" );

        validationRuleService = (ValidationRuleService) getBean( ValidationRuleService.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );

        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );

        expressionService = (ExpressionService) getBean( ExpressionService.ID );

        dataSetService = (DataSetService) getBean( DataSetService.ID );

        dataValueService = (DataValueService) getBean( DataValueService.ID );

        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );

        periodService = (PeriodService) getBean( PeriodService.ID );

        periodType = new MonthlyPeriodType();

        dataElementA = createDataElement( 'A' );
        dataElementB = createDataElement( 'B' );
        dataElementC = createDataElement( 'C' );
        dataElementD = createDataElement( 'D' );

        dataElementIdA = dataElementService.addDataElement( dataElementA );
        dataElementIdB = dataElementService.addDataElement( dataElementB );
        dataElementIdC = dataElementService.addDataElement( dataElementC );
        dataElementIdD = dataElementService.addDataElement( dataElementD );

        dataElementsA.add( dataElementA );
        dataElementsA.add( dataElementB );
        dataElementsB.add( dataElementC );
        dataElementsB.add( dataElementD );
        dataElementsC.add( dataElementB );

        categoryCombo = categoryService
            .getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );

        categoryOptionCombo = categoryCombo.getOptionCombos().iterator().next();

        String suffix = SEPARATOR + categoryOptionCombo.getId();

        optionCombos = new HashSet<DataElementCategoryOptionCombo>();
        optionCombos.add( categoryOptionCombo );

        expressionA = new Expression( "[" + dataElementIdA + suffix + "] + [" + dataElementIdB + suffix + "]",
            "descriptionA", dataElementsA, optionCombos );
        expressionB = new Expression( "[" + dataElementIdC + suffix + "] - [" + dataElementIdD + suffix + "]",
            "descriptionB", dataElementsB , optionCombos);
        expressionC = new Expression( "[" + dataElementIdB + suffix + "] * 2", "descriptionC", dataElementsC, optionCombos);

        expressionService.addExpression( expressionA );
        expressionService.addExpression( expressionB );
        expressionService.addExpression( expressionC );

        periodA = createPeriod( periodType, getDate( 2000, 3, 1 ), getDate( 2000, 3, 31 ) );
        periodB = createPeriod( periodType, getDate( 2000, 4, 1 ), getDate( 2000, 4, 30 ) );

        dataSet = createDataSet( 'A', periodType );

        sourceA = createOrganisationUnit( 'A' );
        sourceB = createOrganisationUnit( 'B' );

        sourceA.getDataSets().add( dataSet );
        sourceB.getDataSets().add( dataSet );

        organisationUnitService.addOrganisationUnit( sourceA );
        organisationUnitService.addOrganisationUnit( sourceB );

        sourcesA.add( sourceA );
        sourcesA.add( sourceB );

        dataSet.getDataElements().add( dataElementA );
        dataSet.getDataElements().add( dataElementB );
        dataSet.getDataElements().add( dataElementC );
        dataSet.getDataElements().add( dataElementD );

        dataSet.getSources().add( sourceA );
        dataSet.getSources().add( sourceB );

        dataElementA.getDataSets().add( dataSet );
        dataElementB.getDataSets().add( dataSet );
        dataElementC.getDataSets().add( dataSet );
        dataElementD.getDataSets().add( dataSet );

        dataSetService.addDataSet( dataSet );

        dataElementService.updateDataElement( dataElementA );
        dataElementService.updateDataElement( dataElementB );
        dataElementService.updateDataElement( dataElementC );
        dataElementService.updateDataElement( dataElementD );

        validationRuleA = createValidationRule( 'A', equal_to, expressionA, expressionB, periodType );
        validationRuleB = createValidationRule( 'B', greater_than, expressionB, expressionC, periodType );
        validationRuleC = createValidationRule( 'C', less_than_or_equal_to, expressionB, expressionA, periodType );
        validationRuleD = createValidationRule( 'D', less_than, expressionA, expressionC, periodType );

        group = createValidationRuleGroup( 'A' );
    }

    @Override
    public boolean emptyDatabaseAfterTest()
    {
        return true;
    }

    // ----------------------------------------------------------------------
    // Business logic tests
    // ----------------------------------------------------------------------

    @Test
    public void testGetAggregatedValidationResult()
    {
        validationRuleService.saveValidationRule( validationRuleA );
        validationRuleService.saveValidationRule( validationRuleB );
        validationRuleService.saveValidationRule( validationRuleC );
        validationRuleService.saveValidationRule( validationRuleD );

        List<Period> periods = new ArrayList<Period>();
        periods.add( periodA );
        periods.add( periodB );

        List<OrganisationUnit> sources = new ArrayList<OrganisationUnit>();
        sources.add( sourceA );
        sources.add( sourceB );

        Collection<ValidationResult> results = new HashSet<ValidationResult>();

        results.add( new ValidationResult( periodA, sourceA, validationRuleA, 1, 1 ) );
        results.add( new ValidationResult( periodA, sourceA, validationRuleB, 1, 1 ) );
        results.add( new ValidationResult( periodA, sourceA, validationRuleC, 1, 1 ) );
        results.add( new ValidationResult( periodB, sourceB, validationRuleA, 1, 1 ) );
        results.add( new ValidationResult( periodB, sourceB, validationRuleB, 1, 1 ) );

        Grid grid = validationRuleService.getAggregateValidationResult( results, periods, sources );

        // First row is Periods, first column in each row is Source

        assertEquals( "75.0", grid.getValue( 1, 1 ) );
        assertEquals( "0.0", grid.getValue( 1, 2 ) );
        assertEquals( "0.0", grid.getValue( 2, 1 ) );
        assertEquals( "50.0", grid.getValue( 2, 2 ) );
    }

    @Test
    public void testValidateAggregatedDateDateSources()
    {
        periodService.addPeriod( periodA );
        periodService.addPeriod( periodB );

        BatchHandler<AggregatedDataValue> batchHandler = batchHandlerFactory.createBatchHandler(
            AggregatedDataValueBatchHandler.class ).init();

        batchHandler.addObject( new AggregatedDataValue( dataElementA.getId(), categoryOptionCombo.getId(), periodA
            .getId(), 0, sourceA.getId(), 0, 1.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementB.getId(), categoryOptionCombo.getId(), periodA
            .getId(), 0, sourceA.getId(), 0, 2.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementC.getId(), categoryOptionCombo.getId(), periodA
            .getId(), 0, sourceA.getId(), 0, 3.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementD.getId(), categoryOptionCombo.getId(), periodA
            .getId(), 0, sourceA.getId(), 0, 4.0 ) );

        batchHandler.addObject( new AggregatedDataValue( dataElementA.getId(), categoryOptionCombo.getId(), periodB
            .getId(), 0, sourceA.getId(), 0, 1.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementB.getId(), categoryOptionCombo.getId(), periodB
            .getId(), 0, sourceA.getId(), 0, 2.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementC.getId(), categoryOptionCombo.getId(), periodB
            .getId(), 0, sourceA.getId(), 0, 3.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementD.getId(), categoryOptionCombo.getId(), periodB
            .getId(), 0, sourceA.getId(), 0, 4.0 ) );

        batchHandler.addObject( new AggregatedDataValue( dataElementA.getId(), categoryOptionCombo.getId(), periodA
            .getId(), 0, sourceB.getId(), 0, 1.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementB.getId(), categoryOptionCombo.getId(), periodA
            .getId(), 0, sourceB.getId(), 0, 2.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementC.getId(), categoryOptionCombo.getId(), periodA
            .getId(), 0, sourceB.getId(), 0, 3.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementD.getId(), categoryOptionCombo.getId(), periodA
            .getId(), 0, sourceB.getId(), 0, 4.0 ) );

        batchHandler.addObject( new AggregatedDataValue( dataElementA.getId(), categoryOptionCombo.getId(), periodB
            .getId(), 0, sourceB.getId(), 0, 1.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementB.getId(), categoryOptionCombo.getId(), periodB
            .getId(), 0, sourceB.getId(), 0, 2.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementC.getId(), categoryOptionCombo.getId(), periodB
            .getId(), 0, sourceB.getId(), 0, 3.0 ) );
        batchHandler.addObject( new AggregatedDataValue( dataElementD.getId(), categoryOptionCombo.getId(), periodB
            .getId(), 0, sourceB.getId(), 0, 4.0 ) );

        batchHandler.flush();

        validationRuleService.saveValidationRule( validationRuleA );
        validationRuleService.saveValidationRule( validationRuleB );
        validationRuleService.saveValidationRule( validationRuleC );
        validationRuleService.saveValidationRule( validationRuleD );

        Collection<ValidationResult> results = validationRuleService.validateAggregate( getDate( 2000, 2, 1 ), getDate(
            2000, 6, 1 ), sourcesA );

        Collection<ValidationResult> reference = new HashSet<ValidationResult>();

        reference.add( new ValidationResult( periodA, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodA, sourceB, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceB, validationRuleA, 3.0, -1.0 ) );

        reference.add( new ValidationResult( periodA, sourceA, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodA, sourceB, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodB, sourceB, validationRuleB, -1.0, 4.0 ) );

        for ( ValidationResult result : results )
        {
            assertFalse( MathUtils.expressionIsTrue( result.getLeftsideValue(), result.getValidationRule()
                .getOperator(), result.getRightsideValue() ) );
        }

        assertEquals( results.size(), 8 );
        assertEquals( reference, results );
    }

    @Test
    public void testValidateDateDateSources()
    {
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceA, "4", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodB, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, sourceA, "4", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceB, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceB, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceB, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceB, "4", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodB, sourceB, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, sourceB, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, sourceB, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, sourceB, "4", categoryOptionCombo ) );

        validationRuleService.saveValidationRule( validationRuleA ); // Invalid
        validationRuleService.saveValidationRule( validationRuleB ); // Invalid
        validationRuleService.saveValidationRule( validationRuleC ); // Valid
        validationRuleService.saveValidationRule( validationRuleD ); // Valid

        Collection<ValidationResult> results = validationRuleService.validate( getDate( 2000, 2, 1 ), getDate( 2000, 6,
            1 ), sourcesA );

        Collection<ValidationResult> reference = new HashSet<ValidationResult>();

        reference.add( new ValidationResult( periodA, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodA, sourceB, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceB, validationRuleA, 3.0, -1.0 ) );

        reference.add( new ValidationResult( periodA, sourceA, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodA, sourceB, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodB, sourceB, validationRuleB, -1.0, 4.0 ) );

        for ( ValidationResult result : results )
        {
            assertFalse( MathUtils.expressionIsTrue( result.getLeftsideValue(), result.getValidationRule()
                .getOperator(), result.getRightsideValue() ) );
        }

        assertEquals( results.size(), 8 );
        assertEquals( reference, results );
    }

    @Test
    public void testValidateDateDateSourcesGroup()
    {
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceA, "4", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodB, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, sourceA, "4", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceB, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceB, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceB, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceB, "4", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodB, sourceB, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, sourceB, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, sourceB, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, sourceB, "4", categoryOptionCombo ) );

        validationRuleService.saveValidationRule( validationRuleA ); // Invalid
        validationRuleService.saveValidationRule( validationRuleB ); // Invalid
        validationRuleService.saveValidationRule( validationRuleC ); // Valid
        validationRuleService.saveValidationRule( validationRuleD ); // Valid

        group.getMembers().add( validationRuleA );
        group.getMembers().add( validationRuleC );

        validationRuleService.addValidationRuleGroup( group );

        Collection<ValidationResult> results = validationRuleService.validate( getDate( 2000, 2, 1 ), getDate( 2000, 6,
            1 ), sourcesA, group );

        Collection<ValidationResult> reference = new HashSet<ValidationResult>();

        reference.add( new ValidationResult( periodA, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodA, sourceB, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceB, validationRuleA, 3.0, -1.0 ) );

        for ( ValidationResult result : results )
        {
            assertFalse( MathUtils.expressionIsTrue( result.getLeftsideValue(), result.getValidationRule()
                .getOperator(), result.getRightsideValue() ) );
        }

        assertEquals( results.size(), 4 );
        assertEquals( reference, results );
    }

    @Test
    public void testValidateDateDateSource()
    {
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceA, "4", categoryOptionCombo ) );

        dataValueService.addDataValue( createDataValue( dataElementA, periodB, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodB, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodB, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodB, sourceA, "4", categoryOptionCombo ) );

        validationRuleService.saveValidationRule( validationRuleA );
        validationRuleService.saveValidationRule( validationRuleB );
        validationRuleService.saveValidationRule( validationRuleC );
        validationRuleService.saveValidationRule( validationRuleD );

        Collection<ValidationResult> results = validationRuleService.validate( getDate( 2000, 2, 1 ), getDate( 2000, 6,
            1 ), sourceA );

        Collection<ValidationResult> reference = new HashSet<ValidationResult>();

        reference.add( new ValidationResult( periodA, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodA, sourceA, validationRuleB, -1.0, 4.0 ) );
        reference.add( new ValidationResult( periodB, sourceA, validationRuleB, -1.0, 4.0 ) );

        for ( ValidationResult result : results )
        {
            assertFalse( MathUtils.expressionIsTrue( result.getLeftsideValue(), result.getValidationRule()
                .getOperator(), result.getRightsideValue() ) );
        }

        assertEquals( results.size(), 4 );
        assertEquals( reference, results );
    }

    @Test
    public void testValidateDataSetPeriodSource()
    {
        dataValueService.addDataValue( createDataValue( dataElementA, periodA, sourceA, "1", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, periodA, sourceA, "2", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementC, periodA, sourceA, "3", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementD, periodA, sourceA, "4", categoryOptionCombo ) );

        validationRuleService.saveValidationRule( validationRuleA );
        validationRuleService.saveValidationRule( validationRuleB );
        validationRuleService.saveValidationRule( validationRuleC );
        validationRuleService.saveValidationRule( validationRuleD );

        Collection<ValidationResult> results = validationRuleService.validate( dataSet, periodA, sourceA );

        Collection<ValidationResult> reference = new HashSet<ValidationResult>();

        reference.add( new ValidationResult( periodA, sourceA, validationRuleA, 3.0, -1.0 ) );
        reference.add( new ValidationResult( periodA, sourceA, validationRuleB, -1.0, 4.0 ) );

        for ( ValidationResult result : results )
        {
            assertFalse( MathUtils.expressionIsTrue( result.getLeftsideValue(), result.getValidationRule()
                .getOperator(), result.getRightsideValue() ) );
        }

        assertEquals( results.size(), 2 );
        assertEquals( reference, results );
    }

    // ----------------------------------------------------------------------
    // CURD functionality tests
    // ----------------------------------------------------------------------

    @Test
    public void testSaveValidationRule()
    {
        int id = validationRuleService.saveValidationRule( validationRuleA );

        validationRuleA = validationRuleService.getValidationRule( id );

        assertEquals( validationRuleA.getName(), "ValidationRuleA" );
        assertEquals( validationRuleA.getDescription(), "DescriptionA" );
        assertEquals( validationRuleA.getType(), ValidationRule.TYPE_ABSOLUTE );
        assertEquals( validationRuleA.getOperator(), equal_to );
        assertNotNull( validationRuleA.getLeftSide().getExpression() );
        assertNotNull( validationRuleA.getRightSide().getExpression() );
        assertEquals( validationRuleA.getPeriodType(), periodType );
    }

    @Test
    public void testUpdateValidationRule()
    {
        int id = validationRuleService.saveValidationRule( validationRuleA );
        validationRuleA = validationRuleService.getValidationRuleByName( "ValidationRuleA" );

        assertEquals( validationRuleA.getName(), "ValidationRuleA" );
        assertEquals( validationRuleA.getDescription(), "DescriptionA" );
        assertEquals( validationRuleA.getType(), ValidationRule.TYPE_ABSOLUTE );
        assertEquals( validationRuleA.getOperator(), equal_to );

        validationRuleA.setId( id );
        validationRuleA.setName( "ValidationRuleB" );
        validationRuleA.setDescription( "DescriptionB" );
        validationRuleA.setType( ValidationRule.TYPE_STATISTICAL );
        validationRuleA.setOperator( greater_than );

        validationRuleService.updateValidationRule( validationRuleA );
        validationRuleA = validationRuleService.getValidationRule( id );

        assertEquals( validationRuleA.getName(), "ValidationRuleB" );
        assertEquals( validationRuleA.getDescription(), "DescriptionB" );
        assertEquals( validationRuleA.getType(), ValidationRule.TYPE_STATISTICAL );
        assertEquals( validationRuleA.getOperator(), greater_than );
    }

    @Test
    public void testDeleteValidationRule()
    {
        int idA = validationRuleService.saveValidationRule( validationRuleA );
        int idB = validationRuleService.saveValidationRule( validationRuleB );

        assertNotNull( validationRuleService.getValidationRule( idA ) );
        assertNotNull( validationRuleService.getValidationRule( idB ) );

        validationRuleA.clearExpressions();

        validationRuleService.deleteValidationRule( validationRuleA );

        assertNull( validationRuleService.getValidationRule( idA ) );
        assertNotNull( validationRuleService.getValidationRule( idB ) );

        validationRuleB.clearExpressions();

        validationRuleService.deleteValidationRule( validationRuleB );

        assertNull( validationRuleService.getValidationRule( idA ) );
        assertNull( validationRuleService.getValidationRule( idB ) );
    }

    @Test
    public void testGetAllValidationRules()
    {
        validationRuleService.saveValidationRule( validationRuleA );
        validationRuleService.saveValidationRule( validationRuleB );

        Collection<ValidationRule> rules = validationRuleService.getAllValidationRules();

        assertTrue( rules.size() == 2 );
        assertTrue( rules.contains( validationRuleA ) );
        assertTrue( rules.contains( validationRuleB ) );
    }

    @Test
    public void testGetValidationRuleByName()
    {
        int id = validationRuleService.saveValidationRule( validationRuleA );
        validationRuleService.saveValidationRule( validationRuleB );

        ValidationRule rule = validationRuleService.getValidationRuleByName( "ValidationRuleA" );

        assertEquals( rule.getId(), id );
        assertEquals( rule.getName(), "ValidationRuleA" );
    }

    // -------------------------------------------------------------------------
    // ValidationRuleGroup
    // -------------------------------------------------------------------------

    @Test
    public void testAddValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', equal_to, null, null, periodType );
        ValidationRule ruleB = createValidationRule( 'B', equal_to, null, null, periodType );

        validationRuleService.saveValidationRule( ruleA );
        validationRuleService.saveValidationRule( ruleB );

        Set<ValidationRule> rules = new HashSet<ValidationRule>();

        rules.add( ruleA );
        rules.add( ruleB );

        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );

        groupA.setMembers( rules );
        groupB.setMembers( rules );

        int idA = validationRuleService.addValidationRuleGroup( groupA );
        int idB = validationRuleService.addValidationRuleGroup( groupB );

        assertEquals( groupA, validationRuleService.getValidationRuleGroup( idA ) );
        assertEquals( groupB, validationRuleService.getValidationRuleGroup( idB ) );
    }

    @Test
    public void testUpdateValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', equal_to, null, null, periodType );
        ValidationRule ruleB = createValidationRule( 'B', equal_to, null, null, periodType );

        validationRuleService.saveValidationRule( ruleA );
        validationRuleService.saveValidationRule( ruleB );

        Set<ValidationRule> rules = new HashSet<ValidationRule>();

        rules.add( ruleA );
        rules.add( ruleB );

        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );

        groupA.setMembers( rules );
        groupB.setMembers( rules );

        int idA = validationRuleService.addValidationRuleGroup( groupA );
        int idB = validationRuleService.addValidationRuleGroup( groupB );

        assertEquals( groupA, validationRuleService.getValidationRuleGroup( idA ) );
        assertEquals( groupB, validationRuleService.getValidationRuleGroup( idB ) );

        ruleA.setName( "UpdatedValidationRuleA" );
        ruleB.setName( "UpdatedValidationRuleB" );

        validationRuleService.updateValidationRuleGroup( groupA );
        validationRuleService.updateValidationRuleGroup( groupB );

        assertEquals( groupA, validationRuleService.getValidationRuleGroup( idA ) );
        assertEquals( groupB, validationRuleService.getValidationRuleGroup( idB ) );
    }

    @Test
    public void testDeleteValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', equal_to, null, null, periodType );
        ValidationRule ruleB = createValidationRule( 'B', equal_to, null, null, periodType );

        validationRuleService.saveValidationRule( ruleA );
        validationRuleService.saveValidationRule( ruleB );

        Set<ValidationRule> rules = new HashSet<ValidationRule>();

        rules.add( ruleA );
        rules.add( ruleB );

        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );

        groupA.setMembers( rules );
        groupB.setMembers( rules );

        int idA = validationRuleService.addValidationRuleGroup( groupA );
        int idB = validationRuleService.addValidationRuleGroup( groupB );

        assertNotNull( validationRuleService.getValidationRuleGroup( idA ) );
        assertNotNull( validationRuleService.getValidationRuleGroup( idB ) );

        validationRuleService.deleteValidationRuleGroup( groupA );

        assertNull( validationRuleService.getValidationRuleGroup( idA ) );
        assertNotNull( validationRuleService.getValidationRuleGroup( idB ) );

        validationRuleService.deleteValidationRuleGroup( groupB );

        assertNull( validationRuleService.getValidationRuleGroup( idA ) );
        assertNull( validationRuleService.getValidationRuleGroup( idB ) );
    }

    @Test
    public void testGetAllValidationRuleGroup()
    {
        ValidationRule ruleA = createValidationRule( 'A', equal_to, null, null, periodType );
        ValidationRule ruleB = createValidationRule( 'B', equal_to, null, null, periodType );

        validationRuleService.saveValidationRule( ruleA );
        validationRuleService.saveValidationRule( ruleB );

        Set<ValidationRule> rules = new HashSet<ValidationRule>();

        rules.add( ruleA );
        rules.add( ruleB );

        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );

        groupA.setMembers( rules );
        groupB.setMembers( rules );

        validationRuleService.addValidationRuleGroup( groupA );
        validationRuleService.addValidationRuleGroup( groupB );

        Collection<ValidationRuleGroup> groups = validationRuleService.getAllValidationRuleGroups();

        assertEquals( 2, groups.size() );
        assertTrue( groups.contains( groupA ) );
        assertTrue( groups.contains( groupB ) );
    }

    @Test
    public void testGetValidationRuleGroupByName()
    {
        ValidationRule ruleA = createValidationRule( 'A', equal_to, null, null, periodType );
        ValidationRule ruleB = createValidationRule( 'B', equal_to, null, null, periodType );

        validationRuleService.saveValidationRule( ruleA );
        validationRuleService.saveValidationRule( ruleB );

        Set<ValidationRule> rules = new HashSet<ValidationRule>();

        rules.add( ruleA );
        rules.add( ruleB );

        ValidationRuleGroup groupA = createValidationRuleGroup( 'A' );
        ValidationRuleGroup groupB = createValidationRuleGroup( 'B' );

        groupA.setMembers( rules );
        groupB.setMembers( rules );

        validationRuleService.addValidationRuleGroup( groupA );
        validationRuleService.addValidationRuleGroup( groupB );

        ValidationRuleGroup groupByName = validationRuleService.getValidationRuleGroupByName( groupA.getName() );

        assertEquals( groupA, groupByName );
    }
}
