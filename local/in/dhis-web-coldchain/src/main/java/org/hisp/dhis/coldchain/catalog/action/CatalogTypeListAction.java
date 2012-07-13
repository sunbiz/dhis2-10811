package org.hisp.dhis.coldchain.catalog.action;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.paging.ActionPagingSupport;

public class CatalogTypeListAction
extends ActionPagingSupport<CatalogType>
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
    // Getters & Setters
    // -------------------------------------------------------------------------
    
    private List<CatalogType> catalogTypes;
    
    public List<CatalogType> getCatalogTypes()
    {
        return catalogTypes;
    }
    
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------


    private String key;

    public String getKey()
    {
        return key;
    }

    public void setKey( String key )
    {
        this.key = key;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        
        if ( isNotBlank( key ) ) // Filter on key only if set
        {
            this.paging = createPaging( catalogTypeService.getCatalogTypeCountByName( key ) );
            
            catalogTypes = new ArrayList<CatalogType>( catalogTypeService.getCatalogTypesBetweenByName( key, paging.getStartPos(), paging.getPageSize() ));
        }
        else
        {
            this.paging = createPaging( catalogTypeService.getCatalogTypeCount() );
            
            catalogTypes = new ArrayList<CatalogType>( catalogTypeService.getCatalogTypesBetween( paging.getStartPos(), paging.getPageSize() ));
        }
        /*
        catalogTypes = new ArrayList<CatalogType>( catalogTypeService.getAllCatalogTypes());
        Collections.sort( catalogTypes, new CatalogTypeComparator() );
        */
        
        Collections.sort( catalogTypes, new IdentifiableObjectNameComparator() );

        return SUCCESS;
    }
}

