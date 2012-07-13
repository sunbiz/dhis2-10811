package org.hisp.dhis.coldchain.catalog.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeService;
import org.hisp.dhis.coldchain.catalog.comparator.CatalogTypeAttributeComparator;

import com.opensymphony.xwork2.Action;

public class GetCatalogTypeAttributesAction implements Action
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private CatalogTypeAttributeService catalogTypeAttributeService;
    
    public void setCatalogTypeAttributeService( CatalogTypeAttributeService catalogTypeAttributeService )
    {
        this.catalogTypeAttributeService = catalogTypeAttributeService;
    }

    // -------------------------------------------------------------------------
    // Input & output
    // -------------------------------------------------------------------------


    private String key;

    public void setKey( String key )
    {
        this.key = key;
    }
    
    public String getKey()
    {
        return key;
    }

    private List<CatalogTypeAttribute> catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>();
    
    public List<CatalogTypeAttribute> getCatalogTypeAttributes()
    {
        return catalogTypeAttributes;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        
        catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>(catalogTypeAttributeService.getAllCatalogTypeAttributes());
        Collections.sort( catalogTypeAttributes, new CatalogTypeAttributeComparator() );
        
        /*
        if ( key != null )
        {
            catalogTypeAttributes = IdentifiableObjectUtils.filterNameByKey( catalogTypeAttributes, key, true );
        }

        Collections.sort( catalogTypeAttributes, IdentifiableObjectNameComparator.INSTANCE );
        */
        
        
        /*
        if ( id != null && id != ALL )
        {
            DataElementGroup dataElementGroup = dataElementService.getDataElementGroup( id );

            if ( dataElementGroup != null )
            {
                dataElements = new ArrayList<DataElement>( dataElementGroup.getMembers() );
            }
        }
        else if ( categoryComboId != null && categoryComboId != ALL )
        {
            DataElementCategoryCombo categoryCombo = categoryService.getDataElementCategoryCombo( categoryComboId );

            if ( categoryCombo != null )
            {
                dataElements = new ArrayList<DataElement>(
                    dataElementService.getDataElementByCategoryCombo( categoryCombo ) );
            }
        }
        else if ( dataSetId != null )
        {
            DataSet dataset = dataSetService.getDataSet( dataSetId );

            if ( dataset != null )
            {
                dataElements = new ArrayList<DataElement>( dataset.getDataElements() );
            }
        }
        else if ( periodTypeName != null )
        {
            PeriodType periodType = periodService.getPeriodTypeByName( periodTypeName );

            if ( periodType != null )
            {
                dataElements = new ArrayList<DataElement>( dataElementService.getDataElementsByPeriodType( periodType ) );
            }
        }
        else if ( domain != null )
        {
            dataElements = new ArrayList<DataElement>(
                dataElementService.getDataElementsByDomainType( DataElement.DOMAIN_TYPE_PATIENT ) );
        }
        else
        {
            dataElements = new ArrayList<DataElement>( dataElementService.getAllDataElements() );
            
            ContextUtils.clearIfNotModified( ServletActionContext.getRequest(), ServletActionContext.getResponse(), dataElements );
        }

        if ( key != null )
        {
            dataElements = IdentifiableObjectUtils.filterNameByKey( dataElements, key, true );
        }

        Collections.sort( dataElements, IdentifiableObjectNameComparator.INSTANCE );

        if ( aggregate )
        {
            FilterUtils.filter( dataElements, new AggregatableDataElementFilter() );
        }
        */

        return SUCCESS;
    }

}
