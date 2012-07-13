package org.hisp.dhis.light.dataentry.action;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.light.utils.FormUtils;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserCredentials;
import org.junit.Before;
import org.junit.Test;

public class GetDataSetsActionTest
{
    private GetDataSetsAction getDataSetsAction;

    private DataSet ds2;

    private DataSet ds3;

    @Before
    public void setup()
    {
        getDataSetsAction = new GetDataSetsAction();

        // Shared dataSets
        ds2 = new DataSet( "2" );
        ds3 = new DataSet( "3" );

        Set<DataSet> unitDataSets = createUnitDataSets();
        Set<DataSet> userDataSets = createUserDataSets();

        OrganisationUnitService organisationUnitService = mock( OrganisationUnitService.class );
        CurrentUserService currentUserService = mock( CurrentUserService.class );

        FormUtils formUtils = new FormUtils();
        formUtils.setCurrentUserService( currentUserService );
        formUtils.setOrganisationUnitService( organisationUnitService );

        // Initializing action
        getDataSetsAction.setOrganisationUnitId( 1 );
        getDataSetsAction.setOrganisationUnitService( organisationUnitService );
        getDataSetsAction.setFormUtils( formUtils );

        // Populating mocks
        OrganisationUnit unit = new OrganisationUnit();
        unit.setDataSets( unitDataSets );

        when( organisationUnitService.getOrganisationUnit( anyInt() ) ).thenReturn( unit );

        UserCredentials credentials = mock( UserCredentials.class );
        when( credentials.isSuper() ).thenReturn( false );
        when( credentials.getAllDataSets() ).thenReturn( userDataSets );

        User user = new User();
        user.setUserCredentials( credentials );

        when( currentUserService.getCurrentUser() ).thenReturn( user );
    }

    private Set<DataSet> createUserDataSets()
    {
        Set<DataSet> userDataSets;
        userDataSets = new HashSet<DataSet>();
        userDataSets.add( ds2 );
        userDataSets.add( ds3 );
        userDataSets.add( new DataSet( "5" ) );
        return userDataSets;
    }

    private Set<DataSet> createUnitDataSets()
    {
        Set<DataSet> unitDataSets;
        unitDataSets = new HashSet<DataSet>();
        unitDataSets.add( new DataSet( "1" ) );
        unitDataSets.add( ds2 );
        unitDataSets.add( ds3 );
        unitDataSets.add( new DataSet( "4" ) );
        return unitDataSets;
    }

    @Test
    public void testUserDataSetsFiltering()
    {

        getDataSetsAction.execute();

        List<DataSet> dataSets = getDataSetsAction.getDataSets();
        assertEquals( 2, dataSets.size() );

        DataSet dataSet1 = dataSets.get( 0 );
        DataSet dataSet2 = dataSets.get( 1 );

        assertTrue( (dataSet1.equals( ds2 ) && dataSet2.equals( ds3 ))
            || (dataSet2.equals( ds2 ) && dataSet1.equals( ds3 )) );
    }

}
