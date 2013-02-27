package org.hisp.dhis.api.controller;

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

import static org.hisp.dhis.common.IdentifiableObjectUtils.getUids;
import static org.hisp.dhis.system.util.CodecUtils.filenameEncode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.api.utils.ContextUtils.CacheStrategy;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.dxf2.utils.JacksonUtils;
import org.hisp.dhis.i18n.I18nManager;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Cal;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.system.grid.GridUtils;
import org.hisp.dhis.system.util.DateUtils;
import org.hisp.dhis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 * @author Lars Helge Overland
 */
@Controller
@RequestMapping( value = ReportTableController.RESOURCE_PATH )
public class ReportTableController
    extends AbstractCrudController<ReportTable>
{
    public static final String RESOURCE_PATH = "/reportTables";

    private static final String DATA_NAME = "data";

    @Autowired
    public ReportTableService reportTableService;

    @Autowired
    private OrganisationUnitService organisationUnitService;
    
    @Autowired
    private OrganisationUnitGroupService organisationUnitGroupService;

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private PeriodService periodService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private I18nManager i18nManager;

    @Autowired
    private ContextUtils contextUtils;

    //--------------------------------------------------------------------------
    // CRUD
    //--------------------------------------------------------------------------

    @Override
    @RequestMapping( method = RequestMethod.POST, consumes = "application/json" )
    public void postJsonObject( HttpServletResponse response, HttpServletRequest request, InputStream input ) throws Exception
    {
        ReportTable reportTable = JacksonUtils.fromJson( input, ReportTable.class );
        System.out.println("rt " + reportTable.getName());
        System.out.println(reportTable.getColumnDimensions());
        
        reportTable.readPresentationProps();

        mergeReportTable( reportTable );
        
        reportTableService.saveReportTable( reportTable );

        ContextUtils.createdResponse( response, "Report table created", RESOURCE_PATH + "/" + reportTable.getUid() );
    }
    
    @Override
    @RequestMapping( value = "/{uid}", method = RequestMethod.PUT, consumes = "application/json" )
    @ResponseStatus( value = HttpStatus.NO_CONTENT )
    public void putJsonObject( HttpServletResponse response, HttpServletRequest request, @PathVariable( "uid" ) String uid, InputStream input ) throws Exception
    {
        ReportTable reportTable = reportTableService.getReportTable( uid );
        
        if ( reportTable == null )
        {
            ContextUtils.notFoundResponse( response, "Report table does not exist: " + uid );
            return;
        }        

        ReportTable newReportTable = JacksonUtils.fromJson( input, ReportTable.class );
        
        newReportTable.readPresentationProps();
        
        mergeReportTable( newReportTable );

        reportTable.mergeWith( newReportTable );
        
        reportTableService.updateReportTable( reportTable );
    }
    
    @Override
    @RequestMapping( value = "/{uid}", method = RequestMethod.DELETE )
    @ResponseStatus( value = HttpStatus.NO_CONTENT )
    public void deleteObject( HttpServletResponse response, HttpServletRequest request, @PathVariable( "uid" ) String uid ) throws Exception
    {
        ReportTable reportTable = reportTableService.getReportTable( uid );
        
        if ( reportTable == null )
        {
            ContextUtils.notFoundResponse( response, "Report table does not exist: " + uid );
            return;
        }
        
        reportTableService.deleteReportTable( reportTable );
    }
    
    @Override
    protected void postProcessEntity( ReportTable reportTable )
    {
        reportTable.populatePresentationProps();
    }
    
    //--------------------------------------------------------------------------
    // GET - Dynamic data
    //--------------------------------------------------------------------------

    @RequestMapping( value = "/data", method = RequestMethod.GET ) // For json, jsonp
    public String getReportTableDynamicData( @RequestParam( required = false, value = "in" ) List<String> indicators,
        @RequestParam( required = false, value = "de" ) List<String> dataElements,
        @RequestParam( required = false, value = "ds" ) List<String> dataSets,
        @RequestParam( value = "ou" ) List<String> orgUnits,
        @RequestParam( required = false, value = "crosstab" ) List<String> crossTab,
        @RequestParam( required = false ) boolean orgUnitIsParent,
        @RequestParam( required = false ) boolean minimal,
        RelativePeriods relatives,
        Model model,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableDynamicGrid( indicators, dataElements, dataSets,
            orgUnits, crossTab, orgUnitIsParent, minimal, relatives, response );

        model.addAttribute( "model", grid );
        model.addAttribute( "viewClass", "detailed" );        
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_JSON, CacheStrategy.RESPECT_SYSTEM_SETTING );

        return grid != null ? "reportTableData" : null;
    }

    @RequestMapping( value = "/data.html", method = RequestMethod.GET )
    public void getReportTableDynamicDataHtml( @RequestParam( required = false, value = "in" ) List<String> indicators,
        @RequestParam( required = false, value = "de" ) List<String> dataElements,
        @RequestParam( required = false, value = "ds" ) List<String> dataSets,
        @RequestParam( value = "ou" ) List<String> orgUnits,
        @RequestParam( required = false, value = "crosstab" ) List<String> crossTab,
        @RequestParam( required = false ) boolean orgUnitIsParent,
        @RequestParam( required = false ) boolean minimal,
        RelativePeriods relatives,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableDynamicGrid( indicators, dataElements, dataSets,
            orgUnits, crossTab, orgUnitIsParent, minimal, relatives, response );

        String filename = DATA_NAME + ".html";
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_HTML, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, false );

        GridUtils.toHtml( grid, response.getWriter() );
    }

    @RequestMapping( value = "/data.xml", method = RequestMethod.GET )
    public void getReportTableDynamicDataXml( @RequestParam( required = false, value = "in" ) List<String> indicators,
        @RequestParam( required = false, value = "de" ) List<String> dataElements,
        @RequestParam( required = false, value = "ds" ) List<String> dataSets,
        @RequestParam( value = "ou" ) List<String> orgUnits,
        @RequestParam( required = false, value = "crosstab" ) List<String> crossTab,
        @RequestParam( required = false ) boolean orgUnitIsParent,
        @RequestParam( required = false ) boolean minimal,
        RelativePeriods relatives,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableDynamicGrid( indicators, dataElements, dataSets,
            orgUnits, crossTab, orgUnitIsParent, minimal, relatives, response );

        String filename = DATA_NAME + ".xml";
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_XML, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, false );

        GridUtils.toXml( grid, response.getOutputStream() );
    }

    @RequestMapping( value = "/data.pdf", method = RequestMethod.GET )
    public void getReportTableDynamicDataPdf( @RequestParam( required = false, value = "in" ) List<String> indicators,
        @RequestParam( required = false, value = "de" ) List<String> dataElements,
        @RequestParam( required = false, value = "ds" ) List<String> dataSets,
        @RequestParam( value = "ou" ) List<String> orgUnits,
        @RequestParam( required = false, value = "crosstab" ) List<String> crossTab,
        @RequestParam( required = false ) boolean orgUnitIsParent,
        @RequestParam( required = false ) boolean minimal,
        RelativePeriods relatives,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableDynamicGrid( indicators, dataElements, dataSets,
            orgUnits, crossTab, orgUnitIsParent, minimal, relatives, response );

        String filename = DATA_NAME + ".pdf";
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_PDF, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, false );

        GridUtils.toPdf( grid, response.getOutputStream() );
    }

    @RequestMapping( value = "/data.xls", method = RequestMethod.GET )
    public void getReportTableDynamicDataXls( @RequestParam( required = false, value = "in" ) List<String> indicators,
        @RequestParam( required = false, value = "de" ) List<String> dataElements,
        @RequestParam( required = false, value = "ds" ) List<String> dataSets,
        @RequestParam( value = "ou" ) List<String> orgUnits,
        @RequestParam( required = false, value = "crosstab" ) List<String> crossTab,
        @RequestParam( required = false ) boolean orgUnitIsParent,
        @RequestParam( required = false ) boolean minimal,
        RelativePeriods relatives,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableDynamicGrid( indicators, dataElements, dataSets,
            orgUnits, crossTab, orgUnitIsParent, minimal, relatives, response );

        String filename = DATA_NAME + ".xls";
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_EXCEL, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, true );

        GridUtils.toXls( grid, response.getOutputStream() );
    }

    @RequestMapping( value = "/data.csv", method = RequestMethod.GET )
    public void getReportTableDynamicDataCsv( @RequestParam( required = false, value = "in" ) List<String> indicators,
        @RequestParam( required = false, value = "de" ) List<String> dataElements,
        @RequestParam( required = false, value = "ds" ) List<String> dataSets,
        @RequestParam( value = "ou" ) List<String> orgUnits,
        @RequestParam( required = false, value = "crosstab" ) List<String> crossTab,
        @RequestParam( required = false ) boolean orgUnitIsParent,
        @RequestParam( required = false ) boolean minimal,
        RelativePeriods relatives,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableDynamicGrid( indicators, dataElements, dataSets,
            orgUnits, crossTab, orgUnitIsParent, minimal, relatives, response );

        String filename = DATA_NAME + ".csv";
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_CSV, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, true );

        GridUtils.toCsv( grid, response.getOutputStream() );
    }

    private Grid getReportTableDynamicGrid( List<String> indicators, List<String> dataElements, List<String> dataSets,
        List<String> orgUnits, List<String> crossTab, boolean orgUnitIsParent, boolean minimal, RelativePeriods relatives, HttpServletResponse response ) throws Exception
    {
        List<Indicator> indicators_ = indicatorService.getIndicatorsByUid( indicators );
        List<DataElement> dataElements_ = dataElementService.getDataElementsByUid( dataElements );
        List<DataSet> dataSets_ = dataSetService.getDataSetsByUid( dataSets );
        List<OrganisationUnit> organisationUnits = organisationUnitService.getOrganisationUnitsByUid( orgUnits );

        if ( indicators_.isEmpty() && dataElements_.isEmpty() && dataSets_.isEmpty() )
        {
            ContextUtils.conflictResponse( response, "No valid indicators, data elements or data sets specified" );
            return null;
        }

        if ( orgUnitIsParent )
        {
            List<OrganisationUnit> childUnits = new ArrayList<OrganisationUnit>();

            for ( OrganisationUnit unit : organisationUnits )
            {
                childUnits.addAll( unit.getChildren() );
            }

            organisationUnits = childUnits;
        }

        if ( organisationUnits.isEmpty() )
        {
            ContextUtils.conflictResponse( response, "No valid organisation units specified" );
            return null;
        }

        ReportTable table = new ReportTable();

        table.setIndicators( indicators_ );
        table.setDataElements( dataElements_ );
        table.setOrganisationUnits( organisationUnits );

        table.setDoIndicators( crossTab != null && crossTab.contains( "data" ) );
        table.setDoPeriods( crossTab != null && crossTab.contains( "periods" ) );
        table.setDoUnits( crossTab != null && crossTab.contains( "orgunits" ) );

        table.setRelatives( relatives );

        return reportTableService.getReportTableGrid( table, i18nManager.getI18nFormat(), new Date(), null, minimal );
    }

    //--------------------------------------------------------------------------
    // GET - Report table data
    //--------------------------------------------------------------------------

    @RequestMapping( value = "/{uid}/data", method = RequestMethod.GET ) // For json, jsonp
    public String getReportTableData( @PathVariable( "uid" ) String uid, Model model,
        @RequestParam( value = "ou", required = false ) String organisationUnitUid,
        @RequestParam( value = "pe", required = false ) String period,
        HttpServletResponse response ) throws Exception
    {
        model.addAttribute( "model", getReportTableGrid( uid, organisationUnitUid, period ) );
        model.addAttribute( "viewClass", "detailed" );

        return "grid";
    }

    @RequestMapping( value = "/{uid}/data.html", method = RequestMethod.GET )
    public void getReportTableHtml( @PathVariable( "uid" ) String uid,
        @RequestParam( value = "ou", required = false ) String organisationUnitUid,
        @RequestParam( value = "pe", required = false ) String period,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableGrid( uid, organisationUnitUid, period );

        String filename = filenameEncode( grid.getTitle() ) + ".html";
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_HTML, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, false );

        GridUtils.toHtml( grid, response.getWriter() );
    }

    @RequestMapping( value = "/{uid}/data.xml", method = RequestMethod.GET )
    public void getReportTableXml( @PathVariable( "uid" ) String uid,
        @RequestParam( value = "ou", required = false ) String organisationUnitUid,
        @RequestParam( value = "pe", required = false ) String period,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableGrid( uid, organisationUnitUid, period );

        String filename = filenameEncode( grid.getTitle() ) + ".xml";
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_XML, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, false );

        GridUtils.toXml( grid, response.getOutputStream() );
    }

    @RequestMapping( value = "/{uid}/data.pdf", method = RequestMethod.GET )
    public void getReportTablePdf( @PathVariable( "uid" ) String uid,
        @RequestParam( value = "ou", required = false ) String organisationUnitUid,
        @RequestParam( value = "pe", required = false ) String period,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableGrid( uid, organisationUnitUid, period );

        String filename = filenameEncode( grid.getTitle() ) + ".pdf";
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_PDF, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, false );

        GridUtils.toPdf( grid, response.getOutputStream() );
    }

    @RequestMapping( value = "/{uid}/data.xls", method = RequestMethod.GET )
    public void getReportTableXls( @PathVariable( "uid" ) String uid,
        @RequestParam( value = "ou", required = false ) String organisationUnitUid,
        @RequestParam( value = "pe", required = false ) String period,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableGrid( uid, organisationUnitUid, period );

        String filename = filenameEncode( grid.getTitle() ) + ".xls";
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_EXCEL, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, true );

        GridUtils.toXls( grid, response.getOutputStream() );
    }

    @RequestMapping( value = "/{uid}/data.csv", method = RequestMethod.GET )
    public void getReportTableCsv( @PathVariable( "uid" ) String uid,
        @RequestParam( value = "ou", required = false ) String organisationUnitUid,
        @RequestParam( value = "pe", required = false ) String period,
        HttpServletResponse response ) throws Exception
    {
        Grid grid = getReportTableGrid( uid, organisationUnitUid, period );

        String filename = filenameEncode( grid.getTitle() ) + ".csv";
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_CSV, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, true );

        GridUtils.toCsv( grid, response.getOutputStream() );
    }

    private Grid getReportTableGrid( String uid, String organisationUnitUid, String period )
        throws Exception
    {
        ReportTable reportTable = reportTableService.getReportTable( uid );

        if ( organisationUnitUid == null && reportTable.hasReportParams() && reportTable.getReportParams().isOrganisationUnitSet() )
        {
            organisationUnitUid = organisationUnitService.getRootOrganisationUnits().iterator().next().getUid();
        }

        Date date = period != null ? DateUtils.getMediumDate( period ) : new Cal().now().subtract( Calendar.MONTH, 1 ).time();

        return reportTableService.getReportTableGrid( uid, i18nManager.getI18nFormat(), date, organisationUnitUid );
    }

    //--------------------------------------------------------------------------
    // Supportive methods
    //--------------------------------------------------------------------------

    private void mergeReportTable( ReportTable reportTable )
    {
        reportTable.setDataElements( dataElementService.getDataElementsByUid( getUids( reportTable.getDataElements() ) ) );
        reportTable.setIndicators( indicatorService.getIndicatorsByUid( getUids( reportTable.getIndicators() ) ) );
        reportTable.setDataSets( dataSetService.getDataSetsByUid( getUids( reportTable.getDataSets() ) ) );
        reportTable.setOrganisationUnits( organisationUnitService.getOrganisationUnitsByUid( getUids( reportTable.getOrganisationUnits() ) ) );
        reportTable.setPeriods( periodService.reloadPeriods( reportTable.getPeriods() ) );
        reportTable.setDataElementGroups( dataElementService.getDataElementGroupsByUid( getUids( reportTable.getDataElementGroups() ) ) );
        reportTable.setOrganisationUnitGroups( organisationUnitGroupService.getOrganisationUnitGroupsByUid( getUids( reportTable.getOrganisationUnitGroups() ) ) );
        
        if ( reportTable.getUser() != null )
        {
            reportTable.setUser( userService.getUser( reportTable.getUser().getUid() ) );
        }
    }
}
