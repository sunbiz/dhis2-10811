package org.hisp.dhis.vn.chr.statement;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.user.User;
import org.hisp.dhis.vn.chr.Egroup;
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.Form;

public class ListRelativeDataStatement
    extends FormStatement
{

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ListRelativeDataStatement( Form form, StatementBuilder statementBuilder, String objectId, String column, int pageSize )
    {
        super( form, statementBuilder, objectId, column, pageSize );
    }

    // -------------------------------------------------------------------------
    // Override
    // -------------------------------------------------------------------------

    @Override
    protected void init( Form form )
    {

        StringBuffer buffer = new StringBuffer();

        // SELECT id,
        buffer.append( "SELECT" + SPACE + "id" + SEPARATOR );

        // Number of columns selected
        int noColumn = form.getNoColumn();
        int index = 0;
        // Break loop for
        boolean flag = false;

        for ( Egroup egroup : form.getEgroups() )
        {

            if ( flag )
                break;

            for ( Element element : egroup.getElements() )
            {
                // element is not a foreign key
                if ( element.getFormLink() == null )
                {
                    if ( index < noColumn )
                    {
                        // <column_name>,
                        buffer.append( element.getName() + SEPARATOR );

                        index++;
                    }

                    else
                    {
                        // <column_name>
                        flag = true;
                        break;
                    }

                }// end if element

            }// end for element

        }// end for egroup

        // FORM <table_name> WHERE column=value
        buffer.append( "addby" + SPACE + "FROM" + SPACE + form.getName().toLowerCase() + SPACE + "WHERE" + SPACE );

        // keyword
        buffer.append( column + "=" + status + SPACE + "AND" + SPACE );

        // users
        buffer.append( "addby" + SPACE + "in" + SPACE + "(" + SPACE );

        Collection<User> users = FormStatement.USERS;

        for ( User user : users )
        {

            buffer.append( user.getId() + "," + SPACE );
        }

        buffer.append( USERS.iterator().next().getId() + SPACE + ")" + SPACE );

        buffer.append( "AND" + SPACE + "editeddate is not null" + SPACE );
        // order by editeddate
        buffer.append( "order by" + SPACE + "editeddate" + SPACE + "desc" + SPACE );

        // limmit number of records showed
        buffer.append( "LIMIT" + SPACE + value );

        statement = buffer.toString();
    }

}