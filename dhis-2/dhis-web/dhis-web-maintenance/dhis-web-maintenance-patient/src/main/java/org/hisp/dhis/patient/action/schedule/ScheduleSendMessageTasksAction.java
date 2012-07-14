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

package org.hisp.dhis.patient.action.schedule;

import static org.hisp.dhis.setting.SystemSettingManager.DEFAULT_TIME_FOR_SENDING_MESSAGE;
import static org.hisp.dhis.setting.SystemSettingManager.KEY_SCHEDULE_MESSAGE_TASKS;
import static org.hisp.dhis.setting.SystemSettingManager.KEY_SEND_MESSAGE_SCHEDULED_TASKS;
import static org.hisp.dhis.setting.SystemSettingManager.KEY_TIME_FOR_SENDING_MESSAGE;

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.patient.scheduling.ProgramSchedulingManager;
import org.hisp.dhis.setting.SystemSettingManager;
import org.hisp.dhis.system.scheduling.Scheduler;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * 
 * @version ScheduleSendMessageTasksAction.java 12:10:35 PM Sep 10, 2012 $
 */
public class ScheduleSendMessageTasksAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SystemSettingManager systemSettingManager;

    public void setSystemSettingManager( SystemSettingManager systemSettingManager )
    {
        this.systemSettingManager = systemSettingManager;
    }

    private ProgramSchedulingManager schedulingManager;

    public void setSchedulingManager( ProgramSchedulingManager schedulingManager )
    {
        this.schedulingManager = schedulingManager;
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
    
    private String timeSendingMessage;

    public void setTimeSendingMessage( String timeSendingMessage )
    {
        this.timeSendingMessage = timeSendingMessage;
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

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        systemSettingManager.saveSystemSetting( KEY_TIME_FOR_SENDING_MESSAGE, timeSendingMessage );
        
        if ( execute )
        {
            schedulingManager.executeTasks();
        }
        else if ( schedule )
        {
            if ( Scheduler.STATUS_RUNNING.equals( schedulingManager.getTaskStatus() ) )
            {
                schedulingManager.stopTasks();
            }
            else
            {
                Map<String, String> keyCronMap = new HashMap<String, String>();
                String time = (String) systemSettingManager.getSystemSetting( KEY_TIME_FOR_SENDING_MESSAGE,
                    DEFAULT_TIME_FOR_SENDING_MESSAGE );

                // Schedule for sending messages
                String[] infor = time.split( ":" );
                String hour = infor[0].trim();
                String minute = infor[1].trim();

                if ( hour.trim().equals( "00" ) )
                {
                    hour = "0";
                }
                if ( minute.trim().equals( "00" ) )
                {
                    minute = "0";
                }
                String cron = "0 " + Integer.parseInt( minute ) + " " + Integer.parseInt( hour ) + " ? * *";

                keyCronMap.put( KEY_SEND_MESSAGE_SCHEDULED_TASKS, cron );
                keyCronMap.put( KEY_SCHEDULE_MESSAGE_TASKS, "0 0 0 * * ?" );

                schedulingManager.scheduleTasks( keyCronMap );
            }
        }

        status = schedulingManager.getTaskStatus();
        running = Scheduler.STATUS_RUNNING.equals( status );

        return SUCCESS;
    }
}
