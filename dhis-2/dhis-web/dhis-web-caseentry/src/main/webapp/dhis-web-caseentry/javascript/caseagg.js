
function validationCaseAggregation()
{
	$.get( 'validateCaseAggregation.action', 
		{}, validationCaseAggregationCompleted, 'xml' );				
}

function validationCaseAggregationCompleted( message )
{
    var type = $(message).find('message').attr('type');
    if( type == "success" ){
        caseAggregationResult();
    }
    else{
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
			dataSetId: getFieldValue('dataSetId'),
			startDate: getFieldValue('startDate'),
			endDate: getFieldValue('endDate')
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