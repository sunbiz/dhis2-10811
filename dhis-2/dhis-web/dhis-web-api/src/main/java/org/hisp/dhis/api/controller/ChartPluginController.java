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

import org.hisp.dhis.aggregation.AggregatedDataValue;
import org.hisp.dhis.aggregation.AggregatedDataValueService;
import org.hisp.dhis.aggregation.AggregatedIndicatorValue;
import org.hisp.dhis.aggregation.AggregatedOrgUnitDataValueService;
import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.api.utils.ContextUtils.CacheStrategy;
import org.hisp.dhis.api.webdomain.ChartPluginValue;
import org.hisp.dhis.completeness.DataSetCompletenessResult;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.i18n.I18nManager;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.user.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hisp.dhis.api.utils.ContextUtils.CONTENT_TYPE_JSON;
import static org.hisp.dhis.system.util.ConversionUtils.getIdentifiers;
import static org.hisp.dhis.system.util.DateUtils.setNames;

@Controller
@RequestMapping( value = ChartPluginController.RESOURCE_PATH )
public class ChartPluginController
{
    public static final String RESOURCE_PATH = "/chartValues";

    @Autowired
    private AggregatedDataValueService aggregatedDataValueService;

    @Autowired
    private AggregatedOrgUnitDataValueService aggregatedOrgUnitDataValueService;
    
    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private DataSetService dataSetService;
    
    @Autowired
    private PeriodService periodService;

    @Autowired
    private OrganisationUnitService organisationUnitService;
    
    @Autowired
    private OrganisationUnitGroupService organisationUnitGroupService;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private I18nManager i18nManager;

    @Autowired
    private ContextUtils contextUtils;

