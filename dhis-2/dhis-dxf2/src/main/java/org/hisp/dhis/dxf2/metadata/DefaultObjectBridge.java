package org.hisp.dhis.dxf2.metadata;

/*
 * Copyright (c) 2012, University of Oslo
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
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.common.NameableObject;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class DefaultObjectBridge
    implements ObjectBridge
{
    private static final Log log = LogFactory.getLog( DefaultObjectBridge.class );

    //-------------------------------------------------------------------------------------------------------
    // Dependencies
    //-------------------------------------------------------------------------------------------------------

    @Autowired
    private IdentifiableObjectManager manager;

    @Autowired
    private PeriodStore periodStore;

    //-------------------------------------------------------------------------------------------------------
    // Internal and Semi-Public maps
    //-------------------------------------------------------------------------------------------------------

    private static final List<Class<?>> registeredTypes = new ArrayList<Class<?>>();

    private static final List<Class<?>> shortNameNotUnique = new ArrayList<Class<?>>();

    private Map<Class<?>, Collection<?>> masterMap;

    private Map<String, PeriodType> periodTypeMap;

    private Map<Class<? extends IdentifiableObject>, Map<String, IdentifiableObject>> uidMap;

    private Map<Class<? extends IdentifiableObject>, Map<String, IdentifiableObject>> codeMap;

    private Map<Class<? extends IdentifiableObject>, Map<String, IdentifiableObject>> nameMap;

    private Map<Class<? extends NameableObject>, Map<String, NameableObject>> shortNameMap;

    private boolean writeEnabled = true;

    //-------------------------------------------------------------------------------------------------------
    // Build maps
    //-------------------------------------------------------------------------------------------------------

    static
    {
        registeredTypes.add( PeriodType.class );

        for ( Class<?> clazz : ExchangeClasses.getImportClasses() )
        {
            registeredTypes.add( clazz );
        }

        shortNameNotUnique.add( OrganisationUnit.class );
    }

    @Override
    public void init()
    {
        log.info( "Started updating lookup maps at " + new Date() );

        masterMap = new HashMap<Class<?>, Collection<?>>();
        periodTypeMap = new HashMap<String, PeriodType>();
        uidMap = new HashMap<Class<? extends IdentifiableObject>, Map<String, IdentifiableObject>>();
        codeMap = new HashMap<Class<? extends IdentifiableObject>, Map<String, IdentifiableObject>>();
        nameMap = new HashMap<Class<? extends IdentifiableObject>, Map<String, IdentifiableObject>>();
        shortNameMap = new HashMap<Class<? extends NameableObject>, Map<String, NameableObject>>();

        for ( Class<?> type : registeredTypes )
        {
            populatePeriodTypeMap( type );
            populateIdentifiableObjectMap( type );
            populateIdentifiableObjectMap( type, IdentifiableObject.IdentifiableProperty.UID );
            populateIdentifiableObjectMap( type, IdentifiableObject.IdentifiableProperty.CODE );
            populateIdentifiableObjectMap( type, IdentifiableObject.IdentifiableProperty.NAME );
            populateNameableObjectMap( type, NameableObject.NameableProperty.SHORT_NAME );
        }

        log.info( "Finished updating lookup maps at " + new Date() );
    }

    @Override
    public void destroy()
    {
        masterMap = null;
        uidMap = null;
        codeMap = null;
        nameMap = null;
        shortNameMap = null;
        periodTypeMap = null;

        writeEnabled = true;
    }

    //-------------------------------------------------------------------------------------------------------
    // Populate Helpers
    //-------------------------------------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    private void populateIdentifiableObjectMap( Class<?> clazz )
    {
        Collection<IdentifiableObject> map = new ArrayList<IdentifiableObject>();

        if ( IdentifiableObject.class.isAssignableFrom( clazz ) )
        {
            map = manager.getAll( (Class<IdentifiableObject>) clazz );
        }

        if ( map != null )
        {
            masterMap.put( clazz, map );
        }
    }

    @SuppressWarnings( "unchecked" )
    private void populateIdentifiableObjectMap( Class<?> clazz, IdentifiableObject.IdentifiableProperty property )
    {
        Map<String, IdentifiableObject> map = new HashMap<String, IdentifiableObject>();

        if ( IdentifiableObject.class.isAssignableFrom( clazz ) )
        {
            map = (Map<String, IdentifiableObject>) manager.getIdMap( (Class<? extends IdentifiableObject>) clazz, property );
        }

        if ( map != null )
        {
            if ( property == IdentifiableObject.IdentifiableProperty.UID )
            {
                uidMap.put( (Class<? extends IdentifiableObject>) clazz, map );
            }
            else if ( property == IdentifiableObject.IdentifiableProperty.CODE )
            {
                codeMap.put( (Class<? extends IdentifiableObject>) clazz, map );
            }
            else if ( property == IdentifiableObject.IdentifiableProperty.NAME )
            {
                nameMap.put( (Class<? extends IdentifiableObject>) clazz, map );
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    private void populateNameableObjectMap( Class<?> clazz, NameableObject.NameableProperty property )
    {
        Map<String, NameableObject> map = null;

        if ( NameableObject.class.isAssignableFrom( clazz ) )
        {
            map = (Map<String, NameableObject>) manager.getIdMap( (Class<? extends NameableObject>) clazz, property );
        }

        if ( map != null )
        {
            if ( property == NameableObject.NameableProperty.SHORT_NAME )
            {
                shortNameMap.put( (Class<? extends NameableObject>) clazz, map );
            }
        }
    }

    private void populatePeriodTypeMap( Class<?> clazz )
    {
        Collection<Object> periodTypes = new ArrayList<Object>();

        if ( PeriodType.class.isAssignableFrom( clazz ) )
        {
            for ( PeriodType periodType : periodStore.getAllPeriodTypes() )
            {
                periodTypes.add( periodType );
                periodTypeMap.put( periodType.getName(), periodType );
            }
        }

        masterMap.put( clazz, periodTypes );
    }

    //-------------------------------------------------------------------------------------------------------
    // ObjectBridge Implementation
    //-------------------------------------------------------------------------------------------------------

    @Override
    public void saveObject( Object object )
    {
        if ( _typeSupported( object.getClass() ) && IdentifiableObject.class.isInstance( object ) )
        {
            if ( writeEnabled )
            {
                manager.save( (IdentifiableObject) object );
            }
        }
        else
        {
            log.warn( "Trying to save unsupported type + " + object.getClass() + " with object " + object + " object discarded." );
        }
    }

    @Override
    public void updateObject( Object object )
    {
        if ( _typeSupported( object.getClass() ) && IdentifiableObject.class.isInstance( object ) )
        {
            if ( writeEnabled )
            {
                manager.update( (IdentifiableObject) object );
            }

            _updateInternalMaps( object );
        }
        else
        {
            log.warn( "Trying to update unsupported type + " + object.getClass() + " with object " + object + " object discarded." );
        }
    }

    @Override
    public <T> T getObject( T object )
    {
        Collection<T> objects = _findMatches( object );

        if ( objects.size() == 1 )
        {
            return objects.iterator().next();
        }
        else if ( objects.size() > 1 )
        {
            log.debug( "Multiple objects found for " + object + ", object discarded, returning null." );
        }
        else
        {
            log.debug( "No object found for " + object + ", returning null." );
        }

        return null;
    }

    @Override
    public <T> Collection<T> getObjects( T object )
    {
        return _findMatches( object );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T> Collection<T> getAllObjects( Class<T> clazz )
    {
        return (Collection<T>) masterMap.get( clazz );
    }

    public Map<Class<?>, Collection<?>> getMasterMap()
    {
        return masterMap;
    }

    public void setMasterMap( Map<Class<?>, Collection<?>> masterMap )
    {
        this.masterMap = masterMap;
    }

    @Override
    public void setWriteEnabled( boolean enabled )
    {
        this.writeEnabled = enabled;
    }

    @Override
    public boolean isWriteEnabled()
    {
        return writeEnabled;
    }

    //-------------------------------------------------------------------------------------------------------
    // Internal Methods
    //-------------------------------------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    private <T> Collection<T> _findMatches( T object )
    {
        Collection<T> objects = new HashSet<T>();

        if ( PeriodType.class.isInstance( object ) )
        {
            PeriodType periodType = (PeriodType) object;
            periodType = periodTypeMap.get( periodType.getName() );

            if ( periodType != null )
            {
                objects.add( (T) periodType );
            }
        }

        if ( IdentifiableObject.class.isInstance( object ) )
        {
            IdentifiableObject identifiableObject = (IdentifiableObject) object;

            if ( identifiableObject.getUid() != null )
            {
                IdentifiableObject match = getUidMatch( identifiableObject );

                if ( match != null )
                {
                    objects.add( (T) match );
                }
            }

            if ( identifiableObject.getCode() != null )
            {
                IdentifiableObject match = getCodeMatch( identifiableObject );

                if ( match != null )
                {
                    objects.add( (T) match );
                }
            }

            if ( identifiableObject.getName() != null )
            {
                IdentifiableObject match = getNameMatch( identifiableObject );

                if ( match != null )
                {
                    objects.add( (T) match );
                }
            }
        }

        if ( NameableObject.class.isInstance( object ) )
        {
            NameableObject nameableObject = (NameableObject) object;

            if ( _shortNameUnique( object ) && nameableObject.getShortName() != null )
            {
                IdentifiableObject match = getShortNameMatch( nameableObject );

                if ( match != null )
                {
                    objects.add( (T) match );
                }
            }
        }

        return objects;
    }

    private <T> void _updateInternalMaps( T object )
    {
        if ( IdentifiableObject.class.isInstance( object ) )
        {
            IdentifiableObject identifiableObject = (IdentifiableObject) object;

            if ( identifiableObject.getUid() != null )
            {
                Map<String, IdentifiableObject> map = uidMap.get( identifiableObject.getClass() );

                if ( map == null )
                {
                    // might be dynamically sub-classed by javassist or cglib, fetch superclass and try again
                    map = uidMap.get( identifiableObject.getClass().getSuperclass() );
                }

                map.put( identifiableObject.getUid(), identifiableObject );
            }

            if ( identifiableObject.getCode() != null )
            {
                Map<String, IdentifiableObject> map = codeMap.get( identifiableObject.getClass() );

                if ( map == null )
                {
                    // might be dynamically sub-classed by javassist or cglib, fetch superclass and try again
                    map = codeMap.get( identifiableObject.getClass().getSuperclass() );
                }

                map.put( identifiableObject.getCode(), identifiableObject );
            }

            if ( identifiableObject.getName() != null )
            {
                Map<String, IdentifiableObject> map = nameMap.get( identifiableObject.getClass() );

                if ( map == null )
                {
                    // might be dynamically sub-classed by javassist or cglib, fetch superclass and try again
                    map = nameMap.get( identifiableObject.getClass().getSuperclass() );
                }

                map.put( identifiableObject.getName(), identifiableObject );
            }
        }

        if ( NameableObject.class.isInstance( object ) )
        {
            NameableObject nameableObject = (NameableObject) object;

            if ( _shortNameUnique( object ) && nameableObject.getShortName() != null )
            {
                Map<String, NameableObject> map = shortNameMap.get( nameableObject.getClass() );

                if ( map == null )
                {
                    // might be dynamically sub-classed by javassist or cglib, fetch superclass and try again
                    map = shortNameMap.get( nameableObject.getClass().getSuperclass() );
                }

                map.put( nameableObject.getShortName(), nameableObject );
            }
        }
    }

    private IdentifiableObject getUidMatch( IdentifiableObject identifiableObject )
    {
        Map<String, IdentifiableObject> map = uidMap.get( identifiableObject.getClass() );

        if ( map != null )
        {
            return map.get( identifiableObject.getUid() );
        }

        return null;
    }

    private IdentifiableObject getCodeMatch( IdentifiableObject identifiableObject )
    {
        Map<String, IdentifiableObject> map = codeMap.get( identifiableObject.getClass() );

        if ( map != null )
        {
            return map.get( identifiableObject.getCode() );
        }

        return null;
    }

    private IdentifiableObject getNameMatch( IdentifiableObject identifiableObject )
    {
        Map<String, IdentifiableObject> map = nameMap.get( identifiableObject.getClass() );

        if ( map != null )
        {
            return map.get( identifiableObject.getName() );
        }

        return null;
    }

    private NameableObject getShortNameMatch( NameableObject nameableObject )
    {
        Map<String, NameableObject> map = shortNameMap.get( nameableObject.getClass() );

        if ( map != null )
        {
            return map.get( nameableObject.getShortName() );
        }

        return null;
    }

    private boolean _typeSupported( Class<?> clazz )
    {
        for ( Class<?> c : registeredTypes )
        {
            if ( c.isAssignableFrom( clazz ) )
            {
                return true;
            }
        }

        return false;
    }

    private <T> boolean _shortNameUnique( T object )
    {
        for ( Class clazz : shortNameNotUnique )
        {
            if ( clazz.isAssignableFrom( object.getClass() ) )
            {
                return false;
            }
        }

        return true;
    }
}
