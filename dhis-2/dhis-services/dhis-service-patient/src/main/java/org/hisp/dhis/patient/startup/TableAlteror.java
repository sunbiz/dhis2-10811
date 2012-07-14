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
import org.hisp.dhis.common.CodeGenerator;
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
        executeSql( "ALTER TABLE patientdatavaluearchive ADD COLUMN storedby VARCHAR(31)" );
        executeSql( "ALTER TABLE patientdatavaluearchive ADD COLUMN providedelsewhere BOOLEAN" );
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

        executeSql( "ALTER TABLE patientattribute DROP COLUMN inheritable" );
        executeSql( "ALTER TABLE programstageinstance DROP COLUMN stageInProgram" );

        updateRelationshipIdentifiers();
        updateRelationshipAttributes();

        executeSql( "UPDATE programstage SET reportDateDescription='Report date' WHERE reportDateDescription is null" );

        executeSql( "CREATE INDEX programstageinstance_executiondate ON programstageinstance (executiondate)" );

        executeSql( "UPDATE programstage SET autoGenerateEvent=true WHERE autoGenerateEvent is null" );

        executeSql( "UPDATE program SET generatedByEnrollmentDate=false WHERE generatedByEnrollmentDate is null" );

        executeSql( "ALTER TABLE programstage DROP COLUMN stageinprogram" );

        executeSql( "CREATE INDEX index_patientdatavalue ON patientdatavalue( programstageinstanceid, dataelementid, value, timestamp )" );

        executeSql( "CREATE INDEX index_programinstance ON programinstance( programinstanceid )" );
        
        executeSql( "ALTER TABLE program DROP COLUMN maxDaysAllowedInputData");
                
        executeSql( "ALTER TABLE period modify periodid int AUTO_INCREMENT");
        executeSql( "CREATE SEQUENCE period_periodid_seq");
        executeSql( "ALTER TABLE period ALTER COLUMN periodid SET DEFAULT NEXTVAL('period_periodid_seq')");
        
        executeSql( "UPDATE program SET programstage_dataelements=false WHERE displayInReports is null" );
        
        executeSql( "ALTER TABLE programvalidation DROP COLUMN leftside" );
        executeSql( "ALTER TABLE programvalidation DROP COLUMN rightside" );
        executeSql( "ALTER TABLE programvalidation DROP COLUMN dateType" );
        
        executeSql( "UPDATE programstage SET validCompleteOnly=false WHERE validCompleteOnly is null" );
        executeSql( "UPDATE program SET ignoreOverdueEvents=false WHERE ignoreOverdueEvents is null" );
        
        executeSql( "UPDATE programstage SET displayGenerateEventBox=true WHERE displayGenerateEventBox is null" );
        executeSql( "ALTER TABLE patientidentifier DROP COLUMN preferred");

        executeSql( "UPDATE patientidentifiertype SET personDisplayName=false WHERE personDisplayName is null");

        executeSql( "ALTER TABLE programvalidation RENAME description TO name" );
        
        executeSql( "UPDATE program SET blockEntryForm=false WHERE blockEntryForm is null" );
        executeSql( "ALTER TABLE dataset DROP CONSTRAINT program_name_key" );
        executeSql( "UPDATE userroleauthorities SET authority='F_PROGRAM_PUBLIC_ADD' WHERE authority='F_PROGRAM_ADD'" );
        
        updateUid();
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
            
            executeSql( "update caseaggregationcondition set \"operator\"='times' where \"operator\"='SUM'" );
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
        String regExp = "\\[" + OBJECT_PROGRAM_STAGE_DATAELEMENT + SEPARATOR_OBJECT + "([0-9]+" + SEPARATOR_ID
            + "[0-9]+" + "\\])";

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
                String expression = resultSet.getString( 2 ).replaceAll( "'", "''" );
                Matcher matcher = pattern.matcher( expression );

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
                            + programId + SEPARATOR_ID + programStageId + SEPARATOR_ID + ids[1] + "]";
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
            executeSql( "UPDATE program SET displayIncidentDate=true WHERE displayIncidentDate is null and type!=3" );
            executeSql( "UPDATE program SET displayIncidentDate=false WHERE displayIncidentDate is null and type==3" );
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

    private void updateRelationshipIdentifiers()
    {
        StatementHolder holder = statementManager.getHolder();

        try
        {
            Statement statement = holder.getStatement();

            ResultSet resultSet = statement
                .executeQuery( "SELECT distinct programid, patientidentifiertypeid FROM patientidentifiertype" );

            while ( resultSet.next() )
            {
                executeSql( "INSERT into program_patientIdentifierTypes( programid, patientidentifiertypeid) values ("
                    + resultSet.getString( 1 ) + "," + resultSet.getString( 2 ) + ")" );
            }

            executeSql( "ALTER TABLE patientidentifiertype DROP COLUMN programid" );
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

    private void updateRelationshipAttributes()
    {
        StatementHolder holder = statementManager.getHolder();

        try
        {
            Statement statement = holder.getStatement();

            ResultSet resultSet = statement
                .executeQuery( "SELECT distinct programid, patientattributeid FROM program_patientAttributes" );

            while ( resultSet.next() )
            {
                executeSql( "INSERT into program_patientAttributes( programid, patientattributeid) values ("
                    + resultSet.getString( 1 ) + "," + resultSet.getString( 2 ) + ")" );
            }

            executeSql( "ALTER TABLE patientattribute DROP COLUMN programid" );
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

    private void updateUid()
    {
        updateUidColumn( "patientattribute" );
        updateUidColumn( "patientattributegroup" );
        updateUidColumn( "patientidentifiertype" );
        updateUidColumn( "program" );
        updateUidColumn( "patientattribute" );
        updateUidColumn( "programstage" );
        updateUidColumn( "programstagesection" );
        updateUidColumn( "programvalidation" );
    }
    
    private void updateUidColumn( String tableName )
    {
        StatementHolder holder = statementManager.getHolder();

        try
        {
            Statement statement = holder.getStatement();

            ResultSet resultSet = statement
                .executeQuery( "SELECT " + tableName + "id FROM " + tableName + " where uid is null" );

            while ( resultSet.next() )
            {
                String uid = CodeGenerator.generateCode();
                
                executeSql( "UPDATE " + tableName + " SET uid='" + uid
                    + "'  WHERE " + tableName + "id=" + resultSet.getInt( 1 ) );
            }
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
