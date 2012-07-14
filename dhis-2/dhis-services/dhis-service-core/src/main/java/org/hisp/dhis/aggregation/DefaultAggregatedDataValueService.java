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

import java.util.Collection;

import org.hisp.dhis.completeness.DataSetCompletenessResult;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DeflatedDataValue;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 */
public class DefaultAggregatedDataValueService
    implements AggregatedDataValueService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private AggregatedDataValueStore aggregatedDataValueStore;

    public void setAggregatedDataValueStore( AggregatedDataValueStore aggregatedDataValueStore )
    {
        this.aggregatedDataValueStore = aggregatedDataValueStore;
    }

    // -------------------------------------------------------------------------
    // AggregatedDataValue
    // -------------------------------------------------------------------------
    
    public Double getAggregatedDataValue( int dataElement, int period, int organisationUnit )
    {
        return aggregatedDataValueStore.getAggregatedDataValue( dataElement, period, organisationUnit );
    }
    
    public Double getAggregatedValue( DataElement dataElement, Period period, OrganisationUnit organisationUnit )
    {
        return aggregatedDataValueStore.getAggregatedDataValue( dataElement.getId(), period.getId(), organisationUnit.getId() );
    }
    
    public Double getAggregatedDataValue( int dataElement, int categoryOptionCombo, int period, int organisationUnit )
    {
        return aggregatedDataValueStore.getAggregatedDataValue( dataElement, categoryOptionCombo, period, organisationUnit );
    }
    
    public Double getAggregatedValue( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo, Period period, OrganisationUnit organisationUnit )
    {
        return aggregatedDataValueStore.getAggregatedDataValue( dataElement.getId(), categoryOptionCombo.getId(), period.getId(), organisationUnit.getId() );
    }

    public Double getAggregatedValue( int dataElement, int categoryOptionCombo, Collection<Integer> periodIds, int organisationUnit )
    {
        return aggregatedDataValueStore.getAggregatedDataValue( dataElement, categoryOptionCombo, periodIds, organisationUnit );
    }
    
    public Double getAggregatedValue( DataElement dataElement, DataElementCategoryOption categoryOption, Period period, OrganisationUnit organisationUnit )
    {
        return aggregatedDataValueStore.getAggregatedDataValue( dataElement, categoryOption, period, organisationUnit );
    }
    
    public Collection<AggregatedDataValue> getAggregatedDataValues( Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        return aggregatedDataValueStore.getAggregatedDataValues( periodIds, organisationUnitIds );
    }
    
    public Collection<AggregatedDataValue> getAggregatedDataValueTotals( Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        return aggregatedDataValueStore.getAggregatedDataValueTotals( periodIds, organisationUnitIds );
    }
    
    public Collection<AggregatedDataValue> getAggregatedDataValues( int dataElementId, Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        return aggregatedDataValueStore.getAggregatedDataValues( dataElementId, periodIds, organisationUnitIds );
    }
    
    public Collection<AggregatedDataValue> getAggregatedDataValues( Collection<Integer> dataElementIds, Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        return aggregatedDataValueStore.getAggregatedDataValues( dataElementIds, periodIds, organisationUnitIds );
    }

    public Collection<AggregatedDataValue> getAggregatedDataValueTotals( Collection<Integer> dataElementIds, Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        return aggregatedDataValueStore.getAggregatedDataValueTotals( dataElementIds, periodIds, organisationUnitIds );
    }
    
    public void deleteAggregatedDataValues( Collection<Integer> dataElementIds, Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        aggregatedDataValueStore.deleteAggregatedDataValues( dataElementIds, periodIds, organisationUnitIds );
    }
    
    public void deleteAggregatedDataValues()
    {
        aggregatedDataValueStore.deleteAggregatedDataValues();
    }

    public StoreIterator<AggregatedDataValue> getAggregateDataValuesAtLevel(OrganisationUnit orgunit, OrganisationUnitLevel level, Collection<Period> periods)
    {
       return aggregatedDataValueStore.getAggregatedDataValuesAtLevel(orgunit, level, periods);
    }

    public int countDataValuesAtLevel( OrganisationUnit orgunit, OrganisationUnitLevel level, Collection<Period> periods )
    {
        return aggregatedDataValueStore.countDataValuesAtLevel( orgunit, level, periods );
    }
        
    // -------------------------------------------------------------------------
    // AggregatedDataMapValue
    // -------------------------------------------------------------------------
    
    public Collection<AggregatedMapValue> getAggregatedDataMapValues( int dataElementId, int periodId, Collection<Integer> organisationUnitIds )
    {
        return aggregatedDataValueStore.getAggregatedDataMapValues( dataElementId, periodId, organisationUnitIds );
    }
    
    public Collection<AggregatedMapValue> getAggregatedDataMapValues( Collection<Integer> dataElementIds, int periodId, int organisationUnitId )
    {
        return aggregatedDataValueStore.getAggregatedDataMapValues( dataElementIds, periodId, organisationUnitId );
    }
    
    // -------------------------------------------------------------------------
    // AggregatedIndicatorValue
    // -------------------------------------------------------------------------
    
    public Double getAggregatedIndicatorValue( int indicator, int period, int organisationUnit )
    {
        return aggregatedDataValueStore.getAggregatedIndicatorValue( indicator, period, organisationUnit );
    }
    
    public Double getAggregatedValue( Indicator indicator, Period period, OrganisationUnit organisationUnit )
    {
        return aggregatedDataValueStore.getAggregatedIndicatorValue( indicator.getId(), period.getId(), organisationUnit.getId() );
    }
    
    public Collection<AggregatedIndicatorValue> getAggregatedIndicatorValues( Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        return aggregatedDataValueStore.getAggregatedIndicatorValues( periodIds, organisationUnitIds );
    }
    
    public Collection<AggregatedIndicatorValue> getAggregatedIndicatorValues( Collection<Integer> indicatorIds,
        Collection<Integer> periodIds, Collection<Integer> organisationUnitIds )
    {
        return aggregatedDataValueStore.getAggregatedIndicatorValues( indicatorIds, periodIds, organisationUnitIds );
    }
    
    public void deleteAggregatedIndicatorValues( Collection<Integer> indicatorIds, Collection<Integer> periodIds,
        Collection<Integer> organisationUnitIds )
    {
        aggregatedDataValueStore.deleteAggregatedIndicatorValues( indicatorIds, periodIds, organisationUnitIds );
    }
    
    public void deleteAggregatedIndicatorValues()
    {
        aggregatedDataValueStore.deleteAggregatedIndicatorValues();
    }

    public StoreIterator<AggregatedIndicatorValue> getAggregateIndicatorValuesAtLevel( OrganisationUnit orgunit, OrganisationUnitLevel level, Collection<Period> periods )
    {
        return aggregatedDataValueStore.getAggregatedIndicatorValuesAtLevel( orgunit, level, periods );
    }

    public int countIndicatorValuesAtLevel( OrganisationUnit orgunit, OrganisationUnitLevel level, Collection<Period> periods )
    {
        return aggregatedDataValueStore.countIndicatorValuesAtLevel( orgunit, level, periods );
    }
    
    // -------------------------------------------------------------------------
    // AggregatedIndicatorMapValue
    // -------------------------------------------------------------------------
    
    public Collection<AggregatedMapValue> getAggregatedIndicatorMapValues( int indicatorId, int periodId, Collection<Integer> organisationUnitIds )
    {
        return aggregatedDataValueStore.getAggregatedIndicatorMapValues( indicatorId, periodId, organisationUnitIds );
    }

    // -------------------------------------------------------------------------
    // AggregatedDataSetCompleteness
    // -------------------------------------------------------------------------
    
    public Collection<DataSetCompletenessResult> getAggregatedDataSetCompleteness( Collection<Integer> dataSetIds, Collection<Integer> periodIds,
        Collection<Integer> organisationUnitIds )
    {
        return aggregatedDataValueStore.getAggregatedDataSetCompleteness( dataSetIds, periodIds, organisationUnitIds );
    }
    
    // -------------------------------------------------------------------------
    // DataValue
    // -------------------------------------------------------------------------
    
    public Collection<DeflatedDataValue> getDeflatedDataValues( int dataElementId, int periodId, Collection<Integer> sourceIds )
    {
        return aggregatedDataValueStore.getDeflatedDataValues( dataElementId, periodId, sourceIds );
    }
    
    public DataValue getDataValue( int dataElementId, int categoryOptionComboId, int periodId, int sourceId )
    {
        return aggregatedDataValueStore.getDataValue( dataElementId, categoryOptionComboId, periodId, sourceId );
    }
}
