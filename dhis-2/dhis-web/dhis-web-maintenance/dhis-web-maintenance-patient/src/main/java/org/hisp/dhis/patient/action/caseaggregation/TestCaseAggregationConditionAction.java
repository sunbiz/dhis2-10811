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

package org.hisp.dhis.patient.action.caseaggregation;

import java.util.Collection;
import java.util.Date;

import org.hisp.dhis.caseaggregation.CaseAggregationCondition;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.AGGRERATION_COUNT;
import org.hisp.dhis.caseaggregation.CaseAggregationConditionService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.program.Program;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * 
 * @version $Id: TestCaseAggregationConditionAction.java Oct 5, 2011 3:45:20 PM
 *          $
 */
public class TestCaseAggregationConditionAction
    implements Action
{

    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private CaseAggregationConditionService aggregationConditionService;

    public void setAggregationConditionService( CaseAggregationConditionService aggregationConditionService )
    {
        this.aggregationConditionService = aggregationConditionService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    private String condition;

    public void setCondition( String condition )
    {
        this.condition = condition;
    }

    private String operator;

    public void setOperator( String operator )
    {
        this.operator = operator;
    }

    private Integer deSumId;

    public void setDeSumId( Integer deSumId )
    {
        this.deSumId = deSumId;
    }

    private String message;

    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        CaseAggregationCondition aggCondition = new CaseAggregationCondition( "", operator, condition, null, null );
        if ( deSumId != null )
        {
            aggCondition.setDeSum( dataElementService.getDataElement( deSumId ) );
        }

        Collection<Program> programs = aggregationConditionService.getProgramsInCondition( condition );

        if ( operator.equals( AGGRERATION_COUNT ) )
        {
            for ( Program program : programs )
            {
                if ( program.getType() == Program.SINGLE_EVENT_WITHOUT_REGISTRATION )
                {
                    message = i18n.getString( "select_operator_number_of_visits_for_this_condition" );
                    return INPUT;
                }
            }
        }

        OrganisationUnit orgunit = new OrganisationUnit();
        orgunit.setId( 1 );

        Period period = new Period();
        period.setStartDate( new Date() );
        period.setEndDate( new Date() );

        Integer value = aggregationConditionService.parseConditition( aggCondition, orgunit, period );

        if ( value == null )
        {
            return INPUT;
        }

        return SUCCESS;
    }
}