    @RequestMapping( method = RequestMethod.GET )
    public String getChartValues( @RequestParam( required = false ) Set<String> in,
        @RequestParam( required = false ) Set<String> de,
        @RequestParam( required = false ) Set<String> ds,
        @RequestParam( required = false ) Set<String> p,
        @RequestParam Set<String> ou,
        @RequestParam( required = false ) boolean orgUnitIsParent,
        @RequestParam( required = false ) String organisationUnitGroupSetId,
        @RequestParam( required = false ) boolean userOrganisationUnit,
        @RequestParam( required = false ) boolean userOrganisationUnitChildren,
        @RequestParam( required = false ) boolean periodIsFilter,
        RelativePeriods relativePeriods, Model model, HttpServletResponse response ) throws Exception
    {
        ChartPluginValue chartValue = new ChartPluginValue();

        I18nFormat format = i18nManager.getI18nFormat();

        // ---------------------------------------------------------------------
        // Periods
        // ---------------------------------------------------------------------
        
        List<Period> periods = relativePeriods.getRelativePeriods();

        if ( p != null && p.size() > 0 )
        {
            for ( String iso : p )
            {
                Period period = PeriodType.getPeriodFromIsoString( iso );
                
                if ( !periods.contains( period ) )
                {
                    periods.add( period );
                }
            }
        }

        periods = periodService.reloadPeriods( setNames( periods, format ) );
        
        if ( periodIsFilter )
        {
            periods = Arrays.asList( periods.get( 0 ) );
        }

        if ( periods.isEmpty() )
        {
            ContextUtils.conflictResponse( response, "No valid periods specified" );
            return null;
        }

        for ( Period period : periods )
        {
            chartValue.getPeriods().add( period.getName() );
        }

        // ---------------------------------------------------------------------
        // Organisation units
        // ---------------------------------------------------------------------

        List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();

        if ( userOrganisationUnit || userOrganisationUnitChildren )
        {
            if ( userOrganisationUnit )
            {
                organisationUnits.add( currentUserService.getCurrentUser().getOrganisationUnit() );
            }

            if ( userOrganisationUnitChildren )
            {
                organisationUnits.addAll( new ArrayList<OrganisationUnit>( currentUserService.getCurrentUser()
                    .getOrganisationUnit().getSortedChildren() ) );
            }
        }
        else
        {
            organisationUnits = new ArrayList<OrganisationUnit>( organisationUnitService.getOrganisationUnitsByUid( ou ) );
        }

        if ( orgUnitIsParent )
        {
            List<OrganisationUnit> childOrganisationUnits = new ArrayList<OrganisationUnit>();

            for ( OrganisationUnit unit : organisationUnits )
            {
                childOrganisationUnits.addAll( unit.getChildren() );
            }

            organisationUnits = childOrganisationUnits;
        }

        if ( organisationUnits.isEmpty() )
        {
            ContextUtils.conflictResponse( response, "No valid organisation units specified" );
            return null;
        }

        // ---------------------------------------------------------------------
        // Organisation unit groups
        // ---------------------------------------------------------------------

        boolean useGroupSets = organisationUnitGroupSetId != null;
        OrganisationUnit firstOrgUnit = organisationUnits.get( 0 );
        
        List<OrganisationUnitGroup> organisationUnitGroups = new ArrayList<OrganisationUnitGroup>();        
        
        if ( useGroupSets )
        {
            OrganisationUnitGroupSet groupSet = organisationUnitGroupService.getOrganisationUnitGroupSet( organisationUnitGroupSetId );
            organisationUnitGroups.addAll( groupSet.getOrganisationUnitGroups() );
        }

        if ( useGroupSets )
        {
            for ( OrganisationUnitGroup groupSet : organisationUnitGroups )
            {
                chartValue.getOrgUnits().add( groupSet.getName() );
            }
        }
        else
        {
            for ( OrganisationUnit unit : organisationUnits )
            {
                chartValue.getOrgUnits().add( unit.getName() );
            }
        }

        // ---------------------------------------------------------------------
        // Indicators
        // ---------------------------------------------------------------------

        if ( in != null )
        {
            List<Indicator> indicators = indicatorService.getIndicatorsByUid( in );

            if ( indicators.isEmpty() )
            {
                ContextUtils.conflictResponse( response, "No valid indicators specified" );
                return null;
            }

            for ( Indicator indicator : indicators )
            {
                chartValue.getData().add( indicator.getDisplayShortName() );
            }

            Collection<AggregatedIndicatorValue> indicatorValues = useGroupSets ?
                aggregatedOrgUnitDataValueService.getAggregatedIndicatorValues( getIdentifiers( Indicator.class, indicators ),
                    getIdentifiers( Period.class, periods ), firstOrgUnit.getId(), getIdentifiers( OrganisationUnitGroup.class, organisationUnitGroups ) ) :
                aggregatedDataValueService.getAggregatedIndicatorValues( getIdentifiers( Indicator.class, indicators ),
                    getIdentifiers( Period.class, periods ), getIdentifiers( OrganisationUnit.class, organisationUnits ) );

            for ( AggregatedIndicatorValue value : indicatorValues )
            {
                String[] record = new String[4];

                record[0] = String.valueOf( value.getValue() );
                record[1] = indicatorService.getIndicator( value.getIndicatorId() ).getDisplayShortName();
                record[2] = format.formatPeriod( periodService.getPeriod( value.getPeriodId() ) );
                record[3] = useGroupSets ?
                    organisationUnitGroupService.getOrganisationUnitGroup( value.getOrganisationUnitGroupId() ).getName() :
                    organisationUnitService.getOrganisationUnit( value.getOrganisationUnitId() ).getName();

                chartValue.getValues().add( record );
            }
        }

        // ---------------------------------------------------------------------
        // Data elements
        // ---------------------------------------------------------------------

        if ( de != null )
        {
            List<DataElement> dataElements = dataElementService.getDataElementsByUid( de );

            if ( dataElements.isEmpty() )
            {
                ContextUtils.conflictResponse( response, "No valid data elements specified" );
                return null;
            }

            for ( DataElement element : dataElements )
            {
                chartValue.getData().add( element.getDisplayShortName() );
            }

            Collection<AggregatedDataValue> dataValues = useGroupSets ?
                aggregatedOrgUnitDataValueService.getAggregatedDataValueTotals( getIdentifiers( DataElement.class, dataElements ), 
                    getIdentifiers( Period.class, periods ), firstOrgUnit.getId(), getIdentifiers( OrganisationUnitGroup.class, organisationUnitGroups ) ) :
                aggregatedDataValueService.getAggregatedDataValueTotals( getIdentifiers( DataElement.class, dataElements ), 
                    getIdentifiers( Period.class, periods ),  getIdentifiers( OrganisationUnit.class, organisationUnits ) );

            for ( AggregatedDataValue value : dataValues )
            {
                String[] record = new String[4];

                record[0] = String.valueOf( value.getValue() );
                record[1] = dataElementService.getDataElement( value.getDataElementId() ).getDisplayShortName();
                record[2] = format.formatPeriod( periodService.getPeriod( value.getPeriodId() ) );
                record[3] = useGroupSets ?
                    organisationUnitGroupService.getOrganisationUnitGroup( value.getOrganisationUnitGroupId() ).getName() :
                    organisationUnitService.getOrganisationUnit( value.getOrganisationUnitId() ).getName();

                chartValue.getValues().add( record );
            }
        }

        // ---------------------------------------------------------------------
        // Data set completeness
        // ---------------------------------------------------------------------

        if ( ds != null )
        {
            Set<DataSet> dataSets = new HashSet<DataSet>();
            
            for ( String id : ds )
            {
                dataSets.add( dataSetService.getDataSet( id ) );
            }
            
            if ( dataSets.isEmpty() )
            {
                ContextUtils.conflictResponse( response, "No valid data sets specified" );
                return null;
            }
            
            for ( DataSet dataSet : dataSets )
            {
                chartValue.getData().add( dataSet.getDisplayShortName() );
            }
            
            Collection<DataSetCompletenessResult> dataSetValues = useGroupSets ?
                new ArrayList<DataSetCompletenessResult>() : // not yet implemented
                aggregatedDataValueService.getAggregatedDataSetCompleteness( getIdentifiers( DataSet.class, dataSets ), 
                    getIdentifiers( Period.class, periods ), getIdentifiers( OrganisationUnit.class, organisationUnits ) ); 
            
            for ( DataSetCompletenessResult value : dataSetValues )
            {
                String[] record = new String[4];
                
                record[0] = String.valueOf( value.getValue() );
                record[1] = dataSetService.getDataSet( value.getDataSetId() ).getDisplayShortName();
                record[2] = format.formatPeriod( periodService.getPeriod( value.getPeriodId() ) );
                record[3] = organisationUnitService.getOrganisationUnit( value.getOrganisationUnitId() ).getName();
                
                chartValue.getValues().add( record );                
            }
        }
        
        contextUtils.configureResponse( response, CONTENT_TYPE_JSON, CacheStrategy.RESPECT_SYSTEM_SETTING, null, false );

        model.addAttribute( "model", chartValue );

        return "chartValues";
    }
}
