jQuery(document).ready(	function(){
			
	jQuery('name').focus();
	
	validation( 'addPatientIdentifierTypeForm', function(form){
		form.submit();
	}); 
	
	checkValueIsExist( "name", "validatePatientIdentifierType.action");			
});		