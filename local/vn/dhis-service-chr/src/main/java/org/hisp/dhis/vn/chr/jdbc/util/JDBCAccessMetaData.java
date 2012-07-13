package org.hisp.dhis.vn.chr.jdbc.util;

/**
 * @author Chau Thu Tran
 * 
 */

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.amplecode.quick.StatementManager;

public class JDBCAccessMetaData
    implements AccessMetaDataService
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementManager statementManager;

    // -------------------------------------------------------------------------
    // Getter & Setter
    // -------------------------------------------------------------------------

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    // -------------------------------------------------------------------------
    // Implements
    // -------------------------------------------------------------------------

    /** Get All table */
    public Set<String> getAllTablesName()
    {
        Connection con = statementManager.getHolder().getConnection();

        Set<String> result = new HashSet<String>();

        try
        {

            DatabaseMetaData meta = con.getMetaData();

            ResultSet tables = meta.getTables( null, null, "%", null );

            while ( tables.next() )
            {
                String tableName = tables.getString( 3 );
                String tableType = tables.getString( 4 );
                if ( tableType != null && tableType.equalsIgnoreCase( "TABLE" ) )
                {
                    result.add( tableName.toLowerCase() );
                }

            }

            tables.close();

            con.close();
        }
        catch ( SQLException e1 )
        {
            e1.printStackTrace();
        }
        return result;
    }

    /** exist table */
    public boolean existTable( String tablename )
        throws SQLException
    {

        Iterator<String> iter = getAllTablesName().iterator();

        tablename = tablename.toLowerCase();

        while ( iter.hasNext() )
        {
            if ( iter.next().equals( tablename ) )
            {
                return true;
            }
        }

        return false;

    }

    /** Get All column */
    public Set<String> getAllColumnsOfTable( String tableName )
    {
        Connection con = statementManager.getHolder().getConnection();

        Set<String> result = new HashSet<String>();

        try
        {
            DatabaseMetaData meta = con.getMetaData();

            ResultSet columns = meta.getColumns( null, null, tableName, null );

            while ( columns.next() )
            {
                result.add( columns.getString( "COLUMN_NAME" ) );
            }

            columns.close();
            con.close();

        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return result;
    }

}
