package org.hisp.dhis.vn.chr.object.action;

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

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.vn.chr.Egroup;
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.Form;
import org.hisp.dhis.vn.chr.FormService;
import org.hisp.dhis.vn.chr.form.action.ActionSupport;

/**
 * @author Chau Thu Tran
 * @version $Id$
 */

public class ValidateObjectAction
    extends ActionSupport
{

    // -----------------------------------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------------------------------

    private FormService formService;

    public void setFormService( FormService formService )
    {
        this.formService = formService;
    }

    // -------------------------------------------
    // Input & Output
    // -------------------------------------------

    // Form ID
    private Integer formId;

    public void setFormId( Integer formId )
    {
        this.formId = formId;
    }

    // -------------------------------------------
    // Action Implementation
    // -------------------------------------------

    public String execute()
        throws Exception
    {
        message = "";

        HttpServletRequest request = ServletActionContext.getRequest();

        Form form = formService.getForm( formId.intValue() );

        Collection<Egroup> egroups = form.getEgroups();

        // int pos = 0;

        for ( Egroup egroup : egroups )
        {

            for ( Element element : egroup.getElements() )
            {

                if ( element.isRequired() )
                {
                    String parameterName = "data" + element.getName();

                    String value = request.getParameterValues( parameterName )[0];

                   if ( value != null && value.length() == 0 )
                    {
                        message += " - " + element.getLabel() + "<br>";

                    }// end not null

                }// end if

            }// end for element

        }// end for egroup

        if ( message.length() > 0 )
        {
            message = i18n.getString( "elements" ) + " : <br>" + message + i18n.getString( "not_null" );

            return ERROR;
        }
        return SUCCESS;
    }

}
