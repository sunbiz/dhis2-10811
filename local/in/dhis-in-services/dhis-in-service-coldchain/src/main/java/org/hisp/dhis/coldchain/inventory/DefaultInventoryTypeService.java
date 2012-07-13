package org.hisp.dhis.coldchain.inventory;

import static org.hisp.dhis.i18n.I18nUtils.getCountByName;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetween;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetweenByName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hisp.dhis.i18n.I18nService;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public class DefaultInventoryTypeService implements InventoryTypeService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private InventoryTypeStore inventoryTypeStore;

    public void setInventoryTypeStore( InventoryTypeStore inventoryTypeStore )
    {
        this.inventoryTypeStore = inventoryTypeStore;
    }
    
    private I18nService i18nService;

    public void setI18nService( I18nService service )
    {
        i18nService = service;
    }
    
    // -------------------------------------------------------------------------
    // InventoryType
    // -------------------------------------------------------------------------
    /*
    @Transactional
    @Override
    public int addInventoryType( InventoryType inventoryType )
    {
        return inventoryTypeStore.addInventoryType( inventoryType );
    }

    @Transactional
    @Override
    public void deleteInventoryType( InventoryType inventoryType )
    {
        inventoryTypeStore.deleteInventoryType( inventoryType );
    }

    @Transactional
    @Override
    public Collection<InventoryType> getAllInventoryTypes()
    {
        return inventoryTypeStore.getAllInventoryTypes();
    }

    @Transactional
    @Override
    public void updateInventoryType( InventoryType inventoryType )
    {
        inventoryTypeStore.updateInventoryType( inventoryType );
    }
    
    public InventoryType getInventoryTypeByName( String name )
    {
        return inventoryTypeStore.getInventoryTypeByName( name );
    }
    
    public InventoryType getInventoryType( int id )
    {
        return inventoryTypeStore.getInventoryType( id );
    }
    */
    
 
    @Override
    public int addInventoryType( InventoryType inventoryType )
    {
        return inventoryTypeStore.save( inventoryType );
    }
   
    @Override
    public void deleteInventoryType( InventoryType inventoryType )
    {
        inventoryTypeStore.delete( inventoryType );
    }
    
    @Override
    public void updateInventoryType( InventoryType inventoryType )
    {
        inventoryTypeStore.update( inventoryType );
    }
    
    @Override
    public Collection<InventoryType> getAllInventoryTypes()
    {
        return inventoryTypeStore.getAllInventoryTypes();
    }
    
    @Override
    public InventoryType getInventoryTypeByName( String name )
    {
        return inventoryTypeStore.getInventoryTypeByName( name );
    }
    
    @Override
    public InventoryType getInventoryType( int id )
    {
        return inventoryTypeStore.getInventoryType( id );
    }
    
    //Methods
    public int getInventoryTypeCount()
    {
        return inventoryTypeStore.getCount();
    }
    
    public int getInventoryTypeCountByName( String name )
    {
        return getCountByName( i18nService, inventoryTypeStore, name );
    }
    
    public Collection<InventoryType> getInventoryTypesBetween( int first, int max )
    {
        return getObjectsBetween( i18nService, inventoryTypeStore, first, max );
    }
    
    public Collection<InventoryType> getInventoryTypesBetweenByName( String name, int first, int max )
    {
        return getObjectsBetweenByName( i18nService, inventoryTypeStore, name, first, max );
    } 
    /*
    public Collection<InventoryTypeAttribute> getAllInventoryTypeAttributesForDisplay( InventoryType inventoryType )
    {
        return inventoryTypeStore.getAllInventoryTypeAttributesForDisplay( inventoryType );
    }
    */
    public Collection<InventoryTypeAttribute> getAllInventoryTypeAttributesForDisplay( InventoryType inventoryType )
    {
        List<InventoryTypeAttribute> inventoryTypeAttributeList = new ArrayList<InventoryTypeAttribute>();
       
        List<InventoryType_Attribute> inventoryType_AttributeList = new ArrayList<InventoryType_Attribute>( inventoryType.getInventoryType_Attributes() );
        for ( InventoryType_Attribute inventoryType_Attribute : inventoryType_AttributeList )
        {
            
            if ( inventoryType_Attribute.isDisplay() )
            {
                inventoryTypeAttributeList.add( inventoryType_Attribute.getInventoryTypeAttribute() );
            }
        }

        return inventoryTypeAttributeList;
    }
    
    
    
}
