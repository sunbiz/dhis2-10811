package org.hisp.dhis.importexport.pdf.converter;

/*
 * Copyright (c) 2004-2011, University of Oslo
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
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.PDFConverter;
import org.hisp.dhis.system.util.PDFUtils;
import org.hisp.dhis.user.UserCredentials;
import org.hisp.dhis.user.UserService;
import org.hisp.dhis.user.comparator.UsernameComparator;

import com.lowagie.text.Document;

/**
 * @author Dang Duy Hieu
 * @version $Id$
 */
public class UserConverter
    extends PDFUtils
    implements PDFConverter
{
    private UserService userService;

    /**
     * Constructor for write operations.
     */
    public UserConverter( UserService userService )
    {
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // PDFConverter implementation
    // -------------------------------------------------------------------------

    public void write( Document document, ExportParams params )
    {
        I18n i18n = params.getI18n();
        I18nFormat format = params.getFormat();
        List<UserCredentials> elements = null;

        if ( params.isMetaData() )
        {
            elements = new ArrayList<UserCredentials>( userService.getUsers( params.getUsers(), params.getCurrentUser() ) );
        }
        else
        {
            elements = new ArrayList<UserCredentials>( params.getUserObjects() );
        }

        Collections.sort( elements, new UsernameComparator() );

        PDFUtils.printObjectFrontPage( document, elements, i18n, format, "users" );

        for ( UserCredentials userCredentials : elements )
        {
            addTableToDocument( document, printUser( userCredentials, i18n, format, true, 0.40f, 0.60f ) );
        }
    }
}
