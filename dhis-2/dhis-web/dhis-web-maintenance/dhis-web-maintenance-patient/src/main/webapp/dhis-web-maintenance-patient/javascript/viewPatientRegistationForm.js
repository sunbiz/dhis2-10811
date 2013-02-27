
$( document ).ready( function() 
{
	$(":button").button();
	$(":submit").button();
	$("#saveButton").button("option", "icons", { primary: "ui-icon-disk" });
	$("#cancelButton").button("option", "icons", { primary: "ui-icon-cancel" });
	$("#deleteButton").button("option", "icons", { primary: "ui-icon-trash" });
	$("#insertButton").button("option", "icons", { primary: "ui-icon-plusthick" });
	$("#propertiesButton").button("option", "icons", { primary: "ui-icon-newwin" });
	$("#insertImagesButton").button("option", "icons", { primary: "ui-icon-newwin" });
	
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
	
function openPropertiesSelector()
{	
	$("#propertiesButton").addClass("ui-state-active2");
	$('#selectionDialog' ).dialog(
		{
			title:i18n_properties,
			maximize:true, 
			closable:true,
			modal:false,
			overlay:{background:'#000000', opacity:0.1},
			width:500,
			height:460,
			close: function(ev, ui) { 
				$("#propertiesButton").removeClass("ui-state-active2"); 
			}
		});
}

function fixAttrOnClick()
{
	$("#insertButton").click(function() {
		insertElement( 'fixedAttr' );
	});	
	
	$("#fixAttrButton").addClass("ui-state-active2");
	$("#identifierTypeButton").removeClass("ui-state-active2");
	$("#attributesButton").removeClass("ui-state-active2");
	$("#programAttrButton").removeClass("ui-state-active2");
	hideById('attributeTab');
	hideById('identifierTypeTab');
	hideById('programAttrTab');
	showById('fixedAttrTab');
}

function identifierTypeOnClick()
{
	$("#insertButton").click(function() {
		insertElement( 'iden' )
	});
	
	$("#fixAttrButton").removeClass("ui-state-active2");
	$("#identifierTypeButton").addClass("ui-state-active2");
	$("#attributesButton").removeClass("ui-state-active2");
	$("#programAttrButton").removeClass("ui-state-active2");
	hideById('attributeTab');
	hideById('fixedAttrTab');
	hideById('programAttrTab');
	showById('identifierTypeTab');
}

function attributesOnClick()
{
	$("#insertButton").click(function() {
		insertElement( 'attr' );
	});	
	
	$("#fixAttrButton").removeClass("ui-state-active2");
	$("#identifierTypeButton").removeClass("ui-state-active2");
	$("#attributesButton").addClass("ui-state-active2");
	$("#programAttrButton").removeClass("ui-state-active2");
	hideById('identifierTypeTab');
	hideById('fixedAttrTab');
	hideById('programAttrTab');
	showById('attributeTab');
}

function programAttrOnClick()
{
	$("#insertButton").click(function() {
		insertElement( 'prg' );
	});	
	
	$("#fixAttrButton").removeClass("ui-state-active2");
	$("#identifierTypeButton").removeClass("ui-state-active2");
	$("#attributesButton").removeClass("ui-state-active2");
	$("#programAttrButton").addClass("ui-state-active2");
	hideById('attributeTab');
	hideById('identifierTypeTab');
	hideById('fixedAttrTab');
	showById('programAttrTab');
}

function getRequiredFields()
{
	var requiredFields = {};
	
	requiredFields['fixedattributeid=registrationDate'] = i18n_registration_date;
	requiredFields['fixedattributeid=fullName'] = i18n_full_name;
	requiredFields['fixedattributeid=gender'] = i18n_gender;
	requiredFields['fixedattributeid=birthDate'] = i18n_date_of_birth;
		
	jQuery('#identifiersSelector option').each(function() {
		var item = jQuery(this);
		if( item.attr('mandatory')=='true'){
			requiredFields['identifierid=' + item.val()] = item.text();
		}
	});

	jQuery('#attributesSelector option').each(function() {
		var item = jQuery(this);
		if( item.attr('mandatory')=='true'){
			requiredFields['attributeid=' + item.val()] = item.text();
		}
	});
	
	return requiredFields;
}

function validateForm()
{
	var result = false;
	var html = jQuery("#designTextarea").ckeditorGet().getData();
	requiredFields = getRequiredFields();
	
	var input = jQuery( html ).find("input");
	if( input.length > 0 )
	{
		input.each( function(i, item){	
			var key = "";
			var inputKey = jQuery(item).attr('fixedattributeid');
			if( inputKey!=undefined)
			{
				key = 'fixedattributeid=' + inputKey
			}
			else if( jQuery(item).attr('identifierid')!=undefined ){
				inputKey = jQuery(item).attr('identifierid');
				key = 'identifierid=' + inputKey
			}
			else if( jQuery(item).attr('attributeid')!=undefined ){
				inputKey = jQuery(item).attr('attributeid');
				key = 'attributeid=' + inputKey
			}
			else if( jQuery(item).attr('programid')!=undefined ){
				inputKey = jQuery(item).attr('programid');
				key = 'programid=' + inputKey
			}
			
			for (var idx in requiredFields){
				//var field = requiredFields[idx];
				if( key == idx)
				{
					//requiredFields.splice(idx,1);
					delete requiredFields[idx];
				}
			}
		});
	
	}
	if( Object.keys(requiredFields).length > 0 ) {
		setFieldValue('requiredField','');
		var violate = '<h3>' + i18n_please_insert_all_required_fields + '<h3>';
		for (var idx in requiredFields){
			violate += " - " + requiredFields[idx] + '<br>';
		}
		
		setInnerHTML('validateDiv', violate);
		jQuery('#validateDiv').dialog({
			title:i18n_required_fields_valivation,
			maximize:true, 
			closable:true,
			modal:false,
			overlay:{background:'#000000', opacity:0.1},
			width:500,
			height:300
		});
		
		return false;
	}
	else{
		setFieldValue('requiredField','everything_is_ok');
		setInnerHTML( 'designTextarea' , jQuery("#designTextarea").ckeditorGet().getData() );
		byId('saveDataEntryForm').submit();
	}
}

function checkExisted( id )
{	
	var result = false;
	var html = jQuery("#designTextarea").ckeditorGet().getData();
	var input = jQuery( html ).find("input");

	input.each( function(i, item){		
		var key = "";
		var inputKey = jQuery(item).attr('fixedattributeid');
		if( inputKey!=undefined)
		{
			key = 'fixedattributeid="' + inputKey + '"';
		}
		else if( jQuery(item).attr('identifierid')!=undefined ){
			inputKey = jQuery(item).attr('identifierid');
			key = 'identifierid="' + inputKey + '"';
		}
		else if( jQuery(item).attr('attributeid')!=undefined ){
			inputKey = jQuery(item).attr('attributeid');
			key = 'attributeid="' + inputKey + '"';
		}
		else if( jQuery(item).attr('programid')!=undefined ){
			inputKey = jQuery(item).attr('programid');
			key = 'programid="' + inputKey + '"';
		}
		
		if( id == key ) result = true;		
		
	});

	return result;
}

function insertElement( type )
{
	var id = '';
	var value = '';
	
	if( type == 'fixedAttr' ){
		var element = jQuery('#fixedAttrSelector option:selected');
		if( element.length == 0 ) return;
		
		id = 'fixedattributeid="' + element.attr('value') + '"';
		value = element.text();
	}
	else if( type == 'iden' ){
		var element = jQuery('#identifiersSelector option:selected');
		if( element.length == 0 ) return;
		
		id = 'identifierid="' + element.attr('value') + '"';
		value = element.text();
	}
	else if( type == 'attr' ){
		var element = jQuery('#attributesSelector option:selected');
		if( element.length == 0 ) return;
		
		id = 'attributeid="' + element.attr('value') + '"';
		value = element.text();
	}
	else if( type == 'prg' ){
		var element = jQuery('#programAttrSelector option:selected');
		if( element.length == 0 ) return;
		
		id = 'programid="' + element.attr('value') + '"';
		value = element.text();
	}
	
	var htmlCode = "<input " + id + " value=\"[" + value + "]\" title=\"" + value + "\">";
	
	if( checkExisted( id ) ){		
		setMessage( "<span class='bold'>" + i18n_property_is_inserted + "</span>" );
		return;
	}else{
		var oEditor = jQuery("#designTextarea").ckeditorGet();
		oEditor.insertHtml( htmlCode );
		setMessage("");
	}

}

function deleteRegistrationForm( id, name )
{
	var result = window.confirm( i18n_confirm_delete + '\n\n' + name );
	if ( result )
	{
		window.location.href = 'delRegistrationEntryFormAction.action?id=' + id;
	}
}

function insertImage() {
	var image = $("#imageDialog :selected").val();
	var html = "<img src=\"" + image + "\" title=\"" + $("#imageDialog :selected").text() + "\">";
	var oEditor = $("#designTextarea").ckeditorGet();
	oEditor.insertHtml( html );
}