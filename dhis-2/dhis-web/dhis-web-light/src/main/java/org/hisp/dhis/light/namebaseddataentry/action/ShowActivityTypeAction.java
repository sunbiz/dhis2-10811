package org.hisp.dhis.light.namebaseddataentry.action;

import com.opensymphony.xwork2.Action;

public class ShowActivityTypeAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private Integer orgUnitId;

    public void setOrgUnitId( Integer orgUnitId )
    {
        this.orgUnitId = orgUnitId;
    }

    public Integer getOrgUnitId( Integer orgUnitId )
    {
        return this.orgUnitId;
    }
    
    private Integer organisationUnitId;

    public Integer getOrganisationUnitId()
    {
        return organisationUnitId;
    }

    public void setOrganisationUnitId( Integer organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }

    @Override
    public String execute()
        throws Exception
    {
        this.organisationUnitId = orgUnitId;
        
        return SUCCESS;
    }
}
