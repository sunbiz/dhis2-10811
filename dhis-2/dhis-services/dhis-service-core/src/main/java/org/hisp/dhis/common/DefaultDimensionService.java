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

import static org.hisp.dhis.common.DimensionType.CATEGORY;
import static org.hisp.dhis.common.DimensionType.DATAELEMENT;
import static org.hisp.dhis.common.DimensionType.DATAELEMENT_GROUPSET;
import static org.hisp.dhis.common.DimensionType.DATAELEMENT_OPERAND;
import static org.hisp.dhis.common.DimensionType.DATASET;
import static org.hisp.dhis.common.DimensionType.INDICATOR;
import static org.hisp.dhis.common.DimensionType.ORGANISATIONUNIT;
import static org.hisp.dhis.common.DimensionType.ORGANISATIONUNIT_GROUPSET;
import static org.hisp.dhis.common.DimensionType.PERIOD;
import static org.hisp.dhis.common.IdentifiableObjectUtils.getUids;
import static org.hisp.dhis.organisationunit.OrganisationUnit.KEY_USER_ORGUNIT;
import static org.hisp.dhis.organisationunit.OrganisationUnit.KEY_USER_ORGUNIT_CHILDREN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryDimension;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementGroupSet;
import org.hisp.dhis.dataelement.DataElementOperandService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.period.RelativePeriodEnum;
import org.hisp.dhis.period.RelativePeriods;
import org.hisp.dhis.user.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lars Helge Overland
 */
public class DefaultDimensionService
    implements DimensionService
{
    @Autowired
    private IdentifiableObjectManager identifiableObjectManager;
    
    @Autowired
    private DataElementCategoryService categoryService;
    
    @Autowired
    private DataElementOperandService operandService;

    @Autowired
    private PeriodService periodService;
    
    //--------------------------------------------------------------------------
    // DimensionService implementation
    //--------------------------------------------------------------------------

    @Override
    public DimensionalObject getDimension( String uid )
    {        
        DataElementCategory cat = identifiableObjectManager.get( DataElementCategory.class, uid );
        
        if ( cat != null )
        {
            return cat;
        }
        
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
        
        return null;
    }
    
    public DimensionType getDimensionType( String uid )
    {
        DataElementCategory cat = identifiableObjectManager.get( DataElementCategory.class, uid );
        
        if ( cat != null )
        {
            return DimensionType.CATEGORY;
        }

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
        Collection<DataElementCategory> dcs = categoryService.getDataDimensionDataElementCategories();
        Collection<DataElementGroupSet> degs = identifiableObjectManager.getAll( DataElementGroupSet.class );
        Collection<OrganisationUnitGroupSet> ougs = identifiableObjectManager.getAll( OrganisationUnitGroupSet.class );

        final List<DimensionalObject> dimensions = new ArrayList<DimensionalObject>();

        dimensions.addAll( dcs );
        dimensions.addAll( degs );
        dimensions.addAll( ougs );
        
        return dimensions;
    }

    @Override
    public void mergeAnalyticalObject( BaseAnalyticalObject object )
    {
        if ( object != null )
        {
            object.getIndicators().clear();
            object.getDataElements().clear();
            object.getDataElementOperands().clear();
            object.getDataSets().clear();
            object.getPeriods().clear();
            object.setRelatives( null );
            object.getOrganisationUnits().clear();
            object.getCategoryDimensions().clear();
            object.getDataElementGroups().clear();
            object.getOrganisationUnitGroups().clear();

            if ( object.getUser() != null )
            {
                object.setUser( identifiableObjectManager.get( User.class, object.getUser().getUid() ) );
            }
            
            mergeDimensionalObjects( object, object.getColumns() );
            mergeDimensionalObjects( object, object.getRows() );
            mergeDimensionalObjects( object, object.getFilters() );
            
        }
    }

    //--------------------------------------------------------------------------
    // Supportive methods
    //--------------------------------------------------------------------------

    private void mergeDimensionalObjects( BaseAnalyticalObject object, List<DimensionalObject> dimensions ) // TODO Add mergeAnalyticalObject to AnalyticsService
    {
        for ( DimensionalObject dimension : dimensions )
        {
            DimensionType type = getDimensionType( dimension.getDimension() );
            
            String dimensionId = dimension.getDimension();
            
            List<String> uids = getUids( dimension.getItems() );
            
            if ( INDICATOR.equals( type ) )
            {
                object.getIndicators().addAll( identifiableObjectManager.getByUid( Indicator.class, uids ) );
            }
            else if ( DATAELEMENT.equals( type ) )
            {
                object.getDataElements().addAll( identifiableObjectManager.getByUid( DataElement.class, uids ) );
            }
            else if ( DATAELEMENT_OPERAND.equals( type ) )
            {
                object.getDataElementOperands().addAll( operandService.getDataElementOperandsByUid( uids ) );
            }
            else if ( DATASET.equals( type ) )
            {
                object.getDataSets().addAll( identifiableObjectManager.getByUid( DataSet.class, uids ) );
            }
            else if ( PERIOD.equals( type ) )
            {
                List<RelativePeriodEnum> enums = new ArrayList<RelativePeriodEnum>();                
                Set<Period> periods = new HashSet<Period>();
                
                for ( String isoPeriod : uids )
                {
                    if ( RelativePeriodEnum.contains( isoPeriod ) )
                    {
                        enums.add( RelativePeriodEnum.valueOf( isoPeriod ) );
                    }
                    else
                    {
                        Period period = PeriodType.getPeriodFromIsoString( isoPeriod );
                    
                        if ( period != null )
                        {
                            periods.add( period );
                        }
                    }
                }

                object.setRelatives( new RelativePeriods().setRelativePeriodsFromEnums( enums ) );
                object.setPeriods( periodService.reloadPeriods( new ArrayList<Period>( periods ) ) );
            }
            else if ( ORGANISATIONUNIT.equals( type ) )
            {
                List<OrganisationUnit> ous = new ArrayList<OrganisationUnit>();
                
                for ( String ou : uids )
                {
                    if ( KEY_USER_ORGUNIT.equals( ou ) )
                    {
                        object.setUserOrganisationUnit( true );
                    }
                    else if ( KEY_USER_ORGUNIT_CHILDREN.equals( ou ) )
                    {
                        object.setUserOrganisationUnitChildren( true );
                    }
                    else
                    {
                        OrganisationUnit unit = identifiableObjectManager.get( OrganisationUnit.class, ou );
                        
                        if ( unit != null )
                        {
                            ous.add( unit );
                        }
                    }
                }
                
                object.setOrganisationUnits( ous );
            }
            else if ( CATEGORY.equals( type ) )
            {
                DataElementCategoryDimension categoryDimension = new DataElementCategoryDimension();
                categoryDimension.setDimension( categoryService.getDataElementCategory( dimensionId ) );
                categoryDimension.getItems().addAll( categoryService.getDataElementCategoryOptionsByUid( uids ) );
                
                object.getCategoryDimensions().add( categoryDimension );
            }
            else if ( DATAELEMENT_GROUPSET.equals( type ) )
            {
                object.getDataElementGroups().addAll( identifiableObjectManager.getByUid( DataElementGroup.class, uids ) );
            }
            else if ( ORGANISATIONUNIT_GROUPSET.equals( type ) )
            {
                object.getOrganisationUnitGroups().addAll( identifiableObjectManager.getByUid( OrganisationUnitGroup.class, uids ) );
            }
        }
    }
}
