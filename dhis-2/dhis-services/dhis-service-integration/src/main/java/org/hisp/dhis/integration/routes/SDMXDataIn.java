package org.hisp.dhis.integration.routes;

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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.DescriptionDefinition;

/**
 * SDMXDataIn route takes an SDMX cross-sectional message, transforms to dxf2 datavalueset
 * and sends to dxf2 endpoint
 * 
 * @author bobj
 */
public class SDMXDataIn 
    extends RouteBuilder
{ 
    // DataInput endpoint

    public static final String SDMXDATA_IN = "direct:sdmxDataIn";
    
    // Route description texts
    
    public static final String SDMXDATA_IN_DESC = "Internal: SDMX Data to DXF2 Input";
 
    
    @Override
    public void configure() throws Exception
    {
        DescriptionDefinition desc = new DescriptionDefinition();
        desc.setText( "SDMX Data to DXF2 Input");
        
        from(SDMXDATA_IN).
            convertBodyTo( java.lang.String.class, "UTF-8" ).to( "log:org.hisp.dhis.integration?level=INFO").
            to("xslt:transform/cross2dxf2.xsl").convertBodyTo( java.io.InputStream.class).
            inOut("dhis2:data?orgUnitIdScheme=CODE&dataElementIdScheme=CODE&importStrategy=NEW_AND_UPDATES").
            setDescription( desc );
    }    
}
