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
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * An internal representation of a map.
 * 
 * It encapsulates all the information of a map built by adding layers to it. It
 * may then create an image representing the map by a call to render.
 * 
 * Finally, one should extend this class with an implementation that uses a
 * specific platform, e.g. GeoTools to draw the map.
 * 
 * @author Kjetil Andresen <kjetand@ifi.uio.no>
 * @author Olai Solheim <olais@ifi.uio.no>
 */
public abstract class InternalMap
{
    // The background color used by this map.
    protected Color backgroundColor = null;

    // True if anti-aliasing is enabled, false otherwise.
    protected boolean isAntiAliasingEnabled = true;

    // The default map image width, in pixels.
    protected static final int DEFAULT_MAP_WIDTH = 500;

    /**
     * Gets the background color of this map.
     * 
     * @return the background color, or null if not set
     */
    public Color getBackgroundColor()
    {
        return this.backgroundColor;
    }

    /**
     * Sets the background color of this map.
     * 
     * Setting this to null enables a transparent background.
     * 
     * @param backgroundColor the background color
     */
    public void setBackgroundColor( Color backgroundColor )
    {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Returns true if anti-aliasing is enabled for rendering, false otherwise.
     * 
     * @return true if anti-aliasing is enabled, false otherwise
     */
    public boolean isAntiAliasingEnabled()
    {
        return this.isAntiAliasingEnabled;
    }

    /**
     * Sets if anti-aliasing should be enabled for rendering.
     * 
     * @param b true to enable anti-aliasing, false to disable
     */
    public void setAntiAliasingEnabled( boolean b )
    {
        this.isAntiAliasingEnabled = b;
    }

    /**
     * Adds a map layer to this map.
     * 
     * @param layer the layer
     */
    public abstract void addMapLayer( InternalMapLayer layer );

    /**
     * Adds all map layers contained in the list.
     * 
     * @param layers the list of layers
     */
    public abstract void addAllMapLayers( List<InternalMapLayer> layers );

    /**
     * Renders all map objects contained in this map to an image with the
     * default image width.
     * 
     * @return the java.awt.image.BufferedImage representing this map
     */
    public abstract BufferedImage render();

    /**
     * Renders all map objects contained in this map to an image with the
     * specified width.
     * 
     * @param width the desired width of the map
     * @return the java.awt.image.BufferedImage representing this map
     */
    public abstract BufferedImage render( int imageWidth );
}
