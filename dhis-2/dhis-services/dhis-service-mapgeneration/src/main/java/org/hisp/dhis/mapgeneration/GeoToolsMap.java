package org.hisp.dhis.mapgeneration;

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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This class can be used to render map objects onto a map image. The projection
 * is transformed automatically to "EPSG 3785".
 * 
 * @author Kjetil Andresen <kjetand@ifi.uio.no>
 * @author Olai Solheim <olais@ifi.uio.no>
 */
public class GeoToolsMap
    extends InternalMap
{
    // The flat list of map objects in this map.
    private List<GeoToolsMapObject> mapObjects;

    /**
     * Creates an empty map.
     */
    public GeoToolsMap()
    {
        this.mapObjects = new LinkedList<GeoToolsMapObject>();
    }

    /**
     * Creates a map with the given initial map layer.
     * 
     * @param layer the initial map layer
     */
    public GeoToolsMap( InternalMapLayer layer )
    {
        this.mapObjects = new LinkedList<GeoToolsMapObject>();
        this.addMapLayer( layer );
    }

    /**
     * Creates a map with the given initial map layers.
     * 
     * @param layers the list of initial map layers
     */
    public GeoToolsMap( List<InternalMapLayer> layers )
    {
        this.mapObjects = new LinkedList<GeoToolsMapObject>();
        this.addAllMapLayers( layers );
    }

    /**
     * Adds a map object to this map.
     * 
     * @param mapObject the map object
     */
    public void addMapObject( GeoToolsMapObject mapObject )
    {
        this.mapObjects.add( mapObject );
    }

    /**
     * Adds all map objects contained in the list.
     * 
     * @param mapObjects the list of map objects
     */
    public void addMapObjects( List<GeoToolsMapObject> mapObjects )
    {
        this.mapObjects.addAll( mapObjects );
    }

    // -------------------------------------------------------------------------
    // InternalMap implementation
    // -------------------------------------------------------------------------

    public void addMapLayer( InternalMapLayer layer )
    {
        for ( InternalMapObject mapObject : layer.getAllMapObjects() )
        {
            this.addMapObject( (GeoToolsMapObject) mapObject );
        }
    }

    public void addAllMapLayers( List<InternalMapLayer> layers )
    {
        for ( InternalMapLayer layer : layers )
        {
            for ( InternalMapObject mapObject : layer.getAllMapObjects() )
            {
                this.addMapObject( (GeoToolsMapObject) mapObject );
            }
        }
    }

    public BufferedImage render()
    {
        return render( DEFAULT_MAP_WIDTH );
    }

    public BufferedImage render( int imageWidth )
    {
        MapContent map = new MapContent();

        // Convert map objects to features, and add them to the map
        for ( GeoToolsMapObject mapObject : mapObjects )
        {
            try
            {
                map.addLayer( createFeatureLayerFromMapObject( mapObject ) );
            }
            catch ( SchemaException ex )
            {
                throw new RuntimeException( "Could not add map object: " + mapObject.toString() + ": " + ex.getMessage() );
            }
        }

        // Create a renderer for this map
        GTRenderer renderer = new StreamingRenderer();
        renderer.setMapContent( map );

        // Calculate image height
        // TODO Might want to add a margin of say 25 pixels surrounding the map
        ReferencedEnvelope mapBounds = map.getMaxBounds();
        double imageHeightFactor = mapBounds.getSpan( 1 ) / mapBounds.getSpan( 0 );
        Rectangle imageBounds = new Rectangle( 0, 0, imageWidth, (int) Math.ceil( imageWidth * imageHeightFactor ) );

        // Create an image and get the graphics context from it
        BufferedImage image = new BufferedImage( imageBounds.width, imageBounds.height, BufferedImage.TYPE_INT_ARGB );
        Graphics2D g = (Graphics2D) image.getGraphics();

        // Draw a background if the background color is specified
        // NOTE It will be transparent otherwise, which is desired
        if ( backgroundColor != null )
        {
            g.setColor( backgroundColor );
            g.fill( imageBounds );
        }

        // Enable anti-aliasing if specified
        if ( isAntiAliasingEnabled )
        {
            g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        }
        else
        {
            g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
        }

        // Render the map
        renderer.paint( g, imageBounds, mapBounds );

        map.dispose();
        
        return image;
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    /**
     * Creates a feature layer based on a map object.
     */
    private Layer createFeatureLayerFromMapObject( GeoToolsMapObject mapObject )
        throws SchemaException
    {
        SimpleFeatureType featureType;
        SimpleFeatureBuilder featureBuilder;
        SimpleFeature feature;
        SimpleFeatureCollection featureCollection;
        Style style = null;

        featureType = createFeatureType( mapObject.getGeometry() );
        featureBuilder = new SimpleFeatureBuilder( featureType );
        featureBuilder.add( mapObject.getGeometry() );
        feature = featureBuilder.buildFeature( null );

        featureCollection = FeatureCollections.newCollection();
        featureCollection.add( feature );

        // Create style for this map object
        if ( mapObject.getGeometry() instanceof Point )
        {
            style = SLD.createPointStyle( null, mapObject.getStrokeColor(), mapObject.getFillColor(),
                mapObject.getFillOpacity(), mapObject.getRadius() );
        }
        else if ( mapObject.getGeometry() instanceof Polygon || mapObject.getGeometry() instanceof MultiPolygon )
        {
            style = SLD.createPolygonStyle( mapObject.getStrokeColor(), mapObject.getFillColor(),
                mapObject.getFillOpacity() );
        }
        else
        {
            style = SLD.createSimpleStyle( featureType );
        }

        return new FeatureLayer( featureCollection, style );
    }

    /**
     * Creates a feature type for a GeoTools geometric primitive.
     */
    private SimpleFeatureType createFeatureType( Geometry geom )
        throws SchemaException
    {
        String type = "";

        if ( geom instanceof Point )
        {
            type = "Point";
        }
        else if ( geom instanceof Polygon )
        {
            type = "Polygon";
        }
        else if ( geom instanceof MultiPolygon )
        {
            type = "MultiPolygon";
        }
        else
        {
            throw new IllegalArgumentException();
        }

        return DataUtilities.createType( "geometries", "geometry:" + type + ":srid=3785" );
    }

    /**
     * Creates an image with text indicating an error.
     */
    @SuppressWarnings( "unused" )
    private BufferedImage createErrorImage( String error )
    {
        String str = "Error creating map image: " + error;
        BufferedImage image = new BufferedImage( 500, 25, BufferedImage.TYPE_INT_RGB );
        Graphics2D g = (Graphics2D) image.createGraphics();

        g.setColor( Color.WHITE );
        g.fill( new Rectangle( 500, 25 ) );

        g.setColor( Color.RED );
        g.drawString( str, 1, 12 );

        return image;
    }
}
