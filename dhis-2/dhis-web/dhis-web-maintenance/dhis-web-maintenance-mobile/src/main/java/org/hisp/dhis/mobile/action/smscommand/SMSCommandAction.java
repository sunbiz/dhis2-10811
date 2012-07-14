package org.hisp.dhis.mobile.action.smscommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.comparator.DataElementSortOrderComparator;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.sms.parse.ParserType;
import org.hisp.dhis.smscommand.SMSCode;
import org.hisp.dhis.smscommand.SMSCommand;
import org.hisp.dhis.smscommand.SMSCommandService;

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

    public List<DataElement> getDataSetElements()
    {
        if ( getSMSCommand() != null )
        {
            DataSet d = getSMSCommand().getDataset();
            if ( d != null )
            {
                List<DataElement> x = new ArrayList<DataElement>( d.getDataElements() );
                Collections.sort( x, new DataElementSortOrderComparator() );
                return x;
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
    
    public ParserType[] getParserType(){       
        return ParserType.values();
    }

}
