package org.hisp.dhis.mapping.action;

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

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.util.CodecUtils;
import org.hisp.dhis.util.ContextUtils;
import org.hisp.dhis.util.StreamActionSupport;

/**
 * @author Tran Thanh Tri
 * @version $Id$
 */

public class ExportImageAction
    extends StreamActionSupport
{
    private static final Log log = LogFactory.getLog( ExportImageAction.class );

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private String svg;

    public void setSvg( String svg )
    {
        this.svg = svg;
    }

    private String title;

    public void setTitle( String title )
    {
        this.title = title;
    }

    @Override
    protected String execute( HttpServletResponse response, OutputStream out )
        throws Exception
    {
        if ( svg == null )
        {
            log.info( "Error: SVG is empty" );
        }

        convertToPNG( new StringBuffer( this.svg ), out );

        return SUCCESS;
    }

    @Override
    protected String getContentType()
    {
        return ContextUtils.CONTENT_TYPE_PNG;
    }

    @Override
    protected String getFilename()
    {
        return "dhis2-gis_" + CodecUtils.filenameEncode( this.title ) + ".png";
    }

    public void convertToPNG( StringBuffer buffer, OutputStream out )
        throws TranscoderException, IOException
    {
        PNGTranscoder t = new PNGTranscoder();

        t.addTranscodingHint( PNGTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE );

        TranscoderInput input = new TranscoderInput( new StringReader( buffer.toString() ) );

        TranscoderOutput output = new TranscoderOutput( out );

        t.transcode( input, output );
    }

    @Override
    protected boolean disallowCache()
    {
        return true;
    }
}
