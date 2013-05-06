isAjax = true;

function multiDataEntryOrgunitSelected( orgUnits, orgUnitNames )
{
	var width = jQuery('#programIdAddPatient').width();
	jQuery('#programIdAddPatient').width(width-30);
	showById( "programLoader" );
	disable('programIdAddPatient');
	setFieldValue('orgunitName', orgUnitNames[0]);
	setFieldValue('orgunitId', orgUnits[0]);
	hideById("listPatientDiv");
	clearListById('programIdAddPatient');
	$('#contentDataRecord').html('');
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
			hideById('programLoader');
			jQuery('#programIdAddPatient').width(width);
			enable('programIdAddPatient');
		});
}

selection.setListenerFunction( multiDataEntryOrgunitSelected );

function listAllPatient()
{
	var scheduledVisitDays = getFieldValue('scheduledVisitDays');
	if( scheduledVisitDays != '' )
	{
		var today = getCurrentDate();
		var date = new Date();
		var d = date.getDate();
		var m = date.getMonth();
		var y= date.getFullYear();
		var lastDays = jQuery.datepicker.formatDate( dateFormat, new Date(y, m, d - eval(scheduledVisitDays)) ) ;
	
		var searchTexts = "stat_" + getFieldValue('programIdAddPatient') + "_" 
					+ lastDays + "_" + today + "_" 
					+ getFieldValue('orgunitId') + "_false_4_3";
					
		getPatientList(searchTexts);
	}
}

function getPatientList(searchTexts)
{
	hideById('listPatientDiv');
	hideById('advanced-search');
	hideById('contentDataRecord');
	contentDiv = 'listPatientDiv';
	setFieldValue('statusEvent', "4");
	var startDate = jQuery.datepicker.formatDate( dateFormat, new Date() );
	var endDate = jQuery.datepicker.formatDate( dateFormat, new Date() );
	var programId = getFieldValue('programIdAddPatient');
	
	showLoader();
	jQuery('#listPatientDiv').load('getDataRecords.action',
		{
			programId:programId,
			listAll:false,
			searchTexts: searchTexts
		}, 
		function()
		{
			setInnerHTML('searchInforLbl',i18n_list_all_patients);
			showById('listPatientDiv');
			setTableStyles();
			hideLoader();
		});
}

// --------------------------------------------------------------------
// Search events
// --------------------------------------------------------------------

function advancedSearch( params )
{
	hideById('contentDataRecord');
	hideById('listPatientDiv');
	showLoader();
	params += "&programId=" + getFieldValue('programIdAddPatient');
	$.ajax({
		url: 'getDataRecords.action',
		type:"POST",
		data: params,
		success: function( html ){
			setTableStyles();
			jQuery('#listPatientDiv').html(html);
			showById('listPatientDiv');
			hideLoader();
		}
	});
}


function advancedSearch( params )
{
	params += "&searchTexts=prg_" + getFieldValue('programIdAddPatient');
	params += "&programId=" + getFieldValue('programIdAddPatient');
	$.ajax({
		url: 'getDataRecords.action',
		type:"POST",
		data: params,
		success: function( html ){
			jQuery('#listPatientDiv').html(html);
			showById('colorHelpLink');
			showById('listPatientDiv');
			hideLoader();
		}
	});
}

function loadDataEntryDialog( programStageInstanceId ) 
{
	jQuery("#patientList input[name='programStageBtn']").each(function(i,item){
		jQuery(item).removeClass('stage-object-selected');
	});
	jQuery( '#' + prefixId + programStageInstanceId ).addClass('stage-object-selected');
	
	$('#contentDataRecord' ).load("viewProgramStageRecords.action",
		{
			programStageInstanceId: programStageInstanceId
		},function()
		{
			setFieldValue( 'programStageInstanceId', programStageInstanceId );
			showById('patientInforTB');
			showById('postCommentTbl');
		}).dialog(
		{
			title:i18n_program_stage,
			maximize:true, 
			closable:true,
			modal:false,
			overlay:{background:'#000000', opacity:0.1},
			width:850,
			height:500
		});
}

function loadProgramStageRecords( programStageInstanceId ) 
{
	setInnerHTML('dataEntryFormDiv', '');
	showLoader();
    $('#dataEntryFormDiv' ).load("loadProgramStageRecords.action",
		{
			programStageInstanceId: programStageInstanceId
		}, function() {
			hideLoader();
		});
}
