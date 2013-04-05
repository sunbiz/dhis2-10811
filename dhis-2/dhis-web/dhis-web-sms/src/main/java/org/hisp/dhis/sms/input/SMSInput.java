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

    private String sender, phone, number, msisdn;

    private String message, text, content;

    private IncomingSmsService incomingSmsService;

    @Override
    public String execute()
        throws Exception
    {
        IncomingSms sms = new IncomingSms();

        // setter for sms's originator
        if ( sender != null)
        {
            sms.setOriginator( sender );
        }
        else if ( phone != null )
        {
            sms.setOriginator( phone );
        }
        else if ( number != null )
        {
            sms.setOriginator( number );
        }
        else if ( msisdn != null )
        {
            sms.setOriginator( msisdn );
        }

        // setter for sms's text
        if ( message != null)
        {
            sms.setText( message );
        }
        else if ( text != null )
        {
            sms.setText( text );
        }
        else if ( content != null )
        {
            sms.setText( content );
        }
        
        // check whether 2 necessary attributes are null 
        if ( sms.getOriginator() == null || sms.getText() == null )
        {
            setNullToAll();
            return ERROR;
        }
        
        java.util.Date rec = new java.util.Date();
        sms.setReceivedDate( rec );
        sms.setSentDate( rec );

        sms.setEncoding( SmsMessageEncoding.ENC7BIT );
        sms.setStatus( SmsMessageStatus.INCOMING );
        sms.setGatewayId( "HARDCODEDTESTGATEWAY" );

        incomingSmsService.save( sms );

        setNullToAll();

        return SUCCESS;
    }

    public void setNullToAll()
    {
        sender = null;
        phone = null;
        number = null;
        message = null;
        text = null;
        content =null;
    }

    public void setSender( String sender )
    {
        this.sender = sender;
    }

    public void setPhone( String phone )
    {
        this.phone = phone;
    }
    
    public void setNumber( String number )
    {
        this.number = number;
    }

    public void setMsisdn( String msisdn )
    {
        this.msisdn = msisdn;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }
    
    public void setText( String text )
    {
        this.text = text;
    }
    
    public void setContent( String content )
    {
        this.content = content;
    }

    public void setIncomingSmsService( IncomingSmsService incomingSmsService )
    {
        this.incomingSmsService = incomingSmsService;
    }
}
