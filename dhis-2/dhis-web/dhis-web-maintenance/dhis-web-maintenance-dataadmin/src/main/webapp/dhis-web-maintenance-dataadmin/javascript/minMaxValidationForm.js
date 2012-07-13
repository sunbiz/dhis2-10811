jQuery(document).ready(function() {
	validation2("minMaxGeneratingForm", function() {
		if (isGenerate) {
			generateMinMaxValue();
		} else {
			removeMinMaxValue();
		}
	}, {
		'rules' : getValidationRules("minMax")
	});
});

var isGenerate = true;
var numberOfSelects = 0;
