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

import static org.hisp.dhis.system.util.ConversionUtils.getIdentifiers;
import static org.hisp.dhis.system.util.TextUtils.getCommaDelimitedString;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import net.sf.jasperreports.engine.util.JRProperties;
import org.amplecode.quick.StatementManager;
import org.hisp.dhis.common.GenericIdentifiableObjectStore;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.constant.ConstantService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportService;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.system.util.Filter;
import org.hisp.dhis.system.util.FilterUtils;
import org.hisp.dhis.system.util.JRExportUtils;
import org.hisp.dhis.system.util.StreamUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
@Transactional
public class DefaultReportService
    implements ReportService
{
    public static final String ORGANISATIONUNIT_LEVEL_COLUMN_PREFIX = "idlevel";
    
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
    
    private OrganisationUnitService organisationUnitService;
    
    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }
    
    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private DataSource dataSource;

    public void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    // -------------------------------------------------------------------------
    // ReportService implementation
    // -------------------------------------------------------------------------

    public JasperPrint renderReport( OutputStream out, String reportUid, Period period,
        String organisationUnitUid, String type, I18nFormat format )
    {
        Report report = getReport( reportUid );
        
        Map<String, Object> params = new HashMap<String, Object>();

        params.putAll( constantService.getConstantParameterMap() );

        Date reportDate = new Date();
        
        if ( period != null )
        {
            params.put( PARAM_PERIOD_NAME, format.formatPeriod( period ) );
            
            reportDate = period.getStartDate();
        }
        
        OrganisationUnit orgUnit = organisationUnitService.getOrganisationUnit( organisationUnitUid );
        
        if ( orgUnit != null )
        {
            int level = organisationUnitService.getLevelOfOrganisationUnit( orgUnit.getId() );
            
            params.put( PARAM_ORGANISATIONUNIT_COLUMN_NAME, orgUnit.getName() );
            params.put( PARAM_ORGANISATIONUNIT_LEVEL, level );
            params.put( PARAM_ORGANISATIONUNIT_LEVEL_COLUMN, ORGANISATIONUNIT_LEVEL_COLUMN_PREFIX + level );
        }

        JasperPrint print = null;

        try
        {
            JRProperties.setProperty( "net.sf.jasperreports.awt.ignore.missing.font", "true" );
            JRProperties.setProperty( "net.sf.jasperreports.default.font.name", "DejaVu Sans" );

            JasperReport jasperReport = JasperCompileManager.compileReport( StreamUtils.getInputStream( report.getDesignContent() ) );

            if ( report.hasReportTable() ) // Use JR data source
            {
                ReportTable reportTable = report.getReportTable();

                Grid grid = reportTableService.getReportTableGrid( reportTable.getUid(), format, reportDate, organisationUnitUid );

                if ( report.isUsingOrganisationUnitGroupSets() )
                {
                    params.putAll( reportTable.getOrganisationUnitGroupMap( organisationUnitGroupService.getCompulsoryOrganisationUnitGroupSets() ) );
                }

                print = JasperFillManager.fillReport( jasperReport, params, grid );
            }
            else // Use JDBC data source
            {
                if ( report.hasRelativePeriods() )
                {
                    Collection<Period> periods = periodService.reloadPeriods( report.getRelatives().getRelativePeriods( reportDate, null, false ) );
                    String periodString = getCommaDelimitedString( getIdentifiers( Period.class, periods ) );
                    params.put( PARAM_RELATIVE_PERIODS, periodString );
                }
                
                if ( report.hasReportParams() && report.getReportParams().isParamOrganisationUnit() && orgUnit != null )
                {
                    params.put( PARAM_ORG_UNITS, String.valueOf( orgUnit.getId() ) );
                }

                Connection connection = DataSourceUtils.getConnection( dataSource );

                try
                {
                    print = JasperFillManager.fillReport( jasperReport, params, connection );
                }
                finally
                {                    
                    DataSourceUtils.releaseConnection( connection, dataSource );
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
        
        return print;
    }

    public int saveReport( Report report )
    {
        return reportStore.save( report );
    }

    public void deleteReport( Report report )
    {
        reportStore.delete( report );
    }

    public List<Report> getAllReports()
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
        return reportStore.getCountLikeName( name );
    }
    
    @Override
    public List<Report> getReportsBetween( int first, int max )
    {
        return reportStore.getAllOrderedName( first, max );
    }

    @Override
    public List<Report> getReportsBetweenByName( String name, int first, int max )
    {
        return reportStore.getAllLikeNameOrderedName( name, first, max );
    }

    public List<Report> getReportByName( String name )
    {
        return reportStore.getAllEqName( name );
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
