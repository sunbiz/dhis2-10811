
var selectedOrganisationUnits = [];

function selectOrganisationUnit__( units )
{
	selectedOrganisationUnits = units;
}

function removeMessage( id )
{
    removeItem( id, "", i18n_confirm_delete_message, "removeMessage.action" );
}

function read( id )
{
    window.location.href = "readMessage.action?id=" + id;
}

function validateMessage()
{
	var subject = $( "#subject" ).val();
	var text = $( "#text" ).val();
	
	if ( subject == null || subject.trim() == '' )
	{
		setHeaderMessage( i18n_enter_subject );
		return false;
	}
	
	if ( text == null || text.trim() == '' )
	{
		setHeaderMessage( i18n_enter_text );
		return false;
	}
	
	return true;
}

function showSenderInfo( messageId, senderId )
{
	var metaData = $( "#metaData" + messageId ).html();
	
	$.getJSON( "../dhis-web-commons-ajax-json/getUser.action", { id:senderId }, function( json ) {
		$( "#senderName" ).html( json.user.firstName + " " + json.user.surname );
		$( "#senderEmail" ).html( json.user.email );
		$( "#senderUsername" ).html( json.user.username );
		$( "#senderPhoneNumber" ).html( json.user.phoneNumber );
		$( "#senderOrganisationUnits" ).html( joinNameableObjects( json.user.organisationUnits ) );
		$( "#senderUserRoles" ).html( joinNameableObjects( json.user.roles ) );		
		$( "#messageMetaData" ).html( metaData );	
				
		$( "#senderInfo" ).dialog( {
	        modal : true,
	        width : 350,
	        height : 350,
	        title : "Sender"
	    } );
	} );
}

function sendReply()
{
	var id = $( "#conversationId" ).val();
	var text = $( "#text" ).val();
	
	if ( text == null || text.trim() == '' )
	{
		setHeaderMessage( i18n_enter_text );
		return false;
	}
	
	$( "#replyButton" ).attr( "disabled", "disabled" );
	
	setHeaderWaitMessage( i18n_sending_message );
	
	$.postUTF8( "sendReply.action", { id:id, text:text }, function() 
	{
		window.location.href = "readMessage.action?id=" + id;
	} );
}

function toggleFollowUp( id, followUp )
{
	var imageId = "followUp" + id;
	
	var url = "toggleFollowUp.action?id=" + id;
	
	$.getJSON( url, function( json )
	{
		var imageSrc = json.message == "true" ? "../images/marked.png" : "../images/unmarked.png";
			
		$( "#" + imageId ).attr( "src", imageSrc );
	} );
}
