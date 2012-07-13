package org.hisp.dhis.system.util;

/*
 * Copyright (c) 2004-2012, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hisp.dhis.system.util.ValidationUtils.coordinateIsValid;
import static org.hisp.dhis.system.util.ValidationUtils.getLatitude;
import static org.hisp.dhis.system.util.ValidationUtils.getLongitude;

import org.junit.Test;

/**
 * @author Lars Helge Overland
 */
public class ValidationUtilsTest
{
    @Test
    public void testCoordinateIsValid()
    {
        assertTrue( coordinateIsValid( "[+37.99034,-28.94221]" ) );
        assertTrue( coordinateIsValid( "[37.99034,-28.94221]" ) );
        assertTrue( coordinateIsValid( "[+37.99034,28.94221]" ) );
        assertTrue( coordinateIsValid( "[170.99034,78.94221]" ) );
        assertTrue( coordinateIsValid( "[-167,-28.94221]" ) );
        assertTrue( coordinateIsValid( "[37.99034,28]" ) );
        
        assertFalse( coordinateIsValid( "23.34343,56.3232" ) );
        assertFalse( coordinateIsValid( "23.34343 56.3232" ) );
        assertFalse( coordinateIsValid( "[23.34f43,56.3232]" ) );
        assertFalse( coordinateIsValid( "23.34343,56.323.2" ) );
        assertFalse( coordinateIsValid( "[23.34343,56..3232]" ) );
        assertFalse( coordinateIsValid( "[++37,-28.94221]" ) );
        assertFalse( coordinateIsValid( "S-0.27726 E37.08472" ) );
        assertFalse( coordinateIsValid( null ) );
                
        assertFalse( coordinateIsValid( "-185.12345,45.45423" ) );
        assertFalse( coordinateIsValid( "192.56789,-45.34332" ) );
        assertFalse( coordinateIsValid( "140.34,92.23323" ) );
        assertFalse( coordinateIsValid( "123.34,-94.23323" ) );
        assertFalse( coordinateIsValid( "000.34,-94.23323" ) );
        assertFalse( coordinateIsValid( "123.34,-00.23323" ) );
    }

    @Test
    public void testGetLongitude()
    {
        assertEquals( "+37.99034", getLongitude( "[+37.99034,-28.94221]" ) );
        assertEquals( "37.99034", getLongitude( "[37.99034,28.94221]" ) );
        assertNull( getLongitude( "23.34343,56.3232" ) );
        assertNull( getLongitude( null ) );
    }
    
    @Test
    public void testGetLatitude()
    {
        assertEquals( "-28.94221", getLatitude( "[+37.99034,-28.94221]" ) );
        assertEquals( "28.94221", getLatitude( "[37.99034,28.94221]" ) );
        assertNull( getLatitude( "23.34343,56.3232" ) );
        assertNull( getLatitude( null ) );
    }
}
