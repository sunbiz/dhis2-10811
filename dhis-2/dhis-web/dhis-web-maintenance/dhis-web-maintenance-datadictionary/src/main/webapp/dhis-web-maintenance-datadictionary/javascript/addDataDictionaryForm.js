jQuery( document ).ready( function()
{
    validation2( 'addDataDictionaryForm', function( form )
    {
        form.submit();
    }, {
        'beforeValidateHandler' : function()
        {
            listValidator( 'memberValidator', 'selectedDataElements' );
            listValidator( 'memberValidatorIn', 'selectedIndicators' );
        },
        'rules' : getValidationRules( "dataDictionary" )
    } );

    checkValueIsExist( "name", "validateDataDictionary.action" );
} );
