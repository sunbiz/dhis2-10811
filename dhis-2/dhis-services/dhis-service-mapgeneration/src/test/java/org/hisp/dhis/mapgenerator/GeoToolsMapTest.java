package org.hisp.dhis.mapgenerator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.awt.Color;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.mapgeneration.GeoToolsMap;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Kenneth Solbø Andersen <kennetsa@ifi.uio.no>
 */
public class GeoToolsMapTest
    extends DhisSpringTest
{
    private GeoToolsMap geoToolsMap;

    @Override
    public void setUpTest()
    {
        geoToolsMap = new GeoToolsMap();
    }

    @Test
    public void testSetGetBackground()
    {
        geoToolsMap.setBackgroundColor( Color.BLUE );
        assertEquals( Color.BLUE, geoToolsMap.getBackgroundColor() );
    }

    @Test
    public void testSetGetAntiAliasingEnabled()
    {
        geoToolsMap.setAntiAliasingEnabled( false );
        assertFalse( geoToolsMap.isAntiAliasingEnabled() );
        geoToolsMap.setAntiAliasingEnabled( true );
        assertTrue( geoToolsMap.isAntiAliasingEnabled() );
    }

    @Test
    @Ignore
    public void testRender()
    {
        //TODO
    }
}
