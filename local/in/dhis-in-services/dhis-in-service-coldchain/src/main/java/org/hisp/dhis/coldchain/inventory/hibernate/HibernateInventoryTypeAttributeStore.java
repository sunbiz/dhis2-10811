package org.hisp.dhis.coldchain.inventory.hibernate;

import java.util.Collection;

import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeStore;
import org.hisp.dhis.common.hibernate.HibernateIdentifiableObjectStore;

public class HibernateInventoryTypeAttributeStore extends HibernateIdentifiableObjectStore<InventoryTypeAttribute> implements InventoryTypeAttributeStore
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
    // Dependencies
    // -------------------------------------------------------------------------
    /*
    @Override
    public int addInventoryTypeAttribute( InventoryTypeAttribute inventoryTypeAttribute )
    {
        Session session = sessionFactory.getCurrentSession();

        return (Integer) session.save( inventoryTypeAttribute );
    }

    @Override
    public void deleteInventoryTypeAttribute( InventoryTypeAttribute inventoryTypeAttribute )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( inventoryTypeAttribute );
    }

    @Override
    public Collection<InventoryTypeAttribute> getAllInventoryTypeAttributes()
    {
        Session session = sessionFactory.getCurrentSession();

        return session.createCriteria( InventoryTypeAttribute.class ).list();
    }

    @Override
    public void updateInventoryTypeAttribute( InventoryTypeAttribute inventoryTypeAttribute )
    {
        Session session = sessionFactory.getCurrentSession();

        session.update( inventoryTypeAttribute );
    }

    public InventoryTypeAttribute getInventoryTypeAttribute( int id )
    {
        Session session = sessionFactory.getCurrentSession();

        return (InventoryTypeAttribute) session.get( InventoryTypeAttribute.class, id );
    }
    
    public InventoryTypeAttribute getInventoryTypeAttributeByName( String name )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( InventoryTypeAttribute.class );
        criteria.add( Restrictions.eq( "name", name ) );

        return (InventoryTypeAttribute) criteria.uniqueResult();
    }
    */
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<InventoryTypeAttribute> getAllInventoryTypeAttributes()
    {
        return sessionFactory.getCurrentSession().createCriteria( InventoryTypeAttribute.class ).list();        
    }

    public InventoryTypeAttribute getInventoryTypeAttribute( int id )
    {
        return (InventoryTypeAttribute) sessionFactory.getCurrentSession().get( InventoryTypeAttribute.class, id );
    }
    
    public InventoryTypeAttribute getInventoryTypeAttributeByName( String name )
    {
        return (InventoryTypeAttribute) getCriteria( Restrictions.eq( "name", name ) ).uniqueResult();
        
    }
    /*
    @SuppressWarnings( "unchecked" )
    public Collection<InventoryTypeAttribute> getAllInventoryTypeAttributesForDisplay( InventoryTypeAttribute inventoryTypeAttribute )
    {
        //return sessionFactory.getCurrentSession().createCriteria( InventoryTypeAttribute.class,inventoryTypeAttribute.isDisplay() ).list();
        
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria( InventoryTypeAttribute.class );
        criteria.setProjection( Projections.property( "display" ) );
        return criteria.list();
    }
    */
    
}
