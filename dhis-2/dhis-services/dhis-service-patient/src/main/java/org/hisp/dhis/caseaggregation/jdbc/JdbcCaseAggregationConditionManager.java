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

package org.hisp.dhis.caseaggregation.jdbc;

import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.AGGRERATION_COUNT;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT_ATTRIBUTE;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT_PROGRAM_STAGE_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_STAGE;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_STAGE_DATAELEMENT;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_STAGE_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OPERATOR_AND;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.SEPARATOR_ID;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.SEPARATOR_OBJECT;
import static org.hisp.dhis.patient.scheduling.CaseAggregateConditionSchedulingManager.TASK_AGGREGATE_QUERY_BUILDER_LAST_12_MONTH;
import static org.hisp.dhis.patient.scheduling.CaseAggregateConditionSchedulingManager.TASK_AGGREGATE_QUERY_BUILDER_LAST_3_MONTH;
import static org.hisp.dhis.patient.scheduling.CaseAggregateConditionSchedulingManager.TASK_AGGREGATE_QUERY_BUILDER_LAST_6_MONTH;
import static org.hisp.dhis.patient.scheduling.CaseAggregateConditionSchedulingManager.TASK_AGGREGATE_QUERY_BUILDER_LAST_MONTH;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.caseaggregation.CaseAggregateSchedule;
import org.hisp.dhis.caseaggregation.CaseAggregationCondition;
import org.hisp.dhis.caseaggregation.CaseAggregationConditionManager;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.period.CalendarPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.system.util.DateUtils;
import org.nfunk.jep.JEP;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Chau Thu Tran
 */
