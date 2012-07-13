package org.hisp.dhis.coldchain.equipment.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.EquipmentInstanceService;
import org.hisp.dhis.coldchain.inventory.EquipmentService;
import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;
import org.hisp.dhis.coldchain.inventory.InventoryType_Attribute;
import org.hisp.dhis.coldchain.inventory.comparator.InventoryTypeAttributeMandatoryComparator;
import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.organisationunit.OrganisationUnit;

import com.opensymphony.xwork2.Action;

public class ShowEquipmentDataEntryFormAction implements Action
{

    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------
    /*
    private EquipmentDataValueService equipmentDataValueService;
    
    public void setEquipmentDataValueService( EquipmentDataValueService equipmentDataValueService )
    {
        this.equipmentDataValueService = equipmentDataValueService;
    }
    */
    private EquipmentInstanceService equipmentInstanceService;
    
    public void setEquipmentInstanceService( EquipmentInstanceService equipmentInstanceService )
    {
        this.equipmentInstanceService = equipmentInstanceService;
    }
    
    private InventoryTypeService inventoryTypeService;
    
    public void setInventoryTypeService( InventoryTypeService inventoryTypeService )
    {
        this.inventoryTypeService = inventoryTypeService;
    }
    private EquipmentService equipmentService;
    
    public void setEquipmentService( EquipmentService equipmentService )
    {
        this.equipmentService = equipmentService;
    }
    
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private Integer equipmentInstanceId;

    public void setEquipmentInstanceId( Integer equipmentInstanceId )
    {
        this.equipmentInstanceId = equipmentInstanceId;
    }
    
    public Integer getEquipmentInstanceId()
    {
        return equipmentInstanceId;
    }
    /*
    private Integer dataSetId;
    
    public void setDataSetId( Integer dataSetId )
    {
        this.dataSetId = dataSetId;
    }
    
    private Integer periodId;
    
    public void setPeriodId( Integer periodId )
    {
        this.periodId = periodId;
    }
    */
    private List<DataSet> dataSetList;
    
    public List<DataSet> getDataSetList()
    {
        return dataSetList;
    }

    private OrganisationUnit organisationUnit;
    
    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    private InventoryType inventoryType;
    
    public InventoryType getInventoryType()
    {
        return inventoryType;
    }

    public List<InventoryTypeAttribute> inventoryTypeAttributeList;
    
    public List<InventoryTypeAttribute> getInventoryTypeAttributeList()
    {
        return inventoryTypeAttributeList;
    }
    
    private Map<String, String> inventryTypeAttributeAndValueMap;
    
    
    
    public Map<String, String> getInventryTypeAttributeAndValueMap()
    {
        return inventryTypeAttributeAndValueMap;
    }

    private String inventoryTypeAttributeNameValue;
    
    public String getInventoryTypeAttributeNameValue()
    {
        return inventoryTypeAttributeNameValue;
    }

    private String inventoryTypeAttributeName;
    
    public String getInventoryTypeAttributeName()
    {
        return inventoryTypeAttributeName;
    }

    private String inventoryTypeAttributeValue;
    
    public String getInventoryTypeAttributeValue()
    {
        return inventoryTypeAttributeValue;
    }




    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        
        EquipmentInstance equipmentInstance = equipmentInstanceService.getEquipmentInstance( equipmentInstanceId );
        //equipmentInstance.getInventoryType().getDataSets();
        organisationUnit = equipmentInstance.getOrganisationUnit();
        inventoryType =  equipmentInstance.getInventoryType();
        dataSetList = new ArrayList<DataSet>(  equipmentInstance.getInventoryType().getDataSets() );
        
        Collections.sort( dataSetList, IdentifiableObjectNameComparator.INSTANCE );
        /*
        for( DataSet dataSet : dataSetList )
        {
            System.out.println( dataSet.getPeriodType().getId() +"--------" +dataSet.getPeriodType().getName());
        }
        */
        
        inventoryTypeAttributeList = new ArrayList<InventoryTypeAttribute>( inventoryTypeService.getAllInventoryTypeAttributesForDisplay( inventoryType ));
        
        if( inventoryTypeAttributeList == null || inventoryTypeAttributeList.size() == 0  )
        {
            inventoryTypeAttributeList = new ArrayList<InventoryTypeAttribute>( );
            for( InventoryType_Attribute inventoryType_Attribute : inventoryType.getInventoryType_Attributes() )
            {
                inventoryTypeAttributeList.add( inventoryType_Attribute.getInventoryTypeAttribute() );
            }
            
            Collections.sort( inventoryTypeAttributeList, new InventoryTypeAttributeMandatoryComparator() );
            if( inventoryTypeAttributeList != null && inventoryTypeAttributeList.size() > 3 )
            {
                int count = 1;
                Iterator<InventoryTypeAttribute> iterator = inventoryTypeAttributeList.iterator();
                while( iterator.hasNext() )
                {
                    iterator.next();
                    
                    if( count > 3 )
                        iterator.remove();
                    
                    count++;
                }            
            }
            
        }
        //List<Equipment> equipmentDetailsList = new ArrayList<Equipment>( equipmentService.getEquipments( equipmentInstance ) );
        
        //inventryTypeAttributeAndValueMap = new HashMap<String, String>();
        
        //inventryTypeAttributeAndValueMap.putAll( equipmentService.inventryTypeAttributeAndValue( equipmentInstance, inventoryTypeAttributeList ));
        
        inventoryTypeAttributeNameValue = equipmentService.inventryTypeAttributeAndValue( equipmentInstance, inventoryTypeAttributeList );
        String[] tempNameValue = inventoryTypeAttributeNameValue.split( "#@#" ); 
       
        inventoryTypeAttributeName = tempNameValue[0];
        
        inventoryTypeAttributeValue = tempNameValue[1];
        
        //System.out.println( inventoryTypeAttributeName + "---" + inventoryTypeAttributeValue );
        
       /*
        for( InventoryTypeAttribute inventoryTypeAttribute : inventoryTypeAttributeList )
        {
            
            System.out.println( inventoryTypeAttribute.getName() + "---" + inventryTypeAttributeAndValueMap.get( inventoryTypeAttribute.getName()) );
            
            
            Equipment equipmentDetails = equipmentService.getEquipment( equipmentInstance, inventoryTypeAttribute );
            if( equipmentDetails != null && equipmentDetails.getValue() != null )
            {
                //System.out.println( inventoryTypeAttribute.getName() + "---" + equipmentDetails.getValue() );
                
                
                //equipmentDetailsMap.put( equipmentInstance.getId()+":"+inventoryTypeAttribute1.getId(), equipmentDetails.getValue() );
            }
            
        }
       */
        
        
        
        
        
        /*
        
        for( Equipment equipmentDetails : equipmentDetailsList )
        {
            if ( InventoryTypeAttribute.TYPE_COMBO.equalsIgnoreCase( equipmentDetails.getInventoryTypeAttribute().getValueType() ) )
            {
                equipmentValueMap.put( equipmentDetails.getInventoryTypeAttribute().getId(), equipmentDetails.getInventoryTypeAttributeOption().getName() );
            }
            else
            {
                equipmentValueMap.put( equipmentDetails.getInventoryTypeAttribute().getId(), equipmentDetails.getValue() );
            }
        }
        */
        
        // data entry parts
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        return SUCCESS;
    }

}
