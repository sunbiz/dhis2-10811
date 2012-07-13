function dataSetSelected()
{
	var dataSetId = $( '#dataSetId' ).val();	
	
	if ( dataSetId && dataSetId != 0 )
	{
	    clearListById( 'sDateLB' );
		clearListById( 'eDateLB' );
	    
	    addOptionById( 'sDateLB', '', '[' + i18n_please_select + ']' );
		addOptionById( 'eDateLB', '', '[' + i18n_please_select + ']' );
	    
	    $.getJSON( "loadPeriods.action", 
			{
				dataSetId:dataSetId 
			}
			, function( json ) 
			{
				for ( i in json.periods ) {
					addOptionById( 'sDateLB', i, json.periods[i].name );
					addOptionById( 'eDateLB', i, json.periods[i].name );
				}
	    
				enable('previousPeriodForStartBtn');
				enable('nextPeriodForStartBtn');
				enable('previousPeriodForEndBtn');
				enable('nextPeriodForEndBtn');
			} );
	}
	else
	{
		disable('previousPeriodForStartBtn');
		disable('nextPeriodForStartBtn');
		disable('previousPeriodForEndBtn');
		disable('nextPeriodForEndBtn');
	}
	
}

function getPreviousPeriodForStart() 
{
	var index = byId('sDateLB').options[byId('sDateLB').selectedIndex].value;
	jQuery.getJSON('previousPeriods.action' , 
		{
			startField: 'true',
			index: index
		}, responseListPeriodForStartReceived );	
}

function getNextPeriodForStart() 
{
	var index = byId('sDateLB').options[byId('sDateLB').selectedIndex].value;
	jQuery.getJSON('nextPeriods.action', 
		{
			startField:'true',
			index: index
		}, responseListPeriodForStartReceived );	
}

function responseListPeriodForStartReceived( json ) 
{	
	clearListById('sDateLB');
	
	jQuery.each( json.periods, function(i, item ){
		addOptionById('sDateLB', i, item.name);
	});
}

function getPreviousPeriodForEnd() 
{
	var index = byId('eDateLB').options[byId('eDateLB').selectedIndex].value;
	jQuery.getJSON('previousPeriods.action', 
		{
			startField: 'false',
			index: index
		}, responseListPeriodForEndReceived );	
}

function getNextPeriodForEnd() 
{
	var index = byId('eDateLB').options[byId('eDateLB').selectedIndex].value;
	jQuery.getJSON('nextPeriods.action', 
		{
			startField: 'false',
			index: index
		}, responseListPeriodForEndReceived );	
}

function responseListPeriodForEndReceived( json ) 
{	
	clearListById('eDateLB');
	
	jQuery.each( json.periods, function(i, item ){
		addOptionById('eDateLB', i, item.name );
	});
}
	
function validationCaseAggregation( )
{
	$.get( 'validateCaseAggregation.action', 
		{ 
			sDateLB:getFieldValue('sDateLB'), 
			eDateLB:getFieldValue('eDateLB')
		}, validationCaseAggregationCompleted, 'xml' );
					
}

function validationCaseAggregationCompleted( message )
{
    var type = $(message).find('message').attr('type');
	
    if( type == "success" )
    {
        caseAggregationResult();
    }
    else
    {
        showWarningMessage( $(message).find('message').text() );
    }
}

function viewResultDetails( orgunitId, periodTypeName, startDate, aggregationConditionId ) 
{
	$('#contentDetails' ).val('');
	var url = 'caseAggregationResultDetails.action?';
		url+= 'orgunitId=' + orgunitId;
		url+= '&periodTypeName=' + periodTypeName;
		url+= '&startDate=' + startDate;
		url+= '&aggregationConditionId=' + aggregationConditionId;
		
	$('#contentDetails' ).load(url).dialog({
        title: i18n_aggregate_details,
		maximize: true, 
		closable: true,
		modal:true,
		overlay:{background:'#000000', opacity:0.1},
		width: 800,
        height: 400
    });
}

function caseAggregationResult()
{
	hideById('caseAggregationForm');
	showLoader();
	
	$('#caseAggregationResult').load("caseAggregationResult.action", 
		{
			facilityLB: getFieldValue('facilityLB'),
			dataSetId: getFieldValue('dataSetId')
		}
		, function(){
			$( "#loaderDiv" ).hide();
			showById('caseAggregationResult');
		});
}

function backBtnOnClick()
{
	hideById('caseAggregationResult');
	showById('caseAggregationForm');
}

function toggleResult( id )
{
	$( "#" + id + "-div" ).slideToggle( "fast" );
}

function saveAggregateDataValues( isSaveAll )
{
	lockScreen();
	
	var params = ""
	if( isSaveAll )
	{
		jQuery("input[name=aggregateValues]").each(function( ){
				params += "aggregateValues=" + $(this).val() + "&";
			}); 
	}
	else
	{
		jQuery("input[name=aggregateValues]:checked").each(function( ){
				params += "aggregateValues=" + $(this).val() + "&";
			}); 
	}
		
	$.ajax({
		   type: "POST",
		   url: "saveAggregateDataValue.action",
		   data: params,
		   dataType: "json",
		   success: function(json){
				if( isSaveAll )
				{
					jQuery("input[name=aggregateValues]").each(function( ){
							$(this).replaceWith('<span>' + i18n_saved + '<span>' );
						}); 
				}
				else
				{
					jQuery("input[name=aggregateValues]:checked").each(function( ){
							$(this).replaceWith('<span>' + i18n_saved + '<span>' );
						}); 
				}
				unLockScreen();
				showSuccessMessage( i18n_save_success );
		   }
		});
}

function toogleAllCheckBoxes( tableDiv, checked )
{
	jQuery("#" + tableDiv + " input[name=aggregateValues]").attr( 'checked', checked );
}