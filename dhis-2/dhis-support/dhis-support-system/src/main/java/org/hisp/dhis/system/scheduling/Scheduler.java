package org.hisp.dhis.system.scheduling;

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

/**
 * @author Lars Helge Overland
 */
public interface Scheduler
{
    final String CRON_DAILY_0AM = "0 0 0 * * ?";
    final String CRON_DAILY_1AM = "0 0 1 * * ?";
    final String CRON_DAILY_0AM_EXCEPT_SUNDAY = "0 0 0 ? * 1-6";
    final String CRON_WEEKLY_SUNDAY_0AM = "0 0 0 ? * 0";
    final String CRON_TEST = "0 * * * * ?";
    
    final String STATUS_RUNNING = "running";
    final String STATUS_DONE = "done";
    final String STATUS_STOPPED  = "stopped";
    final String STATUS_NOT_STARTED = "not_started";
    
    void executeTask( Runnable task );
    
    boolean scheduleTask( String key, Runnable task, String cronExpr );
    
    boolean stopTask( String key );
    
    void stopAllTasks();
    
    String getTaskStatus( String key );
}
