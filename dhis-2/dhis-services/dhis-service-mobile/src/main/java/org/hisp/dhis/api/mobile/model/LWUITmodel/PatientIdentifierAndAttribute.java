package org.hisp.dhis.api.mobile.model.LWUITmodel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.hisp.dhis.api.mobile.model.DataStreamSerializable;
import org.hisp.dhis.api.mobile.model.PatientAttribute;
import org.hisp.dhis.api.mobile.model.PatientIdentifier;

public class PatientIdentifierAndAttribute
    implements DataStreamSerializable
{
    private String clientVersion;

    private Collection<PatientIdentifier> patientIdentifiers;

    private Collection<PatientAttribute> patientAttributes;

    public String getClientVersion()
    {
        return clientVersion;
    }

    public void setClientVersion( String clientVersion )
    {
        this.clientVersion = clientVersion;
    }

    public Collection<PatientIdentifier> getPatientIdentifiers()
    {
        return patientIdentifiers;
    }

    public void setPatientIdentifiers( Collection<PatientIdentifier> patientIdentifiers )
    {
        this.patientIdentifiers = patientIdentifiers;
    }

    public Collection<PatientAttribute> getPatientAttributes()
    {
        return patientAttributes;
    }

    public void setPatientAttributes( Collection<PatientAttribute> patientAttributes )
    {
        this.patientAttributes = patientAttributes;
    }

    @Override
    public void serialize( DataOutputStream dout )
        throws IOException
    {
        if ( this.getClientVersion().equals( DataStreamSerializable.TWO_POINT_EIGHT ) )
        {
            this.serializeVersion2_8( dout );
        }
        else if ( this.getClientVersion().equals( DataStreamSerializable.TWO_POINT_NINE ) )
        {
            this.serializeVersion2_9( dout );
        }
        else if ( this.getClientVersion().equals( DataStreamSerializable.TWO_POINT_TEN ) )
        {
            this.serializeVersion2_10( dout );
        }
    }

    @Override
    public void deSerialize( DataInputStream dataInputStream )
        throws IOException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void serializeVersion2_8( DataOutputStream dataOutputStream )
        throws IOException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void serializeVersion2_9( DataOutputStream dataOutputStream )
        throws IOException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void serializeVersion2_10( DataOutputStream dout )
        throws IOException
    {

        if ( patientIdentifiers == null )
        {
            dout.writeInt( 0 );
        }
        else
        {

            dout.writeInt( patientIdentifiers.size() );

            for ( PatientIdentifier ptype : patientIdentifiers )
            {
                ptype.setClientVersion( DataStreamSerializable.TWO_POINT_TEN );
                ptype.serialize( dout );
            }

        }

        if ( patientAttributes == null )
        {
            dout.writeInt( 0 );
        }
        else
        {
            dout.writeInt( patientAttributes.size() );
            for ( PatientAttribute pa : patientAttributes )
            {
                pa.setClientVersion( DataStreamSerializable.TWO_POINT_TEN );
                pa.serialize( dout );
            }
        }

    }
}
