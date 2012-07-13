package org.hisp.dhis.organisationunit;

/*
 * Copyright (c) 2004-2011, University of Oslo
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

import org.hisp.dhis.hierarchy.HierarchyViolationException;

import java.util.*;

/**
 * Defines methods for working with OrganisationUnits.
 *
 * @author Torgeir Lorange Ostby
 * @version $Id: OrganisationUnitService.java 5951 2008-10-16 17:41:34Z larshelg $
 */
public interface OrganisationUnitService
{
    String ID = OrganisationUnitService.class.getName();

    final int MAX_LIMIT = 500;

    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    /**
     * Adds an OrganisationUnit to the hierarchy.
     *
     * @param organisationUnit the OrganisationUnit to add.
     * @return a generated unique id of the added OrganisationUnit.
     */
    public int addOrganisationUnit( OrganisationUnit organisationUnit );

    /**
     * Updates an OrganisationUnit.
     *
     * @param organisationUnit the OrganisationUnit to update.
     */
    void updateOrganisationUnit( OrganisationUnit organisationUnit );

    /**
     * Updates an OrganisationUnit.
     *
     * @param organisationUnit the organisationUnit to update.
     * @param updateHierarchy  indicate whether the OrganisationUnit hierarchy
     *                         has been updated.
     */
    void updateOrganisationUnit( OrganisationUnit organisationUnit, boolean updateHierarchy );

    /**
     * Deletes an OrganisationUnit. OrganisationUnits with children cannot be
     * deleted.
     *
     * @param organisationUnit the OrganisationUnit to delete.
     * @throws HierarchyViolationException if the OrganisationUnit has children.
     */
    void deleteOrganisationUnit( OrganisationUnit organisationUnit )
        throws HierarchyViolationException;

    /**
     * Returns an OrganisationUnit.
     *
     * @param id the id of the OrganisationUnit to return.
     * @return the OrganisationUnit with the given id, or null if no match.
     */
    OrganisationUnit getOrganisationUnit( int id );

    /**
     * Returns the OrganisationUnit with the given UID.
     *
     * @param uid the UID of the OrganisationUnit to return.
     * @return the OrganisationUnit with the given UID, or null if no match.
     */
    OrganisationUnit getOrganisationUnit( String uid );

    /**
     * Returns the OrganisationUnit with the given code.
     *
     * @param code the code of the OrganisationUnit to return.
     * @return the OrganisationUnit with the given code, or null if no match.
     */
    OrganisationUnit getOrganisationUnitByCode( String code );

    /**
     * Returns all OrganisationUnits.
     *
     * @return a collection of all OrganisationUnits, or an empty collection if
     *         there are no OrganisationUnits.
     */
    Collection<OrganisationUnit> getAllOrganisationUnits();

    /**
     * Returns all OrganisationUnits with corresponding name key based on the given list.
     *
     * @param orgUnits the collection of organization unit objects.
     * @param key      the name key.
     * @return a collection of OrganisationUnits.
     */
    void searchOrganisationUnitByName( List<OrganisationUnit> orgUnits, String key );

    /**
     * Returns all OrganisationUnits with corresponding identifiers.
     *
     * @param identifiers the collection of identifiers.
     * @return a collection of OrganisationUnits.
     */
    Collection<OrganisationUnit> getOrganisationUnits( Collection<Integer> identifiers );

    /**
     * Returns all OrganisationUnits with corresponding identifiers.
     *
     * @param uids the collection of uids.
     * @return a collection of OrganisationUnits.
     */
    List<OrganisationUnit> getOrganisationUnitsByUid( Collection<String> uids );

    /**
     * Returns an OrganisationUnit with a given name.
     *
     * @param name the name of the OrganisationUnit to return.
     * @return the OrganisationUnit with the given name, or null if not match.
     */
    OrganisationUnit getOrganisationUnitByName( String name );

    /**
     * Returns an OrganisationUnit with a given name. Case is ignored.
     *
     * @param name the name of the OrganisationUnit to return.
     * @return the OrganisationUnit with the given name, or null if not match.
     */
    OrganisationUnit getOrganisationUnitByNameIgnoreCase( String name );

    /**
     * Returns all root OrganisationUnits. A root OrganisationUnit is an
     * OrganisationUnit with no parent/the parent set to null.
     *
     * @return a collection containing all root OrganisationUnits, or an empty
     *         collection if there are no OrganisationUnits.
     */
    Collection<OrganisationUnit> getRootOrganisationUnits();

    /**
     * Returns the level of the organisation unit with the given identifier.
     * 
     * @return the level of the organisation unit with the given identifier.
     */
    int getLevelOfOrganisationUnit( int id );
    
    /**
     * Returns all OrganisationUnits which are part of the subtree of the
     * OrganisationUnit with the given identifer and have no children.
     *
     * @param id the identifier of the parent OrganisationUnit.
     * @return a collection of OrganisationUnits.
     */
    Collection<OrganisationUnit> getLeafOrganisationUnits( int id );

    /**
     * Returns an OrganisationUnit and all its children.
     *
     * @param id the id of the parent OrganisationUnit in the subtree.
     * @return a collection containing the OrganisationUnit with the given id
     *         and all its children, or an empty collection if no
     *         OrganisationUnits match.
     */
    Collection<OrganisationUnit> getOrganisationUnitWithChildren( int id );

