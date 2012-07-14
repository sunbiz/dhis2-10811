var prefixId = 'ps_';
var COLOR_RED = "#fb4754";
var COLOR_GREEN = "#8ffe8f";
var COLOR_YELLOW = "#f9f95a";
var COLOR_LIGHTRED = "#fb6bfb";
var COLOR_GREY = "#bbbbbb";
var COLOR_LIGHT_RED = "#ff7676";
var COLOR_LIGHT_YELLOW = "#ffff99";
var COLOR_LIGHT_GREEN = "#ccffcc";
var COLOR_LIGHT_LIGHTRED = "#ff99ff";
var COLOR_LIGHT_GREY = "#ddd";
var MARKED_VISIT_COLOR = '#AAAAAA';
var SUCCESS_COLOR = '#ccffcc';
var ERROR_COLOR = '#ccccff';
var SAVING_COLOR = '#ffffcc';
var SUCCESS = 'success';
var ERROR = 'error';

var isDashboard = false;

// Disable caching for ajax requests in general 

$( document ).ready( function() {
	$.ajaxSetup ({
    	cache: false
	});
} );

function dobTypeOnChange( container ){

	var type = jQuery('#' + container + ' [id=dobType]').val();
	if(type == 'V' || type == 'D')
	{
		jQuery('#' + container + ' [id=age]').rules("remove");
		jQuery('#' + container + ' [id=age]').css("display","none");
		
		jQuery('#' + container + ' [id=birthDate]').rules("add",{required:true});
		datePickerValid( container + ' [id=birthDate]' );
		jQuery('#' + container + ' [id=birthDate]').css("display","");
	}
	else if(type == 'A')
	{
		jQuery('#' + container + ' [id=age]').rules("add",{required:true, number: true});
		jQuery('#' + container + ' [id=age]').css("display","");
		
		jQuery('#' + container + ' [id=birthDate]').rules("remove","required");
		$('#' + container+ ' [id=birthDate]').datepicker("destroy");
		jQuery('#' + container + ' [id=birthDate]').css("display","none");
	}
	else 
	{
		jQuery('#' + container + ' [id=age]').rules("remove");
		jQuery('#' + container + ' [id=age]').css("display","");
		
		jQuery('#' + container + ' [id=birthDate]').rules("remove","required");
		$('#' + container+ ' [id=birthDate]').datepicker("destroy");
		jQuery('#' + container + ' [id=birthDate]').css("display","none");
	}
}

// -----------------------------------------------------------------------------
// Advanced search
// -----------------------------------------------------------------------------

function addAttributeOption()
{
	jQuery('#advancedSearchTB [name=clearSearchBtn]').attr('disabled', false);
	var rowId = 'advSearchBox' + jQuery('#advancedSearchTB select[name=searchObjectId]').length + 1;
	var contend  = '<td>' + getInnerHTML('searchingAttributeIdTD') + '</td>';
		contend += '<td>' + searchTextBox ;
		contend += '&nbsp;<input type="button" name="clearSearchBtn" class="large-button" value="' + i18n_clear + '" onclick="removeAttributeOption(' + "'" + rowId + "'" + ');"></td>';
		contend = '<tr id="' + rowId + '">' + contend + '</tr>';

	jQuery('#advancedSearchTB').append( contend );
}	

function removeAttributeOption( rowId )
{
	jQuery( '#' + rowId ).remove();
	if( jQuery( '#advancedSearchTB tr' ).length == 3 ){
		jQuery('#advancedSearchTB [name=clearSearchBtn]').attr('disabled', true);
	}	
}	

//------------------------------------------------------------------------------
// Search patients by selected attribute
//------------------------------------------------------------------------------

function searchObjectOnChange( this_ )
{	
	var container = jQuery(this_).parent().parent().attr('id');
	var attributeId = jQuery('#' + container + ' [id=searchObjectId]').val(); 
	var element = jQuery('#' + container + ' [name=searchText]');
	var valueType = jQuery('#' + container+ ' [id=searchObjectId] option:selected').attr('valueType');
	
	jQuery('#searchText_' + container).removeAttr('readonly', false);
	jQuery('#searchText_' + container).val("");
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
	var ageField = '<select id="dateOperator" name="dateOperator" style="width:40px"><option value=">"> > </option><option value=">="> >= </option><option value="="> = </option><option value="<"> < </option><option value="<="> <= </option></select>';
	ageField += '<input type="text" id="searchText_' + container + '" name="searchText" style="width:220px;">';
	return ageField;
}

function getDateField( container )
{
	var dateField = '<select id="dateOperator" name="dateOperator" style="width:40px"><option value=">"> > </option><option value=">="> >= </option><option value="="> = </option><option value="<"> < </option><option value="<="> <= </option></select>';
	dateField += '<input type="text" id="searchText_' + container + '" name="searchText" style="width:200px;" onkeyup="searchPatientsOnKeyUp( event );">';
	return dateField;
}

//-----------------------------------------------------------------------------
// Search Patient
//-----------------------------------------------------------------------------

function searchPatientsOnKeyUp( event )
{
	var key = getKeyCode( event );
	
	if ( key==13 )// Enter
	{
		validateAdvancedSearch();
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
	var dateOperator = '';
	
	if (getFieldValue('searchByProgramStage') == "false" 
		|| ( getFieldValue('searchByProgramStage') == "true"  
			&& jQuery( '#advancedSearchTB tr' ).length > 2) ){
		jQuery("#searchDiv :input").each( function( i, item )
		{
			var elementName = $(this).attr('name');
			if( elementName=='searchText' && jQuery( item ).val() == '')
			{
				showWarningMessage( i18n_specify_search_criteria );
				flag = false;
			}
		});
	}
	
	if(flag){
		contentDiv = 'listPatientDiv';
		jQuery( "#loaderDiv" ).show();
		advancedSearch( getSearchParams() );
	}
}

function getSearchParams()
{
	var params = "";
	var programIds = "";
	var programStageId = jQuery('#programStageAddPatient').val();
	if( getFieldValue('searchByProgramStage') == "true" ){
		var statusEvent = jQuery('#programStageAddPatientTR [id=statusEvent]').val();
		var startDueDate = getFieldValue('startDueDate');
		var endDueDate = getFieldValue('endDueDate');
		params = '&searchTexts=stat_' + getFieldValue('programIdAddPatient') 
			   + '_' + startDueDate + '_' + endDueDate
			   + "_" + getFieldValue('orgunitId')
			   + '_false_' + statusEvent;
	}
	
	var flag = false;
	jQuery( '#advancedSearchTB tr' ).each( function( i, row ){
		var dateOperator = "";
		var p = "";
		jQuery( this ).find(':input').each( function( idx, item ){
			if(item.type!="button"){
				if( idx == 0){
					p = "&searchTexts=" + item.value;
					if(item.value=='prg'){
						programIds += '&programIds=';
						flag = true;
					}
				}
				else if( item.name == 'dateOperator'){
					dateOperator = item.value;
				}
				else if( item.name == 'searchText'){
					if( item.value!='')
					{
						p += "_";
						if ( dateOperator.length >0 ) {
							p += dateOperator + "'" +  item.value.toLowerCase() + "'";
						}
						else{
							p += htmlEncode( item.value.toLowerCase().replace(/^\s*/, "").replace(/\s*$/, "") );
						}
						
						if( flag ){
							programIds += item.value;
							flag = false;
						}
					}
					else {
						p = "";
					}
				}
			}
		});
		
		var searchInAllFacility = byId('searchInAllFacility').checked;
		if( getFieldValue('searchByProgramStage') == "false" && !searchInAllFacility ){
			p += "_" + getFieldValue('orgunitId');
		}
		params += p;
	});
		
	params += '&listAll=false';
	if( getFieldValue('searchByProgramStage') == "false"){
		var searchInAllFacility = byId('searchInAllFacility').checked;
		params += '&searchBySelectedOrgunit=' + !searchInAllFacility;
	}
	else
	{
		params += '&searchBySelectedOrgunit=false';
	}
	params += programIds;
	
	return params;
}

// ----------------------------------------------------------------------------
// Show death field in person re form
// ----------------------------------------------------------------------------

function isDeathOnChange()
{
	var isDeath = byId('isDead').checked;
	setFieldValue('deathDate','');
	if(isDeath)
	{
		showById('deathDateTR');
	}
	else
	{
		hideById('deathDateTR');
	}
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
				var checked = jQuery(this).attr('checked') ? true : false;
				params += elementId + "=" + checked + "&";
			}
			else if( elementId =='dateOperator' )
			{
				dateOperator = jQuery(this).val();
			}
			else if( $(this).attr('type') != 'button' )
			{
				var value = "";
				if( jQuery(this).val()!= null && jQuery(this).val() != '' )
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
		});
		
	return params;
}

