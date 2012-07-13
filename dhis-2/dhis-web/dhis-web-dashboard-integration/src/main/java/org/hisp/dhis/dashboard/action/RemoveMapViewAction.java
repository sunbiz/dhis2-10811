package org.hisp.dhis.dashboard.action;

import org.hisp.dhis.dashboard.DashboardContent;
import org.hisp.dhis.dashboard.DashboardService;
import org.hisp.dhis.mapping.MapView;
import org.hisp.dhis.mapping.MappingService;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;

import com.opensymphony.xwork2.Action;

public class RemoveMapViewAction
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

    private DashboardService dashboardService;

    public void setDashboardService( DashboardService dashboardService )
    {
        this.dashboardService = dashboardService;
    }
    
    private MappingService mappingService;

    public void setMappingService( MappingService mappingService )
    {
        this.mappingService = mappingService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        User user = currentUserService.getCurrentUser();
        
        if ( user != null )
        {
            DashboardContent content = dashboardService.getDashboardContent( user );
            
            MapView mapView = mappingService.getMapView( id );
            
            if ( content.getMapViews().remove( mapView ) )
            {
                dashboardService.saveDashboardContent( content );
            }
        }
        
        return SUCCESS;
    }
}
