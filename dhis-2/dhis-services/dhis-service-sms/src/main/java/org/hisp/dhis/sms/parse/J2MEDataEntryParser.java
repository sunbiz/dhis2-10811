package org.hisp.dhis.sms.parse;

import java.util.Map;

public class J2MEDataEntryParser
    implements IParser
{

    @Override
    public Map<String, String> parse( String sms )
    {
        String keyValueSection = sms.split( "" )[1];
        
        return null;
    }

    @Override
    public void setSeparator( String separator )
    {
        // TODO Auto-generated method stub

    }

}
