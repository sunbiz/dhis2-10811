package org.hisp.dhis.sms.incoming;

import org.smslib.AGateway;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;

public class SMPPInboundNotification
    implements IInboundMessageNotification
{
    
    private IncomingSmsService incomingSmsService;

    @Override
    public void process( AGateway gateway, MessageTypes msgType, InboundMessage msg )
    {

        System.out.println( msg );

        IncomingSms incomingSms = new IncomingSms();

        incomingSms.setOriginator( msg.getOriginator() );

        incomingSms.setEncoding( SmsMessageEncoding.ENC7BIT );

        incomingSms.setSentDate( msg.getDate() );

        incomingSms.setReceivedDate( msg.getDate() );

        incomingSms.setText( msg.getText() );

        incomingSms.setGatewayId( msg.getGatewayId() );

        incomingSms.setStatus( SmsMessageStatus.PROCESSED );

        incomingSms.setStatusMessage( "imported" );
        
        incomingSmsService.save( incomingSms );

    }

    public void setIncomingSmsService( IncomingSmsService incomingSmsService )
    {
        this.incomingSmsService = incomingSmsService;
    }

}
