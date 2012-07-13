package org.hisp.dhis.coldchain.inventory;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;


/**
 * @author Mithilesh Kumar Thakur
 *
 * @version DefaultInventoryType_AttributeService.java Jun 14, 2012 3:19:02 PM	
 */

public class DefaultInventoryType_AttributeService implements InventoryType_AttributeService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private InventoryType_AttributeStore inventoryType_AttributeStore;

    public void setInventoryType_AttributeStore( InventoryType_AttributeStore inventoryType_AttributeStore )
    {
        this.inventoryType_AttributeStore = inventoryType_AttributeStore;
    }
    
    
    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------
    @Transactional
    @Override
    public void addInventoryType_Attribute( InventoryType_Attribute inventoryType_Attribute )
    {
        inventoryType_AttributeStore.addInventoryType_Attribute( inventoryType_Attribute );
    }
    
    @Transactional
    @Override
    public void deleteInventoryType_Attribute( InventoryType_Attribute inventoryType_Attribute )
    {
        inventoryType_AttributeStore.deleteInventoryType_Attribute( inventoryType_Attribute );
    }
    
    @Transactional
    @Override
    public void updateInventoryType_Attribute( InventoryType_Attribute inventoryType_Attribute )
    {
        inventoryType_AttributeStore.updateInventoryType_Attribute( inventoryType_Attribute );
    }
    
    @Transactional
    @Override
    public Collection<InventoryType_Attribute> getAllInventoryTypeAttributes()
    {
        return inventoryType_AttributeStore.getAllInventoryTypeAttributes();
    }
    
    @Transactional
    @Override
    public InventoryType_Attribute getInventoryTypeAttribute( InventoryType inventoryType, InventoryTypeAttribute inventoryTypeAttribute )
    {
        return inventoryType_AttributeStore.getInventoryTypeAttribute( inventoryType, inventoryTypeAttribute );
    }
    
    @Transactional
    @Override
    public Collection<InventoryType_Attribute> getAllInventoryTypeAttributesByInventoryType( InventoryType inventoryType )
    {
        return inventoryType_AttributeStore.getAllInventoryTypeAttributesByInventoryType( inventoryType );
    }
    
    @Transactional
    @Override
    public Collection<InventoryTypeAttribute> getListInventoryTypeAttribute( InventoryType inventoryType )
    {
        return inventoryType_AttributeStore.getListInventoryTypeAttribute( inventoryType );
    }

    @Transactional
    @Override
    public InventoryType_Attribute getInventoryTypeAttributeForDisplay( InventoryType inventoryType, InventoryTypeAttribute inventoryTypeAttribute, boolean display)
    {
        return inventoryType_AttributeStore.getInventoryTypeAttributeForDisplay( inventoryType, inventoryTypeAttribute, display );
    }
    
    @Transactional
    @Override
    public Collection<InventoryType_Attribute> getAllInventoryTypeAttributeForDisplay( InventoryType inventoryType, boolean display )
    {
        return inventoryType_AttributeStore.getAllInventoryTypeAttributeForDisplay( inventoryType, display );
    }   
    
}
