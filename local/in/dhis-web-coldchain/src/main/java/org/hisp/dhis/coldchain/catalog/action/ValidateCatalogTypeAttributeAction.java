package org.hisp.dhis.coldchain.catalog.action;

import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork2.Action;

public class ValidateCatalogTypeAttributeAction
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

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String message;

    public String getMessage()
    {
        return message;
    }

    private I18n i18n;

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
        name = name.trim();
        
        CatalogTypeAttribute match = catalogTypeAttributeService.getCatalogTypeAttributeByName( name );

        if ( match != null && (id == null || match.getId() != id.intValue()) )
        {
            message = i18n.getString( "name_in_use" );

            return INPUT;
        }

        message = i18n.getString( "everything_is_ok" );

        return SUCCESS;
    }
}
