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
package org.hisp.dhis.program;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.GridHeader;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.patientreport.TabularReportColumn;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.sms.outbound.OutboundSms;
import org.hisp.dhis.system.grid.ListGrid;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
@Transactional
public class DefaultProgramStageInstanceService
    implements ProgramStageInstanceService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramStageInstanceStore programStageInstanceStore;

    public void setProgramStageInstanceStore( ProgramStageInstanceStore programStageInstanceStore )
    {
        this.programStageInstanceStore = programStageInstanceStore;
    }

    private ProgramInstanceService programInstanceService;

    public void setProgramInstanceService( ProgramInstanceService programInstanceService )
    {
        this.programInstanceService = programInstanceService;
    }

    private PatientDataValueService patientDataValueService;

    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    public int addProgramStageInstance( ProgramStageInstance programStageInstance )
    {
        return programStageInstanceStore.save( programStageInstance );
    }

    public void deleteProgramStageInstance( ProgramStageInstance programStageInstance )
    {
        programStageInstanceStore.delete( programStageInstance );
    }

    public Collection<ProgramStageInstance> getAllProgramStageInstances()
    {
        return programStageInstanceStore.getAll();
    }

    public ProgramStageInstance getProgramStageInstance( int id )
    {
        return programStageInstanceStore.get( id );
    }

    public ProgramStageInstance getProgramStageInstance( ProgramInstance programInstance, ProgramStage programStage )
    {
        return programStageInstanceStore.get( programInstance, programStage );
    }

    public Collection<ProgramStageInstance> getProgramStageInstances( ProgramStage programStage )
    {
        return programStageInstanceStore.get( programStage );
    }

    public void updateProgramStageInstance( ProgramStageInstance programStageInstance )
    {
        programStageInstanceStore.update( programStageInstance );
    }

    public Map<Integer, Integer> statusProgramStageInstances( Collection<ProgramStageInstance> programStageInstances )
    {
        Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();

        for ( ProgramStageInstance programStageInstance : programStageInstances )
        {
            colorMap.put( programStageInstance.getId(), programStageInstance.getEventStatus() );
        }

        return colorMap;
    }

    public Collection<ProgramStageInstance> getProgramStageInstances( Collection<ProgramInstance> programInstances )
    {
        return programStageInstanceStore.get( programInstances );
    }

    public Collection<ProgramStageInstance> getProgramStageInstances( Date dueDate )
    {
        return programStageInstanceStore.get( dueDate );
    }

    public Collection<ProgramStageInstance> getProgramStageInstances( Date dueDate, Boolean completed )
    {
        return programStageInstanceStore.get( dueDate, completed );
    }

    public Collection<ProgramStageInstance> getProgramStageInstances( Date startDate, Date endDate )
    {
        return programStageInstanceStore.get( startDate, endDate );
    }

    public Collection<ProgramStageInstance> getProgramStageInstances( Date startDate, Date endDate, Boolean completed )
    {
        return programStageInstanceStore.get( startDate, endDate, completed );
    }

    public List<ProgramStageInstance> get( OrganisationUnit unit, Date after, Date before, Boolean completed )
    {
        return programStageInstanceStore.get( unit, after, before, completed );
    }

    public List<ProgramStageInstance> getProgramStageInstances( Patient patient, Boolean completed )
    {
        return programStageInstanceStore.get( patient, completed );
    }

    public Grid getTabularReport( ProgramStage programStage, List<TabularReportColumn> columns,
        Collection<Integer> organisationUnits, int level, Date startDate, Date endDate, boolean descOrder,
        Boolean completed, Boolean accessPrivateInfo, Integer min, Integer max, I18n i18n )
    {
        int maxLevel = organisationUnitService.getMaxOfOrganisationUnitLevels();

        Map<Integer, OrganisationUnitLevel> orgUnitLevelMap = organisationUnitService.getOrganisationUnitLevelMap();

        return programStageInstanceStore.getTabularReport( programStage, orgUnitLevelMap, organisationUnits, columns,
            level, maxLevel, startDate, endDate, descOrder, completed, accessPrivateInfo, min, max, i18n );
    }

    public int getTabularReportCount( ProgramStage programStage, List<TabularReportColumn> columns,
        Collection<Integer> organisationUnits, int level, Boolean completed, Date startDate, Date endDate )
    {
        int maxLevel = organisationUnitService.getMaxOfOrganisationUnitLevels();

        return programStageInstanceStore.getTabularReportCount( programStage, columns, organisationUnits, level,
            maxLevel, startDate, endDate, completed );
    }

    public List<Grid> getProgramStageInstancesReport( ProgramInstance programInstance, I18nFormat format, I18n i18n )
    {
        List<Grid> grids = new ArrayList<Grid>();

        Collection<ProgramStageInstance> programStageInstances = programInstance.getProgramStageInstances();

        for ( ProgramStageInstance programStageInstance : programStageInstances )
        {
            Grid grid = new ListGrid();

            // -----------------------------------------------------------------
            // Title
            // -----------------------------------------------------------------

            Date executionDate = programStageInstance.getExecutionDate();
            String executionDateValue = (executionDate != null) ? format.formatDate( programStageInstance
                .getExecutionDate() ) : "[" + i18n.getString( "none" ) + "]";

            grid.setTitle( programStageInstance.getProgramStage().getName() );
            grid.setSubtitle( i18n.getString( "due_date" ) + ": "
                + format.formatDate( programStageInstance.getDueDate() ) + " - " + i18n.getString( "report_date" )
                + ": " + executionDateValue );

            // -----------------------------------------------------------------
            // Headers
            // -----------------------------------------------------------------

            grid.addHeader( new GridHeader( i18n.getString( "name" ), false, true ) );
            grid.addHeader( new GridHeader( i18n.getString( "value" ), false, true ) );

            // -----------------------------------------------------------------
            // Values
            // -----------------------------------------------------------------

            Collection<PatientDataValue> patientDataValues = patientDataValueService
                .getPatientDataValues( programStageInstance );

            if ( executionDate == null || patientDataValues == null || patientDataValues.size() == 0 )
            {
                grid.addRow();
                grid.addValue( "[" + i18n.getString( "none" ) + "]" );
                grid.addValue( "" );
            }
            else
            {
                for ( PatientDataValue patientDataValue : patientDataValues )
                {
                    DataElement dataElement = patientDataValue.getDataElement();

                    grid.addRow();
                    grid.addValue( dataElement.getName() );

                    if ( dataElement.getType().equals( DataElement.VALUE_TYPE_BOOL ) )
                    {
                        grid.addValue( i18n.getString( patientDataValue.getValue() ) );
                    }
                    else
                    {
                        grid.addValue( patientDataValue.getValue() );
                    }
                }
            }

            grids.add( grid );
        }

        return grids;
    }

    public void removeEmptyEvents( ProgramStage programStage, OrganisationUnit organisationUnit )
    {
        programStageInstanceStore.removeEmptyEvents( programStage, organisationUnit );
    }

    @Override
    public void updateProgramStageInstances( Collection<Integer> programStageInstanceIds, OutboundSms outboundSms )
    {
        programStageInstanceStore.update( programStageInstanceIds, outboundSms );
    }

    public Collection<SchedulingProgramObject> getSendMesssageEvents()
    {
        return programStageInstanceStore.getSendMesssageEvents();
    }

    public Collection<ProgramStageInstance> getProgramStageInstances( Program program, Collection<Integer> orgunitIds,
        Date startDate, Date endDate, Boolean completed )
    {
        return programStageInstanceStore.get( program, orgunitIds, startDate, endDate, completed );
    }

    public int getProgramStageInstanceCount( Program program, Collection<Integer> orgunitIds, Date startDate,
        Date endDate, Boolean completed )
    {
        return programStageInstanceStore.count( program, orgunitIds, startDate, endDate, completed );
    }

    public int getProgramStageInstanceCount( ProgramStage programStage, Collection<Integer> orgunitIds, Date startDate,
        Date endDate, Boolean completed )
    {
        return programStageInstanceStore.count( programStage, orgunitIds, startDate, endDate, completed );
    }

    @Override
    public Grid getStatisticalReport( Program program, Collection<Integer> orgunitIds, Date startDate, Date endDate,
        I18n i18n, I18nFormat format )
    {
        Grid grid = new ListGrid();
        grid.setTitle( i18n.getString( "program_overview" ) + " - " + program.getDisplayName() );
        grid.setSubtitle( i18n.getString( "from" ) + " " + format.formatDate( startDate ) + "  "
            + i18n.getString( "to" ) + " " + format.formatDate( endDate ) );

        grid.addHeader( new GridHeader( "", false, true ) );
        grid.addHeader( new GridHeader( "", false, false ) );
        grid.addHeader( new GridHeader( "", false, false ) );
        grid.addHeader( new GridHeader( "", false, false ) );
        grid.addHeader( new GridHeader( "", false, false ) );
        grid.addHeader( new GridHeader( "", false, false ) );
        grid.addHeader( new GridHeader( "", false, false ) );
        grid.addHeader( new GridHeader( "", false, false ) );

        // Total new enrollments in the period

        int total = programInstanceService.countProgramInstances( program, orgunitIds, startDate, endDate );
        grid.addRow();
        grid.addValue( i18n.getString( "total_new_enrollments_in_this_period" ) );
        grid.addValue( total );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );

        // Total programs completed in this period

        int totalDiscontinued = programInstanceService.countUnenrollment( program, orgunitIds, startDate, endDate );
        int totalCompleted = programInstanceService.countProgramInstances( program, orgunitIds, startDate, endDate,
            true );
        grid.addRow();
        grid.addValue( i18n.getString( "total_programs_completed_in_this_period" ) );
        grid.addValue( totalCompleted - totalDiscontinued );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );

        // Total programs discontinued (un-enrollments)

        grid.addRow();
        grid.addValue( i18n.getString( "total_programs_discontinued_unenrollments" ) );
        grid.addValue( totalDiscontinued );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );

        // Average number of stages for complete programs

        grid.addRow();
        grid.addValue( i18n.getString( "average_number_of_stages_for_complete_programs" ) );
        double percent = 0.0;
        if ( totalCompleted != 0 )
        {
            int stageCompleted = averageNumberCompletedProgramInstance( program, orgunitIds, startDate, endDate, true );
            percent = (stageCompleted + 0.0) / (totalCompleted - totalDiscontinued);
        }
        grid.addValue( format.formatValue( percent ) );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );

        // Add empty row

        grid.addRow();
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );

        // Summary by stage

        grid.addRow();
        grid.addValue( i18n.getString( "summary_by_stage" ) );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );
        grid.addValue( "" );

        // Add titles for stage details

        grid.addRow();
        grid.addValue( i18n.getString( "program_stages" ) );
        grid.addValue( i18n.getString( "visits_scheduled_all" ) );
        grid.addValue( i18n.getString( "visits_done" ) );
        grid.addValue( i18n.getString( "visits_done_percent" ) );
        grid.addValue( i18n.getString( "forms_completed" ) );
        grid.addValue( i18n.getString( "forms_completed_percent" ) );
        grid.addValue( i18n.getString( "visits_overdue" ) );
        grid.addValue( i18n.getString( "visits_overdue_percent" ) );

        // Add values for stage details

        for ( ProgramStage programStage : program.getProgramStages() )
        {
            grid.addRow();
            grid.addValue( programStage.getDisplayName() );

            // Visits scheduled (All)

            int totalAll = this.getProgramStageInstanceCount( programStage, orgunitIds, startDate, endDate, null );
            grid.addValue( totalAll );

            // Visits done (#) = Incomplete + Complete stages.

            int totalCompletedEvent = this.getProgramStageInstanceCount( programStage, orgunitIds, startDate, endDate,
                true );
            int totalVisit = totalCompletedEvent
                + this.getProgramStageInstanceCount( programStage, orgunitIds, startDate, endDate, false );
            grid.addValue( totalVisit );

            // Visits done (%)

            percent = 0.0;
            if ( totalAll != 0 )
            {
                percent = (totalVisit + 0.0) * 100 / totalAll;
            }
            grid.addValue( format.formatValue( percent ) + "%" );

            // Forms completed (#) = Program stage instances where the user has
            // clicked complete.

            grid.addValue( totalCompletedEvent );

            // Forms completed (%)
            if ( totalAll != 0 )
            {
                percent = (totalCompletedEvent + 0.0) * 100 / totalAll;
            }
            grid.addValue( format.formatValue( percent ) + "%" );

            // Visits overdue (#)

            int overdue = this.getOverDueEventCount( programStage, orgunitIds, startDate, endDate );
            grid.addValue( overdue );

            // Visits overdue (%)

            percent = 0.0;
            if ( totalAll != 0 )
            {
                percent = (overdue + 0.0) * 100 / totalAll;
            }
            grid.addValue( format.formatValue( percent ) + "%" );
        }

        return grid;
    }

    public List<ProgramStageInstance> getStatisticalProgramStageDetailsReport( ProgramStage programStage,
        Collection<Integer> orgunitIds, Date startDate, Date endDate, int status, Integer min, Integer max )
    {
        return programStageInstanceStore.getStatisticalProgramStageDetailsReport( programStage, orgunitIds, startDate,
            endDate, status, min, max );
    }

    @Override
    public Grid getAggregateReport( int position, ProgramStage programStage, Collection<Integer> orgunitIds,
        String facilityLB, Integer deGroupBy, Integer deSum, Map<Integer, Collection<String>> deFilters,
        List<Period> periods, String aggregateType, Integer limit, Boolean useCompletedEvents, I18nFormat format,
        I18n i18n )
    {
        return programStageInstanceStore.getAggregateReport( position, programStage, orgunitIds, facilityLB, deGroupBy,
            deSum, deFilters, periods, aggregateType, limit, useCompletedEvents, format, i18n );
    }

    @Override
    public int getOverDueEventCount( ProgramStage programStage, Collection<Integer> orgunitIds, Date startDate,
        Date endDate )
    {
        return programStageInstanceStore.getOverDueCount( programStage, orgunitIds, startDate, endDate );
    }

    @Override
    public int averageNumberCompletedProgramInstance( Program program, Collection<Integer> orgunitIds, Date startDate,
        Date endDate, Boolean completed )
    {
        return programStageInstanceStore.averageNumberCompleted( program, orgunitIds, startDate, endDate, completed );
    }

    @Override
    public Collection<Integer> getOrganisationUnitIds( Date startDate, Date endDate )
    {
        return programStageInstanceStore.getOrgunitIds( startDate, endDate );
    }
    
}
