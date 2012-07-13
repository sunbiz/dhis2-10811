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

package org.hisp.dhis.patient.hibernate;

import java.util.Collection;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.patient.Patient;
import org.hisp.dhis.patient.PatientIdentifier;
import org.hisp.dhis.patient.PatientIdentifierStore;
import org.hisp.dhis.patient.PatientIdentifierType;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
public class HibernatePatientIdentifierStore
    extends HibernateGenericStore<PatientIdentifier>
    implements PatientIdentifierStore
{
    public PatientIdentifier get( Patient patient )
    {
        return (PatientIdentifier) getCriteria( Restrictions.eq( "patient", patient ),
            Restrictions.eq( "preferred", true ) ).uniqueResult();
    }

    public PatientIdentifier get( String identifier, OrganisationUnit organisationUnit )
    {
        return (PatientIdentifier) getCriteria( Restrictions.eq( "identifier", identifier ),
            Restrictions.eq( "organisationUnit", organisationUnit ) ).uniqueResult();
    }

    public PatientIdentifier get( PatientIdentifierType type, String identifier )
    {
        return (PatientIdentifier) getCriteria( Restrictions.eq( "identifierType", type ),
            Restrictions.eq( "identifier", identifier ) ).uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<PatientIdentifier> getByIdentifier( String identifier )
    {
        return getCriteria( Restrictions.ilike( "identifier", "%" + identifier + "%" ) ).list();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<PatientIdentifier> getByType( PatientIdentifierType identifierType )
    {
        return getCriteria( Restrictions.eq( "identifierType", identifierType ) ).list();
    }

    public PatientIdentifier getPatientIdentifier( String identifier, Patient patient )
    {
        return (PatientIdentifier) getCriteria( Restrictions.eq( "identifier", identifier ),
            Restrictions.eq( "patient", patient ) ).uniqueResult();
    }

    public PatientIdentifier getPatientIdentifier( PatientIdentifierType identifierType, Patient patient )
    {
        return (PatientIdentifier) getCriteria( Restrictions.eq( "identifierType.id", identifierType.getId() ),
            Restrictions.eq( "patient", patient ) ).uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<PatientIdentifier> getPatientIdentifiers( Patient patient )
    {
        return (Collection<PatientIdentifier>) getCriteria( Restrictions.eq( "patient", patient ) ).list();
    }

    public Patient getPatient( PatientIdentifierType idenType, String value )
    {
        return (Patient) getCriteria(
            Restrictions.and( Restrictions.eq( "identifierType", idenType ), Restrictions.eq( "identifier", value ) ) )
            .setProjection( Projections.property( "patient" ) ).uniqueResult();
    }

    @SuppressWarnings( "unchecked" )
    public Collection<Patient> getPatientsByIdentifier( String identifier, int min, int max )
    {
        return getCriteria( Restrictions.ilike( "identifier", "%" + identifier + "%" ) ).setProjection(
            Projections.property( "patient" ) ).setFirstResult( min ).setMaxResults( max ).list();
    }

    public int countGetPatientsByIdentifier( String identifier )
    {
        Number rs = (Number) getCriteria( Restrictions.ilike( "identifier", "%" + identifier + "%" ) ).setProjection(
            Projections.rowCount() ).uniqueResult();
        return rs != null ? rs.intValue() : 0;
    }

    @SuppressWarnings( "unchecked" )
    public Collection<PatientIdentifier> get( Collection<PatientIdentifierType> identifierTypes, Patient patient )
    {
        return getCriteria( Restrictions.in( "identifierType", identifierTypes ), Restrictions.eq( "patient", patient ) )
            .list();
    }

}
