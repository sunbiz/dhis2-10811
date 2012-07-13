package org.hisp.dhis.aggregation;

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

/**
 * @author Lars Helge Overland
 */
public class AggregatedMapValue
{
    private int organisationUnitId;
    
    private String organisationUnitName;
    
    private int organisationUnitLevel;
    
    private int periodId;
    
    private String dataElementName;
    
    private double value;
    
    private double factor;
    
    private double numeratorValue;
    
    private double denominatorValue;
    
    // ----------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------
    
    public AggregatedMapValue()
    {   
    }
    
    public AggregatedMapValue( int organisationUnitId, String organisationUnitName, int periodId, String dataElementName, double value, double factor, double numeratorValue, double denominatorValue )
    {
        this.organisationUnitId = organisationUnitId;
        this.organisationUnitName = organisationUnitName;
        this.periodId = periodId;
        this.dataElementName = dataElementName;
        this.value = value;
        this.factor = factor;
        this.numeratorValue = numeratorValue;
        this.denominatorValue = denominatorValue;
    }

    // ----------------------------------------------------------------------
    // Logic
    // ----------------------------------------------------------------------
    
    public void clear()
    {
        this.organisationUnitId = 0;
        this.organisationUnitName = null;
        this.value = 0.0;
    }
    
    // ----------------------------------------------------------------------
    // Getters and setters
    // ----------------------------------------------------------------------
    
    public int getOrganisationUnitId()
    {
        return organisationUnitId;
    }

    public void setOrganisationUnitId( int organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }
    
    public String getOrganisationUnitName()
    {
        return organisationUnitName;
    }

    public void setOrganisationUnitName( String organisationUnitName )
    {
        this.organisationUnitName = organisationUnitName;
    }

    public int getOrganisationUnitLevel()
    {
        return organisationUnitLevel;
    }

    public void setOrganisationUnitLevel( int organisationUnitLevel )
    {
        this.organisationUnitLevel = organisationUnitLevel;
    }

    public int getPeriodId()
    {
        return periodId;
    }

    public void setPeriodId( int periodId )
    {
        this.periodId = periodId;
    }

    public String getDataElementName()
    {
        return dataElementName;
    }

    public void setDataElementName( String dataElementName )
    {
        this.dataElementName = dataElementName;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue( double value )
    {
        this.value = value;
    }
    
    public double getFactor()
    {
        return factor;
    }

    public void setFactor( double factor )
    {
        this.factor = factor;
    }

    public double getNumeratorValue()
    {
        return numeratorValue;
    }

    public void setNumeratorValue( double numeratorValue )
    {
        this.numeratorValue = numeratorValue;
    }

    public double getDenominatorValue()
    {
        return denominatorValue;
    }

    public void setDenominatorValue( double denominatorValue )
    {
        this.denominatorValue = denominatorValue;
    }    
}
