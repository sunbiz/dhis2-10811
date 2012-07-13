isAjax = true;
function organisationUnitSelected( orgUnits, orgUnitNames )
{
    showLoader();
	setInnerHTML( 'contentDiv','' );
	jQuery.getJSON( "getReportPrograms.action",{}, 
		function( json ) 
		{    
			setFieldValue( 'orgunitname', orgUnitNames[0] );
			
			clearListById('programId');
			if( json.programs.length == 0)
			{
				disable('programId');
				disable('startDate');
				disable('endDate');
				disable('generateBtn');
			}
			else
			{
				addOptionById( 'programId', "", i18n_please_select_a_program );
				
				for ( var i in json.programs ) 
				{
					addOptionById( 'programId', json.programs[i].id, json.programs[i].name );
				} 
				enable('programId');
				enable('startDate');
				enable('endDate');
				enable('generateBtn');
			}
			
			hideLoader();
		});
}

selection.setListenerFunction( organisationUnitSelected );

function loadGeneratedReport()
{
	showLoader();

	jQuery( "#contentDiv" ).load( "generateReport.action",
	{
		programId: getFieldValue( 'programId' ),
		startDate: getFieldValue( 'startDate' ),
		endDate: getFieldValue( 'endDate' )
	}, function() 
	{ 
		hideLoader();
		hideById( 'message' );
		showById( 'contentDiv' );
	});
}

function viewRecords( programStageInstanceId ) 
{
	$('#viewRecordsDiv' )
		.load( 'viewRecords.action?id=' + programStageInstanceId )
		.dialog({
			title: i18n_reports,
			maximize: true, 
			closable: true,
			modal:false,
			overlay:{background:'#000000', opacity:0.1},
			width: 800,
			height: 400
		});
}
