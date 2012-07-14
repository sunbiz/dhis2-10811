package org.hisp.dhis.dataanalysis;

import java.util.Collection;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.organisationunit.OrganisationUnit;

public interface MinMaxDataAnalysisService
    extends DataAnalysisService
{    
    void generateMinMaxValues( Collection<OrganisationUnit> organisationUnits,
        Collection<DataElement> dataElements, Double stdDevFactor );
}
