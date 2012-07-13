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

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.common.GenericNameableObjectStore;
import org.hisp.dhis.user.User;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class HibernateGenericStore<T>
    implements GenericNameableObjectStore<T>
{
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
     * Creates a Critera for the implementation Class type.
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
    @SuppressWarnings( "unchecked" )
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
    @SuppressWarnings( "unchecked" )
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
        return (Integer) sessionFactory.getCurrentSession().save( object );
    }

    @Override
    public void update( T object )
    {
        sessionFactory.getCurrentSession().update( object );
    }

    @Override
    public void saveOrUpdate( T object )
    {
        sessionFactory.getCurrentSession().saveOrUpdate( object );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final T get( int id )
    {
        return (T) sessionFactory.getCurrentSession().get( getClazz(), id );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final T load( int id )
    {
        return (T) sessionFactory.getCurrentSession().load( getClazz(), id );
    }
    
    @Override
    public final T getByUid( String uid )
    {
        return getObject( Restrictions.eq( "uid", uid ) );
    }

    @Override
    public final T getByName( String name )
    {
        return getObject( Restrictions.eq( "name", name ) );
    }

    @Override
    public final T getByAlternativeName( String alternativeName )
    {
        return getObject( Restrictions.eq( "alternativeName", alternativeName ) );
    }

    @Override
    public final T getByShortName( String shortName )
    {
        return getObject( Restrictions.eq( "shortName", shortName ) );
    }

    @Override
    public final T getByCode( String code )
    {
        return getObject( Restrictions.eq( "code", code ) );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<T> getLikeName( String name )
    {
        return getCriteria().add( Restrictions.ilike( "name", "%" + name + "%" ) ).list();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final Collection<T> getAll()
    {
        return getCriteria().list();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final Collection<T> getAllSorted()
    {
        return getCriteria().addOrder( Order.asc( "name" ) ).list();
    }

    @Override
    public final void delete( T object )
    {
        sessionFactory.getCurrentSession().delete( object );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<T> getBetween( int first, int max )
    {
        Criteria criteria = getCriteria();
        criteria.addOrder( Order.asc( "name" ) );
        criteria.setFirstResult( first );
        criteria.setMaxResults( max );
        return criteria.list();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<T> getBetweenOrderderByLastUpdated( int first, int max )
    {
        Criteria criteria = getCriteria();
        criteria.addOrder( Order.desc( "lastUpdated" ) );
        criteria.setFirstResult( first );
        criteria.setMaxResults( max );
        return criteria.list();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<T> getBetweenByName( String name, int first, int max )
    {
        Criteria criteria = getCriteria();
        criteria.add( Restrictions.ilike( "name", "%" + name + "%" ) );
        criteria.addOrder( Order.asc( "name" ) );
        criteria.setFirstResult( first );
        criteria.setMaxResults( max );
        return criteria.list();
    }

    @Override
    public int getCount()
    {
        Criteria criteria = getCriteria();
        criteria.setProjection( Projections.rowCount() );
        return ((Number) criteria.uniqueResult()).intValue();
    }

    @Override
    public int getCountByName( String name )
    {
        Criteria criteria = getCriteria();
        criteria.setProjection( Projections.rowCount() );
        criteria.add( Restrictions.ilike( "name", "%" + name + "%" ) );
        return ((Number) criteria.uniqueResult()).intValue();
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

    @Override
    @SuppressWarnings( "unchecked" )
    public List<T> getByLastUpdated( Date lastUpdated )
    {
        return getCriteria().add( Restrictions.ge( "lastUpdated", lastUpdated ) ).list();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<T> getByLastUpdatedSorted( Date lastUpdated )
    {
        return getCriteria().add( Restrictions.ge( "lastUpdated", lastUpdated ) ).addOrder( Order.asc( "name" ) ).list();
    }

    @Override
    public long getCountByLastUpdated( Date lastUpdated )
    {
        Object count  = getCriteria().add( Restrictions.ge( "lastUpdated", lastUpdated ) ).setProjection( Projections.rowCount() ).list().get( 0 );
        
        return count != null ? (Long) count : -1;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<T> getByUser( User user )
    {
        return getCriteria( Restrictions.eq( "user", user ) ).list();
    }
}
