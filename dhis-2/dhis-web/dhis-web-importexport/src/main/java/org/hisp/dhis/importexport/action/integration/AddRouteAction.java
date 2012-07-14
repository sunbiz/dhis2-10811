package org.hisp.dhis.importexport.action.integration;

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

import com.opensymphony.xwork2.Action;
import java.io.File;
import java.io.FileInputStream;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.util.StreamUtils;

/**
 * @author Bob Jolliffe
 * @version $Id$
 */
public class AddRouteAction
    implements Action
{
    private static final Log log = LogFactory.getLog( AddRouteAction.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ModelCamelContext builtinCamelContext;

    public void setBuiltinCamelContext( CamelContext camelContext )
    {
        this.builtinCamelContext = (ModelCamelContext) camelContext;
    }

    private File file;

    public void setUpload( File file )
    {
        this.file = file;
    }

    private String fileName;

    public void setUploadFileName( String fileName )
    {
        this.fileName = fileName;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        log.info( "Uploaded " + fileName );
        
        if ( file != null )
        {
            FileInputStream is = new FileInputStream( file );
            
            try
            {
                RoutesDefinition routes = builtinCamelContext.loadRoutesDefinition( is );
                for (RouteDefinition route : routes.getRoutes() ) 
                {
                    // remove any existing route with this id before adding ...
                    RouteDefinition existingRoute = builtinCamelContext.getRouteDefinition( route.getId() );
                    if (existingRoute != null) 
                    {
                        builtinCamelContext.stopRoute( route.getId());
                        builtinCamelContext.removeRouteDefinition( route );
                    }
                    builtinCamelContext.addRouteDefinitions( routes.getRoutes() );
                }
            }
            catch ( Exception e )
            {
                log.info( "Unable to load route: " + e.getMessage() );
                return ERROR;
            }
            finally
            {
                StreamUtils.closeInputStream( is );
            }
        }
        
        return SUCCESS;
    }
}
