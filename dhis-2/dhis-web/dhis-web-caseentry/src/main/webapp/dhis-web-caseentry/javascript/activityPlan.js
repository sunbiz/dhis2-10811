
function orgunitSelected( orgUnits, orgUnitNames )
{
	hideById("listPatientDiv");
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
		});
}

selection.setListenerFunction( orgunitSelected );

function displayCadendar()
{
	if( byId('useCalendar').checked )
	{
		hideById('showEventSince');
		hideById('showEventUpTo');
		showById('startDueDate');
		showById('endDueDate');
		datePickerInRange( 'startDueDate' , 'endDueDate' );
	}
	else
	{
		showById('showEventSince');
		showById('showEventUpTo');
		hideById('startDueDate');
		hideById('endDueDate');
		jQuery('#startDueDate').datepicker("destroy");
		jQuery('#endDueDate').datepicker("destroy");
	}
}

function showActitityList()
{
	setFieldValue('listAll', "true");
	hideById('listPatientDiv');
	contentDiv = 'listPatientDiv';
	$('#contentDataRecord').html('');
	var programId = getFieldValue('programIdAddPatient');
	var searchTexts = "stat_" + programId
					+ "_" + getFieldValue('startDueDate')
					+ "_" + getFieldValue('endDueDate')
					+ "_" + getFieldValue('orgunitId')
					+ "_false"
					+ "_" + getFieldValue('statusEvent');
	
	showLoader();
	jQuery('#listPatientDiv').load('getActivityPlanRecords.action',
		{
			programId:programId,
			listAll:false,
			searchBySelectedOrgunit: false,
			searchTexts: searchTexts
		}, 
		function()
		{
			showById('colorHelpLink');
			showById('listPatientDiv');
			resize();
			hideLoader();
		});
}

function exportActitityList( type )
{
	var programId = getFieldValue('programIdAddPatient');
	var searchTexts = "stat_" + programId
					+ "_" + getFieldValue('startDueDate')
					+ "_" + getFieldValue('endDueDate')
					+ "_" + getFieldValue('orgunitId')
					+ "_false"
					+ "_" + getFieldValue('statusEvent');
	var params = "searchTexts=" + searchTexts;
		params += "&listAll=fase";
		params += "&type=" + type;
		params += "&programId=" + getFieldValue('programIdAddPatient');
		params += "&searchBySelectedOrgunit=false";
	
	var url = "exportActitityList.action?" + params;
	window.location.href = url;
}

// --------------------------------------------------------------------
// Patient program tracking
// --------------------------------------------------------------------

