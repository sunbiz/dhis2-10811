package org.hisp.dhis.coldchain.catalog.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.coldchain.catalog.CatalogDataEntryService;
import org.hisp.dhis.coldchain.catalog.CatalogDataValue;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOption;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.coldchain.catalog.comparator.CatalogTypeAttributeOptionComparator;
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
    
    private Boolean isCustom;
    
    public Boolean getIsCustom()
    {
        return isCustom;
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
    /*
    private List<CatalogTypeAttributeOption> catalogTypeAttributesOptions = new ArrayList<CatalogTypeAttributeOption>();
    
    public List<CatalogTypeAttributeOption> getCatalogTypeAttributesOptions()
    {
        return catalogTypeAttributesOptions;
    }
    */
    
    private Map<Integer, List<CatalogTypeAttributeOption>> catalogTypeAttributesOptionsMap = new HashMap<Integer, List<CatalogTypeAttributeOption>>();
    
    public Map<Integer, List<CatalogTypeAttributeOption>> getCatalogTypeAttributesOptionsMap()
    {
        return catalogTypeAttributesOptionsMap;
    }
    
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        System.out.println("entering into  ShowAddCatalogFormAction action");
        catalogType = catalogTypeService.getCatalogType( catalogTypeId );
        isCustom = false;
        String disabled = ""; 
        if ( catalogType != null )
        {
            // ---------------------------------------------------------------------
            // Get data-entry-form
            // ---------------------------------------------------------------------

            DataEntryForm dataEntryForm = catalogType.getDataEntryForm();

            System.out.println("dataentryform object retrieved");
            
            if ( dataEntryForm != null )
            {
                isCustom = true;
                System.out.println("dataentryform object is not null");
                Collection<CatalogDataValue> catalogDataValues = new ArrayList<CatalogDataValue>();
                
                customDataEntryFormCode = catalogDataEntryService.prepareDataEntryFormForCatalog( dataEntryForm.getHtmlCode(), catalogDataValues, disabled, i18n, catalogType );
                //customDataEntryFormCode = "custom dataentry form";
            }
            
            catalogTypeAttributes = new ArrayList<CatalogTypeAttribute> ( catalogType.getCatalogTypeAttributes() );
            
            for( CatalogTypeAttribute catalogTypeAttribute : catalogTypeAttributes )
            {
                List<CatalogTypeAttributeOption> catalogTypeAttributesOptions = new ArrayList<CatalogTypeAttributeOption>();
                if( CatalogTypeAttribute.TYPE_COMBO.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) )
                {
                    System.out.println(" inside CatalogTypeAttribute.TYPE_COMBO ");
                    catalogTypeAttributesOptions = new ArrayList<CatalogTypeAttributeOption>( catalogTypeAttribute.getAttributeOptions() );
                    Collections.sort( catalogTypeAttributesOptions, new CatalogTypeAttributeOptionComparator() );
                    catalogTypeAttributesOptionsMap.put( catalogTypeAttribute.getId(), catalogTypeAttributesOptions );
                }

                /*
                System.out.println( "Name :" + catalogTypeAttribute.getName() );
                System.out.println( "valueType :" + catalogTypeAttribute.getValueType() );
                System.out.println( "Is mandatory :" + catalogTypeAttribute.isMandatory() );
                */
            }
            
            
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
        
        System.out.println("going out from  ShowAddCatalogFormAction action");
        return SUCCESS;
    }


}
