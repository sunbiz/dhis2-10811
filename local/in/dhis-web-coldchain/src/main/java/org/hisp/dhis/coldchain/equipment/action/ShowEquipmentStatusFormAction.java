package org.hisp.dhis.coldchain.equipment.action;

import com.opensymphony.xwork2.Action;

public class ShowEquipmentStatusFormAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------


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

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        return SUCCESS;
    }
}
