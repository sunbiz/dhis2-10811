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
public class AddMapViewAction
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
    
    private boolean system;

    public void setSystem( boolean system )
    {
        this.system = system;
    }

    private String mapValueType;

    public void setMapValueType( String mapValueType )
    {
        this.mapValueType = mapValueType;
    }

    private Integer indicatorGroupId;

    public void setIndicatorGroupId( Integer indicatorGroupId )
    {
        this.indicatorGroupId = indicatorGroupId;
    }

    private Integer indicatorId;

    public void setIndicatorId( Integer indicatorId )
    {
        this.indicatorId = indicatorId;
    }

    private Integer dataElementGroupId;

    public void setDataElementGroupId( Integer dataElementGroupId )
    {
        this.dataElementGroupId = dataElementGroupId;
    }

    private Integer dataElementId;

    public void setDataElementId( Integer dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    private String periodTypeId;

    public void setPeriodTypeId( String periodTypeId )
    {
        this.periodTypeId = periodTypeId;
    }

    private Integer periodId;

    public void setPeriodId( Integer periodId )
    {
        this.periodId = periodId;
    }

    private Integer parentOrganisationUnitId;

    public void setParentOrganisationUnitId( Integer parentOrganisationUnitId )
    {
        this.parentOrganisationUnitId = parentOrganisationUnitId;
    }

    private Integer organisationUnitLevel;

    public void setOrganisationUnitLevel( Integer organisationUnitLevel )
    {
        this.organisationUnitLevel = organisationUnitLevel;
    }

    private String mapLegendType;

    public void setMapLegendType( String mapLegendType )
    {
        this.mapLegendType = mapLegendType;
    }

    private Integer method;

    public void setMethod( Integer method )
    {
        this.method = method;
    }

    private Integer classes;

    public void setClasses( Integer classes )
    {
        this.classes = classes;
    }

    private String bounds;

    public void setBounds( String bounds )
    {
        this.bounds = bounds;
    }

    private String colorLow;

    public void setColorLow( String colorLow )
    {
        this.colorLow = colorLow;
    }

    private String colorHigh;

    public void setColorHigh( String colorHigh )
    {
        this.colorHigh = colorHigh;
    }

    private Integer mapLegendSetId;

    public void setMapLegendSetId( Integer mapLegendSetId )
    {
        this.mapLegendSetId = mapLegendSetId;
    }

    private Integer radiusLow;

    public void setRadiusLow( Integer radiusLow )
    {
        this.radiusLow = radiusLow;
    }

    private Integer radiusHigh;

    public void setRadiusHigh( Integer radiusHigh )
    {
        this.radiusHigh = radiusHigh;
    }

    private String longitude;

    public void setLongitude( String longitude )
    {
        this.longitude = longitude;
    }

    private String latitude;

    public void setLatitude( String latitude )
    {
        this.latitude = latitude;
    }

    private int zoom;

    public void setZoom( int zoom )
    {
        this.zoom = zoom;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        mappingService.addMapView( name, system, mapValueType, indicatorGroupId, indicatorId,
            dataElementGroupId, dataElementId, periodTypeId, periodId, parentOrganisationUnitId,
            organisationUnitLevel, mapLegendType, method, classes, bounds, colorLow, colorHigh, mapLegendSetId,
            radiusLow, radiusHigh, longitude, latitude, zoom );

        return SUCCESS;
    }
}