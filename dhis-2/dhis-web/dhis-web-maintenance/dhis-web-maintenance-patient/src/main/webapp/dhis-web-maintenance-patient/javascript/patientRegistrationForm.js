
function addNewForm(){
	window.location.href='viewPatientRegistrationForm.action?programId=' + getFieldValue('programId');
}

function updateNewForm( registrationFormId, programId ){
	window.location.href='viewPatientRegistrationForm.action?programId=' + programId + '&id=' + registrationFormId;
}

function removeRegistrationForm( registrationFormId, name, programId, programName )
{
	removeItem( registrationFormId, name, i18n_confirm_delete, 'delRegistrationFormAction.action' );	
	
	if(programId=='')
	{
		jQuery('#programId').prepend('<option value="" selected>' + i18n_please_select + '</option>');
	}
	else
	{
		jQuery('#programId').append('<option value="' + programId + '" selected>' + programName + '</option>');
	}
}