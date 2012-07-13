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

package org.hisp.dhis.program;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.validation.ValidationCriteria;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
@Transactional
public class DefaultProgramService
    implements ProgramService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramStore programStore;

    public void setProgramStore( ProgramStore programStore )
    {
        this.programStore = programStore;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    public int saveProgram( Program program )
    {
        return programStore.save( program );
    }

    public void updateProgram( Program program )
    {
        programStore.update( program );
    }

    public void deleteProgram( Program program )
    {
        programStore.delete( program );
    }

    public Collection<Program> getAllPrograms()
    {
        return programStore.getAll();
    }

    public Program getProgram( int id )
    {
        return programStore.get( id );
    }

    public Program getProgramByName( String name )
    {
        return programStore.getByName( name );
    }

    public Collection<Program> getPrograms( OrganisationUnit organisationUnit )
    {
        Set<Program> programs = new HashSet<Program>();

        for ( Program program : getAllPrograms() )
        {
            if ( program.getOrganisationUnits().contains( organisationUnit ) )
            {
                programs.add( program );
            }
        }

        return programs;
    }

    public Collection<Program> getPrograms( ValidationCriteria validationCriteria )
    {
        Set<Program> programs = new HashSet<Program>();

        for ( Program program : getAllPrograms() )
        {
            if ( program.getPatientValidationCriteria().contains( validationCriteria ) )
            {
                programs.add( program );
            }
        }

        return programs;
    }
    
    public Collection<Program> getPrograms( int type )
    {
        return programStore.getByType( type );
    }
    
    public Collection<Program> getPrograms( int type, OrganisationUnit orgunit )
    {
        return programStore.get( type, orgunit );
    }

}