public class JdbcCaseAggregationConditionManager
    implements CaseAggregationConditionManager
{
    private final String regExp = "\\[(" + OBJECT_PATIENT + "|" + OBJECT_PROGRAM + "|" + OBJECT_PROGRAM_STAGE + "|"
        + OBJECT_PROGRAM_STAGE_PROPERTY + "|" + OBJECT_PATIENT_PROGRAM_STAGE_PROPERTY + "|"
        + OBJECT_PROGRAM_STAGE_DATAELEMENT + "|" + OBJECT_PATIENT_ATTRIBUTE + "|" + OBJECT_PATIENT_PROPERTY + "|"
        + OBJECT_PROGRAM_PROPERTY + ")" + SEPARATOR_OBJECT + "([a-zA-Z0-9@#\\- ]+[" + SEPARATOR_ID + "[a-zA-Z0-9]*]*)"
        + "\\]";

    private final String IS_NULL = "is null";

    private final String PROPERTY_AGE = "age";

    private final String IN_CONDITION_GET_ALL = "*";

    private final String IN_CONDITION_START_SIGN = "@";

    private final String IN_CONDITION_END_SIGN = "#";

    private final String IN_CONDITION_COUNT_X_TIMES = "COUNT";

    public static final String STORED_BY_DHIS_SYSTEM = "DHIS-System";

    // -------------------------------------------------------------------------
    // Dependency
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
    // Implementation Methods
    // -------------------------------------------------------------------------

    @Override
    public List<Integer> executeSQL( String sql )
    {
        try
        {
            List<Integer> patientIds = jdbcTemplate.query( sql, new RowMapper<Integer>()
            {
                public Integer mapRow( ResultSet rs, int rowNum )
                    throws SQLException
                {
                    return rs.getInt( 1 );
                }
            } );

            return patientIds;
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            return null;
        }
    }

    @Async
    public Future<?> aggregate( ConcurrentLinkedQueue<CaseAggregateSchedule> caseAggregateSchedule, String taskStrategy )
    {
        taskLoop: while ( true )
        {
            CaseAggregateSchedule dataSet = caseAggregateSchedule.poll();

            if ( dataSet == null )
            {
                break taskLoop;
            }

            Collection<Period> periods = getPeriods( dataSet.getPeriodTypeName(), taskStrategy );

            runAggregate( null, dataSet, periods );
        }

        return null;
    }

    public Double getAggregateValue( String caseExpression, String operator, String deType, Integer deSumId,
        Integer orgunitId, Period period )
    {
        String startDate = DateUtils.getMediumDateString( period.getStartDate() );
        String endDate = DateUtils.getMediumDateString( period.getEndDate() );

        if ( operator.equals( CaseAggregationCondition.AGGRERATION_COUNT )
            || operator.equals( CaseAggregationCondition.AGGRERATION_SUM ) )
        {
            String sql = parseExpressionToSql( caseExpression, operator, deType, deSumId, orgunitId, startDate, endDate );
            Collection<Integer> ids = this.executeSQL( sql );
            return (ids == null) ? null : ids.size() + 0.0;
        }

        String sql = "SELECT " + operator + "( cast( pdv.value as DOUBLE PRECISION ) ) ";
        sql += "FROM patientdatavalue pdv ";
        sql += "    INNER JOIN programstageinstance psi  ";
        sql += "    ON psi.programstageinstanceid = pdv.programstageinstanceid ";
        sql += "WHERE executiondate >='" + DateUtils.getMediumDateString( period.getStartDate() ) + "'  ";
        sql += "    AND executiondate <='" + DateUtils.getMediumDateString( period.getEndDate() )
            + "' AND pdv.dataelementid=" + deSumId;

        if ( caseExpression != null && !caseExpression.isEmpty() )
        {
            sql = sql + " AND pdv.programstageinstanceid in ( "
                + parseExpressionToSql( caseExpression, operator, deType, deSumId, orgunitId, startDate, endDate )
                + " ) ";
        }

        Collection<Integer> ids = this.executeSQL( sql );
        return (ids == null) ? null : ids.iterator().next() + 0.0;
    }

    public String parseExpressionToSql( String aggregationExpression, String operator, String deType, Integer deSumId,
        Integer orgunitId, String startDate, String endDate )
    {
        // Get operators between ( )
        Pattern patternOperator = Pattern.compile( "(\\)\\s*(OR|AND)\\s*\\( )" );

        Matcher matcherOperator = patternOperator.matcher( aggregationExpression );

        List<String> operators = new ArrayList<String>();

        while ( matcherOperator.find() )
        {
            operators.add( matcherOperator.group( 2 ) );
        }

        List<String> subSQL = new ArrayList<String>();

        String[] conditions = aggregationExpression.split( "(\\)\\s*(OR|AND)\\s*\\()" );

        // Create SQL statement for the first condition
        String condition = conditions[0].replace( "(", "" ).replace( ")", "" );

        String sql = createSQL( condition, operator, deType, orgunitId, startDate, endDate );

        subSQL.add( sql );

        // Create SQL statement for others
        for ( int index = 1; index < conditions.length; index++ )
        {
            condition = conditions[index].replace( "(", "" ).replace( ")", "" );

            sql = "(" + createSQL( condition, operator, deType, orgunitId, startDate, endDate ) + ")";

            subSQL.add( sql );
        }

        sql = getSQL( operator, subSQL, operators ).replace( IN_CONDITION_START_SIGN, "(" ).replaceAll(
            IN_CONDITION_END_SIGN, ")" );
        return sql;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Aggregate data values for the dataset by periods with a organisation unit
     * list
     * 
     */
    private void runAggregate( Collection<Integer> orgunitIds, CaseAggregateSchedule dataSet, Collection<Period> periods )
    {
        String sql = "select caseaggregationconditionid, aggregationdataelementid, optioncomboid, de.valuetype as deType, "
            + " cagg.aggregationexpression as caseexpression, cagg.\"operator\" as caseoperator, cagg.desum as desumid "
            + "     from caseaggregationcondition cagg inner join datasetmembers dm "
            + "             on cagg.aggregationdataelementid=dm.dataelementid "
            + "     inner join dataset ds "
            + "             on ds.datasetid = dm.datasetid "
            + "     inner join periodtype pt "
            + "             on pt.periodtypeid=ds.periodtypeid "
            + "     inner join dataelement de "
            + "             on de.dataelementid=dm.dataelementid "
            + "     where ds.datasetid = "
            + dataSet.getDataSetId();

        SqlRowSet rs = jdbcTemplate.queryForRowSet( sql );

        while ( rs.next() )
        {
            for ( Period period : periods )
            {
                // -------------------------------------------------------------
                // Get formula, agg-dataelement and option-combo
                // -------------------------------------------------------------

                int dataelementId = rs.getInt( "aggregationdataelementid" );
                int optionComboId = rs.getInt( "optioncomboid" );
                String caseExpression = rs.getString( "caseexpression" );
                String caseOperator = rs.getString( "caseoperator" );
                String deType = rs.getString( "deType" );
                int deSumId = rs.getInt( "desumid" );

                Collection<Integer> _orgunitIds = getServiceOrgunit(
                    DateUtils.getMediumDateString( period.getStartDate() ),
                    DateUtils.getMediumDateString( period.getEndDate() ) );

                if ( orgunitIds == null )
                {
                    orgunitIds = new HashSet<Integer>();
                    orgunitIds.addAll( _orgunitIds );
                }
                else
                {
                    orgunitIds.retainAll( _orgunitIds );
                }

                // ---------------------------------------------------------------------
                // Aggregation
                // ---------------------------------------------------------------------

                for ( Integer orgunitId : orgunitIds )
                {
                    String dataValueSql = "select * from datavalue where dataelementid=" + dataelementId
                        + " and categoryoptioncomboid=" + optionComboId + " and sourceid=" + orgunitId
                        + " and periodid=" + period.getId() + "";

                    boolean hasValue = jdbcTemplate.queryForRowSet( dataValueSql ).next();

                    Double resultValue = getAggregateValue( caseExpression, caseOperator, deType, deSumId, orgunitId,
                        period );

                    if ( resultValue != null && resultValue != 0 )
                    {
                        // -----------------------------------------------------
                        // Add dataValue
                        // -----------------------------------------------------

                        if ( !hasValue )
                        {
                            String insertValueSql = "INSERT INTO datavalue ( dataelementid, periodid, sourceid, categoryoptioncomboid, value, storedby, lastupdated, followup ) "
                                + "VALUES ( "
                                + dataelementId
                                + ", "
                                + period.getId()
                                + ", "
                                + orgunitId
                                + ", "
                                + optionComboId
                                + ", "
                                + resultValue
                                + ", '"
                                + STORED_BY_DHIS_SYSTEM
                                + "', '"
                                + DateUtils.getMediumDateString( new Date() ) + "', false )";
                            jdbcTemplate.execute( insertValueSql );
                        }

                        // -----------------------------------------------------
                        // Update dataValue
                        // -----------------------------------------------------
                        else
                        {
                            sql = "UPDATE datavalue" + " SET value='" + resultValue + "',lastupdated='" + new Date()
                                + "' where dataelementId=" + dataelementId + " and periodid=" + period.getId()
                                + " and sourceid=" + orgunitId + " and categoryoptioncomboid=" + optionComboId
                                + " and storedby='" + STORED_BY_DHIS_SYSTEM + "'";
                            jdbcTemplate.execute( sql );
                        }
                    }

                    // ---------------------------------------------------------
                    // Delete dataValue
                    // ---------------------------------------------------------
                    else if ( hasValue )
                    {
                        String deleteSql = "DELETE from datavalue where dataelementid=dataelementid and periodid=periodid and sourceid=sourceid and categoryoptioncomboid=categoryoptioncomboid";
                        jdbcTemplate.execute( deleteSql );
                    }
                }
            }

        }
    }

    /**
     * Generate period list based on period Type and taskStrategy option
     * 
     * @param periodTypeName The name of period type
     * @param taskStrategy Specify how to get period list based on period type
     *        of each dataset. There are four options, include last month, last
     *        3 month, last 6 month and last 12 month
     * 
     */
    private Collection<Period> getPeriods( String periodTypeName, String taskStrategy )
    {
        Calendar calStartDate = Calendar.getInstance();

        if ( TASK_AGGREGATE_QUERY_BUILDER_LAST_MONTH.equals( taskStrategy ) )
        {
            calStartDate.add( Calendar.MONTH, -1 );
        }
        else if ( TASK_AGGREGATE_QUERY_BUILDER_LAST_3_MONTH.equals( taskStrategy ) )
        {
            calStartDate.add( Calendar.MONTH, -3 );
        }
        else if ( TASK_AGGREGATE_QUERY_BUILDER_LAST_6_MONTH.equals( taskStrategy ) )
        {
            calStartDate.add( Calendar.MONTH, -6 );
        }
        else if ( TASK_AGGREGATE_QUERY_BUILDER_LAST_12_MONTH.equals( taskStrategy ) )
        {
            calStartDate.add( Calendar.MONTH, -12 );
        }

        Date startDate = calStartDate.getTime();

        Calendar calEndDate = Calendar.getInstance();

        Date endDate = calEndDate.getTime();

        CalendarPeriodType periodType = (CalendarPeriodType) PeriodType.getPeriodTypeByName( periodTypeName );
        String sql = "select periodtypeid from periodtype where name='" + periodTypeName + "'";
        int periodTypeId = jdbcTemplate.queryForInt( sql );

        Collection<Period> periods = periodType.generatePeriods( startDate, endDate );

        for ( Period period : periods )
        {
            String start = DateUtils.getMediumDateString( period.getStartDate() );
            String end = DateUtils.getMediumDateString( period.getEndDate() );

            sql = "select periodid from period where periodtypeid=" + periodTypeId + " and startdate='" + start
                + "' and enddate='" + end + "'";
            int periodid = 0;
            SqlRowSet rs = jdbcTemplate.queryForRowSet( sql );
            if ( rs.next() )
            {
                periodid = rs.getInt( "periodid" );
            }

            if ( periodid == 0 )
            {
                String insertSql = "insert into period (periodtypeid,startdate,enddate) " + " VALUES " + "("
                    + periodTypeId + ",'" + start + "','" + end + "' )";
                jdbcTemplate.execute( insertSql );

                period.setId( jdbcTemplate.queryForInt( sql ) );
            }
            else
            {
                period.setId( periodid );
            }
        }

        return periods;
    }

    /**
     * Return standard SQL from query builder formula
     * 
     * @param caseExpression The query builder expression
     * @param operator There are six operators, includes Number of persons,
     *        Number of visits, Sum, Average, Minimum and Maximum of data
     *        element values.
     * @param deType Aggregate Data element type
     * @param orgunitId The id of organisation unit where to aggregate data
     *        value
     * @param startDate Start date
     * @param endDate End date
     */
    private String createSQL( String caseExpression, String operator, String deType, int orgunitId, String startDate,
        String endDate )
    {
        // ---------------------------------------------------------------------
        // get operators
        // ---------------------------------------------------------------------

        Pattern patternOperator = Pattern.compile( "(AND|OR)" );

        Matcher matcherOperator = patternOperator.matcher( caseExpression );

        List<String> operators = new ArrayList<String>();

        while ( matcherOperator.find() )
        {
            operators.add( matcherOperator.group() );
        }

        String[] expression = caseExpression.split( "(AND|OR)" );

        // ---------------------------------------------------------------------
        // parse expressions
        // ---------------------------------------------------------------------

        Pattern patternCondition = Pattern.compile( regExp );

        List<String> conditions = new ArrayList<String>();
        double value = 0.0;

        for ( int i = 0; i < expression.length; i++ )
        {
            String subExp = expression[i];
            List<String> subConditions = new ArrayList<String>();

            Matcher matcherCondition = patternCondition.matcher( expression[i] );

            String condition = "";

            while ( matcherCondition.find() )
            {
                String match = matcherCondition.group();
                subExp = subExp.replace( match, "~" );
                match = match.replaceAll( "[\\[\\]]", "" );

                String[] info = match.split( SEPARATOR_OBJECT );

                if ( info[0].equalsIgnoreCase( OBJECT_PATIENT ) )
                {
                    condition = getConditionForPatient( orgunitId, operator, startDate, endDate );
                }
                else if ( info[0].equalsIgnoreCase( OBJECT_PATIENT_PROPERTY ) )
                {
                    String propertyName = info[1];
                    condition = getConditionForPatientProperty( propertyName, operator, startDate, endDate );

                }
                else if ( info[0].equalsIgnoreCase( OBJECT_PATIENT_ATTRIBUTE ) )
                {
                    int attributeId = Integer.parseInt( info[1] );
                    condition = getConditionForPatientAttribute( attributeId, operator );
                }
                else if ( info[0].equalsIgnoreCase( OBJECT_PROGRAM_STAGE_DATAELEMENT ) )
                {
                    String[] ids = info[1].split( SEPARATOR_ID );

                    int programId = Integer.parseInt( ids[0] );
                    String programStageId = ids[1];
                    int dataElementId = Integer.parseInt( ids[2] );

                    String valueToCompare = expression[i].replace( "[" + match + "]", "" ).trim();

                    if ( valueToCompare.equalsIgnoreCase( IS_NULL ) )
                    {
                        condition = getConditionForNotDataElement( programId, programStageId, operator, dataElementId,
                            orgunitId, startDate, endDate );

                        expression[i] = expression[i].replace( valueToCompare, "" );
                    }
                    else
                    {
                        condition = getConditionForDataElement( programId, programStageId, operator, dataElementId,
                            orgunitId, startDate, endDate );

                        if ( !expression[i].contains( "+" ) )
                        {
                            if ( deType.equals( DataElement.VALUE_TYPE_INT ) )
                            {
                                condition += " AND cast( pd.value as " + statementBuilder.getDoubleColumnType() + ") ";
                            }
                            else
                            {
                                condition += " AND pd.value ";
                            }
                        }
                        else
                        {
                            subConditions.add( condition );
                        }
                    }
                }

                else if ( info[0].equalsIgnoreCase( OBJECT_PROGRAM_PROPERTY ) )
                {
                    condition = getConditionForProgramProperty( operator, startDate, endDate, info[1] );
                }
                else if ( info[0].equalsIgnoreCase( OBJECT_PROGRAM ) )
                {
                    String[] ids = info[1].split( SEPARATOR_ID );
                    condition = getConditionForProgram( ids[0], operator, orgunitId, startDate, endDate );
                    if ( ids.length > 1 )
                    {
                        condition += ids[1];
                    }
                }
                else if ( info[0].equalsIgnoreCase( OBJECT_PROGRAM_STAGE ) )
                {
                    String[] ids = info[1].split( SEPARATOR_ID );
                    if ( ids.length == 2 && ids[1].equals( IN_CONDITION_COUNT_X_TIMES ) )
                    {
                        condition = getConditionForCountProgramStage( ids[0], operator, orgunitId, startDate, endDate );
                    }
                    else
                    {
                        condition = getConditionForProgramStage( ids[0], operator, orgunitId, startDate, endDate );
                    }
                }
                else if ( info[0].equalsIgnoreCase( OBJECT_PROGRAM_STAGE_PROPERTY ) )
                {
                    condition = getConditionForProgramStageProperty( info[1], operator, orgunitId, startDate, endDate );
                }
                else if ( info[0].equalsIgnoreCase( OBJECT_PATIENT_PROGRAM_STAGE_PROPERTY ) )
                {
                    condition = getConditionForPatientProgramStageProperty( info[1], operator, startDate, endDate );
                }

                // -------------------------------------------------------------
                // Replacing the operand with 1 in order to later be able to
                // verify
                // that the formula is mathematically valid
                // -------------------------------------------------------------

                if ( expression[i].contains( "+" ) )
                {
                    Collection<Integer> patientIds = executeSQL( condition );
                    value = patientIds.size();

                    subExp = subExp.replace( "~", value + "" );
                }

                condition = expression[i].replace( match, condition ).replaceAll( "[\\[\\]]", "" );
            }

            if ( expression[i].contains( "+" ) )
            {
                final JEP parser = new JEP();

                parser.parseExpression( subExp );

                String _subExp = (parser.getValue() == 1.0) ? " AND 1 = 1 " : " AND 0 = 1 ";

                int noPlus = expression[i].split( "\\+" ).length - 1;
                List<String> subOperators = new ArrayList<String>();
                for ( int j = 0; j < noPlus; j++ )
                {
                    subOperators.add( "AND" );
                }

                condition = getSQL( operator, subConditions, subOperators ) + _subExp;
            }

            conditions.add( condition );
        }

        return getSQL( operator, conditions, operators );
    }

    /**
     * Return standard SQL of the expression to compare data value as null
     * 
     */
    private String getConditionForNotDataElement( int programId, String programStageId, String operator,
        int dataElementId, int orgunitId, String startDate, String endDate )
    {
        String sql = "SELECT distinct(pi.patientid) ";
        String from = "FROM programstageinstance as psi "
            + "INNER JOIN programinstance as pi ON pi.programinstanceid = psi.programinstanceid ";

        String condition = "pi.patientid ";

        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            sql = "SELECT psi.programstageinstanceid ";
            condition = "psi.programstageinstanceid ";
        }

        sql += from
            + "LEFT OUTER JOIN patientdatavalue as pd ON psi.programstageinstanceid = pd.programstageinstanceid "
            + "WHERE psi.executionDate >= '" + startDate + "' AND psi.executionDate <= '" + endDate + "' "
            + "AND pd.value IS NULL AND " + condition + " NOT IN  ( " + "SELECT " + condition + from
            + "WHERE psi.organisationunitid = " + orgunitId + " AND pi.programid = " + programId + " "
            + "AND psi.executionDate >= '" + startDate + "' AND psi.executionDate <= '" + endDate + "' "
            + "AND pd.dataelementid = " + dataElementId + " ";

        if ( !programStageId.equals( IN_CONDITION_GET_ALL ) )
        {
            sql += " AND psi.programstageid = " + programStageId;
        }

        return sql + "  ) ";
    }

    /**
     * Return standard SQL of a data element expression. E.g [DE:1.2.3]
     * 
     */
    private String getConditionForDataElement( int programId, String programStageId, String operator,
        int dataElementId, int orgunitId, String startDate, String endDate )
    {
        String sql = "SELECT distinct(pi.patientid) ";
        String from = "FROM programstageinstance as psi "
            + "INNER JOIN patientdatavalue as pd ON psi.programstageinstanceid = pd.programstageinstanceid "
            + "INNER JOIN programinstance as pi ON pi.programinstanceid = psi.programinstanceid ";

        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            sql = "SELECT psi.programstageinstanceid ";
            from = "FROM programstageinstance as psi "
                + "INNER JOIN patientdatavalue as pd ON psi.programstageinstanceid = pd.programstageinstanceid ";
        }

        sql += from + " WHERE pd.dataelementid=" + dataElementId + "  AND psi.organisationunitid=" + orgunitId
            + "             AND psi.executionDate>='" + startDate + "' AND psi.executionDate <= '" + endDate + "'";

        if ( !programStageId.equals( IN_CONDITION_GET_ALL ) )
        {
            sql += " AND psi.programstageid = " + programStageId;
        }

        return sql;
    }

    /**
     * Return standard SQL of a patient-attribute expression. E.g [CA:1]
     * 
     */
    private String getConditionForPatientAttribute( int attributeId, String operator )
    {
        String sql = "SELECT distinct(pi.patientid) ";
        String from = "FROM patientattributevalue pi ";

        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            sql = "SELECT psi.programstageinstanceid ";
            from = "FROM programstageinstance psi inner join programinstance pi "
                + "on psi.programinstanceid=pi.programinstanceid " + "inner join patientattributevalue pav "
                + "on pav.patientid=pi.patientid ";
        }

        return sql + from + "WHERE patientattributeid=" + attributeId + " AND value ";
    }

    /**
     * Return standard SQL of the expression which is used for calculating total
     * of person registration
     * 
     */
    private String getConditionForPatient( int orgunitId, String operator, String startDate, String endDate )
    {
        String sql = "SELECT pi.patientid ";
        String from = "FROM patient pi ";
        String where = "WHERE pi.organisationunitid=" + orgunitId + "  AND pi.registrationdate>= '" + startDate + "' "
            + "AND pi.registrationdate <= '" + endDate + "'";

        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            sql = "SELECT psi.programstageinstanceid ";
            from = "FROM programstageinstance psi inner join programinstance pi "
                + "on psi.programinstanceid=pi.programinstanceid "
                + "inner join patient p on p.patientid=pi.patientid ";
            where = "WHERE p.organisationunitid=" + orgunitId + "  AND p.registrationdate>= '" + startDate + "' "
                + "AND p.registrationdate <= '" + endDate + "'";
        }

        return sql + from + where;
    }

    /**
     * Return standard SQL of the patient-fixed-attribute expression. E.g
     * [CP:gender]
     * 
     */
    private String getConditionForPatientProperty( String propertyName, String operator, String startDate,
        String endDate )
    {
        String sql = "SELECT distinct(pi.patientid) FROM patient pi WHERE ";

        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            sql = "SELECT psi.programstageinstanceid " + "FROM programstageinstance psi inner join programinstance pi "
                + "on psi.programinstanceid=pi.programinstanceid "
                + "inner join patient p on p.patientid=pi.patientid WHERE ";
        }

        if ( propertyName.equals( PROPERTY_AGE ) )
        {
            sql += "DATE(registrationdate) - DATE(birthdate) ";
        }
        else
        {
            sql += propertyName + " ";
        }

        return sql;
    }

    /**
     * Return standard SQL of the program-property expression. E.g
     * [PC:executionDate]
     * 
     */
    private String getConditionForPatientProgramStageProperty( String propertyName, String operator, String startDate,
        String endDate )
    {
        String sql = "SELECT distinct(pi.patientid) ";
        String from = "FROM programinstance pi INNER JOIN programstageinstance psi "
            + "ON psi.programinstanceid=pi.programinstanceid ";
        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            sql = "SELECT psi.programstageinstance ";
            from = "FROM programstageinstance psi ";
        }

        from += "inner join patient p on p.patientid=pi.patientid ";

        sql += from + "WHERE executionDate>='" + startDate + "' and executionDate<='" + endDate + "' and "
            + propertyName;

        return sql;
    }

    /**
     * Return standard SQL of the program expression. E.g
     * [PP:DATE@enrollmentdate#-DATE@dateofincident#] for geting the number of
     * days between date of enrollment and date of incident.
     * 
     */
    private String getConditionForProgramProperty( String operator, String startDate, String endDate, String property )
    {
        String sql = "SELECT pi.patientid FROM programinstance as pi ";

        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            sql = "SELECT psi.programstageinstanceid FROM programinstance as pi "
                + "INNER JOIN programstageinstance psi ON psi.programinstanceid=pi.programinstanceid ";
        }

        return sql + "WHERE pi.enrollmentdate>='" + startDate + "' " + "AND pi.enrollmentdate<='" + endDate + "'  AND "
            + property;
    }

    /**
     * Return standard SQL to retrieve the number of persons enrolled into the
     * program. E.g [PG:1]
     * 
     */
    private String getConditionForProgram( String programId, String operator, int orgunitId, String startDate,
        String endDate )
    {
        String sql = "SELECT distinct(pi.patientid) FROM programinstance as pi "
            + "inner join patient psi on psi.patientid=pi.patientid ";

        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            sql = "SELECT psi.programstageinstanceid FROM programinstance as pi "
                + "INNER JOIN programstageinstance psi ON pi.programinstanceid=psi.programinstanceid ";
        }

        return sql + "WHERE pi.programid=" + programId + " " + " AND psi.organisationunitid = " + orgunitId
            + " AND pi.enrollmentdate >= '" + startDate + "' AND pi.enrollmentdate <= '" + endDate + "' ";
    }

    /**
     * Return standard SQL to retrieve the number of visits a program-stage. E.g
     * [PS:1]
     * 
     */
    private String getConditionForProgramStage( String programStageId, String operator, int orgunitId,
        String startDate, String endDate )
    {
        String select = "SELECT distinct(pi.patientid) ";

        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            select = "SELECT psi.programstageinstanceid ";
        }

        return select + "FROM programinstance as pi INNER JOIN programstageinstance psi "
            + "ON pi.programinstanceid = psi.programinstanceid WHERE psi.programstageid=" + programStageId + " "
            + "AND psi.executiondate >= '" + startDate + "' AND psi.executiondate <= '" + endDate
            + "' AND psi.organisationunitid = " + orgunitId + " ";
    }

    /**
     * Return standard SQL to retrieve the x-time of a person visited one
     * program-stage. E.g a mother came to a hospital 3th time for third
     * trimester.
     * 
     */
    private String getConditionForCountProgramStage( String programStageId, String operator, int orgunitId,
        String startDate, String endDate )
    {
        String select = "SELECT distinct(pi.patientid) ";

        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            select = "SELECT psi.programstageinstanceid ";
        }

        select += "FROM programstageinstance as psi "
            + "INNER JOIN programinstance as pi ON pi.programinstanceid = psi.programinstanceid "
            + "WHERE psi.organisationunitid = " + orgunitId + " and psi.programstageid = " + programStageId + " "
            + "AND psi.executionDate >= '" + startDate + "' AND psi.executionDate <= '" + endDate + "' "
            + "GROUP BY psi.programinstanceid ";

        if ( operator.equals( AGGRERATION_COUNT ) )
        {
            select += ",pi.patientid ";
        }

        select += "HAVING count(psi.programstageinstanceid) ";

        return select;

    }

    /**
     * Return standard SQL to retrieve the number of days between report-date
     * and due-date. E.g [PSP:DATE@executionDate#-DATE@dueDate#]
     * 
     */
    private String getConditionForProgramStageProperty( String property, String operator, int orgunitId,
        String startDate, String endDate )
    {
        String select = "SELECT distinct(pi.patientid) ";

        if ( !operator.equals( AGGRERATION_COUNT ) )
        {
            select = "SELECT psi.programstageinstanceid ";
        }

        return select + "FROM programinstance as pi INNER JOIN programstageinstance psi "
            + "ON pi.programinstanceid = psi.programinstanceid WHERE " + " psi.executiondate >= '" + startDate
            + "' AND psi.executiondate <= '" + endDate + "' AND psi.organisationunitid = " + orgunitId + " AND "
            + property;
    }

    /**
     * Return standard SQL by combining all sub-expressions of an aggregate query builder formula.
     * 
     */
    private String getSQL( String aggregateOperator, List<String> conditions, List<String> operators )
    {
        String sql = conditions.get( 0 );

        String sqlAnd = "";

        int index = 0;

        for ( index = 0; index < operators.size(); index++ )
        {
            if ( operators.get( index ).equalsIgnoreCase( OPERATOR_AND ) )
            {
                if ( aggregateOperator.equals( AGGRERATION_COUNT ) )
                {
                    sql += " AND pi.patientid IN ( " + conditions.get( index + 1 );
                }
                else
                {
                    sql += " AND psi.programstageinstanceid IN ( " + conditions.get( index + 1 );
                }
                sqlAnd += ")";
            }
            else
            {
                sql += sqlAnd;
                sql += " UNION ( " + conditions.get( index + 1 ) + " ) ";
                sqlAnd = "";
            }
        }

        sql += sqlAnd;

        return sql;
    }

    /**
     * Return the Ids of organisation units which patients registered or events happened. 
     * 
     */
    private Collection<Integer> getServiceOrgunit( String startDate, String endDate )
    {
        String sql = "(select organisationunitid from programstageinstance where executiondate>= '" + startDate
            + "' and executiondate<='" + endDate + "')";
        sql += " UNION ";
        sql += "( select distinct organisationunitid from patient where registrationdate>='" + startDate
            + "' and registrationdate<='" + endDate + "')";

        Collection<Integer> orgunitIds = new HashSet<Integer>();
        orgunitIds = jdbcTemplate.query( sql, new RowMapper<Integer>()
        {
            public Integer mapRow( ResultSet rs, int rowNum )
                throws SQLException
            {
                return rs.getInt( 1 );
            }
        } );

        return orgunitIds;
    }
}
