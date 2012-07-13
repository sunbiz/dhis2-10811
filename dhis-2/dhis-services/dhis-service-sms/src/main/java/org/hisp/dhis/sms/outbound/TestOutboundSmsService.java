package org.hisp.dhis.sms.outbound;

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
}
