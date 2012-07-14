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
import java.util.HashSet;
import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.patientreport.PatientAggregateReport;
import org.hisp.dhis.patientreport.PatientAggregateReportService;
import org.hisp.dhis.period.PeriodType;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * 
 * @version GetAggregateReportListAction.java 1:38:47 PM Jan 14, 2013 $
 */
public class GetAggregateReportAction
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

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Input && Output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private PatientAggregateReport aggregateReport;

    public PatientAggregateReport getAggregateReport()
    {
        return aggregateReport;
    }

    private Collection<DataElement> selectedDataElements = new HashSet<DataElement>();

    public Collection<DataElement> getSelectedDataElements()
    {
        return selectedDataElements;
    }

    private List<String> fixedPeriodNames = new ArrayList<String>();

    public List<String> getFixedPeriodNames()
    {
        return fixedPeriodNames;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        aggregateReport = aggregateReportService.getPatientAggregateReport( id );

        for ( String fixedPeriodId : aggregateReport.getFixedPeriods() )
        {
            fixedPeriodNames.add( format.formatPeriod( PeriodType.getPeriodFromIsoString( fixedPeriodId ) ) );
        }

        for ( String deFilter : aggregateReport.getFilterValues() )
        {
            int id = Integer.parseInt( deFilter.split( "_" )[0] );
            selectedDataElements.add( dataElementService.getDataElement( id ) );
        }

        return SUCCESS;
    }

}
