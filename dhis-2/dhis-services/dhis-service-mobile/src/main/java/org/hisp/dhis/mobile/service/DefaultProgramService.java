package org.hisp.dhis.mobile.service;

/*
 * Copyright (c) 2010, University of Oslo
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.api.mobile.IProgramService;
import org.hisp.dhis.api.mobile.model.DataElement;
import org.hisp.dhis.api.mobile.model.Model;
import org.hisp.dhis.api.mobile.model.ModelList;
import org.hisp.dhis.api.mobile.model.Program;
import org.hisp.dhis.api.mobile.model.ProgramStage;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.springframework.beans.factory.annotation.Required;

public class DefaultProgramService
    implements IProgramService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private org.hisp.dhis.program.ProgramService programService;

    private org.hisp.dhis.mobile.service.ModelMapping modelMapping;

    // -------------------------------------------------------------------------
    // ProgramService
    // -------------------------------------------------------------------------

    public List<Program> getPrograms( OrganisationUnit unit, String localeString )
    {
        List<Program> programs = new ArrayList<Program>();

        for ( org.hisp.dhis.program.Program program : programService.getPrograms( unit ) )
        {
            programs.add( getProgram( program.getId(), localeString ) );
        }

        return programs;
    }

    public List<Program> updateProgram( ModelList programsFromClient, String localeString, OrganisationUnit unit )
    {
        List<Program> programs = new ArrayList<Program>();
        boolean isExisted = false;

        // Get all Program belong to this OrgUnit
        List<Program> serverPrograms = this.getPrograms( unit, localeString );
        for ( int i = 0; i < serverPrograms.size(); i++ )
        {
            Program program = serverPrograms.get( i );

            // Loop thought the list of program from client
            for ( int j = 0; j < programsFromClient.getModels().size(); j++ )
            {
                Model model = programsFromClient.getModels().get( j );
                if ( program.getId() == model.getId() )
                {
                    // Version is different
                    if ( program.getVersion() != Integer.parseInt( model.getName() ) )
                    {
                        programs.add( program );
                        isExisted = true;
                    }
                }
            }
            // Server has more program than client
            if ( isExisted == false )
            {
                programs.add( program );
            }
        }
        return programs;
    }

    public Program getProgram( int programId, String localeString )
    {
        org.hisp.dhis.program.Program program = programService.getProgram( programId );

        //program = i18n( i18nService, locale, program );

        Program pr = new Program();

        pr.setId( program.getId() );
        pr.setName( program.getName() );
        pr.setVersion( program.getVersion() );

        List<ProgramStage> prStgs = new ArrayList<ProgramStage>();

        for ( org.hisp.dhis.program.ProgramStage programStage : program.getProgramStages() )
        {
            //programStage = i18n( i18nService, locale, programStage );

            ProgramStage prStg = new ProgramStage();

            prStg.setId( programStage.getId() );

            prStg.setName( programStage.getName() );

            List<DataElement> des = new ArrayList<DataElement>();

            Set<ProgramStageDataElement> programStageDataElements =  programStage.getProgramStageDataElements();

            for ( ProgramStageDataElement programStagedataElement : programStageDataElements )
            {
                //programStagedataElement = i18n( i18nService, locale, programStagedataElement );

                org.hisp.dhis.dataelement.DataElement dataElement = programStagedataElement.getDataElement();

                DataElement de = modelMapping.getDataElement( dataElement );

                de.setCompulsory( programStagedataElement.isCompulsory() );

                des.add( de );
            }

            prStg.setDataElements( des );

            prStgs.add( prStg );

        }

        pr.setProgramStages( prStgs );

        return pr;
    }

    @Required
    public void setProgramService( org.hisp.dhis.program.ProgramService programService )
    {
        this.programService = programService;
    }

    @Required
    public void setModelMapping( org.hisp.dhis.mobile.service.ModelMapping modelMapping )
    {
        this.modelMapping = modelMapping;
    }
}
