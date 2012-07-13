package org.hisp.dhis.system.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class TextUtils
{
    public static final TextUtils INSTANCE = new TextUtils();
    
    private static final Pattern LINK_PATTERN = Pattern.compile( "((http://|https://|www\\.).+?)($| )" );
    
    /**
     * Substitutes links in the given text with valid HTML mark-up. For instance, 
     * http://dhis2.org is replaced with <a href="http://dhis2.org">http://dhis2.org</a>,
     * and www.dhis2.org is replaced with <a href="http://dhis2.org">www.dhis2.org</a>.
     * 
     * @param text the text to substitute links for.
     * @return the substituted text.
     */
    public static String htmlLinks( String text )
    {
        if ( text == null || text.trim().isEmpty() )
        {
            return null;
        }
        
        Matcher matcher = LINK_PATTERN.matcher( text );

        StringBuffer buffer = new StringBuffer();
        
        while ( matcher.find() )
        {
            String url = matcher.group( 1 );            
            String suffix = matcher.group( 3 );
            
            String ref = url.startsWith( "www." ) ? "http://" + url : url;

            url = "<a href=\"" + ref + "\">" + url + "</a>" + suffix;
            
            matcher.appendReplacement( buffer, url );
        }
        
        return matcher.appendTail( buffer ).toString();
    }
        
    /**
     * Gets the sub string of the given string. If the beginIndex is larger than
     * the length of the string, the empty string is returned. If the beginIndex +
     * the length is larger than the length of the string, the part of the string
     * following the beginIndex is returned.
     * 
     * @param string the string.
     * @param beginIndex the zero-based begin index.
     * @param length the length of the sub string starting at the begin index.
     * @return the sub string of the given string.
     */
    public static String subString( String string, int beginIndex, int length )
    {
        final int endIndex = beginIndex + length;
        
        if ( beginIndex >= string.length()  )
        {
            return "";
        }
        
        if ( endIndex > string.length() )
        {
            return string.substring( beginIndex, string.length() );
        }
        
        return string.substring( beginIndex, endIndex );
    }
    
    /**
     * Trims the given string from the end.
     * 
     * @param value the value to trim.
     * @param length the number of characters to trim.
     * @return the trimmed value, empty if given value is null or length is higher
     *         than the value length.
     */
    public static String trimEnd( String value, int length )
    {
        if ( value == null || length > value.length() )
        {
            return "";
        }
        
        return value.substring( 0, value.length() - length );
    }

    /**
     * Transforms a collection of Integers into a comma delimited String.
     * 
     * @param elements the collection of Integers
     * @return a comma delimited String.
     */
    public static String getCommaDelimitedString( Collection<Integer> elements )
    {
        if ( elements != null && elements.size() > 0 )
        {
            final StringBuffer buffer = new StringBuffer();        
        
            for ( Integer element : elements )
            {
                buffer.append( element.toString() ).append( ", " );
            }
            
            return buffer.substring( 0, buffer.length() - ", ".length() );
        }
        
        return null;
    }
    
    /**
     * Returns null if the given string is not null and contains no charachters,
     * the string itselft otherwise.
     * 
     * @param string the string.
     * @return null if the given string is not null and contains no charachters,
     *         the string itself otherwise.
     */
    public static String nullIfEmpty( String string )
    {
        return string != null && string.trim().length() == 0 ? null : string;
    }
    
    /**
     * Checks the two strings for equality.
     * 
     * @param s1 string 1.
     * @param s2 string 2.
     * @return true if strings are equal, false otherwise.
     */
    public static boolean equalsNullSafe( String s1, String s2 )
    {
        return s1 == null ? s2 == null : s1.equals( s2 );
    }
    
    /**
     * Returns the string value of the given boolean. Returns null if argument
     * is null.
     * 
     * @param value the boolean.
     * @return the string value.
     */
    public static String valueOf( Boolean value )
    {
        return value != null ? String.valueOf( value ) : null;
    }
    
    /**
     * Returns the boolean value of the given string. Returns null if argument
     * is null.
     * 
     * @param value the string value.
     * @return the boolean.
     */
    public static Boolean valueOf( String value )
    {
        return value != null ? Boolean.valueOf( value ) : null;
    }
    
    /**
     * Null-safe method for converting the given string to lower-case.
     * 
     * @param string the string.
     * @return the string in lower-case.
     */
    public static String lower( String string )
    {
        return string != null ? string.toLowerCase() : null;
    }
}
