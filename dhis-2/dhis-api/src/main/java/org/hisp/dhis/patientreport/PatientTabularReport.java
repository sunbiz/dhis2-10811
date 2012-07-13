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

package org.hisp.dhis.patientreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientIdentifierType;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.hisp.dhis.user.User;

/**
 * @author Chau Thu Tran
 * 
 * @version $PatientTabularReport.java May 7, 2012 12:41:41 PM$
 */
public class PatientTabularReport
    extends BaseIdentifiableObject
{
    private static final long serialVersionUID = -2880334669266185058L;

    public static String PREFIX_EXECUTION_DATE = "executiondate";
    
    public static String  PREFIX_ORGUNIT = "orgunit";
    
    public static String PREFIX_META_DATA = "meta";
    
    public static String PREFIX_IDENTIFIER_TYPE = "iden";

    public static String PREFIX_FIXED_ATTRIBUTE = "fixedAttr";
    
    public static String PREFIX_PATIENT_ATTRIBUTE = "attr";

    public static String PREFIX_DATA_ELEMENT = "de";

    public static String VALUE_TYPE_OPTION_SET = "optionSet";

    
    private Date startDate;

    private Date endDate;

    private List<PatientIdentifierType> identifiers = new ArrayList<PatientIdentifierType>();

    private List<PatientAttribute> attributes = new ArrayList<PatientAttribute>();

    private List<String> fixedAttributes = new ArrayList<String>();

    private List<ProgramStageDataElement> programStageDataElements = new ArrayList<ProgramStageDataElement>();

    private Set<OrganisationUnit> organisationUnits;

    private int level;

    private boolean sortedOrgunitAsc;

    private String facilityLB;

    private User user;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public PatientTabularReport()
    {
    }

    public PatientTabularReport( String name )
    {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    public Set<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setOrganisationUnits( Set<OrganisationUnit> organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }

    public List<PatientIdentifierType> getIdentifiers()
    {
        return identifiers;
    }

    public void setIdentifiers( List<PatientIdentifierType> identifiers )
    {
        this.identifiers = identifiers;
    }

    public List<PatientAttribute> getAttributes()
    {
        return attributes;
    }

    public void setAttributes( List<PatientAttribute> attributes )
    {
        this.attributes = attributes;
    }

    public List<String> getFixedAttributes()
    {
        return fixedAttributes;
    }

    public void setFixedAttributes( List<String> fixedAttributes )
    {
        this.fixedAttributes = fixedAttributes;
    }

    public List<ProgramStageDataElement> getProgramStageDataElements()
    {
        return programStageDataElements;
    }

    public void setProgramStageDataElements( List<ProgramStageDataElement> programStageDataElements )
    {
        this.programStageDataElements = programStageDataElements;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel( int level )
    {
        this.level = level;
    }

    public boolean isSortedOrgunitAsc()
    {
        return sortedOrgunitAsc;
    }

    public void setSortedOrgunitAsc( boolean sortedOrgunitAsc )
    {
        this.sortedOrgunitAsc = sortedOrgunitAsc;
    }

    public String getFacilityLB()
    {
        return facilityLB;
    }

    public void setFacilityLB( String facilityLB )
    {
        this.facilityLB = facilityLB;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

}
