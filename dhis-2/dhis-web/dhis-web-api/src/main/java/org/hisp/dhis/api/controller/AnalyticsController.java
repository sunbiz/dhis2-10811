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
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.hisp.dhis.analytics.AggregationType;
import org.hisp.dhis.analytics.AnalyticsService;
import org.hisp.dhis.analytics.DataQueryParams;
import org.hisp.dhis.analytics.IllegalQueryException;
import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.api.utils.ContextUtils.CacheStrategy;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.i18n.I18nManager;
import org.hisp.dhis.system.grid.GridUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnalyticsController
{
    private static final String RESOURCE_PATH = "/analytics";

    @Autowired
    private AnalyticsService analyticsService;
    
    @Autowired
    private ContextUtils contextUtils;
    
    @Autowired
    private I18nManager i18nManager;
    
    // -------------------------------------------------------------------------
    // Resources
    // -------------------------------------------------------------------------
  
    @RequestMapping( value = RESOURCE_PATH, method = RequestMethod.GET, produces = { "application/json", "application/javascript" } )
    public String getJson( // JSON, JSONP
        @RequestParam Set<String> dimension,
        @RequestParam(required = false) Set<String> filter,
        @RequestParam(required = false) AggregationType aggregationType,
        @RequestParam(required = false) String measureCriteria,
        Model model,
        HttpServletResponse response ) throws Exception
    {
        DataQueryParams params = analyticsService.getFromUrl( dimension, filter, aggregationType, measureCriteria, i18nManager.getI18nFormat() );

        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_JSON, CacheStrategy.RESPECT_SYSTEM_SETTING );
        Grid grid = analyticsService.getAggregatedDataValues( params );        
        model.addAttribute( "model", grid );
        model.addAttribute( "viewClass", "detailed" );
        return "grid";
    }

    @RequestMapping( value = RESOURCE_PATH + ".xml", method = RequestMethod.GET )
    public void getXml( 
        @RequestParam Set<String> dimension,
        @RequestParam(required = false) Set<String> filter,
        @RequestParam(required = false) AggregationType aggregationType,
        @RequestParam(required = false) String measureCriteria,
        Model model,
        HttpServletResponse response ) throws Exception
    {
        DataQueryParams params = analyticsService.getFromUrl( dimension, filter, aggregationType, measureCriteria, i18nManager.getI18nFormat() );

        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_XML, CacheStrategy.RESPECT_SYSTEM_SETTING );
        Grid grid = analyticsService.getAggregatedDataValues( params );
        GridUtils.toXml( grid, response.getOutputStream() );
    }

    @RequestMapping( value = RESOURCE_PATH + ".html", method = RequestMethod.GET )
    public void getHtml( 
        @RequestParam Set<String> dimension,
        @RequestParam(required = false) Set<String> filter,
        @RequestParam(required = false) AggregationType aggregationType,
        @RequestParam(required = false) String measureCriteria,
        Model model,
        HttpServletResponse response ) throws Exception
    {
        DataQueryParams params = analyticsService.getFromUrl( dimension, filter, aggregationType, measureCriteria, i18nManager.getI18nFormat() );

        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_HTML, CacheStrategy.RESPECT_SYSTEM_SETTING );
        Grid grid = analyticsService.getAggregatedDataValues( params );
        GridUtils.toHtml( grid.substituteMetaData(), response.getWriter() );
    }

    @RequestMapping( value = RESOURCE_PATH + ".csv", method = RequestMethod.GET )
    public void getCsv( 
        @RequestParam Set<String> dimension,
        @RequestParam(required = false) Set<String> filter,
        @RequestParam(required = false) AggregationType aggregationType,
        @RequestParam(required = false) String measureCriteria,
        Model model,
        HttpServletResponse response ) throws Exception
    {
        DataQueryParams params = analyticsService.getFromUrl( dimension, filter, aggregationType, measureCriteria, i18nManager.getI18nFormat() );

        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_CSV, CacheStrategy.RESPECT_SYSTEM_SETTING, "data.csv", true );
        Grid grid = analyticsService.getAggregatedDataValues( params );
        GridUtils.toCsv( grid.substituteMetaData(), response.getOutputStream() );
    }
    
    @RequestMapping( value = RESOURCE_PATH + ".xls", method = RequestMethod.GET )
    public void getXls( 
        @RequestParam Set<String> dimension,
        @RequestParam(required = false) Set<String> filter,
        @RequestParam(required = false) AggregationType aggregationType,
        @RequestParam(required = false) String measureCriteria,
        Model model,
        HttpServletResponse response ) throws Exception
    {
        DataQueryParams params = analyticsService.getFromUrl( dimension, filter, aggregationType, measureCriteria, i18nManager.getI18nFormat() );

        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_EXCEL, CacheStrategy.RESPECT_SYSTEM_SETTING, "data.xls", true );
        Grid grid = analyticsService.getAggregatedDataValues( params );
        GridUtils.toXls( grid.substituteMetaData(), response.getOutputStream() );
    }

    // -------------------------------------------------------------------------
    // Exception handling
    // -------------------------------------------------------------------------
  
    @ExceptionHandler(IllegalQueryException.class)
    public void handleError( IllegalQueryException ex, HttpServletResponse response )
        throws IOException
    {
        ContextUtils.conflictResponse( response, ex.getMessage() );
    }
}