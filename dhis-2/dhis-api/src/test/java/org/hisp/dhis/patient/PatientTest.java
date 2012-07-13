package org.hisp.dhis.patient;

import static junit.framework.Assert.*;

import org.junit.Test;

public class PatientTest
{

    
    @Test
    public void testFullname() {

        Patient patient = new Patient();
        
        assertEquals( "", patient.getFullName() );
        
        patient.setFirstName( "firstName" );
        assertEquals( "firstName", patient.getFullName() );
        
        patient.setLastName( "lastName" );
        assertEquals( "firstName lastName", patient.getFullName() );

        patient.setMiddleName( "middleName" );
        assertEquals( "firstName middleName lastName", patient.getFullName() );

        patient.setFirstName( "" );
        assertEquals( "middleName lastName", patient.getFullName() );

        patient.setFirstName( "firstName middleName lastName" );
        patient.setMiddleName( null );
        patient.setLastName( null );
        assertEquals( "firstName middleName lastName", patient.getFullName() );
    }
}
