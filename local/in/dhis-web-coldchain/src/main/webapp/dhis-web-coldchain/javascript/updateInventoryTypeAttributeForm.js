jQuery(document).ready(	function(){
	validation( 'updateInventoryTypeAttributeForm', function(form){
		if( isSubmit && ATTRIBUTE_OPTION.checkOnSubmit() ) {
			form.submit(i18n_field_is_required);
		}
	}, function(){
		isSubmit = true;
		
		var fields = $("#addInventoryTypeAttributeForm").serializeArray();
		jQuery.each(fields, function(i, field) {
			if(  field.name.match("^attrOption")=='attrOption' && field.value == ""){
				setInnerHTML("attrMessage", i18n_field_is_required);
				isSubmit = false;
			}
		});
	});
	
	checkValueIsExist( "name", "validateInventoryTypeAttribute.action", {id:getFieldValue('id')});
});