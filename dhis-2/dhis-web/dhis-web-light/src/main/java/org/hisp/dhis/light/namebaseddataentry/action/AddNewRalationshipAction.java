package org.hisp.dhis.light.namebaseddataentry.action;

import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.relationship.Relationship;
import org.hisp.dhis.relationship.RelationshipService;
import org.hisp.dhis.relationship.RelationshipTypeService;

import com.opensymphony.xwork2.Action;

public class AddNewRalationshipAction
    implements Action
{
    private static final String REDIRECT = "redirect";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private RelationshipService relationshipService;

    public RelationshipService getRelationshipService()
    {
        return relationshipService;
    }

    public void setRelationshipService( RelationshipService relationshipService )
    {
        this.relationshipService = relationshipService;
    }

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

    public void setOriginalPatientId( Integer originalPatientId )
    {
        this.originalPatientId = originalPatientId;
    }

    public Integer getOriginalPatientId()
    {
        return originalPatientId;
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

    private String relationship;

    public String getRelationship()
    {
        return relationship;
    }

    public void setRelationship( String relationship )
    {
        this.relationship = relationship;
    }

    @Override
    public String execute()
        throws Exception
    {
        Relationship newRelationship = new Relationship();
        Patient patientA = patientService.getPatient( originalPatientId );
        Patient patientB = patientService.getPatient( relatedPatientId );

        newRelationship.setRelationshipType( relationshipTypeService.getRelationshipType( relationshipTypeId ) );

        if ( relationship.equals( "A" ) )
        {
            newRelationship.setPatientA( patientA );
            newRelationship.setPatientB( patientB );
        }
        else
        {
            newRelationship.setPatientA( patientB );
            newRelationship.setPatientB( patientA );
        }

        relationshipService.saveRelationship( newRelationship );

        return REDIRECT;
    }

}
