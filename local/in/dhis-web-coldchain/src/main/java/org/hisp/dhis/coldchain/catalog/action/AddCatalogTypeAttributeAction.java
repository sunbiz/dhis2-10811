package org.hisp.dhis.coldchain.catalog.action;

import java.util.List;

import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOption;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOptionService;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeService;

import com.opensymphony.xwork2.Action;

public class AddCatalogTypeAttributeAction implements Action
{
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
    
    private Integer noChars;

    public void setNoChars( Integer noChars )
    {
        this.noChars = noChars;
    }
    
    private List<String> attrOptions;

    public void setAttrOptions( List<String> attrOptions )
    {
        this.attrOptions = attrOptions;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    
    public String execute() throws Exception
    {
        CatalogTypeAttribute catalogTypeAttribute = new CatalogTypeAttribute();
        
        catalogTypeAttribute.setName( name );
        catalogTypeAttribute.setDescription( description );
        catalogTypeAttribute.setValueType( valueType );
        catalogTypeAttribute.setMandatory( mandatory );
        catalogTypeAttribute.setNoChars( noChars );
        
        catalogTypeAttributeService.addCatalogTypeAttribute( catalogTypeAttribute );
        
        if ( CatalogTypeAttribute.TYPE_COMBO.equalsIgnoreCase( valueType ) )
        {
            CatalogTypeAttributeOption option = null;
            for ( String optionName : attrOptions )
            {
                option = new CatalogTypeAttributeOption();
                option.setName( optionName );
                option.setCatalogTypeAttribute( catalogTypeAttribute );
                
                catalogTypeAttributeOptionService.addCatalogTypeAttributeOption( option );
            }
        }
        
        return SUCCESS;
    }
    
    
}
