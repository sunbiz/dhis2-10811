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

package org.hisp.dhis.caseentry.action.patient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.ouwt.manager.OrganisationUnitSelectionManager;
import org.hisp.dhis.paging.ActionPagingSupport;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
public class SearchPatientAction
    extends ActionPagingSupport<Patient>
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitSelectionManager selectionManager;

    private PatientService patientService;

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private List<String> searchTexts = new ArrayList<String>();

    private Boolean searchBySelectedOrgunit;

    private boolean listAll;

    private Collection<Patient> patients = new ArrayList<Patient>();

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public void setSelectionManager( OrganisationUnitSelectionManager selectionManager )
    {
        this.selectionManager = selectionManager;
    }

    public void setSearchBySelectedOrgunit( Boolean searchBySelectedOrgunit )
    {
        this.searchBySelectedOrgunit = searchBySelectedOrgunit;
    }

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    public void setSearchTexts( List<String> searchTexts )
    {
        this.searchTexts = searchTexts;
    }

    public void setListAll( boolean listAll )
    {
        this.listAll = listAll;
    }

    public Collection<Patient> getPatients()
    {
        return patients;
    }

    private Integer total;

    public Integer getTotal()
    {
        return total;
    }

    private Map<Integer, String> mapPatientOrgunit = new HashMap<Integer, String>();

    public Map<Integer, String> getMapPatientOrgunit()
    {
        return mapPatientOrgunit;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        OrganisationUnit organisationUnit = selectionManager.getSelectedOrganisationUnit();

        // List all patients
        if ( listAll )
        {
            total = patientService.countGetPatientsByOrgUnit( organisationUnit );
            this.paging = createPaging( total );

            patients = new ArrayList<Patient>( patientService.getPatients( organisationUnit, paging.getStartPos(),
                paging.getPageSize() ) );

        }
        // search patients
        else if ( searchTexts.size() > 0 )
        {
            organisationUnit = (searchBySelectedOrgunit) ? organisationUnit : null;

            total = patientService.countSearchPatients( searchTexts, organisationUnit );
            this.paging = createPaging( total );
            patients = patientService.searchPatients( searchTexts, organisationUnit, paging.getStartPos(),
                paging.getPageSize() );

            for ( Patient patient : patients )
            {
                mapPatientOrgunit.put( patient.getId(), getHierarchyOrgunit( patient.getOrganisationUnit() ) );
            }
        }

        return SUCCESS;
    }

    // -------------------------------------------------------------------------
    // Supportive method
    // -------------------------------------------------------------------------

    private String getHierarchyOrgunit( OrganisationUnit orgunit )
    {
        String hierarchyOrgunit = orgunit.getName();

        while ( orgunit.getParent() != null )
        {
            hierarchyOrgunit = orgunit.getParent().getName() + " / " + hierarchyOrgunit;

            orgunit = orgunit.getParent();
        }

        return hierarchyOrgunit;
    }
}
