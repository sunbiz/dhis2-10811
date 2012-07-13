package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

public interface FormReportService
{

    /**
     * Adds a FormReport.
     * 
     * @param formReport the FormReport to add.
     * @return a generated unique id of the added FormReport.
     */
    public int addFormReport( FormReport formReport );

    /**
     * Updates a FormReport.
     * 
     * @param formReport the FormReport to update.
     */
    void updateFormReport( FormReport formReport );

    /**
     * Deletes a FormReport.
     * 
     * @param id the id of FormReport to delete.
     */
    void deleteFormReport( int id );

    /**
     * Returns all FormReport.
     * 
     * @return a collection of all FormReport, or an empty collection if there
     *         are no FormReport.
     */
    public Collection<FormReport> getAllFormReports();

    /**
     * Returns all FormReport.
     * 
     * @return a collection of all FormReport, or an empty collection if there
     *         are no FormReport.
     */
    public Collection<FormReport> getFormReports( Form form );

    /**
     * Returns a FormReport.
     * 
     * @param id the id of the FormReport to return.
     * @return the FormReport with the given id, or null if no match.
     */
    public FormReport getFormReport( int id );

}
