/*
 * Copyright (c) 2004-2009, University of Oslo
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

package org.hisp.dhis.program.hibernate;

import java.util.Date;
import java.util.Collection;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramInstance;
import org.hisp.dhis.program.ProgramInstanceStore;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class HibernateProgramInstanceStore
    extends HibernateGenericStore<ProgramInstance>
    implements ProgramInstanceStore
{
    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( boolean completed )
    {
        return getCriteria( Restrictions.eq( "completed", completed ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program )
    {
        return getCriteria( Restrictions.eq( "program", program ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Collection<Program> programs )
    {
        return getCriteria( Restrictions.in( "program", programs ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program, boolean completed )
    {
        return getCriteria( Restrictions.eq( "program", program ), Restrictions.eq( "completed", completed ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Collection<Program> programs, boolean completed )
    {
        return getCriteria( Restrictions.in( "program", programs ), Restrictions.eq( "completed", completed ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Patient patient )
    {
        return getCriteria( Restrictions.eq( "patient", patient ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Patient patient, boolean completed )
    {
        return getCriteria( Restrictions.eq( "patient", patient ), Restrictions.eq( "completed", completed ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Patient patient, Program program )
    {
        return getCriteria( Restrictions.eq( "patient", patient ), Restrictions.eq( "program", program ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Patient patient, Program program, boolean completed )
    {
        return getCriteria( Restrictions.eq( "patient", patient ), Restrictions.eq( "program", program ),
            Restrictions.eq( "completed", completed ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program, OrganisationUnit organisationUnit )
    {
        return getCriteria( Restrictions.eq( "program", program ), Restrictions.isNull( "endDate" ) ).createAlias(
            "patient", "patient" ).add( Restrictions.eq( "patient.organisationUnit", organisationUnit ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program, OrganisationUnit organisationUnit, int min, int max )
    {
        return getCriteria( Restrictions.eq( "program", program ), Restrictions.isNull( "endDate" ) ).add(
            Restrictions.eq( "patient.organisationUnit", organisationUnit ) ).createAlias( "patient", "patient" )
            .addOrder( Order.asc( "patient.id" ) ).setFirstResult( min ).setMaxResults( max ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program, OrganisationUnit organisationUnit, Date startDate,
        Date endDate )
    {
        return getCriteria( Restrictions.eq( "program", program ), Restrictions.isNull( "endDate" ), Restrictions.ge( "enrollmentDate", startDate ),
            Restrictions.le( "enrollmentDate", endDate ) )
            .createAlias( "patient", "patient" )
            .add(Restrictions.eq( "patient.organisationUnit", organisationUnit ) ) 
            .addOrder( Order.asc( "patient.id" ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<ProgramInstance> get( Program program, Collection<Integer> orgunitIds, Date startDate,
        Date endDate, int min, int max )
    {
        return getCriteria( Restrictions.eq( "program", program ), 
            Restrictions.isNull( "endDate" ), 
            Restrictions.ge( "enrollmentDate", startDate ),
            Restrictions.le( "enrollmentDate", endDate ) )
            .createAlias( "patient", "patient" )
            .createAlias( "patient.organisationUnit", "organisationUnit" )
            .add(Restrictions.in( "organisationUnit.id", orgunitIds ) )
            .addOrder( Order.asc( "patient.id" ) ).setFirstResult( min ).setMaxResults( max ).list();
    }

    public int count( Program program, OrganisationUnit organisationUnit )
    {
        Number rs = (Number) getCriteria( Restrictions.eq( "program", program ), 
            Restrictions.isNull( "endDate" ) )
            .createAlias( "patient", "patient" ).add( Restrictions.eq( "patient.organisationUnit", organisationUnit ) )
            .setProjection( Projections.rowCount() ).uniqueResult();
        return rs != null ? rs.intValue() : 0;
    }

    public int count( Program program, Collection<Integer> orgunitIds, Date startDate, Date endDate )
    {
        Number rs = (Number) getCriteria( Restrictions.eq( "program", program ), 
            Restrictions.isNull( "endDate" ),
            Restrictions.ge( "enrollmentDate", startDate ), 
            Restrictions.le( "enrollmentDate", endDate ) )
            .createAlias( "patient", "patient" )
            .createAlias( "patient.organisationUnit", "organisationUnit" )
            .add(Restrictions.in( "organisationUnit.id", orgunitIds ) )
            .setProjection( Projections.rowCount() ).uniqueResult();

        return rs != null ? rs.intValue() : 0;
    }

}
