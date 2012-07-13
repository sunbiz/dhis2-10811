package org.hisp.dhis.vn.chr.hibernate;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.vn.chr.Form;
import org.hisp.dhis.vn.chr.FormReport;
import org.hisp.dhis.vn.chr.FormReportStore;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class HibernateFormReportStore
    implements FormReportStore
{

    // -----------------------------------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------------------------------

    private SessionFactory sessionFactory;

    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    // -----------------------------------------------------------------------------------------------
    // Implements
    // -----------------------------------------------------------------------------------------------

    public int addFormReport( FormReport formReport )
    {

        Session session = sessionFactory.getCurrentSession();

        return (Integer) session.save( formReport );
    }

    public void updateFormReport( FormReport formReport )
    {

        Session session = sessionFactory.getCurrentSession();

        session.update( formReport );

    }

    public void deleteFormReport( int id )
    {

        Session session = sessionFactory.getCurrentSession();

        session.delete( getFormReport( id ) );

    }

    @SuppressWarnings( "unchecked" )
    public Collection<FormReport> getAllFormReports()
    {

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( FormReport.class );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<FormReport> getFormReports( Form form )
    {

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( FormReport.class );

        criteria.add( Restrictions.eq( "Form", form ) );

        return criteria.list();
    }

    public FormReport getFormReport( int id )
    {

        Session session = sessionFactory.getCurrentSession();

        return (FormReport) session.get( FormReport.class, id );
    }
}
