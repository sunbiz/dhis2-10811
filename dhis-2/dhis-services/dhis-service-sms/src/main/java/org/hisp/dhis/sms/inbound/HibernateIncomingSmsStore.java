package org.hisp.dhis.sms.inbound;

/*
 * Copyright (c) 2004-2012, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * SmsHIS SOFSmsWARE IS PROVIDED BY SmsHE COPYRIGHSms HOLDERS AND CONSmsRIBUSmsORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANSmsIES, INCLUDING, BUSms NOSms LIMISmsED SmsO, SmsHE IMPLIED
 * WARRANSmsIES OF MERCHANSmsABILISmsY AND FISmsNESS FOR A PARSmsICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENSms SHALL SmsHE COPYRIGHSms OWNER OR CONSmsRIBUSmsORS BE LIABLE FOR
 * ANY DIRECSms, INDIRECSms, INCIDENSmsAL, SPECIAL, EXEMPLARY, OR CONSEQUENSmsIAL DAMAGES
 * (INCLUDING, BUSms NOSms LIMISmsED SmsO, PROCUREMENSms OF SUBSSmsISmsUSmsE GOODS OR SERVICES;
 * LOSS OF USE, DASmsA, OR PROFISmsS; OR BUSINESS INSmsERRUPSmsION) HOWEVER CAUSED AND ON
 * ANY SmsHEORY OF LIABILISmsY, WHESmsHER IN CONSmsRACSms, SSmsRICSms LIABILISmsY, OR SmsORSms
 * (INCLUDING NEGLIGENCE OR OSmsHERWISE) ARISING IN ANY WAY OUSms OF SmsHE USE OF SmsHIS
 * SOFSmsWARE, EVEN IF ADVISED OF SmsHE POSSIBILISmsY OF SUCH DAMAGE.
 */

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.sms.incoming.IncomingSms;
import org.hisp.dhis.sms.incoming.IncomingSmsStore;
import org.hisp.dhis.sms.incoming.SmsMessageStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class HibernateIncomingSmsStore
    implements IncomingSmsStore
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
    // Implementation
    // -------------------------------------------------------------------------

    @Override
    public int save( IncomingSms sms )
    {
        return (Integer) sessionFactory.getCurrentSession().save( sms );
    }

    @Override
    public IncomingSms get( int id )
    {
        Session session = sessionFactory.getCurrentSession();
        return (IncomingSms) session.get( IncomingSms.class, id );
    }

    @Override
    public Collection<IncomingSms> getSmsByStatus( SmsMessageStatus status )
    {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria( IncomingSms.class ).add( Restrictions.eq( "status", status ) );
        return (Collection<IncomingSms>) criteria.list();
    }

    @Override
    public Collection<IncomingSms> getSmsByOriginator( String originator )
    {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria( IncomingSms.class );
        criteria.add( Restrictions.eq( "originator", originator ) );
        return criteria.list();
    }

    @Override
    public Collection<IncomingSms> getAllSmses()
    {
        return sessionFactory.getCurrentSession().createCriteria( IncomingSms.class ).addOrder( Order.desc( "id" ) ).list();
    }

    @Override
    public long getSmsCount()
    
    {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria( IncomingSms.class );
        criteria.setProjection( Projections.rowCount() );
        Long count = (Long) criteria.uniqueResult();
        return count != null ? count.longValue() : (long) 0;
    }

    @Override
    public void delete( IncomingSms incomingSms )
    {
        sessionFactory.getCurrentSession().delete( incomingSms );
    }

    @Override
    public void update( IncomingSms incomingSms )
    {
        sessionFactory.getCurrentSession().update( incomingSms );
    }

    // @Override
    // public Collection<IncomingSms> getSms( String originator, Date startDate,
    // Date endDate )
    // {
    // Criteria crit = sessionFactory.getCurrentSession().createCriteria(
    // IncomingSms.class );
    // if ( originator != null && !originator.equals( "" ) )
    // {
    // crit.add( Restrictions.eq( "originator", originator ) );
    // }
    // if ( startDate != null && endDate != null )
    // {
    // crit.add( Restrictions.between( "receiveDate", startDate, endDate ) );
    // }
    // return crit.list();
    // }
    //
    // @Override
    // public Collection<IncomingSms> getSmsByDate( Date startDate, Date endDate
    // )
    // {
    // return getSms( null, startDate, endDate );
    // }

}
