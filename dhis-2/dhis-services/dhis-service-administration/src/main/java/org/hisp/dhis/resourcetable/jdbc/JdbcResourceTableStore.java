package org.hisp.dhis.resourcetable.jdbc;

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

import java.util.List;

import org.amplecode.quick.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementGroupSet;
import org.hisp.dhis.indicator.IndicatorGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.resourcetable.ResourceTableStore;
import org.hisp.dhis.resourcetable.statement.CreateCategoryTableStatement;
import org.hisp.dhis.resourcetable.statement.CreateDataElementGroupSetTableStatement;
import org.hisp.dhis.resourcetable.statement.CreateIndicatorGroupSetTableStatement;
import org.hisp.dhis.resourcetable.statement.CreateOrganisationUnitGroupSetTableStatement;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Lars Helge Overland
 */
public class JdbcResourceTableStore
    implements ResourceTableStore
{
    private static final Log log = LogFactory.getLog( JdbcResourceTableStore.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitStructure
    // -------------------------------------------------------------------------

    public void createOrganisationUnitStructure( int maxLevel )
    {
        try
        {
            jdbcTemplate.update( "DROP TABLE " + TABLE_NAME_ORGANISATION_UNIT_STRUCTURE );            
        }
        catch ( BadSqlGrammarException ex )
        {
            // Do nothing, table does not exist
        }
        
        StringBuilder sql = new StringBuilder();
        
        sql.append( "CREATE TABLE " ).append( TABLE_NAME_ORGANISATION_UNIT_STRUCTURE ).
            append( " ( organisationunitid INTEGER NOT NULL, level INTEGER, " );
        
        for ( int k = 1 ; k <= maxLevel; k++ )
        {
            String levelName = "idlevel" + String.valueOf( k );
            sql.append ( levelName );
            sql.append (" INTEGER, ");
        }
        
        sql.append( "PRIMARY KEY ( organisationunitid ) );" );
        
        log.info( "Create organisation unit structure table SQL: " + sql );
        
        jdbcTemplate.update( sql.toString() );
    }
    
    // -------------------------------------------------------------------------
    // DataElementCategoryOptionComboName
    // -------------------------------------------------------------------------
    
    public void createDataElementCategoryOptionComboName()
    {
        try
        {
            jdbcTemplate.update( "DROP TABLE " + TABLE_NAME_CATEGORY_OPTION_COMBO_NAME );            
        }
        catch ( BadSqlGrammarException ex )
        {
            // Do nothing, table does not exist
        }
        
        String sql = "CREATE TABLE " + TABLE_NAME_CATEGORY_OPTION_COMBO_NAME + 
            " ( categoryoptioncomboid INTEGER NOT NULL, categoryoptioncomboname VARCHAR(250) )";
        
        log.info( "Create category option combo name table SQL: " + sql );
        
        jdbcTemplate.update( sql );
    }
    
    // -------------------------------------------------------------------------
    // DataElementGroupSetTable
    // -------------------------------------------------------------------------

    public void createDataElementGroupSetStructure( List<DataElementGroupSet> groupSets )
    {
        try
        {
            jdbcTemplate.update( "DROP TABLE " + CreateDataElementGroupSetTableStatement.TABLE_NAME );
        }
        catch ( BadSqlGrammarException ex )
        {
            // Do nothing, table does not exist
        }
        
        Statement statement = new CreateDataElementGroupSetTableStatement( groupSets );
        
        jdbcTemplate.update( statement.getStatement() );
    }

    // -------------------------------------------------------------------------
    // DataElementGroupSetTable
    // -------------------------------------------------------------------------

    public void createIndicatorGroupSetStructure( List<IndicatorGroupSet> groupSets )
    {
        try
        {
            jdbcTemplate.update( "DROP TABLE " + CreateIndicatorGroupSetTableStatement.TABLE_NAME );
        }
        catch ( BadSqlGrammarException ex )
        {
            // Do nothing, table does not exist
        }
        
        Statement statement = new CreateIndicatorGroupSetTableStatement( groupSets );
        
        jdbcTemplate.update( statement.getStatement() );
    }
    
    // -------------------------------------------------------------------------
    // OrganisationUnitGroupSetTable
    // -------------------------------------------------------------------------

    public void createOrganisationUnitGroupSetStructure( List<OrganisationUnitGroupSet> groupSets )
    {
        try
        {
            jdbcTemplate.update( "DROP TABLE " + CreateOrganisationUnitGroupSetTableStatement.TABLE_NAME );
        }
        catch ( BadSqlGrammarException ex )
        {
            // Do nothing, table does not exist
        }
        
        Statement statement = new CreateOrganisationUnitGroupSetTableStatement( groupSets );
        
        jdbcTemplate.update( statement.getStatement() );
    }
    
    // -------------------------------------------------------------------------
    // CategoryTable
    // -------------------------------------------------------------------------

    public void createCategoryStructure( List<DataElementCategory> categories )
    {
        try
        {
            jdbcTemplate.update( "DROP TABLE " + CreateCategoryTableStatement.TABLE_NAME );
        }
        catch ( BadSqlGrammarException ex )
        {
            // Do nothing, table does not exist
        }
        
        Statement statement = new CreateCategoryTableStatement( categories );
        
        jdbcTemplate.update( statement.getStatement() );
    }

    // -------------------------------------------------------------------------
    // DataElementStructure
    // -------------------------------------------------------------------------

    public void createDataElementStructure()
    {
        try
        {
            jdbcTemplate.update( "DROP TABLE " + TABLE_NAME_DATA_ELEMENT_STRUCTURE );            
        }
        catch ( BadSqlGrammarException ex )
        {
            // Do nothing, table does not exist
        }
        
        String sql = "CREATE TABLE " + TABLE_NAME_DATA_ELEMENT_STRUCTURE + 
            " ( dataelementid INTEGER NOT NULL, dataelementname VARCHAR(250), periodtypeid INTEGER, periodtypename VARCHAR(250) )";
        
        log.info( "Create data element structure SQL: " + sql );
        
        jdbcTemplate.update( sql );        
    }
}
