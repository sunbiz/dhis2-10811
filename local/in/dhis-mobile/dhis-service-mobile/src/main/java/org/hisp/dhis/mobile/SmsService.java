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
package org.hisp.dhis.mobile;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hisp.dhis.messaging.api.MessageService;
import org.hisp.dhis.mobile.api.MobileImportService;
import org.hisp.dhis.mobile.api.SendSMS;
import org.hisp.dhis.mobile.api.SendSMSService;
import org.hisp.dhis.mobile.api.XmlCreatorService;
import org.smslib.GatewayException;
import org.smslib.ICallNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.IQueueSendingNotification;
import org.smslib.InboundBinaryMessage;
import org.smslib.InboundMessage;
import org.smslib.OutboundMessage;
import org.smslib.OutboundWapSIMessage;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.AGateway.Protocols;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundWapSIMessage.WapSISignals;
import org.smslib.modem.SerialModemGateway;

public class SmsService implements MessageService
{

    /*------------------------------------------------------------------
     * Dependencies
    ------------------------------------------------------------------*/
    XmlCreatorService xmlCreatorService;

    public void setXmlCreatorService( XmlCreatorService xmlCreatorService )
    {
        this.xmlCreatorService = xmlCreatorService;
    }

    MobileImportService mobileImportService;

    public void setMobileImportService( MobileImportService mobileImportService )
    {
        this.mobileImportService = mobileImportService;
    }

    private SendSMSService sendSMSService;

    public void setSendSMSService( SendSMSService sendSMSService )
    {
        this.sendSMSService = sendSMSService;
    }

    /*------------------------------------------------------------------
     * Implementation
    ------------------------------------------------------------------*/
    private static String CONFIG_FILE = "SMSServer.conf";

    private static Service serv;

    private static boolean gatewayLoaded;

    private boolean serviceStatus;

    private Properties props;

    private InboundNotification inboundNotification;

    private OutboundNotification outboundNotification;

    private CallNotification callNotification;

    public SmsService()
    {
        serv = new Service();
        inboundNotification = new InboundNotification();
        outboundNotification = new OutboundNotification();
        callNotification = new CallNotification();
    }

    @Override
    public String sendOtaMessage( String recipient, String url, String prompt )
    {
        String status = new String();
        try
        {
            OutboundWapSIMessage wapMsg = new OutboundWapSIMessage( recipient, new URL( url ), prompt );
            wapMsg.setSignal( WapSISignals.HIGH );
            if ( getServiceStatus() )
            {
                getService().sendMessage( wapMsg );
                status = "WAP MESSAGE SENT";
            } else
            {
                status = "SERVICE IS NOT RUNNING";

            }
        } catch ( Exception e )
        {
            e.printStackTrace();
            status = "ERROR SENDING WAP MSG";
        }
        return status;
    }

    private Service getService()
    {
        return serv;
    }

    @Override
    public boolean getServiceStatus()
    {
        return serviceStatus;
    }

    @Override
    public void setServiceStatus( boolean serviceStatus )
    {
        this.serviceStatus = serviceStatus;
    }

    @Override
    public String startService()
    {
        if ( !getServiceStatus() )
        {
            try
            {
                String result = loadConfiguration();
                if ( !result.contains( "ERROR" ) )
                {
                    serv.startService();
                    setServiceStatus( true );
                }
                return result;
            } catch ( Exception ex )
            {
                ex.printStackTrace();
                return "ERROR";
            }
        } else
        {
            return "SERVICE ALREADY RUNNING";
        }
    }

    @Override
    public String stopService()
    {
        if ( getServiceStatus() )
        {
            try
            {
                serv.stopService();
                setServiceStatus( false );
                return "SERVICE STOPPED";
            } catch ( Exception ex )
            {
                ex.printStackTrace();
                return "ERROR";
            }
        } else
        {
            return "SERVICE ALREADY STOPPED";
        }
    }

