package org.hisp.dhis.hibernate;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.common.AccessStringHelper;
import org.hisp.dhis.common.AuditLogUtil;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.GenericNameableObjectStore;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.SharingUtils;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Lars Helge Overland
 */
public class HibernateGenericStore<T>
    implements GenericNameableObjectStore<T>
{
    private static final Log log = LogFactory.getLog( HibernateGenericStore.class );

    protected SessionFactory sessionFactory;

    @Required
    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    protected JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private CurrentUserService currentUserService;

    private Class<T> clazz;

    /**
     * Could be overridden programmatically.
     */
    public Class<T> getClazz()
    {
        return clazz;
    }

    /**
     * Could be injected through container.
     */
    @Required
    public void setClazz( Class<T> clazz )
    {
        this.clazz = clazz;
    }

    private boolean cacheable = false;

    /**
     * Could be overridden programmatically.
     */
    protected boolean isCacheable()
    {
        return cacheable;
    }

    /**
     * Could be injected through container.
     */
    public void setCacheable( boolean cacheable )
    {
        this.cacheable = cacheable;
    }

    // -------------------------------------------------------------------------
    // Convenience methods
    // -------------------------------------------------------------------------

    /**
     * Creates a Query.
     *
     * @param hql the hql query.
     * @return a Query instance.
     */
    protected final Query getQuery( String hql )
    {
        return sessionFactory.getCurrentSession().createQuery( hql ).setCacheable( cacheable );
    }

    /**
     * Creates a SqlQuery.
     *
     * @param sql the sql query.
     * @return a SqlQuery instance.
     */
    protected final SQLQuery getSqlQuery( String sql )
    {
        SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery( sql );
        query.setCacheable( cacheable );
        return query;
    }

    /**
     * Creates a Criteria for the implementation Class type.
     *
     * @return a Criteria instance.
     */
    protected final Criteria getCriteria()
    {
        return getClazzCriteria().setCacheable( cacheable );
    }

    protected Criteria getClazzCriteria()
    {
        return sessionFactory.getCurrentSession().createCriteria( getClazz() );
    }

    /**
     * Creates a Criteria for the implementation Class type restricted by the
     * given Criterions.
     *
     * @param expressions the Criterions for the Criteria.
     * @return a Criteria instance.
     */
    protected final Criteria getCriteria( Criterion... expressions )
    {
        Criteria criteria = getCriteria();

        for ( Criterion expression : expressions )
        {
            criteria.add( expression );
        }

        return criteria;
    }

    /**
     * Retrieves an object based on the given Criterions.
     *
     * @param expressions the Criterions for the Criteria.
     * @return an object of the implementation Class type.
     */
    @SuppressWarnings("unchecked")
    protected final T getObject( Criterion... expressions )
    {
        return (T) getCriteria( expressions ).uniqueResult();
    }

    /**
     * Retrieves a List based on the given Criterions.
     *
     * @param expressions the Criterions for the Criteria.
     * @return a List with objects of the implementation Class type.
     */
    @SuppressWarnings("unchecked")
    protected final List<T> getList( Criterion... expressions )
    {
        return getCriteria( expressions ).list();
    }

    // -------------------------------------------------------------------------
    // GenericIdentifiableObjectStore implementation
    // -------------------------------------------------------------------------

    @Override
    public int save( T object )
    {
        if ( !isWriteAllowed( object ) )
        {
            AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_CREATE_DENIED );
            throw new AccessDeniedException( "You do not have write access to object." );
        }

        if ( currentUserService.getCurrentUser() != null && SharingUtils.isSupported( clazz ) )
        {
            BaseIdentifiableObject identifiableObject = (BaseIdentifiableObject) object;

            if ( identifiableObject.getUser() == null )
            {
                identifiableObject.setUser( currentUserService.getCurrentUser() );
            }

            if ( SharingUtils.canCreatePublic( currentUserService.getCurrentUser(), identifiableObject ) )
            {
                String build = AccessStringHelper.newInstance()
                    .enable( AccessStringHelper.Permission.READ )
                    .enable( AccessStringHelper.Permission.WRITE )
                    .build();

                identifiableObject.setPublicAccess( build );
            }
            else if ( SharingUtils.canCreatePrivate( currentUserService.getCurrentUser(), identifiableObject ) )
            {
                identifiableObject.setPublicAccess( AccessStringHelper.newInstance().build() );
            }
            else
            {
                AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_CREATE_DENIED );
                throw new AccessDeniedException( "You are not allowed to create public or private objects of this kind." );
            }
        }

        AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_CREATE );
        return (Integer) sessionFactory.getCurrentSession().save( object );
    }

    @Override
    public void update( T object )
    {
        if ( !isUpdateAllowed( object ) )
        {
            AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_UPDATE_DENIED );
            throw new AccessDeniedException( "You do not have update access to object." );
        }

        AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_UPDATE );
        sessionFactory.getCurrentSession().update( object );
    }

    @Override
    @SuppressWarnings("unchecked")
    public final T get( int id )
    {
        T object = (T) sessionFactory.getCurrentSession().get( getClazz(), id );

        if ( !isReadAllowed( object ) )
        {
            AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ_DENIED );
            throw new AccessDeniedException( "You do not have read access to object with id " + id + "." );
        }

        // AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ );
        return object;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final T load( int id )
    {
        T object = (T) sessionFactory.getCurrentSession().load( getClazz(), id );

        if ( !isReadAllowed( object ) )
        {
            AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ_DENIED );
            throw new AccessDeniedException( "You do not have read access to object with id " + id );
        }

        // AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ );
        return object;
    }

    @Override
    public final T getByUid( String uid )
    {
        T object = getObject( Restrictions.eq( "uid", uid ) );

        if ( !isReadAllowed( object ) )
        {
            AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ_DENIED );
            throw new AccessDeniedException( "You do not have read access to object with uid " + uid );
        }

        // AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ );
        return object;
    }

    @Override
    @Deprecated
    public final T getByName( String name )
    {
        T object = getObject( Restrictions.eq( "name", name ) );

        if ( !isReadAllowed( object ) )
        {
            AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ_DENIED );
            throw new AccessDeniedException( "You do not have read access to object with name " + name );
        }

        // AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ );
        return object;
    }

    @Override
    @Deprecated
    public final T getByShortName( String shortName )
    {
        T object = getObject( Restrictions.eq( "shortName", shortName ) );

        if ( !isReadAllowed( object ) )
        {
            AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ_DENIED );
            throw new AccessDeniedException( "You do not have read access to object with shortName " + shortName );
        }

        // AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ );
        return object;
    }

    @Override
    public final T getByCode( String code )
    {
        T object = getObject( Restrictions.eq( "code", code ) );

        if ( !isReadAllowed( object ) )
        {
            AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ_DENIED );
            throw new AccessDeniedException( "You do not have read access to object with code " + code );
        }

        // AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_READ );
        return object;
    }

    @Override
    public final void delete( T object )
    {
        if ( !isDeleteAllowed( object ) )
        {
            AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_DELETE_DENIED );
            throw new AccessDeniedException( "You do not have delete access to this object." );
        }

        AuditLogUtil.infoWrapper( log, currentUserService.getCurrentUsername(), object, AuditLogUtil.ACTION_DELETE );
        sessionFactory.getCurrentSession().delete( object );
    }

    @Override
    @SuppressWarnings("unchecked")
    public final List<T> getAll()
    {
        Query query = sharingEnabled() ? getQueryAllACL() : getQueryAll();

        return query.list();
    }

    private Query getQueryAllACL()
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );

        return query;
    }

    private Query getQueryAll()
    {
        return getQuery( "from " + clazz.getName() + " c" );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllEqName( String name )
    {
        Query query = sharingEnabled() ? getQueryAllEqNameACL( name ) : getQueryAllEqName( name );

        return query.list();
    }

    private Query getQueryAllEqNameACL( String name )
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where name = :name and ( c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " ) order by c.name";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );
        query.setString( "name", name );

        return query;
    }

    private Query getQueryAllEqName( String name )
    {
        Query query = getQuery( "from " + clazz.getName() + " c where name = :name order by c.name" );
        query.setString( "name", name );

        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllEqNameIgnoreCase( String name )
    {
        Query query = sharingEnabled() ? getQueryAllEqNameACLIgnoreCase( name ) : getQueryAllEqNameIgnoreCase( name );

        return query.list();
    }

    private Query getQueryAllEqNameACLIgnoreCase( String name )
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where lower(name) = :name and ( c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " ) order by c.name";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );
        query.setString( "name", name.toLowerCase() );

        return query;
    }

    private Query getQueryAllEqNameIgnoreCase( String name )
    {
        Query query = getQuery( "from " + clazz.getName() + " c where lower(name) = :name order by c.name" );
        query.setString( "name", name.toLowerCase() );

        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllEqShortName( String shortName )
    {
        Query query = sharingEnabled() ? getQueryAllEqShortNameACL( shortName ) : getQueryAllEqShortName( shortName );

        return query.list();
    }

    private Query getQueryAllEqShortNameACL( String shortName )
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where shortName = :shortName and ( c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " ) order by c.shortName";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );
        query.setString( "shortName", shortName );

        return query;
    }

    private Query getQueryAllEqShortName( String shortName )
    {
        Query query = getQuery( "from " + clazz.getName() + " c where shortName = :shortName order by c.shortName" );
        query.setString( "shortName", shortName );

        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllEqShortNameIgnoreCase( String shortName )
    {
        Query query = sharingEnabled() ? getQueryAllEqShortNameACLIgnoreCase( shortName ) : getQueryAllEqShortNameIgnoreCase( shortName );

        return query.list();
    }

    private Query getQueryAllEqShortNameACLIgnoreCase( String shortName )
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where lower(shortName) = :shortName and ( c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " ) order by c.shortName";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );
        query.setString( "shortName", shortName.toLowerCase() );

        return query;
    }

    private Query getQueryAllEqShortNameIgnoreCase( String shortName )
    {
        Query query = getQuery( "from " + clazz.getName() + " c where lower(shortName) = :shortName order by c.shortName" );
        query.setString( "shortName", shortName.toLowerCase() );

        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllLikeName( String name )
    {
        Query query = sharingEnabled() ? getQueryAllLikeNameACL( name ) : getQueryAllLikeName( name );

        return query.list();
    }

    private Query getQueryAllLikeNameACL( String name )
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where lower(name) like :name and ( c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " ) order by c.name";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );
        query.setString( "name", "%" + name.toLowerCase() + "%" );

        return query;
    }

    private Query getQueryAllLikeName( String name )
    {
        Query query = getQuery( "from " + clazz.getName() + " c where lower(name) like :name order by c.name" );
        query.setString( "name", "%" + name.toLowerCase() + "%" );

        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final List<T> getAllOrderedName()
    {
        Query query = sharingEnabled() ? getQueryAllOrderedNameACL() : getQueryAllOrderedName();

        return query.list();
    }

    private Query getQueryAllOrderedNameACL()
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " order by c.name";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );

        return query;
    }

    private Query getQueryAllOrderedName()
    {
        return getQuery( "from " + clazz.getName() + " c order by c.name" );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllOrderedName( int first, int max )
    {
        Query query = sharingEnabled() ? getQueryAllOrderedNameACL() : getQueryAllOrderedName();

        query.setFirstResult( first );
        query.setMaxResults( max );

        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllOrderedLastUpdated( int first, int max )
    {
        Query query = sharingEnabled() ? getQueryAllOrderedLastUpdatedACL() : getQueryAllOrderedLastUpdated();

        query.setFirstResult( first );
        query.setMaxResults( max );

        return query.list();
    }

    private Query getQueryAllOrderedLastUpdatedACL()
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " order by c.lastUpdated desc";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );

        return query;
    }

    private Query getQueryAllOrderedLastUpdated()
    {
        return getQuery( "from " + clazz.getName() + " c order by lastUpdated desc" );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllLikeNameOrderedName( String name, int first, int max )
    {
        Query query = sharingEnabled() ? getQueryAllLikeNameOrderedNameACL( name ) : getQueryAllLikeNameOrderedName( name );

        query.setFirstResult( first );
        query.setMaxResults( max );

        return query.list();
    }

    private Query getQueryAllLikeNameOrderedNameACL( String name )
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where lower(c.name) like :name and ( c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " ) order by c.name";

        Query query = getQuery( hql );
        query.setString( "name", "%" + name.toLowerCase() + "%" );
        query.setEntity( "user", currentUserService.getCurrentUser() );

        return query;
    }

    private Query getQueryAllLikeNameOrderedName( String name )
    {
        Query query = getQuery( "from " + clazz.getName() + " c where lower(name) like :name order by name" );
        query.setString( "name", "%" + name.toLowerCase() + "%" );

        return query;
    }

    @Override
    public int getCount()
    {
        Query query = sharingEnabled() ? getQueryCountACL() : getQueryCount();

        return ((Long) query.uniqueResult()).intValue();
    }

    private Query getQueryCountACL()
    {
        String hql = "select count(distinct c) from " + clazz.getName() + " c"
            + " where c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );

        return query;
    }

    private Query getQueryCount()
    {
        return getQuery( "select count(distinct c) from " + clazz.getName() + " c" );
    }

    @Override
    public int getCountLikeName( String name )
    {
        Query query = sharingEnabled() ? getQueryCountLikeNameACL( name ) : getQueryCountLikeName( name );

        return ((Long) query.uniqueResult()).intValue();
    }

    private Query getQueryCountLikeNameACL( String name )
    {
        String hql = "select count(distinct c) from " + clazz.getName() + " c"
            + " where lower(name) like :name and (c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " )";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );
        query.setString( "name", "%" + name.toLowerCase() + "%" );

        return query;
    }

    private Query getQueryCountLikeName( String name )
    {
        Query query = getQuery( "select count(distinct c) from " + clazz.getName() + " c where lower(name) like :name" );
        query.setString( "name", "%" + name.toLowerCase() + "%" );

        return query;
    }

    @Override
    public long getCountGeLastUpdated( Date lastUpdated )
    {
        Query query = sharingEnabled() ? getQueryCountGeLastUpdatedACL( lastUpdated ) : getQueryCountGeLastUpdated( lastUpdated );

        return ((Long) query.uniqueResult()).intValue();
    }

    private Query getQueryCountGeLastUpdatedACL( Date lastUpdated )
    {
        String hql = "select count(distinct c) from " + clazz.getName() + " c"
            + " where c.lastUpdated >= :lastUpdated and (c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " )";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );
        query.setDate( "lastUpdated", lastUpdated );

        return query;
    }

    private Query getQueryCountGeLastUpdated( Date lastUpdated )
    {
        Query query = getQuery( "select count(distinct c) from " + clazz.getName() + " c where lastUpdated >= :lastUpdated" );
        query.setDate( "lastUpdated", lastUpdated );

        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllGeLastUpdated( Date lastUpdated )
    {
        Query query = sharingEnabled() ? getQueryAllGeLastUpdatedACL( lastUpdated ) : getQueryAllGeLastUpdated( lastUpdated );

        return query.list();
    }

    private Query getQueryAllGeLastUpdatedACL( Date lastUpdated )
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where c.lastUpdated >= :lastUpdated and ( c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " )";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );
        query.setDate( "lastUpdated", lastUpdated );

        return query;
    }

    private Query getQueryAllGeLastUpdated( Date lastUpdated )
    {
        Query query = getQuery( "from " + clazz.getName() + " c where c.lastUpdated >= :lastUpdated" );
        query.setDate( "lastUpdated", lastUpdated );

        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllGeCreated( Date created )
    {
        Query query = sharingEnabled() ? getQueryAllGeCreatedACL( created ) : getQueryAllGeCreated( created );

        return query.list();
    }

    private Query getQueryAllGeCreatedACL( Date created )
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where c.created >= :created and ( c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " ) order by c.name";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );
        query.setDate( "created", created );

        return query;
    }

    private Query getQueryAllGeCreated( Date created )
    {
        Query query = getQuery( "from " + clazz.getName() + " c where c.created >= :created" );
        query.setDate( "created", created );

        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAllGeLastUpdatedOrderedName( Date lastUpdated )
    {
        Query query = sharingEnabled() ? getQueryAllGeLastUpdatedOrderedNameACL( lastUpdated ) : getQueryAllGeLastUpdatedOrderedName( lastUpdated );

        return query.list();
    }

    private Query getQueryAllGeLastUpdatedOrderedNameACL( Date lastUpdated )
    {
        String hql = "select distinct c from " + clazz.getName() + " c"
            + " where c.lastUpdated >= :lastUpdated and ( c.publicAccess like 'r%' or c.user IS NULL or c.user=:user"
            + " or exists "
            + "     (from c.userGroupAccesses uga join uga.userGroup ug join ug.members ugm where ugm = :user and uga.access like 'r%')"
            + " ) order by c.name";

        Query query = getQuery( hql );
        query.setEntity( "user", currentUserService.getCurrentUser() );
        query.setDate( "lastUpdated", lastUpdated );

        return query;
    }

    private Query getQueryAllGeLastUpdatedOrderedName( Date lastUpdated )
    {
        Query query = getQuery( "from " + clazz.getName() + " c where c.lastUpdated >= :lastUpdated order by c.name" );
        query.setDate( "lastUpdated", lastUpdated );

        return query;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getByUser( User user )
    {
        Query query = getQuery( "from " + clazz.getName() + " c where user = :user" );
        query.setEntity( "user", user );

        return query.list();
    }

    @Override
    public List<T> getByUid( Collection<String> uids )
    {
        List<T> list = new ArrayList<T>();

        if ( uids != null )
        {
            for ( String uid : uids )
            {
                T object = getByUid( uid );

                if ( object != null )
                {
                    list.add( object );
                }
            }
        }

        return list;
    }

    //----------------------------------------------------------------------------------------------------------------
    // No ACL (unfiltered methods)
    //----------------------------------------------------------------------------------------------------------------

    @Override
    public int getCountEqNameNoAcl( String name )
    {
        Query query = getQuery( "select count(distinct c) from " + clazz.getName() + " c where c.name = :name" );
        query.setParameter( "name", name );

        return ((Long) query.uniqueResult()).intValue();
    }

    @Override
    public int getCountEqShortNameNoAcl( String shortName )
    {
        Query query = getQuery( "select count(distinct c) from " + clazz.getName() + " c where c.shortName = :shortName" );
        query.setParameter( "shortName", shortName );

        return ((Long) query.uniqueResult()).intValue();
    }

    //----------------------------------------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------------------------------------

    protected boolean sharingEnabled()
    {
        return SharingUtils.isSupported( clazz ) && !(currentUserService.getCurrentUser() == null ||
            currentUserService.getCurrentUser().getUserCredentials().getAllAuthorities().contains( SharingUtils.SHARING_OVERRIDE_AUTHORITY ));
    }

    protected boolean isReadAllowed( T object )
    {
        if ( IdentifiableObject.class.isInstance( object ) )
        {
            IdentifiableObject idObject = (IdentifiableObject) object;

            if ( sharingEnabled() )
            {
                return SharingUtils.canRead( currentUserService.getCurrentUser(), idObject );
            }
        }

        return true;
    }

    protected boolean isWriteAllowed( T object )
    {
        if ( IdentifiableObject.class.isInstance( object ) )
        {
            IdentifiableObject idObject = (IdentifiableObject) object;

            if ( sharingEnabled() )
            {
                return SharingUtils.canWrite( currentUserService.getCurrentUser(), idObject );
            }
        }

        return true;
    }

    protected boolean isUpdateAllowed( T object )
    {
        if ( IdentifiableObject.class.isInstance( object ) )
        {
            IdentifiableObject idObject = (IdentifiableObject) object;

            if ( SharingUtils.isSupported( clazz ) )
            {
                return SharingUtils.canUpdate( currentUserService.getCurrentUser(), idObject );
            }
        }

        return true;
    }

    protected boolean isDeleteAllowed( T object )
    {
        if ( IdentifiableObject.class.isInstance( object ) )
        {
            IdentifiableObject idObject = (IdentifiableObject) object;

            if ( SharingUtils.isSupported( clazz ) )
            {
                return SharingUtils.canDelete( currentUserService.getCurrentUser(), idObject );
            }
        }

        return true;
    }
}
