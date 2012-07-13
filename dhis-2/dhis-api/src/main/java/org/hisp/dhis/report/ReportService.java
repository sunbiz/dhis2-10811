package org.hisp.dhis.report;

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

import org.hisp.dhis.i18n.I18nFormat;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;

/**
 * @author Lars Helge Overland
 */
public interface ReportService
{
    final String ID = ReportService.class.getName();

    final String REPORTTYPE_PDF = "pdf";
    final String REPORTTYPE_XLS = "xls";

    void renderReport( OutputStream out, String reportUid, Date reportingPeriod,
                       String organisationUnitUid, String type, I18nFormat format );

    /**
     * Saves a Report.
     *
     * @param report the Report to save.
     * @return the generated identifier.
     */
    int saveReport( Report report );

    /**
     * Retrieves the Report with the given identifier.
     *
     * @param id the identifier of the Report to retrieve.
     * @return the Report.
     */
    Report getReport( int id );

    /**
     * Retrieves the Report with the given uid.
     *
     * @param uid the uid of the Report to retrieve.
     * @return the Report.
     */
    Report getReport( String uid );

    /**
     * Returns the total number of reports. 
     * @return the total number of reports.
     */
    int getReportCount();
    
    /**
     * Returns the number of reports which names are like the given name.
     * Returns the number of reports which names are like the given name.
     */
    int getReportCountByName( String name );

    /**
     * Retrieves the given number of maximum reports starting at the given start
     * index. Reports are sorted on the name property.
     * 
     * @param first the start index.
     * @param max the maximum number of reports.
     * @return a collection of reports.
     */
    Collection<Report> getReportsBetween( int first, int max );

    /**
     * Retrieves the given number of maximum reports starting at the given start
     * index. Reports are sorted on the name property.
     * 
     * @param first the start index.
     * @param max the maximum number of reports.
     * @return a collection of reports.
     */
    Collection<Report> getReportsBetweenByName( String name, int first, int max );

    /**
     * Deletes a Report.
     *
     * @param report the Report to delete.
     */
    void deleteReport( Report report );

    /**
     * Retrieves all Reports.
     *
     * @return a Collection of Reports.
     */
    Collection<Report> getAllReports();

    /**
     * Retrieves the Report with the given name.
     *
     * @param name the name.
     * @return the Report.
     */
    Report getReportByName( String name );

    /**
     * Retrieves all Reports with the given identifiers.
     *
     * @param identifiers the Collection of identifiers.
     * @return a Collection of Reports.
     */
    Collection<Report> getReports( final Collection<Integer> identifiers );
}
