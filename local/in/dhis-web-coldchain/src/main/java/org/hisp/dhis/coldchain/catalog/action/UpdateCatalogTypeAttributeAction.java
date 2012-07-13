package org.hisp.dhis.coldchain.catalog.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOption;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOptionService;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeService;

import com.opensymphony.xwork2.Action;

public class UpdateCatalogTypeAttributeAction implements Action
{
    public static final String PREFIX_ATTRIBUTE_OPTION = "attrOption";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CatalogTypeAttributeService catalogTypeAttributeService;

    public void setCatalogTypeAttributeService( CatalogTypeAttributeService catalogTypeAttributeService )
    {
        this.catalogTypeAttributeService = catalogTypeAttributeService;
    }
    
    private CatalogTypeAttributeOptionService catalogTypeAttributeOptionService;
    
    public void setCatalogTypeAttributeOptionService( CatalogTypeAttributeOptionService catalogTypeAttributeOptionService )
    {
        this.catalogTypeAttributeOptionService = catalogTypeAttributeOptionService;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private int id;

    public void setId( int id )
    {
        this.id = id;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }

    private String valueType;

    public void setValueType( String valueType )
    {
        this.valueType = valueType;
    }

    private boolean mandatory;

    public void setMandatory( boolean mandatory )
    {
        this.mandatory = mandatory;
    }



    private List<String> attrOptions;

    public void setAttrOptions( List<String> attrOptions )
    {
        this.attrOptions = attrOptions;
    }

    private Integer noChars;

    public void setNoChars( Integer noChars )
    {
        this.noChars = noChars;
    }


    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        
        CatalogTypeAttribute catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( id );

        catalogTypeAttribute.setName( name );
        catalogTypeAttribute.setDescription( description );
        catalogTypeAttribute.setValueType( valueType );
        catalogTypeAttribute.setMandatory( mandatory );
       
        catalogTypeAttribute.setNoChars( noChars );
       
        HttpServletRequest request = ServletActionContext.getRequest();
        
        Collection<CatalogTypeAttributeOption> attributeOptions = catalogTypeAttributeOptionService.getCatalogTypeAttributeOptions( catalogTypeAttribute );

        if ( attributeOptions != null && attributeOptions.size() > 0 )
        {
            String value = null;
            for ( CatalogTypeAttributeOption option : attributeOptions )
            {
                value = request.getParameter( PREFIX_ATTRIBUTE_OPTION + option.getId() );
                if ( StringUtils.isNotBlank( value ) )
                {
                    option.setName( value.trim() );
                    
                    catalogTypeAttributeOptionService.updateCatalogTypeAttributeOption( option );
                }
            }
        }

        if ( attrOptions != null )
        {
            CatalogTypeAttributeOption option = null;
            for ( String optionName : attrOptions )
            {
                
                option = catalogTypeAttributeOptionService.getCatalogTypeAttributeOptionName( catalogTypeAttribute, optionName );
                if ( option == null )
                {
                    option = new CatalogTypeAttributeOption();
                    option.setName( optionName );
                    option.setCatalogTypeAttribute( catalogTypeAttribute );
                    
                    catalogTypeAttributeOptionService.addCatalogTypeAttributeOption( option );
                }
            }
        }
        
        catalogTypeAttributeService.updateCatalogTypeAttribute( catalogTypeAttribute );
    
        return SUCCESS;
    }
}

