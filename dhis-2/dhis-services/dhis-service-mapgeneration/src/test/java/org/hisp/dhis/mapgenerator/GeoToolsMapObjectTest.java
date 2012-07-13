package org.hisp.dhis.mapgenerator;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.mapgeneration.GeoToolsMapObject;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Kenneth Solbø Andersen <kennetsa@ifi.uio.no>
 */
public class GeoToolsMapObjectTest
    extends DhisSpringTest
{
    private GeoToolsMapObject geoToolsMapObject;

    @Override
    public void setUpTest()
    {
        geoToolsMapObject = new GeoToolsMapObject();
    }

    @Test
    public void testSetGetName()
    {
        geoToolsMapObject.setName( "Name1" );
        assertEquals( "Name1", geoToolsMapObject.getName() );
        geoToolsMapObject.setName( "Another name" );
        assertEquals( "Another name", geoToolsMapObject.getName() );
    }

    @Test
    public void testSetGetValue()
    {
        geoToolsMapObject.setValue( 489.3 );
        assertEquals( 489.3, geoToolsMapObject.getValue(), 0.00001 );
        geoToolsMapObject.setValue( 41.423 );
        assertEquals( 41.423, geoToolsMapObject.getValue(), 0.00001 );
    }

    @Test
    @Ignore
    public void testSetGetRadius()
    {
        geoToolsMapObject.setRadius( 32 );
        assertEquals( 32.5264F, geoToolsMapObject.getRadius(), 0.00001 );
        geoToolsMapObject.setRadius( 61 );
        assertEquals( 61441.5F, geoToolsMapObject.getRadius(), 0.00001 );
    }

    @Test
    public void testSetGetFillColor()
    {
        geoToolsMapObject.setFillColor( Color.BLUE );
        assertEquals( Color.BLUE, geoToolsMapObject.getFillColor() );
        geoToolsMapObject.setFillColor( Color.CYAN );
        assertEquals( Color.CYAN, geoToolsMapObject.getFillColor() );
    }

    @Test
    public void testSetGetFillOpacity()
    {
        geoToolsMapObject.setFillOpacity( 5.23F );
        assertEquals( 5.23F, geoToolsMapObject.getFillOpacity(), 0.00001 );
        geoToolsMapObject.setFillOpacity( 594208420.134F );
        assertEquals( 594208420.134F, geoToolsMapObject.getFillOpacity(), 0.00001 );
    }

    @Test
    public void testSetGetStrokeColor()
    {
        geoToolsMapObject.setStrokeColor( Color.GREEN );
        assertEquals( Color.GREEN, geoToolsMapObject.getStrokeColor() );
        geoToolsMapObject.setStrokeColor( Color.WHITE );
        assertEquals( Color.WHITE, geoToolsMapObject.getStrokeColor() );
    }

    @Test
    public void testSetGetStrokeWidth()
    {
        geoToolsMapObject.setStrokeWidth( 32 );
        assertEquals( 32, geoToolsMapObject.getStrokeWidth() );
        geoToolsMapObject.setStrokeWidth( 364114 );
        assertEquals( 364114, geoToolsMapObject.getStrokeWidth() );
    }

    @Test
    public void testSetGetMapLayer()
    {
        //TODO
    }

    @Test
    public void testSetGetInterval()
    {
        //TODO
    }

    @Test
    public void testToString()
    {
        //TODO
    }
}
