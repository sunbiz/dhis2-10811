package org.hisp.dhis.sms.outbound;

import org.hisp.dhis.sms.AbstractSmsTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OutboundSmsStoreTest extends AbstractSmsTest
{
    @Autowired
    private OutboundSmsStore outboundSmsStore;
    
    @Test
    public void testSimpleSaveGet() {
        OutboundSms sms = getOutboundSms();

        int id = outboundSmsStore.save( sms );

        flush();
        evict( sms );
        
        OutboundSms outboundSms = outboundSmsStore.get( id );

        verifySms( sms, outboundSms );
    }    
}
