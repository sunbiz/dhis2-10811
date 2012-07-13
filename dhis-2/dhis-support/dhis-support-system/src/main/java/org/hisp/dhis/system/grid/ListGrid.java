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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.GridHeader;
import org.hisp.dhis.common.adapter.JacksonRowDataSerializer;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.system.util.MathUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static org.hisp.dhis.system.util.MathUtils.getRounded;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ListGrid
    implements Grid
{
    private static final String REGRESSION_SUFFIX = "_regression";
    private static final String CUMULATIVE_SUFFIX = "_cumulative";

    /**
     * The title of the grid.
     */
    private String title;

    /**
     * The subtitle of the grid.
     */
    private String subtitle;

    /**
     * The name of a potential corresponding table.
     */
    private String table;

    /**
     * A List which represents the column headers of the grid.
     */
    private List<GridHeader> headers;

    /**
     * A two dimensional List which simulates a grid where the first list
     * represents rows and the second represents columns.
     */
    private List<List<Object>> grid;

    /**
     * Indicating the current row in the grid for writing data.
     */
    private int currentRowWriteIndex = -1;

    /**
     * Indicating the current row in the grid for reading data.
     */
    private int currentRowReadIndex = -1;

    /**
     * Represents a mapping between column names and the index of the column in the grid.
     */
    private Map<String, Integer> columnIndexMap = new HashMap<String, Integer>();

    /**
     * Default constructor.
     */
    public ListGrid()
    {
        headers = new ArrayList<GridHeader>();
        grid = new ArrayList<List<Object>>();
    }

    // ---------------------------------------------------------------------
    // Public methods
    // ---------------------------------------------------------------------

    @JsonProperty
    @JsonView( { DetailedView.class } )
    public String getTitle()
    {
        return title;
    }

    public Grid setTitle( String title )
    {
        this.title = title;

        return this;
    }

    @JsonProperty
    @JsonView( { DetailedView.class } )
    public String getSubtitle()
    {
        return subtitle;
    }

    public Grid setSubtitle( String subtitle )
    {
        this.subtitle = subtitle;

        return this;
    }

    @JsonProperty
    @JsonView( { DetailedView.class } )
    public String getTable()
    {
        return table;
    }

    public Grid setTable( String table )
    {
        this.table = table;

        return this;
    }

    public Grid addHeader( GridHeader header )
    {
        headers.add( header );

        updateColumnIndexMap();

        return this;
    }

    @JsonProperty
    @JsonView( { DetailedView.class } )
    public List<GridHeader> getHeaders()
    {
        return headers;
    }

    public List<GridHeader> getVisibleHeaders()
    {
        List<GridHeader> tempHeaders = new ArrayList<GridHeader>();

        for ( GridHeader header : headers )
        {
            if ( !header.isHidden() )
            {
                tempHeaders.add( header );
            }
        }

        return tempHeaders;
    }

    @JsonProperty
    @JsonView( { DetailedView.class } )
    public int getHeight()
    {
        return (grid != null && grid.size() > 0) ? grid.size() : 0;
    }

    @JsonProperty
    @JsonView( { DetailedView.class } )
    public int getWidth()
    {
        verifyGridState();

        return (grid != null && grid.size() > 0) ? grid.get( 0 ).size() : 0;
    }

    public int getVisibleWidth()
    {
        verifyGridState();

        return (grid != null && grid.size() > 0) ? getVisibleRows().get( 0 ).size() : 0;
    }

    public Grid addRow()
    {
        grid.add( new ArrayList<Object>() );

        currentRowWriteIndex++;

        return this;
    }

    public Grid addValue( Object value )
    {
        grid.get( currentRowWriteIndex ).add( value );

        return this;
    }

    public List<Object> getRow( int rowIndex )
    {
        return grid.get( rowIndex );
    }

    @JsonProperty
    @JsonSerialize( using = JacksonRowDataSerializer.class )
    @JsonView( { DetailedView.class } )
    public List<List<Object>> getRows()
    {
        return grid;
    }

    public List<List<Object>> getVisibleRows()
    {
        verifyGridState();

        List<List<Object>> tempGrid = new ArrayList<List<Object>>();

        if ( headers != null && headers.size() > 0 )
        {
            for ( List<Object> row : grid )
            {
                List<Object> tempRow = new ArrayList<Object>();

                for ( int i = 0; i < row.size(); i++ )
                {
                    if ( !headers.get( i ).isHidden() )
                    {
                        tempRow.add( row.get( i ) );
                    }
                }

                tempGrid.add( tempRow );
            }
        }

        return tempGrid;
    }

    public List<Object> getColumn( int columnIndex )
    {
        List<Object> column = new ArrayList<Object>();

        for ( List<Object> row : grid )
        {
            column.add( row.get( columnIndex ) );
        }

        return column;
    }

    public Object getValue( int rowIndex, int columnIndex )
    {
        if ( grid.size() < rowIndex || grid.get( rowIndex ) == null || grid.get( rowIndex ).size() < columnIndex )
        {
            throw new IllegalArgumentException( "Grid does not contain the requested row / column" );
        }

        return grid.get( rowIndex ).get( columnIndex );
    }

    public Grid addColumn( List<Object> columnValues )
    {
        verifyGridState();

        int rowIndex = 0;
        int columnIndex = 0;

        if ( grid.size() != columnValues.size() )
        {
            throw new IllegalStateException( "Number of column values (" + columnValues.size() + ") is not equal to number of rows (" + grid.size() + ")" );
        }

        for ( int i = 0; i < grid.size(); i++ )
        {
            grid.get( rowIndex++ ).add( columnValues.get( columnIndex++ ) );
        }

        return this;
    }

    public Grid removeColumn( int columnIndex )
    {
        verifyGridState();

        if ( headers.size() > 0 )
        {
            headers.remove( columnIndex );
        }

        for ( List<Object> row : grid )
        {
            row.remove( columnIndex );
        }

        updateColumnIndexMap();

        return this;
    }

    public Grid removeColumn( GridHeader header )
    {
        int index = headers.indexOf( header );

        if ( index != -1 )
        {
            removeColumn( index );
        }

        return this;
    }

    public Grid limitGrid( int limit )
    {
        if ( limit < 0 )
        {
            throw new IllegalStateException( "Illegal limit: " + limit );
        }

        if ( limit > 0 && limit <= getHeight() )
        {
            grid = grid.subList( 0, limit );
        }

        return this;
    }

    public Grid limitGrid( int startPos, int endPos )
    {
        if ( startPos < 0 || endPos < startPos || endPos > getHeight() )
        {
            throw new IllegalStateException( "Illegal start / end pos: " + startPos + ", " + endPos + ", " + getHeight() );
        }

        grid = grid.subList( startPos, endPos );

        return this;
    }

    public Grid sortGrid( int columnIndex, int order )
    {
        if ( order == 0 )
        {
            return this; // No sorting
        }

        columnIndex = columnIndex - 1;

        if ( columnIndex < 0 || columnIndex >= getWidth() )
        {
            throw new IllegalArgumentException( "Column index out of bounds: " + columnIndex );
        }

        Collections.sort( grid, new GridRowComparator( columnIndex, order ) );

        return this;
    }

    public Grid addRegressionColumn( int columnIndex, boolean addHeader )
    {
        verifyGridState();

        SimpleRegression regression = new SimpleRegression();

        List<Object> column = getColumn( columnIndex );

        int index = 0;

        for ( Object value : column )
        {
            // 0 omitted from regression

            if ( value != null && !MathUtils.isEqual( Double.parseDouble( String.valueOf( value ) ), 0d ) )
            {
                regression.addData( index++, Double.parseDouble( String.valueOf( value ) ) );
            }
        }

        List<Object> regressionColumn = new ArrayList<Object>();

        for ( int i = 0; i < column.size(); i++ )
        {
            final double predicted = regression.predict( i );

            // Enough values must exist for regression

            if ( !Double.isNaN( predicted ) )
            {
                regressionColumn.add( getRounded( predicted, 1 ) );
            }
            else
            {
                regressionColumn.add( null );
            }
        }

        addColumn( regressionColumn );

        if ( addHeader && columnIndex < headers.size() )
        {
            GridHeader header = headers.get( columnIndex );

            if ( header != null )
            {
                GridHeader regressionHeader = new GridHeader( header.getName() + REGRESSION_SUFFIX,
                    header.getColumn() + REGRESSION_SUFFIX, header.getType(), header.isHidden(), header.isMeta() );

                addHeader( regressionHeader );
            }
        }

        return this;
    }

    public Grid addCumulativeColumn( int columnIndex, boolean addHeader )
    {
        verifyGridState();

        List<Object> column = getColumn( columnIndex );

        List<Object> cumulativeColumn = new ArrayList<Object>();

        double sum = 0d;

        for ( Object value : column )
        {
            double number = value != null ? Double.parseDouble( String.valueOf( value ) ) : 0d;

            sum += number;

            cumulativeColumn.add( sum );
        }

        addColumn( cumulativeColumn );

        if ( addHeader && columnIndex < headers.size() )
        {
            GridHeader header = headers.get( columnIndex );

            if ( header != null )
            {
                GridHeader regressionHeader = new GridHeader( header.getName() + CUMULATIVE_SUFFIX,
                    header.getColumn() + CUMULATIVE_SUFFIX, header.getType(), header.isHidden(), header.isMeta() );

                addHeader( regressionHeader );
            }
        }

        return this;
    }

    // -------------------------------------------------------------------------
    // JRDataSource implementation
    // -------------------------------------------------------------------------

    public boolean next()
        throws JRException
    {
        boolean next = ++currentRowReadIndex < getHeight();

        if ( !next )
        {
            currentRowReadIndex = -1; // Reset and return false
        }

        return next;
    }

    public Object getFieldValue( JRField field )
        throws JRException
    {
        Integer index = columnIndexMap.get( field.getName() );

        return index != null ? getRow( currentRowReadIndex ).get( index ) : null;
    }

    // -------------------------------------------------------------------------
    // SQL utility methods
    // -------------------------------------------------------------------------

    public Grid addHeaders( ResultSet rs )
    {
        try
        {
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnNo = rsmd.getColumnCount();

            for ( int i = 1; i <= columnNo; i++ )
            {
                addHeader( new GridHeader( rsmd.getColumnLabel( i ), false, false ) );
            }
        } catch ( SQLException ex )
        {
            throw new RuntimeException( ex );
        }

        return this;
    }

    public Grid addRow( ResultSet rs )
    {
        try
        {
            int cols = rs.getMetaData().getColumnCount();

            while ( rs.next() )
            {
                addRow();

                for ( int i = 1; i <= cols; i++ )
                {
                    addValue( rs.getObject( i ) );
                }
            }
        } catch ( SQLException ex )
        {
            throw new RuntimeException( ex );
        }

        return this;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Verifies that all grid rows are of the same length, and that the number
     * of headers is the same as number of columns or 0.
     */
    private void verifyGridState()
    {
        Integer rowLength = null;

        for ( List<Object> row : grid )
        {
            if ( rowLength != null && rowLength != row.size() )
            {
                throw new IllegalStateException( "Grid rows do not have the same number of cells, previous: " + rowLength + ", this: " + row.size() );
            }

            rowLength = row.size();
        }
    }

    /**
     * Updates the mapping between header columns and grid indexes. This method
     * should be invoked whenever the columns are manipulated.
     */
    private void updateColumnIndexMap()
    {
        columnIndexMap.clear();

        for ( int i = 0; i < headers.size(); i++ )
        {
            columnIndexMap.put( headers.get( i ).getColumn(), i );
        }
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer( "[\n" );

        if ( headers != null && headers.size() > 0 )
        {
            List<String> headerNames = new ArrayList<String>();

            for ( GridHeader header : headers )
            {
                headerNames.add( header.getName() );
            }

            buffer.append( headerNames ).append( "\n" );
        }

        for ( List<Object> row : grid )
        {
            buffer.append( row ).append( "\n" );
        }

        return buffer.append( "]" ).toString();
    }

    // -------------------------------------------------------------------------
    // Comparator
    // -------------------------------------------------------------------------

    public static class GridRowComparator
        implements Comparator<List<Object>>
    {
        private int columnIndex;
        private int order;

        protected GridRowComparator( int columnIndex, int order )
        {
            this.columnIndex = columnIndex;
            this.order = order;
        }

        @Override
        @SuppressWarnings( "unchecked" )
        public int compare( List<Object> list1, List<Object> list2 )
        {
            boolean list1Invalid = list1 == null || list1.get( columnIndex ) == null || !(list1.get( columnIndex ) instanceof Comparable<?>);
            boolean list2Invalid = list2 == null || list2.get( columnIndex ) == null || !(list2.get( columnIndex ) instanceof Comparable<?>);

            if ( list1Invalid && list2Invalid )
            {
                return 0;
            }
            else if ( list1Invalid )
            {
                return order > 0 ? 1 : -1;
            }
            else if ( list2Invalid )
            {
                return order > 0 ? -1 : 1;
            }

            final Comparable<Object> value1 = (Comparable<Object>) list1.get( columnIndex );
            final Comparable<Object> value2 = (Comparable<Object>) list2.get( columnIndex );

            return order > 0 ? value2.compareTo( value1 ) : value1.compareTo( value2 );
        }
    }
}
