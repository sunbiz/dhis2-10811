package org.hisp.dhis.dataelement;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hisp.dhis.attribute.AttributeValue;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.Dxf2Namespace;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.annotation.Scanned;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kristian Nordal
 */
@JacksonXmlRootElement( localName = "dataElementGroup", namespace = Dxf2Namespace.NAMESPACE )
public class DataElementGroup
    extends BaseIdentifiableObject
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 6101685842665568056L;

    @Scanned
    private Set<DataElement> members = new HashSet<DataElement>();

    private DataElementGroupSet groupSet;

    /**
     * Set of the dynamic attributes values that belong to this dataElement group.
     */
    private Set<AttributeValue> attributeValues = new HashSet<AttributeValue>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataElementGroup()
    {
    }

    public DataElementGroup( String name )
    {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addDataElement( DataElement dataElement )
    {
        members.add( dataElement );
        dataElement.getGroups().add( this );
    }

    public void removeDataElement( DataElement dataElement )
    {
        members.remove( dataElement );
        dataElement.getGroups().remove( this );
    }

    public void removeAllDataElements()
    {
        for ( DataElement dataElement : members )
        {
            dataElement.getGroups().remove( this );
        }

        members.clear();
    }

    public void updateDataElements( Set<DataElement> updates )
    {
        for ( DataElement dataElement : new HashSet<DataElement>( members ) )
        {
            if ( !updates.contains( dataElement ) )
            {
                removeDataElement( dataElement );
            }
        }

        for ( DataElement dataElement : updates )
        {
            addDataElement( dataElement );
        }
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof DataElementGroup) )
        {
            return false;
        }

        final DataElementGroup other = (DataElementGroup) o;

        return name.equals( other.getName() );
    }

    @Override
    public String toString()
    {
        return "[" + name + "]";
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty( value = "dataElements" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlElementWrapper( localName = "dataElements", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "dataElement", namespace = Dxf2Namespace.NAMESPACE )
    public Set<DataElement> getMembers()
    {
        return members;
    }

    public void setMembers( Set<DataElement> members )
    {
        this.members = members;
    }

    @JsonProperty( value = "dataElementGroupSet" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class } )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public DataElementGroupSet getGroupSet()
    {
        return groupSet;
    }

    public void setGroupSet( DataElementGroupSet groupSet )
    {
        this.groupSet = groupSet;
    }

    @JsonProperty( value = "attributes" )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlElementWrapper( localName = "attributes", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "attribute", namespace = Dxf2Namespace.NAMESPACE )
    public Set<AttributeValue> getAttributeValues()
    {
        return attributeValues;
    }

    public void setAttributeValues( Set<AttributeValue> attributeValues )
    {
        this.attributeValues = attributeValues;
    }

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        super.mergeWith( other );

        if ( other.getClass().isInstance( this ) )
        {
            DataElementGroup dataElementGroup = (DataElementGroup) other;

            groupSet = null;

            removeAllDataElements();

            for ( DataElement dataElement : dataElementGroup.getMembers() )
            {
                addDataElement( dataElement );
            }

            attributeValues.clear();
            attributeValues.addAll( dataElementGroup.getAttributeValues() );
        }
    }
}
