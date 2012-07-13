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

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Utility class.
 * 
 * @author Olai Solheim <olais@ifi.uio.no>
 */
public class MapUtils
{
    private static final String COLOR_PREFIX = "#";
    private static final int COLOR_RADIX = 16;
    
    /**
     * Linear interpolation of int.
     * 
     * @param a from
     * @param b to
     * @param t factor, typically 0-1
     * @return the interpolated int
     */
    public static int lerp( int a, int b, double t )
    {
        return a + (int) ((b - a) * t);
    }

    /**
     * Linear interpolation of double.
     * 
     * @param a from
     * @param b to
     * @param t factor, typically 0-1
     * @return the interpolated double
     */
    public static double lerp( double a, double b, double t )
    {
        return a + (b - a) * t;
    }

    /**
     * Linear interpolation of RGB colors.
     * 
     * @param a from
     * @param b to
     * @param t interpolation factor, typically 0-1
     * @return the interpolated color
     */
    public static Color lerp( Color a, Color b, double t )
    {
        return new Color( lerp( a.getRed(), b.getRed(), t ), lerp( a.getGreen(), b.getGreen(), t ), lerp( a.getBlue(),
            b.getBlue(), t ), lerp( a.getAlpha(), b.getAlpha(), t ) );
    }

    /**
     * Creates a java.awt.Color from a dhis style color string, e.g. '#ff3200'
     * is an orange color.
     * 
     * @param string the color in string, e.g. '#ff3200'
     * @return the Color, or null if string is null or empty.
     */
    public static Color createColorFromString( String string )
    {
        if ( string == null || string.trim().isEmpty() )
        {
            return null;
        }
        
        string = string.startsWith( COLOR_PREFIX ) ? string.substring( 1 ) : string;
        
        return new Color( Integer.parseInt( string, COLOR_RADIX ) );
    }
    
    /**
     * Returns the number of non empty sub JsonNodes in the given JsonNode.
     * 
     * @param json the JsonNode.
     * @return the number of non empty sub JsonNodes.
     */
    public static int getNonEmptyNodes( JsonNode json )
    {
        int count = 0;
        
        for ( int i = 0; i < json.size(); i++ )
        {
            JsonNode node = json.get( i );
            
            count = nodeIsNonEmpty( node ) ? ++count : count;
        }
        
        return count;
    }
    
    /**
     * Indicates whether the given JsonNode is empty, which implies that the
     * node is not null and has a size greater than 0.
     * 
     * @param json the JsonNode.
     * @return true if the given JsonNode is non empty, false otherwise.
     */
    public static boolean nodeIsNonEmpty( JsonNode json )
    {
        return json != null && json.size() > 0;
    }
}
