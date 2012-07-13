package org.hisp.dhis.api.view;

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

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.hisp.dhis.dxf2.utils.JacksonUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class JacksonJsonView
    extends AbstractView
{
    private static String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    private static String CONTENT_TYPE_APPLICATION_JAVASCRIPT = "application/javascript";

    private boolean withPadding = false;

    private String callbackParameter = "callback";

    private String paddingFunction = "callback";

    public JacksonJsonView()
    {
        setContentType( CONTENT_TYPE_APPLICATION_JSON );
    }

    public JacksonJsonView( boolean withPadding )
    {
        this.withPadding = withPadding;

        if ( !withPadding )
        {
            setContentType( CONTENT_TYPE_APPLICATION_JSON );
        }
        else
        {
            setContentType( CONTENT_TYPE_APPLICATION_JAVASCRIPT );
        }
    }

    public void setCallbackParameter( String callbackParameter )
    {
        this.callbackParameter = callbackParameter;
    }

    public void setPaddingFunction( String paddingFunction )
    {
        this.paddingFunction = paddingFunction;
    }

    @Override
    protected void renderMergedOutputModel( Map<String, Object> model, HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        Object object = model.get( "model" );
        Class<?> viewClass = JacksonUtils.getViewClass( model.get( "viewClass" ) );
        response.setContentType( getContentType() );

        if ( withPadding )
        {
            String callback = request.getParameter( callbackParameter );

            if ( callback == null || callback.length() == 0 )
            {
                callback = paddingFunction;
            }

            object = new JSONPObject( callback, object );
        }

        if ( viewClass != null )
        {
            JacksonUtils.toJsonWithView( response.getOutputStream(), object, viewClass );
        }
        else
        {
            JacksonUtils.toJson( response.getOutputStream(), object );
        }
    }
}
