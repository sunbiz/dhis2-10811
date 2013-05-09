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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.sms.SmsServiceException;
import org.hisp.dhis.sms.SmsServiceNotEnabledException;
import org.hisp.dhis.sms.config.SmsConfiguration;
import org.hisp.dhis.sms.outbound.OutboundSms;
import org.hisp.dhis.sms.outbound.OutboundSmsService;
import org.hisp.dhis.sms.outbound.OutboundSmsTransportService;

/**
 * Simple {@link OutboundSmsService} just logging invocations, only to be used
 * for test purposes
 * 
 * <p>
 * Has the property enabled, defaulting to true, which is configured using
 * {@link TestOutboundSmsService#initialize(SmsConfiguration)}
 */
public class TestOutboundSmsService
    implements OutboundSmsTransportService
{
    private static final Log log = LogFactory.getLog( TestOutboundSmsService.class );

    private boolean enabled = true;

    private String message = "success";

    @Override
    public String sendMessage( OutboundSms sms, String gatewayId )
        throws SmsServiceException
    {
        if ( !enabled )
        {
            throw new SmsServiceNotEnabledException();
        }

        log.debug( "Send message: " + sms );

        return message;
    }

    @Override
    public String initialize( SmsConfiguration config )
        throws SmsServiceException
    {
        this.enabled = config.isEnabled();
        log.debug( "initialize()" );
        return message;
    }

    @Override
    public boolean isEnabled()
    {
        return this.enabled;
    }

    @Override
    public Map<String, String> getGatewayMap()
    {
        return new HashMap<String, String>();
    }

    @Override
    public void stopService()
    {
        log.debug( "stopService()" );
    }

    @Override
    public void startService()
    {
        log.debug( "startService()" );
    }

    @Override
    public String getServiceStatus()
    {
        log.debug( "getServiceStatus()" );
        return "STARTED";
    }

    @Override
    public void reloadConfig()
        throws SmsServiceException
    {
        log.debug( "reloadConfig()" );
    }

    @Override
    public String getMessageStatus()
    {
        log.debug( "getMessageStatus()" );
        return null;
    }

    @Override
    public List<OutboundSms> getAllOutboundSms()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int saveOutboundSms( OutboundSms sms )
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void updateOutboundSms( OutboundSms sms )
    {
        // TODO Auto-generated method stub
    }

    @Override
    public List<OutboundSms> getOutboundSms( OutboundSmsStatus status )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteById( Integer outboundSmsId )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDefaultGateway()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
