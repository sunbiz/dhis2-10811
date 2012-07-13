package org.hisp.dhis.patientdatavalue.aggregation;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.amplecode.quick.StatementManager;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.system.util.DateUtils;
import org.hisp.dhis.system.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings( "unused" )
public class DefaultPatientDataValueAggregationEngine
    implements PatientDataValueAggregationEngine
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PatientService patientService;

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    // -------------------------------------------------------------------------
    // Implementation Action
    // -------------------------------------------------------------------------

    public void aggregate( Date startDate, Date endDate, OrganisationUnit organisationUnit )
    {
        Collection<DataElement> dataElements = null;

        Collection<Integer> patients = null;

        Collection<Period> periods = periodService.getPeriodsBetweenDates( startDate, endDate );
    }

    private void aggregate( DataElement dataElement, Period period,
        Collection<Integer> patients )
    {
        // ---------------------------------------------------------------------
        // sum - int
        // ---------------------------------------------------------------------

        String sql = "SELECT sum( value ) " + "FROM patientdatavalue " + "WHERE dataelementid = '"
            + dataElement.getId() + "' " + "AND datetime > '" + DateUtils.getMediumDateString( period.getStartDate() )
            + " " + "AND datetime <= " + DateUtils.getMediumDateString( period.getEndDate() ) + " "
            + "AND patientid IN (" + TextUtils.getCommaDelimitedString( patients ) + ")";

        // ---------------------------------------------------------------------
        // sum - bool
        // ---------------------------------------------------------------------

        sql = "SELECT count( * ) " + "FROM patientdatavalue " + "WHERE dataelementid = '" + dataElement.getId() + "' "
            + "AND datetime > '" + DateUtils.getMediumDateString( period.getStartDate() ) + " " + "AND datetime <= "
            + DateUtils.getMediumDateString( period.getEndDate() ) + " " + "AND patientid IN ("
            + TextUtils.getCommaDelimitedString( patients ) + ") " + "AND value='T'";
    }

    private Collection<Period> filterPeriods( Collection<Period> periods, PeriodType periodType )
    {
        Collection<Period> filteredPeriods = new ArrayList<Period>();

        for ( Period period : periods )
        {
            if ( period.getPeriodType().equals( periodType ) )
            {
                filteredPeriods.add( period );
            }
        }

        return filteredPeriods;
    }
}
