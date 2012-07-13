package org.hisp.dhis.datavalue.hibernate;

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
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.amplecode.quick.StatementHolder;
import org.amplecode.quick.StatementManager;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: HibernateDataValueStore.java 5715 2008-09-17 14:05:28Z larshelg
 *          $
 */
public class HibernateDataValueStore
    implements DataValueStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SessionFactory sessionFactory;

    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    private PeriodStore periodStore;

    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }

    // -------------------------------------------------------------------------
    // Basic DataValue
    // -------------------------------------------------------------------------

    public void addDataValue( DataValue dataValue )
    {
        dataValue.setPeriod( periodStore.reloadForceAddPeriod( dataValue.getPeriod() ) );

        Session session = sessionFactory.getCurrentSession();

        session.save( dataValue );
    }

    public void updateDataValue( DataValue dataValue )
    {
        dataValue.setPeriod( periodStore.reloadForceAddPeriod( dataValue.getPeriod() ) );

        Session session = sessionFactory.getCurrentSession();

        session.update( dataValue );
    }

    public void deleteDataValue( DataValue dataValue )
    {
        Session session = sessionFactory.getCurrentSession();

        session.delete( dataValue );
    }

    public int deleteDataValuesBySource( OrganisationUnit source )
    {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery( "delete DataValue where source = :source" );
        query.setEntity( "source", source );

        return query.executeUpdate();
    }

    public int deleteDataValuesByDataElement( DataElement dataElement )
    {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery( "delete DataValue where dataElement = :dataElement" );
        query.setEntity( "dataElement", dataElement );

        return query.executeUpdate();
    }

    public DataValue getDataValue( OrganisationUnit source, DataElement dataElement, Period period,
        DataElementCategoryOptionCombo optionCombo )
    {
        Session session = sessionFactory.getCurrentSession();

        Period storedPeriod = periodStore.reloadPeriod( period );

        if ( storedPeriod == null )
        {
            return null;
        }

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );
        criteria.add( Restrictions.eq( "period", storedPeriod ) );
        criteria.add( Restrictions.eq( "optionCombo", optionCombo ) );

        return (DataValue) criteria.uniqueResult();
    }

    public String getValue( int dataElementId, int periodId, int sourceId, int categoryOptionComboId )
    {
        final String sql = "SELECT value " + "FROM datavalue " + "WHERE dataelementid='" + dataElementId + "' "
            + "AND periodid='" + periodId + "' " + "AND sourceid='" + sourceId + "' " + "AND categoryoptioncomboid='"
            + categoryOptionComboId + "'";

        return statementManager.getHolder().queryForString( sql );
    }

    // -------------------------------------------------------------------------
    // Collections of DataValues
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getAllDataValues()
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( OrganisationUnit source, Period period )
    {
        Period storedPeriod = periodStore.reloadPeriod( period );

        if ( storedPeriod == null )
        {
            return Collections.emptySet();
        }

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "period", storedPeriod ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( OrganisationUnit source, DataElement dataElement )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( Collection<OrganisationUnit> sources, DataElement dataElement )
    {
        Session session = sessionFactory.getCurrentSession();
        
        if ( sources == null || sources.isEmpty() )
        {
            return Collections.emptySet();
        }
        
        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.in( "source", sources ) );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( OrganisationUnit source, Period period, Collection<DataElement> dataElements )
    {
        Period storedPeriod = periodStore.reloadPeriod( period );

        if ( storedPeriod == null || dataElements == null || dataElements.isEmpty() )
        {
            return Collections.emptySet();
        }

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "period", storedPeriod ) );
        criteria.add( Restrictions.in( "dataElement", dataElements ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( OrganisationUnit source, Period period, Collection<DataElement> dataElements,
        Collection<DataElementCategoryOptionCombo> optionCombos )
    {
        Period storedPeriod = periodStore.reloadPeriod( period );

        if ( storedPeriod == null || dataElements == null || dataElements.isEmpty() || optionCombos == null || optionCombos.isEmpty() )
        {
            return Collections.emptySet();
        }

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "source", source ) );
        criteria.add( Restrictions.eq( "period", storedPeriod ) );
        criteria.add( Restrictions.in( "dataElement", dataElements ) );
        criteria.add( Restrictions.in( "optionCombo", optionCombos ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( DataElement dataElement, Period period,
        Collection<OrganisationUnit> sources )
    {
        Period storedPeriod = periodStore.reloadPeriod( period );

        if ( storedPeriod == null || sources == null || sources.isEmpty() )
        {
            return new HashSet<DataValue>();
        }

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );
        criteria.add( Restrictions.eq( "period", storedPeriod ) );
        criteria.add( Restrictions.in( "source", sources ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( DataElement dataElement, Collection<Period> periods,
        Collection<OrganisationUnit> sources )
    {
        Collection<Period> storedPeriods = new ArrayList<Period>();

        for ( Period period : periods )
        {
            Period storedPeriod = periodStore.reloadPeriod( period );

            if ( storedPeriod != null )
            {
                storedPeriods.add( storedPeriod );
            }
        }

        if ( storedPeriods.isEmpty() || sources == null || sources.isEmpty() )
        {
            return new HashSet<DataValue>();
        }

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );
        criteria.add( Restrictions.in( "period", storedPeriods ) );
        criteria.add( Restrictions.in( "source", sources ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( DataElement dataElement, DataElementCategoryOptionCombo optionCombo,
        Collection<Period> periods, Collection<OrganisationUnit> sources )
    {
        Collection<Period> storedPeriods = new ArrayList<Period>();

        for ( Period period : periods )
        {
            Period storedPeriod = periodStore.reloadPeriod( period );

            if ( storedPeriod != null )
            {
                storedPeriods.add( storedPeriod );
            }
        }

        if ( storedPeriods.isEmpty() || sources == null || sources.isEmpty() )
        {
            return new HashSet<DataValue>();
        }

        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );
        criteria.add( Restrictions.eq( "optionCombo", optionCombo ) );
        criteria.add( Restrictions.in( "period", storedPeriods ) );
        criteria.add( Restrictions.in( "source", sources ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( Collection<DataElementCategoryOptionCombo> optionCombos )
    {
        Session session = sessionFactory.getCurrentSession();

        if ( optionCombos == null || optionCombos.isEmpty() )
        {
            return new HashSet<DataValue>();
        }
        
        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.in( "optionCombo", optionCombos ) );

        return criteria.list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataValue> getDataValues( DataElement dataElement )
    {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria( DataValue.class );
        criteria.add( Restrictions.eq( "dataElement", dataElement ) );

        return criteria.list();
    }

    @Override
    public DataValue getLatestDataValues( DataElement dataElement, PeriodType periodType,
        OrganisationUnit organisationUnit )
    {
        final String hsql = "SELECT v FROM DataValue v, Period p WHERE  v.dataElement =:dataElement "
            + " AND v.period=p AND p.periodType=:periodType AND v.source=:source ORDER BY p.endDate DESC";

        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery( hsql );

        query.setParameter( "dataElement", dataElement );
        query.setParameter( "periodType", periodType );
        query.setParameter( "source", organisationUnit );
        
        query.setFirstResult( 0 );
        query.setMaxResults( 1 );

        return (DataValue) query.uniqueResult();
    }
    
    public Set<DataElementOperand> getOperandsWithDataValues( Set<DataElementOperand> operands )
    {
        final Set<DataElementOperand> operandsWithData = new HashSet<DataElementOperand>();
        
        final StatementHolder holder = statementManager.getHolder();
        
        for ( DataElementOperand operand : operands )
        {
            final String sql = 
                "SELECT COUNT(*) FROM datavalue " + 
                "WHERE dataelementid=" + operand.getDataElementId() + " " +
                "AND categoryoptioncomboid=" + operand.getOptionComboId();
            
            Integer count = holder.queryForInteger( sql );
            
            if ( count != null && count > 0 )
            {
                operandsWithData.add( operand );
            }
        }
        
        return operandsWithData;
    }
    
    public int getDataValueCount( Date date )
    {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria( DataValue.class );
        
        criteria.add( Restrictions.ge( "timestamp", date ) );
        criteria.setProjection( Projections.rowCount() );

        Number rs = (Number) criteria.uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }
}
