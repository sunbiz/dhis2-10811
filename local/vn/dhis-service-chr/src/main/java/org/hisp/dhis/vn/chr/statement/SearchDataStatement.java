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

public class SearchDataStatement
    extends FormStatement
{

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public SearchDataStatement( Form form, StatementBuilder statementBuilder, String keywork, String column, int pageSize )
    {
        super( form, statementBuilder, keywork, column, pageSize );
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

                if ( index < noColumn - 1 )
                {
                    // <column_name>,
                    buffer.append( element.getName() + SEPARATOR + SPACE );
                }
                else
                {
                    // <column_name>
                    buffer.append( element.getName() + SEPARATOR + SPACE );
                    flag = true;
                    break;
                }
                index++;
            }// end for element

        }// end for egroup

        // FORM <table_name> WHERE 1=1
        buffer.append( "addby" + SPACE + "FROM" + SPACE + form.getName() + SPACE + "WHERE" + SPACE + "(" + SPACE );

        int i = 0;
        String condition = "";
        // OR <column_name>=value OR <column_name>=value OR ...
        for ( Egroup egroup : form.getEgroups() )
        {
            for ( Element element : egroup.getElements() )
            {

                condition += element.getName() + SPACE + "like" + SPACE + "'%" + status + "%'" + SPACE + "OR" + SPACE;

                i++;
            }
        }

        buffer.append( condition.substring( 0, condition.length() - 3 ) + SPACE );

        buffer.append( ")" + SPACE + "AND" + SPACE );

        // users
        buffer.append( "addby" + SPACE + "in" + SPACE + "(" + SPACE );

        Collection<User> users = FormStatement.USERS;

        for ( User user : users )
        {

            buffer.append( user.getId() + "," + SPACE );
        }

        buffer.append( USERS.iterator().next().getId() + SPACE + ")" + SPACE );

        // order by editeddate
        buffer.append( "order by" + SPACE + "createddate" + SPACE + "asc" + SPACE );

        // limit munber of records showed into list
        buffer.append( "LIMIT" + SPACE + value );

        statement = buffer.toString();

    }

}
