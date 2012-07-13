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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.configuration.ConfigurationService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.mapping.MapLayer;
import org.hisp.dhis.mapping.MapView;
import org.hisp.dhis.mapping.MappingService;
import org.hisp.dhis.mapping.comparator.MapLayerNameComparator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.PeriodType;

import com.opensymphony.xwork2.Action;

/**
 * @author Jan Henrik Overland
 * @version $Id$
 */
public class InitializeAction
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

    private ConfigurationService configurationService;

    public void setConfigurationService( ConfigurationService configurationService )
    {
        this.configurationService = configurationService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String id;

    public void setId( String id )
    {
        this.id = id;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private MapView mapView;

    public MapView getMapView()
    {
        return mapView;
    }

    private List<MapLayer> baseLayers;

    public List<MapLayer> getBaseLayers()
    {
        return baseLayers;
    }

    private List<MapLayer> overlays;

    public List<MapLayer> getOverlays()
    {
        return overlays;
    }

    private DataElementGroup infrastructuralDataElements;

    public DataElementGroup getInfrastructuralDataElements()
    {
        return infrastructuralDataElements;
    }

    private PeriodType infrastructuralPeriodType;

    public PeriodType getInfrastructuralPeriodType()
    {
        return infrastructuralPeriodType;
    }

    private OrganisationUnit rootNode;

    public OrganisationUnit getRootNode()
    {
        return rootNode;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        if ( id != null )
        {
            mapView = mappingService.getMapView( id );
        }

        baseLayers = new ArrayList<MapLayer>(
            mappingService.getMapLayersByType( MappingService.MAP_LAYER_TYPE_BASELAYER ) );

        Collections.sort( baseLayers, new MapLayerNameComparator() );

        overlays = new ArrayList<MapLayer>( mappingService.getMapLayersByType( MappingService.MAP_LAYER_TYPE_OVERLAY ) );

        Collections.sort( overlays, new MapLayerNameComparator() );

        infrastructuralDataElements = configurationService.getConfiguration().getInfrastructuralDataElements();

        infrastructuralPeriodType = configurationService.getConfiguration().getInfrastructuralPeriodTypeDefaultIfNull();

        Collection<OrganisationUnit> rootUnits = new ArrayList<OrganisationUnit>(
            organisationUnitService.getOrganisationUnitsAtLevel( 1 ) );
        
        rootNode = rootUnits.size() > 0 ? rootUnits.iterator().next() : new OrganisationUnit();

        return SUCCESS;
    }
}