package org.hisp.dhis.interpretation;

/*
 * Copyright (c) 2004-2012, University of Oslo
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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.chart.Chart;
import org.hisp.dhis.chart.ChartService;
import org.hisp.dhis.mock.MockCurrentUserService;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lars Helge Overland
 */
public class InterpretationServiceTest
    extends DhisSpringTest
{
    @Autowired
    private UserService userService;
    
    @Autowired
    private ChartService chartService;
    
    @Autowired
    private InterpretationService interpretationService;
    
    private User userA;
    
    private Chart chartA;
    
    private Interpretation interpretationA;
    private Interpretation interpretationB;
    private Interpretation interpretationC;
    
    @Before    
    public void beforeTest()
    {
        userA = createUser( 'A' );
        userService.addUser( userA );
        
        setDependency( interpretationService, "currentUserService", new MockCurrentUserService( userA ), CurrentUserService.class );
        
        chartA = new Chart( "ChartA" );
        chartService.saveChart( chartA );
        
        interpretationA = new Interpretation( chartA, "Interpration of chart A" );
        interpretationB = new Interpretation( chartA, "Interpration of chart B" );
        interpretationC = new Interpretation( chartA, "Interpration of chart C" );
    }
    
    @Test
    public void testSaveGet()
    {
        int idA = interpretationService.saveInterpretation( interpretationA );
        int idB = interpretationService.saveInterpretation( interpretationB );
        int idC = interpretationService.saveInterpretation( interpretationC );
        
        assertEquals( interpretationA, interpretationService.getInterpretation( idA ) );
        assertEquals( interpretationB, interpretationService.getInterpretation( idB ) );
        assertEquals( interpretationC, interpretationService.getInterpretation( idC ) );
    }
    
    @Test
    public void testDelete()
    {
        int idA = interpretationService.saveInterpretation( interpretationA );
        int idB = interpretationService.saveInterpretation( interpretationB );
        int idC = interpretationService.saveInterpretation( interpretationC );
        
        assertNotNull( interpretationService.getInterpretation( idA ) );
        assertNotNull( interpretationService.getInterpretation( idB ) );
        assertNotNull( interpretationService.getInterpretation( idC ) );
        
        interpretationService.deleteInterpretation( interpretationB );

        assertNotNull( interpretationService.getInterpretation( idA ) );
        assertNull( interpretationService.getInterpretation( idB ) );
        assertNotNull( interpretationService.getInterpretation( idC ) );

        interpretationService.deleteInterpretation( interpretationA );

        assertNull( interpretationService.getInterpretation( idA ) );
        assertNull( interpretationService.getInterpretation( idB ) );
        assertNotNull( interpretationService.getInterpretation( idC ) );

        interpretationService.deleteInterpretation( interpretationC );

        assertNull( interpretationService.getInterpretation( idA ) );
        assertNull( interpretationService.getInterpretation( idB ) );
        assertNull( interpretationService.getInterpretation( idC ) );
    }
    
    @Test
    public void testGetLast()
    {
        interpretationService.saveInterpretation( interpretationA );
        interpretationService.saveInterpretation( interpretationB );
        interpretationService.saveInterpretation( interpretationC );
        
        List<Interpretation> interpretations = interpretationService.getInterpretations( 0, 50 );
        
        assertEquals( 3, interpretations.size() );
        assertTrue( interpretations.contains( interpretationA ) );
        assertTrue( interpretations.contains( interpretationB ) );
        assertTrue( interpretations.contains( interpretationC ) );
    }
    
    @Test
    public void testAddComment()
    {
        interpretationService.saveInterpretation( interpretationA );
        String uid = interpretationA.getUid();        
        assertNotNull( uid );
        
        interpretationService.addInterpretationComment( uid, "This interpretation is good" );
        interpretationService.addInterpretationComment( uid, "This interpretation is bad" );
        
        interpretationA = interpretationService.getInterpretation( uid );
        assertNotNull( interpretationA.getComments() );
        assertEquals( 2, interpretationA.getComments().size() );
    }
    
    @Test
    public void testGetNewCount()
    {
        interpretationService.saveInterpretation( interpretationA );
        interpretationService.saveInterpretation( interpretationB );
        interpretationService.saveInterpretation( interpretationC );
        
        long count = interpretationService.getNewInterpretationCount();
        
        assertEquals( 3, count );
    }
}
