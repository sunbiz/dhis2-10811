/*
 * Copyright (c) 2004-2009, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.patientattributevalue;

import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT_ATTRIBUTE;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT_PROGRAM_STAGE_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PATIENT_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_STAGE;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_STAGE_DATAELEMENT;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_STAGE_PROPERTY;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_PROPERTY_INCIDENT_DATE;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.OBJECT_PROGRAM_PROPERTY_ENROLLEMENT_DATE;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.SEPARATOR_ID;
import static org.hisp.dhis.caseaggregation.CaseAggregationCondition.SEPARATOR_OBJECT;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientAttribute;
import org.hisp.dhis.patient.PatientAttributeOption;
import org.hisp.dhis.patient.PatientAttributeService;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceService;
import org.hisp.dhis.program.ProgramService;
import org.nfunk.jep.JEP;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
@Transactional
public class DefaultPatientAttributeValueService
    implements PatientAttributeValueService
{
    private final String CURRENT_DATE = "current_date";

    private final String regExp = "\\[(" + CURRENT_DATE + "|" + OBJECT_PATIENT + "|" + OBJECT_PROGRAM + "|"
        + OBJECT_PROGRAM_STAGE + "|" + OBJECT_PROGRAM_STAGE_PROPERTY + "|" + OBJECT_PATIENT_PROGRAM_STAGE_PROPERTY
        + "|" + OBJECT_PROGRAM_STAGE_DATAELEMENT + "|" + OBJECT_PATIENT_ATTRIBUTE + "|" + OBJECT_PATIENT_PROPERTY + "|"
        + OBJECT_PROGRAM_PROPERTY + ")" + SEPARATOR_OBJECT + "([0-9]+[" + SEPARATOR_ID + "[a-zA-Z0-9]*]*)" + "\\]";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PatientAttributeValueStore patientAttributeValueStore;

    public void setPatientAttributeValueStore( PatientAttributeValueStore patientAttributeValueStore )
    {
        this.patientAttributeValueStore = patientAttributeValueStore;
    }

    private PatientAttributeService patientAttributeService;

    public void setPatientAttributeService( PatientAttributeService patientAttributeService )
    {
        this.patientAttributeService = patientAttributeService;
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

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    public void deletePatientAttributeValue( PatientAttributeValue patientAttributeValue )
    {
        patientAttributeValueStore.delete( patientAttributeValue );
    }

    public int deletePatientAttributeValue( Patient patient )
    {
        return patientAttributeValueStore.deleteByPatient( patient );
    }

    public int deletePatientAttributeValue( PatientAttribute patientAttribute )
    {
        return patientAttributeValueStore.deleteByAttribute( patientAttribute );
    }

    public Collection<PatientAttributeValue> getAllPatientAttributeValues()
    {
        return patientAttributeValueStore.getAll();
    }

    public PatientAttributeValue getPatientAttributeValue( Patient patient, PatientAttribute patientAttribute )
    {
        return patientAttributeValueStore.get( patient, patientAttribute );
    }

    public Collection<PatientAttributeValue> getPatientAttributeValues( Patient patient )
    {
        return patientAttributeValueStore.get( patient );
    }

    public Collection<PatientAttributeValue> getPatientAttributeValues( PatientAttribute patientAttribute )
    {
        return patientAttributeValueStore.get( patientAttribute );
    }

    public Collection<PatientAttributeValue> getPatientAttributeValues( Collection<Patient> patients )
    {
        if ( patients != null && patients.size() > 0 )
            return patientAttributeValueStore.get( patients );
        return null;
    }

    public void savePatientAttributeValue( PatientAttributeValue patientAttributeValue )
    {
        if ( patientAttributeValue.getValue() != null )
        {
            patientAttributeValueStore.saveVoid( patientAttributeValue );
        }
    }

    public void updatePatientAttributeValue( PatientAttributeValue patientAttributeValue )
    {
        if ( patientAttributeValue.getValue() == null )
        {
            patientAttributeValueStore.delete( patientAttributeValue );
        }
        else
        {
            patientAttributeValueStore.update( patientAttributeValue );
        }
    }

    public Map<Integer, Collection<PatientAttributeValue>> getPatientAttributeValueMapForPatients(
        Collection<Patient> patients )
    {
        Map<Integer, Set<PatientAttributeValue>> attributeValueMap = new HashMap<Integer, Set<PatientAttributeValue>>();

        Collection<PatientAttributeValue> patientAttributeValues = getPatientAttributeValues( patients );

        for ( PatientAttributeValue patientAttributeValue : patientAttributeValues )
        {
            if ( attributeValueMap.containsKey( patientAttributeValue.getPatient().getId() ) )
            {
                attributeValueMap.get( patientAttributeValue.getPatient().getId() ).add( patientAttributeValue );
            }
            else
            {
                Set<PatientAttributeValue> attributeValues = new HashSet<PatientAttributeValue>();
                attributeValues.add( patientAttributeValue );
                attributeValueMap.put( patientAttributeValue.getPatient().getId(), attributeValues );
            }
        }

        Map<Integer, Collection<PatientAttributeValue>> patentAttributeValueMap = new HashMap<Integer, Collection<PatientAttributeValue>>();

        for ( Entry<Integer, Set<PatientAttributeValue>> entry : attributeValueMap.entrySet() )
        {
            SortedMap<String, PatientAttributeValue> sortedByAttribute = new TreeMap<String, PatientAttributeValue>();

            for ( PatientAttributeValue patientAttributeValue : entry.getValue() )
            {
                sortedByAttribute.put( patientAttributeValue.getPatientAttribute().getName(), patientAttributeValue );
            }

            patentAttributeValueMap.put( entry.getKey(), sortedByAttribute.values() );

        }

        return patentAttributeValueMap;
    }

    public Map<Integer, PatientAttributeValue> getPatientAttributeValueMapForPatients( Collection<Patient> patients,
        PatientAttribute patientAttribute )
    {
        Map<Integer, PatientAttributeValue> attributeValueMap = new HashMap<Integer, PatientAttributeValue>();

        Collection<PatientAttributeValue> patientAttributeValues = getPatientAttributeValues( patients );

        if ( patientAttributeValues != null )
        {
            for ( PatientAttributeValue patientAttributeValue : patientAttributeValues )
            {
                if ( patientAttributeValue.getPatientAttribute() == patientAttribute )
                {
                    attributeValueMap.put( patientAttributeValue.getPatient().getId(), patientAttributeValue );
                }
            }
        }

        return attributeValueMap;
    }

    public Collection<PatientAttributeValue> searchPatientAttributeValue( PatientAttribute patientAttribute,
        String searchText )
    {
        return patientAttributeValueStore.searchByValue( patientAttribute, searchText );
    }

    public void copyPatientAttributeValues( Patient source, Patient destination )
    {
        deletePatientAttributeValue( destination );

        for ( PatientAttributeValue patientAttributeValue : getPatientAttributeValues( source ) )
        {
            PatientAttributeValue attributeValue = new PatientAttributeValue(
                patientAttributeValue.getPatientAttribute(), destination, patientAttributeValue.getValue() );

            savePatientAttributeValue( attributeValue );
        }
    }

    public int countByPatientAttributeoption( PatientAttributeOption attributeOption )
    {
        return patientAttributeValueStore.countByPatientAttributeoption( attributeOption );
    }

    public Collection<Patient> getPatient( PatientAttribute attribute, String value )
    {
        return patientAttributeValueStore.getPatient( attribute, value );
    }

    public void updatePatientAttributeValues( PatientAttributeOption patientAttributeOption )
    {
        patientAttributeValueStore.updatePatientAttributeValues( patientAttributeOption );
    }

    public Double getCalculatedPatientAttributeValue( Patient patient, PatientAttribute patientAttribute,
        I18nFormat format )
    {
        StringBuffer result = new StringBuffer();

        String expression = patientAttribute.getExpression();

        Pattern patternCondition = Pattern.compile( regExp );

        Matcher matcher = patternCondition.matcher( expression );

        Date currentDate = new Date();

        while ( matcher.find() )
        {
            String match = matcher.group();
            match = match.replaceAll( "[\\[\\]]", "" );
            String property = matcher.group( 1 );
            if ( CURRENT_DATE.equals( property ) )
            {
                matcher.appendReplacement( result, "0" );
            }
            else
            {
                String[] infor = matcher.group( 2 ).split( SEPARATOR_ID );
                int id = Integer.parseInt( infor[0] );

                if ( property.equalsIgnoreCase( OBJECT_PATIENT_ATTRIBUTE ) )
                {
                    PatientAttribute attribute = patientAttributeService.getPatientAttribute( id );
                    PatientAttributeValue attributeValue = patientAttributeValueStore.get( patient, attribute );

                    if ( attributeValue != null )
                    {
                        if ( PatientAttribute.TYPE_INT.equals( attributeValue.getPatientAttribute().getValueType() ) )
                        {
                            matcher.appendReplacement( result, attributeValue.getValue() );
                        }
                        else if ( PatientAttribute.TYPE_DATE.equals( attributeValue.getPatientAttribute()
                            .getValueType() ) )
                        {
                            matcher.appendReplacement( result,
                                getDays( currentDate, format.parseDate( attributeValue.getValue() ) ) + "" );
                        }
                    }
                }
                else if ( property.equalsIgnoreCase( OBJECT_PATIENT_PROPERTY ) )
                {
                    matcher.appendReplacement( result, getDays( currentDate, patient.getBirthDate() ) + "" );
                }
                else if ( property.equalsIgnoreCase( OBJECT_PROGRAM ) )
                {
                    Program program = programService.getProgram( id );
                    Collection<ProgramInstance> programInstances = programInstanceService.getProgramInstances( patient,
                        program, false );

                    Date value = null;
                    if ( programInstances != null && programInstances.size() > 0 )
                    {
                        ProgramInstance programInstance = programInstances.iterator().next();
                        String propProgram = infor[1];
                        if ( OBJECT_PROGRAM_PROPERTY_INCIDENT_DATE.equals( propProgram ) )
                        {
                            value = programInstance.getDateOfIncident();
                        }
                        else if ( OBJECT_PROGRAM_PROPERTY_ENROLLEMENT_DATE.equals( propProgram ) )
                        {
                            value = programInstance.getEnrollmentDate();
                        }
                    }
                    else
                    {
                        return null;
                    }
                    matcher.appendReplacement( result, getDays( currentDate, value ) + "" );
                }
            }
        }

        final JEP parser = new JEP();

        parser.parseExpression( result.toString() );

        return parser.getValue();
    }

    private long getDays( Date currentDate, Date dateValue )
    {
        long interval = currentDate.getTime() - dateValue.getTime();
        return (-interval / 86400000);
    }
}
