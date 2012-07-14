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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.api.controller.WebMetaData;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.Pager;
import org.hisp.dhis.system.util.ReflectionUtils;

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
                        log.warn( "baseType already set, overwriting" );
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
            String basePath = ContextUtils.getPath( baseType );
            Pager pager = metaData.getPager();

            if ( pager.getPage() < pager.getPageCount() )
            {
                String nextPath = basePath + "?page=" + ( pager.getPage() + 1 );
                nextPath += pager.pageSizeIsDefault() ? "" : "&pageSize=" + pager.getPageSize();
                
                pager.setNextPage( nextPath );
            }

            if ( pager.getPage() > 1 )
            {
                if ( (pager.getPage() - 1) == 1 )
                {
                    String prevPath = pager.pageSizeIsDefault() ? basePath : basePath + "?pageSize=" + pager.getPageSize();
                    pager.setPrevPage( prevPath );
                }
                else
                {
                    String prevPath = basePath + "?page=" + ( pager.getPage() - 1 );
                    prevPath += pager.pageSizeIsDefault() ? "" : "&pageSize=" + pager.getPageSize();
                    
                    pager.setPrevPage( prevPath );
                }
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    public static void generateLinks( Object object )
    {
        if ( IdentifiableObject.class.isAssignableFrom( object.getClass() ) )
        {
            IdentifiableObject identifiableObject = (IdentifiableObject) object;
            identifiableObject.setHref( ContextUtils.getPathWithUid( identifiableObject ) );
        }

        Collection<Field> fields = ReflectionUtils.collectFields( object.getClass(), alwaysTrue );

        for ( Field field : fields )
        {
            if ( IdentifiableObject.class.isAssignableFrom( field.getType() ) )
            {
                Object fieldObject = ReflectionUtils.getFieldObject( field, object );

                if ( fieldObject != null )
                {
                    IdentifiableObject idObject = (IdentifiableObject) fieldObject;
                    idObject.setHref( ContextUtils.getPathWithUid( idObject ) );
                }
            }
            else if ( ReflectionUtils.isCollection( field.getName(), object, IdentifiableObject.class ) )
            {
                Object collection = ReflectionUtils.getFieldObject( field, object );

                if ( collection != null )
                {
                    Collection<IdentifiableObject> collectionObjects = (Collection<IdentifiableObject>) collection;

                    for ( IdentifiableObject collectionObject : collectionObjects )
                    {
                        if ( collectionObject != null )
                        {
                            collectionObject.setHref( ContextUtils.getPathWithUid( collectionObject ) );
                        }
                    }
                }
            }
        }
    }
}
