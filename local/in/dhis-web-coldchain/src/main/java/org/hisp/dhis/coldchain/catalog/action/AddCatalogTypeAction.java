package org.hisp.dhis.coldchain.catalog.action;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeService;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;

import com.opensymphony.xwork2.Action;

public class AddCatalogTypeAction
implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
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

    private List<Integer> selectedCatalogTypeAttributesValidator = new ArrayList<Integer>();

    
    public void setSelectedCatalogTypeAttributesValidator( List<Integer> selectedCatalogTypeAttributesValidator )
    {
        this.selectedCatalogTypeAttributesValidator = selectedCatalogTypeAttributesValidator;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {

        CatalogType catalogType = new CatalogType();
        
        catalogType.setName( name );
        catalogType.setDescription( description );
        //catalogType.getCatalogTypeAttributes().add( arg0 )
        //catalogTypeService.addCatalogType( catalogType );
       
        List<CatalogTypeAttribute> catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>();
        
        for ( int i = 0; i < this.selectedCatalogTypeAttributesValidator.size(); i++ )
        {
            CatalogTypeAttribute catalogTypeAttribute = catalogTypeAttributeService.getCatalogTypeAttribute( selectedCatalogTypeAttributesValidator.get( i ) );
            
            catalogTypeAttributes.add( catalogTypeAttribute );
            
        }
        catalogType.setCatalogTypeAttributes( catalogTypeAttributes );
        
        catalogTypeService.addCatalogType( catalogType );
        
        return SUCCESS;
    }
}
