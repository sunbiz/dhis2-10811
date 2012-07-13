
var pingTimeout = null;

function importDataValue()
{
	if ( !$( "#upload" ).val() )
	{
		setHeaderDelayMessage( "Please select a file to upload" );
		return false;
	}
	
	$( "#importForm" ).submit();
}

function toggleOptions()
{
	$( ".moreOptions" ).toggle();
	$.toggleCss( "inputCriteria", "height", "144px", "230px" );
}

function pingNotificationsTimeout()
{
	pingNotifications( 'DATAVALUE_IMPORT', 'notificationTable', displaySummaryLink );	
	pingTimeout = setTimeout( "pingNotificationsTimeout()", 1500 );
}

function displaySummaryLink()
{
	window.clearTimeout( pingTimeout );
	var html = '<tr><td></td><td><a href="javascript:displaySummary()">Display import summary</a></td></tr>';
	$( '#notificationTable' ).prepend( html );
}

function displaySummary()
{	
	$( '#notificationDiv' ).hide();
	$( '#importSummaryDiv' ).show( 'fast' ).load( 'getDataValueImportSummary.action' );
}