package org.hisp.dhis.mapping.action;

import java.util.Collection;

import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.mapping.MapLegendSet;
import org.hisp.dhis.mapping.MappingService;

import com.opensymphony.xwork2.Action;

public class AssignIndicatorsToMapLegendSetAction
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
    
    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private Integer id;
    
    public void setId( Integer id )
    {
        this.id = id;
    }

    private Collection<String> indicators;
    
    public void setIndicators( Collection<String> indicators )
    {
        this.indicators = indicators;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        MapLegendSet mapLegendSet = mappingService.getMapLegendSet( id );
        
        for ( String indicator : indicators )
        {
            Indicator i = indicatorService.getIndicator( Integer.parseInt( indicator ) );
            i.setLegendSet( mapLegendSet );
            indicatorService.updateIndicator( i );
        }
        
        mappingService.updateMapLegendSet( mapLegendSet );
        
        return SUCCESS;
    }
}
