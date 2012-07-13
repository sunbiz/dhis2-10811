package org.hisp.dhis.sms.namebaseddataentry.action;

import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;

import com.opensymphony.xwork2.Action;

public class GetBeneficiaryDetailAction
    implements Action
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

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

    private String organisationUnitId;

    public String getOrganisationUnitId()
    {
        return organisationUnitId;
    }

    public void setOrganisationUnitId( String organisationUnitId )
    {
        this.organisationUnitId = organisationUnitId;
    }

    private String beneficiaryId;

    public void setBeneficiaryId( String beneficiaryId )
    {
        this.beneficiaryId = beneficiaryId;
    }

    public String getBeneficiaryId()
    {
        return this.beneficiaryId;
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

    private boolean current;

    public void setCurrent( boolean current )
    {
        this.current = current;
    }

    public boolean getCurrent()
    {
        return current;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        this.patient = patientService.getPatient( Integer.parseInt( beneficiaryId ) );
        return SUCCESS;
    }

}
