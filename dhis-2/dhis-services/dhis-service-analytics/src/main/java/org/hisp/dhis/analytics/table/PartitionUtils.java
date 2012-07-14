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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hisp.dhis.analytics.AnalyticsTableManager;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.YearlyPeriodType;
import org.hisp.dhis.system.util.ListMap;

public class PartitionUtils
{
    private static final YearlyPeriodType PERIODTYPE = new YearlyPeriodType();
    
    private static final String SEP = "_";

    public static List<String> getTempTableNames( Date earliest, Date latest, String tableName )
    {   
        if ( earliest == null || latest == null )
        {
            return new ArrayList<String>( Arrays.asList( tableName + AnalyticsTableManager.TABLE_TEMP_SUFFIX ) );
        }
        
        if ( earliest.after( latest ) )
        {
            throw new IllegalArgumentException( "Earliest date is after latest: " + earliest + ", " + latest );
        }
        
        List<String> tables = new ArrayList<String>();
        
        Period period = PERIODTYPE.createPeriod( earliest );
        
        while ( period != null && period.getStartDate().before( latest ) )
        {
            String table = tableName + AnalyticsTableManager.TABLE_TEMP_SUFFIX + SEP + period.getIsoDate();
            
            tables.add( table );
            
            period = PERIODTYPE.getNextPeriod( period );
        }
        
        return tables;
    }
    
    public static String getTable( Period period, String tableName )
    {
        Period quarter = PERIODTYPE.createPeriod( period.getStartDate() );
        
        return tableName + SEP + quarter.getIsoDate();
    }
    
    public static Period getPeriod( String tableName )
    {
        if ( tableName == null || tableName.indexOf( SEP ) == -1 )
        {
            return null;
        }
        
        String[] split = tableName.split( SEP );
        String isoPeriod = split[split.length - 1];
        
        return PeriodType.getPeriodFromIsoString( isoPeriod );
    }
    
    public static ListMap<String, IdentifiableObject> getTablePeriodMap( Collection<IdentifiableObject> periods, String tableName )
    {
        ListMap<String, IdentifiableObject> map = new ListMap<String, IdentifiableObject>();
        
        for ( IdentifiableObject period : periods )
        {
            map.putValue( getTable( (Period) period, tableName ), period );
        }
        
        return map;
    }
}
