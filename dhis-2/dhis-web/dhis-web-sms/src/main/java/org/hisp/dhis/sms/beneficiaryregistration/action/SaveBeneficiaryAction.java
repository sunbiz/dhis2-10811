/*
 * Copyright (c) 2004-2011, University of Oslo
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

package org.hisp.dhis.sms.beneficiaryregistration.action;

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.opensymphony.xwork2.Action;

public class SaveBeneficiaryAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PatientService patientService;

    public PatientService getPatientService()
    {
        return patientService;
    }

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    private OrganisationUnitService organisationUnitService;

    public OrganisationUnitService getOrganisationUnitService()
    {
        return organisationUnitService;
    }

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private String orgUnitId;

    public String getOrgUnitId()
    {
        return orgUnitId;
    }

    public void setOrgUnitId( String orgUnitId )
    {
        this.orgUnitId = orgUnitId;
    }

    private String patientFullName;

    public String getPatientFullName()
    {
        return patientFullName;
    }

    public void setPatientFullName( String patientFullName )
    {
        this.patientFullName = patientFullName;
    }

    private String bloodGroup;

    public String getBloodGroup()
    {
        return bloodGroup;
    }

    public void setBloodGroup( String bloodGroup )
    {
        this.bloodGroup = bloodGroup;
    }

    private String gender;

    public String getGender()
    {
        return gender;
    }

    public void setGender( String gender )
    {
        this.gender = gender;
    }

    private String dateOfBirth;

    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth( String dateOfBirth )
    {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean validated;

    public boolean isValidated()
    {
        return validated;
    }

    public void setValidated( boolean validated )
    {
        this.validated = validated;
    }

    public Map<String, String> validationMap = new HashMap<String, String>();

    public Map<String, String> getValidationMap()
    {
        return validationMap;
    }

    public void setValidationMap( Map<String, String> validationMap )
    {
        this.validationMap = validationMap;
    }

    public Map<String, String> previousValues = new HashMap<String, String>();

    public Map<String, String> getPreviousValues()
    {
        return previousValues;
    }

    public void setPreviousValues( Map<String, String> previousValues )
    {
        this.previousValues = previousValues;
    }

    @Override
    public String execute()
        throws Exception
    {
        Patient patient = new Patient();

        patient.setOrganisationUnit( organisationUnitService.getOrganisationUnit( Integer.parseInt( orgUnitId ) ) );

        if ( this.patientFullName.trim().equals( "" ) )
        {
            validationMap.put( "fullName", "is_empty" );
        }
        else
        {
            String[] tokens = this.patientFullName.split( " " );

            patient.setFirstName( tokens[0] );

            if ( tokens.length == 2 )
            {
                patient.setLastName( tokens[1] );
            }
            else if ( tokens.length > 2 )
            {
                patient.setMiddleName( tokens[1] );
                patient.setLastName( tokens[2] );
            }
        }

        patient.setGender( gender );
       // TODO: is this replaced by something else???? 
       // patient.setBloodGroup( bloodGroup );
       

        try
        {
            DateTimeFormatter sdf = ISODateTimeFormat.yearMonthDay();
            DateTime date = sdf.parseDateTime( dateOfBirth );
            patient.setBirthDate( date.toDate() );
        }
        catch ( Exception e )
        {
            validationMap.put( "dob", "is_invalid_date" );
        }

        if ( this.validationMap.size() > 0 )
        {
            this.validated = false;
            this.previousValues.put( "fullName", this.patientFullName );
            this.previousValues.put( "gender", this.gender );
            this.previousValues.put( "bloodGroup", this.bloodGroup );
            this.previousValues.put( "dob", this.dateOfBirth );
            return SUCCESS;
        }

        patientService.savePatient( patient );
        validated = true;

        return SUCCESS;
    }

}
