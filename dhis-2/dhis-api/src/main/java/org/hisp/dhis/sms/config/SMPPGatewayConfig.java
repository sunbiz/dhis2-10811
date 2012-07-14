package org.hisp.dhis.sms.config;

public class SMPPGatewayConfig
    extends SmsGatewayConfig
{
    private static final long serialVersionUID = 780006289158356660L;

    private String address;

    private int port;

    private String username;

    private String password;

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    @Override
    public boolean isInbound()
    {
        return true;
    }

    @Override
    public boolean isOutbound()
    {
        return true;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort( int port )
    {
        this.port = port;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress( String address )
    {
        this.address = address;
    }

}
