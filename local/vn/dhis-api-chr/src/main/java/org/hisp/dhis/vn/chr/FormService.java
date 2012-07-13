package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

public interface FormService
{

    String ID = FormService.class.getName();

    /**
     * Adds a Form.
     * 
     * @param form the Form to add.
     * @return a generated unique id of the added Form.
     */
    public int addForm( Form form );

    /**
     * Updates a Form.
     * 
     * @param form the Form to update.
     */
    void updateForm( Form form );

    /**
     * Deletes a Form.
     * 
     * @param id the id of Form to delete.
     */
    void deleteForm( int id );

    /**
     * Returns all Form.
     * 
     * @return a collection of all Form, or an empty collection if there are no
     *         Form.
     */
    public Collection<Form> getAllForms();

    /**
     * Returns a Form.
     * 
     * @param id the id of the Form to return.
     * @return the Form with the given id, or null if no match.
     */
    public Form getForm( int id );

    /**
     * Returns a Form list.
     * 
     * @param name the name of the Form to return.
     * @return the Form list include the given name, or null if no match.
     */
    public Collection<Form> getFormsByName( String name );

    /**
     * Returns all visible forms.
     * 
     * @return all visible forms.
     */
    public Collection<Form> getVisibleForms( boolean visible );

    /**
     * Returns all created forms.
     * 
     * @return all created forms.
     */
    public Collection<Form> getCreatedForms();

    /**
     * Returns a Form.
     * 
     * @param name the name of the Form to return.
     * @return the Form include the given name, or null if no match.
     */
    public Form getFormByName( String name );
}
