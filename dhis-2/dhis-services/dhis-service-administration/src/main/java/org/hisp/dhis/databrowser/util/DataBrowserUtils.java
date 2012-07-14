package org.hisp.dhis.databrowser.util;

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.GridHeader;
import org.hisp.dhis.databrowser.MetaValue;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Dang Duy Hieu
 * @version $Id$
 */
public class DataBrowserUtils
{
    protected static final Log log = LogFactory.getLog( DataBrowserUtils.class );

    public static void setMetaStructure( Grid grid, StringBuffer sqlsb, List<Integer> metaIds, JdbcTemplate jdbcTemplate )
    {
        try
        {
            Integer metaId = null;
            String metaName = null;
            ResultSet resultSet = getScrollableResult( sqlsb.toString(), jdbcTemplate );

            while ( resultSet.next() )
            {
                metaId = resultSet.getInt( 1 );
                metaName = resultSet.getString( 2 );

                metaIds.add( metaId );
                grid.addRow().addValue( new MetaValue( metaId, metaName ) );
            }
        }
        catch ( SQLException e )
        {
            log.error( "Failed to add meta value\n" + sqlsb.toString() );
            throw new RuntimeException( "Failed to add meta value\n", e );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Oops. Something else went wrong in setMetaStructure()", e );
        }
    }

    public static void setHeaderStructure( Grid grid, ResultSet resultSet, List<Integer> headerIds, boolean isZeroAdded )
    {
        try
        {
            Integer headerId = null;
            String headerName = null;

            while ( resultSet.next() )
            {
                headerId = resultSet.getInt( 4 );
                headerName = resultSet.getString( 5 );

                GridHeader header = new GridHeader( headerName, headerId + "", String.class.getName(), false, false );

                if ( !headerIds.contains( headerId ) )
                {
                    headerIds.add( headerId );
                    grid.addHeader( header );

                    for ( List<Object> row : grid.getRows() )
                    {
                        row.add( isZeroAdded ? "0" : "" );
                    }
                }
            }
        }
        catch ( SQLException e )
        {
            throw new RuntimeException( "Failed to add header\n", e );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Oops. Something else went wrong in setHeaderStructure()", e );
        }
    }

    public static int fillUpDataBasic( Grid grid, StringBuffer sqlsb, boolean isZeroAdded, JdbcTemplate jdbcTemplate )
    {
        int countRows = 0;

        try
        {
            ResultSet resultSet = getScrollableResult( sqlsb.toString(), jdbcTemplate );

            while ( resultSet.next() )
            {
                MetaValue metaValue = new MetaValue( resultSet.getInt( 1 ), resultSet.getString( 2 ) );

                grid.addRow().addValue( metaValue ).addValue( checkValue( resultSet.getString( 3 ), isZeroAdded ) );
            }
        }
        catch ( SQLException e )
        {
            log.error( "Error executing" + sqlsb.toString() );
            throw new RuntimeException( "Failed to get aggregated data value\n", e );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Oops. Something else went wrong", e );
        }

        return countRows;
    }

    public static int fillUpDataAdvance( Grid grid, StringBuffer sqlsb, List<Integer> metaIds, boolean isZeroAdded,
        JdbcTemplate jdbcTemplate )
    {
        int countRows = 0;
        int rowIndex = -1;
        int columnIndex = -1;
        int oldWidth = grid.getWidth();

        try
        {
            ResultSet rs = getScrollableResult( sqlsb.toString(), jdbcTemplate );

            List<Integer> headerIds = new ArrayList<Integer>();
            setHeaderStructure( grid, rs, headerIds, isZeroAdded );

            if ( rs.first() != true )
            {
                return countRows;
            }

            rs.beforeFirst();

            while ( rs.next() )
            {
                rowIndex = metaIds.indexOf( rs.getInt( 1 ) );
                columnIndex = headerIds.indexOf( rs.getInt( 4 ) ) + oldWidth;

                grid.getRow( rowIndex ).set( columnIndex, checkValue( rs.getString( 3 ), isZeroAdded ) );

                countRows++;
            }
        }
        catch ( SQLException e )
        {
            log.error( "Error executing" + sqlsb.toString() );
            throw new RuntimeException( "Failed to get aggregated data value\n", e );

        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Oops. Somthing else went wrong", e );
        }

        return countRows;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Uses StatementManager to obtain a scroll-able, read-only ResultSet based
     * on the query string.
     * 
     * @param sql the query
     * @param holder the StatementHolder object
     * @return null or the ResultSet
     */
    private static ResultSet getScrollableResult( String sql, JdbcTemplate jdbcTemplate )
        throws SQLException
    {
        Connection con = jdbcTemplate.getDataSource().getConnection();
        Statement stm = con.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
        stm.execute( sql );
        log.debug( sql );

        return stm.getResultSet();
    }

    private static String checkValue( String value, boolean isZeroAdded )
    {
        if ( value == null )
        {
            return "null";
        }
        return (value.equals( "0" ) && !isZeroAdded) ? "" : value;
    }
}
