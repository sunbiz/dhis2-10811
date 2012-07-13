package org.hisp.dhis.coldchain.equipment.action;

import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.EquipmentInstanceService;
import org.hisp.dhis.coldchain.inventory.EquipmentStatus;
import org.hisp.dhis.coldchain.inventory.EquipmentStatusService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork2.Action;

public class UpdateEquipmentStatusAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private EquipmentStatusService equipmentStatusService;
    
    private EquipmentInstanceService equipmentInstanceService;
    
    private CurrentUserService currentUserService;

    private I18nFormat format;
    
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
    
    private Integer equipmentInstanceId;
    
    private String reportingDate;
    
    private String dateOfUpdation;
    
    private String status;
    
    private String description;
    
    
    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        EquipmentInstance equipmentInstance = equipmentInstanceService.getEquipmentInstance( equipmentInstanceId );
        
        if( status.equalsIgnoreCase( "WORKING" ))
        {
            equipmentInstance.setWorking( true );
            equipmentInstanceService.updateEquipmentInstance( equipmentInstance );
        }
        else
        {
            equipmentInstance.setWorking( false );
            equipmentInstanceService.updateEquipmentInstance( equipmentInstance );
        }
        
        String storedBy = currentUserService.getCurrentUsername();
        
        EquipmentStatus equipmentStatus = new EquipmentStatus();
        
        equipmentStatus.setDescription( description );
        equipmentStatus.setEquipmentInstance( equipmentInstance );
        equipmentStatus.setStatus( status );
        equipmentStatus.setReportingDate( format.parseDate( reportingDate.trim() ) );
        equipmentStatus.setUpdationDate( format.parseDate( dateOfUpdation.trim() ) );
        equipmentStatus.setStoredBy( storedBy );
        
        equipmentStatusService.addEquipmentStatus( equipmentStatus );
        
        
        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Setters & Getters
    // -------------------------------------------------------------------------

    public void setEquipmentStatusService( EquipmentStatusService equipmentStatusService )
    {
        this.equipmentStatusService = equipmentStatusService;
    }


    public void setEquipmentInstanceService( EquipmentInstanceService equipmentInstanceService )
    {
        this.equipmentInstanceService = equipmentInstanceService;
    }


    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }


    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }


    public void setEquipmentInstanceId( Integer equipmentInstanceId )
    {
        this.equipmentInstanceId = equipmentInstanceId;
    }


    public void setReportingDate( String reportingDate )
    {
        this.reportingDate = reportingDate;
    }


    public void setDateOfUpdation( String dateOfUpdation )
    {
        this.dateOfUpdation = dateOfUpdation;
    }


    public void setStatus( String status )
    {
        this.status = status;
    }


    public void setDescription( String description )
    {
        this.description = description;
    }
        
}
