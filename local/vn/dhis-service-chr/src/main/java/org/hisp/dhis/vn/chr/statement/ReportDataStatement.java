package org.hisp.dhis.vn.chr.statement;

/**
 * @author Chau Thu Tran
 * 
 */

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.user.User;
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.Form;
import org.hisp.dhis.vn.chr.FormReport;

public class ReportDataStatement
    extends FormStatement
{

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ReportDataStatement( StatementBuilder statementBuilder, String operator, Period period, FormReport formReport )
    {
        super( statementBuilder, operator, period, formReport );
    }

    // -------------------------------------------------------------------------
    // Override
    // -------------------------------------------------------------------------

    @Override
    protected void init( Form form )
    {

        StringBuffer buffer = new StringBuffer();
        // SELECT operator(<column's name>)
        buffer.append( "SELECT" + SPACE + this.operator + "(*)" + SPACE );
        // FROM
        buffer.append( "FROM" + SPACE );
        // -- forms need for select
        String conditionForm = "";
        // -- condition to connect to objects
        String condition = "";
        // loop forms' list
        Iterator<Form> forms = formReport.getForms().iterator();
        while ( forms.hasNext() )
        {

            Form conForm = forms.next();
            conditionForm += conForm.getName().toLowerCase() + SEPARATOR + SPACE;

            Collection<Element> elements = conForm.getElements();

            if ( formReport.getForms().size() > 1 )
            {
                // get all of elements of the form have formlink property to be
                // not null
                for ( Element element : elements )
                {
                    if ( element.getFormLink() != null )
                    {
                        // <form.element.name> = <element.formLink.name.id>
                        // <ref_columm> = <form.id>
                        condition += "AND" + SPACE + element.getForm().getName().toLowerCase() + "."
                            + element.getName().toLowerCase() + "=" + element.getFormLink().getName().toLowerCase()
                            + ".id" + SPACE;
                    }// end if
                }
            }

        }// end while forms

        // <table's name, table's name ...>
        if ( conditionForm.length() > 0 )
        {
            buffer.append( conditionForm.substring( 0, conditionForm.length() - 3 ) + SPACE );
        }

        // -- WHERE createddate >= start day and createddate <= end day
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
        String startDate = formatter.format( period.getStartDate() );
        String endDate = formatter.format( period.getEndDate() );

        // WHERE <mainForm's name>
        buffer.append( SPACE + "WHERE" + SPACE + formReport.getMainForm().getName().toLowerCase() + ".createddate>='"
            + startDate + "'" + SPACE + "AND" + SPACE + formReport.getMainForm().getName().toLowerCase()
            + ".createddate<='" + endDate + "'" + SPACE );
        // and formula
        if ( this.formReport.getFormula().length() > 0 )
        {
            // AND formular
            buffer.append( "AND" + SPACE + this.formReport.getFormula() + SPACE );
        }
        // AND <table>.<id>=<table>.<column>
        buffer.append( condition );

        // -- get data by users
        Collection<User> users = FormStatement.USERS;

        // -- get add by in users' list
        if ( users != null )
        {

            buffer.append( "AND" + SPACE + formReport.getMainForm().getName().toLowerCase() + ".addby" + SPACE + "in"
                + SPACE + "(" + SPACE );

            for ( User user : users )
            {
                buffer.append( user.getId() + "," + SPACE );
            }

            buffer.append( USERS.iterator().next().getId() + SPACE + ")" + SPACE );
        }

        statement = buffer.toString();
    }
}
