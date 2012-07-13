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

package org.hisp.dhis.patient.action.patientidentifiertype;

import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.patient.PatientIdentifierTypeService;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramService;

import com.opensymphony.xwork2.Action;

/**
 * @author Viet
 * @version $Id$
 */
public class UpdatePatientIdentifierTypeAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private PatientIdentifierTypeService patientIdentifierTypeService;

    private ProgramService programService;

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private Integer id;

    private String name;

    private String description;

    private Boolean mandatory;

    private Boolean related;

    private Integer noChars;

    private String type;

    private Integer programId;

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public void setNoChars( Integer noChars )
    {
        this.noChars = noChars;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }

    public void setPatientIdentifierTypeService( PatientIdentifierTypeService patientIdentifierTypeService )
    {
        this.patientIdentifierTypeService = patientIdentifierTypeService;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public void setMandatory( Boolean mandatory )
    {
        this.mandatory = mandatory;
    }

    public void setRelated( Boolean related )
    {
        this.related = related;
    }

    public void setProgramId( Integer programId )
    {
        this.programId = programId;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        PatientIdentifierType identifierType = patientIdentifierTypeService.getPatientIdentifierType( id );
        if ( identifierType != null )
        {
            identifierType.setName( name );
            identifierType.setDescription( description );
            
            related = (related == null) ? false : true;
            identifierType.setRelated( related );
            
            mandatory = (mandatory == null) ? false : true;
            identifierType.setMandatory( mandatory );
            
            identifierType.setNoChars( noChars );
            identifierType.setType( type );

            Program program = (programId != null) ? programService.getProgram( programId ) : null;
            identifierType.setProgram( program );

            patientIdentifierTypeService.updatePatientIdentifierType( identifierType );
        }

        return SUCCESS;
    }
}