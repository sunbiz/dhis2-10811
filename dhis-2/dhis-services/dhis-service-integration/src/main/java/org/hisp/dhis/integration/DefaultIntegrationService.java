package org.hisp.dhis.integration;

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
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.hisp.dhis.dxf2.metadata.ImportOptions;
import org.hisp.dhis.integration.routes.SDMXDataIn;
import org.hisp.dhis.integration.routes.XMLDataIn;
import org.hisp.dhis.scheduling.TaskId;

/**
 * @author bobj
 */
public class DefaultIntegrationService 
    implements IntegrationService
{    
    @EndpointInject(uri = XMLDataIn.XMLDATA_IN)
    private ProducerTemplate xmlIn;

    @EndpointInject(uri = SDMXDataIn.SDMXDATA_IN)
    private ProducerTemplate sdmxIn;

    @Override
    public ImportSummary importXMLDataValueSet( InputStream in, ImportOptions importOptions )
    {
       return (ImportSummary) xmlIn.requestBodyAndHeader( in, IMPORT_OPTIONS_HDR, importOptions);
    }

    @Override
    public ImportSummary importSDMXDataValueSet( InputStream in, ImportOptions importOptions )
    {
       return (ImportSummary) sdmxIn.requestBodyAndHeader( in, IMPORT_OPTIONS_HDR, importOptions);
    }

    @Override
    public ImportSummary importXMLDataValueSet( InputStream in, ImportOptions options, TaskId taskId )
    {
        Map<String,Object> headers = new HashMap<String,Object>();
        headers.put( TASK_ID_HDR, taskId );
        headers.put( IMPORT_OPTIONS_HDR, options);
        
       return (ImportSummary) xmlIn.requestBodyAndHeaders( in, headers);
    }

    @Override
    public ImportSummary importSDMXDataValueSet( InputStream in, ImportOptions options, TaskId taskId )
    {
        Map<String,Object> headers = new HashMap<String,Object>();
        headers.put( TASK_ID_HDR, taskId );
        headers.put( IMPORT_OPTIONS_HDR, options);
        
       return (ImportSummary) sdmxIn.requestBodyAndHeaders( in, headers);
    }
}
