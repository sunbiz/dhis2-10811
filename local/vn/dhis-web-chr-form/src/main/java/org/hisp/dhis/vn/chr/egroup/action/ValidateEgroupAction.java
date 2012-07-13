package org.hisp.dhis.vn.chr.egroup.action;

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

import org.hisp.dhis.vn.chr.form.action.ActionSupport;


/**
 * @author Chau Thu Tran
 * @version $Id$
 */

public class ValidateEgroupAction
    extends ActionSupport
{

    // -------------------------------------------
    // Input & Output
    // -------------------------------------------

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private Integer sortOrder;

    public void setSortOrder( Integer sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    // -------------------------------------------
    // Action Implementation
    // -------------------------------------------

    public String execute()
        throws Exception
    {

        if ( name == null || name.trim().length() == 0 )
        {
            message = i18n.getString( "name" ) + " " + i18n.getString( "not_null" );
            return ERROR;
        }

        if ( sortOrder == null  )
        {
            message = i18n.getString( "sortOrder" ) + " " + i18n.getString( "not_null" );
            return ERROR;
        }else if(sortOrder.intValue() == 0){
            message = i18n.getString( "sortOrder" ) + " " + i18n.getString( "not_string" );
            return ERROR;
        }

        return SUCCESS;
    }
}