package org.hisp.dhis.vn.chr.comparator;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Comparator;

import org.hisp.dhis.vn.chr.Element;

public class ElementNameComparator
    implements Comparator<Element>
{
    public int compare( Element o1, Element o2 )
    {
        return o1.getName().compareTo( o2.getName() );
    }

}
