package org.hisp.dhis.patient;

import java.util.Collection;

import org.hisp.dhis.common.GenericNameableObjectStore;

public interface PatientIdentifierTypeStore
    extends GenericNameableObjectStore<PatientIdentifierType>
{
    Collection<PatientIdentifierType> get( boolean mandatory );
    
    Collection<PatientIdentifierType> getByDisplayed( boolean personDisplayName );
}
