function showEditLeftSideExpressionForm()
{
    var description = byId( "leftSideDescription" ).value;
    var expression = byId( "leftSideExpression" ).value;
    var textualExpression = byId( "leftSideTextualExpression" ).value;
    var periodTypeName = byId( "periodTypeName" ).value;

    showExpressionForm( "left", description, expression, textualExpression, periodTypeName );
}

function showEditRightSideExpressionForm()
{
    var description = byId( "rightSideDescription" ).value;
    var expression = byId( "rightSideExpression" ).value;
    var textualExpression = byId( "rightSideTextualExpression" ).value;
    var periodTypeName = byId( "periodTypeName" ).value;

    showExpressionForm( "right", description, expression, textualExpression, periodTypeName );
}

function showExpressionForm( side, description, expression, textualExpression, periodTypeName )
{
    $.post( "showEditExpressionForm.action", {
        side : side,
        description : description,
        expression : expression,
        textualExpression : textualExpression,
        periodTypeName : periodTypeName
    }, function( data )
    {
        byId( 'dynamicContent' ).innerHTML = data;
        showPopupWindowById( 'dynamicContent', 755, 450 );
    }, 'html' );
}

function insertText( inputAreaName, inputText )
{
    insertTextCommon( inputAreaName, inputText );

    updateTextualExpression( inputAreaName );
}

function filterDataElements( dataSetName, filterName )
{
    var dataSet = byId( dataSetName );
    var dataSetId = dataSet.options[dataSet.selectedIndex].value;
    var filter = byId( filterName ).value;
    var periodTypeName = getFieldValue( 'periodTypeName' );

    var url = "getFilteredDataElements.action";

    $.getJSON( url, {
        "dataSetId" : dataSetId,
        "periodTypeName" : periodTypeName,
        "filter" : filter
    }, function( json )
    {
        clearListById( "dataElementId" );

        var objects = json.operands;

        for ( var i = 0; i < objects.length; i++ )
        {
            addOptionById( "dataElementId", "[" + objects[i].operandId + "]", objects[i].operandName );
        }

    } );
}

function updateTextualExpression( expressionFieldName )
{
    var expression = document.getElementById( expressionFieldName ).value;

    jQuery.postJSON( '../dhis-web-commons-ajax-json/getExpressionText.action', {
        expression : expression
    }, function( json )
    {
        byId( "textualExpression" ).innerHTML = json.message;
    } );
}

function checkNotEmpty( field, message )
{

    if ( field.value.length == 0 )
    {
        setInnerHTML( field.name + "Info", message );
        $( '#' + field.name ).css( "background-color", "#ffc5c5" );
        return false;
    } else
    {
        setInnerHTML( field.name + "Info", '' );
        $( '#' + field.name ).css( "background-color", "#ffffff" );
    }

    return true;
}

function validateExpression()
{
    var description = byId( "expDescription" ).value;
    var expression = byId( "expression" ).value;

    if ( checkNotEmpty( byId( "expDescription" ), i18n_description_not_null ) == false )
        return;
    if ( checkNotEmpty( byId( "expression" ), i18n_expression_not_null ) == false )
        return;

    jQuery.postJSON( '../dhis-web-commons-ajax-json/getExpressionText.action', {
        expression : expression
    }, function( json )
    {
        byId( "textualExpression" ).innerHTML = json.message;
        if ( json.response == 'error' )
        {
            $( '#expression' ).css( "background-color", "#ffc5c5" );
            return;
        }
        var description = byId( "expDescription" ).value;
        var expression = byId( "expression" ).value;
        var textualDescription = byId( "textualExpression" ).innerHTML;
        var side = byId( "side" ).value;
        saveExpression( side, description, expression, textualDescription );
        disable( 'periodTypeName' );
        return true;
    } );
}

function validateExpressionReceived( xmlObject )
{
    var type = xmlObject.getAttribute( 'type' );
    var message = xmlObject.firstChild.nodeValue;

    if ( type == "success" )
    {
        var description = byId( "expDescription" ).value;
        var expression = byId( "expression" ).value;
        var textualDescription = byId( "textualExpression" ).innerHTML;
        var side = byId( "side" ).value;
        saveExpression( side, description, expression, textualDescription );
        disable( 'periodTypeName' );
    } else if ( type == "error" )
    {
        byId( "textualExpression" ).innerHTML = message;
    }
}

function saveExpression( side, description, expression, textualDescription )
{
    if ( side == "left" )
    {
        byId( "leftSideDescription" ).value = description;
        byId( "leftSideExpression" ).value = expression;
        byId( "leftSideTextualExpression" ).value = textualDescription;
    } else if ( side == "right" )
    {
        byId( "rightSideDescription" ).value = description;
        byId( "rightSideExpression" ).value = expression;
        byId( "rightSideTextualExpression" ).value = textualDescription;
    }

    hideById( 'dynamicContent' );
    unLockScreen();
}

// -----------------------------------------------------------------------------
// Set Null Expression
// -----------------------------------------------------------------------------

function setNullExpression()
{
    // set left-expression
    var description = byId( "leftSideDescription" ).value;
    byId( "leftSideExpression" ).value = '';
    byId( "leftSideTextualExpression" ).value = '';
    saveExpression( 'left', description, '', '' );

    // set right-expression
    description = byId( "rightSideDescription" ).value;
    byId( "rightSideExpression" ).value = '';
    byId( "rightSideTextualExpression" ).value = '';
    saveExpression( 'right', description, '', '' );

    // Show periodType combo
    enable( 'periodTypeName' );
}
