package org.hisp.dhis.report.impl;

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

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.amplecode.quick.StatementManager;
import org.hisp.dhis.common.GenericIdentifiableObjectStore;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.constant.ConstantService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportService;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.system.util.Filter;
import org.hisp.dhis.system.util.FilterUtils;
import org.hisp.dhis.system.util.JRExportUtils;
import org.hisp.dhis.system.util.StreamUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
@Transactional
public class DefaultReportService
    implements ReportService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private GenericIdentifiableObjectStore<Report> reportStore;

    public void setReportStore( GenericIdentifiableObjectStore<Report> reportStore )
    {
        this.reportStore = reportStore;
    }

    private ReportTableService reportTableService;

    public void setReportTableService( ReportTableService reportTableService )
    {
        this.reportTableService = reportTableService;
    }

    private ConstantService constantService;

    public void setConstantService( ConstantService constantService )
    {
        this.constantService = constantService;
    }

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }
    
    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    // -------------------------------------------------------------------------
    // ReportService implementation
    // -------------------------------------------------------------------------

    public void renderReport( OutputStream out, String reportUid, Date reportingPeriod,
        String organisationUnitUid, String type, I18nFormat format )
    {
        Report report = getReport( reportUid );
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.putAll( constantService.getConstantParameterMap() );

        try
        {
            JasperReport jasperReport = JasperCompileManager.compileReport( StreamUtils.getInputStream( report.getDesignContent() ) );

            JasperPrint print = null;

            if ( report.hasReportTable() ) // Use JR data source
            {
                ReportTable reportTable = report.getReportTable();

                Grid grid = reportTableService.getReportTableGrid( reportTable.getUid(), format, reportingPeriod, organisationUnitUid );

                if ( report.isUsingOrganisationUnitGroupSets() )
                {
                    params.putAll( reportTable.getOrganisationUnitGroupMap( organisationUnitGroupService.getCompulsoryOrganisationUnitGroupSets() ) );
                }

                print = JasperFillManager.fillReport( jasperReport, params, grid );
            }
            else // Assume SQL report and provide JDBC connection
            {
                Connection connection = statementManager.getHolder().getConnection();

                try
                {
                    print = JasperFillManager.fillReport( jasperReport, params, connection );
                } 
                finally
                {
                    connection.close();
                }
            }

            if ( print != null )
            {
                JRExportUtils.export( type, out, print );
            }
        } 
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to render report", ex );
        }
    }

    public int saveReport( Report report )
    {
        return reportStore.save( report );
    }

    public void deleteReport( Report report )
    {
        reportStore.delete( report );
    }

    public Collection<Report> getAllReports()
    {
        return reportStore.getAll();
    }

    public Report getReport( int id )
    {
        return reportStore.get( id );
    }

    public Report getReport( String uid )
    {
        return reportStore.getByUid( uid );
    }

    @Override
    public int getReportCount()
    {
        return reportStore.getCount();
    }

    @Override
    public int getReportCountByName( String name )
    {
        return reportStore.getCountByName( name );
    }
    
    @Override
    public Collection<Report> getReportsBetween( int first, int max )
    {
        return reportStore.getBetween( first, max );
    }

    @Override
    public Collection<Report> getReportsBetweenByName( String name, int first, int max )
    {
        return reportStore.getBetweenByName( name, first, max );
    }

    public Report getReportByName( String name )
    {
        return reportStore.getByName( name );
    }

    public Collection<Report> getReports( final Collection<Integer> identifiers )
    {
        Collection<Report> reports = getAllReports();

        return identifiers == null ? reports : FilterUtils.filter( reports, new Filter<Report>()
        {
            public boolean retain( Report object )
            {
                return identifiers.contains( object.getId() );
            }
        } );
    }
}
