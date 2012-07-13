package org.hisp.dhis.statistics.jdbc;

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

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.common.Objects;
import org.hisp.dhis.statistics.StatisticsProvider;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class JdbcStatisticsProvider
    implements StatisticsProvider
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // StatisticsProvider implementation
    // -------------------------------------------------------------------------
    
    public Map<Objects, Integer> getObjectCounts()
    {
        final Map<Objects, Integer> objectCounts = new HashMap<Objects, Integer>();
        
        objectCounts.put( Objects.DATAELEMENT, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM dataelement" ) );
        objectCounts.put( Objects.DATAELEMENTGROUP, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM dataelementgroup" ) );
        objectCounts.put( Objects.INDICATORTYPE, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM indicatortype" ) );
        objectCounts.put( Objects.INDICATOR, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM indicator" ) );
        objectCounts.put( Objects.INDICATORGROUP, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM indicatorgroup" ) );
        objectCounts.put( Objects.DATASET, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM dataset" ) );
        objectCounts.put( Objects.DATADICTIONARY, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM datadictionary" ) );
        objectCounts.put( Objects.SOURCE, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM organisationunit" ) );
        objectCounts.put( Objects.VALIDATIONRULE, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM validationrule" ) );
        objectCounts.put( Objects.PERIOD, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM period" ) );
        objectCounts.put( Objects.USER, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM users" ) );
        objectCounts.put( Objects.DATAVALUE, jdbcTemplate.queryForInt( "SELECT COUNT(*) FROM datavalue" ) );
        
        return objectCounts;
    }
}
