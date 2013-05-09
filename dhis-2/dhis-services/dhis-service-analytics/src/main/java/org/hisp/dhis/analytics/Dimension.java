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

import org.hisp.dhis.common.DimensionType;
import org.hisp.dhis.common.IdentifiableObject;

/**
 * @author Lars Helge Overland
 */
public class Dimension
{
    private String dimension;
    
    private DimensionType type;

    private String dimensionName;

    private String displayName;
    
    private List<IdentifiableObject> items = new ArrayList<IdentifiableObject>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    
    public Dimension( String dimension )
    {
        this.dimension = dimension;
    }

    public Dimension( String dimension, DimensionType type )
    {
        this.dimension = dimension;
        this.type = type;
    }

    public Dimension( String dimension, DimensionType type, List<IdentifiableObject> items )
    {
        this.dimension = dimension;
        this.type = type;
        this.items = items;
    }

    public Dimension( String dimension, DimensionType type, String dimensionName, List<IdentifiableObject> items )
    {
        this.dimension = dimension;
        this.type = type;
        this.dimensionName = dimensionName;
        this.items = items;
    }

    public Dimension( String dimension, DimensionType type, String dimensionName, String displayName, List<IdentifiableObject> items )
    {
        this.dimension = dimension;
        this.type = type;
        this.dimensionName = dimensionName;
        this.displayName = displayName;
        this.items = items;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------
    
    /**
     * Indicates whether this dimension should use all dimension items. All
     * dimension options is represented as an option list of zero elements.
     */
    public boolean isAllItems()
    {
        return items != null && items.isEmpty();
    }

    /**
     * Indicates whether this dimension has any dimension items.
     */
    public boolean hasItems()
    {
        return items != null && !items.isEmpty();
    }
    
    /**
     * Returns dimension name with fall back to dimension.
     */
    public String getDimensionName()
    {
        return dimensionName != null ? dimensionName : dimension;
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

    public DimensionType getType()
    {
        return type;
    }

    public void setType( DimensionType type )
    {
        this.type = type;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    public List<IdentifiableObject> getItems()
    {
        return items;
    }

    public void setItems( List<IdentifiableObject> items )
    {
        this.items = items;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals, toString
    // -------------------------------------------------------------------------
    
    @Override
    public int hashCode()
    {
        return dimension.hashCode();
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
        
        Dimension other = (Dimension) object;
        
        return dimension.equals( other.dimension );
    }
    
    @Override
    public String toString()
    {
        return "[Dimension: " + dimension + ", type: " + type + ", name: " + dimensionName + ", items: " + items + "]";
    }
}
