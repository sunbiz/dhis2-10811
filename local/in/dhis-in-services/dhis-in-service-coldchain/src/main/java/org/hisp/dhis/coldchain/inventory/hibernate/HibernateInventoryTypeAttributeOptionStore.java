package org.hisp.dhis.coldchain.inventory.hibernate;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOption;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOptionStore;

public class HibernateInventoryTypeAttributeOptionStore implements InventoryTypeAttributeOptionStore
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
    // InventoryTypeAttributeOption
    // -------------------------------------------------------------------------

    @Override
    public int addInventoryTypeAttributeOption( InventoryTypeAttributeOption inventoryTypeAttributeOption )
    {
        Session session = sessionFactory.getCurrentSession();

        return (Integer) session.save( inventoryTypeAttributeOption );
    }

    @Override
    public void deleteInventoryTypeAttributeOption( InventoryTypeAttributeOption inventoryTypeAttributeOption )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( inventoryTypeAttributeOption );
    }

    @Override
    public Collection<InventoryTypeAttributeOption> getAllInventoryTypeAttributeOptions()
    {
        Session session = sessionFactory.getCurrentSession();

        return session.createCriteria( InventoryTypeAttributeOption.class ).list();
    }

    @Override
    public void updateInventoryTypeAttributeOption( InventoryTypeAttributeOption inventoryTypeAttributeOption )
    {
        Session session = sessionFactory.getCurrentSession();

        session.update( inventoryTypeAttributeOption );
    }
    
    public InventoryTypeAttributeOption getInventoryTypeAttributeOption( int id )
    {
        Session session = sessionFactory.getCurrentSession();

        return (InventoryTypeAttributeOption) session.get( InventoryTypeAttributeOption.class, id );
    }

    public Collection<InventoryTypeAttributeOption> get( InventoryTypeAttribute inventoryTypeAttribute )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( InventoryTypeAttributeOption.class );
        criteria.add( Restrictions.eq( "inventoryTypeAttribute", inventoryTypeAttribute ) );

        return criteria.list();
    }

    public InventoryTypeAttributeOption get( InventoryTypeAttribute inventoryTypeAttribute, String name )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( InventoryTypeAttributeOption.class );
        criteria.add( Restrictions.eq( "inventoryTypeAttribute", inventoryTypeAttribute ) );
        criteria.add( Restrictions.eq( "name", name ) );

        return (InventoryTypeAttributeOption) criteria.uniqueResult();
    }
}
