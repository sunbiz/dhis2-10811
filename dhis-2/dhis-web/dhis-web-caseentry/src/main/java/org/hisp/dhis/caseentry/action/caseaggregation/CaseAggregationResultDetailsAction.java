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

package org.hisp.dhis.caseentry.action.caseaggregation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.hisp.dhis.caseaggregation.CaseAggregationCondition;
import org.hisp.dhis.caseaggregation.CaseAggregationConditionService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.program.ProgramStageInstance;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version CaseAggregationResultDetailsAction.java Mar 23, 2011 10:42:51 AM $
 */
public class CaseAggregationResultDetailsAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    private PeriodService periodService;

    private CaseAggregationConditionService aggregationConditionService;

    private PatientDataValueService patientDataValueService;

    private I18nFormat format;

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private Integer orgunitId;

    private Integer aggregationConditionId;

    private String periodTypeName;

    private String startDate;

    private Map<Patient, Collection<PatientDataValue>> mapPatients;

    private Map<ProgramStageInstance, Collection<PatientDataValue>> mapEvents;

    // -------------------------------------------------------------------------
    // Getter/Setter
    // -------------------------------------------------------------------------

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    public void setAggregationConditionService( CaseAggregationConditionService aggregationConditionService )
    {
        this.aggregationConditionService = aggregationConditionService;
    }

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    public void setPeriodTypeName( String periodTypeName )
    {
        this.periodTypeName = periodTypeName;
    }

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }

    public Map<Patient, Collection<PatientDataValue>> getMapPatients()
    {
        return mapPatients;
    }

    public Map<ProgramStageInstance, Collection<PatientDataValue>> getMapEvents()
    {
        return mapEvents;
    }

    public void setOrgunitId( Integer orgunitId )
    {
        this.orgunitId = orgunitId;
    }

    public void setAggregationConditionId( Integer aggregationConditionId )
    {
        this.aggregationConditionId = aggregationConditionId;
    }
    
    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Implementation Action
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        OrganisationUnit orgunit = organisationUnitService.getOrganisationUnit( orgunitId );

        PeriodType periodType = periodService.getPeriodTypeByName( periodTypeName );
        Period period = periodType.createPeriod( format.parseDate( startDate ) );

        CaseAggregationCondition aggCondition = aggregationConditionService
            .getCaseAggregationCondition( aggregationConditionId );

        if ( aggCondition.getOperator().equals( CaseAggregationCondition.AGGRERATION_SUM ) )
        {
            mapEvents = new HashMap<ProgramStageInstance, Collection<PatientDataValue>>();

            Collection<ProgramStageInstance> programStageInstances = aggregationConditionService
                .getProgramStageInstances( aggCondition, orgunit, period );

            for ( ProgramStageInstance programStageInstance : programStageInstances )
            {
                Collection<DataElement> dataElements = aggregationConditionService
                    .getDataElementsInCondition( aggCondition.getAggregationExpression() );

                Collection<PatientDataValue> dataValues = new HashSet<PatientDataValue>();

                if ( dataElements.size() > 0 )
                {
                    dataValues = patientDataValueService.getPatientDataValues( programStageInstance, dataElements );
                }

                mapEvents.put( programStageInstance, dataValues );
            }
        }
        else
        {
            mapPatients = new HashMap<Patient, Collection<PatientDataValue>>();

            Collection<Patient> patients = aggregationConditionService.getPatients( aggCondition, orgunit, period );

            for ( Patient patient : patients )
            {
                Collection<DataElement> dataElements = aggregationConditionService
                    .getDataElementsInCondition( aggCondition.getAggregationExpression() );

                Collection<PatientDataValue> dataValues = new HashSet<PatientDataValue>();

                if ( dataElements.size() > 0 )
                {
                    dataValues = patientDataValueService.getPatientDataValues( patient, dataElements, period
                        .getStartDate(), period.getEndDate() );
                }

                mapPatients.put( patient, dataValues );
            }
        }

        return SUCCESS;
    }
}
