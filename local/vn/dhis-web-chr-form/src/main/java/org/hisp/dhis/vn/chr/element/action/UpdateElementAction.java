package org.hisp.dhis.vn.chr.element.action;

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

import org.hisp.dhis.system.util.CodecUtils;
import org.hisp.dhis.vn.chr.Element;
import org.hisp.dhis.vn.chr.ElementService;
import org.hisp.dhis.vn.chr.FormService;
import org.hisp.dhis.vn.chr.form.action.ActionSupport;

/**
 * @author Chau Thu Tran
 * @version $Id$
 */

public class UpdateElementAction
    extends ActionSupport
{
    // -----------------------------------------------------------------------------------------------
    // Dependency
    // -----------------------------------------------------------------------------------------------

    private FormService formService;

    public void setFormService( FormService formService )
    {
        this.formService = formService;
    }

    private ElementService elementService;

    public void setElementService( ElementService elementService )
    {
        this.elementService = elementService;
    }

    // -----------------------------------------------------------------------------------------------
    // Input && Output
    // -----------------------------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private int formID;

    public void setFormID( int formID )
    {
        this.formID = formID;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String label;

    public void setLabel( String label )
    {
        this.label = label;
    }

    private String type;

    public void setType( String type )
    {
        this.type = type;
    }

    private String controlType;

    public void setControlType( String controlType )
    {
        this.controlType = controlType;
    }

    private String initialValue;

    public void setInitialValue( String initialValue )
    {
        this.initialValue = initialValue;
    }

    private int formLink;

    public void setFormLink( int formLink )
    {
        this.formLink = formLink;
    }

    private boolean required;

    public void setRequired( boolean required )
    {
        this.required = required;
    }

    private int sortOrder;

    public void setSortOrder( int sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    // -----------------------------------------------------------------------------------------------
    // Action Implementation
    // -----------------------------------------------------------------------------------------------

    public String execute()

    {
        try
        {
            Element element = elementService.getElement( id.intValue() );

            element.setName( CodecUtils.unescape( name ).toLowerCase() );

            element.setLabel( CodecUtils.unescape( label ) );

            element.setType( type );

            element.setControlType( controlType );

            element.setInitialValue( CodecUtils.unescape( initialValue ));

            if ( formLink != 0 )
            {

                element.setFormLink( formService.getForm( formLink ) );

            }
            else
            {
                element.setFormLink( null );

            }

            element.setRequired( required );

            element.setSortOrder( sortOrder );

            elementService.updateElement( element );

            element.setForm( formService.getForm( formID ) );

            message = i18n.getString( "success" );

            return SUCCESS;
        }
        catch ( Exception ex )
        {
            message = i18n.getString( "update" ) + " " + i18n.getString( "error" );
        }
        return ERROR;
    }

}
