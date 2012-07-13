package org.hisp.dhis.coldchain.catalog.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.coldchain.catalog.comparator.CatalogTypeComparator;

import com.opensymphony.xwork2.Action;

public class AddCatalogFormAction implements Action
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
    // Input & Output Getters & Setters
    // -------------------------------------------------------------------------
    
    private List<CatalogType> catalogTypes;
    
    public List<CatalogType> getCatalogTypes()
    {
        return catalogTypes;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        catalogTypes = new ArrayList<CatalogType>( catalogTypeService.getAllCatalogTypes());
        
        Collections.sort( catalogTypes, new CatalogTypeComparator() );
        
        return SUCCESS;
    }
}

