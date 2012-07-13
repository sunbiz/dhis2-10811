package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

public interface ElementStore
{
    /**
     * Adds a Element.
     * 
     * @param element the Element to add.
     * @return a generated unique id of the added Element.
     */
    public int addElement( Element element );

    /**
     * Updates a Element.
     * 
     * @param element the Element to update.
     */
    void updateElement( Element element );

    /**
     * Deletes a Element.
     * 
     * @param id the id of Element to delete.
     */
    void deleteElement( int id );

    /**
     * Returns a Element.
     * 
     * @param name the name of the Element to return.
     * @return the Element with the given name, or null if no match.
     */
    public Element getElement( String name );

    /**
     * Returns all Element.
     * 
     * @return a collection of all Element, or an empty collection if there are
     *         no Element.
     */
    public Collection<Element> getAllElements();

    /**
     * Returns a Element.
     * 
     * @param id the id of the Element to return.
     * @return the Element with the given id, or null if no match.
     */
    public Element getElement( int id );

    /**
     * Returns all elements of a form
     * 
     * @return all Elements of a form.
     */
    Collection<Element> getElementsByForm( Form form );

    /**
     * Returns all elements of a Egroup
     * 
     * @return all Elements of a Egroup.
     */
    Collection<Element> getElementsByEgroup( Egroup egroup );

    /**
     * Returns all elements which belong no Egroup
     * 
     * @return all Elements
     */
    Collection<Element> getElementsByNoEgroup();

    /**
     * Returns all Elements with form link greater than 0 and formId inputed
     * 
     * @param formId Form ID
     * @return Elements' list.
     */
    public Collection<Element> getElementsByFormLink( Form form );
}
