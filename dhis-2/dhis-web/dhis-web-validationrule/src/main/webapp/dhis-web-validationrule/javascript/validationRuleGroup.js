// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showValidationRuleGroupDetails( id )
{
	jQuery.post( 'getValidationRuleGroup.action', { 'id': id }, function ( json ) {
		setInnerHTML( 'nameField', json.validationRuleGroup.name );
		setInnerHTML( 'descriptionField', json.validationRuleGroup.description );
		setInnerHTML( 'memberCountField', json.validationRuleGroup.memberCount );

		showDetails();
	});
}

// -----------------------------------------------------------------------------
// Remove data element group
// -----------------------------------------------------------------------------

function removeValidationRuleGroup( validationRuleGroupId, validationRuleGroupName )
{
    removeItem( validationRuleGroupId, validationRuleGroupName, i18n_confirm_delete, 'removeValidationRuleGroup.action' );
}

// -----------------------------------------------------------------------------
// Select lists
// -----------------------------------------------------------------------------

function initLists()
{
    for ( var id in groupMembers )
    {
        $( "#groupMembers" ).append( $( "<option></option>" ).attr( "value", id ).text( groupMembers[id] ) );
    }

    for ( var id in availableValidationRules )
    {
        $( "#availableValidationRules" ).append(
                $( "<option></option>" ).attr( "value", id ).text( availableValidationRules[id] ) );
    }
}

function filterGroupMembers()
{
    var filter = document.getElementById( 'groupMembersFilter' ).value;
    var list = document.getElementById( 'groupMembers' );

    list.options.length = 0;

    for ( var id in groupMembers )
    {
        var value = groupMembers[id];

        if ( value.toLowerCase().indexOf( filter.toLowerCase() ) != -1 )
        {
            list.add( new Option( value, id ), null );
        }
    }
}

function filterAvailableValidationRules()
{
    var filter = document.getElementById( 'availableValidationRulesFilter' ).value;
    var list = document.getElementById( 'availableValidationRules' );

    list.options.length = 0;

    for ( var id in availableValidationRules )
    {
        var value = availableValidationRules[id];

        if ( value.toLowerCase().indexOf( filter.toLowerCase() ) != -1 )
        {
            list.add( new Option( value, id ), null );
        }
    }
}

function addGroupMembers()
{
    var list = document.getElementById( 'availableValidationRules' );

    while ( list.selectedIndex != -1 )
    {
        var id = list.options[list.selectedIndex].value;

        list.options[list.selectedIndex].selected = false;

        groupMembers[id] = availableValidationRules[id];

        delete availableValidationRules[id];
    }

    filterGroupMembers();
    filterAvailableValidationRules();
}

function removeGroupMembers()
{
    var list = document.getElementById( 'groupMembers' );

    while ( list.selectedIndex != -1 )
    {
        var id = list.options[list.selectedIndex].value;

        list.options[list.selectedIndex].selected = false;

        availableValidationRules[id] = groupMembers[id];

        delete groupMembers[id];
    }

    filterGroupMembers();
    filterAvailableValidationRules();
}
