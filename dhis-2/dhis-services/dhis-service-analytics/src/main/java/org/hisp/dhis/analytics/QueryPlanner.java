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

import java.util.List;

public interface QueryPlanner
{
    /**
     * Validates the given query. Throws an IllegalQueryException if the query
     * is not valid with a descriptive message. Returns normally if the query is
     * valid.
     * 
     * @param params the query.
     * @throws IllegalQueryException if the query is invalid.
     */
    void validate( DataQueryParams params )
        throws IllegalQueryException;
    
    /**
     * Creates a list of DataQueryParams. It is mandatory to group the queries by
     * the following criteria: 1) partition / year 2) period type 3) organisation 
     * unit level. If the number of queries produced by this grouping is equal or
     * larger than the number of optimal queries, those queries are returned. Next
     * splits on organisation unit dimension, and returns if optimal queries are
     * satisfied. Next splits on data element dimension, and returns if optimal
     * queries are satisfied. 
     * 
     * Does not attempt to split on period or organisation unit group set dimensions, 
     * as splitting on columns with low cardinality typically decreases performance.
     * 
     * @param params the data query params.
     * @param optimalQueries the number of optimal queries for the planner to return.
     * @param tableName the base table name.
     * @return list of data query params.
     */
    List<DataQueryParams> planQuery( DataQueryParams params, int optimalQueries, String tableName )
        throws IllegalQueryException;
}
