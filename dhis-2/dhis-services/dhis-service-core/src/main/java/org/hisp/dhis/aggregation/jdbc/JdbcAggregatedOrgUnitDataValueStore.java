package org.hisp.dhis.aggregation.jdbc;

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

import static org.hisp.dhis.system.util.TextUtils.getCommaDelimitedString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.amplecode.quick.StatementHolder;
import org.amplecode.quick.StatementManager;
import org.amplecode.quick.mapper.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.AggregatedDataValue;
import org.hisp.dhis.aggregation.AggregatedIndicatorValue;
import org.hisp.dhis.aggregation.AggregatedOrgUnitDataValueStore;
import org.hisp.dhis.system.objectmapper.AggregatedOrgUnitDataValueRowMapper;
import org.hisp.dhis.system.objectmapper.AggregatedOrgUnitIndicatorValueRowMapper;

public class JdbcAggregatedOrgUnitDataValueStore
    implements AggregatedOrgUnitDataValueStore
{
    private static final Log log = LogFactory.getLog( JdbcAggregatedOrgUnitDataValueStore.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    // -------------------------------------------------------------------------
    // AggregatedOrgUnitDataValueStore implementation
    // -------------------------------------------------------------------------

    public Double getAggregatedDataValue( int dataElement, int categoryOptionCombo, int period, int organisationUnit, int organisationUnitGroup )
    {
        final String sql =
            "SELECT value " +
            "FROM aggregatedorgunitdatavalue " +
            "WHERE dataelementid = " + dataElement + " " +
            "AND categoryoptioncomboid = " + categoryOptionCombo + " " +
            "AND periodid = " + period + " " +
            "AND organisationunitid = " + organisationUnit + " " +
            "AND organisationunitgroupid = " + organisationUnitGroup;
        
        return statementManager.getHolder().queryForDouble( sql );
    }

    public Collection<AggregatedDataValue> getAggregatedDataValueTotals( Collection<Integer> dataElementIds, 
        Collection<Integer> periodIds, int organisationUnitId, Collection<Integer> organisationUnitGroupIds )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<AggregatedDataValue> mapper = new ObjectMapper<AggregatedDataValue>();
        
        try
        {
            final String sql = 
                "SELECT dataelementid, 0 as categoryoptioncomboid, periodid, organisationunitid, organisationunitgroupid, periodtypeid, level, SUM(value) as value " +
                "FROM aggregatedorgunitdatavalue " +
                "WHERE dataelementid IN ( " + getCommaDelimitedString( dataElementIds ) + " ) " +
                "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid = " + organisationUnitId + " " +
                "AND organisationunitgroupid IN ( " + getCommaDelimitedString( organisationUnitGroupIds ) + " ) " +
                "GROUP BY dataelementid, periodid, organisationunitid, organisationunitgroupid, periodtypeid, level";
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new AggregatedOrgUnitDataValueRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated org unit data value", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public void deleteAggregatedDataValues( Collection<Integer> dataElementIds, Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        final String sql =
            "DELETE FROM aggregatedorgunitdatavalue " +
            "WHERE dataelementid IN ( " + getCommaDelimitedString( dataElementIds ) + " ) " +
            "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
            "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
        
        statementManager.getHolder().executeUpdate( sql );
    }

    public void deleteAggregatedDataValues( Collection<Integer> periodIds )
    {
        final String sql =
            "DELETE FROM aggregatedorgunitdatavalue " +
            "WHERE periodid IN ( " + getCommaDelimitedString( periodIds ) + " )";
        
        statementManager.getHolder().executeUpdate( sql );
    }

    public void deleteAggregatedDataValues()
    {
        final String sql = "DELETE FROM aggregatedorgunitdatavalue";
        
        statementManager.getHolder().executeUpdate( sql );
    }

    public void createIndex( boolean dataElement, boolean indicator )
    {
        if ( dataElement )
        {
            try
            {
                final String sql = "CREATE INDEX aggregatedorgunitdatavalue_index ON aggregatedorgunitdatavalue (dataelementid, categoryoptioncomboid, periodid, organisationunitid, organisationunitgroupid)";        
                statementManager.getHolder().executeUpdate( sql, true );
            }
            catch ( Exception ex )
            {
                log.debug( "Index already exists" );
            }
        }
        
        if ( indicator )
        {
            try
            {
                final String sql = "CREATE INDEX aggregatedorgunitindicatorvalue_index ON aggregatedorgunitindicatorvalue (indicatorid, periodid, organisationunitid, organisationunitgroupid)";        
                statementManager.getHolder().executeUpdate( sql, true );
            }
            catch ( Exception ex )
            {
                log.debug( "Index already exists" );
            }
        }
    }
    
    public void dropIndex( boolean dataElement, boolean indicator )
    {
        if ( dataElement )
        {
            try
            {
                final String sql = "DROP INDEX aggregatedorgunitdatavalue_index";
                statementManager.getHolder().executeUpdate( sql, true );
            }
            catch ( Exception ex )
            {
                log.debug( "Index does not exist" );
            }
        }
        
        if ( indicator )
        {
            try
            {
                final String sql = "DROP INDEX aggregatedorgunitindicatorvalue_index";
                statementManager.getHolder().executeUpdate( sql, true );
            }
            catch ( Exception ex )
            {
                log.debug( "Index does not exist" );
            }
        }
    }
    
    // -------------------------------------------------------------------------
    // AggregatedIndicatorValue
    // -------------------------------------------------------------------------

    public Double getAggregatedIndicatorValue( int indicator, int period, int organisationUnit, int organisationUnitGroup )
    {
        final String sql =
            "SELECT value " +
            "FROM aggregatedorgunitindicatorvalue " +
            "WHERE indicatorid = " + indicator + " " +
            "AND periodid = " + period + " " +
            "AND organisationunitid = " + organisationUnit + " " +
            "AND organisationunitgroupid = " + organisationUnitGroup;
        
        return statementManager.getHolder().queryForDouble( sql );
    }

    public Collection<AggregatedIndicatorValue> getAggregatedIndicatorValues( Collection<Integer> indicatorIds, 
        Collection<Integer> periodIds, int organisationUnitId, Collection<Integer> organisationUnitGroupIds )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<AggregatedIndicatorValue> mapper = new ObjectMapper<AggregatedIndicatorValue>();
        
        try
        {
            final String sql =
                "SELECT * " +
                "FROM aggregatedorgunitindicatorvalue " +
                "WHERE indicatorid IN ( " + getCommaDelimitedString( indicatorIds ) + " ) " +
                "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid = " + organisationUnitId + " " +
                "AND organisationunitgroupid IN ( " + getCommaDelimitedString( organisationUnitGroupIds ) + " )";
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new AggregatedOrgUnitIndicatorValueRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated indicator value", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public void deleteAggregatedIndicatorValues( Collection<Integer> indicatorIds, Collection<Integer> periodIds,
        Collection<Integer> organisationUnitIds )
    {
        final String sql =
            "DELETE FROM aggregatedorgunitindicatorvalue " +
            "WHERE indicatorid IN ( " + getCommaDelimitedString( indicatorIds ) + " ) " +
            "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
            "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
        
        statementManager.getHolder().executeUpdate( sql );
    }

    public void deleteAggregatedIndicatorValues( Collection<Integer> periodIds )
    {
        final String sql =
            "DELETE FROM aggregatedorgunitindicatorvalue " +
            "WHERE periodid IN ( " + getCommaDelimitedString( periodIds ) + " )";

        statementManager.getHolder().executeUpdate( sql );
    }
    
    public void deleteAggregatedIndicatorValues()
    {
        final String sql = "DELETE FROM aggregatedorgunitindicatorvalue ";
        
        statementManager.getHolder().executeUpdate( sql );
    }
}
