package org.hisp.dhis.useraudit;

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

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.setting.SystemSettingManager;
import org.springframework.transaction.annotation.Transactional;

import static org.hisp.dhis.setting.SystemSettingManager.KEY_MAX_NUMBER_OF_ATTEMPTS;
import static org.hisp.dhis.setting.SystemSettingManager.KEY_TIMEFRAME_MINUTES;

/**
 * @author Saptarshi Purkayastha
 * @author Lars Helge Overland
 */
public class DefaultUserAuditService    
    implements UserAuditService
{
    private static final Log log = LogFactory.getLog( DefaultUserAuditService.class );

    private static final int DEFAULT_MAX_NUMBER_OF_ATTEMPTS = 5;
    private static final int DEFAULT_TIMEFRAME_MINUTES = 10;
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SystemSettingManager systemSettingManager;

    public void setSystemSettingManager( SystemSettingManager systemSettingManager )
    {
        this.systemSettingManager = systemSettingManager;
    }

    private UserAuditStore userAuditStore;

    public void setUserAuditStore( UserAuditStore userAuditStore )
    {
        this.userAuditStore = userAuditStore;
    }

    // -------------------------------------------------------------------------
    // UserAuditService implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void registerLoginSuccess( String username )
    {
        log.info( "User login success: '" + username + "'" );

        userAuditStore.resetLoginFailures( username, getDate() );
    }

    @Override
    public void registerLogout( String username )
    {
        log.info( "User logout: '" + username + "'" );
    }

    @Override
    @Transactional
    public void registerLoginFailure( String username )
    {
        log.info( "User login failure: '" + username + "'" );

        userAuditStore.saveLoginFailure( new LoginFailure( username, new Date() ) );

        int no = userAuditStore.getLoginFailures( username, getDate() );

        int max = (Integer) systemSettingManager.getSystemSetting( KEY_MAX_NUMBER_OF_ATTEMPTS, DEFAULT_MAX_NUMBER_OF_ATTEMPTS );
        
        if ( no >= max )
        {
            log.info( "Max number of login attempts exceeded: '" + username + "'" );
        }
    }

    @Override
    @Transactional
    public int getLoginFailures( String username )
    {
        return userAuditStore.getLoginFailures( username, getDate() );
    }

    @Override
    public int getMaxAttempts()
    {
        return (Integer) systemSettingManager.getSystemSetting( KEY_MAX_NUMBER_OF_ATTEMPTS, DEFAULT_MAX_NUMBER_OF_ATTEMPTS );
    }

    @Override
    public int getLockoutTimeframe()
    {
        return (Integer) systemSettingManager.getSystemSetting( KEY_TIMEFRAME_MINUTES, DEFAULT_TIMEFRAME_MINUTES );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Date getDate()
    {
        int timeframe = (Integer) systemSettingManager.getSystemSetting( KEY_TIMEFRAME_MINUTES, DEFAULT_TIMEFRAME_MINUTES );

        Calendar cal = Calendar.getInstance();
        cal.add( Calendar.MINUTE, timeframe * -1 );
        
        return cal.getTime();
    }
}
