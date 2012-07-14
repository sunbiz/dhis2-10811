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

    $( '#validateButton' ).attr( 'disabled', true )

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

                $( '#validateButton' ).removeAttr( 'disabled' );
	        } );
	    }
	    else if ( json.response == 'input' )
	    {
	        setMessage( json.message );
	    }
	} );

    return false;
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

function exportValidationResult( type )
{
    var url = 'exportValidationResult.action?type=' + type + 
    	"&organisationUnitId=" + $( "#organisationUnitId" ).val();
    	
    window.location.href = url;
}
