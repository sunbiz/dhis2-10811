var MODE_REPORT = "report";
var MODE_TABLE = "table";

// -----------------------------------------------------------------------------
// Validation
// -----------------------------------------------------------------------------

function validationError()
{
    if ( $( "#selectionTree" ).length && !selectionTreeSelection.isSelected() )
    {
        setMessage( i18n_please_select_unit );
        return true;
    }

    return false;
}

// -----------------------------------------------------------------------------
// Report
// -----------------------------------------------------------------------------

function viewReport( type )
{
	var reportType = type != null && type != "" ? type : "pdf";

    if ( validationError() )
    {
        return false;
    }
    
    var mode = $( "#mode" ).val();
    var uid = $( "#uid" ).val();

    setMessage( i18n_process_completed );

    if ( mode == MODE_REPORT )
    {
    	window.location.href = "../api/reports/" + uid + "/data." + type + "?" + getUrlParams();
    } 
    else // MODE_TABLE
    {
        window.location.href = "exportTable.action?uid=" + uid + "&type=html&" + getUrlParams();
    }
}

function getUrlParams()
{
    var url = "";

    if ( $( "#reportingPeriod" ).length )
    {
        url += "pe=" + $( "#reportingPeriod" ).val() + "&";
    }

    if ( selectionTreeSelection.isSelected() )
    {
        url += "ou=" + selectionTreeSelection.getSelectedUid();
    }

    return url;
}

// -----------------------------------------------------------------------------
// Report table
// -----------------------------------------------------------------------------

function exportReport( type, uid )
{
    var url = "exportTable.action?uid=" + uid + "&type=" + type + "&useLast=true";

    url += $( "#id" ).length ? ( "&id=" + $( "#id" ).val() ) : "";

    window.location.href = url;
}

function viewShareForm()
{
	$( "#shareForm" ).dialog( {
		modal : true,
		width : 550,
		resizable: false,
		title : i18n_share_your_interpretation
	} );
}

function shareInterpretation( uid, ou )
{
    var text = $( "#interpretationArea" ).val();
    
    if ( text.length && $.trim( text ).length )
    {
    	text = $.trim( text );
    	
	    var url = "../api/interpretations/reportTable/" + uid;
	    
	    url += ( ou && ou.length ) ? "?ou=" + ou : "";
	    
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
