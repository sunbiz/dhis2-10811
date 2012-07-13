package org.hisp.dhis.dataarchive.jdbc;

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

import static org.hisp.dhis.system.util.DateUtils.getMediumDateString;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataarchive.DataArchiveStore;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lars Helge Overland
 */
@Transactional
public class JdbcDataArchiveStore
    implements DataArchiveStore
{
    private static final Log log = LogFactory.getLog( JdbcDataArchiveStore.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    private StatementBuilder statementBuilder;

    public void setStatementBuilder( StatementBuilder statementBuilder )
    {
        this.statementBuilder = statementBuilder;
    }

    // -------------------------------------------------------------------------
    // Implementation methods for Data values
    // -------------------------------------------------------------------------

    public void archiveData( Date startDate, Date endDate )
    {
        // Move data from datavalue to datavaluearchive
        String sql = "INSERT INTO datavaluearchive ( " 
            + "SELECT d.* FROM datavalue AS d "
            + "JOIN period AS p ON (d.periodid=p.periodid) " 
            + "WHERE p.startdate>='" + getMediumDateString( startDate ) + "' " 
            + "AND p.enddate<='" + getMediumDateString( endDate ) + "' );";

        log.info( sql );
        jdbcTemplate.execute( sql );

        // Delete data from datavalue
        sql = statementBuilder.archiveData( getMediumDateString( startDate ), getMediumDateString( endDate ) );

        log.info( sql );
        jdbcTemplate.execute( sql );
        
    }

    public void unArchiveData( Date startDate, Date endDate )
    {
        // Move data from datavalue to datavaluearchive
        String sql = "INSERT INTO datavalue ( " 
            + "SELECT a.* FROM datavaluearchive AS a "
            + "JOIN period AS p ON (a.periodid=p.periodid) " 
            + "WHERE p.startdate>='" + getMediumDateString( startDate ) + "' " 
            + "AND p.enddate<='" + getMediumDateString( endDate ) + "' );";

        log.info( sql );
        jdbcTemplate.execute( sql );

        // Delete data from datavalue

        sql = statementBuilder.unArchiveData( getMediumDateString( startDate ), getMediumDateString( endDate ) );

        log.info( sql );
        jdbcTemplate.execute( sql );
        
    }

    public int getNumberOfOverlappingValues()
    {
        String sql = "SELECT COUNT(*) FROM datavaluearchive AS a "
            + "JOIN datavalue AS d ON (a.dataelementid=d.dataelementid " 
            + "AND a.periodid=d.periodid "
            + "AND a.sourceid=d.sourceid " 
            + "AND a.categoryoptioncomboid=d.categoryoptioncomboid);";

        log.info( sql );

        return jdbcTemplate.queryForInt( sql );
    }

    public int getNumberOfArchivedValues()
    {
        String sql = "SELECT COUNT(*) as dem FROM datavaluearchive;";

        log.info( sql );
        return jdbcTemplate.queryForInt( sql );
    }

    public void deleteRegularOverlappingData()
    {
        String sql = statementBuilder.deleteRegularOverlappingData();

        log.info( sql );
        jdbcTemplate.execute( sql );
    }

    public void deleteArchivedOverlappingData()
    {
        String sql = statementBuilder.deleteArchivedOverlappingData();

        log.info( sql );
        jdbcTemplate.execute( sql );
    }

    public void deleteOldestOverlappingData()
    {
        // Delete overlaps from datavalue which are older than datavaluearchive
        String sql = statementBuilder.deleteOldestOverlappingDataValue();

        log.info( sql );
        jdbcTemplate.execute( sql );

        // Delete overlaps from datavaluearchive which are older than datavalue
        sql = statementBuilder.deleteOldestOverlappingArchiveData();

        log.info( sql );
        jdbcTemplate.execute( sql );
    }

    // -------------------------------------------------------------------------
    // Implementation methods for Patient data values
    // -------------------------------------------------------------------------

    public void archivePatientData( Date startDate, Date endDate )
    {
        // Move data from patientdatavalue to patientdatavaluearchive
        String sql = "INSERT INTO patientdatavaluearchive ( " 
            + "SELECT pdv.programstageinstanceid, pdv.dataelementid, pdv.value, "
                    +" providedelsewhere, pdv.timestamp FROM patientdatavalue AS pdv "
            + "INNER JOIN programstageinstance AS psi " 
                + "ON pdv.programstageinstanceid = psi.programstageinstanceid "
            + "INNER JOIN programinstance AS pi " 
                + "ON pi.programinstanceid = psi.programinstanceid "
            + "WHERE pi.enddate >= '" + getMediumDateString( startDate ) + "' " + "AND pi.enddate <= '"
            + getMediumDateString( endDate ) + "' );";

        log.info( sql );
        jdbcTemplate.execute( sql );

        // Delete data from patientdatavalue
        sql = statementBuilder.archivePatientData( getMediumDateString( startDate ), getMediumDateString( endDate ) );

        log.info( sql );
        jdbcTemplate.execute( sql );
    }

    public void unArchivePatientData( Date startDate, Date endDate )
    {
        // Move data from patientdatavalue to patientdatavaluearchive
        String sql = "INSERT INTO patientdatavalue ( " 
            + "SELECT pdv.programstageinstanceid, pdv.dataelementid, pdv.value, "
                    +"providedelsewhere, pdv.timestamp FROM patientdatavaluearchive AS pdv "
            + "INNER JOIN programstageinstance AS psi " 
                + "ON pdv.programstageinstanceid = psi.programstageinstanceid "
            + "INNER JOIN programinstance AS pi " 
                + "ON pi.programinstanceid = psi.programinstanceid "
            + "WHERE pi.enddate >= '" + getMediumDateString( startDate ) + "' " + "AND pi.enddate <= '"
            + getMediumDateString( endDate ) + "' );";

        log.info( sql );
        jdbcTemplate.execute( sql );

        // Delete data from patientdatavalue
        sql = statementBuilder.unArchivePatientData( getMediumDateString( startDate ), getMediumDateString( endDate ) );

        log.info( sql );
        jdbcTemplate.execute( sql );
    }

    public int getNumberOfOverlappingPatientValues()
    {
        String sql = "SELECT COUNT(*) FROM patientdatavaluearchive AS pdv " 
            + "INNER JOIN programstageinstance AS psi "
                + "ON pdv.programstageinstanceid = psi.programstageinstanceid " 
            + "INNER JOIN programinstance AS pi "
                + "ON pi.programinstanceid = psi.programinstanceid";
        log.info( sql );

        return jdbcTemplate.queryForInt( sql );
    }

    public int getNumberOfArchivedPatientValues()
    {
        String sql = "SELECT COUNT(*) as dem FROM patientdatavaluearchive;";

        log.info( sql );
        return jdbcTemplate.queryForInt( sql );
    }

    public void deleteRegularOverlappingPatientData()
    {
        String sql = statementBuilder.deleteRegularOverlappingPatientData();

        log.info( sql );
        jdbcTemplate.execute( sql );
    }

    public void deleteArchivedOverlappingPatientData()
    {
        String sql = statementBuilder.deleteArchivedOverlappingPatientData();

        log.info( sql );
        jdbcTemplate.execute( sql );
    }

    public void deleteOldestOverlappingPatientData()
    {
        // Delete overlaps from patientdatavalue which are older than
        // patientdatavaluearchive
        String sql = statementBuilder.deleteOldestOverlappingPatientDataValue();

        log.info( sql );
        jdbcTemplate.execute( sql );

        // Delete overlaps from patientdatavaluearchive which are older than
        // patientdatavalue
        sql = statementBuilder.deleteOldestOverlappingPatientArchiveData();

        log.info( sql );
        jdbcTemplate.execute( sql );
    }

}
