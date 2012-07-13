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

package org.hisp.dhis.sms.namebaseddataentry.action;

import java.util.List;
import org.hisp.dhis.api.mobile.model.Activity;
import org.hisp.dhis.api.mobile.model.ActivityPlan;
import org.hisp.dhis.api.mobile.model.DataElement;
import org.hisp.dhis.api.mobile.model.Program;
import org.hisp.dhis.api.mobile.model.ProgramStage;
import org.hisp.dhis.sms.utils.NamebasedUtils;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

import com.opensymphony.xwork2.Action;

public class GetProgramStageFormAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private NamebasedUtils util;

    public void setUtil( NamebasedUtils util )
    {
        this.util = util;
    }

    public NamebasedUtils getUtil()
    {
        return util;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private String programStageInstanceId;

    public String getProgramStageInstanceId()
    {
        return programStageInstanceId;
    }

    public void setProgramStageInstanceId( String programStageInstanceId )
    {
        this.programStageInstanceId = programStageInstanceId;
    }

    private String beneficiaryId;

    public void setBeneficiaryId( String beneficiaryId )
    {
        this.beneficiaryId = beneficiaryId;
    }

    public String getBeneficiaryId()
    {
        return this.beneficiaryId;
    }

    private OrganisationUnit organisationUnit;

    public OrganisationUnit getOrganisationUnit()
    {
        return this.organisationUnit;
    }
    
    
    public void setOrganisationUnit( OrganisationUnit organisationUnit )
    {
        this.organisationUnit = organisationUnit;
    }

    private List<DataElement> dataElements;

    public List<DataElement> getDataElements()
    {
        return this.dataElements;
    }

    private ProgramStage programStage;

    public ProgramStage getProgramStage()
    {
        return this.programStage;
    }

    private String programId;

    public void setProgramId( String programId )
    {
        this.programId = programId;
    }

    public String getProgramId()
    {
        return programId;
    }

    private String programStageId;

    public void setProgramStageId( String programStageId )
    {
        this.programStageId = programStageId;
    }

    public String getProgramStageId()
    {
        return programStageId;
    }

    private String orgUnitId;

    public void setOrgUnitId( String orgUnitId )
    {
        this.orgUnitId = orgUnitId;
    }

    public String getOrgUnitId()
    {
        return this.orgUnitId;
    }

    private ActivityPlan activityPlan;

    public ActivityPlan getActivityPlan()
    {
        return this.activityPlan;
    }

    private List<Activity> activities;

    public List<Activity> getActivities()
    {
        return this.activities;
    }

    private Program program;

    public Program getProgram()
    {
        return this.program;
    }
    
    private boolean current;
    
    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    public boolean isCurrent()
    {
        return current;
    }

    public void setCurrent( boolean current )
    {
        this.current = current;
    }

    @Override
    public String execute()
        throws Exception
    {
        organisationUnit = organisationUnitService.getOrganisationUnit( Integer.parseInt( orgUnitId ) );

        programStage = util.getProgramStage( Integer.parseInt( programId ), Integer.parseInt( programStageId ) );

        dataElements = programStage.getDataElements();

        return SUCCESS;
    }
}
