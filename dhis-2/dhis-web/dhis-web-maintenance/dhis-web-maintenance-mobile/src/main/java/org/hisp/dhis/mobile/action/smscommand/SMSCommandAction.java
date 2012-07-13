package org.hisp.dhis.mobile.action.smscommand;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.smscommand.SMSCode;
import org.hisp.dhis.smscommand.SMSCommandService;
import org.hisp.dhis.smscommand.SMSCommand;

import com.opensymphony.xwork2.Action;

public class SMSCommandAction
    implements Action
{

    private SMSCommandService smsCommandService;

    private DataSetService dataSetService;

    private int selectedCommandID = -1;

    private Map<String, String> codes = new HashMap<String, String>();

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        if ( getSMSCommand() != null && getSMSCommand().getCodes() != null )
        {
            for ( SMSCode x : getSMSCommand().getCodes() )
            {
                codes.put( "" + x.getDataElement().getId() + x.getOptionId(), x.getCode() );
            }
        }
        return SUCCESS;
    }

    public Set<DataElement> getDataSetElements()
    {
        if ( getSMSCommand() != null )
        {
            DataSet d = getSMSCommand().getDataset();
            if ( d != null )
            {
                return d.getDataElements();
            }
        }
        return null;
    }

    public Collection<DataSet> getDataSets()
    {
        return getDataSetService().getAllDataSets();
    }

    public Collection<SMSCommand> getSMSCommands()
    {
        System.out.println( "get:" + smsCommandService.getSMSCommands() );
        return smsCommandService.getSMSCommands();
    }

    public SMSCommand getSMSCommand()
    {
        if ( selectedCommandID > -1 )
        {
            return smsCommandService.getSMSCommand( selectedCommandID );
        }
        else
        {
            return null;
        }
    }

    public SMSCommandService getSmsCommandService()
    {
        return smsCommandService;
    }

    public void setSmsCommandService( SMSCommandService smsCommandService )
    {
        this.smsCommandService = smsCommandService;
    }

    public int getSelectedCommandID()
    {
        return selectedCommandID;
    }

    public void setSelectedCommandID( int selectedCommandID )
    {
        this.selectedCommandID = selectedCommandID;
    }

    public DataSetService getDataSetService()
    {
        return dataSetService;
    }

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    public Map<String, String> getCodes()
    {
        return codes;
    }

    public void setCodes( Map<String, String> codes )
    {
        this.codes = codes;
    }

}
