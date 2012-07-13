package org.hisp.dhis.dashboard.provider;

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

import static org.hisp.dhis.system.util.MathUtils.getMax;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.amplecode.staxwax.factory.XMLFactory;
import org.amplecode.staxwax.reader.XMLReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dashboard.RssItem;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class RssContentProvider
    implements ContentProvider
{
    private static final Log log = LogFactory.getLog( RssContentProvider.class );
    
    private static final String COLLECTION_NAME = "channel";
    private static final String ITEM_NAME = "item";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_LINK = "link";
    private static final String FIELD_DATE = "pubDate";
    private static final String FIELD_DESCRIPTION = "description";
    
    private static final int MAX_ITEMS = 4;
    
    private String url;
        
    public void setUrl( String url )
    {
        this.url = url;
    }
    
    private String key;
    
    public void setKey( String key )
    {
        this.key = key;
    }

    // -------------------------------------------------------------------------
    // ContentProvider implementation
    // -------------------------------------------------------------------------

    public Map<String, Object> provide()
    {
        Map<String, Object> content = new HashMap<String, Object>();
        
        content.put( key, getRss() );
        
        return content;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private List<RssItem> getRss()
    {
        try
        {
            Resource resource = new UrlResource( url );
            
            XMLReader reader = XMLFactory.getXMLReader( resource.getInputStream() );
            
            List<RssItem> items = new ArrayList<RssItem>();
            
            while ( reader.moveToStartElement( ITEM_NAME, COLLECTION_NAME ) )
            {
                Map<String, String> data = reader.readElements( ITEM_NAME );
                
                RssItem item = new RssItem();
                
                item.setTitle( data.get( FIELD_TITLE ) );
                item.setLink( data.get( FIELD_LINK ) );
                item.setDate( data.get( FIELD_DATE ) );
                item.setDescription( data.get( FIELD_DESCRIPTION ) );
                
                items.add( item );
            }
            
            return items.subList( 0, getMax( items.size(), MAX_ITEMS ) );            
        }
        catch ( MalformedURLException ex )
        {
            throw new RuntimeException( "Malformed RSS URL", ex );
        }
        catch ( Exception ex )
        {
            log.error( "Error while reading RSS, you are probably not connected to the internet", ex );
            
            return new ArrayList<RssItem>();
        }
    }
}
