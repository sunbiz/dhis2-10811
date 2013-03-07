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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.hisp.dhis.smscommand.SMSCommand;

public class J2MEDataEntryParser
    implements IParser
{
    private SMSCommand smsCommand;

    @Override
    public Map<String, String> parse( String sms )
    {
        String[] keyValuePairs = null;
        if ( sms.indexOf( "#" ) > -1 )
        {
            keyValuePairs = sms.split( "#" );
        }
        else
        {
            keyValuePairs = new String[1];
            keyValuePairs[0] = sms;
        }

        Map<String, String> keyValueMap = new HashMap<String, String>();
        for ( String keyValuePair : keyValuePairs )
        {
            String[] token = keyValuePair.split( Pattern.quote( smsCommand.getSeparator() ) );
            keyValueMap.put( token[0], token[1] );
        }

        return keyValueMap;
    }

    @Override
    public void setSeparator( String separator )
    {
        // TODO Auto-generated method stub
    }

    public SMSCommand getSmsCommand()
    {
        return smsCommand;
    }

    public void setSmsCommand( SMSCommand smsCommand )
    {
        this.smsCommand = smsCommand;
    }
}
