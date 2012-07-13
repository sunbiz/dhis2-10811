package org.hisp.dhis.coldchain.inventory.hibernate;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.EquipmentInstanceStore;
import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class HibernateEquipmentInstanceStore 
    extends HibernateGenericStore<EquipmentInstance>
    implements EquipmentInstanceStore
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
    // EquipmentInstance
    // -------------------------------------------------------------------------

    /*
    public int addEquipmentInstance( EquipmentInstance equipmentInstance )
    {
        Session session = sessionFactory.getCurrentSession();

        return (Integer) session.save( equipmentInstance );
    }

    
    public void deleteEquipmentInstance( EquipmentInstance equipmentInstance )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( equipmentInstance );
    }

    
    public Collection<EquipmentInstance> getAllEquipmentInstance()
    {
        Session session = sessionFactory.getCurrentSession();

        return session.createCriteria( EquipmentInstance.class ).list();
    }

    
    public void updateEquipmentInstance( EquipmentInstance equipmentInstance )
    {
        Session session = sessionFactory.getCurrentSession();

        session.update( equipmentInstance );
    }

    */
    
    @SuppressWarnings( "unchecked" )
    public Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit )
    {
        /*
        Session session = sessionFactory.getCurrentSession();
        
        Criteria criteria = session.createCriteria( InventoryTypeAttribute.class );
        criteria.add( Restrictions.eq( "organisationUnit", orgUnit ) );

        return criteria.list();
        */
        
        return getCriteria( Restrictions.eq( "organisationUnit", orgUnit ) ).list();
    }
    
    @SuppressWarnings( "unchecked" )
    public Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit, InventoryType inventoryType )
    {
        /*
        Session session = sessionFactory.getCurrentSession();
        
        Criteria criteria = session.createCriteria( InventoryTypeAttribute.class );
        criteria.add( Restrictions.eq( "organisationUnit", orgUnit ) );
        criteria.add( Restrictions.eq( "inventoryType", inventoryType ) );

        return criteria.list();
        */
        
        Criteria crit = getCriteria();
        Conjunction con = Restrictions.conjunction();

        con.add( Restrictions.eq( "organisationUnit", orgUnit ) );
        con.add( Restrictions.eq( "inventoryType", inventoryType ) );

        crit.add( con );

        return crit.list();
    }
    
    public int getCountEquipmentInstance( OrganisationUnit orgUnit, InventoryType inventoryType )
    {
        Number rs = (Number) getCriteria( Restrictions.eq( "organisationUnit", orgUnit ) ).add( Restrictions.eq( "inventoryType", inventoryType ) ).setProjection(
            Projections.rowCount() ).uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit, InventoryType inventoryType, int min, int max )
    {
        return getCriteria( Restrictions.eq( "organisationUnit", orgUnit ) ).add( Restrictions.eq( "inventoryType", inventoryType ) ).setFirstResult( min ).setMaxResults( max ).list();
    }

    public int getCountEquipmentInstance( OrganisationUnit orgUnit, InventoryType inventoryType, InventoryTypeAttribute inventoryTypeAttribute, String searchText )
    {
        String hql = "SELECT COUNT( DISTINCT ei ) FROM EquipmentInstance AS ei  " +
                        " WHERE ei IN ( SELECT ed.equipmentInstance FROM Equipment AS ed WHERE ed.inventoryTypeAttribute.id = "+ inventoryTypeAttribute.getId()+" AND ed.value LIKE '%" + searchText + "%' ) " +
                        " AND ei.organisationUnit.id = " + orgUnit.getId()  +
                        " AND ei.inventoryType.id = " + inventoryType.getId();

        
        Query query = getQuery( hql );

        Number rs = (Number) query.uniqueResult();

        return (rs != null) ? rs.intValue() : 0;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<EquipmentInstance> getEquipmentInstances( OrganisationUnit orgUnit, InventoryType inventoryType, InventoryTypeAttribute inventoryTypeAttribute, String searchText, int min, int max )
    {
        String hql = "SELECT DISTINCT ei FROM EquipmentInstance AS ei  " +
                        " WHERE ei IN ( SELECT ed.equipmentInstance FROM Equipment AS ed WHERE ed.inventoryTypeAttribute.id = "+ inventoryTypeAttribute.getId()+" AND ed.value like '%" + searchText + "%' ) " +
                        " AND ei.organisationUnit.id = " + orgUnit.getId()  +
                        " AND ei.inventoryType.id = " + inventoryType.getId();

        
        Query query = getQuery( hql ).setFirstResult( min ).setMaxResults( max );

        return query.list();
    }
    
}
