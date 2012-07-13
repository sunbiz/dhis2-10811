package org.hisp.dhis.chart;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.BaseNameableObject;
import org.hisp.dhis.common.Dxf2Namespace;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.NameableObject;
import org.hisp.dhis.common.adapter.JacksonPeriodDeserializer;
import org.hisp.dhis.common.adapter.JacksonPeriodSerializer;
import org.hisp.dhis.common.annotation.Scanned;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lars Helge Overland
 */
@JacksonXmlRootElement( localName = "chart", namespace = Dxf2Namespace.NAMESPACE )
public class Chart
    extends BaseIdentifiableObject
{
    private static final long serialVersionUID = 2570074075484545534L;

    public static final String DIMENSION_PERIOD_INDICATOR = "period";
    public static final String DIMENSION_ORGANISATIONUNIT_INDICATOR = "organisationUnit";
    public static final String DIMENSION_INDICATOR_PERIOD = "indicator";
    public static final String DIMENSION_PERIOD_DATAELEMENT = "period_dataElement";
    public static final String DIMENSION_ORGANISATIONUNIT_DATAELEMENT = "organisationUnit_dataElement";
    public static final String DIMENSION_DATAELEMENT_PERIOD = "dataElement_period";

    public static final String SIZE_NORMAL = "normal";
    public static final String SIZE_WIDE = "wide";
    public static final String SIZE_TALL = "tall";

    public static final String TYPE_COLUMN = "COLUMN";
    public static final String TYPE_STACKED_COLUMN = "STACKEDCOLUMN";
    public static final String TYPE_BAR = "BAR";
    public static final String TYPE_STACKED_BAR = "STACKEDBAR";
    public static final String TYPE_LINE = "LINE";
    public static final String TYPE_AREA = "AREA";
    public static final String TYPE_PIE = "PIE";

    public static final String DIMENSION_DATA = "DATA";
    public static final String DIMENSION_PERIOD = "PERIOD";
    public static final String DIMENSION_ORGANISATIONUNIT = "ORGANISATIONUNIT";

    private String domainAxisLabel;

    private String rangeAxisLabel;

    private String type;

    private String series;

    private String category;

    private String filter;

    private boolean hideLegend;

    private boolean regression;

    private boolean hideSubtitle;

    private Double targetLineValue;

    private String targetLineLabel;

    private Double baseLineValue;

    private String baseLineLabel;

    @Scanned
    private List<Indicator> indicators = new ArrayList<Indicator>();

    @Scanned
    private List<DataElement> dataElements = new ArrayList<DataElement>();

    @Scanned
    private List<DataSet> dataSets = new ArrayList<DataSet>();

    @Scanned
    private List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();

    @Scanned
    private List<Period> periods = new ArrayList<Period>();
    
    private RelativePeriods relatives;

    private boolean userOrganisationUnit;

    private boolean userOrganisationUnitChildren;

    private boolean showData;

    private User user;

    private OrganisationUnitGroupSet organisationUnitGroupSet;

    // -------------------------------------------------------------------------
    // Transient properties
    // -------------------------------------------------------------------------

    private transient I18nFormat format;

    private transient List<Period> relativePeriods = new ArrayList<Period>();

    private transient List<OrganisationUnit> relativeOrganisationUnits = new ArrayList<OrganisationUnit>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Chart()
    {
    }

    public Chart( String name )
    {
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public List<NameableObject> series()
    {
        return dimensionToList( series );
    }

    public List<NameableObject> category()
    {
        return dimensionToList( category );
    }

    public NameableObject filter()
    {
        List<NameableObject> list = dimensionToList( filter );

        return list != null && !list.isEmpty() ? list.iterator().next() : null;
    }

    public String getTitle()
    {
        if ( DIMENSION_PERIOD.equals( filter ) )
        {
            return format.formatPeriod( getRelativePeriods().get( 0 ) );
        }

        return filter().getName();
    }

    public List<OrganisationUnit> getAllOrganisationUnits()
    {
        if ( relativeOrganisationUnits != null && !relativeOrganisationUnits.isEmpty() )
        {
            return relativeOrganisationUnits;
        }
        else
        {
            return organisationUnits;
        }
    }
    
    public OrganisationUnit getFirstOrganisationUnit()
    {
        List<OrganisationUnit> units = getAllOrganisationUnits();
        return units != null && !units.isEmpty() ? units.iterator().next() : null;
    }

    private List<NameableObject> dimensionToList( String dimension )
    {
        List<NameableObject> list = new ArrayList<NameableObject>();

        if ( DIMENSION_DATA.equals( dimension ) )
        {
            list.addAll( dataElements );
            list.addAll( indicators );
            list.addAll( dataSets );
        }
        else if ( DIMENSION_PERIOD.equals( dimension ) )
        {
            namePeriods( getRelativePeriods(), format );

            list.addAll( relativePeriods );
        }
        else if ( DIMENSION_ORGANISATIONUNIT.equals( dimension ) )
        {
            if ( isOrganisationUnitGroupBased() )
            {
                list.addAll( organisationUnitGroupSet.getOrganisationUnitGroups() );
            }
            else
            {
                list.addAll( getAllOrganisationUnits() );
            }
        }

        return list;
    }

    private void namePeriods( List<Period> periods, I18nFormat format )
    {
        for ( Period period : periods )
        {
            period.setName( format.formatPeriod( period ) );
            period.setShortName( format.formatPeriod( period ) );
        }
    }

    /**
     * Indicates whether this report table is based on organisation unit groups
     * or the organisation unit hierarchy.
     */
    public boolean isOrganisationUnitGroupBased()
    {
        return organisationUnitGroupSet != null && organisationUnitGroupSet.getOrganisationUnitGroups() != null;
    }
    
    public void removeAllOrganisationUnits()
    {
        organisationUnits.clear();
    }

    public void removeAllDataSets()
    {
        dataSets.clear();
    }

    public void removeAllDataElements()
    {
        dataElements.clear();
    }

    public void removeAllIndicators()
    {
        indicators.clear();
    }

    /**
     * Sets all dimensions for this chart.
     *
     * @param series   the series dimension.
     * @param category the category dimension.
     * @param filter   the filter dimension.
     */
    public void setDimensions( String series, String category, String filter )
    {
        this.series = series;
        this.category = category;
        this.filter = filter;
    }

    public boolean hasIndicators()
    {
        return indicators != null && indicators.size() > 0;
    }

    public boolean hasDataElements()
    {
        return dataElements != null && dataElements.size() > 0;
    }

    public boolean hasDataSets()
    {
        return dataSets != null && dataSets.size() > 0;
    }

    public boolean isType( String type )
    {
        return this.type != null && this.type.equals( type );
    }

    public boolean isTargetLine()
    {
        return targetLineValue != null;
    }

    public boolean isBaseLine()
    {
        return baseLineValue != null;
    }

    public int getWidth()
    {
        return 700;
    }

    public int getHeight()
    {
        return 500;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public String getDomainAxisLabel()
    {
        return domainAxisLabel;
    }

    public void setDomainAxisLabel( String domainAxisLabel )
    {
        this.domainAxisLabel = domainAxisLabel;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public String getRangeAxisLabel()
    {
        return rangeAxisLabel;
    }

    public void setRangeAxisLabel( String rangeAxisLabel )
    {
        this.rangeAxisLabel = rangeAxisLabel;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public String getSeries()
    {
        return series;
    }

    public void setSeries( String series )
    {
        this.series = series;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public String getCategory()
    {
        return category;
    }

    public void setCategory( String category )
    {
        this.category = category;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public String getFilter()
    {
        return filter;
    }

    public void setFilter( String filter )
    {
        this.filter = filter;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isHideLegend()
    {
        return hideLegend;
    }

    public void setHideLegend( boolean hideLegend )
    {
        this.hideLegend = hideLegend;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isRegression()
    {
        return regression;
    }

    public void setRegression( boolean regression )
    {
        this.regression = regression;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public Double getTargetLineValue()
    {
        return targetLineValue;
    }

    public void setTargetLineValue( Double targetLineValue )
    {
        this.targetLineValue = targetLineValue;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public String getTargetLineLabel()
    {
        return targetLineLabel;
    }

    public void setTargetLineLabel( String targetLineLabel )
    {
        this.targetLineLabel = targetLineLabel;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public Double getBaseLineValue()
    {
        return baseLineValue;
    }

    public void setBaseLineValue( Double baseLineValue )
    {
        this.baseLineValue = baseLineValue;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public String getBaseLineLabel()
    {
        return baseLineLabel;
    }

    public void setBaseLineLabel( String baseLineLabel )
    {
        this.baseLineLabel = baseLineLabel;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isHideSubtitle()
    {
        return hideSubtitle;
    }

    public void setHideSubtitle( Boolean hideSubtitle )
    {
        this.hideSubtitle = hideSubtitle;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseNameableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "indicators", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "indicator", namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlElementWrapper( localName = "dataElements", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "dataElement", namespace = Dxf2Namespace.NAMESPACE )
    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( List<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseNameableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "dataSets", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "dataSet", namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlElementWrapper( localName = "organisationUnits", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "organisationUnit", namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlElementWrapper( localName = "periods", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "period", namespace = Dxf2Namespace.NAMESPACE ) 
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
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public RelativePeriods getRelatives()
    {
        return relatives;
    }

    public void setRelatives( RelativePeriods relatives )
    {
        this.relatives = relatives;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isUserOrganisationUnitChildren()
    {
        return userOrganisationUnitChildren;
    }

    public void setUserOrganisationUnitChildren( boolean userOrganisationUnitChildren )
    {
        this.userOrganisationUnitChildren = userOrganisationUnitChildren;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isShowData()
    {
        return showData;
    }

    public void setShowData( boolean showData )
    {
        this.showData = showData;
    }


    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public OrganisationUnitGroupSet getOrganisationUnitGroupSet()
    {
        return organisationUnitGroupSet;
    }

    public void setOrganisationUnitGroupSet( OrganisationUnitGroupSet organisationUnitGroupSet )
    {
        this.organisationUnitGroupSet = organisationUnitGroupSet;
    }

    // -------------------------------------------------------------------------
    // Getters and setters for transient fields
    // -------------------------------------------------------------------------

    public I18nFormat getFormat()
    {
        return format;
    }

    public void setFormat( I18nFormat format )
    {
        this.format = format;
    }

    @JsonIgnore
    public List<Period> getRelativePeriods()
    {
        return relativePeriods;
    }

    @JsonIgnore
    public void setRelativePeriods( List<Period> relativePeriods )
    {
        this.relativePeriods = relativePeriods;
    }

    @JsonIgnore
    public List<OrganisationUnit> getRelativeOrganisationUnits()
    {
        return relativeOrganisationUnits;
    }

    @JsonIgnore
    public void setRelativeOrganisationUnits( List<OrganisationUnit> relativeOrganisationUnits )
    {
        this.relativeOrganisationUnits = relativeOrganisationUnits;
    }

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        super.mergeWith( other );

        if ( other.getClass().isInstance( this ) )
        {
            Chart chart = (Chart) other;

            domainAxisLabel = chart.getDomainAxisLabel() == null ? domainAxisLabel : chart.getDomainAxisLabel();
            rangeAxisLabel = chart.getRangeAxisLabel() == null ? rangeAxisLabel : chart.getRangeAxisLabel();
            type = chart.getType() == null ? type : chart.getType();
            series = chart.getSeries() == null ? series : chart.getSeries();
            category = chart.getCategory() == null ? category : chart.getCategory();
            filter = chart.getFilter() == null ? filter : chart.getFilter();
            hideLegend = chart.isHideLegend();
            regression = chart.isRegression();
            hideSubtitle = chart.isHideSubtitle();
            targetLineValue = chart.getTargetLineValue() == null ? targetLineValue : chart.getTargetLineValue();
            targetLineLabel = chart.getTargetLineLabel() == null ? targetLineLabel : chart.getTargetLineLabel();
            baseLineValue = chart.getBaseLineValue() == null ? baseLineValue : chart.getBaseLineValue();
            baseLineLabel = chart.getBaseLineLabel() == null ? baseLineLabel : chart.getBaseLineLabel();
            relatives = chart.getRelatives() == null ? relatives : chart.getRelatives();
            userOrganisationUnit = chart.isUserOrganisationUnit();
            user = chart.getUser() == null ? user : chart.getUser();
            organisationUnitGroupSet = chart.getOrganisationUnitGroupSet() == null ? organisationUnitGroupSet : chart.getOrganisationUnitGroupSet();

            removeAllIndicators();
            indicators.addAll( chart.getIndicators() );

            removeAllDataElements();
            dataElements.addAll( chart.getDataElements() );

            removeAllDataSets();
            dataSets.addAll( chart.getDataSets() );

            removeAllOrganisationUnits();
            organisationUnits.addAll( chart.getOrganisationUnits() );
        }
    }
}
