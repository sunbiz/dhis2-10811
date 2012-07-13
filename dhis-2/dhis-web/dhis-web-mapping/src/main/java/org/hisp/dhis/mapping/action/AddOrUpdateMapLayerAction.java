package org.hisp.dhis.mapping.action;

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

import org.hisp.dhis.mapping.MappingService;

import com.opensymphony.xwork2.Action;

/**
 * @author Jan Henrik Overland
 * @version $Id$
 */
public class AddOrUpdateMapLayerAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private MappingService mappingService;

    public void setMappingService( MappingService mappingService )
    {
        this.mappingService = mappingService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private String name;

    public void setName( String name )
    {
        this.name = name;
    }
    
    private String type;
    
    public void setType( String type )
    {
        this.type = type;
    }
    
    private String url;
    
    public void setUrl( String url )
    {
        this.url = url;
    }
    
    private String layers;
    
    public void setLayers( String layers )
    {
        this.layers = layers;
    }
    
    private String time;
    
    public void setTime( String time )
    {
        this.time = time;
    }

    private String fillColor;

    public void setFillColor( String fillColor )
    {
        this.fillColor = fillColor;
    }

    private double fillOpacity;

    public void setFillOpacity( double fillOpacity )
    {
        this.fillOpacity = fillOpacity;
    }
    
    private String strokeColor;

    public void setStrokeColor( String strokeColor )
    {
        this.strokeColor = strokeColor;
    }
    
    private int strokeWidth;

    public void setStrokeWidth( int strokeWidth )
    {
        this.strokeWidth = strokeWidth;
    }    
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        mappingService.addOrUpdateMapLayer( name, type, url, layers, time, fillColor, fillOpacity, strokeColor, strokeWidth );
        
        return SUCCESS;
    }
}
