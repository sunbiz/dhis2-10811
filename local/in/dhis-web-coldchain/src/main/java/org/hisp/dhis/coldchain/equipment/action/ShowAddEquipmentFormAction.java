package org.hisp.dhis.coldchain.equipment.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOption;
import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttributeOption;
import org.hisp.dhis.coldchain.inventory.InventoryTypeService;
import org.hisp.dhis.coldchain.inventory.InventoryType_Attribute;
import org.hisp.dhis.coldchain.inventory.comparator.InventoryTypeAttributeOptionComparator;
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
    
    private Map<Integer, List<InventoryTypeAttributeOption>> inventoryTypeAttributeOptionsMap = new HashMap<Integer, List<InventoryTypeAttributeOption>>();
    
    public Map<Integer, List<InventoryTypeAttributeOption>> getInventoryTypeAttributeOptionsMap()
    {
        return inventoryTypeAttributeOptionsMap;
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
        
        for( InventoryTypeAttribute inventoryTypeAttribute : inventoryTypeAttributes )
        {
            List<InventoryTypeAttributeOption> inventoryTypeAttributeOptions = new ArrayList<InventoryTypeAttributeOption>();
            if( InventoryTypeAttribute.TYPE_COMBO.equalsIgnoreCase( inventoryTypeAttribute.getValueType() ) )
            {
                System.out.println(" inside inventoryTypeAttribute.TYPE_COMBO ");
                inventoryTypeAttributeOptions = new ArrayList<InventoryTypeAttributeOption>( inventoryTypeAttribute.getAttributeOptions() );
                Collections.sort( inventoryTypeAttributeOptions, new InventoryTypeAttributeOptionComparator() );
                inventoryTypeAttributeOptionsMap.put( inventoryTypeAttribute.getId(), inventoryTypeAttributeOptions );
            }

            /*
            System.out.println( "Name :" + catalogTypeAttribute.getName() );
            System.out.println( "valueType :" + catalogTypeAttribute.getValueType() );
            System.out.println( "Is mandatory :" + catalogTypeAttribute.isMandatory() );
            */
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
