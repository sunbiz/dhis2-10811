package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

public class DefaultElementService
    implements ElementService
{

    // -----------------------------------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------------------------------

    private ElementStore elementStore;

    // -----------------------------------------------------------------------------------------------
    // Getter && Setter
    // -----------------------------------------------------------------------------------------------

    public void setElementStore( ElementStore elementStore )
    {
        this.elementStore = elementStore;
    }

    // -----------------------------------------------------------------------------------------------
    // Implements
    // -----------------------------------------------------------------------------------------------

    public int addElement( Element element )
    {

        return elementStore.addElement( element );
    }

    public void deleteElement( int id )
    {

        elementStore.deleteElement( id );
    }

    public Collection<Element> getAllElements()
    {

        return elementStore.getAllElements();
    }

    public Element getElement( int id )
    {

        return elementStore.getElement( id );
    }

    public Collection<Element> getElementsByForm( Form form )
    {

        return elementStore.getElementsByForm( form );
    }

    public Collection<Element> getElementsByEgroup( Egroup egroup )
    {
        return elementStore.getElementsByEgroup( egroup );
    }

    public void updateElement( Element element )
    {

        elementStore.updateElement( element );
    }

    public Collection<Element> getElementsByNoEgroup()
    {

        return elementStore.getElementsByNoEgroup();
    }

    public Element getElement( String name )
    {
        return elementStore.getElement( name );
    }

    public Collection<Element> getElementsByFormLink( Form form )
    {
        return elementStore.getElementsByFormLink( form );
    }
}
