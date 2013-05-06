isAjax = true;

function orgunitSelected( orgUnits, orgUnitNames )
{
	hideById("listPatientDiv");
	setFieldValue('orgunitName', orgUnitNames[0]);
	setFieldValue('orgunitId', orgUnits[0]);
}

selection.setListenerFunction( orgunitSelected );

function generateStageCompleteness()
{
	showLoader();
	jQuery('#completenessDiv').load('generateProgramStageCompleteness.action',
		{
			programId: getFieldValue('programId'),
			startDate: getFieldValue('startDate'),
			endDate: getFieldValue('endDate')
		}, 
		function()
		{
			showById('completenessDiv');
			setTableStyles();
			hideLoader();
		});
}

function exportStageCompleteness( type )
{
	var params = "type=" + type;
	params += "&programId=" + getFieldValue('programId');
	params += "&startDate=" + getFieldValue('startDate');
	params += "&endDate=" + getFieldValue('endDate');
	
	var url = "generateProgramStageCompleteness.action?" + params;
	window.location.href = url;
}
