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

package org.hisp.dhis.program.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientReminder;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceStore;
import org.hisp.dhis.program.SchedulingProgramObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class HibernateProgramInstanceStore
    extends HibernateGenericStore<ProgramInstance>
    implements ProgramInstanceStore
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate( JdbcTemplate jdbcTemplate )
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------------------------------------------------------------------------
    // Implemented methods
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Integer status )
    {
        return getCriteria( Restrictions.eq( "status", status ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program )
    {
        return getCriteria( Restrictions.eq( "program", program ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Collection<Program> programs )
    {
        return getCriteria( Restrictions.in( "program", programs ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program, Integer status )
    {
        return getCriteria( Restrictions.eq( "program", program ), Restrictions.eq( "status", status ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Collection<Program> programs, Integer status )
    {
        return getCriteria( Restrictions.in( "program", programs ), Restrictions.eq( "status", status ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Patient patient )
    {
        return getCriteria( Restrictions.eq( "patient", patient ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Patient patient, Integer status )
    {
        return getCriteria( Restrictions.eq( "patient", patient ), Restrictions.eq( "status", status ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Patient patient, Program program )
    {
        return getCriteria( Restrictions.eq( "patient", patient ), Restrictions.eq( "program", program ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Patient patient, Program program, Integer status )
    {
        return getCriteria( Restrictions.eq( "patient", patient ), Restrictions.eq( "program", program ),
            Restrictions.eq( "status", status ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program, OrganisationUnit organisationUnit )
    {
        return getCriteria( Restrictions.eq( "program", program ), Restrictions.isNull( "endDate" ) )
            .createAlias( "patient", "patient" ).add( Restrictions.eq( "patient.organisationUnit", organisationUnit ) )
            .list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program, OrganisationUnit organisationUnit, int min, int max )
    {
        return getCriteria( Restrictions.eq( "program", program ), Restrictions.isNull( "endDate" ) )
            .add( Restrictions.eq( "patient.organisationUnit", organisationUnit ) ).createAlias( "patient", "patient" )
            .addOrder( Order.asc( "patient.id" ) ).setFirstResult( min ).setMaxResults( max ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program, OrganisationUnit organisationUnit, Date startDate,
        Date endDate )
    {
        return getCriteria( Restrictions.eq( "program", program ), Restrictions.isNull( "endDate" ),
            Restrictions.ge( "enrollmentDate", startDate ), Restrictions.le( "enrollmentDate", endDate ) )
            .createAlias( "patient", "patient" ).add( Restrictions.eq( "patient.organisationUnit", organisationUnit ) )
            .addOrder( Order.asc( "patient.id" ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program, Collection<Integer> orgunitIds, Date startDate,
        Date endDate, int min, int max )
    {
        return getCriteria( Restrictions.eq( "program", program ), Restrictions.ge( "enrollmentDate", startDate ),
            Restrictions.le( "enrollmentDate", endDate ) ).createAlias( "patient", "patient" )
            .createAlias( "patient.organisationUnit", "organisationUnit" )
            .add( Restrictions.in( "organisationUnit.id", orgunitIds ) ).addOrder( Order.asc( "patient.id" ) )
            .setFirstResult( min ).setMaxResults( max ).list();
    }

    public int count( Program program, OrganisationUnit organisationUnit )
    {
        Number rs = (Number) getCriteria( Restrictions.eq( "program", program ), Restrictions.isNull( "endDate" ) )
            .createAlias( "patient", "patient" ).add( Restrictions.eq( "patient.organisationUnit", organisationUnit ) )
            .setProjection( Projections.rowCount() ).uniqueResult();
        return rs != null ? rs.intValue() : 0;
    }

    public int count( Program program, Collection<Integer> orgunitIds, Date startDate, Date endDate )
    {
        Number rs = (Number) getCriteria( Restrictions.eq( "program", program ),
            Restrictions.ge( "enrollmentDate", startDate ), Restrictions.le( "enrollmentDate", endDate ) )
            .createAlias( "patient", "patient" ).createAlias( "patient.organisationUnit", "organisationUnit" )
            .add( Restrictions.in( "organisationUnit.id", orgunitIds ) ).setProjection( Projections.rowCount() )
            .uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    public int countByStatus( Integer status, Program program, Collection<Integer> orgunitIds, Date startDate,
        Date endDate )
    {
        Number rs = (Number) getCriteria( Restrictions.eq( "program", program ),
            Restrictions.between( "endDate", startDate, endDate ) ).createAlias( "patient", "patient" )
            .createAlias( "patient.organisationUnit", "organisationUnit" )
            .add( Restrictions.in( "organisationUnit.id", orgunitIds ) ).add( Restrictions.eq( "status", status ) )
            .setProjection( Projections.projectionList().add( Projections.countDistinct( "id" ) ) ).uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> getByStatus( Integer status, Program program, Collection<Integer> orgunitIds,
        Date startDate, Date endDate )
    {
        return getCriteria( Restrictions.eq( "program", program ), Restrictions.between( "endDate", startDate, endDate ) )
            .createAlias( "patient", "patient" ).createAlias( "patient.organisationUnit", "organisationUnit" )
            .add( Restrictions.in( "organisationUnit.id", orgunitIds ) ).add( Restrictions.eq( "status", status ) )
            .list();
    }

    public void removeProgramEnrollment( ProgramInstance programInstance )
    {
        String sql = "delete from programstageinstance where programinstanceid=" + programInstance.getId();
        jdbcTemplate.execute( sql );

        sql = "delete from programinstance where programinstanceid=" + programInstance.getId();
        jdbcTemplate.execute( sql );
    }

    public Collection<SchedulingProgramObject> getSendMesssageEvents( String dateToCompare )
    {
        String sql = "SELECT pi.programinstanceid, p.phonenumber, prm.templatemessage, "
            + "         p.firstname, p.middlename, p.lastname, org.name as orgunitName, "
            + "         pg.name as programName, pi.dateofincident , "
            + "         pi.enrollmentdate,(DATE(now()) - DATE(pi.enrollmentdate) ) as days_since_erollment_date, "
            + "         (DATE(now()) - DATE(pi.dateofincident) ) as days_since_incident_date "
            + "       FROM patient p INNER JOIN programinstance pi "
            + "              ON p.patientid=pi.patientid INNER JOIN program pg "
            + "              ON pg.programid=pi.programid INNER JOIN organisationunit org "
            + "              ON org.organisationunitid = p.organisationunitid INNER JOIN patientreminder prm "
            + "              ON prm.programid = pi.programid " 
            + "       WHERE pi.status= " + ProgramInstance.STATUS_ACTIVE
            + "         and p.phonenumber is not NULL and p.phonenumber != ''   "
            + "         and prm.templatemessage is not NULL and prm.templatemessage != ''   "
            + "         and pg.type=1 and prm.daysallowedsendmessage is not null    "
            + "         and ( DATE(now()) - DATE(pi." + dateToCompare + ") ) = prm.daysallowedsendmessage "
            + "         and prm.dateToCompare='" + dateToCompare + "'";
        
        SqlRowSet rs = jdbcTemplate.queryForRowSet( sql );

        int cols = rs.getMetaData().getColumnCount();

        Collection<SchedulingProgramObject> schedulingProgramObjects = new HashSet<SchedulingProgramObject>();

        while ( rs.next() )
        {
            String message = "";
            for ( int i = 1; i <= cols; i++ )
            {
                message = rs.getString( "templatemessage" );
                String patientName = rs.getString( "firstName" );
                String organisationunitName = rs.getString( "orgunitName" );
                String programName = rs.getString( "programName" );
                String incidentDate = rs.getString( "dateofincident" ).split( " " )[0];// just get date, remove timestamp
                String daysSinceIncidentDate = rs.getString( "days_since_incident_date" );
                String erollmentDate = rs.getString( "enrollmentdate" ).split( " " )[0];// just get date, remove timestamp
                String daysSinceEnrollementDate = rs.getString( "days_since_erollment_date" );

                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_PATIENT_NAME, patientName );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_PROGRAM_NAME, programName );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_ORGUNIT_NAME, organisationunitName );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_INCIDENT_DATE, incidentDate );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_ENROLLMENT_DATE, erollmentDate );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_DAYS_SINCE_ENROLLMENT_DATE, daysSinceEnrollementDate );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_DAYS_SINCE_INCIDENT_DATE, daysSinceIncidentDate );
            }

            SchedulingProgramObject schedulingProgramObject = new SchedulingProgramObject();
            schedulingProgramObject.setProgramInstanceId( rs.getInt( "programinstanceid" ) );
            schedulingProgramObject.setPhoneNumber( rs.getString( "phonenumber" ) );
            schedulingProgramObject.setMessage( message );

            schedulingProgramObjects.add( schedulingProgramObject );
        }

        return schedulingProgramObjects;
    }

}
