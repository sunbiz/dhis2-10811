jQuery( document ).ready( function()
{
	validation( 'updateInventoryTypeForm', function( form ){ 
		form.submit() ;
	}, function(){
		var selectedInventoryTypeAttributeValidator = jQuery( "#selectedInventoryTypeAttributeValidator" );
		selectedInventoryTypeAttributeValidator.empty();
		
		var display = jQuery( "#display" );
		display.empty();
		
		jQuery("#selectedInventoryTypeAttributeList").find("tr").each( function( i, item ){ 
			
			selectedInventoryTypeAttributeValidator.append( "<option value='" + item.id + "' selected='true'>" + item.id + "</option>" );
			
			var forDisplay = jQuery( item ).find( "input[name='forDisplay']:first");
			var checked = forDisplay.attr('checked') ? true : false;
			display.append( "<option value='" + checked + "' selected='true'>" + checked + "</option>" );
			
		});
	});
	
	checkValueIsExist( "name", "validateInventoryType.action", {id:getFieldValue('id')});
	
	jQuery("#availableInventoryTypeAttributeList").dhisAjaxSelect({
		source: "inventoryTypeAttributes.action",
		iterator: "inventoryTypeAttributes",
		connectedTo: 'selectedInventoryTypeAttributeValidator',
		handler: function(item) {
			var option = jQuery("<option />");
			option.text( item.name );
			option.attr( "value", item.id );
			
			var flag = false;
			jQuery("#selectedInventoryTypeAttributeList").find("tr").each( function( k, selectedItem ){ 
				if(selectedItem.id == item.id )
				{
					flag = true;
					return;
				}
			});
			
			if(!flag) return option;
		}
	});
});




/*

jQuery(document).ready(function() {
	validation2('updateInventoryTypeForm', function(form) {
		form.submit();
	}, {
		'beforeValidateHandler' : function() {
            $("#selectedInventoryTypeAttributeList").find("option").attr("selected", "selected");
		},
	});
	
	checkValueIsExist( "name", "validateInventoryType.action", {id:getFieldValue('id')});	
	
	
	jQuery("#availableInventoryTypeAttributeList").dhisAjaxSelect({
		source: "inventoryTypeAttributes.action",
		iterator: "inventoryTypeAttributes",
		connectedTo: 'selectedInventoryTypeAttributeList',
		handler: function(item) {
			var option = jQuery("<option />");
			option.text( item.name );
			option.attr( "value", item.id );
			
			return option;
			
		}
	});		
	
	
});
*/