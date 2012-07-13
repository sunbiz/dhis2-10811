package org.hisp.dhis.api.controller;

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

import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.common.Pager;
import org.hisp.dhis.dxf2.message.Message;
import org.hisp.dhis.dxf2.utils.JacksonUtils;
import org.hisp.dhis.message.MessageConversation;
import org.hisp.dhis.message.MessageService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
@Controller
@RequestMapping( value = MessageConversationController.RESOURCE_PATH )
public class MessageConversationController
    extends AbstractCrudController<MessageConversation>
{
    public static final String RESOURCE_PATH = "/messageConversations";

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    protected List<MessageConversation> getEntityList( WebMetaData metaData, WebOptions options )
    {
        List<MessageConversation> entityList;

        Date lastUpdated = options.getLastUpdated();

        if ( lastUpdated != null )
        {
            entityList = new ArrayList<MessageConversation>( manager.getByLastUpdated( getEntityClass(), lastUpdated ) );
        }
        else if ( options.hasPaging() )
        {
            int count = manager.getCount( getEntityClass() );

            Pager pager = new Pager( options.getPage(), count );
            metaData.setPager( pager );

            entityList = new ArrayList<MessageConversation>( messageService.getMessageConversations( pager.getOffset(), pager.getPageSize() ) );
        }
        else
        {
            entityList = new ArrayList<MessageConversation>( manager.getAll( getEntityClass() ) );
        }

        return entityList;
    }

    //--------------------------------------------------------------------------
    // POST for new MessageConversation
    //--------------------------------------------------------------------------

    @Override
    @PreAuthorize( "hasRole('ALL') or hasRole('F_SEND_MESSAGE')" )
    public void postXmlObject( HttpServletResponse response, HttpServletRequest request, InputStream input ) throws Exception
    {
        Message message = JacksonUtils.fromXml( input, Message.class );

        List<User> users = new ArrayList<User>( message.getUsers() );
        message.getUsers().clear();

        for ( User u : users )
        {
            User user  = userService.getUser( u.getUid() );
            
            if ( user == null )
            {
                ContextUtils.conflictResponse( response, "User does not exist: " + u.getUid() );
                return;
            }
            
            message.getUsers().add( user );
        }

        String metaData = MessageService.META_USER_AGENT + request.getHeader( ContextUtils.HEADER_USER_AGENT );

        int id = messageService.sendMessage( message.getSubject(), message.getText(), metaData, message.getUsers() );
        MessageConversation m = messageService.getMessageConversation( id );

        response.setStatus( HttpServletResponse.SC_CREATED );
        response.setHeader( "Location", MessageConversationController.RESOURCE_PATH + "/" + m.getUid() );
    }

    //--------------------------------------------------------------------------
    // POST for reply on existing MessageConversation
    //--------------------------------------------------------------------------

    @RequestMapping( value = "/{uid}", method = RequestMethod.POST )
    public void postMessageConversationReply( @PathVariable( "uid" ) String uid, @RequestBody String body,
        HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        String metaData = MessageService.META_USER_AGENT + request.getHeader( ContextUtils.HEADER_USER_AGENT );

        MessageConversation messageConversation = messageService.getMessageConversation( uid );
        
        if ( messageConversation == null )
        {
            ContextUtils.conflictResponse( response, "Message conversation does not exist: " + uid );
            return;
        }

        messageService.sendReply( messageConversation, body, metaData );        

        response.setStatus( HttpServletResponse.SC_CREATED );
    }

    //--------------------------------------------------------------------------
    // POST for feedback
    //--------------------------------------------------------------------------

    @RequestMapping( value = "/feedback", method = RequestMethod.POST )
    public void postMessageConversationFeedback( @RequestParam( "subject" ) String subject, @RequestBody String body,
        HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
        String metaData = MessageService.META_USER_AGENT + request.getHeader( ContextUtils.HEADER_USER_AGENT );

        messageService.sendFeedback( subject, body, metaData );
        
        response.setStatus( HttpServletResponse.SC_CREATED );
    }
}
