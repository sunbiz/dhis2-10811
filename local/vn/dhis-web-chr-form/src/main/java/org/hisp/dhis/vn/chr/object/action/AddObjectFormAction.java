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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.hisp.dhis.vn.chr.Egroup;
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.Form;
import org.hisp.dhis.vn.chr.FormService;
import org.hisp.dhis.vn.chr.jdbc.FormManager;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version $Id$
 */


public class AddObjectFormAction
    implements Action
{

    // -----------------------------------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------------------------------

    private FormService formService;

    public void setFormService( FormService formService )
    {
        this.formService = formService;
    }

    private FormManager formManager;

    public void setFormManager( FormManager formManager )
    {
        this.formManager = formManager;
    }
   
    // -----------------------------------------------------------------------------------------------
    // Getter && Setter
    // -----------------------------------------------------------------------------------------------

    private int formId;

    public void setFormId( int formId )
    {
        this.formId = formId;
    }

    private Collection<Egroup> egroups;

    public Collection<Egroup> getEgroups()
    {
        return egroups;
    }

    private Form form;

    public Form getForm()
    {
        return form;
    }

    private String objectId;

    public void setObjectId( String objectId )
    {
        this.objectId = objectId;
    }

    private ArrayList<String> parentObject;

    public ArrayList<String> getParentObject()
    {
        return parentObject;
    }

    private String code;

    public String getCode()
    {
        return code;
    }
    
    // -----------------------------------------------------------------------------------------------
    // Action implementation
    // -----------------------------------------------------------------------------------------------

    public String execute()
        throws Exception
    {

        form = formService.getForm( formId );

        egroups = form.getEgroups();

        if ( objectId != null )
        {

            Iterator<Egroup> iter = egroups.iterator();
            if ( iter.hasNext() )
            {
                for ( Element element : iter.next().getElements() )
                {
                    if ( element.getFormLink() != null )
                    {
                        Form fparent = element.getFormLink();
                        ArrayList<String> data = formManager.getObject( fparent, Integer.parseInt( objectId ) );
                        parentObject = new ArrayList<String>();
                        int k = 1;
                        for ( Egroup egroup : fparent.getEgroups() )
                        {
                            for ( Element e : egroup.getElements() )
                            {
                                if ( data.get( k ) != null )
                                    parentObject.add( e.getLabel() + " : " + data.get( k ) );
                                k++;
                                if ( k == fparent.getNoColumnLink() )
                                    break;
                            }// end for element

                            if ( k == fparent.getNoColumnLink() )
                                break;
                        }// end for egroup
                    }
                }
            }
        }
        
        Date date = new Date();

        Format formatter = new SimpleDateFormat( "yyMM" );

        code = "MCH" + formatter.format( date );

        code += formManager.createCode( form );

        return SUCCESS;
    }
}
