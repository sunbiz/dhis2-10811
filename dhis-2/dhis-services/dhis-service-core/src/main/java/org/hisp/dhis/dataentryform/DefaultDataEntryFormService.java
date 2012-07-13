package org.hisp.dhis.dataentryform;

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

import static org.hisp.dhis.dataelement.DataElement.VALUE_TYPE_BOOL;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.system.util.Filter;
import org.hisp.dhis.system.util.FilterUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Bharath Kumar
 * @version $Id$
 */
@Transactional
public class DefaultDataEntryFormService
    implements DataEntryFormService
{
    private static final String EMPTY_VALUE_TAG = "value=\"\"";
    private static final String EMPTY_TITLE_TAG = "title=\"\"";
    private static final String TAG_CLOSE = "/>";
    private static final String EMPTY = "";

    // ------------------------------------------------------------------------
    // Dependencies
    // ------------------------------------------------------------------------

    private DataEntryFormStore dataEntryFormStore;

    public void setDataEntryFormStore( DataEntryFormStore dataEntryFormStore )
    {
        this.dataEntryFormStore = dataEntryFormStore;
    }

    private DataElementCategoryService categoryService;

    public void setCategoryService( DataElementCategoryService categoryService )
    {
        this.categoryService = categoryService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    // ------------------------------------------------------------------------
    // Implemented Methods
    // ------------------------------------------------------------------------

    public int addDataEntryForm( DataEntryForm dataEntryForm )
    {
        return dataEntryFormStore.addDataEntryForm( dataEntryForm );
    }

    public void updateDataEntryForm( DataEntryForm dataEntryForm )
    {
        dataEntryFormStore.updateDataEntryForm( dataEntryForm );
    }

    public void deleteDataEntryForm( DataEntryForm dataEntryForm )
    {
        dataEntryFormStore.deleteDataEntryForm( dataEntryForm );
    }

    public DataEntryForm getDataEntryForm( int id )
    {
        return dataEntryFormStore.getDataEntryForm( id );
    }

    public DataEntryForm getDataEntryFormByName( String name )
    {
        return dataEntryFormStore.getDataEntryFormByName( name );
    }

    public Collection<DataEntryForm> getAllDataEntryForms()
    {
        return dataEntryFormStore.getAllDataEntryForms();
    }

    public String prepareDataEntryFormForSave( String htmlCode )
    {
        StringBuffer sb = new StringBuffer();

        Matcher inputMatcher = INPUT_PATTERN.matcher( htmlCode );

        while ( inputMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Remove value and title tags from the HTML code
            // -----------------------------------------------------------------

            String dataElementCode = inputMatcher.group();

            Matcher valueTagMatcher = VALUE_TAG_PATTERN.matcher( dataElementCode );
            Matcher titleTagMatcher = TITLE_TAG_PATTERN.matcher( dataElementCode );

            if ( valueTagMatcher.find() && valueTagMatcher.groupCount() > 0 )
            {
                dataElementCode = dataElementCode.replace( valueTagMatcher.group( 1 ), EMPTY );
            }

            if ( titleTagMatcher.find() && valueTagMatcher.groupCount() > 0 )
            {
                dataElementCode = dataElementCode.replace( titleTagMatcher.group( 1 ), EMPTY );
            }

            inputMatcher.appendReplacement( sb, dataElementCode );
        }

        inputMatcher.appendTail( sb );

        return sb.toString();
    }

    public String prepareDataEntryFormForEdit( String htmlCode, I18n i18n )
    {
        StringBuffer sb = new StringBuffer();

        Matcher inputMatcher = INPUT_PATTERN.matcher( htmlCode );

        while ( inputMatcher.find() )
        {
            String inputHtml = inputMatcher.group();

            Matcher identifierMatcher = IDENTIFIER_PATTERN.matcher( inputHtml );
            Matcher dataElementTotalMatcher = DATAELEMENT_TOTAL_PATTERN.matcher( inputHtml );
            Matcher indicatorMatcher = INDICATOR_PATTERN.matcher( inputHtml );
            
            String displayValue = null;
            String displayTitle = null;

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                int dataElementId = Integer.parseInt( identifierMatcher.group( 1 ) );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );

                int optionComboId = Integer.parseInt( identifierMatcher.group( 2 ) );
                DataElementCategoryOptionCombo categegoryOptionCombo = categoryService.getDataElementCategoryOptionCombo( optionComboId );
                String optionComboName = categegoryOptionCombo != null ? categegoryOptionCombo.getName() : "[ " + i18n.getString( "cate_option_combo_not_exist" ) + " ]";

                StringBuilder title = dataElement != null ? 
                    new StringBuilder( "title=\"" ).append( dataElement.getId() ).append( " - " ).
                    append( dataElement.getDisplayName() ).append( " - " ).append( optionComboId ).append( " - " ).
                    append( optionComboName ).append( " - " ).append( dataElement.getType() ).append( "\"" ) : new StringBuilder();

                displayValue = dataElement != null ? "value=\"[ " + dataElement.getDisplayName() + " " + optionComboName + " ]\"" : "[ " + i18n.getString( "dataelement_not_exist" ) + " ]";
                displayTitle = dataElement != null ? title.toString() : "[ " + i18n.getString( "dataelement_not_exist" ) + " ]";
            }
            else if ( dataElementTotalMatcher.find() && dataElementTotalMatcher.groupCount() > 0 )
            {
                int dataElementId = Integer.parseInt( dataElementTotalMatcher.group( 1 ) );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );

                displayValue = dataElement != null ? "value=\"[ " + dataElement.getDisplayName() + " ]\"" : "[ " + i18n.getString( "dataelement_not_exist" ) + " ]";
                displayTitle = dataElement != null ? "title=\"" + dataElement.getDisplayName() + "\"" : "[ " + i18n.getString( "dataelement_not_exist" ) + " ]";
            }
            else if ( indicatorMatcher.find() && indicatorMatcher.groupCount() > 0 )
            {
                int indicatorId = Integer.parseInt( indicatorMatcher.group( 1 ) );
                Indicator indicator = indicatorService.getIndicator( indicatorId );

                displayValue = indicator != null ? "value=\"[ " + indicator.getDisplayName() + " ]\"" : "[ " + i18n.getString( "indicator_not_exist" ) + " ]";
                displayTitle = indicator != null ? "title=\"" + indicator.getDisplayName() + "\"" : "[ " + i18n.getString( "indicator_not_exist" ) + " ]";
            }            

            // -----------------------------------------------------------------
            // Insert name of data element operand as value and title
            // -----------------------------------------------------------------

            inputHtml = inputHtml.contains( EMPTY_VALUE_TAG ) ? inputHtml.replace( EMPTY_VALUE_TAG, displayValue ) : inputHtml + " " + displayValue;
            inputHtml = inputHtml.contains( EMPTY_TITLE_TAG ) ? inputHtml.replace( EMPTY_TITLE_TAG, displayTitle ) : " " + displayTitle;

            inputMatcher.appendReplacement( sb, inputHtml );
        }

        inputMatcher.appendTail( sb );

        return sb.toString();
    }

    public String prepareDataEntryFormForEntry( String htmlCode, I18n i18n, DataSet dataSet )
    {
        // ---------------------------------------------------------------------
        // Inline javascript/html to add to HTML before output
        // ---------------------------------------------------------------------

        int i = 1;

        final String codeForInputFields = " name=\"entryfield\" ";
        final String codeForSelectLists = " name=\"entryselect\" ";

        StringBuffer sb = new StringBuffer();

        Matcher inputMatcher = INPUT_PATTERN.matcher( htmlCode );

        Map<Integer, DataElement> dataElementMap = getDataElementMap( dataSet );

        while ( inputMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String inputHtml = inputMatcher.group();

            Matcher identifierMatcher = IDENTIFIER_PATTERN.matcher( inputHtml );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                int dataElementId = Integer.parseInt( identifierMatcher.group( 1 ) );
                int optionComboId = Integer.parseInt( identifierMatcher.group( 2 ) );

                DataElement dataElement = dataElementMap.get( dataElementId );

                if ( dataElement == null )
                {
                    return i18n.getString( "dataelement_with_id" ) + ": " + dataElementId + " " + i18n.getString( "does_not_exist" );
                }

                DataElementCategoryOptionCombo categoryOptionCombo = categoryService
                    .getDataElementCategoryOptionCombo( optionComboId );

                if ( categoryOptionCombo == null )
                {
                    return i18n.getString( "cate_option_combo_with_id" ) + ": " + optionComboId + " " + i18n.getString( "does_not_exist" );
                }

                if ( dataElement.getType().equals( DataElement.VALUE_TYPE_BOOL ) )
                {
                    inputHtml = inputHtml.replace( "input", "select" );
                    inputHtml = inputHtml.replaceAll( "value=\".*?\"", "" );
                }

                // -------------------------------------------------------------
                // Insert title info
                // -------------------------------------------------------------

                StringBuilder title = new StringBuilder( "title=\"" ).append( dataElement.getDisplayName() ).append( " " ).append( categoryOptionCombo.getDisplayName() ).append( "\"" );

                inputHtml = inputHtml.contains( EMPTY_TITLE_TAG ) ? inputHtml.replace( EMPTY_TITLE_TAG, title ) : inputHtml + " " + title;

                String appendCode = "";

                if ( dataElement.getType().equals( VALUE_TYPE_BOOL ) )
                {
                    appendCode += codeForSelectLists + "tabindex=\"" + i++ + "\">";

                    appendCode += "<option value=\"\">" + i18n.getString( "no_value" ) + "</option>";
                    appendCode += "<option value=\"true\">" + i18n.getString( "yes" ) + "</option>";
                    appendCode += "<option value=\"false\">" + i18n.getString( "no" ) + "</option>";
                    appendCode += "</select>";
                }
                else
                {
                    appendCode += codeForInputFields + "tabindex=\"" + i++ + "\"" + TAG_CLOSE;
                }

                inputHtml = inputHtml.replace( TAG_CLOSE, appendCode );
                inputHtml += "<span id=\"" + dataElement.getId() + "-dataelement\" style=\"display:none\">" + dataElement.getFormNameFallback() + "</span>";
                inputHtml += "<span id=\"" + categoryOptionCombo.getId() + "-optioncombo\" style=\"display:none\">" + categoryOptionCombo.getName() + "</span>";

                inputMatcher.appendReplacement( sb, inputHtml );
            }
        }

        inputMatcher.appendTail( sb );

        return sb.toString();
    }

    public Set<DataElement> getDataElementsInDataEntryForm( DataEntryForm form )
    {
        Set<DataElement> dataElements = new HashSet<DataElement>();
        
        Matcher inputMatcher = INPUT_PATTERN.matcher( form.getHtmlCode() );
        
        while ( inputMatcher.find() )
        {
            String inputHtml = inputMatcher.group();
            
            Matcher identifierMatcher = IDENTIFIER_PATTERN.matcher( inputHtml );
            Matcher dataElementTotalMatcher = DATAELEMENT_TOTAL_PATTERN.matcher( inputHtml );
            
            DataElement dataElement = null;
            
            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                int dataElementId = Integer.parseInt( identifierMatcher.group( 1 ) );
                dataElement = dataElementService.getDataElement( dataElementId );
            }
            else if ( dataElementTotalMatcher.find() && dataElementTotalMatcher.groupCount() > 0 )
            {
                int dataElementId = Integer.parseInt( dataElementTotalMatcher.group( 1 ) );
                dataElement = dataElementService.getDataElement( dataElementId );
            }
            
            if ( dataElement != null )
            {
                dataElements.add( dataElement );
            }
        }
        
        return dataElements;
    }
    
    public Collection<DataEntryForm> listDisctinctDataEntryFormByProgramStageIds( List<Integer> programStageIds )
    {
        if ( programStageIds == null || programStageIds.isEmpty() )
        {
            return null;
        }

        return dataEntryFormStore.listDisctinctDataEntryFormByProgramStageIds( programStageIds );
    }

    public Collection<DataEntryForm> listDisctinctDataEntryFormByDataSetIds( List<Integer> dataSetIds )
    {
        if ( dataSetIds == null || dataSetIds.size() == 0 )
        {
            return null;
        }

        return dataEntryFormStore.listDisctinctDataEntryFormByDataSetIds( dataSetIds );
    }

    public Collection<DataEntryForm> getDataEntryForms( final Collection<Integer> identifiers )
    {
        Collection<DataEntryForm> dataEntryForms = getAllDataEntryForms();

        return identifiers == null ? dataEntryForms : FilterUtils.filter( dataEntryForms, new Filter<DataEntryForm>()
        {
            public boolean retain( DataEntryForm object )
            {
                return identifiers.contains( object.getId() );
            }
        } );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Returns a Map of all DataElements in the given DataSet where the key is
     * the DataElement identifier and the value is the DataElement.
     */
    private Map<Integer, DataElement> getDataElementMap( DataSet dataSet )
    {
        Map<Integer, DataElement> map = new HashMap<Integer, DataElement>();

        for ( DataElement element : dataSet.getDataElements() )
        {
            map.put( element.getId(), element );
        }

        return map;
    }
}
