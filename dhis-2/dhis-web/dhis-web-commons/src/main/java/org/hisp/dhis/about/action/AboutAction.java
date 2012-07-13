package org.hisp.dhis.about.action;

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

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.external.location.LocationManager;
import org.hisp.dhis.external.location.LocationManagerException;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.system.database.DatabaseInfo;
import org.hisp.dhis.system.database.DatabaseInfoProvider;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.util.ContextUtils;

import com.opensymphony.xwork2.Action;

/**
 * @author Dang Duy Hieu
 */
public class AboutAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private LocationManager locationManager;

    public void setLocationManager( LocationManager locationManager )
    {
        this.locationManager = locationManager;
    }

    private DatabaseInfoProvider databaseInfoProvider;

    public void setDatabaseInfoProvider( DatabaseInfoProvider databaseInfoProvider )
    {
        this.databaseInfoProvider = databaseInfoProvider;
    }

    private CurrentUserService currentUserService;
    
    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }
    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String version;

    public String getVersion()
    {
        return version;
    }

    private String revision;

    public String getRevision()
    {
        return revision;
    }

    private Date buildTime;

    public Date getBuildTime()
    {
        return buildTime;
    }
    
    private String userAgent;

    public String getUserAgent()
    {
        return userAgent;
    }

    private String environmentVariable;

    public String getEnvironmentVariable()
    {
        return environmentVariable;
    }

    private String externalDirectory;

    public String getExternalDirectory()
    {
        return externalDirectory;
    }

    private DatabaseInfo info;
    
    public DatabaseInfo getInfo()
    {
        return info;
    }
    
    private String javaOpts;

    public String getJavaOpts()
    {
        return javaOpts;
    }
    
    private String backUrl;

    public String getBackUrl()
    {
        return backUrl;
    }

    public void setBackUrl( String backUrl )
    {
        this.backUrl = backUrl;
    }
    
    private Properties systemProperties;
    
    public Properties getSystemProperties()
    {
        return systemProperties;
    }

    private boolean currentUserIsSuper;

    public boolean getCurrentUserIsSuper()
    {
        return currentUserIsSuper;
    }
    
    private Date serverDate = new Date();

    public Date getServerDate()
    {
        return serverDate;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Version
        // ---------------------------------------------------------------------

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        InputStream in = classLoader.getResourceAsStream( "build.properties" );

        if ( in != null )
        {
            Properties properties = new Properties();
    
            properties.load( in );
    
            version = properties.getProperty( "build.version" );
    
            revision = properties.getProperty( "build.revision" );
    
            String buildTime = properties.getProperty( "build.time" );

            DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

            this.buildTime = dateFormat.parse( buildTime );
        }
        
        HttpServletRequest request = ServletActionContext.getRequest();

        // ---------------------------------------------------------------------
        // User agent
        // ---------------------------------------------------------------------

        userAgent = request.getHeader( ContextUtils.HEADER_USER_AGENT );

        // ---------------------------------------------------------------------
        // External directory
        // ---------------------------------------------------------------------

        environmentVariable = locationManager.getEnvironmentVariable();
        
        try
        {
            File directory = locationManager.getExternalDirectory();
        
            externalDirectory = directory.getAbsolutePath();
        }
        catch ( LocationManagerException ex )
        {
            externalDirectory = i18n.getString( "not_set" );
        }
        
        // ---------------------------------------------------------------------
        // Database
        // ---------------------------------------------------------------------

        info = databaseInfoProvider.getDatabaseInfo();
        
        try
        {
            javaOpts = System.getenv( "JAVA_OPTS" );
        }
        catch ( SecurityException ex )
        {
            javaOpts = i18n.getString( "unknown" );
        }
        
        systemProperties = System.getProperties();
        
        currentUserIsSuper = currentUserService.currentUserIsSuper();

        return SUCCESS;
    }
}
