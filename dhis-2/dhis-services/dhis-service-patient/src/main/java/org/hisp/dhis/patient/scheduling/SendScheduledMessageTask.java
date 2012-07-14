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

package org.hisp.dhis.patient.scheduling;

import static org.hisp.dhis.sms.outbound.OutboundSms.DHIS_SYSTEM_SENDER;

import java.util.Collection;
import java.util.List;

import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.SchedulingProgramObject;
import org.hisp.dhis.sms.SmsServiceException;
import org.hisp.dhis.sms.outbound.OutboundSms;
import org.hisp.dhis.sms.outbound.OutboundSmsService;
import org.hisp.dhis.sms.outbound.OutboundSmsStatus;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Chau Thu Tran
 * 
 * @version SendScheduledMessageTask.java 12:57:53 PM Sep 10, 2012 $
 */
public class SendScheduledMessageTask
    implements Runnable
{
    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    private OutboundSmsService outboundSmsService;

    public void setOutboundSmsService( OutboundSmsService outboundSmsService )
    {
        this.outboundSmsService = outboundSmsService;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public SendScheduledMessageTask( ProgramStageInstanceService programStageInstanceService,
        JdbcTemplate jdbcTemplate, OutboundSmsService outboundSmsService )
    {
        this.programStageInstanceService = programStageInstanceService;
        this.jdbcTemplate = jdbcTemplate;
        this.outboundSmsService = outboundSmsService;
    }

    // -------------------------------------------------------------------------
    // Params
    // -------------------------------------------------------------------------

    private Boolean sendingMessage;

    public void setSendingMessage( Boolean sendingMessage )
    {
        this.sendingMessage = sendingMessage;
    }

    // -------------------------------------------------------------------------
    // Runnable implementation
    // -------------------------------------------------------------------------

    @Override
    public void run()
    {
        if ( sendingMessage )
        {
            sendMessage();
        }
        else
        {
            scheduleMessage();
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void scheduleMessage()
    {
        Collection<SchedulingProgramObject> schedulingProgramObjects = programStageInstanceService
            .getSendMesssageEvents();

        for ( SchedulingProgramObject schedulingProgramObject : schedulingProgramObjects )
        {
            String message = schedulingProgramObject.getMessage();

            try
            {
                OutboundSms outboundSms = new OutboundSms( message, schedulingProgramObject.getPhoneNumber() );
                outboundSms.setSender( DHIS_SYSTEM_SENDER );
                outboundSmsService.saveOutboundSms( outboundSms );

                String sql = "INSERT INTO programstageinstance_outboundsms"
                    + "( programstageinstanceid, outboundsmsid, sort_order) VALUES " + "("
                    + schedulingProgramObject.getProgramStageInstanceId() + ", " + outboundSms.getId() + ","
                    + (System.currentTimeMillis() / 1000) + ") ";

                jdbcTemplate.execute( sql );
            }
            catch ( SmsServiceException e )
            {
                message = e.getMessage();
            }
        }
    }

    private void sendMessage()
    {
        List<OutboundSms> outboundSmsList = outboundSmsService.getOutboundSms( OutboundSmsStatus.OUTBOUND );

        for ( OutboundSms outboundSms : outboundSmsList )
        {
            outboundSms.setStatus( OutboundSmsStatus.SENT );
            outboundSmsService.sendMessage( outboundSms, null );
        }
    }
}
