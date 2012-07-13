package org.hisp.dhis.vn.chr.comparator;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Comparator;
import org.hisp.dhis.vn.chr.Egroup;

public class EgroupNameComparator
    implements Comparator<Egroup>
{
    public int compare( Egroup o1, Egroup o2 )
    {
        return o1.getName().compareTo( o2.getName() );
    }

}