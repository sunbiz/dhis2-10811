package org.hisp.dhis.mobile.action;

import java.util.List;

import org.hisp.dhis.sms.outbound.OutboundSms;
import org.hisp.dhis.sms.outbound.OutboundSmsService;

import com.opensymphony.xwork2.Action;

public class ShowSentSMSAction
    implements Action
{
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private OutboundSmsService outboundSmsService;
    
    public void setOutboundSmsService( OutboundSmsService outboundSmsService )
    {
        this.outboundSmsService = outboundSmsService;
    }
    
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
    
    private List<OutboundSms> ListOutboundSMS;
    
    public List<OutboundSms> getListOutboundSMS()
    {
        return ListOutboundSMS;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------
    
    @Override
    public String execute()
        throws Exception
    {
        ListOutboundSMS = outboundSmsService.getAllOutboundSms();
        return SUCCESS;
    }

}
