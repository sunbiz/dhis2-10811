// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showPatientIdentifierTypeDetails( patientIdentifierTypeId )
{
	jQuery.getJSON( 'getPatientIdentifierType.action', { id: patientIdentifierTypeId },
		function ( json ) {
			setInnerHTML( 'nameField', json.patientIdentifierType.name );	
			setInnerHTML( 'descriptionField', json.patientIdentifierType.description );
			
			var boolValueMap = { 'true':i18n_yes, 'false':i18n_no };
			var boolType = json.patientIdentifierType.mandatory;
			setInnerHTML( 'mandatoryField', boolValueMap[boolType] );
			
			boolType = json.patientIdentifierType.related;
			setInnerHTML( 'relatedField', boolValueMap[boolType] );
			setInnerHTML( 'noCharsField', json.patientIdentifierType.noChars );
			
			var valueTypeMap = { 'text':i18n_string, 'number':i18n_number, 'letter':i18n_letter_only };
			var valueType = json.patientIdentifierType.type;
			setInnerHTML( 'typeField', valueTypeMap[valueType] );
			
			var programName = json.patientIdentifierType.program;
			if( programName == '')
			{
				programName = i18n_all;
			}
			setInnerHTML( 'programField', programName );
			
			showDetails();
	});
}

// -----------------------------------------------------------------------------
// Remove Patient Identifier Type
// -----------------------------------------------------------------------------

function removePatientIdentifierType( patientIdentifierTypeId, name )
{
    removeItem( patientIdentifierTypeId, name, i18n_confirm_delete, 'removePatientIdentifierType.action' );
}