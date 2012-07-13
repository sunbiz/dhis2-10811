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

package org.hisp.dhis.light.namebaseddataentry.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.api.mobile.ActivityReportingService;
import org.hisp.dhis.api.mobile.NotAllowedException;
import org.hisp.dhis.api.mobile.model.ActivityValue;
import org.hisp.dhis.api.mobile.model.DataElement;
import org.hisp.dhis.api.mobile.model.DataValue;
import org.hisp.dhis.api.mobile.model.ProgramStage;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.light.utils.NamebasedUtils;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.hisp.dhis.program.ProgramStageDataElementService;
import org.hisp.dhis.program.ProgramStageService;
import org.hisp.dhis.util.ContextUtils;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

public class SaveProgramStageFormAction
    implements Action
{
    private static final String SUCCESS_AND_BACK_TO_PROGRAMSTAGE = "success_back_to_programstage";

    private static final String REGISTER_NEXT_DUEDATE = "register_next_duedate";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private NamebasedUtils util;

    public NamebasedUtils getUtil()
    {
        return util;
    }

    public void setUtil( NamebasedUtils util )
    {
        this.util = util;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private ActivityReportingService activityReportingService;

    public void setActivityReportingService( ActivityReportingService activityReportingService )
    {
        this.activityReportingService = activityReportingService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private PatientService patientService;

    public PatientService getPatientService()
    {
        return patientService;
    }

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    private DataElementCategoryService dataElementCategoryService;

    public DataElementCategoryService getDataElementCategoryService()
    {
        return dataElementCategoryService;
    }

    public void setDataElementCategoryService( DataElementCategoryService dataElementCategoryService )
    {
        this.dataElementCategoryService = dataElementCategoryService;
    }

    private ProgramStageService programStageService;

    public ProgramStageService getProgramStageService()
    {
        return programStageService;
    }

    public void setProgramStageService( ProgramStageService programStageService )
    {
        this.programStageService = programStageService;
    }

    private ProgramStageDataElementService programStageDataElementService;

    public ProgramStageDataElementService getProgramStageDataElementService()
    {
        return programStageDataElementService;
    }

    public void setProgramStageDataElementService( ProgramStageDataElementService programStageDataElementService )
    {
        this.programStageDataElementService = programStageDataElementService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private int orgUnitId;

    public void setOrgUnitId( int orgUnitId )
    {
        this.orgUnitId = orgUnitId;
    }

    public int getOrgUnitId()
    {
        return orgUnitId;
    }

    private OrganisationUnit organisationUnit;

    private Integer programStageInstanceId;

    public Integer getProgramStageInstanceId()
    {
        return programStageInstanceId;
    }

    public void setProgramStageInstanceId( Integer programStageInstanceId )
    {
        this.programStageInstanceId = programStageInstanceId;
    }

    private Integer programInstanceId;

    public Integer getProgramInstanceId()
    {
        return programInstanceId;
    }

    public void setProgramInstanceId( Integer programInstanceId )
    {
        this.programInstanceId = programInstanceId;
    }

    private Integer patientId;

    public Integer getPatientId()
    {
        return patientId;
    }

    public void setPatientId( Integer patientId )
    {
        this.patientId = patientId;
    }

    private Integer programId;

    public void setProgramId( Integer programId )
    {
        this.programId = programId;
    }

    public Integer getProgramId()
    {
        return programId;
    }

    private Integer programStageId;

    public void setProgramStageId( Integer programStageId )
    {
        this.programStageId = programStageId;
    }

    public Integer getProgramStageId()
    {
        return programStageId;
    }

    private ProgramStage programStage;

    public ProgramStage getProgramStage()

    {
        return programStage;
    }

    private boolean current;

    public void setCurrent( boolean current )
    {
        this.current = current;
    }

    public boolean getCurrent()
    {
        return current;
    }

    private List<DataElement> dataElements;

    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    private Map<String, String> typeViolations = new HashMap<String, String>();

    public Map<String, String> getTypeViolations()
    {
        return typeViolations;
    }

    private Map<String, String> prevDataValues = new HashMap<String, String>();

    public Map<String, String> getPrevDataValues()
    {
        return prevDataValues;
    }

    private Program program;

    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    private Patient patient;

    public Patient getPatient()
    {
        return patient;
    }

    public void setPatient( Patient patient )
    {
        this.patient = patient;
    }

    @Override
    public String execute()
        throws Exception
    {
        if ( orgUnitId != 0 )
        {
            organisationUnit = organisationUnitService.getOrganisationUnit( orgUnitId );
        }
        else
        {
            organisationUnit = patientService.getPatient( patientId ).getOrganisationUnit();
        }

        programStage = util.getProgramStage( programId, programStageId );
        program = programStageService.getProgramStage( programStageId ).getProgram();
        org.hisp.dhis.program.ProgramStage dhisProgramStage = programStageService.getProgramStage( programStageId );
        
        patient = patientService.getPatient( patientId );
        dataElements = programStage.getDataElements();

        int defaultCategoryOptionId = dataElementCategoryService.getDefaultDataElementCategoryOptionCombo().getId();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(
            ServletActionContext.HTTP_REQUEST );
        Map<String, String> parameterMap = ContextUtils.getParameterMap( request );

        List<DataValue> dataValues = new ArrayList<DataValue>();

        typeViolations.clear();
        prevDataValues.clear();

        for ( String key : parameterMap.keySet() )
        {
            if ( key.startsWith( "DE" ) )
            {
                Integer dataElementId = Integer.parseInt( key.substring( 2, key.length() ) );
                // Integer categoryOptComboId = Integer.parseInt( splitKey[1] );
                String value = parameterMap.get( key );

                // validate types
                org.hisp.dhis.dataelement.DataElement dataElement = dataElementService.getDataElement( dataElementId );
                ProgramStageDataElement programStageDataElement = programStageDataElementService.get( dhisProgramStage,
                    dataElement );
                value = value.trim();
                Boolean valueIsEmpty = (value == null || value.length() == 0);

                if ( !valueIsEmpty )
                {
                    String typeViolation = util.getTypeViolation( dataElement, value );

                    if ( typeViolation != null )
                    {
                        typeViolations.put( key, typeViolation );
                    }
                    prevDataValues.put( key, value );
                }
                else if ( valueIsEmpty && programStageDataElement.isCompulsory() )
                {
                    typeViolations.put( key, "is_empty" );
                    prevDataValues.put( key, value );
                }

                // build dataValue for activity value
                DataValue dataValue = new DataValue();
                dataValue.setId( dataElementId );
                dataValue.setValue( value );

                dataValue.setCategoryOptComboID( defaultCategoryOptionId );

                dataValues.add( dataValue );
            }
        }

        if ( !typeViolations.isEmpty() )
        {
            return ERROR;
        }

        ActivityValue activityValue = new ActivityValue();
        activityValue.setDataValues( dataValues );
        activityValue.setProgramInstanceId( programStageInstanceId );

        try
        {
            activityReportingService.saveActivityReport( organisationUnit, activityValue );
        }
        catch ( NotAllowedException e )
        {
            e.printStackTrace();
            return ERROR;
        }

        if ( dhisProgramStage.getIrregular() )
        {
            return REGISTER_NEXT_DUEDATE;
        }

        if ( orgUnitId != 0 )
        {
            return SUCCESS;
        }
        else
        {
            return SUCCESS_AND_BACK_TO_PROGRAMSTAGE;
        }
    }
}
