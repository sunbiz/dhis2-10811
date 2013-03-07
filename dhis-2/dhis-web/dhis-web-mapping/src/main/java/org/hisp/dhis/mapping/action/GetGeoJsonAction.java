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

import static org.hisp.dhis.util.ContextUtils.clearIfNotModified;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.system.filter.OrganisationUnitWithValidCoordinatesFilter;
import org.hisp.dhis.system.util.FilterUtils;

import com.opensymphony.xwork2.Action;

/**
 * @author Jan Henrik Overland
 */
public class GetGeoJsonAction
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

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String parentId;
    
    public void setParentId( String parentId )
    {
        this.parentId = parentId;
    }

    private String level;

    public void setLevel( String level )
    {
        this.level = level;
    }
    
    private String callback;
    
    public void setCallback( String callback )
    {
        this.callback = callback;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------
    
    public String getCallback()
    {
        return callback;
    }

    private Collection<OrganisationUnit> object = new ArrayList<OrganisationUnit>();

    public Collection<OrganisationUnit> getObject()
    {
        return object;
    }
    
    private boolean hasCoordinatesUp;

    public boolean isHasCoordinatesUp()
    {
        return hasCoordinatesUp;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        OrganisationUnit parent = organisationUnitService.getOrganisationUnit( parentId );

        if ( parent == null )
        {
            parent = organisationUnitService.getOrganisationUnit( Integer.parseInt( parentId ) );
        }
        
        OrganisationUnitLevel orgUnitLevel = organisationUnitService.getOrganisationUnitLevel( level );

        if ( orgUnitLevel == null )
        {
            orgUnitLevel = organisationUnitService.getOrganisationUnitLevel( Integer.parseInt( level ) );
        }
        
        if ( orgUnitLevel == null )
        {
            orgUnitLevel = organisationUnitService.getOrganisationUnitLevel( parent.getOrganisationUnitLevel() );
        }
        
        Collection<OrganisationUnit> organisationUnits = organisationUnitService.getOrganisationUnitsAtLevel(
            orgUnitLevel.getLevel(), parent );

        FilterUtils.filter( organisationUnits, new OrganisationUnitWithValidCoordinatesFilter() );

        boolean modified = !clearIfNotModified( ServletActionContext.getRequest(), ServletActionContext.getResponse(), organisationUnits );

        if ( !modified )
        {
            return SUCCESS;
        }

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
        
        if ( orgUnitLevel.getLevel() > 1 )
        {
            Collection<OrganisationUnit> organisationUnitsUp = organisationUnitService.getOrganisationUnitsAtLevel( orgUnitLevel.getLevel() - 1 );
            
            FilterUtils.filter( organisationUnitsUp, new OrganisationUnitWithValidCoordinatesFilter() );
            
            hasCoordinatesUp = organisationUnitsUp.size() > 0;
        }

        return SUCCESS;
    }
}