    @Override
    public String sendMessageToGroup( String groupName, List<String> recepients, String msg )
    {
        if ( getServiceStatus() )
        {
            serv.createGroup( groupName );

            //Collection<OutboundMessage> messages = new ArrayList<OutboundMessage>();
            for ( String recepient : recepients )
            {
                //OutboundMessage message = new OutboundMessage( recepient, msg );
                //System.out.println("Phone:"+recepient);
                //messages.add( message );
                serv.addToGroup( groupName, recepient );
            }

            OutboundMessage message = new OutboundMessage( groupName, msg );

            try
            {
                serv.sendMessage( message );
                //serv.sendMessages( messages );
                getService().getLogger().logInfo( "Message Sent to Group: " + groupName, null, null );
                return "SUCCESS";
            } catch ( TimeoutException ex )
            {
                getService().getLogger().logError( "Timeout error in sending message", ex, null );
                return "ERROR";
            } catch ( GatewayException ex )
            {
                getService().getLogger().logError( "Gateway Exception in sending message", ex, null );
                return "ERROR";
            } catch ( IOException ex )
            {
                getService().getLogger().logError( "IO Exception in sending message", ex, null );
                return "ERROR";
            } catch ( InterruptedException ex )
            {
                getService().getLogger().logError( "Interrupted Exception in sending message", ex, null );
                return "ERROR";
            } finally
            {
                serv.removeGroup( groupName );
            }
        } else
        {
            getService().getLogger().logError( "Service not running", null, null );
            return "SERVICE NOT RUNNING";
        }
    }

    @Override
    public String sendDrafts()
    {
        int successCount = 0;
        int failCount = 0;

        int draftCount = (int) sendSMSService.getRowCount();

        List<SendSMS> sendSMSList = new ArrayList<SendSMS>();

        if ( draftCount == 0 )
        {
            return "No Drafts to send";
        } else
        {
            if ( draftCount < SendSMS.sendSMSRange )
            {
                sendSMSList.addAll( sendSMSService.getSendSMS( 0, draftCount ) );
            } else
            {
                sendSMSList.addAll( sendSMSService.getSendSMS( 0, SendSMS.sendSMSRange - 1 ) );
            }
        }

        for ( SendSMS sendSMS : sendSMSList )
        {
            String status = sendMessage( sendSMS.getSenderInfo().split( "_" )[0], sendSMS.getSendingMessage() );
            if ( status.equalsIgnoreCase( "SUCCESS" ) )
            {
                sendSMSService.deleteSendSMS( sendSMS );
                successCount++;
            } else
            {
                if ( status.equalsIgnoreCase( "MODEMERROR" ) )
                {
                    getService().getLogger().logError( "Modem Stops Responding...Till then successfully sent : " + successCount, null, null );
                    return "Modem Stops Responding...Till then successfully sent : " + successCount;
                } else
                {
                    failCount++;
                }
            }
        }

        return "SMS Successfully Sent : " + successCount + " Failed : " + failCount;
    }

    @Override
    public String sendMessages( List<SendSMS> sendSMSList )
    {
        int successCount = 0;
        int failCount = 0;
        String status = null;
        for ( int i = 0; i < sendSMSList.size(); i++ )
        {
            //System.out.println("Successfully sent : "+ successCount +" Failed : " + failCount);
            status = sendMessage( sendSMSList.get( i ).getSenderInfo(), sendSMSList.get( i ).getSendingMessage() );
            if ( status.equalsIgnoreCase( "SUCCESS" ) )
            {
                successCount++;
            } else
            {
                if ( status.equalsIgnoreCase( "MODEMERROR" ) )
                {
                    getService().getLogger().logError( "Modem Stops Responding...Till then successfully sent : " + successCount, null, null );
                    //System.out.println("Successfully sent : "+ successCount +" Failed : " + failCount);
                    System.out.println( "modem is not responding....waiting for it to respond..." );
                    // return "Modem Stops Responding...Till then successfully sent : "+successCount; 
                    i--;
                } else
                {
                    if ( status.equals( "SERVICE NOT RUNNING" ) )
                    {
                        return "SERVICE NOT RUNNING";
                    }
                }
            }
        }

        return status;
    }

