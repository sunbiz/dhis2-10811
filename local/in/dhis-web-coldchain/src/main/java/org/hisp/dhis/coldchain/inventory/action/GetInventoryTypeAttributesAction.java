package org.hisp.dhis.coldchain.inventory.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeService;
import org.hisp.dhis.coldchain.inventory.comparator.InventoryTypeAttributeComparator;

import com.opensymphony.xwork2.Action;

public class GetInventoryTypeAttributesAction implements Action
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
    // Input & Output
    // -------------------------------------------------------------------------
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
        inventoryTypeAttributes = new ArrayList<InventoryTypeAttribute>( inventoryTypeAttributeService.getAllInventoryTypeAttributes() );
        Collections.sort( inventoryTypeAttributes, new InventoryTypeAttributeComparator() );
        
        /*
        for( InventoryTypeAttribute inventoryTypeAttribute : inventoryTypeAttributes )
        {
            System.out.println( "ID---" + inventoryTypeAttribute.getId() );
            System.out.println( "Name---" + inventoryTypeAttribute.getName());
            System.out.println( "Discription---" + inventoryTypeAttribute.getDescription() );
            System.out.println( "ValueType---" + inventoryTypeAttribute.getValueType() );
        }
        */
        return SUCCESS;
    }

}

