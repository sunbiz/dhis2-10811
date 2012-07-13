package org.hisp.dhis.dataelement;

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

import java.util.ArrayList;
import java.util.Collection;

import org.hisp.dhis.common.GenericStore;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
@Transactional
public class DefaultDataElementOperandService
    implements DataElementOperandService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private GenericStore<DataElementOperand> dataElementOperandStore;

    public void setDataElementOperandStore( GenericStore<DataElementOperand> dataElementOperandStore )
    {
        this.dataElementOperandStore = dataElementOperandStore;
    }

    // -------------------------------------------------------------------------
    // Operand
    // -------------------------------------------------------------------------

    public int addDataElementOperand( DataElementOperand dataElementOperand )
    {
        return dataElementOperandStore.save( dataElementOperand );
    }

    public void deleteDataElementOperand( DataElementOperand dataElementOperand )
    {
        dataElementOperandStore.delete( dataElementOperand );
    }

    public DataElementOperand getDataElementOperand( int id )
    {
        return dataElementOperandStore.get( id );
    }

    public DataElementOperand getDataElementOperand( DataElementOperand dataElementOperand )
    {
        for ( DataElementOperand operand : getAllDataElementOperands() )
        {
            if ( operand.getDataElement().equals( dataElementOperand.getDataElement() )
                && operand.getCategoryOptionCombo().equalsOnName( dataElementOperand.getCategoryOptionCombo() ) )
            {
                return operand;
            }
        }

        return null;
    }

    public Collection<DataElementOperand> getAllDataElementOperands()
    {
        return dataElementOperandStore.getAll();
    }

    public Collection<DataElementOperand> getDataElementOperandByDataElements( Collection<DataElement> dataElements )
    {
        Collection<DataElementOperand> operands = new ArrayList<DataElementOperand>();

        for ( DataElementOperand operand : getAllDataElementOperands() )
        {
            if ( dataElements.contains( operand.getDataElement() ) )
            {
                operands.add( operand );
            }
        }

        return operands;
    }

    public Collection<DataElementOperand> getDataElementOperandByOptionCombos(
        Collection<DataElementCategoryOptionCombo> optionCombos )
    {
        Collection<DataElementOperand> operands = new ArrayList<DataElementOperand>();

        for ( DataElementOperand operand : getAllDataElementOperands() )
        {
            if ( optionCombos.contains( operand.getCategoryOptionCombo() ) )
            {
                operands.add( operand );
            }
        }

        return operands;
    }
}
