package org.hisp.dhis.startup;

import static org.hisp.dhis.dataentryform.DataEntryFormService.*;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.constant.Constant;
import org.hisp.dhis.constant.ConstantService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataentryform.DataEntryForm;
import org.hisp.dhis.dataentryform.DataEntryFormService;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.system.startup.AbstractStartupRoutine;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Upgrades indicator formulas, expressions (for validation rules) and custom
 * data entry forms from using identifiers to using uids.
 * 
 * @author Lars Helge Overland
 */
public class ExpressionUpgrader
    extends AbstractStartupRoutine
{
    private static final String OLD_OPERAND_EXPRESSION = "\\[(\\d+)\\.?(\\d*)\\]";
    private static final String OLD_CONSTANT_EXPRESSION = "\\[C(\\d+?)\\]";

    private final Pattern OLD_OPERAND_PATTERN = Pattern.compile( OLD_OPERAND_EXPRESSION );
    private final Pattern OLD_CONSTANT_PATTERN = Pattern.compile( OLD_CONSTANT_EXPRESSION );

    private static final Log log = LogFactory.getLog( ExpressionUpgrader.class );
    
    @Autowired
    private DataEntryFormService dataEntryFormService;
    
    @Autowired
    private DataElementService dataElementService;
    
    @Autowired
    private DataElementCategoryService categoryService;
    
    @Autowired
    private IndicatorService indicatorService;
    
    @Autowired
    private ConstantService constantService;
    
    @Autowired
    private ExpressionService expressionService;
    
    @Override
    public void execute()
        throws Exception
    {
        upgradeIndicators();
        upgradeExpressions();
        upgradeDataEntryForms();
    }
    
    private void upgradeIndicators()
    {
        Collection<Indicator> indicators = indicatorService.getAllIndicators();
        
        for ( Indicator indicator : indicators )
        {
            String numerator = upgradeExpression( indicator.getNumerator() );
            String denominator = upgradeExpression( indicator.getDenominator() );
            
            if ( numerator != null || denominator != null )
            {
                indicator.setNumerator( numerator );
                indicator.setDenominator( denominator );
                indicatorService.updateIndicator( indicator );
            }
        }
    }

    private void upgradeExpressions()
    {
        Collection<Expression> expressions = expressionService.getAllExpressions();
        
        for ( Expression expression : expressions )
        {
            String expr = upgradeExpression( expression.getExpression() );
            
            if ( expr != null )
            {
                expression.setExpression( expr );
                expressionService.updateExpression( expression );
            }
        }
    }
    
    private String upgradeExpression( String expression )
    {
        if ( expression == null || expression.trim().isEmpty() )
        {
            return null;
        }
        
        boolean changes = false;
        
        StringBuffer sb = new StringBuffer();

        try
        {
            // -----------------------------------------------------------------
            // Constants
            // -----------------------------------------------------------------
    
            Matcher matcher = OLD_CONSTANT_PATTERN.matcher( expression );
            
            while ( matcher.find() )
            {
                Constant constant = constantService.getConstant( Integer.parseInt( matcher.group( 1 ) ) );
                String replacement = "C{" + constant.getUid() + "}";
                matcher.appendReplacement( sb, replacement );
                changes = true;
            }
            
            matcher.appendTail( sb );
            expression = sb.toString();

            // -----------------------------------------------------------------
            // Operands
            // -----------------------------------------------------------------
    
            matcher = OLD_OPERAND_PATTERN.matcher( expression );
            sb = new StringBuffer();
            
            while ( matcher.find() )
            {
                DataElement de = dataElementService.getDataElement( Integer.parseInt( matcher.group( 1 ) ) );
                String replacement = "#{" + de.getUid();
                
                if ( matcher.groupCount() == 2 && matcher.group( 2 ) != null && !matcher.group( 2 ).trim().isEmpty() )
                {
                    DataElementCategoryOptionCombo coc = categoryService.getDataElementCategoryOptionCombo( Integer.parseInt( matcher.group( 2 ) ) );
                    replacement += "." + coc.getUid();
                }
                
                replacement += "}";
                matcher.appendReplacement( sb, replacement );                
                changes = true;
            }
    
            matcher.appendTail( sb );
            expression = sb.toString();            
        }
        catch ( Exception ex )
        {
            log.error( "Failed to upgrade expression: " + expression );
            log.error( ex ); // Log and continue
        }
        
        if ( changes )
        {
            log.info( "Upgraded expression: " + expression );
        }
        
        return changes ? expression : null;
    }
    
    private void upgradeDataEntryForms()
    {
        Collection<DataEntryForm> forms = dataEntryFormService.getAllDataEntryForms();
        
        for ( DataEntryForm form : forms )
        {
            if ( DataEntryForm.CURRENT_FORMAT > form.getFormat() && form.getHtmlCode() != null && !form.getHtmlCode().trim().isEmpty() )
            {
                try
                {
                    // ---------------------------------------------------------
                    // Identifiers
                    // ---------------------------------------------------------

                    Matcher matcher = IDENTIFIER_PATTERN.matcher( form.getHtmlCode() );
                    StringBuffer sb = new StringBuffer();
                    
                    while ( matcher.find() )
                    {
                        DataElement de = dataElementService.getDataElement( Integer.parseInt( matcher.group( 1 ) ) );
                        DataElementCategoryOptionCombo coc = categoryService.getDataElementCategoryOptionCombo( Integer.parseInt( matcher.group( 2 ) ) );                        
                        String replacement = "id=\"" + de.getUid() + "-" + coc.getUid() + "-val\"";                        
                        matcher.appendReplacement( sb, replacement );
                    }
                    
                    matcher.appendTail( sb );                    
                    form.setHtmlCode( sb.toString() );

                    // ---------------------------------------------------------
                    // Data element totals
                    // ---------------------------------------------------------

                    matcher = DATAELEMENT_TOTAL_PATTERN.matcher( form.getHtmlCode() );
                    sb = new StringBuffer();
                    
                    while ( matcher.find() )
                    {
                        DataElement de = dataElementService.getDataElement( Integer.parseInt( matcher.group( 1 ) ) );
                        String replacement = "dataelementid=\"" + de.getUid() + "\"";
                        matcher.appendReplacement( sb, replacement );
                    }

                    matcher.appendTail( sb );                    
                    form.setHtmlCode( sb.toString() );

                    // ---------------------------------------------------------
                    // Indicators
                    // ---------------------------------------------------------

                    matcher = INDICATOR_PATTERN.matcher( form.getHtmlCode() );
                    sb = new StringBuffer();
                    
                    while ( matcher.find() )
                    {
                        Indicator in = indicatorService.getIndicator( Integer.parseInt( matcher.group( 1 ) ) );
                        String replacement = "indicatorid=\"" + in.getUid() + "\"";
                        matcher.appendReplacement( sb, replacement );
                    }

                    matcher.appendTail( sb );                    
                    form.setHtmlCode( sb.toString() );

                    // ---------------------------------------------------------
                    // Dynamic input
                    // ---------------------------------------------------------

                    matcher = DYNAMIC_INPUT_PATTERN.matcher( form.getHtmlCode() );
                    sb = new StringBuffer();
                    
                    while ( matcher.find() )
                    {
                        DataElementCategoryOptionCombo coc = categoryService.getDataElementCategoryOptionCombo( Integer.parseInt( matcher.group( 2 ) ) );
                        String replacement = matcher.group( 1 ) + "-" + coc.getUid() + "-dyninput";
                        matcher.appendReplacement( sb, replacement );
                    }

                    matcher.appendTail( sb );                    
                    form.setHtmlCode( sb.toString() );

                    // ---------------------------------------------------------
                    // Dynamic select
                    // ---------------------------------------------------------

                    matcher = DYNAMIC_SELECT_PATTERN.matcher( form.getHtmlCode() );
                    sb = new StringBuffer();
                    
                    while ( matcher.find() )
                    {
                        DataElementCategoryCombo cc = categoryService.getDataElementCategoryCombo( Integer.parseInt( matcher.group( 1 ) ) );
                        String replacement = "dynselect=\"" + cc.getUid() + "\"";
                        matcher.appendReplacement( sb, replacement );
                    }

                    matcher.appendTail( sb );                    
                    form.setHtmlCode( sb.toString() );

                    // ---------------------------------------------------------
                    // Update format and save
                    // ---------------------------------------------------------

                    form.setFormat( DataEntryForm.CURRENT_FORMAT );                    
                    dataEntryFormService.updateDataEntryForm( form );
                    
                    log.info( "Upgraded custom data entry form: " + form.getName() );
                }
                catch ( Exception ex )
                {
                    log.error( "Upgrading custom data entry form failed: " + form.getName() );
                    log.error( ex ); // Log and continue
                }
            }
        }
    }
}
