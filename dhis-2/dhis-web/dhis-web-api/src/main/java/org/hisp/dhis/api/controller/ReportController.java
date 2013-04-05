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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.api.utils.ContextUtils.CacheStrategy;
import org.hisp.dhis.i18n.I18nManager;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportService;
import org.hisp.dhis.system.util.CodecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 * @author Lars Helge Overland
 */
@Controller
@RequestMapping( value = ReportController.RESOURCE_PATH )
public class ReportController
    extends AbstractCrudController<Report>
{
    public static final String RESOURCE_PATH = "/reports";

    @Autowired
    public ReportService reportService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private I18nManager i18nManager;

    @Autowired
    private ContextUtils contextUtils;

    @RequestMapping( value = { "/{uid}/data", "/{uid}/data.pdf" }, method = RequestMethod.GET )
    public void getReportAsPdf( @PathVariable( "uid" ) String uid,
        @RequestParam( value = "ou", required = false ) String organisationUnitUid,
        @RequestParam( value = "pe", required = false ) String period,
        HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        getReport( request, response, uid, organisationUnitUid, period, "pdf", ContextUtils.CONTENT_TYPE_PDF, false );
    }

    @RequestMapping( value = "/{uid}/data.xls", method = RequestMethod.GET )
    public void getReportAsXls( @PathVariable( "uid" ) String uid,
        @RequestParam( value = "ou", required = false ) String organisationUnitUid,
        @RequestParam( value = "pe", required = false ) String period,
        HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        getReport( request, response, uid, organisationUnitUid, period, "xls", ContextUtils.CONTENT_TYPE_EXCEL, true );
    }

    @RequestMapping( value = "/{uid}/data.html", method = RequestMethod.GET )
    public void getReportAsHtml( @PathVariable( "uid" ) String uid,
        @RequestParam( value = "ou", required = false ) String organisationUnitUid,
        @RequestParam( value = "pe", required = false ) String period,
        HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        JasperPrint print = getReport( request, response, uid, organisationUnitUid, period, "html", ContextUtils.CONTENT_TYPE_HTML, false );
        
        request.getSession().setAttribute( BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, print );
    }
    
    @RequestMapping( value = "/{uid}/design", method = RequestMethod.PUT )
    @PreAuthorize( "hasRole('ALL')" )
    public void updateReportDesign( @PathVariable( "uid" ) String uid, 
        @RequestBody String designContent,
        HttpServletResponse response ) throws Exception
    {
        Report report = reportService.getReport( uid );
        
        if ( report == null )
        {
            ContextUtils.notFoundResponse( response, "Report not found for identifier: " + uid );
            return;
        }
        
        report.setDesignContent( designContent );        
        reportService.saveReport( report );
    }

    /**
     * This methods wraps the Jasper image servlet to avoid having a separate
     * servlet mapping around. Note that the path to images are relative to the
     * reports path in this controller.
     */
    @RequestMapping( value = "/jasperReports/img", method = RequestMethod.GET )
    public void getJasperImage( @RequestParam String image, 
        HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        new ImageServlet().service( request, response );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private JasperPrint getReport( HttpServletRequest request, HttpServletResponse response, String uid, String organisationUnitUid, String isoPeriod,
        String type, String contentType, boolean attachment ) throws Exception
    {
        Report report = reportService.getReport( uid );

        if ( organisationUnitUid == null && report.hasReportTable() && report.getReportTable().hasReportParams()
            && report.getReportTable().getReportParams().isOrganisationUnitSet() )
        {
            organisationUnitUid = organisationUnitService.getRootOrganisationUnits().iterator().next().getUid();
        }

        Period period = isoPeriod != null ? PeriodType.getPeriodFromIsoString( isoPeriod ) : new MonthlyPeriodType().createPeriod();
        
        String filename = CodecUtils.filenameEncode( report.getName() ) + "." + type;
        contextUtils.configureResponse( response, contentType, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, attachment );

        JasperPrint print = reportService.renderReport( response.getOutputStream(), uid, period, organisationUnitUid, type,
            i18nManager.getI18nFormat() );
        
        return print;
    }
}
