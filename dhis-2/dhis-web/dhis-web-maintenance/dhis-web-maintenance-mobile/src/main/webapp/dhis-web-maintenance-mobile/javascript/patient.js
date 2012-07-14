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
	var attributeId = jQuery('#' + container + ' [id=searchObjectId]').val(); 
	var element = jQuery('#' + container + ' [id=searchText]');
	var valueType = jQuery('#' + container+ ' [id=searchObjectId] option:selected').attr('valueType');
	
	if( attributeId == 'fixedAttr_birthDate' )
	{
		element.replaceWith( getDateField( container ) );
		datePickerValid( 'searchText_' + container );
		return;
	}
	
	$( '#searchText_' + container ).datepicker("destroy");
	$('#' + container + ' [id=dateOperator]').replaceWith("");
	if( attributeId == 'prg' )
	{
		element.replaceWith( programComboBox );
	}
	else if ( attributeId=='fixedAttr_gender' )
	{
		element.replaceWith( getGenderSelector() );
	}
	else if ( attributeId=='fixedAttr_age' )
	{
		element.replaceWith( getAgeTextBox() );
	}
	else if ( valueType=='YES/NO' )
	{
		element.replaceWith( getTrueFalseBox() );
	}
	else
	{
		element.replaceWith( searchTextBox );
	}
}

function getTrueFalseBox()
{
	var trueFalseBox  = '<select id="searchText" name="searchText">';
	trueFalseBox += '<option value="true">' + i18n_yes + '</option>';
	trueFalseBox += '<option value="false">' + i18n_no + '</option>';
	trueFalseBox += '</select>';
	return trueFalseBox;
}
	
function getGenderSelector()
{
	var genderSelector = '<select id="searchText" name="searchText">';
		genderSelector += '<option value="M">' + i18n_male + '</option>';
		genderSelector += '<option value="F">' + i18n_female + '</option>';
		genderSelector += '<option value="T">' + i18n_transgender + '</option>';
		genderSelector += '</select>';
	return genderSelector;
}

function getAgeTextBox( container )
{
	var ageField = '<select id="dateOperator" style="width:40px;" name="dateOperator" ><option value="="> = </option><option value="<"> < </option><option value="<="> <= </option><option value=">"> > </option><option value=">="> >= </option></select>';
	ageField += '<input type="text" id="searchText_' + container + '" name="searchText" style="width:200px;">';
	return ageField;
}

function getDateField( container )
{
	var dateField = '<select id="dateOperator" name="dateOperator" style="width:30px"><option value=">"> > </option><option value=">="> >= </option><option value="="> = </option><option value="<"> < </option><option value="<="> <= </option></select>';
	dateField += '<input type="text" id="searchText_' + container + '" name="searchText" maxlength="30" style="width:18em" onkeyup="searchPatientsOnKeyUp( event );">';
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

function validateAdvancedSearch()
{
	hideById( 'listPatientDiv' );
	var flag = true;
	var params = '';
	var dateOperator = '';
	jQuery("#searchDiv :input").each( function( i, item )
    {
		var elementName = $(this).attr('name');
		if( elementName=='searchText' && jQuery( item ).val() == '' )
		{
			showWarningMessage( i18n_specify_search_criteria );
			flag = false;
		}
	});
	
	if(flag){
		jQuery( '#advancedSearchTB tbody tr' ).each( function( i, row ){
			var dateOperator = "";
			jQuery( this ).find(':input').each( function( idx, item ){
				if( idx == 0){
					params += "&searchTexts=" + item.value;
				}
				else if( item.name == 'dateOperator'){
					dateOperator = item.value;
				}
				else if( item.name == 'searchText'){
					params += "_";
					if ( dateOperator.length >0 ) {
						params += dateOperator + "'" +  item.value.toLowerCase() + "'";
					}
					else{
						params += htmlEncode( item.value.toLowerCase().replace(/^\s*/, "").replace(/\s*$/, "") );
					}
				}
			})
		});
		params += '&listAll=false';
		params += '&searchBySelectedOrgunit=' + byId('searchBySelectedOrgunit').checked;
		
		contentDiv = 'listPatientDiv';
		jQuery( "#loaderDiv" ).show();
		advancedSearch( params );
	}
}

function advancedSearch( params )
{
	$.ajax({
		url: 'searchRegistrationPatient.action',
		type:"POST",
		data: params,
		success: function( html ){
				statusSearching = 1;
				setInnerHTML( 'listPatientDiv', html );
				showById('listPatientDiv');
				setFieldValue('listAll',false);
				jQuery( "#loaderDiv" ).hide();
			}
		});
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
