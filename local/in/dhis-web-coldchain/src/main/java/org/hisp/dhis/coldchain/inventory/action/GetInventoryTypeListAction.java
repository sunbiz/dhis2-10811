package org.hisp.dhis.coldchain.inventory.action;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;
import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.paging.ActionPagingSupport;

public class GetInventoryTypeListAction extends ActionPagingSupport<InventoryType>
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------
    private InventoryTypeService inventoryTypeService;

    public void setInventoryTypeService( InventoryTypeService inventoryTypeService )
    {
        this.inventoryTypeService = inventoryTypeService;
    }
    
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
    private List<InventoryType> inventoryTypes;

    public List<InventoryType> getInventoryTypes()
    {
        return inventoryTypes;
    }
    
    private String key;

    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        
        if ( isNotBlank( key ) ) // Filter on key only if set
        {
            this.paging = createPaging( inventoryTypeService.getInventoryTypeCountByName( key ) );
            
            inventoryTypes = new ArrayList<InventoryType>( inventoryTypeService.getInventoryTypesBetweenByName( key, paging.getStartPos(), paging.getPageSize()) );
        }
        else
        {
            this.paging = createPaging( inventoryTypeService.getInventoryTypeCount() );
            
            inventoryTypes = new ArrayList<InventoryType>( inventoryTypeService.getInventoryTypesBetween(paging.getStartPos(), paging.getPageSize()) );
        }
        
        Collections.sort( inventoryTypes, new IdentifiableObjectNameComparator() );
        
        /*
        inventoryTypes = new ArrayList<InventoryType>( inventoryTypeService.getAllInventoryTypes() );
        
        Collections.sort( inventoryTypes, new InventoryTypeComparator() );
        */
        /**
         * TODO - need to write comparator for sorting the list
         */
        
        return SUCCESS;
    }

}
