/**
 * 
 */
package org.hisp.dhis.activityplan;

import java.util.Collection;

/**
 * @author abyotag_adm
 * 
 */
public interface ActivityPlanStore
{
    String ID = ActivityPlanStore.class.getName();

    Collection<Integer> getActivitiesByProvider( Integer orgunitId, int max, int min );
    
    int countActivitiesByProvider ( Integer orgunitId );
}
