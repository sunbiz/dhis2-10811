package org.hisp.dhis.coldchain.catalog.action;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;

import com.opensymphony.xwork2.Action;

/**
 * @author Mithilesh Kumar Thakur
 *
 * @version CatalogSelectAction.java Jun 22, 2012 1:02:17 PM	
 */
public class CatalogSelectAction implements Action
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
    // Input/output
    // -------------------------------------------------------------------------

    private List<CatalogType> catalogTypes;
    
    public List<CatalogType> getCatalogTypes()
    {
        return catalogTypes;
    }
    
    // -------------------------------------------------------------------------
    // Implementation Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        
        catalogTypes = new ArrayList<CatalogType>( catalogTypeService.getAllCatalogTypes() );
        
        return SUCCESS;
    }

}

