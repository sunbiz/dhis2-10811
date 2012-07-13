package org.hisp.dhis.coldchain.catalog.action;

import java.util.ArrayList;
import java.util.Collection;

import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;

import com.opensymphony.xwork2.Action;

public class UpdateCatalogTypeFormAction
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

    public String execute() throws Exception
    {
       
        //System.out.println( id );
        catalogType = catalogTypeService.getCatalogType( id );
        
        //System.out.println( catalogType.getId() + "-----"+catalogType.getDescription());
        
        catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>( catalogType.getCatalogTypeAttributes());        
        //catalogTypeAttributes = catalogType.getCatalogTypeAttributes();
        
        //programStage = programStageService.getProgramStage( id );

        //programStageDataElements = programStage.getProgramStageDataElements();

        return SUCCESS;
    }
}