        public String sendMessagesToPort( List<SendSMS> sendSMSList ,int dest,int src)
    {
        int successCount = 0;
        int failCount = 0;
        String status = null;
        for ( int i = 0; i < sendSMSList.size(); i++ )
        {
            //System.out.println("Successfully sent : "+ successCount +" Failed : " + failCount);
            status = sendMessageToPort( sendSMSList.get( i ).getSenderInfo(), sendSMSList.get( i ).getSendingMessage(),dest,src );
            if ( status.equalsIgnoreCase( "SUCCESS" ) )
            {
                successCount++;
            } else
            {
                if ( status.equalsIgnoreCase( "MODEMERROR" ) )
                {
                    getService().getLogger().logError( "Modem Stops Responding...Till then successfully sent : " + successCount, null, null );
                    //System.out.println("Successfully sent : "+ successCount +" Failed : " + failCount);
                    System.out.println( "modem is not responding....waiting for it to respond..." );
                    // return "Modem Stops Responding...Till then successfully sent : "+successCount; 
                    i--;
                } else
                {
                    if ( status.equals( "SERVICE NOT RUNNING" ) )
                    {
                        return "SERVICE NOT RUNNING";
                    }
                }
            }
        }

        return status;
    }

    @Override
    public String sendMessage( String recipient, String msg )
    {
        OutboundMessage message = new OutboundMessage( recipient, msg );
        message.setDstPort(16000);
        message.setSrcPort(0);
        if ( getServiceStatus() )
        {
            try
            {
                if ( serv.sendMessage( message ) )
                {
                    getService().getLogger().logInfo( "Message Sent to: " + recipient, null, null );
                    return "SUCCESS";
                } else
                {
                    getService().getLogger().logError( "Timeout error in sending message to: " + recipient, null, null );
                    return "MODEMERROR";
                }
            } catch ( TimeoutException ex )
            {
                getService().getLogger().logError( "Timeout error in sending message", ex, null );
                return "ERROR";
            } catch ( GatewayException ex )
            {
                getService().getLogger().logError( "Gateway Exception in sending message", ex, null );
                return "ERROR";
            } catch ( IOException ex )
            {
                getService().getLogger().logError( "IO Exception in sending message", ex, null );
                return "ERROR";
            } catch ( InterruptedException ex )
            {
                getService().getLogger().logError( "Interrupted Exception in sending message", ex, null );
                return "ERROR";
            }
        } else
        {
            getService().getLogger().logError( "Service not running", null, null );
            return "SERVICE NOT RUNNING";
        }
    }

