/*
 * Copyright (c) 2004-2009, University of Oslo
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

package org.hisp.dhis.caseentry.action.reminder;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.sms.SmsServiceException;
import org.hisp.dhis.sms.outbound.OutboundSms;
import org.hisp.dhis.sms.outbound.OutboundSmsService;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * 
 * @version SendSmsAction.java 11:17:37 AM Aug 9, 2012 $
 */
public class SendSmsAction
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

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private Integer programStageInstanceId;

    public void setProgramStageInstanceId( Integer programStageInstanceId )
    {
        this.programStageInstanceId = programStageInstanceId;
    }

    private String msg;

    public void setMsg( String msg )
    {
        this.msg = msg;
    }

    private String message = "";

    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        ProgramStageInstance programStageInstance = programStageInstanceService
            .getProgramStageInstance( programStageInstanceId );

        String phoneNumber = programStageInstance.getProgramInstance().getPatient().getPhoneNumber();

        if ( phoneNumber != null && !phoneNumber.isEmpty() )
        {
            try
            {
                OutboundSms outboundSms = new OutboundSms( msg, phoneNumber );
                outboundSms.setSender( currentUserService.getCurrentUsername() );
                outboundSmsService.sendMessage( outboundSms, null );

                List<OutboundSms> outboundSmsList = programStageInstance.getOutboundSms();
                if ( outboundSmsList == null )
                {
                    outboundSmsList = new ArrayList<OutboundSms>();
                }
                outboundSmsList.add( outboundSms );
                programStageInstance.setOutboundSms( outboundSmsList );
                programStageInstanceService.updateProgramStageInstance( programStageInstance );

                message = i18n.getString( "sent_message_to" ) + " " + phoneNumber;

                return SUCCESS;
            }
            catch ( SmsServiceException e )
            {
                message = e.getMessage();

                return ERROR;
            }
        }

        message = i18n.getString( "patient_did_not_register_a_phone_number" );
        
        return INPUT;
    }

}
