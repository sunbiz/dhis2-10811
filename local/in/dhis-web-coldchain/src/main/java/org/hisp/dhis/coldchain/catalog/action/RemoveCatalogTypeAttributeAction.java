package org.hisp.dhis.coldchain.catalog.action;

import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeService;
import org.hisp.dhis.common.DeleteNotAllowedException;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork2.Action;

public class RemoveCatalogTypeAttributeAction
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
            CatalogTypeAttribute catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( id );

            catalogTypeAttributeService.deleteCatalogTypeAttribute( catalogTypeAttribute );
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
            //message = i18n.getString( "object_not_deleted_associated_by_objects" ) + " " + ex.getMessage();
            message = i18n.getString( "object_not_deleted_associated_by_objects" );
           
            return ERROR;
        }
        
        return SUCCESS;
    }
}

