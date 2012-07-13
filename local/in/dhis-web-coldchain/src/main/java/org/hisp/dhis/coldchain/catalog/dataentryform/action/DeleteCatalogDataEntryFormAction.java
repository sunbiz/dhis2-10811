package org.hisp.dhis.coldchain.catalog.dataentryform.action;

import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.dataentryform.DataEntryForm;
import org.hisp.dhis.dataentryform.DataEntryFormService;

import com.opensymphony.xwork2.Action;

/**
 * @author Mithilesh Kumar Thakur
 *
 * @version DeleteCatalogDataEntryFormAction.java Jun 8, 2012 4:33:05 PM	
 */
public class DeleteCatalogDataEntryFormAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataEntryFormService dataEntryFormService;

    public void setDataEntryFormService( DataEntryFormService dataEntryFormService )
    {
        this.dataEntryFormService = dataEntryFormService;
    }

   private CatalogTypeService catalogTypeService;
    
    public void setCatalogTypeService( CatalogTypeService catalogTypeService )
    {
        this.catalogTypeService = catalogTypeService;
    }

    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------

    private Integer dataEntryFormId;
    
    public void setDataEntryFormId( Integer dataEntryFormId )
    {
        this.dataEntryFormId = dataEntryFormId;
    }


    private String message;

    public String getMessage()
    {
        return message;
    }

    private Integer catalogTypeId;

    public void setCatalogTypeId( Integer catalogTypeId )
    {
        this.catalogTypeId = catalogTypeId;
    }


    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        DataEntryForm dataEntryForm = dataEntryFormService.getDataEntryForm( dataEntryFormId );

        CatalogType catalogType = catalogTypeService.getCatalogType( catalogTypeId );
        
        DataEntryForm catalogTypeDataEntryForm = catalogType.getDataEntryForm();
        
        if ( catalogTypeDataEntryForm != null && catalogTypeDataEntryForm.equals( dataEntryForm ) )
        {
            catalogType.setDataEntryForm( null );

            catalogTypeService.updateCatalogType( catalogType );
        }

        dataEntryFormService.deleteDataEntryForm( dataEntryForm );

        return SUCCESS;
    }
}

