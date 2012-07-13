package org.hisp.dhis.mapping.action;

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

import org.hisp.dhis.aggregation.AggregatedMapValue;
import org.hisp.dhis.mapping.MappingService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.system.filter.OrganisationUnitWithValidCoordinatesFilter;
import org.hisp.dhis.system.util.FilterUtils;

import com.opensymphony.xwork2.Action;

/**
 * @author Jan Henrik Overland
 * @version $Id$
 */
public class GetGeoJsonWithValuesAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    private MappingService mappingService;
    
    public void setMappingService( MappingService mappingService )
    {
        this.mappingService = mappingService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private Integer indicatorId;

    public void setIndicatorId( Integer indicatorId )
    {
        this.indicatorId = indicatorId;
    }
    
    private Integer dataElementId;

    public void setDataElementId( Integer dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    private Integer periodId;

    public void setPeriodId( Integer periodId )
    {
        this.periodId = periodId;
    }

    private Integer parentId;

    public void setParentId( Integer id )
    {
        this.parentId = id;
    }

    private Integer level;

    public void setLevel( Integer level )
    {
        this.level = level;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Collection<OrganisationUnit> object;

    public Collection<OrganisationUnit> getObject()
    {
        return object;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        OrganisationUnit parent = organisationUnitService.getOrganisationUnit( parentId );

        level = level == null ? organisationUnitService.getLevelOfOrganisationUnit( parent.getId() ) : level;

        Collection<OrganisationUnit> organisationUnits = organisationUnitService.getOrganisationUnitsAtLevel( level,
            parent );

        FilterUtils.filter( organisationUnits, new OrganisationUnitWithValidCoordinatesFilter() );

        object = new ArrayList<OrganisationUnit>();

        for ( OrganisationUnit unit : organisationUnits )
        {
            if ( !unit.getFeatureType().equals( OrganisationUnit.FEATURETYPE_POINT ) )
            {
                object.add( unit );
            }
        }

        for ( OrganisationUnit unit : organisationUnits )
        {
            if ( unit.getFeatureType().equals( OrganisationUnit.FEATURETYPE_POINT ) )
            {
                object.add( unit );
            }
        }
        
        Collection<AggregatedMapValue> values = new ArrayList<AggregatedMapValue>();
        
        if ( indicatorId != null )
        {
            values = mappingService.getIndicatorMapValues( indicatorId, periodId, object );
        }
        
        else if ( dataElementId != null )
        {
            values = mappingService.getDataElementMapValues( dataElementId, periodId, object );
        }
        
        if ( values != null )
        {
            for ( OrganisationUnit unit : object )
            {
                for ( AggregatedMapValue value : values )
                {
                    if ( unit.getId() == value.getOrganisationUnitId() )
                    {
                        unit.setValue( value.getValue() );
                    }
                }
            }
        }

        return SUCCESS;
    }
}
