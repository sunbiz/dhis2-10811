package org.hisp.dhis.web.webapi.v1.utils;

import org.springframework.ui.Model;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class ContextUtils
{
    public static void populateContextPath( Model model, HttpServletRequest request )
    {
        UriComponents contextPath = ServletUriComponentsBuilder.fromContextPath( request ).build();

        String contextPathString = contextPath.toString();
        String xForwardedProto = request.getHeader( "X-Forwarded-Proto" );

        if ( xForwardedProto != null )
        {
            if ( contextPathString.contains( "http://" ) && xForwardedProto.equalsIgnoreCase( "https" ) )
            {
                contextPathString = contextPathString.replace( "http://", "https://" );
            }
            else if ( contextPathString.contains( "https://" ) && xForwardedProto.equalsIgnoreCase( "http" ) )
            {
                contextPathString = contextPathString.replace( "https://", "http://" );
            }
        }

        model.addAttribute( "contextPath", contextPathString );
    }
}
