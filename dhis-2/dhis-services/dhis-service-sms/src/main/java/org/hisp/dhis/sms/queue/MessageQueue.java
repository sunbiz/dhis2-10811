package org.hisp.dhis.sms.queue;

import org.hisp.dhis.sms.incoming.IncomingSms;

public interface MessageQueue
{

    public void put( IncomingSms message );

    public IncomingSms get();

    public void remove( IncomingSms message );

    public void initialize();

}
