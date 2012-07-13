package org.hisp.dhis.sms;

public class SmsServiceNotEnabledException
    extends SmsServiceException
{
    private static final long serialVersionUID = -1484667419558937721L;

    public SmsServiceNotEnabledException( )
    {
        super( "Sms service is not enabled" );
    }
}
