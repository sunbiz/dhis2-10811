package org.hisp.dhis.pivottable;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.aggregation.AggregatedIndicatorValue;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.AggregatedValue;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class PivotTable
{
    public static final String SEPARATOR = "-";
    
    private List<? extends BaseIdentifiableObject> indicators = new ArrayList<BaseIdentifiableObject>();
    
    private List<Period> periods = new ArrayList<Period>();
    
    private List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();
    
    private Collection<? extends AggregatedValue> indicatorValues = new ArrayList<AggregatedIndicatorValue>();

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public PivotTable()
    {   
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public Map<String, Double> getValueMap()
    {
        Map<String, Double> map = new HashMap<String, Double>();
        
        for ( AggregatedValue value : indicatorValues )
        {
            String key = value.getElementId() + SEPARATOR + value.getPeriodId() + SEPARATOR + value.getOrganisationUnitId();
            
            map.put( key, value.getValue() );
        }
        
        return map;
    }
    
    public static String getKey( IdentifiableObject element, Period period, OrganisationUnit unit )
    {
        String key = element.getId() + SEPARATOR + period.getId() + SEPARATOR + unit.getId();
        
        return key;
    }
    
    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public List<? extends BaseIdentifiableObject> getIndicators()
    {
        return indicators;
    }

    public void setIndicators( List<? extends BaseIdentifiableObject> indicators )
    {
        this.indicators = indicators;
    }

    public List<Period> getPeriods()
    {
        return periods;
    }

    public void setPeriods( List<Period> periods )
    {
        this.periods = periods;
    }

    public List<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setOrganisationUnits( List<OrganisationUnit> organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }

    public Collection<? extends AggregatedValue> getIndicatorValues()
    {
        return indicatorValues;
    }

    public void setIndicatorValues( Collection<? extends AggregatedValue> indicatorValues )
    {
        this.indicatorValues = indicatorValues;
    }
}
