package org.hisp.dhis.coldchain.inventory.hibernate;

import java.util.Collection;

import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeStore;
import org.hisp.dhis.common.hibernate.HibernateIdentifiableObjectStore;
//public class HibernateInventoryTypeStore implements InventoryTypeStore
public class HibernateInventoryTypeStore extends HibernateIdentifiableObjectStore<InventoryType> implements InventoryTypeStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
   /*
    private SessionFactory sessionFactory;

    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }
    */
    // -------------------------------------------------------------------------
    // InventoryTypeStore
    // -------------------------------------------------------------------------
    /*
    @Override
    public int addInventoryType( InventoryType inventoryType )
    {
        Session session = sessionFactory.getCurrentSession();

        return (Integer) session.save( inventoryType );
    }

    @Override
    public void deleteInventoryType( InventoryType inventoryType )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( inventoryType );
    }

    @Override
    public Collection<InventoryType> getAllInventoryTypes()
    {
        Session session = sessionFactory.getCurrentSession();

        return session.createCriteria( InventoryType.class ).list();
    }

    @Override
    public void updateInventoryType( InventoryType inventoryType )
    {
        Session session = sessionFactory.getCurrentSession();

        session.update( inventoryType );
    }

    public InventoryType getInventoryTypeByName( String name )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( InventoryType.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (InventoryType) criteria.uniqueResult();
    }

    public InventoryType getInventoryType( int id )
    {
        Session session = sessionFactory.getCurrentSession();

        return (InventoryType) session.get( InventoryType.class, id );
    }
    */
    
    // -------------------------------------------------------------------------
    // InventoryTypeStore
    // -------------------------------------------------------------------------
    
    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<InventoryType> getAllInventoryTypes()
    {
        return sessionFactory.getCurrentSession().createCriteria( InventoryType.class ).list();
    }
    @Override
    public InventoryType getInventoryTypeByName( String name )
    {
        return (InventoryType) getCriteria( Restrictions.eq( "name", name ) ).uniqueResult();
    }
    @Override
    public InventoryType getInventoryType( int id )
    {
        return (InventoryType) sessionFactory.getCurrentSession().get( InventoryType.class, id );
    }
    /*
    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<InventoryTypeAttribute> getAllInventoryTypeAttributesForDisplay( InventoryType inventoryType )
    {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria( InventoryTypeAttribute.class );
        criteria.setProjection( Projections.property( "display" ) );
        return criteria.list();
    }
    */
    
}
