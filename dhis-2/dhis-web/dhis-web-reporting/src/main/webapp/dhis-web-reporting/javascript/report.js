function addReport()
{
	if ( !hasText( "upload" ) )
	{
		setMessage( i18n_please_specify_file );
		return false;
	}
	
	$.postJSON( "validateReport.action", { id:$( "#id" ).val(), "name":$( "#name" ).val() }, function( json )
	{
		if ( json.response == "input" )
		{
			setMessage( json.message );
			return false;
		}
		else if ( json.response == "success" )
		{
        	$( "#reportForm" ).submit();
		}
	} );
}

function removeReport( id )
{
    removeItem( id, "", i18n_confirm_remove_report, "removeReport.action" );
}

function addToDashboard( id )
{
    var dialog = window.confirm( i18n_confirm_add_to_dashboard );

    if ( dialog )
    {
        $.get( "addReportToDashboard.action?id=" + id );
    }
}
