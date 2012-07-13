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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.validation.ValidationCriteria;

/**
 * @author Abyot Asalefew
 */
public class Program
    extends BaseIdentifiableObject
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -2581751965520009382L;

    public static final int MULTIPLE_EVENTS_WITH_REGISTRATION = 1;

    public static final int SINGLE_EVENT_WITH_REGISTRATION = 2;

    public static final int SINGLE_EVENT_WITHOUT_REGISTRATION = 3;
    

    private String description;

    private Integer version;

    /**
     * Description of Date of Enrollment This description is differ from each
     * program
     */
    private String dateOfEnrollmentDescription;

    /**
     * Description of Date of Incident This description is differ from each
     * program
     */
    private String dateOfIncidentDescription;

    private Set<OrganisationUnit> organisationUnits = new HashSet<OrganisationUnit>();

    private Set<ProgramInstance> programInstances = new HashSet<ProgramInstance>();

    private Set<ProgramStage> programStages = new HashSet<ProgramStage>();

    private Set<ValidationCriteria> patientValidationCriteria = new HashSet<ValidationCriteria>();

    private Integer maxDaysAllowedInputData;

    private Integer type;

    private Boolean displayProvidedOtherFacility;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Program()
    {
    }

    public Program( String name, String description )
    {
        this.name = name;
        this.description = description;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof Program) )
        {
            return false;
        }

        final Program other = (Program) o;

        return name.equals( other.getName() );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public Boolean getDisplayProvidedOtherFacility()
    {
        return displayProvidedOtherFacility;
    }

    public void setDisplayProvidedOtherFacility( Boolean displayProvidedOtherFacility )
    {
        this.displayProvidedOtherFacility = displayProvidedOtherFacility;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public void setOrganisationUnits( Set<OrganisationUnit> organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( Integer version )
    {
        this.version = version;
    }

    public Set<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setProgramInstances( Set<ProgramInstance> programInstances )
    {
        this.programInstances = programInstances;
    }

    public Set<ProgramInstance> getProgramInstances()
    {
        return programInstances;
    }

    public void setProgramStages( Set<ProgramStage> programStages )
    {
        this.programStages = programStages;
    }

    public Set<ProgramStage> getProgramStages()
    {
        return programStages;
    }

    public String getDateOfEnrollmentDescription()
    {
        return dateOfEnrollmentDescription;
    }

    public void setDateOfEnrollmentDescription( String dateOfEnrollmentDescription )
    {
        this.dateOfEnrollmentDescription = dateOfEnrollmentDescription;
    }

    public Integer getMaxDaysAllowedInputData()
    {
        return maxDaysAllowedInputData;
    }

    public void setMaxDaysAllowedInputData( Integer maxDaysAllowedInputData )
    {
        this.maxDaysAllowedInputData = maxDaysAllowedInputData;
    }

    public String getDateOfIncidentDescription()
    {
        return dateOfIncidentDescription;
    }

    public void setDateOfIncidentDescription( String dateOfIncidentDescription )
    {
        this.dateOfIncidentDescription = dateOfIncidentDescription;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType( Integer type )
    {
        this.type = type;
    }

    public Set<ValidationCriteria> getPatientValidationCriteria()
    {
        return patientValidationCriteria;
    }

    public void setPatientValidationCriteria( Set<ValidationCriteria> patientValidationCriteria )
    {
        this.patientValidationCriteria = patientValidationCriteria;
    }

    // -------------------------------------------------------------------------
    // Logic methods
    // -------------------------------------------------------------------------

    public ProgramStage getProgramStageByStage( int stage )
    {
        int count = 1;

        for ( ProgramStage programStage : programStages )
        {
            if ( count == stage )
            {
                return programStage;
            }
            else
            {
                count++;
            }

        }

        return null;
    }

    @SuppressWarnings( "unchecked" )
    public ValidationCriteria isValid( Patient patient )
    {
        try
        {
            for ( ValidationCriteria criteria : patientValidationCriteria )
            {
                Object propertyValue = getValueFromPatient( StringUtils.capitalize( criteria.getProperty() ), patient );

                // Compare property value with compare value

                int i = ((Comparable<Object>) propertyValue).compareTo( (Comparable<Object>) criteria.getValue() );

                // Return validation criteria if criteria is not met

                if ( i != criteria.getOperator() )
                {
                    return criteria;
                }
            }

            // Return null if all criteria are met

            return null;
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( ex );
        }
    }

    private Object getValueFromPatient( String property, Patient patient )
        throws Exception
    {
        return Patient.class.getMethod( "get" + property ).invoke( patient );
    }
    
    public boolean isSingleEvent()
    {
        return type != null && ( SINGLE_EVENT_WITH_REGISTRATION == type || SINGLE_EVENT_WITHOUT_REGISTRATION == type ); 
    }
    
    public boolean isRegistration()
    {
        return type != null && ( SINGLE_EVENT_WITH_REGISTRATION == type || MULTIPLE_EVENTS_WITH_REGISTRATION == type ); 
    }
}
