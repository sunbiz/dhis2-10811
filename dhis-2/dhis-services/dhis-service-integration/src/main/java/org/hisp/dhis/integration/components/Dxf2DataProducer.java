package org.hisp.dhis.integration.components;

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

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.hisp.dhis.dxf2.metadata.ImportOptions;
import org.hisp.dhis.dxf2.utils.JacksonUtils;
import org.hisp.dhis.integration.IntegrationService;
import org.hisp.dhis.scheduling.TaskId;

/**
 * @author bobj
 */
public class Dxf2DataProducer 
    extends DefaultProducer
{
    public Dxf2DataProducer( Dxf2DataEndpoint endpoint )
    {
        super( endpoint );
    }

    @Override
    public void process( Exchange exchange ) throws Exception
    {
        log.debug( this.getEndpoint().getEndpointUri() + " : " + exchange.getIn().getBody() );
        
        Dxf2DataEndpoint endpoint =  (Dxf2DataEndpoint) this.getEndpoint();
        
        // get importOptions off the message header or default to options set on the endpoint
        ImportOptions options = (ImportOptions) exchange.getIn().
            getHeader( IntegrationService.IMPORT_OPTIONS_HDR, endpoint.getImportOptions() );
        
        TaskId taskId = (TaskId) exchange.getIn().getHeader( IntegrationService.TASK_ID_HDR );
        
        ImportSummary summary = endpoint.getDataValueSetService().saveDataValueSet( (InputStream)exchange.getIn().getBody(), 
             options, taskId );
        
        exchange.getOut().setBody( summary );
        log.debug( this.getEndpoint().getEndpointUri() + " : " + JacksonUtils.toXmlAsString(exchange.getOut().getBody()) );
    }
}
