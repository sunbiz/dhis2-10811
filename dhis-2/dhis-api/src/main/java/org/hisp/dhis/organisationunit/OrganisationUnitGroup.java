package org.hisp.dhis.organisationunit;

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
import org.hisp.dhis.common.BaseNameableObject;
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
@JacksonXmlRootElement( localName = "organisationUnitGroup", namespace = Dxf2Namespace.NAMESPACE )
public class OrganisationUnitGroup
    extends BaseNameableObject
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -1131637847640209166L;

    @Scanned
    private Set<OrganisationUnit> members = new HashSet<OrganisationUnit>();

    private OrganisationUnitGroupSet groupSet;

    /**
     * Set of the dynamic attributes values that belong to this organisationUnit group.
     */
    private Set<AttributeValue> attributeValues = new HashSet<AttributeValue>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnitGroup()
    {
    }

    public OrganisationUnitGroup( String name )
    {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    @Override
    public String getShortName()
    {
        return name;
    }

    @Override
    public String getCode()
    {
        return (name != null && name.length() > 50) ? name.substring( 0, 50 ) : name;
    }

    public void addOrganisationUnit( OrganisationUnit organisationUnit )
    {
        members.add( organisationUnit );
        organisationUnit.getGroups().add( this );
    }

    public void removeOrganisationUnit( OrganisationUnit organisationUnit )
    {
        members.remove( organisationUnit );
        organisationUnit.getGroups().remove( this );
    }

    public void removeAllOrganisationUnits()
    {
        for ( OrganisationUnit organisationUnit : members )
        {
            organisationUnit.getGroups().remove( this );
        }

        members.clear();
    }

    public void updateOrganisationUnits( Set<OrganisationUnit> updates )
    {
        for ( OrganisationUnit unit : new HashSet<OrganisationUnit>( members ) )
        {
            if ( !updates.contains( unit ) )
            {
                removeOrganisationUnit( unit );
            }
        }

        for ( OrganisationUnit unit : updates )
        {
            addOrganisationUnit( unit );
        }
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

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

        if ( !(o instanceof OrganisationUnitGroup) )
        {
            return false;
        }

        final OrganisationUnitGroup other = (OrganisationUnitGroup) o;

        return name.equals( other.getName() );
    }

/*
    @Override
    public String toString()
    {
        return "OrganisationUnitGroup{" +
            "members=" + members +
            ", groupSet=" + groupSet.getName() +
            "} " + super.toString();
    }
*/

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty( value = "organisationUnits" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlElementWrapper( localName = "organisationUnits", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "organisationUnit", namespace = Dxf2Namespace.NAMESPACE )
    public Set<OrganisationUnit> getMembers()
    {
        return members;
    }

    public void setMembers( Set<OrganisationUnit> members )
    {
        this.members = members;
    }

    @JsonProperty( value = "organisationUnitGroupSet" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class } )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public OrganisationUnitGroupSet getGroupSet()
    {
        return groupSet;
    }

    public void setGroupSet( OrganisationUnitGroupSet groupSet )
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
            OrganisationUnitGroup organisationUnitGroup = (OrganisationUnitGroup) other;

            groupSet = null;

            removeAllOrganisationUnits();

            for ( OrganisationUnit organisationUnit : organisationUnitGroup.getMembers() )
            {
                addOrganisationUnit( organisationUnit );
            }

            attributeValues.clear();
            attributeValues.addAll( organisationUnitGroup.getAttributeValues() );
        }
    }
}
