package org.hisp.dhis.mapping;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.adapter.JacksonPeriodDeserializer;
import org.hisp.dhis.common.adapter.JacksonPeriodSerializer;
import org.hisp.dhis.common.adapter.JacksonPeriodTypeDeserializer;
import org.hisp.dhis.common.adapter.JacksonPeriodTypeSerializer;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;

/**
 * @author Jan Henrik Overland
 */
@JacksonXmlRootElement( localName = "mapView", namespace = DxfNamespaces.DXF_2_0)
public class MapView
    extends BaseIdentifiableObject
{
    public static final String LAYER_BOUNDARY = "boundary";
    public static final String LAYER_THEMATIC1 = "thematic1";
    public static final String LAYER_THEMATIC2 = "thematic2";
    public static final String LAYER_FACILITY = "facility";
    public static final String LAYER_SYMBOL = "symbol";

    public static final String VALUE_TYPE_INDICATOR = "indicator";
    public static final String VALUE_TYPE_DATAELEMENT = "dataelement";

    public static final String LEGEND_TYPE_AUTOMATIC = "automatic";
    public static final String LEGEND_TYPE_PREDEFINED = "predefined";

    private static final long serialVersionUID = 1866358818802275436L;

    private String layer;

    private String valueType;

    private IndicatorGroup indicatorGroup;

    private Indicator indicator;

    private DataElementGroup dataElementGroup;

    private DataElement dataElement;

    private Period period;

    private OrganisationUnit parentOrganisationUnit;

    private OrganisationUnitLevel organisationUnitLevel;

    private String legendType;

    private Integer method;

    private Integer classes;

    private String colorLow;

    private String colorHigh;

    private MapLegendSet legendSet;

    private Integer radiusLow;

    private Integer radiusHigh;

    private Double opacity;

    private OrganisationUnitGroupSet organisationUnitGroupSet;

    private Integer areaRadius;

    private transient String parentGraph;

    private transient int parentLevel;

    public MapView()
    {
    }

    public MapView( String layer, String name, String valueType, IndicatorGroup indicatorGroup, Indicator indicator,
        DataElementGroup dataElementGroup, DataElement dataElement,
        Period period, OrganisationUnit parentOrganisationUnit, OrganisationUnitLevel organisationUnitLevel,
        String legendType, Integer method, Integer classes, String colorLow, String colorHigh,
        MapLegendSet legendSet, Integer radiusLow, Integer radiusHigh, Double opacity )
    {
        this.layer = layer;
        this.name = name;
        this.valueType = valueType;
        this.indicatorGroup = indicatorGroup;
        this.indicator = indicator;
        this.dataElementGroup = dataElementGroup;
        this.dataElement = dataElement;
        this.period = period;
        this.parentOrganisationUnit = parentOrganisationUnit;
        this.organisationUnitLevel = organisationUnitLevel;
        this.legendType = legendType;
        this.method = method;
        this.classes = classes;
        this.colorLow = colorLow;
        this.colorHigh = colorHigh;
        this.legendSet = legendSet;
        this.radiusLow = radiusLow;
        this.radiusHigh = radiusHigh;
        this.opacity = opacity;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @Override
    public String getName()
    {
        return indicator != null ? indicator.getName() : dataElement != null ? dataElement.getName() : uid;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getLayer()
    {
        return layer;
    }

    public void setLayer( String layer )
    {
        this.layer = layer;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getValueType()
    {
        return valueType;
    }

    public void setValueType( String valueType )
    {
        this.valueType = valueType;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public IndicatorGroup getIndicatorGroup()
    {
        return indicatorGroup;
    }

    public void setIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        this.indicatorGroup = indicatorGroup;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Indicator getIndicator()
    {
        return indicator;
    }

    public void setIndicator( Indicator indicator )
    {
        this.indicator = indicator;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public DataElementGroup getDataElementGroup()
    {
        return dataElementGroup;
    }

    public void setDataElementGroup( DataElementGroup dataElementGroup )
    {
        this.dataElementGroup = dataElementGroup;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }

    @JsonProperty
    @JsonSerialize( using = JacksonPeriodTypeSerializer.class )
    @JsonDeserialize( using = JacksonPeriodTypeDeserializer.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public PeriodType getPeriodType()
    {
        return period != null ? period.getPeriodType() : null;
    }

    public void setPeriodType( PeriodType periodType )
    {
        // ignore
    }

    @JsonProperty
    @JsonSerialize( using = JacksonPeriodSerializer.class )
    @JsonDeserialize( using = JacksonPeriodDeserializer.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Period getPeriod()
    {
        return period;
    }

    public void setPeriod( Period period )
    {
        this.period = period;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public OrganisationUnit getParentOrganisationUnit()
    {
        return parentOrganisationUnit;
    }

    public void setParentOrganisationUnit( OrganisationUnit parentOrganisationUnit )
    {
        this.parentOrganisationUnit = parentOrganisationUnit;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public OrganisationUnitLevel getOrganisationUnitLevel()
    {
        return organisationUnitLevel;
    }

    public void setOrganisationUnitLevel( OrganisationUnitLevel organisationUnitLevel )
    {
        this.organisationUnitLevel = organisationUnitLevel;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getLegendType()
    {
        return legendType;
    }

    public void setLegendType( String legendType )
    {
        this.legendType = legendType;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Integer getMethod()
    {
        return method;
    }

    public void setMethod( Integer method )
    {
        this.method = method;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Integer getClasses()
    {
        return classes;
    }

    public void setClasses( Integer classes )
    {
        this.classes = classes;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getColorLow()
    {
        return colorLow;
    }

    public void setColorLow( String colorLow )
    {
        this.colorLow = colorLow;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getColorHigh()
    {
        return colorHigh;
    }

    public void setColorHigh( String colorHigh )
    {
        this.colorHigh = colorHigh;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public MapLegendSet getLegendSet()
    {
        return legendSet;
    }

    public void setLegendSet( MapLegendSet legendSet )
    {
        this.legendSet = legendSet;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Integer getRadiusLow()
    {
        return radiusLow;
    }

    public void setRadiusLow( Integer radiusLow )
    {
        this.radiusLow = radiusLow;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Integer getRadiusHigh()
    {
        return radiusHigh;
    }

    public void setRadiusHigh( Integer radiusHigh )
    {
        this.radiusHigh = radiusHigh;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Double getOpacity()
    {
        return opacity;
    }

    public void setOpacity( Double opacity )
    {
        this.opacity = opacity;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public OrganisationUnitGroupSet getOrganisationUnitGroupSet()
    {
        return organisationUnitGroupSet;
    }

    public void setOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet )
    {
        this.organisationUnitGroupSet = organisationUnitGroupSet;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Integer getAreaRadius()
    {
        return areaRadius;
    }

    public void setAreaRadius( Integer areaRadius )
    {
        this.areaRadius = areaRadius;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getParentGraph()
    {
        return parentGraph;
    }

    public void setParentGraph( String parentGraph )
    {
        this.parentGraph = parentGraph;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public int getParentLevel()
    {
        return parentLevel;
    }

    public void setParentLevel( int parentLevel )
    {
        this.parentLevel = parentLevel;
    }

    @Override
    public String toString()
    {
        return "[Indicator: " + indicator + ", org unit: " +
            parentOrganisationUnit + ", period: " + period + ", value type: " + valueType + "]";
    }

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        super.mergeWith( other );

        if ( other.getClass().isInstance( this ) )
        {
            MapView mapView = (MapView) other;

            layer = mapView.getLayer() == null ? layer : mapView.getLayer();
            valueType = mapView.getValueType() == null ? valueType : mapView.getValueType();
            indicatorGroup = mapView.getIndicatorGroup() == null ? indicatorGroup : mapView.getIndicatorGroup();
            indicator = mapView.getIndicator() == null ? indicator : mapView.getIndicator();
            dataElementGroup = mapView.getDataElementGroup() == null ? dataElementGroup : mapView.getDataElementGroup();
            dataElement = mapView.getDataElement() == null ? dataElement : mapView.getDataElement();
            period = mapView.getPeriod() == null ? period : mapView.getPeriod();
            parentOrganisationUnit = mapView.getParentOrganisationUnit() == null ? parentOrganisationUnit : mapView.getParentOrganisationUnit();
            organisationUnitLevel = mapView.getOrganisationUnitLevel() == null ? organisationUnitLevel : mapView.getOrganisationUnitLevel();
            legendType = mapView.getLegendType() == null ? legendType : mapView.getLegendType();
            method = mapView.getMethod() == null ? method : mapView.getMethod();
            classes = mapView.getClasses() == null ? classes : mapView.getClasses();
            colorLow = mapView.getColorLow() == null ? colorLow : mapView.getColorLow();
            colorHigh = mapView.getColorHigh() == null ? colorHigh : mapView.getColorHigh();
            legendSet = mapView.getLegendSet() == null ? legendSet : mapView.getLegendSet();
            radiusLow = mapView.getRadiusLow() == null ? radiusLow : mapView.getRadiusLow();
            radiusHigh = mapView.getRadiusHigh() == null ? radiusHigh : mapView.getRadiusHigh();
            opacity = mapView.getOpacity() == null ? opacity : mapView.getOpacity();
            organisationUnitGroupSet = mapView.getOrganisationUnitGroupSet() == null ? organisationUnitGroupSet : mapView.getOrganisationUnitGroupSet();
            areaRadius = mapView.getAreaRadius() == null ? areaRadius : mapView.getAreaRadius();
        }
    }
}
