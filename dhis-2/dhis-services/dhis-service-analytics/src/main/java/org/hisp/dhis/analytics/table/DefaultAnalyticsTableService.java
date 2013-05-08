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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.analytics.AnalyticsIndex;
import org.hisp.dhis.analytics.AnalyticsTableManager;
import org.hisp.dhis.analytics.AnalyticsTableService;
import org.hisp.dhis.common.IdentifiableObjectUtils;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Cal;
import org.hisp.dhis.scheduling.TaskId;
import org.hisp.dhis.system.notification.Notifier;
import org.hisp.dhis.system.util.Clock;
import org.hisp.dhis.system.util.ConcurrentUtils;
import org.hisp.dhis.system.util.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lars Helge Overland
 */
public class DefaultAnalyticsTableService
    implements AnalyticsTableService
{
    private static final Log log = LogFactory.getLog( DefaultAnalyticsTableService.class );
    
    private AnalyticsTableManager tableManager;
    
    public void setTableManager( AnalyticsTableManager tableManager )
    {
        this.tableManager = tableManager;
    }

    @Autowired
    private OrganisationUnitService organisationUnitService;
    
    @Autowired
    private DataElementService dataElementService;
    
    @Autowired
    private Notifier notifier;

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------
    
    public void update( boolean last3YearsOnly, TaskId taskId )
    {
        Clock clock = new Clock().startClock().logTime( "Starting update, no of processes: " + getProcessNo() );
        
        boolean valid = tableManager.validState();
        
        if ( !valid )
        {
            notifier.notify( taskId, "Table not valid, aborted update" );
            return;
        }
        
        final Date threeYrsAgo = new Cal().subtract( Calendar.YEAR, 2 ).set( 1, 1 ).time();
        final Date earliest = last3YearsOnly ? threeYrsAgo : tableManager.getEarliestData();
        final Date latest = tableManager.getLatestData();
        final String tableName = tableManager.getTableName();
        final List<String> tables = PartitionUtils.getTempTableNames( earliest, latest, tableName );
        
        clock.logTime( "Partition tables: " + tables + ", earliest: " + earliest + ", latest: " + latest + ", last 3 years: " + last3YearsOnly );
        
        notifier.notify( taskId, "Creating analytics tables" );
        
        createTables( tables );
        
        clock.logTime( "Created analytics tables" );
        notifier.notify( taskId, "Populating analytics tables" );
        
        populateTables( tables );
        
        clock.logTime( "Populated analytics tables" );
        notifier.notify( taskId, "Pruned analytics tables" );
        
        pruneTables( tables );
        
        clock.logTime( "Pruned analytics tables" );
        notifier.notify( taskId, "Applying aggregation levels" );
        
        applyAggregationLevels( tables );
        
        clock.logTime( "Applied aggregation levels" );
        notifier.notify( taskId, "Creating indexes" );
        
        createIndexes( tables );
        
        clock.logTime( "Created indexes" );
        notifier.notify( taskId, "Vacuuming tables" );
        
        vacuumTables( tables );
        
        clock.logTime( "Vacuumed tables" );
        notifier.notify( taskId, "Swapping analytics tables" );
        
        swapTables( tables );
        
        clock.logTime( "Table update done" );
        notifier.notify( taskId, "Table update done" );
    }

    public void dropTables()
    {
        List<String> tempTables = PartitionUtils.getTempTableNames( 
            new Cal().set( 1900, 1, 1 ).time(), new Cal().set( 2100, 1, 1 ).time(), tableManager.getTableName() );
        
        for ( String tempTable : tempTables )   
        {
            String realTable = tempTable.replaceFirst( AnalyticsTableManager.TABLE_TEMP_SUFFIX, "" );
            
            tableManager.dropTable( tempTable );
            tableManager.dropTable( realTable );            
        }
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
  
    private void createTables( List<String> tables )
    {
        for ( String table : tables )
        {
            tableManager.createTable( table );
        }
    }
    
    private void populateTables( List<String> tables )
    {
        ConcurrentLinkedQueue<String> tableQ = new ConcurrentLinkedQueue<String>( tables );
        
        List<Future<?>> futures = new ArrayList<Future<?>>();
        
        for ( int i = 0; i < getProcessNo(); i++ )
        {
            futures.add( tableManager.populateTableAsync( tableQ ) );
        }
        
        ConcurrentUtils.waitForCompletion( futures );
    }
    
    private void pruneTables( List<String> tables )
    {
        Iterator<String> iterator = tables.iterator();
        
        while ( iterator.hasNext() )
        {
            if ( tableManager.pruneTable( iterator.next() ) )
            {
                iterator.remove();
            }
        }
    }
    
    private void applyAggregationLevels( List<String> tables )
    {
        int maxLevels = organisationUnitService.getMaxOfOrganisationUnitLevels();
        
        levelLoop : for ( int i = 0; i < maxLevels; i++ )
        {
            int level = maxLevels - i;
            
            Collection<String> dataElements = IdentifiableObjectUtils.getUids( 
                dataElementService.getDataElementsByAggregationLevel( level ) );
            
            if ( dataElements.isEmpty() )
            {
                continue levelLoop;
            }
                        
            ConcurrentLinkedQueue<String> tableQ = new ConcurrentLinkedQueue<String>( tables );

            List<Future<?>> futures = new ArrayList<Future<?>>();
            
            for ( int j = 0; j < getProcessNo(); j++ )
            {
                futures.add( tableManager.applyAggregationLevels( tableQ, dataElements, level ) );
            }

            ConcurrentUtils.waitForCompletion( futures );
        }
    }
    
    private void createIndexes( List<String> tables )
    {
        ConcurrentLinkedQueue<AnalyticsIndex> indexes = new ConcurrentLinkedQueue<AnalyticsIndex>();
        
        List<String> columns = tableManager.getDimensionColumnNames();
        
        for ( String table : tables )
        {
            for ( String column : columns )
            {
                indexes.add( new AnalyticsIndex( table, column ) );
            }
        }
        
        log.info( "No of indexes: " + indexes.size() );
        
        List<Future<?>> futures = new ArrayList<Future<?>>();

        for ( int i = 0; i < getProcessNo(); i++ )
        {
            futures.add( tableManager.createIndexesAsync( indexes ) );
        }

        ConcurrentUtils.waitForCompletion( futures );
    }

    private void vacuumTables( List<String> tables )
    {
        ConcurrentLinkedQueue<String> tableQ = new ConcurrentLinkedQueue<String>( tables );
        
        List<Future<?>> futures = new ArrayList<Future<?>>();
        
        for ( int i = 0; i < getProcessNo(); i++ )
        {
            tableManager.vacuumTablesAsync( tableQ );
        }
        
        ConcurrentUtils.waitForCompletion( futures );        
    }
    
    private void swapTables( List<String> tables )
    {
        for ( String table : tables )
        {
            tableManager.swapTable( table );
        }
    }
    
    private int getProcessNo()
    {
        int cores = SystemUtils.getCpuCores();
        
        return cores > 2 ? ( cores - 1 ) : cores;
    }
}
