package org.hisp.dhis.api.utils;

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

import javassist.util.proxy.ProxyObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.api.controller.WebMetaData;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.Pager;
import org.hisp.dhis.dxf2.metadata.ExchangeClasses;
import org.hisp.dhis.system.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hisp.dhis.system.util.PredicateUtils.alwaysTrue;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class WebUtils
{
    private static final Log log = LogFactory.getLog( WebUtils.class );

    @SuppressWarnings( "unchecked" )
    public static void generateLinks( WebMetaData metaData )
    {
        Class<?> baseType = null;
        Collection<Field> fields = ReflectionUtils.collectFields( metaData.getClass(), alwaysTrue );

        for ( Field field : fields )
        {
            if ( ReflectionUtils.isCollection( field.getName(), metaData, IdentifiableObject.class ) )
            {
                List<IdentifiableObject> objects = new ArrayList<IdentifiableObject>( (Collection<IdentifiableObject>) ReflectionUtils.getFieldObject( field, metaData ) );

                if ( !objects.isEmpty() )
                {
                    if ( baseType != null )
                    {
                        log.warn( "baseType already set, overwriting.." );
                    }

                    baseType = objects.get( 0 ).getClass();

                    for ( IdentifiableObject object : objects )
                    {
                        generateLinks( object );
                    }
                }
            }
        }

        if ( metaData.getPager() != null && baseType != null )
        {
            String basePath = getPath( baseType );
            Pager pager = metaData.getPager();

            if ( pager.getPage() < pager.getPageCount() )
            {
                pager.setNextPage( basePath + "?page=" + (pager.getPage() + 1) );
            }

            if ( pager.getPage() > 1 )
            {
                if ( (pager.getPage() - 1) == 1 )
                {
                    pager.setPrevPage( basePath );
                }
                else
                {
                    pager.setPrevPage( basePath + "?page=" + (pager.getPage() - 1) );
                }

            }
        }
    }

    @SuppressWarnings( "unchecked" )
    public static void generateLinks( IdentifiableObject identifiableObject )
    {
        identifiableObject.setLink( getPathWithUid( identifiableObject ) );

        Collection<Field> fields = ReflectionUtils.collectFields( identifiableObject.getClass(), alwaysTrue );

        for ( Field field : fields )
        {
            if ( IdentifiableObject.class.isAssignableFrom( field.getType() ) )
            {
                Object object = ReflectionUtils.getFieldObject( field, identifiableObject );

                if ( object != null )
                {
                    IdentifiableObject idObject = (IdentifiableObject) object;
                    idObject.setLink( getPathWithUid( idObject ) );
                }
            }
            else if ( ReflectionUtils.isCollection( field.getName(), identifiableObject, IdentifiableObject.class ) )
            {
                Object collection = ReflectionUtils.getFieldObject( field, identifiableObject );

                if ( collection != null )
                {
                    Collection<IdentifiableObject> objects = (Collection<IdentifiableObject>) collection;

                    for ( IdentifiableObject object : objects )
                    {
                        if ( object != null )
                        {
                            object.setLink( getPathWithUid( object ) );
                        }
                    }
                }
            }
        }
    }

    public static HttpServletRequest getRequest()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static String getPathWithUid( IdentifiableObject identifiableObject )
    {
        return getPath( identifiableObject.getClass() ) + "/" + identifiableObject.getUid();
    }

    public static String getPath( Class<?> clazz )
    {
        if ( ProxyObject.class.isAssignableFrom( clazz ) )
        {
            clazz = clazz.getSuperclass();
        }

        String resourcePath = ExchangeClasses.getExportMap().get( clazz );

        return getRootPath( getRequest() ) + "/" + resourcePath;
    }

    private static String getRootPath( HttpServletRequest request )
    {
        StringBuilder builder = new StringBuilder();
        String xForwardedProto = request.getHeader( "X-Forwarded-Proto" );
        String xForwardedPort = request.getHeader( "X-Forwarded-Port" );

        if ( xForwardedProto != null && (xForwardedProto.equalsIgnoreCase( "http" ) || xForwardedProto.equalsIgnoreCase( "https" )) )
        {
            builder.append( xForwardedProto );
        }
        else
        {
            builder.append( request.getScheme() );
        }

        builder.append( "://" ).append( request.getServerName() );

        int port;

        try
        {
            port = Integer.parseInt( xForwardedPort );
        } catch ( NumberFormatException e )
        {
            port = request.getServerPort();
        }

        if ( port != 80 && port != 443 )
        {
            builder.append( ":" ).append( port );
        }

        builder.append( request.getContextPath() );
        builder.append( request.getServletPath() );

        return builder.toString();
    }

}
