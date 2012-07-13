package org.hisp.dhis.de.action;

import java.util.Collection;
import java.util.Date;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataelement.Operand;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.de.state.SelectedStateManager;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.vn.chr.ElementService;
import org.hisp.dhis.vn.chr.FormReport;
import org.hisp.dhis.vn.chr.FormReportService;
import org.hisp.dhis.vn.chr.jdbc.FormManager;

/**
 * @author Chau Thu Tran
 * 
 */

public class ExportDataAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private FormReportService formReportService;

    private FormManager formManager;

    private CurrentUserService currentUserService;

    private DataElementService dataElementService;

    private DataValueService dataValueService;

    private SelectedStateManager selectedStateManager;

    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    private ElementService elementService;

    private ExpressionService expressionService;

    // -------------------------------------------------------------------------
    // Input && Output
    // -------------------------------------------------------------------------

    private int dataElementId;

    private int optionComboId;

    private int statusCode;

    private Date timestamp;

    private String storedBy;

    private String inputId;

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public void setFormManager( FormManager formManager )
    {
        this.formManager = formManager;
    }

    public void setFormReportService( FormReportService formReportService )
    {
        this.formReportService = formReportService;
    }

    public void setExpressionService( ExpressionService expressionService )
    {
        this.expressionService = expressionService;
    }

    public void setDataElementCategoryOptionComboService(
        DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    public void setElementService( ElementService elementService )
    {
        this.elementService = elementService;
    }

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    public void setDataElementId( int dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public int getDataElementId()
    {
        return dataElementId;
    }

    public void setOptionComboId( int optionComboId )
    {
        this.optionComboId = optionComboId;
    }

    public int getOptionComboId()
    {
        return optionComboId;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setInputId( String inputId )
    {
        this.inputId = inputId;
    }

    public String getInputId()
    {
        return inputId;
    }

    public String getStoredBy()
    {
        return storedBy;
    }

    // --------------------------------------------------------------------
    // Implements
    // --------------------------------------------------------------------

    public String execute()
        throws Exception
    {

        // Get List Of formreports
        Collection<FormReport> formReports = formReportService.getAllFormReports();

        // Current OrgUnit - sourceid
        OrganisationUnit organisationUnit = selectedStateManager.getSelectedOrganisationUnit();
        // Current period
        Period period = selectedStateManager.getSelectedPeriod();

        for ( FormReport formReport : formReports )
        {

            // Calculator value of the report
            int value = formManager.reportDataStatement( "count", period, formReport );

            if ( value != 0 )
            {
                // Get operand of the element
                Operand operand = expressionService.getOperandsInExpression( formReport.getOperand() ).iterator()
                    .next();
                // Create a Datavalue Object
                DataElement dataElement = dataElementService.getDataElement( operand.getDataElementId() );

                DataElementCategoryOptionCombo optionCombo = dataElementCategoryOptionComboService
                    .getDataElementCategoryOptionCombo( operand.getOptionComboId() );

                // logged username
                storedBy = currentUserService.getCurrentUsername();

                DataValue dataValue = dataValueService
                    .getDataValue( organisationUnit, dataElement, period, optionCombo );

                // if dataValue is not exist, that means data not input
                // add value into database
                if ( dataValue == null )
                {

                    dataValue = new DataValue( dataElement, period, organisationUnit, value + "", storedBy, new Date(),
                        null, optionCombo );
                    dataValueService.addDataValue( dataValue );

                }
                // if dataValue is exist, update new value
                else
                {

                    dataValue.setValue( value + "" );
                    dataValue.setTimestamp( new Date() );
                    dataValue.setStoredBy( storedBy );

                    dataValueService.updateDataValue( dataValue );
                }
            }// end if (value != 0)

        }// end if(value > 0)

        message = i18n.getString( "success" );

        return SUCCESS;
    }

}
