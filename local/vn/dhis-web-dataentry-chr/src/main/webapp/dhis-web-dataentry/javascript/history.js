
// -----------------------------------------------------------------------------
// Comments
// -----------------------------------------------------------------------------

function commentSelected( dataElementId, optionComboId )
{  
    var commentSelector = document.getElementById( 'value[' + dataElementId + ':' + optionComboId + '].comments' );
    var commentField = document.getElementById( 'value[' + dataElementId + ':' + optionComboId + '].comment' );

    var value = commentSelector.options[commentSelector.selectedIndex].value;
    
    if ( value == 'custom' )
    {
        commentSelector.style.display = 'none';
        commentField.style.display = 'inline';
        
        commentField.select();
        commentField.focus();
    }
    else
    {
        commentField.value = value;
        
        saveComment( dataElementId, optionComboId, value );
    }
}

function commentLeft( dataElementId, optionComboId )
{
    var commentField = document.getElementById( 'value[' + dataElementId + ':' + optionComboId + '].comment' );
    var commentSelector = document.getElementById( 'value[' + dataElementId + ':' + optionComboId + '].comments' );

    saveComment( dataElementId, optionComboId, commentField.value );

    var value = commentField.value;
    
    if ( value == '' )
    {
        commentField.style.display = 'none';
        commentSelector.style.display = 'inline';

        commentSelector.selectedIndex = 0;
    }
}

function saveComment( dataElementId, optionComboId, commentValue )
{
    var field = document.getElementById( 'value[' + dataElementId + ':' + optionComboId + '].comment' );                
    var select = document.getElementById( 'value[' + dataElementId + ':' + optionComboId + '].comments' );
    
    field.style.backgroundColor = '#ffffcc';
    select.style.backgroundColor = '#ffffcc';
    
    var commentSaver = new CommentSaver( dataElementId, optionComboId, commentValue );
    commentSaver.save();
}

function CommentSaver( dataElementId_, optionComboId_, value_ )
{
    var SUCCESS = '#ccffcc';
    var ERROR = '#ccccff';

    var dataElementId = dataElementId_;
    var optionComboId = optionComboId_
    var value = value_;
    
    this.save = function()
    {
        var request = new Request();
        request.setCallbackSuccess( handleResponse );
        request.setCallbackError( handleHttpError );
        request.setResponseTypeXML( 'status' );
        request.send( 'saveComment.action?dataElementId=' +
                dataElementId + '&optionComboId=' + optionComboId + '&comment=' + value );
    };
    
    function handleResponse( rootElement )
    {
        var codeElement = rootElement.getElementsByTagName( 'code' )[0];
        var code = parseInt( codeElement.firstChild.nodeValue );
        
        if ( code == 0 )
        {
            markComment( SUCCESS );           
        }
        else
        {
            markComment( ERROR );
            window.alert( i18n_saving_comment_failed_status_code + '\n\n' + code );
        }
    }
    
    function handleHttpError( errorCode )
    {
        markComment( ERROR );
        window.alert( i18n_saving_comment_failed_error_code + '\n\n' + errorCode );
    }
    
    function markComment( color )
    {
        var field = document.getElementById( 'value[' + dataElementId + ':' + optionComboId + '].comment' );                
        var select = document.getElementById( 'value[' + dataElementId + ':' + optionComboId + '].comments' );        

        field.style.backgroundColor = color;
        select.style.backgroundColor = color;
    }
}

