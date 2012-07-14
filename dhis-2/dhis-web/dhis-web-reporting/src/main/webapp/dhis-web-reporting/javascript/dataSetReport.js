
var currentPeriodOffset = 0;
var periodTypeFactory = new PeriodType();

//------------------------------------------------------------------------------
// Get and set methods
//------------------------------------------------------------------------------

function getDataSetReport()
{
    var dataSetReport = {
        dataSet: $( "#dataSetId" ).val(),
        periodType: $( "#periodType" ).val(),
        period: $( "#periodId" ).val(),
        orgUnit: selectionTreeSelection.getSelectedUid()[0],
        selectedUnitOnly: $( "#selectedUnitOnly" ).is( ":checked" ),
        offset: currentPeriodOffset
    };
    
    return dataSetReport;
}

function setDataSetReport( dataSetReport )
{
	$( "#dataSetId" ).val( dataSetReport.dataSet );
	$( "#periodType" ).val( dataSetReport.periodType );
	
	currentPeriodOffset = dataSetReport.offset;
	
	displayPeriods();
	$( "#periodId" ).val( dataSetReport.period );
	
	selectionTreeSelection.setMultipleSelectionAllowed( false );
	selectionTree.buildSelectionTree();
		
	$( "body" ).on( "oust.selected", function() {
		$( "body" ).off( "oust.selected" );
		validateDataSetReport();
	} );
}

//------------------------------------------------------------------------------
// Period
//------------------------------------------------------------------------------

function displayPeriods()
{
    var periodType = $( "#periodType" ).val();
    var periods = periodTypeFactory.get( periodType ).generatePeriods( currentPeriodOffset );
    periods = periodTypeFactory.reverse( periods );
    periods = periodTypeFactory.filterFuturePeriodsExceptCurrent( periods );

    $( "#periodId" ).removeAttr( "disabled" );
    clearListById( "periodId" );

    for ( i in periods )
    {
        addOptionById( "periodId", periods[i].iso, periods[i].name );
    }
}

function displayNextPeriods()
{
    if ( currentPeriodOffset < 0 ) // Cannot display future periods
    {
        currentPeriodOffset++;
        displayPeriods();
    }
}

function displayPreviousPeriods()
{
    currentPeriodOffset--;
    displayPeriods();
}

//------------------------------------------------------------------------------
// Run report
//------------------------------------------------------------------------------

function validateDataSetReport()
{
	var dataSetReport = getDataSetReport();
	
    if ( !dataSetReport.dataSet )
    {
        setHeaderMessage( i18n_select_data_set );
        return false;
    }
    if ( !dataSetReport.period )
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
	
    var currentParams = { ds: dataSetReport.dataSet, pe: dataSetReport.period, selectedUnitOnly: dataSetReport.selectedUnitOnly, ou: dataSetReport.orgUnit };
    
    $.get( 'generateDataSetReport.action', currentParams, function( data ) {
    	$( '#content' ).html( data );
    	hideLoader();
    	showContent();
    	setTableStyles();
    } );
}

function exportDataSetReport( type )
{
	var dataSetReport = getDataSetReport();
	
	var url = "generateDataSetReport.action" + 
		"?ds=" + dataSetReport.dataSet +
	    "&pe=" + dataSetReport.period +
	    "&selectedUnitOnly=" + dataSetReport.selectedUnitOnly +
	    "&ou=" + dataSetReport.orgUnit +
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
	$( ".downloadButton" ).show();
	$( "#interpretationArea" ).autogrow();
}

function hideContent()
{
	$( "#content" ).hide( "fast" );
	$( ".downloadButton" ).hide();
}

//------------------------------------------------------------------------------
// Share
//------------------------------------------------------------------------------

function viewShareForm() // Not in use
{
	$( "#shareForm" ).dialog( {
		modal : true,
		width : 550,
		resizable: false,
		title : i18n_share_your_interpretation
	} );
}

function shareInterpretation()
{
	var dataSetReport = getDataSetReport();
    var text = $( "#interpretationArea" ).val();
    
    if ( text.length && $.trim( text ).length )
    {
    	text = $.trim( text );
    	
	    var url = "../api/interpretations/dataSetReport/" + $( "#currentDataSetId" ).val() +
	    	"?pe=" + dataSetReport.period +
	    	"&ou=" + dataSetReport.orgUnit;
	    	    
	    $.ajax( url, {
	    	type: "POST",
	    	contentType: "text/html",
	    	data: text,
	    	success: function() {	    		
	    		$( "#interpretationArea" ).val( "" );
	    		setHeaderDelayMessage( i18n_interpretation_was_shared );
	    	}    	
	    } );
    }
}
