package org.hisp.dhis.databrowser.jdbc;

import java.util.List;

import org.amplecode.quick.StatementManager;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.GridHeader;
import org.hisp.dhis.databrowser.DataBrowserGridStore;
import org.hisp.dhis.databrowser.util.DataBrowserUtils;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.system.grid.ListGrid;

/**
 * @author joakibj, martinwa, briane, eivinhb
 * @version $Id JDBCDataBrowserStore.java 2010-04-06 jpp, ddhieu$
 */
public class JDBCDataBrowserStore
    extends DataBrowserUtils
    implements DataBrowserGridStore
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private StatementBuilder statementBuilder;

    public void setStatementBuilder( StatementBuilder statementBuilder )
    {
        this.statementBuilder = statementBuilder;
    }

    // -------------------------------------------------------------------------
    // DataBrowserStore implementation
    //
    // Basic
    // -------------------------------------------------------------------------

    public Grid getDataSetsBetweenPeriods( List<Integer> betweenPeriodIds, boolean isZeroAdded )
    {
        StringBuffer sqlsb = new StringBuffer();

        sqlsb.append( "(SELECT d.datasetid AS ID, d.name AS DataSet, COUNT(*) AS counts_of_aggregated_values " );
        sqlsb.append( "FROM datavalue dv " );
        sqlsb.append( "JOIN datasetmembers dsm ON (dv.dataelementid = dsm.dataelementid) " );
        sqlsb.append( "JOIN dataset d ON (d.datasetid = dsm.datasetid) " );
        sqlsb.append( "JOIN period p ON (dv.periodid = p.periodid) " );
        sqlsb.append( "WHERE dv.periodid IN " + splitListHelper( betweenPeriodIds ) + " " );
        sqlsb.append( "GROUP BY d.datasetid, d.name " );
        sqlsb.append( "ORDER BY counts_of_aggregated_values DESC)" );

        // Gets all the dataSets in a period with a count attached to the
        // dataSet. The table returned has only 2 columns. They are created here
        // in this method directly

        Grid dataSetGrid = new ListGrid();

        dataSetGrid.addHeader( new GridHeader( "drilldown_data_set", false, false ) );
        dataSetGrid.addHeader( new GridHeader( "counts_of_aggregated_values", false, false ) );

        fillUpDataBasic( dataSetGrid, sqlsb, isZeroAdded, statementManager );

        return dataSetGrid;
    }

    public Grid getDataElementGroupsBetweenPeriods( List<Integer> betweenPeriodIds, boolean isZeroAdded )
    {
        StringBuffer sqlsb = new StringBuffer();

        sqlsb
            .append( "(SELECT d.dataelementgroupid AS ID, d.name AS DataElementGroup, COUNT(*) AS counts_of_aggregated_values " );
        sqlsb.append( "FROM datavalue dv " );
        sqlsb.append( "JOIN dataelementgroupmembers degm ON (dv.dataelementid = degm.dataelementid)" );
        sqlsb.append( "JOIN dataelementgroup d ON (d.dataelementgroupid = degm.dataelementgroupid) " );
        sqlsb.append( "WHERE dv.periodid IN " + splitListHelper( betweenPeriodIds ) + " " );
        sqlsb.append( "GROUP BY d.dataelementgroupid, d.name " );
        sqlsb.append( "ORDER BY counts_of_aggregated_values DESC)" );

        Grid gridDEG = new ListGrid();

        gridDEG.addHeader( new GridHeader( "drilldown_data_element_group", false, false ) );
        gridDEG.addHeader( new GridHeader( "counts_of_aggregated_values", false, false ) );

        fillUpDataBasic( gridDEG, sqlsb, isZeroAdded, statementManager );

        return gridDEG;
    }

    public Grid getOrgUnitGroupsBetweenPeriods( List<Integer> betweenPeriodIds, boolean isZeroAdded )
    {
        StringBuffer sqlsb = new StringBuffer();

        sqlsb.append( "(SELECT oug.orgunitgroupid, oug.name AS OrgUnitGroup, COUNT(*) AS counts_of_aggregated_values " );
        sqlsb.append( "FROM orgunitgroup oug " );
        sqlsb.append( "JOIN orgunitgroupmembers ougm ON oug.orgunitgroupid = ougm.orgunitgroupid " );
        sqlsb.append( "JOIN organisationunit ou ON  ougm.organisationunitid = ou.organisationunitid " );
        sqlsb.append( "JOIN datavalue dv ON ou.organisationunitid = dv.sourceid " );
        sqlsb.append( "WHERE dv.periodid IN " + splitListHelper( betweenPeriodIds ) + " " );
        sqlsb.append( "GROUP BY oug.orgunitgroupid, oug.name " );
        sqlsb.append( "ORDER BY counts_of_aggregated_values DESC) " );

        Grid gridOUG = new ListGrid();

        gridOUG.addHeader( new GridHeader( "drilldown_orgunit_group", false, false ) );
        gridOUG.addHeader( new GridHeader( "counts_of_aggregated_values", false, false ) );

        fillUpDataBasic( gridOUG, sqlsb, isZeroAdded, statementManager );

        return gridOUG;
    }

    // -------------------------------------------------------------------------
    // Advance - Set structure
    // -------------------------------------------------------------------------

    public void setDataElementStructureForDataSet( Grid grid, Integer dataSetId, List<Integer> metaIds )
    {
        StringBuffer sqlsb = new StringBuffer();

        sqlsb.append( "(SELECT de.dataelementid, de.name AS DataElement " );
        sqlsb.append( "FROM dataelement de " );
        sqlsb.append( "JOIN datasetmembers dsm ON (de.dataelementid = dsm.dataelementid) " );
        sqlsb.append( "WHERE dsm.datasetid = '" + dataSetId + "' " );
        sqlsb.append( "ORDER BY de.name) " );

        grid.addHeader( new GridHeader( "drilldown_data_element", false, false ) );
        setMetaStructure( grid, sqlsb, metaIds, statementManager );
    }

    public void setDataElementStructureForDataElementGroup( Grid grid, Integer dataElementGroupId, List<Integer> metaIds )
    {
        StringBuffer sqlsb = new StringBuffer();

        sqlsb.append( "(SELECT de.dataelementid, de.name AS DataElement " );
        sqlsb.append( "FROM dataelement de " );
        sqlsb.append( "JOIN dataelementgroupmembers degm ON (de.dataelementid = degm.dataelementid) " );
        sqlsb.append( "WHERE degm.dataelementgroupid = '" + dataElementGroupId + "' " );
        sqlsb.append( "GROUP BY de.dataelementid, de.name " );
        sqlsb.append( "ORDER BY de.name) " );

        grid.addHeader( new GridHeader( "drilldown_data_element", false, false ) );
        setMetaStructure( grid, sqlsb, metaIds, statementManager );
    }

    public void setDataElementGroupStructureForOrgUnitGroup( Grid grid, Integer orgUnitGroupId, List<Integer> metaIds )
    {
        StringBuffer sqlsb = new StringBuffer();

        sqlsb.append( "(SELECT deg.dataelementgroupid, deg.name AS DataElementGroup " );
        sqlsb.append( "FROM dataelementgroup deg " );
        sqlsb.append( "JOIN dataelementgroupmembers degm ON deg.dataelementgroupid = degm.dataelementgroupid " );
        sqlsb.append( "JOIN datavalue dv ON degm.dataelementid = dv.dataelementid " );
        sqlsb.append( "JOIN organisationunit ou ON dv.sourceid = ou.organisationunitid " );
        sqlsb.append( "JOIN orgunitgroupmembers ougm ON ou.organisationunitid = ougm.organisationunitid " );
        sqlsb.append( "WHERE ougm.orgunitgroupid = '" + orgUnitGroupId + "' " );
        sqlsb.append( "GROUP BY deg.dataelementgroupid, deg.name " );
        sqlsb.append( "ORDER BY deg.name ASC) " );

        grid.addHeader( new GridHeader( "drilldown_data_element_group", false, false ) );
        setMetaStructure( grid, sqlsb, metaIds, statementManager );

    }

    public void setStructureForOrgUnit( Grid grid, Integer orgUnitParent, List<Integer> metaIds )
    {
        StringBuffer sqlsb = new StringBuffer();

        sqlsb.append( "(SELECT o.organisationunitid, o.name AS OrganisationUnit " );
        sqlsb.append( "FROM organisationunit o " );
        sqlsb.append( "WHERE o.parentid = '" + orgUnitParent + "' " );
        sqlsb.append( "ORDER BY o.name)" );

        grid.addHeader( new GridHeader( "drilldown_orgunit", false, false ) );
        setMetaStructure( grid, sqlsb, metaIds, statementManager );
    }

    public void setDataElementStructureForOrgUnit( Grid grid, Integer orgUnitId, List<Integer> metaIds )
    {
        StringBuffer sqlsb = new StringBuffer();

        sqlsb.append( statementBuilder.queryDataElementStructureForOrgUnit() );

        grid.addHeader( new GridHeader( "drilldown_data_element", false, false ) );
        setMetaStructure( grid, sqlsb, metaIds, statementManager );
    }

    // -------------------------------------------------------------------------
    // Advance - Set count
    // -------------------------------------------------------------------------

    public Integer setCountDataElementsForDataSetBetweenPeriods( Grid grid, Integer dataSetId,
        List<Integer> betweenPeriodIds, List<Integer> metaIds, boolean isZeroAdded )
    {
        // Here we uses a for loop to create one big sql statement using UNION.
        // This is done because the count and GROUP BY parts of this query can't
        // be done in another way. The alternative to this method is to actually
        // query the database as many time than betweenPeriodIds.size() tells.
        // But the overhead cost of doing that is bigger than the creation of
        // this UNION query.

        StringBuffer sqlsb = new StringBuffer();

        int i = 0;
        for ( Integer periodId : betweenPeriodIds )
        {
            i++;

            sqlsb.append( "(SELECT de.dataelementid, de.name AS dataelement, COUNT(*) AS counts_of_aggregated_values, p.periodid AS PeriodId, p.startdate AS ColumnHeader " );
            sqlsb.append( "FROM dataelement de JOIN datavalue dv ON (de.dataelementid = dv.dataelementid) " );
            sqlsb.append( "JOIN datasetmembers dsm ON (de.dataelementid = dsm.dataelementid) " );
            sqlsb.append( "JOIN period p ON (dv.periodid = p.periodid) " );
            sqlsb.append( "WHERE dsm.datasetid = '" + dataSetId + "' AND dv.periodid = '" + periodId + "' " );
            sqlsb.append( "GROUP BY de.dataelementid, de.name, p.periodid, p.startDate)" );

            sqlsb.append( i == betweenPeriodIds.size() ? "ORDER BY ColumnHeader" : " UNION " );
        }

        return fillUpDataAdvance( grid, sqlsb, metaIds, isZeroAdded, statementManager );
    }

    public Integer setCountDataElementsForDataElementGroupBetweenPeriods( Grid grid, Integer dataElementGroupId,
        List<Integer> betweenPeriodIds, List<Integer> metaIds, boolean isZeroAdded )
    {
        StringBuffer sqlsb = new StringBuffer();

        int i = 0;
        for ( Integer periodid : betweenPeriodIds )
        {
            i++;

            sqlsb
                .append( "(SELECT de.dataelementid, de.name AS DataElement, COUNT(dv.value) AS counts_of_aggregated_values, p.periodid AS PeriodId, p.startDate AS ColumnHeader " );
            sqlsb.append( "FROM dataelement de JOIN datavalue dv ON (de.dataelementid = dv.dataelementid) " );
            sqlsb.append( "JOIN dataelementgroupmembers degm ON (de.dataelementid = degm.dataelementid) " );
            sqlsb.append( "JOIN period p ON (dv.periodid = p.periodid) " );
            sqlsb.append( "WHERE degm.dataelementgroupid = '" + dataElementGroupId + "' " );
            sqlsb.append( "AND dv.periodid = '" + periodid + "' " );
            sqlsb.append( "GROUP BY de.dataelementid, de.name, p.periodid, p.startDate) " );

            sqlsb.append( i == betweenPeriodIds.size() ? "ORDER BY ColumnHeader" : " UNION " );
        }

        return fillUpDataAdvance( grid, sqlsb, metaIds, isZeroAdded, statementManager );
    }

    public Integer setCountDataElementGroupsForOrgUnitGroupBetweenPeriods( Grid grid, Integer orgUnitGroupId,
        List<Integer> betweenPeriodIds, List<Integer> metaIds, boolean isZeroAdded )
    {
        StringBuffer sqlsb = new StringBuffer();

        int i = 0;
        for ( Integer periodid : betweenPeriodIds )
        {
            i++;

            sqlsb
                .append( "(SELECT deg.dataelementgroupid, deg.name, COUNT(dv.value) AS counts_of_aggregated_values, p.periodid AS PeriodId, p.startdate AS ColumnHeader " );
            sqlsb.append( "FROM dataelementgroup AS deg " );
            sqlsb
                .append( "INNER JOIN dataelementgroupmembers AS degm ON deg.dataelementgroupid = degm.dataelementgroupid " );
            sqlsb.append( "INNER JOIN datavalue AS dv ON degm.dataelementid = dv.dataelementid " );
            sqlsb.append( "INNER JOIN period AS p ON dv.periodid = p.periodid " );
            sqlsb.append( "INNER JOIN organisationunit AS ou ON dv.sourceid = ou.organisationunitid " );
            sqlsb.append( "INNER JOIN orgunitgroupmembers AS ougm ON ou.organisationunitid = ougm.organisationunitid " );
            sqlsb
                .append( "WHERE p.periodid =  '" + periodid + "' AND ougm.orgunitgroupid =  '" + orgUnitGroupId + "' " );
            sqlsb.append( "GROUP BY deg.dataelementgroupid,deg.name,p.periodid,p.startdate) " );

            sqlsb.append( i == betweenPeriodIds.size() ? "ORDER BY ColumnHeader" : " UNION " );
        }

        return fillUpDataAdvance( grid, sqlsb, metaIds, isZeroAdded, statementManager );
    }

    public Integer setCountOrgUnitsBetweenPeriods( Grid grid, Integer orgUnitParent, List<Integer> betweenPeriodIds,
        Integer maxLevel, List<Integer> metaIds, boolean isZeroAdded )
    {
        StringBuffer sqlsbDescentdants = new StringBuffer();

        boolean valid = this.setUpQueryForDrillDownDescendants( sqlsbDescentdants, orgUnitParent, betweenPeriodIds,
            maxLevel );

        return (valid ? fillUpDataAdvance( grid, sqlsbDescentdants, metaIds, isZeroAdded, statementManager ) : 0);

    }

    // This method retrieves raw data for a given orgunit, periods,

    public Integer setRawDataElementsForOrgUnitBetweenPeriods( Grid grid, Integer orgUnitId,
        List<Integer> betweenPeriodIds, List<Integer> metaIds, boolean isZeroAdded )
    {
        StringBuffer sqlsb = new StringBuffer();

        sqlsb.append( statementBuilder.queryRawDataElementsForOrgUnitBetweenPeriods( orgUnitId, betweenPeriodIds ) );

        return fillUpDataAdvance( grid, sqlsb, metaIds, isZeroAdded, statementManager );
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    /**
     * Splits a list of integers by by comma. Use this method if you have a list
     * that will be used in f.ins. a WHERE xxx IN (list) clause in SQL.
     * 
     * @param List <Integer> list of Integers
     * @return the list as a string splitted by a comma.
     */
    private String splitListHelper( List<Integer> list )
    {
        StringBuffer sb = new StringBuffer();
        int count = 0;

        sb.append( "(" );
        for ( Integer i : list )
        {
            sb.append( i );

            count++;

            if ( count < list.size() )
            {
                sb.append( "," );
            }
        }
        sb.append( ")" );

        return sb.toString();
    }

    private boolean setUpQueryForDrillDownDescendants( StringBuffer sb, Integer orgUnitSelected,
        List<Integer> betweenPeriodIds, Integer maxLevel )
    {
        if ( maxLevel == null )
        {
            maxLevel = organisationUnitService.getMaxOfOrganisationUnitLevels();
        }

        int curLevel = organisationUnitService.getLevelOfOrganisationUnit( orgUnitSelected );
        int loopSize = betweenPeriodIds.size();

        String descendantQuery = this.setUpQueryGetDescendants( curLevel, maxLevel, orgUnitSelected );

        if ( !descendantQuery.isEmpty() )
        {
            int i = 0;

            for ( Integer periodid : betweenPeriodIds )
            {
                i++;
                /**
                 * Get all descendant level data for all orgunits under the
                 * selected, grouped by the next immediate children of the
                 * selected orgunit Looping through each period UNION construct
                 * appears to be faster with an index placed on periodid's
                 * rather than joining on periodids and then performing the
                 * aggregation step.
                 * 
                 */
                sb.append( " SELECT a.parentid,a.name AS organisationunit,COUNT(*),p.periodid,p.startdate AS columnheader" );
                sb.append( " FROM datavalue dv" );
                sb.append( " INNER JOIN (SELECT DISTINCT x.parentid,x.childid,ou.name FROM(" + descendantQuery + ") x" );
                sb.append( " INNER JOIN organisationunit ou ON x.parentid=ou.organisationunitid) a ON dv.sourceid=a.childid" );
                sb.append( " INNER JOIN period p ON dv.periodid=p.periodid" );
                sb.append( " WHERE dv.periodid=" + periodid );
                sb.append( " GROUP BY a.parentid,a.name,p.periodid,p.startdate" );
                sb.append( i < loopSize ? " UNION " : "" );

            }

            sb.append( " ORDER BY columnheader,organisationunit" );

            return true;
        }

        return false;
    }

    private String setUpQueryGetDescendants( int curLevel, int maxLevel, Integer orgUnitSelected )
    {
        Integer childLevel = curLevel + 1;
        Integer diffLevel = maxLevel - curLevel;

        // The immediate child level can probably be combined into the for loop
        // but we need to clarify whether the selected unit should be present,
        // and if so, how?

        StringBuilder desc_query = new StringBuilder();

        // Loop through each of the descendants until the diff level is reached
        for ( int j = 0; j < diffLevel; j++ )
        {
            desc_query.append( j != 0 ? " UNION " : "" );
            desc_query.append( "SELECT DISTINCT idlevel" + (childLevel) + " AS parentid," );
            desc_query.append( "idlevel" + (childLevel + j) + " AS childid" );
            desc_query.append( " FROM _orgunitstructure" );
            desc_query.append( " WHERE idlevel" + (curLevel) + "='" + orgUnitSelected + "'" );
            desc_query.append( " AND idlevel" + (childLevel + j) + "<>0" );
        }

        return desc_query.toString();
    }

}
