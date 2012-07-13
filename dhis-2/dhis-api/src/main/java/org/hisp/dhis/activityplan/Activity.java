/**
 * 
 */
package org.hisp.dhis.activityplan;

import java.util.Date;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.program.ProgramStageInstance;

/**
 * @author abyotag_adm
 */
public class Activity
{
    private OrganisationUnit provider;

    private Patient beneficiary;

    private ProgramStageInstance task;

    private Date dueDate;
    
    private boolean completed;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Activity()
    {
    }

    /**
     * @return the provider
     */
    public OrganisationUnit getProvider()
    {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider( OrganisationUnit provider )
    {
        this.provider = provider;
    }

    /**
     * @return the beneficiary
     */
    public Patient getBeneficiary()
    {
        return beneficiary;
    }

    /**
     * @param beneficiary the beneficiary to set
     */
    public void setBeneficiary( Patient beneficiary )
    {
        this.beneficiary = beneficiary;
    }

    /**
     * @return the task
     */
    public ProgramStageInstance getTask()
    {
        return task;
    }

    /**
     * @param task the task to set
     */
    public void setTask( ProgramStageInstance task )
    {
        this.task = task;
    }
    
    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate( Date dueDate )
    {
        this.dueDate = dueDate;
    }

    /**
     * @return the dueDate
     */
    public Date getDueDate()
    {
        return dueDate;
    }

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

}
