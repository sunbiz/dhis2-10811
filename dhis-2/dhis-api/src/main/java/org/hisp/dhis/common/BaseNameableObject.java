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

import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hisp.dhis.common.view.ShortNameView;

/**
 * @author Bob Jolliffe
 */
@JacksonXmlRootElement( localName = "nameableObject", namespace = Dxf2Namespace.NAMESPACE )
public class BaseNameableObject
    extends BaseIdentifiableObject
    implements NameableObject
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 714136796552146362L;

    /**
     * An alternative name of this Object. Optional but unique.
     */
    protected String alternativeName;

    /**
     * An short name representing this Object. Optional but unique.
     */
    protected String shortName;

    /**
     * Description of this Object.
     */
    protected String description;

    /**
     * The i18n variant of the short name. Should not be persisted.
     */
    protected transient String displayShortName;

    /**
     * The i18n variant of the description. Should not be persisted.
     */
    protected transient String displayDescription;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public BaseNameableObject()
    {
    }

    public BaseNameableObject( int id, String uid, String name, String alternativeName, String shortName,
                               String code, String description )
    {
        super( id, uid, name );
        this.alternativeName = alternativeName;
        this.shortName = shortName;
        this.code = code;
        this.description = description;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        if ( !super.equals( o ) ) return false;

        BaseNameableObject that = (BaseNameableObject) o;

        if ( description != null ? !description.equals( that.description ) : that.description != null ) return false;
        if ( shortName != null ? !shortName.equals( that.shortName ) : that.shortName != null ) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public String getAlternativeName()
    {
        return alternativeName;
    }

    public void setAlternativeName( String alternativeName )
    {
        this.alternativeName = alternativeName;
    }

    @JsonProperty
    @JsonView( { ShortNameView.class, DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( isAttribute = true )
    public String getShortName()
    {
        return shortName;
    }

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

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

    public String getDisplayShortName()
    {
        return displayShortName != null && !displayShortName.trim().isEmpty() ? displayShortName : shortName;
    }

    public void setDisplayShortName( String displayShortName )
    {
        this.displayShortName = displayShortName;
    }

    public String getDisplayDescription()
    {
        return displayDescription != null && !displayDescription.trim().isEmpty() ? displayDescription : description;
    }

    public void setDisplayDescription( String displayDescription )
    {
        this.displayDescription = displayDescription;
    }

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        super.mergeWith( other );

        if ( other.getClass().isInstance( this ) )
        {
            NameableObject nameableObject = (NameableObject) other;

            this.shortName = nameableObject.getShortName() == null ? this.shortName : nameableObject.getShortName();
            this.description = nameableObject.getDescription() == null ? this.description : nameableObject.getDescription();
        }
    }
}
