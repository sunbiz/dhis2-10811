package org.hisp.dhis.coldchain.equipment.action;

import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.EquipmentInstanceService;
import org.hisp.dhis.common.DeleteNotAllowedException;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork2.Action;

public class RemoveEquipmentInstanceAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private EquipmentInstanceService equipmentInstanceService;
    
    public void setEquipmentInstanceService( EquipmentInstanceService equipmentInstanceService )
    {
        this.equipmentInstanceService = equipmentInstanceService;
    }
    
    // -------------------------------------------------------------------------
    // Input/Output
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
    // Action Implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        try
        {
            EquipmentInstance equipmentInstance = equipmentInstanceService.getEquipmentInstance( id );
            
            equipmentInstanceService.deleteCompleteEquipmentInstance( equipmentInstance );
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
