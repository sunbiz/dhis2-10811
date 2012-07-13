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

package org.hisp.dhis.patient;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.program.Program;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
public class Patient
    implements Serializable
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 884114994005945275L;

    public static final String MALE = "M";
    public static final String FEMALE = "F";

    public static final char DOB_TYPE_VERIFIED = 'V';
    public static final char DOB_TYPE_DECLARED = 'D';
    public static final char DOB_TYPE_APPROXIATED = 'A';

    public static final char AGE_TYPE_YEAR = 'Y';
    public static final char AGE_TYPE_MONTH = 'M';
    public static final char AGE_TYPE_DAY = 'D';
    
    public static String PREFIX_IDENTIFIER_TYPE = "iden";
    public static String PREFIX_FIXED_ATTRIBUTE = "fixedAttr";    
    public static String PREFIX_PATIENT_ATTRIBUTE = "attr";
    public static String PREFIX_PROGRAM = "prg";
    public static String FIXED_ATTR_BIRTH_DATE = "birthDate";
    public static String FIXED_ATTR_AGE = "age";

    private Integer id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String gender;

    private Date birthDate;

    private String phoneNumber;

    private Date deathDate;

    private Date registrationDate;

    private boolean isDead = false;

    private Set<PatientIdentifier> identifiers = new HashSet<PatientIdentifier>();

    private Set<Program> programs = new HashSet<Program>();

    private OrganisationUnit organisationUnit;

    private Set<PatientAttribute> attributes = new HashSet<PatientAttribute>();

    private Patient representative;

    private boolean underAge;

    private Character dobType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Patient()
    {
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public PatientIdentifier getPreferredPatientIdentifier()
    {
        if ( getIdentifiers() != null && getIdentifiers().size() > 0 )
        {
            for ( PatientIdentifier patientIdentifier : getIdentifiers() )
            {
                if ( patientIdentifier.getPreferred() )
                {
                    return patientIdentifier;
                }
            }
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());

        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null )
        {
            return false;
        }

        if ( getClass() != obj.getClass() )
        {
            return false;
        }

        final Patient other = (Patient) obj;

        if ( birthDate == null )
        {
            if ( other.birthDate != null )
            {
                return false;
            }
        }
        else if ( !birthDate.equals( other.birthDate ) )
        {
            return false;
        }

        if ( firstName == null )
        {
            if ( other.firstName != null )
            {
                return false;
            }
        }
        else if ( !firstName.equals( other.firstName ) )
        {
            return false;
        }

        if ( gender == null )
        {
            if ( other.gender != null )
                return false;
        }
        else if ( !gender.equals( other.gender ) )
        {
            return false;
        }

        if ( lastName == null )
        {
            if ( other.lastName != null )
            {
                return false;
            }
        }
        else if ( !lastName.equals( other.lastName ) )
        {
            return false;
        }

        if ( middleName == null )
        {
            if ( other.middleName != null )
            {
                return false;
            }
        }
        else if ( !middleName.equals( other.middleName ) )
        {
            return false;
        }

        return true;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public Integer getId()
    {
        return id;
    }

    public Set<PatientAttribute> getAttributes()
    {
        return attributes;
    }

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    public void setOrganisationUnit( OrganisationUnit organisationUnit )
    {
        this.organisationUnit = organisationUnit;
    }

    public void setAttributes( Set<PatientAttribute> attributes )
    {
        this.attributes = attributes;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName( String middleName )
    {
        this.middleName = middleName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender( String gender )
    {
        this.gender = gender;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate( Date birthDate )
    {
        this.birthDate = birthDate;
    }

    public Date getDeathDate()
    {
        return deathDate;
    }

    public void setDeathDate( Date deathDate )
    {
        this.deathDate = deathDate;
    }

    public Boolean getIsDead()
    {
        return isDead;
    }

    public void setIsDead( Boolean isDead )
    {
        this.isDead = isDead;
    }

    public Set<PatientIdentifier> getIdentifiers()
    {
        return identifiers;
    }

    public void setIdentifiers( Set<PatientIdentifier> identifiers )
    {
        this.identifiers = identifiers;
    }

    public Set<Program> getPrograms()
    {
        return programs;
    }

    public void setPrograms( Set<Program> programs )
    {
        this.programs = programs;
    }

    public void setRegistrationDate( Date registrationDate )
    {
        this.registrationDate = registrationDate;
    }

    public Date getRegistrationDate()
    {
        return registrationDate;
    }

    public void setRepresentative( Patient representative )
    {
        this.representative = representative;
    }

    public Patient getRepresentative()
    {
        return representative;
    }

    // -------------------------------------------------------------------------
    // Convenience method
    // -------------------------------------------------------------------------

    public String getAge()
    {
        if ( birthDate == null )
        {
            return "0";
        }

        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime( birthDate );

        Calendar todayCalendar = Calendar.getInstance();

        int age = todayCalendar.get( Calendar.YEAR ) - birthCalendar.get( Calendar.YEAR );

        if ( todayCalendar.get( Calendar.MONTH ) < birthCalendar.get( Calendar.MONTH ) )
        {
            age--;
        }
        else if ( todayCalendar.get( Calendar.MONTH ) == birthCalendar.get( Calendar.MONTH )
            && todayCalendar.get( Calendar.DAY_OF_MONTH ) < birthCalendar.get( Calendar.DAY_OF_MONTH ) )
        {
            age--;
        }

        if ( age < 1 )
        {
            return "( < 1 yr )";
        }
        else
        {
            return "( " + age + " yr )";
        }
    }

    public int getIntegerValueOfAge()
    {
        if ( birthDate == null )
        {
            return 0;
        }

        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime( birthDate );

        Calendar todayCalendar = Calendar.getInstance();

        int age = todayCalendar.get( Calendar.YEAR ) - birthCalendar.get( Calendar.YEAR );

        if ( todayCalendar.get( Calendar.MONTH ) < birthCalendar.get( Calendar.MONTH ) )
        {
            age--;
        }
        else if ( todayCalendar.get( Calendar.MONTH ) == birthCalendar.get( Calendar.MONTH )
            && todayCalendar.get( Calendar.DAY_OF_MONTH ) < birthCalendar.get( Calendar.DAY_OF_MONTH ) )
        {
            age--;
        }

        return age;
    }

    public void setBirthDateFromAge( int age, char ageType )
    {
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.clear( Calendar.MILLISECOND );
        todayCalendar.clear( Calendar.SECOND );
        todayCalendar.clear( Calendar.MINUTE );
        todayCalendar.set( Calendar.HOUR_OF_DAY, 0 );

        // Assumed relative to the 1st of January
        // todayCalendar.set( Calendar.DATE, 1 );
        // todayCalendar.set( Calendar.MONTH, Calendar.JANUARY );

        if ( ageType == AGE_TYPE_YEAR )
        {
            todayCalendar.add( Calendar.YEAR, -1 * age );
        }
        else if ( ageType == AGE_TYPE_MONTH )
        {
            todayCalendar.add( Calendar.MONTH, -1 * age );
        }
        else if ( ageType == AGE_TYPE_DAY )
        {
            todayCalendar.add( Calendar.DATE, -1 * age );
        }

        setBirthDate( todayCalendar.getTime() );
    }

    public char getAgeType()
    {
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.clear( Calendar.MILLISECOND );
        todayCalendar.clear( Calendar.SECOND );
        todayCalendar.clear( Calendar.MINUTE );
        todayCalendar.set( Calendar.HOUR_OF_DAY, 0 );

        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime( birthDate );

        int age = todayCalendar.get( Calendar.YEAR ) - birthCalendar.get( Calendar.YEAR );

        if ( age > 0 )
        {
            return AGE_TYPE_YEAR;
        }

        age = todayCalendar.get( Calendar.MONTH ) - birthCalendar.get( Calendar.MONTH );
        if ( age > 0 )
        {
            return AGE_TYPE_MONTH;
        }

        return AGE_TYPE_DAY;
    }

    // -------------------------------------------------------------------------
    // Getter && Setter
    // -------------------------------------------------------------------------

    public String getFullName()
    {
        boolean space = false;
        String name = "";

        if ( firstName != null && firstName.length() != 0 )
        {
            name = firstName;
            space = true;
        }

        if ( middleName != null && middleName.length() != 0 )
        {
            if ( space )
            {
                name += " ";
            }

            name += middleName;
            space = true;
        }

        if ( lastName != null && lastName.length() != 0 )
        {
            if ( space )
            {
                name += " ";
            }

            name += lastName;
        }

        return name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber )
    {
        this.phoneNumber = phoneNumber;
    }

    public boolean isUnderAge()
    {
        return underAge;
    }

    public void setUnderAge( boolean underAge )
    {
        this.underAge = underAge;
    }

    public String getTextGender()
    {
        return gender.equalsIgnoreCase( MALE ) ? "male" : "female";
    }

    public Character getDobType()
    {
        return dobType;
    }

    public void setDobType( Character dobType )
    {
        this.dobType = dobType;
    }

    public String getTextDoBType()
    {
        switch ( dobType )
        {
        case DOB_TYPE_VERIFIED:
            return "Verified";
        case DOB_TYPE_DECLARED:
            return "Declared";
        default:
            return "Approxiated";
        }
    }
}
