package org.hisp.dhis.dashboard.action;

import org.hisp.dhis.interpretation.InterpretationService;
import org.hisp.dhis.message.MessageService;

import com.opensymphony.xwork2.Action;

public class InitAction
    implements Action
{
    private MessageService messageService;

    public void setMessageService( MessageService messageService )
    {
        this.messageService = messageService;
    }
    
    private InterpretationService interpretationService;

    public void setInterpretationService( InterpretationService interpretationService )
    {
        this.interpretationService = interpretationService;
    }

    private long messageCount;

    public long getMessageCount()
    {
        return messageCount;
    }
    
    private long interpretationCount;

    public long getInterpretationCount()
    {
        return interpretationCount;
    }

    public String execute()
    {
        messageCount = messageService.getUnreadMessageConversationCount();
        
        interpretationCount = interpretationService.getNewInterpretationCount();
        
        return SUCCESS;
    }
}
