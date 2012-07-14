package org.hisp.dhis.dxf2.metadata.importers;

/*
 * Copyright (c) 2012, University of Oslo
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hisp.dhis.attribute.Attribute;
import org.hisp.dhis.attribute.AttributeService;
import org.hisp.dhis.attribute.AttributeValue;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.NameableObject;
import org.hisp.dhis.dataelement.DataElementOperand;
import org.hisp.dhis.dataelement.DataElementOperandService;
import org.hisp.dhis.dxf2.importsummary.ImportConflict;
import org.hisp.dhis.dxf2.metadata.*;
import org.hisp.dhis.dxf2.metadata.handlers.ObjectHandler;
import org.hisp.dhis.dxf2.metadata.handlers.ObjectHandlerUtils;
import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.importexport.ImportStrategy;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.system.util.CollectionUtils;
import org.hisp.dhis.system.util.ReflectionUtils;
import org.hisp.dhis.system.util.functional.Function1;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

import static org.hisp.dhis.system.util.PredicateUtils.idObjectCollectionsWithScanned;
import static org.hisp.dhis.system.util.PredicateUtils.idObjects;

/**
 * Importer that can handle IdentifiableObject and NameableObject.
 *
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class DefaultIdentifiableObjectImporter<T extends BaseIdentifiableObject>
    implements Importer<T>
{
    private static final Log log = LogFactory.getLog( DefaultIdentifiableObjectImporter.class );

    //-------------------------------------------------------------------------------------------------------
    // Dependencies
    //-------------------------------------------------------------------------------------------------------

    @Autowired
    private PeriodService periodService;

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private ExpressionService expressionService;

    @Autowired
    private DataElementOperandService dataElementOperandService;

    @Autowired
    private ObjectBridge objectBridge;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired( required = false )
    private List<ObjectHandler<T>> objectHandlers;

    //-------------------------------------------------------------------------------------------------------
    // Constructor
    //-------------------------------------------------------------------------------------------------------

    public DefaultIdentifiableObjectImporter( Class<T> importerClass )
    {
        this.importerClass = importerClass;
    }

    private final Class<T> importerClass;

    //-------------------------------------------------------------------------------------------------------
    // Internal state
    //-------------------------------------------------------------------------------------------------------

    protected ImportTypeSummary summaryType;

    protected ImportOptions options;

    // keeping this internal for now, might be split into several classes
    private class NonIdentifiableObjects
    {
        private Set<AttributeValue> attributeValues = new HashSet<AttributeValue>();

        private Expression leftSide;
        private Expression rightSide;

        private Set<DataElementOperand> compulsoryDataElementOperands = new HashSet<DataElementOperand>();
        private Set<DataElementOperand> greyedFields = new HashSet<DataElementOperand>();

        public void extract( T object )
        {
            attributeValues = extractAttributeValues( object );
            leftSide = extractExpression( object, "leftSide" );
            rightSide = extractExpression( object, "rightSide" );
            compulsoryDataElementOperands = extractDataElementOperands( object, "compulsoryDataElementOperands" );
            greyedFields = extractDataElementOperands( object, "greyedFields" );
        }

        public void delete( T object )
        {
            if ( !options.isDryRun() )
            {
                deleteAttributeValues( object );
                deleteExpression( object, "leftSide" );
                deleteExpression( object, "rightSide" );
                // deleteDataElementOperands( object, "compulsoryDataElementOperands" );
                deleteDataElementOperands( object, "greyedFields" );
            }
        }

        public void save( T object )
        {
            saveAttributeValues( object, attributeValues );
            saveExpression( object, "leftSide", leftSide );
            saveExpression( object, "rightSide", rightSide );
            // saveDataElementOperands( object, "compulsoryDataElementOperands", compulsoryDataElementOperands );
            saveDataElementOperands( object, "greyedFields", greyedFields );
        }

        private Expression extractExpression( T object, String fieldName )
        {
            Expression expression = null;

            if ( ReflectionUtils.findGetterMethod( fieldName, object ) != null )
            {
                expression = ReflectionUtils.invokeGetterMethod( fieldName, object );

                if ( expression != null )
                {
                    ReflectionUtils.invokeSetterMethod( fieldName, object, new Object[] { null } );
                }
            }

            return expression;
        }

        private Set<DataElementOperand> extractDataElementOperands( T object, String fieldName )
        {
            Set<DataElementOperand> dataElementOperands = new HashSet<DataElementOperand>();

            if ( ReflectionUtils.findGetterMethod( fieldName, object ) != null )
            {
                Set<DataElementOperand> detachedDataElementOperands = ReflectionUtils.invokeGetterMethod( fieldName, object );
                dataElementOperands = new HashSet<DataElementOperand>( detachedDataElementOperands );

                if ( detachedDataElementOperands.size() > 0 )
                {
                    detachedDataElementOperands.clear();
                    ReflectionUtils.invokeSetterMethod( fieldName, object, new HashSet<DataElementOperand>() );
                }
            }

            return dataElementOperands;
        }

        private Set<AttributeValue> extractAttributeValues( T object )
        {
            Set<AttributeValue> attributeValues = new HashSet<AttributeValue>();

            if ( ReflectionUtils.findGetterMethod( "attributeValues", object ) != null )
            {
                attributeValues = ReflectionUtils.invokeGetterMethod( "attributeValues", object );

                if ( attributeValues.size() > 0 )
                {
                    ReflectionUtils.invokeSetterMethod( "attributeValues", object, new HashSet<AttributeValue>() );
                }
            }

            return attributeValues;
        }

        private void saveExpression( T object, String fieldName, Expression expression )
        {
            if ( expression != null )
            {
                Map<Field, Collection<Object>> identifiableObjectCollections = detachCollectionFields( expression );
                reattachCollectionFields( expression, identifiableObjectCollections );

                expression.setId( 0 );
                expressionService.addExpression( expression );

                ReflectionUtils.invokeSetterMethod( fieldName, object, expression );
            }
        }

        private void saveDataElementOperands( T object, String fieldName, Set<DataElementOperand> dataElementOperands )
        {
            if ( dataElementOperands.size() > 0 )
            {
                for ( DataElementOperand dataElementOperand : dataElementOperands )
                {
                    Map<Field, Object> identifiableObjects = detachFields( dataElementOperand );
                    reattachFields( dataElementOperand, identifiableObjects );

                    dataElementOperand.setId( 0 );
                    dataElementOperandService.addDataElementOperand( dataElementOperand );
                }

                ReflectionUtils.invokeSetterMethod( fieldName, object, dataElementOperands );
            }
        }

        private void saveAttributeValues( T object, Set<AttributeValue> attributeValues )
        {
            if ( attributeValues.size() > 0 )
            {
                CollectionUtils.forEach( attributeValues, new Function1<AttributeValue>()
                {
                    @Override
                    public void apply( AttributeValue attributeValue )
                    {
                        Attribute attribute = objectBridge.getObject( attributeValue.getAttribute() );

                        if ( attribute == null )
                        {
                            log.warn( "Unknown reference to " + attributeValue.getAttribute() + " on object " + attributeValue );
                            return;
                        }

                        attributeValue.setId( 0 );
                        attributeValue.setAttribute( attribute );
                    }
                } );

                for ( AttributeValue attributeValue : attributeValues )
                {
                    attributeService.addAttributeValue( attributeValue );
                }

                ReflectionUtils.invokeSetterMethod( "attributeValues", object, attributeValues );
            }
        }

        private void deleteExpression( T object, String fieldName )
        {
            Expression expression = extractExpression( object, fieldName );

            if ( expression != null )
            {
                expressionService.deleteExpression( expression );
            }
        }

        private void deleteDataElementOperands( T object, String fieldName )
        {
            Set<DataElementOperand> dataElementOperands = extractDataElementOperands( object, fieldName );

            CollectionUtils.forEach( dataElementOperands, new Function1<DataElementOperand>()
            {
                @Override
                public void apply( DataElementOperand dataElementOperand )
                {
                    dataElementOperandService.deleteDataElementOperand( dataElementOperand );
                }
            } );
        }

        private void deleteAttributeValues( T object )
        {
            if ( !Attribute.class.isAssignableFrom( object.getClass() ) )
            {
                Set<AttributeValue> attributeValues = extractAttributeValues( object );

                CollectionUtils.forEach( attributeValues, new Function1<AttributeValue>()
                {
                    @Override
                    public void apply( AttributeValue attributeValue )
                    {
                        attributeService.deleteAttributeValue( attributeValue );
                    }
                } );
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------
    // Generic implementations of newObject and updatedObject
    //-------------------------------------------------------------------------------------------------------

    /**
     * Called every time a new object is to be imported.
     *
     * @param object Object to import
     * @return An ImportConflict instance if there was a conflict, otherwise null
     */
    protected boolean newObject( T object )
    {
        // make sure that the internalId is 0, so that the system will generate a ID
        object.setId( 0 );

        NonIdentifiableObjects nonIdentifiableObjects = new NonIdentifiableObjects();
        nonIdentifiableObjects.extract( object );

        Map<Field, Object> fields = detachFields( object );
        Map<Field, Collection<Object>> collectionFields = detachCollectionFields( object );

        reattachFields( object, fields );

        log.debug( "Trying to save new object => " + ImportUtils.getDisplayName( object ) + " (" + object.getClass().getSimpleName() + ")" );
        objectBridge.saveObject( object );

        updatePeriodTypes( object );
        reattachCollectionFields( object, collectionFields );

        objectBridge.updateObject( object );

        if ( !options.isDryRun() )
        {
            sessionFactory.getCurrentSession().flush();
            nonIdentifiableObjects.save( object );
            sessionFactory.getCurrentSession().flush();
        }

        log.debug( "Save successful." );

        return true;
    }

    /**
     * Update object from old => new.
     *
     * @param object          Object to import
     * @param persistedObject The current version of the object
     * @return An ImportConflict instance if there was a conflict, otherwise null
     */
    protected boolean updateObject( T object, T persistedObject )
    {
        NonIdentifiableObjects nonIdentifiableObjects = new NonIdentifiableObjects();
        nonIdentifiableObjects.extract( object );
        nonIdentifiableObjects.delete( persistedObject );

        Map<Field, Object> fields = detachFields( object );
        Map<Field, Collection<Object>> collectionFields = detachCollectionFields( object );

        reattachFields( object, fields );

        persistedObject.mergeWith( object );
        updatePeriodTypes( persistedObject );

        reattachCollectionFields( persistedObject, collectionFields );

        log.debug( "Starting update of object " + ImportUtils.getDisplayName( persistedObject ) + " (" + persistedObject.getClass()
            .getSimpleName() + ")" );

        objectBridge.updateObject( persistedObject );

        if ( !options.isDryRun() )
        {
            sessionFactory.getCurrentSession().flush();
            nonIdentifiableObjects.save( persistedObject );
            sessionFactory.getCurrentSession().flush();
        }

        log.debug( "Update successful." );

        return true;
    }

    private void updatePeriodTypes( T object )
    {
        for ( Field field : object.getClass().getDeclaredFields() )
        {
            if ( PeriodType.class.isAssignableFrom( field.getType() ) )
            {
                PeriodType periodType = ReflectionUtils.invokeGetterMethod( field.getName(), object );
                periodType = objectBridge.getObject( periodType );
                ReflectionUtils.invokeSetterMethod( field.getName(), object, periodType );
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------
    // Importer<T> Implementation
    //-------------------------------------------------------------------------------------------------------

    @Override
    public ImportTypeSummary importObjects( List<T> objects, ImportOptions options )
    {
        this.options = options;
        this.summaryType = new ImportTypeSummary( importerClass.getSimpleName() );

        if ( objects.isEmpty() )
        {
            return summaryType;
        }

        ObjectHandlerUtils.preObjectsHandlers( objects, objectHandlers );

        for ( T object : objects )
        {
            ObjectHandlerUtils.preObjectHandlers( object, objectHandlers );

            importObjectLocal( object );

            ObjectHandlerUtils.postObjectHandlers( object, objectHandlers );
        }

        ObjectHandlerUtils.postObjectsHandlers( objects, objectHandlers );

        return summaryType;
    }

    @Override
    public ImportTypeSummary importObject( T object, ImportOptions options )
    {
        this.options = options;
        this.summaryType = new ImportTypeSummary( importerClass.getSimpleName() );

        importObjectLocal( object );

        return summaryType;
    }

    @Override
    public boolean canHandle( Class<?> clazz )
    {
        return importerClass.equals( clazz );
    }

    //-------------------------------------------------------------------------------------------------------
    // Helpers
    //-------------------------------------------------------------------------------------------------------

    private void importObjectLocal( T object )
    {
        if ( validateIdentifiableObject( object ) )
        {
            startImport( object );
        }
        else
        {
            summaryType.incrementIgnored();
        }
    }

    private void startImport( T object )
    {
        T oldObject = objectBridge.getObject( object );

        if ( ImportStrategy.NEW.equals( options.getImportStrategy() ) )
        {
            if ( newObject( object ) )
            {
                summaryType.incrementImported();
            }
        }
        else if ( ImportStrategy.UPDATES.equals( options.getImportStrategy() ) )
        {
            if ( updateObject( object, oldObject ) )
            {
                summaryType.incrementUpdated();
            }
        }
        else if ( ImportStrategy.NEW_AND_UPDATES.equals( options.getImportStrategy() ) )
        {
            if ( oldObject != null )
            {
                if ( updateObject( object, oldObject ) )
                {
                    summaryType.incrementUpdated();
                }
            }
            else
            {
                if ( newObject( object ) )
                {
                    summaryType.incrementImported();
                }
            }
        }
    }

    private boolean validateIdentifiableObject( T object )
    {
        ImportConflict conflict = null;
        boolean success = true;

        if ( object.getName() == null || object.getName().length() == 0 )
        {
            conflict = new ImportConflict( ImportUtils.getDisplayName( object ), "Empty name for object " + object );
        }

        if ( NameableObject.class.isInstance( object ) )
        {
            NameableObject nameableObject = (NameableObject) object;

            if ( nameableObject.getShortName() == null || nameableObject.getShortName().length() == 0 )
            {
                conflict = new ImportConflict( ImportUtils.getDisplayName( object ), "Empty shortName for object " + object );
            }
        }

        if ( conflict != null )
        {
            summaryType.getImportConflicts().add( conflict );
        }

        if ( ImportStrategy.NEW.equals( options.getImportStrategy() ) )
        {
            success = validateForNewStrategy( object );
        }
        else if ( ImportStrategy.UPDATES.equals( options.getImportStrategy() ) )
        {
            success = validateForUpdatesStrategy( object );
        }
        else if ( ImportStrategy.NEW_AND_UPDATES.equals( options.getImportStrategy() ) )
        {
            // if we have a match on at least one of the objects, then assume update
            if ( objectBridge.getObjects( object ).size() > 0 )
            {
                success = validateForUpdatesStrategy( object );
            }
            else
            {
                success = validateForNewStrategy( object );
            }
        }

        return success;
    }

    private boolean validateForUpdatesStrategy( T object )
    {
        ImportConflict conflict = null;
        Collection<T> objects = objectBridge.getObjects( object );

        if ( objects.isEmpty() )
        {
            conflict = reportLookupConflict( object );
        }
        else if ( objects.size() > 1 )
        {
            conflict = reportMoreThanOneConflict( object );
        }

        if ( conflict != null )
        {
            summaryType.getImportConflicts().add( conflict );

            return false;
        }

        return true;
    }

    private boolean validateForNewStrategy( T object )
    {
        ImportConflict conflict;
        Collection<T> objects = objectBridge.getObjects( object );

        if ( objects.size() > 0 )
        {
            conflict = reportConflict( object );
            summaryType.getImportConflicts().add( conflict );

            return false;
        }

        return true;
    }

    private IdentifiableObject findObjectByReference( IdentifiableObject identifiableObject )
    {
        if ( identifiableObject == null )
        {
            return null;
        }
        else if ( Period.class.isAssignableFrom( identifiableObject.getClass() ) )
        {
            Period period = (Period) identifiableObject;
            period = periodService.reloadPeriod( period );

            if ( !options.isDryRun() )
            {
                sessionFactory.getCurrentSession().flush();
            }

            return period;
        }

        return objectBridge.getObject( identifiableObject );
    }

    private Map<Field, Object> detachFields( final Object object )
    {
        final Map<Field, Object> fieldMap = new HashMap<Field, Object>();
        final Collection<Field> fieldCollection = ReflectionUtils.collectFields( object.getClass(), idObjects );

        CollectionUtils.forEach( fieldCollection, new Function1<Field>()
        {
            @Override
            public void apply( Field field )
            {
                Object ref = ReflectionUtils.invokeGetterMethod( field.getName(), object );

                if ( ref != null )
                {
                    fieldMap.put( field, ref );
                    ReflectionUtils.invokeSetterMethod( field.getName(), object, new Object[] { null } );
                }
            }
        } );

        return fieldMap;
    }

    private void reattachFields( Object object, Map<Field, Object> fields )
    {
        for ( Field field : fields.keySet() )
        {
            IdentifiableObject idObject = (IdentifiableObject) fields.get( field );
            IdentifiableObject ref = findObjectByReference( idObject );

            if ( ref == null )
            {
                if ( ExchangeClasses.getImportMap().get( idObject.getClass() ) != null )
                {
                    reportReferenceError( object, idObject );
                }
            }

            if ( !options.isDryRun() )
            {
                ReflectionUtils.invokeSetterMethod( field.getName(), object, ref );
            }
        }
    }

    private Map<Field, Collection<Object>> detachCollectionFields( final Object object )
    {
        final Map<Field, Collection<Object>> collectionFields = new HashMap<Field, Collection<Object>>();
        final Collection<Field> fieldCollection = ReflectionUtils.collectFields( object.getClass(), idObjectCollectionsWithScanned );

        CollectionUtils.forEach( fieldCollection, new Function1<Field>()
        {
            @Override
            public void apply( Field field )
            {
                Collection<Object> objects = ReflectionUtils.invokeGetterMethod( field.getName(), object );

                if ( objects != null && !objects.isEmpty() )
                {
                    collectionFields.put( field, objects );
                    Collection<Object> emptyCollection = ReflectionUtils.newCollectionInstance( field.getType() );
                    ReflectionUtils.invokeSetterMethod( field.getName(), object, emptyCollection );
                }
            }
        } );

        return collectionFields;
    }

    private void reattachCollectionFields( final Object object, Map<Field, Collection<Object>> collectionFields )
    {
        for ( Field field : collectionFields.keySet() )
        {
            Collection<Object> collection = collectionFields.get( field );
            final Collection<Object> objects = ReflectionUtils.newCollectionInstance( field.getType() );

            CollectionUtils.forEach( collection, new Function1<Object>()
            {
                @Override
                public void apply( Object object )
                {
                    IdentifiableObject ref = findObjectByReference( (IdentifiableObject) object );

                    if ( ref != null )
                    {
                        objects.add( ref );
                    }
                    else
                    {
                        if ( ExchangeClasses.getImportMap().get( object.getClass() ) != null )
                        {
                            reportReferenceError( object, object );
                        }
                    }
                }
            } );

            if ( !options.isDryRun() )
            {
                ReflectionUtils.invokeSetterMethod( field.getName(), object, objects );
            }
        }
    }

    private ImportConflict reportLookupConflict( IdentifiableObject object )
    {
        return new ImportConflict( ImportUtils.getDisplayName( object ), "Object does not exist." );
    }

    private ImportConflict reportMoreThanOneConflict( IdentifiableObject object )
    {
        return new ImportConflict( ImportUtils.getDisplayName( object ), "More than one object matches identifiers." );
    }

    private ImportConflict reportConflict( IdentifiableObject object )
    {
        return new ImportConflict( ImportUtils.getDisplayName( object ), "Object already exists." );
    }

    private void reportReferenceError( Object object, Object idObject )
    {
        String referenceName = idObject != null ? idObject.getClass().getSimpleName() : "null";
        String objectName = object != null ? object.getClass().getSimpleName() : "null";

        String logMsg = "Unknown reference to " + idObject + " (" + referenceName + ")" +
            " on object " + object + " (" + objectName + ").";

        log.warn( logMsg );

        ImportConflict importConflict = new ImportConflict( ImportUtils.getDisplayName( object ), logMsg );
        summaryType.getImportConflicts().add( importConflict );
    }
}
