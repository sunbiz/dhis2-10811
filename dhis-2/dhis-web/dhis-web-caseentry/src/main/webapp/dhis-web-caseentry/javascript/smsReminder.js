
function orgunitSelected( orgUnits, orgUnitNames )
{
	showById('mainLinkLbl');
	showById('searchDiv');
	hideById('listEventDiv');
	hideById('listEventDiv');
	hideById('patientDashboard');
	hideById('smsManagementDiv');
	hideById('sendSmsFormDiv');
	hideById('editPatientDiv');
	hideById('resultSearchDiv');
	hideById('enrollmentDiv');
	hideById('listRelationshipDiv');
	hideById('addRelationshipDiv');
	hideById('migrationPatientDiv');

	clearListById('programIdAddPatient');
	$('#contentDataRecord').html('');
	setFieldValue('orgunitName', orgUnitNames[0]);
	setFieldValue('orgunitId', orgUnits[0]);
	jQuery.get("getPrograms.action",{}, 
		function(json)
		{
			jQuery( '#programIdAddPatient').append( '<option value="">' + i18n_please_select + '</option>' );
			for ( i in json.programs ) {
				if(json.programs[i].type==1){
					jQuery( '#programIdAddPatient').append( '<option value="' + json.programs[i].id +'" type="' + json.programs[i].type + '">' + json.programs[i].name + '</option>' );
				}
			}
			enableBtn();
		});
}

selection.setListenerFunction( orgunitSelected );

// --------------------------------------------------------------------
// List all events
// --------------------------------------------------------------------

function listAllPatient()
{
	setFieldValue('listAll', "true");
	hideById('listEventDiv');
	hideById('advanced-search');
	setFieldValue('statusEvent', "4");
	contentDiv = 'listEventDiv';
	$('#contentDataRecord').html('');
	hideById('advanced-search');
	eventList = 1;
	
	var date = new Date();
	var d = date.getDate() - 1;
	var m = date.getMonth();
	var y1 = date.getFullYear() - 100;
	var y2 = date.getFullYear();
	var startDate = jQuery.datepicker.formatDate( dateFormat, new Date(y1, m, d) );
	var endDate = jQuery.datepicker.formatDate( dateFormat, new Date(y2, m, d) );
	
	var programId = getFieldValue('programIdAddPatient');
	var searchTexts = "stat_" + programId + "_" 
				+ startDate + "_" + endDate + "_" 
				+ getFieldValue('orgunitId') + "_false_4";
	
	showLoader();
	jQuery('#listEventDiv').load('getSMSPatientRecords.action',
		{
			programId:programId,
			listAll:false,
			searchBySelectedOrgunit: false,
			searchTexts: searchTexts
		}, 
		function()
		{
			setInnerHTML('searchInforLbl',i18n_list_all_patients);
			showById('colorHelpLink');
			showById('listEventDiv');
			resize();
			hideLoader();
		});
}

// --------------------------------------------------------------------
// Search events
// --------------------------------------------------------------------

function advancedSearch( params )
{
	setFieldValue('listAll', "false");
	$('#contentDataRecord').html('');
	$('#listEventDiv').html('');
	hideById('listEventDiv');
	showLoader();
	params += "&programId=" + getFieldValue('programIdAddPatient');
	$.ajax({
		url: 'getSMSPatientRecords.action',
		type:"POST",
		data: params,
		success: function( html ){
			jQuery('#listEventDiv').html(html);
			showById('colorHelpLink');
			showById('listEventDiv');
			eventList = 2;
			resize();
			hideLoader();
		}
	});
}

// --------------------------------------------------------------------
// program tracking form
// --------------------------------------------------------------------

function programTrackingList( programStageInstanceId, isSendSMS ) 
{
	setFieldValue('sendToList', "false");
	$('#smsManagementDiv' ).load("programTrackingList.action",
		{
			programStageInstanceId: programStageInstanceId
		}
		, function(){
			hideById('mainLinkLbl');
			hideById('mainFormLink');
			hideById('searchDiv');
			hideById('listEventDiv');
			showById('smsManagementDiv');
		});
}

function eventFlowToggle( programInstanceId )
{
	jQuery("#tb_" + programInstanceId + " .stage-object").each( function(){
		var programStageInstance = this.id.split('_')[1];
		jQuery('#arrow_' + programStageInstance ).toggle();
		jQuery('#ps_' + programStageInstance ).toggle();
		jQuery(this).removeClass("stage-object-selected");
	});
		
	if( jQuery("#tb_" + programInstanceId + " .searched").length>0)
	{	
		var id = jQuery("#tb_" + programInstanceId + " .searched").attr('id').split('_')[1];
		showById("arrow_" + id);
		showById("ps_" + id );
	}
	resize();
}

// --------------------------------------------------------------------
// Send SMS 
// --------------------------------------------------------------------

function showSendSmsForm()
{
	jQuery('#sendSmsToListForm').dialog({
			title: i18n_send_message,
			maximize: true, 
			closable: true,
			modal:true,
			overlay:{background:'#000000', opacity:0.1},
			width: 420,
			height: 200
		});
}

function sendSmsToList()
{
	params = getSearchParams();
	params += "&msg=" + getFieldValue( 'smsMessage' );
	params += "&programStageInstanceId=" + getFieldValue('programStageInstanceId');
	$.ajax({
		url: 'sendSMSTotList.action',
		type:"POST",
		data: params,
		success: function( json ){
			if ( json.response == "success" ) {
				var programStageName = getFieldValue('programStageName');
				var currentTime = date.getHours() + ":" + date.getMinutes();
				jQuery('#commentTB').prepend("<tr><td>" + getFieldValue("currentDate") + " " + currentTime + "</td>"
						+ "<td>" + programStageName + "</td>"
						+ "<td>" + getFieldValue( 'smsMessage' ) + "</td></tr>");
				showSuccessMessage( json.message );
			}
			else {
				showErrorMessage( json.message );
			}
			jQuery('#sendSmsFormDiv').dialog('close')
		}
	});
}

