// Current offset, next or previous corresponding to increasing or decreasing
// value with one
var currentPeriodOffset = 0;

// Period type object
var periodTypeFactory = new PeriodType();

// The current selected report type name
var currentReportTypeName = '';

// The current selected period type name
var currentPeriodTypeName = '';

// The current selected orgunit name
var currentOrgunitName = '';

// Functions
function organisationUnitSelected( orgUnits, orgUnitNames )
{
	currentOrgunitName = orgUnitNames[0];
	getExportReportsByGroup( currentOrgunitName );	
}

selection.setListenerFunction( organisationUnitSelected );

function getExportReportsByGroup( selectedOrgUnitName ) {

	var groupId = getFieldValue( 'group' );

	if ( selectedOrgUnitName )
	{
		setInnerHTML( "selectedOrganisationUnit", selectedOrgUnitName );

		if ( groupId )
		{
			jQuery.postJSON( 'getExportReportsByGroup.action',
			{
				group: groupId
			},
			function ( json )
			{
				jQuery('#exportReport').empty();
				jQuery.each( json.exportReports, function(i, item){
					addOptionById( 'exportReport', item.id + '_' + item.periodType + '_' + item.reportType, item.name );
				});

				currentPeriodOffset = 0;

				reportSelected();
				displayPeriodsInternal();
			});
		}
	}
}

function changeExportType( _this )
{
	if ( _this.checked )
	{
		byId( "exportReport" ).multiple = true;
		enable( "periodType" );
		reportSelected( getFieldValue( "periodType" ) );
	}
	else
	{
		byId( "exportReport" ).multiple = false;
		deselectAllById( "periodType" );
		disable( "periodType" );
		reportSelected();
	}
	
	displayPeriodsInternal();
	showById( "periodRow" );
}

function reportSelected( _periodType )
{
	if ( _periodType )
	{
		currentPeriodTypeName = _periodType;
	}
	else if ( !isChecked( 'exportTypeCB' ) )
	{
		var value = getFieldValue( 'exportReport' );

		if ( value && value != null )
		{
			currentPeriodTypeName = value.split( '_' )[1] == "" ? "Monthly" : value.split( '_' )[1];
			currentReportTypeName = value.split( '_' )[2];

			if ( currentReportTypeName == "P" ) {
				hideById( "periodCol" );
			}else {
				showById( "periodCol" );
			}
		}
	}

	displayPeriodsInternal();
}

function displayPeriodsInternal()
{
	if ( currentPeriodTypeName )
	{
		var periods = periodTypeFactory.get( currentPeriodTypeName ).generatePeriods( currentPeriodOffset );
		periods = periodTypeFactory.filterFuturePeriods( periods );

		clearListById( 'selectedPeriodId' );

		for ( i in periods )
		{
			addOptionById( 'selectedPeriodId', periods[i].id, periods[i].name );
		}
	}

	if ( currentReportTypeName == "P" ) {
		getRelativePeriods ( jQuery('#selectedPeriodId option:first' ).val() );
	}
	else {
		getRelativePeriods( getFieldValue( "selectedPeriodId" ) );
	}
}

function getRelativePeriods( value )
{
	if ( value )
	{
		var periodType = value.split( "_" )[0];
		var date = value.split( "_" )[1];
		var submitDateId = "";

		if ( periodType == "Weekly" )
		{
		}
		else if ( periodType == "Quarterly" || periodType == "Yearly" || periodType == "SixMonthly" )
		{
			submitDateId = "Monthly" + "_" + date;
		}
		else // Daily, Monthly
		{
			submitDateId = periodType + "_" + date;
		}
		
		if ( submitDateId && submitDateId != "" )
		{
			setFieldValue( 'selectedPeriodId2', submitDateId );
		}
	}
}

function getNextPeriod()
{
    if ( currentPeriodOffset < 0 ) // Cannot display future periods
    {
        currentPeriodOffset++;
        displayPeriodsInternal();
    }
}

function getPreviousPeriod()
{
    currentPeriodOffset--;
    displayPeriodsInternal();
}

function hideExportDiv()
{
	hideById( 'exportReportDiv' );
	showById( 'showButtonDiv' );
}

function showExportDiv()
{
	showById( 'exportReportDiv' );
	hideById( 'showButtonDiv' );
}

function validateGenerateReport( isAdvanced )
{
	var exportReports = jQuery( 'select[id=exportReport]' ).children( 'option:selected' );

	if ( exportReports.length == 0 )
	{
		showErrorMessage( i18n_specify_export_report );
		return;
	}
	
	var periodIndex = getFieldValue( 'selectedPeriodId2' );
	
	if ( periodIndex.length == 0 )
	{
		showErrorMessage( i18n_specify_periodtype_or_period );
		return;
	}
	
	var url = 'validateGenerateReport.action?';
	
	jQuery.each( exportReports, function ( i, item )
	{
		url += 'exportReportIds=' + item.value.split( "_" )[0] + '&';
	} );
	
	url = url.substring( 0, url.length - 1 );
	
	if ( url && url != '' )
	{
		hideExportDiv();
		lockScreen();

		jQuery.postJSON( url,
		{
			'periodIndex': periodIndex
		},
		function( json )
		{
			if ( json.response == "success" ) {
				if ( isAdvanced ) {
					generateAdvancedExportReport();
				}
				else generateExportReport();
			}
			else {
				unLockScreen();
				showWarningMessage( json.message );
			}
		});
	}
}

function generateExportReport() {
		
	jQuery.postJSON( 'generateExportReport.action', {}, function ( json ) {
		if ( json.response == "success" ) {
			window.location = "downloadFile.action";		
			unLockScreen();
		}
	});
}

function getALLExportReportByGroup() {

	jQuery.postJSON( "getALLExportReportByGroup.action", {
		group: byId("group").value
	}, function( json ) {
		clearListById( 'exportReport' );
		var list = json.exportReports;
		
		for ( var i = 0 ; i < list.length ; i++ )
		{
			addOption( 'exportReport', item[i].name, item[i].id );
		}
	} );
}

function generateAdvancedExportReport()
{
	jQuery.postJSON( 'generateAdvancedExportReport.action', {
		organisationGroupId: byId("availableOrgunitGroups").value
	}, function( json ) {
		if ( json.response == "success" ) {
			showSuccessMessage( json.message );
		}
	} );
}
