/*
 * Copyright (c) 2004-2009, University of Oslo
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

package org.hisp.dhis.patient.scheduling;

import static org.hisp.dhis.patient.scheduling.CaseAggregateConditionSchedulingManager.TASK_AGGREGATE_QUERY_BUILDER_LAST_12_MONTH;
import static org.hisp.dhis.patient.scheduling.CaseAggregateConditionSchedulingManager.TASK_AGGREGATE_QUERY_BUILDER_LAST_3_MONTH;
import static org.hisp.dhis.patient.scheduling.CaseAggregateConditionSchedulingManager.TASK_AGGREGATE_QUERY_BUILDER_LAST_6_MONTH;
import static org.hisp.dhis.patient.scheduling.CaseAggregateConditionSchedulingManager.TASK_AGGREGATE_QUERY_BUILDER_LAST_MONTH;
import static org.hisp.dhis.setting.SystemSettingManager.DEFAULT_SCHEDULE_AGGREGATE_QUERY_BUILDER_TASK_STRATEGY;
import static org.hisp.dhis.setting.SystemSettingManager.KEY_SCHEDULE_AGGREGATE_QUERY_BUILDER_TASK_STRATEGY;
import static org.hisp.dhis.system.notification.NotificationLevel.INFO;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.hisp.dhis.caseaggregation.CaseAggregationCondition;
import org.hisp.dhis.caseaggregation.CaseAggregationConditionService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.CalendarPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.scheduling.TaskId;
import org.hisp.dhis.setting.SystemSettingManager;
import org.hisp.dhis.system.notification.Notifier;
import org.hisp.dhis.system.util.Clock;
import org.hisp.dhis.system.util.SystemUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author Chau Thu Tran
 * 
 * @version RunCaseAggregateConditionTask.java 9:52:10 AM Oct 10, 2012 $
 */
