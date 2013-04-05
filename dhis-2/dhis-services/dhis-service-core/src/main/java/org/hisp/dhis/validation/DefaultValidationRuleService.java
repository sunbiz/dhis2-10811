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

import static org.hisp.dhis.i18n.I18nUtils.getCountByName;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetween;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetweenByName;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsByName;
import static org.hisp.dhis.i18n.I18nUtils.i18n;
import static org.hisp.dhis.system.util.MathUtils.expressionIsTrue;
import static org.hisp.dhis.system.util.MathUtils.getRounded;
import static org.hisp.dhis.system.util.MathUtils.zeroIfNull;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.common.GenericIdentifiableObjectStore;
import org.hisp.dhis.constant.ConstantService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.dataentryform.DataEntryFormService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.expression.Operator;
import org.hisp.dhis.i18n.I18nService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.system.util.Filter;
import org.hisp.dhis.system.util.FilterUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Margrethe Store
 * @author Lars Helge Overland
 * @version $Id
 */
@Transactional
public class DefaultValidationRuleService
    implements ValidationRuleService
{
    private static final int DECIMALS = 1;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ValidationRuleStore validationRuleStore;

    public void setValidationRuleStore( ValidationRuleStore validationRuleStore )
    {
        this.validationRuleStore = validationRuleStore;
    }

    private GenericIdentifiableObjectStore<ValidationRuleGroup> validationRuleGroupStore;

    public void setValidationRuleGroupStore( GenericIdentifiableObjectStore<ValidationRuleGroup> validationRuleGroupStore )
    {
        this.validationRuleGroupStore = validationRuleGroupStore;
    }

    private ExpressionService expressionService;

    public void setExpressionService( ExpressionService expressionService )
    {
        this.expressionService = expressionService;
    }
    
    private DataEntryFormService dataEntryFormService;

    public void setDataEntryFormService( DataEntryFormService dataEntryFormService )
    {
        this.dataEntryFormService = dataEntryFormService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }
    
    private ConstantService constantService;

    public void setConstantService( ConstantService constantService )
    {
        this.constantService = constantService;
    }

    private I18nService i18nService;

    public void setI18nService( I18nService service )
    {
        i18nService = service;
    }

    // -------------------------------------------------------------------------
    // ValidationRule business logic
    // -------------------------------------------------------------------------

    public Collection<ValidationResult> validate( Date startDate, Date endDate, Collection<OrganisationUnit> sources )
    {
        Map<String, Double> constantMap = constantService.getConstantMap();
        
        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        Collection<Period> relevantPeriods = periodService.getPeriodsBetweenDates( startDate, endDate );

        for ( OrganisationUnit source : sources )
        {
            Collection<ValidationRule> relevantRules = getRelevantValidationRules( source.getDataElementsInDataSets() );
            
            Set<DataElement> dataElements = getDataElementsInValidationRules( relevantRules ); //TODO move outside loop?
            
            if ( relevantRules != null && relevantRules.size() > 0 )
            {
                for ( Period period : relevantPeriods )
                {
                    validationViolations.addAll( validateInternal( period, source, relevantRules, dataElements, constantMap, 
                        validationViolations.size() ) );
                }
            }
        }

        return validationViolations;
    }

    public Collection<ValidationResult> validate( Date startDate, Date endDate, Collection<OrganisationUnit> sources,
        ValidationRuleGroup group )
    {
        Map<String, Double> constantMap = constantService.getConstantMap();
        
        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        Collection<Period> relevantPeriods = periodService.getPeriodsBetweenDates( startDate, endDate );

        for ( OrganisationUnit source : sources )
        {
            Collection<ValidationRule> relevantRules = getRelevantValidationRules( source.getDataElementsInDataSets() );
            relevantRules.retainAll( group.getMembers() );

            Set<DataElement> dataElements = getDataElementsInValidationRules( relevantRules );
            
            if ( !relevantRules.isEmpty() )
            {
                for ( Period period : relevantPeriods )
                {
                    validationViolations.addAll( validateInternal( period, source, relevantRules, dataElements, constantMap, 
                        validationViolations.size() ) );
                }
            }
        }

        return validationViolations;
    }

    public Collection<ValidationResult> validate( Date startDate, Date endDate, OrganisationUnit source )
    {
        Map<String, Double> constantMap = constantService.getConstantMap();
        
        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        Collection<ValidationRule> relevantRules = getRelevantValidationRules( source.getDataElementsInDataSets() );

        Set<DataElement> dataElements = getDataElementsInValidationRules( relevantRules );
        
        Collection<Period> relevantPeriods = periodService.getPeriodsBetweenDates( startDate, endDate );

        for ( Period period : relevantPeriods )
        {
            validationViolations.addAll( validateInternal( period, source, relevantRules, dataElements, constantMap, validationViolations
                .size() ) );
        }

        return validationViolations;
    }

    public Collection<ValidationResult> validate( DataSet dataSet, Period period, OrganisationUnit source )
    {
        Map<String, Double> constantMap = constantService.getConstantMap();
        
        Collection<ValidationRule> relevantRules = null;
        
        if ( DataSet.TYPE_CUSTOM.equals( dataSet.getDataSetType() ) )
        {
            relevantRules = getRelevantValidationRules( dataSet );
        }
        else
        {
            relevantRules = getRelevantValidationRules( dataSet.getDataElements() );
        }
        
        Set<DataElement> dataElements = getDataElementsInValidationRules( relevantRules );
        
        return validateInternal( period, source, relevantRules, dataElements, constantMap, 0 );
    }

    public Collection<DataElement> getDataElementsInValidationRules()
    {
        Set<DataElement> dataElements = new HashSet<DataElement>();

        for ( ValidationRule rule : getAllValidationRules() )
        {
            dataElements.addAll( rule.getLeftSide().getDataElementsInExpression() );
            dataElements.addAll( rule.getRightSide().getDataElementsInExpression() );
        }

        return dataElements;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Validates a collection of validation rules.
     * 
     * @param period the period to validate for.
     * @param source the source to validate for.
     * @param validationRules the rules to validate.
     * @param dataElementsInRules the data elements which are part of the rules expressions.
     * @param constantMap the constants which are part of the rule expressions.
     * @param currentSize the current number of validation violations.
     * @returns a collection of rules that did not pass validation.
     */
    private Collection<ValidationResult> validateInternal( Period period, OrganisationUnit unit,
        Collection<ValidationRule> validationRules, Set<DataElement> dataElementsInRules, Map<String, Double> constantMap, int currentSize )
    {
        Map<DataElementOperand, Double> valueMap = dataValueService.getDataValueMap( dataElementsInRules, period, unit );

        final Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        if ( currentSize < MAX_VIOLATIONS && !valueMap.isEmpty() )
        {
            Double leftSide = null;
            Double rightSide = null;

            boolean violation = false;

            for ( final ValidationRule validationRule : validationRules )
            {
                if ( validationRule.getPeriodType() != null
                    && validationRule.getPeriodType().equals( period.getPeriodType() ) )
                {
                    Operator operator = validationRule.getOperator();
                    
                    leftSide = expressionService.getExpressionValue( validationRule.getLeftSide(), valueMap, constantMap, null );

                    if ( leftSide != null || Operator.compulsory_pair.equals( operator ) )
                    {
                        rightSide = expressionService.getExpressionValue( validationRule.getRightSide(), valueMap, constantMap, null );

                        if ( rightSide != null || Operator.compulsory_pair.equals( operator )  )
                        {
                            if ( Operator.compulsory_pair.equals( operator ) )
                            {
                                violation = ( leftSide != null && rightSide == null ) || ( leftSide == null && rightSide != null );
                            }
                            else
                            {
                                violation = !expressionIsTrue( leftSide, operator, rightSide );
                            }
                            
                            if ( violation )
                            {
                                validationViolations.add( new ValidationResult( period, unit, validationRule,
                                    getRounded( zeroIfNull( leftSide ), DECIMALS ), getRounded( zeroIfNull( rightSide ), DECIMALS ) ) );
                            }
                        }
                    }
                }
            }
        }

        return validationViolations;
    }

    /**
     * Returns all validation rules which have data elements assigned to it
     * which are members of the given data set.
     * 
     * @param dataSet the data set.
     * @return all validation rules which have data elements assigned to it
     *         which are members of the given data set.
     */
    private Collection<ValidationRule> getRelevantValidationRules( Set<DataElement> dataElements )
    {
        Set<ValidationRule> relevantValidationRules = new HashSet<ValidationRule>();
        
        Set<DataElement> validationRuleElements = new HashSet<DataElement>();
        
        for ( ValidationRule validationRule : getAllValidationRules() )
        {
            validationRuleElements.clear();
            validationRuleElements.addAll( validationRule.getLeftSide().getDataElementsInExpression() );
            validationRuleElements.addAll( validationRule.getRightSide().getDataElementsInExpression() );
            
            if ( dataElements.containsAll( validationRuleElements ) )
            {
                relevantValidationRules.add( validationRule );
            }
        }

        return relevantValidationRules;
    }
    
    public Collection<ValidationRule> getRelevantValidationRules( DataSet dataSet )
    {
        Set<ValidationRule> relevantValidationRules = new HashSet<ValidationRule>();
        
        Set<DataElementOperand> operands = dataEntryFormService.getOperandsInDataEntryForm( dataSet );
        
        Set<DataElementOperand> validationRuleOperands = new HashSet<DataElementOperand>();
        
        for ( ValidationRule validationRule : getAllValidationRules() )
        {
            validationRuleOperands.clear();
            validationRuleOperands.addAll( expressionService.getOperandsInExpression( validationRule.getLeftSide().getExpression() ) );
            validationRuleOperands.addAll( expressionService.getOperandsInExpression( validationRule.getRightSide().getExpression() ) );
            
            if ( operands.containsAll( validationRuleOperands ) )
            {
                relevantValidationRules.add( validationRule );
            }
        }
        
        return relevantValidationRules;
    }

    /**
     * Returns all validation rules referred to in the left and right side expressions
     * of the given validation rules.
     * 
     * @param validationRules the validation rules.
     * @return a collection of data elements.
     */
    private Set<DataElement> getDataElementsInValidationRules( Collection<ValidationRule> validationRules )
    {
        Set<DataElement> dataElements = new HashSet<DataElement>();

        for ( ValidationRule rule : validationRules )
        {
            dataElements.addAll( rule.getLeftSide().getDataElementsInExpression() );
            dataElements.addAll( rule.getRightSide().getDataElementsInExpression() );
        }

        return dataElements;
    }
    
    // -------------------------------------------------------------------------
    // ValidationRule CRUD operations
    // -------------------------------------------------------------------------

    public int saveValidationRule( ValidationRule validationRule )
    {
        return validationRuleStore.save( validationRule );
    }

    public void updateValidationRule( ValidationRule validationRule )
    {
        validationRuleStore.update( validationRule );
    }

    public void deleteValidationRule( ValidationRule validationRule )
    {
        validationRuleStore.delete( validationRule );
    }

    public ValidationRule getValidationRule( int id )
    {
        return i18n( i18nService, validationRuleStore.get( id ) );
    }

    public ValidationRule getValidationRule( String uid )
    {
        return i18n( i18nService, validationRuleStore.getByUid( uid ) );
    }

    public ValidationRule getValidationRuleByName( String name )
    {
        return i18n( i18nService, validationRuleStore.getByName( name ) );
    }

    public Collection<ValidationRule> getAllValidationRules()
    {
        return i18n( i18nService, validationRuleStore.getAll() );
    }

    public Collection<ValidationRule> getValidationRules( final Collection<Integer> identifiers )
    {
        Collection<ValidationRule> objects = getAllValidationRules();

        return identifiers == null ? objects : FilterUtils.filter( objects, new Filter<ValidationRule>()
        {
            public boolean retain( ValidationRule object )
            {
                return identifiers.contains( object.getId() );
            }
        } );
    }

    public Collection<ValidationRule> getValidationRulesByName( String name )
    {        
        return getObjectsByName( i18nService, validationRuleStore, name );
    }
    
    public Collection<ValidationRule> getValidationRulesByDataElements( Collection<DataElement> dataElements )
    {
        return i18n( i18nService, validationRuleStore.getValidationRulesByDataElements( dataElements ) );
    }

    public int getValidationRuleCount()
    {
        return validationRuleStore.getCount();
    }

    public int getValidationRuleCountByName( String name )
    {
        return getCountByName( i18nService, validationRuleStore, name );
    }

    public Collection<ValidationRule> getValidationRulesBetween( int first, int max )
    {
        return getObjectsBetween( i18nService, validationRuleStore, first, max );
    }

    public Collection<ValidationRule> getValidationRulesBetweenByName( String name, int first, int max )
    {
        return getObjectsBetweenByName( i18nService, validationRuleStore, name, first, max );
    }

    // -------------------------------------------------------------------------
    // ValidationRuleGroup CRUD operations
    // -------------------------------------------------------------------------

    public int addValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {
        return validationRuleGroupStore.save( validationRuleGroup );
    }

    public void deleteValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {
        validationRuleGroupStore.delete( validationRuleGroup );
    }

    public void updateValidationRuleGroup( ValidationRuleGroup validationRuleGroup )
    {
        validationRuleGroupStore.update( validationRuleGroup );
    }

    public ValidationRuleGroup getValidationRuleGroup( int id )
    {
        return i18n( i18nService, validationRuleGroupStore.get( id ) );
    }

    public ValidationRuleGroup getValidationRuleGroup( int id, boolean i18nValidationRules )
    {
        ValidationRuleGroup group = getValidationRuleGroup( id );
        
        if ( i18nValidationRules )
        {
            i18n( i18nService, group.getMembers() );
        }
        
        return group;
    }

    public ValidationRuleGroup getValidationRuleGroup( String uid )
    {
        return i18n( i18nService, validationRuleGroupStore.getByUid( uid ) );
    }
    
    public Collection<ValidationRuleGroup> getAllValidationRuleGroups()
    {
        return i18n( i18nService, validationRuleGroupStore.getAll() );
    }

    public ValidationRuleGroup getValidationRuleGroupByName( String name )
    {
        return i18n( i18nService, validationRuleGroupStore.getByName( name ) );
    }

    public int getValidationRuleGroupCount()
    {
        return validationRuleGroupStore.getCount();
    }

    public int getValidationRuleGroupCountByName( String name )
    {
        return getCountByName( i18nService, validationRuleGroupStore, name );
    }

    public Collection<ValidationRuleGroup> getValidationRuleGroupsBetween( int first, int max )
    {
        return getObjectsBetween( i18nService, validationRuleGroupStore, first, max );
    }

    public Collection<ValidationRuleGroup> getValidationRuleGroupsBetweenByName( String name, int first, int max )
    {
        return getObjectsBetweenByName( i18nService, validationRuleGroupStore, name, first, max );
    }
}
