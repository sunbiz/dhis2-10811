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

import static org.hisp.dhis.expression.Expression.EXP_CLOSE;
import static org.hisp.dhis.expression.Expression.EXP_OPEN;
import static org.hisp.dhis.expression.Expression.PAR_CLOSE;
import static org.hisp.dhis.expression.Expression.PAR_OPEN;
import static org.hisp.dhis.expression.Expression.SEPARATOR;
import static org.hisp.dhis.system.util.MathUtils.calculateExpression;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.AggregatedDataValueService;
import org.hisp.dhis.common.GenericStore;
import org.hisp.dhis.constant.Constant;
import org.hisp.dhis.constant.ConstantService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.system.util.MathUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * The expression is a string describing a formula containing data element ids
 * and category option combo ids. The formula can potentially contain references
 * to category totals (also called sub-totals) and data element totals (also
 * called totals).
 * 
 * @author Margrethe Store
 * @author Lars Helge Overland
 * @version $Id: DefaultExpressionService.java 6463 2008-11-24 12:05:46Z
 *          larshelg $
 */
@Transactional
public class DefaultExpressionService
    implements ExpressionService
{
    private static final Log log = LogFactory.getLog( DefaultExpressionService.class );

    private final Pattern FORMULA_PATTERN = Pattern.compile( FORMULA_EXPRESSION );
    private final Pattern OPERAND_PATTERN = Pattern.compile( OPERAND_EXPRESSION );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private GenericStore<Expression> expressionStore;

    public void setExpressionStore( GenericStore<Expression> expressionStore )
    {
        this.expressionStore = expressionStore;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    
    private ConstantService constantService;

    public void setConstantService( ConstantService constantService )
    {
        this.constantService = constantService;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private AggregatedDataValueService aggregatedDataValueService;

    public void setAggregatedDataValueService( AggregatedDataValueService aggregatedDataValueService )
    {
        this.aggregatedDataValueService = aggregatedDataValueService;
    }

    private DataElementCategoryService categoryService;

    public void setCategoryService( DataElementCategoryService categoryService )
    {
        this.categoryService = categoryService;
    }

    // -------------------------------------------------------------------------
    // Expression CRUD operations
    // -------------------------------------------------------------------------

    public int addExpression( Expression expression )
    {
        return expressionStore.save( expression );
    }

    public void deleteExpression( Expression expression )
    {
        expressionStore.delete( expression );
    }

    public Expression getExpression( int id )
    {
        return expressionStore.get( id );
    }

    public void updateExpression( Expression expression )
    {
        expressionStore.update( expression );
    }

    public Collection<Expression> getAllExpressions()
    {
        return expressionStore.getAll();
    }

    // -------------------------------------------------------------------------
    // Business logic
    // -------------------------------------------------------------------------

    public Double getExpressionValue( Expression expression, Period period, OrganisationUnit source,
        boolean nullIfNoValues, boolean aggregate, Integer days )
    {
        final String expressionString = generateExpression( expression.getExpression(), period, source, nullIfNoValues,
            aggregate, days );

        return expressionString != null ? calculateExpression( expressionString ) : null;
    }

    public Set<DataElement> getDataElementsInExpression( String expression )
    {
        Set<DataElement> dataElementsInExpression = null;

        if ( expression != null )
        {
            dataElementsInExpression = new HashSet<DataElement>();

            final Matcher matcher = OPERAND_PATTERN.matcher( expression );

            while ( matcher.find() )
            {
                final DataElement dataElement = dataElementService.getDataElement( DataElementOperand.getOperand(
                    matcher.group() ).getDataElementId() );

                if ( dataElement != null )
                {
                    dataElementsInExpression.add( dataElement );
                }
            }
        }

        return dataElementsInExpression;
    }

    public Set<DataElementCategoryOptionCombo> getOptionCombosInExpression( String expression )
    {
        Set<DataElementCategoryOptionCombo> optionCombosInExpression = null;

        if ( expression != null )
        {
            optionCombosInExpression = new HashSet<DataElementCategoryOptionCombo>();

            final Matcher matcher = OPERAND_PATTERN.matcher( expression );

            while ( matcher.find() )
            {
                DataElementCategoryOptionCombo categoryOptionCombo = categoryService.getDataElementCategoryOptionCombo( 
                    DataElementOperand.getOperand( matcher.group() ).getOptionComboId() );

                if ( categoryOptionCombo != null )
                {
                    optionCombosInExpression.add( categoryOptionCombo );
                }
            }
        }

        return optionCombosInExpression;
    }
    
    public String convertExpression( String expression, Map<Object, Integer> dataElementMapping,
        Map<Object, Integer> categoryOptionComboMapping )
    {
        //TODO constants
        
        final StringBuffer convertedFormula = new StringBuffer();

        if ( expression != null )
        {
            final Matcher matcher = OPERAND_PATTERN.matcher( expression );

            while ( matcher.find() )
            {
                String match = matcher.group();

                final DataElementOperand operand = DataElementOperand.getOperand( match );

                final Integer mappedDataElementId = dataElementMapping.get( operand.getDataElementId() );
                final Integer mappedCategoryOptionComboId = categoryOptionComboMapping.get( operand.getOptionComboId() );

                if ( mappedDataElementId == null )
                {
                    log.warn( "Data element identifier refers to non-existing object: " + operand.getDataElementId() );

                    match = NULL_REPLACEMENT;
                }
                else if ( !operand.isTotal() && mappedCategoryOptionComboId == null )
                {
                    log.warn( "Category option combo identifier refers to non-existing object: "
                        + operand.getOptionComboId() );

                    match = NULL_REPLACEMENT;
                }
                else
                {
                    match = EXP_OPEN + mappedDataElementId + SEPARATOR + mappedCategoryOptionComboId + EXP_CLOSE;
                }

                matcher.appendReplacement( convertedFormula, match );
            }

            matcher.appendTail( convertedFormula );
        }

        return convertedFormula.toString();
    }

    public Set<DataElementOperand> getOperandsInExpression( String expression )
    {
        Set<DataElementOperand> operandsInExpression = null;

        if ( expression != null )
        {
            operandsInExpression = new HashSet<DataElementOperand>();

            final Matcher matcher = OPERAND_PATTERN.matcher( expression );

            while ( matcher.find() )
            {
                operandsInExpression.add( DataElementOperand.getOperand( matcher.group() ) );
            }
        }

        return operandsInExpression;
    }
    
    public void filterInvalidIndicators( Collection<Indicator> indicators )
    {
        if ( indicators != null )
        {
            Iterator<Indicator> iterator = indicators.iterator();
            
            while ( iterator.hasNext() )
            {
                Indicator indicator = iterator.next();
                
                if ( !expressionIsValid( indicator.getNumerator() ).equals( VALID ) ||
                    !expressionIsValid( indicator.getDenominator() ).equals( VALID ) )
                {
                    iterator.remove();
                    log.warn( "Indicator is invalid: " + indicator );
                }
            }
        }
    }

    public String expressionIsValid( String formula )
    {
        if ( formula == null )
        {
            return EXPRESSION_IS_EMPTY;
        }

        final StringBuffer buffer = new StringBuffer();

        final Matcher matcher = FORMULA_PATTERN.matcher( formula );

        while ( matcher.find() )
        {
            DataElementOperand operand = null;

            final String match = matcher.group();

            if ( DAYS_EXPRESSION.equals( match ) )
            {
                // Ignore
            }
            else if ( match.matches( CONSTANT_EXPRESSION ) )
            {
                Integer id = null;
                
                try
                {
                    id = Integer.parseInt( stripConstantExpression( match ) );
                }
                catch ( NumberFormatException ex )
                {
                    return ID_NOT_NUMERIC;
                }
                
                if ( constantService.getConstant( id ) == null )
                {
                    return CONSTANT_DOES_NOT_EXIST;
                }                    
            }
            else
            {
                try
                {
                    operand = DataElementOperand.getOperand( match );
                }
                catch ( NumberFormatException ex )
                {
                    return ID_NOT_NUMERIC;
                }

                if ( !dataElementService.dataElementExists( operand.getDataElementId() ) )
                {
                    return DATAELEMENT_DOES_NOT_EXIST;
                }

                if ( !operand.isTotal() && !dataElementService.dataElementCategoryOptionComboExists( operand.getOptionComboId() ) )
                {
                    return CATEGORYOPTIONCOMBO_DOES_NOT_EXIST;
                }
            }

            // -----------------------------------------------------------------
            // Replacing the operand with 1.1 in order to later be able to
            // verify that the formula is mathematically valid
            // -----------------------------------------------------------------

            matcher.appendReplacement( buffer, "1.1" );
        }

        matcher.appendTail( buffer );

        if ( MathUtils.expressionHasErrors( buffer.toString() ) )
        {
            return EXPRESSION_NOT_WELL_FORMED;
        }

        return VALID;
    }

    public String getExpressionDescription( String formula )
    {
        StringBuffer buffer = null;

        if ( formula != null )
        {
            buffer = new StringBuffer();

            final Matcher matcher = FORMULA_PATTERN.matcher( formula );

            while ( matcher.find() )
            {
                String match = matcher.group();

                if ( DAYS_EXPRESSION.equals( match ) )
                {
                    match = DAYS_DESCRIPTION;
                }
                else if ( match.matches( CONSTANT_EXPRESSION ) )
                {
                    final Integer id = Integer.parseInt( stripConstantExpression( match ) );
                    
                    final Constant constant = constantService.getConstant( id );
                    
                    if ( constant == null )
                    {
                        throw new IllegalArgumentException( "Identifier does not reference a constant: " + id );
                    }
                    
                    match = constant.getDisplayName();
                }
                else
                {
                    final DataElementOperand operand = DataElementOperand.getOperand( match );

                    final DataElement dataElement = dataElementService.getDataElement( operand.getDataElementId() );
                    final DataElementCategoryOptionCombo categoryOptionCombo = categoryService
                        .getDataElementCategoryOptionCombo( operand.getOptionComboId() );

                    if ( dataElement == null )
                    {
                        throw new IllegalArgumentException( "Identifier does not reference a data element: "
                            + operand.getDataElementId() );
                    }

                    if ( !operand.isTotal() && categoryOptionCombo == null )
                    {
                        throw new IllegalArgumentException( "Identifier does not reference a category option combo: "
                            + operand.getOptionComboId() );
                    }

                    match = DataElementOperand.getPrettyName( dataElement, categoryOptionCombo );
                }

                matcher.appendReplacement( buffer, match );
            }

            matcher.appendTail( buffer );
        }

        return buffer != null ? buffer.toString() : null;
    }

    public void explodeAndSubstituteExpressions( Collection<Indicator> indicators, Integer days )
    {
        if ( indicators != null && !indicators.isEmpty() )
        {
            for ( Indicator indicator : indicators )
            {
                indicator.setExplodedNumerator( substituteExpression( indicator.getNumerator(), days ) );
                indicator.setExplodedDenominator( substituteExpression( indicator.getDenominator(), days ) );
            }

            final Map<Integer, Set<Integer>> dataElementMap = dataElementService.getDataElementCategoryOptionCombos();
            
            for ( Indicator indicator : indicators )
            {
                indicator.setExplodedNumerator( explodeExpression( indicator.getExplodedNumerator() != null ? indicator.getExplodedNumerator() : "", dataElementMap ) );
                indicator.setExplodedDenominator( explodeExpression( indicator.getExplodedDenominator() != null ? indicator.getExplodedDenominator() : "", dataElementMap ) );
            }     
        }
    }

    private String explodeExpression( String expression, Map<Integer, Set<Integer>> dataElementMap )
    {
        StringBuffer buffer = null;

        if ( expression != null )
        {
            final Matcher matcher = OPERAND_PATTERN.matcher( expression );

            buffer = new StringBuffer();

            while ( matcher.find() )
            {
                final DataElementOperand operand = DataElementOperand.getOperand( matcher.group() );
                
                if ( operand.isTotal() )
                {
                    final StringBuilder replace = new StringBuilder( PAR_OPEN );

                    for ( Integer categoryOptionCombo : dataElementMap.get( operand.getDataElementId() ) )
                    {
                        replace.append( EXP_OPEN ).append( operand.getDataElementId() ).append( SEPARATOR ).append(
                            categoryOptionCombo ).append( EXP_CLOSE ).append( "+" );
                    }

                    replace.deleteCharAt( replace.length() - 1 ).append( PAR_CLOSE );

                    matcher.appendReplacement( buffer, replace.toString() );
                }
            }

            matcher.appendTail( buffer );
        }

        return buffer != null ? buffer.toString() : null;
    }
    
    public String explodeExpression( String expression )
    {
        StringBuffer buffer = null;

        if ( expression != null )
        {
            final Matcher matcher = OPERAND_PATTERN.matcher( expression );

            buffer = new StringBuffer();

            while ( matcher.find() )
            {
                final DataElementOperand operand = DataElementOperand.getOperand( matcher.group() );
                
                if ( operand.isTotal() )
                {
                    final StringBuilder replace = new StringBuilder( PAR_OPEN );

                    final DataElement dataElement = dataElementService.getDataElement( operand.getDataElementId() );

                    final DataElementCategoryCombo categoryCombo = dataElement.getCategoryCombo();

                    for ( DataElementCategoryOptionCombo categoryOptionCombo : categoryCombo.getOptionCombos() )
                    {
                        replace.append( EXP_OPEN ).append( dataElement.getId() ).append( SEPARATOR ).append(
                            categoryOptionCombo.getId() ).append( EXP_CLOSE ).append( "+" );
                    }

                    replace.deleteCharAt( replace.length() - 1 ).append( PAR_CLOSE );

                    matcher.appendReplacement( buffer, replace.toString() );
                }
            }

            matcher.appendTail( buffer );
        }

        return buffer != null ? buffer.toString() : null;
    }
    
    public String substituteExpression( String expression, Integer days )
    {
        StringBuffer buffer = null;
        
        if ( expression != null )
        {
            buffer = new StringBuffer();

            final Matcher matcher = FORMULA_PATTERN.matcher( expression );
            while ( matcher.find() )
            {
                String match = matcher.group();

                if ( DAYS_EXPRESSION.equals( match ) ) // Days
                {
                    match = days != null ? String.valueOf( days ) : NULL_REPLACEMENT;
                }
                else if ( match.matches( CONSTANT_EXPRESSION ) ) // Constant
                {
                    final Constant constant = constantService.getConstant( Integer.parseInt( stripConstantExpression( match ) ) );
                    
                    match = constant != null ? String.valueOf( constant.getValue() ) : NULL_REPLACEMENT; 
                }
                
                matcher.appendReplacement( buffer, match );
            }

            matcher.appendTail( buffer );
        }

        return buffer != null ? buffer.toString() : null;
    }
    
    public String generateExpression( String expression, Period period, OrganisationUnit source,
        boolean nullIfNoValues, boolean aggregated, Integer days )
    {
        StringBuffer buffer = null;

        if ( expression != null )
        {
            buffer = new StringBuffer();

            final Matcher matcher = FORMULA_PATTERN.matcher( expression );

            while ( matcher.find() )
            {
                String match = matcher.group();
                
                if ( DAYS_EXPRESSION.equals( match ) ) // Days
                {
                    match = days != null ? String.valueOf( days ) : NULL_REPLACEMENT;
                }
                else if ( match.matches( CONSTANT_EXPRESSION ) ) // Constant
                {
                    final Constant constant = constantService.getConstant( Integer.parseInt( stripConstantExpression( match ) ) );
                    
                    match = constant != null ? String.valueOf( constant.getValue() ) : NULL_REPLACEMENT; 
                }
                else // Operand
                {
                    final DataElementOperand operand = DataElementOperand.getOperand( match );

                    String value = null;

                    if ( aggregated )
                    {
                        final Double aggregatedValue = aggregatedDataValueService.getAggregatedDataValue( operand
                            .getDataElementId(), operand.getOptionComboId(), period.getId(), source.getId() );

                        value = aggregatedValue != null ? String.valueOf( aggregatedValue ) : null;
                    }
                    else
                    {
                        value = dataValueService.getValue( operand.getDataElementId(), period.getId(), source.getId(),
                            operand.getOptionComboId() );
                    }

                    if ( value == null && nullIfNoValues )
                    {
                        return null;
                    }

                    match = value != null ? value : NULL_REPLACEMENT;
                }

                matcher.appendReplacement( buffer, match );
            }

            matcher.appendTail( buffer );
        }

        return buffer != null ? buffer.toString() : null;
    }

    public String generateExpression( String expression, Map<DataElementOperand, Double> valueMap, Map<Integer, Double> constantMap, Integer days )
    {
        StringBuffer buffer = null;

        if ( expression != null )
        {
            final Matcher matcher = FORMULA_PATTERN.matcher( expression );

            buffer = new StringBuffer();

            while ( matcher.find() )
            {
                String match = matcher.group();

                if ( DAYS_EXPRESSION.equals( match ) ) // Days
                {
                    match = days != null ? String.valueOf( days ) : NULL_REPLACEMENT;
                }
                else if ( match.matches( CONSTANT_EXPRESSION ) ) // Constant
                {
                    final Double constant = constantMap.get( Integer.parseInt( stripConstantExpression( match ) ) );
                    
                    match = constant != null ? String.valueOf( constant ) : NULL_REPLACEMENT;
                }
                else // Operand
                {
                    final DataElementOperand operand = DataElementOperand.getOperand( match );

                    final Double aggregatedValue = valueMap.get( operand );

                    match = aggregatedValue != null ? String.valueOf( aggregatedValue ) : NULL_REPLACEMENT;
                }

                matcher.appendReplacement( buffer, match );
            }

            matcher.appendTail( buffer );
        }

        return buffer != null ? buffer.toString() : null;
    }

    public Set<DataElementOperand> getOperandsInIndicators( Collection<Indicator> indicators )
    {
        final Set<DataElementOperand> operands = new HashSet<DataElementOperand>();
        
        for ( Indicator indicator : indicators )
        {
            Set<DataElementOperand> temp = getOperandsInExpression( indicator.getExplodedNumerator() );
            operands.addAll( temp != null ? temp : new HashSet<DataElementOperand>() );
            
            temp = getOperandsInExpression( indicator.getExplodedDenominator() );            
            operands.addAll( temp != null ? temp : new HashSet<DataElementOperand>() );
        }
        
        return operands;
    }
    
    private static final String stripConstantExpression( String match )
    {
        return match != null ? match.replaceAll( "[\\[C\\]]", "" ) : null;
    }
}
