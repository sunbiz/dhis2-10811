package org.hisp.dhis.vn.chr.statement;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.vn.chr.Form;

public class CreateCodeStatement extends FormStatement
{

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public CreateCodeStatement( Form form, StatementBuilder statementBuilder)
    {
        super( form, statementBuilder );
    }

    // -------------------------------------------------------------------------
    // Override
    // -------------------------------------------------------------------------

    @Override
    protected void init( Form form )
    {
        StringBuffer buffer = new StringBuffer();

        // SELECT
        buffer.append( "SELECT" + SPACE  + "COUNT(*)" + SPACE);

        
        // FROM <table_name> WHERE id = <id_column>
        buffer.append( "FROM" + SPACE  + form.getName().toLowerCase() + SPACE);
        // WHERE
        Date date = new Date();
        // year
        Format formatter = new SimpleDateFormat( "yyyy" );
        String year = formatter.format(date);
        // month
        formatter = new SimpleDateFormat( "MM" );
        int month =Integer.parseInt( formatter.format(date));
        
        buffer.append( "WHERE" +  SPACE + "createddate>='" + year + "-" + month + "-" + "01'" + SPACE );
        buffer.append( "AND" +  SPACE + "createddate<'"+ year + "-" + (month + 1) + "-" + "01'" );

        statement = buffer.toString();
    } 
}
