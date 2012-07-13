package org.hisp.dhis.reportsheet.exporting.action;

/*
 * Copyright (c) 2004-2011, University of Oslo
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.reportsheet.ExportItem;
import org.hisp.dhis.reportsheet.ExportReport;
import org.hisp.dhis.reportsheet.ExportReportOrganizationGroupListing;
import org.hisp.dhis.reportsheet.exporting.AbstractGenerateExcelReportSupport;
import org.hisp.dhis.reportsheet.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Tran Thanh Tri
 * @author Dang Duy Hieu
 * @version $Id$
 * @since 2009-09-18
 */
public class GenerateReportOrgGroupListingAction
    extends AbstractGenerateExcelReportSupport
{
    private static final String PREFIX_FORMULA_SUM = "SUM(";

    private Map<Integer, List<OrganisationUnit>> childrenGroupMap = new HashMap<Integer, List<OrganisationUnit>>();

    // -------------------------------------------------------------------------
    // Dependency
    // -------------------------------------------------------------------------

    @Autowired
    private OrganisationUnitService organisationUnitService;

    // -------------------------------------------------------------------------
    // Override
    // -------------------------------------------------------------------------

    @Override
    protected void executeGenerateOutputFile( ExportReport exportReport, Period period )
        throws Exception
    {
        OrganisationUnit organisationUnit = organisationUnitSelectionManager.getSelectedOrganisationUnit();

        ExportReportOrganizationGroupListing exportReportInstance = (ExportReportOrganizationGroupListing) exportReport;

        Map<OrganisationUnitGroup, OrganisationUnitLevel> orgUnitGroupAtLevels = exportReportInstance
            .getOrganisationUnitLevels();

        prepareChildrenGroupMap( exportReportInstance, organisationUnit, orgUnitGroupAtLevels );

        this.installReadTemplateFile( exportReportInstance, period, organisationUnit );

        Collection<ExportItem> exportReportItems = null;

        for ( Integer sheetNo : exportReportService.getSheets( selectionManager.getSelectedReportId() ) )
        {
            Sheet sheet = this.templateWorkbook.getSheetAt( sheetNo - 1 );

            exportReportItems = exportReportInstance.getExportItemBySheet( sheetNo );

            generateOutPutFile( exportReportInstance, exportReportItems, sheet );
        }

        /**
         * Garbage
         */
        exportReportItems = null;
        orgUnitGroupAtLevels = null;
        childrenGroupMap = null;
    }

    // -------------------------------------------------------------------------
    // Supportive method
    // -------------------------------------------------------------------------

    private void prepareChildrenGroupMap( ExportReportOrganizationGroupListing exportReport,
        OrganisationUnit organisationUnit, Map<OrganisationUnitGroup, OrganisationUnitLevel> orgUnitGroupAtLevels )
    {
        OrganisationUnitLevel organisationUnitLevel = null;
        Collection<OrganisationUnit> membersOfGroup = null;
        Collection<OrganisationUnit> organisationUnitsAtLevel = null;
        Collection<OrganisationUnit> childrens = organisationUnit.getChildren();

        for ( OrganisationUnitGroup organisationUnitGroup : exportReport.getOrganisationUnitGroups() )
        {
            List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();

            membersOfGroup = organisationUnitGroup.getMembers();

            if ( membersOfGroup != null && !membersOfGroup.isEmpty() )
            {
                organisationUnits.addAll( membersOfGroup );
            }

            organisationUnitLevel = orgUnitGroupAtLevels.get( organisationUnitGroup );

            if ( organisationUnitLevel != null )
            {
                organisationUnitsAtLevel = organisationUnitService.getOrganisationUnitsAtLevel( organisationUnitLevel
                    .getLevel(), organisationUnit );

                organisationUnits.retainAll( organisationUnitsAtLevel );
            }
            else
            {
                organisationUnits.retainAll( childrens );
            }

            Collections.sort( organisationUnits, new IdentifiableObjectNameComparator() );

            childrenGroupMap.put( organisationUnitGroup.getId(), organisationUnits );
        }

        /**
         * Garbage
         */
        childrens = null;
        membersOfGroup = null;
        organisationUnitLevel = null;
        organisationUnitsAtLevel = null;
    }

    private void generateOutPutFile( ExportReportOrganizationGroupListing exportReport,
        Collection<ExportItem> exportReportItems, Sheet sheet )
    {
        List<OrganisationUnit> organisationUnits = null;

        for ( ExportItem reportItem : exportReportItems )
        {
            int run = 0;
            int next = 0;
            int chapperNo = 0;
            int firstRow = reportItem.getRow();
            int rowBegin = firstRow + 1;

            String totalFormula = PREFIX_FORMULA_SUM;

            for ( OrganisationUnitGroup organisationUnitGroup : exportReport.getOrganisationUnitGroups() )
            {
                int beginChapter = rowBegin;

                organisationUnits = childrenGroupMap.get( organisationUnitGroup.getId() );

                if ( reportItem.getItemType().equalsIgnoreCase( ExportItem.TYPE.ORGANISATION )
                    && (!organisationUnits.isEmpty()) )
                {
                    ExcelUtils.writeValueByPOI( rowBegin, reportItem.getColumn(), organisationUnitGroup.getName(),
                        ExcelUtils.TEXT, sheet, this.csText12BoldCenter );
                }
                else if ( reportItem.getItemType().equalsIgnoreCase( ExportItem.TYPE.SERIAL )
                    && (!organisationUnits.isEmpty()) )
                {
                    ExcelUtils.writeValueByPOI( rowBegin, reportItem.getColumn(), chappter[chapperNo++],
                        ExcelUtils.TEXT, sheet, this.csText12BoldCenter );
                }

                run++;
                rowBegin++;
                int serial = 1;

                for ( OrganisationUnit o : organisationUnits )
                {
                    if ( reportItem.getItemType().equalsIgnoreCase( ExportItem.TYPE.ORGANISATION ) )
                    {
                        ExcelUtils.writeValueByPOI( rowBegin, reportItem.getColumn(), o.getName(), ExcelUtils.TEXT,
                            sheet, this.csText10Bold );
                    }
                    else if ( reportItem.getItemType().equalsIgnoreCase( ExportItem.TYPE.SERIAL ) )
                    {
                        ExcelUtils.writeValueByPOI( rowBegin, reportItem.getColumn(), serial + "", ExcelUtils.NUMBER,
                            sheet, this.csTextSerial );
                    }
                    else if ( reportItem.getItemType().equalsIgnoreCase( ExportItem.TYPE.DATAELEMENT ) )
                    {
                        double value = this.getDataValue( reportItem, o );

                        ExcelUtils.writeValueByPOI( rowBegin, reportItem.getColumn(), value + "", ExcelUtils.NUMBER,
                            sheet, this.csNumber );
                    }
                    else if ( reportItem.getItemType().equalsIgnoreCase( ExportItem.TYPE.INDICATOR ) )
                    {
                        double value = this.getIndicatorValue( reportItem, o );

                        ExcelUtils.writeValueByPOI( rowBegin, reportItem.getColumn(), value + "", ExcelUtils.NUMBER,
                            sheet, this.csNumber );
                    }
                    else
                    // FORMULA_EXCEL
                    {
                        ExcelUtils.writeFormulaByPOI( rowBegin, reportItem.getColumn(), ExcelUtils
                            .generateExcelFormula( reportItem.getExpression(), run, run ), sheet, this.csFormula );
                    }

                    run++;
                    rowBegin++;
                    serial++;
                }

                if ( !organisationUnits.isEmpty() )
                {
                    String formula = "";

                    if ( reportItem.getItemType().equalsIgnoreCase( ExportItem.TYPE.DATAELEMENT ) )
                    {
                        String columnName = ExcelUtils.convertColumnNumberToName( reportItem.getColumn() );
                        formula = "SUM(" + columnName + (beginChapter + 1) + ":" + columnName + (rowBegin - 1) + ")";

                        ExcelUtils.writeFormulaByPOI( beginChapter, reportItem.getColumn(), formula, sheet,
                            this.csFormula );

                        totalFormula += columnName + beginChapter + ",";
                    }
                    else if ( reportItem.getItemType().equalsIgnoreCase( ExportItem.TYPE.INDICATOR ) )
                    {
                        formula = ExcelUtils.generateExcelFormula( reportItem.getExtraExpression(), next + 1, 0 );

                        ExcelUtils.writeFormulaByPOI( beginChapter, reportItem.getColumn(), formula, sheet,
                            this.csFormula );
                    }
                }

                next = run;
            }

            if ( (reportItem.getItemType().equalsIgnoreCase( ExportItem.TYPE.DATAELEMENT ))
                && !totalFormula.equals( PREFIX_FORMULA_SUM ) )
            {
                totalFormula = totalFormula.substring( 0, totalFormula.length() - 1 ) + ")";

                ExcelUtils.writeFormulaByPOI( firstRow, reportItem.getColumn(), totalFormula, sheet, this.csFormula );
            }
            else if ( reportItem.getItemType().equalsIgnoreCase( ExportItem.TYPE.INDICATOR ) )
            {
                totalFormula = ExcelUtils.generateExcelFormula( reportItem.getExtraExpression(), 0, 0 );

                ExcelUtils.writeFormulaByPOI( firstRow, reportItem.getColumn(), totalFormula, sheet, this.csFormula );
            }
        }

        organisationUnits = null;
    }
}
