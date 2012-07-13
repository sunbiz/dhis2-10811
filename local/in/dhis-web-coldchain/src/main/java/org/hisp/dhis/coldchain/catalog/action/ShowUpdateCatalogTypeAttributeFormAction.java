package org.hisp.dhis.coldchain.catalog.action;

import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeService;

import com.opensymphony.xwork2.Action;

public class ShowUpdateCatalogTypeAttributeFormAction
implements Action
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private CatalogTypeAttributeService catalogTypeAttributeService;

    public void setCatalogTypeAttributeService( CatalogTypeAttributeService catalogTypeAttributeService )
    {
        this.catalogTypeAttributeService = catalogTypeAttributeService;
    }


    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private int id;

    public void setId( int id )
    {
        this.id = id;
    }

    private CatalogTypeAttribute catalogTypeAttribute;

    public CatalogTypeAttribute getCatalogTypeAttribute()
    {
        return catalogTypeAttribute;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( id );
        
        return SUCCESS;
    }
}