// -----------------------------------------------------------------------------
// View patient details
// -----------------------------------------------------------------------------

function showPatientDetails( patientId )
{
    $('#detailsInfo').load("getPatientDetails.action", 
		{
			id:patientId
		}
		, function( ){
		}).dialog({
			title: i18n_patient_details,
			maximize: true, 
			closable: true,
			modal:false,
			overlay:{background:'#000000', opacity:0.1},
			width: 450,
			height: 300
		});
}

function showPatientHistory( patientId )
{
	$('#detailsInfo').load("getPatientHistory.action", 
		{
			patientId:patientId
		}
		, function( ){
			
		}).dialog({
			title: i18n_patient_details_and_history,
			maximize: true, 
			closable: true,
			modal:false,
			overlay:{background:'#000000', opacity:0.1},
			width: 800,
			height: 520
		});
}

function exportPatientHistory( patientId, type )
{
	var url = "getPatientHistory.action?patientId=" + patientId + "&type=" + type;
	window.location.href = url;
}

function setEventColorStatus( programStageInstanceId, status )
{
	var boxElement = jQuery('#ps_' + programStageInstanceId );
	var dueDateElementId = 'value_' + programStageInstanceId + '_date';
	var status = eval(status);
	
	switch(status)
	{
		case 1:
			boxElement.css('border-color', COLOR_GREEN);
			boxElement.css('background-color', COLOR_LIGHT_GREEN);
			jQuery("#" + dueDateElementId ).datepicker("destroy");
			disable( dueDateElementId );
			return;
		case 2:
			boxElement.css('border-color', COLOR_LIGHTRED);
			boxElement.css('background-color', COLOR_LIGHT_LIGHTRED);
			datePicker( dueDateElementId );
			enable( dueDateElementId );
			return;
		case 3:
			boxElement.css('border-color', COLOR_YELLOW);
			boxElement.css('background-color', COLOR_LIGHT_YELLOW);
			datePicker( dueDateElementId );
			enable( dueDateElementId );
			return;
		case 4:
			boxElement.css('border-color', COLOR_RED);
			boxElement.css('background-color', COLOR_LIGHT_RED);
			datePicker( dueDateElementId );
			enable( dueDateElementId );
			return;
		case 5:
			boxElement.css('border-color', COLOR_GREY);
			boxElement.css('background-color', COLOR_LIGHT_GREY);
			disable( 'ps_' + programStageInstanceId );
			jQuery( "#" + dueDateElementId ).datepicker("destroy");
			disable( dueDateElementId );
			return;
		default:
		  return;
	}
}

// -----------------------------------------------------------------------------
// check duplicate patient
// -----------------------------------------------------------------------------

function checkDuplicate( divname )
{
	$.postUTF8( 'validatePatient.action', 
		{
			id: jQuery( '#' + divname + ' [id=id]' ).val(),
			fullName: jQuery( '#' + divname + ' [id=fullName]' ).val(),
			dobType: jQuery( '#' + divname + ' [id=dobType]' ).val(),
			gender: jQuery( '#' + divname + ' [id=gender]' ).val(),
			birthDate: jQuery( '#' + divname + ' [id=birthDate]' ).val(),        
			age: jQuery( '#' + divname + ' [id=age]' ).val()
		}, function( xmlObject, divname )
		{
			checkDuplicateCompleted( xmlObject, divname );
		});
}

function checkDuplicateCompleted( messageElement, divname )
{
	checkedDuplicate = true;    
	var type = jQuery(messageElement).find('message').attr('type');
	var message = jQuery(messageElement).find('message').text();
    
    if( type == 'success')
    {
    	showSuccessMessage(i18n_no_duplicate_found);
    }
    if ( type == 'input' )
    {
        showWarningMessage(message);
    }
    else if( type == 'duplicate' )
    {
    	showListPatientDuplicate( messageElement, true );
    }
}

function enableBtn(){
	if(registration==undefined || !registration)
	{
		var programIdAddPatient = getFieldValue('programIdAddPatient');
		if( programIdAddPatient!='' ){
			enable('listPatientBtn');
			enable('addPatientBtn');
			enable('advancedSearchBtn');
			jQuery('#advanced-search :input').each( function( idx, item ){
				enable(this.id);
			});
		}
		else
		{
			disable('listPatientBtn');
			disable('addPatientBtn');
			disable('advancedSearchBtn');
			jQuery('#advanced-search :input').each( function( idx, item ){
				disable(this.id);
			});
		}
	}
}

function enableRadioButton( programId )
{
	var prorgamStageId = jQuery('#programStageAddPatient').val();
	if( prorgamStageId== ''){
		jQuery('#programStageAddPatientTR [name=statusEvent]').attr("disabled", true);
	}
	else{
		jQuery('#programStageAddPatientTR [name=statusEvent]').removeAttr("disabled");
	}
}

function showColorHelp()
{
	jQuery('#colorHelpDiv').dialog({
		title: i18n_color_quick_help,
		maximize: true, 
		closable: true,
		modal:false,
		width: 380,
		height: 270
	}).show('fast');
}

// ----------------------------------------------------------------------------
// Create New Event
// ----------------------------------------------------------------------------

function showCreateNewEvent( programInstanceId, programStageId )
{
	setInnerHTML('createEventMessage_' + programInstanceId, '');
	jQuery('#createNewEncounterDiv_' + programInstanceId ).dialog({
			title: i18n_create_new_event,
			maximize: true, 
			closable: true,
			modal:false,
			overlay:{background:'#000000', opacity:0.1},
			width: 450,
			height: 160
		}).show('fast');
		
	if( programStageId != undefined )
	{
		jQuery('#repeatableProgramStage_' + programInstanceId).val(programStageId);
	}
	setSuggestedDueDate( programInstanceId );
}

