package org.hisp.dhis.light.anonymous.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import com.opensymphony.xwork2.Action;

public class SearchOrgUnitAction
    implements Action
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private OrganisationUnitService organisationUnitService;

    public OrganisationUnitService getOrganisationUnitService()
    {
        return organisationUnitService;
    }

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    public CurrentUserService getCurrentUserService()
    {
        return currentUserService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();

    public List<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    private String keyword;

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword( String keyword )
    {
        this.keyword = keyword;
    }

    @Override
    public String execute()
        throws Exception
    {
        User user = currentUserService.getCurrentUser();

        if ( keyword == null )
            keyword = "";

        if ( user != null )
        {
            organisationUnits = new ArrayList<OrganisationUnit>( user.getOrganisationUnits() );
            organisationUnitService.searchOrganisationUnitByName( organisationUnits, keyword );
            Collections.sort( organisationUnits, IdentifiableObjectNameComparator.INSTANCE );
        }

        return SUCCESS;
    }

}
