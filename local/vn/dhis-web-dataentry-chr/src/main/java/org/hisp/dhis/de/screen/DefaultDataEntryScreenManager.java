package org.hisp.dhis.de.screen;

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

import static org.hisp.dhis.expression.Expression.SEPARATOR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.customvalue.CustomValue;
import org.hisp.dhis.customvalue.CustomValueService;
import org.hisp.dhis.dataelement.CalculatedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.dataset.SectionService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class DefaultDataEntryScreenManager
    implements DataEntryScreenManager
{
    private static final String DEFAULT_FORM = "defaultform";

    private static final String MULTI_DIMENSIONAL_FORM = "multidimensionalform";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CustomValueService customValueService;

    public void setCustomValueService( CustomValueService customValueService )
    {
        this.customValueService = customValueService;
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

    public void setDataElementCategoryOptionComboService(
        DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }

    private SectionService sectionService;

    public void setSectionService( SectionService sectionService )
    {
        this.sectionService = sectionService;
    }

    // -------------------------------------------------------------------------
    // DataEntryScreenManager implementation
    // -------------------------------------------------------------------------

    public boolean hasMixOfDimensions( DataSet dataSet )
    {
        if ( dataSet.getDataElements().size() > 0 )
        {
            Iterator<DataElement> dataElementIterator = dataSet.getDataElements().iterator();

            DataElementCategoryCombo catCombo = dataElementIterator.next().getCategoryCombo();

            for ( DataElement de : dataSet.getDataElements() )
            {
                if ( catCombo != de.getCategoryCombo() )
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasMultiDimensionalDataElement( DataSet dataSet )
    {
        int numberOfTotalColumns = 1;

        if ( dataSet.getDataElements().size() > 0 )
        {
            for ( DataElement de : dataSet.getDataElements() )
            {
                for ( DataElementCategory category : de.getCategoryCombo().getCategories() )
                {
                    numberOfTotalColumns = numberOfTotalColumns * category.getCategoryOptions().size();
                }

                if ( numberOfTotalColumns > 1 )
                {
                    return true;
                }
            }
        }

        return false;
    }

    public String getScreenType( DataSet dataSet )
    {
        int numberOfTotalColumns = 1;

        if ( dataSet.getDataElements().size() > 0 )
        {
            for ( DataElement de : dataSet.getDataElements() )
            {
                for ( DataElementCategory category : de.getCategoryCombo().getCategories() )
                {
                    numberOfTotalColumns = numberOfTotalColumns * category.getCategoryOptions().size();
                }

                if ( numberOfTotalColumns > 1 )
                {
                    return MULTI_DIMENSIONAL_FORM;
                }
            }
        }

        return DEFAULT_FORM;
    }

    public Collection<Integer> getAllCalculatedDataElements( DataSet dataSet )
    {
        Collection<Integer> calculatedDataElementIds = new HashSet<Integer>();

        CalculatedDataElement cde;

        for ( DataElement dataElement : dataSet.getDataElements() )
        {
            if ( dataElement instanceof CalculatedDataElement )
            {
                cde = (CalculatedDataElement) dataElement;

                calculatedDataElementIds.add( cde.getId() );

            }
        }

        return calculatedDataElementIds;
    }

    public Map<CalculatedDataElement, Map<DataElement, Integer>> getNonSavedCalculatedDataElements( DataSet dataSet )
    {
        Map<CalculatedDataElement, Map<DataElement, Integer>> calculatedDataElementMap = new HashMap<CalculatedDataElement, Map<DataElement, Integer>>();

        CalculatedDataElement cde;

        for ( DataElement dataElement : dataSet.getDataElements() )
        {
            if ( dataElement instanceof CalculatedDataElement )
            {
                cde = (CalculatedDataElement) dataElement;

                if ( cde.isSaved() )
                {
                    continue;
                }

                calculatedDataElementMap.put( cde, dataElementService.getDataElementFactors( cde ) );
            }
        }

        return calculatedDataElementMap;
    }

    public boolean hasSection( DataSet dataSet )
    {
        List<Section> sections = (List<Section>) sectionService.getSectionByDataSet( dataSet );

        return sections.size() != 0;

    }

    public Map<CalculatedDataElement, Integer> populateValuesForCalculatedDataElements(
        OrganisationUnit organisationUnit, DataSet dataSet, Period period )
    {
        Map<CalculatedDataElement, Integer> calculatedValueMap = new HashMap<CalculatedDataElement, Integer>();

        CalculatedDataElement cde;

        Map<String, Integer> factorMap;

        DataValue dataValue;
        int factor;
        int value = 0;

        Collection<String> operandIds = new ArrayList<String>();

        for ( DataElement dataElement : dataSet.getDataElements() )
        {
            if ( !(dataElement instanceof CalculatedDataElement) )
            {
                continue;
            }

            cde = (CalculatedDataElement) dataElement;

            if ( cde.isSaved() )
            {
                continue;
            }

            factorMap = dataElementService.getOperandFactors( cde );

            operandIds = dataElementService.getOperandIds( cde );

            for ( String operandId : operandIds )
            {
                factor = factorMap.get( operandId );

                String dataElementIdString = operandId.substring( 0, operandId.indexOf( SEPARATOR ) );
                String optionComboIdString = operandId.substring( operandId.indexOf( SEPARATOR ) + 1, operandId
                    .length() );

                DataElement de = dataElementService.getDataElement( Integer.parseInt( dataElementIdString ) );
                DataElementCategoryOptionCombo optionCombo = dataElementCategoryOptionComboService
                    .getDataElementCategoryOptionCombo( Integer.parseInt( optionComboIdString ) );

                dataValue = dataValueService.getDataValue( organisationUnit, de, period, optionCombo );

                if ( dataValue != null )
                {
                    value += Integer.parseInt( dataValue.getValue() ) * factor;
                }
            }

            calculatedValueMap.put( cde, value );

            value = 0;
        }

        return calculatedValueMap;
    }

    public String populateCustomDataEntryScreen( String dataEntryFormCode, Collection<DataValue> dataValues,
        Map<CalculatedDataElement, Integer> calculatedValueMap, Map<Integer, MinMaxDataElement> minMaxMap,
        String disabled, Boolean saveMode, I18n i18n, DataSet dataSet )
    {
        // ---------------------------------------------------------------------
        // Populating Custom Value data.
        // ---------------------------------------------------------------------

        List<CustomValue> customValues = (List<CustomValue>) customValueService.getCustomValuesByDataSet( dataSet );

        // ---------------------------------------------------------------------
        // Inline Javascript to add to HTML before outputting.
        // ---------------------------------------------------------------------

        final String jsCodeForInputs = " $DISABLED onchange=\"saveValue( $DATAELEMENTID, $OPTIONCOMBOID, '$DATAELEMENTNAME', $SAVEMODE )\" onkeypress=\"return keyPress(event, this)\" style=\"text-align:center\" ";
        final String jsCodeForCombos = " $DISABLED onchange=\"saveBoolean( $DATAELEMENTID, $OPTIONCOMBOID, this )\">";
        final String historyCode = " ondblclick='javascript:viewHistory( $DATAELEMENTID, $OPTIONCOMBOID, true )' ";
        final String calDataElementCode = " class=\"calculated\" disabled ";

        // ---------------------------------------------------------------------
        // Metadata code to add to HTML before outputting.
        // ---------------------------------------------------------------------

        final String metaDataCode = "<span id=\"value[$DATAELEMENTID].name\" style=\"display:none\">$DATAELEMENTNAME</span>"
            + "<span id=\"value[$DATAELEMENTID].type\" style=\"display:none\">$DATAELEMENTTYPE</span>"
            + "<div id=\"value[$DATAELEMENTID:$OPTIONCOMBOID].min\" style=\"display:none\">$MIN</div>"
            + "<div id=\"value[$DATAELEMENTID:$OPTIONCOMBOID].max\" style=\"display:none\">$MAX</div>";

        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match data elements in the HTML code.
        // ---------------------------------------------------------------------

        Pattern patDataElement = Pattern.compile( "(<input.*?)[/]?>", Pattern.DOTALL );
        Matcher matDataElement = patDataElement.matcher( dataEntryFormCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching data element fields.
        // ---------------------------------------------------------------------

        boolean result = matDataElement.find();

        while ( result )
        {
            // Get input HTML code (HTML input field code).
            String dataElementCode = matDataElement.group( 1 );

            // Pattern to extract data element ID from data element field
            Pattern patDataElementId = Pattern.compile( "value\\[(.*)\\].value:value\\[(.*)\\].value" );

            Matcher matDataElementId = patDataElementId.matcher( dataElementCode );

            if ( matDataElementId.find() && matDataElementId.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get data element ID of data element.
                // -------------------------------------------------------------

                int dataElementId = Integer.parseInt( matDataElementId.group( 1 ) );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );

                int optionComboId = Integer.parseInt( matDataElementId.group( 2 ) );

                // -------------------------------------------------------------
                // Find type of data element
                // -------------------------------------------------------------

                String dataElementType = dataElement.getType();

                // -------------------------------------------------------------
                // Find existing value of data element in data set.
                // -------------------------------------------------------------

                String dataElementValue = "";

                if ( (dataElement instanceof CalculatedDataElement) )
                {
                    CalculatedDataElement cde = (CalculatedDataElement) dataElement;

                    if ( cde.isSaved() )
                    {
                        for ( DataValue dv : dataValues )
                        {
                            if ( dv.getDataElement().getId() == dataElementId )
                            {
                                dataElementValue = dv.getValue();
                                break;
                            }
                        }
                    }
                    else
                    {
                        dataElementValue = String.valueOf( calculatedValueMap.get( cde ) );
                    }
                }
                else
                {
                    for ( DataValue dv : dataValues )
                    {
                        if ( dv.getDataElement().getId() == dataElementId )
                        {
                            dataElementValue = dv.getValue();
                            break;
                        }
                    }
                }

                // -------------------------------------------------------------
                // Insert value of data element in output code.
                // -------------------------------------------------------------

                int count = 0;
                for ( CustomValue customValue : customValues )
                {
                    if ( dataElementId == customValue.getDataElement().getId()
                        && optionComboId == customValue.getOptionCombo().getId() )
                    {
                        count += 1;
                    }
                }

                if ( dataElement.getType().equals( "bool" ) || (dataElement.getType().equals( "string" ) && count > 0) )
                {
                    dataElementCode = dataElementCode.replace( "input", "select" );
                    dataElementCode = dataElementCode.replaceAll( "value=\".*?\"", "" );
                    dataElementCode = dataElementCode.replace( "value\\[" + dataElementId + "\\].value:value\\["
                        + optionComboId + "\\].value", "value\\[" + dataElementId + "]" );
                    dataElementCode = dataElementCode.replaceAll( "view=\".*?\"", "" );
                }
                else
                {
                    if ( dataElementCode.contains( "value=\"\"" ) )
                    {
                        dataElementCode = dataElementCode.replace( "value=\"\"", "value=\"" + dataElementValue + "\"" );
                    }
                    else
                    {
                        dataElementCode += "value=\"" + dataElementValue + "\"";
                    }
                }

                // -------------------------------------------------------------
                // MIN-MAX Values
                // -------------------------------------------------------------

                MinMaxDataElement minMaxDataElement = minMaxMap.get( dataElement.getId() );
                String minValue = "No Min";
                String maxValue = "No Max";

                if ( minMaxDataElement != null )
                {
                    minValue = String.valueOf( minMaxDataElement.getMin() );
                    maxValue = String.valueOf( minMaxDataElement.getMax() );
                }

                // -------------------------------------------------------------
                // Remove placeholder view attribute from input field.
                // -------------------------------------------------------------

                dataElementCode = dataElementCode.replaceAll( "view=\".*?\"", "" );

                // -------------------------------------------------------------
                // Insert Title Information - DataElement id,name,type,min,max
                // -------------------------------------------------------------

                if ( dataElementCode.contains( "title=\"\"" ) )
                {
                    dataElementCode = dataElementCode.replace( "title=\"\"", "title=\"-- ID:" + dataElement.getId()
                        + " Name:" + dataElement.getShortName() + " Type:" + dataElement.getType() + " Min:" + minValue
                        + " Max:" + maxValue + " --\"" );
                }
                else
                {
                    dataElementCode += "title=\"-- ID:" + dataElement.getId() + " Name:" + dataElement.getShortName()
                        + " Type:" + dataElement.getType() + " Min:" + minValue + " Max:" + maxValue + " --\"";
                }

                // -------------------------------------------------------------
                // Append Javascript code and meta data (type/min/max) for
                // persisting to output code, and insert value and type for
                // fields.
                // -------------------------------------------------------------

                String appendCode = dataElementCode;

                if ( dataElement.getType().equalsIgnoreCase( "bool" ) )
                {
                    appendCode += jsCodeForCombos;

                    appendCode += "<option value=\"\">" + i18n.getString( "no_value" ) + "</option>";

                    if ( dataElementValue.equalsIgnoreCase( "true" ) )
                    {
                        appendCode += "<option value=\"true\" selected>" + i18n.getString( "yes" ) + "</option>";
                    }
                    else
                    {
                        appendCode += "<option value=\"true\">" + i18n.getString( "yes" ) + "</option>";
                    }

                    if ( dataElementValue.equalsIgnoreCase( "false" ) )
                    {
                        appendCode += "<option value=\"false\" selected>" + i18n.getString( "no" ) + "</option>";
                    }
                    else
                    {
                        appendCode += "<option value=\"false\">" + i18n.getString( "no" ) + "</option>";
                    }

                    appendCode += "</select>";
                }
                else if ( dataElement.getType().equalsIgnoreCase( "string" ) && count > 0 )
                {
                    appendCode += jsCodeForCombos;

                    appendCode += "<option value=\"\"></option>";

                    for ( CustomValue customValue : customValues )
                    {
                        if ( dataElementId == customValue.getDataElement().getId()
                            && optionComboId == customValue.getOptionCombo().getId() )
                        {
                            if ( dataElementValue.equalsIgnoreCase( customValue.getCustomValue() ) )
                            {
                                appendCode += "<option value=\"" + customValue.getCustomValue() + "\" selected >"
                                    + customValue.getCustomValue() + "</option>";
                            }
                            else
                            {
                                appendCode += "<option value=\"" + customValue.getCustomValue() + "\">"
                                    + customValue.getCustomValue() + "</option>";
                            }
                        }
                    }

                    appendCode += "</select>";
                }
                else
                {
                    appendCode += jsCodeForInputs;

                    if ( dataElement.getType().equalsIgnoreCase( "int" ) )
                    {
                        appendCode += historyCode;
                    }

                    if ( (dataElement instanceof CalculatedDataElement) )
                    {
                        appendCode += calDataElementCode;
                    }

                    appendCode += " />";
                }

                if ( !dataElement.getAggregationOperator().equalsIgnoreCase( DataElement.AGGREGATION_OPERATOR_SUM ) )
                {
                    saveMode = true;
                }

                appendCode += metaDataCode;
                appendCode = appendCode.replace( "$DATAELEMENTID", String.valueOf( dataElementId ) );
                appendCode = appendCode.replace( "$OPTIONCOMBOID", String.valueOf( optionComboId ) );
                appendCode = appendCode.replace( "$DATAELEMENTNAME", dataElement.getName() );
                appendCode = appendCode.replace( "$DATAELEMENTTYPE", dataElementType );
                appendCode = appendCode.replace( "$SAVEMODE", "" + saveMode + "" );
                appendCode = appendCode.replace( "$DISABLED", disabled );

                if ( minMaxDataElement == null )
                {
                    appendCode = appendCode.replace( "$MIN", minValue );
                    appendCode = appendCode.replace( "$MAX", maxValue );
                }
                else
                {
                    appendCode = appendCode.replace( "$MIN", String.valueOf( minMaxDataElement.getMin() ) );
                    appendCode = appendCode.replace( "$MAX", String.valueOf( minMaxDataElement.getMax() ) );
                }

                matDataElement.appendReplacement( sb, appendCode );
            }

            // Go to next data entry field
            result = matDataElement.find();
        }

        // Add remaining code (after the last match), and return formatted code.
        matDataElement.appendTail( sb );

        return sb.toString();

    }

    public String populateCustomDataEntryScreenForMultiDimensional( String dataEntryFormCode,
        Collection<DataValue> dataValues, Map<CalculatedDataElement, Integer> calculatedValueMap,
        Map<String, MinMaxDataElement> minMaxMap, String disabled, Boolean saveMode, I18n i18n, DataSet dataSet )
    {
        // ---------------------------------------------------------------------
        // Populating Custom Value data.
        // ---------------------------------------------------------------------

        List<CustomValue> customValues = (List<CustomValue>) customValueService.getCustomValuesByDataSet( dataSet );

        // ---------------------------------------------------------------------
        // Inline Javascript to add to HTML before outputting.
        // ---------------------------------------------------------------------

        final String jsCodeForInputs = " $DISABLED onchange=\"saveValue( $DATAELEMENTID, $OPTIONCOMBOID, '$DATAELEMENTNAME', $SAVEMODE )\" onkeypress=\"return keyPress(event, this)\" style=\"text-align:center\" ";
        final String jsCodeForCombos = " $DISABLED onchange=\"saveBoolean( $DATAELEMENTID, $OPTIONCOMBOID, this )\">";
        final String historyCode = " ondblclick='javascript:viewHistory( $DATAELEMENTID, $OPTIONCOMBOID, true )' ";
        final String calDataElementCode = " class=\"calculated\" disabled ";

        // ---------------------------------------------------------------------
        // Metadata code to add to HTML before outputting.
        // ---------------------------------------------------------------------

        final String metaDataCode = "<span id=\"value[$DATAELEMENTID].name\" style=\"display:none\">$DATAELEMENTNAME</span>"
            + "<span id=\"value[$DATAELEMENTID].type\" style=\"display:none\">$DATAELEMENTTYPE</span>"
            + "<div id=\"value[$DATAELEMENTID:$OPTIONCOMBOID].min\" style=\"display:none\">$MIN</div>"
            + "<div id=\"value[$DATAELEMENTID:$OPTIONCOMBOID].max\" style=\"display:none\">$MAX</div>";

        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match data elements in the HTML code.
        // ---------------------------------------------------------------------

        Pattern patDataElement = Pattern.compile( "(<input.*?)[/]?>", Pattern.DOTALL );
        Matcher matDataElement = patDataElement.matcher( dataEntryFormCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching data element fields.
        // ---------------------------------------------------------------------

        boolean result = matDataElement.find();

        while ( result )
        {
            // Get input HTML code (HTML input field code).
            String dataElementCode = matDataElement.group( 1 );

            // Pattern to extract data element ID from data element field
            Pattern patDataElementId = Pattern.compile( "value\\[(.*)\\].value:value\\[(.*)\\].value" );

            Matcher matDataElementId = patDataElementId.matcher( dataElementCode );

            if ( matDataElementId.find() && matDataElementId.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get data element ID of data element.
                // -------------------------------------------------------------

                int dataElementId = Integer.parseInt( matDataElementId.group( 1 ) );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );
                int optionComboId = Integer.parseInt( matDataElementId.group( 2 ) );

                // -------------------------------------------------------------
                // Find type of data element
                // -------------------------------------------------------------

                String dataElementType = dataElement.getType();

                // -------------------------------------------------------------
                // Find existing value of data element in data set.
                // -------------------------------------------------------------

                String dataElementValue = "";

                if ( (dataElement instanceof CalculatedDataElement) )
                {
                    CalculatedDataElement cde = (CalculatedDataElement) dataElement;

                    if ( cde.isSaved() )
                    {
                        for ( DataValue dv : dataValues )
                        {
                            if ( dv.getDataElement().getId() == dataElementId
                                && dv.getOptionCombo().getId() == optionComboId )
                            {
                                dataElementValue = dv.getValue();
                                break;
                            }
                        }
                    }
                    else
                    {
                        dataElementValue = String.valueOf( calculatedValueMap.get( cde ) );
                    }
                }
                else
                {
                    for ( DataValue dv : dataValues )
                    {
                        if ( dv.getDataElement().getId() == dataElementId
                            && dv.getOptionCombo().getId() == optionComboId )
                        {
                            dataElementValue = dv.getValue();
                            break;
                        }
                    }
                }

                // -------------------------------------------------------------
                // Insert value of data element in output code.
                // -------------------------------------------------------------

                int count = 0;
                for ( CustomValue customValue : customValues )
                {
                    if ( dataElementId == customValue.getDataElement().getId()
                        && optionComboId == customValue.getOptionCombo().getId() )
                    {
                        count += 1;
                    }
                }

                if ( dataElement.getType().equals( "bool" ) || (dataElement.getType().equals( "string" ) && count > 0) )
                {
                    dataElementCode = dataElementCode.replace( "input", "select" );
                    dataElementCode = dataElementCode.replaceAll( "value=\".*?\"", "" );
                    dataElementCode = dataElementCode.replaceAll( "view=\".*?\"", "" );
                }
                else
                {
                    if ( dataElementCode.contains( "value=\"\"" ) )
                    {
                        dataElementCode = dataElementCode.replace( "value=\"\"", "value=\"" + dataElementValue + "\"" );
                    }
                    else
                    {
                        dataElementCode += "value=\"" + dataElementValue + "\"";
                    }
                }

                // -------------------------------------------------------------
                // MIN-MAX Values
                // -------------------------------------------------------------

                MinMaxDataElement minMaxDataElement = minMaxMap.get( dataElement.getId() + ":" + optionComboId );
                String minValue = "No Min";
                String maxValue = "No Max";

                if ( minMaxDataElement != null )
                {
                    minValue = String.valueOf( minMaxDataElement.getMin() );
                    maxValue = String.valueOf( minMaxDataElement.getMax() );
                }

                // -------------------------------------------------------------
                // Remove placeholder view attribute from input field.
                // -------------------------------------------------------------

                dataElementCode = dataElementCode.replaceAll( "view=\".*?\"", "" );

                // -------------------------------------------------------------
                // Insert Title Information - DataElement id,name,type,min,max
                // -------------------------------------------------------------

                if ( dataElementCode.contains( "title=\"\"" ) )
                {
                    dataElementCode = dataElementCode.replace( "title=\"\"", "title=\"-- ID:" + dataElement.getId()
                        + " Name:" + dataElement.getShortName() + " Type:" + dataElement.getType() + " Min:" + minValue
                        + " Max:" + maxValue + " --\"" );
                }
                else
                {
                    dataElementCode += "title=\"-- ID:" + dataElement.getId() + " Name:" + dataElement.getShortName()
                        + " Type:" + dataElement.getType() + " Min:" + minValue + " Max:" + maxValue + " --\"";
                }

                // -------------------------------------------------------------
                // Append Javascript code and meta data (type/min/max) for
                // persisting to output code, and insert value and type for
                // fields.
                // -------------------------------------------------------------

                String appendCode = dataElementCode;

                if ( dataElement.getType().equalsIgnoreCase( "bool" ) )
                {
                    appendCode += jsCodeForCombos;

                    appendCode += "<option value=\"\">" + i18n.getString( "no_value" ) + "</option>";

                    if ( dataElementValue.equalsIgnoreCase( "true" ) )
                    {
                        appendCode += "<option value=\"true\" selected>" + i18n.getString( "yes" ) + "</option>";
                    }
                    else
                    {
                        appendCode += "<option value=\"true\">" + i18n.getString( "yes" ) + "</option>";
                    }

                    if ( dataElementValue.equalsIgnoreCase( "false" ) )
                    {
                        appendCode += "<option value=\"false\" selected>" + i18n.getString( "no" ) + "</option>";
                    }
                    else
                    {
                        appendCode += "<option value=\"false\">" + i18n.getString( "no" ) + "</option>";
                    }

                    appendCode += "</select>";
                }
                else if ( dataElement.getType().equalsIgnoreCase( "string" ) && count > 0 )
                {
                    appendCode += jsCodeForCombos;

                    appendCode += "<option value=\"\"></option>";

                    for ( CustomValue customValue : customValues )
                    {
                        if ( dataElementId == customValue.getDataElement().getId()
                            && optionComboId == customValue.getOptionCombo().getId() )
                        {
                            if ( dataElementValue.equalsIgnoreCase( customValue.getCustomValue() ) )
                            {
                                appendCode += "<option value=\"" + customValue.getCustomValue() + "\" selected >"
                                    + customValue.getCustomValue() + "</option>";
                            }
                            else
                            {
                                appendCode += "<option value=\"" + customValue.getCustomValue() + "\">"
                                    + customValue.getCustomValue() + "</option>";
                            }
                        }
                    }

                    appendCode += "</select>";
                }
                else
                {
                    appendCode += jsCodeForInputs;

                    if ( dataElement.getType().equalsIgnoreCase( "int" ) )
                    {
                        appendCode += historyCode;
                    }

                    if ( (dataElement instanceof CalculatedDataElement) )
                    {
                        appendCode += calDataElementCode;
                    }

                    appendCode += " />";
                }

                if ( !dataElement.getAggregationOperator().equalsIgnoreCase( DataElement.AGGREGATION_OPERATOR_SUM ) )
                {
                    saveMode = true;
                }

                appendCode += metaDataCode;
                appendCode = appendCode.replace( "$DATAELEMENTID", String.valueOf( dataElementId ) );
                appendCode = appendCode.replace( "$DATAELEMENTNAME", dataElement.getName() );
                appendCode = appendCode.replace( "$DATAELEMENTTYPE", dataElementType );
                appendCode = appendCode.replace( "$OPTIONCOMBOID", String.valueOf( optionComboId ) );
                appendCode = appendCode.replace( "$SAVEMODE", "" + saveMode + "" );
                appendCode = appendCode.replace( "$DISABLED", disabled );

                if ( minMaxDataElement == null )
                {
                    appendCode = appendCode.replace( "$MIN", minValue );
                    appendCode = appendCode.replace( "$MAX", maxValue );
                }
                else
                {
                    appendCode = appendCode.replace( "$MIN", String.valueOf( minMaxDataElement.getMin() ) );
                    appendCode = appendCode.replace( "$MAX", String.valueOf( minMaxDataElement.getMax() ) );
                }

                matDataElement.appendReplacement( sb, appendCode );
            }

            // Go to next data entry field
            result = matDataElement.find();
        }

        // Add remaining code (after the last match), and return formatted code.
        matDataElement.appendTail( sb );

        return sb.toString();
    }
}