function setSuggestedDueDate( programInstanceId )
{
	var lastVisit = jQuery('.stage-object-selected').attr('reportDate');
	jQuery('#tb_' + programInstanceId + ' input').each(function()
	{
		var reportDate = jQuery(this).attr('reportDate');
		if( reportDate > lastVisit )
		{
			lastVisit = reportDate;
		}
	});
	
	var standardInterval = jQuery('#repeatableProgramStage_' + programInstanceId + ' option:selected').attr('standardInterval');
	var date = $.datepicker.parseDate( dateFormat, lastVisit );
	var d = date.getDate() + eval(standardInterval);
	var m = date.getMonth();
	var y = date.getFullYear();
	var edate= new Date(y, m, d);
	var sdate = jQuery.datepicker.formatDate( dateFormat , edate ) ;
	jQuery( '#dueDateNewEncounter_' + programInstanceId ).val(sdate);
}

function closeDueDateDiv( programInstanceId )
{
	jQuery('#createNewEncounterDiv_' + programInstanceId).dialog('close');
}

//------------------------------------------------------
// Register Irregular-encounter
//------------------------------------------------------

function registerIrregularEncounter( programInstanceId, programStageId, programStageName, dueDate )
{
	jQuery.postJSON( "registerIrregularEncounter.action",
		{ 
			programInstanceId:programInstanceId,
			programStageId: programStageId, 
			dueDate: dueDate 
		}, 
		function( json ) 
		{   
			var programStageInstanceId = json.message;
			disableCompletedButton(false);
			
			var elementId = prefixId + programStageInstanceId;
			var flag = false;
			var programType = jQuery('.stage-object-selected').attr('type');
			
			jQuery("#programStageIdTR_" + programInstanceId + " input[name='programStageBtn']").each(function(i,item){
				var element = jQuery(item);
				var dueDateInStage = element.attr('dueDate');
				if( dueDate < dueDateInStage && !flag)
				{	
					jQuery('<td>'
						+ '<div class="orgunit-object" id="org_' + programStageInstanceId + '">&nbsp;</div>'
						+ '<input name="programStageBtn" '
						+ 'pi="' + programInstanceId + '" ' 
						+ 'id="' + elementId + '" ' 
						+ 'psid="' + programStageId + '" '
						+ 'programType="' + programType + '" '
						+ 'psname="' + programStageName + '" '
						+ 'dueDate="' + dueDate + '" '
						+ 'value="'+ programStageName + '&#13;&#10;' + dueDate + '" '
						+ 'onclick="javascript:loadDataEntry(' + programStageInstanceId + ')" '
						+ 'type="button" class="stage-object" '
						+ '></td>'
						+ '<td id="arrow_' + programStageInstanceId + '"><img src="images/rightarrow.png"></td>')
					.insertBefore(element.parent());
					flag = true;
				}
			});
			
			if( !flag )
			{
				jQuery("#programStageIdTR_" + programInstanceId).append('<td id="arrow_' + programStageInstanceId + '"><img src="images/rightarrow.png"></td>'
					+ '<td>'
					+ '<div class="orgunit-object" id="org_' + programStageInstanceId + '">&nbsp;</div>'
					+ '<input name="programStageBtn" '
					+ 'id="' + elementId + '" ' 
					+ 'psid="' + programStageId + '" '
					+ 'programType="' + programType + '" '
					+ 'psname="' + programStageName + '" '
					+ 'dueDate="' + dueDate + '" '
					+ 'value="'+ programStageName + '&#13;&#10;' + dueDate + '" '
					+ 'onclick="javascript:loadDataEntry(' + programStageInstanceId + ')" '
					+ 'type="button" class="stage-object" '
					+ '></td>');
			}
			if( jQuery('#tb_' + programInstanceId + " :input" ).length > 4 ){
				jQuery('#tb_' + programInstanceId + ' .arrow-left').removeClass("hidden");
				jQuery('#tb_' + programInstanceId + ' .arrow-right').removeClass("hidden");
			}
			
			if( dueDate < getCurrentDate() ){
				setEventColorStatus( programStageInstanceId, 4 );
			}
			else{
				setEventColorStatus( programStageInstanceId, 3 );
			}
			
			jQuery('#ps_' + programStageInstanceId ).focus();
			var repeatable = jQuery('#repeatableProgramStage_' + programInstanceId + " [value=" + programStageId + "]" )
			if( repeatable.attr("repeatable")=="false"){
				repeatable.remove();
			}
			jQuery('#createNewEncounterDiv_' + programInstanceId).dialog("close");
			resetActiveEvent(programInstanceId);
			loadDataEntry( programStageInstanceId );
			showSuccessMessage(i18n_create_event_success);
		});
}

function disableCompletedButton( disabled )
{
	if(disabled){
		disable('completeBtn');
		enable('uncompleteBtn');
		enable('uncompleteAndAddNewBtn');
	}
	else{
		enable('completeBtn');
		disable('uncompleteBtn');
		disable('uncompleteAndAddNewBtn');
	}
}

//-----------------------------------------------------------------------------
// Save due-date
//-----------------------------------------------------------------------------

function saveDueDate( programInstanceId, programStageInstanceId, programStageInstanceName )
{
	var field = document.getElementById( 'value_' + programStageInstanceId + '_date' );
	var dateOfIncident = new Date( byId('dateOfIncident').value );
	var dueDate = new Date(field.value);
	
	if( dueDate < dateOfIncident )
	{
		field.style.backgroundColor = '#FFCC00';
		alert( i18n_date_less_incident );
		return;
	}
	
	field.style.backgroundColor = '#ffffcc';
	
	var dateDueSaver = new DateDueSaver( programStageInstanceId, field.value, '#ccffcc' );
	dateDueSaver.save();
}

function DateDueSaver( programStageInstanceId_, dueDate_, resultColor_ )
{
	var programStageInstanceId = programStageInstanceId_;	
	var dueDate = dueDate_;
	var resultColor = resultColor_;	

	this.save = function()
	{
		var params = 'programStageInstanceId=' + programStageInstanceId + '&dueDate=' + dueDate;
		$.ajax({
			   type: "POST",
			   url: "saveDueDate.action",
			   data: params,
			   dataType: "xml",
			   success: function(result){
					handleResponse (result);
			   },
			   error: function(request,status,errorThrown) {
					handleHttpError (request);
			   }
			});
	};

	function handleResponse( rootElement )
	{
		var codeElement = rootElement.getElementsByTagName( 'code' )[0];
		var code = parseInt( codeElement.firstChild.nodeValue );
   
		if ( code == 0 )
		{
			var box = jQuery('#ps_' + programStageInstanceId );
			box.attr('dueDate', dueDate );
			var boxName = box.attr('psname') + "\n" + dueDate;
			box.val( boxName );
			if( dueDate < getCurrentDate() )
			{
				box.css('border-color', COLOR_RED);
				box.css('background-color', COLOR_LIGHT_RED);
				jQuery('#stat_' + programStageInstanceId + " option[value=3]").remove();
				jQuery('#stat_' + programStageInstanceId ).prepend("<option value='4' selected>" + i18n_overdue + "</option>");
			}
			else
			{
				box.css('border-color', COLOR_YELLOW);
				box.css('background-color', COLOR_LIGHT_YELLOW);
				jQuery('#stat_' + programStageInstanceId + " option[value=4]").remove();
				jQuery('#stat_' + programStageInstanceId ).prepend("<option value='3' selected>" + i18n_scheduled_in_future + "</option>");
			}
			markValue( resultColor );                   
		}
		else
		{
			markValue( COLOR_GREY );
			window.alert( i18n_saving_value_failed_status_code + '\n\n' + code );
		}
	}

	function handleHttpError( errorCode )
	{
		markValue( COLOR_GREY );
		window.alert( i18n_saving_value_failed_error_code + '\n\n' + errorCode );
	}   

	function markValue( color )
	{       
   
		var element = document.getElementById( 'value_' + programStageInstanceId + '_date' );	
           
		element.style.backgroundColor = color;
	}
}

