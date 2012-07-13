package org.hisp.dhis.coldchain.catalog.hibernate;

import java.util.Collection;

import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeStore;
import org.hisp.dhis.common.hibernate.HibernateIdentifiableObjectStore;

//public class HibernateCatalogTypeAttributeStore implements CatalogTypeAttributeStore
public class HibernateCatalogTypeAttributeStore extends HibernateIdentifiableObjectStore<CatalogTypeAttribute> implements CatalogTypeAttributeStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    /*
    private SessionFactory sessionFactory;

    public void setSessionFactory( SessionFactory sessionFactory )o
    {
        this.sessionFactory = sessionFactory;
    }
    */
    // -------------------------------------------------------------------------
    // CatalogTypeAttribute
    // -------------------------------------------------------------------------

    /*
    @Override
    public void addCatalogTypeAttribute( CatalogTypeAttribute catalogTypeAttribute )
    {
        
        sessionFactory.getCurrentSession().save( catalogTypeAttribute );
       
        Session session = sessionFactory.getCurrentSession();

        return (Integer) session.save( catalogTypeAttribute );
        
    }

    @Override
    public void deleteCatalogTypeAttribute( CatalogTypeAttribute catalogTypeAttribute )
    {
        
        sessionFactory.getCurrentSession().delete( catalogTypeAttribute );
       
        Session session = sessionFactory.getCurrentSession();

        session.delete( catalogTypeAttribute );
        
    }
    
    @Override
    public void updateCatalogTypeAttribute( CatalogTypeAttribute catalogTypeAttribute )
    {
        sessionFactory.getCurrentSession().update( catalogTypeAttribute );
        
        Session session = sessionFactory.getCurrentSession();

        session.update( catalogTypeAttribute );
             
    }
    */
    @Override
    public CatalogTypeAttribute getCatalogTypeAttribute( int id )
    {
        return (CatalogTypeAttribute) sessionFactory.getCurrentSession().get( CatalogTypeAttribute.class, id );
        
        /*
        Session session = sessionFactory.getCurrentSession();

        return (CatalogTypeAttribute) session.get( CatalogTypeAttribute.class, id );
        */
    }

    @Override
    public CatalogTypeAttribute getCatalogTypeAttributeByName( String name )
    {
        return (CatalogTypeAttribute) getCriteria( Restrictions.eq( "name", name ) ).uniqueResult();
        
        /*
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( CatalogTypeAttribute.class );
        criteria.add( Restrictions.eq( "name", name ) );
        return (CatalogTypeAttribute) criteria.uniqueResult();
        */

    }
    @SuppressWarnings( "unchecked" )
    public Collection<CatalogTypeAttribute> getAllCatalogTypeAttributes()
    {
        return sessionFactory.getCurrentSession().createCriteria( CatalogTypeAttribute.class ).list();
        /*
        Session session = sessionFactory.getCurrentSession();

        return session.createCriteria( CatalogTypeAttribute.class ).list();
        */
    }
}
