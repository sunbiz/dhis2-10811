package org.hisp.dhis.api.controller.user;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.hisp.dhis.api.utils.ContextUtils;
import org.hisp.dhis.api.utils.ContextUtils.CacheStrategy;
import org.hisp.dhis.api.utils.FormUtils;
import org.hisp.dhis.api.webdomain.FormDataSet;
import org.hisp.dhis.api.webdomain.FormOrganisationUnit;
import org.hisp.dhis.api.webdomain.Forms;
import org.hisp.dhis.api.webdomain.user.Dashboard;
import org.hisp.dhis.api.webdomain.user.Inbox;
import org.hisp.dhis.api.webdomain.user.Recipients;
import org.hisp.dhis.api.webdomain.user.UserAccount;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.dxf2.utils.JacksonUtils;
import org.hisp.dhis.i18n.I18nService;
import org.hisp.dhis.interpretation.Interpretation;
import org.hisp.dhis.interpretation.InterpretationService;
import org.hisp.dhis.message.MessageConversation;
import org.hisp.dhis.message.MessageService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserGroup;
import org.hisp.dhis.user.UserGroupService;
import org.hisp.dhis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
@Controller
@RequestMapping(value = CurrentUserController.RESOURCE_PATH, method = RequestMethod.GET)
public class CurrentUserController
{
    public static final String RESOURCE_PATH = "/currentUser";

    private static final int MAX_OBJECTS = 50;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private InterpretationService interpretationService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private ContextUtils contextUtils;

    @Autowired
    private I18nService i18nService;

