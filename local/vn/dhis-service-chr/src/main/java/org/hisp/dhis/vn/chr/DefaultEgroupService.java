package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

import org.hisp.dhis.vn.chr.Form;

public class DefaultEgroupService
    implements EgroupService
{

    // -----------------------------------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------------------------------

    private EgroupStore egroupStore;

    // -----------------------------------------------------------------------------------------------
    // Getter && Setter
    // -----------------------------------------------------------------------------------------------

    public void setEgroupStore( EgroupStore egroupStore )
    {
        this.egroupStore = egroupStore;
    }

    // -----------------------------------------------------------------------------------------------
    // Implements
    // -----------------------------------------------------------------------------------------------

    public int addEgroup( Egroup egroup )
    {

        return egroupStore.addEgroup( egroup );
    }

    public void deleteEgroup( int id )
    {

        egroupStore.deleteEgroup( id );

    }

    public Collection<Egroup> getAllEgroups()
    {

        return egroupStore.getAllEgroups();
    }

    public Egroup getEgroup( int id )
    {

        return egroupStore.getEgroup( id );
    }

    public Collection<Egroup> getEgroupsByForm( Form form )
    {

        return egroupStore.getEgroupsByForm( form );
    }

    public void updateEgroup( Egroup egroup )
    {

        egroupStore.updateEgroup( egroup );

    }

}
