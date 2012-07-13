package org.hisp.dhis.coldchain.inventory;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

public class DefaultInventoryTypeAttributeOptionService implements InventoryTypeAttributeOptionService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private InventoryTypeAttributeOptionStore inventoryTypeAttributeOptionStore;

    public void setInventoryTypeAttributeOptionStore( InventoryTypeAttributeOptionStore inventoryTypeAttributeOptionStore )
    {
        this.inventoryTypeAttributeOptionStore = inventoryTypeAttributeOptionStore;
    }

    // -------------------------------------------------------------------------
    // InventoryTypeAttributeOption
    // -------------------------------------------------------------------------
    @Transactional
    @Override
    public int addInventoryTypeAttributeOption( InventoryTypeAttributeOption inventoryTypeAttributeOption )
    {
        return inventoryTypeAttributeOptionStore.addInventoryTypeAttributeOption( inventoryTypeAttributeOption );
    }
    @Transactional
    @Override
    public void deleteInventoryTypeAttributeOption( InventoryTypeAttributeOption inventoryTypeAttributeOption )
    {
        inventoryTypeAttributeOptionStore.deleteInventoryTypeAttributeOption( inventoryTypeAttributeOption );
    }
    @Transactional
    @Override
    public Collection<InventoryTypeAttributeOption> getAllInventoryTypeAttributeOptions()
    {
        return inventoryTypeAttributeOptionStore.getAllInventoryTypeAttributeOptions();
    }
    @Transactional
    @Override
    public void updateInventoryTypeAttributeOption( InventoryTypeAttributeOption inventoryTypeAttributeOption )
    {
        inventoryTypeAttributeOptionStore.updateInventoryTypeAttributeOption( inventoryTypeAttributeOption );
    }
    
    public Collection<InventoryTypeAttributeOption> get( InventoryTypeAttribute inventoryTypeAttribute)
    {
        return inventoryTypeAttributeOptionStore.get( inventoryTypeAttribute );
    }
    
    public InventoryTypeAttributeOption get( InventoryTypeAttribute inventoryTypeAttribute, String name )
    {
        return inventoryTypeAttributeOptionStore.get( inventoryTypeAttribute, name );
    }
    
    public InventoryTypeAttributeOption getInventoryTypeAttributeOption( int id )
    {
        return inventoryTypeAttributeOptionStore.getInventoryTypeAttributeOption( id );
    }
}
