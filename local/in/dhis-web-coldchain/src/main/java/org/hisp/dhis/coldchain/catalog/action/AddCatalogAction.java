package org.hisp.dhis.coldchain.catalog.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogDataValue;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOption;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOptionService;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;

import com.opensymphony.xwork2.Action;

public class AddCatalogAction
implements Action
{
    public static final String PREFIX_ATTRIBUTE = "attr";

    //public static final String PREFIX_IDENTIFIER = "iden";

    // -------------------------------------------------------------------------
    // Dependencies
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
    
    private CatalogTypeAttributeOptionService catalogTypeAttributeOptionService;
    
    public void setCatalogTypeAttributeOptionService( CatalogTypeAttributeOptionService catalogTypeAttributeOptionService )
    {
        this.catalogTypeAttributeOptionService = catalogTypeAttributeOptionService;
    }
    /*
    private CatalogDataValueService catalogDataValueService;
    
    public void setCatalogDataValueService( CatalogDataValueService catalogDataValueService )
    {
        this.catalogDataValueService = catalogDataValueService;
    }
    */
    
    // -------------------------------------------------------------------------
    // Input/output Getter/Setter
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

    private String message;

    public String getMessage()
    {
        return message;
    }
    
    private int catalogType;
    
    public void setCatalogType( int catalogType )
    {
        this.catalogType = catalogType;
    }

    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        // ---------------------------------------------------------------------
        // Set Name, Description, CatalogType
        // ---------------------------------------------------------------------

        Catalog catalog = new Catalog();
        catalog.setName( name );
        catalog.setDescription( description );
        
        CatalogType tempcatalogType = catalogTypeService.getCatalogType( catalogType );
        catalog.setCatalogType( tempcatalogType );
        
        //catalogService.addCatalog( catalog );
        

        // -----------------------------------------------------------------------------
        // Prepare catalog type Attributes
        // -----------------------------------------------------------------------------
        
        HttpServletRequest request = ServletActionContext.getRequest();
        
        Collection<CatalogTypeAttribute> catalogTypeAttributes = tempcatalogType.getCatalogTypeAttributes();
        
        List<CatalogDataValue> catalogDataValues = new ArrayList<CatalogDataValue>();
        
        String value = null;
        
        CatalogDataValue catalogDataValue = null;
        
        if ( catalogTypeAttributes != null && catalogTypeAttributes.size() > 0 )
        {
            for ( CatalogTypeAttribute catalogTypeAttribute : catalogTypeAttributes )
            {
                value = request.getParameter( PREFIX_ATTRIBUTE + catalogTypeAttribute.getId() );
                if ( StringUtils.isNotBlank( value ) )
                {
                    catalogDataValue = new CatalogDataValue();
                    catalogDataValue.setCatalog( catalog );
                    catalogDataValue.setCatalogTypeAttribute( catalogTypeAttribute );
                    
                    if ( CatalogTypeAttribute.TYPE_COMBO.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) )
                    {
                        CatalogTypeAttributeOption option = catalogTypeAttributeOptionService.getCatalogTypeAttributeOption( NumberUtils.toInt( value ) );
                        
                        if ( option != null )
                        {
                            catalogDataValue.setCatalogTypeAttributeOption( option );
                            catalogDataValue.setValue( option.getName() );
                        }
                        else
                        {
                            
                        }
                    }
                    else
                    {
                        catalogDataValue.setValue( value.trim() );
                    }
                    
                    catalogDataValues.add( catalogDataValue );
                    
                    //catalogDataValueService.addCatalogDataValue( catalogDataValue );
                }
            }
        }
        /*
        System.out.println( "Size of catalog Data Values  :" + catalogDataValues.size() );
        for( CatalogDataValue  tempcatalogDataValue  : catalogDataValues )
        {
            System.out.println( "Name :" + tempcatalogDataValue.getCatalog().getName() );
            System.out.println( "Catalog Type Attribute Name :" + tempcatalogDataValue.getCatalogTypeAttribute().getName() );
            System.out.println( "Value :" + tempcatalogDataValue.getValue() );
        }
        */
        // -------------------------------------------------------------------------
        // Save catalog
        // -------------------------------------------------------------------------

            
        Integer id = catalogService.createCatalog(  catalog, catalogDataValues );

            message = id + "";

            return SUCCESS;
    }

}

