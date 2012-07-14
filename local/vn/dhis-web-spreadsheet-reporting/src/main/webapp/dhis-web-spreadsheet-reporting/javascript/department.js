// -----------------------------------------------------------------------------
// Organisation unit selection listener
// -----------------------------------------------------------------------------

$( document ).ready( function()
{
    selection.setAutoSelectRoot( false );
    selection.setRootUnselectAllowed( true );
    selection.setListenerFunction( organisationUnitSelected, true );
} );

function organisationUnitSelected( orgUnitIds )
{
    window.location.href = 'department.action';
}

// -----------------------------------------------------------------------------
// Export to PDF
// -----------------------------------------------------------------------------

function exportPDF( type )
{
	var params = "type=" + type;
	
	exportPdfByType( type, params );
}

// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showOrganisationUnitDetails( unitId )
{
    jQuery.post( '../dhis-web-commons-ajax-json/getOrganisationUnit.action',
		{ id: unitId }, function ( json ) {
		setInnerHTML( 'nameField', json.organisationUnit.name );
		setInnerHTML( 'shortNameField', json.organisationUnit.shortName );
		setInnerHTML( 'descriptionField', json.organisationUnit.description );
		setInnerHTML( 'openingDateField', json.organisationUnit.openingDate );

		var orgUnitCode = json.organisationUnit.code;
		setInnerHTML( 'codeField', orgUnitCode ? orgUnitCode : '[' + none + ']' );

		var closedDate = json.organisationUnit.closedDate;
		setInnerHTML( 'closedDateField', closedDate ? closedDate : '[' + none + ']' );

		var commentValue = json.organisationUnit.comment;
		setInnerHTML( 'commentField', commentValue ? commentValue.replace( /\n/g, '<br>' ) : '[' + none + ']' );

		var active = json.organisationUnit.active;
		setInnerHTML( 'activeField', active == 'true' ? yes : no );

		var url = json.organisationUnit.url;
		setInnerHTML( 'urlField', url ? '<a href="' + url + '">' + url + '</a>' : '[' + none + ']' );

		var lastUpdated = json.organisationUnit.lastUpdated;
		setInnerHTML( 'lastUpdatedField', lastUpdated ? lastUpdated : '[' + none + ']' );

		showDetails();
	});
}

// -----------------------------------------------------------------------------
// Remove organisation unit
// -----------------------------------------------------------------------------

function removeOrganisationUnit( unitId, unitName )
{
    removeItem( unitId, unitName, confirm_to_delete_org_unit, '../dhis-web-maintenance-organisationunit/removeOrganisationUnit.action', subtree.refreshTree );
}

function nameChanged()
{
	/* fail quietly if previousName is not available */
	if( previousName === undefined ) {
		return;
	}
	
    var nameField = document.getElementById( 'name' );
    var shortNameField = document.getElementById( 'shortName' );
    var maxLength = parseInt( shortNameField.maxLength );
    
    if ( previousName != nameField.value
        && nameField.value.length <= maxLength
        && ( shortNameField.value == previousName
          || shortNameField.value.length == 0 ))
    {
            shortNameField.value = nameField.value;
    }
    
    previousName = nameField.value;
}