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
import org.hisp.dhis.dxf2.datavalueset.DataValueSetService;
import org.hisp.dhis.dxf2.metadata.ImportOptions;
import org.hisp.dhis.importexport.ImportStrategy;

/**
 * dataendpoint is created using a uri of the form:
 * dhis2:metadata?dryRun=true|false&importStrategy=NEW|NEW_AND_UPDATES|UPDATES
 * 
 * @author bobj
 */
public class Dxf2DataEndpoint 
    extends DefaultEndpoint
{
    protected DataValueSetService dataValueSetService;

    public DataValueSetService getDataValueSetService()
    {
        return dataValueSetService;
    }
    
    // -------------------------------------------------------------------------
    // Parameters supported by this endpoint 
    // -------------------------------------------------------------------------
    
    protected ImportOptions importOptions;
    
    public ImportOptions getImportOptions()
    {
        return importOptions;
    }

    public void setImportStrategy( ImportStrategy importStrategy )
    {
        importOptions.setImportStrategy(importStrategy.toString());
    }
    
    public void setDryRun( Boolean dryRun )
    {
        importOptions.setDryRun( dryRun );
    }

    public void setDataElementIdScheme( String dataElementIdScheme )
    {
        importOptions.setDataElementIdScheme( dataElementIdScheme );
    }

    public void setOrgUnitIdScheme( String orgUnitIdScheme )
    {
        importOptions.setOrgUnitIdScheme( orgUnitIdScheme );
    }
    
    public Dxf2DataEndpoint( String uri, Dxf2Component component, DataValueSetService dataValueSetService )
    {
        super( uri, component );
        importOptions = new ImportOptions();
        this.dataValueSetService = dataValueSetService;
    }

    @Override
    public Producer createProducer() throws Exception
    {
        return new Dxf2DataProducer( this );
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
