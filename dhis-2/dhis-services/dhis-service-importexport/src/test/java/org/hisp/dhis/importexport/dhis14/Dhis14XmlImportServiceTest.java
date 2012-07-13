package org.hisp.dhis.importexport.dhis14;

import java.io.InputStream;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.ImportService;
import org.hisp.dhis.importexport.ImportType;
import org.junit.Test;

public class Dhis14XmlImportServiceTest
    extends DhisSpringTest
{
    private ImportService importService;

    private InputStream inputStream;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        inputStream = classLoader.getResourceAsStream( "dhis14A.zip" );
        
        importService = (ImportService) getBean( "org.hisp.dhis.importexport.Dhis14XMLImportService" );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Test
    public void testImport() throws Exception
    {
        ImportParams params = new ImportParams();

        params.setType( ImportType.PREVIEW );
        params.setDataValues( true );
        params.setExtendedMode( false );
        params.setSkipCheckMatching( false );
        
        importService.importData( params, inputStream );
    }
}
