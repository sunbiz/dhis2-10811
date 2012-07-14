package org.hisp.dhis.analytics.data;

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

import static org.hisp.dhis.analytics.AggregationType.AVERAGE_BOOL;
import static org.hisp.dhis.analytics.AggregationType.AVERAGE_INT;
import static org.hisp.dhis.analytics.AggregationType.AVERAGE_INT_DISAGGREGATION;
import static org.hisp.dhis.analytics.AggregationType.SUM;
import static org.hisp.dhis.analytics.DataQueryParams.INDICATOR_DIM_ID;
import static org.hisp.dhis.analytics.DataQueryParams.CATEGORYOPTIONCOMBO_DIM_ID;
import static org.hisp.dhis.dataelement.DataElement.AGGREGATION_OPERATOR_AVERAGE;
import static org.hisp.dhis.dataelement.DataElement.AGGREGATION_OPERATOR_SUM;
import static org.hisp.dhis.dataelement.DataElement.VALUE_TYPE_BOOL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hisp.dhis.analytics.AggregationType;
import org.hisp.dhis.analytics.DataQueryParams;
import org.hisp.dhis.analytics.Dimension;
import org.hisp.dhis.analytics.IllegalQueryException;
import org.hisp.dhis.analytics.QueryPlanner;
import org.hisp.dhis.analytics.table.PartitionUtils;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.system.util.ListMap;
import org.hisp.dhis.system.util.MathUtils;
import org.hisp.dhis.system.util.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultQueryPlanner
    implements QueryPlanner
{
    //TODO call getLevelOrgUnitMap once?
    //TODO shortcut group by methods when only 1 option?
    
    @Autowired
    private OrganisationUnitService organisationUnitService;
    
    // -------------------------------------------------------------------------
    // DefaultQueryPlanner implementation
    // -------------------------------------------------------------------------

    public void validate( DataQueryParams params )
        throws IllegalQueryException
    {
        if ( params == null || params.getDimensions().isEmpty() )
        {
            throw new IllegalQueryException( "At least one dimension must be specified" );
        }
        
        if ( !params.dimensionsAsFilters().isEmpty() )
        {
            throw new IllegalQueryException( "Dimensions cannot be specified as dimension and filter simultaneously: " + params.dimensionsAsFilters() );
        }
        
        if ( !params.hasPeriods() && !params.isSkipPartitioning() )
        {
            throw new IllegalQueryException( "At least one period must be specified as dimension or filter" );
        }
        
        if ( params.getFilters().contains( new Dimension( INDICATOR_DIM_ID ) ) )
        {
            throw new IllegalQueryException( "Indicators cannot be specified as filter" );
        }
        
        if ( params.getFilters().contains( new Dimension( CATEGORYOPTIONCOMBO_DIM_ID ) ) )
        {
            throw new IllegalQueryException( "Category option combos cannot be specified as filter" );
        }
        
        //TODO check if any dimension occur more than once
    }
    
    public List<DataQueryParams> planQuery( DataQueryParams params, int optimalQueries, String tableName )
    {
        validate( params );

        // ---------------------------------------------------------------------
        // Group queries by partition, period type and organisation unit level
        // ---------------------------------------------------------------------
        
        params = new DataQueryParams( params );
        
        List<DataQueryParams> queries = new ArrayList<DataQueryParams>();
        
        List<DataQueryParams> groupedByPartition = groupByPartition( params, tableName );
        
        for ( DataQueryParams byPartition : groupedByPartition )
        {
            List<DataQueryParams> groupedByOrgUnitLevel = groupByOrgUnitLevel( byPartition );
            
            for ( DataQueryParams byOrgUnitLevel : groupedByOrgUnitLevel )
            {
                List<DataQueryParams> groupedByPeriodType = groupByPeriodType( byOrgUnitLevel );
                
                for ( DataQueryParams byPeriodType : groupedByPeriodType )
                {
                    List<DataQueryParams> groupedByAggregationType = groupByAggregationType( byPeriodType );
                    
                    for ( DataQueryParams byAggregationType : groupedByAggregationType )
                    {
                        if ( AVERAGE_INT_DISAGGREGATION.equals( byAggregationType.getAggregationType() ) )
                        {
                            List<DataQueryParams> groupedByDataPeriodType = groupByDataPeriodType( byAggregationType );
                            
                            for ( DataQueryParams byDataPeriodType : groupedByDataPeriodType )
                            {
                                byDataPeriodType.setTableName( byPartition.getTableName() );
                                byDataPeriodType.setOrganisationUnitLevel( byOrgUnitLevel.getOrganisationUnitLevel() );
                                byDataPeriodType.setPeriodType( byPeriodType.getPeriodType() );
                                byDataPeriodType.setAggregationType( byAggregationType.getAggregationType() );
                                
                                queries.add( byDataPeriodType );
                            }
                        }
                        else
                        {
                            byAggregationType.setTableName( byPartition.getTableName() );
                            byAggregationType.setOrganisationUnitLevel( byOrgUnitLevel.getOrganisationUnitLevel() );
                            byAggregationType.setPeriodType( byPeriodType.getPeriodType() );
                            
                            queries.add( byAggregationType );
                        }
                    }
                }
            }
        }

        if ( queries.size() >= optimalQueries )
        {
            return queries;
        }

        // ---------------------------------------------------------------------
        // Group by data element
        // ---------------------------------------------------------------------
        
        queries = splitByDimensionOrFilter( queries, DataQueryParams.DATAELEMENT_DIM_ID, optimalQueries );

        if ( queries.size() >= optimalQueries )
        {
            return queries;
        }

        // ---------------------------------------------------------------------
        // Group by organisation unit
        // ---------------------------------------------------------------------
        
        queries = splitByDimensionOrFilter( queries, DataQueryParams.ORGUNIT_DIM_ID, optimalQueries );
        
        return queries;
    }
        
    public boolean canQueryFromDataMart( DataQueryParams params )
    {
        return true;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    /**
     * Splits the given list of queries in sub queries on the given dimension.
     */
    private List<DataQueryParams> splitByDimensionOrFilter( List<DataQueryParams> queries, String dimension, int optimalQueries )
    {
        int optimalForSubQuery = MathUtils.divideToFloor( optimalQueries, queries.size() );
        
        List<DataQueryParams> subQueries = new ArrayList<DataQueryParams>();
        
        for ( DataQueryParams query : queries )
        {
            List<IdentifiableObject> values = query.getDimensionOrFilter( dimension );

            if ( values == null || values.isEmpty() )
            {
                subQueries.add( new DataQueryParams( query ) );
                continue;
            }
            
            List<List<IdentifiableObject>> valuePages = new PaginatedList<IdentifiableObject>( values ).setNumberOfPages( optimalForSubQuery ).getPages();
            
            for ( List<IdentifiableObject> valuePage : valuePages )
            {
                DataQueryParams subQuery = new DataQueryParams( query );
                subQuery.resetDimensionOrFilter( dimension, valuePage );
                subQueries.add( subQuery );
            }
        }

        return subQueries;
    }
    
    /**
     * Groups the given query into sub queries based on its periods and which 
     * partition it should be executed against. Sets the partition table name on
     * each query. Queries are grouped based on both dimensions and filters.
     */
    private List<DataQueryParams> groupByPartition( DataQueryParams params, String tableName )
    {
        List<DataQueryParams> queries = new ArrayList<DataQueryParams>();

        if ( params.isSkipPartitioning() )
        {
            params.setTableName( tableName );
            queries.add( params );
        }
        else if ( params.getPeriods() != null && !params.getPeriods().isEmpty() )
        {
            ListMap<String, IdentifiableObject> tablePeriodMap = PartitionUtils.getTablePeriodMap( params.getPeriods(), tableName );
            
            for ( String table : tablePeriodMap.keySet() )
            {
                DataQueryParams query = new DataQueryParams( params );
                query.setPeriods( tablePeriodMap.get( table ) );
                query.setTableName( table );
                queries.add( query );            
            }
        }
        else if ( params.getFilterPeriods() != null && !params.getFilterPeriods().isEmpty() )
        {
            ListMap<String, IdentifiableObject> tablePeriodMap = PartitionUtils.getTablePeriodMap( params.getFilterPeriods(), tableName );
            
            for ( String table : tablePeriodMap.keySet() )
            {
                DataQueryParams query = new DataQueryParams( params );
                query.setFilterPeriods( tablePeriodMap.get( table ) );
                query.setTableName( table );
                queries.add( query );            
            }
        }
        else
        {
            throw new IllegalQueryException( "Query does not contain any period dimension options" );
        }
        
        return queries;
    }
    
    /**
     * Groups the given query into sub queries based on the period type of its
     * periods. Sets the period type name on each query.
     */
    private List<DataQueryParams> groupByPeriodType( DataQueryParams params )
    {
        List<DataQueryParams> queries = new ArrayList<DataQueryParams>();

        if ( params.isSkipPartitioning() )
        {
            queries.add( params );
        }
        else if ( params.getPeriods() != null && !params.getPeriods().isEmpty() )
        {
            ListMap<String, IdentifiableObject> periodTypePeriodMap = getPeriodTypePeriodMap( params.getPeriods() );
    
            for ( String periodType : periodTypePeriodMap.keySet() )
            {
                DataQueryParams query = new DataQueryParams( params );
                query.setPeriods( periodTypePeriodMap.get( periodType ) );
                query.setPeriodType( periodType );
                queries.add( query );
            }
        }
        else if ( params.getFilterPeriods() != null && !params.getFilterPeriods().isEmpty() )
        {
            ListMap<String, IdentifiableObject> periodTypePeriodMap = getPeriodTypePeriodMap( params.getFilterPeriods() );
            
            for ( String periodType : periodTypePeriodMap.keySet() )
            {
                DataQueryParams query = new DataQueryParams( params );
                query.setFilterPeriods( periodTypePeriodMap.get( periodType ) );
                query.setPeriodType( periodType );
                queries.add( query );
            }
        }
        else
        {
            throw new IllegalQueryException( "Query does not contain any period dimension options" );
        }
        
        return queries;        
    }
    
    /**
     * Groups the given query into sub queries based on the level of its organisation 
     * units. Sets the organisation unit level on each query.
     */
    private List<DataQueryParams> groupByOrgUnitLevel( DataQueryParams params )
    {
        List<DataQueryParams> queries = new ArrayList<DataQueryParams>();

        if ( params.getOrganisationUnits() != null && !params.getOrganisationUnits().isEmpty() )
        {
            ListMap<Integer, IdentifiableObject> levelOrgUnitMap = getLevelOrgUnitMap( params.getOrganisationUnits() );
            
            for ( Integer level : levelOrgUnitMap.keySet() )
            {
                DataQueryParams query = new DataQueryParams( params );
                query.setOrganisationUnits( levelOrgUnitMap.get( level ) );
                query.setOrganisationUnitLevel( level );
                queries.add( query );
            }
        }
        else if ( params.getFilterOrganisationUnits() != null && !params.getFilterOrganisationUnits().isEmpty() )
        {
            ListMap<Integer, IdentifiableObject> levelOrgUnitMap = getLevelOrgUnitMap( params.getFilterOrganisationUnits() );
            
            for ( Integer level : levelOrgUnitMap.keySet() )
            {
                DataQueryParams query = new DataQueryParams( params );
                query.setFilterOrganisationUnits( levelOrgUnitMap.get( level ) );
                query.setOrganisationUnitLevel( level );
                queries.add( query );
            }
        }
        else
        {
            queries.add( new DataQueryParams( params ) );
            return queries;
        }
        
        return queries;    
    }
    
    /**
     * Groups the given query in sub queries based on the aggregation type of its
     * data elements. The aggregation type can be sum, average aggregation or
     * average disaggregation. Sum means that the data elements have sum aggregation
     * operator. Average aggregation means that the data elements have the average
     * aggregation operator and that the period type of the data elements have 
     * higher or equal frequency than the aggregation period type. Average disaggregation
     * means that the data elements have the average aggregation operator and
     * that the period type of the data elements have lower frequency than the
     * aggregation period type. Average bool means that the data elements have the
     * average aggregation operator and the bool value type.
     * 
     * The query is grouped on data elements if any exists, then on data element
     * group sets if any exists. In the case where multiple data element group
     * sets exists, the query will be grouped on the data element groups of the
     * first group set found. A constraint for data element groups is that they
     * must contain data elements with equal aggregation type. Hence it is not
     * meaningful to split on multiple data element group sets.
     * 
     * If the aggregation type is already set/overridden in the request, the
     * query will be returned unchanged. If there are no dimension options specified
     * the aggregation type will fall back to sum.
     */
    private List<DataQueryParams> groupByAggregationType( DataQueryParams params )
    {
        List<DataQueryParams> queries = new ArrayList<DataQueryParams>();
     
        if ( params.getAggregationType() != null )
        {
            queries.add( new DataQueryParams( params ) );
            return queries;
        }

        Dimension groupSet = null;
        
        if ( params.getDataElements() != null && !params.getDataElements().isEmpty() )
        {
            PeriodType periodType = PeriodType.getPeriodTypeByName( params.getPeriodType() );
            
            ListMap<AggregationType, IdentifiableObject> aggregationTypeDataElementMap = getAggregationTypeDataElementMap( params.getDataElements(), periodType );
            
            for ( AggregationType aggregationType : aggregationTypeDataElementMap.keySet() )
            {
                DataQueryParams query = new DataQueryParams( params );
                query.setDataElements( aggregationTypeDataElementMap.get( aggregationType ) );
                query.setAggregationType( aggregationType );
                queries.add( query );
            }
        }
        else if ( params.getDataElementGroupSets() != null && !params.getDataElementGroupSets().isEmpty() &&
            ( groupSet = params.getDataElementGroupSets().iterator().next() ).getOptions() != null && !groupSet.getOptions().isEmpty() )
        {
            PeriodType periodType = PeriodType.getPeriodTypeByName( params.getPeriodType() );
            
            ListMap<AggregationType, IdentifiableObject> aggregationTypeDataElementGroupMap = getAggregationTypeDataElementGroupMap( groupSet.getOptions(), periodType );

            for ( AggregationType aggregationType : aggregationTypeDataElementGroupMap.keySet() )
            {
                DataQueryParams query = new DataQueryParams( params );
                query.setDataElementGroupSet( groupSet, aggregationTypeDataElementGroupMap.get( aggregationType ) );
                query.setAggregationType( aggregationType );
                queries.add( query );
            }
        }
        else
        {
            DataQueryParams query = new DataQueryParams( params );
            query.setAggregationType( SUM );
            queries.add( query );            
        }        
        
        return queries;
    }
        
    /**
     * Groups the given query in sub queries based on the period type of its
     * data elements. Sets the data period type on each query.
     */
    private List<DataQueryParams> groupByDataPeriodType( DataQueryParams params )
    {
        List<DataQueryParams> queries = new ArrayList<DataQueryParams>();

        if ( params.getDataElements() == null || params.getDataElements().isEmpty() )
        {
            queries.add( new DataQueryParams( params ) );
            return queries;
        }
        
        ListMap<PeriodType, IdentifiableObject> periodTypeDataElementMap = getPeriodTypeDataElementMap( params.getDataElements() );
        
        for ( PeriodType periodType : periodTypeDataElementMap.keySet() )
        {
            DataQueryParams query = new DataQueryParams( params );
            query.setDataElements( periodTypeDataElementMap.get( periodType ) );
            query.setDataPeriodType( periodType );
            queries.add( query );
        }
        
        return queries;
    }
    
    /**
     * Creates a mapping between period type name and period for the given periods.
     */
    private ListMap<String, IdentifiableObject> getPeriodTypePeriodMap( Collection<IdentifiableObject> periods )
    {
        ListMap<String, IdentifiableObject> map = new ListMap<String, IdentifiableObject>();
        
        for ( IdentifiableObject period : periods )
        {
            String periodTypeName = ((Period) period).getPeriodType().getName();
            
            map.putValue( periodTypeName, period );
        }
        
        return map;
    }
    
    /**
     * Creates a mapping between level and organisation unit for the given organisation
     * units.
     */
    private ListMap<Integer, IdentifiableObject> getLevelOrgUnitMap( Collection<IdentifiableObject> orgUnits )
    {
        ListMap<Integer, IdentifiableObject> map = new ListMap<Integer, IdentifiableObject>();
        
        for ( IdentifiableObject orgUnit : orgUnits )
        {
            int level = organisationUnitService.getLevelOfOrganisationUnit( ((OrganisationUnit) orgUnit).getUid() );
            
            map.putValue( level, orgUnit );
        }
        
        return map;
    }
        
    /**
     * Creates a mapping between the aggregation type and data element for the
     * given data elements and period type.
     */
    private ListMap<AggregationType, IdentifiableObject> getAggregationTypeDataElementMap( Collection<IdentifiableObject> dataElements, PeriodType aggregationPeriodType )
    {
        ListMap<AggregationType, IdentifiableObject> map = new ListMap<AggregationType, IdentifiableObject>();
        
        for ( IdentifiableObject element : dataElements )
        {
            DataElement dataElement = (DataElement) element;

            putByAggregationType( map, dataElement.getType(), dataElement.getAggregationOperator(), dataElement, aggregationPeriodType, dataElement.getPeriodType() );
        }
        
        return map;
    }

    /**
     * Creates a mapping between the aggregation type and data element for the
     * given data elements and period type.
     */
    private ListMap<AggregationType, IdentifiableObject> getAggregationTypeDataElementGroupMap( Collection<IdentifiableObject> dataElementGroups, PeriodType aggregationPeriodType )
    {
        ListMap<AggregationType, IdentifiableObject> map = new ListMap<AggregationType, IdentifiableObject>();
        
        for ( IdentifiableObject element : dataElementGroups )
        {
            DataElementGroup group = (DataElementGroup) element;

            putByAggregationType( map, group.getValueType(), group.getAggregationOperator(), group, aggregationPeriodType, group.getPeriodType() );
        }
        
        return map;
    }
    
    /**
     * Puts the given element into the map according to the value type, aggregation
     * operator, aggregation period type and data period type.
     */
    private void putByAggregationType( ListMap<AggregationType, IdentifiableObject> map, String valueType, String aggregationOperator, 
        IdentifiableObject element, PeriodType aggregationPeriodType, PeriodType dataPeriodType )
    {
        if ( AGGREGATION_OPERATOR_SUM.equals( aggregationOperator ) )
        {
            map.putValue( SUM, element );
        }
        else if ( AGGREGATION_OPERATOR_AVERAGE.equals( aggregationOperator ) )
        {
            if ( VALUE_TYPE_BOOL.equals( valueType ) )
            {
                map.putValue( AVERAGE_BOOL, element );
            }
            else
            {
                if ( dataPeriodType == null || aggregationPeriodType == null || aggregationPeriodType.getFrequencyOrder() >= dataPeriodType.getFrequencyOrder() )
                {
                    map.putValue( AVERAGE_INT, element );
                }
                else
                {
                    map.putValue( AVERAGE_INT_DISAGGREGATION, element );
                }
            }
        }
    }

    /**
     * Creates a mapping between the period type and the data element for the
     * given data elements.
     */
    private ListMap<PeriodType, IdentifiableObject> getPeriodTypeDataElementMap( Collection<IdentifiableObject> dataElements )
    {
        ListMap<PeriodType, IdentifiableObject> map = new ListMap<PeriodType, IdentifiableObject>();
        
        for ( IdentifiableObject element : dataElements )
        {
            DataElement dataElement = (DataElement) element;
            
            map.putValue( dataElement.getPeriodType(), element );
        }
        
        return map;
    }
}
