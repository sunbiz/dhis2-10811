package org.hisp.dhis.common;

/*
 * Copyright (c) 2004-2005, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lars Helge Overland
 */
public class DefaultDimensionService
    implements DimensionService
{
    @Autowired
    private IdentifiableObjectManager identifiableObjectManager;
    
    @Override
    public DimensionalObject getDimension( String uid )
    {        
        DataElementGroupSet degs = identifiableObjectManager.get( DataElementGroupSet.class, uid );
        
        if ( degs != null )
        {
            return degs;
        }
        
        OrganisationUnitGroupSet ougs = identifiableObjectManager.get( OrganisationUnitGroupSet.class, uid );
        
        if ( ougs != null )
        {
            return ougs;
        }
        
        DataElementCategory cat = identifiableObjectManager.get( DataElementCategory.class, uid );
        
        if ( cat != null )
        {
            return cat;
        }
        
        return null;
    }
    
    public DimensionType getDimensionType( String uid )
    {
        DataElementGroupSet degs = identifiableObjectManager.get( DataElementGroupSet.class, uid );
        
        if ( degs != null )
        {
            return DimensionType.DATAELEMENT_GROUPSET;
        }
        
        OrganisationUnitGroupSet ougs = identifiableObjectManager.get( OrganisationUnitGroupSet.class, uid );
        
        if ( ougs != null )
        {
            return DimensionType.ORGANISATIONUNIT_GROUPSET;
        }
        
        DataElementCategory cat = identifiableObjectManager.get( DataElementCategory.class, uid );
        
        if ( cat != null )
        {
            return DimensionType.CATEGORY;
        }

        final Map<String, DimensionType> dimObjectTypeMap = new HashMap<String, DimensionType>();
        
        dimObjectTypeMap.put( DimensionalObject.DATA_X_DIM_ID, DimensionType.DATA_X );
        dimObjectTypeMap.put( DimensionalObject.INDICATOR_DIM_ID, DimensionType.INDICATOR );
        dimObjectTypeMap.put( DimensionalObject.DATAELEMENT_DIM_ID, DimensionType.DATAELEMENT );
        dimObjectTypeMap.put( DimensionalObject.DATASET_DIM_ID, DimensionType.DATASET );
        dimObjectTypeMap.put( DimensionalObject.DATAELEMENT_OPERAND_ID, DimensionType.DATAELEMENT_OPERAND );
        dimObjectTypeMap.put( DimensionalObject.PERIOD_DIM_ID, DimensionType.PERIOD );
        dimObjectTypeMap.put( DimensionalObject.ORGUNIT_DIM_ID, DimensionType.ORGANISATIONUNIT );
        
        return dimObjectTypeMap.get( uid );
    }
    
    @Override
    public List<DimensionalObject> getAllDimensions()
    {
        Collection<DataElementGroupSet> degs = identifiableObjectManager.getAll( DataElementGroupSet.class );
        Collection<OrganisationUnitGroupSet> ougs = identifiableObjectManager.getAll( OrganisationUnitGroupSet.class );
        Collection<DataElementCategory> dcs = identifiableObjectManager.getAll( DataElementCategory.class );

        final List<DimensionalObject> dimensions = new ArrayList<DimensionalObject>();
        
        dimensions.addAll( degs );
        dimensions.addAll( ougs );
        dimensions.addAll( dcs );
        
        return dimensions;
    }
}
