package org.hisp.dhis.datasetreport.jdbc;

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

import static org.hisp.dhis.system.util.ConversionUtils.getIdentifiers;
import static org.hisp.dhis.system.util.TextUtils.getCommaDelimitedString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.datasetreport.DataSetReportStore;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.system.filter.AggregatableDataElementFilter;
import org.hisp.dhis.system.util.FilterUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author Lars Helge Overland
 */
public class JdbcDataSetReportStore
    implements DataSetReportStore
{
    private JdbcTemplate jdbcTemplate;
    
    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    // -------------------------------------------------------------------------
    // DataSetReportStore implementation
    // -------------------------------------------------------------------------

    public Map<String, Double> getAggregatedValues( DataSet dataSet, Period period, OrganisationUnit unit, 
        Set<OrganisationUnitGroup> groups, boolean rawData )
    {
        Map<String, Double> map = new HashMap<String, Double>();
        
        Set<DataElement> dataElements = new HashSet<DataElement>( dataSet.getDataElements() );

        FilterUtils.filter( dataElements, new AggregatableDataElementFilter() );

        if ( !dataElements.isEmpty() )
        {
            final String sql = rawData ?
                "select de.uid, coc.uid, dv.value " +
                "from datavalue dv " +
                "left join dataelement de on dv.dataelementid = de.dataelementid " +
                "left join categoryoptioncombo coc on dv.categoryoptioncomboid = coc.categoryoptioncomboid " +
                "where dv.dataelementid in (" + getCommaDelimitedString( getIdentifiers( DataElement.class, dataElements ) ) + ") " +
                "and dv.periodid = " + period.getId() + " " +
                "and dv.sourceid = " + unit.getId()
                :
                "select de.uid, coc.uid, dv.value " +
                "from aggregateddatavalue dv " +
                "left join dataelement de on dv.dataelementid = de.dataelementid " +
                "left join categoryoptioncombo coc on dv.categoryoptioncomboid = coc.categoryoptioncomboid " +
                "where dv.dataelementid in (" + getCommaDelimitedString( getIdentifiers( DataElement.class, dataElements ) ) + ") " +
                "and dv.periodid = " + period.getId() + " " +
                "and dv.organisationunitid = " + unit.getId();            
            
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
            
            while ( rowSet.next() )
            {
                String dataElementId = rowSet.getString( 1 );
                String categoryOptionComboId = rowSet.getString( 2 );
                Double value = rowSet.getDouble( 3 );
                
                map.put( dataElementId + SEPARATOR + categoryOptionComboId, value );
            }
        }
        
        return map;
    }
    
    public Map<String, Double> getAggregatedSubTotals( DataSet dataSet, Period period, OrganisationUnit unit, Set<OrganisationUnitGroup> groups )
    {
        Map<String, Double> map = new HashMap<String, Double>();
        
        for ( Section section : dataSet.getSections() )
        {
            DataElementCategoryCombo categoryCombo = section.getCategoryCombo();
            Set<DataElement> dataElements = new HashSet<DataElement>( section.getDataElements() );
            
            if ( !dataElements.isEmpty() )
            {
                for ( DataElementCategoryOption categoryOption : categoryCombo.getCategoryOptions() )
                {
                    final String sql = 
                        "select de.uid, sum(dv.value) as total " +
                        "from aggregateddatavalue dv " +
                        "left join dataelement de on dv.dataelementid = de.dataelementid " +
                        "where dv.dataelementid in (" + getCommaDelimitedString( getIdentifiers( DataElement.class, dataElements ) ) + ") " +
                        "and dv.categoryoptioncomboid in (" + getCommaDelimitedString( getIdentifiers( DataElementCategoryOptionCombo.class, categoryOption.getCategoryOptionCombos() ) ) + ") " +
                        "and dv.periodid = " + period.getId() + " " +
                        "and dv.organisationunitid = " + unit.getId() + " " +
                        "group by de.uid";
                    
                    SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
                    
                    while ( rowSet.next() )
                    {
                        String dataElementId = rowSet.getString( 1 );
                        Double value = rowSet.getDouble( 2 );
                        
                        map.put( dataElementId + SEPARATOR + categoryOption.getUid(), value );
                    }
                }
            }
        }
        
        return map;
    }
    
    public Map<String, Double> getAggregatedTotals( DataSet dataSet, Period period, OrganisationUnit unit, Set<OrganisationUnitGroup> groups )
    {
        Map<String, Double> map = new HashMap<String, Double>();
        
        Set<DataElement> dataElements = new HashSet<DataElement>( dataSet.getDataElements() );

        FilterUtils.filter( dataElements, new AggregatableDataElementFilter() );

        if ( !dataElements.isEmpty() )
        {
            final String sql = 
                "select de.uid, sum(dv.value) as total " +
                "from aggregateddatavalue dv " +
                "left join dataelement de on dv.dataelementid = de.dataelementid " +
                "where dv.dataelementid in (" + getCommaDelimitedString( getIdentifiers( DataElement.class, dataElements ) ) + ") " +
                "and dv.periodid = " + period.getId() + " " +
                "and dv.organisationunitid = " + unit.getId() + " " +
                "group by de.uid";
    
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
            
            while ( rowSet.next() )
            {
                String dataElementId = rowSet.getString( 1 );
                Double value = rowSet.getDouble( 2 );
                
                map.put( String.valueOf( dataElementId ), value );
            }
        }
        
        return map;
    }
    
    public Map<String, Double> getAggregatedIndicatorValues( DataSet dataSet, Period period, OrganisationUnit unit, Set<OrganisationUnitGroup> groups )
    {
        Map<String, Double> map = new HashMap<String, Double>();
        
        Set<Indicator> indicators = new HashSet<Indicator>( dataSet.getIndicators() );
        
        if ( !indicators.isEmpty() )
        {
            final String sql =
                "select ind.uid, sum(dv.value) as total " +
                "from aggregatedindicatorvalue dv " +
                "left join indicator ind on dv.indicatorid = ind.indicatorid " +
                "where dv.indicatorid in (" + getCommaDelimitedString( getIdentifiers( Indicator.class, indicators ) ) + ") " +
                "and dv.periodid = " + period.getId() + " " +
                "and dv.organisationunitid = " + unit.getId() + " " +
                "group by ind.uid";
    
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
            
            while ( rowSet.next() )
            {
                String indicatorid = rowSet.getString( 1 );
                Double value = rowSet.getDouble( 2 );
                
                map.put( String.valueOf( indicatorid ), value );
            }
        }
        
        return map;
    }
}
