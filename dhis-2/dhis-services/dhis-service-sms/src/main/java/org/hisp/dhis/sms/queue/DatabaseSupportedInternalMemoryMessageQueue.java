package org.hisp.dhis.sms.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hisp.dhis.sms.incoming.IncomingSms;
import org.hisp.dhis.sms.incoming.IncomingSmsStore;

public class DatabaseSupportedInternalMemoryMessageQueue
    implements MessageQueue
{

    List<IncomingSms> queue = new ArrayList<IncomingSms>();

    private IncomingSmsStore smsStore;

    @Override
    public void put( IncomingSms message )
    {
        queue.add( message );
    }

    @Override
    public IncomingSms get()
    {
        return queue.get( 0 );
    }

    @Override
    public void remove( IncomingSms message )
    {
        message.setParsed( true );
        smsStore.update( message );
        queue.remove( message );
    }

    @Override
    public void initialize()
    {
        Collection<IncomingSms> messages = smsStore.getAllUnparsedSmses();
        if ( messages != null )
        {
            queue.addAll( messages );
        }
    }

    public void setSmsStore( IncomingSmsStore smsStore )
    {
        this.smsStore = smsStore;
    }

}
