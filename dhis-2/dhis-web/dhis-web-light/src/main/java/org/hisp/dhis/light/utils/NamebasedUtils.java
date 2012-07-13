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

package org.hisp.dhis.light.utils;

import java.util.Collection;
import java.util.Set;

import org.hisp.dhis.api.mobile.IProgramService;
import org.hisp.dhis.api.mobile.model.Program;
import org.hisp.dhis.api.mobile.model.ProgramStage;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.program.ProgramStageInstance;

public class NamebasedUtils
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IProgramService programService;

    public void setProgramService( IProgramService programService )
    {
        this.programService = programService;
    }

    public ProgramStage getProgramStage( int programId, int programStageId )
    {
        Program program = programService.getProgram( programId, "" );

        Collection<ProgramStage> stages = program.getProgramStages();

        for ( ProgramStage programStage : stages )
        {
            if ( programStage.getId() == programStageId )
            {
                return programStage;
            }
        }
        return null;
    }

    public String getTypeViolation( DataElement dataElement, String value )
    {
        String type = dataElement.getType();
        String numberType = dataElement.getNumberType();

        if ( type.equals( DataElement.VALUE_TYPE_STRING ) )
        {
        }
        else if ( type.equals( DataElement.VALUE_TYPE_BOOL ) )
        {
            if ( !FormUtils.isBoolean( value ) )
            {
                return "is_invalid_boolean";
            }
        }
        else if ( type.equals( DataElement.VALUE_TYPE_DATE ) )
        {
            if ( !FormUtils.isDate( value ) )
            {
                return "is_invalid_date";
            }
        }
        else if ( type.equals( DataElement.VALUE_TYPE_INT ) && numberType.equals( DataElement.VALUE_TYPE_NUMBER ) )
        {
            if ( !FormUtils.isNumber( value ) )
            {
                return "is_invalid_number";
            }
        }
        else if ( type.equals( DataElement.VALUE_TYPE_INT ) && numberType.equals( DataElement.VALUE_TYPE_INT ) )
        {
            if ( !FormUtils.isInteger( value ) )
            {
                return "is_invalid_integer";
            }
        }
        else if ( type.equals( DataElement.VALUE_TYPE_INT ) && numberType.equals( DataElement.VALUE_TYPE_POSITIVE_INT ) )
        {
            if ( !FormUtils.isPositiveInteger( value ) )
            {
                return "is_invalid_positive_integer";
            }
        }
        else if ( type.equals( DataElement.VALUE_TYPE_INT ) && numberType.equals( DataElement.VALUE_TYPE_NEGATIVE_INT ) )
        {
            if ( !FormUtils.isNegativeInteger( value ) )
            {
                return "is_invalid_negative_integer";
            }
        }
        return null;
    }

    public ProgramStageInstance getNextStage( Set<ProgramStageInstance> programStageInstances )
    {
        for ( ProgramStageInstance programStageInstance : programStageInstances )
        {
            if ( !programStageInstance.isCompleted() )
            {
                return programStageInstance;
            }
        }
        return null;
    }
}
