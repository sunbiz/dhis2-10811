
var dataElementSelector;
var otherProgramStageDataElements;
var existedDataEntry;

jQuery(function(){
	dataElementSelector = jQuery("#dataElementSelection").dialog({
		title: i18n_dataelement,
		height: 350,
		width:350,
		autoOpen: false,
		zIndex:99999
	});
	
	otherProgramStageDataElements = jQuery("#otherProgramStageDataElements").dialog({
		title: i18n_dataelement_of_orther_program_stage,
		height: 350,
		width:350,
		autoOpen: false,
		zIndex:99999
	});
	
	existedDataEntry = jQuery("#existedDataEntry").dialog({
		title: i18n_choose_existing_dataentry,
		height: 350,
		width:350,
		autoOpen: false,
		zIndex:99999
	});	
});

function openOtherProgramStageDataElements()
{
	otherProgramStageDataElements.dialog("open");
}
	
function openDataElementSelector()
{
	dataElementSelector.dialog("open");
}	

function openloadExistedForm()
{
	existedDataEntry.dialog("open");
}

function loadExistedForm()
{
	jQuery.get("showDataEntryForm.action",{
		dataEntryFormId: getFieldValue( 'existedDataEntryId' )
	}, function( html ){
		jQuery("#designTextarea").ckeditorGet().setData( html );

		var dataEntryFormField = byId('existedDataEntryId');
		var optionField = dataEntryFormField.options[dataEntryFormField.selectedIndex];

		setFieldValue('dataEntryFormId', optionField.value );
		setFieldValue('name', optionField.text );
		
		checkValueIsExist('name', 'validateDataEntryForm.action', {dataEntryFormId:getFieldValue('dataEntryFormId')});
	});
}

function deleteDataEntryForm( dataEntryFormId, programStageId )
{
	if( window.confirm( i18n_delete_program_data_entry_confirm ) )
	{
		window.location.href = 'delDataEntryForm.action?id=' + dataEntryFormId + "&programStageId=" + programStageId;
	}
}

function getProgramStageDataElements( id )
{
	var dataElements = jQuery( "#otherProgramStageDataElements #dataElementIds" );
	dataElements.empty();
	var dataElementIdsStore = jQuery( "#otherProgramStageDataElements #dataElementIdsStore" );
	dataElementIdsStore.empty();
	
	if( id != '' ){
		jQuery.post("getSelectedDataElements.action",{
			associationId: id
		}, function( xml ){			
			jQuery( xml ).find( 'dataElement' ).each( function(i, item ){
				dataElements.append("<option value='" + jQuery( item ).find( "json" ).text() + "'>" + jQuery( item ).find( "name" ).text() + "</option>");
				dataElementIdsStore.append("<option value='" + jQuery( item ).find( "json" ).text() + "'>" + jQuery( item ).find( "name" ).text() + "</option>");
			});
		});
	}
}

function getSelectedValues( jQueryString )
{
	var result = new Array();
	jQuery.each( jQuery( jQueryString ).children(), function(i, item ){
		if( item.selected==true){
			result.push( JSON.parse( item.value ) );
		}
	});
	
	return result;
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

function filterDataElements( filter, container, list )
{
	var filterLower = filter.toString().toLowerCase();
	
	var dataElementList = jQuery( container + " " + list );
	dataElementList.empty();
	
	jQuery( container + " " + list + "Store" ).children().each( function(i, item){
		item = jQuery( item );		
		var toMatch = item.text().toString().toLowerCase();		
        if( toMatch.indexOf(filterLower) != -1 ){
			dataElementList.append( "<option value='" + item.attr('value') + "'>" + item.text() + "</option>" );
		};
	});	
}

function insertDataElement( source, programStageId )
{
	var oEditor = jQuery("#designTextarea").ckeditorGet();
	var dataElement = JSON.parse( jQuery( source + ' #dataElementIds').val() );

	if( dataElement == null )
	{
		jQuery( source + " #message_").html( "<span class='bold'>" + i18n_specify_dataelememt + "</span>" );
		return;
	} else {
		jQuery( source + " #message_").html( "" );
	}

	var dataElementId = dataElement.id;	
	var dataElementName = dataElement.name;	
	var dataElementType = dataElement.type;
	
	var htmlCode = "";
	var id = programStageId + "-" + dataElementId + "-val" ;
	
	if ( dataElementType == "bool" )
	{
		var titleValue = "-- " + dataElementId + "." + dataElementName + " ("+dataElementType+") --";
		var displayName = dataElementName;
		htmlCode = "<input title=\"" + titleValue + "\" name=\"entryselect\" id=\"" + id + "\" value=\"" + displayName + "\" title=\"" + displayName + "\">";
	} 
	else if ( dataElementType == "trueOnly" )
	{
		var titleValue = "-- " + dataElementId + "." + dataElementName + " ("+dataElementType+") --";
		var displayName = dataElementName;
		htmlCode = "<input type=\"checkbox\" title=\"" + titleValue + "\" name=\"entryselect\" id=\"" + id + "\" title=\"" + displayName + "\">";
	} 
	else if ( dataElementType == "longText" )
	{
		var titleValue = "-- " + dataElementId + "." + dataElementName + " ("+dataElementType+") --";
		var displayName = dataElementName;
		htmlCode = "<textarea title=\"" + titleValue + " \"name=\"entryfield\" id=\"" + id + "\" title=\"" + displayName + "\" ></textarea>";
	}
	else if ( dataElementType == "int" || dataElementType == "text" ) 
	{
		var titleValue = "-- " + dataElementId + "." + dataElementName +" (" + dataElementType + ") --";
		var displayName = dataElementName;
		htmlCode += "<input title=\"" + titleValue + "\" value=\"" + displayName + "\" name=\"entryfield\" id=\"" + id + "\" />";
	}
	
	if( checkExisted( id ) )
	{		
		jQuery( source + " #message_").html( "<span class='bold'>" + i18n_dataelement_is_inserted + "</span>" );
		return;
	}else{
		jQuery( source + " #message_").html("");
	}

	oEditor.insertHtml( htmlCode );
}
