package org.hisp.dhis.smscommand;

import java.util.Collection;
import java.util.Set;

public interface SMSCommandStore  {
    Collection<SMSCommand> getSMSCommands();
    SMSCommand getSMSCommand(int id);
    int save(SMSCommand cmd);
    void delete(SMSCommand cmd);
    void save(Set<SMSCode> codes);
    
}
