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

public class AddDataStatement
    extends FormStatement
{

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public AddDataStatement( Form form, StatementBuilder statementBuilder, ArrayList<String> data )
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

        // INSERT INTO <table_name> (
        buffer.append( "INSERT" + SPACE + "INTO" + SPACE + form.getName() + SPACE + "(" );

        // Get Columns
        String columns = "";
        // Get Values - VALUES (
        String values = "VALUES" + SPACE + "(";

        int i = 0;

        for ( Egroup egroup : form.getEgroups() )
        {

            for ( Element element : egroup.getElements() )
            {

                if ( !element.getControlType().equals( "break" ) && data.get( i ).length() > 0 )
                {

                    // <column_name>=<data>,
                    columns += element.getName() + SEPARATOR;
                    values += "'" + data.get( i ) + "'" + SEPARATOR;
                    i++;
                }// end if
            }// end for element
        }// end egroup

        // buffer.append(columns + "id, addby)" + SPACE);
        buffer.append( columns + "id" + SEPARATOR + SPACE + "createddate" + SEPARATOR + SPACE + "editeddate"
            + SEPARATOR + SPACE + "addby)" + SPACE );

        // buffer.append(values + "'" + System.identityHashCode(values) + "','"
        // + USERS.iterator().next().getId() + "')" + SPACE);
        buffer.append( values + "'" + ((int) (System.currentTimeMillis())) + "'" + SEPARATOR + SPACE + "'" + new Date()
            + "'" + SEPARATOR + SPACE + "'" + new Date() + "'" + SEPARATOR + SPACE + "'"
            + USERS.iterator().next().getId() + "')" + SPACE );

        statement = buffer.toString();

    }

}
