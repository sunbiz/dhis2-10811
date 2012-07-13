package org.hisp.dhis.message;

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

import static org.hisp.dhis.setting.SystemSettingManager.KEY_EMAIL_HOST_NAME;
import static org.hisp.dhis.setting.SystemSettingManager.KEY_EMAIL_PASSWORD;
import static org.hisp.dhis.setting.SystemSettingManager.KEY_EMAIL_USERNAME;
import static org.hisp.dhis.user.UserSettingService.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.hisp.dhis.setting.SystemSettingManager;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;

/**
 * @author Lars Helge Overland
 */
public class EmailMessageSender
    implements MessageSender
{
    private static final Log log = LogFactory.getLog( EmailMessageSender.class );
    
    private static final int SMTP_PORT = 587;
    private static final String FROM_ADDRESS = "noreply@dhis2.org";
    private static final String FROM_NAME = "DHIS2 Message [No reply]";
    private static final String SUBJECT_PREFIX = "[DHIS2] ";
    private static final String LB = System.getProperty( "line.separator" );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SystemSettingManager systemSettingManager;
    
    public void setSystemSettingManager( SystemSettingManager systemSettingManager )
    {
        this.systemSettingManager = systemSettingManager;
    }
    
    private UserService userService;

    public void setUserService( UserService userService )
    {
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // MessageSender implementation
    // -------------------------------------------------------------------------

    @Override
    public void sendMessage( String subject, String text, User sender, Set<User> users )
    {        
        String hostName = StringUtils.trimToNull( (String) systemSettingManager.getSystemSetting( KEY_EMAIL_HOST_NAME ) );
        String username = StringUtils.trimToNull( (String) systemSettingManager.getSystemSetting( KEY_EMAIL_USERNAME ) );
        String password = StringUtils.trimToNull( (String) systemSettingManager.getSystemSetting( KEY_EMAIL_PASSWORD ) );

        if ( hostName == null || username == null || password == null )
        {
            return;
        }

        text = sender == null ? text : ( text + LB + LB + 
            sender.getName() + LB + 
            sender.getOrganisationUnitsName() + LB +
            ( sender.getEmail() != null ? ( sender.getEmail() + LB ) : StringUtils.EMPTY ) +
            ( sender.getPhoneNumber() != null ? ( sender.getPhoneNumber() + LB ) : StringUtils.EMPTY ) );
        
        Map<User,Serializable> settings = userService.getUserSettings( KEY_MESSAGE_EMAIL_NOTIFICATION, false );

        try
        {
            Email email = getEmail( hostName, username, password );
            email.setSubject( SUBJECT_PREFIX + subject );
            email.setMsg( text );
            
            boolean hasRecipients = false;
            
            for ( User user : users )
            {
                boolean emailNotification = settings.get( user ) != null && (Boolean) settings.get( user ) == true;
    
                if ( emailNotification && user.getEmail() != null && !user.getEmail().trim().isEmpty() )
                {
                    email.addBcc( user.getEmail() );
                    
                    log.debug( "Sent email to user: " + user + " with email address: " + user.getEmail() );
                    
                    hasRecipients = true;
                }
            }

            if ( hasRecipients )
            {
                email.send();
            }
        }
        catch ( EmailException ex )
        {
            log.warn( "Could not send email: " + ex.getMessage() );
        }
    }

    private Email getEmail( String hostName, String username, String password )
        throws EmailException
    {
        Email email = new SimpleEmail();
        email.setHostName( hostName );
        email.setSmtpPort( SMTP_PORT );
        email.setAuthenticator( new DefaultAuthenticator( username, password ) );
        email.setTLS( true );
        email.setFrom( FROM_ADDRESS, FROM_NAME );
        
        return email;
    }
}
