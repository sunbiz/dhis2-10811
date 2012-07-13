package org.hisp.dhis.help;

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

import static org.hisp.dhis.system.util.StreamUtils.ENCODING_UTF;

import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.hisp.dhis.help.HelpManager;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Lars Helge Overland
 */
public class DefaultHelpManager
    implements HelpManager
{
    // -------------------------------------------------------------------------
    // HelpManager implementation
    // -------------------------------------------------------------------------

    public void getHelpContent( OutputStream out, String id )
    {
        try
        {
            Source source = new StreamSource( new ClassPathResource( "help_content.xml" ).getInputStream(), ENCODING_UTF );
            
            Result result = new StreamResult( out );
            
            Transformer transformer = getTransformer( "help_stylesheet.xsl" );
            
            transformer.setParameter( "sectionId", id );
            
            transformer.transform( source, result );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to get help content", ex );
        }
    }
    
    public void getHelpItems( OutputStream out )
    {
        try
        {
            Source source = new StreamSource( new ClassPathResource( "help_content.xml" ).getInputStream(), ENCODING_UTF );
            
            Result result = new StreamResult( out );
            
            getTransformer( "helpitems_stylesheet.xsl" ).transform( source, result );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to get help content", ex );
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Transformer getTransformer( String stylesheetName )
        throws Exception
    {
        Source stylesheet = new StreamSource( new ClassPathResource( stylesheetName ).getInputStream(), ENCODING_UTF );
        
        return TransformerFactory.newInstance().newTransformer( stylesheet );
    }
}
