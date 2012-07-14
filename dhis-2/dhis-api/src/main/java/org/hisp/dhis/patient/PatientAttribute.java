/*
 * Copyright (c) 2004-2009, University of Oslo
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
package org.hisp.dhis.patient;

import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.common.BaseIdentifiableObject;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */

public class PatientAttribute
    extends BaseIdentifiableObject
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 3026922158464592390L;

    public static final String TYPE_DATE = "DATE";

    public static final String TYPE_STRING = "TEXT";

    public static final String TYPE_INT = "NUMBER";

    public static final String TYPE_BOOL = "YES/NO";

    public static final String TYPE_COMBO = "COMBO";

    public static final String TYPE_CALCULATED = "CALCULATED";

    private String description;

    private String valueType;

    private boolean mandatory;

    private Boolean inherit;

    private Boolean groupBy;

    private PatientAttributeGroup patientAttributeGroup;

    private Set<PatientAttributeOption> attributeOptions;

    private String expression;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public PatientAttribute()
    {
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
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

        if ( !(o instanceof PatientAttribute) )
        {
            return false;
        }

        final PatientAttribute other = (PatientAttribute) o;

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

    public Set<PatientAttributeOption> getAttributeOptions()
    {
        return attributeOptions == null ? new HashSet<PatientAttributeOption>() : attributeOptions;
    }

    public void setAttributeOptions( Set<PatientAttributeOption> attributeOptions )
    {
        this.attributeOptions = attributeOptions;
    }

    public void addAttributeOptions( PatientAttributeOption option )
    {
        if ( attributeOptions == null )
            attributeOptions = new HashSet<PatientAttributeOption>();
        attributeOptions.add( option );
    }

    public void setInherit( Boolean inherit )
    {
        this.inherit = inherit;
    }

    public Boolean getInherit()
    {
        return inherit;
    }

    public void setGroupBy( Boolean groupBy )
    {
        this.groupBy = groupBy;
    }

    public Boolean getGroupBy()
    {
        return groupBy;
    }

    public boolean isMandatory()
    {
        return mandatory;
    }

    public void setMandatory( boolean mandatory )
    {
        this.mandatory = mandatory;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getValueType()
    {
        return valueType;
    }

    public void setValueType( String valueType )
    {
        this.valueType = valueType;
    }

    public PatientAttributeGroup getPatientAttributeGroup()
    {
        return patientAttributeGroup;
    }

    public void setPatientAttributeGroup( PatientAttributeGroup patientAttributeGroup )
    {
        this.patientAttributeGroup = patientAttributeGroup;
    }

    public String getExpression()
    {
        return expression;
    }

    public void setExpression( String expression )
    {
        this.expression = expression;
    }

}
