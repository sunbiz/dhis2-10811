package org.hisp.dhis.vn.chr.formreport.action;

/*
 * Copyright (c) 2004-2012, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hisp.dhis.system.util.CodecUtils;
import org.hisp.dhis.vn.chr.Form;
import org.hisp.dhis.vn.chr.FormReport;
import org.hisp.dhis.vn.chr.FormReportService;
import org.hisp.dhis.vn.chr.FormService;
import org.hisp.dhis.vn.chr.form.action.ActionSupport;

/**
 * @author Chau Thu Tran
 * @version $Id$
 */


public class AddFormReport
    extends ActionSupport
{

    // -----------------------------------------------------------------------------------------------
    // Dependency
    // -----------------------------------------------------------------------------------------------

    private FormService formService;

    public void setFormReportService( FormReportService formReportService )
    {
        this.formReportService = formReportService;
    }

    private FormReportService formReportService;

    public void setFormService( FormService formService )
    {
        this.formService = formService;
    }

    // -----------------------------------------------------------------------------------------------
    // Input && Output
    // -----------------------------------------------------------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String chosenOperand;

    public void setChosenOperand( String chosenOperand )
    {
        this.chosenOperand = chosenOperand;
    }

    private String formula;

    public void setFormula( String formula )
    {
        this.formula = formula;
    }

    private Integer mainForm;

    public void setMainForm( Integer mainForm )
    {
        this.mainForm = mainForm;
    }

    // -----------------------------------------------------------------------------------------------
    // Implement
    // -----------------------------------------------------------------------------------------------

    public String execute()
    {
        try
        {

            // create a new formReport
            FormReport formReport = new FormReport();

            // set name
            formReport.setName( CodecUtils.unescape( name ) );

            // set formula for the element
            formula = formula.toLowerCase();
            formReport.setFormula( CodecUtils.unescape(formula ));

            // get all forms
            Collection<Form> forms = formService.getAllForms();
            // forms used in the formula
            List<Form> formulaForms = new ArrayList<Form>();
            for ( Form form : forms )
            {
                String formName = form.getName().toLowerCase() + ".";
                if ( formula.contains( formName ) )
                {
                    formulaForms.add( form );
                }
            }
            // set forms used in the formula
            Form main = formService.getForm( mainForm.intValue() );
            formulaForms.add( main );
            formReport.setForms( formulaForms );
            // set mainForm used to identify statistics-form
            formReport.setMainForm( main );

            // set operand of dataelement
            formReport.setOperand( chosenOperand );

            // insert new formReport into database
            formReportService.addFormReport( formReport );

            message = i18n.getString( "success" );

            return SUCCESS;
        }
        catch ( Exception ex )
        {
            message = i18n.getString( "add" ) + " " + i18n.getString( "error" );

            ex.printStackTrace();
        }
        return ERROR;
    }

}
