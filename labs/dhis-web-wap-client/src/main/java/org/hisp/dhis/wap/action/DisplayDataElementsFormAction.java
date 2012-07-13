/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hisp.dhis.wap.action;


import com.opensymphony.xwork2.Action;
import java.util.Collection;
import java.util.Iterator;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;

/**
 *
 * @author harsh
 */
public class DisplayDataElementsFormAction implements Action
{

    private String dsRadio;

    public void setDsRadio( String dsRadio )
    {
        this.dsRadio = dsRadio;
    }

    DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    Collection<DataElement> dataElements;

    public Collection<DataElement> getDataElements()
    {
        return dataElements;
    }

    @Override
    public String execute() throws Exception
    {
        int dataSetId = Integer.parseInt( dsRadio );
        DataSet dataSet = null;
        Collection<DataSet> allDataSets = dataSetService.getAllDataSets();
        Iterator itr = allDataSets.iterator();
        while ( itr.hasNext() )
        {
            dataSet = (DataSet) itr.next();
            if ( dataSet.getId() == dataSetId )
            {
                break;
            }
            dataSet = null;
        }
        if ( dataSet != null )
        {
            dataElements = dataSetService.getDataElements( dataSet );
        }

        System.out.println( "drdio=" + dsRadio + "dataele=" + dataElements.size() );
        return SUCCESS;
        //throw new UnsupportedOperationException( "Not supported yet." );
    }
}
