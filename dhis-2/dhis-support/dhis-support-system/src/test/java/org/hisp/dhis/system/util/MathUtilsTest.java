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
import static junit.framework.Assert.assertTrue;
import static org.hisp.dhis.expression.Operator.equal_to;
import static org.hisp.dhis.expression.Operator.greater_than;
import static org.hisp.dhis.expression.Operator.greater_than_or_equal_to;
import static org.hisp.dhis.expression.Operator.less_than;
import static org.hisp.dhis.expression.Operator.less_than_or_equal_to;
import static org.hisp.dhis.expression.Operator.not_equal_to;
import static org.hisp.dhis.system.util.MathUtils.expressionIsTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author Lars Helge Overland
 * @version $Id: MathUtil.java 4712 2008-03-12 10:32:45Z larshelg $
 */
public class MathUtilsTest
{
    @Test
    public void testExpressionIsTrue()
    {
        assertFalse( expressionIsTrue( 20.0, equal_to, 10.0 ) );
        assertTrue( expressionIsTrue( 20.0, not_equal_to, 10.0 ) );
        assertTrue( expressionIsTrue( 20.0, greater_than, 10.0 ) );
        assertTrue( expressionIsTrue( 20.0, greater_than_or_equal_to, 20.0 ) );
        assertFalse( expressionIsTrue( 30.0, less_than, 15.0 ) );
        assertTrue( expressionIsTrue( 40.0, less_than_or_equal_to, 50.0 ) );
        assertFalse( expressionIsTrue( 0.0, greater_than_or_equal_to, 20.0 ) );
    }
    
    @Test
    public void testGetMin()
    {
        double[] array = { 5.0, 2.0, 6.0, 12.0 };
        
        assertEquals( 2.0, MathUtils.getMin( array ) );
    }

    @Test
    public void testGetMax()
    {
        double[] array = { 5.0, 2.0, 12.0, 6.0 };
        
        assertEquals( 12.0, MathUtils.getMax( array ) );
    }

    @Test
    public void testRounding()
    {
        double[] numbers = { 34532.0, 23467000.0, 0.0034568 };
        String [] rounded = {"34530", "23470000", "0.003457" };

        for (int i=0; i < numbers.length; ++i)
        {
            assertEquals( rounded[i], MathUtils.roundToString( numbers[i], 4) );
        }
    }
    
    @Test
    public void testIsNumeric()
    {
        assertTrue( MathUtils.isNumeric( "123" ) );
        assertTrue( MathUtils.isNumeric( "0" ) );
        assertTrue( MathUtils.isNumeric( "1.2" ) );
        assertTrue( MathUtils.isNumeric( "12.34" ) );
        assertTrue( MathUtils.isNumeric( "0.0" ) );
        assertTrue( MathUtils.isNumeric( ".1" ) );
        assertTrue( MathUtils.isNumeric( "1.234" ) );
        assertTrue( MathUtils.isNumeric( "1234  " ) );
        assertTrue( MathUtils.isNumeric( "  1234" ) );

        assertFalse( MathUtils.isNumeric( "Hey" ) );
        assertFalse( MathUtils.isNumeric( "45 Perinatal Condition" ) );
        assertFalse( MathUtils.isNumeric( "Long street 2" ) );
        assertFalse( MathUtils.isNumeric( "1.2f" ) );
        assertFalse( MathUtils.isNumeric( "1 234" ) );
        assertFalse( MathUtils.isNumeric( "." ) );
        assertFalse( MathUtils.isNumeric( "1." ) );
        assertFalse( MathUtils.isNumeric( "" ) );
        assertFalse( MathUtils.isNumeric( " " ) );
        assertFalse( MathUtils.isNumeric( null ) );
    }
    
    @Test
    public void testGetAverage()
    {
        assertEquals( 7.5, MathUtils.getAverage( Arrays.asList( 5.0, 5.0, 10.0, 10.0 ) ) );
    }
}
