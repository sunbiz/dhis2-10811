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
import static org.hisp.dhis.analytics.DimensionType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hisp.dhis.common.CombinationGenerator;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.system.util.CollectionUtils;
import org.hisp.dhis.system.util.ListMap;
import org.hisp.dhis.system.util.ListUtils;
import org.hisp.dhis.system.util.MapMap;
import org.hisp.dhis.system.util.MathUtils;

public class DataQueryParams
{
    public static final String DATA_X_DIM_ID = "dx"; // IN, DE, DS
    public static final String INDICATOR_DIM_ID = "in";
    public static final String DATAELEMENT_DIM_ID = "de";
    public static final String DATASET_DIM_ID = "ds";
    public static final String CATEGORYOPTIONCOMBO_DIM_ID = "coc";
    public static final String PERIOD_DIM_ID = "pe";
    public static final String ORGUNIT_DIM_ID = "ou";
    public static final String VALUE_ID = "value";    
    public static final String LEVEL_PREFIX = "uidlevel";
    
    private static final String DIMENSION_NAME_SEP = ":";
    private static final String OPTION_SEP = ";";
    public static final String DIMENSION_SEP = "-";

    public static final List<String> DATA_DIMS = Arrays.asList( INDICATOR_DIM_ID, DATAELEMENT_DIM_ID, DATASET_DIM_ID );
    public static final List<String> FIXED_DIMS = Arrays.asList( DATA_X_DIM_ID, INDICATOR_DIM_ID, DATAELEMENT_DIM_ID, DATASET_DIM_ID, PERIOD_DIM_ID, ORGUNIT_DIM_ID );
    
    private static final List<DimensionType> COMPLETENESS_DIMENSION_TYTPES = Arrays.asList( DATASET, ORGANISATIONUNIT, ORGANISATIONUNIT_GROUPSET );
    
    private static final DimensionOption[] DIM_OPT_ARR = new DimensionOption[0];
    private static final DimensionOption[][] DIM_OPT_2D_ARR = new DimensionOption[0][];
    
    private List<Dimension> dimensions = new ArrayList<Dimension>();
    
    private List<Dimension> filters = new ArrayList<Dimension>();

    private AggregationType aggregationType;
    
    private Map<MeasureFilter, Double> measureCriteria = new HashMap<MeasureFilter, Double>();
    
    // -------------------------------------------------------------------------
    // Transient properties
    // -------------------------------------------------------------------------
    
    private transient String tableName;

    private transient String periodType;
    
    private transient int organisationUnitLevel;
    
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
        this.dimensions = new ArrayList<Dimension>( params.getDimensions() );
        this.filters = new ArrayList<Dimension>( params.getFilters() );
        this.aggregationType = params.getAggregationType();
        this.measureCriteria = params.getMeasureCriteria();
        
        this.tableName = params.getTableName();
        this.periodType = params.getPeriodType();
        this.organisationUnitLevel = params.getOrganisationUnitLevel();
        this.dataPeriodType = params.getDataPeriodType();
        this.skipPartitioning = params.isSkipPartitioning();
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    /**
     * Ensures conformity for this query. The category option combo dimension
     * can only be present if the data element dimension exists and the indicator
     * and data set dimensions do not exist.
     */
    public void conform()
    {
        if ( !dimensions.contains( new Dimension( DATAELEMENT_DIM_ID ) ) ||
            dimensions.contains( new Dimension( INDICATOR_DIM_ID ) ) ||
            dimensions.contains( new Dimension( DATASET_DIM_ID ) ) )
        {
            removeDimension( CATEGORYOPTIONCOMBO_DIM_ID );
        }
    }
    
    /**
     * Creates a list of dimensions for use as headers. Will replace any of
     * the indicator, data element or data set dimensions with the common
     * data x dimension. If the category option combo dimension is given but
     * not the data element dimension, the former will be removed.
     */
    public List<Dimension> getHeaderDimensions()
    {
        List<Dimension> list = new ArrayList<Dimension>( dimensions );
        
        ListIterator<Dimension> iter = list.listIterator();
        
        dimensions : while ( iter.hasNext() )
        {
            if ( DATA_DIMS.contains( iter.next().getDimension() ) )
            {
                iter.set( new Dimension( DATA_X_DIM_ID, DimensionType.DATA_X ) );
                break dimensions;
            }
        }
        
        list.remove( new Dimension( INDICATOR_DIM_ID ) );
        list.remove( new Dimension( DATAELEMENT_DIM_ID ) );
        list.remove( new Dimension( DATASET_DIM_ID ) );
        
        return list;
    }
    
    /**
     * Creates a list of dimensions used to query. 
     */
    public List<Dimension> getQueryDimensions()
    {
        List<Dimension> list = new ArrayList<Dimension>( dimensions );
        
        list.remove( new Dimension( INDICATOR_DIM_ID ) );
        
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
            if ( COMPLETENESS_DIMENSION_TYTPES.contains( dimensions.get( i ).getType() ) )
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
            if ( COMPLETENESS_DIMENSION_TYTPES.contains( filters.get( i ).getType() ) )
            {
                indexes.add( i );
            }
        }
        
