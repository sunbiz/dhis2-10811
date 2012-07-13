package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

public interface EgroupService
{

    /**
     * Adds a Egroup.
     * 
     * @param egroup the Egroup to add.
     * @return a generated unique id of the added Egroup.
     */
    public int addEgroup( Egroup egroup );

    /**
     * Updates a Egroup.
     * 
     * @param egroup the Egroup to update.
     */
    void updateEgroup( Egroup egroup );

    /**
     * Deletes a Egroup.
     * 
     * @param id the id of Egroup to delete.
     */
    void deleteEgroup( int id );

    /**
     * Returns all Egroup.
     * 
     * @return a collection of all Egroup, or an empty collection if there are
     *         no Egroup.
     */
    public Collection<Egroup> getAllEgroups();

    /**
     * Returns a Egroup.
     * 
     * @param id the id of the Egroup to return.
     * @return the Egroup with the given id, or null if no match.
     */
    public Egroup getEgroup( int id );

    /**
     * Returns all egroups of a form
     * 
     * @return all DataEgroups of a form.
     */
    Collection<Egroup> getEgroupsByForm( Form form );
}
