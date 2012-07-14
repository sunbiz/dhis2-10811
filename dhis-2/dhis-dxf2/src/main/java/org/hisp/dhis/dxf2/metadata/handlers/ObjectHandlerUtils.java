package org.hisp.dhis.dxf2.metadata.handlers;

import java.util.List;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class ObjectHandlerUtils
{
    public static <T> void preObjectHandlers( T object, List<ObjectHandler<T>> objectHandlers )
    {
        for ( ObjectHandler<T> objectHandler : objectHandlers )
        {
            if ( objectHandler.canHandle( object.getClass() ) )
            {
                objectHandler.preImportObject( object );
            }
        }
    }

    public static <T> void postObjectHandlers( T object, List<ObjectHandler<T>> objectHandlers )
    {
        for ( ObjectHandler<T> objectHandler : objectHandlers )
        {
            if ( objectHandler.canHandle( object.getClass() ) )
            {
                objectHandler.postImportObject( object );
            }
        }
    }

    public static <T> void preObjectsHandlers( List<T> objects, List<ObjectHandler<T>> objectHandlers )
    {
        if ( objects.size() > 0 )
        {
            for ( ObjectHandler<T> objectHandler : objectHandlers )
            {
                if ( objectHandler.canHandle( objects.get( 0 ).getClass() ) )
                {
                    objectHandler.preImportObjects( objects );
                }
            }
        }
    }

    public static <T> void postObjectsHandlers( List<T> objects, List<ObjectHandler<T>> objectHandlers )
    {
        if ( objects.size() > 0 )
        {
            for ( ObjectHandler<T> objectHandler : objectHandlers )
            {
                if ( objectHandler.canHandle( objects.get( 0 ).getClass() ) )
                {
                    objectHandler.postImportObjects( objects );
                }
            }
        }
    }

}
