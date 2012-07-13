package org.hisp.dhis.coldchain.catalog.action;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeService;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.paging.ActionPagingSupport;

/**
 * @author Mithilesh Kumar Thakur
 *
 * @version GetCatalogListAction.java Jun 22, 2012 3:30:36 PM	
 */
public class GetCatalogListAction extends ActionPagingSupport<Catalog>
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------
   
    private CatalogService catalogService;
    
    public void setCatalogService( CatalogService catalogService )
    {
        this.catalogService = catalogService;
    }
    
    private CatalogTypeService catalogTypeService;
    
    public void setCatalogTypeService( CatalogTypeService catalogTypeService )
    {
        this.catalogTypeService = catalogTypeService;
    }
    
    private CatalogTypeAttributeService catalogTypeAttributeService;
    
    public void setCatalogTypeAttributeService( CatalogTypeAttributeService catalogTypeAttributeService )
    {
        this.catalogTypeAttributeService = catalogTypeAttributeService;
    }
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
    /*
    private int catalogTypeId;
    
    public void setCatalogTypeId( int catalogTypeId )
    {
        this.catalogTypeId = catalogTypeId;
    }
    */
    
    private String catalogTypeId;
    
    public void setCatalogTypeId( String catalogTypeId )
    {
        this.catalogTypeId = catalogTypeId;
    }

    private Boolean listAll;
    
    public void setListAll( Boolean listAll )
    {
        this.listAll = listAll;
    }
    
    private CatalogType catalogType;
    
    public CatalogType getCatalogType()
    {
        return catalogType;
    }
    
    private Integer total;
    
    public Integer getTotal()
    {
        return total;
    }
    
    private List<Catalog> catalogList;
    
    public List<Catalog> getCatalogList()
    {
        return catalogList;
    }
    /*
    private int catalogTypeAttributeId;
    
    public void setCatalogTypeAttributeId( int catalogTypeAttributeId )
    {
        this.catalogTypeAttributeId = catalogTypeAttributeId;
    }
    */
    
    private String catalogTypeAttributeId;
    
    public void setCatalogTypeAttributeId( String catalogTypeAttributeId )
    {
        this.catalogTypeAttributeId = catalogTypeAttributeId;
    }

    private String searchText;
    
    public String getSearchText()
    {
        return searchText;
    }

    public void setSearchText( String searchText )
    {
        this.searchText = searchText;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        
        catalogType = catalogTypeService.getCatalogType( Integer.parseInt( catalogTypeId ) );
        //catalogType = catalogTypeService.getCatalogType(  catalogTypeId );
        
        if ( listAll != null && listAll )
        {
            listAllCatalog( catalogType );

            return SUCCESS;
        }
        
        CatalogTypeAttribute catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( Integer.parseInt( catalogTypeAttributeId ) );
        //CatalogTypeAttribute catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute(  catalogTypeAttributeId  );
        //System.out.println("catalogTypeAttribute " + catalogTypeAttribute.getName() + "--- catalogType Name " + catalogType.getName() +"--- searchText is  " + searchText  );
        
        listCatalogByFilter( catalogType, catalogTypeAttribute, searchText);
        
        return SUCCESS;
    }
    
    // -------------------------------------------------------------------------
    // Support Methods
    // -------------------------------------------------------------------------
    private void listAllCatalog( CatalogType catalogType )
    {
        total = catalogService.getCountCatalog( catalogType );
        
        this.paging = createPaging( total );
        
        catalogList = new ArrayList<Catalog>( catalogService.getCatalogs( catalogType, paging.getStartPos(), paging.getPageSize() ));
    }
    
    private void listCatalogByFilter( CatalogType catalogType, CatalogTypeAttribute catalogTypeAttribute, String searchKey )
    {
        total = catalogService.getCountCatalog( catalogType, catalogTypeAttribute, searchText );
        
        this.paging = createPaging( total );
        
        catalogList = new ArrayList<Catalog>( catalogService.getCatalogs( catalogType, catalogTypeAttribute, searchText, paging.getStartPos(), paging.getPageSize() ));
    }   
    
}
