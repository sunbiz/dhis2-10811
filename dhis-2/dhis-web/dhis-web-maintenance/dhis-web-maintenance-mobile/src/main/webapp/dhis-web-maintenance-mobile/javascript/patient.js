isAjax = true;

function organisationUnitSelected( orgUnits, orgUnitNames )
{	
	showById('selectDiv');
	hideById('listPatientDiv');

	setFieldValue( "selectedOrgunitText", orgUnitNames[0] );
}

selection.setListenerFunction( organisationUnitSelected );

// ----------------------------------------------------------------------------
// Search patients by name
// ----------------------------------------------------------------------------

function getPatientsByName( divname )
{	
	var fullName = jQuery('#' + divname + ' [id=fullName]').val().replace(/^\s+|\s+$/g,"");
	if( fullName.length > 0) 
	{
		contentDiv = 'resultSearchDiv';
		$('#resultSearchDiv' ).load("getPatientsByName.action",
			{
				fullName: fullName
			}).dialog({
				title: i18n_search_result,
				maximize: true, 
				closable: true,
				modal:true,
				overlay:{ background:'#000000', opacity: 0.8},
				width: 800,
				height: 400
		});
	}
	else
	{
		alert( i18n_no_patients_found );
	}
}

// -----------------------------------------------------------------------------
// Advanced search
// -----------------------------------------------------------------------------

function addAttributeOption()
{
	var rowId = 'advSearchBox' + jQuery('#advancedSearchTB select[name=searchingAttributeId]').length + 1;
	var contend  = '<td>' + getInnerHTML('searchingAttributeIdTD') + '</td>';
		contend += '<td>' + searchTextBox ;
		contend += '<input type="button" value="-" onclick="removeAttributeOption(' + "'" + rowId + "'" + ');"></td>';
		contend = '<tr id="' + rowId + '">' + contend + '</tr>';

	jQuery('#advancedSearchTB > tbody:last').append( contend );
}	

function removeAttributeOption( rowId )
{
	jQuery( '#' + rowId ).remove();
}		

//------------------------------------------------------------------------------
// Search patients by selected attribute
//------------------------------------------------------------------------------

function searchingAttributeOnChange( this_ )
{	
	var container = jQuery(this_).parent().parent().attr('id');
	var attributeId = jQuery('#' + container+ ' [id=searchingAttributeId]').val(); 
	var element = jQuery('#' + container+ ' [id=searchText]');
	var valueType = jQuery('#' + container+ ' [id=searchingAttributeId] option:selected').attr('valueType');
	
	if( attributeId == '-1' )
	{
		element.replaceWith( getDateField( container ) );
		datePickerValid( container + ' [id=searchText]' );
		return;
	}
	
	$('#' + container+ ' [id=searchText]').datepicker("destroy");
	$('#' + container+ ' [id=dateOperator]').replaceWith("");

	if( attributeId == '0' )
	{
		element.replaceWith( programComboBox );
	}
	else if ( attributeId == '-2' )
	{
		element.replaceWith( genderSelector );
	}
	else if ( valueType=='YES/NO' )
	{
		element.replaceWith( trueFalseBox );
	}
	else
	{
		element.replaceWith( searchTextBox );
	}
}

function getDateField( container )
{
	var dateField = '<select id="dateOperator" name="dateOperator" ><option value="&gt;"> &gt; </option><option value="="> = </option><option value="&lt;"> &lt; </option></select>';
	dateField += '<input type="text" id="searchText" name="searchText" maxlength="30" style="width:18em" onkeyup="searchPatientsOnKeyUp( event );">';
	return dateField;
}
	
//-----------------------------------------------------------------------------
// Search Patient
//-----------------------------------------------------------------------------

function searchPatientsOnKeyUp( event )
{
	var key = getKeyCode( event );
	
	if ( key == 13 )// Enter
	{
		searchAdvancedPatients()();
	}
}

function getKeyCode(e)
{
	 if (window.event)
		return window.event.keyCode;
	 return (e)? e.which : null;
}

