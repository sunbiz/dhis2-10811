package org.hisp.dhis.vn.chr.statement;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.ArrayList;
import java.util.Date;

import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.vn.chr.Egroup;
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.Form;

public class UpdateDataStatement
    extends FormStatement
{

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public UpdateDataStatement( Form form, StatementBuilder statementBuilder, ArrayList<String> data )
    {
        super( form, statementBuilder, data );
    }

    // -------------------------------------------------------------------------
    // Override
    // -------------------------------------------------------------------------

    @Override
    protected void init( Form form )
    {

        StringBuffer buffer = new StringBuffer();

        // Update <table_name> SET
        buffer.append( "UPDATE" + SPACE + form.getName() + SPACE + "SET" + SPACE );

        // Get Columns and correlative values
        int i = 1;

        for ( Egroup egroup : form.getEgroups() )
        {
            for ( Element element : egroup.getElements() )
            {
                // <column_name>=<data>,
                if ( element.getFormLink() == null && !element.getControlType().equals( "break" ) )
                {
                    if ( data.get( i ).length() > 0 )
                    {

                        buffer.append( element.getName() + "='" + data.get( i ) + "'" + SEPARATOR + SPACE );

                    }// end if data > 0
                    i++;
                }// end if formlink
            }
        }

        // editeddate = '<now>'
        buffer.append( "editeddate='" + new Date() + "'" + SPACE );
        // FROM <table_name> WHERE id = <id_column>
        buffer.append( "WHERE" + SPACE + "id='" + data.get( 0 ) + "'" + SPACE + "AND" + SPACE + "addby="
            + USERS.iterator().next().getId() );

        statement = buffer.toString();

    }

}
