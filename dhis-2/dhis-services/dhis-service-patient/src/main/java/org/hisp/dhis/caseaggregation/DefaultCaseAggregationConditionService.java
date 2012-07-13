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

package org.hisp.dhis.caseaggregation;

import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.AGGRERATION_SUM;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT_ATTRIBUTE;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_STAGE;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_STAGE_DATAELEMENT;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_STAGE_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT_PROGRAM_STAGE_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OPERATOR_AND;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.SEPARATOR_ID;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.SEPARATOR_OBJECT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientAttributeService;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.program.ProgramStageService;
import org.hisp.dhis.system.util.DateUtils;
import org.nfunk.jep.JEP;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Chau Thu Tran
 * @version DefaultPatientAggregationExpressionService.java Nov 17, 2010
 *          11:16:37 AM
 */

@Transactional
public class DefaultCaseAggregationConditionService
    implements CaseAggregationConditionService
{
    private final String regExp = "\\[(" + OBJECT_PATIENT + "|" + OBJECT_PROGRAM + "|" + OBJECT_PROGRAM_STAGE + "|"
        + OBJECT_PROGRAM_STAGE_PROPERTY + "|" + OBJECT_PATIENT_PROGRAM_STAGE_PROPERTY + "|"
        + OBJECT_PROGRAM_STAGE_DATAELEMENT + "|" + OBJECT_PATIENT_ATTRIBUTE + "|" + OBJECT_PATIENT_PROPERTY + "|"
        + OBJECT_PROGRAM_PROPERTY + ")" + SEPARATOR_OBJECT + "([a-zA-Z0-9@#\\- ]+[" + SEPARATOR_ID + "[0-9]*]*)"
        + "\\]";

    private final String IS_NULL = "is null";

    private final String PROPERTY_AGE = "age";

    private final String INVALID_CONDITION = "Invalid condition";

    private final String TOTAL_OF_PATIENTS_REGISTERED = "Total of patient registration";

    private final String IN_CONDITION_GET_ALL = "*";

    private final String IN_CONDITION_START_SIGN = "@";

    private final String IN_CONDITION_END_SIGN = "#";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CaseAggregationConditionStore aggregationConditionStore;

    private DataElementService dataElementService;

    private PatientService patientService;

    private PatientDataValueService dataValueService;

    private ProgramStageService programStageService;

    private ProgramService programService;

    private PatientAttributeService patientAttributeService;

    private ProgramStageInstanceService programStageInstanceService;

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public void setAggregationConditionStore( CaseAggregationConditionStore aggregationConditionStore )
    {
        this.aggregationConditionStore = aggregationConditionStore;
    }

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    public void setPatientAttributeService( PatientAttributeService patientAttributeService )
    {
        this.patientAttributeService = patientAttributeService;
    }

    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }

    public void setProgramStageService( ProgramStageService programStageService )
    {
        this.programStageService = programStageService;
    }

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    // -------------------------------------------------------------------------
    // Implementation Methods
    // -------------------------------------------------------------------------

    @Override
    public int addCaseAggregationCondition( CaseAggregationCondition caseAggregationCondition )
    {
        return aggregationConditionStore.save( caseAggregationCondition );
    }

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    public void setDataValueService( PatientDataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    @Override
    public void deleteCaseAggregationCondition( CaseAggregationCondition caseAggregationCondition )
    {
        aggregationConditionStore.delete( caseAggregationCondition );
    }

    @Override
    public Collection<CaseAggregationCondition> getAllCaseAggregationCondition()
    {
        return aggregationConditionStore.getAll();
    }

    @Override
    public CaseAggregationCondition getCaseAggregationCondition( int id )
    {
        return aggregationConditionStore.get( id );

    }

    @Override
    public void updateCaseAggregationCondition( CaseAggregationCondition caseAggregationCondition )
    {
        aggregationConditionStore.update( caseAggregationCondition );
    }

    @Override
    public Collection<CaseAggregationCondition> getCaseAggregationCondition( DataElement dataElement )
    {
        return aggregationConditionStore.get( dataElement );
    }

    @Override
    public CaseAggregationCondition getCaseAggregationCondition( DataElement dataElement,
        DataElementCategoryOptionCombo optionCombo )
    {
        return aggregationConditionStore.get( dataElement, optionCombo );
    }

    @Override
    public Double parseConditition( CaseAggregationCondition aggregationCondition, OrganisationUnit orgunit,
        Period period )
    {
        String sql = convertCondition( aggregationCondition, orgunit, period );
        
        Collection<Integer> patientIds = aggregationConditionStore.executeSQL( sql );

        if ( patientIds == null )
        {
            return null;
        }

        return calValue( patientIds, aggregationCondition.getOperator() );
    }

    @Override
    public Collection<PatientDataValue> getPatientDataValues( CaseAggregationCondition aggregationCondition,
        OrganisationUnit orgunit, Period period )
    {
        // get params
        int orgunitId = orgunit.getId();
        String startDate = DateUtils.getMediumDateString( period.getStartDate() );
        String endDate = DateUtils.getMediumDateString( period.getEndDate() );

        Collection<PatientDataValue> result = new HashSet<PatientDataValue>();

        String sql = createSQL( aggregationCondition.getAggregationExpression(), aggregationCondition.getOperator(),
            orgunitId, startDate, endDate );

        Collection<DataElement> dataElements = getDataElementsInCondition( aggregationCondition
            .getAggregationExpression() );

        if ( dataElements.size() > 0 )
        {
            Collection<Integer> patientIds = aggregationConditionStore.executeSQL( sql );

            for ( Integer patientId : patientIds )
            {
                Patient patient = patientService.getPatient( patientId );

                Collection<PatientDataValue> dataValues = dataValueService.getPatientDataValues( patient, dataElements,
                    period.getStartDate(), period.getEndDate() );

                result.addAll( dataValues );
            }
        }

        return result;
    }

    public Collection<Patient> getPatients( CaseAggregationCondition aggregationCondition, OrganisationUnit orgunit,
        Period period )
    {
        Collection<Patient> result = new HashSet<Patient>();

        String sql = convertCondition( aggregationCondition, orgunit, period );

        Collection<Integer> patientIds = aggregationConditionStore.executeSQL( sql );

        if ( patientIds != null )
        {
            for ( Integer patientId : patientIds )
            {
                result.add( patientService.getPatient( patientId ) );
            }
        }

        return result;
    }

    public Collection<ProgramStageInstance> getProgramStageInstances( CaseAggregationCondition aggregationCondition,
        OrganisationUnit orgunit, Period period )
    {
        Collection<ProgramStageInstance> result = new HashSet<ProgramStageInstance>();
        aggregationCondition.setOperator( AGGRERATION_SUM );

        // get params
        int orgunitId = orgunit.getId();
        String startDate = DateUtils.getMediumDateString( period.getStartDate() );
        String endDate = DateUtils.getMediumDateString( period.getEndDate() );

        String sql = createSQL( aggregationCondition.getAggregationExpression(), aggregationCondition.getOperator(),
            orgunitId, startDate, endDate );

        Collection<Integer> stageInstanceIds = aggregationConditionStore.executeSQL( sql );

        for ( Integer stageInstanceId : stageInstanceIds )
        {
            result.add( programStageInstanceService.getProgramStageInstance( stageInstanceId ) );
        }

        return result;
    }

    public String getConditionDescription( String condition )
    {
        StringBuffer description = new StringBuffer();

        Pattern patternCondition = Pattern.compile( regExp );

        Matcher matcher = patternCondition.matcher( condition );

        while ( matcher.find() )
        {
            String match = matcher.group();
            match = match.replaceAll( "[\\[\\]]", "" );

            String[] info = match.split( SEPARATOR_OBJECT );

            if ( info[0].equalsIgnoreCase( OBJECT_PROGRAM_STAGE_DATAELEMENT ) )
            {
                String[] ids = info[1].split( SEPARATOR_ID );

                int programId = Integer.parseInt( ids[0] );
                Program program = programService.getProgram( programId );

                String programStage = ids[1];
                if ( !programStage.equals( IN_CONDITION_GET_ALL ) )
                {
                    programStage = programStageService.getProgramStage( Integer.parseInt( programStage ) ).getName();
                }
                int dataElementId = Integer.parseInt( ids[2] );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );

                if ( program == null || dataElement == null )
                {
                    return INVALID_CONDITION;
                }

                matcher.appendReplacement( description, "[" + program.getName() + SEPARATOR_ID + programStage
                    + SEPARATOR_ID + dataElement.getName() + "]" );
            }
            else
            {
                String[] ids = info[1].split( SEPARATOR_ID );

                if ( info[0].equalsIgnoreCase( OBJECT_PATIENT ) )
                {
                    matcher.appendReplacement( description, "[" + OBJECT_PATIENT + SEPARATOR_OBJECT
                        + TOTAL_OF_PATIENTS_REGISTERED + "]" );
                }
                else if ( info[0].equalsIgnoreCase( OBJECT_PATIENT_ATTRIBUTE ) )
                {
                    int objectId = Integer.parseInt( ids[0] );

                    PatientAttribute patientAttribute = patientAttributeService.getPatientAttribute( objectId );

                    if ( patientAttribute == null )
                    {
                        return INVALID_CONDITION;
                    }

                    matcher.appendReplacement( description, "[" + OBJECT_PATIENT_ATTRIBUTE + SEPARATOR_OBJECT
                        + patientAttribute.getName() + "]" );
                }
                else if ( info[0].equalsIgnoreCase( OBJECT_PROGRAM ) )
                {
                    int objectId = Integer.parseInt( ids[0] );

                    Program program = programService.getProgram( objectId );

                    if ( program == null )
                    {
                        return INVALID_CONDITION;
                    }

                    matcher.appendReplacement( description, "[" + OBJECT_PROGRAM + SEPARATOR_OBJECT + program.getName()
                        + "]" );
                }
                else if ( info[0].equalsIgnoreCase( OBJECT_PROGRAM_STAGE ) )
                {
                    int objectId = Integer.parseInt( ids[0] );

                    ProgramStage programStage = programStageService.getProgramStage( objectId );

                    if ( programStage == null )
                    {
                        return INVALID_CONDITION;
                    }

                    matcher.appendReplacement( description, "[" + OBJECT_PROGRAM_STAGE + SEPARATOR_OBJECT
                        + programStage.getName() + "]" );
                }
            }

        }

        matcher.appendTail( description );

        return description.toString();
    }

    public Collection<DataElement> getDataElementsInCondition( String aggregationExpression )
    {
        String regExp = "\\[" + OBJECT_PROGRAM_STAGE_DATAELEMENT + SEPARATOR_OBJECT + "[0-9]+" + SEPARATOR_ID
            + "[0-9]+" + SEPARATOR_ID + "[0-9]+" + "\\]";

        Collection<DataElement> dataElements = new HashSet<DataElement>();

        // ---------------------------------------------------------------------
        // parse expressions
        // ---------------------------------------------------------------------

        Pattern pattern = Pattern.compile( regExp );

        Matcher matcher = pattern.matcher( aggregationExpression );

        while ( matcher.find() )
        {
            String match = matcher.group();
            match = match.replaceAll( "[\\[\\]]", "" );

            String[] info = match.split( SEPARATOR_OBJECT );
            String[] ids = info[1].split( SEPARATOR_ID );

            int dataElementId = Integer.parseInt( ids[2] );
            DataElement dataElement = dataElementService.getDataElement( dataElementId );

            dataElements.add( dataElement );
        }

        return dataElements;
    }

    public Collection<Program> getProgramsInCondition( String aggregationExpression )
    {
        String regExp = "\\[(" + OBJECT_PROGRAM + "|" + OBJECT_PROGRAM_STAGE_DATAELEMENT + ")" + SEPARATOR_OBJECT
            + "[a-zA-Z0-9\\- ]+";

        Collection<Program> programs = new HashSet<Program>();

        // ---------------------------------------------------------------------
        // parse expressions
        // ---------------------------------------------------------------------

        Pattern pattern = Pattern.compile( regExp );

        Matcher matcher = pattern.matcher( aggregationExpression );

        while ( matcher.find() )
        {
            String match = matcher.group();
            match = match.replaceAll( "[\\[\\]]", "" );

            String[] info = match.split( SEPARATOR_OBJECT );
            String[] ids = info[1].split( SEPARATOR_ID );

            int programId = Integer.parseInt( ids[0] );
            Program program = programService.getProgram( programId );

            programs.add( program );
        }

        return programs;
    }

    public Collection<PatientAttribute> getPatientAttributesInCondition( String aggregationExpression )
    {
        String regExp = "\\[" + OBJECT_PATIENT_ATTRIBUTE + SEPARATOR_OBJECT + "[0-9]+\\]";

        Collection<PatientAttribute> patientAttributes = new HashSet<PatientAttribute>();

        // ---------------------------------------------------------------------
        // parse expressions
        // ---------------------------------------------------------------------

        Pattern pattern = Pattern.compile( regExp );

        Matcher matcher = pattern.matcher( aggregationExpression );

        while ( matcher.find() )
        {
            String match = matcher.group();
            match = match.replaceAll( "[\\[\\]]", "" );

            String[] info = match.split( SEPARATOR_OBJECT );

            int patientAttributeId = Integer.parseInt( info[1] );
            PatientAttribute patientAttribute = patientAttributeService.getPatientAttribute( patientAttributeId );

            patientAttributes.add( patientAttribute );
        }

        return patientAttributes;
    }

    public Collection<CaseAggregationCondition> getCaseAggregationCondition( Collection<DataElement> dataElements )
    {
        return aggregationConditionStore.get( dataElements );
    }

    // -------------------------------------------------------------------------
    // Support Methods
    // -------------------------------------------------------------------------

    private String convertCondition( CaseAggregationCondition aggregationCondition, OrganisationUnit orgunit,
        Period period )
    {
        // get params
        int orgunitId = orgunit.getId();
        String startDate = DateUtils.getMediumDateString( period.getStartDate() );
        String endDate = DateUtils.getMediumDateString( period.getEndDate() );

        // Get operators between ( )
        Pattern patternOperator = Pattern.compile( "(\\)\\s*(OR|AND)\\s*\\( )" );

        Matcher matcherOperator = patternOperator.matcher( aggregationCondition.getAggregationExpression() );

        List<String> operators = new ArrayList<String>();

        while ( matcherOperator.find() )
        {
            operators.add( matcherOperator.group( 2 ) );
        }

        List<String> subSQL = new ArrayList<String>();

        String[] conditions = aggregationCondition.getAggregationExpression().split( "(\\)\\s*(OR|AND)\\s*\\()" );

        // Create SQL statement for the first condition
        String condition = conditions[0].replace( "(", "" ).replace( ")", "" );

        String sql = createSQL( condition, aggregationCondition.getOperator(), orgunitId, startDate, endDate );

        subSQL.add( sql );

        // Create SQL statement for others
        for ( int index = 1; index < conditions.length; index++ )
        {
            condition = conditions[index].replace( "(", "" ).replace( ")", "" );

            sql = "(" + createSQL( condition, aggregationCondition.getOperator(), orgunitId, startDate, endDate ) + ")";

            subSQL.add( sql );
        }

        sql = getSQL( aggregationCondition.getOperator(), subSQL, operators ).replace( IN_CONDITION_START_SIGN, "(" )
            .replaceAll( IN_CONDITION_END_SIGN, ")" );
        return sql;
    }

    private String createSQL( String aggregationExpression, String operator, int orgunitId, String startDate,
        String endDate )
    {
        // ---------------------------------------------------------------------
        // get operators
        // ---------------------------------------------------------------------

        Pattern patternOperator = Pattern.compile( "(AND|OR)" );

        Matcher matcherOperator = patternOperator.matcher( aggregationExpression );

        List<String> operators = new ArrayList<String>();

        while ( matcherOperator.find() )
        {
            operators.add( matcherOperator.group() );
        }

        String[] expression = aggregationExpression.split( "(AND|OR)" );

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
                            condition += " AND pd.value ";
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
                    condition = getConditionForProgramStage( info[1], operator, orgunitId, startDate, endDate );
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
                    Collection<Integer> patientIds = aggregationConditionStore.executeSQL( condition );
                    value = calValue( patientIds, AGGRERATION_SUM );

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

    private String getConditionForNotDataElement( int programId, String programStageId, String operator,
        int dataElementId, int orgunitId, String startDate, String endDate )
    {
        String sql = "SELECT distinct(pi.patientid) ";
        String condition = "pi.patientid";

        if ( operator.equals( AGGRERATION_SUM ) )
        {
            sql = "SELECT psi.programstageinstanceid ";
            condition = "psi.programstageinstanceid";
        }

        sql += "FROM programstageinstance as psi "
            + "INNER JOIN programstage as ps ON psi.programstageid = ps.programstageid "
            + "INNER JOIN programinstance as pi ON pi.programinstanceid = psi.programinstanceid "
            + "LEFT OUTER JOIN patientdatavalue as pd ON psi.programstageinstanceid = pd.programstageinstanceid "
            + "WHERE psi.executionDate >= '"
            + startDate
            + "' AND psi.executionDate <= '"
            + endDate
            + "' "
            + "AND pd.value IS NULL AND "
            + condition
            + " NOT IN  ( "
            + "SELECT distinct("
            + condition
            + ") FROM programstageinstance as psi "
            + "INNER JOIN programstage as ps ON psi.programstageid = ps.programstageid "
            + "INNER JOIN programinstance as pgi ON pi.programinstanceid = psi.programinstanceid "
            + "INNER JOIN patientdatavalue as pd ON psi.programstageinstanceid = pd.programstageinstanceid "
            + "WHERE psi.organisationunitid = "
            + orgunitId
            + " AND pgi.programid = "
            + programId
            + "AND psi.executionDate >= '"
            + startDate
            + "' AND psi.executionDate <= '"
            + endDate
            + "' "
            + "AND pd.dataelementid = " + dataElementId + " ";

        if ( !programStageId.equals( IN_CONDITION_GET_ALL ) )
        {
            sql += " AND ps.programstageid = " + programStageId;
        }

        return sql + "  ) ";
    }

    private String getConditionForDataElement( int programId, String programStageId, String operator,
        int dataElementId, int orgunitId, String startDate, String endDate )
    {
        String sql = "SELECT distinct(pi.patientid) ";

        if ( operator.equals( AGGRERATION_SUM ) )
        {
            sql = "SELECT psi.programstageinstanceid ";
        }

        sql += "FROM programstageinstance as psi "
            + "INNER JOIN programstage as ps ON psi.programstageid = ps.programstageid "
            + "INNER JOIN patientdatavalue as pd ON psi.programstageinstanceid = pd.programstageinstanceid "
            + "INNER JOIN programstage_dataelements as psd ON ps.programstageid = psd.programstageid "
            + "INNER JOIN programinstance as pi ON pi.programinstanceid = psi.programinstanceid "
            + "WHERE psd.dataelementid = " + dataElementId + " " + "AND psi.organisationunitid = " + orgunitId + " "
            + "AND psi.executionDate >= '" + startDate + "' AND psi.executionDate <= '" + endDate + "' ";

        if ( !programStageId.equals( IN_CONDITION_GET_ALL ) )
        {
            sql += " AND ps.programstageid = " + programStageId;
        }

        return sql;
    }

    private String getConditionForPatientAttribute( int attributeId, String operator )
    {
        String sql = "SELECT distinct(pi.patientid) ";

        if ( operator.equals( AGGRERATION_SUM ) )
        {
            sql = "SELECT pi.patientid ";
        }

        return sql + "FROM patient as pi INNER JOIN patientattributevalue as pav ON pav.patientid = pi.patientid "
            + "WHERE pav.patientattributeid = " + attributeId + " " + "AND pav.value ";
    }

    private String getConditionForPatient( int orgunitId, String operator, String startDate, String endDate )
    {
        String sql = "SELECT distinct(pi.patientid) ";

        if ( operator.equals( AGGRERATION_SUM ) )
        {
            sql = "SELECT pi.patientid ";
        }

        sql += "FROM patient as pi INNER JOIN programinstance pgi ON pi.patientid = pgi.patientid "
            + "INNER JOIN programstageinstance psi ON psi.programinstanceid = pgi.programinstanceid "
            + "INNER JOIN patientattributevalue as pav ON pav.patientid = pi.patientid "
            + "WHERE pi.organisationunitid = " + orgunitId + " " + "AND pi.registrationdate >= '" + startDate
            + "' AND pi.registrationdate <= '" + endDate + "' ";

        return sql;
    }

    private String getConditionForPatientProperty( String propertyName, String operator, String startDate,
        String endDate )
    {
        String sql = "SELECT distinct(pi.patientid) ";

        if ( operator.equals( AGGRERATION_SUM ) )
        {
            sql = "SELECT pi.patientid ";
        }

        sql += "FROM patient as pi INNER JOIN programinstance pgi ON pi.patientid = pgi.patientid "
            + "INNER JOIN programstageinstance psi ON psi.programinstanceid = pgi.programinstanceid WHERE ";

        if ( propertyName.equals( PROPERTY_AGE ) )
        {
            sql += "DATE('" + startDate + "') - DATE(birthdate) ";
        }
        else
        {
            sql += " pi." + propertyName + " ";
        }

        return sql;
    }

    private String getConditionForPatientProgramStageProperty( String propertyName, String operator, String startDate,
        String endDate )
    {
        String sql = "SELECT distinct(pi.patientid) ";

        if ( operator.equals( AGGRERATION_SUM ) )
        {
            sql = "SELECT pi.patientid ";
        }

        sql += "FROM patient as pi INNER JOIN programinstance pgi ON pi.patientid = pgi.patientid "
            + "INNER JOIN programstageinstance psi ON psi.programinstanceid = pgi.programinstanceid WHERE "
            + propertyName;

        return sql;
    }

    private String getConditionForProgramProperty( String operator, String startDate, String endDate, String property )
    {
        String sql = "SELECT distinct(pi.patientid) ";

        if ( operator.equals( AGGRERATION_SUM ) )
        {
            sql = "SELECT psi.programstageinstanceid ";
        }

        return sql + "FROM programstageinstance as psi "
            + "INNER JOIN programinstance as pi ON psi.programinstanceid = pi.programinstanceid "
            + "WHERE psi.executionDate >= '" + startDate + "' AND psi.executionDate <= '" + endDate + "' AND "
            + property;
    }

    private String getConditionForProgram( String programId, String operator, int orgunitId, String startDate,
        String endDate )
    {
        String select = "SELECT distinct(pi.patientid) ";

        if ( operator.equals( AGGRERATION_SUM ) )
        {
            select = "SELECT psi.programstageinstanceid ";
        }

        return select + "FROM programinstance as pi INNER JOIN programstageinstance psi "
            + "ON pi.programinstanceid = psi.programinstanceid WHERE pi.programid=" + programId + " "
            + "AND psi.organisationunitid = " + orgunitId + " AND pi.enrollmentdate >= '" + startDate
            + "' AND pi.enrollmentdate <= '" + endDate + "' ";
    }

    private String getConditionForProgramStage( String programStageId, String operator, int orgunitId,
        String startDate, String endDate )
    {
        String select = "SELECT distinct(pi.patientid) ";

        if ( operator.equals( AGGRERATION_SUM ) )
        {
            select = "SELECT psi.programstageinstanceid ";
        }

        return select + "FROM programinstance as pi INNER JOIN programstageinstance psi "
            + "ON pi.programinstanceid = psi.programinstanceid WHERE psi.programstageid=" + programStageId + " "
            + "AND psi.executiondate >= '" + startDate + "' AND psi.executiondate <= '" + endDate
            + "' AND psi.organisationunitid = " + orgunitId + " ";
    }

    private String getConditionForProgramStageProperty( String property, String operator, int orgunitId,
        String startDate, String endDate )
    {
        String select = "SELECT distinct(pi.patientid) ";

        if ( operator.equals( AGGRERATION_SUM ) )
        {
            select = "SELECT psi.programstageinstanceid ";
        }

        return select + "FROM programinstance as pi INNER JOIN programstageinstance psi "
            + "ON pi.programinstanceid = psi.programinstanceid WHERE " + " psi.executiondate >= '" + startDate
            + "' AND psi.executiondate <= '" + endDate + "' AND psi.organisationunitid = " + orgunitId + " AND "
            + property;
    }

    private String getSQL( String aggregateOperator, List<String> conditions, List<String> operators )
    {
        String sql = conditions.get( 0 );

        String sqlAnd = "";

        int index = 0;

        for ( index = 0; index < operators.size(); index++ )
        {
            if ( operators.get( index ).equalsIgnoreCase( OPERATOR_AND ) )
            {
                if ( aggregateOperator.equals( AGGRERATION_SUM ) )
                {
                    sql += " AND psi.programstageinstanceid IN ( " + conditions.get( index + 1 );
                }
                else
                {
                    sql += " AND pi.patientid IN ( " + conditions.get( index + 1 );
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

    public Double calValue( Collection<Integer> patientIds, String operator )
    {
        return new Double( patientIds.size() );
    }
}
