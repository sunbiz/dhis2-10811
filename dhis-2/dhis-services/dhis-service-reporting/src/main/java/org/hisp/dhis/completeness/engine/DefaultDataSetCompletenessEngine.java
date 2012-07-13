package org.hisp.dhis.completeness.engine;

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

import static org.hisp.dhis.setting.SystemSettingManager.DEFAULT_COMPLETENESS_OFFSET;
import static org.hisp.dhis.setting.SystemSettingManager.KEY_COMPLETENESS_OFFSET;
import static org.hisp.dhis.system.notification.NotificationLevel.INFO;
import static org.hisp.dhis.scheduling.TaskCategory.DATAMART;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.hisp.dhis.completeness.DataSetCompletenessEngine;
import org.hisp.dhis.completeness.DataSetCompletenessService;
import org.hisp.dhis.completeness.DataSetCompletenessStore;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.scheduling.TaskId;
import org.hisp.dhis.setting.SystemSettingManager;
import org.hisp.dhis.system.filter.DataSetWithOrganisationUnitsFilter;
import org.hisp.dhis.system.notification.Notifier;
import org.hisp.dhis.system.util.Clock;
import org.hisp.dhis.system.util.ConcurrentUtils;
import org.hisp.dhis.system.util.ConversionUtils;
import org.hisp.dhis.system.util.FilterUtils;
import org.hisp.dhis.system.util.PaginatedList;
import org.hisp.dhis.system.util.SystemUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lars Helge Overland
 */
public class DefaultDataSetCompletenessEngine
    implements DataSetCompletenessEngine
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetCompletenessService completenessService;
    
    public void setCompletenessService( DataSetCompletenessService completenessService )
    {
        this.completenessService = completenessService;
    }

    private DataSetCompletenessStore completenessStore;

    public void setCompletenessStore( DataSetCompletenessStore completenessStore )
    {
        this.completenessStore = completenessStore;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private SystemSettingManager systemSettingManager;

    public void setSystemSettingManager( SystemSettingManager systemSettingManager )
    {
        this.systemSettingManager = systemSettingManager;
    }

    private Notifier notifier;

    public void setNotifier( Notifier notifier )
    {
        this.notifier = notifier;
    }

    // -------------------------------------------------------------------------
    // DataSetCompletenessEngine implementation
    // -------------------------------------------------------------------------

    @Transactional
    public void exportDataSetCompleteness( Collection<Integer> dataSetIds, RelativePeriods relatives,
        Collection<Integer> organisationUnitIds, TaskId id )
    {
        if ( relatives != null )
        {
            Collection<Integer> periodIds = ConversionUtils.getIdentifiers( Period.class,
                periodService.reloadPeriods( relatives.getRelativePeriods() ) );

            exportDataSetCompleteness( dataSetIds, periodIds, organisationUnitIds, id );
        }
    }

    @Transactional
    public void exportDataSetCompleteness( Collection<Integer> dataSetIds, Collection<Integer> periodIds,
        Collection<Integer> organisationUnitIds, TaskId id )
    {
        final int cpuCores = SystemUtils.getCpuCores();
        
        Clock clock = new Clock().startClock().logTime( "Data completeness export process started, number of CPU cores: " + cpuCores + ", " + SystemUtils.getMemoryString() );
        notifier.notify( id, DATAMART, "Completeness export process started" );

        completenessStore.dropIndex();

        clock.logTime( "Dropped potential index" );

        int days = (Integer) systemSettingManager.getSystemSetting( KEY_COMPLETENESS_OFFSET,
            DEFAULT_COMPLETENESS_OFFSET );

        completenessStore.deleteDataSetCompleteness( dataSetIds, periodIds, organisationUnitIds );

        clock.logTime( "Deleted existing completeness data" );
        notifier.notify( id, DATAMART, "Exporting completeness for data sets" );

        Collection<Period> periods = periodService.getPeriods( periodIds );
        Collection<OrganisationUnit> organisationUnits = organisationUnitService.getOrganisationUnits( organisationUnitIds );
        Collection<DataSet> dataSets = dataSetService.getDataSets( dataSetIds );

        dataSets = completenessStore.getDataSetsWithRegistrations( dataSets );

        FilterUtils.filter( dataSets, new DataSetWithOrganisationUnitsFilter() );
        
        List<List<OrganisationUnit>> organisationUnitPages = new PaginatedList<OrganisationUnit>( organisationUnits ).setNumberOfPages( cpuCores ).getPages();
        
        List<Future<?>> futures = new ArrayList<Future<?>>();
        
        for ( List<OrganisationUnit> organisationUnitPage : organisationUnitPages )
        {
            futures.add( completenessService.exportDataSetCompleteness( dataSets, periods, organisationUnitPage, days ) );
        }
        
        ConcurrentUtils.waitForCompletion( futures );
        
        completenessStore.createIndex();

        clock.logTime( "Created index" );

        clock.logTime( "Completeness export process completed" );
        notifier.notify( id, DATAMART, INFO, "Completeness process completed", true );
    }
}
