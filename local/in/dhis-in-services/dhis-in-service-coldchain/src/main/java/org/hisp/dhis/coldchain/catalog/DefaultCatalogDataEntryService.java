package org.hisp.dhis.coldchain.catalog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.BooleanUtils;
import org.hisp.dhis.i18n.I18n;

/**
 * @author Mithilesh Kumar Thakur
 *
 * @version DefaultCatalogDataEntryService.java Jun 7, 2012 5:18:09 PM	
 */
public class DefaultCatalogDataEntryService implements CatalogDataEntryService
{
    
    private static final String EMPTY = "";

    private static final String CATALOG_TYPE_ATTRIBUTE_DOES_NOT_EXIST = "[ Catalogtype attribute does not exist ]";

    private static final String EMPTY_VALUE_TAG = "value=\"\"";

    private static final String EMPTY_TITLE_TAG = "title=\"\"";
    
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private CatalogTypeAttributeService catalogTypeAttributeService;
    
    public void setCatalogTypeAttributeService( CatalogTypeAttributeService catalogTypeAttributeService )
    {
        this.catalogTypeAttributeService = catalogTypeAttributeService;
    }
    
    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    public  String prepareDataEntryFormForCatalog( String htmlCode, Collection<CatalogDataValue> dataValues, String disabled,
        I18n i18n, CatalogType catalogType )
    {
        Map<Integer, Collection<CatalogDataValue>> mapDataValue = new HashMap<Integer, Collection<CatalogDataValue>>();

        String result = "";

        result = populateCustomDataEntryForTextBox( htmlCode, dataValues, disabled, i18n, catalogType, mapDataValue );
        
        result = populateCustomDataEntryForCOMBO( htmlCode, dataValues, disabled, i18n, catalogType, mapDataValue );

        result = populateCustomDataEntryForDate( result, dataValues, disabled, i18n, catalogType, mapDataValue );

        result = populateCustomDataEntryForBoolean( result, dataValues, disabled, i18n, catalogType,  mapDataValue );

        result = populateI18nStrings( result, i18n );

        return result;
    }
    
    
    
    public String prepareDataEntryFormForEdit( String htmlCode )
    {
        String result = populateCustomDataEntryForDate( htmlCode );

        result = populateCustomDataEntryForBoolean( result );

        result = populateCustomDataEntryForTextBox( result );
        
        result = populateCustomDataEntryForCOMBO( result );

        return result;
    }
    
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private String populateCustomDataEntryForTextBox( String htmlCode )
    {
     // ---------------------------------------------------------------------
        // Metadata code to add to HTML before outputting
        // ---------------------------------------------------------------------

        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match catalogType attribute in the HTML code
        // ---------------------------------------------------------------------

        Matcher inputMatcher = INPUT_PATTERN.matcher( htmlCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching catalogType attribute fields
        // ---------------------------------------------------------------------
        
        while ( inputMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String catalogTypeAttributeCode = inputMatcher.group( 1 );

            String inputHTML = inputMatcher.group();
            inputHTML = inputHTML.replace( ">", "" );

            Matcher identifierMatcher = CatalogDataEntryService.IDENTIFIER_PATTERN_FIELD.matcher( catalogTypeAttributeCode );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get catalogType attribute ID of catalogType attribute
                // -------------------------------------------------------------

                int catalogTypeAttributeId = Integer.parseInt( identifierMatcher.group( 2 ) );
                
                CatalogTypeAttribute catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( catalogTypeAttributeId );
                
                if ( catalogTypeAttribute != null && (!CatalogTypeAttribute.TYPE_INT.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) && !CatalogTypeAttribute.TYPE_STRING.equalsIgnoreCase( catalogTypeAttribute.getValueType() )) )
                {
                    continue;
                }
                

                String displayValue = ( catalogTypeAttribute == null ) ? " value=\"" + CATALOG_TYPE_ATTRIBUTE_DOES_NOT_EXIST + "\" "
                    : " value=\"[ " + catalogTypeAttribute.getName() + " ]\"";
                
                inputHTML = inputHTML.contains( EMPTY_VALUE_TAG ) ? inputHTML.replace( EMPTY_VALUE_TAG, displayValue )
                    : inputHTML + " " + displayValue;

                String displayTitle = ( catalogTypeAttribute == null ) ? " title=\"" + CATALOG_TYPE_ATTRIBUTE_DOES_NOT_EXIST + "\" "
                    : " title=\"" + catalogTypeAttribute.getId() + "." + catalogTypeAttribute.getName() + "-"
                        + catalogTypeAttribute.getValueType() + "\" ";
                
                inputHTML = inputHTML.contains( EMPTY_TITLE_TAG ) ? inputHTML.replace( EMPTY_TITLE_TAG, displayTitle )
                    : inputHTML + " " + displayTitle;

                inputHTML = inputHTML + ">";

                inputMatcher.appendReplacement( sb, inputHTML );
            }
        }

        inputMatcher.appendTail( sb );

