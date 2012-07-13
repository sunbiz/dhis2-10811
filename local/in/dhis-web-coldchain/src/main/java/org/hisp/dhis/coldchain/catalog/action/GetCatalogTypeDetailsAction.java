package org.hisp.dhis.coldchain.catalog.action;

import java.util.Collection;

import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;

import com.opensymphony.xwork2.Action;

public class GetCatalogTypeDetailsAction
implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private CatalogTypeService catalogTypeService;
    
    public void setCatalogTypeService( CatalogTypeService catalogTypeService )
    {
        this.catalogTypeService = catalogTypeService;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private int id;

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    private CatalogType catalogType;

    public CatalogType getCatalogType()
    {
        return catalogType;
    }

    private Collection<CatalogTypeAttribute> catalogTypeAttributes;
    
    public Collection<CatalogTypeAttribute> getCatalogTypeAttributes()
    {
        return catalogTypeAttributes;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        
        catalogType = catalogTypeService.getCatalogType( id );
        //catalogType.getDataEntryForm().getName();
        catalogTypeAttributes = catalogType.getCatalogTypeAttributes();
        
        return SUCCESS;
    }
}

