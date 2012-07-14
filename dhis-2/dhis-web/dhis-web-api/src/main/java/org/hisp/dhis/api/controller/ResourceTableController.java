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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.hisp.dhis.analytics.AnalyticsTableService;
import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.resourcetable.ResourceTableService;
import org.hisp.dhis.sqlview.SqlViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Lars Helge Overland
 */
@Controller
@RequestMapping( value = ResourceTableController.RESOURCE_PATH )
public class ResourceTableController
{
    public static final String RESOURCE_PATH = "/resourceTables";
    
    @Resource(name="org.hisp.dhis.analytics.AnalyticsTableService")
    private AnalyticsTableService analyticsTableService;

    @Resource(name="org.hisp.dhis.analytics.CompletenessTableService")
    private AnalyticsTableService completenessTableService;
    
    @Resource(name="org.hisp.dhis.analytics.CompletenessTargetTableService")
    private AnalyticsTableService completenessTargetTableService;
    
    @Autowired
    private ResourceTableService resourceTableService;
        
    @Autowired
    private SqlViewService sqlViewService;
    
    @RequestMapping( value = "/analytics", method = RequestMethod.PUT )
    @PreAuthorize( "hasRole('ALL') or hasRole('F_DATA_MART_ADMIN')" )
    public void analytics( HttpServletResponse response )
    {
        analyticsTableService.update();
        
        ContextUtils.okResponse( response, "Initiated analytics table update" );
    }

    @RequestMapping( value = "/completeness", method = RequestMethod.PUT )
    @PreAuthorize( "hasRole('ALL') or hasRole('F_DATA_MART_ADMIN')" )
    public void completeness( HttpServletResponse response )
    {
        completenessTableService.update();
        
        ContextUtils.okResponse( response, "Initiated completeness table update" );
    }

    @RequestMapping( value = "/completenessTarget", method = RequestMethod.PUT )
    @PreAuthorize( "hasRole('ALL') or hasRole('F_DATA_MART_ADMIN')" )
    public void completenessTarget( HttpServletResponse response )
    {
        completenessTargetTableService.update();
        
        ContextUtils.okResponse( response, "Initiated completeness target table update" );
    }
    
    @RequestMapping( method = RequestMethod.PUT )
    @PreAuthorize( "hasRole('ALL') or hasRole('F_PERFORM_MAINTENANCE')" )
    public void resourceTables( HttpServletResponse response )
    {
        sqlViewService.dropAllSqlViewTables();
        
        resourceTableService.generateCategoryOptionComboNames();
        resourceTableService.generateCategoryTable();
        resourceTableService.generateDataElementGroupSetTable();
        resourceTableService.generateDataElementTable();
        resourceTableService.generateIndicatorGroupSetTable();
        resourceTableService.generateOrganisationUnitGroupSetTable();
        resourceTableService.generateOrganisationUnitStructures();
        resourceTableService.generatePeriodTable();
        
        sqlViewService.createAllViewTables();
        
        ContextUtils.okResponse( response, "All resource tables updated" );
    }
}
