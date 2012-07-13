package org.hisp.dhis.sms;

/*
 * Copyright (c) 2004-2011, University of Oslo
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.sms.outbound.OutboundSms;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSmsTest
    extends DhisSpringTest
{
    protected String gatewayId;

    @Autowired
    protected SessionFactory sessionFactory;

    protected void flush()
    {
        sessionFactory.getCurrentSession().flush();
    }

    protected void evict( Object o )
    {
        sessionFactory.getCurrentSession().evict( o );
    }

    @SuppressWarnings("serial")
    protected OutboundSms getOutboundSms()
    {
        OutboundSms sms = new OutboundSms();
        sms.setMessage( "1" );
        Set<String> recipients = new HashSet<String>() {{ add("1"); add("2");}};
        sms.setRecipients( recipients  );
        return sms;
    }

    protected void verifySms( OutboundSms expected, OutboundSms actual )
    {
        assertNotNull(actual);
        assertNotNull( actual.getDate() );
        assertEquals( expected.getId(), actual.getId());
        assertEquals( expected.getMessage(), actual.getMessage() );
        assertEquals( expected.getRecipients(), actual.getRecipients() );
    }

    protected void assertNotNullSize( Collection<?> c, int i )
    {
        assertNotNull( c );
        assertEquals(i, c.size());
    }

}
