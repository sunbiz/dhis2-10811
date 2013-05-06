
function getStageByProgram( programId )
{
	window.location.href = "programStage.action?id=" + programId;
}

function addProgramStage()
{
	var programId = document.getElementById('id').value;

	if( programId == "null"  || programId == "" )
	{
		showWarningMessage( i18n_please_select_program );
	}
	else
	{
		window.location.href="showAddProgramStageForm.action?id=" + programId;
	}
}

function sortProgramStages()
{
	var programId = getFieldValue('id');
	if( programId == "null"  || programId == "" )
	{
		showWarningMessage( i18n_please_select_program );
	}
	else
	{
		jQuery.getJSON( 'saveProgramStageSortOder.action', { id: programId }, 
			function ( json ) {
				showSuccessMessage( i18n_success );
				loadProgramStageList( programId );
			});
	}
}

function loadProgramStageList( programId )
{
	jQuery('#programStageListDiv').load('programStageList.action',{
			id: programId
		},
		function( html ){
			setInnerHTML('programStageListDiv', html );
		});
}

// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showProgramStageDetails( programStageId )
{
	jQuery.getJSON( 'getProgramStage.action', { id: programStageId }, function ( json ) {
		setInnerHTML( 'nameField', json.programStage.name );	
		setInnerHTML( 'descriptionField', json.programStage.description );
		setInnerHTML( 'scheduledDaysFromStartField', json.programStage.minDaysFromStart ); 

		var irregular = (json.programStage.irregular=='true') ? i18n_yes : i18n_no;
		setInnerHTML( 'irregularField', irregular );  
		
		var autoGenerateEvent = (json.programStage.autoGenerateEvent=='true') ? i18n_yes : i18n_no;
		setInnerHTML( 'autoGenerateEventField', autoGenerateEvent );  
		
		var displayGenerateEventBox = (json.programStage.displayGenerateEventBox=='true') ? i18n_yes : i18n_no;
		setInnerHTML( 'displayGenerateEventBoxField', displayGenerateEventBox );  
		
		var validCompleteOnly = (json.programStage.validCompleteOnly=='true') ? i18n_yes : i18n_no;
		setInnerHTML( 'validCompleteOnlyField', validCompleteOnly );  
		
		var captureCoordinates = (json.programStage.captureCoordinates=='true') ? i18n_yes : i18n_no;
		setInnerHTML( 'captureCoordinatesField', captureCoordinates );
		
		setInnerHTML( 'standardIntervalField', json.programStage.standardInterval );  
		setInnerHTML( 'dataElementCountField', json.programStage.dataElementCount );   
		setInnerHTML( 'reportDateDescriptionField', json.programStage.reportDateDescription );
		
		var templateMessage = "";
		for(var i in json.programStage.patientReminders){
			var index = eval(i) + 1;
			templateMessage += "<p class='bold'>" + i18n_template_reminder_message + " " + index + "</p>";
			templateMessage += "<p class='bold'>" + i18n_days_before_after_due_date + ":</p>" ;
			templateMessage	+= "<p>" + json.programStage.patientReminders[i].daysAllowedSendMessage + "</p>";
			templateMessage	+= "<p class='bold'>" + i18n_message + ":</p>";
			templateMessage	+= "<p>" + json.programStage.patientReminders[i].templateMessage + "</p>";
		}
		setInnerHTML('templateMessageField', templateMessage);
		
		showDetails();
	});
}

// -----------------------------------------------------------------------------
// select data-elements
// -----------------------------------------------------------------------------

function selectDataElements()
{
	var selectedList = jQuery("#selectedList");
	jQuery("#availableList").children().each(function(i, item){
		if( item.selected ){
			html = "<tr class='selected' id='" + item.value + "' ondblclick='unSelectDataElement( this )'><td onmousedown='select(event,this)'>" + item.text + "</td>";
			html += "<td align='center'><input type='checkbox' name='compulsory'></td>";
			html += "<td align='center'><input type='checkbox' name='allowProvided'></td>";
			html += "<td align='center'><input type='checkbox' name='displayInReport'></td>";
			html += "</tr>";
			selectedList.append( html );
			jQuery( item ).remove();
		}
	});
}


function selectAllDataElements()
{
	var selectedList = jQuery("#selectedList");
	jQuery("#availableList").children().each(function(i, item){
		html = "<tr class='selected' id='" + item.value + "' ondblclick='unSelectDataElement( this )'><td onmousedown='select(this)'>" + item.text + "</td>";
		html += "<td align='center'><input type='checkbox' name='compulsory'></td>";
		html += "<td align='center'><input type='checkbox' name='allowProvided'></td>";
		html += "<td align='center'><input type='checkbox' name='displayInReport'></td>";
		html += "</tr>";
		selectedList.append( html );
		jQuery( item ).remove();
	});
}

function unSelectDataElements()
{
	var availableList = jQuery("#availableList");
	jQuery("#selectedList").find("tr").each( function( i, item ){
		item = jQuery(item);
		if( item.hasClass("selected") )
		{		
			availableList.append( "<option value='" + item.attr( "id" ) + "' selected='true'>" + item.find("td:first").text() + "</option>" );
			item.remove();
		}
	});
}


