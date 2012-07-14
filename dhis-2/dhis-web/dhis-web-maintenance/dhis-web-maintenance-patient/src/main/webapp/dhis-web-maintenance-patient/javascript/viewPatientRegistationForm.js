
function openPropertiesSelector()
{
	$('#selectionDialog' ).dialog(
		{
			title:'fafds',
			maximize:true, 
			closable:true,
			modal:false,
			overlay:{background:'#000000', opacity:0.1},
			width:500,
			height:400
		});
	showById('fixedAttrTab');
}

function fixAttrOnClick()
{
	hideById('attributeTab');
	hideById('identifierTypeTab');
	hideById('programAttrTab');
	showById('fixedAttrTab');
}

function identifierTypeOnClick()
{
	hideById('attributeTab');
	hideById('fixedAttrTab');
	hideById('programAttrTab');
	showById('identifierTypeTab');
}

function attributesOnClick()
{
	hideById('identifierTypeTab');
	hideById('fixedAttrTab');
	hideById('programAttrTab');
	showById('attributeTab');
}

function programAttrOnClick()
{
	hideById('attributeTab');
	hideById('identifierTypeTab');
	hideById('fixedAttrTab');
	showById('programAttrTab');
}

function insertElement( type )
{
	var oEditor = jQuery("#designTextarea").ckeditorGet();
	var id = '';
	var value = '';
	if( type == 'fixedAttr' ){
		var element = jQuery('#fixedAttrSelector option:selected');
		id = 'fixedattributeid="' + element.attr('value') + '"';
		value = element.text();
	}
	else if( type == 'iden' ){
		var element = jQuery('#identifiersSelector option:selected');
		id = 'identifierid="' + element.attr('value') + '"';
		value = element.text();
	}
	else if( type == 'attr' ){
		var element = jQuery('#attributesSelector option:selected');
		id = 'attributeid="' + element.attr('value') + '"';
		value = element.text();
	}
	else if( type == 'prg' ){
		var element = jQuery('#programAttrSelector option:selected');
		id = 'programid="' + element.attr('value') + '"';
		value = element.text();
	}
	
	var htmlCode = "<input " + id + " value=\"[" + value + "]\" title=\"" + value + "\">";
	
	oEditor.insertHtml( htmlCode );
}
