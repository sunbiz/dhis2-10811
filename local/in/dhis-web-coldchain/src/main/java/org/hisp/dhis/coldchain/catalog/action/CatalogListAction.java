package org.hisp.dhis.coldchain.catalog.action;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.paging.ActionPagingSupport;
import org.hisp.dhis.user.UserSettingService;

//public class CatalogListAction implements Action
public class CatalogListAction extends ActionPagingSupport<Catalog>
{
    
    final String KEY_CURRENT_CATALOG = "currentCatalog";
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
    
    private UserSettingService userSettingService;

    public void setUserSettingService( UserSettingService userSettingService )
    {
        this.userSettingService = userSettingService;
    }
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------
    
    private List<Catalog> catalogs = new ArrayList<Catalog>();
    
    public List<Catalog> getCatalogs()
    {
        return catalogs;
    }

    private Integer id;

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }
    
    private List<CatalogType> catalogTypes;
    
    public List<CatalogType> getCatalogTypes()
    {
        return catalogTypes;
    }
    
    private CatalogType catalogType;
    
    public CatalogType getCatalogType()
    {
        return catalogType;
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
       
        if ( id == null ) // None, get current data dictionary
        {
            id = (Integer) userSettingService.getUserSetting( KEY_CURRENT_CATALOG );
        }
        else if ( id == -1 ) // All, reset current data dictionary
        {
            userSettingService.saveUserSetting( KEY_CURRENT_CATALOG, null );
            
            id = null;
        }
        else // Specified, set current data dictionary
        {
            userSettingService.saveUserSetting( KEY_CURRENT_CATALOG, id );
        }
        
        catalogTypes = new ArrayList<CatalogType>( catalogTypeService.getAllCatalogTypes());
        Collections.sort( catalogTypes, IdentifiableObjectNameComparator.INSTANCE );
        
        // -------------------------------------------------------------------------
        // Criteria
        // -------------------------------------------------------------------------

        if ( isNotBlank( key ) ) // Filter on key only if set
        {
            this.paging = createPaging( catalogService.getCatalogCountByName( key ) );
            
            catalogs = new ArrayList<Catalog>( catalogService.getCatalogsBetweenByName( key, paging.getStartPos(), paging.getPageSize() ) );
        }
        
        else if ( id != null )
        {
            catalogType = catalogTypeService.getCatalogType( id );
            
            catalogs = new ArrayList<Catalog>( catalogService.getCatalogs( catalogType ) );
            
            this.paging = createPaging( catalogs.size() );
            
            catalogs = getBlockElement( catalogs, paging.getStartPos(), paging.getPageSize() );
        }
        
        else
        {
            this.paging = createPaging( catalogService.getCatalogCount() );
            
            catalogs = new ArrayList<Catalog>( catalogService.getCatalogsBetween( paging.getStartPos(), paging.getPageSize() ) );
        }
        
        Collections.sort( catalogs, new IdentifiableObjectNameComparator() );
        
        /*
        catalogTypes = new ArrayList<CatalogType>( catalogTypeService.getAllCatalogTypes());
        Collections.sort( catalogTypes, new CatalogTypeComparator() );
        
        if ( id != null )
        {
            catalogType = catalogTypeService.getCatalogType( id );
            
            catalogs = new ArrayList<Catalog>( catalogService.getCatalogs( catalogType ) );
        }
        else
        {
            catalogs = new ArrayList<Catalog>( catalogService.getAllCatalogs() );
        }
        Collections.sort( catalogs, new CatalogComparator() );
        */
        return SUCCESS;
    }
    
}
