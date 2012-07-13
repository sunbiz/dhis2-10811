package org.hisp.dhis.api.mobile.model;

/*
 * Copyright (c) 2010, University of Oslo
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

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "orgUnit" )
public class MobileOrgUnitLinks
    implements DataStreamSerializable
{
    private String clientVersion;

    private int id;

    private String name;

    private String downloadAllUrl;

    private String updateActivityPlanUrl;

    private String uploadFacilityReportUrl;

    private String uploadActivityReportUrl;

    private String updateDataSetUrl;

    private String changeUpdateDataSetLangUrl;

    private String searchUrl;

    public static double currentVersion = 2.9;

    private String updateNewVersionUrl;

    @XmlAttribute
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @XmlAttribute
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDownloadAllUrl()
    {
        return downloadAllUrl;
    }

    public void setDownloadAllUrl( String downloadAllUrl )
    {
        this.downloadAllUrl = downloadAllUrl;
    }

    public String getUploadFacilityReportUrl()
    {
        return uploadFacilityReportUrl;
    }

    public void setUploadFacilityReportUrl( String uploadFacilityReportUrl )
    {
        this.uploadFacilityReportUrl = uploadFacilityReportUrl;
    }

    public String getUploadActivityReportUrl()
    {
        return uploadActivityReportUrl;
    }

    public void setUploadActivityReportUrl( String uploadActivityReportUrl )
    {
        this.uploadActivityReportUrl = uploadActivityReportUrl;
    }

    public String getUpdateDataSetUrl()
    {
        return updateDataSetUrl;
    }

    public void setUpdateDataSetUrl( String updateDataSetUrl )
    {
        this.updateDataSetUrl = updateDataSetUrl;
    }

    public String getChangeUpdateDataSetLangUrl()
    {
        return changeUpdateDataSetLangUrl;
    }

    public void setChangeUpdateDataSetLangUrl( String changeUpdateDataSetLangUrl )
    {
        this.changeUpdateDataSetLangUrl = changeUpdateDataSetLangUrl;
    }

    public String getSearchUrl()
    {
        return searchUrl;
    }

    public void setSearchUrl( String searchUrl )
    {
        this.searchUrl = searchUrl;
    }

    public String getClientVersion()
    {
        return clientVersion;
    }

    public void setClientVersion( String clientVersion )
    {
        this.clientVersion = clientVersion;
    }

    public String getUpdateActivityPlanUrl()
    {
        return updateActivityPlanUrl;
    }

    public void setUpdateActivityPlanUrl( String updateActivityPlanUrl )
    {
        this.updateActivityPlanUrl = updateActivityPlanUrl;
    }

    public String getUpdateNewVersionUrl()
    {
        return updateNewVersionUrl;
    }

    public void setUpdateNewVersionUrl( String updateNewVersionUrl )
    {
        this.updateNewVersionUrl = updateNewVersionUrl;
    }

    public void serialize( DataOutputStream dataOutputStream )

        throws IOException
    {
        dataOutputStream.writeInt( this.id );
        dataOutputStream.writeUTF( this.name );
        dataOutputStream.writeUTF( this.downloadAllUrl );
        dataOutputStream.writeUTF( this.updateActivityPlanUrl );
        dataOutputStream.writeUTF( this.uploadFacilityReportUrl );
        dataOutputStream.writeUTF( this.uploadActivityReportUrl );
        dataOutputStream.writeUTF( this.updateDataSetUrl );
        dataOutputStream.writeUTF( this.changeUpdateDataSetLangUrl );
        dataOutputStream.writeUTF( this.searchUrl );
        dataOutputStream.writeUTF( this.updateNewVersionUrl );

    }

    public void deSerialize( DataInputStream dataInputStream )
        throws IOException
    {
        this.id = dataInputStream.readInt();
        this.name = dataInputStream.readUTF();
        this.downloadAllUrl = dataInputStream.readUTF();
        this.updateActivityPlanUrl = dataInputStream.readUTF();
        this.uploadFacilityReportUrl = dataInputStream.readUTF();
        this.uploadActivityReportUrl = dataInputStream.readUTF();
        this.updateDataSetUrl = dataInputStream.readUTF();
        this.changeUpdateDataSetLangUrl = dataInputStream.readUTF();
        this.searchUrl = dataInputStream.readUTF();
        this.updateNewVersionUrl = dataInputStream.readUTF();
    }

    @Override
    public void serializeVerssion2_8( DataOutputStream dataOutputStream )
        throws IOException
    {
        dataOutputStream.writeInt( this.id );
        dataOutputStream.writeUTF( this.name );
        dataOutputStream.writeUTF( this.downloadAllUrl );
        dataOutputStream.writeUTF( this.updateActivityPlanUrl );
        dataOutputStream.writeUTF( this.uploadFacilityReportUrl );
        dataOutputStream.writeUTF( this.uploadActivityReportUrl );
        dataOutputStream.writeUTF( this.updateDataSetUrl );
        dataOutputStream.writeUTF( this.changeUpdateDataSetLangUrl );
        dataOutputStream.writeUTF( this.searchUrl );
    }

    @Override
    public void serializeVerssion2_9( DataOutputStream dataOutputStream )
        throws IOException
    {
        dataOutputStream.writeInt( this.id );
        dataOutputStream.writeUTF( this.name );
        dataOutputStream.writeUTF( this.downloadAllUrl );
        dataOutputStream.writeUTF( this.updateActivityPlanUrl );
        dataOutputStream.writeUTF( this.uploadFacilityReportUrl );
        dataOutputStream.writeUTF( this.uploadActivityReportUrl );
        dataOutputStream.writeUTF( this.updateDataSetUrl );
        dataOutputStream.writeUTF( this.changeUpdateDataSetLangUrl );
        dataOutputStream.writeUTF( this.searchUrl );
    }

}