    @RequestMapping( produces = { "application/json", "text/*" } )
    public void getCurrentUser( HttpServletResponse response ) throws Exception
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            ContextUtils.notFoundResponse( response, "User object is null, user is not authenticated." );
            return;
        }

        JacksonUtils.toJson( response.getOutputStream(), currentUser );
    }

    @RequestMapping(value = "/inbox", produces = { "application/json", "text/*" })
    public void getInbox( HttpServletResponse response ) throws Exception
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            ContextUtils.notFoundResponse( response, "User object is null, user is not authenticated." );
            return;
        }

        Inbox inbox = new Inbox();
        inbox.setMessageConversations( new ArrayList<MessageConversation>( messageService.getMessageConversations( 0, MAX_OBJECTS ) ) );
        inbox.setInterpretations( new ArrayList<Interpretation>( interpretationService.getInterpretations( 0, MAX_OBJECTS ) ) );

        JacksonUtils.toJson( response.getOutputStream(), inbox );
    }

    @RequestMapping(value = "/dashboard", produces = { "application/json", "text/*" })
    public void getDashboard( HttpServletResponse response ) throws Exception
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            ContextUtils.notFoundResponse( response, "User object is null, user is not authenticated." );
            return;
        }

        Dashboard dashboard = new Dashboard();
        dashboard.setUnreadMessageConversation( messageService.getUnreadMessageConversationCount() );
        dashboard.setUnreadInterpretations( interpretationService.getNewInterpretationCount() );

        JacksonUtils.toJson( response.getOutputStream(), dashboard );
    }

    @RequestMapping(value = "/user-account", produces = { "application/json", "text/*" })
    public void getUserAccount( HttpServletResponse response ) throws Exception
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            ContextUtils.notFoundResponse( response, "User object is null, user is not authenticated." );
            return;
        }

        UserAccount userAccount = new UserAccount();

        // user account
        userAccount.setFirstName( currentUser.getFirstName() );
        userAccount.setSurname( currentUser.getSurname() );
        userAccount.setEmail( currentUser.getEmail() );
        userAccount.setPhoneNumber( currentUser.getPhoneNumber() );

        // profile
        userAccount.setIntroduction( currentUser.getIntroduction() );
        userAccount.setJobTitle( currentUser.getJobTitle() );
        userAccount.setGender( currentUser.getGender() );

        if ( currentUser.getBirthday() != null )
        {
            userAccount.setBirthday( simpleDateFormat.format( currentUser.getBirthday() ) );
        }

        userAccount.setNationality( currentUser.getNationality() );
        userAccount.setEmployer( currentUser.getEmployer() );
        userAccount.setEducation( currentUser.getEducation() );
        userAccount.setInterests( currentUser.getInterests() );
        userAccount.setLanguages( currentUser.getLanguages() );

        JacksonUtils.toJson( response.getOutputStream(), userAccount );
    }

    @RequestMapping(value = "/user-account", method = RequestMethod.POST, consumes = "application/json")
    public void postUserAccountJson( HttpServletResponse response, HttpServletRequest request ) throws Exception
    {
        UserAccount userAccount = JacksonUtils.fromJson( request.getInputStream(), UserAccount.class );
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            ContextUtils.notFoundResponse( response, "User object is null, user is not authenticated." );
            return;
        }

        // basic user account
        currentUser.setFirstName( userAccount.getFirstName() );
        currentUser.setSurname( userAccount.getSurname() );
        currentUser.setEmail( userAccount.getEmail() );
        currentUser.setPhoneNumber( userAccount.getPhoneNumber() );

        // profile
        currentUser.setIntroduction( userAccount.getIntroduction() );
        currentUser.setJobTitle( userAccount.getJobTitle() );
        currentUser.setGender( userAccount.getGender() );

        if ( userAccount.getBirthday() != null && !userAccount.getBirthday().isEmpty() )
        {
            currentUser.setBirthday( simpleDateFormat.parse( userAccount.getBirthday() ) );
        }

        currentUser.setNationality( userAccount.getNationality() );
        currentUser.setEmployer( userAccount.getEmployer() );
        currentUser.setEducation( userAccount.getEducation() );
        currentUser.setInterests( userAccount.getInterests() );
        currentUser.setLanguages( userAccount.getLanguages() );

        userService.updateUser( currentUser );
    }

    @RequestMapping(value = "/recipients", produces = { "application/json", "text/*" })
    public void recipientsJson( HttpServletResponse response,
        @RequestParam(value = "filter") String filter ) throws IOException
    {
        User currentUser = currentUserService.getCurrentUser();

        contextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_JSON, CacheStrategy.CACHE_1_HOUR );

        if ( currentUser == null )
        {
            ContextUtils.notFoundResponse( response, "User object is null, user is not authenticated." );
            return;
        }

        Recipients recipients = new Recipients();
        recipients.setOrganisationUnits( new HashSet<OrganisationUnit>( organisationUnitService.getOrganisationUnitsBetweenByName( filter, 0, MAX_OBJECTS ) ) );

        recipients.setUsers( new HashSet<User>( userService.getAllUsersBetweenByName( filter, 0, MAX_OBJECTS ) ) );
        recipients.setUserGroups( new HashSet<UserGroup>( userGroupService.getUserGroupsBetweenByName( filter, 0, MAX_OBJECTS ) ) );

        JacksonUtils.toJson( response.getOutputStream(), recipients );
    }

    @RequestMapping(value = "/assignedOrganisationUnits", produces = { "application/json", "text/*" })
    public void getAssignedOrganisationUnits( HttpServletResponse response ) throws IOException
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            ContextUtils.notFoundResponse( response, "User object is null, user is not authenticated." );
            return;
        }

        JacksonUtils.toJson( response.getOutputStream(), currentUser.getOrganisationUnits() );
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/forms", produces = { "application/json", "text/*" })
    public void getForms( HttpServletResponse response ) throws IOException
    {
        User currentUser = currentUserService.getCurrentUser();

        if ( currentUser == null )
        {
            ContextUtils.notFoundResponse( response, "User object is null, user is not authenticated." );
            return;
        }

        Forms forms = new Forms();

        Set<OrganisationUnit> organisationUnits = new HashSet<OrganisationUnit>();
        Set<DataSet> userDataSets;
        Set<OrganisationUnit> userOrganisationUnits = new HashSet<OrganisationUnit>( currentUser.getOrganisationUnits() );

        if ( currentUser.getUserCredentials().getAllAuthorities().contains( "ALL" ) )
        {
            userDataSets = new HashSet<DataSet>( dataSetService.getAllDataSets() );

            if ( userOrganisationUnits.isEmpty() )
            {
                userOrganisationUnits = new HashSet<OrganisationUnit>( organisationUnitService.getRootOrganisationUnits() );
            }
        }
        else
        {
            userDataSets = currentUser.getUserCredentials().getAllDataSets();
        }

        for ( OrganisationUnit ou : userOrganisationUnits )
        {
            Set<DataSet> dataSets = new HashSet<DataSet>( CollectionUtils.intersection( ou.getDataSets(), userDataSets ) );

            if ( dataSets.size() > 0 )
            {
                organisationUnits.add( ou );
            }

            for ( OrganisationUnit child : ou.getChildren() )
            {
                Set<DataSet> childDataSets = new HashSet<DataSet>( CollectionUtils.intersection( child.getDataSets(), userDataSets ) );

                if ( childDataSets.size() > 0 )
                {
                    organisationUnits.add( child );
                }
            }
        }

        i18nService.internationalise( organisationUnits );

        for ( OrganisationUnit organisationUnit : organisationUnits )
        {
            FormOrganisationUnit ou = new FormOrganisationUnit();
            ou.setId( organisationUnit.getUid() );
            ou.setLabel( organisationUnit.getDisplayName() );

            Set<DataSet> dataSets = new HashSet<DataSet>( CollectionUtils.intersection( organisationUnit.getDataSets(), userDataSets ) );
            i18nService.internationalise( dataSets );

            for ( DataSet dataSet : dataSets )
            {
                i18nService.internationalise( dataSet.getDataElements() );
                i18nService.internationalise( dataSet.getSections() );

                String uid = dataSet.getUid();

                FormDataSet ds = new FormDataSet();
                ds.setId( uid );
                ds.setLabel( dataSet.getDisplayName() );

                forms.getForms().put( uid, FormUtils.fromDataSet( dataSet ) );
                ou.getDataSets().add( ds );
            }

            forms.getOrganisationUnits().put( ou.getId(), ou );
        }

        JacksonUtils.toJson( response.getOutputStream(), forms );
    }
}
