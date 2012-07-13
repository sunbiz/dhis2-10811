package org.hisp.dhis.coldchain.catalog;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

public class DefaultCatalogTypeAttributeOptionService implements CatalogTypeAttributeOptionService
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CatalogTypeAttributeOptionStore catalogTypeAttributeOptionStore;

    public void setCatalogTypeAttributeOptionStore( CatalogTypeAttributeOptionStore catalogTypeAttributeOptionStore )
    {
        this.catalogTypeAttributeOptionStore = catalogTypeAttributeOptionStore;
    }

    // -------------------------------------------------------------------------
    // CatalogTypeAttributeOption
    // -------------------------------------------------------------------------
    @Transactional
    @Override
    public int addCatalogTypeAttributeOption( CatalogTypeAttributeOption catalogTypeAttributeOption )
    {
        return catalogTypeAttributeOptionStore.addCatalogTypeAttributeOption( catalogTypeAttributeOption );
    }
    
    @Transactional
    @Override
    public void deleteCatalogTypeAttributeOption( CatalogTypeAttributeOption catalogTypeAttributeOption )
    {
        catalogTypeAttributeOptionStore.deleteCatalogTypeAttributeOption( catalogTypeAttributeOption );
    }
    
    @Transactional
    @Override
    public Collection<CatalogTypeAttributeOption> getAllCatalogTypeAttributeOptions()
    {
        return catalogTypeAttributeOptionStore.getAllCatalogTypeAttributeOptions();
    }
    
    @Transactional
    @Override
    public void updateCatalogTypeAttributeOption( CatalogTypeAttributeOption catalogTypeAttributeOption )
    {
        catalogTypeAttributeOptionStore.updateCatalogTypeAttributeOption( catalogTypeAttributeOption );
    }
   
    @Transactional
    @Override
    public CatalogTypeAttributeOption getCatalogTypeAttributeOption( int id )
    {
        return catalogTypeAttributeOptionStore.getCatalogTypeAttributeOption( id );
    }
    
    @Transactional
    @Override
    public int countByCatalogTypeAttributeoption( CatalogTypeAttributeOption catalogTypeAttributeOption )
    {
        return catalogTypeAttributeOptionStore.countByCatalogTypeAttributeoption( catalogTypeAttributeOption );
    }
    
    @Transactional
    @Override
    public Collection<CatalogTypeAttributeOption> getCatalogTypeAttributeOptions( CatalogTypeAttribute catalogTypeAttribute )
    {
        return catalogTypeAttributeOptionStore.getCatalogTypeAttributeOptions( catalogTypeAttribute );
    }
    
    @Transactional
    @Override
    public CatalogTypeAttributeOption getCatalogTypeAttributeOptionName( CatalogTypeAttribute catalogTypeAttribute, String name )
    {
        return catalogTypeAttributeOptionStore.getCatalogTypeAttributeOptionName( catalogTypeAttribute, name );
    }
}
