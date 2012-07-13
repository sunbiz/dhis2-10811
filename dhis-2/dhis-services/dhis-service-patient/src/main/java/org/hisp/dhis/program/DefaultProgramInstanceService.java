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
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientIdentifier;
import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.patientattributevalue.PatientAttributeValue;
import org.hisp.dhis.patientattributevalue.PatientAttributeValueService;
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

    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
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

    public Collection<ProgramInstance> getProgramInstances( boolean completed )
    {
        return programInstanceStore.get( completed );
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

    public Collection<ProgramInstance> getProgramInstances( Collection<Program> programs, boolean completed )
    {
        return programInstanceStore.get( programs, completed );
    }

    public Collection<ProgramInstance> getProgramInstances( Program program, boolean completed )
    {
        return programInstanceStore.get( program, completed );
    }

    public Collection<ProgramInstance> getProgramInstances( Patient patient )
    {
        return programInstanceStore.get( patient );
    }

    public Collection<ProgramInstance> getProgramInstances( Patient patient, boolean completed )
    {
        return programInstanceStore.get( patient, completed );
    }

    public Collection<ProgramInstance> getProgramInstances( Patient patient, Program program )
    {
        return programInstanceStore.get( patient, program );
    }

    public Collection<ProgramInstance> getProgramInstances( Patient patient, Program program, boolean completed )
    {
        return programInstanceStore.get( patient, program, completed );
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

    public Collection<ProgramInstance> getProgramInstances( Program program, OrganisationUnit organisationUnit,
        Date startDate, Date endDate, int min, int max )
    {
        return programInstanceStore.get( program, organisationUnit, startDate, endDate, min, max );
    }

    public int countProgramInstances( Program program, OrganisationUnit organisationUnit )
    {
        return programInstanceStore.count( program, organisationUnit );
    }

    public int countProgramInstances( Program program, OrganisationUnit organisationUnit, Date startDate, Date endDate )
    {
        return programInstanceStore.count( program, organisationUnit, startDate, endDate );
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
        attrGrid.addValue( ( patient.getPhoneNumber() == null || patient.getPhoneNumber().isEmpty() ) ? PatientAttributeValue.UNKNOWN : patient.getPhoneNumber() );

        // ---------------------------------------------------------------------
        // Add dynamic attribues
        // ---------------------------------------------------------------------

        for ( PatientAttribute patientAttribute : patientAttributes )
        {
            attrGrid.addRow();
            attrGrid.addValue( patientAttribute.getName() );
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
                Grid gridProgram = new ListGrid();
                gridProgram.setTitle( programInstance.getProgram().getName() );
                gridProgram.setSubtitle( "" );

                // ---------------------------------------------------------------------
                // Headers
                // ---------------------------------------------------------------------

                gridProgram.addHeader( new GridHeader( i18n.getString( "name" ), false, false ) );
                gridProgram.addHeader( new GridHeader( i18n.getString( "value" ), false, false ) );
                gridProgram.addHeader( new GridHeader( "", true, false ) );

                // ---------------------------------------------------------------------
                // Values
                // ---------------------------------------------------------------------

                gridProgram.addRow();
                gridProgram.addValue( i18n.getString( "date_of_enrollment" ) );
                gridProgram.addValue( format.formatDate( programInstance.getEnrollmentDate() ) );

                gridProgram.addRow();
                gridProgram.addValue( i18n.getString( "date_of_incident" ) );
                gridProgram.addValue( format.formatDate( programInstance.getDateOfIncident() ) );

                grids.add( gridProgram );

                // ---------------------------------------------------------------------
                // Grids for program-stage-instance
                // ---------------------------------------------------------------------

                List<Grid> programInstanceGrids = programStageInstanceService.getProgramStageInstancesReport(
                    programInstance, format, i18n );
                grids.addAll( programInstanceGrids );
            }
        }

        return grids;
    }
}
