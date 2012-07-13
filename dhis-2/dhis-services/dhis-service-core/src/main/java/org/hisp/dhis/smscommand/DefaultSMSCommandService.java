package org.hisp.dhis.smscommand;

import java.util.Collection;
import java.util.Set;

import org.hisp.dhis.smscommand.SMSCommandStore;

public class DefaultSMSCommandService implements SMSCommandService{
    
    private SMSCommandStore smsCommandStore;

    @Override
    public void updateSMSCommand(SMSCommand cmd) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Collection<SMSCommand> getSMSCommands() {
        return smsCommandStore.getSMSCommands();
    }

    public void setSmsCommandStore(SMSCommandStore smsCommandStore) {
        this.smsCommandStore = smsCommandStore;
    }

    public void save(SMSCommand cmd){
        smsCommandStore.save(cmd);
    }
    
    public SMSCommand getSMSCommand(int id){
        return smsCommandStore.getSMSCommand(id);
    }
    

    public void save(Set<SMSCode> codes){
        smsCommandStore.save(codes);
    }
    
    public void delete(SMSCommand cmd){
       smsCommandStore.delete(cmd);
    }
}
