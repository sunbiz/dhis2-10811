package org.hisp.dhis.coldchain.inventory;

import java.util.Collection;

import org.hisp.dhis.common.GenericStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;

public interface EquipmentInstanceStore extends GenericStore<EquipmentInstance>
{
    String ID = EquipmentInstanceStore.class.getName();
    
    //int addEquipmentInstance( EquipmentInstance equipmentInstance );

    //void updateEquipmentInstance( EquipmentInstance equipmentInstance );

    //void deleteEquipmentInstance( EquipmentInstance equipmentInstance );

    //Collection<EquipmentInstance> getAllEquipmentInstance();
    
    Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit );
    
    Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit, InventoryType inventoryType );
    
    int getCountEquipmentInstance( OrganisationUnit orgUnit, InventoryType inventoryType );
    
    Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit, InventoryType inventoryType, int min, int max );

    int getCountEquipmentInstance( OrganisationUnit orgUnit, InventoryType inventoryType, InventoryTypeAttribute inventoryTypeAttribute, String searchText );

    Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit, InventoryType inventoryType, InventoryTypeAttribute inventoryTypeAttribute, String searchText, int min, int max );
}
