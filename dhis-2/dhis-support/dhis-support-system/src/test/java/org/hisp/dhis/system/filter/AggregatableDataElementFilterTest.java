package org.hisp.dhis.system.filter;

import static junit.framework.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.DhisTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.system.util.FilterUtils;
import org.junit.Test;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class AggregatableDataElementFilterTest
    extends DhisTest
{
    @Test
    public void filter()
    {
        DataElement elementA = createDataElement( 'A' );
        DataElement elementB = createDataElement( 'B' );
        DataElement elementC = createDataElement( 'C' );
        DataElement elementD = createDataElement( 'D' );
        DataElement elementE = createDataElement( 'E' );
        DataElement elementF = createDataElement( 'F' );
        
        elementA.setType( DataElement.VALUE_TYPE_BOOL );
        elementB.setType( DataElement.VALUE_TYPE_INT );
        elementC.setType( DataElement.VALUE_TYPE_STRING );
        elementD.setType( DataElement.VALUE_TYPE_BOOL );
        elementE.setType( DataElement.VALUE_TYPE_INT );
        elementF.setType( DataElement.VALUE_TYPE_STRING );        
        
        Set<DataElement> set = new HashSet<DataElement>();
        
        set.add( elementA );
        set.add( elementB );
        set.add( elementC );
        set.add( elementD );
        set.add( elementE );
        set.add( elementF );
        
        Set<DataElement> reference = new HashSet<DataElement>();
        
        reference.add( elementA );
        reference.add( elementB );
        reference.add( elementD );
        reference.add( elementE );
        
        FilterUtils.filter( set, new AggregatableDataElementFilter() );
        
        assertEquals( reference.size(), set.size() );
        assertEquals( reference, set );
    }
}
