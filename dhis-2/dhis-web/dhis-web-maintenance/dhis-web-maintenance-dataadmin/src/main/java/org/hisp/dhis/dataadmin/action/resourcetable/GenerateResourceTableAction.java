package org.hisp.dhis.dataadmin.action.resourcetable;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.resourcetable.ResourceTableService;
import org.hisp.dhis.sqlview.SqlViewService;

import com.opensymphony.xwork2.Action;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GenerateResourceTableAction
    implements Action
{
    private static final Log log = LogFactory.getLog( GenerateResourceTableAction.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SqlViewService sqlViewService;

    public void setSqlViewService( SqlViewService sqlViewService )
    {
        this.sqlViewService = sqlViewService;
    }

    private ResourceTableService resourceTableService;

    public void setResourceTableService( ResourceTableService resourceTableService )
    {
        this.resourceTableService = resourceTableService;
    }
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private boolean organisationUnit;

    public void setOrganisationUnit( boolean organisationUnit )
    {
        this.organisationUnit = organisationUnit;
    }

    private boolean dataElementGroupSetStructure;

    public void setDataElementGroupSetStructure( boolean dataElementGroupSetStructure )
    {
        this.dataElementGroupSetStructure = dataElementGroupSetStructure;
    }
    
    private boolean indicatorGroupSetStructure;

    public void setIndicatorGroupSetStructure( boolean indicatorGroupSetStructure )
    {
        this.indicatorGroupSetStructure = indicatorGroupSetStructure;
    }

    private boolean organisationUnitGroupSetStructure;

    public void setOrganisationUnitGroupSetStructure( boolean organisationUnitGroupSetStructure )
    {
        this.organisationUnitGroupSetStructure = organisationUnitGroupSetStructure;
    }
    
    private boolean categoryStructure;

    public void setCategoryStructure( boolean categoryStructure )
    {
        this.categoryStructure = categoryStructure;
    }

    private boolean categoryOptionComboName; 

    public void setCategoryOptionComboName( boolean categoryOptionComboName )
    {
        this.categoryOptionComboName = categoryOptionComboName;
    }
    
    private boolean dataElementStructure;

    public void setDataElementStructure( boolean dataElementStructure )
    {
        this.dataElementStructure = dataElementStructure;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() 
        throws Exception
    {
        sqlViewService.dropAllSqlViewTables();
        
        log.info( "Dropped all sql views" );
        
        if ( organisationUnit )
        {
            resourceTableService.generateOrganisationUnitStructures();
        }
        
        if ( dataElementGroupSetStructure )
        {
            resourceTableService.generateDataElementGroupSetTable();
        }
        
        if ( indicatorGroupSetStructure )
        {
            resourceTableService.generateIndicatorGroupSetTable();
        }
        
        if ( organisationUnitGroupSetStructure )
        {
            resourceTableService.generateOrganisationUnitGroupSetTable();
        }
        
        if ( categoryStructure )
        {
            resourceTableService.generateCategoryTable();
        }
        
        if ( categoryOptionComboName )
        {
            resourceTableService.generateCategoryOptionComboNames();
        }
        
        if ( dataElementStructure )
        {
            resourceTableService.generateDataElementTable();
        }
        
        log.info( "Generated resource tables" );
        
        sqlViewService.createAllViewTables();
        
        log.info( "Created all views" );
        
        return SUCCESS;
    }
}
