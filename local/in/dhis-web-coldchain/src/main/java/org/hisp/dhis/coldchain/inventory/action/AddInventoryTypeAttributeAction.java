package org.hisp.dhis.coldchain.inventory.action;

import java.util.List;

import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOption;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOptionService;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeService;

import com.opensymphony.xwork2.Action;

public class AddInventoryTypeAttributeAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private InventoryTypeAttributeService inventoryTypeAttributeService;

    public void setInventoryTypeAttributeService( InventoryTypeAttributeService inventoryTypeAttributeService )
    {
        this.inventoryTypeAttributeService = inventoryTypeAttributeService;
    }

    private InventoryTypeAttributeOptionService inventoryTypeAttributeOptionService;

    public void setInventoryTypeAttributeOptionService(
        InventoryTypeAttributeOptionService inventoryTypeAttributeOptionService )
    {
        this.inventoryTypeAttributeOptionService = inventoryTypeAttributeOptionService;
    }
    
    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }

    private String valueType;

    public void setValueType( String valueType )
    {
        this.valueType = valueType;
    }

    private boolean mandatory;

    public void setMandatory( boolean mandatory )
    {
        this.mandatory = mandatory;
    }

    private List<String> attrOptions;

    public void setAttrOptions( List<String> attrOptions )
    {
        this.attrOptions = attrOptions;
    }

    private Integer noChars;

    public void setNoChars( Integer noChars )
    {
        this.noChars = noChars;
    }
    
    private boolean display;
    
    public void setDisplay( boolean display )
    {
        this.display = display;
    }




    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        System.out.println("Inside AddInventoryTypeAttributeAction");
        
        InventoryTypeAttribute inventoryTypeAttribute = new InventoryTypeAttribute();
        
        inventoryTypeAttribute.setName( name );
        inventoryTypeAttribute.setDescription( description );
        inventoryTypeAttribute.setMandatory( mandatory );
        inventoryTypeAttribute.setNoChars( noChars );
        inventoryTypeAttribute.setValueType( valueType );
        //inventoryTypeAttribute.setDisplay( display );
        
        inventoryTypeAttributeService.addInventoryTypeAttribute( inventoryTypeAttribute );
        
        if ( InventoryTypeAttribute.TYPE_COMBO.equalsIgnoreCase( valueType ) )
        {
            InventoryTypeAttributeOption opt = null;
            for ( String optionName : attrOptions )
            {
                opt = new InventoryTypeAttributeOption();
                opt.setName( optionName );
                opt.setInventoryTypeAttribute( inventoryTypeAttribute );
                inventoryTypeAttribute.addAttributeOptions( opt );
                inventoryTypeAttributeOptionService.addInventoryTypeAttributeOption( opt );
            }
        }
        
        return SUCCESS;
    }
}
