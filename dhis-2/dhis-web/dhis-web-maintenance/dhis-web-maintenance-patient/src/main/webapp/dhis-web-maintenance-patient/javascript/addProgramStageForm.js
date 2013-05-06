var duplicate = false;
jQuery( document ).ready( function()
{
	validation( 'addProgramStageForm', function( form ){ 
		if( duplicate==true) 
			return false;
		else
			form.submit();
	}, function(){
		duplicate = false;
		var COLOR_RED = '#ff8a8a';
		jQuery(".daysAllowedSendMessage").each( function( i, a ){ 
			jQuery(".daysAllowedSendMessage").each( function(j, b){ 
				if( i!=j && a.value==b.value){
					jQuery( a ).css( 'background-color', COLOR_RED );
					jQuery( b ).css( 'background-color', COLOR_RED );
					duplicate = true;
				}
			});
		});
		
		var selectedDataElementsValidator = jQuery( "#selectedDataElementsValidator" );
		selectedDataElementsValidator.empty();
		
		var compulsories = jQuery( "#compulsories" );
		compulsories.empty();
		
		var displayInReports = jQuery( "#displayInReports" );
		displayInReports.empty();
		
		var daysAllowedSendMessages = jQuery( "#daysAllowedSendMessages" );
		daysAllowedSendMessages.empty();
		
		var templateMessages = jQuery( "#templateMessages" );
		templateMessages.empty();
		
		var allowProvidedElsewhere = jQuery( "#allowProvidedElsewhere" );
		allowProvidedElsewhere.empty();
		
		jQuery("#selectedList").find("tr").each( function( i, item ){ 
			
			selectedDataElementsValidator.append( "<option value='" + item.id + "' selected='true'>" + item.id + "</option>" );
			
			var compulsory = jQuery( item ).find( "input[name='compulsory']:first");
			var checked = compulsory.attr('checked') ? true : false;
			compulsories.append( "<option value='" + checked + "' selected='true'>" + checked + "</option>" );
			
			var allowProvided = jQuery( item ).find( "input[name='allowProvided']:first");
			checked = allowProvided.attr('checked') ? true : false;
			allowProvidedElsewhere.append( "<option value='" + checked + "' selected='true'>" + checked + "</option>" );
			
			var displayInReport = jQuery( item ).find( "input[name='displayInReport']:first");
			checked = displayInReport.attr('checked') ? true : false;
			displayInReports.append( "<option value='" + checked + "' selected='true'>" + checked + "</option>" );
		});
		jQuery(".daysAllowedSendMessage").each( function( i, item ){ 
			daysAllowedSendMessages.append( "<option value='" + item.value + "' selected='true'>" + item.value +"</option>" );
		});
		jQuery(".templateMessage").each( function( i, item ){ 
			templateMessages.append( "<option value='" + item.value + "' selected='true'>" +item.value+"</option>" );
		});
	});
	
	jQuery("#availableList").dhisAjaxSelect({
			source: "../dhis-web-commons-ajax-json/getDataElements.action?domain=patient",
			iterator: "dataElements",
			connectedTo: 'selectedDataElementsValidator',
			handler: function(item) {
				var option = jQuery("<option />");
				option.text( item.name );
				option.attr( "value", item.id );

				return option;
			}
		});
		
	checkValueIsExist( "name", "validateProgramStage.action",{id: getFieldValue('programId')});	
});
