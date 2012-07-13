package org.hisp.dhis.coldchain.inventory.action;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeService;

import com.opensymphony.xwork2.Action;

public class ShowAddInventoryTypeAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------
    private CatalogTypeService catalogTypeService;

    public void setCatalogTypeService( CatalogTypeService catalogTypeService )
    {
        this.catalogTypeService = catalogTypeService;
    }
    
    private InventoryTypeAttributeService inventoryTypeAttributeService;
    
    public void setInventoryTypeAttributeService( InventoryTypeAttributeService inventoryTypeAttributeService )
    {
        this.inventoryTypeAttributeService = inventoryTypeAttributeService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
    private List<CatalogType> catalogTypes;

    public List<CatalogType> getCatalogTypes()
    {
        return catalogTypes;
    }
    
    private List<InventoryTypeAttribute> inventoryTypeAttributes;
    
    public List<InventoryTypeAttribute> getInventoryTypeAttributes()
    {
        return inventoryTypeAttributes;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        catalogTypes =  new ArrayList<CatalogType>( catalogTypeService.getAllCatalogTypes() );
        
        inventoryTypeAttributes = new ArrayList<InventoryTypeAttribute>( inventoryTypeAttributeService.getAllInventoryTypeAttributes() );
        
        return SUCCESS;
    }

}
