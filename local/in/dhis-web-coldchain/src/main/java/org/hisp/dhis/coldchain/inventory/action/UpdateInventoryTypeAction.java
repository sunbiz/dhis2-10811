package org.hisp.dhis.coldchain.inventory.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeService;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;
import org.hisp.dhis.coldchain.inventory.InventoryType_Attribute;
import org.hisp.dhis.coldchain.inventory.InventoryType_AttributeService;

import com.opensymphony.xwork2.Action;

public class UpdateInventoryTypeAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------
    private InventoryTypeService inventoryTypeService;

    public void setInventoryTypeService( InventoryTypeService inventoryTypeService )
    {
        this.inventoryTypeService = inventoryTypeService;
    }
    
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
    
    private InventoryType_AttributeService inventoryType_AttributeService;
    
    public void setInventoryType_AttributeService( InventoryType_AttributeService inventoryType_AttributeService )
    {
        this.inventoryType_AttributeService = inventoryType_AttributeService;
    }
    
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
    private Integer id;
    
    public void setId( Integer id )
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

    private Integer catalogType;

    public void setCatalogType( Integer catalogType )
    {
        this.catalogType = catalogType;
    }

    private boolean tracking;

    public void setTracking( boolean tracking )
    {
        this.tracking = tracking;
    }
    /*
    private List<Integer> selectedInventoryTypeAttributeList;
    
    public void setSelectedInventoryTypeAttributeList( List<Integer> selectedInventoryTypeAttributeList )
    {
        this.selectedInventoryTypeAttributeList = selectedInventoryTypeAttributeList;
    }
    */
    
    private List<Integer> selectedInventoryTypeAttributeValidator = new ArrayList<Integer>();
    
    public void setSelectedInventoryTypeAttributeValidator( List<Integer> selectedInventoryTypeAttributeValidator )
    {
        this.selectedInventoryTypeAttributeValidator = selectedInventoryTypeAttributeValidator;
    }
    
    private List<Boolean> display = new ArrayList<Boolean>();
    
    public void setDisplay( List<Boolean> display )
    {
        this.display = display;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        InventoryType inventoryType = inventoryTypeService.getInventoryType( id );
        
        inventoryType.setName( name );
        inventoryType.setDescription( description );
        inventoryType.setTracking( tracking );
        
        if( catalogType != null )
        {
            inventoryType.setCatalogType( catalogTypeService.getCatalogType( catalogType ) );
        }
        
        /*
        if( inventoryType != null )
        {
            inventoryType.getInventoryTypeAttributes().clear();
        }
        */
        
        //Set<InventoryTypeAttribute> inventoryTypeSet = new HashSet<InventoryTypeAttribute>();
        
        List<InventoryTypeAttribute> inventoryTypeList = new ArrayList<InventoryTypeAttribute>( );
        
        if ( selectedInventoryTypeAttributeValidator != null && selectedInventoryTypeAttributeValidator.size() > 0 )
        {
           
            for ( int i = 0; i < this.selectedInventoryTypeAttributeValidator.size(); i++ )
            {
                InventoryTypeAttribute inventoryTypeAttribute = inventoryTypeAttributeService.getInventoryTypeAttribute( selectedInventoryTypeAttributeValidator.get( i ) );
                inventoryTypeList.add( inventoryTypeAttribute );
            }
        }
        
        //inventoryType.setInventoryTypeAttributes( inventoryTypeList );
        inventoryTypeService.updateInventoryType( inventoryType );
        
        // for InventoryType_Attribute
        
        Set<InventoryType_Attribute> inventoryType_Attributes = new HashSet<InventoryType_Attribute>( inventoryType.getInventoryType_Attributes());

        for ( int i = 0; i < this.selectedInventoryTypeAttributeValidator.size(); i++ )
        {
            InventoryTypeAttribute inventoryTypeAttribute = inventoryTypeAttributeService.getInventoryTypeAttribute( selectedInventoryTypeAttributeValidator.get( i ) );
            
            InventoryType_Attribute inventoryType_Attribute = inventoryType_AttributeService.getInventoryTypeAttribute( inventoryType, inventoryTypeAttribute );
            
            if ( inventoryType_Attribute == null )
            {
                inventoryType_Attribute = new InventoryType_Attribute( inventoryType,  inventoryTypeAttribute, this.display.get( i ), new Integer( i ) );
                inventoryType_AttributeService.addInventoryType_Attribute( inventoryType_Attribute );
            }
            else
            {
                inventoryType_Attribute.setDisplay( this.display.get( i ) );

                inventoryType_Attribute.setSortOrder( new Integer( i ) );
                
                inventoryType_AttributeService.updateInventoryType_Attribute( inventoryType_Attribute );

                inventoryType_Attributes.remove( inventoryType_Attribute );
            }
        }

        for ( InventoryType_Attribute inventoryType_AttributeDelete : inventoryType_Attributes )
        {
            inventoryType_AttributeService.deleteInventoryType_Attribute( inventoryType_AttributeDelete );
        }
        
        return SUCCESS;
    }
}
