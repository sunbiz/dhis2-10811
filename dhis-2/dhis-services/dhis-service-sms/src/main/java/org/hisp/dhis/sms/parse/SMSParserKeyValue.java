/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hisp.dhis.sms.parse;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Magnus Korvald
 */
public class SMSParserKeyValue
    implements IParser
{
    // "(\\w+)\\s*\\*\\s*([\\w ]+)\\s*(\\*|$)*\\s*";
    // = "([a-zA-Z]+)\\s*(\\d+)";
    private String defaultPattern = "([a-zA-Z]+)\\s*(\\d+)";

    private Pattern pattern = Pattern.compile( defaultPattern );

    @Override
    public Map<String, String> parse( String sms )
    {

        HashMap<String, String> output = new HashMap<String, String>();

        Matcher m = pattern.matcher( sms );
        while ( m.find() )
        {
            String key = m.group( 1 );
            String value = m.group( 2 );
            System.out.println( "Key: " + key + " Value: " + value );
            if ( key != null && value != null )
            {
                output.put( key.toUpperCase(), value );
            }
        }

        return output;
    }

    public void setSeparator( String separator )
    {
        String x = null;
        x = "(\\w+)\\s*\\" + separator.trim() + "\\s*([\\w ]+)\\s*(\\" + separator.trim() + "|$)*\\s*";
        pattern = Pattern.compile( x );
    }

}
