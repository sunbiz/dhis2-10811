package org.hisp.dhis.analytics;

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

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.system.util.CollectionUtils;

public class DimensionOption
{
    private String dimension;
    
    private IdentifiableObject option;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public DimensionOption( String dimension, IdentifiableObject option )
    {
        this.dimension = dimension;
        this.option = option;
    }

    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    public String getDimension()
    {
        return dimension;
    }

    public void setDimension( String dimension )
    {
        this.dimension = dimension;
    }

    public IdentifiableObject getOption()
    {
        return option;
    }

    public void setOption( IdentifiableObject option )
    {
        this.option = option;
    }

    // -------------------------------------------------------------------------
    // Static methods
    // -------------------------------------------------------------------------

    /**
     * Returns a string key for dimension options in the given list. The key is 
     * a concatenation of the dimension options separated by the dimension separator.
     * If no options are given or options is null, an empty string is returned.
     */
    public static String asOptionKey( List<DimensionOption> options )
    {
        StringBuilder builder = new StringBuilder();
        
        if ( options != null && !options.isEmpty() )
        {
            for ( DimensionOption option : options )
            {
                builder.append( option.getOption().getUid() ).append( DataQueryParams.DIMENSION_SEP );
            }
            
            builder.deleteCharAt( builder.length() - 1 );
        }
        
        return builder.toString();
    }

    /**
     * Returns an array of identifiers of the dimension options in the given list.
     * If no options are given or options is null, an empty array is returned.
     */
    public static String[] getOptionIdentifiers( List<DimensionOption> options )
    {
        List<String> optionUids = new ArrayList<String>();
        
        if ( options != null && !options.isEmpty() )
        {
            for ( DimensionOption option : options )
            {
                optionUids.add( option != null ? option.getOption().getUid() : null );
            }
        }
        
        return optionUids.toArray( CollectionUtils.STRING_ARR );
    }

    /**
     * Returns the period dimension option object from the given list of
     * dimension options. If no options are given, options is null or there is 
     * no period dimension, null is returned.
     */
    public static IdentifiableObject getPeriodOption( List<DimensionOption> options )
    {
        if ( options != null && !options.isEmpty() )
        {
            for ( DimensionOption option : options )
            {
                if ( DataQueryParams.PERIOD_DIM_ID.equals( option.getDimension() ) )
                {
                    return option.getOption();
                }
            }
        }
        
        return null;
    }
    
    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( dimension == null ) ? 0 : dimension.hashCode() );
        result = prime * result + ( ( option == null ) ? 0 : option.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( getClass() != object.getClass() )
        {
            return false;
        }
        
        DimensionOption other = (DimensionOption) object;
        
        if ( dimension == null )
        {
            if ( other.dimension != null )
            {
                return false;
            }
        }
        else if ( !dimension.equals( other.dimension ) )
        {
            return false;
        }
        
        if ( option == null )
        {
            if ( other.option != null )
            {
                return false;
            }
        }
        else if ( !option.equals( other.option ) )
        {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString()
    {
        return "[" + dimension + ", " + option + "]";
    }
}
