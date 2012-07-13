package org.hisp.dhis.reporttable;

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

import org.hisp.dhis.common.Grid;
import org.hisp.dhis.i18n.I18nFormat;

import java.util.Collection;
import java.util.Date;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public interface ReportTableService
{
    String ID = ReportTableService.class.getName();

    final String MODE_REPORT = "report";
    final String MODE_REPORT_TABLE = "table";

    /**
     * Saves a ReportTable.
     *
     * @param reportTable the ReportTable to save.
     * @return the generated identifier.
     */
    int saveReportTable( ReportTable reportTable );

    /**
     * Updates a ReportTable.
     *
     * @param reportTable the ReportTable to update.
     */
    void updateReportTable( ReportTable reportTable );

    /**
     * Deletes a ReportTable.
     *
     * @param reportTable the ReportTable to delete.
     */
    void deleteReportTable( ReportTable reportTable );

    /**
     * Retrieves the ReportTable with the given identifier.
     *
     * @param id the identifier of the ReportTable to retrieve.
     * @return the ReportTable.
     */
    ReportTable getReportTable( int id );

    /**
     * Retrieves the ReportTable with the given uid.
     *
     * @param uid the uid of the ReportTable to retrieve.
     * @return the ReportTable.
     */
    ReportTable getReportTable( String uid );

    /**
     * Retrieves a Collection of all ReportTables.
     *
     * @return a Collection of ReportTables.
     */
    Collection<ReportTable> getAllReportTables();

    /**
     * Retrieves ReportTables with the given identifiers.
     *
     * @param reportTables the identfiers of the ReportTables to retrieve.
     * @return a Collection of ReportTables.
     */
    Collection<ReportTable> getReportTables( Collection<Integer> reportTables );

    /**
     * Retrieves the ReportTable with the given name.
     *
     * @param name the name of the ReportTable.
     * @return the ReportTable.
     */
    ReportTable getReportTableByName( String name );

    /**
     * Instantiates and populates a Grid populated with data from the ReportTable
     * with the given identifier.
     *
     * @param id the ReportTable identifier.
     * @param format the I18nFormat.
     * @param reportingPeriod the reporting date.
     * @param organisationUnitId the organisation unit identifier.
     * @return a Grid.
     */
    Grid getReportTableGrid( int id, I18nFormat format, Date reportingPeriod, Integer organisationUnitId );

    /**
     * Instantiates and populates a Grid populated with data from the ReportTable
     * with the given identifier.
     *
     * @param uid the ReportTable unique identifier.
     * @param format the I18nFormat.
     * @param reportingPeriod the reporting date.
     * @param organisationUnitUid the organisation unit uid.
     * @return a Grid.
     */
    Grid getReportTableGrid( String uid, I18nFormat format, Date reportingPeriod, String organisationUnitUid );

    /**
     * Instantiates and populates a Grid populated with data from the given 
     * ReportTable.
     * 
     * @param reportTable the ReportTable.
     * @param format the I18nFormat.
     * @param reportingPeriod the reporting date.
     * @param organisationUnitUid the organisation unit uid.
     * @param minimal indicates whether to include visible columns only in the Grid.
     * @return a Grid.
     */
    Grid getReportTableGrid( ReportTable reportTable, I18nFormat format, Date reportingPeriod, String organisationUnitUid, boolean minimal );
    
    /**
     * If report table mode, this method will return the report table with the
     * given identifier. If report mode, this method will return the report
     * tables associated with the report.
     *
     * @param uid the uid.
     * @param mode the mode.
     */
    ReportTable getReportTable( String uid, String mode );

    Collection<ReportTable> getReportTablesBetween( int first, int max );

    Collection<ReportTable> getReportTablesBetweenByName( String name, int first, int max );

    int getReportTableCount();

    int getReportTableCountByName( String name );
}
