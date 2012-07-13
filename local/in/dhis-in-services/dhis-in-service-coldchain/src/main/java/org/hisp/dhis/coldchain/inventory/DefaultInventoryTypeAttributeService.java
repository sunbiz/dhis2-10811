package org.hisp.dhis.coldchain.inventory;

import static org.hisp.dhis.i18n.I18nUtils.getCountByName;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetween;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetweenByName;

import java.util.Collection;

import org.hisp.dhis.i18n.I18nService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultInventoryTypeAttributeService implements InventoryTypeAttributeService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private InventoryTypeAttributeStore inventoryTypeAttributeStore;

    public void setInventoryTypeAttributeStore( InventoryTypeAttributeStore inventoryTypeAttributeStore )
    {
        this.inventoryTypeAttributeStore = inventoryTypeAttributeStore;
    }
    
    private I18nService i18nService;

    public void setI18nService( I18nService service )
    {
        i18nService = service;
    }

    // -------------------------------------------------------------------------
    // InventoryTypeAttribute
    // -------------------------------------------------------------------------
    /*
    @Transactional
    @Override
    public int addInventoryTypeAttribute( InventoryTypeAttribute inventoryTypeAttribute )
    {
        return inventoryTypeAttributeStore.addInventoryTypeAttribute( inventoryTypeAttribute );
    }
    
    @Transactional
    @Override
    public void deleteInventoryTypeAttribute( InventoryTypeAttribute inventoryTypeAttribute )
    {
        inventoryTypeAttributeStore.deleteInventoryTypeAttribute( inventoryTypeAttribute );
    }
    
    @Transactional
    @Override
    public Collection<InventoryTypeAttribute> getAllInventoryTypeAttributes()
    {
        return inventoryTypeAttributeStore.getAllInventoryTypeAttributes();
    }
    
    @Transactional
    @Override
    public void updateInventoryTypeAttribute( InventoryTypeAttribute inventoryTypeAttribute )
    {
        inventoryTypeAttributeStore.updateInventoryTypeAttribute( inventoryTypeAttribute );
    }
    
    public InventoryTypeAttribute getInventoryTypeAttribute( int id )
    {
        return inventoryTypeAttributeStore.getInventoryTypeAttribute( id );
    }
    
    public  InventoryTypeAttribute getInventoryTypeAttributeByName( String name )
    {
        return inventoryTypeAttributeStore.getInventoryTypeAttributeByName( name );
    }
    */
    

    @Override
    public int addInventoryTypeAttribute( InventoryTypeAttribute inventoryTypeAttribute )
    {
        return inventoryTypeAttributeStore.save( inventoryTypeAttribute );
    }
    
    @Override
    public void deleteInventoryTypeAttribute( InventoryTypeAttribute inventoryTypeAttribute )
    {
        inventoryTypeAttributeStore.delete( inventoryTypeAttribute );
    }
    
    @Override
    public Collection<InventoryTypeAttribute> getAllInventoryTypeAttributes()
    {
        return inventoryTypeAttributeStore.getAllInventoryTypeAttributes();
    }
    
    @Override
    public void updateInventoryTypeAttribute( InventoryTypeAttribute inventoryTypeAttribute )
    {
        inventoryTypeAttributeStore.update( inventoryTypeAttribute );
    }
    
    public InventoryTypeAttribute getInventoryTypeAttribute( int id )
    {
        return inventoryTypeAttributeStore.getInventoryTypeAttribute( id );
    }
    
    public  InventoryTypeAttribute getInventoryTypeAttributeByName( String name )
    {
        return inventoryTypeAttributeStore.getInventoryTypeAttributeByName( name );
    }

    
    //Methods
    public int getInventoryTypeAttributeCount()
    {
        return inventoryTypeAttributeStore.getCount();
    }
    
    public int getInventoryTypeAttributeCountByName( String name )
    {
        return getCountByName( i18nService, inventoryTypeAttributeStore, name );
    }

    public Collection<InventoryTypeAttribute> getInventoryTypeAttributesBetween( int first, int max )
    {
        return getObjectsBetween( i18nService, inventoryTypeAttributeStore, first, max );
    }
    
    public Collection<InventoryTypeAttribute> getInventoryTypeAttributesBetweenByName( String name, int first, int max )
    {
        return getObjectsBetweenByName( i18nService, inventoryTypeAttributeStore, name, first, max );
    }
    /*
    public Collection<InventoryTypeAttribute> getAllInventoryTypeAttributesForDisplay( InventoryTypeAttribute inventoryTypeAttribute )
    {
        return inventoryTypeAttributeStore.getAllInventoryTypeAttributesForDisplay( inventoryTypeAttribute );
    }
    */
}
