package org.hisp.dhis.vn.chr.hibernate;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.vn.chr.Egroup;
import org.hisp.dhis.vn.chr.EgroupStore;
import org.hisp.dhis.vn.chr.Form;
import org.hisp.dhis.vn.chr.comparator.EgroupNameComparator;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class HibernateEgroupStore
    implements EgroupStore
{

    // -----------------------------------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------------------------------

    private SessionFactory sessionFactory;

    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    // -----------------------------------------------------------------------------------------------
    // Implements
    // -----------------------------------------------------------------------------------------------

    public int addEgroup( Egroup egroup )
    {

        Session session = sessionFactory.getCurrentSession();

        String name = egroup.getName().toLowerCase();

        egroup.setName( name );

        return (Integer) session.save( egroup );
    }

    public void deleteEgroup( int id )
    {

        Session session = sessionFactory.getCurrentSession();

        session.delete( getEgroup( id ) );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Egroup> getAllEgroups()
    {

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( Egroup.class );

        List<Egroup> list = criteria.list();

        Collections.sort( list, new EgroupNameComparator() );

        return list;
    }

    public Egroup getEgroup( int id )
    {

        Session session = sessionFactory.getCurrentSession();

        return (Egroup) session.get( Egroup.class, id );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Egroup> getEgroupsByForm( Form form )
    {

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( Egroup.class );

        criteria.add( Restrictions.eq( "form", form ) );

        return criteria.list();

    }

    public void updateEgroup( Egroup egroup )
    {

        Session session = sessionFactory.getCurrentSession();

        session.update( egroup );
    }

}
