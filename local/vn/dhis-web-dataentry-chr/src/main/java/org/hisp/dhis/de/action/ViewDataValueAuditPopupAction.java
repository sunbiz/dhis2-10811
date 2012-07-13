package org.hisp.dhis.de.action;

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

import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValueAudit;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;

import com.opensymphony.xwork2.Action;

/**
 * @author Latifov Murodillo Abdusamadovich
 * @version $Id$
 */
public class ViewDataValueAuditPopupAction
    implements Action
{
    private Integer organisationUnitId;

    private Integer dataElementComboId;

    private Integer dataElementId;

    private Integer periodId;

    private List<DataValueAudit> dataValueAudits;

    private DataValueAudit dataValueAudit;

    private OrganisationUnit organisationUnit;

    private Period period;

    private DataElementCategoryOptionCombo dataElementCategoryOptionCombo;

    private DataElement dataElement;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public DataElementCategoryOptionCombo getDataElementCategoryOptionCombo()
    {
        return dataElementCategoryOptionCombo;
    }

    public void setDataElementCategoryOptionCombo( DataElementCategoryOptionCombo dataElementCategoryOptionCombo )
    {
        this.dataElementCategoryOptionCombo = dataElementCategoryOptionCombo;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    public void setDataElementCategoryOptionComboService( DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    public void setOrganisationUnit( OrganisationUnit organisationUnit )
    {
        this.organisationUnit = organisationUnit;
    }

    public Period getPeriod()
    {
        return period;
    }

    public void setPeriod( Period period )
    {
        this.period = period;
    }

    public DataValueAudit getDataValueAudit()
    {
        return dataValueAudit;
    }

    public void setDataValueAudit( DataValueAudit dataValueAudit )
    {
        this.dataValueAudit = dataValueAudit;
    }

    public List<DataValueAudit> getDataValueAudits()
    {
        return dataValueAudits;
    }

    public void setDataValueAudits( List<DataValueAudit> dataValueAudits )
    {
        this.dataValueAudits = dataValueAudits;
    }

    public Integer getOrganisationUnitId()
    {
        return organisationUnitId;
    }

    public void setOrganisationUnitId( Integer organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }

    public Integer getDataElementComboId()
    {
        return dataElementComboId;
    }

    public void setDataElementComboId( Integer dataElementComboId )
    {
        this.dataElementComboId = dataElementComboId;
    }

    public Integer getDataElementId()
    {
        return dataElementId;
    }

    public void setDataElementId( Integer dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public Integer getPeriodId()
    {
        return periodId;
    }

    public void setPeriodId( Integer periodId )
    {
        this.periodId = periodId;
    }

    public String execute()
        throws Exception
    {
        dataElement = dataElementService.getDataElement( getDataElementId() );
        organisationUnit = organisationUnitService.getOrganisationUnit( getOrganisationUnitId() );
        period = periodService.getPeriod( getPeriodId() );
        
        dataElementCategoryOptionCombo = dataElementCategoryOptionComboService
            .getDataElementCategoryOptionCombo( getDataElementComboId() );

        dataValueAudits = (List<DataValueAudit>) dataValueService.getDataValueAudits( organisationUnit, period,
            dataElement, dataElementCategoryOptionCombo );

        return SUCCESS;
    }
}
