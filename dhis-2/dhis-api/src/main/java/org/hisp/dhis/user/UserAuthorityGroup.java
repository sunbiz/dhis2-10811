package org.hisp.dhis.user;

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
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.Dxf2Namespace;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.annotation.Scanned;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.dataset.DataSet;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nguyen Hong Duc
 */
@JacksonXmlRootElement( localName = "userAuthorityGroup", namespace = Dxf2Namespace.NAMESPACE )
public class UserAuthorityGroup
    extends BaseIdentifiableObject
{
    public static final String AUTHORITY_ALL = "ALL";

    /**
     * Required and unique.
     */
    private String description;

    private Set<String> authorities = new HashSet<String>();

    private Set<UserCredentials> members = new HashSet<UserCredentials>();

    @Scanned
    private Set<DataSet> dataSets = new HashSet<DataSet>();

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

        if ( !(o instanceof UserAuthorityGroup) )
        {
            return false;
        }

        final UserAuthorityGroup other = (UserAuthorityGroup) o;

        return name.equals( other.getName() );
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addUserCredentials( UserCredentials userCredentials )
    {
        members.add( userCredentials );
        userCredentials.getUserAuthorityGroups().add( this );
    }

    public void removeUserCredentials( UserCredentials userCredentials )
    {
        members.remove( userCredentials );
        userCredentials.getUserAuthorityGroups().remove( this );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "authorities", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "authority", namespace = Dxf2Namespace.NAMESPACE )
    public Set<String> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities( Set<String> authorities )
    {
        this.authorities = authorities;
    }

    public Set<UserCredentials> getMembers()
    {
        return members;
    }

    public void setMembers( Set<UserCredentials> members )
    {
        this.members = members;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "dataSets", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "dataSet", namespace = Dxf2Namespace.NAMESPACE )
    public Set<DataSet> getDataSets()
    {
        return dataSets;
    }

    public void setDataSets( Set<DataSet> dataSets )
    {
        this.dataSets = dataSets;
    }

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        super.mergeWith( other );

        if ( other.getClass().isInstance( this ) )
        {
            UserAuthorityGroup userAuthorityGroup = (UserAuthorityGroup) other;

            description = userAuthorityGroup.getDescription() == null ? description : userAuthorityGroup.getDescription();

            removeAllAuthorities();
            authorities.addAll( authorities );

            removeAllDataSets();
            dataSets.addAll( userAuthorityGroup.getDataSets() );
        }
    }

    public void removeAllDataSets()
    {
        dataSets.clear();
    }

    private void removeAllAuthorities()
    {
        authorities.clear();
    }
}
