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

import static org.hisp.dhis.api.utils.ContextUtils.CONTENT_TYPE_XML;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.api.webdomain.DataValueSets;
import org.hisp.dhis.dxf2.datavalueset.DataValueSet;
import org.hisp.dhis.dxf2.datavalueset.DataValueSetService;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.hisp.dhis.dxf2.metadata.ImportOptions;
import org.hisp.dhis.dxf2.utils.JacksonUtils;
import org.hisp.dhis.integration.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping( value = DataValueSetController.RESOURCE_PATH )
public class DataValueSetController
{
    public static final String RESOURCE_PATH = "/dataValueSets";

    private static final Log log = LogFactory.getLog( DataValueSetController.class );
    
    @Autowired
    private DataValueSetService dataValueSetService;

    @Autowired
    private IntegrationService integrationService;
    
    @RequestMapping( method = RequestMethod.GET, produces = { "text/html", "text/plain" } )
    public String getDataValueSets( Model model ) throws Exception
    {
        DataValueSets dataValueSets = new DataValueSets();
        dataValueSets.getDataValueSets().add( new DataValueSet() );
        
        model.addAttribute( "model", dataValueSets );

        return "dataValueSets";
    }

    @RequestMapping( method = RequestMethod.GET, produces = "application/xml" )
    public void getDataValueSet( @RequestParam String dataSet,
                                 @RequestParam String period,
                                 @RequestParam String orgUnit,
                                 HttpServletResponse response ) throws IOException
    {
        log.info( "Get data value set for data set: " + dataSet + ", period: " + period + ", org unit: " + orgUnit );

        response.setContentType( CONTENT_TYPE_XML );
        dataValueSetService.writeDataValueSet( dataSet, period, orgUnit, response.getOutputStream() );
    }

    @RequestMapping( method = RequestMethod.POST, consumes = "application/xml" )
    @PreAuthorize( "hasRole('ALL') or hasRole('F_DATAVALUE_ADD')" )
    public void postDxf2DataValueSet( ImportOptions importOptions,
                                  HttpServletResponse response, 
                                  InputStream in,
                                  Model model ) throws IOException
    {
        ImportSummary summary = dataValueSetService.saveDataValueSet( in, importOptions );

        log.info( "Data values set saved " + importOptions );    

        response.setContentType( CONTENT_TYPE_XML );        
        JacksonUtils.toXml( response.getOutputStream(), summary );
    }
    
    @RequestMapping( method = RequestMethod.POST, consumes = "application/sdmx+xml" )
    @PreAuthorize( "hasRole('ALL') or hasRole('F_DATAVALUE_ADD')" )
    public void postSDMXDataValueSet( ImportOptions importOptions,
                                  HttpServletResponse response, 
                                  InputStream in,
                                  Model model ) throws IOException
    {
        ImportSummary summary = integrationService.importSDMXDataValueSet( in, importOptions );
        
        log.info( "Data values set saved " + importOptions );    

        response.setContentType( CONTENT_TYPE_XML );        
        JacksonUtils.toXml( response.getOutputStream(), summary );
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleError( IllegalArgumentException ex, HttpServletResponse response )
        throws IOException
    {
        ContextUtils.conflictResponse( response, ex.getMessage() );
    }
}
                                                