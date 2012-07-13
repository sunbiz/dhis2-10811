package org.hisp.dhis.common;

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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Lars Helge Overland
 */
public class IdentifiableObjectUtils
{
    private static final String SEPARATOR_JOIN = ", ";
    private static final String SEPARATOR = "-";
    private static final SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );

    /**
     * Joins the names of the IdentifiableObjects in the given list and separates
     * them with a comma and space. Returns null if the given list is null or has
     * no elements.
     *
     * @param objects the list of IdentifiableObjects.
     * @return the joined string.
     */
    public static String join( Collection<? extends IdentifiableObject> objects )
    {
        if ( objects != null && objects.size() > 0 )
        {
            Iterator<? extends IdentifiableObject> iterator = objects.iterator();

            StringBuilder builder = new StringBuilder( iterator.next().getDisplayName() );

            while ( iterator.hasNext() )
            {
                builder.append( SEPARATOR_JOIN ).append( iterator.next().getDisplayName() );
            }

            return builder.toString();
        }

        return null;
    }

    /**
     * Filters the given list of IdentifiableObjects based on the given key.
     *
     * @param identifiableObjects the list of IdentifiableObjects.
     * @param key                 the key.
     * @param ignoreCase          indicates whether to ignore case when filtering.
     * @return a filtered list of IdentifiableObjects.
     */
    public static <T extends IdentifiableObject> List<T> filterNameByKey( List<T> identifiableObjects, String key,
                                                                          boolean ignoreCase )
    {
        List<T> objects = new ArrayList<T>();
        ListIterator<T> iterator = identifiableObjects.listIterator();

        if ( ignoreCase )
        {
            key = key.toLowerCase();
        }

        while ( iterator.hasNext() )
        {
            T object = iterator.next();
            String name = ignoreCase ? object.getDisplayName().toLowerCase() : object.getDisplayName();

            if ( name.indexOf( key ) != -1 )
            {
                objects.add( object );
            }
        }

        return objects;
    }

    /**
     * Generates a tag reflecting the date of when the most recently updated
     * IdentifiableObject in the given collection was modified.
     *
     * @param objects the collection of IdentifiableObjects.
     * @return a string tag.
     */
    public static <T extends IdentifiableObject> String getLastUpdatedTag( Collection<T> objects )
    {
        Date latest = null;

        if ( objects != null )
        {
            for ( IdentifiableObject object : objects )
            {
                if ( object != null && object.getLastUpdated() != null && (latest == null || object.getLastUpdated().after( latest )) )
                {
                    latest = object.getLastUpdated();
                }
            }
        }

        return latest != null && objects != null ? objects.size() + SEPARATOR + LONG_DATE_FORMAT.format( latest ) : null;
    }
    
    /**
     * Generates a tag reflecting the date of when the object was last updated.
     * 
     * @param object the identifiable object.
     * @return a string tag.
     */
    public static String getLastUpdatedTag( IdentifiableObject object )
    {
        return object != null ? LONG_DATE_FORMAT.format( object.getLastUpdated() ) : null;
    }
}
