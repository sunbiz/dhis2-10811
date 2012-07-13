function beforeSubmit()
{
    memberValidator = jQuery( "#memberValidator" );
    memberValidator.children().remove();

    jQuery.each( jQuery( "#groupMembers" ).children(), function( i, item )
    {
        item.selected = 'selected';
        memberValidator.append( '<option value="' + item.value + '" selected="selected">' + item.value + '</option>' );
    } );
}

// -----------------------------------------------------------------------------
// Validate Add Data Element Group
// -----------------------------------------------------------------------------

function validateAddDataElementGroupSet()
{
   $.post( 'validateDataElementGroupSet.action',{name:getFieldValue( "name" )}
		, function( messageElement ) 
		{
			messageElement = messageElement.getElementsByTagName( 'message' )[0];
			var type = messageElement.getAttribute( "type" );
			if ( type == "success" )
			{
				selectAllById( "groupMembers" );
				document.forms['addDataElementGroupSet'].submit();
			} 
			else
			{
				setMessage( messageElement.firstChild.nodeValue );
			}
		} );
}

// -----------------------------------------------------------------------------
// Delete Data Element Group
// -----------------------------------------------------------------------------

function deleteDataElementGroupSet( groupSetId, groupSetName )
{
    removeItem( groupSetId, groupSetName, i18n_confirm_delete, "deleteDataElementGroupSet.action" );
}

// -----------------------------------------------------------------------------
// Show Data Element Group Set details
// -----------------------------------------------------------------------------

function showDataElementGroupSetDetails( id )
{
    jQuery.post( '../dhis-web-commons-ajax-json/getDataElementGroupSet.action', { id: id },
		function ( json ) {
			setInnerHTML( 'nameField', json.dataElementGroupSet.name );
			setInnerHTML( 'memberCountField', json.dataElementGroupSet.memberCount );

			showDetails();
	});
}
