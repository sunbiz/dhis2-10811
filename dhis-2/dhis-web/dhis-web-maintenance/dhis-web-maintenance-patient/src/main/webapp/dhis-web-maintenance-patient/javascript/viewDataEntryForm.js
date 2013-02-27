
var dataElementSelector;
var otherProgramStageDataElements;
var existedDataEntry;

jQuery(function(){
	dataElementSelector = jQuery("#dataElementSelection").dialog({
		title: i18n_dataelement,
		height: 420,
		width: 480,
		autoOpen: false,
		zIndex:99999
	});
	
	otherProgramStageDataElements = jQuery("#otherProgramStageDataElements").dialog({
		title: i18n_dataelement_of_orther_program_stage,
		height: 460,
		width:jQuery("#otherProgramStageDataElements [id=dataElementIds]").outerWidth() + 50,
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
	
	$(":button").button();
	$(":submit").button();
	$("#saveButton").button("option", "icons", { primary: "ui-icon-disk" });
	$("#cancelButton").button("option", "icons", { primary: "ui-icon-cancel" });
	$("#deleteButton").button("option", "icons", { primary: "ui-icon-trash" });
	$("#insertButton").button("option", "icons", { primary: "ui-icon-plusthick" });
	$("#insertImagesButton").button("option", "icons", { primary: "ui-icon-newwin" });
	$("#loadExistForms").button("option", "icons", { primary: "ui-icon-newwin" });
	$("#insertDataElements").button("option", "icons", { primary: "ui-icon-newwin" });
	$("#insertOtherDataElements").button("option", "icons", { primary: "ui-icon-newwin" });
	
	$("#imageDialog").bind("dialogopen", function(event, ui) {
		$("#insertImagesButton").button("disable");
	})
	$("#imageDialog").bind("dialogclose", function(event, ui) {
		$("#insertImagesButton").button("enable");
	})
	
	$("#insertImagesButton").click(function() {
		$("#imageDialog").dialog();
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
				dataElements.append("<option value='" + jQuery( item ).find( "json" ).text() + "' dename='" + jQuery( item ).find( "name" ).text() + "' decode='" + jQuery( item ).find( "code" ).text() + "'>" + jQuery( item ).find( "name" ).text() + "</option>");
				dataElementIdsStore.append("<option value='" + jQuery( item ).find( "json" ).text() + "' dename='" + jQuery( item ).find( "name" ).text() + "' decode='" + jQuery( item ).find( "code" ).text() + "'>" + jQuery( item ).find( "name" ).text() + "</option>");
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
			dataElementList.append( "<option value='" + item.attr('value') + "' dename='"+item.attr('dename')+"' decode='"+item.attr('decode')+"'>" + item.text() + "</option>" );
		};
	});	
}

function insertDataElement( source, programStageId )
{
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
	var titleValue = dataElementId + " - " + dataElementName + " - " + dataElementType;
	
	if ( dataElementType == "bool" )
	{
		var displayName = dataElementName;
		htmlCode = "<input title=\"" + titleValue + "\" name=\"entryselect\" id=\"" + id + "\" value=\"[" + displayName + "]\" title=\"" + displayName + "\">";
	} 
	else if ( dataElementType == "trueOnly" )
	{
		var displayName = dataElementName;
		htmlCode = "<input type=\"checkbox\" title=\"" + titleValue + "\" name=\"entryselect\" id=\"" + id + "\" title=\"" + displayName + "\">";
	} 
	else if ( dataElementType == "username" )
	{
		var displayName = dataElementName;
		htmlCode = "<input title=\"" + titleValue + "\" value=\"[" + displayName + "]\" name=\"entryfield\" id=\"" + id + "\" username=\"true\" />";
	} 
	else
	{
		var displayName = dataElementName;
		htmlCode = "<input title=\"" + titleValue + "\" value=\"[" + displayName + "]\" name=\"entryfield\" id=\"" + id + "\" />";
	}
	
	if( checkExisted( id ) )
	{		
		jQuery( source + " #message_").html( "<span class='bold'>" + i18n_dataelement_is_inserted + "</span>" );
		return;
	}else{
		var oEditor = jQuery("#designTextarea").ckeditorGet();
		oEditor.insertHtml( htmlCode );
		jQuery( source + " #message_").html("");
	}

}

function displayNameOnChange( div, displayName )
{
	// display - name
	if(displayName=='1'){
		jQuery('#' + div + ' [id=dataElementIds] option').each(function(){
			var item = jQuery(this);
			item[0].text = item.attr('dename');
			item[0].title = item[0].text;
		});
		jQuery('#' + div + ' [id=dataElementIdsStore] option').each(function(){
			var item = jQuery(this);
			item[0].text = item.attr('dename');
		});
	}
	// display - code
	else if(displayName=='2'){
		jQuery('#' + div + ' [id=dataElementIds] option').each(function(){
			var item = jQuery(this);
			item[0].text = item.attr('decode');
			item[0].title = item[0].text;
		});
		jQuery('#' + div + ' [id=dataElementIdsStore] option').each(function(){
			var item = jQuery(this);
			item[0].text = item.attr('decode');
		});
	}
	// display - code and name
	else{
		jQuery('#' + div + ' [id=dataElementIds] option').each(function(){
			var item = jQuery(this);
			item[0].text = "(" + item.attr('decode') + ") " + item.attr('dename');
			item[0].title = item[0].text;
		});
		jQuery('#' + div + ' [id=dataElementIdsStore] option').each(function(){
			var item = jQuery(this);
			item[0].text = "(" + item.attr('decode') + ") " + item.attr('dename');
		});
	}
}

function sortByOnChange( div, sortBy)
{
	if( sortBy == 1)
	{
		jQuery('#' + div + ' [id=dataElementIds]').each(function() {

			// Keep track of the selected option.
			var selectedValue = $(this).val();

			// sort it out
			$(this).html($("option", $(this)).sort(function(a, b) { 
				return $(a).attr('dename') == $(b).attr('dename') ? 0 : $(a).attr('dename') < $(b).attr('dename') ? -1 : 1 
			}));

			// Select one option.
			$(this).val(selectedValue);

		});
	}
	else
	{
		jQuery('#' + div + ' [id=dataElementIds]').each(function() {

			// Keep track of the selected option.
			var selectedValue = $(this).val();

			// sort it out
			$(this).html($("option", $(this)).sort(function(a, b) { 
				return $(a).attr('decode') == $(b).attr('decode') ? 0 : $(a).attr('decode') < $(b).attr('decode') ? -1 : 1 
			}));

			// Select one option.
			$(this).val(selectedValue);

		});
	} 
}

function insertImage() {
	var image = $("#imageDialog :selected").val();
	var html = "<img src=\"" + image + "\" title=\"" + $("#imageDialog :selected").text() + "\">";
	var oEditor = $("#designTextarea").ckeditorGet();
	oEditor.insertHtml( html );
}