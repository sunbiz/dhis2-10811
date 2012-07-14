package org.hisp.dhis.mobile.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.SchedulingProgramObject;
import org.hisp.dhis.sms.outbound.OutboundSms;
import org.hisp.dhis.sms.outbound.OutboundSmsService;
import org.hisp.dhis.sms.outbound.OutboundSmsStatus;

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
    
    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }
    
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
    
    private List<OutboundSms> listOutboundSMS;
    
    public List<OutboundSms> getListOutboundSMS()
    {
        return listOutboundSMS;
    }
    
    private Integer filterStatusType;
    
    public Integer getFilterStatusType()
    {
        return filterStatusType;
    }

    public void setFilterStatusType( Integer filterStatusType )
    {
        this.filterStatusType = filterStatusType;
    }

    private Collection<SchedulingProgramObject> schedulingProgramObjects;
    
    public Collection<SchedulingProgramObject> getSchedulingProgramObjects()
    {
        return schedulingProgramObjects;
    }
    
    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        List<OutboundSms> tempListOutboundSMS = outboundSmsService.getAllOutboundSms();
        
        listOutboundSMS = new ArrayList<OutboundSms>();
        
        if ( filterStatusType != null && filterStatusType == 0)
        {
            for ( OutboundSms each: tempListOutboundSMS )
            {
                if (each.getStatus().equals( OutboundSmsStatus.OUTBOUND ))
                {
                    this.listOutboundSMS.add( each );
                }
            }
        }
        if ( filterStatusType != null && filterStatusType == 1 )
        {
            for ( OutboundSms each: tempListOutboundSMS )
            {
                if (each.getStatus().equals( OutboundSmsStatus.SENT ))
                {
                    this.listOutboundSMS.add( each );
                }
            }
        }
        if ( filterStatusType != null && filterStatusType == 2 || filterStatusType == null)
        {
            for ( OutboundSms each: tempListOutboundSMS )
            {
                this.listOutboundSMS.add( each );
            }
        }
        schedulingProgramObjects = programStageInstanceService.getSendMesssageEvents();
        return SUCCESS;
    }

}
