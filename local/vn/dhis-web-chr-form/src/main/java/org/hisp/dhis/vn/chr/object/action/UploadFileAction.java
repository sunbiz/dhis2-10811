package org.hisp.dhis.vn.chr.object.action;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.options.SystemSettingManager;

import com.opensymphony.xwork2.Action;

/**
 * @author Chau Thu Tran
 * @version $Id$
 */

public class UploadFileAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SystemSettingManager systemSettingManager;

    public void setSystemSettingManager( SystemSettingManager systemSettingManager )
    {
        this.systemSettingManager = systemSettingManager;
    }

    // -----------------------------------------------------------------------------------------------
    // Input && Output
    // -----------------------------------------------------------------------------------------------

    private List<File> files;

    public List<File> getFiles()
    {
        return files;
    }

    private File uploadFile;

    public void setUploadFile( File uploadFile )
    {
        this.uploadFile = uploadFile;
    }

    private String uploadFilename;

    public void setUploadFilename( String uploadFilename )
    {
        this.uploadFilename = uploadFilename;
    }

    // -----------------------------------------------------------------------------------------------
    // Implements
    // -----------------------------------------------------------------------------------------------

    public String execute()
    {
        files = new ArrayList<File>();

        FileInputStream fin = null;
        FileOutputStream fout = null;

        String imageDirectoryOnServer = (String) systemSettingManager
            .getSystemSetting( SystemSettingManager.KEY_CHR_IMAGE_DIRECTORY );

        try
        {

            if ( uploadFile != null )
            {
                fin = new FileInputStream( uploadFile );// (doc.getPath());
                byte[] data = new byte[8192];
                int byteReads = fin.read( data );

                fout = new FileOutputStream( imageDirectoryOnServer + "/" + uploadFilename );

                while ( byteReads != -1 )
                {
                    fout.write( data, 0, byteReads );
                    fout.flush();
                    byteReads = fin.read( data );
                }
                fin.close();
                fout.close();
            }

            // Load files in the directory on server
            File myDir = new File( imageDirectoryOnServer );
            if ( myDir.exists() && myDir.isDirectory() )
            {
                File[] listfiles = myDir.listFiles();
                for ( int i = 0; i < listfiles.length; i++ )
                {
                    if ( !listfiles[i].isDirectory() && !listfiles[i].isHidden() )
                        files.add( listfiles[i] );
                }
            }

            return SUCCESS;

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            return ERROR;
        }

    }

}