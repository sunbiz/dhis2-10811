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

import static org.hisp.dhis.i18n.I18nUtils.*;
import static org.hisp.dhis.system.util.MathUtils.expressionIsTrue;
import static org.hisp.dhis.system.util.MathUtils.getRounded;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hisp.dhis.common.GenericIdentifiableObjectStore;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.i18n.I18nService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.system.grid.ListGrid;
import org.hisp.dhis.system.util.CompositeCounter;
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

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private I18nService i18nService;

    public void setI18nService( I18nService service )
    {
        i18nService = service;
    }

    // -------------------------------------------------------------------------
    // ValidationRule business logic
    // -------------------------------------------------------------------------

    public Grid getAggregateValidationResult( Collection<ValidationResult> results, List<Period> periods,
        List<OrganisationUnit> sources )
    {
        int number = validationRuleStore.getCount();

        Grid grid = new ListGrid();

        CompositeCounter counter = new CompositeCounter();

        for ( ValidationResult result : results )
        {
            counter.count( result.getPeriod(), result.getSource() );
        }

        grid.addRow();
        grid.addValue( StringUtils.EMPTY );

        for ( Period period : periods )
        {
            grid.addValue( period.getName() );
        }

        for ( OrganisationUnit source : sources )
        {
            grid.addRow();
            grid.addValue( source.getName() );

            for ( Period period : periods )
            {
                double percentage = (double) (100 * counter.getCount( period, source )) / number;

                grid.addValue( String.valueOf( getRounded( percentage, DECIMALS ) ) );
            }
        }

        return grid;
    }

    public Collection<ValidationResult> validateAggregate( Date startDate, Date endDate,
        Collection<OrganisationUnit> sources )
    {
        Collection<Period> periods = periodService.getPeriodsBetweenDates( startDate, endDate );

        Collection<ValidationRule> validationRules = getAllValidationRules();

        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        for ( OrganisationUnit source : sources )
        {
            for ( Period period : periods )
            {
                validationViolations.addAll( validateInternal( period, source, validationRules, true,
                    validationViolations.size() ) );
            }
        }

        return validationViolations;
    }

    public Collection<ValidationResult> validateAggregate( Date startDate, Date endDate,
        Collection<OrganisationUnit> sources, ValidationRuleGroup group )
    {
        Collection<Period> periods = periodService.getPeriodsBetweenDates( startDate, endDate );

        Collection<DataSet> dataSets = dataSetService.getDataSetsBySources( sources );

        Collection<DataElement> dataElements = dataElementService.getDataElementsByDataSets( dataSets );

        Collection<ValidationRule> validationRules = getValidationRulesByDataElements( dataElements );

        validationRules.retainAll( group.getMembers() );

        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        for ( OrganisationUnit source : sources )
        {
            for ( Period period : periods )
            {
                validationViolations.addAll( validateInternal( period, source, validationRules, true,
                    validationViolations.size() ) );
            }
        }

        return validationViolations;
    }

    public Collection<ValidationResult> validate( Date startDate, Date endDate, Collection<OrganisationUnit> sources )
    {
        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        Collection<Period> relevantPeriods = periodService.getPeriodsBetweenDates( startDate, endDate );

        for ( OrganisationUnit source : sources )
        {
            Collection<ValidationRule> relevantRules = getRelevantValidationRules( source.getDataElementsInDataSets() );

            if ( relevantRules != null && relevantRules.size() > 0 )
            {
                for ( Period period : relevantPeriods )
                {
                    validationViolations.addAll( validateInternal( period, source, relevantRules, false,
                        validationViolations.size() ) );
                }
            }
        }

        return validationViolations;
    }

    public Collection<ValidationResult> validate( Date startDate, Date endDate, Collection<OrganisationUnit> sources,
        ValidationRuleGroup group )
    {
        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        Collection<Period> relevantPeriods = periodService.getPeriodsBetweenDates( startDate, endDate );

        for ( OrganisationUnit source : sources )
        {
            Collection<ValidationRule> relevantRules = getRelevantValidationRules( source.getDataElementsInDataSets() );
            relevantRules.retainAll( group.getMembers() );

            if ( relevantRules != null && relevantRules.size() > 0 )
            {
                for ( Period period : relevantPeriods )
                {
                    validationViolations.addAll( validateInternal( period, source, relevantRules, false,
                        validationViolations.size() ) );
                }
            }
        }

        return validationViolations;
    }

    public Collection<ValidationResult> validate( Date startDate, Date endDate, OrganisationUnit source )
    {
        Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        Collection<ValidationRule> relevantRules = getRelevantValidationRules( source.getDataElementsInDataSets() );

        Collection<Period> relevantPeriods = periodService.getPeriodsBetweenDates( startDate, endDate );

        for ( Period period : relevantPeriods )
        {
            validationViolations.addAll( validateInternal( period, source, relevantRules, false, validationViolations
                .size() ) );
        }

        return validationViolations;
    }

    public Collection<ValidationResult> validate( DataSet dataSet, Period period, OrganisationUnit source )
    {
        return validateInternal( period, source, getRelevantValidationRules( dataSet.getDataElements() ), false, 0 );
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
     * @returns a collection of rules that did not pass validation.
     */
    private Collection<ValidationResult> validateInternal( final Period period, final OrganisationUnit source,
        final Collection<ValidationRule> validationRules, boolean aggregate, int currentSize )
    {
        final Collection<ValidationResult> validationViolations = new HashSet<ValidationResult>();

        if ( currentSize < MAX_VIOLATIONS )
        {
            Double leftSide = null;
            Double rightSide = null;

            boolean violation = false;

            for ( final ValidationRule validationRule : validationRules )
            {
                if ( validationRule.getPeriodType() != null
                    && validationRule.getPeriodType().equals( period.getPeriodType() ) )
                {
                    leftSide = expressionService.getExpressionValue( validationRule.getLeftSide(), period, source,
                        true, aggregate, null );

                    if ( leftSide != null )
                    {
                        rightSide = expressionService.getExpressionValue( validationRule.getRightSide(), period,
                            source, true, aggregate, null );

                        if ( rightSide != null )
                        {
                            violation = !expressionIsTrue( leftSide, validationRule.getOperator(), rightSide );

                            if ( violation )
                            {
                                validationViolations.add( new ValidationResult( period, source, validationRule,
                                    getRounded( leftSide, DECIMALS ), getRounded( rightSide, DECIMALS ) ) );
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
    private Collection<ValidationRule> getRelevantValidationRules( Collection<DataElement> dataElements )
    {
        final Set<ValidationRule> relevantValidationRules = new HashSet<ValidationRule>();

        for ( ValidationRule validationRule : getAllValidationRules() )
        {
            for ( DataElement dataElement : dataElements )
            {
                if ( validationRule.getPeriodType() != null && dataElement.getPeriodType() != null
                    && validationRule.getPeriodType().equals( dataElement.getPeriodType() ) )
                {
                    if ( validationRule.getLeftSide().getDataElementsInExpression().contains( dataElement )
                        || validationRule.getRightSide().getDataElementsInExpression().contains( dataElement ) )
                    {
                        relevantValidationRules.add( validationRule );
                    }
                }
            }
        }

        return relevantValidationRules;
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
