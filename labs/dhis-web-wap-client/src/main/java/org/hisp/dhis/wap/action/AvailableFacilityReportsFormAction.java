/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hisp.dhis.wap.action;


import com.opensymphony.xwork2.Action;
import java.util.Collection;
import org.hisp.dhis.dataset.DataSet;

import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;

/**
 *
 * @author harsh
 */
public class AvailableFacilityReportsFormAction implements Action
{

    String facility;

    public String getFacility()
    {
        return facility;
    }

    public void setFacility( String facility )
    {
        this.facility = facility;
    }
    
    User user;
    OrganisationUnit orgUnit;
     Collection<DataSet> dataSetsForMobile;

    public Collection<DataSet> getDataSetsForMobile()
    {
        return dataSetsForMobile;
    }
    
    DataSetService dataSetService;
    CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    public void setOrgUnit( OrganisationUnit orgUnit )
    {
        this.orgUnit = orgUnit;
    }
    
    
    @Override
    public String execute() throws Exception
    {
       
        user=currentUserService.getCurrentUser();
       System.out.println("user--->"+user.getFirstName());
       orgUnit=user.getOrganisationUnit();
       System.out.println("orgunit---->"+orgUnit.getName());
       dataSetsForMobile = dataSetService.getDataSetsForMobile( orgUnit );
       System.out.println("--------->"+dataSetsForMobile.size()+"<----");
       
       return SUCCESS;
        //throw new UnsupportedOperationException( "Not supported yet." );
    }
    
}
