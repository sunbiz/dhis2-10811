package org.hisp.dhis.sms.input;

import org.hisp.dhis.sms.incoming.IncomingSms;
import org.hisp.dhis.sms.incoming.IncomingSmsService;
import org.hisp.dhis.sms.incoming.SmsMessageEncoding;
import org.hisp.dhis.sms.incoming.SmsMessageStatus;

import com.opensymphony.xwork2.Action;

/**
 * 
 * @author Christian and Magnus
 */
public class SMSInput
    implements Action
{

    private String sender, message;
    private IncomingSmsService incomingSmsService;

    @Override
    public String execute()
        throws Exception
    {

        if(sender == null || message == null ){
            return ERROR;
        }
        
        System.out.println( "Sender: " + sender + ", Message: " + message );
        IncomingSms sms = new IncomingSms();
        sms.setText( message );
        sms.setOriginator( sender );

        java.util.Date rec = new java.util.Date();
        sms.setReceivedDate( rec );
        sms.setSentDate( rec );

        sms.setEncoding( SmsMessageEncoding.ENC7BIT );
        sms.setStatus( SmsMessageStatus.INCOMING );
        sms.setGatewayId( "HARDCODEDTESTGATEWAY" );

        incomingSmsService.save( sms );

        sender = null;
        message = null;
        
        return SUCCESS;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender( String sender )
    {
        this.sender = sender;
    }

    public void setIncomingSmsService( IncomingSmsService incomingSmsService )
    {
        this.incomingSmsService = incomingSmsService;
    }
}
