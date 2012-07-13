package org.hisp.dhis.sms.outbound;

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

import static org.hisp.dhis.user.UserSettingService.KEY_MESSAGE_SMS_NOTIFICATION;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.sms.MessageSender;
import org.hisp.dhis.sms.SmsServiceException;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;

public class SmsMessageSender
    implements MessageSender
{
    private static final Log log = LogFactory.getLog( SmsMessageSender.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private UserService userService;

    public void setUserService( UserService userService )
    {
        this.userService = userService;
    }

    private OutboundSmsService outboundSmsService;

    public void setOutboundSmsService( OutboundSmsService outboundSmsService )
    {
        this.outboundSmsService = outboundSmsService;
    }

    // -------------------------------------------------------------------------
    // MessageSender implementation
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public String sendMessage( String subject, String text, User sender, boolean isPhone, Set<?> recipients,
        String gatewayId )
    {
        String message = null;

        if ( outboundSmsService == null || !outboundSmsService.isEnabled() )
        {
            return "outboundsmsservice_is_null_or_unable";
        }

        text = createMessage( subject, text, sender );

        Set<String> phones = null;

        if ( isPhone )
        {
            phones = (Set<String>) recipients;
        }
        else
        {
            phones = getRecipients( (Set<User>) recipients );
        }

        if ( !phones.isEmpty() && phones.size() > 0 )
        {
            message = sendMessage( text, phones, gatewayId );
        }
        else if ( log.isDebugEnabled() )
        {
            log.debug( "Not sending message to any of the recipients" );
        }
        else
        {
            message = "no_recipient";
        }

        return message;
    }

    private Set<String> getRecipients( Set<User> users )
    {
        Set<String> recipients = new HashSet<String>();

        Map<User, Serializable> settings = userService.getUserSettings( KEY_MESSAGE_SMS_NOTIFICATION, false );

        for ( User user : users )
        {
            boolean smsNotification = settings.get( user ) != null && (Boolean) settings.get( user );

            String phoneNumber = user.getPhoneNumber();

            if ( smsNotification && phoneNumber != null && !phoneNumber.trim().isEmpty() )
            {
                recipients.add( phoneNumber );

                if ( log.isDebugEnabled() )
                {
                    log.debug( "Adding user as sms recipient: " + user + " with phone number: " + phoneNumber );
                }
            }
        }

        return recipients;
    }

    private String createMessage( String subject, String text, User sender )
    {
        String name = "unknown";

        if ( sender != null )
        {
            name = sender.getUsername();
        }

        if ( subject == null || subject.isEmpty() )
        {
            subject = "";
        }
        else
        {
            subject = " - " + subject;
        }

        text = "From " + name + subject + ": " + text;

        // Simplistic cutoff 160 characters..
        int length = text.length();

        return (length > 160) ? text.substring( 0, 157 ) + "..." : text;
    }

    private String sendMessage( String text, Set<String> recipients, String id )
    {
        String message = null;
        OutboundSms sms = new OutboundSms();
        sms.setMessage( text );
        sms.setRecipients( recipients );

        try
        {
            message = outboundSmsService.sendMessage( sms, id );

            if ( log.isDebugEnabled() )
            {
                log.debug( "Sent message to " + recipients + ": " + text );
            }
        }
        catch ( SmsServiceException e )
        {
            message = "Unable to send message through sms: " + sms + e.getCause().getMessage();

            log.warn( "Unable to send message through sms: " + sms, e );
        }

        return message;
    }

}
