package org.hisp.dhis.reporttable.jdbc;

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

import static org.hisp.dhis.reporttable.ReportTable.getIdentifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.chart.Chart;
import org.hisp.dhis.common.NameableObject;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.system.util.ConversionUtils;
import org.hisp.dhis.system.util.TextUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class JDBCReportTableManager
    implements ReportTableManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // ReportTableManager implementation
    // -------------------------------------------------------------------------

    public Map<String, Double> getAggregatedValueMap( ReportTable reportTable )
    {
        if ( reportTable.isOrganisationUnitGroupBased() )
        {
            return getAggregatedValueMapOrgUnitGroups( reportTable.getDataElements(), reportTable.getIndicators(),
                reportTable.getAllPeriods(), reportTable.getAllUnits(), reportTable.getParentOrganisationUnit() );
        }
        else
        {
            return getAggregatedValueMapOrgUnitHierarchy( reportTable.getDataElements(), reportTable.getIndicators(), reportTable.getDataSets(),
                reportTable.getAllPeriods(), reportTable.getAllUnits(), reportTable.getCategoryCombo(), reportTable.isDimensional(), reportTable.doTotal() );
        }
    }
    
    public Map<String, Double> getAggregatedValueMap( Chart chart )
    {
        if ( chart.isOrganisationUnitGroupBased() )
        {
            return getAggregatedValueMapOrgUnitGroups( chart.getDataElements(), chart.getIndicators(),
                chart.getRelativePeriods(), chart.getOrganisationUnitGroupSet().getOrganisationUnitGroups(), chart.getFirstOrganisationUnit() );
        }
        else
        {
            return getAggregatedValueMapOrgUnitHierarchy( chart.getDataElements(), chart.getIndicators(), chart.getDataSets(),
                chart.getRelativePeriods(), chart.getAllOrganisationUnits(), null, false, false );
        }
    }

    // -------------------------------------------------------------------------
    // Org unit groups
    // -------------------------------------------------------------------------

    private Map<String, Double> getAggregatedValueMapOrgUnitGroups( List<DataElement> dataElements, List<Indicator> indicators, 
        List<Period> periods, Collection<? extends NameableObject> groups, OrganisationUnit organisationUnit )        
    {
        Map<String, Double> map = new HashMap<String, Double>();
        
        String dataElementIds = TextUtils.getCommaDelimitedString( 
            ConversionUtils.getIdentifiers( DataElement.class, dataElements ) );
        String indicatorIds = TextUtils.getCommaDelimitedString( 
            ConversionUtils.getIdentifiers( Indicator.class, indicators ) );
        String periodIds = TextUtils.getCommaDelimitedString( 
            ConversionUtils.getIdentifiers( Period.class, periods ) );
        String groupIds = TextUtils.getCommaDelimitedString( 
            ConversionUtils.getIdentifiers( NameableObject.class, groups ) );

        if ( dataElementIds != null && !dataElementIds.isEmpty() )
        {
            final String sql = "SELECT dataelementid, periodid, organisationunitgroupid, SUM(value) FROM aggregatedorgunitdatavalue " + 
                "WHERE dataelementid IN (" + dataElementIds + ") AND periodid IN (" + periodIds + ") AND organisationunitgroupid IN (" + groupIds + ") " + 
                "AND organisationunitid = " + organisationUnit.getId() + " " +
                "GROUP BY dataelementid, periodid, organisationunitgroupid"; // Sum of category option combos

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
            
            while ( rowSet.next() )
            {
                String id = getIdentifier( getIdentifier( DataElement.class, rowSet.getInt( 1 ) ),
                    getIdentifier( Period.class, rowSet.getInt( 2 ) ),
                    getIdentifier( OrganisationUnitGroup.class, rowSet.getInt( 3 ) ) );

                map.put( id, rowSet.getDouble( 4 ) );
            }
        }

        if ( indicatorIds != null && !indicatorIds.isEmpty() )
        {
            final String sql = "SELECT indicatorid, periodid, organisationunitgroupid, value FROM aggregatedorgunitindicatorvalue " + 
                "WHERE indicatorid IN (" + indicatorIds + ") AND periodid IN (" + periodIds + ") AND organisationunitgroupid IN (" + groupIds + ") " +
                "AND organisationunitid = " + organisationUnit.getId();

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
            
            while ( rowSet.next() )
            {
                String id = getIdentifier( getIdentifier( Indicator.class, rowSet.getInt( 1 ) ),
                    getIdentifier( Period.class, rowSet.getInt( 2 ) ),
                    getIdentifier( OrganisationUnitGroup.class, rowSet.getInt( 3 ) ) );

                map.put( id, rowSet.getDouble( 4 ) );
            }
        }

        return map;
    }

    // -------------------------------------------------------------------------
    // Org unit hierarchy
    // -------------------------------------------------------------------------

    private Map<String, Double> getAggregatedValueMapOrgUnitHierarchy( List<DataElement> dataElements, List<Indicator> indicators, 
        List<DataSet> dataSets, List<Period> periods, Collection<? extends NameableObject> organisationUnits, DataElementCategoryCombo categoryCombo,
        boolean isDimensional, boolean doTotal )
    {
        Map<String, Double> map = new HashMap<String, Double>();

        String dataElementIds = TextUtils.getCommaDelimitedString( 
            ConversionUtils.getIdentifiers( DataElement.class, dataElements ) );
        String indicatorIds = TextUtils.getCommaDelimitedString( 
            ConversionUtils.getIdentifiers( Indicator.class, indicators ) );
        String dataSetIds = TextUtils.getCommaDelimitedString( 
            ConversionUtils.getIdentifiers( DataSet.class, dataSets ) );
        String periodIds = TextUtils.getCommaDelimitedString( 
            ConversionUtils.getIdentifiers( Period.class, periods ) );
        String unitIds = TextUtils.getCommaDelimitedString( 
            ConversionUtils.getIdentifiers( NameableObject.class, organisationUnits ) );

        if ( dataElementIds != null && !dataElementIds.isEmpty() )
        {
            final String sql = "SELECT dataelementid, periodid, organisationunitid, SUM(value) FROM aggregateddatavalue " + 
                "WHERE dataelementid IN (" + dataElementIds + ") AND periodid IN (" + periodIds + ") AND organisationunitid IN (" + unitIds + ") " + 
                "GROUP BY dataelementid, periodid, organisationunitid"; // Sum of category option combos

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
            
            while ( rowSet.next() )
            {
                String id = getIdentifier( getIdentifier( DataElement.class, rowSet.getInt( 1 ) ),
                    getIdentifier( Period.class, rowSet.getInt( 2 ) ),
                    getIdentifier( OrganisationUnit.class, rowSet.getInt( 3 ) ) );

                map.put( id, rowSet.getDouble( 4 ) );
            }
        }
        
        if ( indicatorIds != null && !indicatorIds.isEmpty() )
        {
            final String sql = "SELECT indicatorid, periodid, organisationunitid, value FROM aggregatedindicatorvalue " + 
                "WHERE indicatorid IN (" + indicatorIds + ") AND periodid IN (" + periodIds + ") AND organisationunitid IN (" + unitIds + ")";

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
            
            while ( rowSet.next() )
            {
                String id = getIdentifier( getIdentifier( Indicator.class, rowSet.getInt( 1 ) ),
                    getIdentifier( Period.class, rowSet.getInt( 2 ) ),
                    getIdentifier( OrganisationUnit.class, rowSet.getInt( 3 ) ) );

                map.put( id, rowSet.getDouble( 4 ) );
            }
        }

        if ( dataSetIds != null && !dataSetIds.isEmpty() )
        {
            final String sql = "SELECT datasetid, periodid, organisationunitid, value FROM aggregateddatasetcompleteness " + 
                "WHERE datasetid IN (" + dataSetIds + ") AND periodid IN (" + periodIds + ") AND organisationunitid IN (" + unitIds + ")";

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
            
            while ( rowSet.next() )
            {
                String id = getIdentifier( getIdentifier( DataSet.class, rowSet.getInt( 1 ) ),
                    getIdentifier( Period.class, rowSet.getInt( 2 ) ),
                    getIdentifier( OrganisationUnit.class, rowSet.getInt( 3 ) ) );

                map.put( id, rowSet.getDouble( 4 ) );
            }
        }
        
        if ( isDimensional )
        {
            final String sql = "SELECT dataelementid, categoryoptioncomboid, periodid, organisationunitid, value FROM aggregateddatavalue " + 
                "WHERE dataelementid IN (" + dataElementIds + ") AND periodid IN (" + periodIds + ") AND organisationunitid IN (" + unitIds + ")";

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
            
            while ( rowSet.next() )
            {
                String id = getIdentifier( getIdentifier( DataElement.class, rowSet.getInt( 1 ) ),
                    getIdentifier( DataElementCategoryOptionCombo.class, rowSet.getInt( 2 ) ),
                    getIdentifier( Period.class, rowSet.getInt( 3 ) ),
                    getIdentifier( OrganisationUnit.class, rowSet.getInt( 4 ) ) );

                map.put( id, rowSet.getDouble( 5 ) );
            }
        }
        
        if ( doTotal )
        {
            for ( DataElementCategoryOption categoryOption : categoryCombo.getCategoryOptions() ) //categorycombo
            {
                String cocIds = TextUtils.getCommaDelimitedString( 
                    ConversionUtils.getIdentifiers( DataElementCategoryOptionCombo.class, categoryOption.getCategoryOptionCombos() ) );
                
                final String sql = "SELECT dataelementid, periodid, organisationunitid, SUM(value) FROM aggregateddatavalue " +
                    "WHERE dataelementid IN (" + dataElementIds + ") AND categoryoptioncomboid IN (" + cocIds +
                    ") AND periodid IN (" + periodIds + ") AND organisationunitid IN (" + unitIds +
                    ") GROUP BY dataelementid, periodid, organisationunitid"; // Sum of category option combos

                SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );
                
                while ( rowSet.next() )
                {
                    String id = getIdentifier( getIdentifier( DataElement.class, rowSet.getInt( 1 ) ),
                        getIdentifier( Period.class, rowSet.getInt( 2 ) ),
                        getIdentifier( OrganisationUnit.class, rowSet.getInt( 3 ) ),
                        getIdentifier( DataElementCategoryOption.class, categoryOption.getId() ) );
    
                    map.put( id, rowSet.getDouble( 4 ) );
                }
            }
        }

        return map;
    }
}
