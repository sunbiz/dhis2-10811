/*
 * Copyright (c) 2004-2010, University of Oslo
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

package org.hisp.dhis.program;

import java.io.Serializable;

/**
 * @author Chau Thu Tran
 * @version $ ProgramValidation.java Apr 28, 2011 10:27:29 AM $
 */
public class ProgramValidation
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 4785165717118297802L;

    public static final String SEPARATOR_ID = "\\.";

    public static final String SEPARATOR_OBJECT = ":";

    public static String OBJECT_PROGRAM_STAGE_DATAELEMENT = "DE";
    

    public static final int BEFORE_CURRENT_DATE = 1;

    public static final int BEFORE_OR_EQUALS_TO_CURRENT_DATE = 2;

    public static final int AFTER_CURRENT_DATE = 3;

    public static final int AFTER_OR_EQUALS_TO_CURRENT_DATE = 4;
    

    public static final int BEFORE_DUE_DATE = -1;

    public static final int BEFORE_OR_EQUALS_TO_DUE_DATE = -2;

    public static final int AFTER_DUE_DATE = -3;

    public static final int AFTER_OR_EQUALS_TO_DUE_DATE = -4;
    
    public static final int BEFORE_DUE_DATE_PLUS_OR_MINUS_MAX_DAYS = -5;

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private int id;

    private String description;

    private String leftSide;

    private String rightSide;

    private Program program;

    private Boolean dateType;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ProgramValidation()
    {

    }

    public ProgramValidation( String description, String leftSide, String rightSide, Program program )
    {
        this.description = description;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.program = program;
    }

    public ProgramValidation( String description, String leftSide, String rightSide, Program program, Boolean dateType )
    {
        this.description = description;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.program = program;
        this.dateType = dateType;
    }

    // -------------------------------------------------------------------------
    // hashCode() and equals()
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((leftSide == null) ? 0 : leftSide.hashCode());
        result = prime * result + ((program == null) ? 0 : program.hashCode());
        result = prime * result + ((rightSide == null) ? 0 : rightSide.hashCode());
        return result;
    }

    @Override
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

        if ( getClass() != obj.getClass() )
        {
            return false;
        }

        ProgramValidation other = (ProgramValidation) obj;

        if ( leftSide == null )
        {
            if ( other.leftSide != null )
            {
                return false;
            }
        }
        else if ( !leftSide.equals( other.leftSide ) )
        {
            return false;
        }

        if ( program == null )
        {
            if ( other.program != null )
            {
                return false;
            }
        }
        else if ( !program.equals( other.program ) )
        {
            return false;
        }

        if ( rightSide == null )
        {
            if ( other.rightSide != null )
            {
                return false;
            }
        }
        else if ( !rightSide.equals( other.rightSide ) )
        {
            return false;
        }

        return true;
    }

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getLeftSide()
    {
        return leftSide;
    }

    public void setLeftSide( String leftSide )
    {
        this.leftSide = leftSide;
    }

    public String getRightSide()
    {
        return rightSide;
    }

    public void setRightSide( String rightSide )
    {
        this.rightSide = rightSide;
    }

    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    public Boolean getDateType()
    {
        return dateType;
    }

    public void setDateType( Boolean dateType )
    {
        this.dateType = dateType;
    }
}
