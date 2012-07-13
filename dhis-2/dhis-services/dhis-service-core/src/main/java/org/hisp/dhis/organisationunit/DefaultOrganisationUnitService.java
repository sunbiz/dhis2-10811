package org.hisp.dhis.organisationunit;

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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.hierarchy.HierarchyViolationException;
import org.hisp.dhis.organisationunit.comparator.OrganisationUnitLevelComparator;
import org.hisp.dhis.system.util.AuditLogUtil;
import org.hisp.dhis.system.util.ConversionUtils;
import org.hisp.dhis.system.util.Filter;
import org.hisp.dhis.system.util.FilterUtils;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.version.VersionService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: DefaultOrganisationUnitService.java 5951 2008-10-16 17:41:34Z
 *          larshelg $
 */
@Transactional
public class DefaultOrganisationUnitService
    implements OrganisationUnitService
{
    private static final String LEVEL_PREFIX = "Level ";

    private static final Log log = LogFactory.getLog( DefaultOrganisationUnitService.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitStore organisationUnitStore;

    public void setOrganisationUnitStore( OrganisationUnitStore organisationUnitStore )
    {
        this.organisationUnitStore = organisationUnitStore;
    }

    private OrganisationUnitLevelStore organisationUnitLevelStore;

    public void setOrganisationUnitLevelStore( OrganisationUnitLevelStore organisationUnitLevelStore )
    {
        this.organisationUnitLevelStore = organisationUnitLevelStore;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private VersionService versionService;

    public void setVersionService( VersionService versionService )
    {
        this.versionService = versionService;
    }

    // -------------------------------------------------------------------------
    // OrganisationUnit
    // -------------------------------------------------------------------------

    @Override
    public int addOrganisationUnit( OrganisationUnit organisationUnit )
    {
        int id = organisationUnitStore.save( organisationUnit );

        if ( organisationUnit.getParent() == null && currentUserService.getCurrentUser() != null )
        {
            // we are adding a new root node, add this node to the current user
            // this makes sense in most cases, and makes sure that we don't have "zombie nodes"
            currentUserService.getCurrentUser().getOrganisationUnits().add( organisationUnit );
        }

        log.info( AuditLogUtil.logMessage( currentUserService.getCurrentUsername(), AuditLogUtil.ACTION_ADD,
            OrganisationUnit.class.getSimpleName(), organisationUnit.getName() ) );

        updateVersion();

        return id;
    }

    public void updateOrganisationUnit( OrganisationUnit organisationUnit )
    {
        organisationUnitStore.update( organisationUnit );

        log.info( AuditLogUtil.logMessage( currentUserService.getCurrentUsername(), AuditLogUtil.ACTION_EDIT,
            OrganisationUnit.class.getSimpleName(), organisationUnit.getName() ) );

        updateVersion();
    }

    public void updateOrganisationUnit( OrganisationUnit organisationUnit, boolean updateHierarchy )
    {
        updateOrganisationUnit( organisationUnit );
    }

    public void deleteOrganisationUnit( OrganisationUnit organisationUnit )
        throws HierarchyViolationException
    {
        organisationUnit = getOrganisationUnit( organisationUnit.getId() );
        
        if ( !organisationUnit.getChildren().isEmpty() )
        {
            throw new HierarchyViolationException( "Cannot delete an OrganisationUnit with children" );
        }

        OrganisationUnit parent = organisationUnit.getParent();

        if ( parent != null )
        {
            parent.getChildren().remove( organisationUnit );

            organisationUnitStore.update( parent );
        }

        log.info( AuditLogUtil.logMessage( currentUserService.getCurrentUsername(), AuditLogUtil.ACTION_DELETE,
            OrganisationUnit.class.getSimpleName(), organisationUnit.getName() ) );

        organisationUnitStore.delete( organisationUnit );

        updateVersion();
    }

    public OrganisationUnit getOrganisationUnit( int id )
    {
        return organisationUnitStore.get( id );
    }

    public Collection<OrganisationUnit> getAllOrganisationUnits()
    {
        return organisationUnitStore.getAll();
    }

    public void searchOrganisationUnitByName( List<OrganisationUnit> orgUnits, String key )
    {
        Iterator<OrganisationUnit> iterator = orgUnits.iterator();

        while ( iterator.hasNext() )
        {
            if ( !iterator.next().getName().toLowerCase().contains( key.toLowerCase() ) )
            {
                iterator.remove();
            }
        }
    }

    public Collection<OrganisationUnit> getOrganisationUnits( final Collection<Integer> identifiers )
    {
        Collection<OrganisationUnit> objects = getAllOrganisationUnits();

        return identifiers == null ? objects : FilterUtils.filter( objects, new Filter<OrganisationUnit>()
        {
            public boolean retain( OrganisationUnit object )
            {
                return identifiers.contains( object.getId() );
            }
        } );
    }

    public List<OrganisationUnit> getOrganisationUnitsByUid( Collection<String> uids )
    {
        return organisationUnitStore.getByUid( uids );
    }

    public OrganisationUnit getOrganisationUnit( String uid )
    {
        return organisationUnitStore.getByUid( uid );
    }

    public OrganisationUnit getOrganisationUnitByName( String name )
    {
        return organisationUnitStore.getByName( name );
    }

    public OrganisationUnit getOrganisationUnitByCode( String code )
    {
        return organisationUnitStore.getByCode( code );
    }

    public OrganisationUnit getOrganisationUnitByNameIgnoreCase( String name )
    {
        return organisationUnitStore.getOrganisationUnitByNameIgnoreCase( name );
    }

    public Collection<OrganisationUnit> getRootOrganisationUnits()
    {
        return organisationUnitStore.getRootOrganisationUnits();
    }

    public int getLevelOfOrganisationUnit( int id )
    {
        return getOrganisationUnit( id ).getOrganisationUnitLevel();
    }

    public Collection<OrganisationUnit> getLeafOrganisationUnits( int id )
    {
        Collection<OrganisationUnit> units = getOrganisationUnitWithChildren( id );

        return FilterUtils.filter( units, new Filter<OrganisationUnit>()
        {
            public boolean retain( OrganisationUnit object )
            {
                return object != null && object.getChildren().isEmpty();
            }
        } );
    }

    public Collection<OrganisationUnit> getOrganisationUnitWithChildren( int id )
    {
        OrganisationUnit organisationUnit = getOrganisationUnit( id );

        if ( organisationUnit == null )
        {
            return Collections.emptySet();
        }

        List<OrganisationUnit> result = new ArrayList<OrganisationUnit>();

        int rootLevel = 1;

        organisationUnit.setLevel( rootLevel );

        result.add( organisationUnit );

        addOrganisationUnitChildren( organisationUnit, result, rootLevel );

        return result;
    }

    /**
     * Support method for getOrganisationUnitWithChildren(). Adds all
     * OrganisationUnit children to a result collection.
     */
    private void addOrganisationUnitChildren( OrganisationUnit parent, List<OrganisationUnit> result, int level )
    {
        if ( parent.getChildren() != null && parent.getChildren().size() > 0 )
        {
            level++;
        }

        List<OrganisationUnit> childList = parent.getSortedChildren();

        for ( OrganisationUnit child : childList )
        {
            child.setLevel( level );

            result.add( child );

            addOrganisationUnitChildren( child, result, level );
        }

        level--;
    }

    public List<OrganisationUnit> getOrganisationUnitBranch( int id )
    {
        OrganisationUnit organisationUnit = getOrganisationUnit( id );

        if ( organisationUnit == null )
        {
            return Collections.emptyList();
        }

        ArrayList<OrganisationUnit> result = new ArrayList<OrganisationUnit>();

        result.add( organisationUnit );

        OrganisationUnit parent = organisationUnit.getParent();

        while ( parent != null )
        {
            result.add( parent );

            parent = parent.getParent();
        }

        Collections.reverse( result ); // From root to target

        return result;
    }

    public Collection<OrganisationUnit> getOrganisationUnitsAtLevel( int level )
    {
        if ( level < 1 )
        {
            throw new IllegalArgumentException( "Level must be greater than zero" );
        }

        if ( level == 1 )
        {
            return getRootOrganisationUnits();
        }

        Set<OrganisationUnit> result = new HashSet<OrganisationUnit>();

        for ( OrganisationUnit root : organisationUnitStore.getRootOrganisationUnits() )
        {
            addOrganisationUnitChildrenAtLevel( root, 2, level, result );
        }

        return result;
    }

    public Collection<OrganisationUnit> getOrganisationUnitsAtLevel( int level, OrganisationUnit parent )
    {
        if ( level < 1 )
        {
            throw new IllegalArgumentException( "Level must be greater than zero" );
        }

        int parentLevel = parent.getOrganisationUnitLevel();

        if ( level < parentLevel )
        {
            throw new IllegalArgumentException(
                "Level must be greater than or equal to level of parent OrganisationUnit" );
        }

        Set<OrganisationUnit> result = new HashSet<OrganisationUnit>();

        if ( level == parentLevel )
        {
            result.add( parent );
        }
        else
        {
            addOrganisationUnitChildrenAtLevel( parent, parentLevel + 1, level, result );
        }

        return result;
    }

    /**
     * Support method for getOrganisationUnitsAtLevel(). Adds all children at a
     * given targetLevel to a result collection. The parent's children are at
     * the current level.
     */
    private void addOrganisationUnitChildrenAtLevel( OrganisationUnit parent, int currentLevel, int targetLevel,
        Set<OrganisationUnit> result )
    {
        if ( currentLevel == targetLevel )
        {
            result.addAll( parent.getChildren() );
        }
        else
        {
            for ( OrganisationUnit child : parent.getChildren() )
            {
                addOrganisationUnitChildrenAtLevel( child, currentLevel + 1, targetLevel, result );
            }
        }
    }

    public int getNumberOfOrganisationalLevels()
    {
        int maxDepth = 0;
        int depth;

        for ( OrganisationUnit root : getRootOrganisationUnits() )
        {
            depth = getDepth( root, 1 );

            if ( depth > maxDepth )
            {
                maxDepth = depth;
            }
        }

        return maxDepth;
    }

    /**
     * Support method for getNumberOfOrganisationalLevels(). Finds the depth of
     * a given subtree. The parent is at the current level.
     */
    private int getDepth( OrganisationUnit parent, int currentLevel )
    {
        int maxDepth = currentLevel;
        int depth;

        for ( OrganisationUnit child : parent.getChildren() )
        {
            depth = getDepth( child, currentLevel + 1 );

            if ( depth > maxDepth )
            {
                maxDepth = depth;
            }
        }

        return maxDepth;
    }

    public Collection<OrganisationUnit> getOrganisationUnitsWithoutGroups()
    {
        return organisationUnitStore.getOrganisationUnitsWithoutGroups();
    }

    public Collection<OrganisationUnit> getOrganisationUnitsByNameAndGroups( String name,
        Collection<OrganisationUnitGroup> groups, boolean limit )
    {
        return organisationUnitStore.getOrganisationUnitsByNameAndGroups( name, groups, limit );
    }

    @SuppressWarnings( "unchecked" )
    public Collection<OrganisationUnit> getOrganisationUnitsByNameAndGroups( String name,
        Collection<OrganisationUnitGroup> groups, OrganisationUnit parent, boolean limit )
    {
        // Can only limit in query if parent is not set and we get all units

        boolean _limit = limit && parent == null;

        final Collection<OrganisationUnit> result = organisationUnitStore.getOrganisationUnitsByNameAndGroups( name,
            groups, _limit );

        if ( parent == null )
        {
            return result;
        }

        final Collection<OrganisationUnit> subTree = getOrganisationUnitWithChildren( parent.getId() );

        List<OrganisationUnit> intersection = new ArrayList<OrganisationUnit>( CollectionUtils.intersection( subTree,
            result ) );

        return limit && intersection != null && intersection.size() > MAX_LIMIT ? intersection.subList( 0, MAX_LIMIT )
            : intersection;
    }

    public OrganisationUnitDataSetAssociationSet getOrganisationUnitDataSetAssociationSet()
    {
        Map<Integer, Set<Integer>> associationSet = organisationUnitStore.getOrganisationUnitDataSetAssocationMap();

        filterUserDataSets( associationSet );
        filterChildOrganisationUnits( associationSet );

        OrganisationUnitDataSetAssociationSet set = new OrganisationUnitDataSetAssociationSet();

        for ( Map.Entry<Integer, Set<Integer>> entry : associationSet.entrySet() )
        {
            int index = set.getDataSetAssociationSets().indexOf( entry.getValue() );

            if ( index == -1 ) // Association set does not exist, add new
            {
                index = set.getDataSetAssociationSets().size();
                set.getDataSetAssociationSets().add( entry.getValue() );
            }

            set.getOrganisationUnitAssociationSetMap().put( entry.getKey(), index );
            set.getDistinctDataSets().addAll( entry.getValue() );
        }

        return set;
    }

    private void filterUserDataSets( Map<Integer, Set<Integer>> associationMap )
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser != null && !currentUser.getUserCredentials().isSuper() )
        {
            Collection<Integer> userDataSets = ConversionUtils.getIdentifiers( DataSet.class, currentUser
                .getUserCredentials().getAllDataSets() );

            for ( Set<Integer> dataSets : associationMap.values() )
            {
                dataSets.retainAll( userDataSets );
            }
        }
    }

    private void filterChildOrganisationUnits( Map<Integer, Set<Integer>> associatonMap )
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser != null )
        {
            Collection<Integer> parentIds = ConversionUtils.getIdentifiers( OrganisationUnit.class,
                currentUser.getOrganisationUnits() );

            Collection<Integer> children = getOrganisationUnitHierarchy().getChildren( parentIds );

            associatonMap.keySet().retainAll( children );
        }
    }

    public void filterOrganisationUnitsWithoutData( Collection<OrganisationUnit> organisationUnits )
    {
        final Set<Integer> unitsWithoutData = organisationUnitStore.getOrganisationUnitIdsWithoutData();

        FilterUtils.filter( organisationUnits, new Filter<OrganisationUnit>()
        {
            public boolean retain( OrganisationUnit unit )
            {
                return unit != null && (!unitsWithoutData.contains( unit.getId() ) || unit.hasChild());
            }
        } );
    }

    public Collection<OrganisationUnit> getOrganisationUnitsBetween( int first, int max )
    {
        return organisationUnitStore.getBetween( first, max );
    }

    public Collection<OrganisationUnit> getOrganisationUnitsBetweenByName( String name, int first, int max )
    {
        return organisationUnitStore.getBetweenByName( name, first, max );
    }
    
    // -------------------------------------------------------------------------
    // OrganisationUnitHierarchy
    // -------------------------------------------------------------------------

    public OrganisationUnitHierarchy getOrganisationUnitHierarchy()
    {
        return organisationUnitStore.getOrganisationUnitHierarchy();
    }

    public void updateOrganisationUnitParent( int organisationUnitId, int parentId )
    {
        organisationUnitStore.updateOrganisationUnitParent( organisationUnitId, parentId );
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitLevel
    // -------------------------------------------------------------------------

    public int addOrganisationUnitLevel( OrganisationUnitLevel organisationUnitLevel )
    {
        return organisationUnitLevelStore.save( organisationUnitLevel );
    }

    public void updateOrganisationUnitLevel( OrganisationUnitLevel organisationUnitLevel )
    {
        organisationUnitLevelStore.update( organisationUnitLevel );
    }

    public void addOrUpdateOrganisationUnitLevel( OrganisationUnitLevel level )
    {
        OrganisationUnitLevel existing = getOrganisationUnitLevelByLevel( level.getLevel() );

        if ( existing == null )
        {
            addOrganisationUnitLevel( level );
        }
        else
        {
            existing.setName( level.getName() );

            updateOrganisationUnitLevel( existing );
        }
    }

    public void pruneOrganisationUnitLevels( Set<Integer> currentLevels )
    {
        for ( OrganisationUnitLevel level : getOrganisationUnitLevels() )
        {
            if ( !currentLevels.contains( level.getLevel() ) )
            {
                deleteOrganisationUnitLevel( level );
            }
        }
    }

    public OrganisationUnitLevel getOrganisationUnitLevel( int id )
    {
        return organisationUnitLevelStore.get( id );
    }

    public OrganisationUnitLevel getOrganisationUnitLevel( String uid )
    {
        return organisationUnitLevelStore.getByUid( uid );
    }

    public Collection<OrganisationUnitLevel> getOrganisationUnitLevels( final Collection<Integer> identifiers )
    {
        Collection<OrganisationUnitLevel> objects = getOrganisationUnitLevels();

        return identifiers == null ? objects : FilterUtils.filter( objects, new Filter<OrganisationUnitLevel>()
        {
            public boolean retain( OrganisationUnitLevel object )
            {
                return identifiers.contains( object.getId() );
            }
        } );
    }

    public void deleteOrganisationUnitLevel( OrganisationUnitLevel organisationUnitLevel )
    {
        organisationUnitLevelStore.delete( organisationUnitLevel );
    }

    public void deleteOrganisationUnitLevels()
    {
        organisationUnitLevelStore.deleteAll();
    }

    public List<OrganisationUnitLevel> getOrganisationUnitLevels()
    {
        List<OrganisationUnitLevel> organisationUnitLevels = new ArrayList<OrganisationUnitLevel>(
            organisationUnitLevelStore.getAll() );

        Collections.sort( organisationUnitLevels, new OrganisationUnitLevelComparator() );

        return organisationUnitLevels;
    }

    public OrganisationUnitLevel getOrganisationUnitLevelByLevel( int level )
    {
        return organisationUnitLevelStore.getByLevel( level );
    }

    public OrganisationUnitLevel getOrganisationUnitLevelByName( String name )
    {
        return organisationUnitLevelStore.getByName( name );
    }

    public List<OrganisationUnitLevel> getFilledOrganisationUnitLevels()
    {
        Map<Integer, OrganisationUnitLevel> levelMap = getOrganisationUnitLevelMap();

        List<OrganisationUnitLevel> levels = new ArrayList<OrganisationUnitLevel>();

        for ( int i = 0; i < getNumberOfOrganisationalLevels(); i++ )
        {
            int level = i + 1;

            levels.add( levelMap.get( level ) != null ? levelMap.get( level ) : new OrganisationUnitLevel( level,
                LEVEL_PREFIX + level ) );
        }

        return levels;
    }

    public Map<Integer, OrganisationUnitLevel> getOrganisationUnitLevelMap()
    {
        Map<Integer, OrganisationUnitLevel> levelMap = new HashMap<Integer, OrganisationUnitLevel>();

        Collection<OrganisationUnitLevel> levels = getOrganisationUnitLevels();

        for ( OrganisationUnitLevel level : levels )
        {
            levelMap.put( level.getLevel(), level );
        }

        return levelMap;
    }

    @Override
    public int getNumberOfOrganisationUnits()
    {
        return organisationUnitStore.getCount();
    }

    @Override
    public int getMaxOfOrganisationUnitLevels()
    {
        return organisationUnitLevelStore.getMaxLevels();
    }

    // -------------------------------------------------------------------------
    // Version
    // -------------------------------------------------------------------------

    @Override
    public void updateVersion()
    {
        versionService.updateVersion( VersionService.ORGANISATIONUNIT_VERSION, UUID.randomUUID().toString() );
    }
}
