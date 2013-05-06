// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showProgramDetails( programId )
{
	jQuery.getJSON( "getProgram.action", {
		id:programId
	}, function(json){
		setInnerHTML( 'nameField', json.program.name );
		setInnerHTML( 'descriptionField', json.program.description );
		
		var type = i18n_multiple_events_with_registration;
		if( json.program.type == "2" )
			type = i18n_single_event_with_registration;
		else if( json.program.type == "3"  )
			type = i18n_single_event_without_registration;
		setInnerHTML( 'typeField', type );  
		
		var displayProvidedOtherFacility = ( json.program.displayProvidedOtherFacility == 'true') ? i18n_yes : i18n_no;
		setInnerHTML( 'displayProvidedOtherFacilityField', displayProvidedOtherFacility );   	
		
		var blockEntryForm = ( json.program.blockEntryForm == 'true') ? i18n_yes : i18n_no;
		setInnerHTML( 'blockEntryFormField', blockEntryForm );   	
		
		var displayIncidentDate = ( json.program.displayIncidentDate == 'true') ? i18n_yes : i18n_no;
		setInnerHTML( 'displayIncidentDateField', displayIncidentDate );   	
		
		var generatedByEnrollmentDate = ( json.program.generatedByEnrollmentDate == 'true') ? i18n_yes : i18n_no;
		setInnerHTML( 'generatedByEnrollmentDateField', generatedByEnrollmentDate );   	
		
		var ignoreOverdueEvents = ( json.program.ignoreOverdueEvents == 'true') ? i18n_yes : i18n_no;
		setInnerHTML( 'ignoreOverdueEventsField', ignoreOverdueEvents );   	
		
		var onlyEnrollOnce = ( json.program.onlyEnrollOnce == 'true') ? i18n_yes : i18n_no;
		setInnerHTML( 'onlyEnrollOnceField', onlyEnrollOnce );   	
		
		var remindCompleted = ( json.program.remindCompleted == 'true') ? i18n_yes : i18n_no;
		setInnerHTML( 'remindCompletedField', remindCompleted );   	
		
		var disableRegistrationFields = ( json.program.disableRegistrationFields == 'true') ? i18n_yes : i18n_no;
		setInnerHTML( 'disableRegistrationFieldsField', disableRegistrationFields );   	
		
		setInnerHTML( 'dateOfEnrollmentDescriptionField', json.program.dateOfEnrollmentDescription );   
		setInnerHTML( 'dateOfIncidentDescriptionField', json.program.dateOfIncidentDescription );   		
		setInnerHTML( 'programStageCountField',  json.program.programStageCount );
		setInnerHTML( 'noAttributesField', json.program.noAttributes );
		setInnerHTML( 'noIdentifierTypesField', json.program.noIdentifierTypes );
		
		showDetails();
	});   
}

// -----------------------------------------------------------------------------
// Remove Program
// -----------------------------------------------------------------------------

function removeProgram( programId, name )
{
	removeItem( programId, name, i18n_confirm_delete, 'removeProgram.action' );
}

function programOnChange()
{
	var type = getFieldValue('type');
	
	// anonymous
	if(type == "3")
	{
		disable('onlyEnrollOnce');
		disable('dateOfEnrollmentDescription');
		disable("displayIncidentDate");
		disable("dateOfIncidentDescription");
		disable("generatedByEnrollmentDate");
		disable("availablePropertyIds");
		disable('ignoreOverdueEvents');
		hideById('selectedList');
		hideById('programMessageTB');
		
		jQuery("[name=displayed]").attr("disabled", true);
		jQuery("[name=displayed]").removeAttr("checked");
		
		jQuery("[name=nonAnonymous]").hide();
	}
	else{
		enable('onlyEnrollOnce');
		jQuery("[name=displayed]").prop("disabled", false);
		enable("availablePropertyIds");
		enable("generatedByEnrollmentDate");
		enable('dateOfEnrollmentDescription');
		enable("displayIncidentDate");
		enable('ignoreOverdueEvents');
		showById('programMessageTB');
		showById("selectedList");
		
		jQuery("[name=nonAnonymous]").show();
		if( type == 2 ){
			disable('ignoreOverdueEvents');
			disable('onlyEnrollOnce');
			disable('generatedByEnrollmentDate');
		}
		
		if(byId('displayIncidentDate').checked){
			enable("dateOfIncidentDescription");
		}
		else {
			disable("dateOfIncidentDescription");
		}
	}
}

