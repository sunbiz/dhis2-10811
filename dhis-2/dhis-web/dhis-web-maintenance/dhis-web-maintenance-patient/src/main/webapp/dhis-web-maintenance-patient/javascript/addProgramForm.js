jQuery(document).ready(	function(){
	validation( 'addProgramForm', function( form ){		
		enable('dateOfEnrollmentDescription');
		enable('dateOfIncidentDescription');
		form.submit();
	});				
	
	checkValueIsExist( "name", "validateProgram.action");
});	