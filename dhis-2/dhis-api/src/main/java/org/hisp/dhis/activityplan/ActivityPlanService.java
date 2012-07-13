/**
 * 
 */
package org.hisp.dhis.activityplan;

import java.util.Collection;
import java.util.Date;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramStageInstance;

/**
 * @author abyotag_adm
 * 
 */
public interface ActivityPlanService
{
    String ID = ActivityPlanService.class.getName();

    Collection<Activity> getActivitiesByProvider( OrganisationUnit organisationUnit );
    
    Collection<Activity> getActivitiesByProvider( OrganisationUnit organisationUnit, int min, int max );

    Collection<Activity> getActivitiesByProvider( OrganisationUnit organisationUnit, Collection<Program> programs );

    Collection<Activity> getActivitiesByProgram( Collection<Program> programs );

    Collection<Activity> getActivitiesByBeneficiary( Patient beneficiary );

    Collection<Activity> getActivitiesByTask( ProgramStageInstance task );

    Collection<Activity> getActivitiesByDueDate( Date dueDate );

    Collection<Activity> getActivitiesWithInDate( Date startDate, Date endDate );

    Collection<Activity> getCurrentActivitiesByProvider( OrganisationUnit organisationUnit );
    
    int countActivitiesByProvider ( OrganisationUnit organisationUnit );

}
