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
package org.hisp.dhis.sms.outbound;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.message.MessageSender;
import org.hisp.dhis.sms.SmsServiceException;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.hisp.dhis.user.UserSetting;
import org.hisp.dhis.user.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Nguyen Kim Lai
 * 
 * @version SmsSender.java 10:29:11 AM Apr 16, 2013 $
 */
public class SmsSender
    implements MessageSender
{
    private static final Log log = LogFactory.getLog( SmsMessageSender.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }
    
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
    
    @Autowired
    private OutboundSmsTransportService transportService;
    
    @Override
    public void sendMessage( String subject, String text, User sender, Set<User> users, boolean forceSend )
    {
        Set<User> toSendUserList = new HashSet<User>();

        String gatewayId = transportService.getDefaultGateway();

        if ( gatewayId != null || gatewayId.trim().length() != 0 )
        {
            boolean sendSMSNotification = false;
            for ( User user : users )
            {
                if ( currentUserService.getCurrentUser() != user )
                {
                    UserSetting userSetting = userService.getUserSetting( user, UserSettingService.KEY_MESSAGE_SMS_NOTIFICATION );
                    if ( userSetting != null )
                    {
                        sendSMSNotification = (Boolean) userSetting.getValue();
                        if ( sendSMSNotification == true )
                        {
                            toSendUserList.add( user );
                            sendSMSNotification = false;
                        }
                    }
                }
            }
            
            Set<String> phoneNumbers = null;

            if ( outboundSmsService != null || outboundSmsService.isEnabled() )
            {
                text = createMessage( subject, text, sender );
                
                phoneNumbers = getRecipientsPhoneNumber( users );
                
                if ( !phoneNumbers.isEmpty() && phoneNumbers.size() > 0 )
                {
                    sendMessage( text, phoneNumbers, gatewayId );
                }

            }
        }
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
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
    
    private Set<String> getRecipientsPhoneNumber( Set<User> users )
    {
        Set<String> recipients = new HashSet<String>();

        for ( User user : users )
        {
            String phoneNumber = user.getPhoneNumber();

            if ( phoneNumber != null && !phoneNumber.trim().isEmpty() )
            {
                recipients.add( phoneNumber );
            }
        }

        return recipients;
    }
    
    private void sendMessage( String text, Set<String> recipients, String gateWayId )
    {
        OutboundSms sms = new OutboundSms();
        sms.setMessage( text );
        sms.setRecipients( recipients );

        try
        {
            outboundSmsService.sendMessage( sms, gateWayId );
        }
        catch ( SmsServiceException e )
        {
            log.warn( "Unable to send message through sms: " + sms, e );
        }

    }

}
