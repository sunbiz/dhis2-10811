

var catalogTypeAttributeSelector;


jQuery(function(){
	catalogTypeAttributeSelector = jQuery("#catalogTypeAttributeSelection").dialog({
		title: i18n_catalogType_attribute,
		height: 350,
		width:350,
		autoOpen: false,
		zIndex:99999
	});




});


function openCatalogTypeAttributeSelector()
{
	catalogTypeAttributeSelector.dialog("open");
}

// delete dataentry form
function deleteCatalogDataEntryForm( catalogDataEntryFormId, catalogTypeId )
{
	if( window.confirm( i18n_delete_catalogType_data_entry_confirm ) )
	{
		window.location.href = 'delCatalogDataEntryForm.action?dataEntryFormId=' + catalogDataEntryFormId + "&catalogTypeId=" + catalogTypeId;
	}
}


function filterCatalogTypeAttributes( filter, container, list )
{
	var filterLower = filter.toString().toLowerCase();
	
	var catalogTypeAttributeList = jQuery( container + " " + list );
	
	catalogTypeAttributeList.empty();
	
	jQuery( container + " " + list + "Store" ).children().each( function(i, item){
		item = jQuery( item );		
		var toMatch = item.text().toString().toLowerCase();		
        if( toMatch.indexOf(filterLower) != -1 ){
        	catalogTypeAttributeList.append( "<option value='" + item.attr('value') + "'>" + item.text() + "</option>" );
		};
	});	
}




function insertCatalogTypeAttribute( source, catalogTypeId )
{
	var oEditor = jQuery("#designTextarea").ckeditorGet();
	var catalogTypeAttribute = JSON.parse( jQuery( source + ' #catalogTypeAttributeIds').val() );

	if( catalogTypeAttribute == null )
	{
		jQuery( source + " #message_").html( "<span class='bold'>" + i18n_specify_catalogType_attribute + "</span>" );
		return;
	} else {
		jQuery( source + " #message_").html( "" );
	}

	var catalogTypeAttributeId = catalogTypeAttribute.id;	
	var catalogTypeAttributeName = catalogTypeAttribute.name;	
	var catalogTypeAttributevalueType = catalogTypeAttribute.valueType;
	
	var htmlCode = "";
	var id = catalogTypeId + "-" + catalogTypeAttributeId + "-val" ;
	
	if ( catalogTypeAttributevalueType == "YES/NO" )
	{
		var titleValue = "-- " + catalogTypeAttributeId + "." + catalogTypeAttributeName + " ("+catalogTypeAttributevalueType+") --";
		var displayName = catalogTypeAttributeName;
		htmlCode = "<input title=\"" + titleValue + "\" name=\"entryselect\" id=\"" + id + "\" value=\"" + displayName + "\" title=\"" + displayName + "\">";
	} 
	else if ( catalogTypeAttributevalueType == "DATE" )
	{
		var titleValue = "-- " + catalogTypeAttributeId + "." + catalogTypeAttributeName + " ("+catalogTypeAttributevalueType+") --";
		var displayName = catalogTypeAttributeName;
		htmlCode = "<input title=\"" + titleValue + " \"name=\"entryfield\" id=\"" + id + "\" value=\"" + displayName + "\" title=\"" + displayName + "\">";
	} 
	else if ( catalogTypeAttributevalueType == "NUMBER" || catalogTypeAttributevalueType == "TEXT"  ) 
	{
		var titleValue = "-- " + catalogTypeAttributeId + "." + catalogTypeAttributeName +" (" + catalogTypeAttributevalueType + ") --";
		var displayName = catalogTypeAttributeName;
		htmlCode += "<input title=\"" + titleValue + "\" value=\"" + displayName + "\" name=\"entryfield\" id=\"" + id + "\" />";
	}
	
	
	else if ( catalogTypeAttributevalueType == "COMBO" ) 
	{
		var titleValue = "-- " + catalogTypeAttributeId + "." + catalogTypeAttributeName +" (" + catalogTypeAttributevalueType + ") --";
		var displayName = catalogTypeAttributeName;
		htmlCode += "<input title=\"" + titleValue + "\" value=\"" + displayName + "\" name=\"entryfield\" id=\"" + id + "\" />";
	}
	
	if( checkExisted( id ) )
	{		
		jQuery( source + " #message_").html( "<span class='bold'>" + i18n_catalogType_attribute_is_inserted + "</span>" );
		return;
	}else{
		jQuery( source + " #message_").html("");
	}

	oEditor.insertHtml( htmlCode );
}

function checkExisted( id )
{	
	var result = false;
	var html = jQuery("#designTextarea").ckeditorGet().getData();
	var input = jQuery( html ).find("select, :text");

	input.each( function(i, item){		
		if( id == item.id ) result = true;		
	});

	return result;
}


