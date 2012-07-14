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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.Validate;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserGroupAccess;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Bob Jolliffe
 */
@JacksonXmlRootElement(localName = "identifiableObject", namespace = DxfNamespaces.DXF_2_0)
public class BaseIdentifiableObject
    extends BaseLinkableObject
    implements IdentifiableObject
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 5532508099213570673L;

    /**
     * The database internal identifier for this Object.
     */
    protected int id;

    /**
     * The Unique Identifier for this Object.
     */
    protected String uid;

    /**
     * The unique code for this Object.
     */
    protected String code;

    /**
     * The name of this Object. Required and unique.
     */
    protected String name;

    /**
     * The date this object was created.
     */
    protected Date created;

    /**
     * The date this object was last updated.
     */
    protected Date lastUpdated;

    /**
     * Access string for public access.
     */
    protected String publicAccess;

    /**
     * Owner of this object.
     */
    protected User user;

    /**
     * Access for userGroups
     */
    protected Set<UserGroupAccess> userGroupAccesses = new HashSet<UserGroupAccess>();

    /**
     * The i18n variant of the name. Should not be persisted.
     */
    protected transient String displayName;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public BaseIdentifiableObject()
    {
    }

    public BaseIdentifiableObject( int id, String uid, String name )
    {
        this.id = id;
        this.uid = uid;
        this.name = name;
    }

    public BaseIdentifiableObject( IdentifiableObject identifiableObject )
    {
        this.id = identifiableObject.getId();
        this.uid = identifiableObject.getUid();
        this.name = identifiableObject.getName();
        this.created = identifiableObject.getCreated();
        this.lastUpdated = identifiableObject.getLastUpdated();
    }

    // -------------------------------------------------------------------------
    // Comparable implementation
    // -------------------------------------------------------------------------

    @Override
    public int compareTo( IdentifiableObject object )
    {
        return name == null ? (object.getName() == null ? 0 : -1) : name.compareTo( object.getName() );
    }

    // -------------------------------------------------------------------------
    // Setters and getters
    // -------------------------------------------------------------------------

    @JsonIgnore
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty(value = "id")
    @JacksonXmlProperty(localName = "id", isAttribute = true)
    public String getUid()
    {
        return uid;
    }

    public void setUid( String uid )
    {
        this.uid = uid;
    }

    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public boolean haveUniqueNames()
    {
        return true;
    }

    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    @JsonProperty
    @JacksonXmlProperty(isAttribute = true)
    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated( Date lastUpdated )
    {
        this.lastUpdated = lastUpdated;
    }

    @Override
    @JsonProperty
    @JsonView({ DetailedView.class, ExportView.class })
    @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
    public String getPublicAccess()
    {
        return publicAccess;
    }

    public void setPublicAccess( String publicAccess )
    {
        this.publicAccess = publicAccess;
    }

    @Override
    @JsonProperty
    @JsonView({ DetailedView.class, ExportView.class })
    @JsonSerialize(as = BaseIdentifiableObject.class)
    @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    @JsonProperty
    @JsonView({ DetailedView.class, ExportView.class })
    @JacksonXmlElementWrapper(localName = "userGroupAccesses", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty(localName = "userGroupAccess", namespace = DxfNamespaces.DXF_2_0)
    public Set<UserGroupAccess> getUserGroupAccesses()
    {
        return userGroupAccesses;
    }

    public void setUserGroupAccesses( Set<UserGroupAccess> userGroupAccesses )
    {
        this.userGroupAccesses = userGroupAccesses;
    }

    public String getDisplayName()
    {
        return displayName != null && !displayName.trim().isEmpty() ? displayName : getName();
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);

        return result;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null || getClass() != o.getClass() ) return false;

        BaseIdentifiableObject that = (BaseIdentifiableObject) o;

        if ( uid != null ? !uid.equals( that.uid ) : that.uid != null )
        {
            return false;
        }

        if ( code != null ? !code.equals( that.code ) : that.code != null )
        {
            return false;
        }

        if ( name != null ? !name.equals( that.name ) : that.name != null )
        {
            return false;
        }

        return true;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    /**
     * Set auto-generated fields on save or update
     */
    public void setAutoFields()
    {
        if ( uid == null || uid.length() == 0 )
        {
            setUid( CodeGenerator.generateCode() );
        }

        Date date = new Date();

        if ( created == null )
        {
            created = date;
        }

        setLastUpdated( date );
    }

    /**
     * Get a map of uids to internal identifiers
     *
     * @param objects the IdentifiableObjects to put in the map
     * @return the map
     */
    public static Map<String, Integer> getUIDMap( Collection<? extends BaseIdentifiableObject> objects )
    {
        Map<String, Integer> map = new HashMap<String, Integer>();

        for ( IdentifiableObject object : objects )
        {
            String uid = object.getUid();
            int internalId = object.getId();

            map.put( uid, internalId );
        }

        return map;
    }

    /**
     * Get a map of codes to internal identifiers
     *
     * @param objects the NameableObjects to put in the map
     * @return the map
     */
    public static Map<String, Integer> getCodeMap( Collection<? extends BaseIdentifiableObject> objects )
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for ( BaseIdentifiableObject object : objects )
        {
            String code = object.getCode();
            int internalId = object.getId();

            map.put( code, internalId );
        }
        return map;
    }

    /**
     * Get a map of names to internal identifiers
     *
     * @param objects the NameableObjects to put in the map
     * @return the map
     */
    public static Map<String, Integer> getNameMap( Collection<? extends BaseIdentifiableObject> objects )
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for ( BaseIdentifiableObject object : objects )
        {
            String name = object.getName();
            int internalId = object.getId();

            map.put( name, internalId );
        }
        return map;
    }

    @Override
    public String toString()
    {
        return "IdentifiableObject{" +
            "id=" + id +
            ", uid='" + uid + '\'' +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", created=" + created +
            ", lastUpdated=" + lastUpdated +
            '}';
    }

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        Validate.notNull( other );

        this.uid = other.getUid() == null ? this.uid : other.getUid();
        this.name = other.getName() == null ? this.name : other.getName();
        this.code = other.getCode() == null ? this.code : other.getCode();
        this.lastUpdated = other.getLastUpdated() == null ? this.lastUpdated : other.getLastUpdated();
    }
}
