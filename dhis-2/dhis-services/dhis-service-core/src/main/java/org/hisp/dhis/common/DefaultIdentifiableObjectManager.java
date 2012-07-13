package org.hisp.dhis.common;

/*
 * Copyright (c) 2004-2005, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may
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
import org.hisp.dhis.common.IdentifiableObject.IdentifiableProperty;
import org.hisp.dhis.common.NameableObject.NameableProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author Lars Helge Overland
 */
@Transactional
public class DefaultIdentifiableObjectManager
    implements IdentifiableObjectManager
{
    private static final Log log = LogFactory.getLog( DefaultIdentifiableObjectManager.class );

    @Autowired
    private Set<GenericIdentifiableObjectStore<IdentifiableObject>> identifiableObjectStores;

    @Autowired
    private Set<GenericNameableObjectStore<NameableObject>> nameableObjectStores;

    private Map<Class<IdentifiableObject>, GenericIdentifiableObjectStore<IdentifiableObject>> identifiableObjectStoreMap;

    private Map<Class<NameableObject>, GenericNameableObjectStore<NameableObject>> nameableObjectStoreMap;

    @PostConstruct
    public void init()
    {
        identifiableObjectStoreMap = new HashMap<Class<IdentifiableObject>, GenericIdentifiableObjectStore<IdentifiableObject>>();

        for ( GenericIdentifiableObjectStore<IdentifiableObject> store : identifiableObjectStores )
        {
            identifiableObjectStoreMap.put( store.getClazz(), store );
        }

        nameableObjectStoreMap = new HashMap<Class<NameableObject>, GenericNameableObjectStore<NameableObject>>();

        for ( GenericNameableObjectStore<NameableObject> store : nameableObjectStores )
        {
            nameableObjectStoreMap.put( store.getClazz(), store );
        }
    }

    @Override
    public void save( IdentifiableObject object )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( object.getClass() );

        if ( store != null )
        {
            store.save( object );
        }
        else
        {
            log.warn( "No IdentifiableObject store found for " + object.getClass() + " (save)." );
        }
    }

    @Override
    public void update( IdentifiableObject object )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( object.getClass() );

        if ( store != null )
        {
            store.update( object );
        }
        else
        {
            log.warn( "No IdentifiableObject store found for " + object.getClass() + " (update)." );
        }
    }

    @Override
    public void delete( IdentifiableObject object )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( object.getClass() );

        if ( store != null )
        {
            store.delete( object );
        }
        else
        {
            log.warn( "No IdentifiableObject store found for " + object.getClass() + " (delete)." );
        }
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> T get( Class<T> clazz, String uid )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( clazz );

        if ( store == null )
        {
            log.warn( "No IdentifiableObject store found for " + clazz + ", returning null." );

            return null;
        }

        return (T) store.getByUid( uid );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> T getByCode( Class<T> clazz, String code )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( clazz );

        if ( store == null )
        {
            log.warn( "No IdentifiableObject store found for " + clazz + ", returning null (getByCode)." );

            return null;
        }

        return (T) store.getByCode( code );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> T getByName( Class<T> clazz, String name )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( clazz );

        if ( store == null )
        {
            log.warn( "No IdentifiableObject store found for " + clazz + ", returning null (getByName)." );

            return null;
        }

        return (T) store.getByName( name );
    }

    @Override
    public <T extends IdentifiableObject> T search( Class<T> clazz, String query )
    {
        T object = get( clazz, query );

        if ( object == null )
        {
            object = getByCode( clazz, query );
        }

        if ( object == null )
        {
            object = getByName( clazz, query );
        }

        return object;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> Collection<T> getAll( Class<T> clazz )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( clazz );

        if ( store == null )
        {
            log.warn( "No IdentifiableObject store found for " + clazz + ", returning empty collection (getAll)." );

            return new ArrayList<T>();
        }

        return (Collection<T>) store.getAll();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> Collection<T> getAllSorted( Class<T> clazz )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( clazz );

        if ( store == null )
        {
            log.warn( "No IdentifiableObject store found for " + clazz + ", returning empty collection (getAllSorted)." );

            return new ArrayList<T>();
        }

        return (Collection<T>) store.getAllSorted();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> Collection<T> getBetween( Class<T> clazz, int first, int max )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( clazz );

        if ( store == null )
        {
            log.warn( "No IdentifiableObject store found for " + clazz + ", returning empty collection (getBetween)." );

            return new ArrayList<T>();
        }

        return (Collection<T>) store.getBetween( first, max );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> Collection<T> getBetweenByName( Class<T> clazz, String name, int first, int max )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( clazz );

        if ( store == null )
        {
            log.warn( "No IdentifiableObject store found for " + clazz + ", returning empty collection (getBetweenByName)." );

            return new ArrayList<T>();
        }

        return (Collection<T>) store.getBetweenByName( name, first, max );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> Collection<T> getByLastUpdated( Class<T> clazz, Date lastUpdated )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( clazz );

        if ( store == null )
        {
            log.warn( "No IdentifiableObject store found for " + clazz + ", returning empty collection (getByLastUpdated)." );

            return new ArrayList<T>();
        }

        return (Collection<T>) store.getByLastUpdated( lastUpdated );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> Collection<T> getByLastUpdatedSorted( Class<T> clazz, Date lastUpdated )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( clazz );

        if ( store == null )
        {
            log.warn( "No IdentifiableObject store found for " + clazz + ", returning empty collection (getByLastUpdatedSorted)." );

            return new ArrayList<T>();
        }

        return (Collection<T>) store.getByLastUpdatedSorted( lastUpdated );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> Map<String, T> getIdMap( Class<T> clazz, IdentifiableProperty property )
    {
        Map<String, T> map = new HashMap<String, T>();

        GenericIdentifiableObjectStore<T> store = (GenericIdentifiableObjectStore<T>) getIdentifiableObjectStore( clazz );

        if ( store == null )
        {
            log.warn( "No IdentifiableObject store found for " + clazz + ", returning empty map (getIdMap)." );

            return map;
        }

        Collection<T> objects = store.getAll();

        for ( T object : objects )
        {
            if ( IdentifiableProperty.ID.equals( property ) )
            {
                if ( object.getId() > 0 )
                {
                    map.put( String.valueOf( object.getId() ), object );
                }
            }
            else if ( IdentifiableProperty.UID.equals( property ) )
            {
                if ( object.getUid() != null )
                {
                    map.put( object.getUid(), object );
                }
            }
            else if ( IdentifiableProperty.NAME.equals( property ) )
            {
                if ( object.getName() != null )
                {
                    map.put( object.getName(), object );
                }
            }
            else if ( IdentifiableProperty.CODE.equals( property ) )
            {
                if ( object.getCode() != null )
                {
                    map.put( object.getCode(), object );
                }
            }
        }

        return map;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends NameableObject> Map<String, T> getIdMap( Class<T> clazz, NameableProperty property )
    {
        Map<String, T> map = new HashMap<String, T>();

        GenericNameableObjectStore<T> store = (GenericNameableObjectStore<T>) getNameableObjectStore( clazz );

        Collection<T> objects = store.getAll();

        for ( T object : objects )
        {
            if ( property == NameableProperty.SHORT_NAME )
            {
                if ( object.getShortName() != null )
                {
                    map.put( object.getShortName(), object );
                }
            }
            else if ( property == NameableProperty.ALTERNATIVE_NAME )
            {
                if ( object.getAlternativeName() != null )
                {
                    map.put( object.getAlternativeName(), object );
                }
            }
        }

        return map;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends IdentifiableObject> T getObject( Class<T> clazz, IdentifiableProperty property, String id )
    {
        GenericIdentifiableObjectStore<T> store = (GenericIdentifiableObjectStore<T>) getIdentifiableObjectStore( clazz );

        if ( id != null )
        {
            if ( IdentifiableProperty.ID.equals( property ) )
            {
                if ( Integer.valueOf( id ) > 0 )
                {
                    return store.get( Integer.valueOf( id ) );
                }
            }
            else if ( IdentifiableProperty.UID.equals( property ) )
            {
                return store.getByUid( id );
            }
            else if ( IdentifiableProperty.CODE.equals( property ) )
            {
                return store.getByCode( id );
            }
            else if ( IdentifiableProperty.NAME.equals( property ) )
            {
                return store.getByName( id );
            }
        }

        throw new IllegalArgumentException( String.valueOf( property ) );
    }

    @Override
    public IdentifiableObject getObject( String uid, String simpleClassName )
    {
        for ( GenericIdentifiableObjectStore<IdentifiableObject> objectStore : identifiableObjectStores )
        {
            if ( simpleClassName.equals( objectStore.getClass().getSimpleName() ) )
            {
                return objectStore.getByUid( uid );
            }
        }

        return null;
    }

    @Override
    public IdentifiableObject getObject( int id, String simpleClassName )
    {
        for ( GenericIdentifiableObjectStore<IdentifiableObject> objectStore : identifiableObjectStores )
        {
            if ( simpleClassName.equals( objectStore.getClazz().getSimpleName() ) )
            {
                return objectStore.get( id );
            }
        }

        return null;
    }

    @Override
    public <T extends IdentifiableObject> int getCount( Class<T> clazz )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore( clazz );

        if ( store != null )
        {
            return store.getCount();
        }
        else
        {
            log.warn( "No IdentifiableObject store found for " + clazz + " (getCount)." );
        }

        return 0;
    }

    private <T extends IdentifiableObject> GenericIdentifiableObjectStore<IdentifiableObject> getIdentifiableObjectStore( Class<T> clazz )
    {
        GenericIdentifiableObjectStore<IdentifiableObject> store = identifiableObjectStoreMap.get( clazz );

        if ( store == null )
        {
            store = identifiableObjectStoreMap.get( clazz.getSuperclass() );
        }

        return store;
    }

    private <T extends NameableObject> GenericNameableObjectStore<NameableObject> getNameableObjectStore( Class<T> clazz )
    {
        GenericNameableObjectStore<NameableObject> store = nameableObjectStoreMap.get( clazz );

        if ( store == null )
        {
            store = nameableObjectStoreMap.get( clazz.getSuperclass() );
        }

        return store;
    }
}
