package org.hisp.dhis.interpretation;

/*
 * Copyright (c) 2004-2012, University of Oslo
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hisp.dhis.chart.Chart;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.Dxf2Namespace;
import org.hisp.dhis.common.annotation.Scanned;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.mapping.MapView;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Lars Helge Overland
 */
@JacksonXmlRootElement( localName = "interpretation", namespace = Dxf2Namespace.NAMESPACE )
public class Interpretation
    extends BaseIdentifiableObject
{
    private Chart chart;

    private MapView mapView;
    
    private ReportTable reportTable;
    
    private OrganisationUnit organisationUnit; // Applicable to report table only
    
    private String text;

    private User user;

    private Date created;

    @Scanned
    private List<InterpretationComment> comments = new ArrayList<InterpretationComment>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Interpretation()
    {
        this.created = new Date();
    }

    public Interpretation( Chart chart, String text )
    {
        this.chart = chart;
        this.text = text;
        this.created = new Date();
    }

    public Interpretation( MapView mapView, String text )
    {
        this.mapView = mapView;
        this.text = text;
        this.created = new Date();
    }
    
    public Interpretation( ReportTable reportTable, OrganisationUnit organisationUnit, String text )
    {
        this.reportTable = reportTable;
        this.organisationUnit = organisationUnit;
        this.text = text;
        this.created = new Date();
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addComment( InterpretationComment comment )
    {
        this.comments.add( comment );
    }

    public boolean isChartInterpretation()
    {
        return chart != null;
    }
    
    public boolean isMapViewInterpretation()
    {
        return mapView != null;
    }
    
    public boolean isReportTableInterpretation()
    {
        return reportTable != null;
    }
        
    // -------------------------------------------------------------------------
    // Get and set methods
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonDeserialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public Chart getChart()
    {
        return chart;
    }

    public void setChart( Chart chart )
    {
        this.chart = chart;
    }

    @JsonProperty
    @JsonDeserialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public MapView getMapView()
    {
        return mapView;
    }

    public void setMapView( MapView mapView )
    {
        this.mapView = mapView;
    }

    @JsonProperty
    @JsonDeserialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public ReportTable getReportTable()
    {
        return reportTable;
    }

    public void setReportTable( ReportTable reportTable )
    {
        this.reportTable = reportTable;
    }

    @JsonProperty
    @JsonDeserialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    public void setOrganisationUnit( OrganisationUnit organisationUnit )
    {
        this.organisationUnit = organisationUnit;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    @JsonProperty
    @JsonDeserialize( as = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    @JsonProperty
    @JsonDeserialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public List<InterpretationComment> getComments()
    {
        return comments;
    }

    public void setComments( List<InterpretationComment> comments )
    {
        this.comments = comments;
    }
}
