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
package org.hisp.dhis.api.mobile.model.LWUITmodel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hisp.dhis.api.mobile.model.DataElement;
import org.hisp.dhis.api.mobile.model.Model;

 /**
 * @author Nguyen Kim Lai
 *
 * @version ProgramStage.java 1:04:07 PM Jan 22, 2013 $
 */
public class ProgramStage extends Model
{
    private String clientVersion;
    
    private boolean isRepeatable;
    
    private boolean isCompleted;

    private List<Section> sections;
    
    private List<ProgramStageDataElement> dataElements = new ArrayList<ProgramStageDataElement>();

    public List<Section> getSections()
    {
        return sections;
    }

    public void setSections( List<Section> sections )
    {
        this.sections = sections;
    }

    public List<ProgramStageDataElement> getDataElements()
    {
        return dataElements;
    }

    public void setDataElements( List<ProgramStageDataElement> dataElements )
    {
        this.dataElements = dataElements;
    }

    public String getClientVersion()
    {
        return clientVersion;
    }

    public void setClientVersion( String clientVersion )
    {
        this.clientVersion = clientVersion;
    }

    public boolean isRepeatable()
    {
        return isRepeatable;
    }

    public void setRepeatable( boolean isRepeatable )
    {
        this.isRepeatable = isRepeatable;
    }
    
    public boolean isCompleted()
    {
        return isCompleted;
    }

    public void setCompleted( boolean isCompleted )
    {
        this.isCompleted = isCompleted;
    }

    @Override
    public void serialize( DataOutputStream dout )
        throws IOException
    {
        super.serialize( dout );
        dout.writeBoolean( this.isRepeatable() );
        dout.writeBoolean( this.isCompleted() );
        
        dout.writeInt( this.dataElements.size() );
        for ( int i = 0; i < this.dataElements.size(); i++ )
        {
            this.dataElements.get( i ).serialize( dout );
        }
        
        dout.writeInt( this.sections.size() );
        for ( int i = 0; i < this.sections.size(); i++ )
        {
            this.sections.get( i ).serialize( dout );
        }
    }

    @Override
    public void deSerialize( DataInputStream dint )
        throws IOException
    {
        super.deSerialize( dint );
        this.setRepeatable( dint.readBoolean() );
        this.setCompleted( dint.readBoolean() );
        int dataElementSize = dint.readInt();
        if ( dataElementSize > 0 )
        {
            for ( int i = 0; i < dataElementSize; i++ )
            {
                ProgramStageDataElement de = new ProgramStageDataElement();
                de.deSerialize( dint );
                this.dataElements.add( de );
            }
        }
        else
        {
        }
        
        int sectionSize = dint.readInt();
        if ( sectionSize > 0 )
        {
            for ( int i = 0; i < sectionSize; i++ )
            {
                sections = new ArrayList<Section>();
                Section se = new Section();
                se.deSerialize( dint );
                this.sections.add( se );
            }
        }
        else
        {
        }
    }
}
