package org.hisp.dhis.dataadmin.action.dataarchive;

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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataarchive.DataArchiveOperation;
import org.hisp.dhis.dataarchive.DataArchiveService;
import org.hisp.dhis.dataarchive.DataEliminationStrategy;
import org.hisp.dhis.system.util.DateUtils;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 */
public class ArchivePatientDataAction
    implements Action
{
    private static final Log log = LogFactory.getLog( ArchivePatientDataAction.class );
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataArchiveService dataArchiveService;

    public void setDataArchiveService( DataArchiveService dataArchiveService )
    {
        this.dataArchiveService = dataArchiveService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private String startDate;

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }

    private String endDate;
    
    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }

    private String operation;

    public void setOperation( String operation )
    {
        this.operation = operation;
    }

    private String strategy;

    public void setStrategy( String strategy )
    {
        this.strategy = strategy;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private int number;

    public int getNumber()
    {
        return number;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        Date startDate_ = DateUtils.getMediumDate( startDate );
        
        Date endDate_ = DateUtils.getMediumDate( endDate );
        
        DataArchiveOperation operation_ = DataArchiveOperation.valueOf( operation.toUpperCase() );
        
        DataEliminationStrategy strategy_ = DataEliminationStrategy.valueOf( strategy.toUpperCase() );
        
        log.info( "Archiving: start date: " + startDate + ", end date: " + endDate + ", operation: " + operation + ", strategy: " + strategy );
        
        number = dataArchiveService.archivePatientData( startDate_, endDate_, operation_, strategy_ );
        
        return SUCCESS;
    }
}
