package org.hisp.dhis.dashboard;

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

import org.hibernate.NonUniqueObjectException;
import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.common.GenericStore;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.report.ReportService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.junit.Test;

/**
 * @author Lars Helge Overland
 */
public class DashboardStoreTest
    extends DhisSpringTest
{
    private UserService userService;
    
    private ReportService reportService;
    
    private GenericStore<DashboardContent> dashboardContentStore;
    
    private User userA;
    
    private Report reportA;
    
    private DashboardContent contentA;
    private DashboardContent contentB;
    
    @Override
    @SuppressWarnings("unchecked")
    public void setUpTest()
    {
        userService = (UserService) getBean( UserService.ID );
        
        reportService = (ReportService) getBean( ReportService.ID );

        dashboardContentStore = (GenericStore<DashboardContent>) getBean( "org.hisp.dhis.dashboard.DashboardContentStore" );
        
        userA = createUser( 'A' );
        userService.addUser( userA );
        
        reportA = new Report( "ReportA", "DesignA", null );
        reportService.saveReport( reportA );
        
        contentA = new DashboardContent();
        contentB = new DashboardContent();
    }
    
    @Test
    public void saveGet()
    {
        contentA.setUser( userA );
        contentA.getReports().add( reportA );
        
        dashboardContentStore.save( contentA );
        
        assertEquals( contentA, dashboardContentStore.get( userA.getId() ) );
        assertEquals( userA, dashboardContentStore.get( userA.getId() ).getUser() );
        assertEquals( reportA, dashboardContentStore.get( userA.getId() ).getReports().iterator().next() );
    }
    
    @Test(expected=NonUniqueObjectException.class)
    public void duplicate()
    {
        contentA.setUser( userA );
        contentB.setUser( userA );
        
        dashboardContentStore.save( contentA );
        dashboardContentStore.save( contentB );        
    }
        
    @Test
    public void delete()
    {
        contentA.setUser( userA );
        contentA.getReports().add( reportA );
        
        dashboardContentStore.save( contentA );
        
        assertNotNull( dashboardContentStore.get( userA.getId() ) );
        
        dashboardContentStore.delete( contentA );
        
        assertNull( dashboardContentStore.get( userA.getId() ) );
    }
}
