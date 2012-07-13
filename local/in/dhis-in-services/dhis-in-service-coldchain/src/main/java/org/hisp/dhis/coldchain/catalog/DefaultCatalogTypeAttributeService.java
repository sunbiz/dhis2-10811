package org.hisp.dhis.coldchain.catalog;

import static org.hisp.dhis.i18n.I18nUtils.getCountByName;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetween;
import static org.hisp.dhis.i18n.I18nUtils.getObjectsBetweenByName;

import java.util.Collection;

import org.hisp.dhis.i18n.I18nService;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public class DefaultCatalogTypeAttributeService implements CatalogTypeAttributeService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CatalogTypeAttributeStore catalogTypeAttributeStore;

    public void setCatalogTypeAttributeStore( CatalogTypeAttributeStore catalogTypeAttributeStore )
    {
        this.catalogTypeAttributeStore = catalogTypeAttributeStore;
    }
    
    private I18nService i18nService;

    public void setI18nService( I18nService service )
    {
        i18nService = service;
    }
    
    // -------------------------------------------------------------------------
    // CatalogTypeAttribute
    // -------------------------------------------------------------------------
    /*
    @Override
    public int addCatalogTypeAttribute( CatalogTypeAttribute catalogTypeAttribute )
    {
        return catalogTypeAttributeStore.addCatalogTypeAttribute( catalogTypeAttribute );
    }
    @Override
    public void deleteCatalogTypeAttribute( CatalogTypeAttribute catalogTypeAttribute )
    {
        catalogTypeAttributeStore.deleteCatalogTypeAttribute( catalogTypeAttribute );
    }
    @Override
    public Collection<CatalogTypeAttribute> getAllCatalogTypeAttributes()
    {
        return catalogTypeAttributeStore.getAllCatalogTypeAttributes();
    }
    
    @Transactional
    @Override
    public void updateCatalogTypeAttribute( CatalogTypeAttribute catalogTypeAttribute )
    {
        catalogTypeAttributeStore.updateCatalogTypeAttribute( catalogTypeAttribute );
    }
    @Override
    public CatalogTypeAttribute getCatalogTypeAttribute( int id )
    {
        return catalogTypeAttributeStore.getCatalogTypeAttribute( id );
    }
    @Override
    public CatalogTypeAttribute getCatalogTypeAttributeByName( String name )
    {
        return catalogTypeAttributeStore.getCatalogTypeAttributeByName( name );
        
    }
    */
    // -------------------------------------------------------------------------
    // CatalogTypeAttribute
    // -------------------------------------------------------------------------
    @Override
    public int addCatalogTypeAttribute( CatalogTypeAttribute catalogTypeAttribute )
    {
        return catalogTypeAttributeStore.save( catalogTypeAttribute );
    }
    @Override
    public void deleteCatalogTypeAttribute( CatalogTypeAttribute catalogTypeAttribute )
    {
        catalogTypeAttributeStore.delete( catalogTypeAttribute );       
    }
    @Override
    public void updateCatalogTypeAttribute( CatalogTypeAttribute catalogTypeAttribute )
    {
        catalogTypeAttributeStore.update( catalogTypeAttribute );
    }
    
    @Override
    public Collection<CatalogTypeAttribute> getAllCatalogTypeAttributes()
    {
        return catalogTypeAttributeStore.getAllCatalogTypeAttributes();
    }
    @Override
    public CatalogTypeAttribute getCatalogTypeAttribute( int id )
    {
        return catalogTypeAttributeStore.getCatalogTypeAttribute( id );
    }
    @Override
    public CatalogTypeAttribute getCatalogTypeAttributeByName( String name )
    {
        return catalogTypeAttributeStore.getCatalogTypeAttributeByName( name );
        
    }
    //Methods
    public int getCatalogTypeAttributeCount()
    {
        return catalogTypeAttributeStore.getCount();
    }
    
    public int getCatalogTypeAttributeCountByName( String name )
    {
        return getCountByName( i18nService, catalogTypeAttributeStore, name );
    }

    public Collection<CatalogTypeAttribute> getCatalogTypeAttributesBetween( int first, int max )
    {
        return getObjectsBetween( i18nService, catalogTypeAttributeStore, first, max );
    }

    public Collection<CatalogTypeAttribute> getCatalogTypeAttributesBetweenByName( String name, int first, int max )
    {
        return getObjectsBetweenByName( i18nService, catalogTypeAttributeStore, name, first, max );
    }
}
