
function showValidationRuleDetails( validationId )
{
    jQuery.post( 'getValidationRule.action', { id: validationId }, function ( json ) {
		setInnerHTML( 'nameField', json.validationRule.name );
		
		var description = json.validationRule.description;
		setInnerHTML( 'descriptionField', description ? description : '[' + i18n_none + ']' );
		
		var leftSideDescription = json.validationRule.leftSideDescription;
		setInnerHTML( 'leftSideDescriptionField', leftSideDescription ? leftSideDescription : '[' + i18n_none + ']' );
		
		var operator = json.validationRule.operator;
		setInnerHTML( 'operatorField', i18nalizeOperator( operator ) );
		
		var rightSideDescription = json.validationRule.rightSideDescription;
		setInnerHTML( 'rightSideDescriptionField', rightSideDescription ? rightSideDescription : '[' + i18n_none + ']' );

		showDetails();
	});
}

function i18nalizeOperator( operator )
{
    if ( operator == "equal_to" )
    {
        return i18n_equal_to;
    }
    else if ( operator == "not_equal_to" )
    {
        return i18n_not_equal_to;
    }
    else if ( operator == "greater_than" )
    {
        return i18n_greater_than;       
    }
    else if ( operator == "greater_than_or_equal_to" )
    {
        return i18n_greater_than_or_equal_to;
    }
    else if ( operator == "less_than" )
    {
        return i18n_less_than;
    }
    else if ( operator == "less_than_or_equal_to" )
    {
        return i18n_less_than_or_equal_to;
    }
    
    return null;
}

function removeValidationRule( ruleId, ruleName )
{
	removeItem( ruleId, ruleName, i18n_confirm_delete, 'removeValidationRule.action' );
}
