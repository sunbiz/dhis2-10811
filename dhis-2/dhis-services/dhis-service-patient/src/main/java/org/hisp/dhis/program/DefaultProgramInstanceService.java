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
import java.util.List;

import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.GridHeader;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientIdentifier;
import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.patient.PatientReminder;
import org.hisp.dhis.patientattributevalue.PatientAttributeValue;
import org.hisp.dhis.patientattributevalue.PatientAttributeValueService;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.system.grid.ListGrid;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
@Transactional
public class DefaultProgramInstanceService
    implements ProgramInstanceService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramInstanceStore programInstanceStore;

    public void setProgramInstanceStore( ProgramInstanceStore programInstanceStore )
    {
        this.programInstanceStore = programInstanceStore;
    }

    private PatientAttributeValueService patientAttributeValueService;

    public void setPatientAttributeValueService( PatientAttributeValueService patientAttributeValueService )
    {
        this.patientAttributeValueService = patientAttributeValueService;
    }

    private PatientDataValueService patientDataValueService;

    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    public int addProgramInstance( ProgramInstance programInstance )
    {
        return programInstanceStore.save( programInstance );
    }

    public void deleteProgramInstance( ProgramInstance programInstance )
    {
        programInstanceStore.delete( programInstance );
    }

    public Collection<ProgramInstance> getAllProgramInstances()
    {
        return programInstanceStore.getAll();
    }

    public ProgramInstance getProgramInstance( int id )
    {
        return programInstanceStore.get( id );
    }

    public Collection<ProgramInstance> getProgramInstances( Integer status )
    {
        return programInstanceStore.get( status );
    }

    public void updateProgramInstance( ProgramInstance programInstance )
    {
        programInstanceStore.update( programInstance );
    }

    public Collection<ProgramInstance> getProgramInstances( Program program )
    {
        return programInstanceStore.get( program );
    }

    public Collection<ProgramInstance> getProgramInstances( Collection<Program> programs )
    {
        return programInstanceStore.get( programs );
    }

    public Collection<ProgramInstance> getProgramInstances( Collection<Program> programs, Integer status )
    {
        return programInstanceStore.get( programs, status );
    }

    public Collection<ProgramInstance> getProgramInstances( Program program, Integer status )
    {
        return programInstanceStore.get( program, status );
    }

    public Collection<ProgramInstance> getProgramInstances( Patient patient )
    {
        return programInstanceStore.get( patient );
    }

    public Collection<ProgramInstance> getProgramInstances( Patient patient, Integer status )
    {
        return programInstanceStore.get( patient, status );
    }

    public Collection<ProgramInstance> getProgramInstances( Patient patient, Program program )
    {
        return programInstanceStore.get( patient, program );
    }

    public Collection<ProgramInstance> getProgramInstances( Patient patient, Program program, Integer status )
    {
        return programInstanceStore.get( patient, program, status );
    }

    public Collection<ProgramInstance> getProgramInstances( Program program, OrganisationUnit organisationUnit )
    {
        return programInstanceStore.get( program, organisationUnit );
    }

    public Collection<ProgramInstance> getProgramInstances( Program program, OrganisationUnit organisationUnit,
        int min, int max )
    {
        return programInstanceStore.get( program, organisationUnit, min, max );
    }

    public Collection<ProgramInstance> getProgramInstances( Program program, OrganisationUnit organisationUnit,
        Date startDate, Date endDate )
    {
        return programInstanceStore.get( program, organisationUnit, startDate, endDate );
    }

    public Collection<ProgramInstance> getProgramInstances( Program program, Collection<Integer> orgunitIds,
        Date startDate, Date endDate, int min, int max )
    {
        return programInstanceStore.get( program, orgunitIds, startDate, endDate, min, max );
    }

    public int countProgramInstances( Program program, OrganisationUnit organisationUnit )
    {
        return programInstanceStore.count( program, organisationUnit );
    }

    public int countProgramInstances( Program program, Collection<Integer> orgunitIds, Date startDate, Date endDate )
    {
        return programInstanceStore.count( program, orgunitIds, startDate, endDate );
    }

    public List<Grid> getProgramInstanceReport( Patient patient, I18n i18n, I18nFormat format )
    {
        List<Grid> grids = new ArrayList<Grid>();

        // ---------------------------------------------------------------------
        // Get registered personal patient data
        // ---------------------------------------------------------------------

        Grid attrGrid = new ListGrid();

        attrGrid.setTitle( patient.getFullName() );
        attrGrid.setSubtitle( "" );

        attrGrid.addHeader( new GridHeader( i18n.getString( "name" ), false, true ) );
        attrGrid.addHeader( new GridHeader( i18n.getString( "value" ), false, true ) );
        attrGrid.addHeader( new GridHeader( "", true, false ) );

        Collection<PatientAttribute> patientAttributes = patient.getAttributes();

        // ---------------------------------------------------------------------
        // Add fixed attribues
        // ---------------------------------------------------------------------

        attrGrid.addRow();
        attrGrid.addValue( i18n.getString( "gender" ) );
        attrGrid.addValue( i18n.getString( patient.getGender() ) );

        attrGrid.addRow();
        attrGrid.addValue( i18n.getString( "date_of_birth" ) );
        attrGrid.addValue( format.formatDate( patient.getBirthDate() ) );

        attrGrid.addRow();
        attrGrid.addValue( i18n.getString( "age" ) );
        attrGrid.addValue( patient.getAge() );

        attrGrid.addRow();
        attrGrid.addValue( i18n.getString( "dob_type" ) );
        attrGrid.addValue( i18n.getString( patient.getDobType() + "" ) );

        attrGrid.addRow();
        attrGrid.addValue( i18n.getString( "phoneNumber" ) );
        attrGrid
            .addValue( (patient.getPhoneNumber() == null || patient.getPhoneNumber().isEmpty()) ? PatientAttributeValue.UNKNOWN
                : patient.getPhoneNumber() );

        // ---------------------------------------------------------------------
        // Add dynamic attribues
        // ---------------------------------------------------------------------

        for ( PatientAttribute patientAttribute : patientAttributes )
        {
            attrGrid.addRow();
            attrGrid.addValue( patientAttribute.getDisplayName() );
            PatientAttributeValue attributeValue = patientAttributeValueService.getPatientAttributeValue( patient,
                patientAttribute );
            String value = "";
            if ( attributeValue == null )
            {
                value = PatientAttributeValue.UNKNOWN;
            }
            else if ( attributeValue.getPatientAttribute().getValueType().equals( PatientAttribute.TYPE_BOOL ) )
            {
                value = i18n.getString( attributeValue.getValue() );
            }
            else
            {
                value = attributeValue.getValue();
            }

            attrGrid.addValue( value );
        }

        // ---------------------------------------------------------------------
        // Add identifier
        // ---------------------------------------------------------------------

        for ( PatientIdentifier identifier : patient.getIdentifiers() )
        {
            attrGrid.addRow();

            PatientIdentifierType idType = identifier.getIdentifierType();
            if ( idType != null )
            {
                attrGrid.addValue( idType.getName() );
                attrGrid.addValue( identifier.getIdentifier() );
            }
            else
            {
                attrGrid.addValue( i18n.getString( "system_identifier" ) );
                attrGrid.addValue( identifier.getIdentifier() );
            }
        }

        grids.add( attrGrid );

        // ---------------------------------------------------------------------
        // Get all program data registered
        // ---------------------------------------------------------------------

        Collection<ProgramInstance> programInstances = getProgramInstances( patient );

        if ( programInstances.size() > 0 )
        {
            for ( ProgramInstance programInstance : programInstances )
            {
                Grid gridProgram = getProgramInstanceReport( programInstance, i18n, format );

                // ---------------------------------------------------------------------
                // Grids for program-stage-instance
                // ---------------------------------------------------------------------

                getProgramStageInstancesReport( gridProgram, programInstance, format, i18n );

                grids.add( gridProgram );
            }
        }

        return grids;
    }

    public Grid getProgramInstanceReport( ProgramInstance programInstance, I18n i18n, I18nFormat format )
    {
        Grid grid = new ListGrid();

        // ---------------------------------------------------------------------
        // Get all program data registered
        // ---------------------------------------------------------------------

        grid.setTitle( programInstance.getProgram().getName() );
        grid.setSubtitle( "" );

        // ---------------------------------------------------------------------
        // Headers
        // ---------------------------------------------------------------------

        grid.addHeader( new GridHeader( i18n.getString( "name" ), false, false ) );
        grid.addHeader( new GridHeader( i18n.getString( "value" ), false, false ) );
        grid.addHeader( new GridHeader( "", true, false ) );

        // ---------------------------------------------------------------------
        // Grids for program-stage-instance
        // ---------------------------------------------------------------------

        grid.addRow();
        grid.addValue( programInstance.getProgram().getDateOfEnrollmentDescription() );
        grid.addValue( format.formatDate( programInstance.getEnrollmentDate() ) );

        if ( programInstance.getProgram().getDisplayIncidentDate() != null
            && programInstance.getProgram().getDisplayIncidentDate() )
        {
            grid.addRow();
            grid.addValue( programInstance.getProgram().getDateOfIncidentDescription() );
            grid.addValue( format.formatDate( programInstance.getDateOfIncident() ) );
        }

        getProgramStageInstancesReport( grid, programInstance, format, i18n );

        return grid;
    }

    public int countProgramInstancesByStatus( Integer status, Program program, Collection<Integer> orgunitIds,
        Date startDate, Date endDate )
    {
        return programInstanceStore.countByStatus( status, program, orgunitIds, startDate, endDate );
    }

    public Collection<ProgramInstance> getProgramInstancesByStatus( Integer status, Program program,
        Collection<Integer> orgunitIds, Date startDate, Date endDate )
    {
        return programInstanceStore.getByStatus( status, program, orgunitIds, startDate, endDate );
    }

    public void removeProgramEnrollment( ProgramInstance programInstance )
    {
        programInstanceStore.removeProgramEnrollment( programInstance );
    }

    public Collection<SchedulingProgramObject> getSendMesssageEvents()
    {
        Collection<SchedulingProgramObject> result = programInstanceStore
            .getSendMesssageEvents( PatientReminder.ENROLLEMENT_DATE_TO_COMPARE );

        result.addAll( programInstanceStore.getSendMesssageEvents( PatientReminder.INCIDENT_DATE_TO_COMPARE ) );

        return result;
    }

    // -------------------------------------------------------------------------
    // due-date && report-date
    // -------------------------------------------------------------------------

    private void getProgramStageInstancesReport( Grid grid, ProgramInstance programInstance, I18nFormat format,
        I18n i18n )
    {
        Collection<ProgramStageInstance> programStageInstances = programInstance.getProgramStageInstances();

        for ( ProgramStageInstance programStageInstance : programStageInstances )
        {
            grid.addRow();
            grid.addValue( "" );
            grid.addValue( "" );

            grid.addRow();
            grid.addValue( ">> " + programStageInstance.getProgramStage().getName() );
            grid.addValue( "" );

            // -----------------------------------------------------------------
            // due-date && report-date
            // -----------------------------------------------------------------

            grid.addRow();
            grid.addValue( i18n.getString( "due_date" ) );
            grid.addValue( format.formatDate( programStageInstance.getDueDate() ) );

            if ( programStageInstance.getExecutionDate() != null )
            {
                grid.addRow();
                grid.addValue( programStageInstance.getProgramStage().getReportDateDescription() );
                grid.addValue( format.formatDate( programStageInstance.getExecutionDate() ) );
            }

            // -----------------------------------------------------------------
            // Values
            // -----------------------------------------------------------------

            Collection<PatientDataValue> patientDataValues = patientDataValueService
                .getPatientDataValues( programStageInstance );

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
    }

}
