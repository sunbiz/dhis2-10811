
$( document ).ready( function()
{
	showLoader();
	
	$.getJSON( "getNumberOfOverlaps.action", {}, displayNumberOfOverlaps );
} );

function displayNumberOfOverlaps( json )
{
	hideLoader();
	
    if ( json.number != "0" )
    {
        $( "div#overlapsDiv" ).show();
        $( "span#number" ).html( json.number );
    }
    else
    {
    	$( "div#noOverlapsDiv" ).show();
    }
}

function archive( operation )
{
	var startDate = $( "#startDate" ).val();
	var endDate = $( "#endDate" ).val();
    var strategy = $( "input[name='strategy']:checked" ).val();
    
    var message = operation == "archive" ? i18n_archiving : i18n_unarchiving;
    
    setWaitMessage( message );
    
    $.getJSON( 
        "archiveData.action",
        {
        	"startDate": startDate,
        	"endDate": endDate,
        	"operation": operation,
        	"strategy": strategy
        },
        function( json )
        {
        	setMessage( message + " " + i18n_done_number_of_values + " " + json.number + "." );
        }
    );
}

function patientArchive( operation )
{
	var startDate = $( "#startDate" ).val();
	var endDate = $( "#endDate" ).val();
    var strategy = $( "input[name='strategy']:checked" ).val();
    
    var message = operation == "archive" ? i18n_archiving : i18n_unarchiving;
    
    setWaitMessage( message );
    
    $.getJSON( 
        "archivePatientData.action",
        {
        	"startDate": startDate,
        	"endDate": endDate,
        	"operation": operation,
        	"strategy": strategy
        },
        function( json )
        {
        	setMessage( message + " " + i18n_done_number_of_values + " " + json.number + "." );
        }
    );
}
