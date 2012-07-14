package org.hisp.dhis.mobile.action.smscommand;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.smscommand.SMSCode;
import org.hisp.dhis.smscommand.SMSCommand;
import org.hisp.dhis.smscommand.SMSCommandService;

import com.opensymphony.xwork2.Action;

public class EditSMSCommandForm
    implements Action
{
    private SMSCommandService smsCommandService;

    private DataSetService dataSetService;

    private DataElementService dataElementService;

    private String name; 

    private int selectedDataSetID; 

    private String codeDataelementOption;

    private String separator;

    private String codeSeparator;

    private String defaultMessage;
    
    private int selectedCommandID = -1;

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {

        Set<SMSCode> codeSet = new HashSet<SMSCode>();

        @SuppressWarnings( "unchecked" )
        List<JSONObject> jsonCodes = (List<JSONObject>) JSONObject.fromObject( codeDataelementOption ).get( "codes" );
        for ( JSONObject x : jsonCodes )
        {
            SMSCode c = new SMSCode();
            c.setCode( x.getString( "code" ) );
            c.setDataElement( dataElementService.getDataElement( x.getInt( "dataElementId" ) ) );
            c.setOptionId( x.getInt( "optionId" ) );
            codeSet.add( c );
        }

        if ( codeSet.size() > 0 )
        {
            smsCommandService.save( codeSet );
        }

        SMSCommand c = getSMSCommand();
        if ( selectedDataSetID > -1 && c != null )
        {
           // c.setDataset( getDataSetService().getDataSet( getSelectedDataSetID() ) );
            c.setName( name );
            c.setSeparator( separator );
            c.setCodes( codeSet );
           // c.setCodeSeparator( codeSeparator );
            c.setDefaultMessage( defaultMessage );
            smsCommandService.save( c );
        }

        return SUCCESS;
    }

    public Collection<DataSet> getDataSets()
    {
        return getDataSetService().getAllDataSets();
    }

    public Set<DataElement> getDataSetElements()
    {
        DataSet d = dataSetService.getDataSet( selectedDataSetID );
        if ( d != null )
        {
            return d.getDataElements();
        }
        return null;
    }

    public SMSCommand getSMSCommand()
    {
        return smsCommandService.getSMSCommand( selectedCommandID );
    }

    public DataSetService getDataSetService()
    {
        return dataSetService;
    }

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    public int getSelectedDataSetID()
    {
        return selectedDataSetID;
    }

    public void setSelectedDataSetID( int selectedDataSetID )
    {
        this.selectedDataSetID = selectedDataSetID;
    }

    public String getCodeDataelementOption()
    {
        return codeDataelementOption;
    }

    public void setCodeDataelementOption( String codeDataelementOption )
    {
        this.codeDataelementOption = codeDataelementOption;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
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

    public String getSeparator()
    {
        return separator;
    }

    public void setSeparator( String separator )
    {
        this.separator = separator;
    }

    public DataElementService getDataElementService()
    {
        return dataElementService;
    }

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    

    public String getCodeSeparator()
    {
        return codeSeparator;
    }

    public void setCodeSeparator( String codeSeparator )
    {
        this.codeSeparator = codeSeparator;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

}