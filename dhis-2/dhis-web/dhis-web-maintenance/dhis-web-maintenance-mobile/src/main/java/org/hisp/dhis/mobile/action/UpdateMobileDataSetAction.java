package org.hisp.dhis.mobile.action;

import java.util.Collection;
import java.util.HashSet;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;

import com.opensymphony.xwork2.Action;

public class UpdateMobileDataSetAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------
    private Collection<String> selectedList = new HashSet<String>();

    public void setSelectedList( Collection<String> selectedList )
    {
        this.selectedList = selectedList;
    }

    private Collection<String> availableList = new HashSet<String>();

    public void setAvailableList( Collection<String> availableList )
    {
        this.availableList = availableList;
    }

    @Override
    public String execute()
        throws Exception
    {
        DataSet dataset = null;
        for ( String id : selectedList )
        {
            dataset = dataSetService.getDataSet( Integer.parseInt( id ) );
            if ( !dataset.isMobile() )
            {
                dataset.setMobile( true );
                dataSetService.updateDataSet( dataset );
            }
        }
     
        for ( String id : availableList )
        {
            dataset = dataSetService.getDataSet( Integer.parseInt( id ) );
            if ( dataset.isMobile() )
            {
                dataset.setMobile( false );
                dataSetService.updateDataSet( dataset );
            }
        }

        return SUCCESS;
    }

}
