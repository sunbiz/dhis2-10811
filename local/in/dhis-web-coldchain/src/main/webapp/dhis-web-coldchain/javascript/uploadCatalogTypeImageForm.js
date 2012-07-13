jQuery(document).ready(	function(){
	
	validation( 'uploadCatalogTypeImageForm', function(form){
		form.submit();
	}, function(){
		isSubmit = true;
		
	});
	
	checkValueIsExist( "name", "validateCatalogType.action", {id:getFieldValue('id')});	
});
