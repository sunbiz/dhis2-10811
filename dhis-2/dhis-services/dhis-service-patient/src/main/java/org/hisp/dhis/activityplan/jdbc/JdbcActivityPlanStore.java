/*
 * Copyright (c) 2004-2009, University of Oslo
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

package org.hisp.dhis.activityplan.jdbc;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;

import org.amplecode.quick.StatementHolder;
import org.amplecode.quick.StatementManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.activityplan.ActivityPlanStore;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.patient.startup.TableAlteror;

/**
 * @author Chau Thu Tran
 * @version $ JdbcActivityPlanStore.java May 22, 2011 7:52:03 PM $
 * 
 */
public class JdbcActivityPlanStore
    implements ActivityPlanStore
{
    private static final Log log = LogFactory.getLog( TableAlteror.class );

    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private StatementManager statementManager;

    private StatementBuilder statementBuilder;

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    public void setStatementBuilder( StatementBuilder statementBuilder )
    {
        this.statementBuilder = statementBuilder;
    }

    // -------------------------------------------------------------------------
    // Implementation Methods
    // -------------------------------------------------------------------------

    @Override
    public Collection<Integer> getActivitiesByProvider( Integer orgunitId, int min, int max )
    {
        StatementHolder holder = statementManager.getHolder();

        Collection<Integer> programStageInstanceIds = new HashSet<Integer>();
        try
        {
            Statement statement = holder.getStatement();

            String sql = "SELECT distinct psi.programstageinstanceid " + "FROM programstageinstance psi "
                + "INNER JOIN programinstance pi " + "ON pi.programinstanceid = psi.programinstanceid "
                + "INNER JOIN programstage ps " + "ON ps.programstageid=psi.programstageid "
                + "INNER JOIN program_organisationunits po " + "ON po.programid=pi.programid "
                + "INNER JOIN program pg " + "ON po.programid=pg.programid " + "WHERE pi.completed = FALSE  "
                + "AND pg.type=1 " + "AND po.organisationunitid = " + orgunitId
                + " AND psi.completed = FALSE " + "AND ps.stageinprogram in ( SELECT min(ps1.stageinprogram) "
                + "FROM programstageinstance psi1 " + "INNER JOIN programinstance pi1 "
                + "ON pi1.programinstanceid = psi1.programinstanceid " + "INNER JOIN programstage ps1 "
                + "ON ps1.programstageid=psi1.programstageid " + "INNER JOIN program_organisationunits po1 "
                + "ON po1.programid=pi1.programid " + "WHERE pi1.completed = FALSE  " + "AND po1.organisationunitid = "
                + orgunitId + " AND psi1.completed = FALSE ) " + "ORDER BY ps.stageinprogram "
                + statementBuilder.limitRecord( min, max );

            ResultSet resultSet = statement.executeQuery( sql );

            while ( resultSet.next() )
            {
                programStageInstanceIds.add( resultSet.getInt( 1 ) );
            }

            return programStageInstanceIds;

        }
        catch ( Exception ex )
        {
            log.debug( ex );

            return null;
        }
        finally
        {
            holder.close();
        }
    }

    @Override
    public int countActivitiesByProvider( Integer orgunitId )
    {
        StatementHolder holder = statementManager.getHolder();

        try
        {
            Statement statement = holder.getStatement();

            String sql = "SELECT count(distinct psi.programstageinstanceid) " + "FROM programstageinstance psi "
                + "INNER JOIN programinstance pi " + "ON pi.programinstanceid = psi.programinstanceid "
                + "INNER JOIN programstage ps " + "ON ps.programstageid=psi.programstageid "
                + "INNER JOIN program_organisationunits po " + "ON po.programid=pi.programid "
                + "INNER JOIN program pg " + "ON po.programid=pg.programid " + "WHERE pi.completed = FALSE  "
                + "AND pg.type=1 " + "AND po.organisationunitid = " + orgunitId
                + " AND psi.completed = FALSE " + "AND ps.stageinprogram in ( SELECT min(ps1.stageinprogram) "
                + "FROM programstageinstance psi1 " + "INNER JOIN programinstance pi1 "
                + "ON pi1.programinstanceid = psi1.programinstanceid " + "INNER JOIN programstage ps1 "
                + "ON ps1.programstageid=psi1.programstageid " + "INNER JOIN program_organisationunits po1 "
                + "ON po1.programid=pi1.programid " + "WHERE pi1.completed = FALSE  " + "AND po1.organisationunitid = "
                + orgunitId + " AND psi1.completed = FALSE ) ";

            ResultSet resultSet = statement.executeQuery( sql );
            if ( resultSet.next() )
            {
                return resultSet.getInt( 1 );
            }

            return 0;
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            return 0;
        }
        finally
        {
            holder.close();
        }
    }

}
