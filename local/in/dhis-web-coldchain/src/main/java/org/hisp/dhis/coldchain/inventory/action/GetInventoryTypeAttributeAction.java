package org.hisp.dhis.coldchain.inventory.action;

import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeService;

import com.opensymphony.xwork2.Action;

public class GetInventoryTypeAttributeAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------
    private InventoryTypeAttributeService inventoryTypeAttributeService;

    public void setInventoryTypeAttributeService( InventoryTypeAttributeService inventoryTypeAttributeService )
    {
        this.inventoryTypeAttributeService = inventoryTypeAttributeService;
    }
    
    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private int id;

    public void setId( int id )
    {
        this.id = id;
    }

    private InventoryTypeAttribute inventoryTypeAttribute;

    public InventoryTypeAttribute getInventoryTypeAttribute()
    {
        return inventoryTypeAttribute;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        //System.out.println("inside GetInventoryTypeAttributeAction");
        
        inventoryTypeAttribute = inventoryTypeAttributeService.getInventoryTypeAttribute( id );
        
        return SUCCESS;
    }

}
