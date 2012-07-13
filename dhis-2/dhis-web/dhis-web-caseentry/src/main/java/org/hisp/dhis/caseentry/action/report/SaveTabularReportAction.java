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

package org.hisp.dhis.caseentry.action.report;

import static org.hisp.dhis.patientreport.PatientTabularReport.PREFIX_DATA_ELEMENT;
import static org.hisp.dhis.patientreport.PatientTabularReport.PREFIX_FIXED_ATTRIBUTE;
import static org.hisp.dhis.patientreport.PatientTabularReport.PREFIX_IDENTIFIER_TYPE;
import static org.hisp.dhis.patientreport.PatientTabularReport.PREFIX_PATIENT_ATTRIBUTE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientAttributeService;
import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.patient.PatientIdentifierTypeService;
import org.hisp.dhis.patientreport.PatientTabularReport;
import org.hisp.dhis.patientreport.PatientTabularReportService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.hisp.dhis.program.ProgramStageDataElementService;
import org.hisp.dhis.program.ProgramStageService;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * 
 * @version $SaveTabularReportAction.java May 7, 2012 3:13:11 PM$
 */
public class SaveTabularReportAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PatientTabularReportService tabularReportService;

    public void setTabularReportService( PatientTabularReportService tabularReportService )
    {
        this.tabularReportService = tabularReportService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private ProgramStageService programStageService;

    public void setProgramStageService( ProgramStageService programStageService )
    {
        this.programStageService = programStageService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private PatientIdentifierTypeService identifierTypeService;

    public void setIdentifierTypeService( PatientIdentifierTypeService identifierTypeService )
    {
        this.identifierTypeService = identifierTypeService;
    }

    private PatientAttributeService patientAttributeService;

    public void setPatientAttributeService( PatientAttributeService patientAttributeService )
    {
        this.patientAttributeService = patientAttributeService;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private ProgramStageDataElementService programStageDataElementService;

    public void setProgramStageDataElementService( ProgramStageDataElementService programStageDataElementService )
    {
        this.programStageDataElementService = programStageDataElementService;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String name;

    private String startDate;

    private String endDate;

    private Integer programStageId;

    private List<String> searchingValues = new ArrayList<String>();

    private Collection<Integer> orgunitIds;

    private boolean orderByOrgunitAsc;

    private Integer level;

    private String facilityLB;

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public void setName( String name )
    {
        this.name = name;
    }

    public void setLevel( Integer level )
    {
        this.level = level;
    }

    public void setSearchingValues( List<String> searchingValues )
    {
        this.searchingValues = searchingValues;
    }

    public void setFacilityLB( String facilityLB )
    {
        this.facilityLB = facilityLB;
    }

    public void setOrderByOrgunitAsc( boolean orderByOrgunitAsc )
    {
        this.orderByOrgunitAsc = orderByOrgunitAsc;
    }

    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }

    public void setOrgunitIds( Collection<Integer> orgunitIds )
    {
        this.orgunitIds = orgunitIds;
    }

    public void setProgramStageId( Integer programStageId )
    {
        this.programStageId = programStageId;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        Set<OrganisationUnit> orgunits = new HashSet<OrganisationUnit>( organisationUnitService
            .getOrganisationUnits( orgunitIds ) );
        ProgramStage programStage = programStageService.getProgramStage( programStageId );

        // ---------------------------------------------------------------------
        // Get fixed-attributes
        // ---------------------------------------------------------------------

        PatientTabularReport tabularReport = new PatientTabularReport( name );
        tabularReport.setStartDate( format.parseDate( startDate ) );
        tabularReport.setEndDate( format.parseDate( endDate ) );
        tabularReport.setOrganisationUnits( orgunits );
        tabularReport.setLevel( level );
        tabularReport.setFacilityLB( facilityLB );
        tabularReport.setSortedOrgunitAsc( orderByOrgunitAsc );
        tabularReport.setUser( currentUserService.getCurrentUser() );

        // ---------------------------------------------------------------------
        // Get searching-keys
        // ---------------------------------------------------------------------

        List<PatientIdentifierType> identifiers = new ArrayList<PatientIdentifierType>();

        List<PatientAttribute> attributes = new ArrayList<PatientAttribute>();

        List<ProgramStageDataElement> programStageDataElements = new ArrayList<ProgramStageDataElement>();
        List<String> fixedAttributes = new ArrayList<String>();

        for ( String searchingValue : searchingValues )
        {
            String[] infor = searchingValue.split( "_" );
            String objectType = infor[0];

            if ( objectType.equals( PREFIX_IDENTIFIER_TYPE ) )
            {
                int objectId = Integer.parseInt( infor[1] );
                identifiers.add( identifierTypeService.getPatientIdentifierType( objectId ) );
            }
            else if ( objectType.equals( PREFIX_FIXED_ATTRIBUTE ) )
            {
                fixedAttributes.add( infor[1] );
            }
            else if ( objectType.equals( PREFIX_PATIENT_ATTRIBUTE ) )
            {
                int objectId = Integer.parseInt( infor[1] );
                attributes.add( patientAttributeService.getPatientAttribute( objectId ) );
            }
            else if ( objectType.equals( PREFIX_DATA_ELEMENT ) )
            {
                int objectId = Integer.parseInt( infor[1] );
                DataElement dataElement = dataElementService.getDataElement( objectId );
                programStageDataElements.add( programStageDataElementService.get( programStage, dataElement ) );
            }
        }

        tabularReport.setFixedAttributes( fixedAttributes );
        tabularReport.setIdentifiers( identifiers );
        tabularReport.setAttributes( attributes );
        tabularReport.setProgramStageDataElements( programStageDataElements );

        tabularReportService.saveOrUpdate( tabularReport );

        return SUCCESS;
    }

}
