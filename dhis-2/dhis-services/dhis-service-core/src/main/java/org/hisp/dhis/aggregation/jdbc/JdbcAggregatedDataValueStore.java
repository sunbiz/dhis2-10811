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

import static org.hisp.dhis.system.util.ConversionUtils.getIdentifiers;
import static org.hisp.dhis.system.util.TextUtils.getCommaDelimitedString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.amplecode.quick.StatementHolder;
import org.amplecode.quick.StatementManager;
import org.amplecode.quick.mapper.ObjectMapper;
import org.amplecode.quick.mapper.RowMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.AggregatedDataValue;
import org.hisp.dhis.aggregation.AggregatedDataValueStore;
import org.hisp.dhis.aggregation.AggregatedIndicatorValue;
import org.hisp.dhis.aggregation.AggregatedMapValue;
import org.hisp.dhis.aggregation.StoreIterator;
import org.hisp.dhis.completeness.DataSetCompletenessResult;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DeflatedDataValue;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.system.objectmapper.AggregatedDataMapValueRowMapper;
import org.hisp.dhis.system.objectmapper.AggregatedDataSetCompletenessRowMapper;
import org.hisp.dhis.system.objectmapper.AggregatedDataValueRowMapper;
import org.hisp.dhis.system.objectmapper.AggregatedIndicatorMapValueRowMapper;
import org.hisp.dhis.system.objectmapper.AggregatedIndicatorValueRowMapper;
import org.hisp.dhis.system.objectmapper.DataValueRowMapper;
import org.hisp.dhis.system.objectmapper.DeflatedDataValueRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Lars Helge Overland
 */
