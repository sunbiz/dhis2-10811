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

package org.hisp.dhis.caseentry.action.caseentry;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.hisp.dhis.caseentry.state.SelectedStateManager;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.paging.ActionPagingSupport;
import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;

/**
 * @author Chau Thu Tran
 * 
 * @version GetActivityPlansAction.java 7:10:29 PM Mar 4, 2013 $
 */
public class GetActivityPlansAction
    extends ActionPagingSupport<ProgramStageInstance>
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    private ProgramService programService;

    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }

    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private Integer programId;

    public void setProgramId( Integer programId )
    {
        this.programId = programId;
    }

    private Collection<Integer> statusList;

    public void setStatusList( Collection<Integer> statusList )
    {
        this.statusList = statusList;
    }

    private String startDate;

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }

    private String endDate;

    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }

    private String facilityLB;

    public void setFacilityLB( String facilityLB )
    {
        this.facilityLB = facilityLB;
    }

    private String type;

    public void setType( String type )
    {
        this.type = type;
    }

    private Integer total;

    public Integer getTotal()
    {
        return total;
    }

    private Program program;

    public Program getProgram()
    {
        return program;
    }

    private List<PatientIdentifierType> identifierTypes;

    public List<PatientIdentifierType> getIdentifierTypes()
    {
        return identifierTypes;
    }

    private List<ProgramStageInstance> programStageInstances;

    public List<ProgramStageInstance> getProgramStageInstances()
    {
        return programStageInstances;
    }

    private Grid grid;

    public Grid getGrid()
    {
        return grid;
    }

    // -------------------------------------------------------------------------
    // Implementation Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        OrganisationUnit orgunit = selectedStateManager.getSelectedOrganisationUnit();

        // ---------------------------------------------------------------------
        // Get orgunitIds
        // ---------------------------------------------------------------------

        Collection<Integer> orgunitIds = new HashSet<Integer>();

        if ( facilityLB.equals( "selected" ) )
        {
            orgunitIds.add( orgunit.getId() );
        }
        else if ( facilityLB.equals( "childrenOnly" ) )
        {
            orgunitIds.addAll( organisationUnitService.getOrganisationUnitHierarchy().getChildren( orgunit.getId() ) );
            orgunitIds.remove( orgunit.getId() );
        }
        else
        {
            orgunitIds.addAll( organisationUnitService.getOrganisationUnitHierarchy().getChildren( orgunit.getId() ) );
        }

        // ---------------------------------------------------------------------
        // Program instances for the selected program
        // ---------------------------------------------------------------------

        program = programService.getProgram( programId );

        if ( type == null )
        {
            identifierTypes = program.getPatientIdentifierTypes();

            total = programStageInstanceService.getActiveInstanceCount( program, orgunitIds,
                format.parseDate( startDate ), format.parseDate( endDate ), statusList );

            this.paging = createPaging( total );

            programStageInstances = programStageInstanceService.activityPlanList( program, orgunitIds,
                format.parseDate( startDate ), format.parseDate( endDate ), statusList, paging.getStartPos(),
                paging.getPageSize() );
        }
        else
        {
            grid = programStageInstanceService.activityPlans( program, orgunitIds, format.parseDate( startDate ),
                format.parseDate( endDate ), statusList, null, null, i18n );
        }
        return type == null ? SUCCESS : type;
    }
}
