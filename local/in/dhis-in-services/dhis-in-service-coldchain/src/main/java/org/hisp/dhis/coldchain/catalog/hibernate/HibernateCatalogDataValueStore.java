package org.hisp.dhis.coldchain.catalog.hibernate;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogDataValue;
import org.hisp.dhis.coldchain.catalog.CatalogDataValueStore;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOption;

public class HibernateCatalogDataValueStore implements CatalogDataValueStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SessionFactory sessionFactory;

    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }
    
    // -------------------------------------------------------------------------
    // CatalogDataValue
    // -------------------------------------------------------------------------

    @Override
    public void addCatalogDataValue( CatalogDataValue catalogDataValue )
    {
        Session session = sessionFactory.getCurrentSession();

        session.save( catalogDataValue );
    }

    @Override
    public void deleteCatalogDataValue( CatalogDataValue catalogDataValue )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( catalogDataValue );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<CatalogDataValue> getAllCatalogDataValues()
    {
        Session session = sessionFactory.getCurrentSession();

        return session.createCriteria( CatalogDataValue.class ).list();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<CatalogDataValue> getAllCatalogDataValuesByCatalog( Catalog catalog )
    {
        Session session = sessionFactory.getCurrentSession();
        
        Criteria criteria = session.createCriteria( CatalogDataValue.class );
        
        criteria.add( Restrictions.eq( "catalog", catalog ) );
        return criteria.list();
    }
    
    @Override
    public void updateCatalogDataValue( CatalogDataValue catalogDataValue )
    {
        Session session = sessionFactory.getCurrentSession();

        session.update( catalogDataValue );
    }
    
    @Override
    @SuppressWarnings( "unchecked" )
    public CatalogDataValue catalogDataValue( Catalog catalog ,CatalogTypeAttribute catalogTypeAttribute )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( CatalogDataValue.class );
        criteria.add( Restrictions.eq( "catalog", catalog ) );
        criteria.add( Restrictions.eq( "catalogTypeAttribute", catalogTypeAttribute ) );

        return (CatalogDataValue) criteria.uniqueResult();
    }
    
    @Override
    @SuppressWarnings( "unchecked" )
    public CatalogDataValue catalogDataValue( Catalog catalog ,CatalogTypeAttribute catalogTypeAttribute, CatalogTypeAttributeOption catalogTypeAttributeOption )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( CatalogDataValue.class );
        criteria.add( Restrictions.eq( "catalog", catalog ) );
        criteria.add( Restrictions.eq( "catalogTypeAttribute", catalogTypeAttribute ) );
        criteria.add( Restrictions.eq( "catalogTypeAttributeOption", catalogTypeAttributeOption ) );

        return (CatalogDataValue) criteria.uniqueResult();
    }
}
