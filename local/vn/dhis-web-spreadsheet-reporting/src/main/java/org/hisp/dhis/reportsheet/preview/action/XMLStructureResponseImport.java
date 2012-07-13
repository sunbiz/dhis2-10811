package org.hisp.dhis.reportsheet.preview.action;

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

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.hisp.dhis.reportsheet.utils.ExcelUtils.convertAlignmentString;
import static org.hisp.dhis.reportsheet.utils.ExcelUtils.readValueByPOI;
import static org.hisp.dhis.reportsheet.utils.NumberUtils.PATTERN_DECIMAL_FORMAT1;
import static org.hisp.dhis.reportsheet.utils.NumberUtils.applyPatternDecimalFormat;
import static org.hisp.dhis.reportsheet.utils.NumberUtils.resetDecimalFormatByLocale;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hisp.dhis.reportsheet.importitem.ImportItem;

/**
 * 
 * @author Dang Duy Hieu
 * @version $Id XMLStructureResponse.java 2011-06-28 16:08:00$
 */

public class XMLStructureResponseImport
{
    /**
     * The encoding to write
     */
    private StringBuffer xml = new StringBuffer( 200000 );

    /**
     * The workbook we are reading from a given file
     */
    private Workbook WORKBOOK;

    private FormulaEvaluator evaluatorFormula;

    private static final String WORKBOOK_OPENTAG = "<workbook>";

    private static final String WORKBOOK_CLOSETAG = "</workbook>";

    private static final String MERGEDCELL_OPENTAG = "<MergedCells>";

    private static final String MERGEDCELL_CLOSETAG = "</MergedCells>";

    // -------------------------------------------------------------------------
    // Get & Set methods
    // -------------------------------------------------------------------------

    public String getXml()
    {
        return xml.toString();
    }

    /**
     * Constructor
     * 
     * @param importItems
     * 
     * @param w The workbook to interrogate
     * @param enc The encoding used by the output stream. Null or unrecognized
     *        values cause the encoding to default to UTF8
     * @param f Indicates whether the generated XML document should contain the
     *        cell format information
     * @exception java.io.IOException
     */

    public XMLStructureResponseImport( String pathFileName, Collection<Integer> collectSheets,
        List<ImportItem> importItems, boolean bWriteDescription )
        throws Exception
    {
        this.cleanUpForResponse();

        FileInputStream inputStream = new FileInputStream( pathFileName );

        if ( getExtension( pathFileName ).equals( "xls" ) )
        {
            this.WORKBOOK = new HSSFWorkbook( inputStream );
        }
        else
        {
            this.WORKBOOK = new XSSFWorkbook( inputStream );
        }

        resetDecimalFormatByLocale( Locale.GERMAN );
        applyPatternDecimalFormat( PATTERN_DECIMAL_FORMAT1 );

        this.evaluatorFormula = WORKBOOK.getCreationHelper().createFormulaEvaluator();

        this.writeFormattedXML( collectSheets, importItems, bWriteDescription );
    }

    // -------------------------------------------------------------------------
    // Private methods
    // -------------------------------------------------------------------------

    private void cleanUpForResponse()
    {
        System.gc();
    }

    private void writeFormattedXML( Collection<Integer> collectSheets, List<ImportItem> importItems,
        boolean bWriteDescription )
        throws Exception
    {
        if ( bWriteDescription )
        {
            this.writeXMLMergedDescription( collectSheets );
        }

        xml.append( WORKBOOK_OPENTAG );

        for ( Integer sheet : collectSheets )
        {
            this.writeData( sheet, importItems );
        }

        xml.append( WORKBOOK_CLOSETAG );
    }

    private void writeXMLMergedDescription( Collection<Integer> collectSheets )
        throws IOException
    {
        xml.append( MERGEDCELL_OPENTAG );

        for ( Integer sheet : collectSheets )
        {
            writeBySheetNo( sheet );
        }

        xml.append( MERGEDCELL_CLOSETAG );
    }

    private void writeData( int sheetNo, List<ImportItem> importItems )
    {
        Sheet s = WORKBOOK.getSheetAt( sheetNo - 1 );

        xml.append( "<sheet id='" + (sheetNo) + "'>" );
        xml.append( "<name><![CDATA[" + s.getSheetName() + "]]></name>" );

        int run = 0;
        int i = 0;// Presented as row index
        int j = 0;// Presented as column index

        for ( Row row : s )
        {
            j = 0;

            xml.append( "<row index='" + i + "'>" );

            for ( Cell cell : row )
            {
                run = 0;

                // Remember that empty cells can contain format information
                if ( (cell.getCellStyle() != null) || cell.getCellType() != Cell.CELL_TYPE_BLANK )
                {
                    xml.append( "<col no='" + j + "'" );

                    for ( ImportItem importItem : importItems )
                    {
                        if ( (importItem.getSheetNo() == sheetNo) && (importItem.getRow() == (i + 1))
                            && (importItem.getColumn() == (j + 1)) )
                        {
                            xml.append( " id='" + importItem.getExpression() + "'>" );
                            // If there is any importItem matched the condition
                            // then break out the for loop
                            break;
                        }

                        run++;
                    }

                    if ( run == importItems.size() )
                    {
                        xml.append( ">" );
                    } // end checking

                    xml.append( "<data><![CDATA[" + readValueByPOI( i + 1, j + 1, s, evaluatorFormula ) + "]]></data>" );

                    this.readingDetailsFormattedCell( s, cell );

                    xml.append( "</col>" );
                }

                j++;
            }

            i++;

            xml.append( "</row>" );
        }
        xml.append( "</sheet>" );
    }

    private void writeBySheetNo( int sheetNo )
    {
        Sheet sheet = WORKBOOK.getSheetAt( sheetNo - 1 );
        CellRangeAddress cellRangeAddress = null;

        for ( int i = 0; i < sheet.getNumMergedRegions(); i++ )
        {
            cellRangeAddress = sheet.getMergedRegion( i );

            if ( cellRangeAddress.getFirstColumn() != cellRangeAddress.getLastColumn() )
            {
                xml.append( "<cell " + "iKey='" + (sheetNo) + "#" + cellRangeAddress.getFirstRow() + "#"
                    + cellRangeAddress.getFirstColumn() + "'>"
                    + (cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn() + 1) + "</cell>" );
            }
        }
    }

    private void readingDetailsFormattedCell( Sheet sheet, Cell objCell )
    {
        CellStyle format = objCell.getCellStyle();

        if ( format != null )
        {
            xml.append( "<format align='" + convertAlignmentString( format.getAlignment() ) + "'" );
            xml.append( " width='" + sheet.getColumnWidth( objCell.getColumnIndex() ) + "'" );
            xml.append( " border='" + format.getBorderBottom() + format.getBorderLeft() + format.getBorderRight()
                + format.getBorderTop() + "'/>" );
        }
    }
}