package org.hisp.dhis.vn.chr.jdbc;

/**
 * @author Chau Thu Tran
 * 
 */

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Set;

import org.amplecode.quick.StatementHolder;
import org.amplecode.quick.StatementManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.ElementService;
import org.hisp.dhis.vn.chr.Form;
import org.hisp.dhis.vn.chr.FormReport;
import org.hisp.dhis.vn.chr.jdbc.util.AccessMetaDataService;
import org.hisp.dhis.vn.chr.statement.AddDataStatement;
import org.hisp.dhis.vn.chr.statement.AlterColumnStatement;
import org.hisp.dhis.vn.chr.statement.CreateCodeStatement;
import org.hisp.dhis.vn.chr.statement.CreateTableStatement;
import org.hisp.dhis.vn.chr.statement.DeleteDataStatement;
import org.hisp.dhis.vn.chr.statement.FormStatement;
import org.hisp.dhis.vn.chr.statement.GetDataStatement;
import org.hisp.dhis.vn.chr.statement.ListDataStatement;
import org.hisp.dhis.vn.chr.statement.ListRelativeDataStatement;
import org.hisp.dhis.vn.chr.statement.ReportDataStatement;
import org.hisp.dhis.vn.chr.statement.SearchDataStatement;
import org.hisp.dhis.vn.chr.statement.UpdateDataStatement;

