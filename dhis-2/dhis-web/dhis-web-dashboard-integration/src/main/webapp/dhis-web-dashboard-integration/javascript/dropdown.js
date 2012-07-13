
$( document ).ready( function()
{
	$( "#interpretationArea" ).autogrow();
	
	var viewportWidth = parseInt( $( window ).width() );
	var linkWidth = parseInt( 338 );
	var chartWidth = parseInt( 325 );
	
	if ( viewportWidth == undefined )
	{
		viewportWidth = parseInt( 1366 );
	}
	
	var noCharts = 2 * Math.floor( ( viewportWidth - linkWidth ) / chartWidth ); 
		
	$( "#contentDiv" ).load( "provideContent.action?noCharts=" + noCharts + "&_dc=" + getRandomNumber() );
} );

function setAreaItem( area, item )
{
    $.get( "setAreaItem.action", {
        'area' : area,
        'item' : item
    }, function()
    {
        window.location.href = "index.action";
    } );
}

function clearArea( area )
{
    $.get( "clearArea.action", {
        'area' : area
    }, function()
    {
        window.location.href = "index.action";
    } );
}

function viewChart( url, name )
{
    var width = 820;
    var height = 520;
    var title = i18n_viewing + " " + name;

    $( "#chartImage" ).attr( "src", url );
    $( "#chartView" ).dialog( {
        autoOpen : true,
        modal : true,
        height : height + 65,
        width : width + 25,
        resizable : false,
        title : title
    } );
}

function explore( uid )
{
	window.location.href = "../dhis-web-visualizer/app/index.html?id=" + uid;
}

function viewShareForm( uid, name )
{	
	$( "#interpretationChartId" ).val( uid );
	
	var title = i18n_share_your_interpretation_of + " " + name;
	
	$( "#shareForm" ).dialog( {
		modal: true,
		width: 550,
		resizable: false,
		title: title
	} );
}

function shareInterpretation()
{
    var chartId = $( "#interpretationChartId" ).val();
    var text = $( "#interpretationArea" ).val();
    
    if ( text.length && $.trim( text ).length )
    {
    	text = $.trim( text );
    	
	    var url = "../api/interpretations/chart/" + chartId;
	    
	    // TODO url += ( ou && ou.length ) ? "?ou=" + ou : "";
	    
	    $.ajax( url, {
	    	type: "POST",
	    	contentType: "text/html",
	    	data: text,
	    	success: function() {
	    		$( "#shareForm" ).dialog( "close" );
	    		$( "#interpretationArea" ).val( "" );
	    		setHeaderDelayMessage( i18n_interpretation_was_shared );
	    	}    	
	    } );
    }
}
