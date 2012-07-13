package org.hisp.dhis.coldchain.equipment.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.coldchain.inventory.Equipment;
import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.EquipmentInstanceService;
import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOption;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOptionService;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;
import org.hisp.dhis.coldchain.inventory.InventoryType_Attribute;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

import com.opensymphony.xwork2.Action;

public class AddEquipmentAction implements Action
{
    public static final String PREFIX_ATTRIBUTE = "attr";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;
    
    private InventoryTypeService inventoryTypeService;
    
    private InventoryTypeAttributeOptionService inventoryTypeAttributeOptionService;
    
    private EquipmentInstanceService equipmentInstanceService;
    
    private CatalogService catalogService;
    
    // -------------------------------------------------------------------------
    // Input/ Output
    // -------------------------------------------------------------------------
    
    private Integer ouId;
    
    private Integer itypeId;
    
    private String message;
    
    private Boolean workingStatus = false;
    
    public void setWorkingStatus( Boolean workingStatus )
    {
        this.workingStatus = workingStatus;
    }

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

        OrganisationUnit orgUnit = organisationUnitService.getOrganisationUnit( ouId );
        
        InventoryType inventoryType = inventoryTypeService.getInventoryType( itypeId );
        
        Catalog selCatalog = null;
        
        if( catalog != null )
        {    
            selCatalog = catalogService.getCatalog( catalog );
        }
        
        // -----------------------------------------------------------------------------
        // Preparing EquipmentInstance
        // -----------------------------------------------------------------------------
        EquipmentInstance equipmentInstance = new EquipmentInstance();
        
        equipmentInstance.setInventoryType( inventoryType );
        equipmentInstance.setOrganisationUnit( orgUnit );
        equipmentInstance.setWorking( workingStatus );
        if( selCatalog != null )
        {
            equipmentInstance.setCatalog( selCatalog );
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
        
        List<Equipment> equipmentDeatilsList = new ArrayList<Equipment>();
        
        Equipment equipmentDetails = null;
        for ( InventoryTypeAttribute attribute : inventoryTypeAttributes )
        {
            value = request.getParameter( PREFIX_ATTRIBUTE + attribute.getId() );
            if ( StringUtils.isNotBlank( value ) )
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
                equipmentDeatilsList.add( equipmentDetails );
            }
        }
        
        // -----------------------------------------------------------------------------
        // Creating Equipment Instance and saving equipment data
        // -----------------------------------------------------------------------------
        Integer id = equipmentInstanceService.createEquipment( equipmentInstance, equipmentDeatilsList );

        message = id + "";
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Setters and Getters
    // -------------------------------------------------------------------------

    public String getMessage()
    {
        return message;
    }

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    public void setInventoryTypeService( InventoryTypeService inventoryTypeService )
    {
        this.inventoryTypeService = inventoryTypeService;
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

    public void setCatalogService( CatalogService catalogService )
    {
        this.catalogService = catalogService;
    }

    public void setOuId( Integer ouId )
    {
        this.ouId = ouId;
    }

    public void setItypeId( Integer itypeId )
    {
        this.itypeId = itypeId;
    }
}
