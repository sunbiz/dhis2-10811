jQuery( document ).ready( function()
{
    validation2( 'updateDataDictionaryForm', function( form )
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
} );
