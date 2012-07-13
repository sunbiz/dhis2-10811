currentType = '';

function changeValueType( value )
{
	hideAll();

    if ( value == 'modem' ) {
        showById( "modemFields" );
    } else if ( value == 'bulksms' ) {
    	showById( "bulksmsFields" );
    } else if ( value == 'clickatell' ) {
    	showById( "clickatellFields" );
    } else {
	    showById( "genericHTTPFields" );
	}
	
	currentType = value;
}

function hideAll() 
{
	 hideById( "modemFields" );
	 hideById( "bulksmsFields" );
	 hideById( "clickatellFields" );
	 hideById( "genericHTTPFields" );
}

function getValidationRulesGateway()
{
	var rules = {};

	if ( currentType == 'modem' ) {
		rules = {
			'modemFields input[id=name]' : { 'required' : true },
			'modemFields input[id=port]' : { 'required' : true },
			'modemFields input[id=baudrate]' : { 'required' : true },
			'modemFields input[id=manufacturer]' : { 'required' : true },
			'modemFields input[id=model]' : { 'required' : true },
			'modemFields input[id=pin]' : { 'required' : true },
			'modemFields select[id=inbound]' : { 'required' : true },
			'modemFields select[id=outbound]' : { 'required' : true }
		};
	} else if ( currentType == 'bulksms' ) {
		rules = {
			'bulksmsFields input[id=name]' : { 'required' : true },
			'bulksmsFields input[id=username]' : { 'required' : true },
			'bulksmsFields input[id=password]' : { 'required' : true }
		};
	} else if ( currentType == 'clickatell' ) {
		rules = {
			'clickatellFields input[id=name]' : { 'required' : true },
			'clickatellFields input[id=username]' : { 'required' : true },
			'clickatellFields input[id=password]' : { 'required' : true },
			'clickatellFields input[id=apiId]' : { 'required' : true }
		};
	} else {
		rules = {
			'genericHTTPFields input[id=name]' : { 'required' : true },
			'genericHTTPFields input[id=username]' : { 'required' : true },
			'genericHTTPFields input[id=password]' : { 'required' : true },
			'genericHTTPFields input[id=urlTemplate]' : { 'required' : true }
		};
	}

	return rules;
}

function saveGatewayConfig()
{
	if ( currentType == 'modem' )
	{
		jQuery.postJSON( "saveModemConfig.action", {
			gatewayType: getFieldValue( 'gatewayType' ),
			name: getFieldValue( 'modemFields input[id=name]' ),
			port: getFieldValue( 'modemFields input[id=port]' ),
			baudRate: getFieldValue( 'modemFields input[id=baudRate]' ),
			manufacturer: getFieldValue( 'modemFields input[id=manufacturer]' ),
			model: getFieldValue( 'modemFields input[id=model]' ),
			pin: getFieldValue( 'modemFields input[id=pin]' ),
			inbound: getFieldValue( 'modemFields select[id=inbound]' ),
			outbound: getFieldValue( 'modemFields select[id=outbound]' )
		}, function ( json ) {
			showMessage( json );
		} );
	}
	else if ( currentType == 'bulksms' )
	{
		jQuery.postJSON( "saveBulkSMSConfig.action", {
			gatewayType: getFieldValue( 'gatewayType' ),
			name: getFieldValue( 'bulksmsFields input[id=name]' ),
			username: getFieldValue( 'bulksmsFields input[id=username]' ),
			password: getFieldValue( 'bulksmsFields input[id=password]' )
		}, function ( json ) {
			showMessage( json );
		} );
	}
	else if ( currentType == 'clickatell' )
	{
		jQuery.postJSON( "saveClickatellConfig.action", {
			gatewayType: getFieldValue( 'gatewayType' ),
			name: getFieldValue( 'clickatellFields input[id=name]' ),
			username: getFieldValue( 'clickatellFields input[id=username]' ),
			password: getFieldValue( 'clickatellFields input[id=password]' ),
			apiId: getFieldValue( 'clickatellFields input[id=apiId]' )
		}, function ( json ) {
			showMessage( json );
		} );
	}
	else
	{
		jQuery.postJSON( "saveHTTPConfig.action", {
			gatewayType: getFieldValue( 'gatewayType' ),
			name: getFieldValue( 'genericHTTPFields input[id=name]' ),
			username: getFieldValue( 'genericHTTPFields input[id=username]' ),
			password: getFieldValue( 'genericHTTPFields input[id=password]' ),
			urlTemplate: getFieldValue( 'genericHTTPFields input[id=urlTemplate]' )
		}, function ( json ) {
			showMessage( json );
		} );
	}
}

function showMessage( json )
{
	if ( json.response == "success" ) {
		showSuccessMessage( i18n_add_update_success );
	} else {
		showErrorMessage( json.message, 7000 );
	}
}