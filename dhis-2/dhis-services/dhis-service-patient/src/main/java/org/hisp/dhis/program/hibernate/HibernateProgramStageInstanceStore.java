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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.GridHeader;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAudit;
import org.hisp.dhis.patient.PatientAuditService;
import org.hisp.dhis.patient.PatientReminder;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.patientreport.PatientAggregateReport;
import org.hisp.dhis.patientreport.TabularReportColumn;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceStore;
import org.hisp.dhis.program.SchedulingProgramObject;
import org.hisp.dhis.sms.outbound.OutboundSms;
import org.hisp.dhis.system.grid.GridUtils;
import org.hisp.dhis.system.grid.ListGrid;
import org.hisp.dhis.system.util.DateUtils;
import org.hisp.dhis.system.util.TextUtils;
import org.hisp.dhis.user.CurrentUserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author Abyot Asalefew
 */
public class HibernateProgramStageInstanceStore
    extends HibernateGenericStore<ProgramStageInstance>
    implements ProgramStageInstanceStore
{
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

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private PatientAuditService patientAuditService;

    public void setPatientAuditService( PatientAuditService patientAuditService )
    {
        this.patientAuditService = patientAuditService;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private PatientService patientService;

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    private ProgramInstanceService programInstanceService;

    public void setProgramInstanceService( ProgramInstanceService programInstanceService )
    {
        this.programInstanceService = programInstanceService;
    }

    // -------------------------------------------------------------------------
    // Implemented methods
    // -------------------------------------------------------------------------

    @SuppressWarnings( "unchecked" )
    public ProgramStageInstance get( ProgramInstance programInstance, ProgramStage programStage )
    {
        List<ProgramStageInstance> list = new ArrayList<ProgramStageInstance>( getCriteria(
            Restrictions.eq( "programInstance", programInstance ), Restrictions.eq( "programStage", programStage ) )
            .addOrder( Order.asc( "id" ) ).list() );

        return list.isEmpty() ? null : list.get( list.size() - 1 );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramStageInstance> get( ProgramStage programStage )
    {
        return getCriteria( Restrictions.eq( "programStage", programStage ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramStageInstance> get( Collection<ProgramInstance> programInstances )
    {
        return getCriteria( Restrictions.in( "programInstance", programInstances ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramStageInstance> get( Date dueDate )
    {
        return getCriteria( Restrictions.eq( "dueDate", dueDate ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramStageInstance> get( Date dueDate, Boolean completed )
    {
        return getCriteria( Restrictions.eq( "dueDate", dueDate ), Restrictions.eq( "completed", completed ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramStageInstance> get( Date startDate, Date endDate )
    {
        return (getCriteria( Restrictions.ge( "dueDate", startDate ), Restrictions.le( "dueDate", endDate ) )).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramStageInstance> get( Date startDate, Date endDate, Boolean completed )
    {
        return (getCriteria( Restrictions.ge( "dueDate", startDate ), Restrictions.le( "dueDate", endDate ),
            Restrictions.eq( "completed", completed ) )).list();
    }

    @SuppressWarnings( "unchecked" )
    public List<ProgramStageInstance> get( OrganisationUnit unit, Date after, Date before, Boolean completed )
    {
        String hql = "from ProgramStageInstance psi where psi.organisationUnit = :unit";

        if ( after != null )
        {
            hql += " and dueDate >= :after";
        }

        if ( before != null )
        {
            hql += " and dueDate <= :before";
        }

        if ( completed != null )
        {
            hql += " and completed = :completed";
        }

        Query q = getQuery( hql ).setEntity( "unit", unit );

        if ( after != null )
        {
            q.setDate( "after", after );
        }

        if ( before != null )
        {
            q.setDate( "before", before );
        }

        if ( completed != null )
        {
            q.setBoolean( "completed", completed );
        }

        return q.list();
    }

    @SuppressWarnings( "unchecked" )
    public List<ProgramStageInstance> get( Patient patient, Boolean completed )
    {
        String hql = "from ProgramStageInstance where programInstance.patient = :patient and completed = :completed";

        return getQuery( hql ).setEntity( "patient", patient ).setBoolean( "completed", completed ).list();
    }

    @SuppressWarnings( "unchecked" )
    public List<ProgramStageInstance> get( ProgramStage programStage, OrganisationUnit orgunit, Date startDate,
        Date endDate, int min, int max )
    {
        return getCriteria( Restrictions.eq( "programStage", programStage ),
            Restrictions.eq( "organisationUnit", orgunit ), Restrictions.between( "dueDate", startDate, endDate ) )
            .setFirstResult( min ).setMaxResults( max ).list();
    }

    public Grid getTabularReport( Boolean anonynousEntryForm, ProgramStage programStage,
        Map<Integer, OrganisationUnitLevel> orgUnitLevelMap, Collection<Integer> orgUnits,
        List<TabularReportColumn> columns, int level, int maxLevel, Date startDate, Date endDate, boolean descOrder,
        Boolean completed, Boolean accessPrivateInfo, Integer min, Integer max, I18n i18n )
    {
        // ---------------------------------------------------------------------
        // Headers cols
        // ---------------------------------------------------------------------

        Grid grid = new ListGrid();
        grid.setTitle( programStage.getDisplayName() );
        grid.setSubtitle( i18n.getString( "from" ) + " " + DateUtils.getMediumDateString( startDate ) + " "
            + i18n.getString( "to" ) + " " + DateUtils.getMediumDateString( endDate ) );

        grid.addHeader( new GridHeader( "id", true, true ) );
        grid.addHeader( new GridHeader( programStage.getReportDateDescription(), false, true ) );

        if ( anonynousEntryForm == null || !anonynousEntryForm )
        {
            for ( int i = level; i <= maxLevel; i++ )
            {
                String name = orgUnitLevelMap.containsKey( i ) ? orgUnitLevelMap.get( i ).getName() : "Level " + i;
                grid.addHeader( new GridHeader( name, false, true ) );
            }
        }

        Collection<String> deKeys = new HashSet<String>();
        for ( TabularReportColumn column : columns )
        {
            if ( !column.isMeta() )
            {
                String deKey = "element_" + column.getIdentifier();
                if ( !deKeys.contains( deKey ) )
                {
                    grid.addHeader( new GridHeader( column.getName(), column.isHidden(), true ) );
                    deKeys.add( deKey );
                }
            }
        }

        grid.addHeader( new GridHeader( "Complete", true, true ) );
        grid.addHeader( new GridHeader( "PatientId", true, true ) );

        // ---------------------------------------------------------------------
        // Get SQL and build grid
        // ---------------------------------------------------------------------

        String sql = getTabularReportSql( anonynousEntryForm, false, programStage, columns, orgUnits, level, maxLevel,
            startDate, endDate, descOrder, completed, accessPrivateInfo, min, max );

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );

        GridUtils.addRows( grid, rowSet );

        // Save PatientAudit

        if ( accessPrivateInfo != null && accessPrivateInfo )
        {
            long millisInDay = 60 * 60 * 24 * 1000;
            long currentTime = new Date().getTime();
            long dateOnly = (currentTime / millisInDay) * millisInDay;
            Date date = new Date( dateOnly );
            String visitor = currentUserService.getCurrentUsername();

            List<Map<String, Object>> rows = jdbcTemplate.queryForList( sql );

            if ( rows != null && !rows.isEmpty() )
            {
                for ( Map<String, Object> row : rows )
                {
                    Integer patientId = (Integer) row.get( "patientid" );

                    PatientAudit patientAudit = patientAuditService.getPatientAudit( patientId, visitor, date,
                        PatientAudit.MODULE_TABULAR_REPORT );
                    if ( patientAudit == null )
                    {
                        Patient patient = patientService.getPatient( patientId );
                        patientAudit = new PatientAudit( patient, visitor, date, PatientAudit.MODULE_TABULAR_REPORT );
                        patientAuditService.savePatientAudit( patientAudit );
                    }
                }
            }
        }

        return grid;
    }

    public int getTabularReportCount( Boolean anonynousEntryForm, ProgramStage programStage,
        List<TabularReportColumn> columns, Collection<Integer> organisationUnits, int level, int maxLevel,
        Date startDate, Date endDate, Boolean completed )
    {
        String sql = getTabularReportSql( anonynousEntryForm, true, programStage, columns, organisationUnits, level,
            maxLevel, startDate, endDate, false, completed, null, null, null );

        return jdbcTemplate.queryForObject( sql, Integer.class );
    }

    public void removeEmptyEvents( ProgramStage programStage, OrganisationUnit organisationUnit )
    {
        String sql = "delete from programstageinstance where programstageid=" + programStage.getId()
            + " and organisationunitid=" + organisationUnit.getId() + " and programstageinstanceid not in "
            + "(select pdv.programstageinstanceid from patientdatavalue pdv )";
        jdbcTemplate.execute( sql );
    }

    @Override
    public void update( Collection<Integer> programStageInstanceIds, OutboundSms outboundSms )
    {
        for ( Integer programStageInstanceId : programStageInstanceIds )
        {
            if ( programStageInstanceId != null && programStageInstanceId != 0 )
            {
                ProgramStageInstance programStageInstance = get( programStageInstanceId );

                List<OutboundSms> outboundSmsList = programStageInstance.getOutboundSms();

                if ( outboundSmsList == null )
                {
                    outboundSmsList = new ArrayList<OutboundSms>();
                }

                outboundSmsList.add( outboundSms );
                programStageInstance.setOutboundSms( outboundSmsList );
                update( programStageInstance );
            }
        }
    }

    public Collection<SchedulingProgramObject> getSendMesssageEvents()
    {
        String sql = "select psi.programstageinstanceid, p.phonenumber, prm.templatemessage, p.firstname, p.middlename, p.lastname, org.name as orgunitName "
            + ",pg.name as programName, ps.name as programStageName, psi.duedate,(DATE(now()) - DATE(psi.duedate) ) as days_since_due_date,psi.duedate "
            + "from patient p INNER JOIN programinstance pi "
            + "     ON p.patientid=pi.patientid "
            + " INNER JOIN programstageinstance psi  "
            + "     ON psi.programinstanceid=pi.programinstanceid "
            + " INNER JOIN program pg  "
            + "     ON pg.programid=pi.programid "
            + " INNER JOIN programstage ps  "
            + "     ON ps.programstageid=psi.programstageid "
            + " INNER JOIN organisationunit org  "
            + "     ON org.organisationunitid = p.organisationunitid "
            + " INNER JOIN patientreminder prm  "
            + "     ON prm.programstageid = ps.programstageid "
            + "WHERE pi.status="
            + ProgramInstance.STATUS_ACTIVE
            + "     and p.phonenumber is not NULL and p.phonenumber != '' "
            + "     and prm.templatemessage is not NULL and prm.templatemessage != '' "
            + "     and pg.type=1 and prm.daysallowedsendmessage is not null  "
            + "     and psi.executiondate is null "
            + "     and (  DATE(now()) - DATE(psi.duedate) ) = prm.daysallowedsendmessage ";

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
                String programStageName = rs.getString( "programStageName" );
                String daysSinceDueDate = rs.getString( "days_since_due_date" );
                String dueDate = rs.getString( "duedate" ).split( " " )[0];// just
                                                                           // get
                                                                           // date,
                                                                           // remove
                                                                           // timestamp

                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_PATIENT_NAME, patientName );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_PROGRAM_NAME, programName );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_PROGAM_STAGE_NAME, programStageName );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_DUE_DATE, dueDate );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_ORGUNIT_NAME, organisationunitName );
                message = message.replace( PatientReminder.TEMPLATE_MESSSAGE_DAYS_SINCE_DUE_DATE, daysSinceDueDate );
            }

            SchedulingProgramObject schedulingProgramObject = new SchedulingProgramObject();
            schedulingProgramObject.setProgramStageInstanceId( rs.getInt( "programstageinstanceid" ) );
            schedulingProgramObject.setPhoneNumber( rs.getString( "phonenumber" ) );
            schedulingProgramObject.setMessage( message );

            schedulingProgramObjects.add( schedulingProgramObject );
        }

        return schedulingProgramObjects;
    }

    public int getStatisticalProgramStageReport( ProgramStage programStage, Collection<Integer> orgunitIds,
        Date startDate, Date endDate, int status )
    {
        Criteria criteria = getStatisticalProgramStageCriteria( programStage, orgunitIds, startDate, endDate, status );

        Number rs = (Number) criteria.setProjection( Projections.rowCount() ).uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    @SuppressWarnings( "unchecked" )
    public List<ProgramStageInstance> getStatisticalProgramStageDetailsReport( ProgramStage programStage,
        Collection<Integer> orgunitIds, Date startDate, Date endDate, int status, Integer min, Integer max )
    {
        Criteria criteria = getStatisticalProgramStageCriteria( programStage, orgunitIds, startDate, endDate, status );

        if ( min != null && max != null )
        {
            criteria.setFirstResult( min );
            criteria.setMaxResults( max );
        }

        return criteria.list();
    }

    public Grid getAggregateReport( int position, ProgramStage programStage, Collection<Integer> orgunitIds,
        String facilityLB, Integer deGroupBy, Integer deSum, Map<Integer, Collection<String>> deFilters,
        List<Period> periods, String aggregateType, Integer limit, Boolean useCompletedEvents, Boolean displayTotals,
        I18nFormat format, I18n i18n )
    {
        String sql = "";
        String filterSQL = filterSQLStatement( deFilters );

        Grid grid = new ListGrid();
        grid.setTitle( programStage.getProgram().getDisplayName() );

        // ---------------------------------------------------------------------
        // Set Sub-title is filter value
        // ---------------------------------------------------------------------

        String subTitle = " ";
        if ( deSum != null )
        {
            subTitle = i18n.getString( "group_by" ) + ": "
                + dataElementService.getDataElement( deSum ).getDisplayName() + "; ";
        }

        // Filter is only one orgunit
        if ( position == PatientAggregateReport.POSITION_ROW_PERIOD_COLUMN_DATA )
        {
            String orgunitName = organisationUnitService.getOrganisationUnit( orgunitIds.iterator().next() )
                .getDisplayName();

            String filterDataDes = getFilterDataDescription( deFilters );
            if ( !filterDataDes.isEmpty() )
            {
                filterDataDes = "; " + i18n.getString( "data_filter" ) + ": " + filterDataDes;
            }
            grid.setSubtitle( subTitle + i18n.getString( "orgunit" ) + ": " + orgunitName + filterDataDes );
        }
        // Filter is only one period
        else if ( position == PatientAggregateReport.POSITION_ROW_ORGUNIT
            || position == PatientAggregateReport.POSITION_ROW_ORGUNIT_COLUMN_DATA
            || position == PatientAggregateReport.POSITION_ROW_DATA_COLUMN_ORGUNIT
            || position == PatientAggregateReport.POSITION_ROW_DATA )
        {

            Period period = periods.iterator().next();
            String periodName = "";
            if ( period.getPeriodType() != null )
            {
                periodName += format.formatPeriod( period );
            }
            else
            {
                String startDate = format.formatDate( period.getStartDate() );
                String endDate = format.formatDate( period.getEndDate() );
                periodName += startDate + " -> " + endDate;
            }

            String filterDataDes = getFilterDataDescription( deFilters );
            if ( !filterDataDes.isEmpty() )
            {
                filterDataDes = "; " + i18n.getString( "data_filter" ) + ": " + filterDataDes;
            }

            grid.setSubtitle( subTitle + i18n.getString( "period" ) + ": " + periodName + filterDataDes );
        }
        else
        {
            // Orgunit filter description
            String filterOrgunitDes = "";
            if ( position == PatientAggregateReport.POSITION_ROW_DATA_COLUMN_PERIOD
                || position == PatientAggregateReport.POSITION_ROW_PERIOD
                || position == PatientAggregateReport.POSITION_ROW_DATA )
            {
                filterOrgunitDes = getFilterOrgunitDescription( orgunitIds );

                if ( !filterOrgunitDes.isEmpty() )
                {
                    filterOrgunitDes = i18n.getString( "orgunit" ) + ": " + filterOrgunitDes + "; ";
                }
            }

            // Period filter description
            String filterPeriodDes = "";
            if ( position == PatientAggregateReport.POSITION_ROW_ORGUNIT
                || position == PatientAggregateReport.POSITION_ROW_ORGUNIT_COLUMN_DATA
                || position == PatientAggregateReport.POSITION_ROW_DATA
                || position == PatientAggregateReport.POSITION_ROW_DATA_COLUMN_ORGUNIT )
            {
                filterPeriodDes = getFilterPeriodDescription( periods, format );

                if ( !filterPeriodDes.isEmpty() )
                {
                    filterPeriodDes = i18n.getString( "period" ) + ": " + filterPeriodDes + "; ";
                }
            }

            // Data filter description
            String filterDataDes = "";
            if ( position == PatientAggregateReport.POSITION_ROW_ORGUNIT_COLUMN_PERIOD
                || position == PatientAggregateReport.POSITION_ROW_PERIOD_COLUMN_ORGUNIT
                || position == PatientAggregateReport.POSITION_ROW_ORGUNIT_ROW_PERIOD
                || position == PatientAggregateReport.POSITION_ROW_PERIOD
                || position == PatientAggregateReport.POSITION_ROW_ORGUNIT )
            {
                filterDataDes = getFilterDataDescription( deFilters );
                if ( !filterDataDes.isEmpty() )
                {
                    filterDataDes = i18n.getString( "data_filter" ) + ": " + filterDataDes + "; ";
                }
            }

            subTitle += filterOrgunitDes + filterPeriodDes + filterDataDes;
            if ( subTitle.isEmpty() )
            {
                grid.setSubtitle( i18n.getString( "filter" ) + ": [" + i18n.getString( "none" ) + "]" );
            }
            else
            {
                grid.setSubtitle( subTitle );
            }
        }

        // ---------------------------------------------------------------------
        // Get SQL and build grid
        // ---------------------------------------------------------------------

        // Type = 1
        if ( position == PatientAggregateReport.POSITION_ROW_ORGUNIT_COLUMN_PERIOD )
        {
            sql = getAggregateReportSQL12( programStage, orgunitIds, facilityLB, filterSQL, deGroupBy, deSum, periods,
                aggregateType, limit, useCompletedEvents, format );
        }
        // Type = 2
        if ( position == PatientAggregateReport.POSITION_ROW_PERIOD_COLUMN_ORGUNIT )
        {
            // ---------------------------------------------------------------------
            // Get SQL and build grid
            // ---------------------------------------------------------------------

            sql = getAggregateReportSQL12( programStage, orgunitIds, facilityLB, filterSQL, deGroupBy, deSum, periods,
                aggregateType, limit, useCompletedEvents, format );

        }
        // Type = 3
        else if ( position == PatientAggregateReport.POSITION_ROW_ORGUNIT_ROW_PERIOD )
        {
            sql = getAggregateReportSQL3( position, programStage, orgunitIds, facilityLB, filterSQL, deGroupBy, deSum,
                periods, aggregateType, limit, useCompletedEvents, format );
        }
        // Type = 4
        else if ( position == PatientAggregateReport.POSITION_ROW_PERIOD )
        {
            sql = getAggregateReportSQL4( position, programStage, orgunitIds, facilityLB, filterSQL, deGroupBy, deSum,
                periods, aggregateType, limit, useCompletedEvents, format );
        }
        // type = 5
        else if ( position == PatientAggregateReport.POSITION_ROW_ORGUNIT )
        {
            sql = getAggregateReportSQL5( position, programStage, orgunitIds, facilityLB, filterSQL, deGroupBy, deSum,
                periods.iterator().next(), aggregateType, limit, useCompletedEvents, format );
        }

        // Type = 9 && With group-by
        else if ( position == PatientAggregateReport.POSITION_ROW_DATA_COLUMN_PERIOD && deGroupBy != null )
        {
            sql = getAggregateReportSQL9( programStage, orgunitIds.iterator().next(), facilityLB, filterSQL, deGroupBy,
                deSum, periods, aggregateType, limit, useCompletedEvents, format );
        }

        // Type = 6 && With group-by
        else if ( position == PatientAggregateReport.POSITION_ROW_PERIOD_COLUMN_DATA && deGroupBy != null )
        {
            sql = getAggregateReportSQL6( programStage, orgunitIds.iterator().next(), facilityLB, filterSQL, deGroupBy,
                deSum, periods, aggregateType, limit, useCompletedEvents, format );
        }

        // Type = 6-9 && NOT group-by
        else if ( (position == PatientAggregateReport.POSITION_ROW_PERIOD_COLUMN_DATA || position == PatientAggregateReport.POSITION_ROW_DATA_COLUMN_PERIOD)
            && deGroupBy == null )
        {
            sql = getAggregateReportSQL6WithoutGroup( programStage, orgunitIds.iterator().next(), facilityLB,
                filterSQL, deSum, periods, aggregateType, limit, useCompletedEvents, format );
        }

        // Type = 7 && Group-by
        else if ( (position == PatientAggregateReport.POSITION_ROW_ORGUNIT_COLUMN_DATA || position == PatientAggregateReport.POSITION_ROW_DATA_COLUMN_ORGUNIT)
            && deGroupBy != null )
        {
            sql = getAggregateReportSQL7( programStage, orgunitIds, facilityLB, filterSQL, deGroupBy, deSum, periods
                .iterator().next(), aggregateType, limit, useCompletedEvents, format );
        }

        // Type = 7 && NOT group-by
        else if ( (position == PatientAggregateReport.POSITION_ROW_ORGUNIT_COLUMN_DATA || position == PatientAggregateReport.POSITION_ROW_DATA_COLUMN_ORGUNIT)
            && deGroupBy == null )
        {
            sql = getAggregateReportSQL7WithoutGroup( programStage, orgunitIds, facilityLB, filterSQL, deSum, periods
                .iterator().next(), aggregateType, limit, useCompletedEvents, format );
        }

        // type = 8 && With group-by
        else if ( position == PatientAggregateReport.POSITION_ROW_DATA )
        {
            sql = getAggregateReportSQL8( programStage, orgunitIds, facilityLB, filterSQL, deGroupBy, periods
                .iterator().next(), aggregateType, limit, useCompletedEvents, format );
        }

        if ( !sql.isEmpty() )
        {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet( sql );

            // Type ==2 && ==9 && ==10
            if ( position == PatientAggregateReport.POSITION_ROW_PERIOD_COLUMN_ORGUNIT
                || position == PatientAggregateReport.POSITION_ROW_DATA_COLUMN_ORGUNIT
                || (position == PatientAggregateReport.POSITION_ROW_DATA_COLUMN_PERIOD && deGroupBy == null) )
            {
                pivotTable( grid, rowSet, displayTotals, i18n, format );
            }
            else
            {
                fillDataInGrid( grid, rowSet, displayTotals, i18n, format );
            }
        }

        return grid;
    }

    public int getOverDueCount( ProgramStage programStage, Collection<Integer> orgunitIds, Date startDate, Date endDate )
    {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add( Calendar.DATE, -1 );
        PeriodType.clearTimeOfDay( yesterday );
        Date now = yesterday.getTime();

        if ( endDate.before( now ) )
        {
            now = endDate;
        }

        Criteria criteria = getCriteria();
        criteria.createAlias( "programInstance", "programInstance" );
        criteria.createAlias( "programInstance.patient", "patient" );
        criteria.createAlias( "patient.organisationUnit", "regOrgunit" );
        criteria.add( Restrictions.eq( "programStage", programStage ) );
        criteria.add( Restrictions.isNull( "programInstance.endDate" ) );
        criteria.add( Restrictions.isNull( "executionDate" ) );
        criteria.add( Restrictions.between( "dueDate", startDate, now ) );
        criteria.add( Restrictions.in( "regOrgunit.id", orgunitIds ) );
        criteria.setProjection( Projections.rowCount() ).uniqueResult();

        Number rs = (Number) criteria.setProjection( Projections.rowCount() ).uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramStageInstance> get( Program program, Collection<Integer> orgunitIds, Date startDate,
        Date endDate, Boolean completed )
    {
        return getCriteria( program, orgunitIds, startDate, endDate, completed ).list();
    }

    public int count( Program program, Collection<Integer> orgunitIds, Date startDate, Date endDate, Boolean completed )
    {
        Number rs = (Number) getCriteria( program, orgunitIds, startDate, endDate, completed ).setProjection(
            Projections.rowCount() ).uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    public int count( ProgramStage programStage, Collection<Integer> orgunitIds, Date startDate, Date endDate,
        Boolean completed )
    {
        Number rs = (Number) getCriteria( programStage, orgunitIds, startDate, endDate, completed ).setProjection(
            Projections.rowCount() ).uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

    public Grid getCompleteness( OrganisationUnit orgunit, Program program, String startDate, String endDate, I18n i18n )
    {
        String sql = "SELECT ou.name as orgunit, ps.name as program_stage, psi.completeduser as user_name, count(psi.programstageinstanceid) as number_of_events "
            + "         FROM programstageinstance psi INNER JOIN programstage ps "
            + "                         ON psi.programstageid = ps.programstageid "
            + "                 INNER JOIN organisationunit ou "
            + "                         ON ou.organisationunitid=psi.organisationunitid"
            + "                 INNER JOIN program pg "
            + "                         ON pg.programid = ps.programid "
            + "         WHERE psi.organisationunitid = "
            + orgunit.getId()
            + "                 AND pg.programid = "
            + program.getId()
            + "         GROUP BY ou.name, ps.name, psi.completeduser, psi.completeddate, psi.completed "
            + "         HAVING psi.completeddate >= '"
            + startDate
            + "'                AND psi.completeddate <= '"
            + endDate
            + "' "
            + "                 AND psi.completed=true "
            + "         ORDER BY ou.name, ps.name, psi.completeduser";

        SqlRowSet rs = jdbcTemplate.queryForRowSet( sql );

        // Create column with Total column

        Grid grid = new ListGrid();

        grid.setTitle( program.getDisplayName() );
        grid.setSubtitle( i18n.getString( "from" ) + " " + startDate + " " + i18n.getString( "to" ) + " " + endDate );

        int cols = rs.getMetaData().getColumnCount();

        for ( int i = 1; i <= cols; i++ )
        {
            grid.addHeader( new GridHeader( i18n.getString( rs.getMetaData().getColumnLabel( i ) ), false, false ) );
        }

        GridUtils.addRows( grid, rs );

        return grid;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Criteria getCriteria( Program program, Collection<Integer> orgunitIds, Date startDate, Date endDate,
        Boolean completed )
    {
        Criteria criteria = getCriteria();
        criteria.createAlias( "programInstance", "programInstance" );
        criteria.add( Restrictions.eq( "programInstance.program", program ) );

        if ( completed == null )
        {
            criteria.add( Restrictions.between( "programInstance.enrollmentDate", startDate, endDate ) );
        }
        else
        {
            if ( completed )
            {
                criteria.add( Restrictions.and( Restrictions.eq( "completed", true ),
                    Restrictions.between( "executionDate", startDate, endDate ),
                    Restrictions.in( "organisationUnit.id", orgunitIds ) ) );
            }
            else
            {
                criteria.createAlias( "programInstance.patient", "patient" );
                criteria.createAlias( "patient.organisationUnit", "regOrgunit" );
                criteria.add( Restrictions.or( Restrictions.and( Restrictions.isNull( "executionDate" ),
                    Restrictions.between( "dueDate", startDate, endDate ),
                    Restrictions.in( "regOrgunit.id", orgunitIds ) ), Restrictions.and(
                    Restrictions.eq( "completed", false ), Restrictions.isNotNull( "executionDate" ),
                    Restrictions.between( "executionDate", startDate, endDate ),
                    Restrictions.in( "organisationUnit.id", orgunitIds ) ) ) );
            }
        }

        return criteria;
    }

    private Criteria getCriteria( ProgramStage programStage, Collection<Integer> orgunitIds, Date startDate,
        Date endDate, Boolean completed )
    {
        Criteria criteria = getCriteria();
        criteria.createAlias( "programInstance", "programInstance" );
        criteria.add( Restrictions.eq( "programStage", programStage ) );

        if ( completed == null )
        {
            criteria.createAlias( "programInstance.patient", "patient" );
            criteria.createAlias( "patient.organisationUnit", "regOrgunit" );
            criteria.add( Restrictions.or( Restrictions.and( Restrictions.eq( "completed", true ),
                Restrictions.between( "executionDate", startDate, endDate ),
                Restrictions.in( "organisationUnit.id", orgunitIds ) ), Restrictions.and(
                Restrictions.eq( "completed", false ), Restrictions.isNotNull( "executionDate" ),
                Restrictions.between( "executionDate", startDate, endDate ),
                Restrictions.in( "organisationUnit.id", orgunitIds ) ),
                Restrictions.and( Restrictions.eq( "completed", false ), Restrictions.isNull( "executionDate" ),
                    Restrictions.between( "dueDate", startDate, endDate ),
                    Restrictions.in( "regOrgunit.id", orgunitIds ) ), Restrictions.and(
                    Restrictions.eq( "status", ProgramStageInstance.SKIPPED_STATUS ),
                    Restrictions.between( "dueDate", startDate, endDate ),
                    Restrictions.in( "regOrgunit.id", orgunitIds ) ) ) );
        }
        else
        {
            if ( completed )
            {
                criteria.add( Restrictions.and( Restrictions.eq( "completed", true ),
                    Restrictions.between( "executionDate", startDate, endDate ),
                    Restrictions.in( "organisationUnit.id", orgunitIds ) ) );
            }
            else
            {
                criteria.createAlias( "programInstance.patient", "patient" );
                criteria.createAlias( "patient.organisationUnit", "regOrgunit" );
                criteria.add( Restrictions.and( Restrictions.eq( "completed", false ),
                    Restrictions.isNotNull( "executionDate" ),
                    Restrictions.between( "executionDate", startDate, endDate ),
                    Restrictions.in( "organisationUnit.id", orgunitIds ) ) );
            }
        }

        return criteria;
    }

    private String getTabularReportSql( Boolean anonynousEntryForm, boolean count, ProgramStage programStage,
        List<TabularReportColumn> columns, Collection<Integer> orgUnits, int level, int maxLevel, Date startDate,
        Date endDate, boolean descOrder, Boolean completed, Boolean accessPrivateInfo, Integer min, Integer max )
    {
        Set<String> deKeys = new HashSet<String>();
        String selector = count ? "count(*) " : "* ";

        String sql = "select " + selector + "from ( select DISTINCT psi.programstageinstanceid, psi.executiondate,";
        String where = "";
        String operator = "where ";

        if ( anonynousEntryForm == null || !anonynousEntryForm )
        {
            for ( int i = level; i <= maxLevel; i++ )
            {
                sql += "(select name from organisationunit where organisationunitid=ous.idlevel" + i + ") as level_"
                    + i + ",";
            }
        }

        for ( TabularReportColumn column : columns )
        {
            if ( column.isFixedAttribute() )
            {
                sql += "p." + column.getIdentifier() + ",";

                if ( column.hasQuery() )
                {
                    if ( column.isDateType() )
                    {
                        where += operator + column.getIdentifier() + " " + column.getQuery() + " ";
                    }
                    else
                    {
                        where += operator + "lower(" + column.getIdentifier() + ") " + column.getQuery() + " ";
                    }
                    operator = "and ";
                }
            }
            else if ( column.isIdentifierType() )
            {
                String deKey = "identifier_" + column.getIdentifier();
                if ( !deKeys.contains( deKey ) )
                {
                    sql += "(select identifier from patientidentifier where patientid=p.patientid and patientidentifiertypeid="
                        + column.getIdentifier() + ") as identifier_" + column.getIdentifier() + ",";
                }

                if ( column.hasQuery() )
                {
                    if ( column.isDateType() )
                    {
                        where += operator + "identifier_" + column.getIdentifier() + " " + column.getQuery() + " ";
                    }
                    else
                    {
                        where += operator + "lower(identifier_" + column.getIdentifier() + ") " + column.getQuery()
                            + " ";
                    }
                    operator = "and ";
                }
            }
            else if ( column.isDynamicAttribute() )
            {
                String deKey = "attribute_" + column.getIdentifier();
                if ( !deKeys.contains( deKey ) )
                {
                    sql += "(select value from patientattributevalue where patientid=p.patientid and patientattributeid="
                        + column.getIdentifier() + ") as attribute_" + column.getIdentifier() + ",";
                }

                if ( column.hasQuery() )
                {
                    if ( column.isDateType() )
                    {
                        where += operator + "attribute_" + column.getIdentifier() + " " + column.getQuery() + " ";
                    }
                    else
                    {
                        where += operator + "lower(attribute_" + column.getIdentifier() + ") " + column.getQuery()
                            + " ";
                    }
                    operator = "and ";
                }
            }
            if ( column.isNumberDataElement() )
            {
                String deKey = "element_" + column.getIdentifier();
                if ( !deKeys.contains( deKey ) )
                {
                    sql += "(select cast( value as "
                        + statementBuilder.getDoubleColumnType()
                        + " ) from patientdatavalue where programstageinstanceid=psi.programstageinstanceid and dataelementid="
                        + column.getIdentifier() + ") as element_" + column.getIdentifier() + ",";
                    deKeys.add( deKey );
                }

                if ( column.hasQuery() )
                {
                    where += operator + "element_" + column.getIdentifier() + " " + column.getQuery() + " ";
                    operator = "and ";
                }
            }
            else if ( column.isDataElement() )
            {
                String deKey = "element_" + column.getIdentifier();
                if ( !deKeys.contains( deKey ) )
                {
                    sql += "(select value from patientdatavalue where programstageinstanceid=psi.programstageinstanceid and dataelementid="
                        + column.getIdentifier() + ") as element_" + column.getIdentifier() + ",";
                    deKeys.add( deKey );
                }

                if ( column.hasQuery() )
                {
                    if ( column.isDateType() )
                    {
                        where += operator + "element_" + column.getIdentifier() + " " + column.getQuery() + " ";
                    }
                    else
                    {
                        where += operator + "lower(element_" + column.getIdentifier() + ") " + column.getQuery() + " ";
                    }
                    operator = "and ";
                }
            }
        }

        sql += " psi.completed ";
        if ( accessPrivateInfo != null && accessPrivateInfo )
        {
            sql += ", p.patientid ";
        }

        sql += "from programstageinstance psi ";
        sql += "left join programinstance pi on (psi.programinstanceid=pi.programinstanceid) ";
        sql += "left join patient p on (pi.patientid=p.patientid) ";
        sql += "join organisationunit ou on (ou.organisationunitid=psi.organisationunitid) ";

        if ( anonynousEntryForm == null || !anonynousEntryForm )
        {
            sql += "join _orgunitstructure ous on (psi.organisationunitid=ous.organisationunitid) ";
        }

        sql += "where psi.programstageid=" + programStage.getId() + " ";

        if ( startDate != null && endDate != null )
        {
            String sDate = DateUtils.getMediumDateString( startDate );
            String eDate = DateUtils.getMediumDateString( endDate );

            sql += "and psi.executiondate >= '" + sDate + "' ";
            sql += "and psi.executiondate <= '" + eDate + "' ";
        }

        if ( orgUnits != null )
        {
            sql += "and ou.organisationunitid in (" + TextUtils.getCommaDelimitedString( orgUnits ) + ") ";
        }
        if ( completed != null )
        {
            sql += "and psi.completed=" + completed + " ";
        }

        sql += "order by ";

        if ( anonynousEntryForm == null || !anonynousEntryForm )
        {
            for ( int i = level; i <= maxLevel; i++ )
            {
                sql += "level_" + i + ",";
            }
        }

        sql += "psi.executiondate ";
        sql += descOrder ? "desc " : "";
        sql += ") as tabular ";
        sql += where; // filters
        sql = sql.substring( 0, sql.length() - 1 ) + " "; // Remove last comma
        sql += (min != null && max != null) ? statementBuilder.limitRecord( min, max ) : "";

        return sql;
    }

    private Criteria getStatisticalProgramStageCriteria( ProgramStage programStage, Collection<Integer> orgunitIds,
        Date startDate, Date endDate, int status )
    {
        Criteria criteria = getCriteria( Restrictions.eq( "programStage", programStage ),
            Restrictions.isNull( "programInstance.endDate" ) );
        criteria.createAlias( "programInstance", "programInstance" );
        criteria.createAlias( "programInstance.patient", "patient" );
        criteria.createAlias( "patient.organisationUnit", "regOrgunit" );
        criteria.add( Restrictions.in( "regOrgunit.id", orgunitIds ) );

        switch ( status )
        {
        case ProgramStageInstance.COMPLETED_STATUS:
            criteria.add( Restrictions.eq( "completed", true ) );
            criteria.add( Restrictions.between( "executionDate", startDate, endDate ) );
            break;
        case ProgramStageInstance.VISITED_STATUS:
            criteria.add( Restrictions.eq( "completed", false ) );
            criteria.add( Restrictions.between( "executionDate", startDate, endDate ) );
            break;
        case ProgramStageInstance.FUTURE_VISIT_STATUS:
            criteria.add( Restrictions.between( "programInstance.enrollmentDate", startDate, endDate ) );
            criteria.add( Restrictions.isNull( "executionDate" ) );
            criteria.add( Restrictions.ge( "dueDate", new Date() ) );
            break;
        case ProgramStageInstance.LATE_VISIT_STATUS:
            criteria.add( Restrictions.between( "programInstance.enrollmentDate", startDate, endDate ) );
            criteria.add( Restrictions.isNull( "executionDate" ) );
            criteria.add( Restrictions.lt( "dueDate", new Date() ) );
            break;
        default:
            break;
        }

        return criteria;
    }

    /**
     * Aggregate report Position Orgunit Rows - Period Columns - Data Filter
     * Aggregate report Position Orgunit Columns - Period Rows - Data Filter
     * This result is not included orgunits without any data
     * 
     **/
    private String getAggregateReportSQL12( ProgramStage programStage, Collection<Integer> roots, String facilityLB,
        String filterSQL, Integer deGroupBy, Integer deSum, Collection<Period> periods, String aggregateType,
        Integer limit, Boolean useCompletedEvents, I18nFormat format )
    {
        String sql = "";

        // orgunit
        for ( Integer root : roots )
        {
            sql += " (SELECT ";

            sql += "( SELECT ou.name FROM organisationunit ou ";
            sql += "WHERE ou.organisationunitid=" + root + " ) as orgunit, ";

            Collection<Integer> allOrgunitIds = getOrganisationUnits( root, facilityLB );

            // -- period
            for ( Period period : periods )
            {
                String periodName = "";
                String startDate = format.formatDate( period.getStartDate() );
                String endDate = format.formatDate( period.getEndDate() );
                if ( period.getPeriodType() != null )
                {
                    periodName = format.formatPeriod( period );
                }
                else
                {
                    periodName = startDate + " -> " + endDate;
                }

                Collection<Integer> orgunitIds = getServiceOrgunit( allOrgunitIds, period );

                if ( orgunitIds.size() == 0 )
                {
                    sql += "(SELECT \'0\' ";
                }
                else
                {
                    if ( aggregateType.equals( PatientAggregateReport.AGGREGATE_TYPE_COUNT ) )
                    {
                        sql += "(SELECT count(DISTINCT psi_1.programstageinstanceid) ";
                    }
                    else
                    {
                        sql += "(SELECT " + aggregateType + "( cast( value as DOUBLE PRECISION )) ";
                    }
                    sql += "FROM programstageinstance psi_1 ";
                    sql += "        JOIN patientdatavalue pdv_1 ";
                    sql += "                ON psi_1.programstageinstanceid=pdv_1.programstageinstanceid ";
                    sql += "WHERE ";
                    sql += "     psi_1.organisationunitid in ( " + TextUtils.getCommaDelimitedString( orgunitIds )
                        + " )  AND ";
                    sql += "     psi_1.executiondate >= '" + startDate + "' AND ";
                    sql += "     psi_1.executiondate <= '" + endDate + "' AND ";
                    if ( deSum != null )
                    {
                        sql += " dataelementid=" + deSum + " AND ";
                    }
                    if ( useCompletedEvents != null )
                    {
                        sql += " AND psi_1.completed = " + useCompletedEvents + " AND ";
                    }
                    if ( deGroupBy != null )
                    {
                        sql += "(SELECT value from patientdatavalue ";
                        sql += "WHERE programstageinstanceid=psi_1.programstageinstanceid AND ";
                        sql += "      dataelementid=" + deGroupBy + ") is not null AND ";
                    }
                    sql += "     psi_1.programstageid=" + programStage.getId() + " ";
                    sql += filterSQL + "LIMIT 1 ) ";
                }

                sql += "as \"" + periodName + "\" ,";
            }
            // -- end period

            sql = sql.substring( 0, sql.length() - 1 ) + " ";
            sql += " ) ";
            sql += " UNION ";
        }

        sql = sql.substring( 0, sql.length() - 6 );
        sql += " ORDER BY orgunit asc ";
        if ( limit != null )
        {
            sql += "LIMIT " + limit;
        }

        return sql;
    }

    /**
     * Aggregate report Position Orgunit Rows - Period Rows - Data Filter
     * 
     **/
    private String getAggregateReportSQL3( int position, ProgramStage programStage, Collection<Integer> roots,
        String facilityLB, String filterSQL, Integer deGroupBy, Integer deSum, Collection<Period> periods,
        String aggregateType, Integer limit, Boolean useCompletedEvents, I18nFormat format )
    {
        String sql = "";

        for ( Integer root : roots )
        {
            Collection<Integer> allOrgunitIds = getOrganisationUnits( root, facilityLB );

            for ( Period period : periods )
            {
                String periodName = "";
                String startDate = format.formatDate( period.getStartDate() );
                String endDate = format.formatDate( period.getEndDate() );
                if ( period.getPeriodType() != null )
                {
                    periodName = format.formatPeriod( period );
                }
                else
                {
                    periodName = startDate + " -> " + endDate;
                }

                sql += "( SELECT ";
                sql += "( SELECT ou.name FROM organisationunit ou WHERE organisationunitid=" + root + " ) as orgunit, ";
                sql += "'" + periodName + "' as period, ";

                Collection<Integer> orgunitIds = getServiceOrgunit( allOrgunitIds, period );

                if ( orgunitIds.size() == 0 )
                {
                    sql += "(SELECT \'0\' ";
                }
                else
                {
                    if ( aggregateType.equals( PatientAggregateReport.AGGREGATE_TYPE_COUNT ) )
                    {
                        sql += "(SELECT count(DISTINCT psi_1.programstageinstanceid) ";
                    }
                    else
                    {
                        sql += "(SELECT " + aggregateType + "( cast( value as DOUBLE PRECISION )) ";
                    }
                    sql += "FROM ";
                    sql += "   patientdatavalue pdv_1 JOIN programstageinstance psi_1 ";
                    sql += "        ON psi_1.programstageinstanceid=pdv_1.programstageinstanceid ";
                    sql += "   JOIN organisationunit ou on (ou.organisationunitid=psi_1.organisationunitid ) ";
                    sql += "WHERE ";
                    sql += "    ou.organisationunitid in ( " + TextUtils.getCommaDelimitedString( orgunitIds )
                        + " ) AND ";
                    sql += "    psi_1.programstageid=" + programStage.getId() + " AND ";
                    if ( deSum != null )
                    {
                        sql += " dataelementid=" + deSum + " AND ";
                    }
                    if ( useCompletedEvents != null )
                    {
                        sql += " psi_1.completed = " + useCompletedEvents + " AND ";
                    }
                    if ( deGroupBy != null )
                    {
                        sql += "(SELECT value from patientdatavalue ";
                        sql += "WHERE programstageinstanceid=psi_1.programstageinstanceid AND ";
                        sql += "      dataelementid=" + deGroupBy + ") is not null AND ";
                    }
                    sql += "     psi_1.executiondate >= '" + startDate + "' AND ";
                    sql += "     psi_1.executiondate <= '" + endDate + "' ";
                    sql += filterSQL + " LIMIT 1 ) ";
                }

                sql += " as " + aggregateType;
                sql += "  ) ";
                sql += " UNION ";
            }
        }

        sql = sql.substring( 0, sql.length() - 6 ) + " ";
        sql += " ORDER BY orgunit asc ";
        if ( limit != null )
        {
            sql += "LIMIT " + limit;
        }

        return sql;
    }

    /**
     * Aggregate report Orgunit Filter - Period Rows - Data Filter
     * 
     **/
    private String getAggregateReportSQL4( int position, ProgramStage programStage, Collection<Integer> roots,
        String facilityLB, String filterSQL, Integer deGroupBy, Integer deSum, Collection<Period> periods,
        String aggregateType, Integer limit, Boolean useCompletedEvents, I18nFormat format )
    {
        String sql = "";

        for ( Integer root : roots )
        {
            Collection<Integer> allOrgunitIds = getOrganisationUnits( root, facilityLB );

            for ( Period period : periods )
            {
                String periodName = "";
                String startDate = format.formatDate( period.getStartDate() );
                String endDate = format.formatDate( period.getEndDate() );
                if ( period.getPeriodType() != null )
                {
                    periodName = format.formatPeriod( period );
                }
                else
                {
                    periodName = startDate + " -> " + endDate;
                }

                sql += "( SELECT ";
                sql += "'" + periodName + "' as period, ";

                Collection<Integer> orgunitIds = getServiceOrgunit( allOrgunitIds, period );
                if ( orgunitIds.size() == 0 )
                {
                    sql += "(SELECT \'0\' ";
                }
                else
                {
                    if ( aggregateType.equals( PatientAggregateReport.AGGREGATE_TYPE_COUNT ) )
                    {
                        sql += "(SELECT count(DISTINCT psi_1.programstageinstanceid) ";
                    }
                    else
                    {
                        sql += "(SELECT " + aggregateType + "( cast( value as DOUBLE PRECISION )) ";
                    }
                    sql += "FROM ";
                    sql += "   patientdatavalue pdv_1 JOIN programstageinstance psi_1 ";
                    sql += "        ON psi_1.programstageinstanceid=pdv_1.programstageinstanceid ";
                    sql += "   JOIN organisationunit ou on (ou.organisationunitid=psi_1.organisationunitid ) ";
                    sql += "WHERE ";
                    sql += "    ou.organisationunitid in ( " + TextUtils.getCommaDelimitedString( orgunitIds )
                        + " ) AND ";
                    sql += "    psi_1.programstageid=" + programStage.getId() + " AND ";
                    if ( deSum != null )
                    {
                        sql += " dataelementid=" + deSum + " AND ";
                    }
                    if ( useCompletedEvents != null )
                    {
                        sql += " psi_1.completed = " + useCompletedEvents + " AND ";
                    }
                    if ( deGroupBy != null )
                    {
                        sql += "(SELECT value from patientdatavalue ";
                        sql += "WHERE programstageinstanceid=psi_1.programstageinstanceid AND ";
                        sql += "      dataelementid=" + deGroupBy + ") is not null AND ";
                    }
                    sql += "     psi_1.executiondate >= '" + startDate + "' AND ";
                    sql += "     psi_1.executiondate <= '" + endDate + "' ";
                    sql += filterSQL + " LIMIT 1 ) ";
                }

                sql += " as " + aggregateType;
                sql += ") ";
                sql += " UNION ALL ";

            }
        }

        sql = sql.substring( 0, sql.length() - 10 );
        if ( limit != null )
        {
            sql += " LIMIT " + limit;
        }

        return sql;
    }

    /**
     * Aggregate report Position Orgunit Rows -Period Filter - Data Filter
     * 
     **/
    private String getAggregateReportSQL5( int position, ProgramStage programStage, Collection<Integer> roots,
        String facilityLB, String filterSQL, Integer deGroupBy, Integer deSum, Period period, String aggregateType,
        Integer limit, Boolean useCompletedEvents, I18nFormat format )
    {
        String sql = "";

        for ( Integer root : roots )
        {
            Collection<Integer> allOrgunitIds = getOrganisationUnits( root, facilityLB );

            sql += "( SELECT  ";
            sql += "( SELECT ou.name  ";
            sql += "FROM organisationunit ou  ";
            sql += "WHERE ou.organisationunitid=" + root + " ) as orgunit, ";

            Collection<Integer> orgunitIds = getServiceOrgunit( allOrgunitIds, period );
            if ( orgunitIds.size() == 0 )
            {
                sql += "(SELECT \'0\' ";
            }
            else
            {
                if ( aggregateType.equals( PatientAggregateReport.AGGREGATE_TYPE_COUNT ) )
                {
                    sql += "(SELECT count(DISTINCT psi_1.programstageinstanceid) ";
                }
                else
                {
                    sql += "(SELECT " + aggregateType + "( cast( value as DOUBLE PRECISION )) ";
                }
                sql += "FROM ";
                sql += "    patientdatavalue pdv_1 RIGHT JOIN programstageinstance psi_1 ";
                sql += "            ON psi_1.programstageinstanceid=pdv_1.programstageinstanceid ";
                sql += "WHERE ";
                sql += "    psi_1.organisationunitid in ( " + TextUtils.getCommaDelimitedString( orgunitIds )
                    + " ) AND ";
                sql += "    psi_1.programstageid=" + programStage.getId() + " AND ";
                sql += "    psi_1.executiondate >= '" + format.formatDate( period.getStartDate() ) + "' AND ";
                sql += "    psi_1.executiondate <= '" + format.formatDate( period.getEndDate() ) + "' ";
                sql += filterSQL + " ";
                if ( deGroupBy != null )
                {
                    sql += " AND (SELECT value from patientdatavalue ";
                    sql += "WHERE programstageinstanceid=psi_1.programstageinstanceid AND ";
                    sql += "      dataelementid=" + deGroupBy + ") is not null ";
                }
                if ( useCompletedEvents != null )
                {
                    sql += " AND psi_1.completed = " + useCompletedEvents + " ";
                }
                if ( deSum != null )
                {
                    sql += " AND dataelementid=" + deSum + "  LIMIT 1 ";
                }
            }

            sql += " ) as " + aggregateType + "  ) ";
            sql += " UNION ";

        }

        sql = sql.substring( 0, sql.length() - 6 ) + " ";
        sql += " ORDER BY orgunit asc ";
        if ( limit != null )
        {
            sql += " LIMIT " + limit;
        }

        return sql;
    }

    /**
     * Aggregate report Position Orgunit Filter - Period Rows - Data Columns
     * with group-by
     **/
    private String getAggregateReportSQL6( ProgramStage programStage, Integer root, String facilityLB,
        String filterSQL, Integer deGroupBy, Integer deSum, Collection<Period> periods, String aggregateType,
        Integer limit, Boolean useCompletedEvents, I18nFormat format )
    {
        String sql = "";
        Collection<Integer> allOrgunitIds = getOrganisationUnits( root, facilityLB );

        String dataValueSql = "SELECT DISTINCT(pdv.value) ";
        dataValueSql += "FROM patientdatavalue pdv JOIN programstageinstance psi";
        dataValueSql += "       ON pdv.programstageinstanceid=psi.programstageinstanceid ";
        dataValueSql += "WHERE pdv.dataelementid=" + deGroupBy + " AND ";
        dataValueSql += "       psi.organisationunitid in ( " + TextUtils.getCommaDelimitedString( allOrgunitIds )
            + " ) AND ";
        dataValueSql += "       psi.programstageid=" + programStage.getId() + " AND ( ";
        for ( Period period : periods )
        {
            dataValueSql += " ( psi.executiondate >= '" + format.formatDate( period.getStartDate() ) + "' AND ";
            dataValueSql += "   psi.executiondate <= '" + format.formatDate( period.getEndDate() ) + "') OR ";
        }
        dataValueSql = dataValueSql.substring( 0, dataValueSql.length() - 3 );
        dataValueSql += ") ORDER BY value asc";

        Collection<String> deValues = new HashSet<String>();
        try
        {
            deValues = jdbcTemplate.query( dataValueSql, new RowMapper<String>()
            {
                public String mapRow( ResultSet rs, int rowNum )
                    throws SQLException
                {
                    return rs.getString( 1 );
                }
            } );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

        if ( deValues.size() > 0 )
        {
            for ( Period period : periods )
            {
                String periodName = "";
                String startDate = format.formatDate( period.getStartDate() );
                String endDate = format.formatDate( period.getEndDate() );
                if ( period.getPeriodType() != null )
                {
                    periodName = format.formatPeriod( period );
                }
                else
                {
                    periodName = startDate + " -> " + endDate;
                }

                sql += "(SELECT DISTINCT '" + periodName + "' as period, ";

                Collection<Integer> orgunitIds = getServiceOrgunit( allOrgunitIds, period );
                if ( orgunitIds.size() == 0 )
                {
                    for ( String deValue : deValues )
                    {
                        sql += "(SELECT \'0\' as \"" + deValue + "\",";
                    }
                }
                else
                {
                    for ( String deValue : deValues )
                    {
                        if ( aggregateType.equals( PatientAggregateReport.AGGREGATE_TYPE_COUNT ) )
                        {
                            sql += "(SELECT count(DISTINCT psi_1.programstageinstanceid) ";
                        }
                        else
                        {
                            sql += "(SELECT " + aggregateType + "( cast( value as DOUBLE PRECISION )) ";
                        }
                        sql += "FROM programstageinstance psi_1 JOIN patientdatavalue pdv_1 ";
                        sql += "    on psi_1.programstageinstanceid = pdv_1.programstageinstanceid ";
                        sql += "WHERE ";
                        sql += "    psi_1.organisationunitid in ( " + TextUtils.getCommaDelimitedString( orgunitIds )
                            + "     ) AND ";
                        sql += "    psi_1.executiondate >= '" + startDate + "' AND ";
                        sql += "    psi_1.executiondate <= '" + endDate + "' ";
                        sql += filterSQL + " AND ";
                        sql += "        (SELECT value from patientdatavalue ";
                        sql += "        WHERE programstageinstanceid=psi_1.programstageinstanceid AND ";
                        sql += "              dataelementid=" + deGroupBy + ") = '" + deValue + "' ";
                        sql += "        LIMIT 1 ) as \"" + deValue + "\",";
                    }
                    sql = sql.substring( 0, sql.length() - 1 ) + " ";

                    sql += "FROM  programstageinstance psi JOIN patientdatavalue pdv ";
                    sql += "    on psi.programstageinstanceid = pdv.programstageinstanceid ";
                    sql += "WHERE ";
                    sql += "    psi.programstageid=" + programStage.getId() + " ";
                    if ( deSum != null )
                    {
                        sql += " AND dataelementid=" + deSum + "  ";
                    }
                    if ( useCompletedEvents != null )
                    {
                        sql += " AND psi.completed = " + useCompletedEvents + " ";
                    }
                    sql += "GROUP BY dataelementid ";
                }

                sql += ") UNION ALL ";

            }

            sql = sql.substring( 0, sql.length() - 10 );

            if ( limit != null )
            {
                sql += " LIMIT " + limit;
            }

        }

        return sql;
    }

    /**
     * Aggregate report Position Orgunit Filter - Period Rows - Data Columns
     * without group-by
     **/
    private String getAggregateReportSQL6WithoutGroup( ProgramStage programStage, Integer root, String facilityLB,
        String filterSQL, Integer deSum, Collection<Period> periods, String aggregateType, Integer limit,
        Boolean useCompletedEvents, I18nFormat format )
    {
        String sql = "";

        Collection<Integer> allOrgunitIds = getOrganisationUnits( root, facilityLB );

        for ( Period period : periods )
        {
            String periodName = "";
            String startDate = format.formatDate( period.getStartDate() );
            String endDate = format.formatDate( period.getEndDate() );
            if ( period.getPeriodType() != null )
            {
                periodName = format.formatPeriod( period );
            }
            else
            {
                periodName = startDate + " -> " + endDate;
            }

            sql += "(SELECT DISTINCT '" + periodName + "' as period, ";

            Collection<Integer> orgunitIds = getServiceOrgunit( allOrgunitIds, period );
            if ( orgunitIds.size() == 0 )
            {
                sql += "(SELECT \'0\' ";
            }
            else
            {
                if ( aggregateType.equals( PatientAggregateReport.AGGREGATE_TYPE_COUNT ) )
                {
                    sql += "(SELECT count(DISTINCT psi_1.programstageinstanceid) ";
                }
                else
                {
                    sql += "(SELECT " + aggregateType + "( cast( value as DOUBLE PRECISION )) ";
                }
                sql += "FROM programstageinstance psi_1 JOIN patientdatavalue pdv_1 ";
                sql += "    on psi_1.programstageinstanceid = pdv_1.programstageinstanceid ";
                sql += "WHERE ";
                sql += "    psi_1.organisationunitid in ( " + TextUtils.getCommaDelimitedString( orgunitIds )
                    + "     ) AND ";
                sql += "    psi_1.executiondate >= '" + startDate + "' AND ";
                sql += "    psi_1.executiondate <= '" + endDate + "' AND ";
                sql += "    psi_1.programstageid=" + programStage.getId() + " ";
                sql += filterSQL + "  LIMIT 1 ) as \"" + aggregateType + "\",";

                sql = sql.substring( 0, sql.length() - 1 ) + " ";

                sql += "FROM  programstageinstance psi JOIN patientdatavalue pdv ";
                sql += "    on psi.programstageinstanceid = pdv.programstageinstanceid ";
                sql += "WHERE ";
                sql += "    psi.programstageid=" + programStage.getId() + " ";
                if ( deSum != null )
                {
                    sql += " AND dataelementid=" + deSum + "  ";
                }
                if ( useCompletedEvents != null )
                {
                    sql += " AND psi.completed = " + useCompletedEvents + " ";
                }
                sql += "GROUP BY dataelementid ";
            }

            sql += ") UNION ALL ";
        }

        sql = sql.substring( 0, sql.length() - 10 );
        if ( limit != null )
        {
            sql += " LIMIT " + limit;
        }

        return sql;
    }

    /**
     * Aggregate report Position Orgunit Rows - Period Filter - Data Columns
     * 
     **/
    private String getAggregateReportSQL7( ProgramStage programStage, Collection<Integer> roots, String facilityLB,
        String filterSQL, Integer deGroupBy, Integer deSum, Period period, String aggregateType, Integer limit,
        Boolean useCompletedEvents, I18nFormat format )
    {
        String sql = "";

        Collection<Integer> allOrgunitIds = new HashSet<Integer>();

        for ( Integer root : roots )
        {
            allOrgunitIds.addAll( getOrganisationUnits( root, facilityLB ) );
        }

        String dataValueSql = "SELECT DISTINCT(pdv.value) ";
        dataValueSql += "FROM patientdatavalue pdv JOIN programstageinstance psi";
        dataValueSql += "       ON pdv.programstageinstanceid=psi.programstageinstanceid ";
        dataValueSql += "WHERE pdv.dataelementid=" + deGroupBy + " AND ";
        dataValueSql += "       psi.organisationunitid in ( " + TextUtils.getCommaDelimitedString( allOrgunitIds )
            + " ) AND ";
        dataValueSql += "       psi.programstageid=" + programStage.getId() + " AND ( ";

        dataValueSql += " ( psi.executiondate >= '" + format.formatDate( period.getStartDate() ) + "' AND ";
        dataValueSql += "   psi.executiondate <= '" + format.formatDate( period.getEndDate() ) + "') OR ";

        dataValueSql = dataValueSql.substring( 0, dataValueSql.length() - 3 );
        dataValueSql += ") ORDER BY value asc";

        Collection<String> deValues = new HashSet<String>();
        try
        {
            deValues = jdbcTemplate.query( dataValueSql, new RowMapper<String>()
            {
                public String mapRow( ResultSet rs, int rowNum )
                    throws SQLException
                {
                    return rs.getString( 1 );
                }
            } );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

        if ( deValues.size() > 0 )
        {
            for ( Integer root : roots )
            {
                allOrgunitIds = getOrganisationUnits( root, facilityLB );
                Collection<Integer> orgunitIds = getServiceOrgunit( allOrgunitIds, period );

                sql += "(SELECT ";
                sql += "( SELECT ou.name FROM organisationunit ou WHERE ou.organisationunitid=" + root
                    + " ) as orgunit, ";
                for ( String deValue : deValues )
                {
                    if ( aggregateType.equals( PatientAggregateReport.AGGREGATE_TYPE_COUNT ) )
                    {
                        sql += "(SELECT count(DISTINCT psi_1.programstageinstanceid) ";
                    }
                    else
                    {
                        sql += "(SELECT " + aggregateType + "( cast( value as DOUBLE PRECISION )) ";
                    }
                    sql += "FROM patientdatavalue pdv_1 ";
                    sql += "        inner join programstageinstance psi_1 ";
                    sql += "          on psi_1.programstageinstanceid = pdv_1.programstageinstanceid ";
                    sql += "WHERE ";
                    sql += "        psi_1.executiondate >= '" + format.formatDate( period.getStartDate() ) + "' AND ";
                    sql += "        psi_1.executiondate <= '" + format.formatDate( period.getEndDate() ) + "' AND ";
                    sql += "        psi_1.organisationunitid in (" + TextUtils.getCommaDelimitedString( orgunitIds )
                        + ") AND ";
                    if ( deSum != null )
                    {
                        sql += " dataelementid=" + deSum + " AND ";
                    }
                    if ( useCompletedEvents != null )
                    {
                        sql += " psi_1.completed = " + useCompletedEvents + " AND ";
                    }
                    sql += "        psi_1.programstageid=" + programStage.getId() + " ";
                    sql += filterSQL + " AND ";
                    sql += "   (SELECT value FROM patientdatavalue  ";
                    sql += "   WHERE programstageinstanceid=psi_1.programstageinstanceid AND ";
                    sql += "     dataelementid= pdv_1.dataelementid AND ";
                    sql += "     dataelementid=" + deGroupBy + "  ) = '" + deValue + "' ";
                    sql += "   LIMIT 1 ) as \"" + deValue + "\",";
                }

                sql = sql.substring( 0, sql.length() - 1 ) + " ) ";
                sql += " UNION ";
            }

            sql = sql.substring( 0, sql.length() - 6 );
            if ( limit != null )
            {
                sql += " LIMIT " + limit;
            }
        }

        return sql;
    }

    /**
     * Aggregate report Position Orgunit Rows - Period Filter - Data Columns
     * 
     **/
    private String getAggregateReportSQL7WithoutGroup( ProgramStage programStage, Collection<Integer> roots,
        String facilityLB, String filterSQL, Integer deSum, Period period, String aggregateType, Integer limit,
        Boolean useCompletedEvents, I18nFormat format )
    {

        String sql = "";

        for ( Integer root : roots )
        {
            Collection<Integer> allOrgunitIds = getOrganisationUnits( root, facilityLB );
            Collection<Integer> orgunitIds = getServiceOrgunit( allOrgunitIds, period );

            sql += "(SELECT ";
            sql += "( SELECT ou.name FROM organisationunit ou WHERE ou.organisationunitid=" + root + " ) as orgunit, ";

            if ( aggregateType.equals( PatientAggregateReport.AGGREGATE_TYPE_COUNT ) )
            {
                sql += "(SELECT count(DISTINCT psi_1.programstageinstanceid) ";
            }
            else
            {
                sql += "(SELECT " + aggregateType + "( cast( value as DOUBLE PRECISION )) ";
            }

            sql += "FROM patientdatavalue pdv_1 ";
            sql += "        inner join programstageinstance psi_1 ";
            sql += "          on psi_1.programstageinstanceid = pdv_1.programstageinstanceid ";
            sql += "WHERE ";
            sql += "        psi_1.executiondate >= '" + format.formatDate( period.getStartDate() ) + "' AND ";
            sql += "        psi_1.executiondate <= '" + format.formatDate( period.getEndDate() ) + "' AND ";
            if ( deSum != null )
            {
                sql += " dataelementid=" + deSum + " AND ";
            }
            if ( useCompletedEvents != null )
            {
                sql += " psi_1.completed = " + useCompletedEvents + " AND ";
            }
            sql += "        psi_1.organisationunitid in (" + TextUtils.getCommaDelimitedString( orgunitIds ) + ") AND ";
            sql += "        psi_1.programstageid=" + programStage.getId() + " ";
            sql += filterSQL + " LIMIT 1 ) as \"" + aggregateType + "\" ) ";

            sql += " UNION ";
        }

        sql = sql.substring( 0, sql.length() - 6 );
        if ( limit != null )
        {
            sql += " LIMIT " + limit;
        }

        return sql;
    }

    /**
     * Aggregate report Position Data Rows
     * 
     **/
    private String getAggregateReportSQL8( ProgramStage programStage, Collection<Integer> roots, String facilityLB,
        String filterSQL, Integer deGroupBy, Period period, String aggregateType, Integer limit,
        Boolean useCompletedEvents, I18nFormat format )
    {
        String sql = "";
        for ( Integer root : roots )
        {
            Collection<Integer> allOrgunitIds = getOrganisationUnits( root, facilityLB );
            Collection<Integer> orgunitIds = getServiceOrgunit( allOrgunitIds, period );

            sql += "(SELECT pdv_1.value, " + aggregateType + "(pdv_1.value) as \"" + aggregateType + "\" ";
            sql += "FROM patientdatavalue pdv_1 ";
            sql += "    JOIN programstageinstance psi_1 ";
            sql += "            ON psi_1.programstageinstanceid = pdv_1.programstageinstanceid ";
            sql += "WHERE ";
            sql += " psi_1.programstageid=" + programStage.getId() + " AND ";
            if ( useCompletedEvents != null )
            {
                sql += " psi_1.completed = " + useCompletedEvents + " AND ";
            }
            sql += "    psi_1.executiondate >= '" + format.formatDate( period.getStartDate() ) + "' AND ";
            sql += "    psi_1.executiondate <= '" + format.formatDate( period.getEndDate() ) + "' AND ";
            sql += "    psi_1.organisationunitid in( " + TextUtils.getCommaDelimitedString( orgunitIds ) + " )  ";
            if ( deGroupBy != null )
            {
                sql += " AND pdv_1.dataelementid=" + deGroupBy + " ";
            }
            sql += filterSQL + " ";
            sql += "GROUP BY pdv_1.value )";
            sql += " UNION ";
        }

        sql = sql.substring( 0, sql.length() - 6 ) + " ";
        sql += "ORDER BY  \"" + aggregateType + "\" desc ";
        if ( limit != null )
        {
            sql += " LIMIT " + limit;
        }

        return sql;
    }

    /**
     * Aggregate report Position Orgunit Filter - Period Columns - Data Rows
     * with group-by
     **/
    private String getAggregateReportSQL9( ProgramStage programStage, Integer root, String facilityLB,
        String filterSQL, Integer deGroupBy, Integer deSum, Collection<Period> periods, String aggregateType,
        Integer limit, Boolean useCompletedEvents, I18nFormat format )
    {
        String sql = "";
        Collection<Integer> allOrgunitIds = getOrganisationUnits( root, facilityLB );

        String dataValueSql = "SELECT DISTINCT(pdv.value) ";
        dataValueSql += "FROM patientdatavalue pdv JOIN programstageinstance psi";
        dataValueSql += "         ON pdv.programstageinstanceid=psi.programstageinstanceid ";
        dataValueSql += "WHERE pdv.dataelementid=" + deGroupBy + " AND ";
        dataValueSql += "       psi.organisationunitid in ( " + TextUtils.getCommaDelimitedString( allOrgunitIds )
            + " ) AND ";
        dataValueSql += "      psi.programstageid=" + programStage.getId() + " AND ( ";
        for ( Period period : periods )
        {
            dataValueSql += " ( psi.executiondate >= '" + format.formatDate( period.getStartDate() ) + "' AND ";
            dataValueSql += "   psi.executiondate <= '" + format.formatDate( period.getEndDate() ) + "') OR ";
        }
        dataValueSql = dataValueSql.substring( 0, dataValueSql.length() - 3 );
        dataValueSql += ") ORDER BY value asc";

        Collection<String> deValues = new HashSet<String>();
        try
        {
            deValues = jdbcTemplate.query( dataValueSql, new RowMapper<String>()
            {
                public String mapRow( ResultSet rs, int rowNum )
                    throws SQLException
                {
                    return rs.getString( 1 );
                }
            } );
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }

        String firstPeriodName = "";

        String groupByName = dataElementService.getDataElement( deGroupBy ).getDisplayName();
        for ( String deValue : deValues )
        {
            sql += "(SELECT DISTINCT '" + deValue + "' as \"" + groupByName + "\", ";

            for ( Period period : periods )
            {
                String periodName = "";
                String startDate = format.formatDate( period.getStartDate() );
                String endDate = format.formatDate( period.getEndDate() );
                if ( period.getPeriodType() != null )
                {
                    periodName = format.formatPeriod( period );
                }
                else
                {
                    periodName = startDate + " -> " + endDate;
                }

                if ( firstPeriodName.isEmpty() )
                {
                    firstPeriodName = periodName;
                }

                Collection<Integer> orgunitIds = getServiceOrgunit( allOrgunitIds, period );
                if ( orgunitIds.size() == 0 )
                {
                    sql += "(SELECT \'0\' ";
                }
                else
                {
                    if ( aggregateType.equals( PatientAggregateReport.AGGREGATE_TYPE_COUNT ) )
                    {
                        sql += "(SELECT count(DISTINCT psi_1.programstageinstanceid) ";
                    }
                    else
                    {
                        sql += "(SELECT " + aggregateType + "( cast( value as DOUBLE PRECISION )) ";
                    }

                    sql += "FROM programstageinstance psi_1 JOIN patientdatavalue pdv_1 ";
                    sql += "    on psi_1.programstageinstanceid = pdv_1.programstageinstanceid ";
                    sql += "WHERE ";
                    sql += "    psi_1.organisationunitid in ( " + TextUtils.getCommaDelimitedString( orgunitIds )
                        + "     ) AND ";
                    sql += "    psi_1.executiondate >= '" + startDate + "' AND ";
                    sql += "    psi_1.executiondate <= '" + endDate + "' ";
                    sql += filterSQL + " AND ";
                    sql += "        (SELECT value from patientdatavalue ";
                    sql += "        WHERE programstageinstanceid=psi_1.programstageinstanceid AND ";
                    sql += "              dataelementid=" + deGroupBy + ") = '" + deValue + "' ";
                    if ( deSum != null )
                    {
                        sql += " AND dataelementid=" + deSum + " ";
                    }
                    if ( useCompletedEvents != null )
                    {
                        sql += " AND psi_1.completed = " + useCompletedEvents + " ";
                    }

                }
                sql += ") as \"" + periodName + "\",";
            }
            sql = sql.substring( 0, sql.length() - 1 );
            sql += " ) UNION ALL ";
        }

        if ( !sql.isEmpty() )
        {
            sql = sql.substring( 0, sql.length() - 10 );
            if ( periods.size() == 1 )
            {
                sql += "ORDER BY  \"" + firstPeriodName + "\" desc ";
            }

            if ( limit != null )
            {
                sql += " LIMIT " + limit;
            }
        }

        return sql;
    }

    private String filterSQLStatement( Map<Integer, Collection<String>> deFilters )
    {
        String filter = "";
        if ( deFilters != null )
        {
            // Get filter criteria
            Iterator<Integer> iterFilter = deFilters.keySet().iterator();
            while ( iterFilter.hasNext() )
            {
                Integer id = iterFilter.next();
                for ( String filterValue : deFilters.get( id ) )
                {
                    DataElement dataElement = dataElementService.getDataElement( id );
                    int index = filterValue.indexOf( PatientAggregateReport.SEPARATE_FILTER );
                    String operator = (filterValue.substring( 0, index ));
                    String value = filterValue.substring( index + 1, filterValue.length() );
                    filter += "AND (SELECT ";
                    if ( dataElement.getType().equals( DataElement.VALUE_TYPE_INT ) )
                    {
                        filter += "cast(value as " + statementBuilder.getDoubleColumnType() + ") ";

                    }
                    else
                    {
                        filter += " value ";
                    }
                    filter += "FROM patientdatavalue ";
                    filter += "WHERE programstageinstanceid=psi_1.programstageinstanceid AND ";
                    filter += "dataelementid=" + id + "  ";
                    filter += ") " + operator + " " + value + " ";
                }
            }
        }

        return filter;
    }

    private String getFilterOrgunitDescription( Collection<Integer> orgunitIds )
    {
        String description = "";
        for ( Integer orgunit : orgunitIds )
        {
            description += organisationUnitService.getOrganisationUnit( orgunit ).getDisplayName() + " AND ";
        }

        return description.substring( 0, description.length() - 5 );
    }

    private String getFilterPeriodDescription( Collection<Period> periods, I18nFormat format )
    {
        String description = "";
        for ( Period period : periods )
        {
            String startDate = format.formatDate( period.getStartDate() );
            String endDate = format.formatDate( period.getEndDate() );
            if ( period.getPeriodType() != null )
            {
                description += format.formatPeriod( period );
            }
            else
            {
                description += startDate + " -> " + endDate;
            }
            description += " AND ";
        }

        return description.substring( 0, description.length() - 5 );
    }

    private String getFilterDataDescription( Map<Integer, Collection<String>> deFilters )
    {
        String description = "";

        if ( deFilters != null )
        {
            // Get filter criteria
            Iterator<Integer> iterFilter = deFilters.keySet().iterator();
            while ( iterFilter.hasNext() )
            {
                Integer id = iterFilter.next();
                String deName = dataElementService.getDataElement( id ).getDisplayName();
                for ( String filterValue : deFilters.get( id ) )
                {
                    int index = filterValue.indexOf( PatientAggregateReport.SEPARATE_FILTER );
                    String operator = (filterValue.substring( 0, index ));
                    String value = filterValue.substring( index + 1, filterValue.length() );

                    description += deName + " " + operator + " " + value + " AND ";
                }
            }
            description = description.substring( 0, description.length() - 5 );
        }

        return description;
    }

    public int averageNumberCompleted( Program program, Collection<Integer> orgunitIds, Date startDate, Date endDate,
        Integer status )
    {
        Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstancesByStatus(
            ProgramInstance.STATUS_COMPLETED, program, orgunitIds, startDate, endDate );
        Criteria criteria = getCriteria();
        criteria.createAlias( "programInstance", "programInstance" );
        criteria.createAlias( "programStage", "programStage" );
        criteria.createAlias( "programInstance.patient", "patient" );
        criteria.add( Restrictions.eq( "programInstance.program", program ) );
        criteria.add( Restrictions.eq( "programInstance.status", status ) );
        criteria.add( Restrictions.in( "organisationUnit.id", orgunitIds ) );
        criteria.add( Restrictions.between( "programInstance.endDate", startDate, endDate ) );
        criteria.add( Restrictions.eq( "completed", true ) );
        if ( programInstances != null && programInstances.size() > 0 )
        {
            criteria.add( Restrictions.not( Restrictions.in( "programInstance", programInstances ) ) );
        }

        Number rs = (Number) criteria.setProjection( Projections.rowCount() ).uniqueResult();
        return rs != null ? rs.intValue() : 0;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Integer> getOrgunitIds( Date startDate, Date endDate )
    {
        Criteria criteria = getCriteria();
        criteria.add( Restrictions.between( "executionDate", startDate, endDate ) );
        criteria.createAlias( "organisationUnit", "orgunit" );
        criteria.setProjection( Projections.distinct( Projections.projectionList().add(
            Projections.property( "orgunit.id" ), "orgunitid" ) ) );
        return criteria.list();
    }

    // ---------------------------------------------------------------------
    // Get orgunitIds
    // ---------------------------------------------------------------------

    private Collection<Integer> getOrganisationUnits( Integer root, String facilityLB )
    {
        Set<Integer> orgunitIds = new HashSet<Integer>();

        if ( facilityLB.equals( "selected" ) )
        {
            orgunitIds.add( root );
        }
        else if ( facilityLB.equals( "childrenOnly" ) )
        {
            orgunitIds.addAll( organisationUnitService.getOrganisationUnitHierarchy().getChildren( root ) );
            orgunitIds.remove( root );
        }
        else
        {
            orgunitIds.addAll( organisationUnitService.getOrganisationUnitHierarchy().getChildren( root ) );
        }

        return orgunitIds;
    }

    /**
     * Return the Ids of organisation units which events happened.
     * 
     */
    private Collection<Integer> getServiceOrgunit( Collection<Integer> orgunitIds, Period period )
    {
        String sql = "select distinct organisationunitid from programstageinstance where executiondate>= '"
            + DateUtils.getMediumDateString( period.getStartDate() ) + "' and executiondate<='"
            + DateUtils.getMediumDateString( period.getEndDate() ) + "' and organisationunitid in ( "
            + TextUtils.getCommaDelimitedString( orgunitIds ) + " )";

        Collection<Integer> result = new HashSet<Integer>();
        result = jdbcTemplate.query( sql, new RowMapper<Integer>()
        {
            public Integer mapRow( ResultSet rs, int rowNum )
                throws SQLException
            {
                return rs.getInt( 1 );
            }
        } );

        return result;
    }

    private void fillDataInGrid( Grid grid, SqlRowSet rs, Boolean displayTotals, I18n i18n, I18nFormat format )
    {
        int cols = rs.getMetaData().getColumnCount();
        int dataCols = 0;

        // Create column with Total column
        for ( int i = 1; i <= cols; i++ )
        {
            grid.addHeader( new GridHeader( i18n.getString( rs.getMetaData().getColumnLabel( i ) ), false, false ) );
            if ( rs.getMetaData().getColumnType( i ) != Types.VARCHAR )
            {
                dataCols++;
            }
        }

        // Add total column if the number of columns is greater then 1
        if ( displayTotals && dataCols > 1 )
        {
            grid.addHeader( new GridHeader( i18n.getString( "total" ), false, false ) );
        }

        double[] sumRow = new double[rs.getMetaData().getColumnCount() + 1];
        while ( rs.next() )
        {
            grid.addRow();

            double total = 0;
            for ( int i = 1; i <= cols; i++ )
            {
                // meta column
                if ( rs.getMetaData().getColumnType( i ) == Types.VARCHAR )
                {
                    grid.addValue( rs.getObject( i ) );
                }
                // values
                else
                {
                    double value = rs.getDouble( i );
                    sumRow[i] += value;
                    total += value;
                    grid.addValue( format.formatValue( value ) );
                }
            }

            // total
            if ( displayTotals && dataCols > 1 )
            {
                grid.addValue( format.formatValue( total ) );
            }
        }

        // Add total row if the number of rows is greater then 1
        if ( displayTotals && grid.getRows().size() > 1 )
        {
            grid.addRow();
            grid.addValue( i18n.getString( "total" ) );
            int total = 0;
            for ( int i = cols - dataCols + 1; i <= cols; i++ )
            {
                grid.addValue( format.formatValue( sumRow[i] ) );

                total += sumRow[i];
            }
            if ( cols > cols - dataCols + 1 )
            {
                grid.addValue( format.formatValue( total ) );
            }
        }
    }

    private void pivotTable( Grid grid, SqlRowSet rowSet, Boolean displayTotals, I18n i18n, I18nFormat format )
    {
        try
        {
            int cols = rowSet.getMetaData().getColumnCount();
            int rows = 0;
            double total = 0;
            Map<Integer, List<Object>> columnValues = new HashMap<Integer, List<Object>>();
            int index = 2;

            grid.addHeader( new GridHeader( "", false, true ) );
            while ( rowSet.next() )
            {
                rows++;

                // Header grid
                grid.addHeader( new GridHeader( rowSet.getString( 1 ), false, false ) );

                // Column values
                List<Object> column = new ArrayList<Object>();
                total = 0;
                for ( int i = 2; i <= cols; i++ )
                {
                    column.add( rowSet.getObject( i ) );

                    // Total value of the column
                    if ( rowSet.getMetaData().getColumnType( i ) != Types.VARCHAR )
                    {
                        total += rowSet.getDouble( i );
                    }
                }

                // Add total value of the column
                if ( displayTotals && cols > 2 )
                {
                    // grid.addValue( format.formatValue( total ) );
                    column.add( format.formatValue( total ) );
                }

                columnValues.put( index, column );
                index++;
            }

            // Add total header
            if ( displayTotals && rows > 1 )
            {
                grid.addHeader( new GridHeader( i18n.getString( "total" ), false, false ) );
            }

            // First column
            List<Object> column = new ArrayList<Object>();
            for ( int i = 2; i <= cols; i++ )
            {
                grid.addRow();
                column.add( i18n.getString( rowSet.getMetaData().getColumnLabel( i ) ) );
            }

            if ( displayTotals && cols > 2 )
            {
                grid.addRow();
                column.add( i18n.getString( "total" ) );
            }
            grid.addColumn( column );

            // Other columns
            for ( int i = 2; i < index; i++ )
            {
                grid.addColumn( columnValues.get( i ) );
            }

            if ( displayTotals && rows > 1 )
            {
                // Total column
                int allTotal = 0;
                column = new ArrayList<Object>();
                for ( int j = 0; j < cols - 1; j++ )
                {
                    total = 0;
                    for ( int i = 2; i < index; i++ )
                    {
                        if ( rowSet.getMetaData().getColumnType( j + 2 ) != Types.VARCHAR )
                        {
                            total += (Long) columnValues.get( i ).get( j );
                        }
                    }
                    column.add( format.formatValue( total ) );

                    allTotal += total;
                }
                if ( cols > 2 )
                {
                    column.add( format.formatValue( allTotal ) );
                }
                grid.addColumn( column );
            }
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
    }

}
