package org.hisp.dhis.vn.chr.comparator;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Comparator;
import org.hisp.dhis.vn.chr.FormReport;

public class FormReportNameComparator
    implements Comparator<FormReport>
{
    public int compare( FormReport o1, FormReport o2 )
    {
        return o1.getName().compareTo( o2.getName() );
    }

}
