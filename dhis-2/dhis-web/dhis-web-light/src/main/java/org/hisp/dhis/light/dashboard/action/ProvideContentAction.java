package org.hisp.dhis.light.dashboard.action;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.dashboard.DashboardConfiguration;
import org.hisp.dhis.dashboard.DashboardContent;
import org.hisp.dhis.dashboard.DashboardManager;
import org.hisp.dhis.dashboard.DashboardService;
import org.hisp.dhis.document.Document;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;

import com.opensymphony.xwork2.Action;

public class ProvideContentAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DashboardService dashboardService;

    public void setDashboardService( DashboardService dashboardService )
    {
        this.dashboardService = dashboardService;
    }

    private DashboardManager dashboardManager;

    public void setDashboardManager( DashboardManager dashboardManager )
    {
        this.dashboardManager = dashboardManager;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<ReportTable> reportTables;

    public List<ReportTable> getReportTables()
    {
        return reportTables;
    }

    private List<Document> documents;

    public List<Document> getDocuments()
    {
        return documents;
    }

    private List<String> charts = new ArrayList<String>();

    public List<String> getCharts()
    {
        return charts;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        DashboardConfiguration config = dashboardManager.getConfiguration();

        for ( int i = 0; i < 8; i++ )
        {
            String id = config.getAreaItems().get( DashboardManager.CHART_AREA_PREFIX + (i + 1) );

            if ( id != null )
            {
                charts.add( id );
            }
        }

        User user = currentUserService.getCurrentUser();

        DashboardContent content = dashboardService.getDashboardContent( user );

        reportTables = content.getReportTables();

        documents = content.getDocuments();

        return SUCCESS;
    }
}
