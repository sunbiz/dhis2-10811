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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.sms.SmsServiceException;
import org.hisp.dhis.sms.SmsServiceNotEnabledException;
import org.hisp.dhis.sms.config.SmsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Simple {@link OutboundSmsService sms service} storing the sms in a store and
 * forwards the request to a {@link OutboundSmsTransportService sms transport
 * service} for sending.
 */
public class OutboundSmsServiceImpl
    implements OutboundSmsService
{
    private static final Log log = LogFactory.getLog( OutboundSmsServiceImpl.class );

    private boolean enabled;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OutboundSmsStore outboundSmsStore;

    public void setOutboundSmsStore( OutboundSmsStore outboundSmsStore )
    {
        this.outboundSmsStore = outboundSmsStore;
    }

    private OutboundSmsTransportService transportService;

    @Autowired( required = false )
    protected void setTransportService( OutboundSmsTransportService transportService )
    {
        this.transportService = transportService;
        log.debug( "Got OutboundSmsTransportService: " + transportService.getClass().getSimpleName() );
    }

    // -------------------------------------------------------------------------
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public String initialize( SmsConfiguration smsConfiguration )
        throws SmsServiceException
    {
        if ( smsConfiguration != null )
        {
            enabled = smsConfiguration.isEnabled();
        }

        return "success";
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    @Transactional
    public String sendMessage( OutboundSms sms, String gatewayId )
        throws SmsServiceException
    {
        if ( !enabled )
        {
            throw new SmsServiceNotEnabledException();
        }
        
        if ( transportService != null )
        {
            return sendMessageInternal( sms, gatewayId );
        }

        return "outboundsms_saved";
    }

    @Override
    public List<OutboundSms> getAllOutboundSms()
    {
        return outboundSmsStore.getAll();
    }

    @Override
    public List<OutboundSms> getOutboundSms( OutboundSmsStatus status )
    {
        return outboundSmsStore.get( status );
    }

    @Override
    public void updateOutboundSms( OutboundSms sms)
    {
        outboundSmsStore.update( sms );
    }
    
    @Override
    public int saveOutboundSms(OutboundSms sms) {
        return outboundSmsStore.save( sms );
    }

    @Override
    public void deleteById( Integer outboundSmsId )
    {
        OutboundSms sms = outboundSmsStore.get( outboundSmsId );
        outboundSmsStore.delete( sms );
    }
    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    private String sendMessageInternal( OutboundSms sms, String id )
    {
        try
        {
            return transportService.sendMessage( sms, id );
        }
        catch ( SmsServiceException e )
        {
            log.debug( "Exception sending message " + sms, e );
            sms.setStatus( OutboundSmsStatus.ERROR );
            this.saveOutboundSms( sms );
            return "Exception sending message " + sms + e.getMessage();
        }
    }


}
