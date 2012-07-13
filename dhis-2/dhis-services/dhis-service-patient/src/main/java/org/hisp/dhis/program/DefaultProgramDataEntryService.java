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

package org.hisp.dhis.program;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.BooleanUtils;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patientdatavalue.PatientDataValue;
import org.hisp.dhis.patientdatavalue.PatientDataValueService;

/**
 * @author Chau Thu Tran
 * @version $ DefaultProgramDataEntryService.java May 26, 2011 3:59:43 PM $
 * 
 */
public class DefaultProgramDataEntryService
    implements ProgramDataEntryService
{
    private static final String EMPTY = "";

    private static final String DATA_ELEMENT_DOES_NOT_EXIST = "[ Data element does not exist ]";

    private static final String EMPTY_VALUE_TAG = "value=\"\"";

    private static final String EMPTY_TITLE_TAG = "title=\"\"";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PatientDataValueService patientDataValueService;

    public void setPatientDataValueService( PatientDataValueService patientDataValueService )
    {
        this.patientDataValueService = patientDataValueService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    private ProgramStageService programStageService;

    public void setProgramStageService( ProgramStageService programStageService )
    {
        this.programStageService = programStageService;
    }

    private ProgramStageDataElementService programStageDataElementService;

    public void setProgramStageDataElementService( ProgramStageDataElementService programStageDataElementService )
    {
        this.programStageDataElementService = programStageDataElementService;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public String prepareDataEntryFormForEntry( String htmlCode, Collection<PatientDataValue> dataValues,
        String disabled, I18n i18n, ProgramStage programStage, ProgramStageInstance programStageInstance,
        OrganisationUnit organisationUnit )
    {
        Map<Integer, Collection<PatientDataValue>> mapDataValue = new HashMap<Integer, Collection<PatientDataValue>>();

        // ---------------------------------------------------------------------
        // Inline Javascript to add to HTML before outputting
        // ---------------------------------------------------------------------

        final String jQueryCalendar = "<script>datePicker(\"$PROGRAMSTAGEID-$DATAELEMENTID-val\", false);</script>";

        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match data elements in the HTML code
        // ---------------------------------------------------------------------

        Matcher dataElementMatcher = INPUT_PATTERN.matcher( htmlCode );
        int tabindex = 0;

        // ---------------------------------------------------------------------
        // Iterate through all matching data element fields
        // ---------------------------------------------------------------------

        Map<Integer, DataElement> dataElementMap = getDataElementMap( programStage );

        while ( dataElementMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String compulsory = "null";
            boolean allowProvidedElsewhere = false;
            String inputHTML = dataElementMatcher.group( 1 );

            Matcher identifierMatcher = IDENTIFIER_PATTERN_FIELD.matcher( inputHTML );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get data element ID of data element
                // -------------------------------------------------------------

                int programStageId = Integer.parseInt( identifierMatcher.group( 1 ) );

                int dataElementId = Integer.parseInt( identifierMatcher.group( 2 ) );

                DataElement dataElement = null;

                String programStageName = programStage.getName();

                if ( programStageId != programStage.getId() )
                {
                    dataElement = dataElementService.getDataElement( dataElementId );

                    ProgramStage otherProgramStage = programStageService.getProgramStage( programStageId );
                    programStageName = otherProgramStage != null ? otherProgramStage.getName() : "N/A";
                }
                else
                {
                    dataElement = dataElementMap.get( dataElementId );
                    if ( dataElement == null )
                    {
                        return i18n.getString( "some_data_element_not_exist" );
                    }

                    ProgramStageDataElement psde = programStageDataElementService.get( programStage, dataElement );

                    compulsory = BooleanUtils.toStringTrueFalse( psde.isCompulsory() );
                    allowProvidedElsewhere = psde.getAllowProvidedElsewhere();
                }

                if ( dataElement == null )
                {
                    continue;
                }

                // -------------------------------------------------------------
                // Find type of data element
                // -------------------------------------------------------------

                String dataElementType = dataElement.getDetailedNumberType();

                // -------------------------------------------------------------
                // Find existing value of data element in data set
                // -------------------------------------------------------------

                PatientDataValue patientDataValue = null;

                String dataElementValue = EMPTY;

                if ( programStageId != programStage.getId() )
                {
                    Collection<PatientDataValue> patientDataValues = mapDataValue.get( programStageId );

                    if ( patientDataValues == null )
                    {
                        ProgramStage otherProgramStage = programStageService.getProgramStage( programStageId );
                        ProgramStageInstance otherProgramStageInstance = programStageInstanceService
                            .getProgramStageInstance( programStageInstance.getProgramInstance(), otherProgramStage );
                        patientDataValues = patientDataValueService.getPatientDataValues( otherProgramStageInstance );
                        mapDataValue.put( programStageId, patientDataValues );
                    }

                    patientDataValue = getValue( patientDataValues, dataElementId );

                    dataElementValue = patientDataValue != null ? patientDataValue.getValue() : dataElementValue;
                }
                else
                {
                    patientDataValue = getValue( dataValues, dataElementId );

                    dataElementValue = patientDataValue != null ? patientDataValue.getValue() : dataElementValue;
                }

                // -------------------------------------------------------------
                // Insert title information - Data element id, name, type, min,
                // max
                // -------------------------------------------------------------

                if ( inputHTML.contains( "title=\"\"" ) )
                {
                    inputHTML = inputHTML.replace( "title=\"\"",
                        "title=\"" + dataElement.getId() + "." + dataElement.getName() + " (" + dataElementType
                            + ")\" " );
                }
                else
                {
                    inputHTML += "title=\"" + dataElement.getId() + "." + dataElement.getName() + " ("
                        + dataElementType + ")\" ";
                }

                // -------------------------------------------------------------
                // Set field for dataElement
                // -------------------------------------------------------------

                tabindex++;

                if ( DataElement.VALUE_TYPE_INT.equals( dataElement.getType() )
                    || DataElement.VALUE_TYPE_STRING.equals( dataElement.getType() ) )
                {
                    inputHTML = populateCustomDataEntryForTextBox( dataElement, inputHTML, dataElementValue );
                }
                else if ( DataElement.VALUE_TYPE_DATE.equals( dataElement.getType() ) )
                {
                    inputHTML = populateCustomDataEntryForDate( inputHTML, dataElementValue );
                }
                else if ( DataElement.VALUE_TYPE_TRUE_ONLY.equals( dataElement.getType() ) )
                {
                    inputHTML = populateCustomDataEntryForTrueOnly( dataElement, inputHTML, dataElementValue );
                }
                else if ( DataElement.VALUE_TYPE_BOOL.equals( dataElement.getType() ) )
                {
                    inputHTML = populateCustomDataEntryForBoolean( dataElement, inputHTML, dataElementValue, i18n );
                }

                // -----------------------------------------------------------
                // Check if this dataElement is from another programStage then
                // disable
                // If programStagsInstance is completed then disabled it
                // -----------------------------------------------------------

                disabled = "";
                if ( programStageId != programStage.getId() )
                {
                    disabled = "disabled=\"\"";
                }
                else
                {
                    if ( DataElement.VALUE_TYPE_DATE.equals( dataElement.getType() ) )
                    {
                        inputHTML += jQueryCalendar;
                    }
                    
                    if ( !programStageInstance.isCompleted() && allowProvidedElsewhere )
                    {
                        // Add ProvidedByOtherFacility checkbox
                        inputHTML = addProvidedElsewherCheckbox( inputHTML, patientDataValue, programStage );
                    }
                }

                // -----------------------------------------------------------
                //
                // -----------------------------------------------------------

                inputHTML = inputHTML.replace( "$DATAELEMENTID", String.valueOf( dataElementId ) );
                inputHTML = inputHTML.replace( "$VALUE",dataElementValue );
                inputHTML = inputHTML.replace( "$PROGRAMSTAGEID", String.valueOf( programStageId ) );
                inputHTML = inputHTML.replace( "$PROGRAMSTAGENAME", programStageName );
                inputHTML = inputHTML.replace( "$DATAELEMENTNAME", dataElement.getName() );
                inputHTML = inputHTML.replace( "$DATAELEMENTTYPE", dataElementType );
                inputHTML = inputHTML.replace( "$DISABLED", disabled );
                inputHTML = inputHTML.replace( "$COMPULSORY", compulsory );
                inputHTML = inputHTML.replace( "$SAVEMODE", "false" );
                inputHTML = inputHTML.replace( "$TABINDEX", tabindex + "" );
                inputHTML = inputHTML.replaceAll( "\\$", "\\\\\\$" );

                dataElementMatcher.appendReplacement( sb, inputHTML );
            }
        }

        dataElementMatcher.appendTail( sb );

        return populateI18nStrings( sb.toString(), i18n );
    }

    public String prepareDataEntryFormForEdit( String htmlCode )
    {
        // ---------------------------------------------------------------------
        // Metadata code to add to HTML before outputting
        // ---------------------------------------------------------------------

        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match data elements in the HTML code
        // ---------------------------------------------------------------------

        Matcher inputMatcher = INPUT_PATTERN.matcher( htmlCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching data element fields
        // ---------------------------------------------------------------------

        while ( inputMatcher.find() )
        {
            String inputHTML = inputMatcher.group();
            inputHTML = inputHTML.replace( ">", "" );

            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String dataElementCode = inputMatcher.group( 1 );

            Matcher identifierMatcher = IDENTIFIER_PATTERN_FIELD.matcher( dataElementCode );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get data element ID of data element
                // -------------------------------------------------------------

                int dataElementId = Integer.parseInt( identifierMatcher.group( 2 ) );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );

                if ( dataElement != null )
                {
                    if ( DataElement.VALUE_TYPE_DATE.equals( dataElement.getType() ) )
                    {
                        inputHTML = populateCustomDataEntryForDate( dataElement, inputHTML );
                    }
                    else if ( DataElement.VALUE_TYPE_BOOL.equals( dataElement.getType() ) )
                    {
                        inputHTML = populateCustomDataEntryForBoolean( dataElement, inputHTML );
                    }
                    else if ( !DataElement.VALUE_TYPE_TRUE_ONLY.equals( dataElement.getType() ) )
                    {
                        inputHTML = populateCustomDataEntryForTrueOnly( dataElement, inputHTML );
                    }
                    else
                    {
                        inputHTML = populateCustomDataEntryForTextBox( dataElement, inputHTML );
                    }
                }

                inputHTML = inputHTML + ">";

                inputMatcher.appendReplacement( sb, inputHTML );

            }
        }

        inputMatcher.appendTail( sb );

        return (sb.toString().isEmpty()) ? htmlCode : sb.toString();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String populateCustomDataEntryForTextBox( DataElement dataElement, String inputHTML )
    {
        String displayValue = (dataElement == null) ? " value=\"" + DATA_ELEMENT_DOES_NOT_EXIST + "\" " : " value=\"[ "
            + dataElement.getName() + " ]\"";
        inputHTML = inputHTML.contains( EMPTY_VALUE_TAG ) ? inputHTML.replace( EMPTY_VALUE_TAG, displayValue )
            : inputHTML + " " + displayValue;

        String displayTitle = (dataElement == null) ? " title=\"" + DATA_ELEMENT_DOES_NOT_EXIST + "\" " : " title=\""
            + dataElement.getId() + "." + dataElement.getName() + "-" + dataElement.getDetailedNumberType() + "\" ";
        inputHTML = inputHTML.contains( EMPTY_TITLE_TAG ) ? inputHTML.replace( EMPTY_TITLE_TAG, displayTitle )
            : inputHTML + " " + displayTitle;
        return inputHTML;
    }

    private String populateCustomDataEntryForBoolean( DataElement dataElement, String inputHTML )
    {
        String displayValue = (dataElement == null) ? " value=\"" + DATA_ELEMENT_DOES_NOT_EXIST + "\" " : " value=\"[ "
            + dataElement.getName() + " ]\" ";
        inputHTML = inputHTML.contains( EMPTY_VALUE_TAG ) ? inputHTML.replace( EMPTY_VALUE_TAG, displayValue )
            : inputHTML + " " + displayValue;

        String displayTitle = (dataElement == null) ? " title=\"" + DATA_ELEMENT_DOES_NOT_EXIST + "\" " : " title=\""
            + dataElement.getId() + "." + dataElement.getName() + "-" + dataElement.getDetailedNumberType() + "\" ";
        inputHTML = inputHTML.contains( EMPTY_TITLE_TAG ) ? inputHTML.replace( EMPTY_TITLE_TAG, displayTitle )
            : inputHTML + " " + displayTitle;

        return inputHTML;
    }

    private String populateCustomDataEntryForTrueOnly( DataElement dataElement, String inputHTML )
    {
        String displayValue = (dataElement == null) ? " value=\"" + DATA_ELEMENT_DOES_NOT_EXIST + "\" " : " value=\"[ "
            + dataElement.getName() + " ]\" ";
        inputHTML = inputHTML.contains( EMPTY_VALUE_TAG ) ? inputHTML.replace( EMPTY_VALUE_TAG, displayValue )
            : inputHTML + " " + displayValue;

        String displayTitle = (dataElement == null) ? " title=\"" + DATA_ELEMENT_DOES_NOT_EXIST + "\" " : " title=\""
            + dataElement.getId() + "." + dataElement.getName() + "-" + dataElement.getDetailedNumberType() + "\" ";
        inputHTML = inputHTML.contains( EMPTY_TITLE_TAG ) ? inputHTML.replace( EMPTY_TITLE_TAG, displayTitle )
            : inputHTML + " " + displayTitle;

        return inputHTML;
    }

    private String populateCustomDataEntryForDate( DataElement dataElement, String inputHTML )
    {
        String displayValue = (dataElement == null) ? " value=\"" + DATA_ELEMENT_DOES_NOT_EXIST + "\"" : " value=\"[ "
            + dataElement.getName() + " ]\"";
        inputHTML = inputHTML.contains( EMPTY_VALUE_TAG ) ? inputHTML.replace( EMPTY_VALUE_TAG, displayValue )
            : inputHTML + " " + displayValue;

        String displayTitle = (dataElement == null) ? " title=\"" + DATA_ELEMENT_DOES_NOT_EXIST + "\"" : " title=\""
            + dataElement.getId() + "." + dataElement.getName() + "-" + dataElement.getDetailedNumberType() + "\" ";
        inputHTML = inputHTML.contains( EMPTY_TITLE_TAG ) ? inputHTML.replace( EMPTY_TITLE_TAG, displayTitle )
            : inputHTML + " " + displayTitle;

        return inputHTML;
    }

    private String populateCustomDataEntryForBoolean( DataElement dataElement, String inputHTML,
        String patientDataValue, I18n i18n )
    {
        final String jsCodeForBoolean = " name=\"entryselect\" tabIndex=\"$TABINDEX\" $DISABLED data=\"{compulsory:$COMPULSORY, deName:'$DATAELEMENTNAME' }\" onchange=\"saveOpt( $DATAELEMENTID )\" style=\" text-align:center;\" ";

        inputHTML = inputHTML.replaceFirst( "input", "select" );
        inputHTML = inputHTML.replace( "name=\"entryselect\"", jsCodeForBoolean );

        inputHTML += ">";
        inputHTML += "<option value=\"\">" + i18n.getString( "no_value" ) + "</option>";
        inputHTML += "<option value=\"true\">" + i18n.getString( "yes" ) + "</option>";
        inputHTML += "<option value=\"false\">" + i18n.getString( "no" ) + "</option>";

        // -------------------------------------------------------------
        // Insert value of data element in output code
        // -------------------------------------------------------------
        if ( patientDataValue != null )
        {
            if ( patientDataValue.equalsIgnoreCase( "true" ) )
            {
                inputHTML = inputHTML.replace( "<option value=\"true\">", "<option value=\"" + i18n.getString( "true" )
                    + "\" selected>" );
            }
            else if ( patientDataValue.equalsIgnoreCase( "false" ) )
            {
                inputHTML = inputHTML.replace( "<option value=\"false\">",
                    "<option value=\"" + i18n.getString( "false" ) + "\" selected>" );
            }
        }

        inputHTML += "</select>";

        return inputHTML;

    }

    private String populateCustomDataEntryForTrueOnly( DataElement dataElement, String inputHTML,
        String dataElementValue )
    {
        final String jsCodeForInputs = " name=\"entryfield\" tabIndex=\"$TABINDEX\" $DISABLED data=\"{compulsory:$COMPULSORY, deName:'$DATAELEMENTNAME', deType:'$DATAELEMENTTYPE'}\" onchange=\"saveVal( $DATAELEMENTID )\" onkeypress=\"return keyPress(event, this)\" style=\" text-align:center;\"  ";

        String checked = "";
        if ( !dataElementValue.equals( EMPTY ) && dataElementValue.equals( "true" ) )
        {
            checked = "checked";
        }

        if ( inputHTML.contains( "value=\"\"" ) )
        {
            inputHTML = inputHTML.replace( "value=\"\"", checked );
        }
        else
        {
            inputHTML += " " + checked;
        }

        inputHTML += jsCodeForInputs;
        inputHTML += " />";

        return inputHTML;
    }

    private String populateCustomDataEntryForTextBox( DataElement dataElement, String inputHTML, String dataElementValue )
    {
        final String jsCodeForInputs = " name=\"entryfield\" tabIndex=\"$TABINDEX\" $DISABLED data=\"{compulsory:$COMPULSORY, deName:'$DATAELEMENTNAME', deType:'$DATAELEMENTTYPE'}\" options='$OPTIONS' maxlength=255 style=\" text-align:center;\"  ";
        final String jsCodeForOnchange = " name=\"entryfield\" tabIndex=\"$TABINDEX\" onchange=\"saveVal( $DATAELEMENTID )\" onkeypress=\"return keyPress(event, this)\" maxlength=255 ";

        // -------------------------------------------------------------
        // Insert value of data element in output code
        // -------------------------------------------------------------

        if ( inputHTML.contains( "value=\"\"" ) )
        {
            inputHTML = inputHTML.replace( "value=\"\"", "value=\"" + dataElementValue + "\"" );
        }
        else
        {
            inputHTML += "value=\"" + dataElementValue + "\"";
        }

        inputHTML += jsCodeForInputs;

        Boolean hasOptionSet = (dataElement.getOptionSet() != null);
        inputHTML = inputHTML.replace( "$OPTIONS", hasOptionSet.toString() );
        if ( !hasOptionSet )
        {
            inputHTML += jsCodeForOnchange;
        }

        if( DataElement.VALUE_TYPE_LONG_TEXT.equals( dataElement.getDetailedTextType() ))
            inputHTML += " >$VALUE";
        
        return inputHTML;
    }

    private String populateCustomDataEntryForDate( String inputHTML, String dataElementValue )
    {
        final String jsCodeForDate = " name=\"entryfield\" tabIndex=\"$TABINDEX\" $DISABLED data=\"{compulsory:$COMPULSORY, deName:'$DATAELEMENTNAME'}\" onchange=\"saveVal( $DATAELEMENTID )\" style=\" text-align:center;\" ";

        // -------------------------------------------------------------
        // Insert value of data element in output code
        // -------------------------------------------------------------

        if ( inputHTML.contains( "value=\"\"" ) )
        {
            inputHTML = inputHTML.replace( "value=\"\"", "value=\"" + dataElementValue + "\"" );
        }
        else
        {
            inputHTML += "value=\"" + dataElementValue + "\"";
        }

        inputHTML = inputHTML.replace( "name=\"entryfield\"", jsCodeForDate );
        inputHTML += " />";

        return inputHTML;
    }

    private String addProvidedElsewherCheckbox( String appendCode, PatientDataValue patientDataValue,
        ProgramStage programStage )
    {
        String id = "$PROGRAMSTAGEID_$DATAELEMENTID_facility";
        appendCode += "<div id=\"span_"
            + id
            + "\" class=\"provided-elsewhere\"><input name=\"providedByAnotherFacility\" title=\"is provided by another Facility ?\"  id=\""
            + id + "\"  type=\"checkbox\" ";

        if ( patientDataValue != null && patientDataValue.getProvidedElsewhere() )
        {
            appendCode += " checked=\"checked\" ";
        }

        appendCode += "onChange=\"updateProvidingFacility( $DATAELEMENTID, this )\"  ></div>";

        return appendCode;

    }

    /**
     * Returns the value of the PatientDataValue in the Collection of DataValues
     * with the given data element identifier.
     */
    private PatientDataValue getValue( Collection<PatientDataValue> dataValues, int dataElementId )
    {
        for ( PatientDataValue dataValue : dataValues )
        {
            if ( dataValue.getDataElement().getId() == dataElementId )
            {
                return dataValue;
            }
        }

        return null;
    }

    /**
     * Returns a Map of all DataElements in the given ProgramStage where the key
     * is the DataElement identifier and the value is the DataElement.
     */
    private Map<Integer, DataElement> getDataElementMap( ProgramStage programStage )
    {
        Collection<DataElement> dataElements = programStageDataElementService.getListDataElement( programStage );

        if ( programStage == null )
        {
            return null;
        }
        Map<Integer, DataElement> map = new HashMap<Integer, DataElement>();

        for ( DataElement element : dataElements )
        {
            map.put( element.getId(), element );
        }

        return map;
    }

    /**
     * Replaces i18n string in the custom form code.
     * 
     * @param dataEntryFormCode the data entry form html.
     * @param i18n the I18n object.
     * @return internationalized data entry form html.
     */
    private String populateI18nStrings( String dataEntryFormCode, I18n i18n )
    {
        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match i18n strings in the HTML code
        // ---------------------------------------------------------------------

        Pattern i18nPattern = Pattern.compile( "(<i18n.*?)[/]?</i18n>", Pattern.DOTALL );
        Matcher i18nMatcher = i18nPattern.matcher( dataEntryFormCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching i18n element fields
        // ---------------------------------------------------------------------

        while ( i18nMatcher.find() )
        {
            String i18nCode = i18nMatcher.group( 1 );

            i18nCode = i18nCode.replaceAll( "<i18n>", "" );

            i18nCode = i18n.getString( i18nCode );

            i18nMatcher.appendReplacement( sb, i18nCode );
        }

        i18nMatcher.appendTail( sb );

        String result = sb.toString();

        result.replaceAll( "</i18n>", "" );

        return result;
    }
}