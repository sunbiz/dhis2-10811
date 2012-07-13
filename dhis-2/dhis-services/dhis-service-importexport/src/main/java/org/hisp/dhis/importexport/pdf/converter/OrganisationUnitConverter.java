package org.hisp.dhis.importexport.pdf.converter;

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
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.PDFConverter;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.system.util.PDFUtils;

import com.lowagie.text.Document;

/**
 * @author Lars Helge Overland
 * @version $Id: OrganisationUnitConverter.java 6251 2008-11-10 14:37:05Z
 *          larshelg $
 * @modifier Dang Duy Hieu
 * @since 2010-05-19
 */
public class OrganisationUnitConverter
    extends PDFUtils
    implements PDFConverter
{
    private OrganisationUnitService organisationUnitService;

    public OrganisationUnitConverter( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    public OrganisationUnitConverter()
    {
    }

    // -------------------------------------------------------------------------
    // PDFConverter implementation
    // -------------------------------------------------------------------------

    public void write( Document document, ExportParams params )
    {
        I18n i18n = params.getI18n();
        I18nFormat format = params.getFormat();
        List<OrganisationUnit> elements = null;

        if ( params.isMetaData() )
        {
            elements = new ArrayList<OrganisationUnit>( organisationUnitService.getOrganisationUnits( params.getOrganisationUnits() ) );
        }
        else
        {
            elements = new ArrayList<OrganisationUnit>( params.getOrganisationUnitObjects() );
        }

        Collections.sort( elements, IdentifiableObjectNameComparator.INSTANCE );
        
        PDFUtils.printObjectFrontPage( document, elements, i18n, format, "organisation_units" );

        for ( OrganisationUnit unit : elements )
        {
            addTableToDocument( document, printOrganisationUnit( unit, i18n, format, true, 0.40f, 0.60f ) );
        }
    }
}
