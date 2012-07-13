jQuery(document).ready(function() {
	validation2('updateInventoryTypeDataSetForm', function(form) {
		form.submit();
	}, {
		'beforeValidateHandler' : function() {
            $("#selectedInventoryTypeDataSetList").find("option").attr("selected", "selected");
		},
	});
	
	checkValueIsExist( "name", "validateInventoryType.action", {id:getFieldValue('id')});	
	
	
	jQuery("#availableDataSetList").dhisAjaxSelect({
		source: "dataSetList.action",
		iterator: "dataSets",
		connectedTo: 'selectedInventoryTypeDataSetList',
		handler: function(item) {
			var option = jQuery("<option />");
			option.text( item.name );
			option.attr( "value", item.id );
			
			return option;
			
		}
	});		
		
});
