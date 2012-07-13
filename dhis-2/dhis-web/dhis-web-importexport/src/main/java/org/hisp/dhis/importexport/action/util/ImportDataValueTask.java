package org.hisp.dhis.importexport.action.util;

/*
 * Copyright (c) 2011, University of Oslo
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

import java.io.InputStream;
import java.io.Reader;

import org.hisp.dhis.dxf2.datavalueset.DataValueSetService;
import org.hisp.dhis.dxf2.metadata.ImportOptions;
import org.hisp.dhis.scheduling.TaskId;

/**
 * @author Lars Helge Overland
 */
public class ImportDataValueTask
    implements Runnable
{
    public static final String FORMAT_CSV = "csv";
    
    private DataValueSetService dataValueSetService;
    private InputStream in;
    private Reader reader;
    private ImportOptions options;
    private TaskId taskId;
    private String format;
    
    public ImportDataValueTask( DataValueSetService dataValueSetService, InputStream in, Reader reader, ImportOptions options, TaskId taskId, String format )
    {
        this.dataValueSetService = dataValueSetService;
        this.in = in;
        this.reader = reader;
        this.options = options;
        this.taskId = taskId;
        this.format = format;
    }
    
    @Override
    public void run()
    {
        if ( FORMAT_CSV.equals( format ) )
        {
            dataValueSetService.saveDataValueSetCsv( reader, options, taskId );
        }
        else
        {
            dataValueSetService.saveDataValueSet( in, options, taskId );
        }
    }
}
