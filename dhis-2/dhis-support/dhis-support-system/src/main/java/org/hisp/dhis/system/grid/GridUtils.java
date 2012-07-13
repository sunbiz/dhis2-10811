package org.hisp.dhis.system.grid;

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

import static org.hisp.dhis.system.util.CsvUtils.NEWLINE;
import static org.hisp.dhis.system.util.CsvUtils.SEPARATOR_B;
import static org.hisp.dhis.system.util.CsvUtils.csvEncode;
import static org.hisp.dhis.system.util.PDFUtils.addTableToDocument;
import static org.hisp.dhis.system.util.PDFUtils.closeDocument;
import static org.hisp.dhis.system.util.PDFUtils.getEmptyCell;
import static org.hisp.dhis.system.util.PDFUtils.getItalicCell;
import static org.hisp.dhis.system.util.PDFUtils.getSubtitleCell;
import static org.hisp.dhis.system.util.PDFUtils.getTextCell;
import static org.hisp.dhis.system.util.PDFUtils.getTitleCell;
import static org.hisp.dhis.system.util.PDFUtils.openDocument;
import static org.hisp.dhis.system.util.PDFUtils.resetPaddings;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.amplecode.staxwax.factory.XMLFactory;
import org.amplecode.staxwax.writer.XMLWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.GridHeader;
import org.hisp.dhis.system.util.CodecUtils;
import org.hisp.dhis.system.util.Encoder;
import org.hisp.dhis.system.util.ExcelUtils;
import org.hisp.dhis.system.util.MathUtils;
import org.hisp.dhis.system.util.StreamUtils;
import org.hisp.dhis.system.velocity.VelocityManager;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Lars Helge Overland
 */
public class GridUtils
{
    private static final String EMPTY = "";
    
    private static final String XLS_SHEET_PREFIX = "Sheet ";
    
    private static final WritableCellFormat XLS_FORMAT_TTTLE = new WritableCellFormat( new WritableFont(
        WritableFont.TAHOMA, 13, WritableFont.NO_BOLD, false ) );

    private static final WritableCellFormat XLS_FORMAT_SUBTTTLE = new WritableCellFormat( new WritableFont(
        WritableFont.TAHOMA, 12, WritableFont.NO_BOLD, false ) );
    
    private static final WritableCellFormat XLS_FORMAT_LABEL = new WritableCellFormat( new WritableFont(
        WritableFont.ARIAL, 11, WritableFont.NO_BOLD, true ) );
    
    private static final WritableCellFormat XLS_FORMAT_TEXT = new WritableCellFormat( new WritableFont( WritableFont.ARIAL,
        11, WritableFont.NO_BOLD, false ) );

    private static final Encoder ENCODER = new Encoder();
    
    private static final String KEY_GRID = "grid";
    private static final String KEY_ENCODER = "encoder";
    private static final String KEY_PARAMS = "params";
    private static final String JASPER_TEMPLATE = "grid.vm";
    private static final String HTML_TEMPLATE = "grid-html.vm";

    private static final String ATTR_GRID = "grid";
    private static final String ATTR_TITLE = "title";
    private static final String ATTR_SUBTITLE = "subtitle";
    private static final String ATTR_WIDTH = "width";
    private static final String ATTR_HEIGHT = "height";
    private static final String ATTR_HEADERS = "headers";
    private static final String ATTR_HEADER = "header";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_COLUMN = "column";
    private static final String ATTR_TYPE = "type";
    private static final String ATTR_HIDDEN = "hidden";
    private static final String ATTR_META = "meta";
    private static final String ATTR_ROWS = "rows";
    private static final String ATTR_ROW = "row";
    private static final String ATTR_FIELD = "field";
    
    /**
     * Writes a PDF representation of the given Grid to the given OutputStream.
     */
    public static void toPdf( Grid grid, OutputStream out )
    {
        if ( isNonEmptyGrid( grid ) )
        {
            Document document = openDocument( out );
            
            toPdfInternal( grid, document, 0F );
    
            closeDocument( document );
        }
    }

    /**
     * Writes a PDF representation of the given list of Grids to the given OutputStream.
     */
    public static void toPdf( List<Grid> grids, OutputStream out )
    {
        if ( hasNonEmptyGrid( grids ) )
        {
            Document document = openDocument( out );
            
            for ( Grid grid : grids )
            {
                toPdfInternal( grid, document, 40F );
            }
            
            closeDocument( document );
        }
    }
    