//-----------------------------------------------------------------------------
// Cosmetic UI
//-----------------------------------------------------------------------------

function resize(){
	var width = 400;
	var w = $(window).width(); 
	if(jQuery(".patient-object").length > 1 ){
		width += 150;
	}
	if(jQuery(".show-new-event").length > 0 ){
		width += 150;
	}
	
	$('.stage-flow').each(function(){
		var programInstanceId = this.id.split('_')[1];
		if(jQuery(this).find(".table-flow").outerWidth() > jQuery(this).width() ){
			jQuery('#tb_' + programInstanceId ).find('.arrow-left').removeClass("hidden");
			jQuery('#tb_' + programInstanceId ).find('.arrow-right').removeClass("hidden");
		}
		else{
			jQuery('#tb_' + programInstanceId ).find('.arrow-left').addClass("hidden");
			jQuery('#tb_' + programInstanceId ).find('.arrow-right').addClass("hidden");
		}
	});
	
	$('.stage-flow').css('width', w-width); 
	$('.table-flow').css('width', w-width); 
	$('.table-flow tr').each(function(){
		jQuery(this).find('td').attr("width", "10px");
		jQuery(this).find('td:last').removeAttr("width");
	});
}

function moveLeft( programInstanceFlowDiv ){
	jQuery("#" + programInstanceFlowDiv).animate({scrollLeft: "-=200"}, 'fast');
}

function moveRight(programInstanceFlowDiv){
	jQuery("#" + programInstanceFlowDiv).animate({scrollLeft: "+=200"}, 'fast');
}

function setEventStatus( field, programStageInstanceId )
{	
	var status = field.value;
	field.style.backgroundColor = SAVING_COLOR;
	jQuery.postUTF8( 'setEventStatus.action',
		{
			programStageInstanceId:programStageInstanceId,
			status:status
		}, function ( json )
		{
			enable('ps_' + programStageInstanceId);
			var eventBox = jQuery('#ps_' + programStageInstanceId);
			eventBox.attr('status',status);
			setEventColorStatus( programStageInstanceId, status );
			resetActiveEvent( eventBox.attr("pi") );
			
			if( status==1 || status==2 ){
				hideById('del_' + programStageInstanceId);
			}
			else{
				showById('del_' + programStageInstanceId);
				if( status==5){
					disable('ps_' + programStageInstanceId);
					var id = 'ps_' + programStageInstanceId;
					if( jQuery(".stage-object-selected").attr('id')==id )
					{
						hideById('entryForm');
						hideById('executionDateTB');
						hideById('inputCriteriaDiv');
					}
				}
			}
			field.style.backgroundColor = SUCCESS_COLOR;
		} );
}

function resetActiveEvent( programInstanceId )
{
	var hasActiveEvent = false;
	jQuery(".stage-object").each(function(){
		var status = jQuery(this).attr('status');
		if(status !=1 && status != 5 && !hasActiveEvent){
			var value = jQuery(this).val();
			var programStageInstanceId = jQuery(this).attr('id').split('_')[1];
			
			jQuery('#td_' + programInstanceId).attr("onClick", "javascript:loadActiveProgramStageRecords("+ programInstanceId +", "+ programStageInstanceId  +")")
			jQuery('#tr2_' + programInstanceId).html("<a>>>" + value + "</a>");
			jQuery('#tr2_' + programInstanceId).attr("onClick", "javascript:loadActiveProgramStageRecords("+ programInstanceId +", "+ programStageInstanceId + ")");
			
			var id = 'ps_' + programStageInstanceId;
			enable('ps_' + programStageInstanceId );
			if( jQuery(".stage-object-selected").attr('id')!=jQuery(this).attr('id') )
			{
				hasActiveEvent = true;
			}
		}
	});
	
	if( !hasActiveEvent ){
		jQuery('#td_' + programInstanceId).attr("onClick", "javascript:loadActiveProgramStageRecords("+ programInstanceId +", false)")
		jQuery('#tr2_' + programInstanceId).html("");
		jQuery('#tr2_' + programInstanceId).attr("onClick", "");
		
		//hideById('entryForm');
		hideById('executionDateTB');
		hideById('inputCriteriaDiv');
	}
}

function removeEvent( programStageInstanceId, isEvent )
{	
    var result = window.confirm( i18n_comfirm_delete_event );
    if ( result )
    {
    	$.postJSON(
    	    "removeCurrentEncounter.action",
    	    {
    	        "id": programStageInstanceId   
    	    },
    	    function( json )
    	    { 
    	    	if ( json.response == "success" )
    	    	{
					jQuery( "tr#tr" + programStageInstanceId ).remove();
	                
					jQuery( "table.listTable tbody tr" ).removeClass( "listRow listAlternateRow" );
	                jQuery( "table.listTable tbody tr:odd" ).addClass( "listAlternateRow" );
	                jQuery( "table.listTable tbody tr:even" ).addClass( "listRow" );
					jQuery( "table.listTable tbody" ).trigger("update");
					
					hideById('smsManagementDiv');
					if(isEvent)
					{
						showById('searchDiv');
						showById('listPatientDiv');
					}
					if(jQuery(".stage-object-selected").attr('id')== id)
					{
						hideById('entryForm');
						hideById('executionDateTB');
						hideById('inputCriteriaDiv');
					}
					var id = 'ps_' + programStageInstanceId;
					var programInstanceId = jQuery('#' + id).attr('pi');
					jQuery('#ps_' + programStageInstanceId).remove();
					jQuery('#arrow_' + programStageInstanceId).remove();
					jQuery('#org_' + programStageInstanceId).remove();
					resetActiveEvent( programInstanceId );
					showSuccessMessage( i18n_delete_success );
    	    	}
    	    	else if ( json.response == "error" )
    	    	{ 
					showWarningMessage( json.message );
    	    	}
    	    }
    	);
    }
}

// -----------------------------------------------------------------------------
// Show relationship with new patient
// -----------------------------------------------------------------------------

function showRelationshipList( patientId )
{
	hideById('addRelationshipDiv');
	hideById('patientDashboard');
	hideById('selectDiv');
	hideById('searchDiv');
	hideById('listPatientDiv');
	hideById('entryForm');

	jQuery('#loaderDiv').show();
	jQuery('#listRelationshipDiv').load('showRelationshipList.action',
		{
			id:patientId
		}, function()
		{
			showById('listRelationshipDiv');
			jQuery('#loaderDiv').hide();
		});
}

// -----------------------------------------------------------------------------
// Update Patient
// -----------------------------------------------------------------------------

