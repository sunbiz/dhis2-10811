package org.hisp.dhis.user;

import java.util.Collection;

public interface UserGroupService
{
    String ID = UserGroupService.class.getName();

    void addUserGroup( UserGroup userGroup );

    void updateUserGroup( UserGroup userGroup );

    void deleteUserGroup( UserGroup userGroup );

    UserGroup getUserGroup( int userGroupId );

    UserGroup getUserGroup( String uid );

    Collection<UserGroup> getAllUserGroups();

    UserGroup getUserGroupByName( String name );

    Collection<UserGroup> getUserGroupsBetween( int first, int max );

    Collection<UserGroup> getUserGroupsBetweenByName( String name, int first, int max );

    int getUserGroupCount();

    int getUserGroupCountByName( String name );
}