function createChart()
{
    var canvas = document.getElementById( 'canvas' );
    var width = parseInt( canvas.style.width ) - 1;
    var height = parseInt( canvas.style.height ) - 1;
    var verticalPadding = height * 0.02;
    var horzontalPadding = verticalPadding;
    var cellWidth = width / historyLength;
    var fontSizePixels = 10;
    var lineHeight = 4; // ?
    var barBase = Math.round( height - verticalPadding * 2 - fontSizePixels * 2 - lineHeight );
    var maxBarHeight = barBase - verticalPadding * 2 - fontSizePixels;
    var barWidth = cellWidth * 0.4;
    var barPaddingLeft = ( cellWidth - barWidth ) / 2;
    var barColor = '#0000ff';
    var barBaseColor = '#808080';
    var textColor = '#000000';
    var minLimitColor = '#008000';
    var maxLimitColor = '#ff0000';
    var averageColor = '#0080ff';

    var g = new jsGraphics( 'canvas' );
    g.setFont( 'sans-serif', fontSizePixels + 'px', Font.PLAIN );
    g.setStroke( 2 );

    g.setColor( barColor );
    var barHeight;

    for ( i = 0; i < historyLength; ++i )
    {
        if ( historyPoints[i][1] )
        {
    	    barHeight = Math.round( maxBarHeight * historyPoints[i][1] / maxValue );
            g.fillRect( cellWidth * i + barPaddingLeft, barBase - barHeight, barWidth, barHeight );
        }
    }

    g.setColor( averageColor );
    var previousPoint = barBase - Math.round( maxBarHeight * historyPoints[0][2] / maxValue );
    var nextPoint;

    for ( i = 1; i < historyLength; ++i )
    {
        nextPoint = barBase - Math.round( maxBarHeight * historyPoints[i][2] / maxValue );
    	g.drawLine( cellWidth * ( i - 1 ) + cellWidth / 2, previousPoint, cellWidth * i + cellWidth / 2, nextPoint );
    	previousPoint = nextPoint;
    }

    g.setColor( barBaseColor );
    g.drawLine( 0, barBase, width, barBase );

    if ( minLimit )
    {
        var minLimitPos = barBase - maxBarHeight * minLimit / maxValue;
        g.setColor( minLimitColor );
    	g.drawLine( 0, minLimitPos, width, minLimitPos );
    }

    if ( maxLimit )
    {
        var maxLimitPos = barBase - maxBarHeight * maxLimit / maxValue;
    	g.setColor( maxLimitColor );
    	g.drawLine( 0, maxLimitPos, width, maxLimitPos );
    }

    g.setColor( textColor );

    for ( i = 0; i < historyLength; ++i )
    {
        if ( historyPoints[i][1] )
        {
    	    barHeight = Math.round( maxBarHeight * historyPoints[i][1] / maxValue );
    		g.drawStringRect( historyPoints[i][1].toFixed(), cellWidth * ( i - 1 ) + cellWidth / 2, barBase - barHeight - verticalPadding - fontSizePixels, cellWidth * 2, 'center' );
        }
var qstr = '<a href="javascript:openAuditForm(';
qstr = qstr+ ouid;
qstr = qstr+ ',';
qstr = qstr +  coid;
qstr = qstr+ ',';
qstr = qstr +  deid;
qstr = qstr+ ',';
qstr = qstr +  historyPoints[i][3];
qstr = qstr +');">';
qstr = qstr + historyPoints[i][0];
qstr = qstr + '</a>';

//        g.drawStringRect( "<a href='javascript:openAuditForm(" + ouid, coid, deid, historyPoints[i][3], historyPoints[i][4], historyPoints[i][5] + ");'>" + historyPoints[i][0] + "</a>", cellWidth * i, barBase + verticalPadding, cellWidth, 'center' );
        g.drawStringRect( qstr , cellWidth * i, barBase + verticalPadding, cellWidth, 'center' );
    }
    
    g.paint();
}

function saveMinLimit( organisationUnitId, dataElementId, optionComboId )
{
    var minLimitField = document.getElementById( "minLimit" );
    var maxLimitField = document.getElementById( "maxLimit" );

    var request = new Request();
    request.setCallbackSuccess( refreshWindow );
    request.setCallbackError( refreshWindow );

    if ( minLimitField.value == '' )
    {
        request.send( 'removeMinMaxLimits.action?organisationUnitId=' + organisationUnitId + '&dataElementId=' + dataElementId + '&optionComboId=' + optionComboId );
    }
    else
    {
        var minLimit = Number( minLimitField.value );
        var maxLimit = Number( maxLimitField.value );
        
        if ( minLimit )
        {
        	if ( minLimit < 0 )
        	{
        	    minLimit = 0;
        	}

            if ( !maxLimit || maxLimit <= minLimit )
            {
                maxLimit = minLimit + 1;
            }

            request.send( 'saveMinMaxLimits.action?organisationUnitId=' + organisationUnitId + '&dataElementId=' + dataElementId + '&optionComboId=' + optionComboId + '&minLimit=' + minLimit + '&maxLimit=' + maxLimit );
        }
        else
        {
            refreshWindow();
        }
    }
}

function saveMaxLimit( organisationUnitId, dataElementId, optionComboId )
{
    var minLimitField = document.getElementById( "minLimit" );
    var maxLimitField = document.getElementById( "maxLimit" );

    var request = new Request();
    request.setCallbackSuccess( refreshWindow );
    request.setCallbackError( refreshWindow );

    if ( maxLimitField.value == '' )
    {
        request.send( 'removeMinMaxLimits.action?organisationUnitId=' + organisationUnitId + '&dataElementId=' + dataElementId + '&optionComboId=' + optionComboId );
    }
    else
    {
        var minLimit = Number( minLimitField.value );
        var maxLimit = Number( maxLimitField.value );
        
        if ( maxLimit )
        {
            if ( maxLimit < 1 )
            {
                maxLimit = 1;
            }

            if ( !minLimit )
            {
                minLimit = 0;
            }
            else if ( minLimit >= maxLimit )
            {
                minLimit = maxLimit - 1;
            }

            request.send( 'saveMinMaxLimits.action?organisationUnitId=' + organisationUnitId + '&dataElementId=' + dataElementId + '&optionComboId=' + optionComboId + '&minLimit=' + minLimit + '&maxLimit=' + maxLimit );
        }
        else
        {
            refreshWindow();
        }
    }
}

function openAuditForm (idou, idco, idde, psd)
    {
		window.open ('../dhis-web-dataentry/viewDataValuePopupForm.action?organisationUnitId=' + idou + '&dataElementComboId=' +idco + '&dataElementId=' + idde +'&periodId=' + psd + '&width=800,height=600,scrollbars=yes');
    }

function refreshWindow()
{
    window.location.reload();
}
