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

import org.hisp.dhis.aggregation.AggregatedMapValue;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.user.User;

import java.util.Collection;
import java.util.Set;

/**
 * @author Jan Henrik Overland
 * @version $Id$
 */
public interface MappingService
{
    final String ID = MappingService.class.getName();

    final String GEOJSON_DIR = "geojson";

    final String MAP_VALUE_TYPE_INDICATOR = "indicator";
    final String MAP_VALUE_TYPE_DATAELEMENT = "dataelement";

    final String MAP_LEGEND_SYMBOLIZER_COLOR = "color";
    final String MAP_LEGEND_SYMBOLIZER_IMAGE = "image";

    final String MAPLEGENDSET_TYPE_AUTOMATIC = "automatic";
    final String MAPLEGENDSET_TYPE_PREDEFINED = "predefined";

    final String KEY_MAP_DATE_TYPE = "dateType";

    final String MAP_DATE_TYPE_FIXED = "fixed";
    final String MAP_DATE_TYPE_START_END = "start-end";

    final String ORGANISATION_UNIT_SELECTION_TYPE_PARENT = "parent";
    final String ORGANISATION_UNIT_SELECTION_TYPE_LEVEL = "level";

    final String MAP_LAYER_TYPE_BASELAYER = "baselayer";
    final String MAP_LAYER_TYPE_OVERLAY = "overlay";

    // -------------------------------------------------------------------------
    // IndicatorMapValue
    // -------------------------------------------------------------------------

    Collection<AggregatedMapValue> getIndicatorMapValues( int indicatorId, int periodId, int parentOrganisationUnitId,
                                                          Integer level );

    Collection<AggregatedMapValue> getIndicatorMapValues( int indicatorId, int periodId, Collection<OrganisationUnit> units );

    // -------------------------------------------------------------------------
    // DataMapValue
    // -------------------------------------------------------------------------

    Collection<AggregatedMapValue> getDataElementMapValues( int dataElementId, int periodId,
                                                            int parentOrganisationUnitId, Integer level );

    Collection<AggregatedMapValue> getDataElementMapValues( int dataElementId, int periodId, Collection<OrganisationUnit> units );

    Collection<AggregatedMapValue> getInfrastructuralDataElementMapValues( Integer periodId, Integer organisationUnitId );

    // -------------------------------------------------------------------------
    // MapLegend
    // -------------------------------------------------------------------------

    void addOrUpdateMapLegend( String name, Double startValue, Double endValue, String color, String image );

    void deleteMapLegend( MapLegend legend );

    MapLegend getMapLegend( int id );

    MapLegend getMapLegend( String uid );

    MapLegend getMapLegendByName( String name );

    Collection<MapLegend> getAllMapLegends();

    // -------------------------------------------------------------------------
    // MapLegendSet
    // -------------------------------------------------------------------------

    int addMapLegendSet( MapLegendSet legendSet );

    void updateMapLegendSet( MapLegendSet legendSet );

    void addOrUpdateMapLegendSet( String name, String type, String symbolizer, Set<MapLegend> mapLegends );

    void deleteMapLegendSet( MapLegendSet legendSet );

    MapLegendSet getMapLegendSet( int id );

    MapLegendSet getMapLegendSet( String uid );

    MapLegendSet getMapLegendSetByName( String name );

    Collection<MapLegendSet> getMapLegendSetsByType( String type );

    MapLegendSet getMapLegendSetByIndicator( int indicatorId );

    MapLegendSet getMapLegendSetByDataElement( int dataElementId );

    Collection<MapLegendSet> getAllMapLegendSets();

    boolean indicatorHasMapLegendSet( int indicatorId );

    // -------------------------------------------------------------------------
    // MapView
    // -------------------------------------------------------------------------

    int addMapView( MapView mapView );

    void addMapView( String name, boolean system, String mapValueType, Integer indicatorGroupId, Integer indicatorId,
                     Integer dataElementGroupId, Integer dataElementId, String periodTypeName, Integer periodId,
                     Integer parentOrganisationUnitId, Integer organisationUnitLevel, String mapLegendType, Integer method,
                     Integer classes, String bounds, String colorLow, String colorHigh, Integer mapLegendSetId, Integer radiusLow,
                     Integer radiusHigh, String longitude, String latitude, int zoom );

    void updateMapView( MapView mapView );

    void deleteMapView( MapView view );

    MapView getMapView( int id );

    MapView getMapView( String uid );

    MapView getMapViewByName( String name );

    MapView getIndicatorLastYearMapView( String indicatorUid, String organisationUnitUid, int level );

    Collection<MapView> getAllMapViews();
    
    Collection<MapView> getSystemAndUserMapViews();

    Collection<MapView> getMapViewsByFeatureType( String featureType );
    
    Collection<MapView> getMapViewsByUser( User user );

    // -------------------------------------------------------------------------
    // MapLayer
    // -------------------------------------------------------------------------

    int addMapLayer( MapLayer mapLayer );

    void updateMapLayer( MapLayer mapLayer );

    void addOrUpdateMapLayer( String name, String type, String url, String layers, String time, String fillColor,
                              double fillOpacity, String strokeColor, int strokeWidth );

    void deleteMapLayer( MapLayer mapLayer );

    MapLayer getMapLayer( int id );

    MapLayer getMapLayer( String uid );

    MapLayer getMapLayerByName( String name );

    Collection<MapLayer> getMapLayersByType( String type );

    MapLayer getMapLayerByMapSource( String mapSource );

    Collection<MapLayer> getAllMapLayers();
}