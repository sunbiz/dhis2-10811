package org.hisp.dhis.light.namebaseddataentry.action;

import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.relationship.RelationshipType;
import org.hisp.dhis.relationship.RelationshipTypeService;
import com.opensymphony.xwork2.Action;

public class GetAddNewRelationshipFormAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private RelationshipTypeService relationshipTypeService;

    public RelationshipTypeService getRelationshipTypeService()
    {
        return relationshipTypeService;
    }

    public void setRelationshipTypeService( RelationshipTypeService relationshipTypeService )
    {
        this.relationshipTypeService = relationshipTypeService;
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

    private Integer relatedPatientId;

    public Integer getRelatedPatientId()
    {
        return relatedPatientId;
    }

    public void setRelatedPatientId( Integer relatedPatientId )
    {
        this.relatedPatientId = relatedPatientId;
    }

    private Integer originalPatientId;

    public Integer getOriginalPatientId()
    {
        return originalPatientId;
    }

    public void setOriginalPatientId( Integer originalPatientId )
    {
        this.originalPatientId = originalPatientId;
    }

    private Integer relationshipTypeId;

    public Integer getRelationshipTypeId()
    {
        return relationshipTypeId;
    }

    public void setRelationshipTypeId( Integer relationshipTypeId )
    {
        this.relationshipTypeId = relationshipTypeId;
    }

    private Patient relatedPatient;

    public Patient getRelatedPatient()
    {
        return relatedPatient;
    }

    public void setRelatedPatient( Patient relatedPatient )
    {
        this.relatedPatient = relatedPatient;
    }

    private Patient originalPatient;

    public Patient getOriginalPatient()
    {
        return originalPatient;
    }

    public void setOriginalPatient( Patient originalPatient )
    {
        this.originalPatient = originalPatient;
    }

    private RelationshipType relationshipType;

    public RelationshipType getRelationshipType()
    {
        return relationshipType;
    }

    public void setRelationshipType( RelationshipType relationshipType )
    {
        this.relationshipType = relationshipType;
    }

    @Override
    public String execute()
        throws Exception
    {
        originalPatient = patientService.getPatient( originalPatientId );
        relatedPatient = patientService.getPatient( relatedPatientId );
        relationshipType = relationshipTypeService.getRelationshipType( relationshipTypeId );

        return SUCCESS;
    }

}
