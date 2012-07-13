package org.hisp.dhis.coldchain.catalog.dataentryform.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.dataentryform.DataEntryForm;
import org.hisp.dhis.dataentryform.DataEntryFormService;

import com.opensymphony.xwork2.Action;

/**
 * @author Mithilesh Kumar Thakur
 *
 * @version SaveCatalogDataEntryFormAction.java Jun 7, 2012 4:09:05 PM	
 */
public class SaveCatalogDataEntryFormAction implements Action
{
    Log logger = LogFactory.getLog( getClass() );

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
    // Getters & Setters
    // -------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String designTextarea;

    public void setDesignTextarea( String designTextarea )
    {
        this.designTextarea = designTextarea;
    }

    private Integer catalogTypeId;
    
    public void setCatalogTypeId( Integer catalogTypeId )
    {
        this.catalogTypeId = catalogTypeId;
    }


    private Integer catalogDataEntryFormId;
    
    public void setCatalogDataEntryFormId( Integer catalogDataEntryFormId )
    {
        this.catalogDataEntryFormId = catalogDataEntryFormId;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        CatalogType catalogType = catalogTypeService.getCatalogType( catalogTypeId );
        
        DataEntryForm catalogDataEntryForm = null;

        // ---------------------------------------------------------------------
        // Get data-entry-form
        // ---------------------------------------------------------------------

        if ( catalogDataEntryFormId == null )
        {
            catalogDataEntryForm = catalogType.getDataEntryForm();
            
        }
        else
        {
            catalogDataEntryForm = dataEntryFormService.getDataEntryForm( catalogDataEntryFormId );
        }

        // ---------------------------------------------------------------------
        // Save data-entry-form
        // ---------------------------------------------------------------------

        if ( catalogDataEntryForm == null )
        {
            catalogDataEntryForm = new DataEntryForm( name, dataEntryFormService.prepareDataEntryFormForSave( designTextarea ) );
            dataEntryFormService.addDataEntryForm( catalogDataEntryForm );
        }
        else
        {
            catalogDataEntryForm.setName( name );
            catalogDataEntryForm.setHtmlCode( dataEntryFormService.prepareDataEntryFormForSave( designTextarea ) );
            dataEntryFormService.updateDataEntryForm( catalogDataEntryForm );
        }            
        
        catalogType.setDataEntryForm( catalogDataEntryForm );
        catalogTypeService.updateCatalogType( catalogType );

        return SUCCESS;
    }
    
}

