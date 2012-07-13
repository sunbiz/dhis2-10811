package org.hisp.dhis.option;

/*
 * Copyright (c) 2004-2011, University of Oslo
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

import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lars Helge Overland
 */
@Transactional
public class DefaultOptionService
    implements OptionService
{
    private OptionStore optionStore;

    public void setOptionStore( OptionStore optionStore )
    {
        this.optionStore = optionStore;
    }

    public int saveOptionSet( OptionSet optionSet )
    {
        return optionStore.save( optionSet );
    }

    public void updateOptionSet( OptionSet optionSet )
    {
        optionStore.update( optionSet );
    }
    
    public OptionSet getOptionSet( int id )
    {
        return optionStore.get( id );
    }

    public OptionSet getOptionSet( String uid )
    {
        return optionStore.getByUid( uid );
    }
    
    public OptionSet getOptionSetByName( String name )
    {
        return optionStore.getByName( name );
    }

    public void deleteOptionSet( OptionSet optionSet )
    {
        optionStore.delete( optionSet );
    }

    public Collection<OptionSet> getAllOptionSets()
    {
        return optionStore.getAll();
    }
    
    public List<String> getOptions( OptionSet optionSet, String key, Integer max  )
    {
        return optionStore.getOptions( optionSet, key, max );
    }
}