function searchAdvancedPatients()
{
	hideById( 'listPatientDiv' );

	var searchTextFields = jQuery( '[name=searchText]' );
	var flag = true;

	jQuery( searchTextFields ).each( function( i, item )
    {
		if( jQuery( item ).val() == '' )
		{
			showWarningMessage( i18n_specify_search_criteria );
			flag = false;
		}
	});
	
	if ( !flag ) return;
	
	contentDiv = 'listPatientDiv';
	jQuery( "#loaderDiv" ).show();
	searchPatient();
	
}

// ----------------------------------------------------------------
// Get Params form Div
// ----------------------------------------------------------------

function getParamsForDiv( patientDiv)
{
	var params = '';
	var dateOperator = '';

	jQuery("#" + patientDiv + " :input").each(function()
	{
		var elementId = $(this).attr('id');
		
		if( $(this).attr('type') == 'checkbox' )
		{
			var checked = jQuery(this).is( ':checked' );
			params += elementId + "=" + checked + "&";
		}
		else if( elementId == 'dateOperator' )
		{
			dateOperator = jQuery(this).val();
		}
		else if( $(this).attr('type') != 'button' )
		{
			var value = "";
			if( jQuery(this).val() != '' )
			{
				value = htmlEncode(jQuery(this).val());
			}
			if( dateOperator != '' )
			{
				value = dateOperator + "'" + value + "'";
				dateOperator = "";
			}
			params += elementId + "="+ value + "&";
		}
	} );
		
	return params;
}

// -----------------------------------------------------------------------------
// Load all patients
// -----------------------------------------------------------------------------

function loadAllPatients()
{
	hideById( 'listPatientDiv' );
	
	var sortPatientAttributeId = getFieldValue('sortPatientAttributeId');
	
	jQuery('#loaderDiv').show();
	contentDiv = 'listPatientDiv';
	jQuery('#listPatientDiv').load('searchRegistrationPatient.action',{
			listAll:true,
			sortPatientAttributeId: (sortPatientAttributeId ? sortPatientAttributeId : "")
		},
		function(){
			statusSearching = 0;
			showById('listPatientDiv');
			jQuery('#loaderDiv').hide();
		});
	hideLoader();
}

function addPhoneToList( elementList, _id, _patientName, _phoneNo )
{
	var list = jQuery( "#" + elementList );
	list.append( "<option value='" + _id + "'>\"" + _patientName + " <" + _phoneNo + ">" + "\"</option>" );
	
	jQuery( "tr#tr" + _id ).hide();
}

function removePhoneFromList( elementList, _id )
{
	var list = jQuery( "#" + elementList + " option[value='" + _id + "']" ).remove();
	
	jQuery( "tr#tr" + _id ).show();
}

function searchPatient()
{
	var params = '';
	jQuery( '#advancedSearchTB tbody tr' ).each( function( i, row ){
		jQuery( this ).find(':input').each( function( idx, item ){
			if( idx == 0){
				params += "searchTexts=" + item.value;
			}
			else if( idx == 1){
				params += "_" + htmlEncode( item.value.toLowerCase() );
			}
		})
	});
	params += '&listAll=false';
	params += '&searchBySelectedOrgunit=' + byId('searchBySelectedOrgunit').checked;
		
	$.ajax({
		url: 'searchRegistrationPatient.action',
		type:"POST",
		data: params,
		success: function( html ){
				statusSearching = 1;
				setInnerHTML( 'listPatientDiv', html );
				showById('listPatientDiv');
				jQuery( "#loaderDiv" ).hide();
			}
		});
}

//--------------------------------------------------------------------------------------------
// Migration patient
//--------------------------------------------------------------------------------------------

function getPatientLocation( patientId )
{
	hideById('listPatientDiv');
	hideById('selectDiv');
	hideById('searchPatientDiv');
				
	jQuery('#loaderDiv').show();
	
	jQuery('#migrationPatientDiv').load("getPatientLocation.action", 
		{
			patientId: patientId
		}
		, function(){
			showById( 'migrationPatientDiv' );
			jQuery( "#loaderDiv" ).hide();
		});
}
