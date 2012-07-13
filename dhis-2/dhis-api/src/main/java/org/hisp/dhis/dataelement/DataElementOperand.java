package org.hisp.dhis.dataelement;

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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.Dxf2Namespace;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This object can act both as a hydrated persisted object and as a wrapper
 * object (but not both at the same time).
 *
 * @author Abyot Asalefew
 */
@JacksonXmlRootElement( localName = "dataElementOperand", namespace = Dxf2Namespace.NAMESPACE )
public class DataElementOperand
    implements Serializable, Comparable<DataElementOperand>
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 2490172100580528479L;

    public static final String SEPARATOR = ".";
    public static final String NAME_TOTAL = "(Total)";

    private static final String TYPE_VALUE = "value";
    private static final String TYPE_TOTAL = "total";

    private static final String SPACE = " ";
    private static final String COLUMN_PREFIX = "de";
    private static final String COLUMN_SEPARATOR = "_";

    // -------------------------------------------------------------------------
    // Persisted properties
    // -------------------------------------------------------------------------

    private int id;

    private DataElement dataElement;

    private DataElementCategoryOptionCombo categoryOptionCombo;

    // -------------------------------------------------------------------------
    // Transient properties
    // -------------------------------------------------------------------------

    private int dataElementId;

    private int optionComboId;

    private String operandId;

    private String operandName;

    private String valueType;

    private String aggregationOperator;

    private List<Integer> aggregationLevels = new ArrayList<Integer>();

    private int frequencyOrder;

    private String operandType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataElementOperand()
    {
    }

    public DataElementOperand( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo )
    {
        this.dataElement = dataElement;
        this.categoryOptionCombo = categoryOptionCombo;
    }

    public DataElementOperand( int dataElementId, int optionComboId )
    {
        this.dataElementId = dataElementId;
        this.optionComboId = optionComboId;
        this.operandId = dataElementId + SEPARATOR + optionComboId;
    }

    public DataElementOperand( int dataElementId, int optionComboId, String operandName )
    {
        this.dataElementId = dataElementId;
        this.optionComboId = optionComboId;
        this.operandId = dataElementId + SEPARATOR + optionComboId;
        this.operandName = operandName;
    }

    public DataElementOperand( int dataElementId, int optionComboId, String operandName, String valueType,
        String aggregationOperator, List<Integer> aggregationLevels, int frequencyOrder )
    {
        this.dataElementId = dataElementId;
        this.optionComboId = optionComboId;
        this.operandId = dataElementId + SEPARATOR + optionComboId;
        this.operandName = operandName;
        this.valueType = valueType;
        this.aggregationOperator = aggregationOperator;
        this.aggregationLevels = aggregationLevels;
        this.frequencyOrder = frequencyOrder;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    /**
     * Tests whether the hierarchy level of the OrganisationUnit associated with
     * the relevant DataValue is equal to or higher than the relevant
     * aggregation level. Returns true if no aggregation levels exist.
     *
     * @param organisationUnitLevel the hierarchy level of the aggregation
     *                              OrganisationUnit.
     * @param dataValueLevel        the hierarchy level of the OrganisationUnit
     *                              associated with the relevant DataValue.
     */
    public boolean aggregationLevelIsValid( int organisationUnitLevel, int dataValueLevel )
    {
        if ( aggregationLevels == null || aggregationLevels.size() == 0 )
        {
            return true;
        }

        final Integer aggregationLevel = getRelevantAggregationLevel( organisationUnitLevel );

        return aggregationLevel == null || dataValueLevel <= aggregationLevel;
    }

    /**
     * Returns the relevant aggregation level for the DataElement. The relevant
     * aggregation level will be the next in ascending order after the
     * organisation unit level. If no aggregation levels lower than the
     * organisation unit level exist, null is returned.
     *
     * @param organisationUnitLevel the hiearchy level of the relevant
     *                              OrganisationUnit.
     */
    public Integer getRelevantAggregationLevel( int organisationUnitLevel )
    {
        Collections.sort( aggregationLevels );

        for ( final Integer aggregationLevel : aggregationLevels )
        {
            if ( aggregationLevel >= organisationUnitLevel )
            {
                return aggregationLevel;
            }
        }

        return null;
    }

    /**
     * Returns an id based on the DataElement and the
     * DataElementCategoryOptionCombo.
     *
     * @return the id.
     */
    public String getPersistedId()
    {
        return dataElement.getId() + SEPARATOR + categoryOptionCombo.getId();
    }

    /**
     * Returns a database-friendly name.
     *
     * @return the name.
     */
    public String getColumnName()
    {
        return COLUMN_PREFIX + dataElementId + COLUMN_SEPARATOR + optionComboId;
    }

    /**
     * Returns a pretty-print name based on the given data element and category
     * option combo.
     *
     * @param dataElement         the data element.
     * @param categoryOptionCombo the category option combo.
     * @return the name.
     */
    public static String getPrettyName( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo )
    {
        if ( dataElement == null ) // Invalid
        {
            return null;
        }

        if ( categoryOptionCombo == null ) // Total
        {
            return dataElement.getDisplayName() + SPACE + NAME_TOTAL;
        }

        return categoryOptionCombo.isDefault() ? dataElement.getDisplayName() : dataElement.getDisplayName() + SPACE + categoryOptionCombo.getName();
    }

    /**
     * Returns a pretty name, requires the operand to be in persistent mode.
     *
     * @return the name.
     */
    public String getPrettyName()
    {
        return getPrettyName( dataElement, categoryOptionCombo );
    }

    /**
     * Indicators whether this operand represents a total value or not.
     *
     * @return true or false.
     */
    public boolean isTotal()
    {
        return operandType != null && operandType.equals( TYPE_TOTAL );
    }

    /**
     * Updates all transient properties.
     *
     * @param dataElement
     * @param categoryOptionCombo
     */
    public void updateProperties( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo )
    {
        this.dataElementId = dataElement.getId();
        this.optionComboId = categoryOptionCombo.getId();
        this.operandId = dataElement.getId() + SEPARATOR + categoryOptionCombo.getId();
        this.operandName = getPrettyName( dataElement, categoryOptionCombo );
        this.aggregationOperator = dataElement.getAggregationOperator();
        this.frequencyOrder = dataElement.getFrequencyOrder();
        this.aggregationLevels = new ArrayList<Integer>( dataElement.getAggregationLevels() );
        this.valueType = dataElement.getType();
    }

    /**
     * Updates all transient properties.
     *
     * @param dataElement
     */
    public void updateProperties( DataElement dataElement )
    {
        this.dataElementId = dataElement.getId();
        this.operandId = String.valueOf( dataElement.getId() );
        this.operandName = dataElement.getDisplayName() + SPACE + NAME_TOTAL;
        this.aggregationOperator = dataElement.getAggregationOperator();
        this.frequencyOrder = dataElement.getFrequencyOrder();
        this.aggregationLevels = new ArrayList<Integer>( dataElement.getAggregationLevels() );
        this.valueType = dataElement.getType();
    }

    /**
     * Generates a DataElementOperand based on the given formula. The formula
     * needs to be on the form "[<dataelementid>.<categoryoptioncomboid>]".
     *
     * @param formula the formula.
     * @return a DataElementOperand.
     */
    public static DataElementOperand getOperand( String formula )
        throws NumberFormatException
    {
        formula = formula.replaceAll( "[\\[\\]]", "" );

        int dataElementId = 0;
        int categoryOptionComboId = 0;
        String operandType = null;

        if ( formula.contains( SEPARATOR ) ) // Value
        {
            dataElementId = Integer.parseInt( formula.substring( 0, formula.indexOf( SEPARATOR ) ) );
            categoryOptionComboId = Integer.parseInt( formula.substring( formula.indexOf( SEPARATOR ) + 1, formula.length() ) );

            operandType = TYPE_VALUE;
        }
        else // Total
        {
            dataElementId = Integer.parseInt( formula );

            operandType = TYPE_TOTAL;
        }

        final DataElementOperand operand = new DataElementOperand();
        operand.setDataElementId( dataElementId );
        operand.setOptionComboId( categoryOptionComboId );
        operand.setOperandType( operandType );

        return operand;
    }

    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    public DataElementCategoryOptionCombo getCategoryOptionCombo()
    {
        return categoryOptionCombo;
    }

    public void setCategoryOptionCombo( DataElementCategoryOptionCombo categoryOptionCombo )
    {
        this.categoryOptionCombo = categoryOptionCombo;
    }

    public int getDataElementId()
    {
        return dataElementId;
    }

    public void setDataElementId( int dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public int getOptionComboId()
    {
        return optionComboId;
    }

    public void setOptionComboId( int optionComboId )
    {
        this.optionComboId = optionComboId;
    }

    public String getOperandId()
    {
        return operandId;
    }

    public void setOperandId( String operandId )
    {
        this.operandId = operandId;
    }

    public String getOperandName()
    {
        return operandName;
    }

    public void setOperandName( String operandName )
    {
        this.operandName = operandName;
    }

    public String getValueType()
    {
        return valueType;
    }

    public void setValueType( String valueType )
    {
        this.valueType = valueType;
    }

    public String getAggregationOperator()
    {
        return aggregationOperator;
    }

    public void setAggregationOperator( String aggregationOperator )
    {
        this.aggregationOperator = aggregationOperator;
    }

    public List<Integer> getAggregationLevels()
    {
        return aggregationLevels;
    }

    public void setAggregationLevels( List<Integer> aggregationLevels )
    {
        this.aggregationLevels = aggregationLevels;
    }

    public int getFrequencyOrder()
    {
        return frequencyOrder;
    }

    public void setFrequencyOrder( int frequencyOrder )
    {
        this.frequencyOrder = frequencyOrder;
    }

    public String getOperandType()
    {
        return operandType;
    }

    public void setOperandType( String operandType )
    {
        this.operandType = operandType;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals, toString, compareTo
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((categoryOptionCombo == null) ? 0 : categoryOptionCombo.hashCode());
        result = prime * result + ((dataElement == null) ? 0 : dataElement.hashCode());
        result = prime * result + dataElementId;
        result = prime * result + optionComboId;

        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null )
        {
            return false;
        }

        if ( getClass() != object.getClass() )
        {
            return false;
        }

        final DataElementOperand other = (DataElementOperand) object;

        if ( dataElement == null )
        {
            if ( other.dataElement != null )
            {
                return false;
            }
        }
        else if ( !dataElement.equals( other.dataElement ) )
        {
            return false;
        }

        if ( categoryOptionCombo == null )
        {
            if ( other.categoryOptionCombo != null )
            {
                return false;
            }
        }
        else if ( !categoryOptionCombo.equals( other.categoryOptionCombo ) )
        {
            return false;
        }

        if ( dataElementId != other.dataElementId )
        {
            return false;
        }

        if ( optionComboId != other.optionComboId )
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return "DataElementOperand{" +
            "id=" + id +
            ", dataElement=" + dataElement +
            ", categoryOptionCombo=" + categoryOptionCombo +
            ", dataElementId=" + dataElementId +
            ", optionComboId=" + optionComboId +
            ", operandId='" + operandId + '\'' +
            ", operandName='" + operandName + '\'' +
            ", valueType='" + valueType + '\'' +
            ", aggregationOperator='" + aggregationOperator + '\'' +
            ", aggregationLevels=" + aggregationLevels +
            ", frequencyOrder=" + frequencyOrder +
            ", operandType='" + operandType + '\'' +
            '}';
    }

    public int compareTo( DataElementOperand other )
    {
        if ( this.getDataElementId() != other.getDataElementId() )
        {
            return this.getDataElementId() - other.getDataElementId();
        }

        return this.getOptionComboId() - other.getOptionComboId();
    }
}
