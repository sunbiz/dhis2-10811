// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showInventoryTypeDetails( inventoryTypeId )
{
	jQuery.getJSON( 'getInventoryType.action', { id: inventoryTypeId },
		function ( json ) {
			setInnerHTML( 'nameField', json.inventoryType.name );	
			setInnerHTML( 'descriptionField', json.inventoryType.description );
			
			var tracking = ( json.inventoryType.tracking == 'true') ? i18n_yes : i18n_no;
			setInnerHTML( 'trackingField', tracking );
			
			setInnerHTML( 'catalogTypeField', json.inventoryType.catalogType );    
	   
			showDetails();
	});
}

// -----------------------------------------------------------------------------
// Remove InvenotryType
// -----------------------------------------------------------------------------
function removeInventoryType( invenotryTypeId, name )
{
	removeItem( invenotryTypeId, name, i18n_confirm_delete, 'removeInventoryType.action' );	
}

//-----------------------------------------------------------------------------
//Move Table Row Up and Down
//-----------------------------------------------------------------------------

/**
* Moves the selected option in a select list up one position.
* 
* @param listId the id of the list.
*/
function moveUpSelectedOption( listId ){
  var list = document.getElementById( listId );
  for ( var i = 0; i < list.length; i++ ) {
      if ( list.options[ i ].selected ) {
          if ( i > 0 ) {	// Cannot move up the option at the top
              var precedingOption = new Option( list.options[ i - 1 ].text, list.options[ i - 1 ].value );
              var currentOption = new Option( list.options[ i ].text, list.options[ i ].value );

              list.options[ i - 1 ] = currentOption; // Swapping place in the
                                                      // list
              list.options[ i - 1 ].selected = true;
              list.options[ i ] = precedingOption;
          }
      }
  }
}
/**
* Moves the selected option in a list down one position.
* 
* @param listId the id of the list.
*/
function moveDownSelectedOption( listId ) {
  var list = document.getElementById( listId );

  for ( var i = list.options.length - 1; i >= 0; i-- ) {
      if ( list.options[ i ].selected ) {
          if ( i < list.options.length - 1 ) { 	// Cannot move down the
                                                  // option at the bottom
              var subsequentOption = new Option( list.options[ i + 1 ].text, list.options[ i + 1 ].value );
              var currentOption = new Option( list.options[ i ].text, list.options[ i ].value );

              list.options[ i + 1 ] = currentOption; // Swapping place in the
                                                      // list
              list.options[ i + 1 ].selected = true;
              list.options[ i ] = subsequentOption;
          }
      }
  }
}


//-----------------------------------------------------------------------------
//select unselect InventoryTypeAttribute
//-----------------------------------------------------------------------------

function selectInventoryTypeAttributes()
{
	var selectedInventoryTypeAttributeList = jQuery("#selectedInventoryTypeAttributeList");
	jQuery("#availableInventoryTypeAttributeList").children().each(function(i, item){
		if( item.selected ){
			html = "<tr class='selected' id='" + item.value + "' ondblclick='unSelectInventoryTypeAttribute( this )'><td onclick='select(this)'>" + item.text + "</td>";
			html += "<td align='center'><input type='checkbox' name='forDisplay' value='" + item.value + "'></td>";
			html += "</tr>";
			selectedInventoryTypeAttributeList.append( html );
			jQuery( item ).remove();
		}
	});
}


function unSelectInventoryTypeAttributes()
{
	var availableInventoryTypeAttributeList = jQuery("#availableInventoryTypeAttributeList");
	jQuery("#selectedInventoryTypeAttributeList").find("tr").each( function( i, item ){
		item = jQuery(item);
		if( item.hasClass("selected") )
		{		
			availableInventoryTypeAttributeList.append( "<option value='" + item.attr( "id" ) + "' selected='true'>" + item.find("td:first").text() + "</option>" );
			item.remove();
		}
	});
}


function selectAllInventoryTypeAttributes()
{
	var selectedInventoryTypeAttributeList = jQuery("#selectedInventoryTypeAttributeList");
	jQuery("#availableInventoryTypeAttributeList").children().each(function(i, item){
		html = "<tr class='selected' id='" + item.value + "' ondblclick='unSelectInventoryTypeAttribute( this )'><td onclick='select(this)'>" + item.text + "</td>";
		html += "<td align='center'><input type='checkbox' name='forDisplay' value='" + item.value + "'></td>";
		html += "</tr>";
		selectedInventoryTypeAttributeList.append( html );
		jQuery( item ).remove();
	});
}


function unSelectAllInventoryTypeAttributes()
{
	var availableInventoryTypeAttributeList = jQuery("#availableInventoryTypeAttributeList");
	jQuery("#selectedInventoryTypeAttributeList").find("tr").each( function( i, item ){
		item = jQuery(item);
		availableInventoryTypeAttributeList.append( "<option value='" + item.attr( "id" ) + "' selected='true'>" + item.find("td:first").text() + "</option>" );
		item.remove();
	});
}



function unSelectInventoryTypeAttribute( element )
{
	element = jQuery(element);	
	jQuery("#availableInventoryTypeAttributeList").append( "<option value='" + element.attr( "id" ) + "' selected='true'>" + element.find("td:first").text() + "</option>" );
	element.remove();
}


function select( element )
{
	element = jQuery( element ).parent();
	if( element.hasClass( 'selected') ) element.removeClass( 'selected' );
	else element.addClass( 'selected' );
}
//-----------------------------------------------------------------------------
//Move Table Row Up and Down
//-----------------------------------------------------------------------------


function moveUpInventoryTypeAttribute()
{
	var selectedList = jQuery("#selectedInventoryTypeAttributeList");

	jQuery("#selectedInventoryTypeAttributeList").find("tr").each( function( i, item ){
		item = jQuery(item);
		if( item.hasClass("selected") )
		{
			var prev = item.prev('#selectedInventoryTypeAttributeList tr');
			if (prev.length == 1) 
			{ 
				prev.before(item);
			}
		}
	});
}

function moveDownInventoryTypeAttribute()
{
	var selectedList = jQuery("#selectedInventoryTypeAttributeList");
	var items = new Array();
	jQuery("#selectedInventoryTypeAttributeList").find("tr").each( function( i, item ){
		items.push(jQuery(item));
	});
	
	for( var i=items.length-1;i>=0;i--)
	{	
		var item = items[i];
		if( item.hasClass("selected") )
		{
			var next = item.next('#selectedInventoryTypeAttributeList tr');
			if (next.length == 1) 
			{ 
				next.after(item);
			}
		}
	}
}






