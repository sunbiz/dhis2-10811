package org.hisp.dhis.reporting.chart.action;

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

import static org.hisp.dhis.system.util.ConversionUtils.getIntegerCollection;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.chart.ChartService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.jfree.chart.JFreeChart;

import com.opensymphony.xwork2.Action;

/**
 * Known usage of this class is the pivot table chart functionality.
 * 
 * @author Lars Helge Overland
 */
public class GenerateChartAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ChartService chartService;

    public void setChartService( ChartService chartService )
    {
        this.chartService = chartService;
    }

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private List<String> indicatorId;

    public void setIndicatorId( List<String> indicatorId )
    {
        this.indicatorId = indicatorId;
    }

    private List<String> dataElementId;

    public void setDataElementId( List<String> dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    private List<String> periodId;

    public void setPeriodId( List<String> periodId )
    {
        this.periodId = periodId;
    }

    private List<String> organisationUnitId;

    public void setOrganisationUnitId( List<String> organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }

    private String series;

    public void setSeries( String series )
    {
        this.series = series;
    }

    private String category;

    public void setCategory( String category )
    {
        this.category = category;
    }

    private String filter;
    
    public void setFilter( String filter )
    {
        this.filter = filter;
    }

    private boolean regression;

    public void setRegression( boolean regression )
    {
        this.regression = regression;
    }

    private String chartWidth;

    public void setChartWidth( String chartWidth )
    {
        this.chartWidth = chartWidth;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private JFreeChart chart;

    public JFreeChart getChart()
    {
        return chart;
    }

    private int width;

    public int getWidth()
    {
        return width;
    }

    private int height;

    public int getHeight()
    {
        return height;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        List<Indicator> indicators = new ArrayList<Indicator>();

        for ( String id : indicatorId )
        {
            indicators.add( indicatorService.getIndicator( Integer.parseInt( id ) ) );
        }

        List<DataElement> dataElements = new ArrayList<DataElement>();

        for ( Integer id : getIntegerCollection( dataElementId ) )
        {
            dataElements.add( dataElementService.getDataElement( id ) );
        }

        List<Period> periods = new ArrayList<Period>();

        for ( String id : periodId )
        {
            periods.add( periodService.getPeriod( Integer.parseInt( id ) ) );
        }

        List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();

        for ( String id : organisationUnitId )
        {
            organisationUnits.add( organisationUnitService.getOrganisationUnit( Integer.parseInt( id ) ) );
        }

        width = chartWidth != null ? Integer.valueOf( chartWidth ) : 700;

        height = 500;

        chart = chartService.getJFreeChart( indicators, dataElements, periods, organisationUnits,
            series, category, filter, regression, format );

        return SUCCESS;
    }
}
