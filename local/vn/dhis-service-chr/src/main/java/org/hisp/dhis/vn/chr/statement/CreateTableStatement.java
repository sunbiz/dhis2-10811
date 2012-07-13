package org.hisp.dhis.vn.chr.statement;

/**
 * @author Chau Thu Tran
 * 
 */

import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.Form;

public class CreateTableStatement
    extends FormStatement
{

    private static final String REQUIRED = "";

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public CreateTableStatement( Form form, StatementBuilder statementBuilder )
    {
        super( form, statementBuilder );
    }

    @Override
    protected void init( Form form )
    {

        StringBuffer buffer = new StringBuffer();

        // CREATE TABLE <table_name> (
        buffer.append( "CREATE TABLE " + form.getName().toLowerCase() + " ( " );

        // ---------------------------------------------------------------------
        // Create table
        // ---------------------------------------------------------------------

        // id INTEGER ;
        // buffer.append("id" + SPACE + Element.INTEGER + SPACE + REQUIRED +
        // SEPARATOR);
        buffer.append( "id" + SPACE + Element.SERIAL + SPACE + REQUIRED + SEPARATOR );
        // addby INTEGER ;
        buffer.append( "addby" + SPACE + Element.INTEGER + SPACE + REQUIRED + SEPARATOR );
        // createddate DATE;
        buffer.append( "createddate" + SPACE + Element.DATETIME + SPACE + REQUIRED + SEPARATOR );
        // editeddate DATE;
        buffer.append( "editeddate" + SPACE + Element.DATETIME + SPACE + REQUIRED + SEPARATOR );

        for ( Element column : form.getElements() )
        {
            // Column name
            // <column_name>
            buffer.append( column.getName().toLowerCase() + SPACE );
            // datatype
            buffer.append( column.getType() + SPACE );
            // ;
            buffer.append( SEPARATOR );

            // FOREIGN KEY
            if ( column.getFormLink() != null )
            {
                // System.out.print("CONSTRAINT"+ SPACE + form.getName()+"_"+
                // column.getFormLink().getName() + "_" +
                // System.currentTimeMillis()+ SPACE + "FOREIGN KEY(" + SPACE +
                // column.getName()+ SPACE + ")"+ SPACE + "REFERENCES"+
                // column.getFormLink().getName()+ "(id)");
                buffer.append( "CONSTRAINT" + SPACE + form.getName() + "_" + column.getFormLink().getName() + "_"
                    + System.currentTimeMillis() + SPACE + "FOREIGN KEY(" + SPACE + column.getName() + SPACE + ")"
                    + SPACE + "REFERENCES" + SPACE + column.getFormLink().getName() + SPACE + "(id)" + SEPARATOR
                    + SPACE );
            }

        }

        // ---------------------------------------------------------------------
        // Primary key
        // ---------------------------------------------------------------------

        // CONSTRAINT <form_name + random number>+ PRIMARY KEY (id)
        buffer.append( "CONSTRAINT" + SPACE + form.getName() + System.currentTimeMillis() + SPACE + "PRIMARY KEY (id)" );

        // end create table
        // ;
        buffer.append( ") " );

        statement = buffer.toString();

    }

}
