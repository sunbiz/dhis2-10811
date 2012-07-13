package org.hisp.dhis.sms.namebaseddataentry.action;

import com.opensymphony.xwork2.Action;

public class ShowActivityTypeAction
    implements Action
{

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private String orgUnitId;

    public void setOrgUnitId( String orgUnitId )
    {
        this.orgUnitId = orgUnitId;
    }

    public String getOrgUnitId( String orgUnitId )
    {
        return this.orgUnitId;
    }
    
    private String organisationUnitId;

    public String getOrganisationUnitId()
    {
        return organisationUnitId;
    }

    public void setOrganisationUnitId( String organisationUnitId )
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