function loadDataEntry( programStageInstanceId ) 
{
	jQuery("#patientList input[name='programStageBtn']").each(function(i,item){
		jQuery(item).removeClass('stage-object-selected');
	});
	jQuery( '#' + prefixId + programStageInstanceId ).addClass('stage-object-selected');
	setFieldValue('programStageInstanceId', programStageInstanceId);
	
	$('#contentDataRecord' ).load("viewProgramStageRecords.action",
		{
			programStageInstanceId: programStageInstanceId
		}, function(){
			showById('patientInforTB');
			showById('postCommentTbl');
			showById('entryForm');
			showById('inputCriteriaDiv');
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

function statusEventOnChange()
{
	if( !byId('useCalendar').checked )
	{
		var statusEvent = getFieldValue("statusEvent");
		
		if( statusEvent == '1_2_3_4' 
			|| statusEvent == '3_4' 
			|| statusEvent == '2_3_4' ){
			enable('showEventSince');
			enable('showEventUpTo');
			setDateRange();
		}
		else if( statusEvent == '3' ){
			disable('showEventSince');
			enable('showEventUpTo');
			setDateRange();
		}
		else{
			enable('showEventSince');
			disable('showEventUpTo');
			setDateRange();
		}
	}
}

function setDateRange()
{
	var statusEvent = getFieldValue("statusEvent");
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y= date.getFullYear();
	
	var startDateSince = "";
	var endDateSince = "";
	var startDateUpTo = "";
	var endDateUpTo = "";
	var startDate = "";
	var endDate = "";
		
	// Get dateRangeSince
	var days = getFieldValue('showEventSince');
	if( days == 'ALL'){
		startDateSince = jQuery.datepicker.formatDate( dateFormat, new Date(y-100, m, d) ) ;
	}
	else{
		startDateSince = jQuery.datepicker.formatDate( dateFormat, new Date(y, m, d + eval(days)) ) ;
	}
	endDateSince = jQuery.datepicker.formatDate( dateFormat, new Date() );
	
	// getDateRangeUpTo
	days = getFieldValue('showEventUpTo');
	startDateUpTo = jQuery.datepicker.formatDate( dateFormat, new Date() );
	endDateUpTo = "";
	if( days == 'ALL'){
		endDateUpTo = jQuery.datepicker.formatDate( dateFormat, new Date(y+100, m, d) ) ;
	}
	else{
		endDateUpTo = jQuery.datepicker.formatDate( dateFormat, new Date(y, m, d + eval(days)) ) ;
	}

	// check status to get date-range
	if( statusEvent == '1_2_3_4' 
		|| statusEvent == '3_4' 
		|| statusEvent == '2_3_4')
	{
		startDate = startDateSince;
		endDate = endDateUpTo;
	
	}else if (statusEvent=='3'){
		startDate = startDateUpTo;
		endDate = endDateUpTo;
	}
	else
	{
		startDate = startDateSince;
		endDate = endDateSince;
	}
	
	jQuery("#startDueDate").val(startDate);
	jQuery("#endDueDate").val(endDate);
}

function setDateRangeUpTo( days )
{
	if(days == "")
		return;
		
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y= date.getFullYear();
	
	var startDate = jQuery.datepicker.formatDate( dateFormat, new Date() );
	var endDate = "";
	if( days == 'ALL'){
		endDate = jQuery.datepicker.formatDate( dateFormat, new Date(y+100, m, d) ) ;
	}
	else{
		d = d + eval(days);
		endDate = jQuery.datepicker.formatDate( dateFormat, new Date(y, m, d) ) ;
	}
	
	jQuery("#startDueDate").val(startDate);
	jQuery("#endDueDate").val(endDate);
}

function setDateRangeAll()
{
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y= date.getFullYear();
}

// --------------------------------------------------------------------
// Cosmetic UI
// --------------------------------------------------------------------

function reloadRecordList()
{
	var startDate = getFieldValue('startDueDate');
	var endDate = getFieldValue('endDueDate');
	var arrStatus = getFieldValue('statusEvent').split('_');
	var paddingIndex = 1;
	
	jQuery("#patientList .stage-object").each( function(){
		var id = this.id.split('_')[1];
		var dueDate = jQuery(this).attr('dueDate');
		var statusEvent = jQuery(this).attr('status');
		var programInstanceId = jQuery(this).attr('programInstanceId');
		if( dueDate >= startDate && dueDate <= endDate 
			&& jQuery.inArray(statusEvent, arrStatus) > -1)
		{
			if( jQuery("#tb_" + programInstanceId + " .searched").length == 0 )
			{
				jQuery("#arrow_" + id ).addClass("displayed");
				var index = eval(jQuery("#ps_" + id ).attr("index"));
				if( paddingIndex < index ){
					 paddingIndex = index;
				}
			}
			jQuery("#ps_" + id ).addClass("stage-object-selected searched");
		}
		hideById('arrow_' + id );
		hideById('ps_' + id );
	});
	
	jQuery(".table-flow").each( function(){
		var scheduledEvent = jQuery(this).find("[status='3']:first");
		scheduledEvent.addClass("stage-scheduled");
		scheduledEvent.css('border-color', MARKED_VISIT_COLOR);
		scheduledEvent.focus();
		
		var firstEvent = jQuery(this).find(".searched:first");
		firstEvent.show();
		var id = firstEvent.attr("id").split('_')[1];
		showById('arrow_' + id );
		var index = firstEvent.attr("index");
		if( index<paddingIndex){
			var paddingLeft = ( paddingIndex - index ) * 20;
			jQuery('#arrow_' + id).css("padding-left", paddingLeft + "px");
		}
	});
	
	resize();
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
	
	jQuery("#tb_" + programInstanceId + " .table-flow").each( function(){
		var scheduledEvent = jQuery(this).find("[status='3']:first");
		scheduledEvent.focus();
	});
	
	resize();
}
