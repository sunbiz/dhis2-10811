package org.hisp.dhis.coldchain.inventory.action;

import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;
import org.hisp.dhis.common.DeleteNotAllowedException;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork2.Action;

public class RemoveInventoryTypeAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------
    private InventoryTypeService inventoryTypeService;

    public void setInventoryTypeService( InventoryTypeService inventoryTypeService )
    {
        this.inventoryTypeService = inventoryTypeService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
    private int id;

    public void setId( int id )
    {
        this.id = id;
    }
    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    private String message;

    public String getMessage()
    {
        return message;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        try
        {
            InventoryType inventoryType = inventoryTypeService.getInventoryType( id );
            
            inventoryTypeService.deleteInventoryType( inventoryType );
        }
        catch ( DeleteNotAllowedException ex )
        {
            if ( ex.getErrorCode().equals( DeleteNotAllowedException.ERROR_ASSOCIATED_BY_OTHER_OBJECTS ) )
            {
                message = i18n.getString( "object_not_deleted_associated_by_objects" ) + " " + ex.getMessage();
            }
            
            return ERROR;
        }
        
        return SUCCESS;
    }
}
