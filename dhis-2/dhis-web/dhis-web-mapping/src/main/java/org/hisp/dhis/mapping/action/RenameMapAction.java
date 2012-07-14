package org.hisp.dhis.mapping.action;

import org.hisp.dhis.mapping.Map;
import org.hisp.dhis.mapping.MappingService;
import org.hisp.dhis.user.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

public class RenameMapAction
    implements Action
{
    @Autowired
    private MappingService mappingService;

    @Autowired
    private CurrentUserService currentUserService;
    
    private String id;

    public void setId( String id )
    {
        this.id = id;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private boolean user;

    public void setUser( boolean user )
    {
        this.user = user;
    }

    public String execute()
    {
        Map map = mappingService.getMap( id );
        
        map.setName( name );
        
        if ( user )
        {
            map.setUser( currentUserService.getCurrentUser() );
        }
        else
        {
            map.setUser( null );
        }
        
        mappingService.updateMap( map );
        
        return SUCCESS;
    }
}
