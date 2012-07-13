jQuery(document).ready(	function(){
	validation( 'updateCatalogForm', function(form){
		form.submit();
	}, function(){
		isSubmit = true;
		
	});
	
	checkValueIsExist( "name", "validateCatalog.action", {id:getFieldValue('id')});
});
