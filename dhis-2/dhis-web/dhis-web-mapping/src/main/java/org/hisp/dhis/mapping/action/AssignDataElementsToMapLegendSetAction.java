package org.hisp.dhis.mapping.action;

import java.util.Collection;

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
        
        for ( String dataElement : dataElements )
        {
            DataElement element = dataElementService.getDataElement( Integer.parseInt( dataElement ) );
            element.setLegendSet( mapLegendSet );
            dataElementService.updateDataElement( element );
        }
        
        return SUCCESS;
    }
}