function showUpdatePatientForm( patientId )
{
	hideById('listPatientDiv');
	setInnerHTML('editPatientDiv', '');
	hideById('selectDiv');
	hideById('searchDiv');
	hideById('migrationPatientDiv');
	hideById('patientDashboard');
	
	jQuery('#loaderDiv').show();
	jQuery('#editPatientDiv').load('showUpdatePatientForm.action',
		{
			id:patientId,
			programId: getFieldValue('programIdAddPatient')
		}, function()
		{
			jQuery('#loaderDiv').hide();
			showById('editPatientDiv');
		});
}

function validateUpdatePatient()
{
	$("#editPatientDiv :input").attr("disabled", true);
	$.ajax({
		type: "POST",
		url: 'validatePatient.action',
		data: getParamsForDiv('editPatientDiv'),
		success:updateValidationCompleted
     });
}

function updateValidationCompleted( messageElement )
{
    var type = jQuery(messageElement).find('message').attr('type');
	var message = jQuery(messageElement).find('message').text();
    
    if ( type == 'success' )
    {
    	removeDisabledIdentifier();
    	updatePatient();
    }
	else
	{
		$("#editPatientDiv :input").attr("disabled", true);
		if ( type == 'error' )
		{
			showErrorMessage( i18n_saving_patient_failed + ':' + '\n' + message );
		}
		else if ( type == 'input' )
		{
			showWarningMessage( message );
		}
		else if( type == 'duplicate' )
		{
			showListPatientDuplicate(messageElement, true);
		}
		$("#editPatientDiv :input").attr("disabled", false);
	}
}

function updatePatient()
{
	var params = 'programId=' + getFieldValue('programIdAddPatient') 
		+ '&' + getParamsForDiv('editPatientDiv');

	$.ajax({
      type: "POST",
      url: 'updatePatient.action',
      data: params,
      success: function( json ) {
			if( getFieldValue('programIdAddPatient')!='')
			{
				jQuery.postJSON( "saveProgramEnrollment.action",
				{
					patientId: getFieldValue('id'),
					programId: getFieldValue('programIdAddPatient'),
					dateOfIncident: jQuery('#patientForm [id=dateOfIncident]').val(),
					enrollmentDate: jQuery('#patientForm [id=enrollmentDate]').val()
				}, 
				function( json ) 
				{ 
					showPatientDashboardForm( getFieldValue('id') );
				});
			}
			else
			{
				showPatientDashboardForm( getFieldValue('id') );
			}
      }
     });
}

function addEventForPatientForm( divname )
{
	jQuery("#" + divname + " [id=checkDuplicateBtn]").click(function() {
		checkDuplicate( divname );
	});
	
	jQuery("#" + divname + " [id=dobType]").change(function() {
		dobTypeOnChange( divname );
	});
}

function showRepresentativeInfo( patientId)
{
	jQuery('#representativeInfo' ).dialog({
			title: i18n_representative_info,
			maximize: true, 
			closable: true,
			modal: false,
			overlay: {background:'#000000', opacity:0.1},
			width: 400,
			height: 300
		});
}

function removeDisabledIdentifier()
{
	jQuery("input.idfield").each(function(){
		if( jQuery(this).is(":disabled"))
			jQuery(this).val("");
	});
}

/**
 * Show list patient duplicate  by jQuery thickbox plugin
 * @param rootElement : root element of the response xml
 * @param validate  :  is TRUE if this method is called from validation method  
 */
function showListPatientDuplicate( rootElement, validate )
{
	var message = jQuery(rootElement).find('message').text();
	var patients = jQuery(rootElement).find('patient');
	
	var sPatient = "";
	jQuery( patients ).each( function( i, patient )
        {
			sPatient += "<hr style='margin:5px 0px;'><table>";
			sPatient += "<tr><td class='bold'>" + i18n_patient_system_id + "</td><td>" + jQuery(patient).find('systemIdentifier').text() + "</td></tr>" ;
			sPatient += "<tr><td class='bold'>" + i18n_patient_full_name + "</td><td>" + jQuery(patient).find('fullName').text() + "</td></tr>" ;
			sPatient += "<tr><td class='bold'>" + i18n_patient_gender + "</td><td>" + jQuery(patient).find('gender').text() + "</td></tr>" ;
			sPatient += "<tr><td class='bold'>" + i18n_patient_date_of_birth + "</td><td>" + jQuery(patient).find('dateOfBirth').text() + "</td></tr>" ;
			sPatient += "<tr><td class='bold'>" + i18n_patient_age + "</td><td>" + jQuery(patient).find('age').text() + "</td></tr>" ;
			sPatient += "<tr><td class='bold'>" + i18n_patient_phone_number + "</td><td>" + jQuery(patient).find('phoneNumber').text() + "</td></tr>";
        	
			var identifiers = jQuery(patient).find('identifier');
        	if( identifiers.length > 0 )
        	{
        		sPatient += "<tr><td colspan='2' class='bold'>" + i18n_patient_identifiers + "</td></tr>";

        		jQuery( identifiers ).each( function( i, identifier )
				{
        			sPatient +="<tr class='identifierRow'>"
        				+"<td class='bold'>" + jQuery(identifier).find('name').text() + "</td>"
        				+"<td>" + jQuery(identifier).find('value').text() + "</td>	"	
        				+"</tr>";
        		});
        	}
			
        	var attributes = jQuery(patient).find('attribute');
        	if( attributes.length > 0 )
        	{
        		sPatient += "<tr><td colspan='2' class='bold'>" + i18n_patient_attributes + "</td></tr>";

        		jQuery( attributes ).each( function( i, attribute )
				{
        			sPatient +="<tr class='attributeRow'>"
        				+"<td class='bold'>" + jQuery(attribute).find('name').text() + "</td>"
        				+"<td>" + jQuery(attribute).find('value').text() + "</td>	"	
        				+"</tr>";
        		});
        	}
        	sPatient += "<tr><td colspan='2'><input type='button' id='"+ jQuery(patient).find('id').first().text() + "' value='" + i18n_edit_this_patient + "' onclick='showUpdatePatientForm(this.id)'/></td></tr>";
        	sPatient += "</table>";
		});
		
		var result = i18n_duplicate_warning;
		if( !validate )
		{
			result += "<input type='button' value='" + i18n_create_new_patient + "' onClick='removeDisabledIdentifier( );addPatient();'/>";
			result += "<br><hr style='margin:5px 0px;'>";
		}
		
		result += "<br>" + sPatient;
		jQuery('#resultSearchDiv' ).html( result );
		jQuery('#resultSearchDiv' ).dialog({
			title: i18n_duplicated_patient_list,
			maximize: true, 
			closable: true,
			modal:true,
			overlay:{background:'#000000', opacity:0.1},
			width: 800,
			height: 400
		});
}

// -----------------------------------------------------------------------------
// Show representative form
// -----------------------------------------------------------------------------

