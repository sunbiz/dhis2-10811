package org.hisp.dhis.smscommand;

import org.hisp.dhis.dataelement.DataElement;

public class SMSCode {
    
    int id; // hibernate
    
    String code;
    DataElement dataElement;
    int optionId;
    
    public SMSCode(String code, DataElement dataElement, int optionId) {
        this.code = code;
        this.dataElement = dataElement;
        this.optionId = optionId;
    }
    
    public SMSCode(){
        
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public DataElement getDataElement() {
        return dataElement;
    }
    public void setDataElement(DataElement dataElement) {
        this.dataElement = dataElement;
    }
    public int getOptionId() {
        return optionId;
    }
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

}
