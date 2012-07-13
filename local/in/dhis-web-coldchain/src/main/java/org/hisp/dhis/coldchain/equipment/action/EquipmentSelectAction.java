package org.hisp.dhis.coldchain.equipment.action;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;

import com.opensymphony.xwork2.Action;

public class EquipmentSelectAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitSelectionManager selectionManager;

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    private InventoryTypeService inventoryTypeService;

    public void setInventoryTypeService( InventoryTypeService inventoryTypeService )
    {
        this.inventoryTypeService = inventoryTypeService;
    }
    
    private OrganisationUnitService organisationUnitService;
    
    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private OrganisationUnit organisationUnit;

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    private List<InventoryType> inventoryTypes;

    public List<InventoryType> getInventoryTypes()
    {
        return inventoryTypes;
    }
    
    private Integer orgUnitId;
    
    public void setOrgUnitId( Integer orgUnitId )
    {
        this.orgUnitId = orgUnitId;
    }
    
    // -------------------------------------------------------------------------
    // Implementation Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        
        organisationUnit = selectionManager.getSelectedOrganisationUnit();
        
        if( organisationUnit == null )
        {
            System.out.println("Organisationunit is null");
        }
        else
        {
            System.out.println("Organisationunit is not null ---" + orgUnitId);
        }
        
        if( organisationUnit == null && orgUnitId != null )
        {
            organisationUnit = organisationUnitService.getOrganisationUnit( orgUnitId );
        }
        
        inventoryTypes = new ArrayList<InventoryType>( inventoryTypeService.getAllInventoryTypes() );
        
        return SUCCESS;
    }

}
