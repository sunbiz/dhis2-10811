package org.hisp.dhis.coldchain.catalog;

import static org.hisp.dhis.i18n.I18nUtils.getCountByName;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetween;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetweenByName;

import java.util.Collection;

import org.hisp.dhis.i18n.I18nService;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public class DefaultCatalogTypeService implements CatalogTypeService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CatalogTypeStore catalogTypeStore;
    
    public void setCatalogTypeStore( CatalogTypeStore catalogTypeStore )
    {
        this.catalogTypeStore = catalogTypeStore;
    }
    private I18nService i18nService;

    public void setI18nService( I18nService service )
    {
        i18nService = service;
    }
    
    // -------------------------------------------------------------------------
    // CatalogType
    // -------------------------------------------------------------------------
    /*
    @Transactional
    @Override
    public int addCatalogType( CatalogType catalogType )
    {
        return catalogTypeStore.addCatalogType( catalogType );
    }
    
    @Transactional
    @Override
    public void deleteCatalogType( CatalogType catalogType )
    {
        catalogTypeStore.deleteCatalogType( catalogType );
    }

    @Transactional
    @Override
    public Collection<CatalogType> getAllCatalogTypes()
    {
        return catalogTypeStore.getAllCatalogTypes();
    }

    @Transactional
    @Override
    public void updateCatalogType( CatalogType catalogType )
    {
        catalogTypeStore.updateCatalogType( catalogType );
    }
    
    @Transactional
    @Override
    public CatalogType getCatalogType( int id )
    {
        return catalogTypeStore.getCatalogType( id );
    }
    
    @Transactional
    @Override
    public CatalogType getCatalogTypeByName( String name )
    {
        return catalogTypeStore.getCatalogTypeByName( name );
    }
    */
    
    
    // -------------------------------------------------------------------------
    // CatalogType
    // -------------------------------------------------------------------------
    
    @Override
    public int addCatalogType( CatalogType catalogType )
    {
        return catalogTypeStore.save( catalogType );
    }
    
    @Override
    public void deleteCatalogType( CatalogType catalogType )
    {
        catalogTypeStore.delete( catalogType );
    }
    
    @Override
    public void updateCatalogType( CatalogType catalogType )
    {
        catalogTypeStore.update( catalogType );
    }
    
    @Override
    public Collection<CatalogType> getAllCatalogTypes()
    {
        return catalogTypeStore.getAllCatalogTypes();
    }
    
    @Override
    public CatalogType getCatalogType( int id )
    {
        return catalogTypeStore.getCatalogType( id );
    }
    
    @Override
    public CatalogType getCatalogTypeByName( String name )
    {
        return catalogTypeStore.getCatalogTypeByName( name );
    }
    
    //Methods
    public int getCatalogTypeCount()
    {
        return catalogTypeStore.getCount();
    }
    
    public int getCatalogTypeCountByName( String name )
    {
        return getCountByName( i18nService, catalogTypeStore, name );
    }

    public Collection<CatalogType> getCatalogTypesBetween( int first, int max )
    {
        return getObjectsBetween( i18nService, catalogTypeStore, first, max );
    }

    public Collection<CatalogType> getCatalogTypesBetweenByName( String name, int first, int max )
    {
        return getObjectsBetweenByName( i18nService, catalogTypeStore, name, first, max );
    }    
}
