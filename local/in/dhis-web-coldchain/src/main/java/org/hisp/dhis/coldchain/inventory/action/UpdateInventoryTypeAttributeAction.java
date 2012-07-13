package org.hisp.dhis.coldchain.inventory.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOption;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOptionService;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeService;

import com.opensymphony.xwork2.Action;

public class UpdateInventoryTypeAttributeAction implements Action
{
    public static final String PREFIX_ATTRIBUTE_OPTION = "attrOption";

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
    private int id;

    public void setId( int id )
    {
        this.id = id;
    }

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
        InventoryTypeAttribute inventoryTypeAttribute = inventoryTypeAttributeService.getInventoryTypeAttribute( id );
        
        inventoryTypeAttribute.setName( name );
        inventoryTypeAttribute.setDescription( description );
        inventoryTypeAttribute.setMandatory( mandatory );
        inventoryTypeAttribute.setNoChars( noChars );
        inventoryTypeAttribute.setValueType( valueType );
        //inventoryTypeAttribute.setDisplay( display );

        HttpServletRequest request = ServletActionContext.getRequest();
        Collection<InventoryTypeAttributeOption> attributeOptions = inventoryTypeAttributeOptionService.get( inventoryTypeAttribute );
        
        if ( attributeOptions != null && attributeOptions.size() > 0 )
        {
            String value = null;
            for ( InventoryTypeAttributeOption option : attributeOptions )
            {
                value = request.getParameter( PREFIX_ATTRIBUTE_OPTION + option.getId() );
                if ( StringUtils.isNotBlank( value ) )
                {
                    option.setName( value.trim() );
                    inventoryTypeAttributeOptionService.updateInventoryTypeAttributeOption( option );
                   // inventoryTypeAttributeOptionService.updateInventoryTypeAttributeValues( option );
                }
            }
        }
        
        if ( attrOptions != null )
        {
            InventoryTypeAttributeOption opt = null;
            for ( String optionName : attrOptions )
            {
                opt = inventoryTypeAttributeOptionService.get( inventoryTypeAttribute, optionName );
                if ( opt == null )
                {
                    opt = new InventoryTypeAttributeOption();
                    opt.setName( optionName );
                    opt.setInventoryTypeAttribute( inventoryTypeAttribute );
                    inventoryTypeAttribute.addAttributeOptions( opt );
                    inventoryTypeAttributeOptionService.addInventoryTypeAttributeOption( opt );
                }
            }
        }
        
        inventoryTypeAttributeService.updateInventoryTypeAttribute( inventoryTypeAttribute );
        
        return SUCCESS;
    }
}
