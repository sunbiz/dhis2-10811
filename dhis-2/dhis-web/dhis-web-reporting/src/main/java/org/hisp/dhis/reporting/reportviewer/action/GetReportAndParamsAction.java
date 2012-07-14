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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

public class GetReportAndParamsAction
    implements Action
{
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private OrganisationUnitService organisationUnitService;
    
    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String uid;

    public void setUid( String uid )
    {
        this.uid = uid;
    }

    private String pe;

    public void setPe( String pe )
    {
        this.pe = pe;
    }

    private String ou;

    public void setOu( String ou )
    {
        this.ou = ou;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Report report;
    
    public Report getReport()
    {
        return report;
    }
    
    private OrganisationUnit organisationUnit;

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    private List<OrganisationUnit> organisationUnitHierarchy = new ArrayList<OrganisationUnit>();
    
    public List<OrganisationUnit> getOrganisationUnitHierarchy()
    {
        return organisationUnitHierarchy;
    }
    
    private List<OrganisationUnit> organisationUnitChildren = new ArrayList<OrganisationUnit>();
    
    public List<OrganisationUnit> getOrganisationUnitChildren()
    {
        return organisationUnitChildren;
    }

    private List<Period> periods;
    
    public List<Period> getPeriods()
    {
        return periods;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        report = reportService.getReport( uid );
        
        if ( ou != null )
        {
            organisationUnit = organisationUnitService.getOrganisationUnit( ou );
            
            if ( organisationUnit != null )
            {
                organisationUnitHierarchy.add( organisationUnit );
                
                OrganisationUnit parent = organisationUnit;
                
                while ( parent.getParent() != null )
                {
                    parent = parent.getParent();
                    organisationUnitHierarchy.add( parent );
                }
                
                organisationUnitChildren.addAll( organisationUnit.getChildren() );
            }
        }
        
        Date date = new Date();
        
        if ( pe != null )
        {
            date = PeriodType.getPeriodFromIsoString( pe ).getStartDate();
        }
        
        if ( report != null && report.hasRelativePeriods() )
        {
            periods = report.getRelatives().getRelativePeriods( date, format, true );
        }
        
        return SUCCESS;
    }
}
