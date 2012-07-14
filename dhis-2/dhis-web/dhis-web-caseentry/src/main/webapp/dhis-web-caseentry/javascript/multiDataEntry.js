isAjax = true;

function multiDataEntryOrgunitSelected( orgUnits, orgUnitNames )
{
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
		});
}

selection.setListenerFunction( multiDataEntryOrgunitSelected );

function listAllPatient()
{
	hideById('listPatientDiv');
	hideById('advanced-search');
	
	contentDiv = 'listPatientDiv';
	$('#contentDataRecord').html('');
	showLoader();
	jQuery('#listPatientDiv').load('getDataRecords.action',
		{
			programId:getFieldValue('programIdAddPatient'),
			listAll:true
		}, 
		function()
		{
			showById('colorHelpLink');
			showById('listPatientDiv');
			resize();
			hideLoader();
		});
}

function advancedSearch( params )
{
	$('#contentDataRecord').html('');
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

function loadDataEntry( programStageInstanceId ) 
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
