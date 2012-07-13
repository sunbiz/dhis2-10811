package org.hisp.dhis.dataset;

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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.BaseNameableObject;
import org.hisp.dhis.common.Dxf2Namespace;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.adapter.JacksonPeriodTypeDeserializer;
import org.hisp.dhis.common.adapter.JacksonPeriodTypeSerializer;
import org.hisp.dhis.common.annotation.Scanned;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.dataentryform.DataEntryForm;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.PeriodType;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used for defining the standardized DataSets. A DataSet consists
 * of a collection of DataElements.
 *
 * @author Kristian Nordal
 */
@JacksonXmlRootElement( localName = "dataSet", namespace = Dxf2Namespace.NAMESPACE )
public class DataSet
    extends BaseNameableObject
{
    public static final String TYPE_DEFAULT = "default";
    public static final String TYPE_SECTION = "section";
    public static final String TYPE_CUSTOM = "custom";

    public static final int NO_EXPIRY = 0;

    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = -2466830446144115499L;

    /**
     * The PeriodType indicating the frequency that this DataSet should be used
     */
    private PeriodType periodType;

    /**
     * All DataElements associated with this DataSet.
     */
    @Scanned
    private Set<DataElement> dataElements = new HashSet<DataElement>();

    /**
     * Indicators associated with this data set. Indicators are used for view and
     * output purposes, such as calculated fields in forms and reports.
     */
    @Scanned
    private Set<Indicator> indicators = new HashSet<Indicator>();

    /**
     * The DataElementOperands for which data must be entered in order for the
     * DataSet to be considered as complete.
     */
    private Set<DataElementOperand> compulsoryDataElementOperands = new HashSet<DataElementOperand>();

    /**
     * All Sources that register data with this DataSet.
     */
    @Scanned
    private Set<OrganisationUnit> sources = new HashSet<OrganisationUnit>();

    /**
     * The Sections associated with the DataSet.
     */
    private Set<Section> sections = new HashSet<Section>();

    /**
     * Indicating position in the custom sort order.
     */
    private Integer sortOrder;

    /**
     * Property indicating if the dataset could be collected using mobile data entry.
     */
    private boolean mobile;

    /**
     * Property indicating whether it should allow to enter data for future periods.
     */
    private boolean allowFuturePeriods;
    
    /**
     * Indicating custom data entry form.
     */
    private DataEntryForm dataEntryForm;

    /**
     * Indicating version number.
     */
    private Integer version;

    /**
     * How many days after period is over will this dataSet auto-lock
     */
    private int expiryDays;
    
    /**
     * Property indicating whether aggregation should be skipped.
     */
    private boolean skipAggregation;
    
    // -------------------------------------------------------------------------
    // Contructors
    // -------------------------------------------------------------------------

    public DataSet()
    {
    }

    public DataSet( String name )
    {
        this.name = name;
    }

    public DataSet( String name, PeriodType periodType )
    {
        this.name = name;
        this.periodType = periodType;
    }

    public DataSet( String name, String shortName, PeriodType periodType )
    {
        this.name = name;
        this.shortName = shortName;
        this.periodType = periodType;
    }

    public DataSet( String name, String shortName, String code, PeriodType periodType )
    {
        this.name = name;
        this.shortName = shortName;
        this.code = code;
        this.periodType = periodType;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addOrganisationUnit( OrganisationUnit unit )
    {
        sources.add( unit );
        unit.getDataSets().add( this );
    }

    public void removeOrganisationUnit( OrganisationUnit unit )
    {
        sources.remove( unit );
        unit.getDataSets().remove( this );
    }

    public void removeAllOrganisationUnits()
    {
        sources.clear();
    }

    public void updateOrganisationUnits( Set<OrganisationUnit> updates )
    {
        for ( OrganisationUnit unit : new HashSet<OrganisationUnit>( sources ) )
        {
            if ( !updates.contains( unit ) )
            {
                removeOrganisationUnit( unit );
            }
        }

        for ( OrganisationUnit unit : updates )
        {
            addOrganisationUnit( unit );
        }
    }

    public void addDataElement( DataElement dataElement )
    {
        dataElements.add( dataElement );
        dataElement.getDataSets().add( this );
    }

    public void removeDataElement( DataElement dataElement )
    {
        dataElements.remove( dataElement );
        dataElement.getDataSets().remove( dataElement );
    }

    public void removeAllDataElements()
    {
        dataElements.clear();
    }

    public void updateDataElements( Set<DataElement> updates )
    {
        for ( DataElement dataElement : new HashSet<DataElement>( dataElements ) )
        {
            if ( !updates.contains( dataElement ) )
            {
                removeDataElement( dataElement );
            }
        }

        for ( DataElement dataElement : updates )
        {
            addDataElement( dataElement );
        }
    }

    public void addIndicator( Indicator indicator )
    {
        indicators.add( indicator );
        indicator.getDataSets().add( this );
    }

    public void removeIndicator( Indicator indicator )
    {
        indicators.remove( indicator );
        indicator.getDataSets().remove( this );
    }

    public void removeAllIndicators()
    {
        indicators.clear();
    }

    public void addCompulsoryDataElementOperand( DataElementOperand dataElementOperand )
    {
        compulsoryDataElementOperands.add( dataElementOperand );
    }

    public void removeCompulsoryDataElementOperand( DataElementOperand dataElementOperand )
    {
        compulsoryDataElementOperands.remove( dataElementOperand );
    }

    public void removeAllCompulsoryDataElementOperands()
    {
        compulsoryDataElementOperands.clear();
    }

    public boolean hasDataEntryForm()
    {
        return dataEntryForm != null;
    }

    public boolean hasSections()
    {
        return sections != null && sections.size() > 0;
    }

    public String getDataSetType()
    {
        if ( hasDataEntryForm() )
        {
            return TYPE_CUSTOM;
        }

        if ( hasSections() )
        {
            return TYPE_SECTION;
        }

        return TYPE_DEFAULT;
    }

    public Set<DataElement> getDataElementsInSections()
    {
        Set<DataElement> dataElements = new HashSet<DataElement>();

        for ( Section section : sections )
        {
            dataElements.addAll( section.getDataElements() );
        }

        return dataElements;
    }

    public DataSet increaseVersion()
    {
        version = version != null ? version + 1 : 1;
        return this;
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof DataSet) )
        {
            return false;
        }

        final DataSet other = (DataSet) o;

        return name.equals( other.getName() );
    }

    @Override
    public String toString()
    {
        return "[" + name + "]";
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( using = JacksonPeriodTypeSerializer.class )
    @JsonDeserialize( using = JacksonPeriodTypeDeserializer.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public PeriodType getPeriodType()
    {
        return periodType;
    }

    public void setPeriodType( PeriodType periodType )
    {
        this.periodType = periodType;
    }

    //@JsonProperty
    //@JsonView( {DetailedView.class, ExportView.class} )
    //@JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    // Leaving dataEntryForm out for the moment since we are using IDs there and not UIDs.
    // At some point it should also be upgraded to idObject (to make it work a bit better with the importer).
    public DataEntryForm getDataEntryForm()
    {
        return dataEntryForm;
    }

    public void setDataEntryForm( DataEntryForm dataEntryForm )
    {
        this.dataEntryForm = dataEntryForm;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "dataElements", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "dataElement", namespace = Dxf2Namespace.NAMESPACE )
    public Set<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( Set<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "indicators", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "indicator", namespace = Dxf2Namespace.NAMESPACE )
    public Set<Indicator> getIndicators()
    {
        return indicators;
    }

    public void setIndicators( Set<Indicator> indicators )
    {
        this.indicators = indicators;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "compulsoryDataElementOperands", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "compulsoryDataElementOperand", namespace = Dxf2Namespace.NAMESPACE )
    public Set<DataElementOperand> getCompulsoryDataElementOperands()
    {
        return compulsoryDataElementOperands;
    }

    public void setCompulsoryDataElementOperands( Set<DataElementOperand> compulsoryDataElementOperands )
    {
        this.compulsoryDataElementOperands = compulsoryDataElementOperands;
    }

    @JsonProperty( value = "organisationUnits" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "organisationUnits", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "organisationUnit", namespace = Dxf2Namespace.NAMESPACE )
    public Set<OrganisationUnit> getSources()
    {
        return sources;
    }

    public void setSources( Set<OrganisationUnit> sources )
    {
        this.sources = sources;
    }

    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( Integer sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "sections", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "section", namespace = Dxf2Namespace.NAMESPACE )
    public Set<Section> getSections()
    {
        return sections;
    }

    public void setSections( Set<Section> sections )
    {
        this.sections = sections;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isMobile()
    {
        return mobile;
    }

    public void setMobile( boolean mobile )
    {
        this.mobile = mobile;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isAllowFuturePeriods()
    {
        return allowFuturePeriods;
    }

    public void setAllowFuturePeriods( boolean allowFuturePeriods )
    {
        this.allowFuturePeriods = allowFuturePeriods;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( Integer version )
    {
        this.version = version;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public int getExpiryDays()
    {
        return expiryDays;
    }

    public void setExpiryDays( int expiryDays )
    {
        this.expiryDays = expiryDays;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isSkipAggregation()
    {
        return skipAggregation;
    }

    public void setSkipAggregation( boolean skipAggregation )
    {
        this.skipAggregation = skipAggregation;
    }

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        super.mergeWith( other );

        if ( other.getClass().isInstance( this ) )
        {
            DataSet dataSet = (DataSet) other;

            periodType = dataSet.getPeriodType() == null ? periodType : dataSet.getPeriodType();
            sortOrder = dataSet.getSortOrder() == null ? sortOrder : dataSet.getSortOrder();
            mobile = dataSet.isMobile();
            dataEntryForm = dataSet.getDataEntryForm() == null ? dataEntryForm : dataSet.getDataEntryForm();
            version = dataSet.getVersion() == null ? version : dataSet.getVersion();
            expiryDays = dataSet.getExpiryDays();

            removeAllDataElements();

            for ( DataElement dataElement : dataSet.getDataElements() )
            {
                addDataElement( dataElement );
            }

            removeAllIndicators();

            for ( Indicator indicator : dataSet.getIndicators() )
            {
                addIndicator( indicator );
            }

            removeAllCompulsoryDataElementOperands();

            for ( DataElementOperand dataElementOperand : dataSet.getCompulsoryDataElementOperands() )
            {
                addCompulsoryDataElementOperand( dataElementOperand );
            }

            removeAllOrganisationUnits();

            for ( OrganisationUnit organisationUnit : dataSet.getSources() )
            {
                addOrganisationUnit( organisationUnit );
            }
        }
    }
}
