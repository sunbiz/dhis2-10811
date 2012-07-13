package org.hisp.dhis.coldchain.catalog.dataentryform.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.CatalogDataEntryService;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.dataentryform.DataEntryForm;

import com.opensymphony.xwork2.Action;

public class ViewCatalogDataEntryFormAction implements Action
{

    // -------------------------------------------------------------------------
    // Dependency
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
    // Getters & Setters
    // -------------------------------------------------------------------------



    private Integer catalogTypeId;

    public void setCatalogTypeId( Integer catalogTypeId )
    {
        this.catalogTypeId = catalogTypeId;
    }
    
    private DataEntryForm dataEntryForm;

    public DataEntryForm getDataEntryForm()
    {
        return dataEntryForm;
    }
    
    private CatalogType catalogType;
    
    public CatalogType getCatalogType()
    {
        return catalogType;
    }

    private String dataEntryValue;

    public String getDataEntryValue()
    {
        return dataEntryValue;
    }

    private List<CatalogTypeAttribute> catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>();
    
    public List<CatalogTypeAttribute> getCatalogTypeAttributes()
    {
        return catalogTypeAttributes;
    }
    
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------


    public String execute()
        throws Exception
    {
        catalogType = catalogTypeService.getCatalogType( catalogTypeId );
        
        
        // ---------------------------------------------------------------------
        // Get dataEntryForm of selected catalogtype
        // ---------------------------------------------------------------------
        
        dataEntryForm = catalogType.getDataEntryForm();
        
        if ( dataEntryForm != null )
        {
            dataEntryValue = catalogDataEntryService.prepareDataEntryFormForEdit( dataEntryForm.getHtmlCode() );
        }
        else
        {
            dataEntryValue = "";
        }

        // ---------------------------------------------------------------------
        // Get CatalogType Attribute
        // ---------------------------------------------------------------------
        
        catalogTypeAttributes = new ArrayList<CatalogTypeAttribute> ( catalogType.getCatalogTypeAttributes());
        
        Collections.sort( catalogTypeAttributes, new IdentifiableObjectNameComparator() );
        
        return SUCCESS;
    }
}
