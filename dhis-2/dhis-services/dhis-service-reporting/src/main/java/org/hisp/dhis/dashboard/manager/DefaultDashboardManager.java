package org.hisp.dhis.dashboard.manager;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.dashboard.DashboardConfiguration;
import org.hisp.dhis.dashboard.DashboardManager;
import org.hisp.dhis.dashboard.provider.ContentProvider;
import org.hisp.dhis.user.UserSettingService;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DefaultDashboardManager
    implements DashboardManager
{
    private static final String KEY_USERSETTING = "dashboardConfig";

    private Map<String, ContentProvider> contentProviders;
    
    public void setContentProviders( Map<String, ContentProvider> contentProviders )
    {
        this.contentProviders = contentProviders;
    }

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserSettingService userSettingService;

    public void setUserSettingService( UserSettingService userSettingService )
    {
        this.userSettingService = userSettingService;
    }
    
    // -------------------------------------------------------------------------
    // DashboardManager implementation
    // -------------------------------------------------------------------------

    public void setAreaItem( String area, String item )
    {
        DashboardConfiguration config = getConfiguration();
            
        config.setAreaItem( area, item );
        
        setConfiguration( config );
    }
    
    public void clearArea( String area )
    {
        DashboardConfiguration config = getConfiguration();
        
        config.clearArea( area );
        
        setConfiguration( config );
    }
    
    public Map<String, Object> getContent()
    {
        Map<String, Object> content = new HashMap<String, Object>();
        
        DashboardConfiguration config = getConfiguration();
        
        Collection<String> items = config.getAreaItems().values();
        
        for ( String key : contentProviders.keySet() )
        {
            if ( items.contains( key ) )
            {
                content.putAll( contentProviders.get( key ).provide() );
            }
        }
        
        content.putAll( config.getAreaItems() );
        
        return content;
    }
    
    public Set<String> getContentProviderNames()
    {
        return contentProviders.keySet();
    }

    public DashboardConfiguration getConfiguration()
    {
        DashboardConfiguration config = (DashboardConfiguration) userSettingService.getUserSetting( KEY_USERSETTING );
        
        return config != null ? config : new DashboardConfiguration();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void setConfiguration( DashboardConfiguration config )
    {
        userSettingService.saveUserSetting( KEY_USERSETTING, config );
    }
}
