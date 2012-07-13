package org.hisp.dhis.coldchain.inventory.action;

import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOption;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOptionService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork2.Action;

public class RemoveInventoryTypeAttributeOptionAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

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

    private String message;

    public String getMessage()
    {
        return message;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        
        InventoryTypeAttributeOption attributeOption = inventoryTypeAttributeOptionService.getInventoryTypeAttributeOption( id );

        if ( attributeOption != null )
        {
            //int count = equipment.countByInventoryTypeAttributeOption( attributeOption );
            //if ( count > 0 )
            //{
            //    message = i18n.getString( "warning_delete_patient_attribute_option" );
            //    return INPUT;
            //}
            //else
            {
                inventoryTypeAttributeOptionService.deleteInventoryTypeAttributeOption( attributeOption );
                message = i18n.getString( "success_delete_inventorytype_attribute_option" );
                return SUCCESS;
            }
        }
        else
        {
            message = i18n.getString( "error_delete_inventorytype_attribute_option" );
            return ERROR;
        }
    }

}
