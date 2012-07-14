package org.hisp.dhis.analytics.table;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.analytics.AnalyticsIndex;
import org.hisp.dhis.analytics.AnalyticsTableManager;
import org.hisp.dhis.common.CodeGenerator;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;

public abstract class AbstractJdbcTableManager
    implements AnalyticsTableManager
{
    protected static final Log log = LogFactory.getLog( JdbcAnalyticsTableManager.class );

    public static final String PREFIX_ORGUNITGROUPSET = "ougs_";
    public static final String PREFIX_ORGUNITLEVEL = "uidlevel";
    public static final String PREFIX_INDEX = "in_";
    
    private static final String TABLE_TEMP_SUFFIX = "_temp";
    
    @Autowired
    protected OrganisationUnitService organisationUnitService;
    
    @Autowired
    protected DataElementService dataElementService;
    
    @Autowired
    protected OrganisationUnitGroupService organisationUnitGroupService;
   
    @Autowired
    protected StatementBuilder statementBuilder;
    
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------
  
    public String getTempTableName()
    {
        return getTableName() + TABLE_TEMP_SUFFIX;
    }
    
    @Async
    public Future<?> createIndexesAsync( ConcurrentLinkedQueue<AnalyticsIndex> indexes )
    {
        taskLoop : while ( true )
        {
            AnalyticsIndex inx = indexes.poll();
            
            if ( inx == null )
            {
                break taskLoop;
            }
            
            final String index = PREFIX_INDEX + inx.getColumn() + "_" + inx.getTable() + "_" + CodeGenerator.generateCode();
            
            final String sql = "create index " + index + " on " + inx.getTable() + " (" + inx.getColumn() + ")";
                
            executeSilently( sql );
            
            log.info( "Created index: " + index );
        }
        
        return null;
    }

    public void swapTable( String tableName )
    {
        final String realTable = tableName.replaceFirst( TABLE_TEMP_SUFFIX, "" );
        
        final String sqlDrop = "drop table " + realTable;
        
        executeSilently( sqlDrop );
        
        final String sqlAlter = "alter table " + tableName + " rename to " + realTable;
        
        executeSilently( sqlAlter );
    }

    public List<String> getDimensionColumnNames()
    {
        List<String[]> columns = getDimensionColumns();
        
        List<String> columnNames = new ArrayList<String>();
        
        for ( String[] column : columns )
        {
            columnNames.add( column[0] );
        }
        
        return columnNames;
    }

    public boolean pruneTable( String tableName )
    {
        final String sqlCount = "select count(*) from " + tableName;
        
        log.info( "Count SQL: " + sqlCount );
        
        final boolean empty = jdbcTemplate.queryForInt( sqlCount ) == 0;
        
        if ( empty )
        {
            final String sqlDrop = "drop table " + tableName;
            
            executeSilently( sqlDrop );
            
            log.info( "Drop SQL: " + sqlDrop );
            
            return true;
        }
        
        return false;
    }

    @Async
    public Future<?> vacuumTablesAsync( ConcurrentLinkedQueue<String> tables )
    {
        taskLoop : while ( true )
        {
            String table = tables.poll();
            
            if ( table == null )
            {
                break taskLoop;
            }
            
            final String sql = statementBuilder.getVacuum( table );
            
            log.info( "Vacuum SQL: " + sql );
            
            jdbcTemplate.execute( sql );
        }
        
        return null;
    }

    public void dropTable( String tableName )
    {
        final String realTable = tableName.replaceFirst( TABLE_TEMP_SUFFIX, "" );
        
        executeSilently( "drop table " + tableName );
        executeSilently( "drop table " + realTable );
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
  
    /**
     * Executes a SQL statement. Ignores existing tables/indexes when attempting
     * to create new.
     */
    protected void executeSilently( String sql )
    {
        try
        {
            jdbcTemplate.execute( sql );
        }
        catch ( BadSqlGrammarException ex )
        {
            log.warn( ex.getMessage() );
        }
    }
}
