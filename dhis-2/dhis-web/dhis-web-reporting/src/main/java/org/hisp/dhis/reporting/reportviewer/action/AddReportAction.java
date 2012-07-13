package org.hisp.dhis.reporting.reportviewer.action;

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

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportService;
import org.hisp.dhis.reporttable.ReportTableService;
import org.hisp.dhis.system.util.StreamUtils;

import com.opensymphony.xwork2.Action;

/**
 * @author Lars Helge Overland
 * @version $Id: UploadDesignAction.java 5207 2008-05-22 12:16:36Z larshelg $
 */
public class AddReportAction
    implements Action
{
    private static final Log log = LogFactory.getLog( AddReportAction.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ReportService reportService;

    public void setReportService( ReportService reportService )
    {
        this.reportService = reportService;
    }

    private ReportTableService reportTableService;

    public void setReportTableService( ReportTableService reportTableService )
    {
        this.reportTableService = reportTableService;
    }
    
    // -----------------------------------------------------------------------
    // I18n
    // -----------------------------------------------------------------------

    protected I18n i18n;
    
    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }
    
    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private Integer reportTableId;
    
    public void setReportTableId( Integer reportTableId )
    {
        this.reportTableId = reportTableId;
    }

    private boolean usingOrgUnitGroupSets;
    
    public void setUsingOrgUnitGroupSets( boolean usingOrgUnitGroupSets )
    {
        this.usingOrgUnitGroupSets = usingOrgUnitGroupSets;
    }

    private File file;

    public void setUpload( File file )
    {
        this.file = file;
    }
    
    private String fileName;
    
    public void setUploadFileName( String fileName )
    {
        this.fileName = fileName;
    }
    
    private String contentType;
    
    public void setUploadContentType( String contentType )
    {
    	this.contentType = contentType;
    }
    
    private String currentDesign;

    public void setCurrentDesign( String currentDesign )
    {
        this.currentDesign = currentDesign;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String message;
    
    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Set fileName to the current design file name in case of update
        // ---------------------------------------------------------------------

        if ( fileName == null && currentDesign != null )
        {
            fileName = currentDesign;
        }
        
        // ---------------------------------------------------------------------
        // Validation
        // ---------------------------------------------------------------------
        
        if ( id == null && ( fileName == null || fileName.trim().length() == 0 ) )
        {
            return ERROR;
        }

        // ---------------------------------------------------------------------
        // Create report
        // ---------------------------------------------------------------------

        Report report = ( id == null ) ? new Report() : reportService.getReport( id );
        
        report.setName( name );
        report.setReportTable( reportTableService.getReportTable( reportTableId ) );
        report.setUsingOrgUnitGroupSets( usingOrgUnitGroupSets );
                
        log.info( "Upload file name: " + fileName + ", content type: " + contentType );

        // ---------------------------------------------------------------------
        // Design file upload
        // ---------------------------------------------------------------------

        if ( file != null )
        {
            report.setDesignContent( StreamUtils.getContent( file ) );
        }
        
        reportService.saveReport( report );
        
        return SUCCESS;
    }
}
