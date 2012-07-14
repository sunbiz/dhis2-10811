package org.hisp.dhis.api.mobile.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SMSCode
    implements DataStreamSerializable
{
    private String code;

    private int dataElementId;

    private int optionId;
    
    private String clientVersion;

    @Override
    public void serialize( DataOutputStream dataOutputStream )
        throws IOException
    {
        this.serializeVerssion2_10( dataOutputStream );
    }

    @Override
    public void deSerialize( DataInputStream dataInputStream )
        throws IOException
    {
        this.setCode( dataInputStream.readUTF() );
        this.setDataElementId( dataInputStream.readInt() );
        this.setOptionId( dataInputStream.readInt() );

    }

    @Override
    public void serializeVerssion2_8( DataOutputStream dataOutputStream )
        throws IOException
    {
        // does not exist in version 2.8
    }

    @Override
    public void serializeVerssion2_9( DataOutputStream dataOutputStream )
        throws IOException
    {
     // does not exist in version 2.9

    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public int getDataElementId()
    {
        return dataElementId;
    }

    public void setDataElementId( int dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public int getOptionId()
    {
        return optionId;
    }

    public void setOptionId( int optionId )
    {
        this.optionId = optionId;
    }
    
    public String getClientVersion()
    {
        return clientVersion;
    }

    public void setClientVersion( String clientVersion )
    {
        this.clientVersion = clientVersion;
    }

    @Override
    public void serializeVerssion2_10( DataOutputStream dataOutputStream )
        throws IOException
    {
        dataOutputStream.writeUTF( this.code );
        dataOutputStream.writeInt( this.dataElementId );
        dataOutputStream.writeInt( this.optionId );
    }

}
