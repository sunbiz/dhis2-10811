package org.hisp.dhis.coldchain.catalog.action;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeService;
import org.hisp.dhis.coldchain.catalog.comparator.CatalogTypeAttributeComparator;
import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.paging.ActionPagingSupport;

//public class GetColdChainCatalogTypeAttributeListAction  implements Action
public class GetColdChainCatalogTypeAttributeListAction  extends ActionPagingSupport<CatalogTypeAttribute>
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
    // Output
    // -------------------------------------------------------------------------
    
    private List<CatalogTypeAttribute> catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>();
    
    public List<CatalogTypeAttribute> getCatalogTypeAttributes()
    {
        return catalogTypeAttributes;
    }
    
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
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        // -------------------------------------------------------------------------
        // Criteria
        // -------------------------------------------------------------------------

        if ( isNotBlank( key ) ) // Filter on key only if set
        {
            this.paging = createPaging( catalogTypeAttributeService.getCatalogTypeAttributeCountByName( key ) );
            
            catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>( catalogTypeAttributeService.getCatalogTypeAttributesBetweenByName( key, paging.getStartPos(), paging.getPageSize() ));
        }
        else
        {
            this.paging = createPaging( catalogTypeAttributeService.getCatalogTypeAttributeCount() );
            
            catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>( catalogTypeAttributeService.getCatalogTypeAttributesBetween( paging.getStartPos(), paging.getPageSize() ));
        }
        
        Collections.sort( catalogTypeAttributes, new IdentifiableObjectNameComparator() );
       
        /*
        catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>( catalogTypeAttributeService.getAllCatalogTypeAttributes() );
        
        Collections.sort( catalogTypeAttributes, new CatalogTypeAttributeComparator() );
        */
        return SUCCESS;
    }
    
}
