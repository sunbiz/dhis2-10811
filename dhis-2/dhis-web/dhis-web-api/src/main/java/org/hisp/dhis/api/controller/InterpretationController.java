package org.hisp.dhis.api.controller;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.chart.Chart;
import org.hisp.dhis.chart.ChartService;
import org.hisp.dhis.common.Pager;
import org.hisp.dhis.interpretation.Interpretation;
import org.hisp.dhis.interpretation.InterpretationService;
import org.hisp.dhis.mapping.MapView;
import org.hisp.dhis.mapping.MappingService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lars Helge Overland
 */
@Controller
@RequestMapping( value = InterpretationController.RESOURCE_PATH )
public class InterpretationController
    extends AbstractCrudController<Interpretation>
{
    public static final String RESOURCE_PATH = "/interpretations";

    @Autowired
    private InterpretationService interpretationService;

    @Autowired
    private ChartService chartService;
    
    @Autowired
    private ReportTableService reportTableService;
    
    @Autowired
    private OrganisationUnitService organisationUnitService;
    
    @Autowired
    private MappingService mappingService;
    
    @Override
    protected List<Interpretation> getEntityList( WebMetaData metaData, WebOptions options )
    {
        List<Interpretation> entityList;

        Date lastUpdated = options.getLastUpdated();

        if ( lastUpdated != null )
        {
            entityList = new ArrayList<Interpretation>( manager.getByLastUpdated( getEntityClass(), lastUpdated ) );
        }
        else if ( options.hasPaging() )
        {
            int count = manager.getCount( getEntityClass() );

            Pager pager = new Pager( options.getPage(), count );
            metaData.setPager( pager );

            entityList = new ArrayList<Interpretation>( interpretationService.getInterpretations( pager.getOffset(), pager.getPageSize() ) );

        }
        else
        {
            entityList = new ArrayList<Interpretation>( manager.getAll( getEntityClass() ) );
        }

        return entityList;
    }

    @RequestMapping( value = "/chart/{uid}", method = RequestMethod.POST, consumes = { "text/html", "text/plain" } )
    public void shareChartInterpretation( 
        @PathVariable( "uid" ) String chartUid, 
        @RequestBody String text, HttpServletResponse response ) throws IOException
    {
        Chart chart = chartService.getChart( chartUid );
        
        if ( chart == null )
        {
            ContextUtils.conflictResponse( response, "Chart identifier not valid: " + chartUid );
            return;
        }
        
        Interpretation interpretation = new Interpretation( chart, text );
        
        interpretationService.saveInterpretation( interpretation );

        response.setStatus( HttpServletResponse.SC_CREATED );
        response.setHeader( "Location", InterpretationController.RESOURCE_PATH + "/" + interpretation.getUid() );
    }

    @RequestMapping( value = "/map/{uid}", method = RequestMethod.POST, consumes = { "text/html", "text/plain" } )
    public void shareMapInterpretation( 
        @PathVariable( "uid" ) String mapViewUid, 
        @RequestBody String text, HttpServletResponse response ) throws IOException
    {
        MapView mapView = mappingService.getMapView( mapViewUid );
        
        if ( mapView == null )
        {
            ContextUtils.conflictResponse( response, "Map view identifier not valid: " + mapViewUid );
            return;
        }
        
        Interpretation interpretation = new Interpretation( mapView, text );
        
        interpretationService.saveInterpretation( interpretation );

        response.setStatus( HttpServletResponse.SC_CREATED );
        response.setHeader( "Location", InterpretationController.RESOURCE_PATH + "/" + interpretation.getUid() );
    }

    @RequestMapping( value = "/reportTable/{uid}", method = RequestMethod.POST, consumes = { "text/html", "text/plain" } )
    public void shareReportTableInterpretation( 
        @PathVariable( "uid" ) String reportTableUid, 
        @RequestParam( value = "ou", required = false ) String orgUnitUid, 
        @RequestBody String text, HttpServletResponse response ) throws IOException
    {
        ReportTable reportTable = reportTableService.getReportTable( reportTableUid );
        
        if ( reportTable == null )
        {
            ContextUtils.conflictResponse( response, "Report table identifier not valid: " + reportTableUid );
            return;
        }
        
        OrganisationUnit orgUnit = null;
        
        if ( orgUnitUid != null )
        {
            orgUnit = organisationUnitService.getOrganisationUnit( orgUnitUid );
            
            if ( orgUnit == null )
            {
                ContextUtils.conflictResponse( response, "Organisation unit identifier not valid: " + orgUnitUid );
                return;
            }
        }
        
        Interpretation interpretation = new Interpretation( reportTable, orgUnit, text );
        
        interpretationService.saveInterpretation( interpretation );

        response.setStatus( HttpServletResponse.SC_CREATED );
        response.setHeader( "Location", InterpretationController.RESOURCE_PATH + "/" + interpretation.getUid() );
    }
    
    @RequestMapping( value = "/{uid}/comment", method = RequestMethod.POST, consumes = { "text/html", "text/plain" } )
    public void postComment( 
        @PathVariable( "uid" ) String uid, 
        @RequestBody String text, HttpServletResponse response ) throws IOException
    {
        interpretationService.addInterpretationComment( uid, text );

        response.setStatus( HttpServletResponse.SC_CREATED );
        response.setHeader( "Location", InterpretationController.RESOURCE_PATH + "/" + uid );
    }
}
