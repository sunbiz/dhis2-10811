
function recoverAccount()
{
	var username = $.trim( $( "#username" ).val() );
	
	if ( username.length == 0 )
	{
		return false;
	}
	
	$.ajax( {
		url: "../../api/account/recovery",
		data: {
			username: username
		},
		type: "post",
		success: function( data ) {
			$( "#recoveryForm" ).hide();
			$( "#recoverySuccessMessage" ).fadeIn();
		},
		error: function( data ) {
			$( "#recoveryForm" ).hide();
			$( "#recoveryErrorMessage" ).fadeIn();
		}
	} );
}