public class JdbcAggregatedDataValueStore
    implements AggregatedDataValueStore
{
    private int FETCH_SIZE = 1000; // Number of rows to fetch from db for large resultset

    private static final Log log = LogFactory.getLog( JdbcAggregatedDataValueStore.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    private JdbcTemplate jdbcTemplate;
    
    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // AggregatedDataValue
    // -------------------------------------------------------------------------
    
    public Double getAggregatedDataValue( int dataElement, int period, int organisationUnit )
    {
        final String sql = 
            "SELECT SUM(value) " +
            "FROM aggregateddatavalue " +
            "WHERE dataelementid = " + dataElement + " " +
            "AND periodid = " + period + " " +
            "AND organisationunitid = " + organisationUnit;
        
        return statementManager.getHolder().queryForDouble( sql );
    }

    public Double getAggregatedDataValue( int dataElement, int categoryOptionCombo, int period, int organisationUnit )
    {
        final String sql =
            "SELECT value " +
            "FROM aggregateddatavalue " +
            "WHERE dataelementid = " + dataElement + " " +
            "AND categoryoptioncomboid = " + categoryOptionCombo + " " +
            "AND periodid = " + period + " " +
            "AND organisationunitid = " + organisationUnit;
        
        return statementManager.getHolder().queryForDouble( sql );
    }

    public Double getAggregatedDataValue( int dataElement, int categoryOptionCombo, Collection<Integer> periodIds, int organisationUnit )
    {
        final String sql =
            "SELECT SUM(value) " +
            "FROM aggregateddatavalue " +
            "WHERE dataelementid = " + dataElement + " " +
            "AND categoryoptioncomboid = " + categoryOptionCombo + " " +
            "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
            "AND organisationunitid = " + organisationUnit;
        
        return statementManager.getHolder().queryForDouble( sql );
    }
    
    public Double getAggregatedDataValue( DataElement dataElement, DataElementCategoryOption categoryOption, Period period, OrganisationUnit organisationUnit )
    {
        String ids = getCommaDelimitedString( getIdentifiers( DataElementCategoryOptionCombo.class, categoryOption.getCategoryOptionCombos() ) );
        
        final String sql =
            "SELECT SUM(value) " +
            "FROM aggregateddatavalue " +
            "WHERE dataelementid = " + dataElement.getId() + " " +
            "AND categoryoptioncomboid IN (" + ids + ") " +
            "AND periodid = " + period.getId() + " " +
            "AND organisationunitid = " + organisationUnit.getId();

        return statementManager.getHolder().queryForDouble( sql );
    }

    public Collection<AggregatedDataValue> getAggregatedDataValues( Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<AggregatedDataValue> mapper = new ObjectMapper<AggregatedDataValue>();
        
        try
        {
            final String sql = 
                "SELECT * " +
                "FROM aggregateddatavalue " +
                "WHERE periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new AggregatedDataValueRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated data value", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public Collection<AggregatedDataValue> getAggregatedDataValueTotals( Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<AggregatedDataValue> mapper = new ObjectMapper<AggregatedDataValue>();
        
        try
        {
            final String sql = 
                "SELECT dataelementid, 0 as categoryoptioncomboid, periodid, organisationunitid, periodtypeid, level, SUM(value) as value " +
                "FROM aggregateddatavalue " +
                "WHERE periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " ) " +
                "GROUP BY dataelementid, periodid, organisationunitid, periodtypeid, level";
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new AggregatedDataValueRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated data value", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public Collection<AggregatedDataValue> getAggregatedDataValues( int dataElementId, 
        Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<AggregatedDataValue> mapper = new ObjectMapper<AggregatedDataValue>();
        
        try
        {
            final String sql = 
                "SELECT * " +
                "FROM aggregateddatavalue " +
                "WHERE dataelementid = " + dataElementId + " " +
                "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new AggregatedDataValueRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated data value", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public Collection<AggregatedDataValue> getAggregatedDataValues( Collection<Integer> dataElementIds, Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<AggregatedDataValue> mapper = new ObjectMapper<AggregatedDataValue>();
        
        try
        {
            final String sql = 
                "SELECT * " +
                "FROM aggregateddatavalue " +
                "WHERE dataelementid IN ( " + getCommaDelimitedString( dataElementIds ) + " ) " +
                "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new AggregatedDataValueRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated data value", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public Collection<AggregatedDataValue> getAggregatedDataValueTotals( Collection<Integer> dataElementIds, Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<AggregatedDataValue> mapper = new ObjectMapper<AggregatedDataValue>();
        
        try
        {
            final String sql = 
                "SELECT dataelementid, 0 as categoryoptioncomboid, periodid, organisationunitid, periodtypeid, level, SUM(value) as value " +
                "FROM aggregateddatavalue " +
                "WHERE dataelementid IN ( " + getCommaDelimitedString( dataElementIds ) + " ) " +
                "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " ) " +
                "GROUP BY dataelementid, periodid, organisationunitid, periodtypeid, level";
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new AggregatedDataValueRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated data value", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public StoreIterator<AggregatedDataValue> getAggregatedDataValuesAtLevel( OrganisationUnit rootOrgunit, OrganisationUnitLevel level, Collection<Period> periods )
    {
        final StatementHolder holder = statementManager.getHolder( false );

        try
        {
            int rootlevel = rootOrgunit.getLevel();

            String periodids = getCommaDelimitedString( getIdentifiers(Period.class, periods));

            final String sql =
                "SELECT dataelementid, categoryoptioncomboid, periodid, adv.organisationunitid, periodtypeid, adv.level, value " +
                "FROM aggregateddatavalue AS adv " +
                "INNER JOIN _orgunitstructure AS ous on adv.organisationunitid=ous.organisationunitid " +
                "WHERE adv.level = " + level.getLevel() +
                " AND ous.idlevel" + rootlevel + "=" + rootOrgunit.getId() +
                " AND adv.periodid IN (" + periodids + ") ";

            Statement statement = holder.getStatement();

            statement.setFetchSize( FETCH_SIZE );

            final ResultSet resultSet = statement.executeQuery( sql );

            RowMapper<AggregatedDataValue> rm = new AggregatedDataValueRowMapper();
            
            return new JdbcStoreIterator<AggregatedDataValue>( resultSet, holder, rm );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated data values", ex );
        }
        finally
        {
            // Don't close holder or we lose resultset - iterator must close
        }
    }

    public int countDataValuesAtLevel( OrganisationUnit rootOrgunit, OrganisationUnitLevel level, Collection<Period> periods )
    {
        final String periodids = getCommaDelimitedString( getIdentifiers( Period.class, periods ) );

        final String sql =
            "SELECT count(*) " +
            "FROM aggregateddatavalue AS adv " +
            "INNER JOIN _orgunitstructure AS ous on adv.organisationunitid=ous.organisationunitid " +
            "WHERE adv.level = " + level.getLevel() +
            " AND ous.idlevel" + rootOrgunit.getLevel() + "=" + rootOrgunit.getId() +
            " AND adv.periodid IN (" + periodids + ") ";

        return statementManager.getHolder().queryForInteger( sql );
    }

    public void deleteAggregatedDataValues( Collection<Integer> dataElementIds, Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        final String sql =
            "DELETE FROM aggregateddatavalue " +
            "WHERE dataelementid IN ( " + getCommaDelimitedString( dataElementIds ) + " ) " +
            "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
            "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
        
        statementManager.getHolder().executeUpdate( sql );
    }

    public void deleteAggregatedDataValues( Collection<Integer> periodIds )
    {
        final String sql =
            "DELETE FROM aggregateddatavalue " +
            "WHERE periodid IN ( " + getCommaDelimitedString( periodIds ) + " )";
        
        statementManager.getHolder().executeUpdate( sql );
    }

    public void deleteAggregatedDataValues()
    {
        final String sql = "DELETE FROM aggregateddatavalue";
        
        statementManager.getHolder().executeUpdate( sql );
    }
    
    public void createIndex( boolean dataElement, boolean indicator )
    {
        if ( dataElement )
        {
            try
            {
                final String sql = "CREATE INDEX aggregateddatavalue_index ON aggregateddatavalue (dataelementid, categoryoptioncomboid, periodid, organisationunitid)";        
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
                final String sql = "CREATE INDEX aggregatedindicatorvalue_index ON aggregatedindicatorvalue (indicatorid, periodid, organisationunitid)";        
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
                final String sql = "DROP INDEX aggregateddatavalue_index";
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
                final String sql = "DROP INDEX aggregatedindicatorvalue_index";
                statementManager.getHolder().executeUpdate( sql, true );
            }
            catch ( Exception ex )
            {
                log.debug( "Index does not exist" );
            }
        }
    }

    // -------------------------------------------------------------------------
    // AggregatedDataMapValue
    // -------------------------------------------------------------------------
    
    public Collection<AggregatedMapValue> getAggregatedDataMapValues( int dataElementId, int periodId, Collection<Integer> organisationUnitIds )
    {
        final String sql = 
            "SELECT a.periodid, o.organisationunitid, o.name, SUM(a.value) AS value " +
            "FROM aggregateddatavalue AS a " +
            "JOIN organisationunit AS o ON (a.organisationunitid=o.organisationunitid) " +
            "WHERE a.dataelementid  = " + dataElementId + " " +
            "AND a.periodid = " + periodId + " " + 
            "AND a.organisationunitid IN (" + getCommaDelimitedString( organisationUnitIds ) + ") " +
            "GROUP BY a.periodid, o.organisationunitid, o.name";
        
        return jdbcTemplate.query( sql, new AggregatedDataMapValueRowMapper() );        
    }

    public Collection<AggregatedMapValue> getAggregatedDataMapValues( Collection<Integer> dataElementIds, int periodId, int organisationUnitId )
    {
        final String sql = 
            "SELECT d.name, a.periodid, SUM(a.value) AS value " +
            "FROM aggregateddatavalue AS a " +
            "JOIN dataelement AS d ON (a.dataelementid = d.dataelementid) " +
            "WHERE a.dataelementid IN (" + getCommaDelimitedString( dataElementIds ) + ") " +
            "AND a.periodid = " + periodId + " " + 
            "AND a.organisationunitid = " + organisationUnitId + " " +
            "GROUP BY d.name, a.periodid";
        
        return jdbcTemplate.query( sql, new org.springframework.jdbc.core.RowMapper<AggregatedMapValue>()
        {
            public AggregatedMapValue mapRow( ResultSet resultSet, int rowNum )
                throws SQLException
            {
                AggregatedMapValue value = new AggregatedMapValue();
                value.setDataElementName( resultSet.getString( 1 ) );
                value.setPeriodId( resultSet.getInt( 2 ) );
                value.setValue( resultSet.getDouble( 3 ) );
                return value;
            }
        } );
    }

    // -------------------------------------------------------------------------
    // AggregatedIndicatorValue
    // -------------------------------------------------------------------------

    public Double getAggregatedIndicatorValue( int indicator, int period, int organisationUnit )
    {
        final String sql =
            "SELECT value " +
            "FROM aggregatedindicatorvalue " +
            "WHERE indicatorid = " + indicator + " " +
            "AND periodid = " + period + " " +
            "AND organisationunitid = " + organisationUnit;
        
        return statementManager.getHolder().queryForDouble( sql );
    }

    public Collection<AggregatedIndicatorValue> getAggregatedIndicatorValues( Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<AggregatedIndicatorValue> mapper = new ObjectMapper<AggregatedIndicatorValue>();
        
        try
        {
            final String sql =
                "SELECT * " +
                "FROM aggregatedindicatorvalue " +
                "WHERE periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new AggregatedIndicatorValueRowMapper() );
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

    public Collection<AggregatedIndicatorValue> getAggregatedIndicatorValues( Collection<Integer> indicatorIds, 
        Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<AggregatedIndicatorValue> mapper = new ObjectMapper<AggregatedIndicatorValue>();
        
        try
        {
            final String sql =
                "SELECT * " +
                "FROM aggregatedindicatorvalue " +
                "WHERE indicatorid IN ( " + getCommaDelimitedString( indicatorIds ) + " ) " +
                "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
                "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new AggregatedIndicatorValueRowMapper() );
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
            "DELETE FROM aggregatedindicatorvalue " +
            "WHERE indicatorid IN ( " + getCommaDelimitedString( indicatorIds ) + " ) " +
            "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
            "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " )";
        
        statementManager.getHolder().executeUpdate( sql );
    }

    public void deleteAggregatedIndicatorValues( Collection<Integer> periodIds )
    {
        final String sql =
            "DELETE FROM aggregatedindicatorvalue " +
            "WHERE periodid IN ( " + getCommaDelimitedString( periodIds ) + " )";

        statementManager.getHolder().executeUpdate( sql );
    }
    
    public void deleteAggregatedIndicatorValues()
    {
        final String sql = "DELETE FROM aggregatedindicatorvalue";
        
        statementManager.getHolder().executeUpdate( sql );
    }

    @Override
    public StoreIterator<AggregatedIndicatorValue> getAggregatedIndicatorValuesAtLevel( OrganisationUnit rootOrgunit, OrganisationUnitLevel level, Collection<Period> periods )
    {
        final StatementHolder holder = statementManager.getHolder( false );

        try
        {
            int rootlevel = rootOrgunit.getLevel();

            String periodids = getCommaDelimitedString( getIdentifiers(Period.class, periods));

            final String sql =
                "SELECT aiv.* " +
                "FROM aggregatedindicatorvalue AS aiv " +
                "INNER JOIN _orgunitstructure AS ous on aiv.organisationunitid=ous.organisationunitid " +
                "WHERE aiv.level = " + level.getLevel() +
                " AND ous.idlevel" + rootlevel + "=" + rootOrgunit.getId() +
                " AND aiv.periodid IN (" + periodids + ") ";

            Statement statement = holder.getStatement();

            statement.setFetchSize( FETCH_SIZE );

            final ResultSet resultSet = statement.executeQuery( sql );

            RowMapper<AggregatedIndicatorValue> rm = new AggregatedIndicatorValueRowMapper();
            return new JdbcStoreIterator<AggregatedIndicatorValue>( resultSet, holder, rm );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated indicator values", ex );
        }
        finally
        {
            // don't close holder or we lose resultset - iterator must close
        }
    }

    @Override
    public int countIndicatorValuesAtLevel( OrganisationUnit rootOrgunit, OrganisationUnitLevel level, Collection<Period> periods )
    {
        final StatementHolder holder = statementManager.getHolder();

        try
        {
            int rootlevel = rootOrgunit.getLevel();

            String periodids = getCommaDelimitedString( getIdentifiers(Period.class, periods));

            final String sql =
                "SELECT count(*) as rowcount " +
                "FROM aggregatedindicatorvalue AS aiv " +
                "INNER JOIN _orgunitstructure AS ous on aiv.organisationunitid=ous.organisationunitid " +
                "WHERE aiv.level = " + level.getLevel() +
                " AND ous.idlevel" + rootlevel + "=" + rootOrgunit.getId() +
                " AND aiv.periodid IN (" + periodids + ") ";

            Statement statement = holder.getStatement();

            final ResultSet resultSet = statement.executeQuery( sql );

            resultSet.next();

            return resultSet.getInt( "rowcount");
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get aggregated indicator values", ex );
        }
        finally
        {
            holder.close();
        }
    }

    // -------------------------------------------------------------------------
    // AggregatedIndicatorMapValue
    // -------------------------------------------------------------------------

    public Collection<AggregatedMapValue> getAggregatedIndicatorMapValues( int indicatorId, int periodId, Collection<Integer> organisationUnitIds )
    {
        final String sql = 
            "SELECT a.periodid, o.organisationunitid, o.name, a.value, a.factor, a.numeratorvalue, a.denominatorvalue " +
            "FROM aggregatedindicatorvalue AS a " +
            "JOIN organisationunit AS o ON (a.organisationunitid=o.organisationunitid) " +
            "WHERE a.indicatorid  = " + indicatorId + " " +
            "AND a.periodid = " + periodId + " " +
            "AND a.organisationunitid IN (" + getCommaDelimitedString( organisationUnitIds ) + ")";
        
        return jdbcTemplate.query( sql, new AggregatedIndicatorMapValueRowMapper() );
    }

    // -------------------------------------------------------------------------
    // DataSetCompleteness
    // -------------------------------------------------------------------------

    public Collection<DataSetCompletenessResult> getAggregatedDataSetCompleteness( Collection<Integer> dataSetIds, Collection<Integer> periodIds,
        Collection<Integer> organisationUnitIds )
    {
        final String sql =
            "SELECT datasetid, periodid, organisationunitid, value " +
            "FROM aggregateddatasetcompleteness " +
            "WHERE datasetid IN ( " + getCommaDelimitedString( dataSetIds ) + " ) " +
            "AND periodid IN ( " + getCommaDelimitedString( periodIds ) + " ) " +
            "AND organisationunitid IN ( " + getCommaDelimitedString( organisationUnitIds ) + " ) ";
        
        return jdbcTemplate.query( sql, new AggregatedDataSetCompletenessRowMapper() );
    }
    
    // -------------------------------------------------------------------------
    // DataValue
    // -------------------------------------------------------------------------

    public Collection<DeflatedDataValue> getDeflatedDataValues( int dataElementId, int periodId, Collection<Integer> sourceIds )
    {
        final StatementHolder holder = statementManager.getHolder();
            
        final ObjectMapper<DeflatedDataValue> mapper = new ObjectMapper<DeflatedDataValue>();
        
        try
        {
            final String sql =
                "SELECT * FROM datavalue " +
                "WHERE dataelementid = " + dataElementId + " " +
                "AND periodid = " + periodId + " " +
                "AND sourceid IN ( " + getCommaDelimitedString( sourceIds ) + " )";
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new DeflatedDataValueRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get deflated data values", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public DataValue getDataValue( int dataElementId, int categoryOptionComboId, int periodId, int sourceId )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<DataValue> mapper = new ObjectMapper<DataValue>();
        
        try
        {
            final String sql =
                "SELECT * FROM datavalue " +
                "WHERE dataelementid = " + dataElementId + " " +
                "AND categoryoptioncomboid = " + categoryOptionComboId + " " +
                "AND periodid = " + periodId + " " +
                "AND sourceid = " + sourceId;
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getObject( resultSet, new DataValueRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get deflated data values", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public Map<DataElementOperand, String> getDataValueMap( int periodId, int sourceId )
    {
        final StatementHolder holder = statementManager.getHolder();
            
        try
        {
            final String sql =
                "SELECT dataelementid, categoryoptioncomboid, value " +
                "FROM datavalue " +
                "WHERE periodid = " + periodId + " " +
                "AND sourceid = " + sourceId;
            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            final Map<DataElementOperand, String> map = new HashMap<DataElementOperand, String>();
            
            while ( resultSet.next() )
            {
                final DataElementOperand operand = new DataElementOperand( resultSet.getInt( 1 ), resultSet.getInt( 2 ) );
                
                map.put( operand, resultSet.getString( 3 ) );
            }
            
            return map;
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get DataValues", ex );
        }
        finally
        {
            holder.close();
        }
    }
}
