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

import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.mapping.export.SVGDocument;
import org.hisp.dhis.mapping.export.SVGUtils;
import org.hisp.dhis.system.util.CodecUtils;
import org.hisp.dhis.util.ContextUtils;
import org.hisp.dhis.util.SessionUtils;
import org.hisp.dhis.util.StreamActionSupport;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Tran Thanh Tri
 * @version $Id$
 */

public class ExportImageAction
    extends StreamActionSupport
{
    private static final Log log = LogFactory.getLog( ExportImageAction.class );

    private static final String SVGDOCUMENT = "SVGDOCUMENT";

    // -------------------------------------------------------------------------
    // Output & input
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

    private Integer layer;

    public void setLayer( Integer layer )
    {
        this.layer = layer;
    }
    
    private Integer imageLegendRows;

    public void setImageLegendRows( Integer imageLegendRows )
    {
        this.imageLegendRows = imageLegendRows;
    }

    private String indicator;

    public void setIndicator( String indicator )
    {
        this.indicator = indicator;
    }
    
    private String indicator2;

    public void setIndicator2( String indicator2 )
    {
        this.indicator2 = indicator2;
    }

    private String period;

    public void setPeriod( String period )
    {
        this.period = period;
    }

    private String period2;

    public void setPeriod2( String period2 )
    {
        this.period2 = period2;
    }

    private String legends;

    public void setLegends( String legends )
    {
        this.legends = legends;
    }

    private String legends2;

    public void setLegends2( String legends2 )
    {
        this.legends2 = legends2;
    }

    private boolean includeLegends;

    public void setIncludeLegends( boolean includeLegends )
    {
        this.includeLegends = includeLegends;
    }

    private SVGDocument svgDocument;

    @Override
    protected String execute( HttpServletResponse response, OutputStream out )
        throws Exception
    {
        if ( svg == null || title == null || indicator == null || period == null )
        {
            log.info( "Export map from session" );

            svgDocument = (SVGDocument) SessionUtils.getSessionVar( SVGDOCUMENT );
        }
        else
        {
            log.info( "Export map from request" );
            
            svgDocument = new SVGDocument();
            
            svgDocument.setTitle( this.title );
            svgDocument.setSvg( this.svg );
            svgDocument.setLayer( this.layer );
            svgDocument.setIndicator( this.indicator );
            svgDocument.setPeriod( this.period );
            svgDocument.setLegends( this.legends );
            svgDocument.setIncludeLegends( this.includeLegends );
            
            if ( this.layer == 3 )
            {
                svgDocument.setImageLegendRows( this.imageLegendRows );
                svgDocument.setPeriod2( this.period2 );
                svgDocument.setIndicator2( this.indicator2 );
                svgDocument.setLegends2( this.legends2 );
            }
            
            SessionUtils.setSessionVar( SVGDOCUMENT, svgDocument );
        }
        
        SVGUtils.convertToPNG( svgDocument.getSVGForImage(), out );

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
        return "dhis2_gis_" + CodecUtils.filenameEncode( this.title ) + ".png";
    }
    
    @Override
    protected boolean disallowCache()
    {
        return true;
    }
}
