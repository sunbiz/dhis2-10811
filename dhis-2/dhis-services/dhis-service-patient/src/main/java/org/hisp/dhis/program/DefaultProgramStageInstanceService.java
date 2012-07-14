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
        Boolean completed, Integer min, Integer max, I18n i18n )
    {
        int maxLevel = organisationUnitService.getMaxOfOrganisationUnitLevels();

        Map<Integer, OrganisationUnitLevel> orgUnitLevelMap = organisationUnitService.getOrganisationUnitLevelMap();

        return programStageInstanceStore.getTabularReport( programStage, orgUnitLevelMap, organisationUnits, columns,
            level, maxLevel, startDate, endDate, descOrder, completed, min, max, i18n );
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

    @Override
    public Grid getStatisticalReport( Program program, Collection<Integer> orgunitIds, Date startDate, Date endDate,
        I18n i18n, I18nFormat format )
    {

        Grid grid = new ListGrid();
        grid.setTitle( program.getDisplayName() + " ( " + format.formatDate( startDate ) + " - "
            + format.formatDate( endDate ) + " )" );

        int total = programInstanceService.countProgramInstances( program, orgunitIds, startDate, endDate );
        grid.setSubtitle( i18n.getString( "total_persons_enrolled" ) + ": " + total );

        if ( total > 0 )
        {
            grid.addHeader( new GridHeader( i18n.getString( "id" ), true, true ) );
            grid.addHeader( new GridHeader( i18n.getString( "program_stage" ), false, true ) );
            grid.addHeader( new GridHeader( i18n.getString( "completed" ), false, false ) );
            grid.addHeader( new GridHeader( i18n.getString( "percent_completed" ), false, false ) );
            grid.addHeader( new GridHeader( i18n.getString( "incomplete" ), false, false ) );
            grid.addHeader( new GridHeader( i18n.getString( "percent_incomplete" ), false, false ) );
            grid.addHeader( new GridHeader( i18n.getString( "scheduled" ), false, false ) );
            grid.addHeader( new GridHeader( i18n.getString( "percent_Scheduled" ), false, false ) );
            grid.addHeader( new GridHeader( i18n.getString( "overdue" ), false, false ) );
            grid.addHeader( new GridHeader( i18n.getString( "percent_overdue" ), false, false ) );

            for ( ProgramStage programStage : program.getProgramStages() )
            {
                grid.addRow();
                grid.addValue( programStage.getId() );
                grid.addValue( programStage.getDisplayName() );

                int completed = programStageInstanceStore.getStatisticalProgramStageReport( programStage, orgunitIds,
                    startDate, endDate, ProgramStageInstance.COMPLETED_STATUS );
                grid.addValue( completed );
                grid.addValue( (completed + 0.0) / total );

                int incomplete = programStageInstanceStore.getStatisticalProgramStageReport( programStage, orgunitIds,
                    startDate, endDate, ProgramStageInstance.VISITED_STATUS );
                grid.addValue( incomplete );
                grid.addValue( (incomplete + 0.0) / total );

                int Scheduled = programStageInstanceStore.getStatisticalProgramStageReport( programStage, orgunitIds,
                    startDate, endDate, ProgramStageInstance.FUTURE_VISIT_STATUS );
                grid.addValue( Scheduled );
                grid.addValue( (Scheduled + 0.0) / total );

                int overdue = programStageInstanceStore.getStatisticalProgramStageReport( programStage, orgunitIds,
                    startDate, endDate, ProgramStageInstance.LATE_VISIT_STATUS );
                grid.addValue( overdue );
                grid.addValue( (overdue + 0.0) / total );
            }
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

}
