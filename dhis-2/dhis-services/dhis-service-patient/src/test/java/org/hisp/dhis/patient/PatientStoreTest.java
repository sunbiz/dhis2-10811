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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.junit.Test;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class PatientStoreTest
    extends DhisSpringTest
{
    private PatientStore patientStore;
    private OrganisationUnitService organisationUnitService;
    
    private Patient patientA;
    private Patient patientB;
    
    private OrganisationUnit organisationUnit;

    @Override
    public void setUpTest()
    {
        patientStore = (PatientStore) getBean( PatientStore.ID );
        organisationUnitService = (OrganisationUnitService) getBean ( OrganisationUnitService.ID );
        
        organisationUnit = createOrganisationUnit( 'A' );
        organisationUnitService.addOrganisationUnit( organisationUnit );
        
        patientA = createPatient( 'A', organisationUnit );
        patientB = createPatient( 'B', organisationUnit );
        
    }
    
    @Test
    public void addGet()
    {
        int idA = patientStore.save( patientA );
        int idB = patientStore.save( patientB );
        
        assertEquals( patientA.getFirstName(), patientStore.get( idA ).getFirstName() );
        assertEquals( patientB.getFirstName(), patientStore.get( idB ).getFirstName() );        
    }

    @Test
    public void addGetbyOu()
    {
        int idA = patientStore.save( patientA );
        int idB = patientStore.save( patientB );
      
        assertEquals( patientA.getFirstName(), patientStore.get( idA ).getFirstName() );
        assertEquals( patientB.getFirstName(), patientStore.get( idB ).getFirstName() );
    }
    
    @Test
    public void delete()
    {
        int idA = patientStore.save( patientA );
        int idB = patientStore.save( patientB );
        
        assertNotNull( patientStore.get( idA ) );
        assertNotNull( patientStore.get( idB ) );

        patientStore.delete( patientA );
        
        assertNull( patientStore.get( idA ) );
        assertNotNull( patientStore.get( idB ) );

        patientStore.delete( patientB );
        
        assertNull( patientStore.get( idA ) );
        assertNull( patientStore.get( idB ) );        
    }
    
    @Test
    public void getAll()
    {
        patientStore.save( patientA );
        patientStore.save( patientB );
        
        assertTrue( equals( patientStore.getAll(), patientA, patientB ) );
    }
}
