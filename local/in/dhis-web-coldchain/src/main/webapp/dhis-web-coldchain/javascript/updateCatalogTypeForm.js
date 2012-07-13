jQuery( document ).ready( function()
{validation( 'updateCatalogTypeForm', function( form ){ 
		form.submit() ;
	}, function(){
		selectedCatalogTypeAttributesValidator = jQuery( "#selectedCatalogTypeAttributesValidator" );
		selectedCatalogTypeAttributesValidator.empty();
		
		jQuery("#selectedList").find("tr").each( function( i, item ){ 
			selectedCatalogTypeAttributesValidator.append( "<option value='" + item.id + "' selected='true'>" + item.id + "</option>" );
			
		});
	});
	
	checkValueIsExist( "name", "validateCatalogType.action", {id:getFieldValue('id')});	
	
	jQuery("#availableList").dhisAjaxSelect({
		source: "getCatalogTypeAttributes.action",
		iterator: "catalogTypeAttributes",
		connectedTo: 'selectedCatalogTypeAttributesValidator',
		handler: function(item) {
			var option = jQuery("<option />");
			option.text( item.name );
			option.attr( "value", item.id );
			
			var flag = false;
			jQuery("#selectedList").find("tr").each( function( k, selectedItem ){ 
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