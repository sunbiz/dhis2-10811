jQuery( document ).ready( function()
{
    jQuery( "#name" ).focus();

    validation2( 'updateValidationRuleGroupForm', function( form )
    {
        form.submit();
    }, {
        'beforeValidateHandler' : function()
        {
            selectAllById( 'groupMembers' );
        },
        'rules' : getValidationRules( "validationRuleGroup" )
    } );

    checkValueIsExist( "name", "validateValidationRuleGroup.action", {
        id : getFieldValue( 'id' )
    } );
} );
