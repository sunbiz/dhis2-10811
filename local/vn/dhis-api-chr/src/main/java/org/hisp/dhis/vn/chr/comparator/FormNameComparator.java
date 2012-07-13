package org.hisp.dhis.vn.chr.comparator;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Comparator;
import org.hisp.dhis.vn.chr.Form;

public class FormNameComparator
    implements Comparator<Form>
{
    public int compare( Form o1, Form o2 )
    {
        return o1.getName().compareTo( o2.getName() );
    }

}
