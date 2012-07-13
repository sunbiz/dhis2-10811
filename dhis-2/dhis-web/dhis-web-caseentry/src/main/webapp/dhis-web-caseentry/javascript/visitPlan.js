isAjax = true;

function organisationUnitSelected( orgUnits )
{
    window.location.href = 'visitplan.action';
}

selection.setListenerFunction( organisationUnitSelected );

function sortByAttribute( sortingAttributeId )
{
	var url = "visitplanSortByAttribute.action";
	
	showLoader();
	
	if ( url.length > 0 )
	{
		jQuery( "#contentDiv" ).load( url,
			{ "sortingAttributeId": sortingAttributeId },
			function()
			{
				jQuery( "table.listTable tbody tr" ).removeClass( "listRow listAlternateRow" );
				jQuery( "table.listTable tbody tr:odd" ).addClass( "listAlternateRow" );
				jQuery( "table.listTable tbody tr:even" ).addClass( "listRow" );
				jQuery( "table.listTable tbody" ).trigger( "update" );

				hideLoader();
			}
		);
	}
}
