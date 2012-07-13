package org.hisp.dhis.common.adapter;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Parameters
{

    List<Parameter> parameters;

    public Parameters()
    {
    }

    public Parameters( List<Parameter> parameters )
    {
        this.parameters = parameters;
    }

    @XmlElement( name = "parameter" )
    public List<Parameter> getParameters()
    {
        return parameters;
    }

    public void setParameters( List<Parameter> parameters )
    {
        this.parameters = parameters;
    }

    
}
