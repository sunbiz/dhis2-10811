package org.hisp.dhis.common.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;


public class ParametersMapXmlAdapter extends XmlAdapter<Parameters, Map<String,String>> {

    @Override
    public Parameters marshal( Map<String, String> v )
        throws Exception
    {
        ArrayList<Parameter> list = new ArrayList<Parameter>();
        
        for ( Map.Entry<String,String> e : v.entrySet() ) {

            list.add( new Parameter(e.getKey(), e.getValue()) );
        }
        return new Parameters(list);
    }

    @Override
    public Map<String, String> unmarshal( Parameters v)
        throws Exception
    {
        Map<String,String> map = new HashMap<String,String>();
        for ( Parameter p : v.getParameters() ) {
            map.put(p.getKey(), p.getValue());
        }
        return map;
    }

}