package org.hisp.dhis.mapping.action;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
        
        Set<Indicator> indicatorSet = new HashSet<Indicator>();

        for ( String indicator : indicators )
        {
            indicatorSet.add( indicatorService.getIndicator( Integer.parseInt( indicator ) ) );
        }
        
        mapLegendSet.setIndicators( indicatorSet );
        
        mappingService.updateMapLegendSet( mapLegendSet );
        
        return SUCCESS;
    }
}
