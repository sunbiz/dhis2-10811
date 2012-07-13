package org.hisp.dhis.dashboard;

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

import java.util.Collection;

import org.hisp.dhis.document.Document;
import org.hisp.dhis.mapping.MapView;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.system.deletion.DeletionHandler;
import org.hisp.dhis.user.User;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DashboardContentDeletionHandler
    extends DeletionHandler
{
    private DashboardService dashboardService;

    public void setDashboardService( DashboardService dashboardService )
    {
        this.dashboardService = dashboardService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    public String getClassName()
    {
        return DashboardContent.class.getSimpleName();
    }
    
    @Override
    public void deleteReport( Report report )
    {
        Collection<DashboardContent> contents = dashboardService.getByReport( report );
        
        for ( DashboardContent content : contents )
        {
            content.getReports().remove( report );
            dashboardService.updateDashboardContent( content );
        }
    }
            
    @Override
    public void deleteDocument( Document document )
    {
        Collection<DashboardContent> contents = dashboardService.getByDocument( document );
        
        for ( DashboardContent content : contents )
        {
            content.getDocuments().remove( document );
            dashboardService.updateDashboardContent( content );
            
        }
    }
    
    @Override
    public void deleteReportTable( ReportTable reportTable )
    {
        Collection<DashboardContent> contents = dashboardService.getByReportTable( reportTable );
        
        for ( DashboardContent content : contents )
        {
            content.getReportTables().remove( reportTable );
            dashboardService.updateDashboardContent( content );
        }        
    }
    
    @Override
    public void deleteMapView( MapView mapView )
    {
        Collection<DashboardContent> contents = dashboardService.getByMapView( mapView );
        
        for ( DashboardContent content : contents )
        {
            content.getMapViews().remove( mapView );
            dashboardService.updateDashboardContent( content );
        }
    }
    
    @Override
    public void deleteUser( User user )
    {
        DashboardContent content = dashboardService.getDashboardContent( user );
        
        dashboardService.deleteDashboardContent( content );
    }
}
