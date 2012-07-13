package org.hisp.dhis.coldchain.inventory.hibernate;

import java.util.Collection;

import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.EquipmentStatus;
import org.hisp.dhis.coldchain.inventory.EquipmentStatusStore;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class HibernateEquipmentStatusStore 
    extends HibernateGenericStore<EquipmentStatus>
    implements EquipmentStatusStore
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
    // EquipmentWorkingStatus
    // -------------------------------------------------------------------------

    /*
    @Override
    public int addEquipmentStatus( EquipmentStatus equipmentStatus )
    {
        Session session = sessionFactory.getCurrentSession();

        return (Integer) session.save( equipmentStatus );
    }

    @Override
    public void deleteEquipmentStatus( EquipmentStatus equipmentStatus )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( equipmentStatus );
    }

    @Override
    public Collection<EquipmentStatus> getAllEquipmentStatus()
    {
        Session session = sessionFactory.getCurrentSession();

        return session.createCriteria( EquipmentStatus.class ).list();
    }

    @Override
    public void updateEquipmentStatus( EquipmentStatus equipmentStatus )
    {
        Session session = sessionFactory.getCurrentSession();

        session.update( equipmentStatus );
    }
    */
    
    @SuppressWarnings( "unchecked" )
    public Collection<EquipmentStatus> getEquipmentStatusHistory( EquipmentInstance equipmentInstance )
    {
        return getCriteria( Restrictions.eq( "equipmentInstance", equipmentInstance ) ).list();
    }
}
