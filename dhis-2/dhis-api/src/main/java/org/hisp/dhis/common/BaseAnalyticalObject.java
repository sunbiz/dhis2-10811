package org.hisp.dhis.common;

/*
 * Copyright (c) 2004-2012, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import static org.hisp.dhis.common.DimensionalObject.DATAELEMENT_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.DATAELEMENT_OPERAND_ID;
import static org.hisp.dhis.common.DimensionalObject.DATASET_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.DATA_X_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.INDICATOR_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.ORGUNIT_DIM_ID;
import static org.hisp.dhis.common.DimensionalObject.PERIOD_DIM_ID;
import static org.hisp.dhis.organisationunit.OrganisationUnit.KEY_USER_ORGUNIT;
import static org.hisp.dhis.organisationunit.OrganisationUnit.KEY_USER_ORGUNIT_CHILDREN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.common.adapter.JacksonPeriodDeserializer;
import org.hisp.dhis.common.adapter.JacksonPeriodSerializer;
import org.hisp.dhis.common.annotation.Scanned;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.DimensionalView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryDimension;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.period.ConfigurablePeriod;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.RelativePeriodEnum;
import org.hisp.dhis.period.RelativePeriods;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * This class contains associations to dimensional meta-data. Should typically
 * be sub-classed by analytical objects like tables, maps and charts.
 * 
 * @author Lars Helge Overland
 */
