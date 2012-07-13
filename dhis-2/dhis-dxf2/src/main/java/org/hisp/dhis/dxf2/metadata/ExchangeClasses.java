package org.hisp.dhis.dxf2.metadata;

/*
 * Copyright (c) 2012, University of Oslo
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

import org.hisp.dhis.attribute.Attribute;
import org.hisp.dhis.chart.Chart;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.concept.Concept;
import org.hisp.dhis.constant.Constant;
import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.dataelement.*;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.document.Document;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorGroupSet;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.interpretation.Interpretation;
import org.hisp.dhis.mapping.MapLayer;
import org.hisp.dhis.mapping.MapLegend;
import org.hisp.dhis.mapping.MapLegendSet;
import org.hisp.dhis.mapping.MapView;
import org.hisp.dhis.message.MessageConversation;
import org.hisp.dhis.option.OptionSet;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.report.Report;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.sqlview.SqlView;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserAuthorityGroup;
import org.hisp.dhis.user.UserGroup;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleGroup;

import java.util.*;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
final public class ExchangeClasses
{
    private static Map<Class<? extends IdentifiableObject>, String> exportClasses;

    private static Map<Class<? extends IdentifiableObject>, String> importClasses;

    static
    {
        exportClasses = new LinkedHashMap<Class<? extends IdentifiableObject>, String>();

        exportClasses.put( SqlView.class, "sqlViews" );
        exportClasses.put( Concept.class, "concepts" );
        exportClasses.put( Constant.class, "constants" );
        exportClasses.put( Document.class, "documents" );
        exportClasses.put( OptionSet.class, "optionSets" );
        exportClasses.put( Attribute.class, "attributeTypes" );

        exportClasses.put( OrganisationUnit.class, "organisationUnits" );
        exportClasses.put( OrganisationUnitLevel.class, "organisationUnitLevels" );
        exportClasses.put( OrganisationUnitGroup.class, "organisationUnitGroups" );
        exportClasses.put( OrganisationUnitGroupSet.class, "organisationUnitGroupSets" );

        exportClasses.put( DataElementCategoryOption.class, "categoryOptions" );
        exportClasses.put( DataElementCategory.class, "categories" );
        exportClasses.put( DataElementCategoryCombo.class, "categoryCombos" );
        exportClasses.put( DataElementCategoryOptionCombo.class, "categoryOptionCombos" );

        exportClasses.put( DataElement.class, "dataElements" );
        exportClasses.put( DataElementGroup.class, "dataElementGroups" );
        exportClasses.put( DataElementGroupSet.class, "dataElementGroupSets" );

        exportClasses.put( IndicatorType.class, "indicatorTypes" );
        exportClasses.put( Indicator.class, "indicators" );
        exportClasses.put( IndicatorGroup.class, "indicatorGroups" );
        exportClasses.put( IndicatorGroupSet.class, "indicatorGroupSets" );

        exportClasses.put( DataDictionary.class, "dataDictionaries" );

        exportClasses.put( DataSet.class, "dataSets" );
        exportClasses.put( Section.class, "sections" );

        exportClasses.put( ReportTable.class, "reportTables" );
        exportClasses.put( Report.class, "reports" );
        exportClasses.put( Chart.class, "charts" );

        exportClasses.put( ValidationRule.class, "validationRules" );
        exportClasses.put( ValidationRuleGroup.class, "validationRuleGroups" );

        exportClasses.put( MapView.class, "maps" );
        exportClasses.put( MapLegend.class, "mapLegends" );
        exportClasses.put( MapLegendSet.class, "mapLegendSets" );
        exportClasses.put( MapLayer.class, "mapLayers" );

        exportClasses.put( User.class, "users" );
        exportClasses.put( UserGroup.class, "userGroups" );
        exportClasses.put( UserAuthorityGroup.class, "userRoles" );

        exportClasses.put( MessageConversation.class, "messageConversations" );
        exportClasses.put( Interpretation.class, "interpretations" );

        importClasses = new LinkedHashMap<Class<? extends IdentifiableObject>, String>( exportClasses );

        importClasses.remove( User.class );
        importClasses.remove( UserAuthorityGroup.class );
        importClasses.remove( UserGroup.class );

        importClasses.remove( MessageConversation.class );
        importClasses.remove( Interpretation.class );
    }

    public static Map<Class<? extends IdentifiableObject>, String> getExportMap()
    {
        return Collections.unmodifiableMap( exportClasses );
    }

    public static List<Class<? extends IdentifiableObject>> getExportClasses()
    {
        return new ArrayList<Class<? extends IdentifiableObject>>( exportClasses.keySet() );
    }

    public static Map<Class<? extends IdentifiableObject>, String> getImportMap()
    {
        return Collections.unmodifiableMap( importClasses );
    }

    public static List<Class<? extends IdentifiableObject>> getImportClasses()
    {
        return new ArrayList<Class<? extends IdentifiableObject>>( importClasses.keySet() );
    }
}
