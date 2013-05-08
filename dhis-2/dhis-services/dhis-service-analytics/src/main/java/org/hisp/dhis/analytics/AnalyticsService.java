package org.hisp.dhis.analytics;

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

import java.util.Map;
import java.util.Set;

import org.hisp.dhis.common.Grid;
import org.hisp.dhis.i18n.I18nFormat;

/**
 * @author Lars Helge Overland
 */
public interface AnalyticsService
{
    final String NAMES_META_KEY = "names";
    
    /**
     * Generates aggregated values for the given query.
     * 
     * @param params the data query parameters.
     * @return aggregated data as a Grid object.
     */
    Grid getAggregatedDataValues( DataQueryParams params );
    
    /**
     * Generates aggregated values for the given query. Creates a mapping between 
     * a dimension key and the aggregated value. The dimension key is a 
     * concatenation of the identifiers of the dimension items separated by "-".
     * 
     * @param params the data query parameters.
     * @return a mapping between a dimension key and the aggregated value.
     */
    Map<String, Double> getAggregatedDataValueMap( DataQueryParams params );

    /**
     * Generates aggregated values for the given query. Creates a mapping between 
     * a dimension key and the aggregated value. The dimension key is a 
     * concatenation of the identifiers of the dimension items separated by "-".
     * 
     * @param params the data query parameters.
     * @return a mapping between a dimension key and the aggregated value.
     */
    Map<String, Double> getAggregatedCompletenessValueMap( DataQueryParams params );
    
    /**
     * Creates a data query parameter object from the given URL.
     * 
     * @param dimensionParams the dimension URL parameters.
     * @param filterParams the filter URL parameters.
     * @param aggregationType the aggregation type.
     * @param measureCriteria the measure criteria.
     * @param format the i18n format.
     * @return a data query parameter object created based on the given URL info.
     */
    DataQueryParams getFromUrl( Set<String> dimensionParams, Set<String> filterParams, 
        AggregationType aggregationType, String measureCriteria, I18nFormat format );
}
