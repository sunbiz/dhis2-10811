package org.hisp.dhis.web.webapi.v1.controller;

/*
 * Copyright (c) 2004-2013, University of Oslo
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

import org.hamcrest.Matchers;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.web.FredSpringWebTest;
import org.hisp.dhis.web.webapi.v1.domain.Facility;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class FacilityControllerTest extends FredSpringWebTest
{
    @Autowired
    private IdentifiableObjectManager manager;

    @Test
    public void testRedirectedToV1() throws Exception
    {
        MockHttpSession session = getSession( "ALL" );

        mvc.perform( get( "/api-fred" ).session( session ) ).andExpect( redirectedUrl( "/api-fred/v1" ) );
        mvc.perform( get( "/api-fred/" ).session( session ) ).andExpect( redirectedUrl( "/api-fred/v1" ) );
    }

    @Test
    public void testGetFacilitiesWithALL() throws Exception
    {
        MockHttpSession session = getSession( "ALL" );

        mvc.perform( get( "/v1/facilities" ).session( session ).accept( MediaType.APPLICATION_JSON ) )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.facilities" ).isArray() )
            .andExpect( status().isOk() );
    }

    @Test
    public void testGetFacilitiesWithModuleRights() throws Exception
    {
        MockHttpSession session = getSession( "M_dhis-web-api-fred" );

        mvc.perform( get( "/v1/facilities" ).session( session ).accept( MediaType.APPLICATION_JSON ) )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.facilities" ).isArray() )
            .andExpect( status().isOk() );
    }

    @Test
    public void testGetFacilitiesNoAccess() throws Exception
    {
        MockHttpSession session = getSession( "DUMMY" );

        mvc.perform( get( "/v1/facilities" ).session( session ).accept( MediaType.APPLICATION_JSON ) )
            .andExpect( status().isForbidden() );
    }

    @Test
    public void testGetFacilitiesWithContent() throws Exception
    {
        OrganisationUnit organisationUnit = createOrganisationUnit( 'A' );
        manager.save( organisationUnit );

        MockHttpSession session = getSession( "ALL" );

        mvc.perform( get( "/v1/facilities" ).session( session ).accept( MediaType.APPLICATION_JSON ) )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.facilities" ).isArray() )
            .andExpect( jsonPath( "$.facilities[0].name" ).value( "OrgUnitA" ) )
            .andExpect( status().isOk() );
    }

    @Test
    public void testGetFacility404() throws Exception
    {
        MockHttpSession session = getSession( "ALL" );

        mvc.perform( get( "/v1/facilities/abc123" ).session( session ).accept( MediaType.APPLICATION_JSON ) )
            .andExpect( status().isNotFound() );
    }

    @Test
    public void testGetFacilityUid() throws Exception
    {
        OrganisationUnit organisationUnit = createOrganisationUnit( 'A' );
        manager.save( organisationUnit );

        MockHttpSession session = getSession( "ALL" );

        mvc.perform( get( "/v1/facilities/" + organisationUnit.getUid() ).session( session ).accept( MediaType.APPLICATION_JSON ) )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.name" ).value( "OrgUnitA" ) )
            .andExpect( header().string( "ETag", Matchers.notNullValue() ) )
            .andExpect( status().isOk() );
    }

    @Test
    public void testGetFacilityVerifyPresenceOfETag() throws Exception
    {
        OrganisationUnit organisationUnit = createOrganisationUnit( 'A' );
        manager.save( organisationUnit );

        MockHttpSession session = getSession( "ALL" );

        mvc.perform( get( "/v1/facilities/" + organisationUnit.getUid() ).session( session ).accept( MediaType.APPLICATION_JSON ) )
            .andExpect( header().string( "ETag", Matchers.notNullValue() ) )
            .andExpect( status().isOk() );
    }

    @Test
    public void testGetFacilityUuid() throws Exception
    {
        OrganisationUnit organisationUnit = createOrganisationUnit( 'A' );
        manager.save( organisationUnit );

        MockHttpSession session = getSession( "ALL" );

        mvc.perform( get( "/v1/facilities/" + organisationUnit.getUuid() ).session( session ).accept( MediaType.APPLICATION_JSON ) )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.name" ).value( "OrgUnitA" ) )
            .andExpect( status().isOk() );
    }

    @Test
    public void testPutFacility404() throws Exception
    {
        MockHttpSession session = getSession( "ALL" );

        mvc.perform( put( "/v1/facilities/abc123" ).content( "{}" ).session( session ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( status().isNotFound() );
    }

    @Test
    public void testPostName() throws Exception
    {
        MockHttpSession session = getSession( "ALL" );

        Facility facility = new Facility( "FacilityA" );

        mvc.perform( post( "/v1/facilities" ).content( objectMapper.writeValueAsString( facility ) )
            .session( session ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.name" ).value( "FacilityA" ) )
            .andExpect( status().isCreated() );
    }

    @Test
    public void testPostNameActive() throws Exception
    {
        MockHttpSession session = getSession( "ALL" );

        Facility facility = new Facility( "FacilityA", false );

        mvc.perform( post( "/v1/facilities" ).content( objectMapper.writeValueAsString( facility ) )
            .session( session ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.name" ).value( "FacilityA" ) )
            .andExpect( jsonPath( "$.active" ).value( false ) )
            .andExpect( status().isCreated() );
    }

    @Test
    public void testPostNameDuplicate() throws Exception
    {
        MockHttpSession session = getSession( "ALL" );

        Facility facility = new Facility( "FacilityA" );

        mvc.perform( post( "/v1/facilities" ).content( objectMapper.writeValueAsString( facility ) )
            .session( session ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.name" ).value( "FacilityA" ) )
            .andExpect( status().isCreated() );

        mvc.perform( post( "/v1/facilities" ).content( objectMapper.writeValueAsString( facility ) )
            .session( session ).contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
            .andExpect( jsonPath( "$.name" ).value( "FacilityA" ) )
            .andExpect( status().isCreated() );
    }
}
