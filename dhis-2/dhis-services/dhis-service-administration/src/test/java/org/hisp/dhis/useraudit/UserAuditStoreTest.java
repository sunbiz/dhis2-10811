package org.hisp.dhis.useraudit;

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

import org.hisp.dhis.DhisSpringTest;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * @author Lars Helge Overland
 */
public class UserAuditStoreTest
    extends DhisSpringTest
{
    private UserAuditStore userAuditStore;
    
    @Override
    public void setUpTest()
    {
        userAuditStore = (UserAuditStore) getBean( UserAuditStore.ID );
    }
    
    @Test
    public void save()
    {
        userAuditStore.saveLoginFailure( new LoginFailure( "userA", getDate( 2000, 1, 3 ) ) );
        userAuditStore.saveLoginFailure( new LoginFailure( "userA", getDate( 2000, 1, 4 ) ) );
        userAuditStore.saveLoginFailure( new LoginFailure( "userB", getDate( 2000, 1, 5 ) ) );
        
        assertNotNull( userAuditStore.getAllLoginFailures() );
        assertEquals( 3, userAuditStore.getAllLoginFailures().size() );
    }
    
    @Test
    public void get()
    {
        userAuditStore.saveLoginFailure( new LoginFailure( "userA", getDate( 2000, 1, 3 ) ) );
        userAuditStore.saveLoginFailure( new LoginFailure( "userA", getDate( 2000, 1, 4 ) ) );
        userAuditStore.saveLoginFailure( new LoginFailure( "userA", getDate( 2000, 1, 5 ) ) );
        userAuditStore.saveLoginFailure( new LoginFailure( "userA", getDate( 2000, 1, 6 ) ) );
        userAuditStore.saveLoginFailure( new LoginFailure( "userB", getDate( 2000, 1, 7 ) ) );
        
        assertEquals( 2, userAuditStore.getLoginFailures( "userA", getDate( 2000, 1, 4 ) ) );
    }
}
