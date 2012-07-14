package org.hisp.dhis.light.messaging.action;

import java.util.Collection;

import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;

import com.opensymphony.xwork2.Action;

public class FindUserAction
    implements Action
{

    private static final String REDIRECT = "redirect";

    private UserService userService;

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService( UserService userService )
    {
        this.userService = userService;
    }

    private Collection<User> users;

    public Collection<User> getUsers()
    {
        return users;
    }

    public void setUsers( Collection<User> users )
    {
        this.users = users;
    }

    private String keyword;

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword( String keyword )
    {
        this.keyword = keyword;
    }

    private Integer organisationUnitId;

    public Integer getOrganisationUnitId()
    {
        return organisationUnitId;
    }

    public void setOrganisationUnitId( Integer organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }

    private Integer userId;

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId( Integer userId )
    {
        this.userId = userId;
    }

    @Override
    public String execute()
        throws Exception
    {
        if ( keyword != null )
        {
            int index = keyword.indexOf( ' ' );

            if ( index != -1 && index == keyword.lastIndexOf( ' ' ) )
            {
                String[] keys = keyword.split( " " );
                keyword = keys[0] + "  " + keys[1];
            }
        }
        
        users = userService.getUsersByName( keyword );

        if ( users.size() == 1 )
        {
            User user = users.iterator().next();
            userId = user.getId();
            return REDIRECT;
        }

        return SUCCESS;
    }
}
