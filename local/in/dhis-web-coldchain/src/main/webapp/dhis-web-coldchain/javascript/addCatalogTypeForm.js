jQuery( document ).ready( function()
{
	validation( 'addCatalogTypeForm', function( form ){ 
		form.submit();
	}, function(){
		selectedCatalogTypeAttributesValidator = jQuery( "#selectedCatalogTypeAttributesValidator" );
		selectedCatalogTypeAttributesValidator.empty();
		
		jQuery("#selectedList").find("tr").each( function( i, item ){ 
			selectedCatalogTypeAttributesValidator.append( "<option value='" + item.id + "' selected='true'>" + item.id + "</option>" );
		});
	});
	
	jQuery("#availableList").dhisAjaxSelect({
			source: "getCatalogTypeAttributes.action",
			iterator: "catalogTypeAttributes",
			connectedTo: 'selectedCatalogTypeAttributesValidator',
			handler: function(item) {
				var option = jQuery("<option />");
				option.text( item.name );
				option.attr( "value", item.id );
				
				return option;
				
			}
		});
		
	checkValueIsExist( "name", "validateCatalogType.action");	
});