    /**
     * Returns the branch of OrganisationUnits from a root to a given
     * OrganisationUnit. Both root and target OrganisationUnits are included in
     * the returned collection.
     *
     * @param id the id of the OrganisationUnit to trace upwards from.
     * @return the list of OrganisationUnits from a root to the given
     *         OrganisationUnit, or an empty list if the given OrganisationUnit
     *         doesn't exist.
     */
    List<OrganisationUnit> getOrganisationUnitBranch( int id );

    /**
     * Returns all OrganisationUnits at a given hierarchical level. The root
     * OrganisationUnits are at level 1.
     *
     * @param level the hierarchical level.
     * @return a collection of all OrganisationUnits at a given hierarchical
     *         level, or an empty collection if the level is empty.
     * @throws IllegalArgumentException if the level is zero or negative.
     */
    Collection<OrganisationUnit> getOrganisationUnitsAtLevel( int level );

    /**
     * Returns all OrganisationUnits which are children of the given unit and are
     * at the given hierarchical level. The root OrganisationUnits are at level 1.
     *
     * @param level  the hierarchical level.
     * @param parent the parent unit.
     * @return all OrganisationUnits which are children of the given unit and are
     *         at the given hierarchical level.
     * @throws IllegalArgumentException if the level is illegal.
     */
    Collection<OrganisationUnit> getOrganisationUnitsAtLevel( int level, OrganisationUnit parent );

    /**
     * Returns the number of levels in the OrganisationUnit hierarchy.
     *
     * @return the number of hierarchical levels.
     */
    int getNumberOfOrganisationalLevels();

    /**
     * Returns all OrganisationUnits which are not a member of any OrganisationUnitGroups.
     *
     * @return all OrganisationUnits which are not a member of any OrganisationUnitGroups.
     */
    Collection<OrganisationUnit> getOrganisationUnitsWithoutGroups();

    /**
     * Get the units which name are like the given name and are members of the
     * given groups. If name or groups are null or empty they are ignored in the
     * search. If name and groups are null an empty collection is returned.
     *
     * @param name   the name.
     * @param groups the organisation unit groups.
     * @return a collection of organisation units.
     */
    Collection<OrganisationUnit> getOrganisationUnitsByNameAndGroups( String name, Collection<OrganisationUnitGroup> groups, boolean limit );

    /**
     * Get the units which name are like the given name, are members of the
     * given groups and children of the given parent organisation unit. If name
     * or groups are null or empty they are ignored in the sarch. If name and
     * groups are null an empty collection is returned.
     *
     * @param name   the name.
     * @param groups the organisation unit groups.
     * @return a collection of organisation units.
     */
    Collection<OrganisationUnit> getOrganisationUnitsByNameAndGroups( String name, Collection<OrganisationUnitGroup> groups, OrganisationUnit parent, boolean limit );

    OrganisationUnitDataSetAssociationSet getOrganisationUnitDataSetAssociationSet();

    void filterOrganisationUnitsWithoutData( Collection<OrganisationUnit> organisationUnits );

    Collection<OrganisationUnit> getOrganisationUnitsBetween( int first, int max );
    
    Collection<OrganisationUnit> getOrganisationUnitsBetweenByName( String name, int first, int max );
    
    // -------------------------------------------------------------------------
    // OrganisationUnitHierarchy
    // -------------------------------------------------------------------------

    /**
     * Get the OrganisationUnit hierarchy.
     *
     * @return a Collection with OrganisationUnitRelationship entries.
     */
    OrganisationUnitHierarchy getOrganisationUnitHierarchy();

    /**
     * Updates the parent id of the organisation unit with the given id.
     *
     * @param organisationUnitId the child organisation unit identifier.
     * @param parentId           the parent organisation unit identifier.
     */
    void updateOrganisationUnitParent( int organisationUnitId, int parentId );

    // -------------------------------------------------------------------------
    // OrganisationUnitLevel
    // -------------------------------------------------------------------------

    int addOrganisationUnitLevel( OrganisationUnitLevel level );

    void updateOrganisationUnitLevel( OrganisationUnitLevel level );

    void addOrUpdateOrganisationUnitLevel( OrganisationUnitLevel level );

    void pruneOrganisationUnitLevels( Set<Integer> currentLevels );

    OrganisationUnitLevel getOrganisationUnitLevel( int id );

    OrganisationUnitLevel getOrganisationUnitLevel( String uid );

    Collection<OrganisationUnitLevel> getOrganisationUnitLevels( Collection<Integer> identifiers );

    void deleteOrganisationUnitLevel( OrganisationUnitLevel level );

    void deleteOrganisationUnitLevels();

    List<OrganisationUnitLevel> getOrganisationUnitLevels();

    OrganisationUnitLevel getOrganisationUnitLevelByLevel( int level );

    OrganisationUnitLevel getOrganisationUnitLevelByName( String name );

    List<OrganisationUnitLevel> getFilledOrganisationUnitLevels();

    Map<Integer, OrganisationUnitLevel> getOrganisationUnitLevelMap();

    int getNumberOfOrganisationUnits();

    int getMaxOfOrganisationUnitLevels();

    // -------------------------------------------------------------------------
    // Version
    // -------------------------------------------------------------------------

    void updateVersion();
}
