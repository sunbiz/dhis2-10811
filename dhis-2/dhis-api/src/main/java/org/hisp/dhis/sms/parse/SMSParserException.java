package org.hisp.dhis.sms.parse;

public class SMSParserException
    extends RuntimeException
{

    private static final long serialVersionUID = -8088120989819092567L;

    SMSParserException(String message){
        super(message);
    }
    
}