public abstract class BaseAnalyticalObject
    extends BaseIdentifiableObject
{
    // -------------------------------------------------------------------------
    // Persisted properties
    // -------------------------------------------------------------------------

    @Scanned
    protected List<Indicator> indicators = new ArrayList<Indicator>();

    @Scanned
    protected List<DataElement> dataElements = new ArrayList<DataElement>();

    @Scanned
    protected List<DataElementOperand> dataElementOperands = new ArrayList<DataElementOperand>();
    
    @Scanned
    protected List<DataSet> dataSets = new ArrayList<DataSet>();

    @Scanned
    protected List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();

    @Scanned
    protected List<Period> periods = new ArrayList<Period>();
    
    protected RelativePeriods relatives;

    @Scanned
    protected List<DataElementCategoryDimension> categoryDimensions = new ArrayList<DataElementCategoryDimension>();
    
    @Scanned
    protected List<DataElementGroup> dataElementGroups = new ArrayList<DataElementGroup>();

    @Scanned
    protected List<OrganisationUnitGroup> organisationUnitGroups = new ArrayList<OrganisationUnitGroup>();

    protected boolean userOrganisationUnit;

    protected boolean userOrganisationUnitChildren;

    // -------------------------------------------------------------------------
    // Analytical properties
    // -------------------------------------------------------------------------

    protected transient List<DimensionalObject> columns = new ArrayList<DimensionalObject>();
    
    protected transient List<DimensionalObject> rows = new ArrayList<DimensionalObject>();
    
    protected transient List<DimensionalObject> filters = new ArrayList<DimensionalObject>();
    
    protected Map<String, String> parentGraphMap = new HashMap<String, String>();

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public abstract void populateAnalyticalProperties();
    
    public boolean hasUserOrgUnit()
    {
        return userOrganisationUnit || userOrganisationUnitChildren;
    }
    
    public boolean hasRelativePeriods()
    {
        return relatives != null && !relatives.isEmpty();
    }
    
    protected List<DimensionalObject> getDimensionalObjectList( String dimension )
    {
        List<DimensionalObject> objects = new ArrayList<DimensionalObject>();
        
        List<String> categoryDims = new ArrayList<String>();
        
        for ( DataElementCategoryDimension dim : categoryDimensions )
        {
            categoryDims.add( dim.getDimension().getDimension() );
        }
        
        if ( DATA_X_DIM_ID.equals( dimension ) )
        {
            if ( !indicators.isEmpty() )
            {
                objects.add( new BaseDimensionalObject( INDICATOR_DIM_ID, DimensionType.INDICATOR, indicators ) );
            }
            
            if ( !dataElements.isEmpty() )
            {
                objects.add( new BaseDimensionalObject( DATAELEMENT_DIM_ID, DimensionType.DATAELEMENT, dataElements ) );
            }
            
            if ( !dataElementOperands.isEmpty() )
            {
                objects.add( new BaseDimensionalObject( DATAELEMENT_OPERAND_ID, DimensionType.DATAELEMENT_OPERAND, dataElementOperands ) );
            }
            
            if ( !dataSets.isEmpty() )
            {
                objects.add( new BaseDimensionalObject( DATASET_DIM_ID, DimensionType.DATASET, dataSets ) );
            }
        }
        else if ( PERIOD_DIM_ID.equals( dimension ) && ( !periods.isEmpty() || hasRelativePeriods() ) )
        {
            List<IdentifiableObject> periodList = new ArrayList<IdentifiableObject>( periods );
            
            if ( hasRelativePeriods() )
            {
                List<RelativePeriodEnum> list = relatives.getRelativePeriodEnums();

                for ( RelativePeriodEnum periodEnum : list )
                {
                    periodList.add( new ConfigurablePeriod( periodEnum.toString() ) );
                }
            }
            
            objects.add( new BaseDimensionalObject( dimension, DimensionType.PERIOD, periodList ) );
        }        
        else if ( ORGUNIT_DIM_ID.equals( dimension ) && ( !organisationUnits.isEmpty() || hasUserOrgUnit() ) )
        {
            List<IdentifiableObject> ouList = new ArrayList<IdentifiableObject>( organisationUnits );
            
            if ( userOrganisationUnit )
            {
                ouList.add( new BaseIdentifiableObject( KEY_USER_ORGUNIT, KEY_USER_ORGUNIT, KEY_USER_ORGUNIT ) );
            }
            
            if ( userOrganisationUnitChildren )
            {
                ouList.add( new BaseIdentifiableObject( KEY_USER_ORGUNIT_CHILDREN, KEY_USER_ORGUNIT_CHILDREN, KEY_USER_ORGUNIT_CHILDREN ) );
            }
                
            objects.add( new BaseDimensionalObject( dimension, DimensionType.ORGANISATIONUNIT, ouList ) );
        }
        else if ( categoryDims.contains( dimension ) )
        {
            DataElementCategoryDimension categoryDimension = categoryDimensions.get( categoryDims.indexOf( dimension ) );
            
            objects.add( new BaseDimensionalObject( dimension, DimensionType.CATEGORY, categoryDimension.getItems() ) );
        }
        else // Group set
        {
            ListMap<String, IdentifiableObject> deGroupMap = new ListMap<String, IdentifiableObject>();
            
            for ( DataElementGroup group : dataElementGroups )
            {
                deGroupMap.putValue( group.getGroupSet().getDimension(), group );
            }
            
            if ( deGroupMap.containsKey( dimension ) )
            {
                objects.add( new BaseDimensionalObject( dimension, DimensionType.DATAELEMENT_GROUPSET, deGroupMap.get( dimension ) ) );
            }

            ListMap<String, IdentifiableObject> ouGroupMap = new ListMap<String, IdentifiableObject>();
            
            for ( OrganisationUnitGroup group : organisationUnitGroups )
            {
                ouGroupMap.putValue( group.getGroupSet().getUid(), group );
            }
                        
            if ( ouGroupMap.containsKey( dimension ) )
            {
                objects.add( new BaseDimensionalObject( dimension, DimensionType.ORGANISATIONUNIT_GROUPSET, ouGroupMap.get( dimension ) ) );
            }
        }
        
        return objects;
    }
    
    public void mergeWith( BaseAnalyticalObject other )
    {
        super.mergeWith( other );
        
        if ( other.getClass().isInstance( this ) )
        {
            filters.clear();
            filters.addAll( other.getFilters() );
            
            indicators.clear();
            indicators.addAll( other.getIndicators() );

            dataElements.clear();
            dataElements.addAll( other.getDataElements() );

            dataSets.clear();
            dataSets.addAll( other.getDataSets() );
            
            periods.clear();
            periods.addAll( other.getPeriods() );

            relatives = other.getRelatives() == null ? relatives : other.getRelatives();
            
            organisationUnits.clear();
            organisationUnits.addAll( other.getOrganisationUnits() );
            
            userOrganisationUnit = other.isUserOrganisationUnit();
            userOrganisationUnitChildren = other.isUserOrganisationUnitChildren();            
        }
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( contentAs = BaseNameableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "indicators", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "indicator", namespace = DxfNamespaces.DXF_2_0)
    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    public void setIndicators( List<Indicator> indicators )
    {
        this.indicators = indicators;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseNameableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "dataElements", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "dataElement", namespace = DxfNamespaces.DXF_2_0)
    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( List<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "dataElementOperands", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "dataElementOperand", namespace = DxfNamespaces.DXF_2_0)
    public List<DataElementOperand> getDataElementOperands()
    {
        return dataElementOperands;
    }

    public void setDataElementOperands( List<DataElementOperand> dataElementOperands )
    {
        this.dataElementOperands = dataElementOperands;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseNameableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "dataSets", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "dataSet", namespace = DxfNamespaces.DXF_2_0)
    public List<DataSet> getDataSets()
    {
        return dataSets;
    }

    public void setDataSets( List<DataSet> dataSets )
    {
        this.dataSets = dataSets;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseNameableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "organisationUnits", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "organisationUnit", namespace = DxfNamespaces.DXF_2_0)
    public List<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setOrganisationUnits( List<OrganisationUnit> organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }

    @JsonProperty
    @JsonSerialize( contentUsing = JacksonPeriodSerializer.class )
    @JsonDeserialize( contentUsing = JacksonPeriodDeserializer.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "periods", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "period", namespace = DxfNamespaces.DXF_2_0)
    public List<Period> getPeriods()
    {
        return periods;
    }

    public void setPeriods( List<Period> periods )
    {
        this.periods = periods;
    }

    @JsonProperty( value = "relativePeriods" )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public RelativePeriods getRelatives()
    {
        return relatives;
    }

    public void setRelatives( RelativePeriods relatives )
    {
        this.relatives = relatives;
    }

    public List<DataElementCategoryDimension> getCategoryDimensions()
    {
        return categoryDimensions;
    }

    public void setCategoryDimensions( List<DataElementCategoryDimension> categoryDimensions )
    {
        this.categoryDimensions = categoryDimensions;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "dataElementGroups", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "dataElementGroup", namespace = DxfNamespaces.DXF_2_0)
    public List<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }

    public void setDataElementGroups( List<DataElementGroup> dataElementGroups )
    {
        this.dataElementGroups = dataElementGroups;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "organisationUnitGroups", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "organisationUnitGroup", namespace = DxfNamespaces.DXF_2_0)
    public List<OrganisationUnitGroup> getOrganisationUnitGroups()
    {
        return organisationUnitGroups;
    }

    public void setOrganisationUnitGroups( List<OrganisationUnitGroup> organisationUnitGroups )
    {
        this.organisationUnitGroups = organisationUnitGroups;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isUserOrganisationUnit()
    {
        return userOrganisationUnit;
    }

    public void setUserOrganisationUnit( boolean userOrganisationUnit )
    {
        this.userOrganisationUnit = userOrganisationUnit;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isUserOrganisationUnitChildren()
    {
        return userOrganisationUnitChildren;
    }

    public void setUserOrganisationUnitChildren( boolean userOrganisationUnitChildren )
    {
        this.userOrganisationUnitChildren = userOrganisationUnitChildren;
    }

    // -------------------------------------------------------------------------
    // Web domain properties
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonDeserialize( contentAs = BaseDimensionalObject.class )
    @JsonSerialize( contentAs = BaseDimensionalObject.class )
    @JsonView( {DimensionalView.class} )
    @JacksonXmlElementWrapper( localName = "columns", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "column", namespace = DxfNamespaces.DXF_2_0)
    public List<DimensionalObject> getColumns()
    {
        return columns;
    }

    public void setColumns( List<DimensionalObject> columns )
    {
        this.columns = columns;
    }

    @JsonProperty
    @JsonDeserialize( contentAs = BaseDimensionalObject.class )
    @JsonSerialize( contentAs = BaseDimensionalObject.class )
    @JsonView( {DimensionalView.class} )
    @JacksonXmlElementWrapper( localName = "rows", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "row", namespace = DxfNamespaces.DXF_2_0)
    public List<DimensionalObject> getRows()
    {
        return rows;
    }

    public void setRows( List<DimensionalObject> rows )
    {
        this.rows = rows;
    }

    @JsonProperty
    @JsonDeserialize( contentAs = BaseDimensionalObject.class )
    @JsonSerialize( contentAs = BaseDimensionalObject.class )
    @JsonView( {DimensionalView.class} )
    @JacksonXmlElementWrapper( localName = "filters", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "filter", namespace = DxfNamespaces.DXF_2_0)
    public List<DimensionalObject> getFilters()
    {
        return filters;
    }

    public void setFilters( List<DimensionalObject> filters )
    {
        this.filters = filters;
    }

    @JsonProperty
    @JsonView({ DetailedView.class, ExportView.class })
    public Map<String, String> getParentGraphMap()
    {
        return parentGraphMap;
    }

    public void setParentGraphMap( Map<String, String> parentGraphMap )
    {
        this.parentGraphMap = parentGraphMap;
    }
}
