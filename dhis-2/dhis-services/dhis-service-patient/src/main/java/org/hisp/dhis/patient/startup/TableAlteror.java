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

package org.hisp.dhis.patient.startup;

import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_STAGE_DATAELEMENT;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.SEPARATOR_ID;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.SEPARATOR_OBJECT;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.amplecode.quick.StatementHolder;
import org.amplecode.quick.StatementManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.startup.AbstractStartupRoutine;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Chau Thu Tran
 * 
 * @version TableAlteror.java Sep 9, 2010 10:22:29 PM
 */
public class TableAlteror
    extends AbstractStartupRoutine
{
    private static final Log log = LogFactory.getLog( TableAlteror.class );

    Pattern IDENTIFIER_PATTERN = Pattern.compile( "DE:(\\d+)\\.(\\d+)\\.(\\d+)" );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    @Transactional
    public void execute()
        throws Exception
    {
        executeSql( "ALTER TABLE relationshiptype RENAME description TO name" );

        updateProgramStageInstanceOrgunit();

        executeSql( "ALTER TABLE programstage_dataelements DROP COLUMN showOnReport" );

        executeSql( "ALTER TABLE patientdatavalue DROP COLUMN categoryoptioncomboid" );
        executeSql( "ALTER TABLE patientdatavaluearchive DROP COLUMN providedbyanotherfacility" );
        executeSql( "ALTER TABLE patientdatavaluearchive DROP COLUMN organisationunitid" );
        executeSql( "ALTER TABLE patientdatavaluearchive DROP COLUMN storedby" );
        executeSql( "DROP TABLE patientchart" );

        executeSql( "ALTER TABLE program DROP COLUMN hidedateofincident" );

        executeSql( "UPDATE program SET type=2 where singleevent=true" );
        executeSql( "UPDATE program SET type=3 where anonymous=true" );
        executeSql( "ALTER TABLE program DROP COLUMN singleevent" );
        executeSql( "ALTER TABLE program DROP COLUMN anonymous" );
        executeSql( "UPDATE program SET type=1 where type is null" );

        executeSql( "UPDATE programstage SET irregular=false WHERE irregular is null" );

        executeSql( "DROP TABLE programattributevalue" );
        executeSql( "DROP TABLE programinstance_attributes" );
        executeSql( "DROP TABLE programattributeoption" );
        executeSql( "DROP TABLE programattribute" );

        executeSql( "ALTER TABLE patientattribute DROP COLUMN noChars" );
        executeSql( "ALTER TABLE programstageinstance ALTER executiondate TYPE date" );

        executeSql( "ALTER TABLE patientidentifier ALTER COLUMN patientid DROP NOT NULL" );
        executeSql( "ALTER TABLE patient DROP COLUMN bloodgroup" );
        executeSql( "ALTER TABLE patientmobilesetting DROP COLUMN bloodGroup" );

        executeSql( "ALTER TABLE caseaggregationcondition RENAME description TO name" );
        updateCaseAggregationCondition();

        executeSql( "UPDATE programstage_dataelements SET allowProvidedElsewhere=false WHERE allowProvidedElsewhere is null" );
        executeSql( "UPDATE patientdatavalue SET providedElsewhere=false WHERE providedElsewhere is null" );
        executeSql( "ALTER TABLE programstageinstance DROP COLUMN providedbyanotherfacility" );

        updateMultiOrgunitTabularReportTable();
        updateProgramStageTabularReportTable();
        moveStoredByFormStageInstanceToDataValue();
    }

    // -------------------------------------------------------------------------
    // Supporting methods
    // -------------------------------------------------------------------------

    private void updateProgramStageInstanceOrgunit()
    {
        StatementHolder holder = statementManager.getHolder();

        try
        {
            Statement statement = holder.getStatement();

            ResultSet resultSet = statement
                .executeQuery( "SELECT distinct programstageinstanceid, organisationunitid, providedByAnotherFacility FROM patientdatavalue" );

            while ( resultSet.next() )
            {
                executeSql( "UPDATE programstageinstance SET organisationunitid=" + resultSet.getInt( 2 )
                    + ", providedByAnotherFacility=" + resultSet.getBoolean( 3 ) + "  WHERE programstageinstanceid="
                    + resultSet.getInt( 1 ) );
            }

            executeSql( "ALTER TABLE patientdatavalue DROP COLUMN organisationUnitid" );
            executeSql( "ALTER TABLE patientdatavalue DROP COLUMN providedByAnotherFacility" );
            executeSql( "ALTER TABLE patientdatavalue ADD PRIMARY KEY ( programstageinstanceid, dataelementid )" );
        }
        catch ( Exception ex )
        {
            log.debug( ex );
        }
        finally
        {
            holder.close();
        }
    }

    private void updateCaseAggregationCondition()
    {
        String regExp = "\\[" + OBJECT_PROGRAM_STAGE_DATAELEMENT + SEPARATOR_OBJECT + "[0-9]+" + SEPARATOR_ID
            + "[0-9]+" + "\\]";

        try
        {
            StatementHolder holder = statementManager.getHolder();

            Statement statement = holder.getStatement();

            ResultSet resultSet = statement
                .executeQuery( "SELECT caseaggregationconditionid, aggregationExpression FROM caseaggregationcondition" );

            while ( resultSet.next() )
            {
                StringBuffer formular = new StringBuffer();

                // ---------------------------------------------------------------------
                // parse expressions
                // ---------------------------------------------------------------------

                Pattern pattern = Pattern.compile( regExp );

                Matcher matcher = pattern.matcher( resultSet.getString( 2 ) );

                while ( matcher.find() )
                {
                    String match = matcher.group();
                    match = match.replaceAll( "[\\[\\]]", "" );

                    String[] info = match.split( SEPARATOR_OBJECT );
                    String[] ids = info[1].split( SEPARATOR_ID );
                    int programStageId = Integer.parseInt( ids[0] );

                    StatementHolder _holder = statementManager.getHolder();
                    Statement _statement = _holder.getStatement();
                    ResultSet rsProgramId = _statement
                        .executeQuery( "SELECT programid FROM programstage where programstageid=" + programStageId );

                    if ( rsProgramId.next() )
                    {
                        int programId = rsProgramId.getInt( 1 );

                        String aggregationExpression = "[" + OBJECT_PROGRAM_STAGE_DATAELEMENT + SEPARATOR_OBJECT
                            + programId + "." + programStageId + "." + ids[1] + "]";

                        matcher.appendReplacement( formular, aggregationExpression );
                    }
                }

                matcher.appendTail( formular );

                executeSql( "UPDATE caseaggregationcondition SET aggregationExpression='" + formular.toString()
                    + "'  WHERE caseaggregationconditionid=" + resultSet.getInt( 1 ) );

            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private void updateMultiOrgunitTabularReportTable()
    {
        try
        {
            StatementHolder holder = statementManager.getHolder();

            Statement statement = holder.getStatement();

            ResultSet resultSet = statement
                .executeQuery( "SELECT patienttabularreportid, organisationunitid FROM patienttabularreport" );

            while ( resultSet.next() )
            {
                executeSql( " INSERT INTO patienttabularreport_organisationUnits ( patienttabularreportid, organisationunitid ) VALUES ( "
                    + resultSet.getInt( 1 ) + ", " + resultSet.getInt( 2 ) + ")" );
            }
            executeSql( "ALTER TABLE patienttabularreport DROP COLUMN organisationunitid" );
        }
        catch ( Exception e )
        {

        }
    }

    private void updateProgramStageTabularReportTable()
    {
        try
        {
            StatementHolder holder = statementManager.getHolder();

            Statement statement = holder.getStatement();

            ResultSet resultSet = statement
                .executeQuery( "SELECT pd.patienttabularreportid, tr.programstageid, pd.elt, sort_order "
                    + " FROM patienttabularreport_dataelements pd inner join patienttabularreport  tr"
                    + " on pd.patienttabularreportid=tr.patienttabularreportid" + " order by pd.patienttabularreportid" );

            while ( resultSet.next() )
            {
                executeSql( "INSERT INTO patienttabularreport_programstagedataelements ( patienttabularreportid, programstageid, dataelementid, sort_order ) VALUES ( "
                    + resultSet.getInt( 1 )
                    + ", "
                    + resultSet.getInt( 2 )
                    + ", "
                    + resultSet.getInt( 3 )
                    + ", "
                    + resultSet.getInt( 4 ) + ")" );
            }
            executeSql( "ALTER TABLE patienttabularreport DROP COLUMN programstageid" );
            executeSql( "DROP TABLE patienttabularreport_dataelements" );
        }
        catch ( Exception e )
        {

        }
    }

    private void moveStoredByFormStageInstanceToDataValue()
    {
        try
        {
            StatementHolder holder = statementManager.getHolder();
            Statement statement = holder.getStatement();

            ResultSet resultSet = statement.executeQuery( "SELECT programstageinstanceid, storedBy"
                + " FROM programstageinstance where storedBy is not null" );

            while ( resultSet.next() )
            {
                executeSql( "UPDATE patientdatavalue SET storedBy='" + resultSet.getString( 2 )
                    + "' where programstageinstanceid=" + resultSet.getInt( 1 ) );
            }

            executeSql( "ALTER TABLE programstageinstance DROP COLUMN storedBy" );
        }
        catch ( Exception ex )
        {
        }
    }

    private int executeSql( String sql )
    {
        try
        {
            return statementManager.getHolder().executeUpdate( sql );
        }
        catch ( Exception ex )
        {
            log.debug( ex );

            return -1;
        }
    }
}
