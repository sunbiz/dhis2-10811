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

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.hisp.dhis.dxf2.metadata.ImportOptions;
import org.hisp.dhis.dxf2.metadata.ImportService;

/**
 * metadataendpoint is created using a uri of the form:
 * dhis2:metadata?dryRun=true|false
 *
 * @author bobj
 */
public class Dxf2MetaDataEndpoint
    extends DefaultEndpoint
{
    protected ImportService importService;

    public ImportService getImportService()
    {
        return importService;
    }

    protected ImportOptions options;

    public ImportOptions getImportOptions()
    {
        return this.options;
    }

    // -------------------------------------------------------------------------
    // Parameters supported by this end point 
    // -------------------------------------------------------------------------

    public void setDryRun( Boolean dryRun )
    {
        options.setDryRun( dryRun );
    }

    public void setStrategy( String strategy )
    {
        options.setStrategy( strategy );
    }

    public Dxf2MetaDataEndpoint( String uri, Dxf2Component component, ImportService importService )
    {
        super( uri, component );
        this.importService = importService;
        options = new ImportOptions();
    }

    @Override
    public Producer createProducer() throws Exception
    {
        return new Dxf2MetaDataProducer( this );
    }

    @Override
    public Consumer createConsumer( Processor prcsr ) throws Exception
    {
        throw new UnsupportedOperationException(
            "You can't consume messages from this endpoint: " + getEndpointUri() );
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
