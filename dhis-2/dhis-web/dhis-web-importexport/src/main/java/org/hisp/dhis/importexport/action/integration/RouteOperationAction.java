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
import org.apache.camel.CamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Bob Jolliffe
 * @version $Id$
 */
public class RouteOperationAction
    implements Action
{

    private static final Log log = LogFactory.getLog( RouteOperationAction.class );
    
    // -------------------------------------------------------------------------
    // Http Parameters
    // -------------------------------------------------------------------------
    
    private String id;
    
    public void setId( String id )
    {
        this.id = id;
    }

    public enum Operation { enable, disable ; }
    
    private Operation operation;

    public void setOperation( Operation operation )
    {
        this.operation = operation;
    }
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ModelCamelContext builtinCamelContext;

    public void setBuiltinCamelContext( CamelContext camelContext )
    {
        this.builtinCamelContext = (ModelCamelContext) camelContext;
    }

    public CamelContext getBuiltinCamelContext()
    {
        return builtinCamelContext;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        switch (operation) {
            case enable:
                enableRoute();
                break;
                
            case disable:
                disableRoute();
                break;
                
            default:
                log.debug( "Unsupported route operation");
                break;
        }
        
        return SUCCESS;
    }

    private void enableRoute()
    {
        try
        {
            builtinCamelContext.startRoute( id );
        } catch ( Exception ex )
        {
            log.info( "Route start exception: " + ex.getMessage());
        }
    }

    private void disableRoute()
    {
        try
        {
            builtinCamelContext.stopRoute( id );
        } catch ( Exception ex )
        {
            log.info( "Route stop exception: " + ex.getMessage());
        }
    }
}
