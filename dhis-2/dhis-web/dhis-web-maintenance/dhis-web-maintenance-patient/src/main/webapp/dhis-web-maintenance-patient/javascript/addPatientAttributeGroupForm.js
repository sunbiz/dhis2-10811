jQuery(document).ready(	function(){
		
		jQuery('name').focus();
		
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
		
		
		validation2( 'addPatientAttributeGroupForm', function(form){
			form.submit();
		}, {
			'beforeValidateHandler' : function()
			{
				listValidator( 'memberValidator', 'selectedAttributes' );
			},
			'rules' : getValidationRules( 'patientAttributeGroup' )
		} ); 
		
		checkValueIsExist( "name", "validatePatientAttributeGroup.action" );
		
		
	});		