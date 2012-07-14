package org.hisp.dhis.api.controller.mapping;

/*
 * Copyright (c) 2011, University of Oslo
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

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.hisp.dhis.aggregation.AggregatedMapValue;
import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.api.utils.ContextUtils.CacheStrategy;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.mapping.MappingService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Jan Henrik Overland
 */
@Controller
@RequestMapping( value = MapValueController.RESOURCE_PATH )
public class MapValueController
{
    public static final String RESOURCE_PATH = "/mapValues";

    @Autowired
    private MappingService mappingService;
    
    @Autowired
    private IndicatorService indicatorService;
    
    @Autowired
    private DataElementService dataElementService;
    
    @Autowired
    private PeriodService periodService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private ContextUtils contextUtils;
    
    @RequestMapping( value = "/in", method = RequestMethod.GET )
    public String getIndicatorMapValues(
        @RequestParam String in,
        @RequestParam String pe,
        @RequestParam String ou,
        @RequestParam String le,
        Model model,
        HttpServletResponse response ) throws Exception
    {
        Indicator indicator = indicatorService.getIndicator( in );
        
        if ( indicator == null )
        {
            ContextUtils.conflictResponse( response, "Invalid indicator identifier" );
            return null;            
        }
        
        Period period = PeriodType.getPeriodFromIsoString( pe );
        
        if ( period == null )
        {
            ContextUtils.conflictResponse( response, "Invalid period identifier" );
            return null;            
        }
        
        period = periodService.reloadPeriod( period );
        
        OrganisationUnit organisationUnit = organisationUnitService.getOrganisationUnit( ou );
        
        if ( organisationUnit == null )
        {
            ContextUtils.conflictResponse( response, "Invalid organisation unit identifier" );
            return null;            
        }
        
        OrganisationUnitLevel level = organisationUnitService.getOrganisationUnitLevel( le );
        
        if ( level == null )
        {
            ContextUtils.conflictResponse( response, "Invalid organisation unit level identifier" );
            return null;            
        }
        
        Collection<AggregatedMapValue> mapValues = mappingService.getIndicatorMapValues( indicator.getId(), period.getId(), organisationUnit.getId(), level.getLevel() );
        
        model.addAttribute( "model", mapValues );
        
        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_JSON, CacheStrategy.RESPECT_SYSTEM_SETTING );
        
        return "mapValues";
    }

    @RequestMapping( value = "/de", method = RequestMethod.GET )
    public String getDataElementMapValues(
        @RequestParam String de,
        @RequestParam String pe,
        @RequestParam String ou,
        @RequestParam String le,
        Model model,
        HttpServletResponse response ) throws Exception
    {
        DataElement dataElement = dataElementService.getDataElement( de );
        
        if ( dataElement == null )
        {
            ContextUtils.conflictResponse( response, "Invalid indicator identifier" );
            return null;            
        }
        
        Period period = PeriodType.getPeriodFromIsoString( pe );
        
        if ( period == null )
        {
            ContextUtils.conflictResponse( response, "Invalid period identifier" );
            return null;            
        }
        
        period = periodService.reloadPeriod( period );
        
        OrganisationUnit organisationUnit = organisationUnitService.getOrganisationUnit( ou );
        
        if ( organisationUnit == null )
        {
            ContextUtils.conflictResponse( response, "Invalid organisation unit identifier" );
            return null;            
        }
        
        OrganisationUnitLevel level = organisationUnitService.getOrganisationUnitLevel( le );
        
        if ( level == null )
        {
            ContextUtils.conflictResponse( response, "Invalid organisation unit level identifier" );
            return null;            
        }
        
        Collection<AggregatedMapValue> mapValues = mappingService.getDataElementMapValues( dataElement.getId(), period.getId(), organisationUnit.getId(), level.getLevel() );
        
        model.addAttribute( "model", mapValues );

        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_JSON, CacheStrategy.RESPECT_SYSTEM_SETTING );
        
        return "mapValues";
    }
}
