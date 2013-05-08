package org.hisp.dhis.analytics.table;

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

import static org.hisp.dhis.DhisConvenienceTest.createPeriod;
import static org.hisp.dhis.analytics.AnalyticsTableManager.*;
import static org.hisp.dhis.common.IdentifiableObjectUtils.getList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.period.Cal;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.YearlyPeriodType;
import org.hisp.dhis.common.ListMap;
import org.junit.Test;

/**
 * @author Lars Helge Overland
 */
public class PartitionUtilsTest
{
    private static final String TABLE_NAME_TEMP = ANALYTICS_TABLE_NAME + TABLE_TEMP_SUFFIX;
    private static final String TABLE_NAME = ANALYTICS_TABLE_NAME;
    
    @Test
    public void testGetTableNames()
    {
        Cal cal = new Cal();
        Date earliest = cal.set( 2000, 5, 4 ).time();
        Date latest = cal.set( 2003, 2, 10 ).time();
        
        List<String> tables = PartitionUtils.getTempTableNames( earliest, latest, TABLE_NAME );
        
        assertEquals( 4, tables.size() );
        assertTrue( tables.contains( TABLE_NAME_TEMP + "_2000" ) );
        assertTrue( tables.contains( TABLE_NAME_TEMP + "_2001" ) );
        assertTrue( tables.contains( TABLE_NAME_TEMP + "_2002" ) );
        assertTrue( tables.contains( TABLE_NAME_TEMP + "_2003" ) );
    }
    
    @Test
    public void testGetTable()
    {
        assertEquals( TABLE_NAME + "_2000", PartitionUtils.getTableName( createPeriod( "200011" ), TABLE_NAME ) );
        assertEquals( TABLE_NAME + "_2001", PartitionUtils.getTableName( createPeriod( "2001W02" ), TABLE_NAME ) );
        assertEquals( TABLE_NAME + "_2002", PartitionUtils.getTableName( createPeriod( "2002Q2" ), TABLE_NAME ) );
        assertEquals( TABLE_NAME + "_2003", PartitionUtils.getTableName( createPeriod( "2003S2" ), TABLE_NAME ) );
    }
    
    @Test
    public void testGetPeriod()
    {
        Cal cal = new Cal();
        
        Period p1 = new YearlyPeriodType().createPeriod( cal.set( 2000, 4, 1 ).time() );
        Period p2 = new YearlyPeriodType().createPeriod( cal.set( 2001, 10, 1 ).time() );
        
        assertEquals( p1, PartitionUtils.getPeriod( TABLE_NAME_TEMP + "_2000" ) );
        assertEquals( p2, PartitionUtils.getPeriod( TABLE_NAME_TEMP + "_2001" ) );
    }
    
    @Test
    public void testGetTablePeriodMap()
    {        
        ListMap<String, IdentifiableObject> map = PartitionUtils.getTableNamePeriodMap( getList( 
            createPeriod( "2000S1" ), createPeriod( "2000S2" ), createPeriod( "2001S1" ), createPeriod( "2001S2" ), createPeriod( "2002S1" ) ), TABLE_NAME );
        
        assertEquals( 3, map.size() );
        
        assertTrue( map.keySet().contains( TABLE_NAME + "_2000" ) );
        assertTrue( map.keySet().contains( TABLE_NAME + "_2001" ) );
        assertTrue( map.keySet().contains( TABLE_NAME + "_2002" ) );
        
        assertEquals( 2, map.get( TABLE_NAME + "_2000" ).size() );
        assertEquals( 2, map.get( TABLE_NAME + "_2001" ).size() );
        assertEquals( 1, map.get( TABLE_NAME + "_2002" ).size() );
    }
}
