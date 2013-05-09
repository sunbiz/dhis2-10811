package org.hisp.dhis.period;

public class ConfigurablePeriod
    extends Period
{
    private String isoDate;

    public ConfigurablePeriod( String isoDate, String name, String code )
    {
        this.isoDate = isoDate;
        this.name = name;
        this.code = code;
    }
    
    @Override
    public String getIsoDate()
    {
        return isoDate;
    }
}