function toggleUnderAge(this_)
{
	if( jQuery(this_).is(":checked"))
	{
		jQuery('#representativeDiv').dialog('destroy').remove();
		jQuery('<div id="representativeDiv">' ).load( 'showAddRepresentative.action',{},
			function(){
				$('#patientForm [id=birthDate]').attr('id','birthDate_id');
				$('#patientForm [id=birthDate_id]').attr('name','birthDate_id');
				
				$('#patientForm [id=registrationDate]').attr('id','registrationDate_id');
				$('#patientForm [id=registrationDate_id]').attr('name','registrationDate_id');
				
				datePickerValid( 'representativeDiv [id=registrationDate]' );
			}).dialog({
			title: i18n_child_representative,
			maximize: true, 
			closable: true,
			modal:true,
			overlay:{background:'#000000', opacity:0.1},
			width: 800,
			height: 450,
			close:function()
			{
				$('#patientForm [id=birthDate_id]').attr('id','birthDate');
				$('#patientForm [id=birthDate]').attr('name','birthDate');
				
				$('#patientForm [id=registrationDate_id]').attr('id','registrationDate');
				$('#patientForm [id=registrationDate]').attr('name','registrationDate');
			}
		});
	}else
	{
		jQuery("#representativeDiv :input.idfield").each(function(){
			if( jQuery(this).is(":disabled"))
			{
				jQuery(this).removeAttr("disabled").val("");
			}
		});
		jQuery("#representativeId").val("");
		jQuery("#relationshipTypeId").val("");
	}
}

// ----------------------------------------------------------------
// Enrollment program
// ----------------------------------------------------------------

function showProgramEnrollmentForm( patientId )
{
	jQuery('#enrollmentDiv').load('showProgramEnrollmentForm.action',
		{
			id:patientId
		}).dialog({
			title: i18n_enroll_program,
			maximize: true, 
			closable: true,
			modal:true,
			overlay:{background:'#000000', opacity:0.1},
			width: 550,
			height: 450
		});
}

function programOnchange( programId )
{
	if( programId==0){
		hideById('enrollmentDateTR');
		hideById('dateOfIncidentTR');
		disable('enrollmentDateField');
		disable('dateOfIncidentField');
	}
	else{
		var type = jQuery('#enrollmentDiv [name=programId] option:selected').attr('programType')
		if(type=='2'){
			hideById('enrollmentDateTR');
			hideById('dateOfIncidentTR');
			disable('enrollmentDateField');
			disable('dateOfIncidentField');
		}
		else{
			showById('enrollmentDateTR');
			enable('enrollmentDateField');
			
			var dateOfEnrollmentDescription = jQuery('#enrollmentDiv [name=programId] option:selected').attr('dateOfEnrollmentDescription');
			var dateOfIncidentDescription = jQuery('#enrollmentDiv [name=programId] option:selected').attr('dateOfIncidentDescription');
			setInnerHTML('enrollmentDateDescription', dateOfEnrollmentDescription);
			setInnerHTML('dateOfIncidentDescription', dateOfIncidentDescription);
			
			var displayIncidentDate = jQuery('#enrollmentDiv [name=programId] option:selected').attr('displayIncidentDate');
			if( displayIncidentDate=='true'){
				showById('dateOfIncidentTR');
				enable('dateOfIncidentField');
			}
			else{
				hideById('dateOfIncidentTR');
				disable('dateOfIncidentField');
			}
		}
		var programId = jQuery('#programEnrollmentSelectDiv [id=programId] option:selected').val();
		jQuery('#identifierAndAttributeDiv').load("getPatientIdentifierAndAttribute.action", 
		{
			id:programId
		}, function(){
			showById('identifierAndAttributeDiv');
		});
	}
}

function saveSingleEnrollment(patientId, programId)
{
	jQuery.postJSON( "saveProgramEnrollment.action",
		{
			patientId: patientId,
			programId: programId,
			dateOfIncident: getCurrentDate(),
			enrollmentDate: getCurrentDate()
		}, 
		function( json ) 
		{    
			var programInstanceId = json.programInstanceId;
			var programStageInstanceId = json.activeProgramStageInstanceId;
			var programStageName = json.activeProgramStageName;
			var programInfor = getInnerHTML('infor_' + programId);
			var dueDate = json.dueDate;
			var type = jQuery('#enrollmentDiv [id=programId] option:selected').attr('programType');
			
			var activedRow = "<tr id='tr1_" + programInstanceId 
							+ "' type='" + type +"'"
							+ " programStageInstanceId='" + programStageInstanceId + "'>"
							+ " <td id='td_" + programInstanceId + "'>"
							+ " <a href='javascript:loadActiveProgramStageRecords(" + programInstanceId + "," + programStageInstanceId + ")'>"
							+ "<span id='infor_" + programInstanceId + "' class='selected bold'>" 
							+ programInfor + "</span></a></td>"
							+ "</tr>";
			
			jQuery('#tr_' + programId ).remove();
			
			jQuery('#activeTB' ).append(activedRow);
			jQuery('#enrollmentDiv').dialog("close");
			saveIdentifierAndAttribute( patientId, programId,'identifierAndAttributeDiv' );
			loadActiveProgramStageRecords( programInstanceId );
			showSuccessMessage(i18n_enrol_success);
		});
}

// ----------------------------------------------------------------
// Program enrollmment && unenrollment
// ----------------------------------------------------------------

function validateProgramEnrollment()
{	
	jQuery('#loaderDiv').show();
	$.ajax({
		type: "GET",
		url: 'validatePatientProgramEnrollment.action',
		data: getParamsForDiv('programEnrollmentSelectDiv'),
		success: function(json) {
			hideById('message');
			var type = json.response;
			if ( type == 'success' ){
				saveEnrollment();
			}
			else if ( type == 'error' ){
				setMessage( i18n_program_enrollment_failed + ':' + '\n' + message );
			}
			else if ( type == 'input' ){
				setMessage( json.message );
			}
			jQuery('#loaderDiv').hide();
      }
    });
}

function saveEnrollment()
{
	var patientId = jQuery('#enrollmentDiv [id=patientId]').val();
	var programId = jQuery('#enrollmentDiv [id=programId] option:selected').val();
	var programName = jQuery('#enrollmentDiv [id=programId] option:selected').text();
	var dateOfIncident = jQuery('#enrollmentDiv [id=dateOfIncidentField]').val();
	var enrollmentDate = jQuery('#enrollmentDiv [id=enrollmentDateField]').val();
	
	jQuery.postJSON( "saveProgramEnrollment.action",
		{
			patientId: patientId,
			programId: programId,
			dateOfIncident: dateOfIncident,
			enrollmentDate: enrollmentDate
		}, 
		function( json ) 
		{    
			var programInstanceId = json.programInstanceId;
			var programStageInstanceId = json.activeProgramStageInstanceId;
			var programStageName = json.activeProgramStageName;
			var dueDate = json.dueDate;
			var type = jQuery('#enrollmentDiv [id=programId] option:selected').attr('programType');
			
			var activedRow = "";
			if(programStageInstanceId != '')
			{
				activedRow = "<tr id='tr1_" + programInstanceId 
							+ "' type='" + type +"'"
							+ " programStageInstanceId='" + programStageInstanceId + "'>"
							+ " <td id='td_" + programInstanceId + "'>"
							+ " <a href='javascript:loadActiveProgramStageRecords(" + programInstanceId + "," + programStageInstanceId + ")'>"
							+ "<span id='infor_" + programInstanceId + "' class='selected bold'>" 
							+ programName + " (" + enrollmentDate + ")</span></a></td>"
							+ "</tr>";
			
				activedRow += "<tr id='tr2_" + programInstanceId +"'"+
							+ " onclick='javascript:loadActiveProgramStageRecords(" + programInstanceId + "," + programStageInstanceId + ")' style='cursor:pointer;'>"
							+ "<td colspan='2'><a>&#8226; " + programStageName + " (" + dueDate + ")</a></td></tr>";
			}
			else
			{
				activedRow = "<tr id='tr1_" + programInstanceId 
							+ "' type='" + type +"'>"
							+ " <td id='td_" + programInstanceId + "'>"
							+ " <a href='javascript:loadActiveProgramStageRecords(" + programInstanceId + ")'>"
							+ "<span id='infor_" + programInstanceId + "' class='selected bold'>" 
							+ programName + " (" + enrollmentDate + ")</span></a></td>"
							+ "</tr>";			
			}
			
			jQuery('#activeTB' ).prepend(activedRow);
			jQuery('#enrollmentDiv').dialog("close");
			saveIdentifierAndAttribute( patientId, programId,'identifierAndAttributeDiv' );
			loadActiveProgramStageRecords( programInstanceId );
			showSuccessMessage(i18n_enrol_success);
		});
}

