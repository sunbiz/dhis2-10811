package org.hisp.dhis.sms;

/**
 * SmsServiceException signalling no transport provider available to sms service
 */
public class SmsTransportProviderNotFoundException
    extends SmsServiceException
{
    private static final long serialVersionUID = 8644436214252461786L;

    public SmsTransportProviderNotFoundException( String message, Exception cause )
    {
        super( message, cause );
    }

    public SmsTransportProviderNotFoundException( String message )
    {
        super( message );
    }
}
