package org.hisp.dhis.coldchain.inventory.hibernate;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.coldchain.inventory.Equipment;
import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.EquipmentStore;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;

public class HibernateEquipmentStore implements EquipmentStore
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
    // EquipmentDetails
    // -------------------------------------------------------------------------
    
    @Override
    public void addEquipment( Equipment equipment )
    {
        Session session = sessionFactory.getCurrentSession();

        session.save( equipment );
    }

    @Override
    public void deleteEquipment( Equipment equipment )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( equipment );
    }

    @Override
    public Collection<Equipment> getAllEquipments()
    {
        Session session = sessionFactory.getCurrentSession();

        return session.createCriteria( Equipment.class ).list();
    }

    @Override
    public void updateEquipment( Equipment equipment )
    {
        Session session = sessionFactory.getCurrentSession();

        session.update( equipment );
    }
    
    
    public Collection<Equipment> getEquipments( EquipmentInstance equipmentInstance )
    {
        Session session = sessionFactory.getCurrentSession();
        
        Criteria criteria = session.createCriteria( Equipment.class );
        
        criteria.add( Restrictions.eq( "equipmentInstance", equipmentInstance ) );
        
        return criteria.list();
    }

    public Equipment getEquipment( EquipmentInstance equipmentInstance, InventoryTypeAttribute inventoryTypeAttribute )
    {
        Session session = sessionFactory.getCurrentSession();
        
        Criteria criteria = session.createCriteria( Equipment.class );
        
        criteria.add( Restrictions.eq( "equipmentInstance", equipmentInstance ) );
        criteria.add( Restrictions.eq( "inventoryTypeAttribute", inventoryTypeAttribute ) );
        
        return (Equipment) criteria.uniqueResult();
    }
    
}
