package org.hisp.dhis.vn.chr.statement;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.ArrayList;
import java.util.Collection;

import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.user.User;
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.Form;
import org.hisp.dhis.vn.chr.FormReport;

public abstract class FormStatement
{

    /** Commands of SQL */
    public static final String DROP_STATUS = "DROP COLUMN";

    public static final String ADD_STATUS = "ADD";

    public static final String ALTER_STATUS = "ALTER COLUMN";

    protected static final String NUMERIC_COLUMN_TYPE = "INTEGER NOT NULL";

    protected static final String SHORT_TEXT_COLUMN_TYPE = "VARCHAR (15)";

    protected static final String MEDIUM_TEXT_COLUMN_TYPE = "VARCHAR (40)";

    protected static final String LONG_TEXT_COLUMN_TYPE = "VARCHAR (255)";

    /** Control type */
    protected static final String CHECKBOX = "checkbox";

    /** Symbol in SQL */
    protected static final String QUERY_PARAM_ID = ":";

    protected static final String SEPARATOR_COMMAND = ";";

    public static final String SPACE = " ";

    public static final String SEPARATOR = ", ";

    /** Variables needs to create statement */
    public static Collection<User> USERS;

    protected String DAY = "createddate";

    protected String MONTH = "cast(substring(createddate,6,2) as int)";

    protected String YEAR = "cast(substring(createddate,1,4) as int)";

    protected String status;

    protected Element element;

    protected FormReport formReport;

    protected String column;

    protected int value;

    protected ArrayList<String> data;

    protected String keyword;

    // this value to get day, month, year of the createddate column
    protected Period period;

    // protected String date;
    protected String operator;

    /** Statement Builder */
    protected StatementBuilder statementBuilder;

    /** Statement String */
    protected String statement;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     * 
     * */
    @SuppressWarnings( "unused" )
    private FormStatement()
    {

    }

    /**
     * Constructor method
     * 
     * @param form Form needs to create a table
     * @param dialect the dialect in configuration file
     * 
     */
    public FormStatement( Form form, StatementBuilder statementBuilder )
    {
        init( form, statementBuilder );
    }

    /**
     * Constructor method
     * 
     * @param form Form needs to create a table
     * @param dialect The dialect in configuration file
     * @param element Column needs to add or alter into the table
     * 
     */
    public FormStatement( StatementBuilder statementBuilder, String status, Element element )
    {
        this.status = status;
        this.element = element;
        init( element.getForm(), statementBuilder );
    }

    /**
     * Constructor method
     * 
     * @param form Form needs to create a table
     * @param dialect The dialect in configuration file
     * @param column Column's name needs to delete
     * 
     */
    public FormStatement( Form form, StatementBuilder statementBuilder, String status, String column )
    {
        this.status = status;
        this.column = column;
        init( form, statementBuilder );
    }

    /**
     * Constructor method
     * 
     * @param form Form needs to create a table
     * @param dialect The dialect in configuration file
     * @param column Column's name needs to delete
     * 
     */
    public FormStatement( Form form, StatementBuilder statementBuilder, String keywork, String column, int pageSize )
    {
        this.status = keywork;
        this.column = column;
        this.value = pageSize;
        init( form, statementBuilder );
    }

    /**
     * Constructor method used to select list of object
     * 
     * @param form Form needs to create a table
     * @param dialect The dialect in configuration file
     * @param value Index of page, ID of object, ...
     * 
     */
    public FormStatement( Form form, StatementBuilder statementBuilder, int value )
    {
        this.value = value;

        init( form, statementBuilder );
    }

    /**
     * Constructor method used to add a object
     * 
     * @param form Form needs to create a table
     * @param dialect The dialect in configuration file
     * @param keyword Keyword to search
     * 
     */
    public FormStatement( Form form, StatementBuilder statementBuilder, String keyword )
    {
        this.keyword = keyword;

        init( form, statementBuilder );
    }

    /**
     * Constructor method used to add a object
     * 
     * @param form Form needs to create a table
     * @param dialect The dialect in configuration file
     * @param data Data of Object
     * 
     */
    public FormStatement( Form form, StatementBuilder statementBuilder, ArrayList<String> data )
    {
        this.data = data;

        init( form, statementBuilder );
    }

    public FormStatement( StatementBuilder statementBuilder, String operator, Period period, FormReport formReport )
    {

        this.formReport = formReport;

        this.period = period;

        // count, sum
        this.operator = operator;

        init( null, statementBuilder );
    }

    /**
     * Initial method used to create constructors
     * 
     * @param form Form needs to create a table
     * @param dialect The dialect in configuration file
     * @param pageIndex Index of page
     * 
     */
    public void init( Form form, StatementBuilder statementBuilder )
    {
        this.statementBuilder = statementBuilder;

        init( form );
    }

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public String getStatement()
    {
        return statement;
    }

    public void setString( String param, String value )
    {
        statement = statement.replace( QUERY_PARAM_ID + param, value );
    }

    public void setInt( String param, Integer value )
    {
        statement = statement.replace( QUERY_PARAM_ID + param, String.valueOf( value ) );
    }

    // -------------------------------------------------------------------------
    // Abstract methods
    // -------------------------------------------------------------------------

    protected abstract void init( Form form );
}
