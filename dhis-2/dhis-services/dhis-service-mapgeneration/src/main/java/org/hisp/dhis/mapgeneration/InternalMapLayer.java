package org.hisp.dhis.mapgeneration;

/*
 * Copyright (c) 2011, University of Oslo
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
import java.util.ArrayList;
import java.util.Collection;

import org.hisp.dhis.period.Period;
import org.springframework.util.Assert;

/**
 * An internal representation of a map layer in a map.
 * 
 * It encapsulates all the information of a layer on a map that should contain
 * map objects associated with the same data-set. Thus, a map layer should
 * represent grouped data from a data-set e.g. 'deaths from malaria' is one
 * layer, 'anc coverage' is another layer, etc.
 * 
 * It is typically built using the properties of an external map layer
 * (currently MapView) defined by the user.
 * 
 * Finally, one might extend this class with an implementation that uses a
 * specific platform, if needed.
 * 
 * @author Olai Solheim <olais@ifi.uio.no>
 */
public class InternalMapLayer
{
    protected String name;

    protected Period period;

    protected int radiusHigh;

    protected int radiusLow;

    protected Color colorHigh;

    protected Color colorLow;

    protected float opacity;

    protected Color strokeColor;

    protected int strokeWidth;

    protected IntervalSet intervalSet;

    protected Collection<InternalMapObject> mapObjects;

    /**
     * Constructs a map layer with no initial map objects.
     */
    public InternalMapLayer()
    {
        this.mapObjects = new ArrayList<InternalMapObject>();
    }

    /**
     * Interpolates the radii of this map layer's set of map objects according
     * the highest and lowest values among them.
     */
    public void applyInterpolatedRadii()
    {
        Assert.isTrue( mapObjects != null );
        Assert.isTrue( mapObjects.size() > 0 );

        InternalMapObject min = null, max = null;

        // Determine the objects with the min and max values
        for ( InternalMapObject mapObject : mapObjects )
        {
            if ( min == null || mapObject.getValue() < min.getValue() )
                min = mapObject;
            if ( max == null || mapObject.getValue() > max.getValue() )
                max = mapObject;
        }

        // Determine and set the radius for each of the map objects according to
        // its value
        for ( InternalMapObject mapObject : mapObjects )
        {
            double factor = (mapObject.getValue() - min.getValue()) / (max.getValue() - min.getValue());
            int radius = MapUtils.lerp( radiusLow, radiusHigh, factor );
            mapObject.setRadius( radius );
        }
    }

    /**
     * Adds a map object to this map layer.
     * 
     * @param mapObject the map object
     */
    public void addMapObject( InternalMapObject mapObject )
    {
        this.mapObjects.add( mapObject );
    }

    /**
     * Gets the collection of all the map objects associated with this map
     * layer.
     * 
     * @return the list of map objects
     */
    public Collection<InternalMapObject> getAllMapObjects()
    {
        return this.mapObjects;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Period getPeriod()
    {
        return this.period;
    }

    public void setPeriod( Period period )
    {
        this.period = period;
    }

    public int getRadiusHigh()
    {
        return this.radiusHigh;
    }

    public void setRadiusHigh( int radiusHigh )
    {
        this.radiusHigh = radiusHigh;
    }

    public int getRadiusLow()
    {
        return this.radiusLow;
    }

    public void setRadiusLow( int radiusLow )
    {
        this.radiusLow = radiusLow;
    }

    public Color getColorHigh()
    {
        return this.colorHigh;
    }

    public void setColorHigh( Color colorHigh )
    {
        this.colorHigh = colorHigh;
    }

    public Color getColorLow()
    {
        return this.colorLow;
    }

    public void setColorLow( Color colorLow )
    {
        this.colorLow = colorLow;
    }

    public float getOpacity()
    {
        return this.opacity;
    }

    public void setOpacity( float opacity )
    {
        this.opacity = opacity;
    }

    public Color getStrokeColor()
    {
        return this.strokeColor;
    }

    public void setStrokeColor( Color strokeColor )
    {
        this.strokeColor = strokeColor;
    }

    public int getStrokeWidth()
    {
        return this.strokeWidth;
    }

    public void setStrokeWidth( int strokeWidth )
    {
        this.strokeWidth = strokeWidth;
    }

    public IntervalSet getIntervalSet()
    {
        return this.intervalSet;
    }

    public void setIntervalSet( IntervalSet intervalSet )
    {
        this.intervalSet = intervalSet;
    }
}
