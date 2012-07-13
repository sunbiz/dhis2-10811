package org.hisp.dhis.validationrule.action;

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
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.util.SessionUtils;
import org.hisp.dhis.validation.ValidationResult;
import org.hisp.dhis.validation.ValidationRuleGroup;
import org.hisp.dhis.validation.ValidationRuleService;
import org.hisp.dhis.validation.comparator.ValidationResultComparator;

import com.opensymphony.xwork2.Action;

/**
 * @author Margrethe Store
 * @author Lars Helge Overland
 * @version $Id: RunValidationAction.java 6059 2008-10-28 15:15:34Z larshelg $
 */
public class RunValidationAction
    implements Action
{
    private static final Log log = LogFactory.getLog( RunValidationAction.class );

    private static final String KEY_VALIDATIONRESULT = "validationResult";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ValidationRuleService validationRuleService;

    public void setValidationRuleService( ValidationRuleService validationRuleService )
    {
        this.validationRuleService = validationRuleService;
    }

    private I18nFormat format;

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }
    
    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private Integer organisationUnitId;
    
    public void setOrganisationUnitId( Integer organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }

    private String startDate;

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }

    private String endDate;

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }

    private Integer validationRuleGroupId;

    public void setValidationRuleGroupId( Integer validationRuleGroupId )
    {
        this.validationRuleGroupId = validationRuleGroupId;
    }
    
    private List<ValidationResult> validationResults = new ArrayList<ValidationResult>();

    public List<ValidationResult> getValidationResults()
    {
        return validationResults;
    }
    
    private Grid aggregateResults;

    public Grid getAggregateResults()
    {
        return aggregateResults;
    }

    private boolean aggregate;

    public boolean isAggregate()
    {
        return aggregate;
    }

    public void setAggregate( boolean aggregate )
    {
        this.aggregate = aggregate;
    }
    
    private boolean maxExceeded;

    public boolean isMaxExceeded()
    {
        return maxExceeded;
    }
    
    private OrganisationUnit organisationUnit;

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    public String execute()
    {
        organisationUnit = organisationUnitService.getOrganisationUnit( organisationUnitId );
        
        if ( aggregate ) // Aggregate data source
        {
            List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>( organisationUnit.getChildren() );

            List<Period> periods = new ArrayList<Period>( periodService.namePeriods( 
                periodService.getPeriodsBetweenDates( format.parseDate( startDate ), format.parseDate( endDate ) ), format ) );
            
            log.info( "Number of periods: " + periods.size() + ", number of organisation units: " + organisationUnits.size() );
                        
            if ( validationRuleGroupId == -1 )
            {
                log.info( "Validating aggregate data for all rules" );

                validationResults = new ArrayList<ValidationResult>( validationRuleService.validateAggregate( format
                    .parseDate( startDate ), format.parseDate( endDate ), organisationUnits ) );
            }
            else
            {
                ValidationRuleGroup group = validationRuleService.getValidationRuleGroup( validationRuleGroupId );

                log.info( "Validating aggregate data for rules for group: '" + group.getName() + "'" );

                validationResults = new ArrayList<ValidationResult>( validationRuleService.validateAggregate( format
                    .parseDate( startDate ), format.parseDate( endDate ), organisationUnits, group ) );
            }
            
            aggregateResults = validationRuleService.getAggregateValidationResult( validationResults, periods, organisationUnits );
        }
        else // Captured data source
        {
            Collection<OrganisationUnit> organisationUnits = organisationUnitService.getOrganisationUnitWithChildren( organisationUnit.getId() );

            if ( validationRuleGroupId == -1 )
            {
                log.info( "Validating captured data for all rules" );
    
                validationResults = new ArrayList<ValidationResult>( validationRuleService.validate( format
                    .parseDate( startDate ), format.parseDate( endDate ), organisationUnits ) );
            }
            else
            {
                ValidationRuleGroup group = validationRuleService.getValidationRuleGroup( validationRuleGroupId );
    
                log.info( "Validating captured data for rules for group: '" + group.getName() + "'" );
    
                validationResults = new ArrayList<ValidationResult>( validationRuleService.validate( format
                    .parseDate( startDate ), format.parseDate( endDate ), organisationUnits, group ) );
            }
        }

        maxExceeded = validationResults.size() > ValidationRuleService.MAX_VIOLATIONS;
        
        Collections.sort( validationResults, new ValidationResultComparator() );

        SessionUtils.setSessionVar( KEY_VALIDATIONRESULT, validationResults );

        return SUCCESS;
    }
}