public class JDBCFormManager
    implements FormManager
{

    private static final Log log = LogFactory.getLog( JDBCFormManager.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }

    private StatementBuilder statementBuilder;

    public void setStatementBuilder( StatementBuilder statementBuilder )
    {
        this.statementBuilder = statementBuilder;
    }

    private AccessMetaDataService accessMetaDataService;

    public void setAccessMetaDataService( AccessMetaDataService accessMetaDataService )
    {
        this.accessMetaDataService = accessMetaDataService;
    }

    private ElementService elementService;

    public void setElementService( ElementService elementService )
    {
        this.elementService = elementService;
    }

    // -------------------------------------------------------------------------
    // Implements
    // -------------------------------------------------------------------------

    /**
     * Create table
     * 
     * @param form needs to create table
     * 
     */
    public void createTable( Form form )
    {
        StatementHolder holder = statementManager.getHolder();

        String allStatement = "";

        try
        {

            // table is exist
            if ( form.isCreated() )
            {
                // if
                // (accessMetaDataService.existTable(form.getName().toLowerCase()))
                // { // exist table

                // Initial statement
                FormStatement statement = null;

                // Get columns into the table
                Set<String> columns = accessMetaDataService.getAllColumnsOfTable( form.getName().toLowerCase() );

                columns.remove( "id" );
                columns.remove( "addby" );
                columns.remove( "createddate" );
                columns.remove( "editeddate" );

                // Add or Alter columns into table
                for ( Element element : form.getElements() )
                {
                    // add the column in to alter list

                    if ( columns.contains( element.getName().toLowerCase() ) )
                    {

                        statement = new AlterColumnStatement( statementBuilder, AlterColumnStatement.ALTER_STATUS,
                            element );

                        log.debug( "Alter column with SQL statement: '" + statement.getStatement() + "'" );
                    }

                    // if column is not exist in table, add column
                    // add column in to add list
                    else
                    // if(!columns.contains(element.getName()))
                    {
                        statement = new AlterColumnStatement( statementBuilder, AlterColumnStatement.ADD_STATUS,
                            element );

                        log.debug( "Add column with SQL statement: '" + statement.getStatement() + "'" );
                    }
                    allStatement += statement.getStatement();

                }// end column

                // if column is exist in table (elements exist in table, not in
                // form),
                // but not exist in form.elements,
                // delete column in table
                // add column delete list
                for ( String column : columns )
                {

                    Element element = elementService.getElement( column );

                    if ( element == null )
                    {

                        statement = new AlterColumnStatement( form, statementBuilder, AlterColumnStatement.DROP_STATUS,
                            column );

                        allStatement += statement.getStatement();

                        log.debug( "Drop column with SQL statement: '" + statement.getStatement() + "'" );
                    } // end if element
                } // end for

            }// end alter columns

            else
            { // Table is not exist or Table is not data

                // create table
                FormStatement statement = new CreateTableStatement( form, statementBuilder );

                allStatement = statement.getStatement();

                log.debug( "Creating form table with SQL statement: '" + statement.getStatement() + "'" );

            }// end create table

            // execute command Statement

            holder.getStatement().executeUpdate( allStatement );

        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to create table: " + form.getName(), ex );
        }
        finally
        {
            holder.close();
        }
    }

    /**
     * Load list object
     * 
     * @param form needs to create the table
     * @param pageIndex Index of page
     * @return List Objects
     */
    public ArrayList<Object> listObject( Form form, int pageSize )
    {

        ArrayList<Object> data = new ArrayList<Object>();

        StatementHolder holder = statementManager.getHolder();

        FormStatement statement = new ListDataStatement( form, statementBuilder, pageSize );

        log.debug( "Selecting data form table with SQL statement: '" + statement.getStatement() + "'" );

        try
        {

            ResultSet resultSet = holder.getStatement().executeQuery( statement.getStatement() );

            int noColumn = form.getNoColumn();

            while ( resultSet.next() )
            {
                ArrayList<Object> rowData = new ArrayList<Object>();

                for ( int i = 1; i < noColumn + 3; i++ )
                {
                    rowData.add( resultSet.getString( i ) );
                }

                data.add( rowData );
            }

        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to query data : " + form.getName(), ex );
        }
        finally
        {
            holder.close();
        }

        return data;
    }

    /**
     * Add Object by ID
     * 
     * @param form needs to create the table
     * @param data Data of Object
     */
    public void addObject( Form form, String[] data )
    {

        StatementHolder holder = statementManager.getHolder();

        try
        {

            ArrayList<String> arrData = new ArrayList<String>();

            for ( int i = 0; i < data.length; i++ )
            {
                arrData.add( data[i] );
            }

            FormStatement statement = new AddDataStatement( form, statementBuilder, arrData );

            log.debug( "Update data form table with SQL statement: '" + statement.getStatement() + "'" );

            holder.getStatement().executeUpdate( statement.getStatement() );

        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to query data : " + form.getName(), ex );
        }
        finally
        {
            holder.close();
        }
    }

    /**
     * Update Object by ID
     * 
     * @param form needs to create the table
     * @param data Data of Object
     */
    public void updateObject( Form form, String[] data )
    {
        StatementHolder holder = statementManager.getHolder();

        try
        {
            ArrayList<String> arrData = new ArrayList<String>();

            for ( int i = 0; i < data.length; i++ )
            {
                arrData.add( data[i] );
            }

            FormStatement statement = new UpdateDataStatement( form, statementBuilder, arrData );

            log.debug( "Update data form table with SQL statement: '" + statement.getStatement() + "'" );

            holder.getStatement().executeUpdate( statement.getStatement() );

        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to query data : " + form.getName(), ex );
        }
        finally
        {
            holder.close();
        }
    }

    /**
     * Delete Object by ID
     * 
     * @param form needs to create the table
     * @param id Id of object
     */
    public void deleteObject( Form form, int id )
    {

        StatementHolder holder = statementManager.getHolder();

        try
        {
            FormStatement statement = new DeleteDataStatement( form, statementBuilder, id );

            log.debug( "Delete data form table with SQL statement: '" + statement.getStatement() + "'" );

            holder.getStatement().executeUpdate( statement.getStatement() );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to query data : " + form.getName(), ex );
        }
        finally
        {
            holder.close();
        }
    }

    /**
     * Get data in a Object by id of Object
     * 
     * @param form needs to create the table
     * @param id Id of object
     * @return Values of a Object
     */
    public ArrayList<String> getObject( Form form, int id )
    {

        ArrayList<String> data = new ArrayList<String>();

        StatementHolder holder = statementManager.getHolder();

        try
        {
            FormStatement statement = new GetDataStatement( form, statementBuilder, id );

            log.debug( "Get data form table with SQL statement: '" + statement.getStatement() + "'" );

            ResultSet resultSet = holder.getStatement().executeQuery( statement.getStatement() );

            while ( resultSet.next() )
            {
                for ( int i = 1; i < resultSet.getMetaData().getColumnCount() + 1; i++ )
                {
                    data.add( resultSet.getString( i ) );
                }
            }
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to query data : " + form.getName(), ex );
        }
        finally
        {
            holder.close();
        }

        return data;
    }

    /**
     * Search Object by keyword
     * 
     * @param form needs to create the table
     * @param keyword Keyword
     */
    public ArrayList<Object> searchObject( Form form, String keyword, int pageSize )
    {

        ArrayList<Object> data = new ArrayList<Object>();

        StatementHolder holder = statementManager.getHolder();

        try
        {
            FormStatement statement = new SearchDataStatement( form, statementBuilder, keyword, "", pageSize );

            log.debug( "Get data form table with SQL statement: '" + statement.getStatement() + "'" );

            ResultSet resultSet = holder.getStatement().executeQuery( statement.getStatement() );
            while ( resultSet.next() )
            {

                ArrayList<String> rowData = new ArrayList<String>();

                for ( int i = 1; i < form.getNoColumn() + 3; i++ )
                {
                    rowData.add( resultSet.getString( i ) );
                }

                data.add( rowData );

            }
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to query data : " + form.getName(), ex );
        }
        finally
        {
            holder.close();
        }

        return data;
    }

    /**
     * Load list relatived objects
     * 
     * @param form needs to create the table
     * @param column Element's name
     * @param pageIndex Index of page
     * @return List Relatived objects
     */
    public ArrayList<Object> listRelativeObject( Form form, String column, String objectId, int pageSize )
    {

        ArrayList<Object> data = new ArrayList<Object>();

        StatementHolder holder = statementManager.getHolder();

        FormStatement statement = new ListRelativeDataStatement( form, statementBuilder, objectId, column, pageSize );

        log.debug( "Selecting data form relative table with SQL statement: '" + statement.getStatement() + "'" );

        try
        {
            ResultSet resultSet = holder.getStatement().executeQuery( statement.getStatement() );

            int noColumn = form.getNoColumn();

            int rowIndex = 0;

            while ( resultSet.next() )
            {
                ArrayList<String> rowData = new ArrayList<String>();

                for ( int i = 1; i < noColumn + 3; i++ )
                {
                    rowData.add( resultSet.getString( i ) );
                }

                data.add( rowData );

                rowIndex++;
            }

        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to query data : " + form.getName(), ex );
        }
        finally
        {
            holder.close();
        }

        return data;
    }

    /**
     * Export data into a report
     * 
     * @param operator needs to create the table
     * @param element Index of page
     * @param period Objects
     * 
     * @return statistics Result
     */
    public int reportDataStatement( String operator, Period period, FormReport formReport )
    {

        StatementHolder holder = statementManager.getHolder();

        try
        {

            FormStatement statement = new ReportDataStatement( statementBuilder, operator, period, formReport );
            log.debug( "Data statistics from relative table with SQL statement: '" + statement.getStatement() + "'" );

            ResultSet resultSet = holder.getStatement().executeQuery( statement.getStatement() );

            if ( resultSet.next() )
            {

                return resultSet.getInt( 1 );
            }
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to query data : ", ex );
        }

        finally
        {
            holder.close();
        }

        return 0;
    }

    public String createCode( Form form )
    {

        StatementHolder holder = statementManager.getHolder();

        int count = 0;
        try
        {

            FormStatement statement = new CreateCodeStatement( form, statementBuilder );

            log.debug( "Statistics data from table with SQL statement: '" + statement.getStatement() + "'" );

            ResultSet resultSet = holder.getStatement().executeQuery( statement.getStatement() );

            if ( resultSet.next() )
            {

                count = resultSet.getInt( 1 ) + 1;
System.out.println("\n\n\n count : " + count);
                if ( count < 10 )
                {
                    return "00" + count;
                }
                else if ( count < 100 )
                {
                    return "0" + count;
                }
            }
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to query data : ", ex );
        }

        finally
        {
            holder.close();
        }
System.out.println("\n\n\n count : " + count);
        return count + "";
    }
}
