package org.hisp.dhis.analytics.data;

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

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.analytics.AnalyticsService;
import org.hisp.dhis.analytics.DataQueryParams;
import org.hisp.dhis.analytics.IllegalQueryException;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AnalyticsServiceTest
    extends DhisSpringTest
{
    private DataElement deA;
    private DataElement deB;
    private DataElement deC;
    private DataElement deD;
    
    private OrganisationUnit ouA;
    private OrganisationUnit ouB;
    private OrganisationUnit ouC;
    private OrganisationUnit ouD;
    private OrganisationUnit ouE;
    
    private OrganisationUnitGroup ouGroupA;
    private OrganisationUnitGroup ouGroupB;
    private OrganisationUnitGroup ouGroupC;
    
    private OrganisationUnitGroupSet ouGroupSetA;
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @Autowired
    private DataElementService dataElementService;
    
    @Autowired
    private OrganisationUnitService organisationUnitService;
    
    @Autowired
    private OrganisationUnitGroupService organisationUnitGroupService;
    
    @Override
    public void setUpTest()
    {
        deA = createDataElement( 'A' );
        deB = createDataElement( 'B' );
        deC = createDataElement( 'C' );
        deD = createDataElement( 'D' );
        
        dataElementService.addDataElement( deA );
        dataElementService.addDataElement( deB );
        dataElementService.addDataElement( deC );
        dataElementService.addDataElement( deD );
        
        ouA = createOrganisationUnit( 'A' );
        ouB = createOrganisationUnit( 'B' );
        ouC = createOrganisationUnit( 'C' );
        ouD = createOrganisationUnit( 'D' );
        ouE = createOrganisationUnit( 'E' );
        
        organisationUnitService.addOrganisationUnit( ouA );
        organisationUnitService.addOrganisationUnit( ouB );
        organisationUnitService.addOrganisationUnit( ouC );
        organisationUnitService.addOrganisationUnit( ouD );
        organisationUnitService.addOrganisationUnit( ouE );
        
        ouGroupA = createOrganisationUnitGroup( 'A' );
        ouGroupB = createOrganisationUnitGroup( 'B' );
        ouGroupC = createOrganisationUnitGroup( 'C' );
        
        ouGroupSetA = createOrganisationUnitGroupSet( 'A' );
        
        organisationUnitGroupService.addOrganisationUnitGroup( ouGroupA );
        organisationUnitGroupService.addOrganisationUnitGroup( ouGroupB );
        organisationUnitGroupService.addOrganisationUnitGroup( ouGroupC );
        
        ouGroupSetA.getOrganisationUnitGroups().add( ouGroupA );
        ouGroupSetA.getOrganisationUnitGroups().add( ouGroupB );
        ouGroupSetA.getOrganisationUnitGroups().add( ouGroupC );
        
        organisationUnitGroupService.addOrganisationUnitGroupSet( ouGroupSetA );
    }
    
    @Test
    public void testGetFromUrlA()
    {
        Set<String> dimensionParams = new HashSet<String>();
        dimensionParams.add( "dx:" + BASE_UID + "A;" + BASE_UID + "B;" + BASE_UID + "C;" + BASE_UID + "D" );
        dimensionParams.add( "pe:2012;2012S1;2012S2" );
        dimensionParams.add( ouGroupSetA.getUid() + ":" + ouGroupA.getUid() + ";" + ouGroupB.getUid() + ";" + ouGroupC.getUid() );
        
        Set<String> filterParams = new HashSet<String>();
        filterParams.add( "ou:" + BASE_UID + "A;" + BASE_UID + "B;" + BASE_UID + "C;" + BASE_UID + "D;" + BASE_UID + "E" );
        
        DataQueryParams params = analyticsService.getFromUrl( dimensionParams, filterParams, null, null, null );
        
        assertEquals( 4, params.getDataElements().size() );
        assertEquals( 3, params.getPeriods().size() );
        assertEquals( 5, params.getFilterOrganisationUnits().size() );
        assertEquals( 3, params.getDimensionOptions( ouGroupSetA.getUid() ).size() );
    }

    @Test
    public void testGetFromUrlB()
    {
        Set<String> dimensionParams = new HashSet<String>();
        dimensionParams.add( "dx:" + BASE_UID + "A;" + BASE_UID + "B;" + BASE_UID + "C;" + BASE_UID + "D" );

        Set<String> filterParams = new HashSet<String>();
        filterParams.add( "ou:" + BASE_UID + "A" );
        
        DataQueryParams params = analyticsService.getFromUrl( dimensionParams, filterParams, null, null, null );
        
        assertEquals( 4, params.getDataElements().size() );
        assertEquals( 1, params.getFilterOrganisationUnits().size() );
    }

    @Test( expected = IllegalQueryException.class )
    public void testGetFromUrlNoDx()
    {
        Set<String> dimensionParams = new HashSet<String>();
        dimensionParams.add( "dx" );
        dimensionParams.add( "pe:2012,2012S1,2012S2" );
        
        analyticsService.getFromUrl( dimensionParams, null, null, null, null );        
    }
    
    @Test( expected = IllegalQueryException.class )
    public void testGetFromUrlNoPeriods()
    {
        Set<String> dimensionParams = new HashSet<String>();
        dimensionParams.add( "dx:" + BASE_UID + "A;" + BASE_UID + "B;" + BASE_UID + "C;" + BASE_UID + "D" );
        dimensionParams.add( "pe" );

        analyticsService.getFromUrl( dimensionParams, null, null, null, null );        
    }

    @Test( expected = IllegalQueryException.class )
    public void testGetFromUrlNoOrganisationUnits()
    {
        Set<String> dimensionParams = new HashSet<String>();
        dimensionParams.add( "dx:" + BASE_UID + "A;" + BASE_UID + "B;" + BASE_UID + "C;" + BASE_UID + "D" );
        dimensionParams.add( "ou" );
        
        analyticsService.getFromUrl( dimensionParams, null, null, null, null );        
    }

    @Test( expected = IllegalQueryException.class )
    public void testGetFromUrlInvalidDimension()
    {
        Set<String> dimensionParams = new HashSet<String>();
        dimensionParams.add( "dx:" + BASE_UID + "A;" + BASE_UID + "B;" + BASE_UID + "C;" + BASE_UID + "D" );
        dimensionParams.add( "yebo:2012,2012S1,2012S2" );
        
        analyticsService.getFromUrl( dimensionParams, null, null, null, null );        
    }    
}
