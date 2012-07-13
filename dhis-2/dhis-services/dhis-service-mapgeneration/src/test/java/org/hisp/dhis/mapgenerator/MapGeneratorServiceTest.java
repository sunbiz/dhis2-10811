package org.hisp.dhis.mapgenerator;

import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.junit.Test;

/**
 * @author Kenneth Solbø Andersen <kennetsa@ifi.uio.no>
 */
public class MapGeneratorServiceTest
    extends DhisSpringTest
{
    private OrganisationUnit organisationUnit;

    private OrganisationUnitLevel organisationUnitLevel;

    private IndicatorGroup indicatorGroup;

    private IndicatorType indicatorType;

    private Indicator indicator;

    private DataElement dataElement;

    private DataElementGroup dataElementGroup;

    private PeriodType periodType;

    private Period period;

    @Override
    public void setUpTest()
    {
        organisationUnitService = (OrganisationUnitService) getBean( OrganisationUnitService.ID );

        indicatorService = (IndicatorService) getBean( IndicatorService.ID );

        dataElementService = (DataElementService) getBean( DataElementService.ID );

        periodService = (PeriodService) getBean( PeriodService.ID );

        organisationUnit = createOrganisationUnit( 'A' );
        organisationUnitLevel = new OrganisationUnitLevel( 1, "Level" );

        organisationUnitService.addOrganisationUnit( organisationUnit );
        organisationUnitService.addOrganisationUnitLevel( organisationUnitLevel );

        indicatorGroup = createIndicatorGroup( 'A' );
        indicatorService.addIndicatorGroup( indicatorGroup );

        indicatorType = createIndicatorType( 'A' );
        indicatorService.addIndicatorType( indicatorType );

        indicator = createIndicator( 'A', indicatorType );
        indicatorService.addIndicator( indicator );

        dataElement = createDataElement( 'A' );
        dataElementService.addDataElement( dataElement );

        dataElementGroup = createDataElementGroup( 'A' );
        dataElementGroup.getMembers().add( dataElement );
        dataElementService.addDataElementGroup( dataElementGroup );

        periodType = periodService.getPeriodTypeByName( MonthlyPeriodType.NAME );
        period = createPeriod( periodType, getDate( 2000, 1, 1 ), getDate( 2000, 2, 1 ) );
        periodService.addPeriod( period );
    }

    @Test
    public void testGenerateMapImage()
    {
        // TODO
    }
}
