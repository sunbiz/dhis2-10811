package org.hisp.dhis.sms.parse;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.sms.incoming.IncomingSms;
import org.hisp.dhis.sms.queue.MessageQueue;

public class SMSConsumer
{
    private static final Log log = LogFactory.getLog( SMSConsumer.class );
    
    private ParserManager parserManager;

    private MessageQueue messageQueue;

    SMSConsumerThread thread;

    public void start()
    {
        messageQueue.initialize();
        if ( thread == null )
        {
            thread = new SMSConsumerThread();
            thread.start();
        }
    }

    public void stop()
    {
        thread.stopFetching();
        thread = null;
    }

    private class SMSConsumerThread
        extends Thread
    {
        private boolean stop;

        public SMSConsumerThread()
        {
        }

        public void run()
        {
            while ( !stop )
            {
                try
                {
                    fetchAndParseSMS();
                }
                catch ( Exception e )
                {
                    // ignore
                }
                try
                {
                    Thread.sleep( 3000 );
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }

        private void fetchAndParseSMS()
        {
            IncomingSms message = messageQueue.get();
            while ( message != null )
            {
                try
                {
                    parserManager.parse( message );
                }
                catch ( Exception e )
                {
                    log.error( e );
                }

                messageQueue.remove( message );
                message = messageQueue.get();
            }
        }

        public void stopFetching()
        {
            this.stop = true;
        }

    }

    public void setMessageQueue( MessageQueue messageQueue )
    {
        this.messageQueue = messageQueue;
    }

    public void setParserManager( ParserManager parserManager )
    {
        this.parserManager = parserManager;
    }
}
