package org.hisp.dhis.analytics;

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

import static org.hisp.dhis.analytics.AggregationType.AVERAGE_INT_DISAGGREGATION;
import static org.hisp.dhis.common.DimensionType.DATASET;
import static org.hisp.dhis.common.DimensionType.ORGANISATIONUNIT;
import static org.hisp.dhis.common.DimensionType.ORGANISATIONUNIT_GROUPSET;
import static org.hisp.dhis.common.DimensionalObject.CATEGORYOPTIONCOMBO_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.DATAELEMENT_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.DATASET_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.DATA_X_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.INDICATOR_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.ORGUNIT_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.PERIOD_DIM_ID;
import static org.hisp.dhis.common.IdentifiableObjectUtils.asList;
import static org.hisp.dhis.common.IdentifiableObjectUtils.getList;
import static org.hisp.dhis.system.util.CollectionUtils.emptyIfNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hisp.dhis.common.BaseDimensionalObject;
import org.hisp.dhis.common.CombinationGenerator;
import org.hisp.dhis.common.DimensionType;
import org.hisp.dhis.common.DimensionalObject;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.ListMap;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementGroupSet;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.system.util.CollectionUtils;
import org.hisp.dhis.system.util.ListUtils;
import org.hisp.dhis.system.util.MapMap;
import org.hisp.dhis.system.util.MathUtils;

/**
 * @author Lars Helge Overland
 */
public class DataQueryParams
{
    public static final String VALUE_ID = "value";    
    public static final String LEVEL_PREFIX = "uidlevel";
    
    public static final String DISPLAY_NAME_DATA_X = "Data";
    public static final String DISPLAY_NAME_CATEGORYOPTIONCOMBO = "Category";
    public static final String DISPLAY_NAME_PERIOD = "Period";
    public static final String DISPLAY_NAME_ORGUNIT = "Organisation unit";    
    
    private static final String DIMENSION_NAME_SEP = ":";
    private static final String OPTION_SEP = ";";
    public static final String DIMENSION_SEP = "-";

    public static final List<String> DATA_DIMS = Arrays.asList( INDICATOR_DIM_ID, DATAELEMENT_DIM_ID, DATASET_DIM_ID );
    public static final List<String> FIXED_DIMS = Arrays.asList( DATA_X_DIM_ID, INDICATOR_DIM_ID, DATAELEMENT_DIM_ID, DATASET_DIM_ID, PERIOD_DIM_ID, ORGUNIT_DIM_ID );
    
    public static final int MAX_DIM_OPT_PERM = 10000;

    private static final List<DimensionType> COMPLETENESS_DIMENSION_TYPES = Arrays.asList( DATASET, ORGANISATIONUNIT, ORGANISATIONUNIT_GROUPSET );
    
    private static final DimensionItem[] DIM_OPT_ARR = new DimensionItem[0];
    private static final DimensionItem[][] DIM_OPT_2D_ARR = new DimensionItem[0][];
    
    private List<DimensionalObject> dimensions = new ArrayList<DimensionalObject>();
    
    private List<DimensionalObject> filters = new ArrayList<DimensionalObject>();

    private AggregationType aggregationType;
    
    private Map<MeasureFilter, Double> measureCriteria = new HashMap<MeasureFilter, Double>();
    
    // -------------------------------------------------------------------------
    // Transient properties
    // -------------------------------------------------------------------------
    
    private transient String tableName;

    private ListMap<String, IdentifiableObject> tableNamePeriodMap;
    
    private transient String periodType;
        
    private transient PeriodType dataPeriodType;
    
    private transient boolean skipPartitioning;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    
    public DataQueryParams()
    {
    }
    
