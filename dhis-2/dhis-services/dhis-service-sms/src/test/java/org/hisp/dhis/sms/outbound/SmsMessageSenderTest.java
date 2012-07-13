package org.hisp.dhis.sms.outbound;

import static org.hisp.dhis.user.UserSettingService.KEY_MESSAGE_SMS_NOTIFICATION;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.hisp.dhis.sms.outbound.OutboundSms;
import org.hisp.dhis.sms.outbound.OutboundSmsService;
import org.hisp.dhis.sms.outbound.SmsMessageSender;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.junit.Test;

@SuppressWarnings( "serial" )
public class SmsMessageSenderTest
{
    @Test
    public void testMessageSender()
    {
        final User user = getUser();
        Map<User, Serializable> settings = getUserSettings( user );

        SmsMessageSender smsMessageSender = new SmsMessageSender();

        OutboundSmsService outboundSmsService = mock( OutboundSmsService.class );
        when( outboundSmsService.isEnabled() ).thenReturn( true );

        UserService userService = mock( UserService.class );
        when( userService.getUserSettings( KEY_MESSAGE_SMS_NOTIFICATION, false ) ).thenReturn( settings );

        smsMessageSender.setOutboundSmsService( outboundSmsService );
        smsMessageSender.setUserService( userService );
        smsMessageSender.sendMessage( "Hello", "hello", user, false, getUserSet( user ), null );

        verify( outboundSmsService ).isEnabled();
        verify( userService ).getUserSettings( KEY_MESSAGE_SMS_NOTIFICATION, false );
        verify( outboundSmsService ).sendMessage( refEq(getSms()), anyString() );
    }

    private OutboundSms getSms()
    {
        OutboundSms sms = new OutboundSms();
        sms.setMessage( "From null - Hello: hello" );
        sms.setRecipients( new HashSet<String>() {{ add("222222");}} );
        return sms;
    }

    private HashSet<User> getUserSet( final User user )
    {
        return new HashSet<User>() {{ add( user ); }};
    }

    private Map<User, Serializable> getUserSettings( final User user )
    {
        return new HashMap<User, Serializable>() {{ put( user, true ); }};
    }

    private User getUser()
    {
        final User user = new User();
        user.setId( 1 );
        user.setPhoneNumber( "222222" );
        user.setFirstName( "firstName" );
        user.setSurname( "surname" );
        return user;
    }
}
