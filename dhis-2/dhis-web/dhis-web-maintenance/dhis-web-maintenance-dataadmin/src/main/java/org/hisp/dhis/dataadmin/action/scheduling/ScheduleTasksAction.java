package org.hisp.dhis.dataadmin.action.scheduling;

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

import static org.hisp.dhis.setting.SystemSettingManager.DEFAULT_SCHEDULED_PERIOD_TYPES;
import static org.hisp.dhis.setting.SystemSettingManager.KEY_ORGUNITGROUPSET_AGG_LEVEL;
import static org.hisp.dhis.setting.SystemSettingManager.DEFAULT_ORGUNITGROUPSET_AGG_LEVEL;
import static org.hisp.dhis.setting.SystemSettingManager.KEY_SCHEDULED_PERIOD_TYPES;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.setting.SystemSettingManager;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.scheduling.SchedulingManager;
import org.hisp.dhis.system.scheduling.Scheduler;

import com.opensymphony.xwork2.Action;

/**
 * @author Lars Helge Overland
 */
public class ScheduleTasksAction
    implements Action
{
    private static final String STRATEGY_LAST_12_DAILY = "last12Daily";
    private static final String STRATEGY_LAST_6_DAILY_6_TO_12_WEEKLY = "last6Daily6To12Weekly";
        
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SystemSettingManager systemSettingManager;
    
    public void setSystemSettingManager( SystemSettingManager systemSettingManager )
    {
        this.systemSettingManager = systemSettingManager;
    }
    
    private SchedulingManager schedulingManager;

    public void setSchedulingManager( SchedulingManager schedulingManager )
    {
        this.schedulingManager = schedulingManager;
    }
    
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private boolean execute;

    public void setExecute( boolean execute )
    {
        this.execute = execute;
    }
    
    private boolean schedule;

    public void setSchedule( boolean schedule )
    {
        this.schedule = schedule;
    }

    private Set<String> scheduledPeriodTypes = new HashSet<String>();

    public Set<String> getScheduledPeriodTypes()
    {
        return scheduledPeriodTypes;
    }

    public void setScheduledPeriodTypes( Set<String> scheduledPeriodTypes )
    {
        this.scheduledPeriodTypes = scheduledPeriodTypes;
    }
    
    private Integer orgUnitGroupSetAggLevel;
    
    public Integer getOrgUnitGroupSetAggLevel()
    {
        return orgUnitGroupSetAggLevel;
    }

    public void setOrgUnitGroupSetAggLevel( Integer orgUnitGroupSetAggLevel )
    {
        this.orgUnitGroupSetAggLevel = orgUnitGroupSetAggLevel;
    }

    private String dataMartStrategy;

    public String getDataMartStrategy()
    {
        return dataMartStrategy;
    }

    public void setDataMartStrategy( String dataMartStrategy )
    {
        this.dataMartStrategy = dataMartStrategy;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String status;

    public String getStatus()
    {
        return status;
    }

    private boolean running;

    public boolean isRunning()
    {
        return running;
    }
        
    private List<OrganisationUnitLevel> levels;

    public List<OrganisationUnitLevel> getLevels()
    {
        return levels;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public String execute()
    {
        if ( execute )
        {
            schedulingManager.executeTasks();
        }
        else if ( schedule )
        {
            systemSettingManager.saveSystemSetting( KEY_SCHEDULED_PERIOD_TYPES, (HashSet<String>) scheduledPeriodTypes );
            systemSettingManager.saveSystemSetting( KEY_ORGUNITGROUPSET_AGG_LEVEL, orgUnitGroupSetAggLevel );
            
            if ( Scheduler.STATUS_RUNNING.equals( schedulingManager.getTaskStatus() ) )
            {
                schedulingManager.stopTasks();
            }
            else
            {
                Map<String, String> keyCronMap = new HashMap<String, String>();
                
                if ( STRATEGY_LAST_12_DAILY.equals( dataMartStrategy ) )
                {
                    keyCronMap.put( SchedulingManager.TASK_DATAMART_LAST_12_MONTHS, Scheduler.CRON_DAILY_0AM );
                }
                else if ( STRATEGY_LAST_6_DAILY_6_TO_12_WEEKLY.equals( dataMartStrategy ) )
                {
                    keyCronMap.put( SchedulingManager.TASK_DATAMART_LAST_6_MONTS, Scheduler.CRON_DAILY_0AM_EXCEPT_SUNDAY );
                    keyCronMap.put( SchedulingManager.TASK_DATAMART_FROM_6_TO_12_MONTS, Scheduler.CRON_WEEKLY_SUNDAY_0AM );
                }
                
                schedulingManager.scheduleTasks( keyCronMap );
            }
        }
        else
        {
            scheduledPeriodTypes = (Set<String>) systemSettingManager.getSystemSetting( KEY_SCHEDULED_PERIOD_TYPES, DEFAULT_SCHEDULED_PERIOD_TYPES );
            orgUnitGroupSetAggLevel = (Integer) systemSettingManager.getSystemSetting( KEY_ORGUNITGROUPSET_AGG_LEVEL, DEFAULT_ORGUNITGROUPSET_AGG_LEVEL );
            dataMartStrategy = schedulingManager.getScheduledTasks().containsKey( SchedulingManager.TASK_DATAMART_LAST_12_MONTHS ) ? 
                STRATEGY_LAST_12_DAILY : STRATEGY_LAST_6_DAILY_6_TO_12_WEEKLY;
        }

        status = schedulingManager.getTaskStatus();        
        running = Scheduler.STATUS_RUNNING.equals( status );
        levels = organisationUnitService.getOrganisationUnitLevels();
        
        return SUCCESS;
    }
}
