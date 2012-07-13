package org.hisp.dhis.coldchain.inventory.hibernate;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryType_Attribute;
import org.hisp.dhis.coldchain.inventory.InventoryType_AttributeStore;

/**
 * @author Mithilesh Kumar Thakur
 *
 * @version HibernateInventoryType_AttributeStore.java Jun 14, 2012 2:50:04 PM	
 */

//public class HibernateInventoryType_AttributeStore extends HibernateGenericStore<InventoryType_Attribute> implements InventoryType_AttributeStore
public class HibernateInventoryType_AttributeStore  implements InventoryType_AttributeStore
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
    // InventoryType_Attribute
    // -------------------------------------------------------------------------
    
    //add
    
    @Override
    public void addInventoryType_Attribute( InventoryType_Attribute inventoryType_Attribute )
    {
        Session session = sessionFactory.getCurrentSession();

        session.save( inventoryType_Attribute );
    }
    
    //update
    @Override
    public void updateInventoryType_Attribute( InventoryType_Attribute inventoryType_Attribute )
    {
        Session session = sessionFactory.getCurrentSession();

        session.update( inventoryType_Attribute );
    }
    
    //delete
    @Override
    public void deleteInventoryType_Attribute( InventoryType_Attribute inventoryType_Attribute )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( inventoryType_Attribute );
    }
    
    // get all
    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<InventoryType_Attribute> getAllInventoryTypeAttributes()
    {
        Session session = sessionFactory.getCurrentSession();

        return session.createCriteria( InventoryType_Attribute.class ).list();
    }

    @Override
    public InventoryType_Attribute getInventoryTypeAttribute( InventoryType inventoryType, InventoryTypeAttribute inventoryTypeAttribute )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( InventoryType_Attribute.class );
        criteria.add( Restrictions.eq( "inventoryType", inventoryType ) );
        criteria.add( Restrictions.eq( "inventoryTypeAttribute", inventoryTypeAttribute ) );

        return (InventoryType_Attribute) criteria.uniqueResult();
        
        //return (InventoryType_Attribute) getCriteria( Restrictions.eq( "inventoryType", inventoryType ),Restrictions.eq( "inventoryTypeAttribute", inventoryTypeAttribute ) ).uniqueResult();
    }
    
    @Override
    public InventoryType_Attribute getInventoryTypeAttributeForDisplay( InventoryType inventoryType, InventoryTypeAttribute inventoryTypeAttribute, boolean display)
    {
        
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( InventoryType_Attribute.class );
        criteria.add( Restrictions.eq( "inventoryType", inventoryType ) );
        criteria.add( Restrictions.eq( "inventoryTypeAttribute", inventoryTypeAttribute ) );
        criteria.add( Restrictions.eq( "display", display ) );
        
        return (InventoryType_Attribute) criteria.uniqueResult();
        
        //return (InventoryType_Attribute) getCriteria( Restrictions.eq( "inventoryType", inventoryType ), Restrictions.eq( "inventoryTypeAttribute", inventoryTypeAttribute ), Restrictions.eq( "display", display ) ).uniqueResult();
    }
    
    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<InventoryType_Attribute> getAllInventoryTypeAttributesByInventoryType( InventoryType inventoryType )
    {
        Session session = sessionFactory.getCurrentSession();
        
        Criteria criteria = session.createCriteria( InventoryType_Attribute.class );
        criteria.add( Restrictions.eq( "inventoryType", inventoryType ) );
        return criteria.list();
        
        //return getCriteria( Restrictions.eq( "inventoryType", inventoryType ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<InventoryType_Attribute> getAllInventoryTypeAttributeForDisplay( InventoryType inventoryType, boolean display )
    {
        Session session = sessionFactory.getCurrentSession();
        
        Criteria criteria = session.createCriteria( InventoryType_Attribute.class );
        criteria.add( Restrictions.eq( "inventoryType", inventoryType ) );
        criteria.add( Restrictions.eq( "display", display ) );
        return criteria.list();
        
        //return getCriteria( Restrictions.eq( "inventoryType", inventoryType ), Restrictions.eq( "display", display ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<InventoryTypeAttribute> getListInventoryTypeAttribute( InventoryType inventoryType )
    {
        Session session = sessionFactory.getCurrentSession();
        
        Criteria criteria = session.createCriteria( InventoryType_Attribute.class );
        
        criteria.add( Restrictions.eq( "inventoryType", inventoryType ) );
        criteria.setProjection( Projections.property( "inventoryTypeAttribute" ) );
        return criteria.list();
        
        /*
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria( getClazz() );
        criteria.add( Restrictions.eq( "inventoryType", inventoryType ) );
        criteria.setProjection( Projections.property( "inventoryTypeAttribute" ) );
        return criteria.list();
        */
    }
}


