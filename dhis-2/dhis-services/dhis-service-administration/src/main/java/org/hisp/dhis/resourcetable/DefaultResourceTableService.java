package org.hisp.dhis.resourcetable;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.amplecode.quick.BatchHandler;
import org.amplecode.quick.BatchHandlerFactory;
import org.hisp.dhis.common.comparator.IdentifiableObjectNameComparator;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementGroupSet;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorGroupSet;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.jdbc.batchhandler.GenericBatchHandler;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.resourcetable.statement.CreateCategoryTableStatement;
import org.hisp.dhis.resourcetable.statement.CreateDataElementGroupSetTableStatement;
import org.hisp.dhis.resourcetable.statement.CreateIndicatorGroupSetTableStatement;
import org.hisp.dhis.resourcetable.statement.CreateOrganisationUnitGroupSetTableStatement;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultResourceTableService.java 5459 2008-06-26 01:12:03Z
 *          larshelg $
 */
public class DefaultResourceTableService
    implements ResourceTableService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ResourceTableStore resourceTableStore;

    public void setResourceTableStore( ResourceTableStore resourceTableStore )
    {
        this.resourceTableStore = resourceTableStore;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    private DataElementCategoryService categoryService;

    public void setCategoryService( DataElementCategoryService categoryService )
    {
        this.categoryService = categoryService;
    }
    
    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }
    
    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private BatchHandlerFactory batchHandlerFactory;

    public void setBatchHandlerFactory( BatchHandlerFactory batchHandlerFactory )
    {
        this.batchHandlerFactory = batchHandlerFactory;
    }

    // -------------------------------------------------------------------------
    // OrganisationUnitStructure
    // -------------------------------------------------------------------------

    public void generateOrganisationUnitStructures()
    {
        int maxLevel = organisationUnitService.getMaxOfOrganisationUnitLevels();
        
        resourceTableStore.createOrganisationUnitStructure( maxLevel );

        BatchHandler<Object> batchHandler = batchHandlerFactory.createBatchHandler( GenericBatchHandler.class ).
            setTableName( ResourceTableStore.TABLE_NAME_ORGANISATION_UNIT_STRUCTURE ).init();
        
        for ( int i = 0; i < maxLevel; i++ )
        {
            int level = i + 1;

            Collection<OrganisationUnit> units = organisationUnitService.getOrganisationUnitsAtLevel( level );

            for ( OrganisationUnit unit : units )
            {
                List<Integer> structure = new ArrayList<Integer>();

                structure.add( unit.getId() );
                structure.add( level );

                Map<Integer, Integer> identifiers = new HashMap<Integer, Integer>();

                for ( int j = level; j > 0; j-- )
                {
                    identifiers.put( j, unit.getId() );

                    unit = unit.getParent();
                }
               
                for (int k = 1 ; k <= maxLevel ; k ++ )
                {
                    structure.add( identifiers.get( k ) );
                }
                
                batchHandler.addObject( structure );
            }
        }

        batchHandler.flush();
    }

    // -------------------------------------------------------------------------
    // DataElementCategoryOptionComboName
    // -------------------------------------------------------------------------

    public void generateCategoryOptionComboNames()
    {
        resourceTableStore.createDataElementCategoryOptionComboName();

        Collection<DataElementCategoryOptionCombo> combos = categoryService.getAllDataElementCategoryOptionCombos();

        BatchHandler<Object> batchHandler = batchHandlerFactory.createBatchHandler( GenericBatchHandler.class ).
            setTableName( ResourceTableStore.TABLE_NAME_CATEGORY_OPTION_COMBO_NAME ).init();
        
        for ( DataElementCategoryOptionCombo combo : combos )
        {
            final List<String> values = new ArrayList<String>();
            
            values.add( String.valueOf( combo.getId() ) );
            values.add( combo.getName() );
            
            batchHandler.addObject( values );
        }
        
        batchHandler.flush();
    }

    // -------------------------------------------------------------------------
    // DataElementGroupSetTable
    // -------------------------------------------------------------------------

    public void generateDataElementGroupSetTable()
    {
        // ---------------------------------------------------------------------
        // Create table
        // ---------------------------------------------------------------------

        List<DataElement> dataElements = new ArrayList<DataElement>( dataElementService.getAllDataElements() );
        
        Collections.sort( dataElements, IdentifiableObjectNameComparator.INSTANCE );
        
        List<DataElementGroupSet> groupSets = new ArrayList<DataElementGroupSet>( dataElementService.getAllDataElementGroupSets() );
        
        Collections.sort( groupSets, IdentifiableObjectNameComparator.INSTANCE );
        
        resourceTableStore.createDataElementGroupSetStructure( groupSets );

        // ---------------------------------------------------------------------
        // Populate table
        // ---------------------------------------------------------------------

        BatchHandler<Object> batchHandler = batchHandlerFactory.createBatchHandler( GenericBatchHandler.class ).
            setTableName( CreateDataElementGroupSetTableStatement.TABLE_NAME ).init();

        for ( DataElement dataElement : dataElements )
        {
            final List<String> values = new ArrayList<String>();

            values.add( String.valueOf( dataElement.getId() ) );
            values.add( dataElement.getName() );
            
            for ( DataElementGroupSet groupSet : groupSets )
            {
                DataElementGroup group = groupSet.getGroup( dataElement );
                
                values.add( group != null ? group.getName() : null );    
            }
            
            batchHandler.addObject( values );
        }
        
        batchHandler.flush();
    }

    // -------------------------------------------------------------------------
    // IndicatorGroupSetTable
    // -------------------------------------------------------------------------

    public void generateIndicatorGroupSetTable()
    {
        // ---------------------------------------------------------------------
        // Create table
        // ---------------------------------------------------------------------

        List<Indicator> indicators = new ArrayList<Indicator>( indicatorService.getAllIndicators() );
        
        Collections.sort( indicators, IdentifiableObjectNameComparator.INSTANCE );
        
        List<IndicatorGroupSet> groupSets = new ArrayList<IndicatorGroupSet>( indicatorService.getAllIndicatorGroupSets() );
        
        Collections.sort( groupSets, IdentifiableObjectNameComparator.INSTANCE );
        
        resourceTableStore.createIndicatorGroupSetStructure( groupSets );

        // ---------------------------------------------------------------------
        // Populate table
        // ---------------------------------------------------------------------

        BatchHandler<Object> batchHandler = batchHandlerFactory.createBatchHandler( GenericBatchHandler.class ).
            setTableName( CreateIndicatorGroupSetTableStatement.TABLE_NAME ).init();

        for ( Indicator indicator : indicators )
        {
            final List<String> values = new ArrayList<String>();
            
            values.add( String.valueOf( indicator.getId() ) );
            values.add( indicator.getName() );
            
            for ( IndicatorGroupSet groupSet : groupSets )
            {
                IndicatorGroup group = groupSet.getGroup( indicator );
                
                values.add( group != null ? group.getName() : null );    
            }
            
            batchHandler.addObject( values );
        }
        
        batchHandler.flush();        
    }
    
    // -------------------------------------------------------------------------
    // OrganisationUnitGroupSetTable
    // -------------------------------------------------------------------------

    public void generateOrganisationUnitGroupSetTable()
    {
        // ---------------------------------------------------------------------
        // Create table
        // ---------------------------------------------------------------------

        List<OrganisationUnit> units = new ArrayList<OrganisationUnit>( organisationUnitService
            .getAllOrganisationUnits() );

        Collections.sort( units, IdentifiableObjectNameComparator.INSTANCE );

        List<OrganisationUnitGroupSet> groupSets = new ArrayList<OrganisationUnitGroupSet>(
            organisationUnitGroupService.getAllOrganisationUnitGroupSets() );

        Collections.sort( groupSets, IdentifiableObjectNameComparator.INSTANCE );

        resourceTableStore.createOrganisationUnitGroupSetStructure( groupSets );

        // ---------------------------------------------------------------------
        // Populate table
        // ---------------------------------------------------------------------

        BatchHandler<Object> batchHandler = batchHandlerFactory.createBatchHandler( GenericBatchHandler.class ).
            setTableName( CreateOrganisationUnitGroupSetTableStatement.TABLE_NAME ).init();

        for ( OrganisationUnit unit : units )
        {
            final List<String> values = new ArrayList<String>();

            values.add( String.valueOf( unit.getId() ) );
            values.add( unit.getName() );

            for ( OrganisationUnitGroupSet groupSet : groupSets )
            {
                OrganisationUnitGroup group = groupSet.getGroup( unit );
                
                values.add( group != null ? group.getName() : null );
            }

            batchHandler.addObject( values );
        }

        batchHandler.flush();
    }
    
    // -------------------------------------------------------------------------
    // CategoryTable
    // -------------------------------------------------------------------------

    public void generateCategoryTable()
    {
        // ---------------------------------------------------------------------
        // Create table
        // ---------------------------------------------------------------------

        List<DataElementCategory> categories = new ArrayList<DataElementCategory>( categoryService.getAllDataElementCategories() );
        
        Collections.sort( categories, IdentifiableObjectNameComparator.INSTANCE );
        
        List<DataElementCategoryOptionCombo> categoryOptionCombos = 
            new ArrayList<DataElementCategoryOptionCombo>( categoryService.getAllDataElementCategoryOptionCombos() );
        
        resourceTableStore.createCategoryStructure( categories );

        // ---------------------------------------------------------------------
        // Populate table
        // ---------------------------------------------------------------------

        BatchHandler<Object> batchHandler = batchHandlerFactory.createBatchHandler( GenericBatchHandler.class ).
            setTableName( CreateCategoryTableStatement.TABLE_NAME ).init();
        
        for ( DataElementCategoryOptionCombo categoryOptionCombo : categoryOptionCombos )
        {
            final List<String> values = new ArrayList<String>();
            
            values.add( String.valueOf( categoryOptionCombo.getId() ) );
            values.add( categoryOptionCombo.getName() );
            
            for ( DataElementCategory category : categories )
            {
                DataElementCategoryOption dimensionOption = category.getCategoryOption( categoryOptionCombo );
                
                values.add( dimensionOption != null ? dimensionOption.getName() : null );    
            }
            
            batchHandler.addObject( values );
        }
        
        batchHandler.flush();
    }

    // -------------------------------------------------------------------------
    // DataElementTable
    // -------------------------------------------------------------------------

    public void generateDataElementTable()
    {
        // ---------------------------------------------------------------------
        // Create table
        // ---------------------------------------------------------------------

        Collection<DataElement> dataElements = dataElementService.getAllDataElements();
        
        resourceTableStore.createDataElementStructure();

        // ---------------------------------------------------------------------
        // Populate table
        // ---------------------------------------------------------------------

        BatchHandler<Object> batchHandler = batchHandlerFactory.createBatchHandler( GenericBatchHandler.class ).
            setTableName( ResourceTableStore.TABLE_NAME_DATA_ELEMENT_STRUCTURE ).init();
        
        for ( DataElement dataElement : dataElements )
        {
            final List<String> values = new ArrayList<String>();
            
            final PeriodType periodType = dataElement.getPeriodType();
            
            values.add( String.valueOf( dataElement.getId() ) );
            values.add( dataElement.getName() );
            values.add( periodType != null ? String.valueOf( periodType.getId() ) : null );
            values.add( periodType != null ? periodType.getName() : null );
            
            batchHandler.addObject( values );
        }
        
        batchHandler.flush();
    }
}
