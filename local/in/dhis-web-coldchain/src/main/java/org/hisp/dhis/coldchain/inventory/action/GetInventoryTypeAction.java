package org.hisp.dhis.coldchain.inventory.action;

import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;

import com.opensymphony.xwork2.Action;

public class GetInventoryTypeAction implements Action
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
    private int id;

    public void setId( int id )
    {
        this.id = id;
    }

    private InventoryType inventoryType;

    public InventoryType getInventoryType()
    {
        return inventoryType;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        inventoryType = inventoryTypeService.getInventoryType( id );
        
        return SUCCESS;
    }
}
