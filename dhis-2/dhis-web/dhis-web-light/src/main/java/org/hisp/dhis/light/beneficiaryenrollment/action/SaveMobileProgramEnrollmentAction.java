package org.hisp.dhis.light.beneficiaryenrollment.action;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.hisp.dhis.light.utils.FormUtils;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientService;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageInstance;
import org.hisp.dhis.program.ProgramStageInstanceService;
import org.hisp.dhis.system.util.DateUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.opensymphony.xwork2.Action;

public class SaveMobileProgramEnrollmentAction
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

    private ProgramService programService;

    public void setProgramService( ProgramService programService )
    {
        this.programService = programService;
    }

    private ProgramInstanceService programInstanceService;

    public void setProgramInstanceService( ProgramInstanceService programInstanceService )
    {
        this.programInstanceService = programInstanceService;
    }

    private ProgramStageInstanceService programStageInstanceService;

    public void setProgramStageInstanceService( ProgramStageInstanceService programStageInstanceService )
    {
        this.programStageInstanceService = programStageInstanceService;
    }

    private FormUtils formUtils;

    public FormUtils getFormUtils()
    {
        return formUtils;
    }

    public void setFormUtils( FormUtils formUtils )
    {
        this.formUtils = formUtils;
    }

    // -------------------------------------------------------------------------
    // Input/Output
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

    private Integer programId;

    public void setProgramId( Integer programId )
    {
        this.programId = programId;
    }

    public Integer getProgramId()
    {
        return programId;
    }

    private Patient patient;

    public Patient getPatient()
    {
        return patient;
    }

    private Program program;

    public Program getProgram()
    {
        return program;
    }

    private String enrollmentDate;

    public void setEnrollmentDate( String enrollmentDate )
    {
        this.enrollmentDate = enrollmentDate;
    }

    private String incidentDate;

    public String getIncidentDate()
    {
        return incidentDate;
    }

    public void setIncidentDate( String incidentDate )
    {
        this.incidentDate = incidentDate;
    }

    private Map<String, String> validationMap = new HashMap<String, String>();

    public Map<String, String> getValidationMap()
    {
        return validationMap;
    }

    public void setValidationMap( Map<String, String> validationMap )
    {
        this.validationMap = validationMap;
    }

    private Map<String, String> previousValues = new HashMap<String, String>();

    public Map<String, String> getPreviousValues()
    {
        return previousValues;
    }

    public void setPreviousValues( Map<String, String> previousValues )
    {
        this.previousValues = previousValues;
    }

    private boolean validated;

    public boolean isValidated()
    {
        return validated;
    }

    public void setValidated( boolean validated )
    {
        this.validated = validated;
    }

    public String execute()
        throws Exception
    {
        DateTimeFormatter sdf = ISODateTimeFormat.yearMonthDay();

        if ( !FormUtils.isDate( enrollmentDate ) )
        {
            validationMap.put( "enrollmentDate", "is_invalid_date" );
        }

        if ( !FormUtils.isDate( incidentDate ) )
        {
            validationMap.put( "incidentDate", "is_invalid_date" );
        }

        if ( validationMap.size() > 0 )
        {
            previousValues.put( "enrollmentDate", enrollmentDate );
            previousValues.put( "incidentDate", incidentDate );
            validated = false;
            return ERROR;
        }

        patient = patientService.getPatient( patientId );

        program = programService.getProgram(  programId );

        Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( patient, program,
            false );

        ProgramInstance programInstance = null;

        if ( programInstances.iterator().hasNext() )
        {
            programInstance = programInstances.iterator().next();
        }

        if ( programInstance == null )
        {
            programInstance = new ProgramInstance();
            programInstance.setEnrollmentDate( sdf.parseDateTime( enrollmentDate ).toDate() );
            programInstance.setDateOfIncident( sdf.parseDateTime( incidentDate ).toDate() );
            programInstance.setProgram( program );
            programInstance.setPatient( patient );
            programInstance.setCompleted( false );

            programInstanceService.addProgramInstance( programInstance );

            patient.getPrograms().add( program );
            patientService.updatePatient( patient );

            for ( ProgramStage programStage : program.getProgramStages() )
            {
                ProgramStageInstance programStageInstance = new ProgramStageInstance();
                programStageInstance.setProgramInstance( programInstance );
                programStageInstance.setProgramStage( programStage );
                programStageInstance.setStageInProgram( programStage.getStageInProgram() );

                Date dueDate = DateUtils.getDateAfterAddition( sdf.parseDateTime( incidentDate ).toDate(),
                    programStage.getMinDaysFromStart() );

                programStageInstance.setDueDate( dueDate );

                programStageInstanceService.addProgramStageInstance( programStageInstance );
            }
        }
        else
        {
            programInstance.setEnrollmentDate( sdf.parseDateTime( enrollmentDate ).toDate() );
            programInstance.setDateOfIncident( sdf.parseDateTime( incidentDate ).toDate() );

            programInstanceService.updateProgramInstance( programInstance );

            for ( ProgramStageInstance programStageInstance : programInstance.getProgramStageInstances() )
            {
                Date dueDate = DateUtils.getDateAfterAddition( sdf.parseDateTime( incidentDate ).toDate(),
                    programStageInstance.getProgramStage().getMinDaysFromStart() );

                programStageInstance.setDueDate( dueDate );

                programStageInstanceService.updateProgramStageInstance( programStageInstance );
            }
        }
        validated = true;
        return SUCCESS;
    }

}
