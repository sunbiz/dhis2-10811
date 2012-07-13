/**
 * Global variables
 */

isImport = false;
idTemp = null;
importlist = null;
importItemIds = new Array();

htmlStyle = ["<style type='text/css'>"];
htmlStyle.push( "td.printclass { font-size: 12px; }" );
htmlStyle.push( ".ui-preview-table{ border-collapse: collapse; }" );
htmlStyle.push( ".ui-preview-normal{ font-weight: bold; color: blue }" );
htmlStyle.push( ".ui-widget-content { border: 1px solid #a6c9e2; background: #fcfdfd url(images/ui-bg_inset-hard_100_fcfdfd_1x100.png) 50% bottom repeat-x; color: #222222; }" );
htmlStyle.push( "</style>" );

// ----------------------------------------------------------------------
// Methods
// ----------------------------------------------------------------------

function validatePreviewReport( isAdvanced )
{
	var exportReports = jQuery( 'select[id=exportReport]' ).children( 'option:selected' );

	if ( exportReports.length == 0 )
	{
		showErrorMessage( i18n_specify_export_report );
		return;
	}
	
	var periodIndex = getFieldValue( 'selectedPeriodId2' );
	
	if ( periodIndex.length == 0 )
	{
		showErrorMessage( i18n_specify_periodtype_or_period );
		return;
	}
	
	var url = 'validateGenerateReport.action?';
	
	jQuery.each( exportReports, function ( i, item )
	{
		url += 'exportReportIds=' + item.value.split( "_" )[0] + '&';
	} );
	
	url = url.substring( 0, url.length - 1 );
	
	if ( url && url != '' )
	{
		hideExportDiv();
		lockScreen();

		jQuery.postJSON( url,
		{
			'periodIndex': periodIndex
		},
		function( json )
		{
			if ( json.response == "success" ) {
				if ( isAdvanced ) {
					previewAdvandReport();
				}
				else previewExportReport();
			}
			else {
				unLockScreen();
				showWarningMessage( json.message );
			}
		});
	}
}

function previewExportReport()
{
	jQuery.post( "previewExportReport.action", {}, previewExportReportReceived );
}

function previewAdvandReport() 
{	
	jQuery.post( "previewAdvancedExportReport.action", { organisationGroupId: byId( "availableOrgunitGroups" ).value }, previewExportReportReceived );
}

function previewExportReportReceived( parentElement ) 
{
	var aKey 	= new Array();
	var aMerged = new Array();	
	var cells 	= parentElement.getElementsByTagName( 'cell' );

	for (var i  = 0 ; i < cells.length ; i ++)
	{	
		aKey[i]		= cells[i].getAttribute( 'iKey' );
		aMerged[i]	= cells[i].firstChild.nodeValue;
	}

	var _index		= 0;
	var _orderSheet	= 0;
	var _sPattern	= "";
	var _rows 		= "";
	var _cols 		= "";
	var _sheets		= parentElement.getElementsByTagName( 'sheet' );
	var _sHTML		= [];
	var tabsHTML 	= ['<div id="tabs"><ul>'];

	for (var s = 0 ; s < _sheets.length ; s ++)
	{
		// Create tab name
		tabsHTML.push( '<li><a href="#tabs-', s, '">', getElementValue( _sheets[s], "name" ), '</a></li>' );

		_rows 		= _sheets[s].getElementsByTagName( 'row' );
		_orderSheet	= getRootElementAttribute( _sheets[s], "id" );

		_sHTML.push( '<div id="tabs-', s, '">' );
		_sHTML.push( "<table class='ui-preview-table'>" );

		for (var i = 0 ; i < _rows.length ; i ++)
		{
			_index	= 0;
			_sHTML.push( "<tr>" );

			_cols 	= _rows[i].getElementsByTagName( 'col' );

			for (var j 	= 0 ; j < _cols.length ; )
			{
				var _number	= getRootElementAttribute( _cols[j], 'no' );
				var keyId 	= getRootElementAttribute( _cols[j], 'id' );

				// Printing out the unformatted cells
				for (; _index < _number ; _index ++)
				{
					_sHTML.push( "<td/>" );
				}

				if ( _index == _number )
				{
					var _sData		= getElementValue( _cols[j], 'data' );
					var _align		= getElementAttribute( _cols[j], 'format', 'align' );
					var _border		= getElementAttribute( _cols[j], 'format', 'border' );
					var _width		= getElementAttribute( _cols[j], 'format', 'width' );

					// If this cell is merged - Key's form: Sheet#Row#Col
					_sPattern 		=  _orderSheet + "#" + i + "#" + _number;
					var _colspan 	= getMergedNumberForEachCell( aKey, _sPattern, aMerged );

					// Jumping for <For Loop> AND <Empty Cells>
					j 		= Number(j) + Number(_colspan);
					_index 	= Number(_index) + Number(_colspan);

					_sHTML.push( "<td align='", _align, "' width='", _width, "px' colspan='", _colspan, "'" );
					_sHTML.push( " class='printclass" );
					_sHTML.push( _border > 0 ? " ui-widget-content" : "" );

					// Preview without importing
					if ( keyId && keyId.length > 0 )
					{
						_sHTML.push( " ui-preview-unselected' id='", keyId );
					}
					else if ( !isImport && isRealNumber( _sData.replace( /[.,]/g, "" ) ) )
					{
						_sHTML.push( " ui-preview-normal" );
					}
					_sHTML.push( "'>", _sData, "</td>" );
				}
			}
			_sHTML.push( "</tr>" );
		}
		_sHTML.push( "</table></div>" );
	}

	tabsHTML.push( '</ul>', _sHTML.join(''), '</div>' );
	
	jQuery( '#previewDiv' ).html( tabsHTML.join('') );
	jQuery( '#tabs' ).tabs({ collapsible : true });
	enable( 'printExcelReportButton' );
	applyStyleIntoPreview();
	unLockScreen();
}

function applyStyleIntoPreview()
{
	importlist = jQuery( 'table.ui-preview-table tr > td.ui-preview-unselected' );

	if ( importlist.length > 0 )
	{
		importlist.mouseover(function()
		{
			jQuery(this).addClass( 'ui-preview-mouseover' );
		});

		importlist.mouseout(function()
		{
			jQuery(this).removeClass( 'ui-preview-mouseover' );
		});

		importlist.click(function()
		{
			idTemp = jQuery(this).attr( 'id' ) + "_" + jQuery(this).html();
			
			if ( jQuery.inArray(idTemp, importItemIds) != -1 )
			{
				importItemIds = jQuery.grep( importItemIds, function(value) {
					return value != idTemp
				});
			}
			else importItemIds.push( idTemp );
			
			jQuery(this).toggleClass( 'ui-preview-selected' );
		});
	}
}

function getMergedNumberForEachCell( aKey, sKey, aMerged )
{
	for (var i = 0 ; i < aKey.length ; i ++)
	{
		if ( aKey[i] == sKey )
		{
			return Number(aMerged[i]);
		}
	}
	return 1;
}

function printExportReport()
{
	var tab = jQuery('#tabs').tabs('option', 'selected');
	jQuery( "#tabs-" + tab ).jqprint( {CSS : htmlStyle.join('')} );
}