        return (sb.toString().isEmpty()) ? htmlCode : sb.toString();
    }
    
 
    private String populateCustomDataEntryForCOMBO( String htmlCode )
    {
     // ---------------------------------------------------------------------
        // Metadata code to add to HTML before outputting
        // ---------------------------------------------------------------------

        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match catalogType attribute in the HTML code
        // ---------------------------------------------------------------------

        Matcher inputMatcher = INPUT_PATTERN.matcher( htmlCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching catalogType attribute fields
        // ---------------------------------------------------------------------
        
        while ( inputMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String catalogTypeAttributeCode = inputMatcher.group( 1 );

            String inputHTML = inputMatcher.group();
            inputHTML = inputHTML.replace( ">", "" );

            Matcher identifierMatcher = CatalogDataEntryService.IDENTIFIER_PATTERN_FIELD.matcher( catalogTypeAttributeCode );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get catalogType attribute ID of catalogType attribute
                // -------------------------------------------------------------

                int catalogTypeAttributeId = Integer.parseInt( identifierMatcher.group( 2 ) );
                
                CatalogTypeAttribute catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( catalogTypeAttributeId );
                
                if ( catalogTypeAttribute != null && !CatalogTypeAttribute.TYPE_COMBO.equalsIgnoreCase( catalogTypeAttribute.getValueType() )  )
                {
                    continue;
                }
                

                String displayValue = ( catalogTypeAttribute == null ) ? " value=\"" + CATALOG_TYPE_ATTRIBUTE_DOES_NOT_EXIST + "\" "
                    : " value=\"[ " + catalogTypeAttribute.getName() + " ]\"";
                
                inputHTML = inputHTML.contains( EMPTY_VALUE_TAG ) ? inputHTML.replace( EMPTY_VALUE_TAG, displayValue )
                    : inputHTML + " " + displayValue;

                String displayTitle = ( catalogTypeAttribute == null ) ? " title=\"" + CATALOG_TYPE_ATTRIBUTE_DOES_NOT_EXIST + "\" "
                    : " title=\"" + catalogTypeAttribute.getId() + "." + catalogTypeAttribute.getName() + "-"
                        + catalogTypeAttribute.getValueType() + "\" ";
                
                inputHTML = inputHTML.contains( EMPTY_TITLE_TAG ) ? inputHTML.replace( EMPTY_TITLE_TAG, displayTitle )
                    : inputHTML + " " + displayTitle;

                inputHTML = inputHTML + ">";

                inputMatcher.appendReplacement( sb, inputHTML );
            }
        }

        inputMatcher.appendTail( sb );

        return (sb.toString().isEmpty()) ? htmlCode : sb.toString();
    }
        
   
    private String populateCustomDataEntryForBoolean( String htmlCode )
    {
        // ---------------------------------------------------------------------
        // Metadata code to add to HTML before outputting
        // ---------------------------------------------------------------------

        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match catalogType attribute in the HTML code
        // ---------------------------------------------------------------------

        Matcher inputMatcher = INPUT_PATTERN.matcher( htmlCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching catalogType attribute fields
        // ---------------------------------------------------------------------

        while ( inputMatcher.find() )
        {
            String inputHTML = inputMatcher.group();
            inputHTML = inputHTML.replace( ">", "" );

            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String catalogTypeAttributeCode = inputMatcher.group( 1 );

            Matcher identifierMatcher = IDENTIFIER_PATTERN_FIELD.matcher( catalogTypeAttributeCode );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get catalogType attribute ID of catalogType attribute
                // -------------------------------------------------------------
                int catalogTypeAttributeId = Integer.parseInt( identifierMatcher.group( 2 ) );
                
                CatalogTypeAttribute catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( catalogTypeAttributeId );
                
                if ( catalogTypeAttribute != null && !CatalogTypeAttribute.TYPE_BOOL.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) )
                {
                    continue;
                }

                String displayValue = ( catalogTypeAttribute == null) ? " value=\"" + CATALOG_TYPE_ATTRIBUTE_DOES_NOT_EXIST + "\" "
                    : " value=\"[ " + catalogTypeAttribute.getName() + " ]\" ";
                
                inputHTML = inputHTML.contains( EMPTY_VALUE_TAG ) ? inputHTML.replace( EMPTY_VALUE_TAG, displayValue )
                    : inputHTML + " " + displayValue;

                String displayTitle = ( catalogTypeAttribute == null) ? " title=\"" + CATALOG_TYPE_ATTRIBUTE_DOES_NOT_EXIST + "\" "
                    : " title=\"" + catalogTypeAttribute.getId() + "." + catalogTypeAttribute.getName() + "-"
                        + catalogTypeAttribute.getValueType() + "\" ";
                
                inputHTML = inputHTML.contains( EMPTY_TITLE_TAG ) ? inputHTML.replace( EMPTY_TITLE_TAG, displayTitle )
                    : inputHTML + " " + displayTitle;

                inputHTML = inputHTML + ">";

                inputMatcher.appendReplacement( sb, inputHTML );
            }
        }

        inputMatcher.appendTail( sb );

        return (sb.toString().isEmpty()) ? htmlCode : sb.toString();
    }
    
 
    private String populateCustomDataEntryForDate( String htmlCode )
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

            String catalogTypeAttributeCode = inputMatcher.group( 1 );

            Matcher identifierMatcher = IDENTIFIER_PATTERN_FIELD.matcher( catalogTypeAttributeCode );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get catalogType attribute ID of catalogType attribute
                // -------------------------------------------------------------
                
                int catalogTypeAttributeId = Integer.parseInt( identifierMatcher.group( 2 ) );
                
                CatalogTypeAttribute catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( catalogTypeAttributeId );

                //int dataElementId = Integer.parseInt( identifierMatcher.group( 2 ) );
                //DataElement dataElement = dataElementService.getDataElement( dataElementId );

                if ( catalogTypeAttribute != null && !CatalogTypeAttribute.TYPE_DATE.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) )
                {
                    continue;
                }

                String displayValue = ( catalogTypeAttribute == null ) ? " value=\"" + CATALOG_TYPE_ATTRIBUTE_DOES_NOT_EXIST + "\""
                    : " value=\"[ " + catalogTypeAttribute.getName() + " ]\"";
                
                inputHTML = inputHTML.contains( EMPTY_VALUE_TAG ) ? inputHTML.replace( EMPTY_VALUE_TAG, displayValue )
                    : inputHTML + " " + displayValue;

                String displayTitle = (catalogTypeAttribute == null) ? " title=\"" + CATALOG_TYPE_ATTRIBUTE_DOES_NOT_EXIST + "\""
                    : " title=\"" + catalogTypeAttribute.getId() + "." + catalogTypeAttribute.getName() + "-"
                        + catalogTypeAttribute.getValueType() + "\" ";
                
                inputHTML = inputHTML.contains( EMPTY_TITLE_TAG ) ? inputHTML.replace( EMPTY_TITLE_TAG, displayTitle )
                    : inputHTML + " " + displayTitle;

                inputHTML = inputHTML + ">";

                inputMatcher.appendReplacement( sb, inputHTML );
            }
        }

        inputMatcher.appendTail( sb );

        return (sb.toString().isEmpty()) ? htmlCode : sb.toString();
    }    
    
    private String populateCustomDataEntryForBoolean( String dataEntryFormCode,
        Collection<CatalogDataValue> dataValues, String disabled, I18n i18n, CatalogType catalogType,Map<Integer, Collection<CatalogDataValue>> mapDataValue )
    {
        // ---------------------------------------------------------------------
        // Inline Javascript to add to HTML before outputting
        // ---------------------------------------------------------------------

        final String jsCodeForBoolean = " name=\"entryselect\" $DISABLED data=\"{mandatory:$MANDATORY, catalogTypeAttributeName:'$CATALOGTYPEATTRIBUTENAME' }\" onchange=\"saveOpt( $CATALOGTYPEATTRIBUTEID )\" style=\"  text-align:center;\" ";

        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match CatalogTypeAttributes in the HTML code
        // ---------------------------------------------------------------------

        Matcher CatalogTypeAttributeMatcher = INPUT_PATTERN.matcher( dataEntryFormCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching catalogTypeAttribute fields
        // ---------------------------------------------------------------------
       
        Map<Integer, CatalogTypeAttribute> catalogTypeAttributeMap = getCatalogTypeAttributeMap( catalogType );

        while ( CatalogTypeAttributeMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String mandatory = "null";
           
            
            String catalogTypeAttributeCode = CatalogTypeAttributeMatcher.group( 1 );
            
            Matcher identifierMatcher = IDENTIFIER_PATTERN_FIELD.matcher( catalogTypeAttributeCode );
            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
               
                // -------------------------------------------------------------
                // Get catalogType attribute ID of catalogType attribute
                // -------------------------------------------------------------

                int catalogTypeId = Integer.parseInt( identifierMatcher.group( 1 ) );
                
                int catalogTypeAttributeId = Integer.parseInt( identifierMatcher.group( 2 ) );

                CatalogTypeAttribute catalogTypeAttribute = null;

                String catalogTypeName = catalogType.getName();

                if ( catalogTypeId != catalogType.getId() )
                {
                    catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( catalogTypeAttributeId );
                }
                else
                {
                    catalogTypeAttribute = catalogTypeAttributeMap.get( catalogTypeAttributeId );
                    
                    if ( catalogTypeAttribute == null )
                    {
                        return i18n.getString( "some_catalogType_attribute_not_exist" );
                    }
                   
                    mandatory = BooleanUtils.toStringTrueFalse( catalogTypeAttribute.isMandatory() );
                }

                if ( catalogTypeAttribute == null )
                {
                    continue;
                }

                if ( !CatalogTypeAttribute.TYPE_BOOL.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) )
                {
                    continue;
                }

                // -------------------------------------------------------------
                // Find type of catalogType attribute
                // -------------------------------------------------------------

                String catalogTypeAttributeType = catalogTypeAttribute.getValueType();

                // -------------------------------------------------------------
                // Find existing value of catalogType Attribute
                // -------------------------------------------------------------

                CatalogDataValue catalogDataValue = null;
               

                String catalogTypeAttributeValue = EMPTY;

                if ( catalogTypeId != catalogType.getId() )
                {
                    
                    
                    Collection<CatalogDataValue> catalogDataValues = mapDataValue.get( catalogTypeId );

                    if ( catalogDataValues == null )
                    {
                       
                    }

                    catalogDataValue = getValue( catalogDataValues, catalogTypeAttributeId );

                    catalogTypeAttributeValue = catalogDataValue != null ? catalogDataValue.getValue() : catalogTypeAttributeValue;
                }
                else
                {

                    catalogDataValue = getValue( dataValues, catalogTypeAttributeId );

                    if ( catalogDataValue != null )
                    {
                        catalogTypeAttributeValue = catalogDataValue.getValue();
                    }
                }

                String appendCode = catalogTypeAttributeCode.replaceFirst( "input", "select" );
                appendCode = appendCode.replace( "name=\"entryselect\"", jsCodeForBoolean );

                // -------------------------------------------------------------
                // Add title
                // -------------------------------------------------------------

                if ( catalogTypeAttributeCode.contains( "title=\"\"" ) )
                {
                    appendCode = appendCode.replace( "title=\"\"", "title=\"" + catalogTypeAttribute.getId() + "."
                        + catalogTypeAttribute.getName() + "-" + catalogTypeAttributeType + "\" " );
                }
                else
                {
                    appendCode += "title=\"" + catalogTypeAttribute.getId() + "." + catalogTypeAttribute.getName() + "-"
                        + catalogTypeAttributeType + "\" ";
                }

                appendCode += ">";
                appendCode += "<option value=\"\">" + i18n.getString( "Please select" ) + "</option>";
                appendCode += "<option value=\"true\">" + i18n.getString( "yes" ) + "</option>";
                appendCode += "<option value=\"false\">" + i18n.getString( "no" ) + "</option>";

                // -------------------------------------------------------------
                // Insert value of catalogType Attribute in output code
                // -------------------------------------------------------------

                if ( catalogDataValue != null )
                {
                    if ( catalogTypeAttributeValue.equalsIgnoreCase( "true" ) )
                    {
                        appendCode = appendCode.replace( "<option value=\"true\">", "<option value=\""
                            + i18n.getString( "true" ) + "\" selected>" );
                    }

                    if ( catalogTypeAttributeValue.equalsIgnoreCase( "false" ) )
                    {
                        appendCode = appendCode.replace( "<option value=\"false\">", "<option value=\""
                            + i18n.getString( "false" ) + "\" selected>" );
                    }
                }

                appendCode += "</select>";

                // -----------------------------------------------------------
                // Check if this dataElement is from another programStage then
                // disable
                // If programStagsInstance is completed then disabled it
                // -----------------------------------------------------------

                disabled = "";
                if ( catalogTypeId != catalogType.getId() )
                {
                    disabled = "disabled";
                }
                // -----------------------------------------------------------
                // 
                // -----------------------------------------------------------

                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTEID", String.valueOf( catalogTypeAttributeId ) );
                appendCode = appendCode.replace( "$CATALOGTYPEID", String.valueOf( catalogTypeId ) );
                appendCode = appendCode.replace( "$CATALOGTYPENAME", catalogTypeName );
                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTENAME", catalogTypeAttribute.getName() );
                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTETYPE", catalogTypeAttributeType );
                appendCode = appendCode.replace( "$DISABLED", disabled );
                appendCode = appendCode.replace( "$MANDATORY", mandatory );
                appendCode = appendCode.replace( "i18n_yes", i18n.getString( "yes" ) );
                appendCode = appendCode.replace( "i18n_no", i18n.getString( "no" ) );
                appendCode = appendCode.replace( "i18n_select_value", i18n.getString( "select_value" ) );
                appendCode = appendCode.replace( "$SAVEMODE", "false" );

                appendCode = appendCode.replaceAll( "\\$", "\\\\\\$" );

                CatalogTypeAttributeMatcher.appendReplacement( sb, appendCode );
            }
        }

        CatalogTypeAttributeMatcher.appendTail( sb );

        return sb.toString();
    }


    private String populateCustomDataEntryForTextBox( String dataEntryFormCode,
        Collection<CatalogDataValue> dataValues, String disabled, I18n i18n, CatalogType catalogType,
         Map<Integer, Collection<CatalogDataValue>> mapDataValue )
    {
        // ---------------------------------------------------------------------
        // Inline Javascript to add to HTML before outputting
        // ---------------------------------------------------------------------

        final String jsCodeForInputs = " $DISABLED onchange=\"saveVal( $CATALOGTYPEATTRIBUTEID )\" data=\"{mandatory:$MANDATORY, catalogTypeAttributeName:'$CATALOGTYPEATTRIBUTENAME', catalogTypeAttributeValueType:'$CATALOGTYPEATTRIBUTEVALUETYPE'}\" onkeypress=\"return keyPress(event, this)\" style=\" text-align:center;\"  ";
       // final String jsCodeForOptions = " $DISABLED options='$OPTIONS' catalogTypeAttributeId=\"$CATALOGTYPEATTRIBUTEID\" data=\"{mandatory:$MANDATORY, catalogTypeAttributeName:'$CATALOGTYPEATTRIBUTENAME', catalogTypeAttributeValueType:'$CATALOGTYPEATTRIBUTEVALUETYPE'}\" onkeypress=\"return keyPress(event, this)\" style=\" text-align:center;\"  ";

        
        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match CatalogTypeAttributes in the HTML code
        // ---------------------------------------------------------------------


        
        Pattern INPUT_PATTERN = Pattern.compile( "(<input.*?)[/]?>", Pattern.DOTALL );
        
        Matcher CatalogTypeAttributeMatcher = INPUT_PATTERN.matcher( dataEntryFormCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching catalogTypeAttribute fields
        // ---------------------------------------------------------------------
       
        Map<Integer, CatalogTypeAttribute> catalogTypeAttributeMap = getCatalogTypeAttributeMap( catalogType );

       // Map<Integer, DataElement> dataElementMap = getDataElementMap( programStage );

        while ( CatalogTypeAttributeMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String mandatory = "null";
            
            String catalogTypeAttributeCode = CatalogTypeAttributeMatcher.group( 1 );

            Matcher identifierMatcher = IDENTIFIER_PATTERN_FIELD.matcher( catalogTypeAttributeCode );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get catalogType attribute ID of catalogType attribute
                // -------------------------------------------------------------

                int catalogTypeId = Integer.parseInt( identifierMatcher.group( 1 ) );
                
                int catalogTypeAttributeId = Integer.parseInt( identifierMatcher.group( 2 ) );

                CatalogTypeAttribute catalogTypeAttribute = null;

                String catalogTypeName = catalogType.getName();

                
                if ( catalogTypeId != catalogType.getId() )
                {
                    catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( catalogTypeAttributeId );
                }
                
                else
                {
                    catalogTypeAttribute = catalogTypeAttributeMap.get( catalogTypeAttributeId );
                    
                    if ( catalogTypeAttribute == null )
                    {
                        return i18n.getString( "some_catalogType_attribute_not_exist" );
                    }
                   
                    mandatory = BooleanUtils.toStringTrueFalse( catalogTypeAttribute.isMandatory() );
                    
                }

                if ( catalogTypeAttribute == null )
                {
                    continue;
                }
               
                if ( !CatalogTypeAttribute.TYPE_INT.equalsIgnoreCase( catalogTypeAttribute.getValueType() )
                    && !CatalogTypeAttribute.TYPE_STRING.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) )
                {
                    continue;
                }
               
                
                // -------------------------------------------------------------
                // Find type of catalogType attribute
                // -------------------------------------------------------------

                String catalogTypeAttributeType = catalogTypeAttribute.getValueType();

                
                // -------------------------------------------------------------
                // Find existing value of catalogType Attribute
                // -------------------------------------------------------------

                CatalogDataValue catalogDataValue = null;
               

                String catalogTypeAttributeValue = EMPTY;
                
                
                
                if ( catalogTypeId != catalogType.getId() )
                {
                    
                    
                    Collection<CatalogDataValue> catalogDataValues = mapDataValue.get( catalogTypeId );

                    if ( catalogDataValues == null )
                    {
                       
                    }

                    catalogDataValue = getValue( catalogDataValues, catalogTypeAttributeId );

                    catalogTypeAttributeValue = catalogDataValue != null ? catalogDataValue.getValue() : catalogTypeAttributeValue;
                }
                else
                {
                    catalogDataValue = getValue( dataValues, catalogTypeAttributeId );

                    if ( catalogDataValue != null )
                    {
                        catalogTypeAttributeValue = catalogDataValue.getValue();
                    }
                }

                // -------------------------------------------------------------
                // Insert title information - Data element id, name, type, min,
                // max
                // -------------------------------------------------------------
                
                
                if ( catalogTypeAttributeCode.contains( "title=\"\"" ) )
                {
                    catalogTypeAttributeCode = catalogTypeAttributeCode.replace( "title=\"\"", "title=\"" + catalogTypeAttribute.getId() + "."
                        + catalogTypeAttribute.getName() + " (" + catalogTypeAttributeType + ")\" " );
                }
                else
                {
                    catalogTypeAttributeCode += "title=\"" + catalogTypeAttribute.getId() + "." + catalogTypeAttribute.getName() + " ("
                        + catalogTypeAttributeType + ")\" ";
                }

                // -------------------------------------------------------------
                // Insert value of catalogType Attribute in output code
                // -------------------------------------------------------------
                
                String appendCode = catalogTypeAttributeCode;

                if ( appendCode.contains( "value=\"\"" ) )
                {
                    appendCode = appendCode.replace( "value=\"\"", "value=\"" + catalogTypeAttributeValue + "\"" );
                }
                else
                {
                    appendCode += "value=\"" + catalogTypeAttributeValue + "\"";
                }

                appendCode += jsCodeForInputs;
              

                appendCode += " />";


                disabled = "";
                
                if ( catalogTypeId != catalogType.getId() )
                {
                    disabled = "disabled=\"\"";
                }
                
                // -----------------------------------------------------------
                // 
                // -----------------------------------------------------------
                
                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTEID", String.valueOf( catalogTypeAttributeId ) );
                appendCode = appendCode.replace( "$CATALOGTYPEID", String.valueOf( catalogTypeId ) );
                appendCode = appendCode.replace( "$CATALOGTYPENAME", catalogTypeName );
                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTENAME", catalogTypeAttribute.getName() );
                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTEVALUETYPE", catalogTypeAttributeType );
                appendCode = appendCode.replace( "$DISABLED", disabled );
                appendCode = appendCode.replace( "$MANDATORY", mandatory );
                appendCode = appendCode.replace( "$SAVEMODE", "false" );
                appendCode = appendCode.replaceAll( "\\$", "\\\\\\$" );
                
                System.out.println( "---appendCode---" + appendCode );
                
                CatalogTypeAttributeMatcher.appendReplacement( sb, appendCode );
            }
        }

        CatalogTypeAttributeMatcher.appendTail( sb );

        return sb.toString();
    }    
    
 
    
    
    
    private String populateCustomDataEntryForCOMBO( String dataEntryFormCode,
        Collection<CatalogDataValue> dataValues, String disabled, I18n i18n, CatalogType catalogType,
         Map<Integer, Collection<CatalogDataValue>> mapDataValue )
    {
        // ---------------------------------------------------------------------
        // Inline Javascript to add to HTML before outputting
        // ---------------------------------------------------------------------

        //final String jsCodeForInputs = " $DISABLED onchange=\"saveVal( $CATALOGTYPEATTRIBUTEID )\" data=\"{mandatory:$MANDATORY, catalogTypeAttributeName:'$CATALOGTYPEATTRIBUTENAME', catalogTypeAttributeValueType:'$CATALOGTYPEATTRIBUTEVALUETYPE'}\" onkeypress=\"return keyPress(event, this)\" style=\" text-align:center;\"  ";
        final String jsCodeForOptions = " $DISABLED options='$OPTIONS' catalogTypeAttributeId=\"$CATALOGTYPEATTRIBUTEID\" data=\"{mandatory:$MANDATORY, catalogTypeAttributeName:'$CATALOGTYPEATTRIBUTENAME', catalogTypeAttributeValueType:'$CATALOGTYPEATTRIBUTEVALUETYPE'}\" onkeypress=\"return keyPress(event, this)\" style=\" text-align:center;\"  ";

        
        StringBuffer sb = new StringBuffer();

        // ---------------------------------------------------------------------
        // Pattern to match CatalogTypeAttributes in the HTML code
        // ---------------------------------------------------------------------


        
        Pattern INPUT_PATTERN = Pattern.compile( "(<input.*?)[/]?>", Pattern.DOTALL );
        
        Matcher CatalogTypeAttributeMatcher = INPUT_PATTERN.matcher( dataEntryFormCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching catalogTypeAttribute fields
        // ---------------------------------------------------------------------
       
        Map<Integer, CatalogTypeAttribute> catalogTypeAttributeMap = getCatalogTypeAttributeMap( catalogType );

       // Map<Integer, DataElement> dataElementMap = getDataElementMap( programStage );

        while ( CatalogTypeAttributeMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String mandatory = "null";
            
            String catalogTypeAttributeCode = CatalogTypeAttributeMatcher.group( 1 );

            Matcher identifierMatcher = IDENTIFIER_PATTERN_FIELD.matcher( catalogTypeAttributeCode );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get catalogType attribute ID of catalogType attribute
                // -------------------------------------------------------------

                int catalogTypeId = Integer.parseInt( identifierMatcher.group( 1 ) );
                
                int catalogTypeAttributeId = Integer.parseInt( identifierMatcher.group( 2 ) );

                CatalogTypeAttribute catalogTypeAttribute = null;

                String catalogTypeName = catalogType.getName();

                
                if ( catalogTypeId != catalogType.getId() )
                {
                    catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( catalogTypeAttributeId );
                }
                
                else
                {
                    catalogTypeAttribute = catalogTypeAttributeMap.get( catalogTypeAttributeId );
                    
                    if ( catalogTypeAttribute == null )
                    {
                        return i18n.getString( "some_catalogType_attribute_not_exist" );
                    }
                   
                    mandatory = BooleanUtils.toStringTrueFalse( catalogTypeAttribute.isMandatory() );
                    
                }

                if ( catalogTypeAttribute == null )
                {
                    continue;
                }
                
                if ( !CatalogTypeAttribute.TYPE_COMBO.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) )
                {
                    continue;
                }
               
                // -------------------------------------------------------------
                // Find type of catalogType attribute
                // -------------------------------------------------------------

                String catalogTypeAttributeType = catalogTypeAttribute.getValueType();

                
                // -------------------------------------------------------------
                // Find existing value of catalogType Attribute
                // -------------------------------------------------------------

                CatalogDataValue catalogDataValue = null;
               

                String catalogTypeAttributeValue = EMPTY;
                
                
                
                if ( catalogTypeId != catalogType.getId() )
                {
                    
                    Collection<CatalogDataValue> catalogDataValues = mapDataValue.get( catalogTypeId );

                    if ( catalogDataValues == null )
                    {
                       
                    }

                    catalogDataValue = getValue( catalogDataValues, catalogTypeAttributeId );

                    catalogTypeAttributeValue = catalogDataValue != null ? catalogDataValue.getValue() : catalogTypeAttributeValue;
                }
                else
                {
                    catalogDataValue = getValue( dataValues, catalogTypeAttributeId );

                    if ( catalogDataValue != null )
                    {
                        catalogTypeAttributeValue = catalogDataValue.getValue();
                    }
                }

                // Insert title information - Data element id, name, type, min,
                // max
                // -------------------------------------------------------------

                if ( catalogTypeAttributeCode.contains( "title=\"\"" ) )
                {
                    catalogTypeAttributeCode = catalogTypeAttributeCode.replace( "title=\"\"", "title=\"" + catalogTypeAttribute.getId() + "."
                        + catalogTypeAttribute.getName() + " (" + catalogTypeAttributeType + ")\" " );
                }
                else
                {
                    catalogTypeAttributeCode += "title=\"" + catalogTypeAttribute.getId() + "." + catalogTypeAttribute.getName() + " ("
                        + catalogTypeAttributeType + ")\" ";
                }

                // -------------------------------------------------------------
                // Insert value of catalogType Attribute in output code
                // -------------------------------------------------------------
                
                String appendCode = catalogTypeAttributeCode;

                if ( appendCode.contains( "value=\"\"" ) )
                {
                    appendCode = appendCode.replace( "value=\"\"", "value=\"" + catalogTypeAttributeValue + "\"" );
                }
                else
                {
                    appendCode += "value=\"" + catalogTypeAttributeValue + "\"";
                }

                if ( catalogTypeAttribute.getAttributeOptions() != null )
                {
                    appendCode += jsCodeForOptions;

                    appendCode = appendCode.replace( "$OPTIONS", catalogTypeAttribute.getAttributeOptions().toString() );
                }

                appendCode += " />";


                disabled = "";
                
                if ( catalogTypeId != catalogType.getId() )
                {
                    disabled = "disabled=\"\"";
                }
                
                // -----------------------------------------------------------
                // 
                // -----------------------------------------------------------

                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTEID", String.valueOf( catalogTypeAttributeId ) );
                appendCode = appendCode.replace( "$CATALOGTYPEID", String.valueOf( catalogTypeId ) );
                appendCode = appendCode.replace( "$CATALOGTYPENAME", catalogTypeName );
                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTENAME", catalogTypeAttribute.getName() );
                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTEVALUETYPE", catalogTypeAttributeType );
                appendCode = appendCode.replace( "$DISABLED", disabled );
                appendCode = appendCode.replace( "$MANDATORY", mandatory );
                appendCode = appendCode.replace( "$SAVEMODE", "false" );
                appendCode = appendCode.replaceAll( "\\$", "\\\\\\$" );
                
                System.out.println( "---appendCode---" + appendCode );
                
                CatalogTypeAttributeMatcher.appendReplacement( sb, appendCode );
            }
        }

        CatalogTypeAttributeMatcher.appendTail( sb );

        return sb.toString();
    }    
   
    
    private String populateCustomDataEntryForDate( String dataEntryFormCode, Collection<CatalogDataValue> dataValues,
        String disabled, I18n i18n, CatalogType catalogType, Map<Integer, Collection<CatalogDataValue>> mapDataValue )
    {
        // ---------------------------------------------------------------------
        // Inline Javascript to add to HTML before outputting
        // ---------------------------------------------------------------------

        final String jsCodeForDate = " name=\"entryfield\" $DISABLED data=\"{mandatory:$MANDATORY, catalogTypeAttributeName:'$CATALOGTYPEATTRIBUTENAME'}\" onchange=\"saveVal( $CATALOGTYPEATTRIBUTEID )\" style=\" text-align:center;\" ";
        
        // ---------------------------------------------------------------------
        // Metadata code to add to HTML before outputting
        // ---------------------------------------------------------------------

        final String jQueryCalendar = "<script> " + "datePicker(\"$CATALOGTYPEID-$CATALOGTYPEATTRIBUTEID-val\", false)"
            + ";</script>";
       
        //System.out.println( "jQueryCalendar" + jQueryCalendar +  "--- $CATALOGTYPEID----" + catalogType.getId());
        
        StringBuffer sb = new StringBuffer();


        // ---------------------------------------------------------------------
        // Pattern to match CatalogTypeAttributes in the HTML code
        // ---------------------------------------------------------------------

        Pattern catalogTypeAttributePattern = Pattern.compile( "(<input.*?)[/]?/>" );
        
        Matcher CatalogTypeAttributeMatcher = catalogTypeAttributePattern.matcher( dataEntryFormCode );

        // ---------------------------------------------------------------------
        // Pattern to extract catalogTypeAttribute ID from catalogTypeAttribute field
        // ---------------------------------------------------------------------

        // ---------------------------------------------------------------------
        // Iterate through all matching catalogTypeAttribute fields
        // ---------------------------------------------------------------------
        
        Map<Integer, CatalogTypeAttribute> catalogTypeAttributeMap = getCatalogTypeAttributeMap( catalogType );
        

       
        while ( CatalogTypeAttributeMatcher.find() )
        {
            // -----------------------------------------------------------------
            // Get HTML input field code
            // -----------------------------------------------------------------

            String mandatory = "null";
           
           

            String catalogTypeAttributeCode = CatalogTypeAttributeMatcher.group( 1 );
            
            Matcher identifierMatcher = IDENTIFIER_PATTERN_FIELD.matcher( catalogTypeAttributeCode );

            if ( identifierMatcher.find() && identifierMatcher.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get data element ID of data element
                // -------------------------------------------------------------

                int catalogTypeId = Integer.parseInt( identifierMatcher.group( 1 ) );
                
                int catalogTypeAttributeId = Integer.parseInt( identifierMatcher.group( 2 ) );

                CatalogTypeAttribute catalogTypeAttribute = null;

                String catalogTypeName = catalogType.getName();
                

                if ( catalogTypeId != catalogType.getId() )
                {
                    catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( catalogTypeAttributeId );
                }
                else
                {
                   
                    
                    catalogTypeAttribute = catalogTypeAttributeMap.get( catalogTypeAttributeId );
                    
                    if ( catalogTypeAttribute == null )
                    {
                        return i18n.getString( "some_catalogType_attribute_not_exist" );
                    }
                   
                    mandatory = BooleanUtils.toStringTrueFalse( catalogTypeAttribute.isMandatory() );
                    
                }

                if ( catalogTypeAttribute == null )
                {
                    continue;
                }
                if ( !CatalogTypeAttribute.TYPE_DATE.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) )
                {
                    continue;
                }

                // -------------------------------------------------------------
                // Find type of catalogType attribute
                // -------------------------------------------------------------

                String catalogTypeAttributeType = catalogTypeAttribute.getValueType();
                
                
                // -------------------------------------------------------------
                // Find existing value of catalogType Attribute
                // -------------------------------------------------------------

                CatalogDataValue catalogDataValue = null;
               

                String catalogTypeAttributeValue = EMPTY;

                if ( catalogTypeId != catalogType.getId() )
                {
                    
                    
                    Collection<CatalogDataValue> catalogDataValues = mapDataValue.get( catalogTypeId );

                    if ( catalogDataValues == null )
                    {
                       
                    }

                    catalogDataValue = getValue( catalogDataValues, catalogTypeAttributeId );

                    catalogTypeAttributeValue = catalogDataValue != null ? catalogDataValue.getValue() : catalogTypeAttributeValue;
                }
                else
                {
                    
                    catalogDataValue = getValue( dataValues, catalogTypeAttributeId );
                    
                    catalogTypeAttributeValue = catalogDataValue != null ? catalogDataValue.getValue() : catalogTypeAttributeValue;
                }


                // -------------------------------------------------------------
                // Insert value of catalogTypeAttribute in output code
                // -------------------------------------------------------------

                if ( catalogTypeAttributeCode.contains( "value=\"\"" ) )
                {
                    catalogTypeAttributeCode = catalogTypeAttributeCode.replace( "value=\"\"", "value=\"" + catalogTypeAttributeValue + "\"" );
                }
                else
                {
                    catalogTypeAttributeCode += "value=\"" + catalogTypeAttributeValue + "\"";
                }

                // -------------------------------------------------------------
                // Insert title information - catalogTypeAttribute id, name, type,
                // -------------------------------------------------------------

                if ( catalogTypeAttributeCode.contains( "title=\"\"" ) )
                {
                    catalogTypeAttributeCode = catalogTypeAttributeCode.replace( "title=\"\"", "title=\"" + catalogTypeAttribute.getId() + "."
                        + catalogTypeAttribute.getName() + " (" + catalogTypeAttributeType + ")\" " );
                }
                else
                {
                    catalogTypeAttributeCode += "title=\"" + catalogTypeAttribute.getId() + "." + catalogTypeAttribute.getName() + " ("
                        + catalogTypeAttributeType + ")\" ";
                }

                // -------------------------------------------------------------
                // Append Javascript code and meta data (type/min/max) for
                // persisting to output code, and insert value and type for
                // fields
                // -------------------------------------------------------------

                String appendCode = catalogTypeAttributeCode + "/>";
                appendCode = appendCode.replace( "name=\"entryfield\"", jsCodeForDate );

               
                disabled = "";
                if ( catalogTypeId != catalogType.getId() )
                {
                    disabled = "disabled=\"\"";
                }

                appendCode += jQueryCalendar;

                // -------------------------------------------------------------
                // 
                // -------------------------------------------------------------

                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTEID", String.valueOf( catalogTypeAttributeId ) );
                appendCode = appendCode.replace( "$CATALOGTYPEID", String.valueOf( catalogTypeId ) );
                appendCode = appendCode.replace( "$CATALOGTYPENAME", catalogTypeName );
                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTENAME", catalogTypeAttribute.getName() );
                appendCode = appendCode.replace( "$CATALOGTYPEATTRIBUTETYPE", catalogTypeAttributeType );
                appendCode = appendCode.replace( "$DISABLED", disabled );
                appendCode = appendCode.replace( "$MANDATORY", mandatory );
                appendCode = appendCode.replace( "$SAVEMODE", "false" );

                appendCode = appendCode.replaceAll( "\\$", "\\\\\\$" );
                
                
               // System.out.println( "---appendCode---" + appendCode );
                
                CatalogTypeAttributeMatcher.appendReplacement( sb, appendCode );
            }
        }

        CatalogTypeAttributeMatcher.appendTail( sb );

        return sb.toString();
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
    
    
    /**
     * Returns the value of the CatalogDataValue in the Collection of DataValues
     * with the given data element identifier.
     */
    private CatalogDataValue getValue( Collection<CatalogDataValue> dataValues, int catalogTypeAttributeId )
    {
        for ( CatalogDataValue dataValue : dataValues )
        {
            if ( dataValue.getCatalogTypeAttribute().getId() == catalogTypeAttributeId )
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
    private Map<Integer, CatalogTypeAttribute> getCatalogTypeAttributeMap( CatalogType catalogType )
    {
        Collection<CatalogTypeAttribute> catalogTypeAttributes =  catalogType.getCatalogTypeAttributes();
        
        if ( catalogType == null )
        {
            return null;
        }
        Map<Integer, CatalogTypeAttribute> map = new HashMap<Integer, CatalogTypeAttribute>();

        for ( CatalogTypeAttribute attribute : catalogTypeAttributes )
        {
            map.put( attribute.getId(), attribute );
        }

        return map;
    }
    
    
    
    
}
