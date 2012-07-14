        package org.hisp.dhis.sms.parse;

import org.hisp.dhis.sms.incoming.IncomingSms;

public interface ParserManager
{
    public void parse( IncomingSms sms )
        throws SMSParserException;
}