    private static void toPdfInternal( Grid grid, Document document, float spacing )
    {
        if ( grid == null || grid.getVisibleWidth() == 0 )
        {
            return;
        }
        
        PdfPTable table = new PdfPTable( grid.getVisibleWidth() );

        table.setHeaderRows( 1 );
        table.setWidthPercentage( 100F );
        table.setKeepTogether( false );
        table.setSpacingAfter( spacing );

        table.addCell( resetPaddings( getTitleCell( grid.getTitle(), grid.getVisibleWidth() ), 0, 30, 0, 0 ) );
        
        if ( StringUtils.isNotEmpty( grid.getSubtitle() ) )
        {
            table.addCell( getSubtitleCell( grid.getSubtitle(), grid.getVisibleWidth() ) );
            table.addCell( getEmptyCell( grid.getVisibleWidth(), 30 ) );
        }        

        for ( GridHeader header : grid.getVisibleHeaders() )
        {
            table.addCell( getItalicCell( header.getName() ) );
        }

        table.addCell( getEmptyCell( grid.getVisibleWidth(), 10 ) );

        for ( List<Object> row : grid.getVisibleRows() )
        {
            for ( Object col : row )
            {
                table.addCell( getTextCell( col ) );
            }
        }

        addTableToDocument( document, table );
    }

    /**
     * Writes a XLS (Excel workbook) representation of the given list of Grids to the given OutputStream.
     */
    public static void toXls( List<Grid> grids, OutputStream out )
        throws Exception
    {
        WritableWorkbook workbook = ExcelUtils.openWorkbook( out );
        
        for ( int i = 0; i < grids.size(); i++ )
        {
            Grid grid = grids.get( i );
            
            String sheetName = CodecUtils.filenameEncode( StringUtils.defaultIfEmpty( grid.getTitle(), XLS_SHEET_PREFIX + ( i + 1 ) ) );

            toXlsInternal( grid, workbook, sheetName, i );
        }

        workbook.write();
        workbook.close();        
    }
    
    /**
     * Writes a XLS (Excel workbook) representation of the given Grid to the given OutputStream.
     */
    public static void toXls( Grid grid, OutputStream out )
        throws Exception
    {
        WritableWorkbook workbook = ExcelUtils.openWorkbook( out );
        
        String sheetName = CodecUtils.filenameEncode( StringUtils.defaultIfEmpty( grid.getTitle(), XLS_SHEET_PREFIX + 1 ) );

        toXlsInternal( grid, workbook, sheetName, 0 );

        workbook.write();
        workbook.close();
    }
    
    private static void toXlsInternal( Grid grid, WritableWorkbook workbook, String sheetName, int sheetNo )
        throws Exception
    {
        if ( grid == null )
        {
            return;
        }
        
        WritableSheet sheet = workbook.createSheet( sheetName, sheetNo );

        int rowNumber = 1;

        int columnIndex = 0;

        sheet.addCell( new Label( 0, rowNumber++, grid.getTitle(), XLS_FORMAT_TTTLE ) );

        rowNumber++;

        if ( StringUtils.isNotEmpty( grid.getSubtitle() ) )
        {
            sheet.addCell( new Label( 0, rowNumber++, grid.getSubtitle(), XLS_FORMAT_SUBTTTLE ) );
            
            rowNumber++;
        }
        
        for ( GridHeader header : grid.getVisibleHeaders() )
        {
            sheet.addCell( new Label( columnIndex++, rowNumber, header.getName(), XLS_FORMAT_LABEL ) );
        }

        rowNumber++;

        for ( List<Object> row : grid.getVisibleRows() )
        {
            columnIndex = 0;

            for ( Object column : row )
            {
                if ( column != null && MathUtils.isNumeric( String.valueOf( column ) ) )
                {
                    sheet.addCell( new Number( columnIndex++, rowNumber, Double.valueOf( String.valueOf( column ) ), XLS_FORMAT_TEXT ) );
                }
                else
                {
                    String content = column != null ? String.valueOf( column ) : EMPTY;
                    
                    sheet.addCell( new Label( columnIndex++, rowNumber, content, XLS_FORMAT_TEXT ) );
                }
            }

            rowNumber++;
        }
    }

    /**
     * Writes a CSV representation of the given Grid to the given OutputStream.
     */
    public static void toCsv( Grid grid, OutputStream out )
        throws Exception
    {
        if ( grid == null )
        {
            return;
        }
        
        Iterator<GridHeader> headers = grid.getHeaders().iterator();
        
        while ( headers.hasNext() )
        {
            out.write( csvEncode( headers.next().getName() ).getBytes() );
            
            if ( headers.hasNext() )
            {
                out.write( SEPARATOR_B );
            }
        }

        out.write( NEWLINE );
        
        for ( List<Object> row : grid.getRows() )
        {
            Iterator<Object> columns = row.iterator();
            
            while ( columns.hasNext() )
            {
                out.write( csvEncode( columns.next() ).getBytes() );
                
                if ( columns.hasNext() )
                {
                    out.write( SEPARATOR_B );
                }
            }
            
            out.write( NEWLINE );
        }
    }

