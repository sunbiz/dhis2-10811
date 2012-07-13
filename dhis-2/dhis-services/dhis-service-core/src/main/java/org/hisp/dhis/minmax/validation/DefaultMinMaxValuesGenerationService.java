package org.hisp.dhis.minmax.validation;

/*
 * Copyright (c) 2004-2009, University of Oslo
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

import static org.hisp.dhis.system.util.MathUtils.isEqual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.common.AggregatedValue;
import org.hisp.dhis.dataanalysis.DataAnalysisStore;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DeflatedDataValue;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

/**
 * @author Chau Thu Tran
 * @version $Id$
 */
public class DefaultMinMaxValuesGenerationService
    implements MinMaxValuesGenerationService
{
    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    private DataAnalysisStore dataAnalysisStore;

    public void setDataAnalysisStore( DataAnalysisStore dataAnalysisStore )
    {
        this.dataAnalysisStore = dataAnalysisStore;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    public final Collection<MinMaxDataElement> getMinMaxValues( OrganisationUnit organisationUnit,
        Collection<DataElement> dataElements, Double stdDevFactor )
    {
        Collection<MinMaxDataElement> minMaxDataElements = new ArrayList<MinMaxDataElement>();

        for ( DataElement dataElement : dataElements )
        {
            if ( dataElement.getType().equals( DataElement.VALUE_TYPE_INT ) )
            {
                Collection<DataElementCategoryOptionCombo> categoryOptionCombos = dataElement.getCategoryCombo()
                    .getOptionCombos();

                for ( DataElementCategoryOptionCombo categoryOptionCombo : categoryOptionCombos )
                {
                    Double stdDev = dataAnalysisStore.getStandardDeviation( dataElement, categoryOptionCombo,
                        organisationUnit );

                    if ( !isEqual( stdDev, AggregatedValue.ZERO ) ) // No values found or no
                    {
                        Double avg = dataAnalysisStore.getAverage( dataElement, categoryOptionCombo, organisationUnit );

                        double deviation = stdDev * stdDevFactor;
                        Double lowerBound = avg - deviation;
                        Double upperBound = avg + deviation;

                        MinMaxDataElement minMaxDataElement = new MinMaxDataElement();
                        minMaxDataElement.setDataElement( dataElement );
                        minMaxDataElement.setOptionCombo( categoryOptionCombo );
                        minMaxDataElement.setSource( organisationUnit );
                        minMaxDataElement.setMin( lowerBound.intValue() );
                        minMaxDataElement.setMax( upperBound.intValue() );

                        minMaxDataElements.add( minMaxDataElement );
                    }
                }
            }
        }

        return minMaxDataElements;
    }

    public Collection<DeflatedDataValue> findOutliers( OrganisationUnit organisationUnit, Collection<Period> periods,
        Collection<MinMaxDataElement> minMaxDataElements )
    {
        Set<DeflatedDataValue> result = new HashSet<DeflatedDataValue>();

        for ( MinMaxDataElement minMaxs : minMaxDataElements )
        {
            result.addAll( dataAnalysisStore.getDeflatedDataValues( minMaxs.getDataElement(), minMaxs.getOptionCombo(),
                periods, organisationUnit, minMaxs.getMin(), minMaxs.getMax() ) );
        }

        return result;
    }
}
