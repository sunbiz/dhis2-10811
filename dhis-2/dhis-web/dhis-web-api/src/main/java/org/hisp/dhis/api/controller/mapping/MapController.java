package org.hisp.dhis.api.controller.mapping;

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

import static org.hisp.dhis.period.PeriodType.getPeriodFromIsoString;

import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hisp.dhis.api.controller.AbstractAccessControlController;
import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dxf2.utils.JacksonUtils;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.mapping.Map;
import org.hisp.dhis.mapping.MapView;
import org.hisp.dhis.mapping.MappingService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.user.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 * @author Lars Helge Overland
 */
@Controller
@RequestMapping( value = MapController.RESOURCE_PATH )
public class MapController
    extends AbstractAccessControlController<Map>
{
    public static final String RESOURCE_PATH = "/maps";

    @Autowired
    private MappingService mappingService;

    @Autowired
    private OrganisationUnitService organisationUnitService;
    
    @Autowired
    private OrganisationUnitGroupService organisationUnitGroupService;
    
    @Autowired
    private IndicatorService indicatorService;
    
    @Autowired
    private DataElementService dataElementService;
    
    @Autowired
    private PeriodService periodService;
    
    @Autowired
    private CurrentUserService currentUserService;
    
    //--------------------------------------------------------------------------
    // CRUD
    //--------------------------------------------------------------------------

    @RequestMapping( method = RequestMethod.POST, consumes = "application/json" )
    @PreAuthorize( "hasRole('F_GIS_ADMIN') or hasRole('ALL')" )
    public void postJsonObject( HttpServletResponse response, HttpServletRequest request, InputStream input ) throws Exception
    {
        Map map = JacksonUtils.fromJson( input, Map.class );

        mergeMap( map );
        
        for ( MapView view : map.getMapViews() )
        {
            mergeMapView( view );
            
            mappingService.addMapView( view );
        }

        mappingService.addMap( map );
        
        ContextUtils.createdResponse( response, "Map created", RESOURCE_PATH + "/" + map.getUid() );
    }
    
    @RequestMapping( value = "/{uid}", method = RequestMethod.PUT, consumes = "application/json" )
    @ResponseStatus( value = HttpStatus.NO_CONTENT )
    @PreAuthorize( "hasRole('F_GIS_ADMIN') or hasRole('ALL')" )
    public void putJsonObject( HttpServletResponse response, HttpServletRequest request, @PathVariable( "uid" ) String uid, InputStream input ) throws Exception
    {
        Map map = mappingService.getMap( uid );
        
        if ( map == null )
        {
            ContextUtils.notFoundResponse( response, "Map does not exist: " + uid );
            return;
        }

        Iterator<MapView> views = map.getMapViews().iterator();
        
        while ( views.hasNext() )
        {
            MapView view = views.next();
            views.remove();
            mappingService.deleteMapView( view );
        }
        
        Map newMap = JacksonUtils.fromJson( input, Map.class );

        mergeMap( newMap );

        for ( MapView view : newMap.getMapViews() )
        {
            mergeMapView( view );
            
            mappingService.addMapView( view );
        }

        map.mergeWith( newMap );
        
        if ( newMap.getUser() == null )
        {
            map.setUser( null );
        }
        
        mappingService.updateMap( map );
    }
    
    @RequestMapping( value = "/{uid}", method = RequestMethod.DELETE )
    @ResponseStatus( value = HttpStatus.NO_CONTENT )
    @PreAuthorize( "hasRole('F_GIS_ADMIN') or hasRole('ALL')" )
    public void deleteObject( HttpServletResponse response, HttpServletRequest request, @PathVariable( "uid" ) String uid ) throws Exception
    {
        Map map = mappingService.getMap( uid );
        
        if ( map == null )
        {
            ContextUtils.notFoundResponse( response, "Map does not exist: " + uid );
            return;
        }

        Iterator<MapView> views = map.getMapViews().iterator();
        
        while ( views.hasNext() )
        {
            MapView view = views.next();
            views.remove();
            mappingService.deleteMapView( view );
        }
        
        mappingService.deleteMap( map );
    }
    
    @Override
    public void postProcessEntity( Map map )
    {
        for ( MapView view : map.getMapViews() )
        {
            if ( view != null && view.getParentOrganisationUnit() != null )
            {
                String parentUid = view.getParentOrganisationUnit().getUid();
                view.setParentGraph( view.getParentOrganisationUnit().getParentGraph() + "/" + parentUid );
                view.setParentLevel( organisationUnitService.getLevelOfOrganisationUnit( view.getParentOrganisationUnit().getId() ) );
            }
        }
    }

    //--------------------------------------------------------------------------
    // Supportive methods
    //--------------------------------------------------------------------------

    // TODO use the import service instead
    
    private void mergeMap( Map map )
    {
        if ( map.getUser() != null )
        {
            map.setUser( currentUserService.getCurrentUser() );
        }        
    }
    
    private void mergeMapView( MapView view )
    {
        if ( view.getIndicatorGroup() != null )
        {
            view.setIndicatorGroup( indicatorService.getIndicatorGroup( view.getIndicatorGroup().getUid() ) );
        }
        
        if ( view.getIndicator() != null )
        {
            view.setIndicator( indicatorService.getIndicator( view.getIndicator().getUid() ) );
        }
        
        if ( view.getDataElementGroup() != null )
        {
            view.setDataElementGroup( dataElementService.getDataElementGroup( view.getDataElementGroup().getUid() ) );
        }
        
        if ( view.getDataElement() != null )
        {
            view.setDataElement( dataElementService.getDataElement( view.getDataElement().getUid() ) );
        }
        
        if ( view.getPeriod() != null )
        {
            view.setPeriod( periodService.reloadPeriod( getPeriodFromIsoString( view.getPeriod().getUid() ) ) );
        }
        
        if ( view.getParentOrganisationUnit() != null )
        {
            view.setParentOrganisationUnit( organisationUnitService.getOrganisationUnit( view.getParentOrganisationUnit().getUid() ) );
        }
        
        if ( view.getOrganisationUnitLevel() != null )
        {
            view.setOrganisationUnitLevel( organisationUnitService.getOrganisationUnitLevel( view.getOrganisationUnitLevel().getUid() ) );
        }
        
        if ( view.getLegendSet() != null )
        {
            view.setLegendSet( mappingService.getMapLegendSet( view.getLegendSet().getUid() ) );
        }
        
        if ( view.getOrganisationUnitGroupSet() != null )
        {
            view.setOrganisationUnitGroupSet( organisationUnitGroupService.getOrganisationUnitGroupSet( view.getOrganisationUnitGroupSet().getUid() ) );
        }
    }
}
