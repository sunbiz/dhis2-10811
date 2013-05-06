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

package org.hisp.dhis.patient.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientStore;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.system.grid.GridUtils;
import org.hisp.dhis.system.util.TextUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
@Transactional
public class HibernatePatientStore
    extends HibernateGenericStore<Patient>
    implements PatientStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private StatementBuilder statementBuilder;

    public void setStatementBuilder( StatementBuilder statementBuilder )
    {
        this.statementBuilder = statementBuilder;
    }

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    @Override
    public Collection<Patient> getByGender( String gender )
    {
        return getCriteria( Restrictions.eq( "gender", gender ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Collection<Patient> getByBirthDate( Date birthDate )
    {
        return getCriteria( Restrictions.eq( "birthDate", birthDate ) ).list();
    }

    @Override
    public Collection<Patient> getByNames( String fullName, Integer min, Integer max )
    {
        List<Patient> patients = new ArrayList<Patient>();

        fullName = fullName.toLowerCase();
        String sql = "SELECT patientid FROM patient " + "where lower( " + statementBuilder.getPatientFullName() + ") "
            + "like '%" + fullName + "%'";

        if ( min != null && max != null )
        {
            sql += statementBuilder.limitRecord( min, max );
        }

        try
        {
            patients = jdbcTemplate.query( sql, new RowMapper<Patient>()
            {
                public Patient mapRow( ResultSet rs, int rowNum )
                    throws SQLException
                {
                    return get( rs.getInt( 1 ) );
                }
            } );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

        return patients;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Collection<Patient> get( String firstName, String middleName, String lastName, Date birthdate, String gender )

    {
        Criteria crit = getCriteria();
        Conjunction con = Restrictions.conjunction();

        if ( StringUtils.isNotBlank( firstName ) )
            con.add( Restrictions.ilike( "firstName", firstName ) );

        if ( StringUtils.isNotBlank( middleName ) )
            con.add( Restrictions.ilike( "middleName", middleName ) );

        if ( StringUtils.isNotBlank( lastName ) )
            con.add( Restrictions.ilike( "lastName", lastName ) );

        con.add( Restrictions.eq( "gender", gender ) );
        con.add( Restrictions.eq( "birthDate", birthdate ) );
        crit.add( con );

        crit.addOrder( Order.asc( "firstName" ) );

        return crit.list();
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Collection<Patient> getByOrgUnit( OrganisationUnit organisationUnit, Integer min, Integer max )
    {
        String hql = "select p from Patient p where p.organisationUnit = :organisationUnit order by p.id DESC";

        Query query = getQuery( hql );
        query.setEntity( "organisationUnit", organisationUnit );

        if ( min != null && max != null )
        {
            query.setFirstResult( min ).setMaxResults( max );
        }
        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Collection<Patient> getByOrgUnitProgram( OrganisationUnit organisationUnit, Program program, Integer min,
        Integer max )
    {
        Criteria criteria = getCriteria( Restrictions.eq( "organisationUnit", organisationUnit ) ).createAlias(
            "programs", "program" ).add( Restrictions.eq( "program.id", program.getId() ) );

        criteria.addOrder( Order.desc( "id" ) );

        if ( min != null && max != null )
        {
            criteria.setFirstResult( min ).setMaxResults( max );
        }
        return criteria.list();
    }

    @Override
    public int countGetPatientsByName( String fullName )
    {
        fullName = fullName.toLowerCase();
        String sql = "SELECT count(*) FROM patient where lower( " + statementBuilder.getPatientFullName() + ") "
            + "like '%" + fullName + "%' ";

        return jdbcTemplate.queryForObject( sql, Integer.class );
    }

    @Override
    public int countListPatientByOrgunit( OrganisationUnit organisationUnit )
    {
        Query query = getQuery( "select count(p.id) from Patient p where p.organisationUnit.id=:orgUnitId " );

        query.setParameter( "orgUnitId", organisationUnit.getId() );

        Number rs = (Number) query.uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    @Override
    public int countGetPatientsByOrgUnitProgram( OrganisationUnit organisationUnit, Program program )
    {
        Number rs = (Number) getCriteria( Restrictions.eq( "organisationUnit", organisationUnit ) )
            .createAlias( "programs", "program" ).add( Restrictions.eq( "program.id", program.getId() ) )
            .setProjection( Projections.rowCount() ).uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Collection<Patient> getRepresentatives( Patient patient )
    {
        String hql = "select distinct p from Patient p where p.representative = :representative order by p.id DESC";

        return getQuery( hql ).setEntity( "representative", patient ).list();
    }

    @Override
    public void removeErollmentPrograms( Program program )
    {
        String sql = "delete from patient_programs where programid='" + program.getId() + "'";

        jdbcTemplate.execute( sql );
    }

    @Override
    public Collection<Patient> search( List<String> searchKeys, OrganisationUnit orgunit, Integer min, Integer max )
    {
        String sql = searchPatientSql( false, searchKeys, orgunit, min, max );
        Collection<Patient> patients = new HashSet<Patient>();
        try
        {
            patients = jdbcTemplate.query( sql, new RowMapper<Patient>()
            {
                public Patient mapRow( ResultSet rs, int rowNum )
                    throws SQLException
                {
                    return get( rs.getInt( 1 ) );
                }
            } );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
        return patients;
    }

    @Override
    public Collection<String> getPatientPhoneNumbers( List<String> searchKeys, OrganisationUnit orgunit, Integer min,
        Integer max )
    {
        String sql = searchPatientSql( false, searchKeys, orgunit, min, max );
        Collection<String> phoneNumbers = new HashSet<String>();
        try
        {
            phoneNumbers = jdbcTemplate.query( sql, new RowMapper<String>()
            {
                public String mapRow( ResultSet rs, int rowNum )
                    throws SQLException
                {
                    String phoneNumber = rs.getString( "phonenumber" );
                    return (phoneNumber == null || phoneNumber.isEmpty()) ? "0" : phoneNumber;
                }
            } );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
        return phoneNumbers;
    }

    @Override
    public List<Integer> getProgramStageInstances( List<String> searchKeys, OrganisationUnit orgunit, Integer min,
        Integer max )
    {
        String sql = searchPatientSql( false, searchKeys, orgunit, min, max );
        List<Integer> programStageInstanceIds = new ArrayList<Integer>();
        try
        {
            programStageInstanceIds = jdbcTemplate.query( sql, new RowMapper<Integer>()
            {
                public Integer mapRow( ResultSet rs, int rowNum )
                    throws SQLException
                {
                    return rs.getInt( "programstageinstanceid" );
                }
            } );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

        return programStageInstanceIds;
    }

    public int countSearch( List<String> searchKeys, OrganisationUnit orgunit )
    {
        String sql = searchPatientSql( true, searchKeys, orgunit, null, null );
        return jdbcTemplate.queryForObject( sql, Integer.class );
    }

    @Override
    public Grid getPatientEventReport( Grid grid, List<String> searchKeys, OrganisationUnit orgunit )
    {
        // ---------------------------------------------------------------------
        // Get SQL and build grid
        // ---------------------------------------------------------------------

        String sql = searchPatientSql( false, searchKeys, orgunit, null, null );

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );

        GridUtils.addRows( grid, rowSet );

        return grid;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String searchPatientSql( boolean count, List<String> searchKeys, OrganisationUnit orgunit, Integer min,
        Integer max )
    {
        String selector = count ? "count(*) " : "* ";

        String sql = "select " + selector
            + " from ( select distinct p.patientid, p.firstname, p.middlename, p.lastname, p.gender, p.phonenumber,";
        String patientWhere = "";
        String patientOperator = " where ";
        String patientGroupBy = " GROUP BY  p.patientid, p.firstname, p.middlename, p.lastname, p.gender, p.phonenumber ";
        String otherWhere = "";
        String operator = " where ";
        String orderBy = "";
        boolean hasIdentifier = false;
        boolean isSearchEvent = false;
        boolean isPriorityEvent = false;
        Collection<Integer> orgunitChilrenIds = getOrgunitChildren( orgunit );

        for ( String searchKey : searchKeys )
        {
            String[] keys = searchKey.split( "_" );
            String id = keys[1];
            String value = "";
            if ( keys.length >= 3 )
            {
                value = keys[2];
            }

            if ( keys[0].equals( Patient.PREFIX_FIXED_ATTRIBUTE ) )
            {
                patientWhere += patientOperator;
                if ( id.equals( Patient.FIXED_ATTR_BIRTH_DATE ) )
                {
                    patientWhere += " p." + id + value;
                }
                else if ( id.equals( Patient.FIXED_ATTR_AGE ) )
                {
                    patientWhere += " ((DATE(now()) - DATE(birthdate))/365) " + value;
                }
                else
                {
                    patientWhere += " lower(p." + id + ")='" + value + "'";
                }
                patientOperator = " and ";
            }
            else if ( keys[0].equals( Patient.PREFIX_IDENTIFIER_TYPE ) )
            {
                patientWhere += patientOperator + "( ( lower( " + statementBuilder.getPatientFullName() + " ) like '%"
                    + id + "%' ) or lower(pi.identifier)='" + id + "' ";

                String[] keyValues = id.split( " " );
                if ( keyValues.length == 2 )
                {
                    String otherId = keyValues[0] + "  " + keyValues[1];
                    patientWhere += " or lower( " + statementBuilder.getPatientFullName() + " ) like '%" + otherId
                        + "%'  ";
                }
                patientWhere += ")";
                patientOperator = " and ";
                hasIdentifier = true;
            }
            else if ( keys[0].equals( Patient.PREFIX_PATIENT_ATTRIBUTE ) )
            {
                sql += "(select value from patientattributevalue where patientid=p.patientid and patientattributeid="
                    + id + " ) as " + Patient.PREFIX_PATIENT_ATTRIBUTE + "_" + id + ",";
                otherWhere += operator + "lower(" + Patient.PREFIX_PATIENT_ATTRIBUTE + "_" + id + ")='" + value + "'";
                operator = " and ";
            }
            else if ( keys[0].equals( Patient.PREFIX_PROGRAM ) )
            {
                sql += "(select programid from patient_programs where patientid=p.patientid and programid=" + id
                    + " ) as " + Patient.PREFIX_PROGRAM + "_" + id + ",";
                otherWhere += operator + Patient.PREFIX_PROGRAM + "_" + id + "=" + id;
                operator = " and ";
            }
            else if ( keys[0].equals( Patient.PREFIX_PROGRAM_EVENT_BY_STATUS ) )
            {
                isSearchEvent = true;
                isPriorityEvent = Boolean.parseBoolean( keys[5] );
                patientWhere += patientOperator + "pgi.patientid=p.patientid and ";
                patientWhere += "pgi.programid=" + id + " and ";
                patientWhere += "pgi.status=" + ProgramInstance.STATUS_ACTIVE;

                String operatorStatus = "";
                String condition = " and ( ";

                for ( int index = 6; index < keys.length; index++ )
                {
                    int statusEvent = Integer.parseInt( keys[index] );
                    switch ( statusEvent )
                    {
                    case ProgramStageInstance.COMPLETED_STATUS:
                        patientWhere += condition + operatorStatus
                            + "( psi.executiondate is not null and  psi.executiondate>='" + keys[2]
                            + "' and psi.executiondate<='" + keys[3] + "' and psi.completed=true ";
                        // get events by orgunit children
                        if ( keys[4].equals( "-1" ) )
                        {
                            patientWhere += " and psi.organisationunitid in( "
                                + TextUtils.getCommaDelimitedString( orgunitChilrenIds ) + " )";
                        }
                        // get events by selected
                        else if ( !keys[4].equals( "0" ) )
                        {
                            patientWhere += " and psi.organisationunitid=" + keys[4];
                        }
                        patientWhere += ")";
                        operatorStatus = " OR ";
                        condition = "";
                        continue;
                    case ProgramStageInstance.VISITED_STATUS:
                        patientWhere += condition + operatorStatus
                            + "( psi.executiondate is not null and psi.executiondate>='" + keys[2]
                            + "' and psi.executiondate<='" + keys[3] + "' and psi.completed=false ";

                        // get events by orgunit children
                        if ( keys[4].equals( "-1" ) )
                        {
                            patientWhere += " and psi.organisationunitid in( "
                                + TextUtils.getCommaDelimitedString( orgunitChilrenIds ) + " )";
                        }
                        // get events by selected
                        else if ( !keys[4].equals( "0" ) )
                        {
                            patientWhere += " and psi.organisationunitid=" + keys[4];
                        }
                        patientWhere += ")";
                        operatorStatus = " OR ";
                        condition = "";
                        continue;
                    case ProgramStageInstance.FUTURE_VISIT_STATUS:
                        patientWhere += condition + operatorStatus + "( psi.executiondate is null and psi.duedate>='"
                            + keys[2] + "' and psi.duedate<='" + keys[3]
                            + "' and psi.status is null and (DATE(now()) - DATE(psi.duedate) <= 0) ";
                        // get events by orgunit children
                        if ( keys[4].equals( "-1" ) )
                        {
                            patientWhere += " and p.organisationunitid in( "
                                + TextUtils.getCommaDelimitedString( orgunitChilrenIds ) + " )";
                        }
                        // get events by selected
                        else if ( !keys[4].equals( "0" ) )
                        {
                            patientWhere += " and p.organisationunitid=" + keys[4];
                        }
                        patientWhere += ")";
                        operatorStatus = " OR ";
                        condition = "";
                        continue;
                    case ProgramStageInstance.LATE_VISIT_STATUS:
                        patientWhere += condition + operatorStatus + "( psi.executiondate is null and  psi.duedate>='"
                            + keys[2] + "' and psi.duedate<='" + keys[3]
                            + "' and psi.status is null  and (DATE(now()) - DATE(psi.duedate) > 0) ";
                        // get events by orgunit children
                        if ( keys[4].equals( "-1" ) )
                        {
                            patientWhere += " and p.organisationunitid in( "
                                + TextUtils.getCommaDelimitedString( orgunitChilrenIds ) + " )";
                        }
                        // get events by selected
                        else if ( !keys[4].equals( "0" ) )
                        {
                            patientWhere += " and p.organisationunitid=" + keys[4];
                        }
                        patientWhere += ")";
                        operatorStatus = " OR ";
                        condition = "";
                        continue;
                    case ProgramStageInstance.SKIPPED_STATUS:
                        patientWhere += condition + operatorStatus + "( psi.status=5 and  psi.duedate>='" + keys[2]
                            + "' and psi.duedate<='" + keys[3] + "' ";
                        // get events by orgunit children
                        if ( keys[4].equals( "-1" ) )
                        {
                            patientWhere += " and psi.organisationunitid in( "
                                + TextUtils.getCommaDelimitedString( orgunitChilrenIds ) + " )";
                        }
                        // get events by selected
                        else if ( !keys[4].equals( "0" ) )
                        {
                            patientWhere += " and p.organisationunitid=" + keys[4];
                        }
                        patientWhere += ")";
                        operatorStatus = " OR ";
                        condition = "";
                        continue;
                    default:
                        continue;
                    }
                }
                if ( condition.isEmpty() )
                {
                    patientWhere += ")";
                }
                patientWhere += " and pgi.status=" + ProgramInstance.STATUS_ACTIVE + " ";
                patientOperator = " and ";

            }
            else if ( keys[0].equals( Patient.PREFIX_PROGRAM_STAGE ) )
            {
                isSearchEvent = true;
                patientWhere += patientOperator + "pgi.patientid=p.patientid and psi.programstageid=" + id + " and ";
                patientWhere += "psi.duedate>='" + keys[3] + "' and psi.duedate<='" + keys[4] + "' and ";
                patientWhere += "psi.organisationunitid = " + keys[5] + " and ";

                int statusEvent = Integer.parseInt( keys[2] );
                switch ( statusEvent )
                {
                case ProgramStageInstance.COMPLETED_STATUS:
                    patientWhere += "psi.completed=true";
                    break;
                case ProgramStageInstance.VISITED_STATUS:
                    patientWhere += "psi.executiondate is not null and psi.completed=false";
                    break;
                case ProgramStageInstance.FUTURE_VISIT_STATUS:
                    patientWhere += "psi.executiondate is null and psi.duedate >= now()";
                    break;
                case ProgramStageInstance.LATE_VISIT_STATUS:
                    patientWhere += "psi.executiondate is null and psi.duedate < now()";
                    break;
                default:
                    break;
                }

                patientWhere += " and pgi.status=" + ProgramInstance.STATUS_ACTIVE + " ";
                patientOperator = " and ";
            }
        }

        if ( orgunit != null && !isSearchEvent )
        {
            sql += "(select organisationunitid from patient where patientid=p.patientid and organisationunitid = "
                + orgunit.getId() + " ) as orgunitid,";
            otherWhere += operator + "orgunitid=" + orgunit.getId();
        }

        sql = sql.substring( 0, sql.length() - 1 ) + " "; // Removing last comma

        String from = " from patient p ";
        if ( isSearchEvent )
        {
            String subSQL = " , psi.programstageinstanceid as programstageinstanceid, pgs.name as programstagename, psi.duedate as duedate ";
            if ( isPriorityEvent )
            {
                subSQL += ",pgi.followup ";
                orderBy = " ORDER BY pgi.followup desc, duedate asc ";
                patientGroupBy += ",pgi.followup ";
            }
            else
            {
                orderBy = " ORDER BY duedate asc ";
            }
            sql = sql + subSQL + from + " inner join programinstance pgi on " + " (pgi.patientid=p.patientid) "
                + " inner join programstageinstance psi on " + " (psi.programinstanceid=pgi.programinstanceid) "
                + " inner join programstage pgs on (pgs.programstageid=psi.programstageid) ";

            patientGroupBy += ",psi.programstageinstanceid, pgs.name ";

            from = " ";
        }

        if ( hasIdentifier )
        {
            sql += from + " left join patientidentifier pi on p.patientid=pi.patientid ";
            from = " ";
        }

        sql += from + patientWhere;
        if ( isSearchEvent )
        {
            sql += patientGroupBy;
        }
        sql += orderBy;
        sql += " ) as searchresult";
        sql += otherWhere;

        if ( min != null && max != null )
        {
            sql += statementBuilder.limitRecord( min, max );
        }

        return sql;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Collection<Patient> getByPhoneNumber( String phoneNumber, Integer min, Integer max )
    {
        String hql = "select p from Patient p where p.phoneNumber like '%" + phoneNumber + "%'";
        Query query = getQuery( hql );

        if ( min != null && max != null )
        {
            query.setFirstResult( min ).setMaxResults( max );
        }

        return query.list();
    }

    @Override
    public Collection<Patient> getByFullName( String fullName, Integer orgunitId )
    {
        List<Patient> patients = new ArrayList<Patient>();

        fullName = fullName.toLowerCase();
        String sql = "SELECT patientid FROM patient where lower( " + statementBuilder.getPatientFullName() + ") "
            + "='" + fullName + "'";
        if ( orgunitId != null && orgunitId != 0 )
        {
            sql += " and organisationunitid=" + orgunitId;
        }

        try
        {
            patients = jdbcTemplate.query( sql, new RowMapper<Patient>()
            {
                public Patient mapRow( ResultSet rs, int rowNum )
                    throws SQLException
                {
                    return get( rs.getInt( 1 ) );
                }
            } );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

        return patients;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Integer> getRegistrationOrgunitIds( Date startDate, Date endDate )
    {
        Criteria criteria = getCriteria();
        criteria.add( Restrictions.between( "registrationDate", startDate, endDate ) );
        criteria.createAlias( "organisationUnit", "orgunit" );
        criteria.setProjection( Projections.distinct( Projections.projectionList().add(
            Projections.property( "orgunit.id" ), "orgunitid" ) ) );
        return criteria.list();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Collection<Integer> getOrgunitChildren( OrganisationUnit orgunit )
    {
        Collection<Integer> orgunitIds = new HashSet<Integer>();
        if ( orgunit != null )
        {
            orgunitIds.addAll( organisationUnitService.getOrganisationUnitHierarchy().getChildren( orgunit.getId() ) );
            orgunitIds.remove( orgunit.getId() );
        }
        if ( orgunitIds.size() == 0 )
        {
            orgunitIds.add( 0 );
        }
        return orgunitIds;
    }

}
