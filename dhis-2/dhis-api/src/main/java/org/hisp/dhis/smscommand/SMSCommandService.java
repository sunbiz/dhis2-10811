package org.hisp.dhis.smscommand;

import java.util.Collection;
import java.util.Set;

public interface SMSCommandService {
    void updateSMSCommand(SMSCommand cmd);
    Collection<SMSCommand> getSMSCommands();
    SMSCommand getSMSCommand(int id);
    void save(SMSCommand cmd);
    void save(Set<SMSCode> codes);
    void delete(SMSCommand cmd);
}
