package org.hisp.dhis.coldchain.equipment.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.coldchain.inventory.EquipmentDataValue;
import org.hisp.dhis.coldchain.inventory.EquipmentDataValueService;
import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.EquipmentInstanceService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;

import com.opensymphony.xwork2.Action;

public class LoadDataEntryFormAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private EquipmentInstanceService equipmentInstanceService;
    
    public void setEquipmentInstanceService( EquipmentInstanceService equipmentInstanceService )
    {
        this.equipmentInstanceService = equipmentInstanceService;
    }
    
    private DataSetService dataSetService;
    
    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private EquipmentDataValueService equipmentDataValueService;
    
    public void setEquipmentDataValueService( EquipmentDataValueService equipmentDataValueService )
    {
        this.equipmentDataValueService = equipmentDataValueService;
    }
    // -------------------------------------------------------------------------
    // Comparator
    // -------------------------------------------------------------------------

    private Comparator<DataElement> dataElementComparator;

    public void setDataElementComparator( Comparator<DataElement> dataElementComparator )
    {
        this.dataElementComparator = dataElementComparator;
    }
    

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------
    
    private int dataSetId;
    
    public void setDataSetId( int dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    private int equipmentInstanceId;
    
    public void setEquipmentInstanceId( int equipmentInstanceId )
    {
        this.equipmentInstanceId = equipmentInstanceId;
    }
    
    private String selectedPeriodId;
    
    public void setSelectedPeriodId( String selectedPeriodId )
    {
        this.selectedPeriodId = selectedPeriodId;
    }

    private List<DataElement> dataElements = new ArrayList<DataElement>();
    
    public List<DataElement> getDataElements()
    {
        return dataElements;
    }
    
    private Map<Integer, String> equipmentDataValueMap;
    
    public Map<Integer, String> getEquipmentDataValueMap()
    {
        return equipmentDataValueMap;
    }
    
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------


    public String execute()
    {
        //equipmentDataValueMap = null;
        
        EquipmentInstance equipmentInstance = equipmentInstanceService.getEquipmentInstance( equipmentInstanceId );
        
        //OrganisationUnit organisationUnit = equipmentInstance.getOrganisationUnit();
        //InventoryType inventoryType =  equipmentInstance.getInventoryType();
        
        Period period = PeriodType.createPeriodExternalId( selectedPeriodId );
        
        DataSet dataSet = dataSetService.getDataSet( dataSetId );
        
        //DataSet dataSet = dataSetService.getDataSet( dataSetId, true, false, false );

        dataElements = new ArrayList<DataElement>( dataSet.getDataElements() );
        
        Collections.sort( dataElements, dataElementComparator );
        
        //Collections.sort( catalogTypeAttributes, new CatalogTypeAttributeComparator() );
        
        List<EquipmentDataValue> equipmentDataValues = new ArrayList<EquipmentDataValue>( equipmentDataValueService.getEquipmentDataValues( equipmentInstance, period, dataElements ) );      
        
        //System.out.println( " Size of equipmentDataValues List is ======  :" + equipmentDataValues.size() );
        
        if ( equipmentDataValues != null && equipmentDataValues.size() > 0 )
        {
            
            equipmentDataValueMap = new HashMap<Integer, String>();
            
            for( EquipmentDataValue equipmentDataValue : equipmentDataValues )
            {
                equipmentDataValueMap.put( equipmentDataValue.getDataElement().getId(), equipmentDataValue.getValue() );
            }
        }
        
        //System.out.println( " Size of equipmentDataValues Map is  List is ======  :" + equipmentDataValueMap.size() );
        /*
        System.out.println( " equipmentInstance Id is ======  :" + equipmentInstance.getId() );
        System.out.println( " Name of organisationUnit is ====  :" + organisationUnit.getName());
        System.out.println( " Name of inventoryType is ====  :" + inventoryType.getName());
        System.out.println( " Name of dataSet is ====  :" + dataSet.getName());
        System.out.println( " Name of period is ====  :" + selectedPeriodId );
        System.out.println( " Size of dataElements is ====  :" + dataElements.size() );
        
        
        for( DataElement dataElement : dataElements )
        {
            System.out.println( "DataElement Name :" + dataElement.getName() + "-- And value is :  "  + equipmentDataValueMap.get( dataElement.getId() ));
        }
        */
        return SUCCESS;
    }


}
