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

import java.io.Serializable;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
public class PatientIdentifier
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 5576120173066869531L;

    public static final int IDENTIFIER_INDEX_LENGTH = 5;

    private int id;

    private PatientIdentifierType identifierType;

    private Patient patient;

    private String identifier;

    private Boolean preferred = false;

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((identifierType == null) ? 0 : identifierType.hashCode());
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        
        return result;
    }

    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null )
        {
            return false;
        }

        if ( !(obj instanceof PatientIdentifier) )
        {
            return false;
        }

        PatientIdentifier other = (PatientIdentifier) obj;

        return identifier.equals( other.getIdentifier() ) && identifierType.equals( other.getIdentifierType() );
    }

    @Override
    public String toString()
    {
        return "[" + identifierType.getName() + ":"  + identifier + "]";
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public Patient getPatient()
    {
        return patient;
    }

    public void setPatient( Patient patient )
    {
        this.patient = patient;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier( String identifier )
    {
        this.identifier = identifier;
    }

    public Boolean getPreferred()
    {
        return preferred;
    }

    public void setPreferred( Boolean preferred )
    {
        this.preferred = preferred;
    }

    public void setIdentifierType( PatientIdentifierType identifierType )
    {
        this.identifierType = identifierType;
    }

    public PatientIdentifierType getIdentifierType()
    {
        return identifierType;
    }
    
}