    /**
     * Writes a Jasper Reports representation of the given Grid to the given OutputStream.
     */
    public static void toJasperReport( Grid grid, Map<String, Object> params, OutputStream out )
        throws Exception
    {
        if ( grid == null )
        {
            return;
        }
        
        final StringWriter writer = new StringWriter();
        
        render( grid, params, writer, JASPER_TEMPLATE );
        
        String report = writer.toString();

        JasperReport jasperReport = JasperCompileManager.compileReport( StreamUtils.getInputStream( report ) );
        
        JasperPrint print = JasperFillManager.fillReport( jasperReport, params, grid );
        
        JasperExportManager.exportReportToPdfStream( print, out );
    }

    /**
     * Writes a JRXML (Jasper Reports XML) representation of the given Grid to the given Writer.
     */
    public static void toJrxml( Grid grid, Map<?, ?> params, Writer writer )
        throws Exception
    {
        render( grid, params, writer, JASPER_TEMPLATE );
    }

    /**
     * Writes a JRXML (Jasper Reports XML) representation of the given Grid to the given Writer.
     */
    public static void toHtml( Grid grid, Writer writer )
        throws Exception
    {
        render( grid, null, writer, HTML_TEMPLATE );
    }
    
    /**
     * Writes an XML representation of the given Grid to the given OutputStream.
     */
    public static void toXml( Grid grid, OutputStream out )
    {
        XMLWriter writer = XMLFactory.getXMLWriter( out );
        
        writer.openDocument();
        writer.openElement( ATTR_GRID, ATTR_TITLE, grid.getTitle(), ATTR_SUBTITLE, grid.getSubtitle(), 
            ATTR_WIDTH, String.valueOf( grid.getWidth() ), ATTR_HEIGHT, String.valueOf( grid.getHeight() ) );
        
        writer.openElement( ATTR_HEADERS );
        
        for ( GridHeader header : grid.getHeaders() )
        {
            writer.writeElement( ATTR_HEADER, null, ATTR_NAME, header.getName(), ATTR_COLUMN, header.getColumn(), 
                ATTR_TYPE, header.getType(), ATTR_HIDDEN, String.valueOf( header.isHidden() ), ATTR_META, String.valueOf( header.isMeta() ) ); 
        }
        
        writer.closeElement();        
        writer.openElement( ATTR_ROWS );
        
        for ( List<Object> row : grid.getRows() )
        {
            writer.openElement( ATTR_ROW );
            
            for ( Object field : row )
            {
                writer.writeElement( ATTR_FIELD, field != null ? String.valueOf( field ) : EMPTY );
            }
            
            writer.closeElement();
        }
        
        writer.closeElement();
        writer.closeElement();
        
        writer.closeDocument();
    }
    
    /**
     * Writes all rows in the SqlRowSet to the given Grid.
     */
    public static void addRows( Grid grid, SqlRowSet rs )
    {
        int cols = rs.getMetaData().getColumnCount();
        
        while ( rs.next() )
        {
            grid.addRow();
            
            for ( int i = 1; i <= cols; i++ )
            {
                grid.addValue( rs.getObject( i ) );
            }
        }
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Render using Velocity.
     */
    private static void render( Grid grid, Map<?, ?> params, Writer writer, String template )
        throws Exception
    {
        final VelocityContext context = new VelocityContext();
        
        context.put( KEY_GRID, grid );
        context.put( KEY_ENCODER, ENCODER );
        context.put( KEY_PARAMS, params );
        
        new VelocityManager().getEngine().getTemplate( template ).merge( context, writer );
    }
    
    /**
     * Indicates whether the given list of grids have at least one grid which is 
     * not null and has more than zero visible columns.
     */
    private static boolean hasNonEmptyGrid( List<Grid> grids )
    {
        if ( grids != null && grids.size() > 0 )
        {
            for ( Grid grid : grids )
            {
                if ( isNonEmptyGrid( grid ) )
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Indicates whether grid is not null and has more than zero visible columns.
     */
    private static boolean isNonEmptyGrid( Grid grid )
    {
        return grid != null && grid.getVisibleWidth() > 0;
    }
}
