package org.hisp.dhis.coldchain.inventory;

import java.util.Collection;
import java.util.List;

import org.hisp.dhis.organisationunit.OrganisationUnit;

public interface EquipmentInstanceService
{
    String ID = EquipmentInstanceService.class.getName();
    
    int addEquipmentInstance( EquipmentInstance equipmentInstance );

    void updateEquipmentInstance( EquipmentInstance equipmentInstance );

    void deleteEquipmentInstance( EquipmentInstance equipmentInstance );
    
    void deleteCompleteEquipmentInstance( EquipmentInstance equipmentInstance );
    
    int createEquipment( EquipmentInstance equipmentInstance, List<Equipment> equipmentDetails );

    EquipmentInstance getEquipmentInstance( int id );
    
    Collection<EquipmentInstance> getAllEquipmentInstance();

    Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit, InventoryType inventoryType );
    
    Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit );
    
    int getCountEquipmentInstance( OrganisationUnit orgUnit, InventoryType inventoryType );
    
    Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit, InventoryType inventoryType, int min, int max );
    
    int getCountEquipmentInstance( OrganisationUnit orgUnit, InventoryType inventoryType, InventoryTypeAttribute inventoryTypeAttribute, String searchText );
    
    Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit, InventoryType inventoryType, InventoryTypeAttribute inventoryTypeAttribute, String searchText, int min, int max );
}
