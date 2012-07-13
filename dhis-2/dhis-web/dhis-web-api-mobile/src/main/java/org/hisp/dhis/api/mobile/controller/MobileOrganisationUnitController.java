package org.hisp.dhis.api.mobile.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import org.hisp.dhis.api.mobile.ActivityReportingService;
import org.hisp.dhis.api.mobile.FacilityReportingService;
import org.hisp.dhis.api.mobile.IProgramService;
import org.hisp.dhis.api.mobile.NotAllowedException;
import org.hisp.dhis.api.mobile.model.ActivityPlan;
import org.hisp.dhis.api.mobile.model.ActivityValue;
import org.hisp.dhis.api.mobile.model.DataSetList;
import org.hisp.dhis.api.mobile.model.DataSetValue;
import org.hisp.dhis.api.mobile.model.DataStreamSerializable;
import org.hisp.dhis.api.mobile.model.MobileModel;
import org.hisp.dhis.api.mobile.model.ModelList;
import org.hisp.dhis.i18n.I18nService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( value = "/mobile/orgUnits" )
public class MobileOrganisationUnitController
    extends AbstractMobileController
{
    private static final String ACTIVITY_REPORT_UPLOADED = "activity_report_uploaded";

    private static final String DATASET_REPORT_UPLOADED = "dataset_report_uploaded";

    @Autowired
    private ActivityReportingService activityReportingService;

    @Autowired
    private IProgramService programService;

    @Autowired
    private FacilityReportingService facilityReportingService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private I18nService i18nService;

    @RequestMapping( method = RequestMethod.GET, value = "{id}/all" )
    @ResponseBody
    public MobileModel getAllDataForOrgUnit( @PathVariable int id, @RequestHeader( "accept-language" ) String locale )
    {
        MobileModel mobileModel = new MobileModel();
        mobileModel.setClientVersion( DataStreamSerializable.TWO_POINT_EIGHT );
        OrganisationUnit unit = getUnit( id );
        mobileModel.setActivityPlan( activityReportingService.getCurrentActivityPlan( unit, locale ) );
        mobileModel.setPrograms( programService.getPrograms( unit, locale ) );
        mobileModel.setDatasets( facilityReportingService.getMobileDataSetsForUnit( unit, locale ) );
        mobileModel.setServerCurrentDate( new Date() );
        mobileModel.setLocales( getLocalStrings( i18nService.getAvailableLocales() ) );
        return mobileModel;
    }
    
    @RequestMapping( method = RequestMethod.GET, value = "{id}/all/2.9" )
    @ResponseBody
    public MobileModel getAllDataForOrgUnit2_9( @PathVariable int id, @RequestHeader( "accept-language" ) String locale )
    {
        MobileModel mobileModel = new MobileModel();
        mobileModel.setClientVersion( DataStreamSerializable.TWO_POINT_NINE );
        OrganisationUnit unit = getUnit( id );
        mobileModel.setActivityPlan( activityReportingService.getCurrentActivityPlan( unit, locale ) );
        mobileModel.setPrograms( programService.getPrograms( unit, locale ) );
        mobileModel.setDatasets( facilityReportingService.getMobileDataSetsForUnit( unit, locale ) );
        mobileModel.setServerCurrentDate( new Date() );
        mobileModel.setLocales( getLocalStrings( i18nService.getAvailableLocales() ) );

        return mobileModel;
    }

    @RequestMapping( method = RequestMethod.POST, value = "{id}/updateDataSets" )
    @ResponseBody
    public DataSetList checkUpdatedDataSet( @PathVariable int id, @RequestBody DataSetList dataSetList,
        @RequestHeader( "accept-language" ) String locale )
    {
        return facilityReportingService.getUpdatedDataSet( dataSetList, getUnit( id ), locale );
    }

    @RequestMapping( method = RequestMethod.GET, value = "{id}/changeLanguageDataSet" )
    @ResponseBody
    public DataSetList changeLanguageDataSet( @PathVariable int id, @RequestHeader( "accept-language" ) String locale )
    {
        return facilityReportingService.getDataSetsForLocale( getUnit( id ), locale );
    }

    /**
     * Save a facility report for unit
     * 
     * @param dataSetValue - the report to save
     * @throws NotAllowedException if the {@link DataSetValue} is invalid
     */
    @RequestMapping( method = RequestMethod.POST, value = "     {id}/dataSets" )
    @ResponseBody
    public String saveDataSetValues( @PathVariable int id, @RequestBody DataSetValue dataSetValue )
        throws NotAllowedException
    {
        facilityReportingService.saveDataSetValues( getUnit( id ), dataSetValue );
        return DATASET_REPORT_UPLOADED;
    }

    /**
     * Save activity report for unit
     * 
     * @param activityValue - the report to save
     * @throws NotAllowedException if the {@link ActivityValue activity value}
     *         is invalid
     */
    @RequestMapping( method = RequestMethod.POST, value = "{id}/activities" )
    @ResponseBody
    public String saveActivityReport( @PathVariable int id, @RequestBody ActivityValue activityValue )
        throws NotAllowedException
    {
        activityReportingService.saveActivityReport( getUnit( id ), activityValue );
        return ACTIVITY_REPORT_UPLOADED;
    }

    @RequestMapping( method = RequestMethod.POST, value = "{id}/activitiyplan" )
    @ResponseBody
    public MobileModel updatePrograms( @PathVariable int id, @RequestHeader( "accept-language" ) String locale,
        @RequestBody ModelList programsFromClient )
    {
        MobileModel model = new MobileModel();
        model.setPrograms( programService.updateProgram( programsFromClient, locale, getUnit( id ) ) );
        model.setActivityPlan( activityReportingService.getCurrentActivityPlan( getUnit( id ), locale ) );
        model.setServerCurrentDate( new Date() );
        return model;
    }

    @RequestMapping( method = RequestMethod.GET, value = "{id}/search" )
    @ResponseBody
    public ActivityPlan search( @PathVariable int id, @RequestHeader( "identifier" ) String identifier )
        throws NotAllowedException
    {
        return activityReportingService.getActivitiesByIdentifier( identifier );
    }

    private Collection<String> getLocalStrings( Collection<Locale> locales )
    {
        if ( locales == null || locales.isEmpty() )
        {
            return null;
        }
        Collection<String> localeStrings = new ArrayList<String>();

        for ( Locale locale : locales )
        {
            localeStrings.add( locale.getLanguage() + "-" + locale.getCountry() );
        }
        return localeStrings;
    }

    private OrganisationUnit getUnit( int id )
    {
        return organisationUnitService.getOrganisationUnit( id );
    }

}
