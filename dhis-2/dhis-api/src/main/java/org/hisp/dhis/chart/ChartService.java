package org.hisp.dhis.chart;

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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.user.User;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;

/**
 * @author Lars Helge Overland
 */
public interface ChartService
{
    String ID = ChartService.class.getName();

    JFreeChart getJFreeChart( int id, I18nFormat format );

    JFreeChart getJFreeChart( Chart chart, I18nFormat format );

    JFreeChart getJFreeChart( Chart chart, Date date, I18nFormat format );
    
    JFreeChart getJFreePeriodChart( Indicator indicator, OrganisationUnit unit, boolean title, I18nFormat format );

    JFreeChart getJFreeOrganisationUnitChart( Indicator indicator, OrganisationUnit parent, boolean title, I18nFormat format );

    JFreeChart getJFreeChart( List<Indicator> indicators, List<DataElement> dataElements, List<Period> periods,
                              List<OrganisationUnit> organisationUnits, String series, String category, String filter, boolean regression, I18nFormat format );

    JFreeChart getJFreeChart( String name, PlotOrientation orientation, CategoryLabelPositions labelPositions,
                              Map<String, Double> categoryValues );

    JFreeChart getJFreeChartHistory( DataElement dataElement, DataElementCategoryOptionCombo categoryOptionCombo,
                                     Period lastPeriod, OrganisationUnit organisationUnit, int historyLength, I18nFormat format );

    int saveChart( Chart chart );

    void saveOrUpdate( Chart chart );

    Chart getChart( int id );

    Chart getChart( String uid );

    void deleteChart( Chart chart );

    Collection<Chart> getAllCharts();
    
    Collection<Chart> getSystemAndUserCharts();

    Chart getChartByName( String name );

    Collection<Chart> getCharts( final Collection<Integer> identifiers );

    Collection<Chart> getChartsBetween( int first, int max );

    Collection<Chart> getChartsBetweenByName( String name, int first, int max );

    int getChartCount();

    int getChartCountByName( String name );
    
    Collection<Chart> getChartsByUser( User user );
}
