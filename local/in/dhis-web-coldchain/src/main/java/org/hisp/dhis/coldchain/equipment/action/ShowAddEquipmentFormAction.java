package org.hisp.dhis.coldchain.equipment.action;

import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;
import org.hisp.dhis.coldchain.inventory.InventoryType_Attribute;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;

import com.opensymphony.xwork2.Action;

public class ShowAddEquipmentFormAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    private InventoryTypeService inventoryTypeService;
    
    public void setInventoryTypeService( InventoryTypeService inventoryTypeService )
    {
        this.inventoryTypeService = inventoryTypeService;
    }
    
    private CatalogService catalogService;
    
    public void setCatalogService( CatalogService catalogService )
    {
        this.catalogService = catalogService;
    }

    // -------------------------------------------------------------------------
    // Input/Output
    // -------------------------------------------------------------------------

    private String orgUnitId;
    
    public void setOrgUnitId( String orgUnitId )
    {
        this.orgUnitId = orgUnitId;
    }

    private String inventoryTypeId;

    public void setInventoryTypeId( String inventoryTypeId )
    {
        this.inventoryTypeId = inventoryTypeId;
    }

    private OrganisationUnit organisationUnit;

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    private InventoryType inventoryType;

    public InventoryType getInventoryType()
    {
        return inventoryType;
    }

    private List<InventoryTypeAttribute> inventoryTypeAttributes;
    
    public List<InventoryTypeAttribute> getInventoryTypeAttributes()
    {
        return inventoryTypeAttributes;
    }

    private List<Catalog> catalogs;
    
    public List<Catalog> getCatalogs()
    {
        return catalogs;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------
    public String execute() throws Exception
    {
        catalogs = new ArrayList<Catalog>();
        
        organisationUnit = organisationUnitService.getOrganisationUnit( Integer.parseInt( orgUnitId ) );
        
        inventoryType = inventoryTypeService.getInventoryType( Integer.parseInt( inventoryTypeId ) );
        
        inventoryTypeAttributes = new ArrayList<InventoryTypeAttribute>( );
        for( InventoryType_Attribute inventoryType_Attribute : inventoryType.getInventoryType_Attributes() )
        {
            inventoryTypeAttributes.add( inventoryType_Attribute.getInventoryTypeAttribute() );
        }
              
        CatalogType catalogType = inventoryType.getCatalogType();
        
        if( catalogType != null )
        {
            catalogs = new ArrayList<Catalog>( catalogService.getCatalogs( catalogType ) );
        }
        /*
        System.out.println( "Size of catalogs is --- "  + catalogs.size() );
        
        for( Catalog catalog : catalogs)
        {
            System.out.println( "Catalog name is "  + catalog.getName() );
            System.out.println( "Catalog Id is "  + catalog.getId() );
        }
        */
        
        return SUCCESS;
    }

}