    public String sendMessageToPort( String recipient, String msg,int dest,int src )
    {
        
        OutboundMessage message = new OutboundMessage( recipient, msg );
        message.setDstPort( dest);
        message.setSrcPort( src );
        if ( getServiceStatus() )
        {
            try
            {
                if ( serv.sendMessage( message ) )
                {
                    getService().getLogger().logInfo( "Message Sent to: " + recipient, null, null );
                    return "SUCCESS";
                } else
                {
                    getService().getLogger().logError( "Timeout error in sending message to: " + recipient, null, null );
                    return "MODEMERROR";
                }
            } catch ( TimeoutException ex )
            {
                getService().getLogger().logError( "Timeout error in sending message", ex, null );
                return "ERROR";
            } catch ( GatewayException ex )
            {
                getService().getLogger().logError( "Gateway Exception in sending message", ex, null );
                return "ERROR";
            } catch ( IOException ex )
            {
                getService().getLogger().logError( "IO Exception in sending message", ex, null );
                return "ERROR";
            } catch ( InterruptedException ex )
            {
                getService().getLogger().logError( "Interrupted Exception in sending message", ex, null );
                return "ERROR";
            }
        } else
        {
            getService().getLogger().logError( "Service not running", null, null );
            return "SERVICE NOT RUNNING";
        }
    }

    
    @Override
    public void processMessage( Object message )
    {
        int delSMSflag = 0;
        try
        {
            getService().getLogger().logInfo( "---Starting processing message---", null, null );
            InboundBinaryMessage binaryMsg = (InboundBinaryMessage) message;
            byte[] compressedData = binaryMsg.getDataBytes();
            String unCompressedText = new String( Compressor.decompress( compressedData ), "UTF-8" );
            String sender = binaryMsg.getOriginator();
            Date sendTime = binaryMsg.getDate();

            
            if(unCompressedText.startsWith("i"))
            {
                mobileImportService.importInteractionMessage(unCompressedText, sender, sendTime);
            }
            
            else
            {

            // Creating XML File
            getService().getLogger().logInfo( "Creating XML file...", null, null );
            createXMLFile( sender, sendTime, unCompressedText );

            //Delete SMS
            getService().getLogger().logInfo( "Deleting SMS...", null, null );
            if ( getProperties().getProperty( "settings.delete_after_processing", "no" ).equalsIgnoreCase( "yes" ) )
            {
                try
                {
                    getService().deleteMessage( (InboundMessage) message );
                    getService().getLogger().logInfo( "Deleted message", null, null );
                    delSMSflag = 1;
                } catch ( Exception e )
                {
                    getService().getLogger().logError( "Error deleting received message!", e, null );
                }
            }

            // Import data into DHIS
            //getService().getLogger().logInfo( "Importing data into DHIS...", null, null );
            //String statusMessage = importData( sender, sendTime, unCompressedText );

            //Sending ACK/Status SMS
            //getService().getLogger().logInfo( "Sending ACK/Status messge...", null, null );
            //sendMessage( sender, statusMessage );

            getService().getLogger().logInfo( "---Message Processing Finished---", null, null );

            //String statusMessage = saveData( sender, sendTime, unCompressedText );
            //getService().getLogger().logInfo( "Saved Report. Sending Acknowledgement to " + sender, null, null );
            //sendAck( sender, "REPORT", unCompressedText );
            //sendMessage( sender, statusMessage );
            
            }

        } catch ( UnsupportedEncodingException uneex )
        {
            getService().getLogger().logError( "Error reading encoding: ", uneex, null );
            return;
        } catch ( ClassCastException ccex )
        {
            getService().getLogger().logError( "Error performing ClassCast: ", ccex, null );
            return;
        } catch ( ArithmeticException aex )
        {
            getService().getLogger().logError( "Error performing arithmatic operation: ", aex, null );
            return;
        } catch ( ArrayIndexOutOfBoundsException aiobex )
        {
            getService().getLogger().logError( "Error with message format. PLEASE CHECK APP VERSION: ", aiobex, null );
            return;
        } catch ( NullPointerException npex )
        {
            getService().getLogger().logError( "MISSING form number. PLEASE CHECK formIDLayout.csv: ", npex, null );
            return;
        } finally
        {
            if ( getProperties().getProperty( "settings.delete_after_processing", "no" ).equalsIgnoreCase( "yes" ) && delSMSflag == 0 )
            {
                try
                {
                    getService().deleteMessage( (InboundMessage) message );
                    getService().getLogger().logInfo( "Deleted message", null, null );
                } catch ( Exception e )
                {
                    getService().getLogger().logError( "Error deleting received message!", e, null );
                }
            }
            getService().getLogger().logInfo( "---Message Processing Finished---", null, null );
        }
        
    }