// --------------------------------------------------------------------
// Post Comments/Send Message
// --------------------------------------------------------------------

function keypressOnMessage(event, field, programStageInstanceId )
{
	var key = getKeyCode( event );
	if ( key==13 ){ // Enter
		sendSmsOnePatient( field, programStageInstanceId );
	}
}

// --------------------------------------------------------------------
// Dashboard
// --------------------------------------------------------------------

function loadDataEntry( programStageInstanceId )
{
	setInnerHTML('dataEntryFormDiv', '');
	showById('dataEntryFormDiv');
	showById('executionDateTB');
	setFieldValue( 'dueDate', '' );
	setFieldValue( 'executionDate', '' );
	disable('validationBtn');
	disableCompletedButton(true);
	disable('uncompleteBtn');
	
	$('#executionDate').unbind("change");
	$('#executionDate').change(function() {
		saveExecutionDate( getFieldValue('programId'), programStageInstanceId, byId('executionDate') );
	});
	
	jQuery(".stage-object-selected").removeClass('stage-object-selected');
	var selectedProgramStageInstance = jQuery( '#' + prefixId + programStageInstanceId );
	selectedProgramStageInstance.addClass('stage-object-selected');
	setFieldValue( 'programStageId', selectedProgramStageInstance.attr('psid') );
	
	showLoader();	
	$( '#dataEntryFormDiv' ).load( "dataentryform.action", 
		{ 
			programStageInstanceId: programStageInstanceId
		},function()
		{
			var executionDate = jQuery('#executionDate').val();
			var completed = jQuery('#entryFormContainer input[id=completed]').val();
			var irregular = jQuery('#entryFormContainer input[id=irregular]').val();
			var reportDateDes = jQuery("#ps_" + programStageInstanceId).attr("reportDateDes");
			setInnerHTML('reportDateDescriptionField',reportDateDes);
			enable('validationBtn');
			if( executionDate == '' )
			{
				disable('validationBtn');
			}
			else if( executionDate != '' && completed == 'false' )
			{
				disableCompletedButton(false);
			}
			else if( completed == 'true' )
			{
				disableCompletedButton(true);
			}
			resize();
			hideLoader();
			hideById('contentDiv'); 
			jQuery('#dueDate').focus();
		});
}

function entryFormContainerOnReady(){}

// --------------------------------------------------------------------
// Cosmetic UI
// --------------------------------------------------------------------

function reloadRecordList()
{
	var listAll = getFieldValue('listAll');
	var startDate = getFieldValue('startDueDate');
	var endDate = getFieldValue('endDueDate');
	var statusEvent = getFieldValue('statusEvent');
	if( listAll == 'true' )
	{
		var date = new Date();
		var d = date.getDate() - 1;
		var m = date.getMonth();
		var y1 = date.getFullYear() - 100;
		var y2 = date.getFullYear();
		startDate = jQuery.datepicker.formatDate( dateFormat, new Date(y1, m, d) );
		endDate = jQuery.datepicker.formatDate( dateFormat, new Date(y2, m, d) );
		status = 4;
	}
	
	var paddingIndex = 1;
	jQuery("#patientList .stage-object").each( function(){
		var id = this.id.split('_')[1];
		var dueDate = jQuery(this).attr('dueDate');
		var status = jQuery(this).attr('status');
		var programInstanceId = jQuery(this).attr('programInstanceId');
		if( dueDate >= startDate && dueDate <= endDate && statusEvent == status )
		{
			if( jQuery("#tb_" + programInstanceId + " .searched").length > 0 ){
				hideById('arrow_' + id );
				hideById("ps_" + id );
			}
			else
			{
				jQuery("#arrow_" + id ).addClass("displayed");
				var index = eval(jQuery("#ps_" + id ).attr("index"));
				if( paddingIndex < index ){
					 paddingIndex = index;
				}
			}
			jQuery("#ps_" + id ).addClass("stage-object-selected searched");
		}
		else
		{
			hideById('arrow_' + id );
			hideById('ps_' + id );
		}
	});
	
	jQuery("[id^=arrow_].displayed" ).each( function(){
		var id = this.id.split('_')[1];
		var index = eval(jQuery("#ps_" + id ).attr("index"));
		if( index<paddingIndex){
			var paddingLeft = ( paddingIndex - index ) * 20;
			jQuery(this).css("padding-left", paddingLeft + "px");
		}
	});
}

function reloadOneRecord( programInstanceId )
{
	if(jQuery("#tb_" + programInstanceId + " .searched").length == 0 ){
		var total = eval(getInnerHTML('totalTd')) - 1;
		setInnerHTML('totalTd', total)
		hideById("event_" + programInstanceId );
	}
	else
	{
		var firstSearched = jQuery("#tb_" + programInstanceId + " .searched:first");
		firstSearched.show();
		jQuery("#tb_" + programInstanceId + " [name^=arrow_]").show();
	}
}

// --------------------------------------------------------------------
// Show main form
// --------------------------------------------------------------------

function onClickBackBtn()
{
	showById('mainLinkLbl');
	showById('searchDiv');
	showById('listEventDiv');
	hideById('migrationPatientDiv');
	hideById('smsManagementDiv');
	hideById('patientDashboard');
	
	if( eventList == 1){
		listAllPatient();
	}
	else if( eventList == 2){
		validateAdvancedSearch();
	}
}