        return indexes;
    }

    /**
     * Removes the dimension with the given identifier.
     */
    public void removeDimension( String dimension )
    {
        this.dimensions.remove( new Dimension( dimension ) );
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
     * Populates the dimension name property on all dimensions. Will set the 
     * name of the current period type for this query on the period dimension
     * and the a prefixed organisation unit level on the organisation unit
     * dimension.
     */
    public void populateDimensionNames()
    {
        for ( Dimension dimension : dimensions )
        {
            if ( periodType != null && PERIOD_DIM_ID.equals( dimension.getDimension() ) )
            {
                dimension.setDimensionName( periodType );
            }
            else if ( organisationUnitLevel != 0 && ORGUNIT_DIM_ID.equals( dimension.getDimension() ) )
            {
                dimension.setDimensionName( LEVEL_PREFIX + organisationUnitLevel );
            }
        }

        for ( Dimension filter : filters )
        {
            if ( periodType != null && PERIOD_DIM_ID.equals( filter.getDimension() ) )
            {
                filter.setDimensionName( periodType );
            }
            else if ( organisationUnitLevel != 0 && ORGUNIT_DIM_ID.equals( filter.getDimension() ) )
            {
                filter.setDimensionName( LEVEL_PREFIX + organisationUnitLevel );
            }
        }
    }
    
    /**
     * Returns the dimensions which are part of dimensions and filters. If any
     * such dimensions exist this object is in an illegal state.
     */
    public Collection<Dimension> dimensionsAsFilters()
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
     * Returns the index of the category option combo dimension. Returns null
     * if this dimension is not present.
     */
    public Integer getCocIndex()
    {
        int index = dimensions.indexOf( new Dimension( CATEGORYOPTIONCOMBO_DIM_ID ) );
        
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
                setPeriods( new ArrayList<IdentifiableObject>( dataPeriodAggregationPeriodMap.keySet() ) );
            }
            else // Period is filter
            {
                setFilterPeriods( new ArrayList<IdentifiableObject>( dataPeriodAggregationPeriodMap.keySet() ) );
            }
        }
    }
    
    /**
     * Generates all permutations of the dimension and filter options for this query.
     * Ignores the data element, category option combo and indicator dimensions.
     */
    public List<List<DimensionOption>> getDimensionOptionPermutations()
    {
        List<DimensionOption[]> dimensionOptions = new ArrayList<DimensionOption[]>();
        
        List<String> ignoreDims = Arrays.asList( DATAELEMENT_DIM_ID, CATEGORYOPTIONCOMBO_DIM_ID, INDICATOR_DIM_ID );
        
        for ( Dimension dimension : dimensions )
        {
            if ( !ignoreDims.contains( dimension.getDimension() ) )
            {
                List<DimensionOption> options = new ArrayList<DimensionOption>();
                
                for ( IdentifiableObject option : dimension.getOptions() )
                {
                    options.add( new DimensionOption( dimension.getDimension(), option ) );
                }
                
                dimensionOptions.add( options.toArray( DIM_OPT_ARR ) );
            }
        }
                
        CombinationGenerator<DimensionOption> generator = new CombinationGenerator<DimensionOption>( dimensionOptions.toArray( DIM_OPT_2D_ARR ) );
        
        List<List<DimensionOption>> permutations = generator.getCombinations();
        
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
     * Retrieves the options for the given dimension.
     */
    public List<IdentifiableObject> getDimensionOptions( String dimension )
    {
        int index = dimensions.indexOf( new Dimension( dimension ) );
        
        return index != -1 ? dimensions.get( index ).getOptions() : null;
    }

    /**
     * Sets the options for the given dimension.
     */
    public void setDimensionOptions( String dimension, DimensionType type, List<IdentifiableObject> options )
    {
        int index = dimensions.indexOf( new Dimension( dimension ) );
        
        if ( index != -1 )
        {
            dimensions.set( index, new Dimension( dimension, type, options ) );
        }
        else
        {
            dimensions.add( new Dimension( dimension, type, options ) );
        }
    }
    
    /**
     * Retrieves the options for the given filter.
     */
    public List<IdentifiableObject> getFilterOptions( String filter )
    {
        int index = filters.indexOf( new Dimension( filter ) );
        
        return index != -1 ? filters.get( index ).getOptions() : null;
    }
    
    /**
     * Sets the options for the given filter.
     */
    public void setFilterOptions( String filter, DimensionType type, List<IdentifiableObject> options )
    {
        int index = filters.indexOf( new Dimension( filter ) );
        
        if ( index != -1 )
        {
            filters.set( index, new Dimension( filter, type, options ) );
        }
        else
        {
            filters.add( new Dimension( filter, type, options ) );
        }
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
    public static List<String> getDimensionOptionsFromParam( String param )
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
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private List<String> getInputDimensionNamesAsList()
    {
        List<String> list = new ArrayList<String>();
        
        for ( Dimension dimension : dimensions )
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

    public List<Dimension> getDimensions()
    {
        return dimensions;
    }

    public void setDimensions( List<Dimension> dimensions )
    {
        this.dimensions = dimensions;
    }

    public List<Dimension> getFilters()
    {
        return filters;
    }

    public void setFilters( List<Dimension> filters )
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

    public String getPeriodType()
    {
        return periodType;
    }

    public void setPeriodType( String periodType )
    {
        this.periodType = periodType;
    }

    public int getOrganisationUnitLevel()
    {
        return organisationUnitLevel;
    }

    public void setOrganisationUnitLevel( int organisationUnitLevel )
    {
        this.organisationUnitLevel = organisationUnitLevel;
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
    
    public void resetDimensionOrFilter( String key, List<IdentifiableObject> options )
    {
        if ( getDimensionOptions( key ) != null )
        {
            setDimensionOptions( key, null, options );
        }
        else if ( getFilterOptions( key ) != null )
        {
            setFilterOptions( key, null, options );
        }
    }
    
    // -------------------------------------------------------------------------
    // Get and set helpers for dimensions
    // -------------------------------------------------------------------------
  
    public List<IdentifiableObject> getIndicators()
    {
        return getDimensionOptions( INDICATOR_DIM_ID );
    }
    
    public void setIndicators( List<IdentifiableObject> indicators )
    {
        setDimensionOptions( INDICATOR_DIM_ID, DimensionType.INDICATOR, indicators );
    }
    
    public List<IdentifiableObject> getDataElements()
    {
        return getDimensionOptions( DATAELEMENT_DIM_ID );
    }
    
    public void setDataElements( List<IdentifiableObject> dataElements )
    {
        setDimensionOptions( DATAELEMENT_DIM_ID, DimensionType.DATAELEMENT, dataElements );
    }
    
    public List<IdentifiableObject> getDataSets()
    {
        return getDimensionOptions( DATASET_DIM_ID );
    }
    
    public void setDataSets( List<IdentifiableObject> dataSets )
    {
        setDimensionOptions( DATASET_DIM_ID, DimensionType.DATASET, dataSets );
    }
    
    public List<IdentifiableObject> getPeriods()
    {
        return getDimensionOptions( PERIOD_DIM_ID );
    }
    
    public void setPeriods( List<IdentifiableObject> periods )
    {
        setDimensionOptions( PERIOD_DIM_ID, DimensionType.PERIOD, periods );
    }

    public List<IdentifiableObject> getOrganisationUnits()
    {
        return getDimensionOptions( ORGUNIT_DIM_ID );
    }
    
    public void setOrganisationUnits( List<IdentifiableObject> organisationUnits )
    {
        setDimensionOptions( ORGUNIT_DIM_ID, DimensionType.ORGANISATIONUNIT, organisationUnits );
    }
    
    public List<Dimension> getDataElementGroupSets()
    {
        List<Dimension> list = new ArrayList<Dimension>();
        
        for ( Dimension dimension : dimensions )
        {
            if ( DimensionType.DATAELEMENT_GROUPSET.equals( dimension.getType() ) )
            {
                list.add( dimension );
            }
        }
        
        return list;
    }
    
    public void setDataElementGroupSet( Dimension dimension, List<IdentifiableObject> dataElementGroups )
    {
        setDimensionOptions( dimension.getDimension(), DimensionType.DATAELEMENT_GROUPSET, dataElementGroups );
    }
    
    public void enableCategoryOptionCombos()
    {
        setDimensionOptions( CATEGORYOPTIONCOMBO_DIM_ID, DimensionType.CATEGORY_OPTION_COMBO, new ArrayList<IdentifiableObject>() );
    }
    
    // -------------------------------------------------------------------------
    // Get and set helpers for filters
    // -------------------------------------------------------------------------

    public List<IdentifiableObject> getFilterDatElements()
    {
        return getFilterOptions( DATAELEMENT_DIM_ID );
    }
    
    public void setFilterDataElements( List<IdentifiableObject> dataElements )
    {
        setFilterOptions( DATAELEMENT_DIM_ID, DimensionType.DATAELEMENT, dataElements );
    }
    
    public List<IdentifiableObject> getFilterPeriods()
    {
        return getFilterOptions( PERIOD_DIM_ID );
    }
    
    public void setFilterPeriods( List<IdentifiableObject> periods )
    {
        setFilterOptions( PERIOD_DIM_ID, DimensionType.PERIOD, periods );
    }
    
    public List<IdentifiableObject> getFilterOrganisationUnits()
    {
        return getFilterOptions( ORGUNIT_DIM_ID );
    }
    
    public void setFilterOrganisationUnits( List<IdentifiableObject> organisationUnits )
    {
        setFilterOptions( ORGUNIT_DIM_ID, DimensionType.ORGANISATIONUNIT, organisationUnits );
    }
}
