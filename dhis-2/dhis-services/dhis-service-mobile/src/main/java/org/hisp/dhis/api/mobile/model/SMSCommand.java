package org.hisp.dhis.api.mobile.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class SMSCommand
    implements DataStreamSerializable
{
    private String parserType;

    private String separator;

    private String codeSeparator;

    private List<SMSCode> smsCodes;

    private String clientVersion;

    private int dataSetId;

    @Override
    public void serialize( DataOutputStream dataOutputStream )
        throws IOException
    {
        this.serializeVersion2_10( dataOutputStream );
    }

    @Override
    public void deSerialize( DataInputStream dataInputStream )
        throws IOException
    {
        // no need for deserialize
    }

    @Override
    public void serializeVersion2_8( DataOutputStream dataOutputStream )
        throws IOException
    {
        // does not exist in version 2.8
    }

    @Override
    public void serializeVersion2_9( DataOutputStream dataOutputStream )
        throws IOException
    {
        // does not exist in version 2.9
    }

    public String getParserType()
    {
        return parserType;
    }

    public void setParserType( String parserType )
    {
        this.parserType = parserType;
    }

    public String getSeparator()
    {
        return separator;
    }

    public void setSeparator( String separator )
    {
        this.separator = separator;
    }

    public String getCodeSeparator()
    {
        return codeSeparator;
    }

    public void setCodeSeparator( String codeSeparator )
    {
        this.codeSeparator = codeSeparator;
    }

    public List<SMSCode> getSmsCodes()
    {
        return smsCodes;
    }

    public void setSmsCodes( List<SMSCode> smsCodes )
    {
        this.smsCodes = smsCodes;
    }

    public String getClientVersion()
    {
        return clientVersion;
    }

    public void setClientVersion( String clientVersion )
    {
        this.clientVersion = clientVersion;
    }

    public int getDataSetId()
    {
        return dataSetId;
    }

    public void setDataSetId( int dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    @Override
    public void serializeVersion2_10( DataOutputStream dataOutputStream )
        throws IOException
    {
        dataOutputStream.writeUTF( this.parserType );
        if ( this.separator != null )
        {
            dataOutputStream.writeUTF( this.separator );
        }
        else
        {
            dataOutputStream.writeUTF( "" );
        }

        if ( this.codeSeparator != null )
        {
            dataOutputStream.writeUTF( this.codeSeparator );
        }
        else
        {
            dataOutputStream.writeUTF( "" );
        }
        
        dataOutputStream.writeInt( this.dataSetId );

        if ( this.smsCodes == null )
        {
            dataOutputStream.writeInt( 0 );
        }
        else
        {
            dataOutputStream.writeInt( smsCodes.size() );
            for ( SMSCode smsCode : smsCodes )
            {
                smsCode.serialize( dataOutputStream );
            }
        }
    }

}
