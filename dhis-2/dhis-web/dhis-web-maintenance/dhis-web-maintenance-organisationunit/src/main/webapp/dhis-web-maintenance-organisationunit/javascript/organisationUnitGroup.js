
// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showOrganisationUnitGroupDetails( unitId )
{
    jQuery.post( 'getOrganisationUnitGroup.action', { id: unitId },
		function ( json ) {
			setInnerHTML( 'nameField', json.organisationUnitGroup.name );
			setInnerHTML( 'memberCountField', json.organisationUnitGroup.memberCount );
			
			showDetails();
	});
}

// -----------------------------------------------------------------------------
// Remove organisation unit group
// -----------------------------------------------------------------------------

function removeOrganisationUnitGroup( unitGroupId, unitGroupName )
{
	removeItem( unitGroupId, unitGroupName, confirm_to_delete_org_unit_group, 'removeOrganisationUnitGroup.action' );
}