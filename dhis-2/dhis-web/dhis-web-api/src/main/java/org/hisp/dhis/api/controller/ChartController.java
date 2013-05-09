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

import static org.hisp.dhis.common.DimensionType.DATAELEMENT;
import static org.hisp.dhis.common.DimensionType.DATAELEMENT_GROUPSET;
import static org.hisp.dhis.common.DimensionType.DATASET;
import static org.hisp.dhis.common.DimensionType.INDICATOR;
import static org.hisp.dhis.common.DimensionType.ORGANISATIONUNIT;
import static org.hisp.dhis.common.DimensionType.ORGANISATIONUNIT_GROUPSET;
import static org.hisp.dhis.common.DimensionType.PERIOD;
import static org.hisp.dhis.common.IdentifiableObjectUtils.getUids;
import static org.hisp.dhis.organisationunit.OrganisationUnit.KEY_USER_ORGUNIT;
import static org.hisp.dhis.organisationunit.OrganisationUnit.KEY_USER_ORGUNIT_CHILDREN;
import static org.hisp.dhis.common.DimensionalObject.DATA_X_DIMS;
import static org.hisp.dhis.common.DimensionalObject.DATA_X_DIM_ID;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.api.utils.ContextUtils.CacheStrategy;
import org.hisp.dhis.chart.Chart;
import org.hisp.dhis.chart.ChartService;
import org.hisp.dhis.common.DimensionService;
import org.hisp.dhis.common.DimensionType;
import org.hisp.dhis.common.DimensionalObject;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.dxf2.utils.JacksonUtils;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.i18n.I18nManager;
import org.hisp.dhis.i18n.I18nManagerException;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.RelativePeriodEnum;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.system.util.CodecUtils;
import org.hisp.dhis.user.UserService;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
@RequestMapping( value = ChartController.RESOURCE_PATH )
public class ChartController
    extends AbstractCrudController<Chart>
{
    public static final String RESOURCE_PATH = "/charts";

    @Autowired
    private ChartService chartService;

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private UserService userService;

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private OrganisationUnitGroupService organisationUnitGroupService;
    
    @Autowired
    private DimensionService dimensionService;
    
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
        Chart chart = JacksonUtils.fromJson( input, Chart.class );
        
        mergeChart( chart );
        
        chartService.addChart( chart );
        
        ContextUtils.createdResponse( response, "Chart created", RESOURCE_PATH + "/" + chart.getUid() );
    }

    @Override
    @RequestMapping( value = "/{uid}", method = RequestMethod.PUT, consumes = "application/json" )
    @ResponseStatus( value = HttpStatus.NO_CONTENT )
    public void putJsonObject( HttpServletResponse response, HttpServletRequest request, @PathVariable( "uid" ) String uid, InputStream input ) throws Exception
    {
        Chart chart = chartService.getChart( uid );
        
        if ( chart == null )
        {
            ContextUtils.notFoundResponse( response, "Chart does not exist: " + uid );
            return;
        }
        
        Chart newChart = JacksonUtils.fromJson( input, Chart.class );
        
        mergeChart( newChart );
        
        chart.mergeWith( newChart );
        
        chartService.updateChart( chart );
    }

    @Override
    @RequestMapping( value = "/{uid}", method = RequestMethod.DELETE )
    @ResponseStatus( value = HttpStatus.NO_CONTENT )
    public void deleteObject( HttpServletResponse response, HttpServletRequest request, @PathVariable( "uid" ) String uid ) throws Exception
    {
        Chart chart = chartService.getChart( uid );
        
        if ( chart == null )
        {
            ContextUtils.notFoundResponse( response, "Chart does not exist: " + uid );
            return;
        }
        
        chartService.deleteChart( chart );
    }
    
    //--------------------------------------------------------------------------
    // Get data
    //--------------------------------------------------------------------------

    @RequestMapping( value = { "/{uid}/data", "/{uid}/data.png" }, method = RequestMethod.GET )
    public void getChart( @PathVariable( "uid" ) String uid,
        @RequestParam( value = "date", required = false ) @DateTimeFormat( pattern = "yyyy-MM-dd" ) Date date,
        @RequestParam( value = "ou", required = false ) String ou,
        @RequestParam( value = "width", defaultValue = "800", required = false ) int width,
        @RequestParam( value = "height", defaultValue = "500", required = false ) int height,
        HttpServletResponse response ) throws IOException, I18nManagerException
    {
        Chart chart = chartService.getChartNoAcl( uid );

        OrganisationUnit unit = ou != null ? organisationUnitService.getOrganisationUnit( ou ) : null;
        
        JFreeChart jFreeChart = chartService.getJFreeChart( chart, date, unit, i18nManager.getI18nFormat() );

        String filename = CodecUtils.filenameEncode( chart.getName() ) + ".png";

        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_PNG, CacheStrategy.RESPECT_SYSTEM_SETTING, filename, false );

        ChartUtilities.writeChartAsPNG( response.getOutputStream(), jFreeChart, width, height );
    }

    @RequestMapping( value = { "/data", "/data.png" }, method = RequestMethod.GET )
    public void getChart( @RequestParam( value = "in" ) String indicatorUid,
        @RequestParam( value = "ou" ) String organisationUnitUid,
        @RequestParam( value = "periods", required = false ) boolean periods,
        @RequestParam( value = "width", defaultValue = "800", required = false ) int width,
        @RequestParam( value = "height", defaultValue = "500", required = false ) int height,
        @RequestParam( value = "skipTitle", required = false ) boolean skipTitle,
        HttpServletResponse response ) throws Exception
    {
        Indicator indicator = indicatorService.getIndicator( indicatorUid );
        OrganisationUnit unit = organisationUnitService.getOrganisationUnit( organisationUnitUid );

        JFreeChart chart = null;

        if ( periods )
        {
            chart = chartService.getJFreePeriodChart( indicator, unit, !skipTitle, i18nManager.getI18nFormat() );
        }
        else
        {
            chart = chartService.getJFreeOrganisationUnitChart( indicator, unit, !skipTitle, i18nManager.getI18nFormat() );
        }

        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_PNG, CacheStrategy.RESPECT_SYSTEM_SETTING, "chart.png", false );

        ChartUtilities.writeChartAsPNG( response.getOutputStream(), chart, width, height );
    }
    
    @Override
    public void postProcessEntity( Chart chart ) throws Exception
    {
        chart.populateWebDomainProperties();
        
        I18nFormat format = i18nManager.getI18nFormat();
        
        if ( chart.getPeriods() != null && !chart.getPeriods().isEmpty() )
        {
            for ( Period period : chart.getPeriods() )
            {
                period.setName( format.formatPeriod( period ) );
            }
        }
    }

    //--------------------------------------------------------------------------
    // Supportive methods
    //--------------------------------------------------------------------------

    private void mergeChart( Chart chart )
    {
        chart.getIndicators().clear();
        chart.getDataElements().clear();
        chart.getDataSets().clear();
        chart.getPeriods().clear();
        chart.setRelatives( null );
        chart.getOrganisationUnits().clear();
        chart.getDataElementGroups().clear();
        chart.getOrganisationUnitGroups().clear();
        chart.getFilterDimensions().clear();
        
        if ( chart.getUser() != null )
        {
            chart.setUser( userService.getUser( chart.getUser().getUid() ) );
        }
        
        mergeDimensionalObjects( chart, chart.getColumns() );
        mergeDimensionalObjects( chart, chart.getRows() );
        mergeDimensionalObjects( chart, chart.getFilters() );
        
        chart.setSeries( toDimension( chart.getColumns().get( 0 ).getDimension() ) );
        chart.setCategory( toDimension( chart.getRows().get( 0 ).getDimension() ) );
        
        for ( DimensionalObject dimension : chart.getFilters() )
        {
            chart.getFilterDimensions().add( toDimension( dimension.getDimension() ) );
        }
    }
    
    private void mergeDimensionalObjects( Chart chart, List<DimensionalObject> dimensions )
    {
        for ( DimensionalObject dimension : dimensions )
        {
            DimensionType type = dimensionService.getDimensionType( dimension.getDimension() );
            
            List<String> uids = getUids( dimension.getItems() );
            
            if ( INDICATOR.equals( type ) )
            {
                chart.getIndicators().addAll( indicatorService.getIndicatorsByUid( uids ) );
            }
            else if ( DATAELEMENT.equals( type ) )
            {
                chart.getDataElements().addAll( dataElementService.getDataElementsByUid( uids ) );
            }
            else if ( DATASET.equals( type ) )
            {
                chart.getDataSets().addAll( dataSetService.getDataSetsByUid( uids ) );
            }
            else if ( PERIOD.equals( type ) )
            {
                List<RelativePeriodEnum> enums = new ArrayList<RelativePeriodEnum>();                
                Set<Period> periods = new HashSet<Period>();
                
                for ( String isoPeriod : uids )
                {
                    if ( RelativePeriodEnum.contains( isoPeriod ) )
                    {
                        enums.add( RelativePeriodEnum.valueOf( isoPeriod ) );
                    }
                    else
                    {
                        Period period = PeriodType.getPeriodFromIsoString( isoPeriod );
                    
                        if ( period != null )
                        {
                            periods.add( period );
                        }
                    }
                }

                chart.setRelatives( new RelativePeriods().setRelativePeriodsFromEnums( enums ) );
                chart.setPeriods( periodService.reloadPeriods( new ArrayList<Period>( periods ) ) );
            }
            else if ( ORGANISATIONUNIT.equals( type ) )
            {
                List<OrganisationUnit> ous = new ArrayList<OrganisationUnit>();
                
                for ( String ou : uids )
                {
                    if ( KEY_USER_ORGUNIT.equals( ou ) )
                    {
                        chart.setUserOrganisationUnit( true );
                    }
                    else if ( KEY_USER_ORGUNIT_CHILDREN.equals( ou ) )
                    {
                        chart.setUserOrganisationUnitChildren( true );
                    }
                    else
                    {
                        OrganisationUnit unit = organisationUnitService.getOrganisationUnit( ou );
                        
                        if ( unit != null )
                        {
                            ous.add( unit );
                        }
                    }
                }
                
                chart.setOrganisationUnits( ous );
            }
            else if ( DATAELEMENT_GROUPSET.equals( type ) )
            {
                chart.getDataElementGroups().addAll( dataElementService.getDataElementGroupsByUid( uids ) );
            }
            else if ( ORGANISATIONUNIT_GROUPSET.equals( type ) )
            {
                chart.getOrganisationUnitGroups().addAll( organisationUnitGroupService.getOrganisationUnitGroupsByUid( uids ) );
            }
                        
            //TODO categories and operands
        }
    }
    
    private static String toDimension( String object )
    {
        if ( DATA_X_DIMS.contains( object ) )
        {
            return DATA_X_DIM_ID;
        }
        
        return object;
    }
}
