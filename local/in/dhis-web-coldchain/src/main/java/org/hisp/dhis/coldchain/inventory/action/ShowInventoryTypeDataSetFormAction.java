package org.hisp.dhis.coldchain.inventory.action;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;
import org.hisp.dhis.dataset.DataSet;

import com.opensymphony.xwork2.Action;

public class ShowInventoryTypeDataSetFormAction implements Action
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private InventoryTypeService inventoryTypeService;

    public void setInventoryTypeService( InventoryTypeService inventoryTypeService )
    {
        this.inventoryTypeService = inventoryTypeService;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------
    private String id;

    public void setId( String id )
    {
        this.id = id;
    }
    
    private InventoryType inventoryType;
    
    public InventoryType getInventoryType()
    {
        return inventoryType;
    }
    
    private List<DataSet> selInventoryTypeDataSets;
    
    public List<DataSet> getSelInventoryTypeDataSets()
    {
        return selInventoryTypeDataSets;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        inventoryType = inventoryTypeService.getInventoryType( Integer.parseInt( id ) );
        
        selInventoryTypeDataSets = new ArrayList<DataSet>(  inventoryType.getDataSets() );
        
        return SUCCESS;        
    }
}
