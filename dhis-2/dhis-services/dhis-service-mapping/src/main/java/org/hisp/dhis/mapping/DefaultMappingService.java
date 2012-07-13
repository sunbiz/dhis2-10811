package org.hisp.dhis.mapping;

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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.aggregation.AggregatedDataValueService;
import org.hisp.dhis.aggregation.AggregatedMapValue;
import org.hisp.dhis.configuration.ConfigurationService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.system.util.ConversionUtils;
import org.hisp.dhis.system.util.MathUtils;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jan Henrik Overland
 */
@Transactional
public class DefaultMappingService
    implements MappingService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private MapViewStore mapViewStore;

    private MapLayerStore mapLayerStore;

    private MapLegendStore mapLegendStore;

    private MapLegendSetStore mapLegendSetStore;

    public void setMapViewStore( MapViewStore mapViewStore )
    {
        this.mapViewStore = mapViewStore;
    }

    public void setMapLayerStore( MapLayerStore mapLayerStore )
    {
        this.mapLayerStore = mapLayerStore;
    }

    public void setMapLegendStore( MapLegendStore mapLegendStore )
    {
        this.mapLegendStore = mapLegendStore;
    }

    public void setMapLegendSetStore( MapLegendSetStore mapLegendSetStore )
    {
        this.mapLegendSetStore = mapLegendSetStore;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private AggregatedDataValueService aggregatedDataValueService;

    public void setAggregatedDataValueService( AggregatedDataValueService aggregatedDataValueService )
    {
        this.aggregatedDataValueService = aggregatedDataValueService;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private ConfigurationService configurationService;

    public void setConfigurationService( ConfigurationService configurationService )
    {
        this.configurationService = configurationService;
    }

    // -------------------------------------------------------------------------
    // MappingService implementation
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // OrganisationUnits
    // -------------------------------------------------------------------------

    /**
     * Returns the relevant OrganisationUnits for the given parent identifier
     * and / or level.
     * 
     * @param parentOrganisationUnitId the OrganisationUnit level.
     * @param level the OrganisationUnit level.
     * @return a collection of OrganisationUnits.
     */
    private Collection<OrganisationUnit> getOrganisationUnits( Integer parentOrganisationUnitId, Integer level )
    {
        Collection<OrganisationUnit> organisationUnits = null;

        if ( parentOrganisationUnitId != null && level != null )
        {
            organisationUnits = organisationUnitService.getOrganisationUnitsAtLevel( level,
                organisationUnitService.getOrganisationUnit( parentOrganisationUnitId ) );
        }
        else if ( level != null )
        {
            organisationUnits = organisationUnitService.getOrganisationUnitsAtLevel( level );
        }
        else if ( parentOrganisationUnitId != null )
        {
            organisationUnits = organisationUnitService.getOrganisationUnit( parentOrganisationUnitId ).getChildren();
        }

        return organisationUnits;
    }

    // -------------------------------------------------------------------------
    // IndicatorMapValues
    // -------------------------------------------------------------------------

    /**
     * Generates a collection AggregatedMapValues. Only one of Period and
     * start/end date can be specified. At least one of parent organisation unit
     * and level must be specified. Period should be specified with "real time"
     * aggregation strategy, any may be specified with "batch" aggregation
     * strategy.
     * 
     * @param indicatorId the Indicator identifier.
     * @param periodId the Period identifier. Ignored if null.
     * @param parentOrganisationUnitId the parent OrganisationUnit identifier.
     *        Ignored if null.
     * @param level the OrganisationUnit level. Ignored if null.
     * @return a collection of AggregatedMapValues.
     */
    public Collection<AggregatedMapValue> getIndicatorMapValues( int indicatorId, int periodId,
        int parentOrganisationUnitId, Integer level )
    {
        Collection<OrganisationUnit> units = getOrganisationUnits( parentOrganisationUnitId, level );

        return getIndicatorMapValues( indicatorId, periodId, units );
    }

    public Collection<AggregatedMapValue> getIndicatorMapValues( int indicatorId, int periodId,
        Collection<OrganisationUnit> units )
    {
        Collection<AggregatedMapValue> values = aggregatedDataValueService.getAggregatedIndicatorMapValues(
            indicatorId, periodId, ConversionUtils.getIdentifiers( OrganisationUnit.class, units ) );

        for ( AggregatedMapValue value : values )
        {
            value.setValue( MathUtils.getRounded( value.getValue(), 2 ) );
        }

        return values;
    }

    // -------------------------------------------------------------------------
    // DataElementMapValues
    // -------------------------------------------------------------------------

    /**
     * Generates a collection AggregatedMapValues. Only one of Period and
     * start/end date can be specified. At least one of parent organisation unit
     * and level must be specified. Period should be specified with "real time"
     * aggregation strategy, any may be specified with "batch" aggregation
     * strategy.
     * 
     * @param dataElementId the DataElement identifier.
     * @param periodId the Period identifier. Ignored if null.
     * @param parentOrganisationUnitId the parent OrganisationUnit identifier.
     *        Ignored if null.
     * @param level the OrganisationUnit level. Ignored if null.
     * @return a collection of AggregatedMapValues.
     */
    public Collection<AggregatedMapValue> getDataElementMapValues( int dataElementId, int periodId,
        int parentOrganisationUnitId, Integer level )
    {
        Collection<OrganisationUnit> units = getOrganisationUnits( parentOrganisationUnitId, level );

        return getDataElementMapValues( dataElementId, periodId, units );
    }

    public Collection<AggregatedMapValue> getDataElementMapValues( int dataElementId, int periodId,
        Collection<OrganisationUnit> units )
    {
        Collection<AggregatedMapValue> values = aggregatedDataValueService.getAggregatedDataMapValues( dataElementId,
            periodId, ConversionUtils.getIdentifiers( OrganisationUnit.class, units ) );

        for ( AggregatedMapValue value : values )
        {
            value.setValue( MathUtils.getRounded( value.getValue(), 2 ) );
        }

        return values;
    }

    public Collection<AggregatedMapValue> getInfrastructuralDataElementMapValues( Integer periodId,
        Integer organisationUnitId )
    {
        DataElementGroup group = configurationService.getConfiguration().getInfrastructuralDataElements();

        if ( group == null )
        {
            return new HashSet<AggregatedMapValue>();
        }

        Collection<Integer> dataElementIds = ConversionUtils.getIdentifiers( DataElement.class, group.getMembers() );

        return aggregatedDataValueService.getAggregatedDataMapValues( dataElementIds, periodId, organisationUnitId );
    }

    // -------------------------------------------------------------------------
    // MapLegend
    // -------------------------------------------------------------------------

    public void addOrUpdateMapLegend( String name, Double startValue, Double endValue, String color, String image )
    {
        MapLegend mapLegend = getMapLegendByName( name );

        if ( mapLegend != null )
        {
            mapLegend.setName( name );
            mapLegend.setStartValue( startValue );
            mapLegend.setEndValue( endValue );
            mapLegend.setColor( color );
            mapLegend.setImage( image );

            mapLegendStore.update( mapLegend );
        }
        else
        {
            mapLegend = new MapLegend( name, startValue, endValue, color, image );

            mapLegendStore.save( mapLegend );
        }
    }

    public void deleteMapLegend( MapLegend mapLegend )
    {
        mapLegendStore.delete( mapLegend );
    }

    public MapLegend getMapLegend( int id )
    {
        return mapLegendStore.get( id );
    }

    @Override
    public MapLegend getMapLegend( String uid )
    {
        return mapLegendStore.getByUid( uid );
    }

    public MapLegend getMapLegendByName( String name )
    {
        return mapLegendStore.getByName( name );
    }

    public Collection<MapLegend> getAllMapLegends()
    {
        return mapLegendStore.getAll();
    }

    // -------------------------------------------------------------------------
    // MapLegendSet
    // -------------------------------------------------------------------------

    public int addMapLegendSet( MapLegendSet mapLegendSet )
    {
        return mapLegendSetStore.save( mapLegendSet );
    }

    public void updateMapLegendSet( MapLegendSet mapLegendSet )
    {
        mapLegendSetStore.update( mapLegendSet );
    }

    public void addOrUpdateMapLegendSet( String name, String type, String symbolizer, Set<MapLegend> mapLegends )
    {
        MapLegendSet mapLegendSet = getMapLegendSetByName( name );

        Set<Indicator> indicators = new HashSet<Indicator>();

        Set<DataElement> dataElements = new HashSet<DataElement>();

        if ( mapLegendSet != null )
        {
            mapLegendSet.setType( type );
            mapLegendSet.setSymbolizer( symbolizer );
            mapLegendSet.setMapLegends( mapLegends );
            mapLegendSet.setIndicators( indicators );
            mapLegendSet.setDataElements( dataElements );

            mapLegendSetStore.update( mapLegendSet );
        }
        else
        {
            mapLegendSet = new MapLegendSet( name, type, symbolizer, mapLegends, indicators, dataElements );

            mapLegendSetStore.save( mapLegendSet );
        }
    }

    public void deleteMapLegendSet( MapLegendSet mapLegendSet )
    {
        mapLegendSetStore.delete( mapLegendSet );
    }

    public MapLegendSet getMapLegendSet( int id )
    {
        return mapLegendSetStore.get( id );
    }

    @Override
    public MapLegendSet getMapLegendSet( String uid )
    {
        return mapLegendSetStore.getByUid( uid );
    }

    public MapLegendSet getMapLegendSetByName( String name )
    {
        return mapLegendSetStore.getByName( name );
    }

    public Collection<MapLegendSet> getMapLegendSetsByType( String type )
    {
        return mapLegendSetStore.getMapLegendSetsByType( type );
    }

    public MapLegendSet getMapLegendSetByIndicator( int indicatorId )
    {
        Indicator indicator = indicatorService.getIndicator( indicatorId );

        Collection<MapLegendSet> mapLegendSets = mapLegendSetStore.getAll();

        for ( MapLegendSet mapLegendSet : mapLegendSets )
        {
            if ( mapLegendSet.getIndicators().contains( indicator ) )
            {
                return mapLegendSet;
            }
        }

        return null;
    }

    public MapLegendSet getMapLegendSetByDataElement( int dataElementId )
    {
        DataElement dataElement = dataElementService.getDataElement( dataElementId );

        Collection<MapLegendSet> mapLegendSets = mapLegendSetStore.getAll();

        for ( MapLegendSet mapLegendSet : mapLegendSets )
        {
            if ( mapLegendSet.getDataElements().contains( dataElement ) )
            {
                return mapLegendSet;
            }
        }

        return null;
    }

    public Collection<MapLegendSet> getAllMapLegendSets()
    {
        return mapLegendSetStore.getAll();
    }

    public boolean indicatorHasMapLegendSet( int indicatorId )
    {
        Indicator indicator = indicatorService.getIndicator( indicatorId );

        Collection<MapLegendSet> mapLegendSets = mapLegendSetStore.getAll();

        for ( MapLegendSet mapLegendSet : mapLegendSets )
        {
            if ( mapLegendSet.getIndicators().contains( indicator ) )
            {
                return true;
            }
        }

        return false;
    }

    // -------------------------------------------------------------------------
    // MapView
    // -------------------------------------------------------------------------

    public int addMapView( MapView mapView )
    {
        return mapViewStore.save( mapView );
    }

    public void addMapView( String name, boolean system, String mapValueType, Integer indicatorGroupId,
        Integer indicatorId, Integer dataElementGroupId, Integer dataElementId, String periodTypeName,
        Integer periodId, Integer parentOrganisationUnitId, Integer organisationUnitLevel, String mapLegendType,
        Integer method, Integer classes, String bounds, String colorLow, String colorHigh, Integer mapLegendSetId,
        Integer radiusLow, Integer radiusHigh, String longitude, String latitude, int zoom )
    {
        User user = system ? null : currentUserService.getCurrentUser();

        IndicatorGroup indicatorGroup = null;

        Indicator indicator = null;

        DataElementGroup dataElementGroup = null;

        DataElement dataElement = null;

        if ( mapValueType.equals( MappingService.MAP_VALUE_TYPE_INDICATOR ) )
        {
            indicatorGroup = indicatorService.getIndicatorGroup( indicatorGroupId );
            indicator = indicatorService.getIndicator( indicatorId );
        }
        else
        {
            dataElementGroup = dataElementService.getDataElementGroup( dataElementGroupId );
            dataElement = dataElementService.getDataElement( dataElementId );
        }

        PeriodType periodType = periodTypeName != null && !periodTypeName.isEmpty() ? periodService
            .getPeriodTypeByClass( PeriodType.getPeriodTypeByName( periodTypeName ).getClass() ) : null;

        Period period = periodId != null ? periodService.getPeriod( periodId ) : null;

        OrganisationUnit parent = organisationUnitService.getOrganisationUnit( parentOrganisationUnitId );

        OrganisationUnitLevel level = organisationUnitService.getOrganisationUnitLevelByLevel( organisationUnitLevel );

        MapLegendSet mapLegendSet = mapLegendSetId != null ? getMapLegendSet( mapLegendSetId ) : null;

        addMapView( new MapView( name, user, mapValueType, indicatorGroup, indicator, dataElementGroup, dataElement,
            periodType, period, parent, level, mapLegendType, method, classes, bounds, colorLow, colorHigh,
            mapLegendSet, radiusLow, radiusHigh, longitude, latitude, zoom ) );
    }

    public void updateMapView( MapView mapView )
    {
        mapViewStore.update( mapView );
    }

    public void deleteMapView( MapView mapView )
    {
        mapViewStore.delete( mapView );
    }

    public MapView getMapView( int id )
    {
        MapView mapView = mapViewStore.get( id );

        setMapViewLevel( mapView );

        return mapView;
    }

    public MapView getMapView( String uid )
    {
        MapView mapView = mapViewStore.getByUid( uid );

        setMapViewLevel( mapView );

        return mapView;
    }

    private void setMapViewLevel( MapView mapView )
    {
        if ( mapView != null )
        {
            mapView.getParentOrganisationUnit().setLevel(
                organisationUnitService.getLevelOfOrganisationUnit( mapView.getParentOrganisationUnit().getId() ) );
        }
    }

    public MapView getMapViewByName( String name )
    {
        return mapViewStore.getByName( name );
    }

    public MapView getIndicatorLastYearMapView( String indicatorUid, String organisationUnitUid, int level )
    {
        MapView mapView = new MapView();

        Period period = periodService.reloadPeriod( new RelativePeriods().setThisYear( true ).getRelativePeriods()
            .iterator().next() );

        Indicator indicator = indicatorService.getIndicator( indicatorUid );
        OrganisationUnit unit = organisationUnitService.getOrganisationUnit( organisationUnitUid );

        mapView.setIndicator( indicator );
        mapView.setPeriod( period );
        mapView.setParentOrganisationUnit( unit );
        mapView.setOrganisationUnitLevel( new OrganisationUnitLevel( level, "" ) );
        mapView.setName( indicator.getName() );
        mapView.setMapValueType( MappingService.MAP_VALUE_TYPE_INDICATOR );

        return mapView;
    }

    public Collection<MapView> getSystemAndUserMapViews()
    {
        User user = currentUserService.getCurrentUser();
        
        return mapViewStore.getSystemAndUserMapViews( user );
    }
    
    public Collection<MapView> getAllMapViews()
    {
        Collection<MapView> mapViews = mapViewStore.getAll();

        if ( mapViews.size() > 0 )
        {
            for ( MapView mapView : mapViews )
            {
                mapView.getParentOrganisationUnit().setLevel(
                    organisationUnitService.getLevelOfOrganisationUnit( mapView.getParentOrganisationUnit().getId() ) );
            }
        }

        return mapViews;
    }

    public Collection<MapView> getMapViewsByFeatureType( String featureType )
    {
        User user = currentUserService.getCurrentUser();

        Collection<MapView> mapViews = mapViewStore.getMapViewsByFeatureType( featureType, user );

        for ( MapView mapView : mapViews )
        {
            mapView.getParentOrganisationUnit().setLevel(
                organisationUnitService.getLevelOfOrganisationUnit( mapView.getParentOrganisationUnit().getId() ) );
        }

        return mapViews;
    }
    
    public Collection<MapView> getMapViewsByUser( User user )
    {
        return mapViewStore.getByUser( user );
    }

    // -------------------------------------------------------------------------
    // MapLayer
    // -------------------------------------------------------------------------

    public int addMapLayer( MapLayer mapLayer )
    {
        return mapLayerStore.save( mapLayer );
    }

    public void updateMapLayer( MapLayer mapLayer )
    {
        mapLayerStore.update( mapLayer );
    }

    public void addOrUpdateMapLayer( String name, String type, String url, String layers, String time,
        String fillColor, double fillOpacity, String strokeColor, int strokeWidth )
    {
        MapLayer mapLayer = mapLayerStore.getByName( name );

        if ( mapLayer != null )
        {
            mapLayer.setName( name );
            mapLayer.setType( type );
            mapLayer.setUrl( url );
            mapLayer.setLayers( layers );
            mapLayer.setTime( time );
            mapLayer.setFillColor( fillColor );
            mapLayer.setFillOpacity( fillOpacity );
            mapLayer.setStrokeColor( strokeColor );
            mapLayer.setStrokeWidth( strokeWidth );

            updateMapLayer( mapLayer );
        }
        else
        {
            addMapLayer( new MapLayer( name, type, url, layers, time, fillColor, fillOpacity, strokeColor, strokeWidth ) );
        }
    }

    public void deleteMapLayer( MapLayer mapLayer )
    {
        mapLayerStore.delete( mapLayer );
    }

    public MapLayer getMapLayer( int id )
    {
        return mapLayerStore.get( id );
    }

    @Override
    public MapLayer getMapLayer( String uid )
    {
        return mapLayerStore.getByUid( uid );
    }

    public MapLayer getMapLayerByName( String name )
    {
        return mapLayerStore.getByName( name );
    }

    public Collection<MapLayer> getMapLayersByType( String type )
    {
        return mapLayerStore.getMapLayersByType( type );
    }

    public MapLayer getMapLayerByMapSource( String mapSource )
    {
        return mapLayerStore.getMapLayerByMapSource( mapSource );
    }

    public Collection<MapLayer> getAllMapLayers()
    {
        return mapLayerStore.getAll();
    }
}
