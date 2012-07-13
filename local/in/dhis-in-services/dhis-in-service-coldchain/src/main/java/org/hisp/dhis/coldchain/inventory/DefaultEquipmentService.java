package org.hisp.dhis.coldchain.inventory;

import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
@Transactional
public class DefaultEquipmentService implements EquipmentService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private EquipmentStore equipmentStore;
    
    public void setEquipmentStore( EquipmentStore equipmentStore )
    {
        this.equipmentStore = equipmentStore;
    }

    // -------------------------------------------------------------------------
    // EquipmentDetails
    // -------------------------------------------------------------------------
    
    @Override
    public void addEquipment( Equipment equipment )
    {
        equipmentStore.addEquipment( equipment );
    }
    @Override
    public void deleteEquipment( Equipment equipment )
    {
        equipmentStore.deleteEquipment( equipment );
    }
    @Override
    public Collection<Equipment> getAllEquipments()
    {
        return equipmentStore.getAllEquipments();
    }
    @Override
    public void updateEquipment( Equipment equipment )
    {
        equipmentStore.updateEquipment( equipment );
    }
    
    public Collection<Equipment> getEquipments( EquipmentInstance equipmentInstance)
    {
        return equipmentStore.getEquipments( equipmentInstance );
    }

    public Equipment getEquipment( EquipmentInstance equipmentInstance, InventoryTypeAttribute inventoryTypeAttribute )
    {
        return equipmentStore.getEquipment( equipmentInstance, inventoryTypeAttribute );
    }
 /*   
    public Map<String, String> inventryTypeAttributeAndValue( EquipmentInstance equipmentInstance, List<InventoryTypeAttribute> inventoryTypeAttributeList )
    {
        String inventoryTypeAttributeName = "";
        
        String inventoryTypeAttributeValue = "";
        
        String inventoryTypeAttributeNameValue = "";
        
        Map<String, String> inventryTypeAttributeAndValueMap = new HashMap<String, String>();
        
        for( InventoryTypeAttribute inventoryTypeAttribute : inventoryTypeAttributeList )
        {
            Equipment equipmentDetail = getEquipment( equipmentInstance, inventoryTypeAttribute );
            if( equipmentDetail != null && equipmentDetail.getValue() != null )
            {
                
                if ( InventoryTypeAttribute.TYPE_COMBO.equalsIgnoreCase( equipmentDetail.getInventoryTypeAttribute().getValueType() ) )
                {
                    inventryTypeAttributeAndValueMap.put( inventoryTypeAttribute.getName(), equipmentDetail.getInventoryTypeAttributeOption().getName() );
                    
                    inventoryTypeAttributeName += "--" + inventoryTypeAttribute.getName();
                    inventoryTypeAttributeValue += "--" + equipmentDetail.getInventoryTypeAttributeOption().getName();
                }
                else
                {
                    inventryTypeAttributeAndValueMap.put( inventoryTypeAttribute.getName(), equipmentDetail.getValue() );
                    
                    inventoryTypeAttributeName += "--" + inventoryTypeAttribute.getName();
                    inventoryTypeAttributeValue += "--" + equipmentDetail.getValue();
                }
                
                inventoryTypeAttributeNameValue = inventoryTypeAttributeName + "#@" + inventoryTypeAttributeValue;
            }
        }
        
        
        
        return inventryTypeAttributeAndValueMap;
    }
 */
    public String inventryTypeAttributeAndValue( EquipmentInstance equipmentInstance, List<InventoryTypeAttribute> inventoryTypeAttributeList )
    {
        String inventoryTypeAttributeName = "";
        
        String inventoryTypeAttributeValue = "";
        
        String inventoryTypeAttributeNameValue = "";
        
        //Map<String, String> inventryTypeAttributeAndValueMap = new HashMap<String, String>();
        
        for( InventoryTypeAttribute inventoryTypeAttribute : inventoryTypeAttributeList )
        {
            Equipment equipmentDetail = getEquipment( equipmentInstance, inventoryTypeAttribute );
            if( equipmentDetail != null && equipmentDetail.getValue() != null )
            {
                
                if ( InventoryTypeAttribute.TYPE_COMBO.equalsIgnoreCase( equipmentDetail.getInventoryTypeAttribute().getValueType() ) )
                {
                    //inventryTypeAttributeAndValueMap.put( inventoryTypeAttribute.getName(), equipmentDetail.getInventoryTypeAttributeOption().getName() );
                    
                    inventoryTypeAttributeName += "--" + inventoryTypeAttribute.getName();
                    inventoryTypeAttributeValue += "--" + equipmentDetail.getInventoryTypeAttributeOption().getName();
                }
                else
                {
                    //inventryTypeAttributeAndValueMap.put( inventoryTypeAttribute.getName(), equipmentDetail.getValue() );
                    
                    inventoryTypeAttributeName += "--" + inventoryTypeAttribute.getName();
                    inventoryTypeAttributeValue += "--" + equipmentDetail.getValue();
                }
                
                inventoryTypeAttributeNameValue = inventoryTypeAttributeName.substring( 2 ) + "#@#" + inventoryTypeAttributeValue.substring( 2 );
            }
        }
        
        //System.out.println( inventoryTypeAttributeName + "#@#" + inventoryTypeAttributeValue );
        
        return inventoryTypeAttributeNameValue;
    }    
    
}
