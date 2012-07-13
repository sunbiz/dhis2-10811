package org.hisp.dhis.dashboard.hibernate;

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

import org.hisp.dhis.dashboard.DashboardContent;
import org.hisp.dhis.dashboard.DashboardContentStore;
import org.hisp.dhis.document.Document;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.hisp.dhis.mapping.MapView;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.reporttable.ReportTable;

/**
 * @author Lars Helge Overland
 */
public class HibernateDashboardContentStore
    extends HibernateGenericStore<DashboardContent> implements DashboardContentStore
{
    @SuppressWarnings("unchecked")
    public Collection<DashboardContent> getByDocument( Document document )
    {
        String hql = "from DashboardContent dc where :document in elements(dc.documents)";
        return getQuery( hql ).setEntity( "document", document ).list();        
    }

    @SuppressWarnings("unchecked")
    public Collection<DashboardContent> getByMapView( MapView mapView )
    {
        String hql = "from DashboardContent dc where :mapView in elements(dc.mapViews)";
        return getQuery( hql ).setEntity( "mapView", mapView ).list();
    }    

    @SuppressWarnings("unchecked")
    public Collection<DashboardContent> getByReport( Report report )
    {
        String hql = "from DashboardContent dc where :report in elements(dc.reports)";
        return getQuery( hql ).setEntity( "report", report ).list();
    }
    
    @SuppressWarnings("unchecked")
    public Collection<DashboardContent> getByReportTable( ReportTable reportTable )
    {
        String hql = "from DashboardContent dc where :reportTable in elements(dc.reportTables)";
        return getQuery( hql ).setEntity( "reportTable", reportTable ).list();
    }
}
