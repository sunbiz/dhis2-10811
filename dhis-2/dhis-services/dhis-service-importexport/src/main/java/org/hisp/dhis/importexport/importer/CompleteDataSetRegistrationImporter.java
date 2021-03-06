package org.hisp.dhis.importexport.importer;

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

import org.amplecode.quick.BatchHandler;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.Importer;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class CompleteDataSetRegistrationImporter
    extends AbstractImporter<CompleteDataSetRegistration> implements Importer<CompleteDataSetRegistration>
{
    protected ImportParams params;

    public CompleteDataSetRegistrationImporter()
    {
    }

    public CompleteDataSetRegistrationImporter( BatchHandler<CompleteDataSetRegistration> batchHandler, ImportParams params )
    {
        this.batchHandler = batchHandler;
        this.params = params;
    }
    
    @Override
    public void importObject( CompleteDataSetRegistration object, ImportParams params )
    {
        read( object, GroupMemberType.NONE, params );        
    }

    @Override
    protected void importUnique( CompleteDataSetRegistration object )
    {
        batchHandler.addObject( object );    
    }

    @Override
    protected void importMatching( CompleteDataSetRegistration object, CompleteDataSetRegistration match )
    {
        match.setDate( object.getDate() );
        
        batchHandler.updateObject( match );
    }

    @Override
    protected CompleteDataSetRegistration getMatching( CompleteDataSetRegistration object )
    {
        // ---------------------------------------------------------------------
        // CompleteDataSetRegistration cannot be compared against existing 
        // registrations during preview since the elements in its composite id 
        // have not been mapped
        // ---------------------------------------------------------------------

        if ( params.isPreview() )
        {
            return null;
        }
        
        return batchHandler.objectExists( object ) ? object : null;
    }

    @Override
    protected boolean isIdentical( CompleteDataSetRegistration object, CompleteDataSetRegistration existing )
    {
        // ---------------------------------------------------------------------
        // Matching registrations will not be overwritten
        // ---------------------------------------------------------------------

        return true;
    }
}
