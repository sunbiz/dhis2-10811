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
package org.hisp.dhis.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.dataentryform.DataEntryForm;
import org.hisp.dhis.patient.PatientReminder;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Abyot Asalefew
 */
@JacksonXmlRootElement( localName = "programStage", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramStage
    extends BaseIdentifiableObject
{
    public static final String TYPE_DEFAULT = "default";

    public static final String TYPE_SECTION = "section";

    public static final String TYPE_CUSTOM = "custom";

    public static final String TEMPLATE_MESSSAGE_PATIENT_NAME = "{patient-name}";

    public static final String TEMPLATE_MESSSAGE_PROGRAM_NAME = "{program-name}";

    public static final String TEMPLATE_MESSSAGE_PROGAM_STAGE_NAME = "{program-stage-name}";

    public static final String TEMPLATE_MESSSAGE_DUE_DATE = "{due-date}";

    public static final String TEMPLATE_MESSSAGE_ORGUNIT_NAME = "{orgunit-name}";

    public static final String TEMPLATE_MESSSAGE_DAYS_SINCE_DUE_DATE = "{days-since-due-date}";

    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 6876401001559656214L;

    private String description;

    private int minDaysFromStart;

    private Boolean irregular;

    private Program program;

    private Set<ProgramStageDataElement> programStageDataElements = new HashSet<ProgramStageDataElement>();

    private Set<ProgramStageSection> programStageSections = new HashSet<ProgramStageSection>();

    private DataEntryForm dataEntryForm;

    private Integer standardInterval;

    private String reportDateDescription;

    private Set<PatientReminder> patientReminders = new HashSet<PatientReminder>();

    private Boolean autoGenerateEvent = true;

    private Boolean validCompleteOnly = false;

    private Boolean displayGenerateEventBox = true;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramStage()
    {

    }

    public ProgramStage( String name, Program program )
    {
        this.name = name;
        this.program = program;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null )
        {
            return false;
        }

        if ( getClass() != object.getClass() )
        {
            return false;
        }

        final ProgramStage other = (ProgramStage) object;

        return name.equals( other.getName() );
    }

    @Override
    public String toString()
    {
        return "[" + name + "]";
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public DataEntryForm getDataEntryForm()
    {
        return dataEntryForm;
    }

    public Set<PatientReminder> getPatientReminders()
    {
        return patientReminders;
    }

    public void setPatientReminders( Set<PatientReminder> patientReminders )
    {
        this.patientReminders = patientReminders;
    }

    public void setDataEntryForm( DataEntryForm dataEntryForm )
    {
        this.dataEntryForm = dataEntryForm;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDescription()
    {
        return description;
    }

    public Set<ProgramStageSection> getProgramStageSections()
    {
        return programStageSections;
    }

    public void setProgramStageSections( Set<ProgramStageSection> programStageSections )
    {
        this.programStageSections = programStageSections;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getStandardInterval()
    {
        return standardInterval;
    }

    public void setStandardInterval( Integer standardInterval )
    {
        this.standardInterval = standardInterval;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getIrregular()
    {
        return irregular;
    }

    public void setIrregular( Boolean irregular )
    {
        this.irregular = irregular;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getMinDaysFromStart()
    {
        return minDaysFromStart;
    }

    public void setMinDaysFromStart( int minDaysFromStart )
    {
        this.minDaysFromStart = minDaysFromStart;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    // TODO expose as xml/json
    public Set<ProgramStageDataElement> getProgramStageDataElements()
    {
        return programStageDataElements;
    }

    public void setProgramStageDataElements( Set<ProgramStageDataElement> programStageDataElements )
    {
        this.programStageDataElements = programStageDataElements;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getReportDateDescription()
    {
        return reportDateDescription;
    }

    public void setReportDateDescription( String reportDateDescription )
    {
        this.reportDateDescription = reportDateDescription;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getAutoGenerateEvent()
    {
        return autoGenerateEvent;
    }

    public void setAutoGenerateEvent( Boolean autoGenerateEvent )
    {
        this.autoGenerateEvent = autoGenerateEvent;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getValidCompleteOnly()
    {
        return validCompleteOnly;
    }

    public void setValidCompleteOnly( Boolean validCompleteOnly )
    {
        this.validCompleteOnly = validCompleteOnly;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getDisplayGenerateEventBox()
    {
        return displayGenerateEventBox;
    }

    public void setDisplayGenerateEventBox( Boolean displayGenerateEventBox )
    {
        this.displayGenerateEventBox = displayGenerateEventBox;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDataEntryType()
    {
        if ( dataEntryForm != null )
        {
            return TYPE_CUSTOM;
        }

        if ( programStageSections.size() > 0 )
        {
            return TYPE_SECTION;
        }

        return TYPE_DEFAULT;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDefaultTemplateMessage()
    {
        return "Dear {person-name}, please come to your appointment on {program-stage-name} at {due-date}";
    }
}
