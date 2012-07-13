package org.hisp.dhis.expression;

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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hisp.dhis.expression.Expression.SEPARATOR;
import static org.hisp.dhis.expression.ExpressionService.DAYS_EXPRESSION;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.DhisTest;
import org.hisp.dhis.constant.Constant;
import org.hisp.dhis.constant.ConstantService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.junit.Test;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ExpressionServiceTest
    extends DhisTest
{
    private DataElementCategoryOption categoryOptionA;

    private DataElementCategoryOption categoryOptionB;

    private DataElementCategoryOption categoryOptionC;

    private DataElementCategoryOption categoryOptionD;

    private DataElementCategory categoryA;

    private DataElementCategory categoryB;

    private DataElementCategoryCombo categoryCombo;

    private DataElement dataElementA;

    private DataElement dataElementB;

    private DataElement dataElementC;

    private DataElement dataElementD;

    private DataElement dataElementE;

    private Period period;

    private OrganisationUnit source;

    private int dataElementIdA;

    private int dataElementIdB;

    private int dataElementIdC;

    private int dataElementIdD;

    private int dataElementIdE;

    private DataElementCategoryOptionCombo categoryOptionCombo;

    private int categoryOptionComboId;

    private int constantIdA;

    private String expressionA;

    private String expressionB;

    private String expressionC;

    private String expressionD;
    
    private String expressionE;

    private String descriptionA;

    private String descriptionB;
    
    private Set<DataElement> dataElements = new HashSet<DataElement>();

    private Set<DataElementCategoryOptionCombo> optionCombos = new HashSet<DataElementCategoryOptionCombo>();

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
        throws Exception
    {
        expressionService = (ExpressionService) getBean( ExpressionService.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );

        categoryService = (DataElementCategoryService) getBean( DataElementCategoryService.ID );

        constantService = (ConstantService) getBean( ConstantService.ID );
        
        dataValueService = (DataValueService) getBean( DataValueService.ID );

        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );

        categoryOptionA = new DataElementCategoryOption( "Under 5" );
        categoryOptionB = new DataElementCategoryOption( "Over 5" );
        categoryOptionC = new DataElementCategoryOption( "Male" );
        categoryOptionD = new DataElementCategoryOption( "Female" );

        categoryService.addDataElementCategoryOption( categoryOptionA );
        categoryService.addDataElementCategoryOption( categoryOptionB );
        categoryService.addDataElementCategoryOption( categoryOptionC );
        categoryService.addDataElementCategoryOption( categoryOptionD );

        categoryA = new DataElementCategory( "Age" );
        categoryB = new DataElementCategory( "Gender" );

        categoryA.getCategoryOptions().add( categoryOptionA );
        categoryA.getCategoryOptions().add( categoryOptionB );
        categoryB.getCategoryOptions().add( categoryOptionC );
        categoryB.getCategoryOptions().add( categoryOptionD );

        categoryService.addDataElementCategory( categoryA );
        categoryService.addDataElementCategory( categoryB );

        categoryCombo = new DataElementCategoryCombo( "Age and gender" );
        categoryCombo.getCategories().add( categoryA );
        categoryCombo.getCategories().add( categoryB );

        categoryService.addDataElementCategoryCombo( categoryCombo );

        dataElementA = createDataElement( 'A' );
        dataElementB = createDataElement( 'B' );
        dataElementC = createDataElement( 'C' );
        dataElementD = createDataElement( 'D' );
        dataElementE = createDataElement( 'E', categoryCombo );

        dataElementIdA = dataElementService.addDataElement( dataElementA );
        dataElementIdB = dataElementService.addDataElement( dataElementB );
        dataElementIdC = dataElementService.addDataElement( dataElementC );
        dataElementIdD = dataElementService.addDataElement( dataElementD );
        dataElementIdE = dataElementService.addDataElement( dataElementE );

        categoryOptionCombo = categoryService.getDefaultDataElementCategoryOptionCombo();

        categoryOptionComboId = categoryOptionCombo.getId();
        optionCombos.add( categoryOptionCombo );

        period = createPeriod( getDate( 2000, 1, 1 ), getDate( 2000, 2, 1 ) );

        source = createOrganisationUnit( 'A' );

        organisationUnitService.addOrganisationUnit( source );

        constantIdA = constantService.saveConstant( new Constant( "ConstantA", 2.0 ) );
        
        expressionA = "[" + dataElementIdA + SEPARATOR + categoryOptionComboId + "]+[" + dataElementIdB + SEPARATOR
            + categoryOptionComboId + "]";
        expressionB = "[" + dataElementIdC + SEPARATOR + categoryOptionComboId + "]-[" + dataElementIdD + SEPARATOR
            + categoryOptionComboId + "]";
        expressionC = "[" + dataElementIdA + SEPARATOR + categoryOptionComboId + "]+[" + dataElementIdE + "]-10";
        expressionD = "[" + dataElementIdA + SEPARATOR + categoryOptionComboId + "]+" + DAYS_EXPRESSION;
        expressionE = "[" + dataElementIdA + SEPARATOR + categoryOptionComboId + "]*[C" + constantIdA + "]";

        descriptionA = "Expression A";
        descriptionB = "Expression B";

        dataElements.add( dataElementA );
        dataElements.add( dataElementB );
        dataElements.add( dataElementC );
        dataElements.add( dataElementD );
        dataElements.add( dataElementE );

        dataValueService.addDataValue( createDataValue( dataElementA, period, source, "10", categoryOptionCombo ) );
        dataValueService.addDataValue( createDataValue( dataElementB, period, source, "5", categoryOptionCombo ) );
    }

    @Override
    public boolean emptyDatabaseAfterTest()
    {
        return true;
    }

    // -------------------------------------------------------------------------
    // Business logic tests
    // -------------------------------------------------------------------------

    @Test
    public void testExplodeExpressionA()
    {
        categoryService.generateOptionCombos( categoryCombo );

        String actual = expressionService.explodeExpression( expressionC );

        Set<DataElementCategoryOptionCombo> categoryOptionCombos = categoryCombo.getOptionCombos();

        assertTrue( actual.contains( "[" + dataElementIdA + SEPARATOR + categoryOptionComboId + "]" ) );

        for ( DataElementCategoryOptionCombo categoryOptionCombo : categoryOptionCombos )
        {
            assertTrue( actual.contains( "[" + dataElementIdE + SEPARATOR + categoryOptionCombo.getId() + "]" ) );
        }
    }

    @Test
    public void testExplodeExpressionB()
    {
        assertEquals( "1", expressionService.explodeExpression( "1" ) );
        assertEquals( "2+6/4", expressionService.explodeExpression( "2+6/4" ) );
    }

    @Test
    public void testGetExpressionValue()
    {
        Expression expression = new Expression( expressionA, descriptionA, dataElements, optionCombos );

        Double value = expressionService.getExpressionValue( expression, period, source, false, false, null );

        assertEquals( value, 15.0, 0.1 );

        expression = new Expression( expressionB, descriptionB, dataElements, optionCombos );

        value = expressionService.getExpressionValue( expression, period, source, false, false, null );

        assertEquals( 0.0, value, 0.1 );

        expression = new Expression( expressionD, descriptionB, dataElements, optionCombos );

        value = expressionService.getExpressionValue( expression, period, source, false, false, 5 );

        assertEquals( 15.0, value, 0.1 );

        expression = new Expression( expressionE, descriptionB, dataElements, optionCombos );

        value = expressionService.getExpressionValue( expression, period, source, false, false, null );

        assertEquals( 20.0, value, 0.1 );
    }

    @Test
    public void testGetDataElementsInExpression()
    {
        Set<DataElement> dataElements = expressionService.getDataElementsInExpression( expressionA );

        assertTrue( dataElements.size() == 2 );
        assertTrue( dataElements.contains( dataElementA ) );
        assertTrue( dataElements.contains( dataElementB ) );
    }

    @Test
    public void testGetOperandsInExpression()
    {
        Set<DataElementOperand> operands = expressionService.getOperandsInExpression( expressionA );

        assertNotNull( operands );
        assertEquals( 2, operands.size() );

        DataElementOperand operandA = new DataElementOperand( dataElementIdA, categoryOptionComboId );
        DataElementOperand operandB = new DataElementOperand( dataElementIdB, categoryOptionComboId );

        assertTrue( operands.contains( operandA ) );
        assertTrue( operands.contains( operandB ) );
    }

    @Test
    public void testConvertExpression()
    {
        Map<Object, Integer> dataElementMapping = new HashMap<Object, Integer>();
        dataElementMapping.put( 1, 4 );
        dataElementMapping.put( 2, 5 );

        Map<Object, Integer> categoryOptionComboMapping = new HashMap<Object, Integer>();
        categoryOptionComboMapping.put( 1, 6 );
        categoryOptionComboMapping.put( 2, 7 );

        String expression = "[1.1]+2+[2.2]";
        String expected = "[4.6]+2+[5.7]";

        assertEquals( expected, expressionService.convertExpression( expression, dataElementMapping,
            categoryOptionComboMapping ) );
    }

    @Test
    public void testExpressionIsValid()
    {
        assertEquals( ExpressionService.VALID, expressionService.expressionIsValid( expressionA ) );
        assertEquals( ExpressionService.VALID, expressionService.expressionIsValid( expressionB ) );
        assertEquals( ExpressionService.VALID, expressionService.expressionIsValid( expressionC ) );
        assertEquals( ExpressionService.VALID, expressionService.expressionIsValid( expressionD ) );
        assertEquals( ExpressionService.VALID, expressionService.expressionIsValid( expressionE ) );

        expressionA = "[" + dataElementIdA + SEPARATOR + "foo" + "] + 12";

        assertEquals( ExpressionService.ID_NOT_NUMERIC, expressionService.expressionIsValid( expressionA ) );

        expressionA = "[" + 999 + SEPARATOR + categoryOptionComboId + "] + 12";

        assertEquals( ExpressionService.DATAELEMENT_DOES_NOT_EXIST, expressionService.expressionIsValid( expressionA ) );

        expressionA = "[" + dataElementIdA + SEPARATOR + 999 + "] + 12";

        assertEquals( ExpressionService.CATEGORYOPTIONCOMBO_DOES_NOT_EXIST, expressionService
            .expressionIsValid( expressionA ) );

        expressionA = "[" + dataElementIdA + SEPARATOR + categoryOptionComboId + "] + ( 12";

        assertEquals( ExpressionService.EXPRESSION_NOT_WELL_FORMED, expressionService.expressionIsValid( expressionA ) );

        expressionA = "12 x 4";

        assertEquals( ExpressionService.EXPRESSION_NOT_WELL_FORMED, expressionService.expressionIsValid( expressionA ) );
        
        expressionA = "12 + [Cfoo]";

        assertEquals( ExpressionService.ID_NOT_NUMERIC, expressionService.expressionIsValid( expressionA ) );

        expressionA = "12 + [C999999]";

        assertEquals( ExpressionService.CONSTANT_DOES_NOT_EXIST, expressionService.expressionIsValid( expressionA ) );
    }

    @Test
    public void testGetExpressionDescription()
    {
        String description = expressionService.getExpressionDescription( expressionA );

        assertEquals( "DataElementA+DataElementB", description );
        
        description = expressionService.getExpressionDescription( expressionD );
        
        assertEquals( description, "DataElementA+" + ExpressionService.DAYS_DESCRIPTION );

        description = expressionService.getExpressionDescription( expressionE );
        
        assertEquals( description, "DataElementA*ConstantA" );
    }

    @Test
    public void testGenerateExpression()
    {
        assertEquals( "10+5", expressionService.generateExpression( expressionA, period, source, false, false, null ) );
        assertEquals( "0-0", expressionService.generateExpression( expressionB, period, source, false, false, null ) );
        assertEquals( "10+7", expressionService.generateExpression( expressionD, period, source, false, false, 7 ) );
        assertEquals( "10*2.0", expressionService.generateExpression( expressionE, period, source, false, false, null ) );
    }

    @Test
    public void testGenerateExpressionMap()
    {
        Map<DataElementOperand, Double> valueMap = new HashMap<DataElementOperand, Double>();
        valueMap.put( new DataElementOperand( dataElementIdA, categoryOptionComboId ), new Double( 12 ) );
        valueMap.put( new DataElementOperand( dataElementIdB, categoryOptionComboId ), new Double( 34 ) );
        
        Map<Integer, Double> constantMap = new HashMap<Integer, Double>();
        constantMap.put( constantIdA, 2.0 );

        assertEquals( "12.0+34.0", expressionService.generateExpression( expressionA, valueMap, constantMap, null ) );
        assertEquals( "12.0+5", expressionService.generateExpression( expressionD, valueMap, constantMap, 5 ) );
        assertEquals( "12.0*2.0", expressionService.generateExpression( expressionE, valueMap, constantMap, null ) );
    }

    // -------------------------------------------------------------------------
    // CRUD tests
    // -------------------------------------------------------------------------

    @Test
    public void testAddGetExpression()
    {
        Expression expression = new Expression( expressionA, descriptionA, dataElements, optionCombos );

        int id = expressionService.addExpression( expression );

        expression = expressionService.getExpression( id );

        assertEquals( expressionA, expression.getExpression() );
        assertEquals( descriptionA, expression.getDescription() );
        assertEquals( dataElements, expression.getDataElementsInExpression() );
    }

    @Test
    public void testUpdateExpression()
    {
        Expression expression = new Expression( expressionA, descriptionA, dataElements, optionCombos );

        int id = expressionService.addExpression( expression );

        expression = expressionService.getExpression( id );

        assertEquals( expressionA, expression.getExpression() );
        assertEquals( descriptionA, expression.getDescription() );

        expression.setExpression( expressionB );
        expression.setDescription( descriptionB );

        expressionService.updateExpression( expression );

        expression = expressionService.getExpression( id );

        assertEquals( expressionB, expression.getExpression() );
        assertEquals( descriptionB, expression.getDescription() );
    }

    @Test
    public void testDeleteExpression()
    {
        Expression exprA = new Expression( expressionA, descriptionA, dataElements, optionCombos );
        Expression exprB = new Expression( expressionB, descriptionB, dataElements, optionCombos );

        int idA = expressionService.addExpression( exprA );
        int idB = expressionService.addExpression( exprB );

        assertNotNull( expressionService.getExpression( idA ) );
        assertNotNull( expressionService.getExpression( idB ) );

        expressionService.deleteExpression( exprA );

        assertNull( expressionService.getExpression( idA ) );
        assertNotNull( expressionService.getExpression( idB ) );

        expressionService.deleteExpression( exprB );

        assertNull( expressionService.getExpression( idA ) );
        assertNull( expressionService.getExpression( idB ) );
    }

    @Test
    public void testGetAllExpressions()
    {
        Expression exprA = new Expression( expressionA, descriptionA, dataElements, optionCombos );
        Expression exprB = new Expression( expressionB, descriptionB, dataElements, optionCombos );

        expressionService.addExpression( exprA );
        expressionService.addExpression( exprB );

        Collection<Expression> expressions = expressionService.getAllExpressions();

        assertTrue( expressions.size() == 2 );
        assertTrue( expressions.contains( exprA ) );
        assertTrue( expressions.contains( exprB ) );
    }
}
