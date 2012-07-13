package org.hisp.dhis.api.mobile.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

@Component
public class MessageConverterAddingPostProcessor
    implements BeanPostProcessor
{

    private final static Log logger = LogFactory.getLog( MessageConverterAddingPostProcessor.class );

    private HttpMessageConverter<?> messageConverter = new DataStreamSerializableMessageConverter();

    public Object postProcessBeforeInitialization( Object bean, String beanName )
        throws BeansException
    {
        return bean;
    }

    public Object postProcessAfterInitialization( Object bean, String beanName )
        throws BeansException
    {

        if ( !(bean instanceof AnnotationMethodHandlerAdapter) )
        {
            return bean;
        }

        AnnotationMethodHandlerAdapter handlerAdapter = (AnnotationMethodHandlerAdapter) bean;

        HttpMessageConverter<?>[] converterArray = handlerAdapter.getMessageConverters();
        
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(
            Arrays.asList( converterArray ) );

        converters.add( 0, messageConverter );

        converterArray = converters.toArray( new HttpMessageConverter<?>[converters.size()] );

        handlerAdapter.setMessageConverters( converterArray );

        log( converterArray );
        
        return handlerAdapter;
    }

    private void log( HttpMessageConverter<?>[] array )
    {
        StringBuilder sb = new StringBuilder("Converters after adding custom one: ");

        for ( HttpMessageConverter<?> httpMessageConverter : array )
        {
            sb.append( httpMessageConverter.getClass().getName() ).append( ", " );
        }
        
        String string = sb.toString();
        logger.info( string.substring( 0, string.length() - 2 ) );
    }

}
