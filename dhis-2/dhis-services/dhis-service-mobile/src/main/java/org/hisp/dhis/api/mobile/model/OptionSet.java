package org.hisp.dhis.api.mobile.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OptionSet
    extends Model
    implements DataStreamSerializable
{
    private String clientVersion;
    
    private List<String> options = new ArrayList<String>();

    public List<String> getOptions()
    {
        return options;
    }

    public void setOptions( List<String> options )
    {
        this.options = options;
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
    public void serialize( DataOutputStream dout )
        throws IOException
    {

        dout.writeInt( this.getId() );
        dout.writeUTF( this.getName() );
        dout.writeInt( this.options.size() );

        for ( String option : this.options )
        {
            dout.writeUTF( option );
        }

    }
    
    @Override
    public void serializeVerssion2_8( DataOutputStream dout )
        throws IOException
    {
        dout.writeInt( this.getId() );
        dout.writeUTF( this.getName() );
        dout.writeInt( this.options.size() );

        for ( String option : this.options )
        {
            dout.writeUTF( option );
        }
    }
    
    @Override
    public void serializeVerssion2_9( DataOutputStream dout )
        throws IOException
    {
        dout.writeInt( this.getId() );
        dout.writeUTF( this.getName() );

        dout.writeInt( this.options.size() );

        for ( String option : this.options )
        {
            dout.writeUTF( option );
        }
    }

    @Override
    public void deSerialize( DataInputStream dataInputStream )
        throws IOException
    {
        this.setId( dataInputStream.readInt() );
        this.setName( dataInputStream.readUTF() );
        int optionSize = dataInputStream.readInt();

        for ( int i = 0; i < optionSize; i++ )
        {
            String option = dataInputStream.readUTF();
            options.add( option );
        }

    }

}
