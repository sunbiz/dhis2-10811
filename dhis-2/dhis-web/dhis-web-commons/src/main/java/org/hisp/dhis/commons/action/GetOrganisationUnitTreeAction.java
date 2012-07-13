package org.hisp.dhis.commons.action;

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

import com.opensymphony.xwork2.Action;
import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.configuration.ConfigurationService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.system.util.CollectionUtils;
import org.hisp.dhis.system.util.functional.Predicate;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.version.Version;
import org.hisp.dhis.version.VersionService;

import java.util.*;

/**
 * @author mortenoh
 */
public class GetOrganisationUnitTreeAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private VersionService versionService;

    public void setVersionService( VersionService versionService )
    {
        this.versionService = versionService;
    }

    private ConfigurationService configurationService;

    public void setConfigurationService( ConfigurationService configurationService )
    {
        this.configurationService = configurationService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();

    public List<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    private List<OrganisationUnit> rootOrganisationUnits = new ArrayList<OrganisationUnit>();

    public List<OrganisationUnit> getRootOrganisationUnits()
    {
        return rootOrganisationUnits;
    }

    private String version;

    public String getVersion()
    {
        return version;
    }

    private Boolean versionOnly = false;

    public void setVersionOnly( Boolean versionOnly )
    {
        this.versionOnly = versionOnly;
    }

    public Boolean getVersionOnly()
    {
        return versionOnly;
    }

    private Integer parentId;

    public void setParentId( Integer parentId )
    {
        this.parentId = parentId;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        if ( parentId != null )
        {
            OrganisationUnit parent = organisationUnitService.getOrganisationUnit( parentId );

            if ( parent != null )
            {
                organisationUnits.addAll( organisationUnitService.getOrganisationUnitWithChildren( parent.getId() ) );
            }

            return "partial";
        }

        Collection<OrganisationUnit> userOrganisationUnits;

        User user = currentUserService.getCurrentUser();

        if ( user.getOrganisationUnits() != null && user.getOrganisationUnits().size() > 0 )
        {
            userOrganisationUnits = new ArrayList<OrganisationUnit>( user.getOrganisationUnits() );
            rootOrganisationUnits = new ArrayList<OrganisationUnit>( user.getOrganisationUnits() );
        }
        else
        {
            if ( user.getOrganisationUnits() != null && currentUserService.currentUserIsSuper() )
            {
                userOrganisationUnits = new ArrayList<OrganisationUnit>( organisationUnitService.getRootOrganisationUnits() );
                rootOrganisationUnits = new ArrayList<OrganisationUnit>( organisationUnitService.getRootOrganisationUnits() );
            }
            else
            {
                userOrganisationUnits = new ArrayList<OrganisationUnit>();
                rootOrganisationUnits = new ArrayList<OrganisationUnit>();
            }
        }

        if ( !versionOnly )
        {
            for ( OrganisationUnit unit : userOrganisationUnits )
            {
                organisationUnits.addAll( organisationUnitService.getOrganisationUnitWithChildren( unit.getId() ) );
            }

            OrganisationUnitLevel offlineOrganisationUnitLevel = configurationService.getConfiguration().getOfflineOrganisationUnitLevel();

            int size = organisationUnitService.getOrganisationUnitLevels().size();

            if ( offlineOrganisationUnitLevel == null )
            {
                offlineOrganisationUnitLevel = organisationUnitService.getOrganisationUnitLevelByLevel( size );
            }

            int minLevel = rootOrganisationUnits.get( 0 ).getLevel();
            int maxLevel = organisationUnitService.getOrganisationUnitLevelByLevel( size ).getLevel();
            int total = minLevel + offlineOrganisationUnitLevel.getLevel() - 1;

            if ( total > offlineOrganisationUnitLevel.getLevel() )
            {
                total = maxLevel;
            }

            final int finalTotal = total;

            CollectionUtils.filter( organisationUnits, new Predicate<OrganisationUnit>()
            {
                @Override
                public boolean evaluate( OrganisationUnit organisationUnit )
                {
                    return organisationUnit.getLevel() <= finalTotal;
                }
            } );
        }

        Collections.sort( rootOrganisationUnits, IdentifiableObjectNameComparator.INSTANCE );

        version = getVersionString();

        return SUCCESS;
    }

    private String getVersionString()
    {
        Version orgUnitVersion = versionService.getVersionByKey( VersionService.ORGANISATIONUNIT_VERSION );

        if ( orgUnitVersion == null )
        {
            String uuid = UUID.randomUUID().toString();
            orgUnitVersion = new Version();
            orgUnitVersion.setKey( VersionService.ORGANISATIONUNIT_VERSION );
            orgUnitVersion.setValue( uuid );
            versionService.addVersion( orgUnitVersion );
        }

        return orgUnitVersion.getValue();
    }
}
