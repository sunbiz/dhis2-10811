package org.hisp.dhis.mobile.action.smscommand;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.smscommand.SMSCommand;
import org.hisp.dhis.smscommand.SMSCommandService;

import com.opensymphony.xwork2.Action;

public class CreateSMSCommandForm implements Action {

    private SMSCommandService smsCommandService;
    private DataSetService dataSetService;
    
    private String name;
    private String parser;
    private int selectedDataSetID; //
    
    @Override
    public String execute() throws Exception {
        DataSet dataset = getDataSetService().getDataSet(getSelectedDataSetID());
        smsCommandService.save(new SMSCommand(parser,name,dataset));
        return SUCCESS;
    }

    public SMSCommandService getSmsCommandService() {
        return smsCommandService;
    }

    public void setSmsCommandService(SMSCommandService smsCommandService) {
        this.smsCommandService = smsCommandService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
    }

    public int getSelectedDataSetID() {
        return selectedDataSetID;
    }

    public void setSelectedDataSetID(int selectedDataSetID) {
        this.selectedDataSetID = selectedDataSetID;
    }

    public DataSetService getDataSetService() {
        return dataSetService;
    }

    public void setDataSetService(DataSetService dataSetService) {
        this.dataSetService = dataSetService;
    }

}
