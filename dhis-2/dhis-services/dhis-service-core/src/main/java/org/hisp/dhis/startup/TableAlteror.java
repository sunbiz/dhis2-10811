package org.hisp.dhis.startup;

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

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.amplecode.quick.StatementHolder;
import org.amplecode.quick.StatementManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.system.startup.AbstractStartupRoutine;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class TableAlteror
    extends AbstractStartupRoutine
{
    private static final Log log = LogFactory.getLog( TableAlteror.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------

    @Transactional
    public void execute()
    {
        // ---------------------------------------------------------------------
        // Drop outdated tables
        // ---------------------------------------------------------------------

        executeSql( "DROP TABLE categoryoptioncomboname" );
        executeSql( "DROP TABLE orgunitgroupsetstructure" );
        executeSql( "DROP TABLE orgunitstructure" );
        executeSql( "DROP TABLE orgunithierarchystructure" );
        executeSql( "DROP TABLE orgunithierarchy" );
        executeSql( "DROP TABLE datavalueaudit" );
        executeSql( "DROP TABLE columnorder" );
        executeSql( "DROP TABLE roworder" );
        executeSql( "DROP TABLE sectionmembers" );
        executeSql( "DROP TABLE reporttable_categoryoptioncombos" );
        executeSql( "DROP TABLE dashboardcontent_datamartexports" );
        executeSql( "DROP TABLE customvalue" );
        executeSql( "DROP TABLE reporttable_displaycolumns" );
        executeSql( "DROP TABLE reportreporttables" );
        executeSql( "DROP TABLE frequencyoverrideassociation" );
        executeSql( "DROP TABLE dataelement_dataelementgroupsetmembers" );
        executeSql( "DROP TABLE dashboardcontent_olapurls" );
        executeSql( "DROP TABLE olapurl" );
        executeSql( "DROP TABLE target" );
        executeSql( "DROP TABLE calculateddataelement" );
        executeSql( "DROP TABLE systemsequence" );
        executeSql( "DROP TABLE reporttablecolumn" );
        executeSql( "DROP TABLE datamartexport" );
        executeSql( "DROP TABLE datamartexportdataelements" );
        executeSql( "DROP TABLE datamartexportindicators" );
        executeSql( "DROP TABLE datamartexportorgunits" );
        executeSql( "DROP TABLE datamartexportperiods" );
        executeSql( "DROP TABLE datasetlockedperiods" );
        executeSql( "DROP TABLE datasetlocksource" );
        executeSql( "DROP TABLE datasetlock" );
        executeSql( "DROP TABLE datasetlockexceptions" );
        executeSql( "DROP TABLE indicator_indicatorgroupsetmembers" );
        executeSql( "ALTER TABLE dataelementcategoryoption drop column categoryid" );
        executeSql( "ALTER TABLE reporttable DROP column dimension_type" );
        executeSql( "ALTER TABLE reporttable DROP column dimensiontype" );
        executeSql( "ALTER TABLE reporttable DROP column tablename" );
        executeSql( "ALTER TABLE reporttable DROP column existingtablename" );
        executeSql( "ALTER TABLE reporttable DROP column docategoryoptioncombos" );
        executeSql( "ALTER TABLE reporttable DROP column mode" );
        executeSql( "ALTER TABLE categoryoptioncombo DROP COLUMN displayorder" );
        executeSql( "ALTER TABLE dataelementcategoryoption DROP COLUMN shortname" );
        executeSql( "ALTER TABLE section DROP COLUMN label" );
        executeSql( "ALTER TABLE section DROP COLUMN title" );
        executeSql( "ALTER TABLE organisationunit DROP COLUMN polygoncoordinates" );
        executeSql( "ALTER TABLE indicator DROP COLUMN extendeddataelementid" );
        executeSql( "ALTER TABLE indicator DROP COLUMN numeratoraggregationtype" );
        executeSql( "ALTER TABLE indicator DROP COLUMN denominatoraggregationtype" );
        executeSql( "ALTER TABLE dataset DROP COLUMN locked" );
        
        // remove relative period type
        executeSql( "DELETE FROM period WHERE periodtypeid=(select periodtypeid from periodtype where name in ( 'Survey', 'OnChange', 'Relative' ))" );
        executeSql( "DELETE FROM periodtype WHERE name in ( 'Survey', 'OnChange', 'Relative' )" );

        // mapping
        executeSql( "DROP TABLE maporganisationunitrelation" );
        executeSql( "ALTER TABLE mapview DROP COLUMN mapid" );
        executeSql( "ALTER TABLE mapview DROP COLUMN startdate" );
        executeSql( "ALTER TABLE mapview DROP COLUMN enddate" );
        executeSql( "ALTER TABLE mapview DROP COLUMN mapsource" );
        executeSql( "ALTER TABLE mapview DROP COLUMN mapsourcetype" );
        executeSql( "ALTER TABLE mapview DROP COLUMN mapdatetype" );
        executeSql( "DROP TABLE map" );
        executeSql( "DELETE FROM systemsetting WHERE name = 'longitude'" );
        executeSql( "DELETE FROM systemsetting WHERE name = 'latitude'" );
        
        executeSql( "ALTER TABLE map DROP CONSTRAINT fk_map_organisationunitid" );
        executeSql( "ALTER TABLE map DROP COLUMN organisationunitid" );
        executeSql( "ALTER TABLE map DROP COLUMN longitude" );
        executeSql( "ALTER TABLE map DROP COLUMN latitude" );
        executeSql( "ALTER TABLE map DROP COLUMN zoom" );
        executeSql( "ALTER TABLE maplayer DROP CONSTRAINT maplayer_mapsource_key" );
        executeSql( "ALTER TABLE maplayer DROP COLUMN mapsource" );
        executeSql( "ALTER TABLE maplayer DROP COLUMN mapsourcetype" );
        executeSql( "ALTER TABLE maplayer DROP COLUMN layer" );

        // extended data element
        executeSql( "ALTER TABLE dataelement DROP CONSTRAINT fk_dataelement_extendeddataelementid" );
        executeSql( "ALTER TABLE dataelement DROP COLUMN extendeddataelementid" );
        executeSql( "ALTER TABLE indicator DROP CONSTRAINT fk_indicator_extendeddataelementid" );
        executeSql( "ALTER TABLE indicator DROP COLUMN extendeddataelementid" );
        executeSql( "DROP TABLE extendeddataelement" );
        
        executeSql( "ALTER TABLE organisationunit DROP COLUMN hasPatients" );
        
        executeSql( "update dataelement set texttype='text' where valuetype='string'" );
        
        // ---------------------------------------------------------------------
        // Update tables for dimensional model
        // ---------------------------------------------------------------------

        // categories_categoryoptions
        // set to 0 temporarily
        int c1 = executeSql( "UPDATE categories_categoryoptions SET sort_order=0 WHERE sort_order is NULL OR sort_order=0" );
        if ( c1 > 0 )
        {
            updateSortOrder( "categories_categoryoptions", "categoryid", "categoryoptionid" );
        }
        executeSql( "ALTER TABLE categories_categoryoptions DROP CONSTRAINT categories_categoryoptions_pkey" );
        executeSql( "ALTER TABLE categories_categoryoptions ADD CONSTRAINT categories_categoryoptions_pkey PRIMARY KEY (categoryid, sort_order)" );

        // categorycombos_categories
        // set to 0 temporarily
        int c2 = executeSql( "update categorycombos_categories SET sort_order=0 where sort_order is NULL OR sort_order=0" );
        if ( c2 > 0 )
        {
            updateSortOrder( "categorycombos_categories", "categorycomboid", "categoryid" );
        }
        executeSql( "ALTER TABLE categorycombos_categories DROP CONSTRAINT categorycombos_categories_pkey" );
        executeSql( "ALTER TABLE categorycombos_categories ADD CONSTRAINT categorycombos_categories_pkey PRIMARY KEY (categorycomboid, sort_order)" );

        // categorycombos_optioncombos
        executeSql( "ALTER TABLE categorycombos_optioncombos DROP CONSTRAINT categorycombos_optioncombos_pkey" );
        executeSql( "ALTER TABLE categorycombos_optioncombos ADD CONSTRAINT categorycombos_optioncombos_pkey PRIMARY KEY (categoryoptioncomboid)" );
        executeSql( "ALTER TABLE categorycombos_optioncombos DROP CONSTRAINT fk4bae70f697e49675" );

        // categoryoptioncombos_categoryoptions
        // set to 0 temporarily
        int c3 = executeSql( "update categoryoptioncombos_categoryoptions SET sort_order=0 where sort_order is NULL OR sort_order=0" );
        if ( c3 > 0 )
        {
            updateSortOrder( "categoryoptioncombos_categoryoptions", "categoryoptioncomboid", "categoryoptionid" );
        }
        executeSql( "ALTER TABLE categoryoptioncombos_categoryoptions DROP CONSTRAINT categoryoptioncombos_categoryoptions_pkey" );
        executeSql( "ALTER TABLE categoryoptioncombos_categoryoptions ADD CONSTRAINT categoryoptioncombos_categoryoptions_pkey PRIMARY KEY (categoryoptioncomboid, sort_order)" );

        // dataelementcategoryoption
        executeSql( "ALTER TABLE dataelementcategoryoption DROP CONSTRAINT fk_dataelement_categoryid" );        
        executeSql( "ALTER TABLE dataelementcategoryoption DROP CONSTRAINT dataelementcategoryoption_shortname_key" );

        // minmaxdataelement query index
        executeSql( "CREATE INDEX index_minmaxdataelement ON minmaxdataelement( sourceid, dataelementid, categoryoptioncomboid )" );

        // add mandatory boolean field to patientattribute
        executeSql( "ALTER TABLE patientattribute ADD mandatory bool" );
        
        if ( executeSql( "ALTER TABLE patientattribute ADD groupby bool" ) >= 0 )
        {
            executeSql( "UPDATE patientattribute SET groupby=false" );
        }
        
        // update periodType field to ValidationRule
        executeSql( "UPDATE validationrule SET periodtypeid = (SELECT periodtypeid FROM periodtype WHERE name='Monthly') WHERE periodtypeid is null" );

        // update dataelement.domainTypes of which values is null
        executeSql( "UPDATE dataelement SET domaintype='aggregate' WHERE domaintype is null" );
        
        // set varchar to text
        executeSql( "ALTER TABLE dataelement ALTER description TYPE text" );
        executeSql( "ALTER TABLE indicator ALTER description TYPE text" );
        executeSql( "ALTER TABLE datadictionary ALTER description TYPE text" );
        executeSql( "ALTER TABLE validationrule ALTER description TYPE text" );
        executeSql( "ALTER TABLE expression ALTER expression TYPE text" );
        executeSql( "ALTER TABLE translation ALTER value TYPE text" );
        executeSql( "ALTER TABLE organisationunit ALTER comment TYPE text" );

        // orgunit shortname uniqueness
        executeSql( "ALTER TABLE organisationunit DROP CONSTRAINT organisationunit_shortname_key" );

        // update dataset-dataentryform association and programstage-cde association
        if ( updateDataSetAssociation() && updateProgramStageAssociation() )
        {
            // delete table dataentryformassociation
            executeSql( "DROP TABLE dataentryformassociation" );
        }

        executeSql( "ALTER TABLE section DROP CONSTRAINT section_name_key" );
        executeSql( "UPDATE patientattribute set inheritable=false where inheritable is null" );
        executeSql( "UPDATE dataelement SET numbertype='number' where numbertype is null and valuetype='int'" );

       // revert prepare aggregate*Value tables for offline diffs

        executeSql( "ALTER TABLE aggregateddatavalue DROP COLUMN modified");
        executeSql( "ALTER TABLE aggregatedindicatorvalue DROP COLUMN modified ");
        executeSql( "UPDATE indicatortype SET indicatornumber=false WHERE indicatornumber is null" );

        // program
        
        executeSql( "ALTER TABLE programinstance ALTER COLUMN patientid DROP NOT NULL" );

        // migrate charts from dimension to category, series, filter
        
        executeSql( "UPDATE chart SET series='PERIOD', category='DATA', filter='ORGANISATIONUNIT' WHERE dimension='indicator'" );
        executeSql( "UPDATE chart SET series='DATA', category='ORGANISATIONUNIT', filter='PERIOD' WHERE dimension='organisationUnit'" );
        executeSql( "UPDATE chart SET series='PERIOD', category='DATA', filter='ORGANISATIONUNIT' WHERE dimension='dataElement_period'" );
        executeSql( "UPDATE chart SET series='DATA', category='ORGANISATIONUNIT', filter='PERIOD' WHERE dimension='organisationUnit_dataElement'" );
        executeSql( "UPDATE chart SET series='DATA', category='PERIOD', filter='ORGANISATIONUNIT' WHERE dimension='period'" );
        executeSql( "UPDATE chart SET series='DATA', category='PERIOD', filter='ORGANISATIONUNIT' WHERE dimension='period_dataElement'" );

        executeSql( "UPDATE chart SET type='BAR' where type='bar'" );
        executeSql( "UPDATE chart SET type='BAR' where type='bar3d'" );
        executeSql( "UPDATE chart SET type='STACKEDBAR' where type='stackedBar'" );
        executeSql( "UPDATE chart SET type='STACKEDBAR' where type='stackedBar3d'" );
        executeSql( "UPDATE chart SET type='LINE' where type='line'" );
        executeSql( "UPDATE chart SET type='LINE' where type='line3d'" );
        executeSql( "UPDATE chart SET type='PIE' where type='pie'" );
        executeSql( "UPDATE chart SET type='PIE' where type='pie3d'" );

        executeSql( "ALTER TABLE chart ALTER COLUMN dimension DROP NOT NULL" );
        executeSql( "ALTER TABLE chart RENAME COLUMN title TO name" );
        executeSql( "ALTER TABLE chart DROP COLUMN title" );
        executeSql( "ALTER TABLE chart DROP COLUMN size" );
        executeSql( "ALTER TABLE chart DROP COLUMN verticallabels" );
        executeSql( "ALTER TABLE chart DROP COLUMN targetline" );
        executeSql( "ALTER TABLE chart DROP COLUMN horizontalplotorientation" );
        executeSql( "ALTER TABLE chart ADD COLUMN targetline boolean NOT NULL DEFAULT false" );
        executeSql( "ALTER TABLE chart ADD COLUMN hidesubtitle boolean NOT NULL DEFAULT false" );

        executeSql( "ALTER TABLE chart DROP COLUMN monthsLastYear" );
        executeSql( "ALTER TABLE chart DROP COLUMN quartersLastYear" );
        executeSql( "ALTER TABLE chart DROP COLUMN last6BiMonths" );
        
        executeSql( "ALTER TABLE chart DROP CONSTRAINT chart_title_key" );
        executeSql( "ALTER TABLE chart DROP CONSTRAINT chart_name_key" );
        
        executeSql( "ALTER TABLE chart DROP COLUMN domainaxixlabel" );
        
        executeSql( "ALTER TABLE chart ALTER hideLegend DROP NOT NULL" );
        executeSql( "ALTER TABLE chart ALTER regression DROP NOT NULL" );
        executeSql( "ALTER TABLE chart ALTER hideSubtitle DROP NOT NULL" );
        executeSql( "ALTER TABLE chart ALTER userOrganisationUnit DROP NOT NULL" );        
        
        // remove outdated relative periods
        
        executeSql( "ALTER TABLE reporttable DROP COLUMN last3months" );
        executeSql( "ALTER TABLE reporttable DROP COLUMN last6months" );
        executeSql( "ALTER TABLE reporttable DROP COLUMN last9months" );
        executeSql( "ALTER TABLE reporttable DROP COLUMN sofarthisyear" );
        executeSql( "ALTER TABLE reporttable DROP COLUMN sofarthisfinancialyear" );
        executeSql( "ALTER TABLE reporttable DROP COLUMN last3to6months" );
        executeSql( "ALTER TABLE reporttable DROP COLUMN last6to9months" );
        executeSql( "ALTER TABLE reporttable DROP COLUMN last9to12months" );
        executeSql( "ALTER TABLE reporttable DROP COLUMN last12individualmonths" );
        executeSql( "ALTER TABLE reporttable DROP COLUMN individualmonthsthisyear" );
        executeSql( "ALTER TABLE reporttable DROP COLUMN individualquartersthisyear" );

        executeSql( "ALTER TABLE chart DROP COLUMN last3months" );
        executeSql( "ALTER TABLE chart DROP COLUMN last6months" );
        executeSql( "ALTER TABLE chart DROP COLUMN last9months" );
        executeSql( "ALTER TABLE chart DROP COLUMN sofarthisyear" );
        executeSql( "ALTER TABLE chart DROP COLUMN sofarthisfinancialyear" );
        executeSql( "ALTER TABLE chart DROP COLUMN last3to6months" );
        executeSql( "ALTER TABLE chart DROP COLUMN last6to9months" );
        executeSql( "ALTER TABLE chart DROP COLUMN last9to12months" );
        executeSql( "ALTER TABLE chart DROP COLUMN last12individualmonths" );
        executeSql( "ALTER TABLE chart DROP COLUMN individualmonthsthisyear" );
        executeSql( "ALTER TABLE chart DROP COLUMN individualquartersthisyear" );

        // remove source
        
        executeSql( "ALTER TABLE datasetsource DROP CONSTRAINT fk766ae2938fd8026a" );
        executeSql( "ALTER TABLE datasetlocksource DROP CONSTRAINT fk582fdf7e8fd8026a" );
        executeSql( "ALTER TABLE completedatasetregistration DROP CONSTRAINT fk_datasetcompleteregistration_sourceid" );
        executeSql( "ALTER TABLE minmaxdataelement DROP CONSTRAINT fk_minmaxdataelement_sourceid" );
        executeSql( "ALTER TABLE datavalue DROP CONSTRAINT fk_datavalue_sourceid" );
        executeSql( "ALTER TABLE datavaluearchive DROP CONSTRAINT fk_datavaluearchive_sourceid" );
        executeSql( "ALTER TABLE organisationunit DROP CONSTRAINT fke509dd5ef1c932ed" );
        executeSql( "DROP TABLE source CASCADE" );        

        // message

        executeSql( "ALTER TABLE messageconversation DROP COLUMN messageconversationkey" );
        executeSql( "UPDATE messageconversation SET lastmessage=lastupdated WHERE lastmessage is null" );
        executeSql( "ALTER TABLE message DROP COLUMN messagesubject" );
        executeSql( "ALTER TABLE message DROP COLUMN messagekey" );
        executeSql( "ALTER TABLE message DROP COLUMN sentdate" );
        executeSql( "ALTER TABLE usermessage DROP COLUMN messagedate" );
        executeSql( "UPDATE usermessage SET isfollowup=false WHERE isfollowup is null" );
        executeSql( "DROP TABLE message_usermessages" );

        // create code unique constraints
        
        executeSql( "ALTER TABLE dataelement ADD CONSTRAINT dataelement_code_key UNIQUE(code)" );
        executeSql( "ALTER TABLE indicator ADD CONSTRAINT indicator_code_key UNIQUE(code)" );
        executeSql( "ALTER TABLE organisationunit ADD CONSTRAINT organisationunit_code_key UNIQUE(code)" );
        
        // remove uuid
        
        executeSql( "ALTER TABLE attribute DROP COLUMN uuid" );
        executeSql( "ALTER TABLE categorycombo DROP COLUMN uuid" );
        executeSql( "ALTER TABLE categoryoptioncombo DROP COLUMN uuid" );
        executeSql( "ALTER TABLE chart DROP COLUMN uuid" );
        executeSql( "ALTER TABLE concept DROP COLUMN uuid" );
        executeSql( "ALTER TABLE constant DROP COLUMN uuid" );
        executeSql( "ALTER TABLE datadictionary DROP COLUMN uuid" );
        executeSql( "ALTER TABLE dataelement DROP COLUMN uuid" );
        executeSql( "ALTER TABLE dataelementcategory DROP COLUMN uuid" );
        executeSql( "ALTER TABLE dataelementcategoryoption DROP COLUMN uuid" );
        executeSql( "ALTER TABLE dataelementgroup DROP COLUMN uuid" );
        executeSql( "ALTER TABLE dataelementgroupset DROP COLUMN uuid" );
        executeSql( "ALTER TABLE dataset DROP COLUMN uuid" );
        executeSql( "ALTER TABLE indicator DROP COLUMN uuid" );
        executeSql( "ALTER TABLE indicatorgroup DROP COLUMN uuid" );
        executeSql( "ALTER TABLE indicatorgroupset DROP COLUMN uuid" );
        executeSql( "ALTER TABLE indicatortype DROP COLUMN uuid" );
        executeSql( "ALTER TABLE organisationunit DROP COLUMN uuid" );
        executeSql( "ALTER TABLE orgunitgroup DROP COLUMN uuid" );
        executeSql( "ALTER TABLE orgunitgroupset DROP COLUMN uuid" );
        executeSql( "ALTER TABLE orgunitlevel DROP COLUMN uuid" );
        executeSql( "ALTER TABLE report DROP COLUMN uuid" );
        executeSql( "ALTER TABLE validationrule DROP COLUMN uuid" );
        executeSql( "ALTER TABLE validationrulegroup DROP COLUMN uuid" );
        
        // replace null with false for boolean fields
        
        executeSql( "update chart set hidelegend = false where hidelegend is null" );
        executeSql( "update chart set regression = false where regression is null" );
        executeSql( "update chart set targetline = false where targetline is null" );
        executeSql( "update chart set hidesubtitle = false where hidesubtitle is null" );
        executeSql( "update chart set userorganisationunit = false where userorganisationunit is null" );
        executeSql( "update indicator set annualized = false where annualized is null" );
        executeSql( "update indicatortype set indicatornumber = false where indicatornumber is null" );
        executeSql( "update dataset set mobile = false where mobile is null" );
        executeSql( "update dataset set allowfutureperiods = false where allowfutureperiods is null" );
        executeSql( "update dataelement set zeroissignificant = false where zeroissignificant is null" );
        executeSql( "update organisationunit set haspatients = false where haspatients is null" );
        executeSql( "update dataset set expirydays = 0 where expirydays is null" );

        executeSql( "update reporttable set reportingmonth = false where reportingmonth is null" );
        executeSql( "update reporttable set reportingbimonth = false where reportingbimonth is null" );
        executeSql( "update reporttable set reportingquarter = false where reportingquarter is null" );
        executeSql( "update reporttable set monthsthisyear = false where monthsthisyear is null" );
        executeSql( "update reporttable set quartersthisyear = false where quartersthisyear is null" );
        executeSql( "update reporttable set thisyear = false where thisyear is null" );
        executeSql( "update reporttable set monthslastyear = false where monthslastyear is null" );
        executeSql( "update reporttable set quarterslastyear = false where quarterslastyear is null" );
        executeSql( "update reporttable set lastyear = false where lastyear is null" );
        executeSql( "update reporttable set last5years = false where last5years is null" );
        executeSql( "update reporttable set lastsixmonth = false where lastsixmonth is null" );
        executeSql( "update reporttable set last4quarters = false where last4quarters is null" );
        executeSql( "update reporttable set last12months = false where last12months is null" );
        executeSql( "update reporttable set last6bimonths = false where last6bimonths is null" );
        executeSql( "update reporttable set last4quarters = false where last4quarters is null" );
        executeSql( "update reporttable set last2sixmonths = false where last2sixmonths is null" );
        executeSql( "update reporttable set thisfinancialyear = false where thisfinancialyear is null" );
        executeSql( "update reporttable set lastfinancialyear = false where lastfinancialyear is null" );
        executeSql( "update reporttable set last5financialyears = false where last5financialyears is null" );
        executeSql( "update reporttable set cumulative = false where cumulative is null" );

        executeSql( "update chart set reportingmonth = false where reportingmonth is null" );
        executeSql( "update chart set reportingbimonth = false where reportingbimonth is null" );
        executeSql( "update chart set reportingquarter = false where reportingquarter is null" );
        executeSql( "update chart set monthsthisyear = false where monthsthisyear is null" );
        executeSql( "update chart set quartersthisyear = false where quartersthisyear is null" );
        executeSql( "update chart set thisyear = false where thisyear is null" );
        executeSql( "update chart set monthslastyear = false where monthslastyear is null" );
        executeSql( "update chart set quarterslastyear = false where quarterslastyear is null" );
        executeSql( "update chart set lastyear = false where lastyear is null" );
        executeSql( "update chart set lastsixmonth = false where lastsixmonth is null" );
        executeSql( "update chart set last12months = false where last12months is null" );
        executeSql( "update chart set last5years = false where last5years is null" );
        executeSql( "update chart set last4quarters = false where last4quarters is null" );
        executeSql( "update chart set last12months = false where last12months is null" );
        executeSql( "update chart set last6bimonths = false where last6bimonths is null" );
        executeSql( "update chart set last4quarters = false where last4quarters is null" );
        executeSql( "update chart set last2sixmonths = false where last2sixmonths is null" );
        executeSql( "update chart set showdata = false where showdata is null" );
        executeSql( "update chart set userorganisationunitchildren = false where userorganisationunitchildren is null" );
        executeSql( "update chart set userorganisationunit = false where userorganisationunit is null" );

        // report, reporttable, chart groups
        
        executeSql( "DROP TABLE reportgroupmembers" );
        executeSql( "DROP TABLE reportgroup" );
        executeSql( "DROP TABLE reporttablegroupmembers" );
        executeSql( "DROP TABLE reporttablegroup" );
        executeSql( "DROP TABLE chartgroupmembers" );
        executeSql( "DROP TABLE chartgroup" );
        
        executeSql( "ALTER TABLE patientdatavaluearchive DROP COLUMN categoryoptioncomboid" );
        executeSql( "delete from usersetting where name='currentStyle' and value like '%blue/blue.css'" );
        executeSql( "delete from systemsetting where name='currentStyle' and value like '%blue/blue.css'" );
        
        executeSql( "update dataentryform set style='regular' where style is null" );
        executeSql( "update dataset set skipaggregation = false where skipaggregation is null" );
        
        log.info( "Tables updated" );
    }

    private List<Integer> getDistinctIdList( String table, String col1 )
    {
        StatementHolder holder = statementManager.getHolder();

        List<Integer> distinctIds = new ArrayList<Integer>();

        try
        {
            Statement statement = holder.getStatement();

            ResultSet resultSet = statement.executeQuery( "SELECT DISTINCT " + col1 + " FROM " + table );

            while ( resultSet.next() )
            {
                distinctIds.add( resultSet.getInt( 1 ) );
            }
        }
        catch ( Exception ex )
        {
            log.error( ex );
        }
        finally
        {
            holder.close();
        }

        return distinctIds;
    }

    private Map<Integer, List<Integer>> getIdMap( String table, String col1, String col2, List<Integer> distinctIds )
    {
        StatementHolder holder = statementManager.getHolder();

        Map<Integer, List<Integer>> idMap = new HashMap<Integer, List<Integer>>();

        try
        {
            Statement statement = holder.getStatement();

            for ( Integer distinctId : distinctIds )
            {
                List<Integer> foreignIds = new ArrayList<Integer>();

                ResultSet resultSet = statement.executeQuery( "SELECT " + col2 + " FROM " + table + " WHERE " + col1
                    + "=" + distinctId );

                while ( resultSet.next() )
                {
                    foreignIds.add( resultSet.getInt( 1 ) );
                }

                idMap.put( distinctId, foreignIds );
            }
        }
        catch ( Exception ex )
        {
            log.error( ex );
        }
        finally
        {
            holder.close();
        }

        return idMap;
    }

    private void updateSortOrder( String table, String col1, String col2 )
    {
        List<Integer> distinctIds = getDistinctIdList( table, col1 );

        log.info( "Got distinct ids: " + distinctIds.size() );

        Map<Integer, List<Integer>> idMap = getIdMap( table, col1, col2, distinctIds );

        log.info( "Got id map: " + idMap.size() );

        for ( Integer distinctId : idMap.keySet() )
        {
            int sortOrder = 1;

            for ( Integer foreignId : idMap.get( distinctId ) )
            {
                String sql = "UPDATE " + table + " SET sort_order=" + sortOrder++ + " WHERE " + col1 + "=" + distinctId
                    + " AND " + col2 + "=" + foreignId;

                int count = executeSql( sql );

                log.info( "Executed: " + count + " - " + sql );
            }
        }
    }

    private int executeSql( String sql )
    {
        try
        {
            return statementManager.getHolder().executeUpdate( sql );
        }
        catch ( Exception ex )
        {
            log.debug( ex );

            return -1;
        }
    }

    private boolean updateDataSetAssociation()
    {
        StatementHolder holder = statementManager.getHolder();

        try
        {
            Statement statement = holder.getStatement();

            ResultSet isUpdated = statement
                .executeQuery( "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'dataentryformassociation'" );

            if ( isUpdated.next() )
            {

                ResultSet resultSet = statement
                    .executeQuery( "SELECT associationid, dataentryformid FROM dataentryformassociation WHERE associationtablename = 'dataset'" );

                while ( resultSet.next() )
                {
                    executeSql( "UPDATE dataset SET dataentryform=" + resultSet.getInt( 2 ) + " WHERE datasetid="
                        + resultSet.getInt( 1 ) );
                }
                return true;
            }

            return false;

        }
        catch ( Exception ex )
        {
            log.debug( ex );
            return false;
        }
        finally
        {
            holder.close();
        }

    }

    private boolean updateProgramStageAssociation()
    {
        StatementHolder holder = statementManager.getHolder();

        try
        {
            Statement statement = holder.getStatement();

            ResultSet isUpdated = statement
                .executeQuery( "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'dataentryformassociation'" );

            if ( isUpdated.next() )
            {

                ResultSet resultSet = statement
                    .executeQuery( "SELECT associationid, dataentryformid FROM dataentryformassociation WHERE associationtablename = 'programstage'" );

                while ( resultSet.next() )
                {
                    executeSql( "UPDATE programstage SET dataentryform=" + resultSet.getInt( 2 )
                        + " WHERE programstageid=" + resultSet.getInt( 1 ) );
                }
            }
            return true;
        }
        catch ( Exception ex )
        {
            log.debug( ex );
            return false;
        }
        finally
        {
            holder.close();
        }

    }

}
