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

package org.hisp.dhis.dataelement.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import org.amplecode.quick.StatementManager;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.attribute.Attribute;
import org.hisp.dhis.common.hibernate.HibernateIdentifiableObjectStore;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.LocalDataElementStore;
import org.hisp.dhis.dataset.DataSet;

/**
 * @author Chau Thu Tran
 * 
 * @version $HibernateLocalDataElementStore.java Mar 23, 2012 4:08:56 PM$
 */
public class HibernateLocalDataElementStore
    extends HibernateIdentifiableObjectStore<DataElement>
    implements LocalDataElementStore
{
    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<DataElement> getByAttributeValue( Attribute attribute, String value )
    {
        return getCriteria().createAlias( "attributeValues", "attributeValue" ).add(
            Restrictions.eq( "attributeValue.attribute", attribute ) ).add(
            Restrictions.eq( "attributeValue.value", value ).ignoreCase() ).list();
    }

    @Override
    public int getDataElementCount( Integer dataElementId, Integer attributeId, String value )
    {
        Number rs = (Number) getCriteria().add( Restrictions.eq( "id", dataElementId ) ).createAlias(
            "attributeValues", "attributeValue" ).add( Restrictions.eq( "attributeValue.attribute.id", attributeId ) )
            .add( Restrictions.eq( "attributeValue.value", value ).ignoreCase() )
            .setProjection( Projections.rowCount() ).uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    @Override
    public Collection<DataElement> get( DataSet dataSet, String value )
    {
        Collection<DataElement> result = new HashSet<DataElement>();
        try
        {
            String sql = "select distinct(deav.dataelementid) from datasetmembers dsm "
                + "inner join dataelementattributevalues deav on deav.dataelementid = dsm.dataelementid "
                + "inner join attributevalue av on av.attributevalueid = deav.attributevalueid "
                + "inner join attribute att on att.attributeid = av.attributeid " + "where dsm.datasetid = "
                + dataSet.getId() + " and av.value='" + value + "'";

            ResultSet resultSet = statementManager.getHolder().getStatement().executeQuery( sql );

            while ( resultSet.next() )
            {
                result.add( dataElementService.getDataElement( resultSet.getInt( 1 ) ) );
            }

            return result;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
            return new HashSet<DataElement>();
        }
        finally
        {
            statementManager.getHolder().close();
        }
    }
}