public class CaseAggregateConditionTask
    implements Runnable
{
    public static final String STORED_BY_DHIS_SYSTEM = "DHIS-System";

    private CaseAggregationConditionService aggregationConditionService;

    private DataValueService dataValueService;

    private JdbcTemplate jdbcTemplate;

    private DataElementService dataElementService;

    private DataElementCategoryService categoryService;

    private SystemSettingManager systemSettingManager;

    private ProgramStageInstanceService programStageInstanceService;
    
    private OrganisationUnitService organisationUnitService;
    
    private Notifier notifier;

    public void setNotifier( Notifier notifier )
    {
        this.notifier = notifier;
    }
    
    private TaskId taskId;

    public void setTaskId( TaskId taskId )
    {
        this.taskId = taskId;
    }
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CaseAggregateConditionTask( CaseAggregationConditionService aggregationConditionService,
        DataValueService dataValueService, JdbcTemplate jdbcTemplate, DataElementService dataElementService,
        DataElementCategoryService categoryService, SystemSettingManager systemSettingManager,
        ProgramStageInstanceService programStageInstanceService, OrganisationUnitService organisationUnitService, Notifier notifier )
    {
        this.aggregationConditionService = aggregationConditionService;
        this.dataValueService = dataValueService;
        this.jdbcTemplate = jdbcTemplate;
        this.dataElementService = dataElementService;
        this.categoryService = categoryService;
        this.systemSettingManager = systemSettingManager;
        this.programStageInstanceService = programStageInstanceService;
        this.organisationUnitService = organisationUnitService;
        this.notifier = notifier;
    }

    // -------------------------------------------------------------------------
    // Runnable implementation
    // -------------------------------------------------------------------------

    @Override
    public void run()
    {
        final int cpuCores = SystemUtils.getCpuCores();
        Clock clock = new Clock().startClock().logTime( "Aggregate process started, number of CPU cores: " + cpuCores + ", " + SystemUtils.getMemoryString() );
        notifier.clear( taskId ).notify( taskId, "Aggregate process started" );
 
        String taskStrategy = (String) systemSettingManager.getSystemSetting(
            KEY_SCHEDULE_AGGREGATE_QUERY_BUILDER_TASK_STRATEGY, DEFAULT_SCHEDULE_AGGREGATE_QUERY_BUILDER_TASK_STRATEGY );

        String datasetSQL = "select dm.datasetid as datasetid, pt.name as periodname, ds.name as datasetname";
        datasetSQL += "      from caseaggregationcondition cagg inner join datasetmembers dm ";
        datasetSQL += "            on cagg.aggregationdataelementid=dm.dataelementid inner join dataset ds ";
        datasetSQL += "            on ds.datasetid = dm.datasetid inner join periodtype pt ";
        datasetSQL += "            on pt.periodtypeid=ds.periodtypeid ";

        SqlRowSet rsDataset = jdbcTemplate.queryForRowSet( datasetSQL );
        while ( rsDataset.next() )
        {
            int datasetId = rsDataset.getInt( "datasetid" );
            String datasetName = rsDataset.getString( "datasetname" );
            
            Collection<Period> periods = getPeriod( rsDataset.getString( "periodname" ), taskStrategy );

            for ( Period period : periods )
            {
                String sql = "select caseaggregationconditionid, aggregationdataelementid, optioncomboid "
                    + "     from caseaggregationcondition cagg inner join datasetmembers dm "
                    + "             on cagg.aggregationdataelementid=dm.dataelementid " + "inner join dataset ds "
                    + "             on ds.datasetid = dm.datasetid " + "inner join periodtype pt "
                    + "             on pt.periodtypeid=ds.periodtypeid " + "where ds.datasetid = " + datasetId;

                SqlRowSet rs = jdbcTemplate.queryForRowSet( sql );

                Collection<Integer> orgunitIds = programStageInstanceService.getOrganisationUnitIds(
                    period.getStartDate(), period.getEndDate() );
                
                while ( rs.next() )
                {
                    // -------------------------------------------------------------
                    // Get formula, agg-dataelement and option-combo
                    // -------------------------------------------------------------

                    int dataelementId = rs.getInt( "aggregationdataelementid" );
                    int optionComboId = rs.getInt( "optioncomboid" );

                    DataElement dElement = dataElementService.getDataElement( dataelementId );
                    DataElementCategoryOptionCombo optionCombo = categoryService
                        .getDataElementCategoryOptionCombo( optionComboId );

                    CaseAggregationCondition aggCondition = aggregationConditionService.getCaseAggregationCondition( rs
                        .getInt( "caseaggregationconditionid" ) );

                    // ---------------------------------------------------------------------
                    // Aggregation
                    // ---------------------------------------------------------------------

                    for ( Integer orgUnitId : orgunitIds )
                    {
                        OrganisationUnit orgUnit = organisationUnitService.getOrganisationUnit( orgUnitId );
                        DataValue dataValue = dataValueService.getDataValue( orgUnit, dElement, period, optionCombo );

                        Integer resultValue = aggregationConditionService.parseConditition( aggCondition, orgUnit,
                            period );

                        if ( resultValue != null && resultValue != 0 )
                        {
                            // -----------------------------------------------------
                            // Add dataValue
                            // -----------------------------------------------------

                            if ( dataValue == null )
                            {
                                dataValue = new DataValue( dElement, period, orgUnit, "" + resultValue, STORED_BY_DHIS_SYSTEM, new Date(),
                                    null, optionCombo );
                                dataValueService.addDataValue( dataValue );
                            }

                            // -----------------------------------------------------
                            // Update dataValue
                            // -----------------------------------------------------
                            else if ( (double) resultValue != Double.parseDouble( dataValue.getValue() ) )
                            {
                                dataValue.setValue( "" + resultValue );
                                dataValue.setTimestamp( new Date() );
                                sql = "UPDATE datavalue" + " SET value='" + resultValue + "',lastupdated='"
                                    + new Date() + "' where dataelementId=" + dataelementId + " and periodid="
                                    + period.getId() + " and sourceid=" + orgUnit.getId()
                                    + " and categoryoptioncomboid=" + optionComboId + " and storedby='"
                                    + STORED_BY_DHIS_SYSTEM + "'";
                                jdbcTemplate.execute( sql );
                            }
                        }

                        // ---------------------------------------------------------
                        // Delete dataValue
                        // ---------------------------------------------------------
                        else if ( dataValue != null )
                        {
                            dataValueService.deleteDataValue( dataValue );
                        }
                    }
                }

            }
            clock.logTime( "Improrted aggregate data completed for data set " + datasetName );
            notifier.notify( taskId, "Improrted aggregate data completed for data set "  + datasetName );            
        }
        notifier.notify( taskId, INFO, "Improrted aggregate data completed", true );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Collection<Period> getPeriod( String periodTypeName, String taskStrategy )
    {
        Calendar calStartDate = Calendar.getInstance();

        if ( TASK_AGGREGATE_QUERY_BUILDER_LAST_MONTH.equals( taskStrategy ) )
        {
            calStartDate.add( Calendar.MONTH, -1 );
        }
        else if ( TASK_AGGREGATE_QUERY_BUILDER_LAST_3_MONTH.equals( taskStrategy ) )
        {
            calStartDate.add( Calendar.MONTH, -3 );
        }
        else if ( TASK_AGGREGATE_QUERY_BUILDER_LAST_6_MONTH.equals( taskStrategy ) )
        {
            calStartDate.add( Calendar.MONTH, -6 );
        }
        else if ( TASK_AGGREGATE_QUERY_BUILDER_LAST_12_MONTH.equals( taskStrategy ) )
        {
            calStartDate.add( Calendar.MONTH, -12 );
        }

        Date startDate = calStartDate.getTime();

        Calendar calEndDate = Calendar.getInstance();

        Date endDate = calEndDate.getTime();

        CalendarPeriodType periodType = (CalendarPeriodType) CalendarPeriodType.getPeriodTypeByName( periodTypeName );

        return periodType.generatePeriods( startDate, endDate );
    }
}