function unenrollmentForm( programInstanceId )
{	
	if( confirm(i18n_incomplete_confirm_message) )
	{
		$.ajax({
			type: "POST",
			url: 'removeEnrollment.action',
			data: "programInstanceId=" + programInstanceId,
			success: function( json ) 
			{
				var completed  = "<tr onclick='javascript:loadActiveProgramStageRecords(" + programInstanceId + ");' >";
					completed += "<td><a><span id='infor_" + programInstanceId + "'>" + jQuery('#tr1_' + programInstanceId + " span" ).html() + "</span></a></td></tr>";
				jQuery('#completedTB' ).prepend( completed );
				jQuery('#tr1_' + programInstanceId ).remove();
				jQuery('#tr2_' + programInstanceId ).remove();
				
				jQuery("[id=tab-2] :input").prop('disabled', true);
				jQuery("[id=tab-3] :input").prop('disabled', true);
				jQuery("[id=tab-4] :input").prop('disabled', true);
				jQuery("[id=tab-5] :input").prop('disabled', true);
				jQuery("[id=tab-3] :input").datepicker("destroy");
				
				showSuccessMessage( i18n_unenrol_success );
			}
		});
	
	}
	
}

// ----------------------------------------------------------------
// Identifiers && Attributes for selected program
// ----------------------------------------------------------------

function saveIdentifierAndAttribute( patientId, programId, paramsDiv)
{
	var params  = getParamsForDiv(paramsDiv);
		params += "&patientId=" + patientId;
		params +="&programId=" + programId;
	$.ajax({
			type: "POST",
			url: 'savePatientIdentifierAndAttribute.action',
			data: params,
			success: function(json) 
			{
				showSuccessMessage( i18n_save_success );
			}
		});
}

// ----------------------------------------------------------------
// Show selected data-recording
// ----------------------------------------------------------------

function showSelectedDataRecoding( patientId )
{
	showLoader();
	hideById('searchDiv');
	hideById('dataEntryFormDiv');
	hideById('migrationPatientDiv');
	hideById('dataRecordingSelectDiv');
	
	jQuery('#dataRecordingSelectDiv').load( 'selectDataRecording.action', 
		{
			patientId: patientId
		},
		function()
		{
			jQuery('#dataRecordingSelectDiv [id=backBtnFromEntry]').hide();
			showById('dataRecordingSelectDiv');
			
			var programId = jQuery('#programEnrollmentSelectDiv [id=programId] option:selected').val();
			$('#dataRecordingSelectDiv [id=programId]').val( programId );
			$('#dataRecordingSelectDiv [id=inputCriteria]').hide();
			showById('dataRecordingSelectDiv');
			hideLoader();
			hideById('contentDiv');
		});
}

// ----------------------------------------------------------------
// Patient Location
// ----------------------------------------------------------------

