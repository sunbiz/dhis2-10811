package org.hisp.dhis.de.screen;

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
import java.util.Map;

import org.hisp.dhis.dataelement.CalculatedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public interface DataEntryScreenManager 
{	    
    String getScreenType( DataSet dataSet );
    
    boolean hasMixOfDimensions( DataSet dataset );
    
    boolean hasMultiDimensionalDataElement( DataSet dataSet );
    
    boolean hasSection( DataSet dataSet );
    
    Collection<Integer> getAllCalculatedDataElements( DataSet dataSet );
    
    Map<CalculatedDataElement, Map<DataElement, Integer>> getNonSavedCalculatedDataElements( DataSet dataSet );
    
    Map<CalculatedDataElement, Integer> populateValuesForCalculatedDataElements( OrganisationUnit organisationUnit, DataSet dataSet, Period period );
    
    String populateCustomDataEntryScreen( String dataEntryFormCode, Collection<DataValue> dataValues, Map<CalculatedDataElement,Integer> calculatedValueMap, Map<Integer, MinMaxDataElement> minMaxMap, String disabled, Boolean saveMode, I18n i18n, DataSet dataSet );
    
    String populateCustomDataEntryScreenForMultiDimensional( String dataEntryFormCode, Collection<DataValue> dataValues, Map<CalculatedDataElement,Integer> calculatedValueMap, Map<String, MinMaxDataElement> minMaxMap, String disabled, Boolean saveMode, I18n i18n, DataSet dataSet );
}
