package org.hisp.dhis.patient;

import java.util.Collection;

import org.hisp.dhis.common.GenericNameableObjectStore;
import org.hisp.dhis.program.Program;

public interface PatientIdentifierTypeStore
    extends GenericNameableObjectStore<PatientIdentifierType>
{
    Collection<PatientIdentifierType> get( boolean mandatory );

    Collection<PatientIdentifierType> get( Program program );

    Collection<PatientIdentifierType> getWithoutProgram();
}
