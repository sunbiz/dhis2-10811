jQuery(document).ready(	function(){
	validation( 'updateCatalogTypeAttributeForm', function(form){
		if( isSubmit && ATTRIBUTE_OPTION.checkOnSubmit() ) {
			form.submit(i18n_field_is_required);
		}
	}, function(){
		isSubmit = true;
		
		var fields = $("#updateCatalogTypeAttributeForm").serializeArray();
		jQuery.each(fields, function(i, field) {
			if(  field.name.match("^attrOption")=='attrOption' && field.value == ""){
				setInnerHTML("attrMessage", i18n_field_is_required);
				isSubmit = false;
			}
		});
	});
	
	checkValueIsExist( "name", "validateCatalogTypeAttribute.action", {id:getFieldValue('id')});
});		