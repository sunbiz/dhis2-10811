package org.hisp.dhis.coldchain.catalog.action;

import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.common.DeleteNotAllowedException;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork2.Action;

public class RemoveCatalogAction
implements Action
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private CatalogService catalogService;
    
    public void setCatalogService( CatalogService catalogService )
    {
        this.catalogService = catalogService;
    }
    /*
    private CatalogDataValueService catalogDataValueService;
    
    
    public void setCatalogDataValueService( CatalogDataValueService catalogDataValueService )
    {
        this.catalogDataValueService = catalogDataValueService;
    }
    */

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private int id;

    public void setId( int id )
    {
        this.id = id;
    }
    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    private String message;

    public String getMessage()
    {
        return message;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        try
        {
            Catalog catalog = catalogService.getCatalog( id );
            
            //catalogService.deleteCatalogData( catalog );
            
            //catalogService.deleteCatalogData( catalogService.getCatalog( id ) );
            
            //Collection<CatalogDataValue> valuesForDelete = catalogDataValueService.getAllCatalogDataValuesByCatalog( catalogService.getCatalog( id )) ;
            
            catalogService.deleteCatalogAndDataValue( catalog );
           
            catalogService.deleteCatalog( catalog );
            
        }
        catch ( DeleteNotAllowedException ex )
        {
            if ( ex.getErrorCode().equals( DeleteNotAllowedException.ERROR_ASSOCIATED_BY_OTHER_OBJECTS ) )
            {
                message = i18n.getString( "object_not_deleted_associated_by_objects" ) + " " + ex.getMessage();
            }
            
            return INPUT;
        }
        
        catch ( Exception ex )
        {
            message = i18n.getString( "object_not_deleted_associated_by_objects" );
           
            return ERROR;
        }
        
        
        return SUCCESS;
    }
}

