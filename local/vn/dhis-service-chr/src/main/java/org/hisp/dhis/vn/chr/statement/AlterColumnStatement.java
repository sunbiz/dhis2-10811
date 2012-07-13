package org.hisp.dhis.vn.chr.statement;

/**
 * @author Chau Thu Tran
 * 
 */

import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.Form;

public class AlterColumnStatement
    extends FormStatement
{

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public AlterColumnStatement( StatementBuilder statementBuilder, String status, Element element )
    {
        super( statementBuilder, status, element );
    }

    public AlterColumnStatement( Form form, StatementBuilder statementBuilder, String status, String column )
    {
        super( form, statementBuilder, status, column );
    }

    // ----------------------------------------------------------------------------------------------------
    // Init method - Override
    // ----------------------------------------------------------------------------------------------------

    @Override
    protected void init( Form form )
    {

        StringBuffer buffer = new StringBuffer();

        // Alter table <table_name>
        buffer.append( "ALTER TABLE" + SPACE + form.getName() + SPACE );

        // DROP COLUMN <column_name>
        if ( status.equals( DROP_STATUS ) )
        {

            buffer.append( status + SPACE + column + SPACE );

        }
        else
        {

            // ALTER COULMN <column_name>
            // or ADD <column_name>
            buffer.append( status + SPACE + element.getName() + SPACE );

            // TYPE
            if ( status.endsWith( ALTER_STATUS ) )
                buffer.append( "type" + SPACE );

            buffer.append( element.getType() + SPACE );

            if ( element.getFormLink() != null )
            {
                buffer.append( SPACE + ";" + SPACE + "ALTER" + SPACE + "TABLE" + SPACE + form.getName() + SPACE + "ADD"
                    + SPACE + "FOREIGN KEY(" + element.getName() + ")" + SPACE + "REFERENCES" + SPACE
                    + element.getFormLink().getName() + SPACE + "(id)" );
            }

        }// end else

        // ;
        buffer.append( SEPARATOR_COMMAND );

        statement = buffer.toString();

    }

}
