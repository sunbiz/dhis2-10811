package org.hisp.dhis.organisationunit.hibernate;

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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.common.hibernate.HibernateIdentifiableObjectStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitHierarchy;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.organisationunit.OrganisationUnitStore;
import org.hisp.dhis.system.objectmapper.OrganisationUnitRelationshipRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * @author Kristian Nordal
 */
public class HibernateOrganisationUnitStore
    extends HibernateIdentifiableObjectStore<OrganisationUnit>
    implements OrganisationUnitStore
{
    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    @Override
    public OrganisationUnit getOrganisationUnitByNameIgnoreCase( String name )
    {
        return (OrganisationUnit) getCriteria( Restrictions.eq( "name", name ).ignoreCase() ).uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<OrganisationUnit> getRootOrganisationUnits()
    {
        return getQuery( "from OrganisationUnit o where o.parent is null" ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<OrganisationUnit> getOrganisationUnitsWithoutGroups()
    {
        return getQuery( "from OrganisationUnit o where o.groups.size = 0" ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<OrganisationUnit> getOrganisationUnitsByNameAndGroups( String name,
        Collection<OrganisationUnitGroup> groups, boolean limit )
    {
        boolean first = true;

        name = StringUtils.trimToNull( name );
        groups = CollectionUtils.isEmpty( groups ) ? null : groups;

        StringBuilder hql = new StringBuilder( "from OrganisationUnit o" );

        if ( name != null )
        {
            hql.append( " where lower(name) like :name" );

            first = false;
        }

        if ( groups != null )
        {
            for ( int i = 0; i < groups.size(); i++ )
            {
                String clause = first ? " where" : " and";

                hql.append( clause ).append( " :g" ).append( i ).append( " in elements( o.groups )" );

                first = false;
            }
        }

        Query query = sessionFactory.getCurrentSession().createQuery( hql.toString() );

        if ( name != null )
        {
            query.setString( "name", "%" + name.toLowerCase() + "%" );
        }

        if ( groups != null )
        {
            int i = 0;

            for ( OrganisationUnitGroup group : groups )
            {
                query.setEntity( "g" + i++, group );
            }
        }

        if ( limit )
        {
            query.setMaxResults( OrganisationUnitService.MAX_LIMIT );
        }

        return query.list();
    }

    public Map<Integer, Set<Integer>> getOrganisationUnitDataSetAssocationMap()
    {
        final String sql = "select datasetid, sourceid from datasetsource";

        final Map<Integer, Set<Integer>> map = new HashMap<Integer, Set<Integer>>();

        jdbcTemplate.query( sql, new RowCallbackHandler()
        {
            public void processRow( ResultSet rs ) throws SQLException
            {
                int dataSetId = rs.getInt( 1 );
                int organisationUnitId = rs.getInt( 2 );

                Set<Integer> dataSets = map.get( organisationUnitId );

                if ( dataSets == null )
                {
                    dataSets = new HashSet<Integer>();
                    map.put( organisationUnitId, dataSets );
                }

                dataSets.add( dataSetId );
            }
        } );

        return map;
    }

    public Set<Integer> getOrganisationUnitIdsWithoutData()
    {
        final String sql = "select organisationunitid from organisationunit ou where not exists (" +
            "select sourceid from datavalue where sourceid=ou.organisationunitid)";

        final Set<Integer> units = new HashSet<Integer>();

        jdbcTemplate.query( sql, new RowCallbackHandler()
        {
            public void processRow( ResultSet rs ) throws SQLException
            {
                units.add( rs.getInt( 1 ) );
            }
        } );

        return units;
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitHierarchy
    // -------------------------------------------------------------------------

    public OrganisationUnitHierarchy getOrganisationUnitHierarchy()
    {
        final String sql = "select organisationunitid, parentid from organisationunit";

        return new OrganisationUnitHierarchy( jdbcTemplate.query( sql, new OrganisationUnitRelationshipRowMapper() ) );
    }

    public void updateOrganisationUnitParent( int organisationUnitId, int parentId )
    {
        Timestamp now = new Timestamp( new Date().getTime() );

        final String sql = "update organisationunit " + "set parentid=" + parentId + ", lastupdated='"
            + now + "' " + "where organisationunitid=" + organisationUnitId;

        jdbcTemplate.execute( sql );
    }
}
