
function performMaintenance()
{
    var clearAnalytics = document.getElementById( "clearAnalytics" ).checked;
    var clearDataMart = document.getElementById( "clearDataMart" ).checked;
    var dataMartIndex = document.getElementById( "dataMartIndex" ).checked;
    var zeroValues = document.getElementById( "zeroValues" ).checked;
    var dataSetCompleteness = document.getElementById( "dataSetCompleteness" ).checked;
    var prunePeriods = document.getElementById( "prunePeriods" ).checked;
    
    if ( clearAnalytics || clearDataMart || dataMartIndex || zeroValues || dataSetCompleteness || prunePeriods )
    {
        setWaitMessage( i18n_performing_maintenance );
        
        var params = "clearAnalytics=" + clearAnalytics + 
        	"&clearDataMart=" + clearDataMart + 
            "&dataMartIndex=" + dataMartIndex +
            "&zeroValues=" + zeroValues +
            "&dataSetCompleteness=" + dataSetCompleteness +
            "&prunePeriods=" + prunePeriods;
        
		$.ajax({
			   type: "POST",
			   url: "performMaintenance.action",
			   data: params,
			   dataType: "xml",
			   success: function(result){
					setMessage( i18n_maintenance_performed );
			   }
			});
    }
    else
    {
        setMessage( i18n_select_options );
    }
}
