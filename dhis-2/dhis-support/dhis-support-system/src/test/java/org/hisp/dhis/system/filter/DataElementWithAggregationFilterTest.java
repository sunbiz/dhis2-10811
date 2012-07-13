package org.hisp.dhis.system.filter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.DhisTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.system.util.FilterUtils;
import org.junit.Test;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataElementWithAggregationFilterTest
    extends DhisTest
{
    @Test
    public void filter()
    {
        DataElement elementA = createDataElement( 'A' );
        DataElement elementB = createDataElement( 'B' );
        DataElement elementC = createDataElement( 'C' );
        DataElement elementD = createDataElement( 'D' );
        
        DataSet dataSetA = createDataSet( 'A', new MonthlyPeriodType() );
        dataSetA.setSkipAggregation( false );
        dataSetA.addDataElement( elementA );
        dataSetA.addDataElement( elementC );
        
        DataSet dataSetB = createDataSet( 'A', new MonthlyPeriodType() );
        dataSetB.setSkipAggregation( true );
        dataSetB.addDataElement( elementB );
        dataSetB.addDataElement( elementD );
        
        List<DataElement> list = new ArrayList<DataElement>();
        list.add( elementA );
        list.add( elementB );
        list.add( elementC );
        list.add( elementD );
        
        FilterUtils.filter( list, new DataElementWithAggregationFilter() );
        
        assertEquals( 2, list.size() );
        assertTrue( list.contains( elementA ) );
        assertTrue( list.contains( elementC ) );
    }
}
