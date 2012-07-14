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

package org.hisp.dhis.patientreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.user.User;

/**
 * @author Chau Thu Tran
 * 
 * @version PatientAggregateReport.java 11:24:11 AM Jan 10, 2013 $
 */
public class PatientAggregateReport
    extends BaseIdentifiableObject
{
    private static final long serialVersionUID = 3261142704777097572L;

    public static final int POSITION_ROW_ORGUNIT_COLUMN_PERIOD = 1;

    public static final int POSITION_ROW_PERIOD_COLUMN_ORGUNIT = 2;

    public static final int POSITION_ROW_ORGUNIT_ROW_PERIOD = 3;

    public static final int POSITION_ROW_PERIOD = 4;

    public static final int POSITION_ROW_ORGUNIT = 5;

    public static final int POSITION_ROW_PERIOD_COLUMN_DATA = 6;

    public static final int POSITION_ROW_ORGUNIT_COLUMN_DATA = 7;

    public static final int POSITION_ROW_DATA = 8;

    public static final int POSITION_ROW_DATA_COLUMN_PERIOD = 9; // PIVOT FROM 6

    public static final int POSITION_ROW_DATA_COLUMN_ORGUNIT = 10; // PIVOT FROM
                                                                   // 7
    
    public static final String AGGREGATE_TYPE_COUNT = "count";
    public static final String AGGREGATE_TYPE_SUM = "sum";
    public static final String AGGREGATE_TYPE_AVG = "avg";
    
    public static final String SEPARATE_FILTER = "_";

    private ProgramStage programStage;

    // Date period range

    private List<Date> startDates;

    private List<Date> endDates;

    // Relative periods

    private Set<String> relativePeriods = new HashSet<String>();

    // Fixed periods

    private List<String> fixedPeriods = new ArrayList<String>();

    // Organisation units

    private Set<OrganisationUnit> organisationUnits;

    // Data element filter values

    private Set<String> filterValues = new HashSet<String>();

    // Option

    private String facilityLB;

    private Integer limitRecords;

    private int position;

    private DataElement deGroupBy;
    
    private DataElement deSum;

    private String aggregateType;

    private Boolean useCompletedEvents;

    private Boolean userOrganisationUnit;

    private Boolean userOrganisationUnitChildren;

    // User created

    private User user;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public PatientAggregateReport()
    {

    }

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public ProgramStage getProgramStage()
    {
        return programStage;
    }

    public void setProgramStage( ProgramStage programStage )
    {
        this.programStage = programStage;
    }

    public List<Date> getStartDates()
    {
        return startDates;
    }

    public void setStartDates( List<Date> startDates )
    {
        this.startDates = startDates;
    }

    public List<Date> getEndDates()
    {
        return endDates;
    }

    public void setEndDates( List<Date> endDates )
    {
        this.endDates = endDates;
    }

    public Set<String> getRelativePeriods()
    {
        return relativePeriods;
    }

    public void setRelativePeriods( Set<String> relativePeriods )
    {
        this.relativePeriods = relativePeriods;
    }

    public List<String> getFixedPeriods()
    {
        return fixedPeriods;
    }

    public void setFixedPeriods( List<String> fixedPeriods )
    {
        this.fixedPeriods = fixedPeriods;
    }

    public Set<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setOrganisationUnits( Set<OrganisationUnit> organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }

    public Set<String> getFilterValues()
    {
        return filterValues;
    }

    public void setFilterValues( Set<String> filterValues )
    {
        this.filterValues = filterValues;
    }

    public String getFacilityLB()
    {
        return facilityLB;
    }

    public void setFacilityLB( String facilityLB )
    {
        this.facilityLB = facilityLB;
    }

    public Integer getLimitRecords()
    {
        return limitRecords;
    }

    public void setLimitRecords( Integer limitRecords )
    {
        this.limitRecords = limitRecords;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition( int position )
    {
        this.position = position;
    }

    public DataElement getDeGroupBy()
    {
        return deGroupBy;
    }

    public void setDeGroupBy( DataElement deGroupBy )
    {
        this.deGroupBy = deGroupBy;
    }

    public String getAggregateType()
    {
        return aggregateType;
    }

    public void setAggregateType( String aggregateType )
    {
        this.aggregateType = aggregateType;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    public Boolean getUseCompletedEvents()
    {
        return useCompletedEvents;
    }

    public void setUseCompletedEvents( Boolean useCompletedEvents )
    {
        this.useCompletedEvents = useCompletedEvents;
    }

    public Boolean getUserOrganisationUnit()
    {
        return userOrganisationUnit;
    }

    public void setUserOrganisationUnit( Boolean userOrganisationUnit )
    {
        this.userOrganisationUnit = userOrganisationUnit;
    }

    public Boolean getUserOrganisationUnitChildren()
    {
        return userOrganisationUnitChildren;
    }

    public void setUserOrganisationUnitChildren( Boolean userOrganisationUnitChildren )
    {
        this.userOrganisationUnitChildren = userOrganisationUnitChildren;
    }

    public DataElement getDeSum()
    {
        return deSum;
    }

    public void setDeSum( DataElement deSum )
    {
        this.deSum = deSum;
    }

}
