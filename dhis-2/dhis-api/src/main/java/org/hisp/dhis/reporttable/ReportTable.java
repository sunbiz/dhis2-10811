package org.hisp.dhis.reporttable;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.BaseNameableObject;
import org.hisp.dhis.common.CombinationGenerator;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.ListMap;
import org.hisp.dhis.common.NameableObject;
import org.hisp.dhis.common.adapter.JacksonMapListIdentifiableObjectSerializer;
import org.hisp.dhis.common.adapter.JacksonPeriodDeserializer;
import org.hisp.dhis.common.adapter.JacksonPeriodSerializer;
import org.hisp.dhis.common.annotation.Scanned;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.period.comparator.AscendingPeriodComparator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * The ReportTable object represents a customizable database table. It has
 * features like crosstabulation, relative periods, parameters, and display
 * columns.
 *
 * @author Lars Helge Overland
 */
@JacksonXmlRootElement( localName = "reportTable", namespace = DxfNamespaces.DXF_2_0)
public class ReportTable
    extends BaseIdentifiableObject
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 5618655666320890565L;
    
    public static final String DATA_X_DIM_ID = "dx"; // IN, DE, DS
    public static final String PERIOD_DIM_ID = "pe";
    public static final String ORGUNIT_DIM_ID = "ou";
    
    public static final String DATAELEMENT_ID = "dataelementid";
    public static final String CATEGORYCOMBO_ID = "categoryoptioncomboid";
    public static final String CATEGORYOPTION_ID = "categoryoptionid";

    public static final String INDICATOR_ID = "indicatorid";
    public static final String INDICATOR_UID = "indicatoruid";
    public static final String INDICATOR_NAME = "indicatorname";
    public static final String INDICATOR_CODE = "indicatorcode";
    public static final String INDICATOR_DESCRIPTION = "indicatordescription";

    public static final String DATASET_ID = "datasetid";

    public static final String PERIOD_ID = "periodid";
    public static final String PERIOD_UID = "perioduid";
    public static final String PERIOD_NAME = "periodname";
    public static final String PERIOD_CODE = "periodcode";
    public static final String PERIOD_DESCRIPTION = "perioddescription";

    public static final String ORGANISATIONUNIT_ID = "organisationunitid";
    public static final String ORGANISATIONUNIT_UID = "organisationunituid";
    public static final String ORGANISATIONUNIT_NAME = "organisationunitname";
    public static final String ORGANISATIONUNIT_CODE = "organisationunitcode";
    public static final String ORGANISATIONUNIT_DESCRIPTION = "organisationunitdescription";

    public static final String ORGANISATIONUNITGROUP_ID = "organisationunitgroupid";
    public static final String ORGANISATIONUNITGROUP_UID = "organisationunitgroupuid";
    public static final String ORGANISATIONUNITGROUP_NAME = "organisationunitgroupname";
    public static final String ORGANISATIONUNITGROUP_CODE = "organisationunitgroupcode";
    public static final String ORGANISATIONUNITGROUP_DESCRIPTION = "organisationunitgroupdescription";

    public static final String REPORTING_MONTH_COLUMN_NAME = "reporting_month_name";
    public static final String PARAM_ORGANISATIONUNIT_COLUMN_NAME = "param_organisationunit_name";
    public static final String ORGANISATION_UNIT_IS_PARENT_COLUMN_NAME = "organisation_unit_is_parent";

    public static final String SEPARATOR = "_";
    public static final String SPACE = " ";
    public static final String KEY_ORGUNIT_GROUPSET = "orgunit_groupset_";

    public static final String TOTAL_COLUMN_NAME = "total";
    public static final String TOTAL_COLUMN_PRETTY_NAME = "Total";

    public static final String DISPLAY_DENSITY_COMFORTABLE = "comfortable";
    public static final String DISPLAY_DENSITY_NORMAL = "normal";
    public static final String DISPLAY_DENSITY_COMPACT = "compact";
    
    public static final String FONT_SIZE_LARGE = "large";
    public static final String FONT_SIZE_NORMAL = "normal";
    public static final String FONT_SIZE_SMALL = "small";

    public static final String NUMBER_FORMATTING_COMMA = "comma";
    public static final String NUMBER_FORMATTING_SPACE = "space";
    public static final String NUMBER_FORMATTING_NONE = "none";
    
    public static final int ASC = -1;
    public static final int DESC = 1;
    public static final int NONE = 0;

    public static final Map<String, String> PRETTY_COLUMNS = new HashMap<String, String>()
    {
        {
            put( CATEGORYCOMBO_ID, "Category combination ID" );

            put( INDICATOR_ID, "Indicator ID" );
            put( INDICATOR_UID, "Indicator UID" );
            put( INDICATOR_NAME, "Indicator" );
            put( INDICATOR_CODE, "Indicator code" );
            put( INDICATOR_DESCRIPTION, "Indicator description" );

            put( PERIOD_ID, "Period ID" );
            put( PERIOD_UID, "Period UID" );
            put( PERIOD_NAME, "Period" );
            put( PERIOD_CODE, "Period code" );
            put( PERIOD_DESCRIPTION, "Period description" );

            put( ORGANISATIONUNIT_ID, "Organisation unit ID" );
            put( ORGANISATIONUNIT_UID, "Organisation unit UID" );
            put( ORGANISATIONUNIT_NAME, "Organisation unit" );
            put( ORGANISATIONUNIT_CODE, "Organisation unit code" );
            put( ORGANISATIONUNIT_DESCRIPTION, "Organisation unit description" );

            put( ORGANISATIONUNITGROUP_ID, "Organisation unit group ID" );
            put( ORGANISATIONUNITGROUP_UID, "Organisation unit group UID" );
            put( ORGANISATIONUNITGROUP_NAME, "Organisation unit group" );
            put( ORGANISATIONUNITGROUP_CODE, "Organisation unit group code" );
            put( ORGANISATIONUNITGROUP_DESCRIPTION, "Organisation unit group description" );

            put( REPORTING_MONTH_COLUMN_NAME, "Reporting month" );
            put( PARAM_ORGANISATIONUNIT_COLUMN_NAME, "Organisation unit parameter" );
            put( ORGANISATION_UNIT_IS_PARENT_COLUMN_NAME, "Organisation unit is parent" );
        }
    };

    public static final Map<Class<? extends NameableObject>, String> CLASS_ID_MAP = new HashMap<Class<? extends NameableObject>, String>()
    {
        {
            put( Indicator.class, INDICATOR_ID );
            put( DataElement.class, DATAELEMENT_ID );
            put( DataElementCategoryOptionCombo.class, CATEGORYCOMBO_ID );
            put( DataElementCategoryOption.class, CATEGORYOPTION_ID );
            put( DataSet.class, DATASET_ID );
            put( Period.class, PERIOD_ID );
            put( OrganisationUnit.class, ORGANISATIONUNIT_ID );
            put( OrganisationUnitGroup.class, ORGANISATIONUNITGROUP_ID );
        }
    };

    private static final String EMPTY = "";

    private static final NameableObject[] IRT = new NameableObject[0];

    private static final String[] SRT = new String[0];

    private static final String ILLEGAL_FILENAME_CHARS_REGEX = "[/\\?%*:|\"'<>.]";

    // -------------------------------------------------------------------------
    // Persisted properties
    // -------------------------------------------------------------------------

    /**
     * Indicates whether the ReportTable contains regression columns.
     */
    private boolean regression;

    /**
     * Indicates whether the ReportTable contains cumulative columns.
     */
    private boolean cumulative;

    /**
     * The list of DataElements the ReportTable contains.
     */
    @Scanned
    private List<DataElement> dataElements = new ArrayList<DataElement>();

    /**
     * The list of Indicators the ReportTable contains.
     */
    @Scanned
    private List<Indicator> indicators = new ArrayList<Indicator>();

    /**
     * The list of DataSets the ReportTable contains.
     */
    @Scanned
    private List<DataSet> dataSets = new ArrayList<DataSet>();

    /**
     * The list of Periods the ReportTable contains.
     */
    @Scanned
    private List<Period> periods = new ArrayList<Period>();

    /**
     * The list of OrganisationUnits the ReportTable contains.
     */
    @Scanned
    private List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();

    /**
     * The list of DataElementGroups the ReportTable contains.
     */
    @Scanned
    private List<DataElementGroup> dataElementGroups = new ArrayList<DataElementGroup>();
    
    /**
     * The list of OrganisationUnitGroups the ReportTable contains.
     */
    @Scanned
    private List<OrganisationUnitGroup> organisationUnitGroups = new ArrayList<OrganisationUnitGroup>();

    /**
     * Dimensions to crosstabulate / use as columns.
     */
    private List<String> columnDimensions = new ArrayList<String>();
    
    /**
     * Dimensions to use as rows.
     */
    private List<String> rowDimensions = new ArrayList<String>();
    
    /**
     * Dimensions to use as filter.
     */
    private List<String> filterDimensions = new ArrayList<String>();
    
    /**
     * The DataElementCategoryCombo for the ReportTable.
     */
    private DataElementCategoryCombo categoryCombo;

    /**
     * The RelativePeriods of the ReportTable.
     */
    private RelativePeriods relatives;

    /**
     * The ReportParams of the ReportTable.
     */
    private ReportParams reportParams;

    /**
     * The sort order if any applied to the last column of the table.
     */
    private Integer sortOrder;

    /**
     * Indicates whether the table should be limited from top by this value.
     */
    private Integer topLimit;

    /**
     * Indicates rendering of sub-totals for the table.
     */
    private boolean totals;

    /**
     * Indicates rendering of sub-totals for the table.
     */
    private boolean subtotals;

    /**
     * Indicates rendering of empty rows for the table.
     */
    private boolean hideEmptyRows;
    
    /**
     * Indicates rendering of number formatting for the table.
     */
    private String digitGroupSeparator;
    
    /**
     * The display density of the text in the table.
     */
    private String displayDensity;
    
    /**
     * The font size of the text in the table.
     */
    private String fontSize;
    
    /**
     * Indicates use of the organisation unit of the current user.
     */
    private boolean userOrganisationUnit;
    
    /**
     * Indicates use of the organisation unit children of the current user.
     */
    private boolean userOrganisationUnitChildren;

    // -------------------------------------------------------------------------
    // Presentation properties
    // -------------------------------------------------------------------------

    /**
     * Map of data element group set uids and data element groups.
     */
    private Map<String, List<DataElementGroup>> dataElementGroupSets = new HashMap<String, List<DataElementGroup>>();
    
    /**
     * Map of organisation unit group uids and organisation unit groups.
     */
    private Map<String, List<OrganisationUnitGroup>> organisationUnitGroupSets = new HashMap<String, List<OrganisationUnitGroup>>();
    
    // -------------------------------------------------------------------------
    // Transient properties
    // -------------------------------------------------------------------------

    /**
     * Periods relative to the reporting month.
     */
    private List<Period> relativePeriods = new ArrayList<Period>();

    /**
     * Static Periods and relative Periods.
     */
    private List<Period> allPeriods = new ArrayList<Period>();

    /**
     * OrganisationUnits relative to a parent unit or current unit.
     */
    private List<OrganisationUnit> relativeUnits = new ArrayList<OrganisationUnit>();

    /**
     * Static OrganisationUnits and relative OrganisationUnits.
     */
    private List<NameableObject> allUnits = new ArrayList<NameableObject>();

    /**
     * All Indicators, including DateElements, Indicators and DataSets.
     */
    private List<NameableObject> allIndicators = new ArrayList<NameableObject>();

    /**
     * All crosstabulated columns.
     */
    private List<List<NameableObject>> columns = new ArrayList<List<NameableObject>>();

    /**
     * All rows.
     */
    private List<List<NameableObject>> rows = new ArrayList<List<NameableObject>>();

    /**
     * Names of the columns used to query the datavalue table and as index
     * columns in the report table.
     */
    private List<String> indexColumns = new ArrayList<String>();

    /**
     * Names of the columns holding entry uids used to query the datavalue
     * table.
     */
    private List<String> indexUidColumns = new ArrayList<String>();

    /**
     * Names of the columns holding entry names used to query the datavalue
     * table.
     */
    private List<String> indexNameColumns = new ArrayList<String>();

    /**
     * Names of the columns holding entry codes used to query the datavalue
     * table.
     */
    private List<String> indexCodeColumns = new ArrayList<String>();

    /**
     * Names of the columns holding entry descriptions.
     */
    private List<String> indexDescriptionColumns = new ArrayList<String>();

    /**
     * The I18nFormat used for internationalization of ie. periods.
     */
    private I18nFormat i18nFormat;

    /**
     * The name of the reporting month based on the report param.
     */
    private String reportingPeriodName;

    /**
     * The parent organisation unit.
     */
    private OrganisationUnit parentOrganisationUnit;

    /**
     * The category option combos derived from the dimension set.
     */
    private List<DataElementCategoryOptionCombo> categoryOptionCombos = new ArrayList<DataElementCategoryOptionCombo>();

    /**
     * Map linking organisation unit uids and parent graphs.
     */
    private Map<String, String> parentGraphMap = new HashMap<String, String>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Constructor for persistence purposes.
     */
    public ReportTable()
    {
    }

    /**
     * Default constructor.
     *
     * @param name the name.
     * @param dataElements the data elements.
     * @param indicators the indicators.
     * @param dataSets the data sets.
     * @param periods the periods. Cannot have the name property set.
     * @param relativePeriods the relative periods. These periods must have the
     *                        name property set. Not persisted.
     * @param organisationUnits the organisation units.
     * @param relativeUnits the organisation units. Not persisted.
     * @param doIndicators indicating whether indicators should be crosstabulated.
     * @param doPeriods indicating whether periods should be crosstabulated.
     * @param doUnits indicating whether organisation units should be crosstabulated.
     * @param relatives the relative periods.
     * @param i18nFormat the i18n format. Not persisted.
     */
    public ReportTable( String name, List<DataElement> dataElements, List<Indicator> indicators,
                        List<DataSet> dataSets, List<Period> periods, List<Period> relativePeriods, List<OrganisationUnit> organisationUnits,
                        List<OrganisationUnit> relativeUnits, List<OrganisationUnitGroup> organisationUnitGroups,
                        DataElementCategoryCombo categoryCombo, boolean doIndicators,
                        boolean doPeriods, boolean doUnits, RelativePeriods relatives, ReportParams reportParams,
                        I18nFormat i18nFormat, String reportingPeriodName )
    {
        this.name = name;
        this.dataElements = dataElements;
        this.indicators = indicators;
        this.dataSets = dataSets;
        this.periods = periods;
        this.relativePeriods = relativePeriods;
        this.organisationUnits = organisationUnits;
        this.relativeUnits = relativeUnits;
        this.organisationUnitGroups = organisationUnitGroups;
        this.categoryCombo = categoryCombo;
        this.relatives = relatives;
        this.reportParams = reportParams;
        this.i18nFormat = i18nFormat;
        this.reportingPeriodName = reportingPeriodName;
        
        if ( doIndicators )
        {
            columnDimensions.add( DATA_X_DIM_ID );
        }
        else
        {
            rowDimensions.add( DATA_X_DIM_ID );
        }
        
        if ( doPeriods )
        {
            columnDimensions.add( PERIOD_DIM_ID );
        }
        else
        {
            rowDimensions.add( PERIOD_DIM_ID );
        }
        
        if ( doUnits )
        {
            columnDimensions.add( ORGUNIT_DIM_ID );
        }
        else
        {
            rowDimensions.add( ORGUNIT_DIM_ID );
        }
    }

    // -------------------------------------------------------------------------
    // Init
    // -------------------------------------------------------------------------

    public void init()
    {
        verify( nonEmptyLists( dataElements, indicators, dataSets ) > 0,
            "Must contain dataelements, indicators or datasets" );
        verify( nonEmptyLists( periods, relativePeriods ) > 0, "Must contain periods or relative periods" );
        verify( nonEmptyLists( organisationUnits, relativeUnits, organisationUnitGroups ) > 0,
            "Must contain organisation units, relative organisation units or organisation unit groups" );
        verify( !(doTotal() && regression), "Cannot have regression columns with total columns" );
        verify( i18nFormat != null, "I18n format must be set" );

        // ---------------------------------------------------------------------
        // Init dimensions
        // ---------------------------------------------------------------------

        if ( isDimensional() )
        {
            categoryOptionCombos = new ArrayList<DataElementCategoryOptionCombo>( categoryCombo.getOptionCombos() );
            verify( nonEmptyLists( categoryOptionCombos ) == 1, "Category option combos size must be larger than 0" );
        }

        // ---------------------------------------------------------------------
        // Init allPeriods, allUnits, allIndicators
        // ---------------------------------------------------------------------

        allIndicators.addAll( dataElements );
        allIndicators.addAll( indicators );
        allIndicators.addAll( dataSets );

        allPeriods.addAll( periods );
        allPeriods.addAll( relativePeriods );
        allPeriods = removeDuplicates( allPeriods );

        Collections.sort( allPeriods, new AscendingPeriodComparator() );
        setNames( allPeriods ); // Set names on periods

        if ( isOrganisationUnitGroupBased() )
        {
            allUnits.addAll( organisationUnitGroups );
        }
        else
        {
            allUnits.addAll( organisationUnits );
            allUnits.addAll( relativeUnits );
            allUnits = removeDuplicates( allUnits );
        }

        columns = new CombinationGenerator<NameableObject>( getArrays( true ) ).getCombinations();
        rows = new CombinationGenerator<NameableObject>( getArrays( false ) ).getCombinations();

        addIfEmpty( columns ); // Allow for all or none crosstab dimensions
        addIfEmpty( rows );

        add( indexColumns, INDICATOR_ID, isDoIndicators() );
        add( indexColumns, PERIOD_ID, isDoPeriods() );
        add( indexColumns, ORGANISATIONUNIT_ID, isDoUnits() );

        add( indexUidColumns, INDICATOR_UID, isDoIndicators() );
        add( indexUidColumns, PERIOD_UID, isDoPeriods() );
        add( indexUidColumns, ORGANISATIONUNIT_UID, isDoUnits() );

        add( indexNameColumns, INDICATOR_NAME, isDoIndicators() );
        add( indexNameColumns, PERIOD_NAME, isDoPeriods() );
        add( indexNameColumns, ORGANISATIONUNIT_NAME, isDoUnits() );

        add( indexCodeColumns, INDICATOR_CODE, isDoIndicators() );
        add( indexCodeColumns, PERIOD_CODE, isDoPeriods() );
        add( indexCodeColumns, ORGANISATIONUNIT_CODE, isDoUnits() );

        add( indexDescriptionColumns, INDICATOR_DESCRIPTION, isDoIndicators() );
        add( indexDescriptionColumns, PERIOD_DESCRIPTION, isDoPeriods() );
        add( indexDescriptionColumns, ORGANISATIONUNIT_DESCRIPTION, isDoUnits() );
    }

    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------
    
    /**
     * Indicates whether this report table can be rendered by the report table
     * service. Stopgap method until report table UI will be replaced by pivot
     * table UI. TODO temporary
     */
    public boolean isReportTable()
    {
        List<String> STANDARD_DIMS = Arrays.asList( DATA_X_DIM_ID, PERIOD_DIM_ID, ORGUNIT_DIM_ID );
        
        return dataElementGroups.isEmpty() && filterDimensions.isEmpty() &&
            STANDARD_DIMS.containsAll( columnDimensions ) && STANDARD_DIMS.containsAll( filterDimensions );            
    }
    
    /**
     * Populates the presentation properties based on the persisted properties.
     */
    public ReportTable populatePresentationProps()
    {
        ListMap<String, DataElementGroup> degs = new ListMap<String, DataElementGroup>();
        
        for ( DataElementGroup group : dataElementGroups )
        {
            degs.putValue( group.getGroupSet().getUid(), group );
        }
        
        ListMap<String, OrganisationUnitGroup> ougs = new ListMap<String, OrganisationUnitGroup>();
        
        for ( OrganisationUnitGroup group : organisationUnitGroups )
        {
            ougs.putValue( group.getGroupSet().getUid(), group );
        }
        
        dataElementGroupSets.clear();
        dataElementGroupSets.putAll( degs );        
        organisationUnitGroupSets.clear();
        organisationUnitGroupSets.putAll( ougs );
        
        return this;
    }
    
    /**
     * Sets the persisted properties based on the presentation properties.
     */
    public ReportTable readPresentationProps()
    {
        dataElementGroups.clear();        
        organisationUnitGroups.clear();
        
        for ( String groupSet : dataElementGroupSets.keySet() )
        {
            dataElementGroups.addAll( dataElementGroupSets.get( groupSet ) );
        }
        
        for ( String groupSet : organisationUnitGroupSets.keySet() )
        {
            organisationUnitGroups.addAll( organisationUnitGroupSets.get( groupSet ) );
        }
        
        return this;
    }
    
    /**
     * Creates a map which contains mappings between the organisation unit
     * identifier and the name of the group this organisation unit is a member
     * of in all of the given group sets for all organisation units in this
     * report table.
     *
     * @param groupSets the collection of organisation unit group sets.
     * @return a map.
     */
    public Map<String, Object> getOrganisationUnitGroupMap( Collection<OrganisationUnitGroupSet> groupSets )
    {
        Map<String, Object> organisationUnitGroupMap = new HashMap<String, Object>();

        for ( OrganisationUnitGroupSet groupSet : groupSets )
        {
            Map<Integer, String> map = new HashMap<Integer, String>();

            for ( NameableObject unit : allUnits )
            {
                if ( unit instanceof OrganisationUnit )
                {
                    map.put( unit.getId(), ((OrganisationUnit) unit).getGroupNameInGroupSet( groupSet ) );
                }
            }

            organisationUnitGroupMap.put( columnEncode( KEY_ORGUNIT_GROUPSET + groupSet.getName() ), map );
        }

        return organisationUnitGroupMap;
    }

    /**
     * Indicates whether this ReportTable is multi-dimensional.
     */
    public boolean isDimensional()
    {
        return categoryCombo != null;
    }

    /**
     * Indicates whether a total column should be included.
     */
    public boolean doTotal()
    {
        return !isDoIndicators() && !isDoPeriods() && !isDoUnits() && isDimensional();
    }

    /**
     * Indicates whether subtotal columns should be included. The category combo
     * of the report table must have more than one category if subtotal columns
     * will contribute.
     */
    public boolean doSubTotals()
    {
        return doTotal() && categoryCombo.getCategories() != null && categoryCombo.getCategories().size() > 1;
    }

    /**
     * Generates a pretty column name based on short-names of the argument
     * objects. Null arguments are ignored in the name.
     */
    public static String getPrettyColumnName( List<NameableObject> objects )
    {
        StringBuffer buffer = new StringBuffer();

        for ( NameableObject object : objects )
        {
            buffer.append( object != null ? (object.getShortName() + SPACE) : EMPTY );
        }

        return buffer.length() > 0 ? buffer.substring( 0, buffer.lastIndexOf( SPACE ) ) : TOTAL_COLUMN_PRETTY_NAME;
    }

    /**
     * Generates a column name based on short-names of the argument objects.
     * Null arguments are ignored in the name.
     */
    public static String getColumnName( List<NameableObject> objects )
    {
        StringBuffer buffer = new StringBuffer();

        for ( NameableObject object : objects )
        {
            if ( object != null && object instanceof Period )
            {
                // -------------------------------------------------------------
                // Periods need static names when crosstab - set on name prop
                // -------------------------------------------------------------

                buffer.append( object.getName() + SEPARATOR );
            }
            else
            {
                buffer.append( object != null ? (object.getShortName() + SEPARATOR) : EMPTY );
            }
        }

        String column = columnEncode( buffer.toString() );

        return column.length() > 0 ? column.substring( 0, column.lastIndexOf( SEPARATOR ) ) : TOTAL_COLUMN_NAME;
    }

    /**
     * Generates a grid identifier based on the internal identifiers of the
     * argument objects.
     */
    public static String getIdentifier( List<NameableObject> objects )
    {
        return getIdentifier( objects, new ArrayList<NameableObject>() );
    }

    /**
     * Generates a grid identifier based on the internal identifiers of the
     * argument objects.
     */
    public static String getIdentifier( List<? extends NameableObject> objects1, List<? extends NameableObject> objects2 )
    {
        List<String> identifiers = new ArrayList<String>();

        for ( NameableObject object : objects1 )
        {
            identifiers.add( getIdentifier( getNameableClass( object.getClass() ), object.getId() ) );
        }

        for ( NameableObject object : objects2 )
        {
            identifiers.add( getIdentifier( getNameableClass( object.getClass() ), object.getId() ) );
        }

        return getIdentifier( identifiers.toArray( SRT ) );
    }

    /**
     * Generates a grid column identifier based on the argument identifiers.
     */
    public static String getIdentifier( List<NameableObject> objects, Class<? extends NameableObject> clazz, int id )
    {
        List<String> identifiers = new ArrayList<String>();

        for ( NameableObject object : objects )
        {
            identifiers.add( getIdentifier( getNameableClass( object.getClass() ), object.getId() ) );
        }

        identifiers.add( getIdentifier( clazz, id ) );

        return getIdentifier( identifiers.toArray( SRT ) );
    }

    /**
     * Generates a grid column identifier based on the argument identifiers.
     */
    public static String getIdentifier( String... identifiers )
    {
        List<String> ids = Arrays.asList( identifiers );

        Collections.sort( ids ); // Sort to remove the significance of order

        return StringUtils.join( ids, SEPARATOR );
    }

    /**
     * Returns a grid identifier based on the argument class and id.
     */
    public static String getIdentifier( Class<? extends NameableObject> clazz, int id )
    {
        return CLASS_ID_MAP.get( clazz ) + id;
    }

    /**
     * Indicates whether the report table contains data elements.
     */
    public boolean hasDataElements()
    {
        return dataElements != null && dataElements.size() > 0;
    }

    /**
     * Indicates whether the report table contains indicators.
     */
    public boolean hasIndicators()
    {
        return indicators != null && indicators.size() > 0;
    }

    /**
     * Indicates whether the report table contains data sets.
     */
    public boolean hasDataSets()
    {
        return dataSets != null && dataSets.size() > 0;
    }

    /**
     * Generates a string which is acceptable as a filename.
     */
    public static String columnEncode( String string )
    {
        if ( string != null )
        {
            string = string.replaceAll( "<", "_lt" );
            string = string.replaceAll( ">", "_gt" );
            string = string.replaceAll( ILLEGAL_FILENAME_CHARS_REGEX, EMPTY );
            string = string.length() > 255 ? string.substring( 0, 255 ) : string;
            string = string.toLowerCase();
        }

        return string;
    }

    /**
     * Returns null-safe sort order, none if null.
     */
    public int sortOrder()
    {
        return sortOrder != null ? sortOrder : NONE;
    }

    /**
     * Returns null-safe top limit, 0 if null;
     */
    public int topLimit()
    {
        return topLimit != null ? topLimit : 0;
    }

    /**
     * Tests whether this report table has report params.
     */
    public boolean hasReportParams()
    {
        return reportParams != null;
    }

    /**
     * Returns the name of the parent organisation unit, or an empty string if null.
     */
    public String getParentOrganisationUnitName()
    {
        return parentOrganisationUnit != null ? parentOrganisationUnit.getName() : EMPTY;
    }

    /**
     * Indicates whether this report table is based on organisation unit groups
     * or the organisation unit hierarchy.
     */
    public boolean isOrganisationUnitGroupBased()
    {
        return organisationUnitGroups != null && organisationUnitGroups.size() > 0;
    }

    /**
     * Indicates whether this report table has relative periods.
     */
    public boolean hasRelativePeriods()
    {
        return relatives != null && !relatives.isEmpty();
    }

    public boolean isDoIndicators()
    {
        return columnDimensions.contains( DATA_X_DIM_ID );
    }

    public void setDoIndicators( boolean doIndicators )
    {
        this.columnDimensions.remove( DATA_X_DIM_ID );
        
        if ( doIndicators )
        {
            this.columnDimensions.add( DATA_X_DIM_ID );
        }
    }
    
    public boolean isDoPeriods()
    {
        return columnDimensions.contains( PERIOD_DIM_ID );
    }

    public void setDoPeriods( boolean doPeriods )
    {
        this.columnDimensions.remove( PERIOD_DIM_ID );
        
        if ( doPeriods )
        {
            this.columnDimensions.add( PERIOD_DIM_ID );
        }
    }
    
    public boolean isDoUnits()
    {
        return columnDimensions.contains( ORGUNIT_DIM_ID );
    }
    
    public void setDoUnits( boolean doUnits )
    {
        this.columnDimensions.remove( ORGUNIT_DIM_ID );
        
        if ( doUnits )
        {
            this.columnDimensions.add( ORGUNIT_DIM_ID );
        }
    }

    public void removeAllDataElements()
    {
        dataElements.clear();
    }

    public void removeAllIndicators()
    {
        indicators.clear();
    }

    public void removeAllDataSets()
    {
        dataSets.clear();
    }

    public void removeAllPeriods()
    {
        periods.clear();
    }

    public void removeAllOrganisationUnits()
    {
        organisationUnits.clear();
    }

    public void removeAllDataElementGroups()
    {
        dataElementGroups.clear();
    }
    
    public void removeAllOrganisationUnitGroups()
    {
        organisationUnitGroups.clear();
    }
    
    public void removeAllColumnDimensions()
    {
        columnDimensions.clear();
    }
    
    public void removeAllRowDimensions()
    {
        rowDimensions.clear();
    }
    
    public void removeAllFilterDimensions()
    {
        filterDimensions.clear();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private NameableObject[][] getArrays( boolean crosstab )
    {
        List<NameableObject[]> arrays = new ArrayList<NameableObject[]>();

        if ( (isDoIndicators() && crosstab) || (!isDoIndicators() && !crosstab) )
        {
            arrays.add( allIndicators.toArray( IRT ) );
        }

        if ( (isDoPeriods() && crosstab) || (!isDoPeriods() && !crosstab) )
        {
            arrays.add( allPeriods.toArray( IRT ) );
        }

        if ( (isDoUnits() && crosstab) || (!isDoUnits() && !crosstab) )
        {
            arrays.add( allUnits.toArray( IRT ) );
        }

        if ( isDimensional() && crosstab ) // Must be crosstab if exists
        {
            arrays.add( categoryOptionCombos.toArray( IRT ) );
        }

        return arrays.toArray( new NameableObject[0][] );
    }

    /**
     * Adds an empty list of NameableObjects to the given list if empty.
     */
    private static void addIfEmpty( List<List<NameableObject>> list )
    {
        if ( list != null && list.size() == 0 )
        {
            list.add( Arrays.asList( new NameableObject[0] ) );
        }
    }

    /**
     * Returns the number of empty lists among the argument lists.
     */
    private static int nonEmptyLists( List<?>... lists )
    {
        int nonEmpty = 0;

        for ( List<?> list : lists )
        {
            if ( list != null && list.size() > 0 )
            {
                ++nonEmpty;
            }
        }

        return nonEmpty;
    }

    /**
     * Sets the name and short name properties on the given Periods which don't
     * have the name property already set.
     */
    private void setNames( List<Period> periods )
    {
        for ( Period period : periods )
        {
            if ( period.getName() == null ) // Crosstabulated relative periods
            {
                // -------------------------------------------------------------
                // Static periods + index relative periods
                // -------------------------------------------------------------

                period.setName( i18nFormat.formatPeriod( period ) );
                period.setShortName( i18nFormat.formatPeriod( period ) );
            }
        }
    }

    /**
     * Adds the given object to the given list if not skip argument is true.
     */
    private static <T> void add( List<T> list, T object, boolean skip )
    {
        if ( !skip )
        {
            list.add( object );
        }
    }

    /**
     * Removes duplicates from the given list while maintaining the order.
     */
    private static <T> List<T> removeDuplicates( List<T> list )
    {
        final List<T> temp = new ArrayList<T>( list );
        list.clear();

        for ( T object : temp )
        {
            if ( !list.contains( object ) )
            {
                list.add( object );
            }
        }

        return list;
    }

    /**
     * Supportive method.
     */
    private static void verify( boolean expression, String falseMessage )
    {
        if ( !expression )
        {
            throw new IllegalStateException( falseMessage );
        }
    }
    
    /**
     * Gets the real Nameable class in case of a proxy.
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends NameableObject> getNameableClass( Class<?> clazz )
    {
        while ( clazz != null )
        {       
            if ( BaseNameableObject.class.equals( clazz.getSuperclass() ) )
            {
                return (Class<? extends NameableObject>) clazz;
            }
            
            clazz = clazz.getSuperclass();
        }
        
        throw new IllegalStateException( "Class is not a Nameable object: " + clazz );
    }    

    // -------------------------------------------------------------------------
    // Get- and set-methods for persisted properties
    // -------------------------------------------------------------------------

    @Override
    public boolean haveUniqueNames()
    {
        return false;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isRegression()
    {
        return regression;
    }

    public void setRegression( boolean regression )
    {
        this.regression = regression;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isCumulative()
    {
        return cumulative;
    }

    public void setCumulative( boolean cumulative )
    {
        this.cumulative = cumulative;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "dataElements", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "dataElement", namespace = DxfNamespaces.DXF_2_0)
    public List<DataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( List<DataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "indicators", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "indicator", namespace = DxfNamespaces.DXF_2_0)
    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    public void setIndicators( List<Indicator> indicators )
    {
        this.indicators = indicators;
    }

    @JsonProperty
    @JsonSerialize( contentUsing = JacksonPeriodSerializer.class )
    @JsonDeserialize( contentUsing = JacksonPeriodDeserializer.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "periods", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "period", namespace = DxfNamespaces.DXF_2_0)
    public List<Period> getPeriods()
    {
        return periods;
    }

    public void setPeriods( List<Period> periods )
    {
        this.periods = periods;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "dataSets", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "dataSet", namespace = DxfNamespaces.DXF_2_0)
    public List<DataSet> getDataSets()
    {
        return dataSets;
    }

    public void setDataSets( List<DataSet> dataSets )
    {
        this.dataSets = dataSets;
    }

    @JsonProperty( value = "organisationUnits" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "organisationUnits", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "organisationUnit", namespace = DxfNamespaces.DXF_2_0)
    public List<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    public void setOrganisationUnits( List<OrganisationUnit> units )
    {
        this.organisationUnits = units;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "dataElementGroups", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "dataElementGroup", namespace = DxfNamespaces.DXF_2_0)
    public List<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }

    public void setDataElementGroups( List<DataElementGroup> dataElementGroups )
    {
        this.dataElementGroups = dataElementGroups;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "organisationUnitGroups", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "organisationUnitGroup", namespace = DxfNamespaces.DXF_2_0)
    public List<OrganisationUnitGroup> getOrganisationUnitGroups()
    {
        return organisationUnitGroups;
    }

    public void setOrganisationUnitGroups( List<OrganisationUnitGroup> organisationUnitGroups )
    {
        this.organisationUnitGroups = organisationUnitGroups;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "columnDimensions", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "column", namespace = DxfNamespaces.DXF_2_0)
    public List<String> getColumnDimensions()
    {
        return columnDimensions;
    }

    public void setColumnDimensions( List<String> columnDimensions )
    {
        this.columnDimensions = columnDimensions;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "rowDimensions", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "row", namespace = DxfNamespaces.DXF_2_0)
    public List<String> getRowDimensions()
    {
        return rowDimensions;
    }

    public void setRowDimensions( List<String> rowDimensions )
    {
        this.rowDimensions = rowDimensions;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "filterDimensions", namespace = DxfNamespaces.DXF_2_0)
    @JacksonXmlProperty( localName = "filter", namespace = DxfNamespaces.DXF_2_0)
    public List<String> getFilterDimensions()
    {
        return filterDimensions;
    }

    public void setFilterDimensions( List<String> filterDimensions )
    {
        this.filterDimensions = filterDimensions;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public DataElementCategoryCombo getCategoryCombo()
    {
        return categoryCombo;
    }

    public void setCategoryCombo( DataElementCategoryCombo categoryCombo )
    {
        this.categoryCombo = categoryCombo;
    }

    @JsonProperty( value = "relativePeriods" )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public RelativePeriods getRelatives()
    {
        return relatives;
    }

    public void setRelatives( RelativePeriods relatives )
    {
        this.relatives = relatives;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public ReportParams getReportParams()
    {
        return reportParams;
    }

    public void setReportParams( ReportParams reportParams )
    {
        this.reportParams = reportParams;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( Integer sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Integer getTopLimit()
    {
        return topLimit;
    }

    public void setTopLimit( Integer topLimit )
    {
        this.topLimit = topLimit;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isTotals()
    {
        return totals;
    }

    public void setTotals( boolean totals )
    {
        this.totals = totals;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isSubtotals()
    {
        return subtotals;
    }

    public void setSubtotals( boolean subtotals )
    {
        this.subtotals = subtotals;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isHideEmptyRows()
    {
        return hideEmptyRows;
    }

    public void setHideEmptyRows( boolean hideEmptyRows )
    {
        this.hideEmptyRows = hideEmptyRows;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getDigitGroupSeparator()
    {
        return digitGroupSeparator;
    }

    public void setDigitGroupSeparator( String digitGroupSeparator )
    {
        this.digitGroupSeparator = digitGroupSeparator;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getDisplayDensity()
    {
        return displayDensity;
    }

    public void setDisplayDensity( String displayDensity )
    {
        this.displayDensity = displayDensity;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public String getFontSize()
    {
        return fontSize;
    }

    public void setFontSize( String fontSize )
    {
        this.fontSize = fontSize;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isUserOrganisationUnit()
    {
        return userOrganisationUnit;
    }

    public void setUserOrganisationUnit( boolean userOrganisationUnit )
    {
        this.userOrganisationUnit = userOrganisationUnit;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public boolean isUserOrganisationUnitChildren()
    {
        return userOrganisationUnitChildren;
    }

    public void setUserOrganisationUnitChildren( boolean userOrganisationUnitChildren )
    {
        this.userOrganisationUnitChildren = userOrganisationUnitChildren;
    }

    // -------------------------------------------------------------------------
    // Get- and set-methods for presentation properties
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( using = JacksonMapListIdentifiableObjectSerializer.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Map<String, List<DataElementGroup>> getDataElementGroupSets()
    {
        return dataElementGroupSets;
    }

    public void setDataElementGroupSets( Map<String, List<DataElementGroup>> dataElementGroupSets )
    {
        this.dataElementGroupSets = dataElementGroupSets;
    }

    @JsonProperty
    @JsonSerialize( using = JacksonMapListIdentifiableObjectSerializer.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0)
    public Map<String, List<OrganisationUnitGroup>> getOrganisationUnitGroupSets()
    {
        return organisationUnitGroupSets;
    }

    public void setOrganisationUnitGroupSets( Map<String, List<OrganisationUnitGroup>> organisationUnitGroupSets )
    {
        this.organisationUnitGroupSets = organisationUnitGroupSets;
    }

    // -------------------------------------------------------------------------
    // Get- and set-methods for transient properties
    // -------------------------------------------------------------------------

    @JsonIgnore
    public List<Period> getRelativePeriods()
    {
        return relativePeriods;
    }

    @JsonIgnore
    public void setRelativePeriods( List<Period> relativePeriods )
    {
        this.relativePeriods = relativePeriods;
    }

    @JsonIgnore
    public List<Period> getAllPeriods()
    {
        return allPeriods;
    }

    @JsonIgnore
    public List<OrganisationUnit> getRelativeUnits()
    {
        return relativeUnits;
    }

    @JsonIgnore
    public void setRelativeUnits( List<OrganisationUnit> relativeUnits )
    {
        this.relativeUnits = relativeUnits;
    }

    @JsonIgnore
    public List<NameableObject> getAllUnits()
    {
        return allUnits;
    }

    @JsonIgnore
    public I18nFormat getI18nFormat()
    {
        return i18nFormat;
    }

    public void setI18nFormat( I18nFormat format )
    {
        i18nFormat = format;
    }

    @JsonIgnore
    public String getReportingPeriodName()
    {
        return reportingPeriodName;
    }

    @JsonIgnore
    public void setReportingPeriodName( String reportingPeriodName )
    {
        this.reportingPeriodName = reportingPeriodName;
    }

    @JsonIgnore
    public List<List<NameableObject>> getColumns()
    {
        return columns;
    }

    @JsonIgnore
    public List<List<NameableObject>> getRows()
    {
        return rows;
    }

    @JsonIgnore
    public List<String> getIndexColumns()
    {
        return indexColumns;
    }

    @JsonIgnore
    public List<String> getIndexUidColumns()
    {
        return indexUidColumns;
    }

    @JsonIgnore
    public List<String> getIndexNameColumns()
    {
        return indexNameColumns;
    }

    @JsonIgnore
    public List<String> getIndexCodeColumns()
    {
        return indexCodeColumns;
    }

    @JsonIgnore
    public List<String> getIndexDescriptionColumns()
    {
        return indexDescriptionColumns;
    }

    @JsonIgnore
    public OrganisationUnit getParentOrganisationUnit()
    {
        return parentOrganisationUnit;
    }

    @JsonIgnore
    public void setParentOrganisationUnit( OrganisationUnit parentOrganisationUnit )
    {
        this.parentOrganisationUnit = parentOrganisationUnit;
    }

    @JsonIgnore
    public List<DataElementCategoryOptionCombo> getCategoryOptionCombos()
    {
        return categoryOptionCombos;
    }

    @JsonIgnore
    public void setCategoryOptionCombos( List<DataElementCategoryOptionCombo> categoryOptionCombos )
    {
        this.categoryOptionCombos = categoryOptionCombos;
    }

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        super.mergeWith( other );

        if ( other.getClass().isInstance( this ) )
        {
            ReportTable reportTable = (ReportTable) other;

            regression = reportTable.isRegression();
            cumulative = reportTable.isCumulative();
            categoryCombo = reportTable.getCategoryCombo() == null ? categoryCombo : reportTable.getCategoryCombo();
            relatives = reportTable.getRelatives() == null ? relatives : reportTable.getRelatives();
            reportParams = reportTable.getReportParams() == null ? reportParams : reportTable.getReportParams();
            sortOrder = reportTable.getSortOrder() == null ? sortOrder : reportTable.getSortOrder();
            topLimit = reportTable.getTopLimit() == null ? topLimit : reportTable.getTopLimit();
            totals = reportTable.isTotals();
            subtotals = reportTable.isSubtotals();
            hideEmptyRows = reportTable.isHideEmptyRows();
            digitGroupSeparator = reportTable.getDigitGroupSeparator();
            displayDensity = reportTable.getDisplayDensity();
            fontSize = reportTable.getFontSize();
            userOrganisationUnit = reportTable.isUserOrganisationUnit();
            userOrganisationUnitChildren = reportTable.isUserOrganisationUnitChildren();

            removeAllDataElements();
            dataElements.addAll( reportTable.getDataElements() );

            removeAllIndicators();
            indicators.addAll( reportTable.getIndicators() );

            removeAllDataSets();
            dataSets.addAll( reportTable.getDataSets() );

            removeAllOrganisationUnits();
            organisationUnits.addAll( reportTable.getOrganisationUnits() );

            removeAllPeriods();
            periods.addAll( reportTable.getPeriods() );

            removeAllDataElementGroups();
            dataElementGroups.addAll( reportTable.getDataElementGroups() );
            
            removeAllOrganisationUnitGroups();
            organisationUnitGroups.addAll( reportTable.getOrganisationUnitGroups() );
            
            removeAllColumnDimensions();
            columnDimensions.addAll( reportTable.getColumnDimensions() );
            
            removeAllRowDimensions();
            rowDimensions.addAll( reportTable.getRowDimensions() );
            
            removeAllFilterDimensions();
            filterDimensions.addAll( reportTable.getFilterDimensions() );
        }
    }

    @JsonProperty
    @JsonView({ DetailedView.class, ExportView.class })
    public Map<String, String> getParentGraphMap()
    {
        return parentGraphMap;
    }

    public void setParentGraphMap( Map<String, String> parentGraphMap )
    {
        this.parentGraphMap = parentGraphMap;
    }
}
