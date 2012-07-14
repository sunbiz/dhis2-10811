package org.hisp.dhis.light.namebaseddataentry.action;

import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;

import com.opensymphony.xwork2.Action;

public class RegisterPatientLocationAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private PatientService patientService;

    public void setPatientService( PatientService patientService )
    {
        this.patientService = patientService;
    }
    
    private OrganisationUnitService organisationUnitService;
    
    public OrganisationUnitService getOrganisationUnitService()
    {
        return organisationUnitService;
    }

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
    
    private Integer patientId;
    
    public Integer getPatientId()
    {
        return patientId;
    }

    public void setPatientId( Integer patientId )
    {
        this.patientId = patientId;
    }
    
    private Integer orgUnitId;

    public Integer getOrgUnitId()
    {
        return orgUnitId;
    }

    public void setOrgUnitId( Integer orgUnitId )
    {
        this.orgUnitId = orgUnitId;
    }

    @Override
    public String execute()
        throws Exception
    {
        Patient patient = patientService.getPatient( patientId );
        patient.setOrganisationUnit( organisationUnitService.getOrganisationUnit( orgUnitId ) );
        patientService.savePatient( patient );
        
        return SUCCESS;
    }

}
