package org.hisp.dhis.common;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.*;

public class IdentifiableObjectManagerTest
    extends DhisSpringTest
{
    @Autowired
    private DataElementService dataElementService;
    
    @Autowired
    private IdentifiableObjectManager identifiableObjectManager;
    
    @Test
    public void testGetObject()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        
        int dataElementIdA = dataElementService.addDataElement( dataElementA );
        int dataElementIdB = dataElementService.addDataElement( dataElementB );
        
        DataElementGroup dataElementGroupA = createDataElementGroup( 'A' );
        DataElementGroup dataElementGroupB = createDataElementGroup( 'B' );
        
        int dataElementGroupIdA = dataElementService.addDataElementGroup( dataElementGroupA );
        int dataElementGroupIdB = dataElementService.addDataElementGroup( dataElementGroupB );
        
        assertEquals( dataElementA, identifiableObjectManager.getObject( dataElementIdA, DataElement.class.getSimpleName() ) );
        assertEquals( dataElementB, identifiableObjectManager.getObject( dataElementIdB, DataElement.class.getSimpleName() ) );
        
        assertEquals( dataElementGroupA, identifiableObjectManager.getObject( dataElementGroupIdA, DataElementGroup.class.getSimpleName() ) );
        assertEquals( dataElementGroupB, identifiableObjectManager.getObject( dataElementGroupIdB, DataElementGroup.class.getSimpleName() ) );
    }
}
