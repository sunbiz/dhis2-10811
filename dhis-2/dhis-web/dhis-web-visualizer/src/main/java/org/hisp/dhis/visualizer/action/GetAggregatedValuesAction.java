package org.hisp.dhis.visualizer.action;

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
import java.util.HashSet;

import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.aggregation.AggregatedDataValue;
import org.hisp.dhis.aggregation.AggregatedDataValueService;
import org.hisp.dhis.aggregation.AggregatedIndicatorValue;
import org.hisp.dhis.aggregation.AggregatedOrgUnitDataValueService;
import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.api.utils.ContextUtils.CacheStrategy;
import org.hisp.dhis.completeness.DataSetCompletenessResult;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.system.util.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

import static org.hisp.dhis.api.utils.ContextUtils.CONTENT_TYPE_JSON;

/**
 * @author Jan Henrik Overland
 */
public class GetAggregatedValuesAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private AggregatedDataValueService aggregatedDataValueService;

    public void setAggregatedDataValueService( AggregatedDataValueService aggregatedDataValueService )
    {
        this.aggregatedDataValueService = aggregatedDataValueService;
    }

    private AggregatedOrgUnitDataValueService aggregatedOrgUnitDataValueService;

    public void setAggregatedOrgUnitDataValueService(
        AggregatedOrgUnitDataValueService aggregatedOrgUnitDataValueService )
    {
        this.aggregatedOrgUnitDataValueService = aggregatedOrgUnitDataValueService;
    }

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    @Autowired
    private ContextUtils contextUtils;

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Collection<Integer> indicatorIds;

    public void setIndicatorIds( Collection<Integer> indicatorIds )
    {
        this.indicatorIds = indicatorIds;
    }

    private Collection<Integer> dataElementIds;

    public void setDataElementIds( Collection<Integer> dataElementIds )
    {
        this.dataElementIds = dataElementIds;
    }

    private Collection<Integer> dataSetIds;

    public void setDataSetIds( Collection<Integer> dataSetIds )
    {
        this.dataSetIds = dataSetIds;
    }

    private Collection<Integer> periodIds;

    public void setPeriodIds( Collection<Integer> periodIds )
    {
        this.periodIds = periodIds;
    }

    private Collection<Integer> organisationUnitIds;

    public void setOrganisationUnitIds( Collection<Integer> organisationUnitIds )
    {
        this.organisationUnitIds = organisationUnitIds;
    }

    private Integer organisationUnitGroupSetId;

    public void setOrganisationUnitGroupSetId( Integer organisationUnitGroupSetId )
    {
        this.organisationUnitGroupSetId = organisationUnitGroupSetId;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Collection<AggregatedIndicatorValue> indicatorValues = new HashSet<AggregatedIndicatorValue>();

    public Collection<AggregatedIndicatorValue> getIndicatorValues()
    {
        return indicatorValues;
    }

    private Collection<AggregatedDataValue> dataElementValues = new HashSet<AggregatedDataValue>();

    public Collection<AggregatedDataValue> getDataElementValues()
    {
        return dataElementValues;
    }

    private Collection<DataSetCompletenessResult> dataSetValues = new HashSet<DataSetCompletenessResult>();

    public Collection<DataSetCompletenessResult> getDataSetValues()
    {
        return dataSetValues;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Org unit group set data
        // ---------------------------------------------------------------------

        if ( organisationUnitGroupSetId != null && periodIds != null && organisationUnitIds != null
            && organisationUnitIds.size() > 0 )
        {
            Integer organisationUnitId = organisationUnitIds.iterator().next();

            OrganisationUnitGroupSet groupSet = organisationUnitGroupService
                .getOrganisationUnitGroupSet( organisationUnitGroupSetId );

            if ( organisationUnitId == null || groupSet == null )
            {
                return SUCCESS;
            }

            Collection<Integer> groupIds = ConversionUtils.getIdentifiers( OrganisationUnitGroup.class,
                groupSet.getOrganisationUnitGroups() );

            if ( indicatorIds != null )
            {
                indicatorValues = aggregatedOrgUnitDataValueService.getAggregatedIndicatorValues( indicatorIds,
                    periodIds, organisationUnitId, groupIds );
            }

            if ( dataElementIds != null )
            {
                dataElementValues = aggregatedOrgUnitDataValueService.getAggregatedDataValueTotals( dataElementIds,
                    periodIds, organisationUnitId, groupIds );
            }

            if ( dataSetIds != null )
            {
                // FIXME will be implemented soon
            }
        }

        // ---------------------------------------------------------------------
        // Regular data
        // ---------------------------------------------------------------------

        else if ( periodIds != null && organisationUnitIds != null )
        {
            if ( indicatorIds != null )
            {
                indicatorValues = aggregatedDataValueService.getAggregatedIndicatorValues( indicatorIds, periodIds,
                    organisationUnitIds );
            }

            if ( dataElementIds != null )
            {
                dataElementValues = aggregatedDataValueService.getAggregatedDataValueTotals( dataElementIds, periodIds,
                    organisationUnitIds );
            }

            if ( dataSetIds != null )
            {
                dataSetValues = aggregatedDataValueService.getAggregatedDataSetCompleteness( dataSetIds, periodIds,
                    organisationUnitIds );
            }
        }

        contextUtils.configureResponse( ServletActionContext.getResponse(), CONTENT_TYPE_JSON,
            CacheStrategy.RESPECT_SYSTEM_SETTING, null, false );

        return SUCCESS;
    }
}
