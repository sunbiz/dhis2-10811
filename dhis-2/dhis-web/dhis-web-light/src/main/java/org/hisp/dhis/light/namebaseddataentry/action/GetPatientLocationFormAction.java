package org.hisp.dhis.light.namebaseddataentry.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork2.Action;

public class GetPatientLocationFormAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CurrentUserService currentUserService;

    public CurrentUserService getCurrentUserService()
    {
        return currentUserService;
    }

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private PatientService patientService;

    public PatientService getPatientService()
    {
        return patientService;
    }

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private Set<OrganisationUnit> organisationUnits;

    public Set<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setOrganisationUnits( Set<OrganisationUnit> organisationUnits )
    {
        this.organisationUnits = organisationUnits;
    }

    private Integer patientId;

    public Integer getPatientId()
    {
        return patientId;
    }

    public void setPatientId( Integer patientId )
    {
        this.patientId = patientId;
    }

    private Patient patient;

    public Patient getPatient()
    {
        return patient;
    }

    public void setPatient( Patient patient )
    {
        this.patient = patient;
    }

    @Override
    public String execute()
        throws Exception
    {
        Collection<OrganisationUnit> basicOrganisationUnits = currentUserService.getCurrentUser()
            .getOrganisationUnits();
        organisationUnits = new HashSet<OrganisationUnit>();

        patient = patientService.getPatient( patientId );

        for ( OrganisationUnit organisationUnit : basicOrganisationUnits )
        {
            organisationUnits.addAll( this.getAllParentOrganisationUnits( organisationUnit ) );
        }

        organisationUnits.add( patient.getOrganisationUnit() );

        return SUCCESS;
    }

    private Collection<? extends OrganisationUnit> getAllParentOrganisationUnits( OrganisationUnit organisationUnit )
    {
        List<OrganisationUnit> parents = new ArrayList<OrganisationUnit>();
        parents.add( organisationUnit );

        while ( organisationUnit.getParent() != null )
        {
            parents.add( organisationUnit.getParent() );
            organisationUnit = organisationUnit.getParent();
        }
        return parents;
    }

}
