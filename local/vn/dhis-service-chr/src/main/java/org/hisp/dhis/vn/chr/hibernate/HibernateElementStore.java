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
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.ElementStore;
import org.hisp.dhis.vn.chr.Form;
import org.hisp.dhis.vn.chr.comparator.ElementNameComparator;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class HibernateElementStore
    implements ElementStore
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

    public int addElement( Element element )
    {

        Session session = sessionFactory.getCurrentSession();

        String name = element.getName().toLowerCase();

        element.setName( name );

        return (Integer) session.save( element );
    }

    public void deleteElement( int id )
    {

        Session session = sessionFactory.getCurrentSession();

        session.delete( getElement( id ) );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Element> getAllElements()
    {

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( Element.class );

        List<Element> list = criteria.list();

        Collections.sort( list, new ElementNameComparator() );

        return list;

    }

    public Element getElement( int id )
    {
        Session session = sessionFactory.getCurrentSession();

        return (Element) session.get( Element.class, id );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Element> getElementsByForm( Form form )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( Element.class );

        criteria.add( Restrictions.eq( "form", form ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Element> getElementsByEgroup( Egroup egroup )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( Element.class );

        criteria.add( Restrictions.eq( "egroup", egroup ) );

        return criteria.list();
    }

    public void updateElement( Element element )
    {
        Session session = sessionFactory.getCurrentSession();

        session.update( element );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Element> getElementsByNoEgroup()
    {

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( Element.class );

        criteria.add( Restrictions.eq( "egroup", null ) );

        return criteria.list();
    }

    public Element getElement( String name )
    {

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( Element.class );

        criteria.add( Restrictions.eq( "name", name ) );

        return (Element) criteria.uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Element> getElementsByFormLink( Form form )
    {

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( Element.class );

        // criteria.add(Restrictions.eq("formLink", formId));
        criteria.add( Restrictions.eq( "formLink", form ) );

        return criteria.list();
    }
}
