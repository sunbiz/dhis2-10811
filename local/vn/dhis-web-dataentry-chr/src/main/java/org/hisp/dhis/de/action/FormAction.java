package org.hisp.dhis.de.action;

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

import static org.hisp.dhis.options.SystemSettingManager.KEY_ZERO_VALUE_SAVE_MODE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.customvalue.CustomValue;
import org.hisp.dhis.customvalue.CustomValueService;
import org.hisp.dhis.dataelement.CalculatedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataset.DataEntryForm;
import org.hisp.dhis.dataset.DataEntryFormService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.de.comments.StandardCommentsManager;
import org.hisp.dhis.de.screen.DataEntryScreenManager;
import org.hisp.dhis.de.state.SelectedStateManager;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.minmax.MinMaxDataElementService;
import org.hisp.dhis.options.SystemSettingManager;
import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;
import org.hisp.dhis.order.manager.DataElementOrderManager;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

import com.opensymphony.xwork2.Action;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: FormAction.java 6216 2008-11-06 18:06:42Z eivindwa $
 */
public class FormAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CustomValueService customValueService;

    public CustomValueService getCustomValueService()
    {
        return customValueService;
    }

    public void setCustomValueService( CustomValueService customValueService )
    {
        this.customValueService = customValueService;
    }

    private SystemSettingManager systemSettingManager;

    public void setSystemSettingManager( SystemSettingManager systemSettingManager )
    {
        this.systemSettingManager = systemSettingManager;
    }

    private DataElementOrderManager dataElementOrderManager;

    public void setDataElementOrderManager( DataElementOrderManager dataElementOrderManager )
    {
        this.dataElementOrderManager = dataElementOrderManager;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private DataEntryFormService dataEntryFormService;

    public void setDataEntryFormService( DataEntryFormService dataEntryFormService )
    {
        this.dataEntryFormService = dataEntryFormService;
    }

    private StandardCommentsManager standardCommentsManager;

    public void setStandardCommentsManager( StandardCommentsManager standardCommentsManager )
    {
        this.standardCommentsManager = standardCommentsManager;
    }

    private MinMaxDataElementService minMaxDataElementService;

    public void setMinMaxDataElementService( MinMaxDataElementService minMaxDataElementService )
    {
        this.minMaxDataElementService = minMaxDataElementService;
    }

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    private DataEntryScreenManager dataEntryScreenManager;

    public void setDataEntryScreenManager( DataEntryScreenManager dataEntryScreenManager )
    {
        this.dataEntryScreenManager = dataEntryScreenManager;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // DisplayPropertyHandler
    // -------------------------------------------------------------------------

    private DisplayPropertyHandler displayPropertyHandler;

    public void setDisplayPropertyHandler( DisplayPropertyHandler displayPropertyHandler )
    {
        this.displayPropertyHandler = displayPropertyHandler;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<DataElement> orderedDataElements = new ArrayList<DataElement>();

    public List<DataElement> getOrderedDataElements()
    {
        return orderedDataElements;
    }

    private Map<Integer, DataValue> dataValueMap;

    public Map<Integer, DataValue> getDataValueMap()
    {
        return dataValueMap;
    }

    private Map<CalculatedDataElement, Integer> calculatedValueMap;

    public Map<CalculatedDataElement, Integer> getCalculatedValueMap()
    {
        return calculatedValueMap;
    }

    private List<String> standardComments;

    public List<String> getStandardComments()
    {
        return standardComments;
    }

    private Map<String, String> dataElementTypeMap;

    public Map<String, String> getDataElementTypeMap()
    {
        return dataElementTypeMap;
    }

    private Map<Integer, MinMaxDataElement> minMaxMap;

    public Map<Integer, MinMaxDataElement> getMinMaxMap()
    {
        return minMaxMap;
    }

    private Integer integer = 0;

    public Integer getInteger()
    {
        return integer;
    }

    private Boolean cdeFormExists;

    private DataEntryForm dataEntryForm;

    public DataEntryForm getDataEntryForm()
    {
        return this.dataEntryForm;
    }

    private String customDataEntryFormCode;

    public String getCustomDataEntryFormCode()
    {
        return this.customDataEntryFormCode;
    }

    private Boolean zeroValueSaveMode;

    public Boolean getZeroValueSaveMode()
    {
        return zeroValueSaveMode;
    }

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private List<CustomValue> customValues = new ArrayList<CustomValue>();

    public List<CustomValue> getCustomValues()
    {
        return customValues;
    }

    private Integer selectedDataSetId;

    public void setSelectedDataSetId( Integer selectedDataSetId )
    {
        this.selectedDataSetId = selectedDataSetId;
    }

    public Integer getSelectedDataSetId()
    {
        return selectedDataSetId;
    }

    private Integer selectedPeriodIndex;

    public void setSelectedPeriodIndex( Integer selectedPeriodIndex )
    {
        this.selectedPeriodIndex = selectedPeriodIndex;
    }

    public Integer getSelectedPeriodIndex()
    {
        return selectedPeriodIndex;
    }

    private String disabled = " ";

    private Integer optionComboId;

    public Integer getOptionComboId()
    {
        return optionComboId;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        zeroValueSaveMode = (Boolean) systemSettingManager.getSystemSetting( KEY_ZERO_VALUE_SAVE_MODE, false );

        if ( zeroValueSaveMode == null )
        {
            zeroValueSaveMode = false;
        }

        OrganisationUnit organisationUnit = selectedStateManager.getSelectedOrganisationUnit();

        DataSet dataSet = selectedStateManager.getSelectedDataSet();

        customValues = (List<CustomValue>) customValueService.getCustomValuesByDataSet( dataSet );

        Period period = selectedStateManager.getSelectedPeriod();

        if ( dataSet.getLockedPeriods().contains( period ) )
        {
            disabled = "disabled";
        }

        Collection<DataElement> dataElements = dataSet.getDataElements();

        if ( dataElements.size() == 0 )
        {
            return SUCCESS;
        }

        Collection<DataElementCategoryOptionCombo> defaultOptionCombo = dataElements.iterator().next()
            .getCategoryCombo().getOptionCombos();

        optionComboId = defaultOptionCombo.iterator().next().getId();

        // ---------------------------------------------------------------------
        // Get the min/max values
        // ---------------------------------------------------------------------

        Collection<MinMaxDataElement> minMaxDataElements = minMaxDataElementService.getMinMaxDataElements(
            organisationUnit, dataElements );

        minMaxMap = new HashMap<Integer, MinMaxDataElement>( minMaxDataElements.size() );

        for ( MinMaxDataElement minMaxDataElement : minMaxDataElements )
        {
            minMaxMap.put( minMaxDataElement.getDataElement().getId(), minMaxDataElement );
        }

        // ---------------------------------------------------------------------
        // Get the DataValues and create a map
        // ---------------------------------------------------------------------

        Collection<DataValue> dataValues = dataValueService.getDataValues( organisationUnit, period, dataElements,
            defaultOptionCombo );

        dataValueMap = new HashMap<Integer, DataValue>( dataValues.size() );

        for ( DataValue dataValue : dataValues )
        {
            dataValueMap.put( dataValue.getDataElement().getId(), dataValue );
        }

        // ---------------------------------------------------------------------
        // Prepare values for unsaved CalculatedDataElements
        // ---------------------------------------------------------------------

        calculatedValueMap = dataEntryScreenManager.populateValuesForCalculatedDataElements( organisationUnit, dataSet,
            period );

        // ---------------------------------------------------------------------
        // Make the standard comments available
        // ---------------------------------------------------------------------

        standardComments = standardCommentsManager.getStandardComments();

        // ---------------------------------------------------------------------
        // Make the DataElement types available
        // ---------------------------------------------------------------------

        dataElementTypeMap = new HashMap<String, String>();
        dataElementTypeMap.put( DataElement.TYPE_BOOL, i18n.getString( "yes_no" ) );
        dataElementTypeMap.put( DataElement.TYPE_INT, i18n.getString( "number" ) );
        dataElementTypeMap.put( DataElement.TYPE_STRING, i18n.getString( "text" ) );

        // ---------------------------------------------------------------------
        // Get the custom data entry form (if any)
        // ---------------------------------------------------------------------

        dataEntryForm = dataEntryFormService.getDataEntryFormByDataSet( dataSet );
        cdeFormExists = (dataEntryForm != null);

        if ( cdeFormExists )
        {
            customDataEntryFormCode = dataEntryScreenManager.populateCustomDataEntryScreen(
                dataEntryForm.getHtmlCode(), dataValues, calculatedValueMap, minMaxMap, disabled, zeroValueSaveMode,
                i18n, dataSet );
        }

        // ---------------------------------------------------------------------
        // Working on the display of dataelements
        // ---------------------------------------------------------------------

        orderedDataElements = dataElementOrderManager.getOrderedDataElements( dataSet );

        displayPropertyHandler.handle( orderedDataElements );

        return SUCCESS;
    }
}
