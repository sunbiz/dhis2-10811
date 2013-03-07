package org.hisp.dhis.mobile.action;

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
