package org.hisp.dhis.coldchain.catalog.action;

import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOption;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOptionService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork2.Action;

public class RemoveCatalogTypeAttributeOptionAction
implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CatalogTypeAttributeOptionService catalogTypeAttributeOptionService;
    
    public void setCatalogTypeAttributeOptionService( CatalogTypeAttributeOptionService catalogTypeAttributeOptionService )
    {
        this.catalogTypeAttributeOptionService = catalogTypeAttributeOptionService;
    }
    
    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private int id;

    private String message;

    private I18n i18n;

    // -------------------------------------------------------------------------
    // Getter && Setter
    // -------------------------------------------------------------------------


    public void setId( int id )
    {
        this.id = id;
    }

    public String getMessage()
    {
        return message;
    }

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        CatalogTypeAttributeOption catalogTypeAttributeOption = catalogTypeAttributeOptionService.getCatalogTypeAttributeOption( id );
       
        if ( catalogTypeAttributeOption != null )
        {
            /*
            int count = catalogTypeAttributeOptionService.countByCatalogTypeAttributeoption( catalogTypeAttributeOption );
            if ( count > 0 )
            {
                message = i18n.getString( "warning_delete_catalogType_attribute_option" );
                return INPUT;
            }
            */
            //else
            //{
                catalogTypeAttributeOptionService.deleteCatalogTypeAttributeOption( catalogTypeAttributeOption );
                message = i18n.getString( "success_delete_ctalogType_attribute_option" );
                return SUCCESS;
            //}
        }
        else
        {
            message = i18n.getString( "error_delete_catalogType_attribute_option" );
            return ERROR;
        }

    }

}

