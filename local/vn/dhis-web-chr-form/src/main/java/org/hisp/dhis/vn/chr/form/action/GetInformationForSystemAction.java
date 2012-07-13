package org.hisp.dhis.vn.chr.form.action;

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

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserStore;
import org.hisp.dhis.vn.chr.statement.FormStatement;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version $Id$
 */

public class GetInformationForSystemAction
    implements Action
{

    // -----------------------------------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------------------------------

    private UserStore userStore;

    public void setUserStore( UserStore userStore )
    {
        this.userStore = userStore;
    }

    // -----------------------------------------------------------------------------------------------
    // Input && Output
    // -----------------------------------------------------------------------------------------------

    public static int curUserid;

    public User getCurUser()
    {
        return curUser;
    }

    public void setCurUser( User curUser )
    {
        this.curUser = curUser;
    }

    // -----------------------------------------------------------------------------------------------
    // Implement
    // -----------------------------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        try
        {

            // Get All user can process data into database
            Collection<User> users = new ArrayList<User>();

            curUserid = curUser.getId();

            Collection<OrganisationUnit> orgUnits = curUser.getOrganisationUnits();

            for ( OrganisationUnit orgUnit : orgUnits )
            {

                getUsers( orgUnit, users );
            }// end for

            FormStatement.USERS = users;

            return SUCCESS;
        }
        catch ( Exception ex )
        {

            ex.printStackTrace();
        }
        return ERROR;
    }

    // Get All user can process data into database
    User curUser = null;

    // Get All users belong to the curUser
    private void getUsers( OrganisationUnit orgUnit, Collection<User> users )
    {

        users.addAll( userStore.getUsersByOrganisationUnit( orgUnit ) );

        Collection<OrganisationUnit> orgUnits = orgUnit.getChildren();

        if ( orgUnits != null )
        {

            for ( OrganisationUnit org : orgUnits )
            {

                getUsers( org, users );
            }
        }
    }

}
