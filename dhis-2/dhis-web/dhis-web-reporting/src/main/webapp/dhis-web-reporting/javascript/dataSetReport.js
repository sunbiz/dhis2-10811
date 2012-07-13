
function getPeriods( periodTypeList, availableList, selectedList, timespan )
{
    $( "#periodId" ).removeAttr( "disabled" );

    getAvailablePeriods( periodTypeList, availableList, selectedList, timespan );
}

function validateDataSetReport()
{
    if ( !$( "#dataSetId" ).val() )
    {
        setHeaderMessage( i18n_select_data_set );
        return false;
    }
    if ( !$( "#periodId" ).val() )
    {
        setHeaderMessage( i18n_select_period );
        return false;
    }
    if ( !selectionTreeSelection.isSelected() )
    {
        setHeaderMessage( i18n_select_organisation_unit );
        return false;
    }
    
    hideHeaderMessage();
    hideCriteria();
	hideContent();
	showLoader();
	
    var dataSetId = $( "#dataSetId" ).val();
    var periodId = $( "#periodId" ).val();
    var selectedUnitOnly = $( "#selectedUnitOnly" ).is( ":checked" );
    var orgUnitId = selectionTreeSelection.getSelected()[0]; 
    
    var currentParams = { dataSetId: dataSetId, periodId: periodId, selectedUnitOnly: selectedUnitOnly, orgUnitId: orgUnitId };
    
    $( '#content' ).load( 'generateDataSetReport.action', currentParams, function() {
    	hideLoader();
    	showContent();
    	setTableStyles();
    } );
}

function exportDataSetReport( type )
{
	var url = "generateDataSetReport.action?useLast=true" + 
		"&dataSetId=" + $( "#currentDataSetId" ).val() +
	    "&periodId=" + $( "#currentPeriodId" ).val() +
	    "&selectedUnitOnly=" + $( "#currentSelectedUnitOnly" ).val() +
	    "&orgUnitId=" + selectionTreeSelection.getSelected() +
	    "&type=" + type;
	    
	window.location.href = url;
}

function setUserInfo( username )
{
	$( "#userInfo" ).load( "../dhis-web-commons-ajax-html/getUser.action?username=" + username, function() {
		$( "#userInfo" ).dialog( {
	        modal : true,
	        width : 350,
	        height : 350,
	        title : "User"
	    } );
	} );	
}

function showCriteria()
{
	$( "#criteria" ).show( "fast" );
}

function hideCriteria()
{
	$( "#criteria" ).hide( "fast" );
}

function showContent()
{
	$( "#content" ).show( "fast" );
}

function hideContent()
{
	$( "#content" ).hide( "fast" );
}
