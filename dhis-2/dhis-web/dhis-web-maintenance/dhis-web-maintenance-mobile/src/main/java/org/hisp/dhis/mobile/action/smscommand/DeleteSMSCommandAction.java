package org.hisp.dhis.mobile.action.smscommand;

import org.hisp.dhis.smscommand.SMSCommandService;

import com.opensymphony.xwork2.Action;

public class DeleteSMSCommandAction implements Action{

    private SMSCommandService smsCommandService;
   
    private int deleteCommandId;
    
    public String execute() throws Exception {
        smsCommandService.delete(smsCommandService.getSMSCommand(deleteCommandId));
        return SUCCESS;
        
    }

    public int getDeleteCommandId() {
        return deleteCommandId;
    }

    public void setDeleteCommandId(int deleteCommandId) {
        this.deleteCommandId = deleteCommandId;
    }

    public SMSCommandService getSmsCommandService() {
        return smsCommandService;
    }

    public void setSmsCommandService(SMSCommandService smsCommandService) {
        this.smsCommandService = smsCommandService;
    }

}
