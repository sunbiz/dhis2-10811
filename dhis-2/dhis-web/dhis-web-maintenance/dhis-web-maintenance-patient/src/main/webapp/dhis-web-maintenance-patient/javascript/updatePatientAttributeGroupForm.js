jQuery(document).ready(	function() {
	
	jQuery('name').focus();

	validation2( 'updatePatientAttributeGroupForm', function(form){
		form.submit();
	}, {
		'beforeValidateHandler' : function()
		{
			listValidator( 'memberValidator', 'selectedAttributes' );
		},
		'rules' : getValidationRules( 'patientAttributeGroup' )
	});

	checkValueIsExist( "name", "validatePatientAttributeGroup.action", {id:getFieldValue('id')});
	
	jQuery("#availableAttributes").dhisAjaxSelect({
		source: 'getPatientAttributeWithoutGroup.action',
		iterator: 'patientAttributes',
		connectedTo: 'selectedAttributes',
		handler: function(item){
			var option = jQuery( "<option/>" );
			option.attr( "value", item.id );
			option.text( item.name );
			
			return option;
		}
	});
});		