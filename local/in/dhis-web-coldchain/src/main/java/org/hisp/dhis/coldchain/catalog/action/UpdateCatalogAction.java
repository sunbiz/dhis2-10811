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
import org.hisp.dhis.coldchain.catalog.CatalogDataValueService;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOption;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOptionService;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;

import com.opensymphony.xwork2.Action;

public class UpdateCatalogAction
implements Action
{
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
    
    private CatalogDataValueService catalogDataValueService;
    
    
    public void setCatalogDataValueService( CatalogDataValueService catalogDataValueService )
    {
        this.catalogDataValueService = catalogDataValueService;
    }
    
    // -------------------------------------------------------------------------
    // Input/output and Getter/Setter
    // -------------------------------------------------------------------------
    
    private int catalogID;
    
    public void setCatalogID( int catalogID )
    {
        this.catalogID = catalogID;
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

    private String message;

    public String getMessage()
    {
        return message;
    }
    /*
    private int catalogType;
    
    public void setCatalogType( int catalogType )
    {
        this.catalogType = catalogType;
    }
    */

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private Catalog catalog;

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        
        //System.out.println( "catalog id is   :" + catalogID );
        
        catalog = catalogService.getCatalog( catalogID );
        
        // ---------------------------------------------------------------------
        // Set name, description, and  catalogType
        // ---------------------------------------------------------------------
        
        catalog.setName( name );
        catalog.setDescription( description );
        
        CatalogType catalogType = catalogTypeService.getCatalogType( catalog.getCatalogType().getId() );
        
        catalog.setCatalogType( catalogType );
        
        //System.out.println( " catalog Name  is   :" + catalog.getName() + "---- catalogType id is " + catalogType.getId() + " --catalog Type name is " + catalogType.getName());

        // --------------------------------------------------------------------------------------------------------
        // Save catalog Attributes
        // -----------------------------------------------------------------------------------------------------
        
        HttpServletRequest request = ServletActionContext.getRequest();

        String value = null;
        
        CatalogDataValue catalogDataValue = null;
        
        Collection<CatalogTypeAttribute> catalogTypeAttributes = catalogType.getCatalogTypeAttributes();
        
        List<CatalogDataValue> valuesForSave = new ArrayList<CatalogDataValue>();
        List<CatalogDataValue> valuesForUpdate = new ArrayList<CatalogDataValue>();
        Collection<CatalogDataValue> valuesForDelete = null;
        
        
        if ( catalogTypeAttributes != null && catalogTypeAttributes.size() > 0 )
        {
            //catalog.getCatalogType().getCatalogTypeAttributes().clear();
            valuesForDelete = catalogDataValueService.getAllCatalogDataValuesByCatalog( catalogService.getCatalog( catalogID )) ;
            
            for ( CatalogTypeAttribute catalogTypeAttribute : catalogTypeAttributes )
            {
                value = request.getParameter( AddCatalogAction.PREFIX_ATTRIBUTE + catalogTypeAttribute.getId() );
                
                if ( StringUtils.isNotBlank( value ) )
                {
                    catalogDataValue = catalogDataValueService.catalogDataValue( catalog ,catalogTypeAttribute );
                    
                    if ( !catalog.getCatalogType().getCatalogTypeAttributes().contains( catalogTypeAttribute ) )
                    {
                        catalog.getCatalogType().getCatalogTypeAttributes().add( catalogTypeAttribute );
                    }
                    
                    if ( catalogDataValue == null )
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
                        valuesForSave.add( catalogDataValue );
                    }
                    else
                    {
                        if ( CatalogTypeAttribute.TYPE_COMBO.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) )
                        {
                            CatalogTypeAttributeOption option = catalogTypeAttributeOptionService.getCatalogTypeAttributeOption( NumberUtils.toInt( value ));
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
                        valuesForUpdate.add( catalogDataValue );
                        valuesForDelete.remove( catalogDataValue );
                    }
                }
            }
        }
        catalogService.updateCatalogAndDataValue(  catalog, valuesForSave, valuesForUpdate , valuesForDelete);
        return SUCCESS;
    }


}