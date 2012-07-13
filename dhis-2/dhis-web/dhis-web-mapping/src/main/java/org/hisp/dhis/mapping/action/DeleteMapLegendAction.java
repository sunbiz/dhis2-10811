package org.hisp.dhis.mapping.action;

import org.hisp.dhis.mapping.MapLegend;
import org.hisp.dhis.mapping.MappingService;

import com.opensymphony.xwork2.Action;

public class DeleteMapLegendAction
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
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        MapLegend mapLegend = mappingService.getMapLegend( id.intValue() );

        if ( mapLegend != null )
        {
            mappingService.deleteMapLegend( mapLegend );
        }

        return SUCCESS;
    }
}