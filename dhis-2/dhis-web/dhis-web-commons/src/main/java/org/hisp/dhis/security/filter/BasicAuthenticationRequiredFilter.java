package org.hisp.dhis.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Ugly hack, adding a version of {@link BasicAuthenticationFilter} that can
 * require authentication. Don't think this is the way to do it, but it seems to
 * be how it's done with {@link RequiredLoginFilter}, so...
 * <p>
 * Basically, if not already logged in and the request doesn't supply a Basic
 * header (those should be handled by super class), ask for it
 */
public class BasicAuthenticationRequiredFilter
    extends BasicAuthenticationFilter
{

    @Override
    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain )
        throws IOException, ServletException
    {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        String header = request.getHeader( "Authorization" );

        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

        if ( (existingAuth == null || !existingAuth.isAuthenticated())
            && (header == null || !header.startsWith( "Basic " )) )
        {
            super.getAuthenticationEntryPoint().commence( request, response,
                new AuthenticationCredentialsNotFoundException( "Authentication required" ) );
            return;
        }

        super.doFilter( req, res, chain );
    }

}
