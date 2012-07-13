package org.hisp.dhis.coldchain.catalog;

import static org.hisp.dhis.i18n.I18nUtils.getCountByName;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetween;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetweenByName;

import java.util.Collection;
import java.util.List;

import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.InventoryType;
import org.hisp.dhis.coldchain.inventory.InventoryTypeAttribute;
import org.hisp.dhis.i18n.I18nService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultCatalogService
    implements CatalogService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CatalogStore catalogStore;

    public void setCatalogStore( CatalogStore catalogStore )
    {
        this.catalogStore = catalogStore;
    }

    private CatalogDataValueService catalogDataValueService;

    public void setCatalogDataValueService( CatalogDataValueService catalogDataValueService )
    {
        this.catalogDataValueService = catalogDataValueService;
    }

    private I18nService i18nService;

    public void setI18nService( I18nService service )
    {
        i18nService = service;
    }

    // -------------------------------------------------------------------------
    // Catalog
    // -------------------------------------------------------------------------
    /*
     * public int addCatalog( Catalog catalog ) { return
     * catalogStore.addCatalog( catalog ); }
     * 
     * public void deleteCatalog( Catalog catalog ) {
     * catalogStore.deleteCatalog( catalog ); }
     * 
     * public void updateCatalog( Catalog catalog ) {
     * catalogStore.updateCatalog( catalog ); }
     */

    @Override
    public int addCatalog( Catalog catalog )
    {
        return catalogStore.save( catalog );
    }

    @Override
    public void deleteCatalog( Catalog catalog )
    {
        catalogStore.delete( catalog );
    }

    @Override
    public void updateCatalog( Catalog catalog )
    {
        catalogStore.update( catalog );
    }

    @Override
    public void deleteCatalogData( Catalog catalog )
    {
        catalogStore.delete( catalog );
    }

    public Collection<Catalog> getAllCatalogs()
    {
        return catalogStore.getAllCatalogs();
    }

    @Override
    public Catalog getCatalog( int id )
    {
        return catalogStore.getCatalog( id );
    }

    @Override
    public Catalog getCatalogByName( String name )
    {
        return catalogStore.getCatalogByName( name );
    }

    @Override
    public int createCatalog( Catalog catalog, List<CatalogDataValue> catalogDataValues )
    {
        int catalogId = addCatalog( catalog );

        for ( CatalogDataValue catalogDataValue : catalogDataValues )
        {
            catalogDataValueService.addCatalogDataValue( catalogDataValue );
        }

        return catalogId;
    }

    @Override
    public void updateCatalogAndDataValue( Catalog catalog, List<CatalogDataValue> valuesForSave,
        List<CatalogDataValue> valuesForUpdate, Collection<CatalogDataValue> valuesForDelete )
    {
        catalogStore.update( catalog );
        // catalogStore.updateCatalog( catalog );

        for ( CatalogDataValue catalogDataValueAdd : valuesForSave )
        {
            catalogDataValueService.addCatalogDataValue( catalogDataValueAdd );
        }

        for ( CatalogDataValue catalogDataValueUpdate : valuesForUpdate )
        {
            catalogDataValueService.updateCatalogDataValue( catalogDataValueUpdate );
        }

        for ( CatalogDataValue catalogDataValueDelete : valuesForDelete )
        {
            catalogDataValueService.deleteCatalogDataValue( catalogDataValueDelete );
        }
    }

    @Override
    public void deleteCatalogAndDataValue( Catalog catalog )
    {
        Collection<CatalogDataValue> valuesForDelete = catalogDataValueService
            .getAllCatalogDataValuesByCatalog( catalog );
        for ( CatalogDataValue catalogDataValueDelete : valuesForDelete )
        {
            catalogDataValueService.deleteCatalogDataValue( catalogDataValueDelete );
        }

        // catalogStore.deleteCatalog( catalog );
    }

    public Collection<Catalog> getCatalogs( CatalogType catalogType )
    {
        return catalogStore.getCatalogs( catalogType );
    }

    // Methods
    public int getCatalogCount()
    {
        return catalogStore.getCount();
    }

    public int getCatalogCountByName( String name )
    {
        return getCountByName( i18nService, catalogStore, name );
    }

    public Collection<Catalog> getCatalogsBetween( int first, int max )
    {
        return getObjectsBetween( i18nService, catalogStore, first, max );
    }

    public Collection<Catalog> getCatalogsBetweenByName( String name, int first, int max )
    {
        return getObjectsBetweenByName( i18nService, catalogStore, name, first, max );
    }

    @Override
    public int getCountCatalog( CatalogType catalogType )
    {
        return catalogStore.getCountCatalog( catalogType );
    }

    public Collection<Catalog> getCatalogs( CatalogType catalogType, int min, int max )
    {
        return catalogStore.getCatalogs( catalogType, min, max );
    }

    public int getCountCatalog( CatalogType catalogType, CatalogTypeAttribute catalogTypeAttribute, String searchText )
    {
        return catalogStore.getCountCatalog( catalogType, catalogTypeAttribute, searchText );
    }

    public Collection<Catalog> getCatalogs( CatalogType catalogType, CatalogTypeAttribute catalogTypeAttribute,
        String searchText, int min, int max )
    {
        return catalogStore.getCatalogs( catalogType, catalogTypeAttribute, searchText, min, max );
    }

}
