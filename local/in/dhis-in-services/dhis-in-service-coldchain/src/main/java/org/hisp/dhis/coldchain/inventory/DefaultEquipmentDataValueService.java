package org.hisp.dhis.coldchain.inventory;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.period.Period;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultEquipmentDataValueService implements EquipmentDataValueService
{
    private static final Log log = LogFactory.getLog( DefaultEquipmentDataValueService.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private EquipmentDataValueStore equipmentDataValueStore;

    public void setEquipmentDataValueStore( EquipmentDataValueStore equipmentDataValueStore )
    {
        this.equipmentDataValueStore = equipmentDataValueStore;
    }
    
    // -------------------------------------------------------------------------
    // EquipmentDataValue
    // -------------------------------------------------------------------------

    public void addEquipmentDataValue( EquipmentDataValue equipmentDataValue )
    {
        equipmentDataValueStore.addEquipmentDataValue( equipmentDataValue );
    }
    
    public void updateEquipmentDataValue( EquipmentDataValue equipmentDataValue )
    {
        equipmentDataValueStore.updateEquipmentDataValue( equipmentDataValue );
    }
    
    public void deleteEquipmentDataValue( EquipmentDataValue equipmentDataValue )
    {
        equipmentDataValueStore.deleteEquipmentDataValue( equipmentDataValue );
    }
    
    public Collection<EquipmentDataValue> getEquipmentDataValues( EquipmentInstance equipmentInstance, Period period, Collection<DataElement> dataElements )
    {
        return equipmentDataValueStore.getEquipmentDataValues( equipmentInstance, period, dataElements );
    }
    
    public EquipmentDataValue getEquipmentDataValue( EquipmentInstance equipmentInstance, Period period, DataElement dataElement )
    {
        return equipmentDataValueStore.getEquipmentDataValue( equipmentInstance, period, dataElement );
    }
    
    
}
