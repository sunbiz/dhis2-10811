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

package org.hisp.dhis.sms.namebaseddataentry.action;

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
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.sms.utils.NamebasedUtils;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.util.ContextUtils;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

public class SaveProgramStageFormAction
    implements Action
{

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

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private String orgUnitId;

    public void setOrgUnitId( String orgUnitId )
    {
        this.orgUnitId = orgUnitId;
    }

    public String getOrgUnitId()
    {
        return orgUnitId;
    }

    private OrganisationUnit organisationUnit;

    private String programStageInstanceId;

    public String getProgramStageInstanceId()
    {
        return programStageInstanceId;
    }

    public void setProgramStageInstanceId( String programStageInstanceId )
    {
        this.programStageInstanceId = programStageInstanceId;
    }

    private String beneficiaryId;

    public void setBeneficiaryId( String beneficiaryId )
    {
        this.beneficiaryId = beneficiaryId;
    }

    public String getBeneficiaryId()
    {
        return beneficiaryId;
    }

    private String programId;

    public void setProgramId( String programId )
    {
        this.programId = programId;
    }

    public String getProgramId()
    {
        return programId;
    }

    private String programStageId;

    public void setProgramStageId( String programStageId )
    {
        this.programStageId = programStageId;
    }

    public String getProgramStageId()
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

    @Override
    public String execute()
        throws Exception
    {
        organisationUnit = organisationUnitService.getOrganisationUnit( Integer.parseInt( orgUnitId ) );

        programStage = util.getProgramStage( Integer.parseInt( programId ), Integer.parseInt( programStageId ) );

        dataElements = programStage.getDataElements();

        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(
            ServletActionContext.HTTP_REQUEST );
        Map<String, String> parameterMap = ContextUtils.getParameterMap( request );

        List<DataValue> dataValues = new ArrayList<DataValue>();

        typeViolations.clear();
        prevDataValues.clear();

        for ( String key : parameterMap.keySet() )
        {
            if ( key.startsWith( "DE" ) && key.indexOf( "OC" ) != -1 )
            {
                String[] splitKey = key.split( "OC" );
                Integer dataElementId = Integer.parseInt( splitKey[0].substring( 2 ) );
                Integer categoryOptComboId = Integer.parseInt( splitKey[1] );
                String value = parameterMap.get( key );

                // validate types
                org.hisp.dhis.dataelement.DataElement dataElement = dataElementService.getDataElement( dataElementId );
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

                // build dataValue for activity value
                DataValue dataValue = new DataValue();
                dataValue.setId( dataElementId );
                dataValue.setValue( value );
                dataValue.setCategoryOptComboID( categoryOptComboId );

                dataValues.add( dataValue );
            }
        }

        if ( !typeViolations.isEmpty() )
        {
            return ERROR;
        }

        ActivityValue activityValue = new ActivityValue();
        activityValue.setDataValues( dataValues );
        activityValue.setProgramInstanceId( Integer.parseInt( programStageInstanceId ) );

        try
        {
            activityReportingService.saveActivityReport( organisationUnit, activityValue );
        }
        catch ( NotAllowedException e )
        {
            e.printStackTrace();

            return ERROR;
        }

        return SUCCESS;
    }

}
