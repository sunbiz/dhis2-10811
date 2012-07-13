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

package org.hisp.dhis.patient.action.programstage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.hisp.dhis.program.ProgramStageDataElementService;
import org.hisp.dhis.program.ProgramStageService;

import com.opensymphony.xwork2.Action;

/**
 * @author Abyot Asalefew Gizaw
 * @modified Tran Thanh Tri
 * @version $Id$
 */
public class UpdateProgramStageAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramStageService programStageService;

    public void setProgramStageService( ProgramStageService programStageService )
    {
        this.programStageService = programStageService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private ProgramStageDataElementService programStageDataElementService;

    public void setProgramStageDataElementService( ProgramStageDataElementService programStageDataElementService )
    {
        this.programStageDataElementService = programStageDataElementService;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private int id;

    public void setId( int id )
    {
        this.id = id;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }

    private Integer minDaysFromStart;

    public void setMinDaysFromStart( Integer minDaysFromStart )
    {
        this.minDaysFromStart = minDaysFromStart;
    }

    private List<Integer> selectedDataElementsValidator = new ArrayList<Integer>();

    public void setSelectedDataElementsValidator( List<Integer> selectedDataElementsValidator )
    {
        this.selectedDataElementsValidator = selectedDataElementsValidator;
    }

    private List<Boolean> compulsories = new ArrayList<Boolean>();

    public void setCompulsories( List<Boolean> compulsories )
    {
        this.compulsories = compulsories;
    }
 private List<Boolean> allowProvidedElsewhere = new ArrayList<Boolean>();
    
    public void setAllowProvidedElsewhere( List<Boolean> allowProvidedElsewhere )
    {
        this.allowProvidedElsewhere = allowProvidedElsewhere;
    }
    private int programId;

    public int getProgramId()
    {
        return programId;
    }

    private Boolean irregular;

    public void setIrregular( Boolean irregular )
    {
        this.irregular = irregular;
    }

    private Integer standardInterval;

    public void setStandardInterval( Integer standardInterval )
    {
        this.standardInterval = standardInterval;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        ProgramStage programStage = programStageService.getProgramStage( id );

        programStage.setName( name );
        programStage.setDescription( description );
        programStage.setStandardInterval( standardInterval );

        minDaysFromStart = (minDaysFromStart == null) ? 0 : minDaysFromStart;
        programStage.setMinDaysFromStart( minDaysFromStart );

        irregular = (irregular == null) ? false : irregular;
        programStage.setIrregular( irregular );

        programStageService.updateProgramStage( programStage );

        Set<ProgramStageDataElement> programStageDataElements = new HashSet<ProgramStageDataElement>( programStage
            .getProgramStageDataElements() );

        for ( int i = 0; i < this.selectedDataElementsValidator.size(); i++ )
        {
            DataElement dataElement = dataElementService.getDataElement( selectedDataElementsValidator.get( i ) );

            ProgramStageDataElement programStageDataElement = programStageDataElementService.get( programStage,
                dataElement );

            if ( programStageDataElement == null )
            {
                programStageDataElement = new ProgramStageDataElement( programStage, dataElement, this.compulsories
                    .get( i ), new Integer( i ) );
                programStageDataElementService.addProgramStageDataElement( programStageDataElement );
            }
            else
            {
                programStageDataElement.setCompulsory( this.compulsories.get( i ) );

                programStageDataElement.setSortOrder( new Integer( i ) );

                programStageDataElement.setAllowProvidedElsewhere( allowProvidedElsewhere.get( i ) );
                
                programStageDataElementService.updateProgramStageDataElement( programStageDataElement );

                programStageDataElements.remove( programStageDataElement );
            }
        }

        for ( ProgramStageDataElement psdeDelete : programStageDataElements )
        {
            programStageDataElementService.deleteProgramStageDataElement( psdeDelete );
        }

        programId = programStage.getProgram().getId();

        return SUCCESS;
    }
}
