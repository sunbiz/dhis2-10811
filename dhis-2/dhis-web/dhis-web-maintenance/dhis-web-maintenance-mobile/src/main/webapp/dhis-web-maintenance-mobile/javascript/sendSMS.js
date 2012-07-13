var _target = "";
var isOrgunitSelected = false;

function selectedOrganisationUnitSendSMS( unitIds )
{
	isOrgunitSelected = (unitIds && unitIds.length > 0);
}

function toggleSMSGUI( _value )
{
	if ( _value == "phone" ) 
	{
		showById( 'phoneType' );
		hideById( 'orgunitType' );
	}
	else if ( _value == "user" || _value == "unit" )
	{
		selectionTree.clearSelectedOrganisationUnits();
		selectionTree.buildSelectionTree();
	
		hideById( 'phoneType' );
		showById( 'orgunitType' );
	}
	else {
		window.location.href = "showBeneficiarySMSForm.action";
	}
	
	_target = _value;
}

function toggleAll( checked )
{
	var list = jQuery( "input[type=checkbox][name=patientSMSCheckBox]" );
	
	for ( var i in list )
	{
		list[i].checked = checked;
	}
}

function sendSMSMessage( _form )
{
	var params = "";

	if ( _target == "phone" )
	{
		var list = getFieldValue( "recipient" );

		if ( list == '' )
		{
			showErrorMessage( i18n_no_recipient );
			return;
		}
		
		list = list.split( ";" );

		for ( var i in list )
		{
			if ( list[i] && list[i] != '' )
			{
				params += "recipients=" + list[i] + "&";
			}
		}

		params = "?" + params.substring( 0, params.length - 1 );
	}
	else if ( _target == "user" || _target == "unit" )
	{
		if ( !isOrgunitSelected )
		{
			showErrorMessage( i18n_please_select_orgunit );
			return;
		}
	}
	else
	{
		if ( hasElements( 'recipients' ) )
		{
			params = "?" + getParamString( 'recipients', 'recipients' );
		}
		else { markInvalid( "recipients", i18n_list_empty ); }
	}

	jQuery.postUTF8( _form.action + params,
	{
		gatewayId: getFieldValue( 'gatewayId' ),
		smsMessage: getFieldValue( 'smsMessage' ),
		sendTarget: getFieldValue( 'sendTarget' )
	}, function ( json )
	{
		if ( json.response == "success" ) {
			showSuccessMessage( json.message );
		}
		else {
			showErrorMessage( json.message, 7000 );
		}
	} );
}
