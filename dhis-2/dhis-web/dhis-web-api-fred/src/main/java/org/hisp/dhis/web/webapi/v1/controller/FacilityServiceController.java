package org.hisp.dhis.web.webapi.v1.controller;

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

import org.codehaus.jackson.map.ObjectMapper;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.web.webapi.v1.domain.Facility;
import org.hisp.dhis.web.webapi.v1.utils.MessageResponseUtils;
import org.hisp.dhis.web.webapi.v1.utils.ValidationUtils;
import org.hisp.dhis.web.webapi.v1.validation.group.Create;
import org.hisp.dhis.web.webapi.v1.validation.group.Update;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
@Controller( value = "facility-service-controller-" + FredController.PREFIX )
@RequestMapping( FacilityServiceController.RESOURCE_PATH )
@PreAuthorize( "hasRole('M_dhis-web-api-fred') or hasRole('ALL')" )
public class FacilityServiceController
{
    public static final String RESOURCE_PATH = "/" + FredController.PREFIX + "/facility-service";

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private Validator validator;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    @Qualifier( "objectMapperFactoryBean" )
    private ObjectMapper objectMapper;

    //--------------------------------------------------------------------------
    // EXTRA WEB METHODS
    //--------------------------------------------------------------------------

    @RequestMapping( value = "/{id}/activate", method = RequestMethod.POST )
    @PreAuthorize( "hasRole('F_FRED_UPDATE') or hasRole('ALL')" )
    public ResponseEntity<Void> activateFacility( @PathVariable String id )
    {
        OrganisationUnit organisationUnit = organisationUnitService.getOrganisationUnit( id );

        if ( organisationUnit != null )
        {
            organisationUnit.setActive( true );
            organisationUnitService.updateOrganisationUnit( organisationUnit );

            return new ResponseEntity<Void>( HttpStatus.OK );
        }

        return new ResponseEntity<Void>( HttpStatus.NOT_FOUND );
    }

    @RequestMapping( value = "/{id}/deactivate", method = RequestMethod.POST )
    @PreAuthorize( "hasRole('F_FRED_UPDATE') or hasRole('ALL')" )
    public ResponseEntity<Void> deactivateFacility( @PathVariable String id )
    {
        OrganisationUnit organisationUnit = organisationUnitService.getOrganisationUnit( id );

        if ( organisationUnit != null )
        {
            organisationUnit.setActive( false );
            organisationUnitService.updateOrganisationUnit( organisationUnit );

            return new ResponseEntity<Void>( HttpStatus.OK );
        }

        return new ResponseEntity<Void>( HttpStatus.NOT_FOUND );
    }

    @RequestMapping( value = "/validate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> validateFacilityForCreate( @RequestBody Facility facility ) throws IOException
    {
        Set<ConstraintViolation<Facility>> constraintViolations = validator.validate( facility, Default.class, Create.class );

        String json = ValidationUtils.constraintViolationsToJson( constraintViolations );

        HttpHeaders headers = new HttpHeaders();
        headers.add( "Content-Type", MediaType.APPLICATION_JSON_VALUE );

        if ( constraintViolations.isEmpty() )
        {
            OrganisationUnit organisationUnit = conversionService.convert( facility, OrganisationUnit.class );

            if ( organisationUnitService.getOrganisationUnit( organisationUnit.getUid() ) != null )
            {
                return new ResponseEntity<String>( MessageResponseUtils.jsonMessage( "An object with that ID already exists." ), headers, HttpStatus.CONFLICT );
            }
            else if ( organisationUnitService.getOrganisationUnitByName( organisationUnit.getName() ) != null )
            {
                return new ResponseEntity<String>( MessageResponseUtils.jsonMessage( "An object with that name already exists." ), headers, HttpStatus.CONFLICT );
            }
            else if ( organisationUnit.getCode() != null && organisationUnitService.getOrganisationUnitByCode( organisationUnit.getCode() ) != null )
            {
                return new ResponseEntity<String>( MessageResponseUtils.jsonMessage( "An object with that code already exists." ), headers, HttpStatus.CONFLICT );
            }

            return new ResponseEntity<String>( json, headers, HttpStatus.OK );
        }
        else
        {
            return new ResponseEntity<String>( json, headers, HttpStatus.UNPROCESSABLE_ENTITY );
        }
    }

    @RequestMapping( value = "/validate", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> validateFacilityForUpdate( @RequestBody Facility facility ) throws IOException
    {
        Set<ConstraintViolation<Facility>> constraintViolations = validator.validate( facility, Default.class, Update.class );

        String json = ValidationUtils.constraintViolationsToJson( constraintViolations );

        HttpHeaders headers = new HttpHeaders();
        headers.add( "Content-Type", MediaType.APPLICATION_JSON_VALUE );

        if ( constraintViolations.isEmpty() )
        {
            OrganisationUnit organisationUnit = conversionService.convert( facility, OrganisationUnit.class );
            OrganisationUnit ou = organisationUnitService.getOrganisationUnit( facility.getId() );

            if ( ou == null )
            {
                return new ResponseEntity<String>( MessageResponseUtils.jsonMessage( "No object with that identifier exists." ),
                    headers, HttpStatus.NOT_FOUND );
            }
            else if ( organisationUnit.getCode() != null )
            {
                OrganisationUnit ouByCode = organisationUnitService.getOrganisationUnitByCode( organisationUnit.getCode() );

                if ( ouByCode != null && !ou.getUid().equals( ouByCode.getUid() ) )
                {
                    return new ResponseEntity<String>( MessageResponseUtils.jsonMessage( "Another object with the same code already exists." ),
                        headers, HttpStatus.CONFLICT );
                }
            }

            return new ResponseEntity<String>( json, headers, HttpStatus.OK );
        }
        else
        {
            return new ResponseEntity<String>( json, headers, HttpStatus.UNPROCESSABLE_ENTITY );
        }
    }

    @RequestMapping( value = "/cql", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE )
    public ResponseEntity<String> cqlRequest( @RequestBody String cqlString ) throws IOException, CQLException
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add( "Content-Type", MediaType.APPLICATION_JSON_VALUE );

        if ( cqlString == null || cqlString.isEmpty() )
        {
            return new ResponseEntity<String>( "{}", headers, HttpStatus.OK );
        }

        Filter filter = CQL.toFilter( cqlString );

        List<OrganisationUnit> allOrganisationUnits = new ArrayList<OrganisationUnit>( organisationUnitService.getAllOrganisationUnits() );
        List<Facility> facilities = new ArrayList<Facility>();

        for ( OrganisationUnit organisationUnit : allOrganisationUnits )
        {
            if ( organisationUnit.getFeatureType() != null
                && organisationUnit.getFeatureType().equals( OrganisationUnit.FEATURETYPE_POINT )
                && organisationUnit.getCoordinates() != null && !organisationUnit.getCoordinates().isEmpty() )
            {
                SimpleFeature feature = conversionService.convert( organisationUnit, SimpleFeature.class );

                if ( filter.evaluate( feature ) )
                {
                    Facility facility = conversionService.convert( organisationUnit, Facility.class );
                    facilities.add( facility );
                }
            }
        }

        Map<String, Object> resultSet = new HashMap<String, Object>();
        resultSet.put( "facilities", facilities );

        String json = objectMapper.writeValueAsString( resultSet );

        return new ResponseEntity<String>( json, headers, HttpStatus.OK );
    }
}
