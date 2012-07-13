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

public class GetDataStatement
    extends FormStatement
{

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public GetDataStatement( Form form, StatementBuilder statementBuilder, int id )
    {
        super( form, statementBuilder, id );
    }

    // -------------------------------------------------------------------------
    // Override
    // -------------------------------------------------------------------------

    @Override
    protected void init( Form form )
    {

        StringBuffer buffer = new StringBuffer();

        // SELECT
        buffer.append( "SELECT" + SPACE );

        // Get Columns
        for ( Egroup egroup : form.getEgroups() )
        {
            for ( Element element : egroup.getElements() )
            {
                if ( element.getFormLink() == null )
                {
                    // <column_name>,
                    // columns+= element.getName() + SEPARATOR ;
                    buffer.append( element.getName() + SEPARATOR + SPACE );
                }
            }

        }

        // FROM <table_name> WHERE id = <id_column>
        buffer.append( "addby" + SPACE + "FROM" + SPACE + form.getName() + SPACE + "WHERE" + SPACE + "id=" + value
            + SPACE );

        // + SPACE + "AND" + SPACE + "addby=" + USERS.iterator().next().getId()
        buffer.append( "AND" + SPACE + "addby" + SPACE + "in" + SPACE + "(" + SPACE );

        Collection<User> users = FormStatement.USERS;

        for ( User user : users )
        {

            buffer.append( user.getId() + "," + SPACE );
        }

        buffer.append( USERS.iterator().next().getId() + SPACE + ")" + SPACE );

        statement = buffer.toString();

    }

}
