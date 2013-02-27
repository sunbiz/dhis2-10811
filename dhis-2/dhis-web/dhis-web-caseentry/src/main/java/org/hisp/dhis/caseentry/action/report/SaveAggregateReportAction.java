/*
 * Copyright (c) 2004-2009, University of Oslo
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

package org.hisp.dhis.caseentry.action.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patientreport.PatientAggregateReport;
import org.hisp.dhis.patientreport.PatientAggregateReportService;
import org.hisp.dhis.program.ProgramStageService;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version SaveAggregateReportAction.java 1:16:10 PM Jan 14, 2013 $
 */
public class SaveAggregateReportAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PatientAggregateReportService aggregateReportService;

    public void setAggregateReportService( PatientAggregateReportService aggregateReportService )
    {
        this.aggregateReportService = aggregateReportService;
    }

    private ProgramStageService programStageService;

    public void setProgramStageService( ProgramStageService programStageService )
    {
        this.programStageService = programStageService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private Integer programStageId;

    public void setProgramStageId( Integer programStageId )
    {
        this.programStageId = programStageId;
    }

    private String aggregateType;

    public void setAggregateType( String aggregateType )
    {
        this.aggregateType = aggregateType;
    }

    private Collection<Integer> orgunitIds;

    public void setOrgunitIds( Collection<Integer> orgunitIds )
    {
        this.orgunitIds = orgunitIds;
    }

    private Set<String> deFilters;

    public void setDeFilters( Set<String> deFilters )
    {
        this.deFilters = deFilters;
    }

    private List<String> fixedPeriods = new ArrayList<String>();

    public void setFixedPeriods( List<String> fixedPeriods )
    {
        this.fixedPeriods = fixedPeriods;
    }

    private Set<String> relativePeriods = new HashSet<String>();

    public void setRelativePeriods( Set<String> relativePeriods )
    {
        this.relativePeriods = relativePeriods;
    }

    private List<String> startDates;

    public void setStartDates( List<String> startDates )
    {
        this.startDates = startDates;
    }

    private List<String> endDates;

    public void setEndDates( List<String> endDates )
    {
        this.endDates = endDates;
    }

    private String facilityLB; // All, children, current

    public void setFacilityLB( String facilityLB )
    {
        this.facilityLB = facilityLB;
    }

    private Integer position;

    public void setPosition( Integer position )
    {
        this.position = position;
    }

    private Integer limitRecords;

    public void setLimitRecords( Integer limitRecords )
    {
        this.limitRecords = limitRecords;
    }

    private Integer deGroupBy;

    public void setDeGroupBy( Integer deGroupBy )
    {
        this.deGroupBy = deGroupBy;
    }

    private Integer deSum;

    public void setDeSum( Integer deSum )
    {
        this.deSum = deSum;
    }

    private Boolean useCompletedEvents;

    public void setUseCompletedEvents( Boolean useCompletedEvents )
    {
        this.useCompletedEvents = useCompletedEvents;
    }

    private Boolean userOrganisationUnit;

    public void setUserOrganisationUnit( Boolean userOrganisationUnit )
    {
        this.userOrganisationUnit = userOrganisationUnit;
    }

    private Boolean userOrganisationUnitChildren;

    public void setUserOrganisationUnitChildren( Boolean userOrganisationUnitChildren )
    {
        this.userOrganisationUnitChildren = userOrganisationUnitChildren;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        userOrganisationUnit = (userOrganisationUnit == null) ? false : userOrganisationUnit;
        userOrganisationUnitChildren = (userOrganisationUnitChildren == null) ? false : userOrganisationUnitChildren;

        PatientAggregateReport aggregateReport = new PatientAggregateReport();

        aggregateReport.setName( name );
        aggregateReport.setProgramStage( programStageService.getProgramStage( programStageId ) );

        if ( startDates != null && endDates != null )
        {
            List<Date> start = new ArrayList<Date>();
            List<Date> end = new ArrayList<Date>();

            for ( int i = 0; i < startDates.size(); i++ )
            {
                start.add( format.parseDate( startDates.get( i ) ) );
                end.add( format.parseDate( endDates.get( i ) ) );
            }
            aggregateReport.setStartDates( start );
            aggregateReport.setEndDates( end );
        }

        aggregateReport.setRelativePeriods( relativePeriods );
        aggregateReport.setFixedPeriods( fixedPeriods );
        aggregateReport.setOrganisationUnits( new HashSet<OrganisationUnit>( organisationUnitService
            .getOrganisationUnits( orgunitIds ) ) );

        aggregateReport.setFilterValues( deFilters );
        aggregateReport.setFacilityLB( facilityLB );
        aggregateReport.setLimitRecords( limitRecords );
        aggregateReport.setPosition( position );

        if ( deGroupBy != null )
        {
            aggregateReport.setDeGroupBy( dataElementService.getDataElement( deGroupBy ) );
        }

        if ( deSum != null )
        {
            aggregateReport.setDeSum( dataElementService.getDataElement( deSum ) );
        }

        if ( useCompletedEvents != null )
        {
            aggregateReport.setUseCompletedEvents( useCompletedEvents );
        }


        aggregateReport.setUserOrganisationUnit( userOrganisationUnit );
        aggregateReport.setUserOrganisationUnitChildren( userOrganisationUnitChildren );

        aggregateReport.setAggregateType( aggregateType );
        aggregateReport.setUser( currentUserService.getCurrentUser() );

        aggregateReportService.addPatientAggregateReport( aggregateReport );

        return SUCCESS;
    }
}
