package org.hisp.dhis.mapping.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.mapping.MapLegendSet;
import org.hisp.dhis.mapping.MappingService;

import com.opensymphony.xwork2.Action;

public class AssignDataElementsToMapLegendSetAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private MappingService mappingService;

    public void setMappingService( MappingService mappingService )
    {
        this.mappingService = mappingService;
    }
    
    private DataElementService dataElementService;
    
    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private Integer id;
    
    public void setId( Integer id )
    {
        this.id = id;
    }

    private Collection<String> dataElements;
    
    public void setDataElements( Collection<String> dataElements )
    {
        this.dataElements = dataElements;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        MapLegendSet mapLegendSet = mappingService.getMapLegendSet( id );
        
        Set<DataElement> dataElementSet = new HashSet<DataElement>();

        for ( String dataElement : dataElements )
        {
            dataElementSet.add( dataElementService.getDataElement( Integer.parseInt( dataElement ) ) );
        }
        
        mapLegendSet.setDataElements( dataElementSet );
        
        mappingService.updateMapLegendSet( mapLegendSet );
        
        return SUCCESS;
    }
}
