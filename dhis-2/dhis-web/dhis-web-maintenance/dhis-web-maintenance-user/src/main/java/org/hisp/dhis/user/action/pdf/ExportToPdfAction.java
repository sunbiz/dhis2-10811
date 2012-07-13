package org.hisp.dhis.user.action.pdf;

/*
 * Copyright (c) 2004-2011, University of Oslo
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

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.common.ServiceProvider;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.ExportService;
import org.hisp.dhis.system.filter.UserCredentialsCanUpdateFilter;
import org.hisp.dhis.system.util.FilterUtils;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.UserCredentials;
import org.hisp.dhis.user.UserService;

import com.opensymphony.xwork2.Action;

/**
 * @author Dang Duy Hieu
 * @version $Id$
 */
public class ExportToPdfAction
    implements Action
{
    private static final Log log = LogFactory.getLog( ExportToPdfAction.class );

    private static final String EXPORT_FORMAT_PDF = "PDF";

    private static final String TYPE_USER = "alluser";

    private static final String FILENAME_USER = "Users.zip";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserService userService;

    public void setUserService( UserService userService )
    {
        this.userService = userService;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private ServiceProvider<ExportService> serviceProvider;

    public void setServiceProvider( ServiceProvider<ExportService> serviceProvider )
    {
        this.serviceProvider = serviceProvider;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private InputStream inputStream;

    public InputStream getInputStream()
    {
        return inputStream;
    }

    private String fileName;

    public String getFileName()
    {
        return fileName;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String curKey;

    public void setCurKey( String curKey )
    {
        this.curKey = curKey;
    }

    private Integer months;

    public void setMonths( Integer months )
    {
        this.months = months;
    }

    private String type;

    public void setType( String type )
    {
        this.type = type;
    }

    private List<UserCredentials> userCredentialsList = null;

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        if ( type != null )
        {
            ExportParams params = new ExportParams();

            if ( type.equals( TYPE_USER ) )
            {
                if ( isNotBlank( curKey ) ) // Filter on key only if set
                {
                    userCredentialsList = new ArrayList<UserCredentials>( userService.searchUsersByName( curKey ) );
                }
                else if ( months != null && months != 0 )
                {
                    userCredentialsList = new ArrayList<UserCredentials>( userService.getInactiveUsers( months ) );
                }
                else
                {
                    userCredentialsList = new ArrayList<UserCredentials>( userService.getAllUserCredentials() );
                }

                FilterUtils.filter( userCredentialsList, new UserCredentialsCanUpdateFilter( currentUserService
                    .getCurrentUser() ) );

                if ( (userCredentialsList != null) && !userCredentialsList.isEmpty() )
                {
                    params.setUserObjects( userCredentialsList );
                }
                else
                {
                    params.setUserObjects( null );
                }

                fileName = FILENAME_USER;

                log.info( "Exporting to PDF for object type: " + TYPE_USER );
            }

            params.setIncludeDataValues( false );
            params.setI18n( i18n );
            params.setFormat( format );

            ExportService exportService = serviceProvider.provide( EXPORT_FORMAT_PDF );

            inputStream = exportService.exportData( params );
        }

        return SUCCESS;
    }

}
