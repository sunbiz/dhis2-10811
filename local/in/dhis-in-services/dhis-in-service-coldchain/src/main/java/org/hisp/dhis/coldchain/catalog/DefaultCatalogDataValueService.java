package org.hisp.dhis.coldchain.catalog;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

public class DefaultCatalogDataValueService implements CatalogDataValueService
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private CatalogDataValueStore catalogDataValueStore;
    
    public void setCatalogDataValueStore( CatalogDataValueStore catalogDataValueStore )
    {
        this.catalogDataValueStore = catalogDataValueStore;
    }
    
    // -------------------------------------------------------------------------
    // CatalogDataValue
    // -------------------------------------------------------------------------
    @Transactional
    @Override
    public void addCatalogDataValue( CatalogDataValue catalogDataValue )
    {
        
        catalogDataValueStore.addCatalogDataValue( catalogDataValue );
    }
    
    @Transactional
    @Override
    public void deleteCatalogDataValue( CatalogDataValue catalogDataValue )
    {
        catalogDataValueStore.deleteCatalogDataValue( catalogDataValue );
    }
    
    @Transactional
    @Override
    public Collection<CatalogDataValue> getAllCatalogDataValues()
    {
        return catalogDataValueStore.getAllCatalogDataValues();
    }
    
    @Transactional
    @Override
    public Collection<CatalogDataValue> getAllCatalogDataValuesByCatalog( Catalog catalog )
    {
        return catalogDataValueStore.getAllCatalogDataValuesByCatalog( catalog );
    }
    
    @Transactional
    @Override
    public void updateCatalogDataValue( CatalogDataValue catalogDataValue )
    {
        catalogDataValueStore.updateCatalogDataValue( catalogDataValue );
    }
    
    @Transactional
    @Override
    public CatalogDataValue catalogDataValue( Catalog catalog ,CatalogTypeAttribute catalogTypeAttribute )
    {
        return catalogDataValueStore.catalogDataValue( catalog, catalogTypeAttribute );
    }
    
    @Transactional
    @Override
    public CatalogDataValue catalogDataValue( Catalog catalog ,CatalogTypeAttribute catalogTypeAttribute, CatalogTypeAttributeOption catalogTypeAttributeOption )
    {
        return catalogDataValueStore.catalogDataValue( catalog, catalogTypeAttribute, catalogTypeAttributeOption );
    }
    
}
