package org.hisp.dhis.common.adapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"value", "key"})
public class Parameter
{

    private String key;

    private String value;

    public Parameter()
    {
    }

    public Parameter( String key, String value )
    {
        this.key = key;
        this.value = value;
    }

    @XmlAttribute
    public String getKey()
    {
        return key;
    }

    @XmlAttribute
    public String getValue()
    {
        return value;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

}
