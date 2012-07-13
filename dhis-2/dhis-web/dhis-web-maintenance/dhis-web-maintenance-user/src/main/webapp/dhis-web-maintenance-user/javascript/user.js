
// -----------------------------------------------------------------------------
// Export to PDF file
// -----------------------------------------------------------------------------

function exportPDF( type )
{	
	var params = "type=" + type;
	params += "&months=" + jQuery( '#months' ).val();

	exportPdfByType( type, params );
}

// -----------------------------------------------------------------------------
// Search users
// -----------------------------------------------------------------------------

function searchUserName()
{
	var key = getFieldValue( 'key' );
    
    if ( key != '' ) 
    {
		jQuery( '#userForm' ).load( 'searchUser.action', {key:key}, unLockScreen );
    	lockScreen();
    }
    else 
    {
    	jQuery( '#userForm' ).submit();
    }
}

function getInactiveUsers()
{
	var months = $( '#months' ).val();
	
	window.location.href = 'alluser.action?months=' + months;
}

// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showUserDetails( userId )
{
    jQuery.post( 'getUser.action', { id: userId }, function ( json ) {
		setInnerHTML( 'usernameField', json.user.username );
		
		var fullName = json.user.firstName + ", " + json.user.surname;
		setInnerHTML( 'fullNameField', fullName );

		var email = json.user.email;
		setInnerHTML( 'emailField', email ? email : '[' + i18n_none + ']' );

		var phoneNumber = json.user.phoneNumber;
		setInnerHTML( 'phoneNumberField', phoneNumber ? phoneNumber : '[' + i18n_none + ']' );
		
		var lastLogin = json.user.lastLogin;
		setInnerHTML( 'lastLoginField', lastLogin ? lastLogin : '[' + i18n_none + ']' );
		
		var organisationUnits = joinNameableObjects( json.user.organisationUnits );
		setInnerHTML( 'assignedOrgunitField', organisationUnits ? organisationUnits : '[' + i18n_none + ']' );
		
		var roles = joinNameableObjects( json.user.roles );
		setInnerHTML( 'roleField', roles ? roles : '[' + i18n_none + ']' );
		
		showDetails();
	});
}

// -----------------------------------------------------------------------------
// Remove user
// -----------------------------------------------------------------------------

function removeUser( userId, username )
{
	removeItem( userId, username, i18n_confirm_delete, 'removeUser.action' );
}

// -----------------------------------------------------------------------------
// Usergroup functionality
// -----------------------------------------------------------------------------

function showUserGroupDetails( userGroupId )
{
    jQuery.post( 'getUserGroup.action', { userGroupId: userGroupId },
		function ( json ) {
			setInnerHTML( 'nameField', json.userGroup.name );
			setInnerHTML( 'noOfGroupField', json.userGroup.noOfUsers );

			showDetails();
	});
}

function removeUserGroup( userGroupId, userGroupName )
{
    removeItem( userGroupId, userGroupName, i18n_confirm_delete, 'removeUserGroup.action' );
}
