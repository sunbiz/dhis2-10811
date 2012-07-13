package org.hisp.dhis.coldchain.equipment.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.coldchain.inventory.Equipment;
import org.hisp.dhis.coldchain.inventory.EquipmentService;
import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.EquipmentInstanceService;
import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOption;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOptionService;
import org.hisp.dhis.coldchain.inventory.InventoryType_Attribute;

import com.opensymphony.xwork2.Action;

public class UpdateEquipmentAction implements Action
{

    public static final String PREFIX_ATTRIBUTE = "attr";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private InventoryTypeAttributeOptionService inventoryTypeAttributeOptionService;
    
    private EquipmentInstanceService equipmentInstanceService;

    private EquipmentService equipmentService;
    
    private CatalogService catalogService;
    
    // -------------------------------------------------------------------------
    // Input/ Output
    // -------------------------------------------------------------------------
    
    private Integer equipmentInstanceID;
    
    private String message;
    
    private Integer catalog;
    
    public void setCatalog( Integer catalog )
    {
        this.catalog = catalog;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    public String execute()
    {

        //System.out.println("inside UpdateEquipmentAction : "+ equipmentInstanceID);
        
        EquipmentInstance equipmentInstance = equipmentInstanceService.getEquipmentInstance( equipmentInstanceID );
        
        InventoryType inventoryType = equipmentInstance.getInventoryType();
        
        Catalog selCatalog = null;
        
        if( catalog != null )
        {    
            selCatalog = catalogService.getCatalog( catalog );
        }
        if( selCatalog != null )
        {
            equipmentInstance.setCatalog( selCatalog );
            
            equipmentInstanceService.updateEquipmentInstance( equipmentInstance );
        }
        
        // -----------------------------------------------------------------------------
        // Preparing Equipment Details
        // -----------------------------------------------------------------------------
        HttpServletRequest request = ServletActionContext.getRequest();
        String value = null;
        
        List<InventoryTypeAttribute> inventoryTypeAttributes = new ArrayList<InventoryTypeAttribute>( );
        
        for( InventoryType_Attribute inventoryType_Attribute : inventoryType.getInventoryType_Attributes() )
        {
            inventoryTypeAttributes.add( inventoryType_Attribute.getInventoryTypeAttribute() );
        }
        
        Equipment equipmentDetails = null;
        for ( InventoryTypeAttribute attribute : inventoryTypeAttributes )
        {
            value = request.getParameter( PREFIX_ATTRIBUTE + attribute.getId() );
            
            equipmentDetails = equipmentService.getEquipment( equipmentInstance, attribute );
            
            if( equipmentDetails == null && value != null )
            {
                equipmentDetails = new Equipment();
                equipmentDetails.setEquipmentInstance( equipmentInstance );
                equipmentDetails.setInventoryTypeAttribute( attribute );

                if ( InventoryTypeAttribute.TYPE_COMBO.equalsIgnoreCase( attribute.getValueType() ) )
                {
                    InventoryTypeAttributeOption option = inventoryTypeAttributeOptionService.getInventoryTypeAttributeOption( NumberUtils.toInt( value, 0 ) );
                    if ( option != null )
                    {
                        equipmentDetails.setInventoryTypeAttributeOption( option );
                        equipmentDetails.setValue( option.getName() );
                    }
                    else
                    {
                        // Someone deleted this option ...
                    }
                }
                else if ( InventoryTypeAttribute.TYPE_CATALOG.equalsIgnoreCase( attribute.getValueType() ) )
                {
                    Catalog catalog = catalogService.getCatalog( NumberUtils.toInt( value, 0 ) );
                    if ( catalog != null )
                    {
                        //equipmentDetails.setInventoryTypeAttributeOption( option );
                        equipmentDetails.setValue( catalog.getName() );
                    }
                    else
                    {
                        // Someone deleted this catalog ...
                    }
                }
                else
                {
                    equipmentDetails.setValue( value.trim() );
                }
                
                equipmentService.addEquipment( equipmentDetails );
            }
            else
            {
                if ( InventoryTypeAttribute.TYPE_COMBO.equalsIgnoreCase( attribute.getValueType() ) )
                {
                    InventoryTypeAttributeOption option = inventoryTypeAttributeOptionService.getInventoryTypeAttributeOption( NumberUtils.toInt( value, 0 ) );
                    if ( option != null )
                    {
                        equipmentDetails.setInventoryTypeAttributeOption( option );
                        equipmentDetails.setValue( option.getName() );
                    }
                    else
                    {
                        // Someone deleted this option ...
                    }
                }
                else if ( InventoryTypeAttribute.TYPE_CATALOG.equalsIgnoreCase( attribute.getValueType() ) )
                {
                    Catalog catalog = catalogService.getCatalog( NumberUtils.toInt( value, 0 ) );
                    if ( catalog != null )
                    {
                        //equipmentDetails.setInventoryTypeAttributeOption( option );
                        equipmentDetails.setValue( catalog.getName() );
                    }
                    else
                    {
                        // Someone deleted this catalog ...
                    }
                }
                else
                {
                    equipmentDetails.setValue( value.trim() );
                }

                equipmentService.updateEquipment( equipmentDetails );
            }
                
        }
         
        message = ""+ equipmentInstanceID;
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Setters & Getters
    // -------------------------------------------------------------------------

    public String getMessage()
    {
        return message;
    }

    public void setInventoryTypeAttributeOptionService(
        InventoryTypeAttributeOptionService inventoryTypeAttributeOptionService )
    {
        this.inventoryTypeAttributeOptionService = inventoryTypeAttributeOptionService;
    }

    public void setEquipmentInstanceService( EquipmentInstanceService equipmentInstanceService )
    {
        this.equipmentInstanceService = equipmentInstanceService;
    }

    public void setEquipmentService( EquipmentService equipmentService )
    {
        this.equipmentService = equipmentService;
    }

    public void setEquipmentInstanceID( Integer equipmentInstanceID )
    {
        this.equipmentInstanceID = equipmentInstanceID;
    }

    public void setCatalogService( CatalogService catalogService )
    {
        this.catalogService = catalogService;
    }

}
