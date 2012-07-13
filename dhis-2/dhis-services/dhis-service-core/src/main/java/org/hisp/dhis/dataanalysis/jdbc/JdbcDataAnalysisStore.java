package org.hisp.dhis.dataanalysis.jdbc;

/*
 * Copyright (c) 2004-${year}, University of Oslo
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.amplecode.quick.StatementHolder;
import org.amplecode.quick.StatementManager;
import org.amplecode.quick.mapper.ObjectMapper;
import org.hisp.dhis.dataanalysis.DataAnalysisStore;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DeflatedDataValue;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.system.objectmapper.DeflatedDataValueNameMinMaxRowMapper;
import org.hisp.dhis.system.util.ConversionUtils;
import org.hisp.dhis.system.util.TextUtils;

/**
 * @author Lars Helge Overland
 */
public class JdbcDataAnalysisStore
    implements DataAnalysisStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    private StatementBuilder statementBuilder;

    public void setStatementBuilder( StatementBuilder statementBuilder )
    {
        this.statementBuilder = statementBuilder;
    }

    // -------------------------------------------------------------------------
    // OutlierAnalysisStore implementation
    // -------------------------------------------------------------------------

    public Double getStandardDeviation( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo, OrganisationUnit organisationUnit )
    {
         final String sql = statementBuilder.getStandardDeviation( dataElement.getId(), categoryOptionCombo.getId(), organisationUnit.getId() );
        
        return statementManager.getHolder().queryForDouble( sql );
    }
    
    public Double getAverage( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo, OrganisationUnit organisationUnit )
    {
        final String sql =  statementBuilder.getAverage( dataElement.getId(), categoryOptionCombo.getId(), organisationUnit.getId() );
           
        return statementManager.getHolder().queryForDouble( sql );
    }
    
    public Collection<DeflatedDataValue> getDeflatedDataValues( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo,
        Collection<Period> periods, OrganisationUnit organisationUnit, int lowerBound, int upperBound )
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final ObjectMapper<DeflatedDataValue> mapper = new ObjectMapper<DeflatedDataValue>();
        
        final String periodIds = TextUtils.getCommaDelimitedString( ConversionUtils.getIdentifiers( Period.class, periods ) );
        
        final String sql = statementBuilder.getDeflatedDataValues( dataElement.getId(), dataElement.getName(), categoryOptionCombo.getId(),
    		periodIds, organisationUnit.getId(), organisationUnit.getName(), lowerBound, upperBound );
        
        try
        {            
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new DeflatedDataValueNameMinMaxRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get deflated data values", ex );
        }
        finally
        {
            holder.close();
        }
    }
    
    public Collection<DeflatedDataValue> getDeflatedDataValueGaps( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo,
        Collection<Period> periods, OrganisationUnit organisationUnit )
    {
        final StatementHolder holder = statementManager.getHolder();

        final ObjectMapper<DeflatedDataValue> mapper = new ObjectMapper<DeflatedDataValue>();
        
        final String periodIds = TextUtils.getCommaDelimitedString( ConversionUtils.getIdentifiers( Period.class, periods ) );
        
        final String minValueSql = 
            "SELECT minvalue FROM minmaxdataelement " +
            "WHERE sourceid=' " + organisationUnit.getId() + "' " +
            "AND dataelementid='" + dataElement.getId() + "' " +
            "AND categoryoptioncomboid='" + categoryOptionCombo.getId() + "'";
    
        final String maxValueSql = 
            "SELECT maxvalue FROM minmaxdataelement " +
            "WHERE sourceid=' " + organisationUnit.getId() + "' " +
            "AND dataelementid='" + dataElement.getId() + "' " +
            "AND categoryoptioncomboid='" + categoryOptionCombo.getId() + "'";
        
        final String sql = 
            "SELECT '" + dataElement.getId() + "' AS dataelementid, pe.periodid, " +
            "'" + organisationUnit.getId() + "' AS sourceid, '" + categoryOptionCombo.getId() + "' AS categoryoptioncomboid, " +
            "'' AS value, '' AS storedby, '1900-01-01' AS lastupdated, '' AS comment, false AS followup, " +
            "( " + minValueSql + " ) AS minvalue, ( " + maxValueSql + " ) AS maxvalue, " +
            statementBuilder.encode( dataElement.getName() ) + " AS dataelementname, pt.name AS periodtypename, pe.startdate, pe.enddate, " +
            statementBuilder.encode( organisationUnit.getName() ) + " AS sourcename, " + 
            statementBuilder.encode( categoryOptionCombo.getName() ) + " AS categoryoptioncomboname " + //TODO join?
            "FROM period AS pe " +
            "JOIN periodtype AS pt ON (pe.periodtypeid = pt.periodtypeid) " +
            "WHERE periodid IN (" + periodIds + ") " +
            "AND pt.periodtypeid='" + dataElement.getPeriodType().getId() + "' " +
            "AND periodid NOT IN ( " +
                "SELECT periodid FROM datavalue " +
                "WHERE dataelementid='" + dataElement.getId() + "' " +
                "AND categoryoptioncomboid='" + categoryOptionCombo.getId() + "' " +
                "AND sourceid='" + organisationUnit.getId() + "' )";
        
        try
        {   
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return mapper.getCollection( resultSet, new DeflatedDataValueNameMinMaxRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get deflated data values", ex );
        }
        finally
        {
            holder.close();
        }
    }

    public Collection<DeflatedDataValue> getDataValuesMarkedForFollowup()
    {
        final StatementHolder holder = statementManager.getHolder();
        
        final String sql =
            "SELECT dv.dataelementid, dv.periodid, dv.sourceid, dv.categoryoptioncomboid, dv.value, " +
            "dv.storedby, dv.lastupdated, dv.comment, dv.followup, mm.minvalue, mm.maxvalue, de.name AS dataelementname, " +
            "pe.startdate, pe.enddate, pt.name AS periodtypename, ou.name AS sourcename, cc.categoryoptioncomboname " +
            "FROM datavalue AS dv " +
            "LEFT JOIN minmaxdataelement AS mm ON (dv.sourceid = mm.sourceid AND dv.dataelementid = mm.dataelementid AND dv.categoryoptioncomboid = mm.categoryoptioncomboid) " +
            "JOIN dataelement AS de ON (dv.dataelementid = de.dataelementid) " +
            "JOIN period AS pe ON (dv.periodid = pe.periodid) " +
            "JOIN periodtype AS pt ON (pe.periodtypeid = pt.periodtypeid) " +
            "LEFT JOIN organisationunit AS ou ON (ou.organisationunitid = dv.sourceid) " +
            "LEFT JOIN _categoryoptioncomboname AS cc ON (dv.categoryoptioncomboid = cc.categoryoptioncomboid) " +
            "WHERE dv.followup=true";
        
        try
        {
            final ResultSet resultSet = holder.getStatement().executeQuery( sql );
            
            return new ObjectMapper<DeflatedDataValue>().getCollection( resultSet, new DeflatedDataValueNameMinMaxRowMapper() );
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to get deflated data values for followup", ex );
        }
        finally
        {
            holder.close();
        }
    }        
}
