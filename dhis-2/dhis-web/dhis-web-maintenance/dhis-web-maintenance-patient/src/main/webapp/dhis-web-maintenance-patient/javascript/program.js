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
		
		setInnerHTML( 'dateOfEnrollmentDescriptionField', json.program.dateOfEnrollmentDescription );   
		setInnerHTML( 'dateOfIncidentDescriptionField', json.program.dateOfIncidentDescription );   		
		setInnerHTML( 'programStageCountField',  json.program.programStageCount );
		setInnerHTML( 'durationInDaysField',  json.program.maxDay );
		
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

function programTypeOnChange()
{
	var type = getFieldValue('type');
	
	// anonymous
	if(type == "3")
	{
		disable('dateOfEnrollmentDescription');
		disable('dateOfIncidentDescription');
	}
	// single-event
	else if( type=='2')
	{
		enable('dateOfEnrollmentDescription');
		disable('dateOfIncidentDescription');
	}
	else
	{
		enable('dateOfEnrollmentDescription');
		enable('dateOfIncidentDescription');
	}
}

function hideIncidentDateOnchange()
{
	var checked = byId( 'hideDateOfIncident' ).checked;
	
	if( checked)
	{
		disable( 'dateOfIncidentDescription' );
	}
	else
	{
		enable( 'dateOfIncidentDescription' );
	}
}

