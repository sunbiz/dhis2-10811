jQuery(document).ready(	function(){
	
	jQuery('name').focus();
	
	validation( 'updatePatientIdentifierTypeForm', function(form){
		form.submit();
	}); 
	
	checkValueIsExist( "name", "validatePatientIdentifierType.action", {id:getFieldValue('id')});
});	