    public DataQueryParams( DataQueryParams params )
    {
        this.dimensions = new ArrayList<DimensionalObject>( params.getDimensions() );
        this.filters = new ArrayList<DimensionalObject>( params.getFilters() );
        this.aggregationType = params.getAggregationType();
        this.measureCriteria = params.getMeasureCriteria();
        
        this.tableName = params.getTableName();
        this.periodType = params.getPeriodType();
        this.dataPeriodType = params.getDataPeriodType();
        this.skipPartitioning = params.isSkipPartitioning();
        this.tableNamePeriodMap = params.getTableNamePeriodMap();
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------
    
    /**
     * Ensures conformity for this query. The category option combo dimension
     * can only be present if the data element dimension exists and the indicator
     * and data set dimensions do not exist.
     */
    public DataQueryParams conform()
    {
        if ( !dimensions.contains( new BaseDimensionalObject( DATAELEMENT_DIM_ID ) ) ||
            dimensions.contains( new BaseDimensionalObject( INDICATOR_DIM_ID ) ) ||
            dimensions.contains( new BaseDimensionalObject( DATASET_DIM_ID ) ) )
        {
            removeDimension( CATEGORYOPTIONCOMBO_DIM_ID );
        }
        
        return this;
    }
    
    /**
     * Indicates whether the filters of this query spans more than one partition.
     * If true it means that a period filter exists and that the periods span
     * multiple years.
     */
    public boolean filterSpansMultiplePartitions()
    {
        return tableNamePeriodMap != null && tableNamePeriodMap.size() > 1;
    }
    
    /**
     * If the filters of this query spans more than partition, this method will
     * return a list of queries with a query for each partition, generated from 
     * this query, where the table name and filter period items are set according 
     * to the relevant partition.
     */
    public List<DataQueryParams> getPartitionFilterParams()
    {
        List<DataQueryParams> filters = new ArrayList<DataQueryParams>();
        
        if ( !filterSpansMultiplePartitions() )
        {
            return filters;
        }   
        
        for ( String tableName : tableNamePeriodMap.keySet() )
        {
            List<IdentifiableObject> periods = tableNamePeriodMap.get( tableName );
            
            DataQueryParams params = new DataQueryParams( this );
            params.setTableName( tableName );
            params.updateFilterOptions( PERIOD_DIM_ID, periods );
            filters.add( params );
        }
        
        return filters;
    }
    
    /**
     * Creates a mapping between dimension identifiers and filter dimensions. Filters 
     * are guaranteed not to be null.
     */
    public ListMap<String, DimensionalObject> getDimensionFilterMap()
    {
        ListMap<String, DimensionalObject> map = new ListMap<String, DimensionalObject>();
        
        for ( DimensionalObject filter : filters )
        {
            if ( filter != null )
            {
                map.putValue( filter.getDimension(), filter );
            }
        }
        
        return map;
    }
    
    /**
     * Creates a list of dimensions for use as headers. Will replace any of
     * the indicator, data element or data set dimensions with the common
     * data x dimension. If the category option combo dimension is given but
     * not the data element dimension, the former will be removed.
     */
    public List<DimensionalObject> getHeaderDimensions()
    {
        List<DimensionalObject> list = new ArrayList<DimensionalObject>( dimensions );
        
        ListIterator<DimensionalObject> iter = list.listIterator();
        
        dimensions : while ( iter.hasNext() )
        {
            if ( DATA_DIMS.contains( iter.next().getDimension() ) )
            {
                iter.set( new BaseDimensionalObject( DATA_X_DIM_ID, DimensionType.DATA_X, null, DISPLAY_NAME_DATA_X, new ArrayList<IdentifiableObject>() ) );
                break dimensions;
            }
        }
        
        list.remove( new BaseDimensionalObject( INDICATOR_DIM_ID ) );
        list.remove( new BaseDimensionalObject( DATAELEMENT_DIM_ID ) );
        list.remove( new BaseDimensionalObject( DATASET_DIM_ID ) );
        
        return list;
    }
    
    /**
     * Creates a list of dimensions used to query. 
     */
    public List<DimensionalObject> getQueryDimensions()
    {
        List<DimensionalObject> list = new ArrayList<DimensionalObject>( dimensions );
        
        list.remove( new BaseDimensionalObject( INDICATOR_DIM_ID ) );
        
        return list;
    }
    
    /**
     * Creates a list of dimension indexes which are relevant to completeness queries.
     */
    public List<Integer> getCompletenessDimensionIndexes()
    {
        List<Integer> indexes = new ArrayList<Integer>();
        
        for ( int i = 0; i < dimensions.size(); i++ )
        {
            if ( COMPLETENESS_DIMENSION_TYPES.contains( dimensions.get( i ).getType() ) )
            {
                indexes.add( i );
            }
        }
        
        return indexes;
    }

    /**
     * Creates a list of filter indexes which are relevant to completeness queries.
     */
    public List<Integer> getCompletenessFilterIndexes()
    {
        List<Integer> indexes = new ArrayList<Integer>();
        
        for ( int i = 0; i < filters.size(); i++ )
        {
            if ( COMPLETENESS_DIMENSION_TYPES.contains( filters.get( i ).getType() ) )
            {
                indexes.add( i );
            }
        }
        
        return indexes;
    }

    /**
     * Removes the dimension with the given identifier.
     */
    public DataQueryParams removeDimension( String dimension )
    {
        this.dimensions.remove( new BaseDimensionalObject( dimension ) );
        
        return this;
    }

    /**
     * Removes the filter with the given identifier.
     */
    public DataQueryParams removeFilter( String filter )
    {
        this.filters.remove( new BaseDimensionalObject( filter ) );
        
        return this;
    }
    
    /**
     * Returns the index of the indicator dimension in the dimension map.
     */
    public int getIndicatorDimensionIndex()
    {
        return getInputDimensionNamesAsList().indexOf( INDICATOR_DIM_ID );
    }
    
    /**
     * Returns the index of the data element dimension in the dimension map.
     */
    public int getDataElementDimensionIndex()
    {
        return getInputDimensionNamesAsList().indexOf( DATAELEMENT_DIM_ID );
    }
    
    /**
     * Returns the index of the data set dimension in the dimension map.
     */
    public int getDataSetDimensionIndex()
    {
        return getInputDimensionNamesAsList().indexOf( DATASET_DIM_ID );
    }

    /**
     * Returns the index of the category option combo dimension in the dimension map.
     */
    public int getCategoryOptionComboDimensionIndex()
    {
        return getInputDimensionNamesAsList().indexOf( CATEGORYOPTIONCOMBO_DIM_ID );
    }
    
    /**
     * Returns the index of the period dimension in the dimension map.
     */
    public int getPeriodDimensionIndex()
    {
        return getInputDimensionNamesAsList().indexOf( PERIOD_DIM_ID );
    }
    
    /**
     * Returns the dimensions which are part of dimensions and filters. If any
     * such dimensions exist this object is in an illegal state.
     */
    public Collection<DimensionalObject> getDimensionsAsFilters()
    {
        return CollectionUtils.intersection( dimensions, filters );
    }
        
    /**
     * Indicates whether periods are present as a dimension or as a filter. If
     * not this object is in an illegal state.
     */
    public boolean hasPeriods()
    {
        List<IdentifiableObject> dimOpts = getDimensionOptions( PERIOD_DIM_ID );
        List<IdentifiableObject> filterOpts = getFilterOptions( PERIOD_DIM_ID );
        
        return ( dimOpts != null && !dimOpts.isEmpty() ) || ( filterOpts != null && !filterOpts.isEmpty() );
    }
    
    /**
     * Returns the number of dimension option permutations. Merges the three data
     * dimensions into one prior to the calculation.
     */
    public int getNumberOfDimensionOptionPermutations()
    {
        int total = 1;
        
        DataQueryParams query = new DataQueryParams( this );
        
        query.getDimensions().add( new BaseDimensionalObject( DATA_X_DIM_ID ) );
        
        query.getDimension( DATA_X_DIM_ID ).getItems().addAll( emptyIfNull( query.getDimensionOptions( INDICATOR_DIM_ID ) ) );
        query.getDimension( DATA_X_DIM_ID ).getItems().addAll( emptyIfNull( query.getDimensionOptions( DATAELEMENT_DIM_ID ) ) );
        query.getDimension( DATA_X_DIM_ID ).getItems().addAll( emptyIfNull( query.getDimensionOptions( DATASET_DIM_ID ) ) );
        
        query.removeDimension( INDICATOR_DIM_ID );
        query.removeDimension( DATAELEMENT_DIM_ID );
        query.removeDimension( DATASET_DIM_ID );
        
        for ( DimensionalObject dim : query.getDimensions() )
        {
            total *= Math.max( dim.getItems().size(), 1 );
        }
        
        return total;
    }
    
    /**
     * Returns a list of dimensions which occur more than once.
     */
    public List<DimensionalObject> getDuplicateDimensions()
    {
        Set<DimensionalObject> dims = new HashSet<DimensionalObject>();
        List<DimensionalObject> duplicates = new ArrayList<DimensionalObject>();
        
        for ( DimensionalObject dim : dimensions )
        {
            if ( !dims.add( dim ) )
            {
                duplicates.add( dim );
            }
        }
        
        return duplicates;
    }
    
    /**
     * Returns a mapping between identifier and period type for all data sets
     * in this query.
     */
    public Map<String, PeriodType> getDataSetPeriodTypeMap()
    {
        Map<String, PeriodType> map = new HashMap<String, PeriodType>();
        
        for ( IdentifiableObject dataSet : getDataSets() )
        {
            DataSet ds = (DataSet) dataSet;
            
            map.put( ds.getUid(), ds.getPeriodType() );
        }
        
        return map;
    }
    
    /**
     * Returns the index of the category option combo dimension. Returns null
     * if this dimension is not present.
     */
    public Integer getCocIndex()
    {
        int index = dimensions.indexOf( new BaseDimensionalObject( CATEGORYOPTIONCOMBO_DIM_ID ) );
        
        return index == -1 ? null : index;
    }
    
    /**
     * Indicates whether this object is of the given aggregation type.
     */
    public boolean isAggregationType( AggregationType aggregationType )
    {
        return this.aggregationType != null && this.aggregationType.equals( aggregationType );
    }

    /**
     * Creates a mapping between the data periods, based on the data period type
     * for this query, and the aggregation periods for this query.
     */
    public ListMap<IdentifiableObject, IdentifiableObject> getDataPeriodAggregationPeriodMap()
    {
        ListMap<IdentifiableObject, IdentifiableObject> map = new ListMap<IdentifiableObject, IdentifiableObject>();

        if ( dataPeriodType != null )
        {
            for ( IdentifiableObject aggregatePeriod : getDimensionOrFilter( PERIOD_DIM_ID ) )
            {
                Period dataPeriod = dataPeriodType.createPeriod( ((Period) aggregatePeriod).getStartDate() );
                
                map.putValue( dataPeriod, aggregatePeriod );
            }
        }
        
        return map;
    }
    
    /**
     * Replaces the periods of this query with the corresponding data periods.
     * Sets the period type to the data period type. This method is relevant only 
     * when then the data period type has lower frequency than the aggregation 
     * period type.
     */
    public void replaceAggregationPeriodsWithDataPeriods( ListMap<IdentifiableObject, IdentifiableObject> dataPeriodAggregationPeriodMap )
    {
        if ( isAggregationType( AVERAGE_INT_DISAGGREGATION ) &&  dataPeriodType != null )
        {
            this.periodType = this.dataPeriodType.getName();
            
            if ( getPeriods() != null ) // Period is dimension
            {
                setDimensionOptions( PERIOD_DIM_ID, DimensionType.PERIOD, dataPeriodType.getName(), new ArrayList<IdentifiableObject>( dataPeriodAggregationPeriodMap.keySet() ) );
            }
            else // Period is filter
            {
                setFilterOptions( PERIOD_DIM_ID, DimensionType.PERIOD, dataPeriodType.getName(), new ArrayList<IdentifiableObject>( dataPeriodAggregationPeriodMap.keySet() ) );
            }
        }
    }
    
    /**
     * Generates all permutations of the dimension and filter options for this query.
     * Ignores the data element, category option combo and indicator dimensions.
     */
    public List<List<DimensionItem>> getDimensionItemPermutations()
    {
        List<DimensionItem[]> dimensionOptions = new ArrayList<DimensionItem[]>();
        
        List<String> ignoreDims = Arrays.asList( DATAELEMENT_DIM_ID, CATEGORYOPTIONCOMBO_DIM_ID, INDICATOR_DIM_ID );
        
        for ( DimensionalObject dimension : dimensions )
        {
            if ( !ignoreDims.contains( dimension.getDimension() ) )
            {
                List<DimensionItem> options = new ArrayList<DimensionItem>();
                
                for ( IdentifiableObject option : dimension.getItems() )
                {
                    options.add( new DimensionItem( dimension.getDimension(), option ) );
                }
                
                dimensionOptions.add( options.toArray( DIM_OPT_ARR ) );
            }
        }
                
        CombinationGenerator<DimensionItem> generator = new CombinationGenerator<DimensionItem>( dimensionOptions.toArray( DIM_OPT_2D_ARR ) );
        
        List<List<DimensionItem>> permutations = generator.getCombinations();
        
        return permutations;
    }

    /**
     * Returns a mapping of permutation keys and mappings of data element operands
     * and values, based on the given mapping of dimension option keys and 
     * aggregated values.
     */
    public Map<String, Map<DataElementOperand, Double>> getPermutationOperandValueMap( Map<String, Double> aggregatedDataMap )
    {
        MapMap<String, DataElementOperand, Double> valueMap = new MapMap<String, DataElementOperand, Double>();
        
        for ( String key : aggregatedDataMap.keySet() )
        {
            List<String> keys = new ArrayList<String>( Arrays.asList( key.split( DIMENSION_SEP ) ) );
            
            int deInx = getDataElementDimensionIndex();
            int cocInx = getCategoryOptionComboDimensionIndex();
            
            String de = keys.get( deInx );
            String coc = keys.get( cocInx );
            
            ListUtils.removeAll( keys, deInx, cocInx );
            
            String permKey = StringUtils.join( keys, DIMENSION_SEP );
            
            DataElementOperand operand = new DataElementOperand( de, coc );
            
            Double value = aggregatedDataMap.get( key );
            
            valueMap.putEntry( permKey, operand, value );            
        }
        
        return valueMap;
    }

    /**
     * Retrieves the options for the given dimension identifier.
     */
    public List<IdentifiableObject> getDimensionOptions( String dimension )
    {
        int index = dimensions.indexOf( new BaseDimensionalObject( dimension ) );
        
        return index != -1 ? dimensions.get( index ).getItems() : null;
    }
    
    /**
     * Retrieves the dimension with the given dimension identifier.
     */
    public DimensionalObject getDimension( String dimension )
    {
        int index = dimensions.indexOf( new BaseDimensionalObject( dimension ) );
        
        return index != -1 ? dimensions.get( index ) : null;
    }
    
    /**
     * Sets the options for the given dimension.
     */
    public DataQueryParams setDimensionOptions( String dimension, DimensionType type, String dimensionName, List<IdentifiableObject> options )
    {
        int index = dimensions.indexOf( new BaseDimensionalObject( dimension ) );
        
        if ( index != -1 )
        {
            dimensions.set( index, new BaseDimensionalObject( dimension, type, dimensionName, options ) );
        }
        else
        {
            dimensions.add( new BaseDimensionalObject( dimension, type, dimensionName, options ) );
        }
        
        return this;
    }
    
    /**
     * Retrieves the options for the given filter.
     */
    public List<IdentifiableObject> getFilterOptions( String filter )
    {
        int index = filters.indexOf( new BaseDimensionalObject( filter ) );
        
        return index != -1 ? filters.get( index ).getItems() : null;
    }

    /**
     * Retrieves the filter with the given filter identifier.
     */
    public DimensionalObject getFilter( String filter )
    {
        int index = filters.indexOf( new BaseDimensionalObject( filter ) );
        
        return index != -1 ? filters.get( index ) : null;
    }
    
    /**
     * Sets the options for the given filter.
     */
    public DataQueryParams setFilterOptions( String filter, DimensionType type, String dimensionName, List<IdentifiableObject> options )
    {
        int index = filters.indexOf( new BaseDimensionalObject( filter ) );
        
        if ( index != -1 )
        {
            filters.set( index, new BaseDimensionalObject( filter, type, dimensionName, options ) );
        }
        else
        {
            filters.add( new BaseDimensionalObject( filter, type, dimensionName, options ) );
        }
        
        return this;
    }
    
    /**
     * Updates the options for the given filter.
     */
    public DataQueryParams updateFilterOptions( String filter, List<IdentifiableObject> options )
    {
        int index = filters.indexOf( new BaseDimensionalObject( filter ) );
        
        if ( index != -1 )
        {
            DimensionalObject existing = filters.get( index );
            
            filters.set( index, new BaseDimensionalObject( existing.getDimension(), existing.getType(), existing.getDimensionName(), options ) );
        }
        
        return this;
    }
    
    // -------------------------------------------------------------------------
    // Static methods
    // -------------------------------------------------------------------------

    /**
     * Retrieves the dimension name from the given string. Returns the part of
     * the string preceding the dimension name separator, or the whole string if
     * the separator is not present.
     */
    public static String getDimensionFromParam( String param )
    {
        if ( param == null )
        {
            return null;
        }
        
        return param.split( DIMENSION_NAME_SEP ).length > 0 ? param.split( DIMENSION_NAME_SEP )[0] : param;
    }
    
    /**
     * Retrieves the dimension options from the given string. Looks for the part
     * succeeding the dimension name separator, if exists, splits the string part
     * on the option separator and returns the resulting values. If the dimension
     * name separator does not exist an empty list is returned, indicating that
     * all dimension options should be used.
     */
    public static List<String> getDimensionItemsFromParam( String param )
    {
        if ( param == null )
        {
            return null;
        }
        
        if ( param.split( DIMENSION_NAME_SEP ).length > 1 )
        {
            return Arrays.asList( param.split( DIMENSION_NAME_SEP )[1].split( OPTION_SEP ) );
        }
        
        return new ArrayList<String>();
    }
    
    /**
     * Retrieves the measure criteria from the given string. Criteria are separated
     * by the option separator, while the criterion filter and value are separated
     * with the dimension name separator.
     */
    public static Map<MeasureFilter, Double> getMeasureCriteriaFromParam( String param )
    {
        if ( param == null )
        {
            return null;
        }
        
        Map<MeasureFilter, Double> map = new HashMap<MeasureFilter, Double>();
        
        String[] criteria = param.split( OPTION_SEP );
        
        for ( String c : criteria )
        {
            String[] criterion = c.split( DIMENSION_NAME_SEP );
            
            if ( criterion != null && criterion.length == 2 && MathUtils.isNumeric( criterion[1] ) )
            {
                MeasureFilter filter = MeasureFilter.valueOf( criterion[0] );
                Double value = Double.valueOf( criterion[1] );
                map.put( filter, value );
            }
        }
        
        return map;
    }
    
    /**
     * Indicates whether at least one of the given dimenions has at least one
     * item.
     */
    public static boolean anyDimensionHasItems( Collection<DimensionalObject> dimensions )
    {
        if ( dimensions == null || dimensions.isEmpty() )
        {
            return false;
        }
        
        for ( DimensionalObject dim : dimensions )
        {
            if ( dim.hasItems() )
            {
                return true;
            }
        }
        
        return false;
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private List<String> getInputDimensionNamesAsList()
    {
        List<String> list = new ArrayList<String>();
        
        for ( DimensionalObject dimension : dimensions )
        {
            list.add( dimension.getDimension() );
        }
        
        return list;
    }
        
    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( dimensions == null ) ? 0 : dimensions.hashCode() );
        result = prime * result + ( ( filters == null ) ? 0 : filters.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( getClass() != object.getClass() )
        {
            return false;
        }
        
        DataQueryParams other = (DataQueryParams) object;
        
        if ( dimensions == null )
        {
            if ( other.dimensions != null )
            {
                return false;
            }
        }
        else if ( !dimensions.equals( other.dimensions ) )
        {
            return false;
        }
        
        if ( filters == null )
        {
            if ( other.filters != null )
            {
                return false;
            }
        }
        else if ( !filters.equals( other.filters ) )
        {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString()
    {
        return "[Dimensions: " + dimensions + ", Filters: " + filters + "]";
    }
    
    // -------------------------------------------------------------------------
    // Get and set methods for serialize properties
    // -------------------------------------------------------------------------

    public List<DimensionalObject> getDimensions()
    {
        return dimensions;
    }

    public void setDimensions( List<DimensionalObject> dimensions )
    {
        this.dimensions = dimensions;
    }

    public List<DimensionalObject> getFilters()
    {
        return filters;
    }

    public void setFilters( List<DimensionalObject> filters )
    {
        this.filters = filters;
    }

    public AggregationType getAggregationType()
    {
        return aggregationType;
    }

    public void setAggregationType( AggregationType aggregationType )
    {
        this.aggregationType = aggregationType;
    }

    public Map<MeasureFilter, Double> getMeasureCriteria()
    {
        return measureCriteria;
    }

    public void setMeasureCriteria( Map<MeasureFilter, Double> measureCriteria )
    {
        this.measureCriteria = measureCriteria;
    }

    // -------------------------------------------------------------------------
    // Get and set methods for transient properties
    // -------------------------------------------------------------------------

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName( String tableName )
    {
        this.tableName = tableName;
    }

    public ListMap<String, IdentifiableObject> getTableNamePeriodMap()
    {
        return tableNamePeriodMap;
    }

    public void setTableNamePeriodMap( ListMap<String, IdentifiableObject> tableNamePeriodMap )
    {
        this.tableNamePeriodMap = tableNamePeriodMap;
    }

    public String getPeriodType()
    {
        return periodType;
    }

    public void setPeriodType( String periodType )
    {
        this.periodType = periodType;
    }

    public PeriodType getDataPeriodType()
    {
        return dataPeriodType;
    }

    public void setDataPeriodType( PeriodType dataPeriodType )
    {
        this.dataPeriodType = dataPeriodType;
    }

    public boolean isSkipPartitioning()
    {
        return skipPartitioning;
    }

    public void setSkipPartitioning( boolean skipPartitioning )
    {
        this.skipPartitioning = skipPartitioning;
    }

    // -------------------------------------------------------------------------
    // Get and set helpers for dimensions or filter
    // -------------------------------------------------------------------------
  
    public List<IdentifiableObject> getDimensionOrFilter( String key )
    {
        return getDimensionOptions( key ) != null ? getDimensionOptions( key ) : getFilterOptions( key );
    }
    
    public boolean hasDimensionOrFilter( String key )
    {
        return dimensions.indexOf( new BaseDimensionalObject( key ) ) != -1 || filters.indexOf( new BaseDimensionalObject( key ) ) != -1;
    }
    
    // -------------------------------------------------------------------------
    // Get and set helpers for dimensions
    // -------------------------------------------------------------------------
  
    public List<IdentifiableObject> getIndicators()
    {
        return getDimensionOptions( INDICATOR_DIM_ID );
    }
    
    public void setIndicators( List<? extends IdentifiableObject> indicators )
    {
        setDimensionOptions( INDICATOR_DIM_ID, DimensionType.INDICATOR, null, asList( indicators ) );
    }
    
    public List<IdentifiableObject> getDataElements()
    {
        return getDimensionOptions( DATAELEMENT_DIM_ID );
    }
    
    public void setDataElements( List<? extends IdentifiableObject> dataElements )
    {
        setDimensionOptions( DATAELEMENT_DIM_ID, DimensionType.DATAELEMENT, null, asList( dataElements ) );
    }
    
    public List<IdentifiableObject> getDataSets()
    {
        return getDimensionOptions( DATASET_DIM_ID );
    }
    
    public void setDataSets( List<? extends IdentifiableObject> dataSets )
    {
        setDimensionOptions( DATASET_DIM_ID, DimensionType.DATASET, null, asList( dataSets ) );
    }
    
    public List<IdentifiableObject> getPeriods()
    {
        return getDimensionOptions( PERIOD_DIM_ID );
    }
    
    public void setPeriods( List<? extends IdentifiableObject> periods )
    {
        setDimensionOptions( PERIOD_DIM_ID, DimensionType.PERIOD, null, asList( periods ) );
    }

    public List<IdentifiableObject> getOrganisationUnits()
    {
        return getDimensionOptions( ORGUNIT_DIM_ID );
    }
    
    public void setOrganisationUnits( List<? extends IdentifiableObject> organisationUnits )
    {
        setDimensionOptions( ORGUNIT_DIM_ID, DimensionType.ORGANISATIONUNIT, null, asList( organisationUnits ) );
    }
    
    public List<DimensionalObject> getDataElementGroupSets()
    {
        List<DimensionalObject> list = new ArrayList<DimensionalObject>();
        
        for ( DimensionalObject dimension : dimensions )
        {
            if ( DimensionType.DATAELEMENT_GROUPSET.equals( dimension.getType() ) )
            {
                list.add( dimension );
            }
        }
        
        for ( DimensionalObject filter : filters )
        {
            if ( DimensionType.DATAELEMENT_GROUPSET.equals( filter.getType() ) )
            {
                list.add( filter );
            }
        }
        
        return list;
    }
    
    public void setDataElementGroupSet( DataElementGroupSet groupSet )
    {
        setDimensionOptions( groupSet.getUid(), DimensionType.DATAELEMENT_GROUPSET, null, new ArrayList<IdentifiableObject>( groupSet.getDimensionItems() ) );
    }
    
    public void setOrganisationUnitGroupSet( OrganisationUnitGroupSet groupSet )
    {
        setDimensionOptions( groupSet.getUid(), DimensionType.ORGANISATIONUNIT_GROUPSET, null, new ArrayList<IdentifiableObject>( groupSet.getDimensionItems() ) );
    }

    public void setCategory( DataElementCategory category )
    {
        setDimensionOptions( category.getUid(), DimensionType.CATEGORY, null, new ArrayList<IdentifiableObject>( category.getDimensionItems() ) );
    }
    
    public void enableCategoryOptionCombos()
    {
        setDimensionOptions( CATEGORYOPTIONCOMBO_DIM_ID, DimensionType.CATEGORY_OPTION_COMBO, null, new ArrayList<IdentifiableObject>() );
    }
    
    // -------------------------------------------------------------------------
    // Get and set helpers for filters
    // -------------------------------------------------------------------------
    
    public List<IdentifiableObject> getFilterPeriods()
    {
        return getFilterOptions( PERIOD_DIM_ID );
    }
    
    public void setFilterPeriods( List<IdentifiableObject> periods )
    {
        setFilterOptions( PERIOD_DIM_ID, DimensionType.PERIOD, null, periods );
    }
    
    public List<IdentifiableObject> getFilterOrganisationUnits()
    {
        return getFilterOptions( ORGUNIT_DIM_ID );
    }
    
    public void setFilterOrganisationUnits( List<IdentifiableObject> organisationUnits )
    {
        setFilterOptions( ORGUNIT_DIM_ID, DimensionType.ORGANISATIONUNIT, null, organisationUnits );
    }
    
    public void setFilter( String filter, DimensionType type, IdentifiableObject item )
    {
        setFilterOptions( filter, type, null, getList( item ) );
    }
}
