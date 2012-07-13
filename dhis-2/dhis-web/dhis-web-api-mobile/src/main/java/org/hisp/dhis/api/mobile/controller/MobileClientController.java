package org.hisp.dhis.api.mobile.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hisp.dhis.api.mobile.NotAllowedException;
import org.hisp.dhis.api.mobile.model.MobileOrgUnitLinks;
import org.hisp.dhis.api.mobile.model.OrgUnits;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( value = "/mobile" )
public class MobileClientController extends AbstractMobileController
{
    @Autowired
    private CurrentUserService currentUserService;

    @RequestMapping( method = RequestMethod.GET )
    @ResponseBody
    public OrgUnits getOrgUnitsForUser( HttpServletRequest request )
        throws NotAllowedException
    {
        User user = currentUserService.getCurrentUser();

        if ( user == null )
        {
            throw NotAllowedException.NO_USER;
        }

        Collection<OrganisationUnit> units = user.getOrganisationUnits();

        List<MobileOrgUnitLinks> unitList = new ArrayList<MobileOrgUnitLinks>();
        for ( OrganisationUnit unit : units )
        {
            unitList.add( getOrgUnit( unit, request ) );
        }
       
        return new OrgUnits( unitList );
    }

    private MobileOrgUnitLinks getOrgUnit( OrganisationUnit unit, HttpServletRequest request )
    {
        MobileOrgUnitLinks orgUnit = new MobileOrgUnitLinks();

        orgUnit.setId( unit.getId() );
        orgUnit.setName( unit.getShortName() );

        orgUnit.setDownloadAllUrl( getUrl( request, unit.getId(), "all" ) );
        orgUnit.setUpdateActivityPlanUrl( getUrl( request, unit.getId(), "activitiyplan" ) );
        orgUnit.setUploadFacilityReportUrl( getUrl( request, unit.getId(), "dataSets" ) );
        orgUnit.setUploadActivityReportUrl( getUrl( request, unit.getId(), "activities" ) );
        orgUnit.setUpdateDataSetUrl( getUrl( request, unit.getId(), "updateDataSets" ) );
        orgUnit.setChangeUpdateDataSetLangUrl( getUrl( request, unit.getId(), "changeLanguageDataSet" ) );
        orgUnit.setSearchUrl( getUrl( request, unit.getId(), "search" ) );
        
        //generate URL for download new version
        String full = UrlUtils.buildFullRequestUrl( request );
        String root = full.substring( 0, full.length() - UrlUtils.buildRequestUrl( request ).length());
        String updateNewVersionUrl = root + "/dhis-web-api-mobile/updateClient.action";
        orgUnit.setUpdateNewVersionUrl( updateNewVersionUrl );
        return orgUnit;
    }

    private static String getUrl( HttpServletRequest request, int id, String path )
    {
        String url = UrlUtils.buildFullRequestUrl( request );
        url = url + "/orgUnits/" + id + "/" + path;
        return url;
    }
}
