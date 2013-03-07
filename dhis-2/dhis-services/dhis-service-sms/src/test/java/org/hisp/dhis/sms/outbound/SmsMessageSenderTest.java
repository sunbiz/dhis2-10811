package org.hisp.dhis.sms.outbound;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.hisp.dhis.user.User;
import org.junit.Test;

@SuppressWarnings( "serial" )
public class SmsMessageSenderTest
{
    @Test
    public void testMessageSender()
    {
        final User user = getUser();

        SmsMessageSender smsMessageSender = new SmsMessageSender();

        OutboundSmsService outboundSmsService = mock( OutboundSmsService.class );
        when( outboundSmsService.isEnabled() ).thenReturn( true );

        smsMessageSender.setOutboundSmsService( outboundSmsService );
        smsMessageSender.sendMessage( "Hello", "hello", user, false, getUserSet( user ), null );

        verify( outboundSmsService ).isEnabled();
        //verify( userService ).getUserSettings( KEY_MESSAGE_SMS_NOTIFICATION, false );
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
