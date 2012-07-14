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

package org.hisp.dhis.caseentry.action.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;
import org.hisp.dhis.paging.ActionPagingSupport;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.ProgramStageService;

/**
 * @author Chau Thu Tran
 * 
 * @version StatisticalProgramDetailsReportAction.java 12:37:43 PM Dec 16, 2012
 *          $
 */
public class StatisticalProgramDetailsReportAction
    extends ActionPagingSupport<Patient>
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramStageService programStageService;

    public void setProgramStageService( ProgramStageService programStageService )
    {
        this.programStageService = programStageService;
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

    private OrganisationUnitSelectionManager selectionManager;

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private Integer programStageId;

    public void setProgramStageId( Integer programStageId )
    {
        this.programStageId = programStageId;
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

    private Integer status;

    public void setStatus( Integer status )
    {
        this.status = status;
    }

    private String facilityLB;

    public void setFacilityLB( String facilityLB )
    {
        this.facilityLB = facilityLB;
    }

    private Integer total;

    public void setTotal( Integer total )
    {
        this.total = total;
    }

    private ProgramStage programStage;

    public ProgramStage getProgramStage()
    {
        return programStage;
    }

    private List<ProgramStageInstance> programStageInstances = new ArrayList<ProgramStageInstance>();

    public List<ProgramStageInstance> getProgramStageInstances()
    {
        return programStageInstances;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        OrganisationUnit organisationUnit = selectionManager.getSelectedOrganisationUnit();

        programStage = programStageService.getProgramStage( programStageId );

        Date sDate = format.parseDate( startDate );

        Date eDate = format.parseDate( endDate );

        // ---------------------------------------------------------------------
        // Get orgunitIds
        // ---------------------------------------------------------------------

        Collection<Integer> orgunitIds = new HashSet<Integer>();

        if ( facilityLB.equals( "selected" ) )
        {
            orgunitIds.add( organisationUnit.getId() );
        }
        else if ( facilityLB.equals( "childrenOnly" ) )
        {
            orgunitIds.addAll( organisationUnitService.getOrganisationUnitHierarchy().getChildren(
                organisationUnit.getId() ) );
            orgunitIds.remove( organisationUnit.getId() );
        }
        else
        {
            orgunitIds.addAll( organisationUnitService.getOrganisationUnitHierarchy().getChildren(
                organisationUnit.getId() ) );
        }

        this.paging = createPaging( total );
        
        programStageInstances = programStageInstanceService.getStatisticalProgramStageDetailsReport( programStage,
            orgunitIds, sDate, eDate, status, paging.getStartPos(), paging.getPageSize() );

        return SUCCESS;
    }
}