function getPatientLocation( patientId )
{
	hideById('listPatientDiv');
	hideById('selectDiv');
	hideById('searchDiv');
	setInnerHTML('patientDashboard','');
				
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

function registerPatientLocation( patientId )
{
	$.getJSON( 'registerPatientLocation.action',{ patientId:patientId }
		, function( json ) 
		{
			showPatientDashboardForm(patientId);
			showSuccessMessage( i18n_save_success );
		} );
}

// ----------------------------------------------------------------
// List program-stage-instance of selected program
// ----------------------------------------------------------------

function getVisitSchedule( programInstanceId )
{
	$('#tab-3').load("getVisitSchedule.action", {programInstanceId:programInstanceId});
}


// ----------------------------------------------------------------
// Dash board
// ----------------------------------------------------------------

function showPatientDashboardForm( patientId )
{
	hideById('selectDiv');
	hideById('searchDiv');
	hideById('listPatientDiv');
	hideById('editPatientDiv');
	hideById('listRelationshipDiv');
	hideById('addRelationshipDiv');
	hideById('migrationPatientDiv');
	hideById('smsManagementDiv');
	hideById('dataEntryFormDiv');
	
	setInnerHTML('listEventDiv','');
				
	jQuery('#loaderDiv').show();
	jQuery('#patientDashboard').load('patientDashboard.action',
		{
			patientId:patientId
		}, function()
		{	
			setInnerHTML('mainFormLink', i18n_main_form_link);
			jQuery('#activeTB tr:first').click();
			showById('patientDashboard');
			jQuery('#loaderDiv').hide();
		});
}

function activeProgramInstanceDiv( programInstanceId )
{
	jQuery(".selected").each(function(){
		jQuery(this).removeClass();
	});
	
	jQuery("#infor_" + programInstanceId).each(function(){
		jQuery(this).addClass('selected bold');
	});
	
	showById('pi_' + programInstanceId);
}

function hideProgramInstanceDiv( programInstanceId )
{
	hideById('pi_' + programInstanceId);
	jQuery('#pi_' + programInstanceId).removeClass("link-area-active");
	jQuery("#img_" + programInstanceId).attr('src','');
}

function loadActiveProgramStageRecords(programInstanceId, activeProgramStageInstanceId)
{
	hideById('programEnrollmentDiv');
	if( programInstanceId == "") return;
	jQuery('#loaderDiv').show();
	jQuery('#programEnrollmentDiv').load('enrollmentform.action',
		{
			programInstanceId:programInstanceId
		}, function()
		{
			showById('programEnrollmentDiv');
			var hasDataEntry = getFieldValue('hasDataEntry');
			var type = jQuery('#tb_'+programInstanceId).attr('programType');
			if(type=='2'){
				hideById('colorHelpLink');
				hideById('programInstanceDiv');
				if( hasDataEntry=='true' || hasDataEntry==undefined ){
					var programStageInstanceId = jQuery('.stage-object').attr('id').split('_')[1];
					loadDataEntry( programStageInstanceId );
				}
			}
			else{
				showById('programInstanceDiv');
				activeProgramInstanceDiv( programInstanceId );
				if( activeProgramStageInstanceId != undefined 
					&& ( hasDataEntry=='true' || hasDataEntry==undefined ))
				{
					loadDataEntry( activeProgramStageInstanceId );
				}
			}
			
			if( activeProgramStageInstanceId != undefined )
			{
				jQuery('#completedList').val('');
			}
			jQuery('#loaderDiv').hide();
		});
}

function loadProgramStageRecords( programStageInstanceId, completed ) 
{
	showLoader();
    jQuery('#dataEntryFormDiv').load( "viewProgramStageRecords.action",
		{
			programStageInstanceId: programStageInstanceId
		}, function() {
			if(completed){
				jQuery( "#dataEntryFormDiv :input").each(function(){
					disable(this.id);
				});
			}
			showById('dataEntryFormDiv');
			hideLoader();
		});
}

function updateEnrollment( patientId, programId, programInstanceId, programName )
{
	var dateOfIncident = jQuery('#tab-3 [id=dateOfIncident]').val();
	var enrollmentDate = jQuery('#tab-3 [id=enrollmentDate]').val();
	
	jQuery.postJSON( "saveProgramEnrollment.action",
		{
			patientId: getFieldValue('patientId'),
			programId: programId,
			dateOfIncident: dateOfIncident,
			enrollmentDate: enrollmentDate
		}, 
		function( json ) 
		{    
			var infor = programName + " (" + enrollmentDate + ")";
			setInnerHTML("infor_" + programInstanceId, infor );
			showSuccessMessage(i18n_enrol_success);
		});
}

// load program instance history
function programReports( programInstanceId )
{
	$('#programReportDiv').load("getProgramReportHistory.action", {programInstanceId:programInstanceId});
}

// export program instance history
function exportProgramReports( programInstanceId, type )
{
	window.location.href='getProgramReportHistory.action?programInstanceId=' + programInstanceId + "&type=" + type;
}

// load SMS message and comments
function getEventMessages( programInstanceId )
{
	$('#eventMessagesDiv').load("getEventMessages.action", {programInstanceId:programInstanceId});
}

function dashboardHistoryToggle(evt)
{
	jQuery('#dashboardHistoryDiv').toggle();
}

function viewPersonProgram ( displayedDiv, hidedDiv )
{
	showById(displayedDiv);
	hideById(hidedDiv);
}

// --------------------------------------------------------------------
// Comment && Message
// --------------------------------------------------------------------

function sendSmsOnePatient( field, programStageInstanceId )
{
	setInnerHTML('smsError', '');
	if(field.value==""){
		field.style.backgroundColor = ERROR_COLOR;
		jQuery('#' + field.id).attr("placeholder", i18n_this_field_is_required);
		return;
	}
	
	field.style.backgroundColor = SAVING_COLOR;
	jQuery.postUTF8( 'sendSMS.action',
		{
			programStageInstanceId: programStageInstanceId,
			msg: field.value
		}, function ( json )
		{
			if ( json.response == "success" ) {
				jQuery('#smsError').css("color", "green");
				setInnerHTML('smsError', json.message);
				var date = new Date();
				var currentTime = date.getHours() + ":" + date.getMinutes();
				jQuery('[name=commentTB]').prepend("<tr><td>" + getFieldValue('currentDate') + " " + currentTime + "</td>"
					+ "<td>" + getFieldValue('programStageName') + "</td>"
					+ "<td>" + getFieldValue('currentUsername') + "</td>"
					+ "<td>" + field.value + "</td></tr>");
				field.value="";
				field.style.backgroundColor = SUCCESS_COLOR;
			}
			else {
				showSuccessMessage( json.message );
				jQuery('#smsError').css("color", "red");
				setInnerHTML('smsError', json.message);
				field.style.backgroundColor = ERROR_COLOR;
			}
			
			if( jQuery("#commentTB tr.hidden").length > 0 ){
				commentDivToggle(true);
			}
			else{
				commentDivToggle(false);
			}
		});
}

function keypressOnComent(event, field, programStageInstanceId )
{
	var key = getKeyCode( event );
	if ( key==13 ){ // Enter
		addComment( field, programStageInstanceId );
	}
}

function addComment( field, programStageInstanceId )
{
	field.style.backgroundColor = SAVING_COLOR;
	var commentText = field.value;
	if( commentText == ''){
		field.style.backgroundColor = ERROR_COLOR;
		jQuery('#' + field.id).attr("placeholder", i18n_this_field_is_required);
		return;
	}
	
	jQuery.postUTF8( 'addPatientComment.action',
		{
			programStageInstanceId: programStageInstanceId,
			commentText: commentText 
		}, function ( json )
		{
			var programStageName = jQuery("#ps_" + programStageInstanceId).attr('programStageName');
			var date = new Date();
			var currentTime = date.getHours() + ":" + date.getMinutes();
			var content = "<tr><td>" + getCurrentDate("currentDate") + " " + currentTime + "</td>"
			if(programStageName!=undefined)
			{
				content += "<td>" + programStageName + "</td>"
			}
			content += "<td>" + getFieldValue('currentUsername') + "</td>"
			content += "<td>" + commentText + "</td></tr>";
			jQuery('#commentTB').prepend(content);
			field.value="";
			showSuccessMessage( i18n_comment_added );
			field.style.backgroundColor = SUCCESS_COLOR;
			
			if( jQuery("#commentTB tr").length > 5 ){
				commentDivToggle(true);
			}
			else{
				commentDivToggle(false);
			}
		});
}

function removeComment( programStageInstanceId, commentId )
{
	jQuery.postUTF8( 'removePatientComment.action',
		{
			programStageInstanceId:programStageInstanceId,
			id: commentId
		}, function ( json )
		{
			showSuccessMessage( json.message );
			hideById( 'comment_' + commentId );
		} );
}

function commentDivToggle(isHide)
{
	jQuery("#commentReportTB tr").removeClass("hidden");
	jQuery("#commentReportTB tr").each( function(index, item){
		if(isHide && index > 4){
			jQuery(item).addClass("hidden");
		}
		else if(!isHide){		
			jQuery(item).removeClass("hidden");
		}
		index++;
	});
	
	if(jQuery("#commentReportTB tr").length <= 5 )
	{
		hideById('showCommentBtn');
		hideById('hideCommentBtn');
	}
	else if( isHide ){
		showById('showCommentBtn');
		hideById('hideCommentBtn');
	}
	else
	{
		hideById('showCommentBtn');
		showById('hideCommentBtn');
	}
}

function backPreviousPage( patientId )
{
	if(isDashboard){
		showPatientDashboardForm( patientId )
	}
	else{
		loadPatientList();
	}
}

// ----------------------------------------------
// Data entry section
// ----------------------------------------------

function filterInSection( $this )
{
    var $tbody = $this.parent().parent().parent().parent().parent().find("tbody");
	var $trTarget = $tbody.find("tr");

    if ( $this.val() == '' )
    {
        $trTarget.show();
    }
    else 
    {
        var $trTargetChildren = $trTarget.find( 'td:first-child' );

        $trTargetChildren.each( function( idx, item ) 
        {
			if( $( item ).find( 'span' ).length != 0 )
			{
				var text1 = $this.val().toUpperCase();
				var text2 = $( item ).find( 'span' ).html().toUpperCase();

				if ( text2.indexOf( text1 ) >= 0 )
				{
					$( item ).parent().show();
				}
				else
				{
					$( item ).parent().hide();
				}
			}
		} );
    }
	
    refreshZebraStripes( $tbody );
}

function refreshZebraStripes( $tbody )
{
     $tbody.find( 'tr:visible:even' ).removeClass( 'listRow' ).removeClass( 'listAlternateRow' ).addClass( 'listRow' );
     $tbody.find( 'tr:visible:odd' ).removeClass( 'listRow' ).removeClass( 'listAlternateRow' ).addClass( 'listAlternateRow' );
}