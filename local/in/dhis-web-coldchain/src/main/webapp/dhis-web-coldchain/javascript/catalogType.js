// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showCalatogTypeDetails( catalogTypeId )
{
	jQuery.getJSON( 'getCatalogTypeDetails.action', { id: catalogTypeId }, function ( json ) {
		setInnerHTML( 'nameField', json.catalogType.name );	
		setInnerHTML( 'descriptionField', json.catalogType.description );
		setInnerHTML( 'catalogTypeattributesCountField', json.catalogType.catalogTypeAttributeCount );
		setInnerHTML( 'dataEntryFormNameField', json.catalogType.dataEntryForm );
	   
		showDetails();
	});
}


// -----------------------------------------------------------------------------
// select/ Catalog Type Attributes
// -----------------------------------------------------------------------------

function selectCatalogTypeAttributes()
{
	var selectedList = jQuery("#selectedList");
	jQuery("#availableList").children().each(function(i, item){
		if( item.selected ){
			html = "<tr class='selected' id='" + item.value + "' ondblclick='unCatalogTypeAttribute( this )'><td onclick='select(this)'>" + item.text + "</td>";
			html += "</tr>";
			selectedList.append( html );
			jQuery( item ).remove();
		}
	});
}

function unselectCatalogTypeAttributes()
{
	var availableList = jQuery("#availableList");
	jQuery("#selectedList").find("tr").each( function( i, item ){
		item = jQuery(item);
		if( item.hasClass("selected") )
		{		
			availableList.append( "<option value='" + item.attr( "id" ) + "' selected='true'>" + item.find("td:first").text() + "</option>" );
			item.remove();
		}
	});
}




//-----------------------------------------------------------------------------
//Move Table Row Up and Down
//-----------------------------------------------------------------------------


function moveUpCatalogTypeAttribute()
{
	var selectedList = jQuery("#selectedList");

	jQuery("#selectedList").find("tr").each( function( i, item ){
		item = jQuery(item);
		if( item.hasClass("selected") )
		{
			var prev = item.prev('#selectedList tr');
			if (prev.length == 1) 
			{ 
				prev.before(item);
			}
		}
	});
}

function moveDownCatalogTypeAttribute()
{
	var selectedList = jQuery("#selectedList");

	jQuery("#selectedList").find("tr").each( function( i, item ){
		item = jQuery(item);
		if( item.hasClass("selected") )
		{
			var next = item.next('#selectedList tr');
			if (next.length == 1) 
			{ 
				next.after(item);
			}
		}
	});
}

function unCatalogTypeAttribute( element )
{
	element = jQuery(element);	
	jQuery("#availableList").append( "<option value='" + element.attr( "id" ) + "' selected='true'>" + element.find("td:first").text() + "</option>" );
	element.remove();
}

function select( element )
{
	element = jQuery( element ).parent();
	if( element.hasClass( 'selected') ) element.removeClass( 'selected' );
	else element.addClass( 'selected' );
}
