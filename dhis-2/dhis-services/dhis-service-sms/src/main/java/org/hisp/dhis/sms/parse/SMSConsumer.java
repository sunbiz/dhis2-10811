package org.hisp.dhis.sms.parse;

import org.hisp.dhis.sms.incoming.IncomingSms;
import org.hisp.dhis.sms.queue.MessageQueue;

// IEatSMS
public class SMSConsumer
{
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
                    System.out.println( e.getMessage() );
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