function unSelectAllDataElements()
{
	var availableList = jQuery("#availableList");
	jQuery("#selectedList").find("tr").each( function( i, item ){
		item = jQuery(item);
		availableList.append( "<option value='" + item.attr( "id" ) + "' selected='true'>" + item.find("td:first").text() + "</option>" );
		item.remove();
	});
}

//-----------------------------------------------------------------------------
//Move Table Row Up and Down
//-----------------------------------------------------------------------------

function moveUpDataElement()
{
	var selectedList = jQuery("#selectedList");

	jQuery("#selectedList").find("tr").each( function( i, item ){
		item = jQuery(item);
		if( item.hasClass("selected") )
		{
			var prev = item.prev('#selectedList tr');
			if (prev.length == 1) 
			{ 
				prev.before(item);
			}
		}
	});
}

function moveDownDataElement()
{
	var selectedList = jQuery("#selectedList");
	var items = new Array();
	jQuery("#selectedList").find("tr").each( function( i, item ){
		items.push(jQuery(item));
	});
	
	for( var i=items.length-1;i>=0;i--)
	{	
		var item = items[i];
		if( item.hasClass("selected") )
		{
			var next = item.next('#selectedList tr');
			if (next.length == 1) 
			{ 
				next.after(item);
			}
		}
	}
}

function unSelectDataElement( element )
{
	element = jQuery(element);	
	jQuery("#availableList").append( "<option value='" + element.attr( "id" ) + "' selected='true'>" + element.find("td:first").text() + "</option>" );
	element.remove();
}

function select( event, element )
{
	if ( !getKeyCode( event ) )// Ctrl
	{
		jQuery("#selectedList .selected").removeClass( 'selected' );
	}
	
	element = jQuery( element ).parent();
	if( element.hasClass( 'selected') ) element.removeClass( 'selected' );
	else element.addClass( 'selected' );
}

function getKeyCode(e)
{
	var ctrlPressed=0;

	if (parseInt(navigator.appVersion)>3) {

		var evt = e ? e:window.event;

		if (document.layers && navigator.appName=="Netscape"
		&& parseInt(navigator.appVersion)==4) {
			// NETSCAPE 4 CODE
			var mString =(e.modifiers+32).toString(2).substring(3,6);
			ctrlPressed =(mString.charAt(1)=="1");
		}
		else {
			// NEWER BROWSERS [CROSS-PLATFORM]
			ctrlPressed=evt.ctrlKey;
		}
	}
	return ctrlPressed;
}

function repeatableOnChange()
{
	var checked = byId('irregular').checked;
	if( checked )
	{
		enable('standardInterval');
		enable('displayGenerateEventBox');
	}
	else
	{
		disable('standardInterval');
		disable('displayGenerateEventBox');
	}
}

function insertParams( paramValue, rowId )
{
	var templateMessage = paramValue;
	insertTextCommon('templateMessage' + rowId, templateMessage);
}

// --------------------------------------------------------------------
// Generate template message form
// --------------------------------------------------------------------

function generateTemplateMessageForm()
{
	var rowId = jQuery('.daysAllowedSendMessage').length + 1;
	
	var contend = '<tr name="tr' + rowId + '" class="listAlternateRow" >'
				+ 	'<td colspan="2">' + i18n_reminder + ' ' + rowId + '<a href="javascript:removeTemplateMessageForm('+ rowId +')"> ( '+ i18n_remove_reminder + ' )</a></td>'
				+ '</tr>'
				+ '<tr name="tr' + rowId + '">'
				+ 	'<td><label>' + i18n_days_before_after_due_date + '</label></td>'
				+ 	'<td><input type="text" id="daysAllowedSendMessage' + rowId + '" name="daysAllowedSendMessage' + rowId + '" class="daysAllowedSendMessage {validate:{required:true,number:true}}"/></td>'
				+ '</tr>'
				+ '<tr name="tr' + rowId + '">'
				+	'<td>' + i18n_params + '</td>'
				+	'<td>'
				+		'<select multiple size="4" id="params' + rowId +'" name="params" ondblclick="insertParams(this.value, ' + rowId + ');">'
				+			'<option value="{patient-name}">' + i18n_patient_name + '</option>'
				+			'<option value="{program-name}">' + i18n_program_name + '</option>'
				+			'<option value="{program-stage-name}">' + i18n_program_stage_name + '</option>'
				+			'<option value="{due-date}">' + i18n_due_date + '</option>'
				+			'<option value="{days-since-due-date}">' + i18n_days_since_due_date + '</option>'
				+			'<option value="{orgunit-name}">' + i18n_orgunit_name + '</option>'
				+		'</select>'
				+	'</td>'
				+ '</tr>'
				+ '<tr name="tr' + rowId + '">'
				+	'<td><label>' + i18n_message + '</label></td>'
				+	'<td><textarea id="templateMessage' + rowId + '" name="templateMessage' + rowId + '" style="width:320px" class="templateMessage {validate:{required:true, rangelength:[3,160]}}"></textarea></td>'
				+ '</tr>';

	jQuery('#programStageMessage').append( contend );
}

function removeTemplateMessageForm( rowId )
{
	jQuery("[name=tr" + rowId + "]").remove();
}
