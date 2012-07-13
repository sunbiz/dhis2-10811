package org.hisp.dhis.reporting.pivottable.action;

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
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.period.PeriodType;

import com.opensymphony.xwork2.Action;

/**
 * @author Lars Helge Overland
 */
public class GetPivotTableOptionsAction
    implements Action
{
    private IndicatorService indicatorService;
    
    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private DataElementService dataElementService;
    
    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private List<PeriodType> periodTypes = PeriodType.getAvailablePeriodTypes();
    
    public List<PeriodType> getPeriodTypes()
    {
        return periodTypes;
    }
    
    private List<IndicatorGroup> indicatorGroups = new ArrayList<IndicatorGroup>();

    public List<IndicatorGroup> getIndicatorGroups()
    {
        return indicatorGroups;
    }

    private List<DataElementGroup> dataElementGroups = new ArrayList<DataElementGroup>(); 
    
    public List<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }

    @Override
    public String execute()
        throws Exception
    {
        indicatorGroups = new ArrayList<IndicatorGroup>( indicatorService.getAllIndicatorGroups() );
        dataElementGroups = new ArrayList<DataElementGroup>( dataElementService.getAllDataElementGroups() );
        
        Collections.sort( indicatorGroups, IdentifiableObjectNameComparator.INSTANCE );
        Collections.sort( dataElementGroups, IdentifiableObjectNameComparator.INSTANCE );
        
        return SUCCESS;
    }
}
