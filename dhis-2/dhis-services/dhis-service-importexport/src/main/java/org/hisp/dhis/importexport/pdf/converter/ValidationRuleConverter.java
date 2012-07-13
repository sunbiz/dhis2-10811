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

import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.PDFConverter;
import org.hisp.dhis.system.util.PDFUtils;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleService;

import com.lowagie.text.Document;

/**
 * @author Dang Duy Hieu
 * @version $Id$
 */
public class ValidationRuleConverter
    extends PDFUtils
    implements PDFConverter
{
    private ExpressionService expressionService;

    private ValidationRuleService validationRuleService;

    /**
     * Constructor for write operations.
     */
    public ValidationRuleConverter( ExpressionService expressionService, ValidationRuleService validationRuleService )
    {
        this.expressionService = expressionService;
        this.validationRuleService = validationRuleService;
    }

    // -------------------------------------------------------------------------
    // PDFConverter implementation
    // -------------------------------------------------------------------------

    public void write( Document document, ExportParams params )
    {
        I18n i18n = params.getI18n();
        I18nFormat format = params.getFormat();
        List<ValidationRule> elements = null;

        if ( params.isMetaData() )
        {
            elements = new ArrayList<ValidationRule>( validationRuleService.getValidationRules( params
                .getValidationRules() ) );
        }
        else
        {
            elements = new ArrayList<ValidationRule>( params.getValidationRuleObjects() );
        }

        Collections.sort( elements, IdentifiableObjectNameComparator.INSTANCE );

        PDFUtils.printObjectFrontPage( document, elements, i18n, format, "validation_rules" );

        for ( ValidationRule rule : elements )
        {
            addTableToDocument( document, printValidationRule( rule, i18n, expressionService, true, 0.40f, 0.60f ) );
        }
    }
}
