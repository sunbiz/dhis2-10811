package org.hisp.dhis.dxf2.datavalue;

/*
 * Copyright (c) 2011, University of Oslo
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

import static org.hisp.dhis.system.util.TextUtils.valueOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

public class StreamingCsvDataValue
    extends DataValue
{
    private CSVWriter writer;
    
    private List<String> values;
    
    public StreamingCsvDataValue( CSVWriter writer )
    {
        this.writer = writer;
        this.values = new ArrayList<String>();
    }
    
    public StreamingCsvDataValue( String[] row )
    {
        this.values = Arrays.asList( row );
    }
    
    //--------------------------------------------------------------------------
    // Getters
    //--------------------------------------------------------------------------

    @Override
    public String getDataElement()
    {
        return dataElement = dataElement == null ? values.get( 0 ) : dataElement;
    }

    @Override
    public String getPeriod()
    {
        return period = period == null ? values.get( 1 ) : period;
    }

    @Override
    public String getOrgUnit()
    {
        return orgUnit = orgUnit == null ? values.get( 2 ) : orgUnit;
    }

    @Override
    public String getCategoryOptionCombo()
    {
        return categoryOptionCombo = categoryOptionCombo == null ? values.get( 3 ) : categoryOptionCombo;
    }

    @Override
    public String getValue()
    {
        return value = value == null ? values.get( 4 ) : value;
    }

    @Override
    public String getStoredBy()
    {
        return storedBy = storedBy == null ? values.get( 5 ) : storedBy;
    }

    @Override
    public String getTimestamp()
    {
        return timestamp = timestamp == null ? values.get( 6 ) : timestamp;
    }

    @Override
    public String getComment()
    {
        return comment = comment == null ? values.get( 7 ) : comment;
    }

    @Override
    public Boolean getFollowup()
    {
        return followup = followup == null ? valueOf( values.get( 8 ) ) : followup;
    }

    //--------------------------------------------------------------------------
    // Setters
    //--------------------------------------------------------------------------

    @Override
    public void setDataElement( String dataElement )
    {
        values.add( dataElement );
    }

    @Override
    public void setPeriod( String period )
    {
        values.add( period );
    }

    @Override
    public void setOrgUnit( String orgUnit )
    {
        values.add( orgUnit );
    }

    @Override
    public void setCategoryOptionCombo( String categoryOptionCombo )
    {
        values.add( categoryOptionCombo );
    }

    @Override
    public void setValue( String value )
    {
        values.add( value );
    }

    @Override
    public void setStoredBy( String storedBy )
    {
        values.add( storedBy );
    }

    @Override
    public void setTimestamp( String timestamp )
    {
        values.add( timestamp );
    }

    @Override
    public void setComment( String comment )
    {
        values.add( comment );
    }

    @Override
    public void setFollowup( Boolean followup )
    {
        values.add( valueOf( followup ) );
    }

    @Override
    public void close()
    {
        String[] row = new String[values.size()];
        
        writer.writeNext( values.toArray( row ) );
    }
    
    public static String[] getHeaders()
    {
        String[] headers = { 
            "dataelement", "period", "orgunit", 
            "categoryoptioncombo", "value", "storedby", 
            "timestamp", "comment", "followup" };
        
        return headers;
    }
}