    @Override
    public void sendAck( String recipient, Object message, String msg )
    {
        if ( message.equals( "REPORT" ) )
        {
            if ( msg.split( "\\#" )[0].equals( "2" ) )
            {
                String[] msgArr = msg.split( "\\#" )[1].split( "\\*" )[1].split( "\\?" );
                String periodType = msgArr[0];
                String period = msgArr[1].split( "\\$" )[0];
                if ( periodType.equals( "1" ) )
                {
                    try
                    {
                        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
                        Date parsedDate = dateFormat.parse( period );
                        dateFormat = new SimpleDateFormat( "dd-MM-yyyy" );
                        sendMessage( recipient, "THANK YOU FOR SENDING DAILY REPORT FOR " + dateFormat.format( parsedDate ) );
                    } catch ( ParseException ex )
                    {
                        ex.printStackTrace();
                    }
                } else
                {
                    if ( periodType.equals( "3" ) )
                    {
                        try
                        {
                            SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
                            Date parsedDate = dateFormat.parse( period );
                            dateFormat = new SimpleDateFormat( "MMM-yyyy" );
                            sendMessage( recipient, "THANK YOU FOR SENDING MONTHLY REPORT FOR " + dateFormat.format( parsedDate ) );
                        } catch ( ParseException ex )
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void createXMLFile( String mobileNumber, Date sendTime, String data )
    {
        xmlCreatorService.setPhoneNumber( mobileNumber );
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" );
        String timeStamp = dateFormat.format( sendTime );
        xmlCreatorService.setSendTime( timeStamp );
        xmlCreatorService.setInfo( data );
        xmlCreatorService.run(); //should be made thread-safe
        getService().getLogger().logInfo( "XML successfully created for msg from: " + mobileNumber, null, null );
    }

    public String importData( String mobileNumber, Date sendTime, String data )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" );
        String timeStamp = dateFormat.format( sendTime );

        String importFileName = mobileNumber + "_" + timeStamp.replace( ":", "-" ) + ".xml";
        String statusMessage = mobileImportService.importXMLFile( importFileName );
        getService().getLogger().logInfo( "Importing Completed for current messages", null, null );
        return statusMessage;
    }

    @Override
    public String saveData( String mobileNumber, Date sendTime, String data )
    {
        xmlCreatorService.setPhoneNumber( mobileNumber );
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" );
        String timeStamp = dateFormat.format( sendTime );
        xmlCreatorService.setSendTime( timeStamp );
        xmlCreatorService.setInfo( data );
        xmlCreatorService.run(); //should be made thread-safe
        getService().getLogger().logInfo( "XML successfully created for msg from: " + mobileNumber, null, null );
        //mobileImportService.importAllFiles();
        String importFileName = mobileNumber + "_" + timeStamp.replace( ":", "-" ) + ".xml";
        String statusMessage = mobileImportService.importXMLFile( importFileName );
        getService().getLogger().logInfo( "Importing Completed for current messages", null, null );
        return statusMessage;
    }

    private void processStatusReport( InboundMessage message )
    {
        String originator = message.getOriginator();
        getService().getLogger().logInfo( "STATUS REPORT received from: " + originator, null, null );
    }

    @Override
    public String processPendingMessages()
    {
        if ( !getServiceStatus() )
        {
            getService().getLogger().logError( "SMSService not running", null, null );

            return "SMSService not running";
        }

        List<InboundMessage> msgList = new ArrayList<InboundMessage>();

        msgList = readAllMessages();

        if ( msgList != null && msgList.size() > 0 )
        {
            for ( InboundMessage msg : msgList )
            {
                processMessage( msg );
            }

            return "Successfully Processed all Pending Messages.";
        } else
        {
            return "No Pending Messages to Process.";
        }
    }

    @Override
    public Map<String, String> readAllPendingMessages()
    {
        if ( !getServiceStatus() )
        {
            getService().getLogger().logError( "SMSService not running", null, null );

            return null;
        }

        Map<String, String> pendingMessages = new HashMap<String, String>();
        // Define a list which will hold the read messages.
        List<InboundMessage> msgList = new ArrayList<InboundMessage>();
        try
        {
            getService().getLogger().logInfo( "Reading All Pending Messages...", null, null );

            serv.readMessages( msgList, MessageClasses.ALL );

            for ( Object msg : msgList )
            {
                try
                {
                    InboundBinaryMessage binaryMsg = (InboundBinaryMessage) msg;
                    byte[] compressedData = binaryMsg.getDataBytes();
                    String unCompressedText = new String( Compressor.decompress( compressedData ), "UTF-8" );
                    Date sendTime = binaryMsg.getDate();
                    String sender = binaryMsg.getOriginator();
                    SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" );
                    String timeStamp = dateFormat.format( sendTime );

                    pendingMessages.put( sender + "_" + timeStamp, unCompressedText );

                } catch ( ClassCastException ccex )
                {
                    InboundMessage message = (InboundMessage) msg;
                    Date sendTime = message.getDate();
                    String sender = message.getOriginator();
                    SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" );
                    String timeStamp = dateFormat.format( sendTime );

                    try
                    {
                        pendingMessages.put( sender + "_" + timeStamp, message.getText() );
                        getService().getLogger().logInfo( "Normal Text Message", null, null );
                    } catch ( Exception e )
                    {
                        pendingMessages.put( sender + "_" + timeStamp, "Unsupported Format" );
                        getService().getLogger().logError( "UnSupported Format", null, null );
                    }
                } catch ( Exception e )
                {
                    getService().getLogger().logError( "Error While reading messages, returning whatever sms got till now", null, null );
                    return pendingMessages;
                }
            }

            return pendingMessages;
        } catch ( Exception e )
        {
            e.printStackTrace();
            getService().getLogger().logError( "SMSServer: error setting custom balancer!", null, null );

            return null;
        }
    }

    @Override
    public List<InboundMessage> readAllMessages()
    {
        if ( !getServiceStatus() )
        {
            getService().getLogger().logError( "SMSService not running", null, null );

            return null;
        }

        // Define a list which will hold the read messages.
        List<InboundMessage> msgList = new ArrayList<InboundMessage>();
        try
        {
            getService().getLogger().logInfo( "Reading All Messages...", null, null );

            serv.readMessages( msgList, MessageClasses.ALL );

            return msgList;
        } catch ( Exception e )
        {
            e.printStackTrace();
            getService().getLogger().logError( "SMSServer: error setting custom balancer!", null, null );

            return null;
        }
    }

    /*------------------------------------------------------------------
     * Internal methods
    ------------------------------------------------------------------*/
    //<editor-fold defaultstate="collapsed" desc=" Internal Methods ">
    //<editor-fold defaultstate="collapsed" desc=" Load Configuration from DHIS2 HOME ">
    private String loadConfiguration() throws Exception
    {
        CONFIG_FILE = System.getenv( "DHIS2_HOME" ) + File.separator + "SMSServer.conf";

        if ( new File( CONFIG_FILE ).exists() )
        {
            FileInputStream f = new FileInputStream( CONFIG_FILE );
            this.props = new Properties();
            getProperties().load( f );
            f.close();

            //<editor-fold defaultstate="collapsed" desc=" Get Balancer ">
            if ( getProperties().getProperty( "smsserver.balancer", "" ).length() > 0 )
            {
                try
                {
                    Object[] args = new Object[]
                    {
                        getService()
                    };
                    Class<?>[] argsClass = new Class[]
                    {
                        Service.class
                    };
                    Class<?> c = Class.forName( ( getProperties().getProperty( "smsserver.balancer", "" ).indexOf( '.' ) == -1 ? "org.smslib.balancing." : "" ) + getProperties().getProperty( "smsserver.balancer", "" ) );
                    Constructor<?> constructor = c.getConstructor( argsClass );
                    org.smslib.balancing.LoadBalancer balancer = (org.smslib.balancing.LoadBalancer) constructor.newInstance( args );
                    getService().setLoadBalancer( balancer );
                    getService().getLogger().logInfo( "SMSServer: set balancer to: " + getProperties().getProperty( "smsserver.balancer", "" ), null, null );
                } catch ( Exception e )
                {
                    e.printStackTrace();
                    getService().getLogger().logError( "SMSServer: error setting custom balancer!", null, null );
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" Get Router ">
            if ( getProperties().getProperty( "smsserver.router", "" ).length() > 0 )
            {
                try
                {
                    Object[] args = new Object[]
                    {
                        getService()
                    };
                    Class<?>[] argsClass = new Class[]
                    {
                        Service.class
                    };
                    Class<?> c = Class.forName( ( getProperties().getProperty( "smsserver.router", "" ).indexOf( '.' ) == -1 ? "org.smslib.routing." : "" ) + getProperties().getProperty( "smsserver.router", "" ) );
                    Constructor<?> constructor = c.getConstructor( argsClass );
                    org.smslib.routing.Router router = (org.smslib.routing.Router) constructor.newInstance( args );
                    getService().setRouter( router );
                    getService().getLogger().logInfo( "SMSServer: set router to: " + getProperties().getProperty( "smsserver.router", "" ), null, null );
                } catch ( Exception e )
                {
                    getService().getLogger().logError( "SMSServer: error setting custom balancer!", null, null );
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" Get Gateway & Configuration ">
            for ( int i = 0; i < Integer.MAX_VALUE; i++ )
            {
                try
                {
                    String propName = "gateway." + i;
                    String propValue = getProperties().getProperty( propName, "" ).trim();
                    if ( propValue.length() == 0 )
                    {
                        break;
                    }
                    String modemName = propValue.split( "\\," )[0].trim();
                    String port = getProperties().getProperty( modemName + ".port" );
                    int baudRate = Integer.parseInt( getProperties().getProperty( modemName + ".baudrate" ) );
                    String manufacturer = getProperties().getProperty( modemName + ".manufacturer" );
                    String model = getProperties().getProperty( modemName + ".model" );
                    String protocol = getProperties().getProperty( modemName + ".protocol" );
                    String pin = getProperties().getProperty( modemName + ".pin" );
                    String inbound = getProperties().getProperty( modemName + ".inbound" );
                    String outbound = getProperties().getProperty( modemName + ".outbound" );
                    String simMemLocation = getProperties().getProperty( modemName + ".simMemLocation" );

                    SerialModemGateway gateway = new SerialModemGateway( modemName, port, baudRate, manufacturer, model );

                    if ( simMemLocation != null || !simMemLocation.equals( "-" ) )
                    {
                        gateway.getATHandler().setStorageLocations( simMemLocation );
                    }

                    if ( protocol != null && protocol.equalsIgnoreCase( "PDU" ) )
                    {
                        gateway.setProtocol( Protocols.PDU );
                    } else
                    {
                        if ( protocol != null && protocol.equalsIgnoreCase( "TEXT" ) )
                        {
                            gateway.setProtocol( Protocols.TEXT );

                        } else
                        {
                            gateway.setProtocol( Protocols.PDU );
                        }
                    }
                    if ( pin != null )
                    {
                        gateway.setSimPin( pin );
                    }

                    getService().setCallNotification( callNotification );

                    if ( inbound.equalsIgnoreCase( "yes" ) )
                    {
                        gateway.setInbound( true );
                        getService().setInboundMessageNotification( inboundNotification );
                    } else
                    {
                        gateway.setInbound( false );
                    }
                    if ( outbound.equalsIgnoreCase( "yes" ) )
                    {
                        gateway.setOutbound( true );
                        getService().setOutboundMessageNotification( outboundNotification );
                    } else
                    {
                        gateway.setOutbound( false );
                    }
                    if ( !gatewayLoaded )
                    {
                        getService().addGateway( gateway );
                    }
                    getService().getLogger().logInfo( "SMSServer: added gateway " + i + " / ", null, null );
                } catch ( Exception e )
                {
                    getService().getLogger().logError( "SMSServer: Unknown Gateway in configuration file!, " + e.getMessage(), null, null );

                    e.printStackTrace();

                }
            }
            gatewayLoaded = true;
            //</editor-fold>
            return "SUCCESSFULLY STARTED SERVICE";
        } else
        {
            return "ERROR LOADING CONFIGURATION FILE";
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Get Properties from file ">
    private Properties getProperties()
    {
        return this.props;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" InboundNotification Class ">
    class InboundNotification implements IInboundMessageNotification
    {

        @Override
        public void process( String gatewayId, MessageTypes msgType, InboundMessage msg )
        {
            if ( msgType == MessageTypes.INBOUND )
            {
                getService().getLogger().logInfo( "New INBOUND MESSAGE on Gateway: " + gatewayId + " from " + msg.getOriginator(), null, null );
                processMessage( msg );
            } else
            {
                if ( msgType == MessageTypes.STATUSREPORT )
                {
                    getService().getLogger().logInfo( "New STATUS REPORT on Gateway: " + gatewayId + " from " + msg.getOriginator(), null, null );
                    processStatusReport( msg );
                }

                if ( getProperties().getProperty( "settings.delete_after_processing", "no" ).equalsIgnoreCase( "yes" ) )
                {
                    try
                    {
                        getService().deleteMessage( msg );
                        getService().getLogger().logInfo( "Deleted message", null, null );
                    } catch ( Exception e )
                    {
                        getService().getLogger().logError( "Error deleting received message!", e, null );
                    }
                }
            }

            /*
            if ( getProperties().getProperty( "settings.delete_after_processing", "no" ).equalsIgnoreCase( "yes" ) )
            {
            try
            {
            getService().deleteMessage( msg );
            getService().getLogger().logInfo( "Deleted message", null, null );
            } 
            catch ( Exception e )
            {
            getService().getLogger().logError( "Error deleting received message!", e, null );
            }
            }
             */
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" OutboundNotification Class ">
    class OutboundNotification implements IOutboundMessageNotification
    {

        @Override
        public void process( String gtwId, org.smslib.OutboundMessage msg )
        {
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" QueueSendingNotification Class ">
    class QueueSendingNotification implements IQueueSendingNotification
    {

        @Override
        public void process( String gtwId, OutboundMessage msg )
        {
            getService().getLogger().logInfo( "**** >>>> Now Sending: " + msg.getRecipient(), null, gtwId );
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" InboundCallNotification Class ">
    class CallNotification implements ICallNotification
    {

        public void process( String gatewayId, String callerId )
        {
            getService().getLogger().logInfo( "**** >>>> Getting call from: " + callerId, null, null );
        }
    }
    //</editor-fold>
    //</editor-fold>
    /*----------------------------------------------------------------*/
}
