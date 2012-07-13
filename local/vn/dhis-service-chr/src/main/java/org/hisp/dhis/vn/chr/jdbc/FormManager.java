package org.hisp.dhis.vn.chr.jdbc;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.ArrayList;

import org.hisp.dhis.period.Period;
import org.hisp.dhis.vn.chr.FormReport;
import org.hisp.dhis.vn.chr.Form;

public interface FormManager
{

    /**
     * Create table from Form
     * 
     * @param form needs to create table
     */
    public void createTable( Form form );

    /**
     * Load list objects
     * 
     * @param form needs to create the table
     * @param pageIndex Index of page
     * @return List Objects
     */
    public ArrayList<Object> listObject( Form form, int pageIndex );

    /**
     * Get data in a Object by id of Object
     * 
     * @param form needs to create the table
     * @param id Id of object
     * @return values of a Object
     */
    public ArrayList<String> getObject( Form form, int id );

    /**
     * Add Object by ID
     * 
     * @param form needs to create the table
     * @param data Data of Object
     */
    public void addObject( Form form, String[] data );

    /**
     * Update Object by ID
     * 
     * @param form needs to create the table
     * @param data Data of Object
     */
    public void updateObject( Form form, String[] data );

    /**
     * Delete Object by ID
     * 
     * @param form needs to create the table
     * @param id Id of object
     */
    public void deleteObject( Form form, int id );

    /**
     * Search Object by keyword
     * 
     * @param form needs to create the table
     * @param keyword Keyword
     * 
     * @return Result List
     */
    public ArrayList<Object> searchObject( Form form, String keyword, int pageSize );

    /**
     * Load list relatived objects
     * 
     * @param form needs to create the table
     * @param column Element's name
     * @param pageIndex Index of page
     * 
     * @return List Relatived objects
     */
    public ArrayList<Object> listRelativeObject( Form form, String column, String objectId, int pageSize );

    /**
     * Export data into a report
     * 
     * @param operator needs to create the table
     * @param element Index of page
     * @param period Objects
     * 
     * @return statistics Result
     */
    public int reportDataStatement( String operator, Period period, FormReport formReport );
    
    public String createCode(Form form);
}
