package org.hisp.dhis.coldchain.catalog.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.CatalogDataEntryService;
import org.hisp.dhis.coldchain.catalog.CatalogDataValue;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.dataentryform.DataEntryForm;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork2.Action;

public class ShowAddCatalogFormAction
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
    
    private CatalogDataEntryService catalogDataEntryService;
    
    public void setCatalogDataEntryService( CatalogDataEntryService catalogDataEntryService )
    {
        this.catalogDataEntryService = catalogDataEntryService;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------
    
    private int catalogTypeId;
    
    public void setCatalogTypeId( int catalogTypeId )
    {
        this.catalogTypeId = catalogTypeId;
    }
/*
    private Collection<CatalogTypeAttribute> catalogTypeAttributes;
    
    public Collection<CatalogTypeAttribute> getCatalogTypeAttributes()
    {
        return catalogTypeAttributes;
    }
*/    
    
    private List<CatalogTypeAttribute> catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>();
    
    public List<CatalogTypeAttribute> getCatalogTypeAttributes()
    {
        return catalogTypeAttributes;
    }
    
    private CatalogType catalogType;
    
    public CatalogType getCatalogType()
    {
        return catalogType;
    }
    
    private String customDataEntryFormCode;
    
    public String getCustomDataEntryFormCode()
    {
        return customDataEntryFormCode;
    }

    private I18n i18n;
    
    public I18n getI18n()
    {
        return i18n;
    }

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }
    
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

 



    public String execute()
    {
        catalogType = catalogTypeService.getCatalogType( catalogTypeId );
        
        String disabled = ""; 
        if ( catalogType != null )
        {
            // ---------------------------------------------------------------------
            // Get data-entry-form
            // ---------------------------------------------------------------------

            DataEntryForm dataEntryForm = catalogType.getDataEntryForm();

            if ( dataEntryForm != null )
            {
                Collection<CatalogDataValue> catalogDataValues = new ArrayList<CatalogDataValue>();
                
                customDataEntryFormCode = catalogDataEntryService.prepareDataEntryFormForCatalog( dataEntryForm.getHtmlCode(), catalogDataValues, disabled, i18n, catalogType );
            }
            
            catalogTypeAttributes = new ArrayList<CatalogTypeAttribute> ( catalogType.getCatalogTypeAttributes());
            //Collections.sort( catalogTypeAttributes, new CatalogTypeAttributeComparator() );
            
            /*
            System.out.println( "Name of CatalogType is ======  :" + catalogType.getName() );
            System.out.println( "Size of catalogTypeAttributes  :" + catalogTypeAttributes.size() );
            for( CatalogTypeAttribute catalogTypeAttribute : catalogTypeAttributes )
            {
                System.out.println( "Name :" + catalogTypeAttribute.getName() );
                System.out.println( "valueType :" + catalogTypeAttribute.getValueType() );
                System.out.println( "Is mandatory :" + catalogTypeAttribute.isMandatory() );
            }
            */
        }
        

        return SUCCESS;
    }


}
