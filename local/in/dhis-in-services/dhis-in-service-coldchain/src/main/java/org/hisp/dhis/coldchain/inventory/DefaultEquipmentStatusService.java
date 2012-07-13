package org.hisp.dhis.coldchain.inventory;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;
@Transactional
public class DefaultEquipmentStatusService implements EquipmentStatusService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private EquipmentStatusStore equipmentStatusStore;

    public void setEquipmentStatusStore( EquipmentStatusStore equipmentStatusStore )
    {
        this.equipmentStatusStore = equipmentStatusStore;
    }
    
    // -------------------------------------------------------------------------
    // EquipmentWorkingStatus
    // -------------------------------------------------------------------------
    
    @Override
    public int addEquipmentStatus( EquipmentStatus equipmentStatus )
    {
        return equipmentStatusStore.save( equipmentStatus );
    }
    @Override
    public void deleteEquipmentStatus( EquipmentStatus equipmentStatus )
    {
        equipmentStatusStore.delete( equipmentStatus );
    }
    @Override
    public Collection<EquipmentStatus> getAllEquipmentStatus()
    {
        return equipmentStatusStore.getAll();
    }
    @Override
    public void updateEquipmentStatus( EquipmentStatus equipmentStatus )
    {
        equipmentStatusStore.update( equipmentStatus );
    }
    
    public Collection<EquipmentStatus> getEquipmentStatusHistory( EquipmentInstance equipmentInstance )
    {
        return equipmentStatusStore.getEquipmentStatusHistory( equipmentInstance );
    }

}
