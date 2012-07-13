package org.hisp.dhis.coldchain.catalog.dataentryform.action;

import org.hisp.dhis.dataentryform.DataEntryForm;
import org.hisp.dhis.dataentryform.DataEntryFormService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork2.Action;

/**
 * @author Mithilesh Kumar Thakur
 *
 * @version ValidateCatalogDataEntryFormAction.java Jun 7, 2012 2:42:06 PM	
 */
public class ValidateCatalogDataEntryFormAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private DataEntryFormService dataEntryFormService;

    public void setDataEntryFormService( DataEntryFormService dataEntryFormService )
    {
        this.dataEntryFormService = dataEntryFormService;
    }

    // -------------------------------------------------------------------------
    // I18n
    // -------------------------------------------------------------------------

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private Integer catalogDataEntryFormId;
    
    public void setCatalogDataEntryFormId( Integer catalogDataEntryFormId )
    {
        this.catalogDataEntryFormId = catalogDataEntryFormId;
    }


    private String message;

    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        DataEntryForm match = dataEntryFormService.getDataEntryFormByName( name );

        if ( match != null && ( catalogDataEntryFormId == null || match.getId() != catalogDataEntryFormId.intValue()) )
        {
            message = i18n.getString( "duplicate_names" );

            return ERROR;
        }

        return SUCCESS;
    }
}

