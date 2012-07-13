package org.hisp.dhis.sms.outbound;

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

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.hisp.dhis.sms.AbstractSmsTest;
import org.hisp.dhis.sms.SmsServiceException;
import org.hisp.dhis.sms.config.SmsConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;

public class OutboundSmsServiceTest
    extends AbstractSmsTest
{
    // These are only used for the integration test with store

    @Autowired
    private OutboundSmsService outboundSmsService;

    @Autowired
    private OutboundSmsStore outboundSmsStore;

    @Test
    @Ignore
    // FIXME
    public void testIntegrationEnabledNoTransport()
    {
        outboundSmsService.initialize( new SmsConfiguration( true ) );

        OutboundSms outboundSms = getOutboundSms();

        outboundSmsService.sendMessage( outboundSms, gatewayId );

        List<OutboundSms> smses = outboundSmsStore.getAll();
        assertNotNullSize( smses, 1 );

        verifySms( outboundSms, smses.iterator().next() );
    }

    // Unit testing

    @Test
    public void testNotEnabled()
    {
        OutboundSmsService tmpService = new OutboundSmsServiceImpl();
        try
        {
            tmpService.sendMessage( getOutboundSms(), gatewayId );
            fail( "Should fail since service is not enabled" );
        }
        catch ( SmsServiceException e )
        {
        }
    }

    @Test
    public void testWithTransport()
    {
        OutboundSmsServiceImpl tmpService = new OutboundSmsServiceImpl();
        tmpService.setOutboundSmsStore( mock( OutboundSmsStore.class ) );
        OutboundSmsTransportService transportService = mock( OutboundSmsTransportService.class );
        tmpService.setTransportService( transportService );

        OutboundSms outboundSms = getOutboundSms();

        // Service not enabled
        try
        {
            tmpService.sendMessage( outboundSms, gatewayId );
            fail( "Should fail since service is not enabled" );
        }
        catch ( SmsServiceException e )
        {
        }

        // Not sent message to transport service
        verify( transportService, never() ).sendMessage( same( outboundSms ), anyString() );

        // Enable service
        tmpService.initialize( new SmsConfiguration( true ) );

        tmpService.sendMessage( outboundSms, gatewayId );

        verify( transportService ).sendMessage( same( outboundSms ), anyString() );

        verify( transportService ).sendMessage( same( outboundSms ), anyString() );
    }

    @Test
    public void testFailingTransport()
    {
        OutboundSmsServiceImpl tmpService = new OutboundSmsServiceImpl();
        OutboundSmsStore tmpStore = mock( OutboundSmsStore.class );
        tmpService.setOutboundSmsStore( tmpStore );
        OutboundSmsTransportService transportService = mock( OutboundSmsTransportService.class );
        tmpService.setTransportService( transportService );

        tmpService.initialize( new SmsConfiguration( true ) );

        OutboundSms outboundSms = getOutboundSms();

        doThrow( new SmsServiceException( "" ) ).when( transportService )
            .sendMessage( same( outboundSms ), anyString() );

        tmpService.sendMessage( outboundSms, gatewayId );

        verify( transportService ).sendMessage( any( OutboundSms.class ), anyString() );
        ArgumentCaptor<OutboundSms> argument = ArgumentCaptor.forClass( OutboundSms.class );
        verify( tmpStore, times( 1 ) ).save( argument.capture() );

        // Is the SMS Marked with error status in store?
        // Can't test this without using hibernate or adding update on store...
        // assertEquals( OutboundSmsStatus.ERROR,
        // argument.getValue().getStatus() );
    }

}
