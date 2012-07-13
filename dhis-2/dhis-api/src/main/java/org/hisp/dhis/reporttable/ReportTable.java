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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.StringUtils;
import org.hisp.dhis.common.*;
import org.hisp.dhis.common.adapter.JacksonPeriodDeserializer;
import org.hisp.dhis.common.adapter.JacksonPeriodSerializer;
import org.hisp.dhis.common.annotation.Scanned;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.period.comparator.AscendingPeriodComparator;

import java.util.*;

/**
 * The ReportTable object represents a customizable database table. It has
 * features like crosstabulation, relative periods, parameters, and display
 * columns.
 *
 * @author Lars Helge Overland
 * @version $Id$
 */
@JacksonXmlRootElement( localName = "reportTable", namespace = Dxf2Namespace.NAMESPACE )
public class ReportTable
    extends BaseIdentifiableObject
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 5618655666320890565L;

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
    private List<OrganisationUnit> units = new ArrayList<OrganisationUnit>();

    /**
     * The list of OrganisationUnitGroups the ReportTable contains.
     */
    @Scanned
    private List<OrganisationUnitGroup> organisationUnitGroups = new ArrayList<OrganisationUnitGroup>();

    /**
     * The DataElementCategoryCombo for the ReportTable.
     */
    private DataElementCategoryCombo categoryCombo;

    /**
     * Whether to crosstabulate on the Indicator dimension, which also
     * represents DataElements and DataSets.
     */
    private boolean doIndicators;

    /**
     * Whether to crosstabulate on the Period dimension.
     */
    private boolean doPeriods;

    /**
     * Whether to crosstabulate on the OrganisationUnit dimension.
     */
    private boolean doUnits;

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
     * Inidicates whether the table should be limited from top by this value.
     */
    private Integer topLimit;

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
     * @param name            the name.
     * @param dataElements    the data elements.
     * @param indicators      the indicators.
     * @param dataSets        the datasets.
     * @param periods         the periods. These periods cannot have the name property
     *                        set.
     * @param relativePeriods the relative periods. These periods must have the
     *                        name property set. Not persisted.
     * @param units           the organisation units.
     * @param relativeUnits   the organisation units. Not persisted.
     * @param doIndicators    indicating whether indicators should be
     *                        crosstabulated.
     * @param doPeriods       indicating whether periods should be crosstabulated.
     * @param doUnits         indicating whether organisation units should be
     *                        crosstabulated.
     * @param relatives       the relative periods.
     * @param i18nFormat      the i18n format. Not persisted.
     */
    public ReportTable( String name, List<DataElement> dataElements, List<Indicator> indicators,
                        List<DataSet> dataSets, List<Period> periods, List<Period> relativePeriods, List<OrganisationUnit> units,
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
        this.units = units;
        this.relativeUnits = relativeUnits;
        this.organisationUnitGroups = organisationUnitGroups;
        this.categoryCombo = categoryCombo;
        this.doIndicators = doIndicators;
        this.doPeriods = doPeriods;
        this.doUnits = doUnits;
        this.relatives = relatives;
        this.reportParams = reportParams;
        this.i18nFormat = i18nFormat;
        this.reportingPeriodName = reportingPeriodName;
    }

    // -------------------------------------------------------------------------
    // Init
    // -------------------------------------------------------------------------

    public void init()
    {
        verify( nonEmptyLists( dataElements, indicators, dataSets ) > 0,
            "Must contain dataelements, indicators or datasets" );
        verify( nonEmptyLists( periods, relativePeriods ) > 0, "Must contain periods or relative periods" );
        verify( nonEmptyLists( units, relativeUnits, organisationUnitGroups ) > 0,
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
            allUnits.addAll( units );
            allUnits.addAll( relativeUnits );
            allUnits = removeDuplicates( allUnits );
        }

        columns = new CombinationGenerator<NameableObject>( getArrays( true ) ).getCombinations();
        rows = new CombinationGenerator<NameableObject>( getArrays( false ) ).getCombinations();

        addIfEmpty( columns ); // Allow for all or none crosstab dimensions
        addIfEmpty( rows );

        add( indexColumns, INDICATOR_ID, doIndicators );
        add( indexColumns, PERIOD_ID, doPeriods );
        add( indexColumns, ORGANISATIONUNIT_ID, doUnits );

        add( indexUidColumns, INDICATOR_UID, doIndicators );
        add( indexUidColumns, PERIOD_UID, doPeriods );
        add( indexUidColumns, ORGANISATIONUNIT_UID, doUnits );

        add( indexNameColumns, INDICATOR_NAME, doIndicators );
        add( indexNameColumns, PERIOD_NAME, doPeriods );
        add( indexNameColumns, ORGANISATIONUNIT_NAME, doUnits );

        add( indexCodeColumns, INDICATOR_CODE, doIndicators );
        add( indexCodeColumns, PERIOD_CODE, doPeriods );
        add( indexCodeColumns, ORGANISATIONUNIT_CODE, doUnits );

        add( indexDescriptionColumns, INDICATOR_DESCRIPTION, doIndicators );
        add( indexDescriptionColumns, PERIOD_DESCRIPTION, doPeriods );
        add( indexDescriptionColumns, ORGANISATIONUNIT_DESCRIPTION, doUnits );
    }

    // -------------------------------------------------------------------------
    // Public methods
    // -------------------------------------------------------------------------

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
            identifiers.add( getIdentifier( object.getClass(), object.getId() ) );
        }

        for ( NameableObject object : objects2 )
        {
            identifiers.add( getIdentifier( object.getClass(), object.getId() ) );
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
            identifiers.add( getIdentifier( object.getClass(), object.getId() ) );
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
        units.clear();
    }

    public void removeAllOrganisationUnitGroups()
    {
        organisationUnitGroups.clear();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private NameableObject[][] getArrays( boolean crosstab )
    {
        List<NameableObject[]> arrays = new ArrayList<NameableObject[]>();

        if ( (doIndicators && crosstab) || (!doIndicators && !crosstab) )
        {
            arrays.add( allIndicators.toArray( IRT ) );
        }

        if ( (doPeriods && crosstab) || (!doPeriods && !crosstab) )
        {
            arrays.add( allPeriods.toArray( IRT ) );
        }

        if ( (doUnits && crosstab) || (!doUnits && !crosstab) )
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

    // -------------------------------------------------------------------------
    // Equals and hashCode
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int PRIME = 31;

        int result = 1;

        result = PRIME * result + ((name == null) ? 0 : name.hashCode());

        return result;
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

        final ReportTable other = (ReportTable) object;

        return name.equals( other.getName() );
    }

    // -------------------------------------------------------------------------
    // Get- and set-methods for persisted properties
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlElementWrapper( localName = "dataElements", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "dataElement", namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlElementWrapper( localName = "indicators", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "indicator", namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlElementWrapper( localName = "periods", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "period", namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlElementWrapper( localName = "dataSets", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "dataSet", namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlElementWrapper( localName = "organisationUnits", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "organisationUnit", namespace = Dxf2Namespace.NAMESPACE )
    public List<OrganisationUnit> getUnits()
    {
        return units;
    }

    public void setUnits( List<OrganisationUnit> units )
    {
        this.units = units;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlElementWrapper( localName = "organisationUnitGroups", namespace = Dxf2Namespace.NAMESPACE )
    @JacksonXmlProperty( localName = "organisationUnitGroup", namespace = Dxf2Namespace.NAMESPACE )
    public List<OrganisationUnitGroup> getOrganisationUnitGroups()
    {
        return organisationUnitGroups;
    }

    public void setOrganisationUnitGroups( List<OrganisationUnitGroup> organisationUnitGroups )
    {
        this.organisationUnitGroups = organisationUnitGroups;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public DataElementCategoryCombo getCategoryCombo()
    {
        return categoryCombo;
    }

    public void setCategoryCombo( DataElementCategoryCombo categoryCombo )
    {
        this.categoryCombo = categoryCombo;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isDoIndicators()
    {
        return doIndicators;
    }

    public void setDoIndicators( boolean doIndicators )
    {
        this.doIndicators = doIndicators;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isDoPeriods()
    {
        return doPeriods;
    }

    public void setDoPeriods( boolean doPeriods )
    {
        this.doPeriods = doPeriods;
    }

    @JsonProperty
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public boolean isDoUnits()
    {
        return doUnits;
    }

    public void setDoUnits( boolean doUnits )
    {
        this.doUnits = doUnits;
    }

    @JsonProperty( value = "relativePeriods" )
    @JsonView( {DetailedView.class, ExportView.class} )
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
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
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public ReportParams getReportParams()
    {
        return reportParams;
    }

    public void setReportParams( ReportParams reportParams )
    {
        this.reportParams = reportParams;
    }

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
    @JacksonXmlProperty( namespace = Dxf2Namespace.NAMESPACE )
    public Integer getTopLimit()
    {
        return topLimit;
    }

    public void setTopLimit( Integer topLimit )
    {
        this.topLimit = topLimit;
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
            doIndicators = reportTable.isDoIndicators();
            doPeriods = reportTable.isDoPeriods();
            doUnits = reportTable.isDoUnits();
            relatives = reportTable.getRelatives() == null ? relatives : reportTable.getRelatives();
            reportParams = reportTable.getReportParams() == null ? reportParams : reportTable.getReportParams();
            sortOrder = reportTable.getSortOrder() == null ? sortOrder : reportTable.getSortOrder();
            topLimit = reportTable.getTopLimit() == null ? topLimit : reportTable.getTopLimit();

            removeAllOrganisationUnitGroups();
            organisationUnitGroups.addAll( reportTable.getOrganisationUnitGroups() );

            removeAllOrganisationUnits();
            units.addAll( reportTable.getUnits() );

            removeAllPeriods();
            periods.addAll( reportTable.getPeriods() );

            removeAllDataSets();
            dataSets.addAll( reportTable.getDataSets() );

            removeAllIndicators();
            indicators.addAll( reportTable.getIndicators() );

            removeAllDataElements();
            dataElements.addAll( reportTable.getDataElements() );
        }
    }
}
