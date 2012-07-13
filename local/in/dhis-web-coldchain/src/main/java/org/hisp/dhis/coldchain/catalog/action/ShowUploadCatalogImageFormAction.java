package org.hisp.dhis.coldchain.catalog.action;

import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogService;

import com.opensymphony.xwork2.Action;

public class ShowUploadCatalogImageFormAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private CatalogService catalogService;
    
    public void setCatalogService( CatalogService catalogService )
    {
        this.catalogService = catalogService;
    }

    // -------------------------------------------------------------------------
    // Input/Output and Getter / Setter
    // -------------------------------------------------------------------------

    private int id;
    
    public void setId( int id )
    {
        this.id = id;
    }

    private Catalog catalog;
    
    public Catalog getCatalog()
    {
        return catalog;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        catalog = catalogService.getCatalog( id );

        return SUCCESS;
    }

}