// -----------------------------------------------------------------------------
// select identifiers / attributes
// -----------------------------------------------------------------------------

function selectProperties()
{
	var selectedList = jQuery("#selectedList");
	jQuery("#availablePropertyIds").children().each(function(i, item){
		if( item.selected ){
			html = "<tr class='selected' id='" + item.value + "' ondblclick='unSelectProperties( this )'><td onmousedown='select(event,this)'>" + item.text + "</td>";
			html += "<td align='center'><input type='checkbox' name='displayed' value='" + item.value + "'";
			if( item.value.match("^attr_")=="attr_" )
			{
				html += " style='display:none' ";
			}
			html += "></td></tr>";
			selectedList.append( html );
			jQuery( item ).remove();
		}
	});
	
	if(getFieldValue('type') == "3")
	{
		jQuery("[name=displayed]").attr("disabled", true);
	}
}


function selectAllProperties()
{
	var selectedList = jQuery("#selectedList");
	jQuery("#availablePropertyIds").children().each(function(i, item){
		html = "<tr class='selected' id='" + item.value + "' ondblclick='unSelectDataElement( this )'><td onmousedown='select(this)'>" + item.text + "</td>";
		html += "<td align='center'><input type='checkbox' name='displayed' value='" + item.value + "'";
		if( item.value.match("^attr_")=="attr_" )
		{
			html += " style='display:none' ";
		}
		html += "'></td></tr>";
		selectedList.append( html );
		jQuery( item ).remove();
	});
}

function unSelectProperties()
{
	var availableList = jQuery("#availablePropertyIds");
	jQuery("#selectedList").find("tr").each( function( i, item ){
		item = jQuery(item);
		if( item.hasClass("selected") )
		{		
			availableList.append( "<option value='" + item.attr( "id" ) + "' selected='true'>" + item.find("td:first").text() + "</option>" );
			item.remove();
		}
	});
}


function unSelectAllProperties()
{
	var availableList = jQuery("#availablePropertyIds");
	jQuery("#selectedList").find("tr").each( function( i, item ){
		item = jQuery(item);
		availableList.append( "<option value='" + item.attr( "id" ) + "' selected='true'>" + item.find("td:first").text() + "</option>" );
		item.remove();
	});
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

//-----------------------------------------------------------------------------
//Move Table Row Up and Down
//-----------------------------------------------------------------------------

function moveUpPropertyList()
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

function moveDownPropertyList()
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
				+ 	'<td><label>' + i18n_date_to_compare + '</label></td>'
				+ 	'<td>'
				+		'<select class="dateToCompare">'
				+			'<option value="dateofincident">' + i18n_incident_date + '</option>'
				+			'<option value="enrollmentdate">' + i18n_enrollment_date + '</option>'
				+ 		'</select>'
				+   '</td>'
				+ '</tr>'
				+ '<tr name="tr' + rowId + '">'
				+ 	'<td><label>' + i18n_days_before_after_comparison_date + '</label></td>'
				+ 	'<td><input type="text" id="daysAllowedSendMessage' + rowId + '" name="daysAllowedSendMessage' + rowId + '" class="daysAllowedSendMessage {validate:{required:true,number:true}}"/></td>'
				+ '</tr>'
				+ '<tr name="tr' + rowId + '">'
				+	'<td>' + i18n_params + '</td>'
				+	'<td>'
				+		'<select multiple size="4" id="params' + rowId +'" name="params" ondblclick="insertParams(this.value, ' + rowId + ');">'
				+			'<option value="{patient-name}">' + i18n_patient_name + '</option>'
				+			'<option value="{program-name}">' + i18n_program_name + '</option>'
				+			'<option value="{incident-date}">' + i18n_incident_date + '</option>'
				+			'<option value="{days-since-incident-date}">' + i18n_days_since_incident_date + '</option>'
				+			'<option value="{enrollement-date}">' + i18n_enrollment_date + '</option>'
				+			'<option value="{days-since-enrollement-date}">' + i18n_days_since_enrollment_date + '</option>'
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

function insertParams( paramValue, rowId )
{
	var templateMessage = paramValue;
	insertTextCommon('templateMessage' + rowId, templateMessage);
}