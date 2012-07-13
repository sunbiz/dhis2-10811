 package org.hisp.dhis.coldchain.inventory.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;

import com.opensymphony.xwork2.Action;

public class UpdateInventoryTypeDataSetAction  implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private InventoryTypeService inventoryTypeService;

    public void setInventoryTypeService( InventoryTypeService inventoryTypeService )
    {
        this.inventoryTypeService = inventoryTypeService;
    }
    
    private DataSetService dataSetService;
    
    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    
    // -------------------------------------------------------------------------
    // Input/Output
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

    
    private List<Integer> selectedInventoryTypeDataSetList = new ArrayList<Integer>();
    
    public void setSelectedInventoryTypeDataSetList( List<Integer> selectedInventoryTypeDataSetList )
    {
        this.selectedInventoryTypeDataSetList = selectedInventoryTypeDataSetList;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        
        InventoryType inventoryType = inventoryTypeService.getInventoryType( id );
        
        inventoryType.setName( name );
        
        
        Set<DataSet> inventoryTypeDataSet = new HashSet<DataSet>();
        
        if ( selectedInventoryTypeDataSetList != null && selectedInventoryTypeDataSetList.size() > 0 )
        {
            for ( int i = 0; i < this.selectedInventoryTypeDataSetList.size(); i++ )
            {
                DataSet dataSet = dataSetService.getDataSet( selectedInventoryTypeDataSetList.get( i ) );
                
                /*
                System.out.println( "ID---" + dataSet.getId() );
                System.out.println( "Name---" + dataSet.getName());
                System.out.println( "Display Name---" + dataSet.getDisplayName() );
                */
                inventoryTypeDataSet.add( dataSet );
                
            }
        }
        
        inventoryType.setDataSets( inventoryTypeDataSet );
        
        inventoryTypeService.updateInventoryType( inventoryType );
        
        
        return SUCCESS;
    }
}

