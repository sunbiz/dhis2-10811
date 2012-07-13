var startDate;
var endDate;
var aggregate;
var validationRuleGroupId;
var organisationUnitId;

function organisationUnitSelected( ids )
{
    organisationUnitId = ids[0];
}

function validateRunValidation()
{
	startDate = $( '#startDate' ).val();
	endDate = $( '#endDate' ).val();
	aggregate = $( '#aggregate' ).val();
	validationRuleGroupId = $( '#validationRuleGroupId' ).val();

	$.getJSON( 'validateRunValidation.action',
	{ startDate:startDate, endDate:endDate, aggregate:aggregate }, function( json )
	{
		if ( json.response == 'success' )
	    {
	        setWaitMessage( i18n_analysing_please_wait );

	        $.get( 'runValidationAction.action', 
	        { organisationUnitId:organisationUnitId, startDate:startDate, endDate:endDate, validationRuleGroupId:validationRuleGroupId, aggregate:aggregate }, function( data )
	        {
	            $( 'div#analysisInput' ).hide();
	            $( 'div#analysisResult' ).show();
	            $( 'div#analysisResult' ).html( data );
	            setTableStyles();
	        } );
	    }
	    else if ( json.response == 'input' )
	    {
	        setMessage( json.message );
	    }
	} );

    return false;
}

function drillDownValidation( orgUnitId )
{
    setHeaderWaitMessage( i18n_analysing_please_wait );

    var url = 'runValidationAction.action?organisationUnitId=' + orgUnitId + '&startDate=' + startDate + '&endDate='
            + endDate + '&validationRuleGroupId=' + validationRuleGroupId + '&aggregate=' + aggregate;

    $.get( url, function( data )
    {
        hideHeaderMessage();
        $( "div#analysisResult" ).html( data );
        setTableStyles();
    } );
}

function displayValidationDetailsDialog()
{
	$( '#validationResultDetailsDiv' ).dialog( {
	    modal: true,
	   	title: 'Validation details',
	   	width: 550,
	   	height: 500
	} );
}

function viewValidationResultDetails( validationRuleId, sourceId, periodId )
{
	$( '#validationResultDetailsDiv' ).load( 'viewValidationResultDetails.action', {
		validationRuleId: validationRuleId, sourceId: sourceId, periodId: periodId },
		displayValidationDetailsDialog 
	);
}

function aggregateChanged()
{
    var aggregate = getListValue( 'aggregate' );

    if ( aggregate == 'true' )
    {
        $( 'span#info' ).html( i18n_aggregate_data_info );
    } else
    {
        $( 'span#info' ).html( i18n_captured_data_info );
    }
}

function showAggregateResults()
{
    $( 'div#validationResults' ).hide();
    $( 'div#aggregateResults' ).show();
    var button = document.getElementById( "resultTypeButton" );
    button.onclick = function()
    {
        showValidationResults();
    };
    button.value = "See validation";
}

function showValidationResults()
{
    $( 'div#aggregateResults' ).hide();
    $( 'div#validationResults' ).show();

    var button = document.getElementById( "resultTypeButton" );
    button.onclick = function()
    {
        showAggregateResults();
    };
    button.value = "See statistics";
}

function exportValidationResult( type )
{
    var url = 'exportValidationResult.action?type=' + type + 
    	"&organisationUnitId=" + $( "#organisationUnitId" ).val();
    	
    window.location.href = url;
}
