jQuery( document ).ready( function()
{
	validation( 'updateProgramStageForm', function( form ){ 
		form.submit() ;
	}, function(){
		selectedDataElementsValidator = jQuery( "#selectedDataElementsValidator" );
		selectedDataElementsValidator.empty();
		
		compulsories = jQuery( "#compulsories" );
		compulsories.empty();
		
		allowProvidedElsewhere = jQuery( "#allowProvidedElsewhere" );
		allowProvidedElsewhere.empty();
		
		jQuery("#selectedList").find("tr").each( function( i, item ){ 
			
			selectedDataElementsValidator.append( "<option value='" + item.id + "' selected='true'>" + item.id + "</option>" );
			
			var compulsory = jQuery( item ).find( "input[name='compulsory']:first");
			var checked = compulsory.attr('checked') ? true : false;
			compulsories.append( "<option value='" + checked + "' selected='true'>" + checked + "</option>" );
			
			
			var allowProvided = jQuery( item ).find( "input[name='allowProvided']:first");
			checked = allowProvided.attr('checked') ? true : false;
			allowProvidedElsewhere.append( "<option value='" + checked + "' selected='true'>" + checked + "</option>" );
		});
	});
	
	checkValueIsExist( "name", "validateProgramStage.action", {id:getFieldValue('id')});	
	
	jQuery("#availableList").dhisAjaxSelect({
		source: "../dhis-web-commons-ajax-json/getDataElements.action?domain=patient",
		iterator: "dataElements",
		connectedTo: 'selectedDataElementsValidator',
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