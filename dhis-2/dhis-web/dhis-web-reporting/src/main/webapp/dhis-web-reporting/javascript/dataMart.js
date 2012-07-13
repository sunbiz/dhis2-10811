
$( document ).ready( function() {
	datePickerInRange( 'startDate' , 'endDate' );
	pingNotificationsTimeout();
} );

function startExport()
{
	$( '#notificationTable' ).show().prepend( '<tr><td>' + _loading_bar_html + '</td></tr>' );
	
	var startDate = $( '#startDate' ).val();
	var endDate = $( '#endDate' ).val();
	
	var url = 'startExport.action?startDate=' + startDate + '&endDate=' + endDate;
	
	$( 'input[name="periodType"]').each( function() 
	{
		if ( $( this ).is( ':checked' ) )
		{
			url += "&periodType=" + $( this ).val();
		}
	} );
	
	$.get( url, pingNotificationsTimeout );
}

function pingNotificationsTimeout()
{
	pingNotifications( 'DATAMART', 'notificationTable' );
	setTimeout( "pingNotificationsTimeout()", 2500 );
}
