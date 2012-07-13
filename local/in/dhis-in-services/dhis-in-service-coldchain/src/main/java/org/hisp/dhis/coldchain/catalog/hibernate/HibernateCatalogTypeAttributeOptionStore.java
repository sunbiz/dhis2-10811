package org.hisp.dhis.coldchain.catalog.hibernate;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOption;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOptionStore;

public class HibernateCatalogTypeAttributeOptionStore implements CatalogTypeAttributeOptionStore
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
    // CatalogTypeAttributeOption
    // -------------------------------------------------------------------------
    @Override
    public int addCatalogTypeAttributeOption( CatalogTypeAttributeOption catalogTypeAttributeOption )
    {
        Session session = sessionFactory.getCurrentSession();

        return (Integer) session.save( catalogTypeAttributeOption );
    }

    @Override
    public void deleteCatalogTypeAttributeOption( CatalogTypeAttributeOption catalogTypeAttributeOption )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( catalogTypeAttributeOption );
    }

    @Override
    public Collection<CatalogTypeAttributeOption> getAllCatalogTypeAttributeOptions()
    {
        Session session = sessionFactory.getCurrentSession();

        return session.createCriteria( CatalogTypeAttributeOption.class ).list();
    }

    @Override
    public void updateCatalogTypeAttributeOption( CatalogTypeAttributeOption catalogTypeAttributeOption )
    {
        Session session = sessionFactory.getCurrentSession();

        session.update( catalogTypeAttributeOption );
    }
    @Override
    public CatalogTypeAttributeOption getCatalogTypeAttributeOption( int id )
    {
        Session session = sessionFactory.getCurrentSession();

        return (CatalogTypeAttributeOption) session.get( CatalogTypeAttributeOption.class, id );
    }
    
    public int countByCatalogTypeAttributeoption( CatalogTypeAttributeOption catalogTypeAttributeOption )
    {
        
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( CatalogTypeAttributeOption.class );
        Number rs = (Number)criteria.add( Restrictions.eq( "catalogTypeAttributeOption", catalogTypeAttributeOption ) ).setProjection( Projections.rowCount() ).uniqueResult();
        return rs != null ? rs.intValue() : 0;
        
        /*
        Number rs = (Number) getCriteria( Restrictions.eq( "catalogTypeAttributeOption", catalogTypeAttributeOption ) ).setProjection(
            Projections.rowCount() ).uniqueResult();
        return rs != null ? rs.intValue() : 0;
        */
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<CatalogTypeAttributeOption> getCatalogTypeAttributeOptions( CatalogTypeAttribute catalogTypeAttribute )
    {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria( CatalogTypeAttributeOption.class );
        return criteria.add( Restrictions.eq( "catalogTypeAttribute", catalogTypeAttribute ) ).list();
        //return getCriteria( Restrictions.eq( "catalogTypeAttribute", catalogTypeAttribute ) ).list();
    }
    
    public CatalogTypeAttributeOption getCatalogTypeAttributeOptionName( CatalogTypeAttribute catalogTypeAttribute, String name )
    {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria( CatalogTypeAttributeOption.class );
        
        criteria.add( Restrictions.eq( "name", name ) );
       
        criteria.add( Restrictions.eq( "catalogTypeAttribute", catalogTypeAttribute ) );

        return (CatalogTypeAttributeOption) criteria.uniqueResult();
        
        /*
        return (CatalogTypeAttributeOption) getCriteria( Restrictions.eq( "name", name ),
            Restrictions.eq( "catalogTypeAttribute", catalogTypeAttribute ) ).uniqueResult();
       */
    }
    
}
