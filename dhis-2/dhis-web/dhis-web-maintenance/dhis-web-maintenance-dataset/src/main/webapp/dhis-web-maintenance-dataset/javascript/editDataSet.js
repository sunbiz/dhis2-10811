jQuery(document).ready(function() {
	validation2('editDataSetForm', function(form) {
		form.submit();
	}, {
		'beforeValidateHandler' : function() {
            $("#dataElementsSelectedList").find("option").attr("selected", "selected");
            $("#indicatorsSelectedList").find("option").attr("selected", "selected");
		},
		'rules' : getValidationRules("dataSet")
	});

	checkValueIsExist("name", "validateDataSet.action", {
		dataSetId : function() {
			return jQuery("#dataSetId").val();
		}
	});

	checkValueIsExist("shortName", "validateDataSet.action", {
		dataSetId : function() {
			return jQuery("#dataSetId").val();
		}
	});

	checkValueIsExist("code", "validateDataSet.action", {
		dataSetId : function() {
			return jQuery("#dataSetId").val();
		}
	});
});
