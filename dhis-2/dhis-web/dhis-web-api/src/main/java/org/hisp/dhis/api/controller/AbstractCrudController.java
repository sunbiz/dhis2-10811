package org.hisp.dhis.api.controller;

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

import org.hisp.dhis.api.utils.WebUtils;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.common.Pager;
import org.hisp.dhis.dxf2.metadata.ExchangeClasses;
import org.hisp.dhis.system.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public abstract class AbstractCrudController<T extends IdentifiableObject>
{
    //--------------------------------------------------------------------------
    // Dependencies
    //--------------------------------------------------------------------------

    @Autowired
    protected IdentifiableObjectManager manager;

    //--------------------------------------------------------------------------
    // GET
    //--------------------------------------------------------------------------

    @RequestMapping( method = RequestMethod.GET )
    public String getObjectList( @RequestParam Map<String, String> parameters, Model model, HttpServletRequest request ) throws Exception
    {
        WebOptions options = new WebOptions( parameters );
        WebMetaData metaData = new WebMetaData();
        List<T> entityList = getEntityList( metaData, options );

        ReflectionUtils.invokeSetterMethod( ExchangeClasses.getExportMap().get( getEntityClass() ), metaData, entityList );

        if ( options.hasLinks() )
        {
            WebUtils.generateLinks( metaData );
        }

        model.addAttribute( "model", metaData );
        model.addAttribute( "viewClass", options.getViewClass( "basic" ) );

        return StringUtils.uncapitalize( getEntitySimpleName() ) + "List";
    }

    @RequestMapping( value = "/{uid}", method = RequestMethod.GET )
    public String getObject( @PathVariable( "uid" ) String uid, @RequestParam Map<String, String> parameters, Model model, HttpServletRequest request ) throws Exception
    {
        WebOptions options = new WebOptions( parameters );
        T entity = getEntity( uid );

        if ( options.hasLinks() )
        {
            WebUtils.generateLinks( entity );
        }

        model.addAttribute( "model", entity );
        model.addAttribute( "viewClass", options.getViewClass( "detailed" ) );

        return StringUtils.uncapitalize( getEntitySimpleName() );
    }

    // FIXME proper error handling?
    @RequestMapping( value = "/search/{query}", method = RequestMethod.GET )
    public String search( @PathVariable String query, @RequestParam Map<String, String> parameters, Model model, HttpServletRequest request ) throws Exception
    {
        WebOptions options = new WebOptions( parameters );
        T entity = manager.search( getEntityClass(), query );

        if ( options.hasLinks() )
        {
            WebUtils.generateLinks( entity );
        }

        model.addAttribute( "model", entity );
        model.addAttribute( "viewClass", "detailed" );

        return StringUtils.uncapitalize( getEntitySimpleName() );
    }

    //--------------------------------------------------------------------------
    // POST
    //--------------------------------------------------------------------------

    @RequestMapping( method = RequestMethod.POST, consumes = { "application/xml", "text/xml" } )
    @PreAuthorize( "hasRole('ALL')" )
    public void postXmlObject( HttpServletResponse response, HttpServletRequest request, InputStream input ) throws Exception
    {
        throw new HttpRequestMethodNotSupportedException( RequestMethod.POST.toString() );
    }

    @RequestMapping( method = RequestMethod.POST, consumes = "application/json" )
    @PreAuthorize( "hasRole('ALL')" )
    public void postJsonObject( HttpServletResponse response, InputStream input ) throws Exception
    {
        throw new HttpRequestMethodNotSupportedException( RequestMethod.POST.toString() );
    }

    //--------------------------------------------------------------------------
    // PUT
    //--------------------------------------------------------------------------

    @RequestMapping( value = "/{uid}", method = RequestMethod.PUT, consumes = { "application/xml", "text/xml" } )
    @ResponseStatus( value = HttpStatus.NO_CONTENT )
    @PreAuthorize( "hasRole('ALL')" )
    public void putXmlObject( @PathVariable( "uid" ) String uid, InputStream input ) throws Exception
    {
        throw new HttpRequestMethodNotSupportedException( RequestMethod.PUT.toString() );
    }

    @RequestMapping( value = "/{uid}", method = RequestMethod.PUT, consumes = "application/json" )
    @ResponseStatus( value = HttpStatus.NO_CONTENT )
    @PreAuthorize( "hasRole('ALL')" )
    public void putJsonObject( @PathVariable( "uid" ) String uid, InputStream input ) throws Exception
    {
        throw new HttpRequestMethodNotSupportedException( RequestMethod.PUT.toString() );
    }

    //--------------------------------------------------------------------------
    // DELETE
    //--------------------------------------------------------------------------

    @RequestMapping( value = "/{uid}", method = RequestMethod.DELETE )
    @ResponseStatus( value = HttpStatus.NO_CONTENT )
    @PreAuthorize( "hasRole('ALL')" )
    public void deleteObject( @PathVariable( "uid" ) String uid ) throws Exception
    {
        throw new HttpRequestMethodNotSupportedException( RequestMethod.DELETE.toString() );
    }

    //--------------------------------------------------------------------------
    // Helpers
    //--------------------------------------------------------------------------

    protected List<T> getEntityList( WebMetaData metaData, WebOptions options )
    {
        List<T> entityList;

        Date lastUpdated = options.getLastUpdated();

        if ( lastUpdated != null )
        {
            entityList = new ArrayList<T>( manager.getByLastUpdatedSorted( getEntityClass(), lastUpdated ) );
        }
        else if ( options.hasPaging() )
        {
            int count = manager.getCount( getEntityClass() );

            Pager pager = new Pager( options.getPage(), count );
            metaData.setPager( pager );

            entityList = new ArrayList<T>( manager.getBetween( getEntityClass(), pager.getOffset(), pager.getPageSize() ) );
        }
        else
        {
            entityList = new ArrayList<T>( manager.getAllSorted( getEntityClass() ) );
        }

        return entityList;
    }

    protected T getEntity( String uid )
    {
        return manager.get( getEntityClass(), uid );
    }

    //--------------------------------------------------------------------------
    // Reflection helpers
    //--------------------------------------------------------------------------

    private Class<T> entityClass;

    private String entityName;

    private String entitySimpleName;

    @SuppressWarnings( "unchecked" )
    protected Class<T> getEntityClass()
    {
        if ( entityClass == null )
        {
            Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
            entityClass = (Class<T>) actualTypeArguments[0];
        }

        return entityClass;
    }

    protected String getEntityName()
    {
        if ( entityName == null )
        {
            entityName = getEntityClass().getName();
        }

        return entityName;
    }

    protected String getEntitySimpleName()
    {
        if ( entitySimpleName == null )
        {
            entitySimpleName = getEntityClass().getSimpleName();
        }

        return entitySimpleName;
    }

    @SuppressWarnings( "unchecked" )
    protected T getEntityInstance()
    {
        try
        {
            return (T) Class.forName( getEntityName() ).newInstance();
        } catch ( InstantiationException e )
        {
            throw new RuntimeException( e );
        } catch ( IllegalAccessException e )
        {
            throw new RuntimeException( e );
        } catch ( ClassNotFoundException e )
        {
            throw new RuntimeException( e );
        }
    }
}
