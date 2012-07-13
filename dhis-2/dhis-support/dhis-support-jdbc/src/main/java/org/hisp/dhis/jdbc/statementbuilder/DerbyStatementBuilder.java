package org.hisp.dhis.jdbc.statementbuilder;

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

import static org.hisp.dhis.system.util.DateUtils.getSqlDateString;

import java.util.List;

import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DerbyStatementBuilder
    extends AbstractStatementBuilder
{
    public String getDoubleColumnType()
    {
        return "DOUBLE";
    }
    
    public String getPeriodIdentifierStatement( Period period )
    {
        return 
            "SELECT periodid FROM period WHERE periodtypeid=" + period.getPeriodType().getId() + " " + 
            "AND startdate='" + getSqlDateString( period.getStartDate() ) + "' " +
            "AND enddate='" + getSqlDateString( period.getEndDate() ) + "'";
    }    

    public String getDeleteZeroDataValues()
    {
        return 
            "DELETE FROM datavalue " +
            "USING datavalue, dataelement " +
            "WHERE datavalue.dataelementid = dataelement.dataelementid " +
            "AND dataelement.aggregationtype = 'sum' " +
            "AND datavalue.value = '0'";
    }
    
    public int getMaximumNumberOfColumns()
    {
        return 1200; //TODO verify
    }

    public String getDropDatasetForeignKeyForDataEntryFormTable()
    {
        return "ALTER TABLE dataentryform DROP FOREIGN KEY fk_dataentryform_datasetid;" ;
    }

    @Override
    public String getMoveDataValueToDestination( int sourceId, int destinationId )
    {
        return "UPDATE datavalue AS d1 SET sourceid=" + destinationId + " " + "WHERE sourceid=" + sourceId + " "
            + "AND NOT EXISTS ( " + "SELECT * from datavalue AS d2 " + "WHERE d2.sourceid=" + destinationId + " "
            + "AND d1.dataelementid=d2.dataelementid " + "AND d1.periodid=d2.periodid "
            + "AND d1.categoryoptioncomboid=d2.categoryoptioncomboid );";
    }

    @Override
    public String getSummarizeDestinationAndSourceWhereMatching( int sourceId, int destId )
    {
        return "UPDATE datavalue AS d1 SET value=( " + "SELECT SUM( CAST( value AS "
            + getDoubleColumnType() + " ) ) " + "FROM datavalue as d2 "
            + "WHERE d1.dataelementid=d2.dataelementid " + "AND d1.periodid=d2.periodid "
            + "AND d1.categoryoptioncomboid=d2.categoryoptioncomboid " + "AND d2.sourceid IN ( " + destId + ", "
            + sourceId + " ) ) " + "FROM dataelement AS de " + "WHERE d1.sourceid=" + destId + " "
            + "AND d1.dataelementid=de.dataelementid " + "AND de.valuetype='int';";
    }

    @Override
    public String getUpdateDestination( int destDataElementId, int destCategoryOptionComboId,
        int sourceDataElementId, int sourceCategoryOptionComboId )
    {
        return "UPDATE datavalue AS d1 SET dataelementid=" + destDataElementId + ", categoryoptioncomboid=" + destCategoryOptionComboId + " " 
            + "WHERE dataelementid=" + sourceDataElementId + " AND categoryoptioncomboid=" + sourceCategoryOptionComboId + " " + "AND NOT EXISTS ( "
            + "SELECT * FROM datavalue AS d2 " + "WHERE d2.dataelementid=" + destDataElementId + " "
            + "AND d2.categoryoptioncomboid=" + destCategoryOptionComboId + " " + "AND d1.periodid=d2.periodid "
            + "AND d1.sourceid=d2.sourceid );";
    }

    public String getMoveFromSourceToDestination( int destDataElementId, int destCategoryOptionComboId,
        int sourceDataElementId, int sourceCategoryOptionComboId )
    {
        return "UPDATE datavalue SET value=d2.value,storedby=d2.storedby,lastupdated=d2.lastupdated,comment=d2.comment,followup=d2.followup "
            + "FROM datavalue AS d2 "
            + "WHERE datavalue.periodid=d2.periodid "
            + "AND datavalue.sourceid=d2.sourceid "
            + "AND datavalue.lastupdated<d2.lastupdated "
            + "AND datavalue.dataelementid=" + destDataElementId + " AND datavalue.categoryoptioncomboid=" + destCategoryOptionComboId + " "
            + "AND d2.dataelementid=" + sourceDataElementId + " AND d2.categoryoptioncomboid=" + sourceCategoryOptionComboId + ";";
    }

    public String getStandardDeviation( int dataElementId, int categoryOptionComboId, int organisationUnitId )
    {
        return "SELECT STDDEV( CAST( value AS " + getDoubleColumnType() + " ) ) FROM datavalue "
            + "WHERE dataelementid='" + dataElementId + "' " 
            + "AND categoryoptioncomboid='" + categoryOptionComboId  + "' " 
            + "AND sourceid='" + organisationUnitId + "'";
    }

    public String getAverage( int dataElementId, int categoryOptionComboId, int organisationUnitId )
    {
        return "SELECT AVG( CAST( value AS " + getDoubleColumnType() + " ) ) FROM datavalue " 
            + "WHERE dataelementid='" + dataElementId + "' " 
            + "AND categoryoptioncomboid='" + categoryOptionComboId + "' " 
            + "AND sourceid='" + organisationUnitId + "'";
    }

    public String getDeflatedDataValues( int dataElementId, String dataElementName, int categoryOptionComboId,
        String periodIds, int organisationUnitId, String organisationUnitName, int lowerBound, int upperBound )
    {
        return "SELECT dv.dataelementid, dv.periodid, dv.sourceid, dv.categoryoptioncomboid, dv.value, dv.storedby, dv.lastupdated, "
            + "dv.comment, dv.followup, '" + lowerBound + "' AS minvalue, '" + upperBound + "' AS maxvalue, "
            + encode( dataElementName ) + " AS dataelementname, pt.name AS periodtypename, pe.startdate, pe.enddate, "
            + encode( organisationUnitName ) + " AS sourcename, cc.categoryoptioncomboname "
            + "FROM datavalue AS dv "
            + "JOIN period AS pe USING (periodid) "
            + "JOIN periodtype AS pt USING (periodtypeid) "
            + "LEFT JOIN _categoryoptioncomboname AS cc USING (categoryoptioncomboid) "
            + "WHERE dv.dataelementid='" + dataElementId + "' "
            + "AND dv.categoryoptioncomboid='" + categoryOptionComboId + "' "
            + "AND dv.periodid IN (" + periodIds + ") "
            + "AND dv.sourceid='" + organisationUnitId + "' "
            + "AND ( CAST( dv.value AS " + getDoubleColumnType() + " ) < '" + lowerBound + "' "
            + "OR CAST( dv.value AS " + getDoubleColumnType() + " ) > '" + upperBound + "' )";
    }

    public String archiveData( String startDate, String endDate )
    {
        return "DELETE FROM datavaluearchive AS a " 
            + "USING period AS p " 
            + "WHERE a.periodid=p.periodid "
            + "AND p.startdate>='" + startDate + "' " 
            + "AND p.enddate<='" + endDate + "'";
    }

    public String unArchiveData( String startDate, String endDate )
    {
        return "DELETE FROM datavaluearchive AS a " 
            + "USING period AS p " + "WHERE a.periodid=p.periodid "
            + "AND p.startdate>='" + startDate + "' " 
            + "AND p.enddate<='" + endDate + "'";
    }

    public String deleteRegularOverlappingData()
    {
        return "DELETE FROM datavalue AS d " 
            + "USING datavaluearchive AS a "
            + "WHERE d.dataelementid=a.dataelementid " 
            + "AND d.periodid=a.periodid " 
            + "AND d.sourceid=a.sourceid "
            + "AND d.categoryoptioncomboid=a.categoryoptioncomboid";
    }

    public String deleteArchivedOverlappingData()
    {
        return "DELETE FROM datavaluearchive AS a " 
            + "USING datavalue AS d "
            + "WHERE a.dataelementid=d.dataelementid " 
            + "AND a.periodid=d.periodid " 
            + "AND a.sourceid=d.sourceid "
            + "AND a.categoryoptioncomboid=d.categoryoptioncomboid";
    }

    public String deleteOldestOverlappingDataValue()
    {
        return "DELETE FROM datavalue AS d " 
            + "USING datavaluearchive AS a "
            + "WHERE d.dataelementid=a.dataelementid " 
            + "AND d.periodid=a.periodid " 
            + "AND d.sourceid=a.sourceid "
            + "AND d.categoryoptioncomboid=a.categoryoptioncomboid " + "AND d.lastupdated<a.lastupdated";
    }

    public String deleteOldestOverlappingArchiveData()
    {

        return "DELETE FROM datavaluearchive AS a " 
            + "USING datavalue AS d "
            + "WHERE a.dataelementid=d.dataelementid " 
            + "AND a.periodid=d.periodid " + "AND a.sourceid=d.sourceid "
            + "AND a.categoryoptioncomboid=d.categoryoptioncomboid " 
            + "AND a.lastupdated<=d.lastupdated";
    }

    public String archivePatientData( String startDate, String endDate )
    {
        return "DELETE FROM patientdatavalue AS pdv " 
            + "USING programstageinstance AS psi ,  programinstance AS pi "
            + "WHERE pdv.programstageinstanceid = psi.programstageinstanceid "
            + "AND pi.programinstanceid = psi.programinstanceid " + "WHERE pi.enddate >= '" + startDate + "' "
            + "AND pi.enddate <= '" + endDate + "';";
    }

    public String unArchivePatientData( String startDate, String endDate )
    {
        return "DELETE FROM patientdatavaluearchive AS pdv "
            + "USING programstageinstance AS psi ,  programinstance AS pi "
            + "WHERE pdv.programstageinstanceid = psi.programstageinstanceid "
            + "AND pi.programinstanceid = psi.programinstanceid " 
            + "WHERE pi.enddate >= '" + startDate + "' "
            + "AND pi.enddate <= '" + endDate + "';";
    }

    public String deleteRegularOverlappingPatientData()
    {
        return "DELETE FROM patientdatavalue AS d " 
            + "USING patientdatavaluearchive AS a "
            + "WHERE d.programstageinstanceid=a.programstageinstanceid " 
            + "AND d.dataelementid=a.dataelementid "
            + "AND d.timestamp<a.timestamp;";
    }

    public String deleteArchivedOverlappingPatientData()
    {
        return "DELETE FROM patientdatavaluearchive AS a " 
            + "USING patientdatavalue AS d "
            + "WHERE d.programstageinstanceid=a.programstageinstanceid " 
            + "AND d.dataelementid=a.dataelementid ";
    }

    public String deleteOldestOverlappingPatientDataValue()
    {
        return "DELETE FROM patientdatavalue AS d " 
            + "USING patientdatavaluearchive AS a "
            + "WHERE d.programstageinstanceid=a.programstageinstanceid " 
            + "AND d.dataelementid=a.dataelementid "
            + "AND d.timestamp<a.timestamp;";
    }

    public String deleteOldestOverlappingPatientArchiveData()
    {
        return "DELETE FROM patientdatavalue AS d " + "USING patientdatavaluearchive AS a "
            + "WHERE d.programstageinstanceid=a.programstageinstanceid " + "AND d.dataelementid=a.dataelementid "
            + "AND a.timestamp<=d.timestamp;";
    }

    public String queryDataElementStructureForOrgUnit()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "(SELECT DISTINCT de.dataelementid, (de.name || ' ' || cc.categoryoptioncomboname) AS DataElement " );
        sb.append( "FROM dataelement AS de " );
        sb.append( "INNER JOIN categorycombos_optioncombos cat_opts on de.categorycomboid = cat_opts.categorycomboid " );
        sb.append( "INNER JOIN _categoryoptioncomboname cc on cat_opts.categoryoptioncomboid = cc.categoryoptioncomboid " );
        sb.append( "ORDER BY DataElement) " );
        return sb.toString();

    }

    public String queryRawDataElementsForOrgUnitBetweenPeriods(Integer orgUnitId, List<Integer> betweenPeriodIds)
    {
        StringBuffer sqlsb = new StringBuffer();

        int i = 0;
        for ( Integer periodId : betweenPeriodIds )
        {
            i++;

            sqlsb.append( "SELECT de.dataelementid, (de.name || ' ' || cc.categoryoptioncomboname) AS DataElement, dv.value AS counts_of_aggregated_values, p.periodid AS PeriodId, p.startDate AS ColumnHeader " );
            sqlsb.append( "FROM dataelement AS de " );
            sqlsb.append( "INNER JOIN datavalue AS dv ON (de.dataelementid = dv.dataelementid) " );
            sqlsb.append( "INNER JOIN period p ON (dv.periodid = p.periodid) " );
            sqlsb.append( "INNER JOIN categorycombos_optioncombos cat_opts on de.categorycomboid = cat_opts.categorycomboid ");
            sqlsb.append( "INNER JOIN _categoryoptioncomboname cc on cat_opts.categoryoptioncomboid = cc.categoryoptioncomboid ");
            sqlsb.append( "WHERE dv.sourceid = '" + orgUnitId + "' " );
            sqlsb.append( "AND dv.periodid = '" + periodId + "' " );

            sqlsb.append( i == betweenPeriodIds.size() ? "ORDER BY ColumnHeader,dataelement" : " UNION " );
        }
        return sqlsb.toString();
    }
          
    public String limitRecord( int min, int max )
    {
        return " LIMIT " + max + " OFFSET " + min;
    }

    public String getAddDate( String dateField, int days )
    {
        return "DATEADD('DAY'," + days + "," + dateField + ")";
    }
    
    public String getPatientFullName()
    {
        return  "concat( firstname, \" \",middleName , \" \" , lastname)";
    }
}
