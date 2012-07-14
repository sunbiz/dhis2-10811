
/*
jQuery(document).ready(	function(){
		validation( 'catalogForm', function(form){
			form.submit();
		}, function(){
			isSubmit = true;
			
			});
		
		checkValueIsExist( "name", "validateCatalog.action");
	});

*/
//-----------------------------------------------------------------------------
//View catalog type change
//-----------------------------------------------------------------------------

function catalogTypeChange()
{
    var catalogTypeList = document.getElementById("catalogType");
    var catalogTypeId = catalogTypeList.options[ catalogTypeList.selectedIndex ].value;
	
    
    setInnerHTML('addCatalogFormDiv', '');
	showById('addCatalogFormDiv');
    
    
    //setInnerHTML('catalogDataEntryFormDiv', '');
    setInnerHTML('addCatalogFormDiv', '');
    //showById('dataEntryFormDiv');
    showById('addCatalogFormDiv');
    
    //hideById('addCatalogFormDiv');
    //jQuery('#loaderDiv').show();
    //contentDiv = 'addCatalogFormDiv';
    
    
    //jQuery(".stage-object-selected").removeClass('stage-object-selected');
	//var tempCatalogTypeId = jQuery( '#' + catalogTypeId );
	//tempCatalogTypeId.addClass('stage-object-selected');
    
    
    
    showLoader();
	jQuery('#addCatalogFormDiv').load('showAddCataLogForm.action',
		{
			catalogTypeId:catalogTypeId
		}, function()
		{
			
			hideLoader();
			//showById('addCatalogFormDiv');
			//jQuery('#loaderDiv').hide();
		});
	hideLoader();
}

//-----------------------------------------------------------------------------
//View catalog by catalog type change
//-----------------------------------------------------------------------------
function getCatalogByCatalogType( catalogTypeId )
{
	window.location.href = "catalog.action?id=" + catalogTypeId;
}


//-----------------------------------------------------------------------------
//View details
//-----------------------------------------------------------------------------

function showCatalogDetails( catalogId )
{
	/*
	jQuery.getJSON( 'getCatalogDetails.action', { id: catalogId }, function ( json ) {
		setInnerHTML( 'nameField', json.catalog.name );	
		setInnerHTML( 'descriptionField', json.catalog.description );
		setInnerHTML( 'catalogTypeField', json.catalog.catalogType );   
	   
		showDetails();
	});
	*/
	
	
	 $('#detailsCatalogInfo').load("getCatalogDetails.action", 
				{
					id:catalogId
				}
				, function( ){
				}).dialog({
					title: i18n_catalog_details,
					maximize: true, 
					closable: true,
					modal:true,
					overlay:{background:'#000000', opacity:0.1},
					width: 650,
					height: 500
				});;
}

//-----------------------------------------------------------------------------
//Remove catalog
//-----------------------------------------------------------------------------
function removeCatalog( catalogId, name )
{
	removeItem( catalogId, name, i18n_confirm_delete, 'removeCatalog.action' );	
}


//-----------------------------------------------------------------
//
//-----------------------------------------------------------------


TOGGLE = {
	    init : function() {
	        jQuery(".togglePanel").each(function(){
	            jQuery(this).next("table:first").addClass("sectionClose");
	            jQuery(this).addClass("close");
	            jQuery(this).click(function(){
	                var table = jQuery(this).next("table:first");
	                if( table.hasClass("sectionClose")){
	                    table.removeClass("sectionClose").addClass("sectionOpen");
	                    jQuery(this).removeClass("close").addClass("open");
	                    window.scroll(0,jQuery(this).position().top);
	                }else if( table.hasClass("sectionOpen")){
	                    table.removeClass("sectionOpen").addClass("sectionClose");
	                    jQuery(this).removeClass("open").addClass("close");
	                }
	            });
	        });
	    }
	};



function entryFormContainerOnReady()
{
	alert( "options");
	var currentFocus = undefined;
	
    if( jQuery("#entryFormContainer") ) {
		
        jQuery("input[name='entryfield'],select[name='entryselect']").each(function(){
            jQuery(this).focus(function(){
                currentFocus = this;
            });
            
            jQuery(this).addClass("inputText");
        });
		
        TOGGLE.init();
				
		jQuery("#entryForm :input").each(function()
		{ 
			if( jQuery(this).attr( 'options' )!= null )
			{
				
				autocompletedField(jQuery(this).attr('id'));
			}
		});
    }
}


function autocompletedField( idField )
{
	var input = jQuery( "#" +  idField )
	var catalogTypeAttributeId = input.attr( 'catalogTypeAttributeId' );
	var options = new Array();
	options = input.attr('options').replace('[', '').replace(']', '').split(', ');
	options.push(" ");

	input.autocomplete({
			delay: 0,
			minLength: 0,
			source: options,
			select: function( event, ui ) {
				input.val(ui.item.value);
				//saveVal( catalogTypeAttributeId );
				input.autocomplete( "close" );
			},
			change: function( event, ui ) {
				if ( !ui.item ) {
					var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( $(this).val() ) + "$", "i" ),
						valid = false;
					for (var i = 0; i < options.length; i++)
					{
						if (options[i].match( matcher ) ) {
							this.selected = valid = true;
							break;
						}
					}
					if ( !valid ) {
						// remove invalid value, as it didn't match anything
						$( this ).val( "" );
						input.data( "autocomplete" ).term = "";
						return false;
					}
				}
				//saveVal( catalogTypeAttributeId );
			}
		})
		.addClass( "ui-widget" );

	this.button = $( "<button type='button'>&nbsp;</button>" )
		.attr( "tabIndex", -1 )
		.attr( "title", i18n_show_all_items )
		.insertAfter( input )
		.button({
			icons: {
				primary: "ui-icon-triangle-1-s"
			},
			text: false
		})
		.addClass( "optionset-small-button" )
		.click(function() {
			// close if already visible
			if ( input.autocomplete( "widget" ).is( ":visible" ) ) {
				input.autocomplete( "close" );
				return;
			}

			// work around a bug (likely same cause as #5265)
			$( this ).blur();

			// pass empty string as value to search for, displaying all results
			input.autocomplete( "search", "" );
			input.focus();
		});
}





//----------------------------------------------------------------
//	Update Catalog
//----------------------------------------------------------------
/*
unction showUpdateCatalogForm( catalogId )
{
	setInnerHTML('addCatalogFormDiv', '');
				
	jQuery('#loaderDiv').show();
	jQuery('#addCatalogFormDiv').load('showUpdateCatalogForm.action',
		{
			catalogId:catalogId
		}, function()
		{
			showById('addCatalogFormDiv');
		});
	hideLoader();
}


 $(document).ready(function() {
                $('#j_username').focus();

                $('#loginForm').bind('submit', function() {
					$('#submit').attr('disabled', 'disabled');
					$('#reset').attr('disabled', 'disabled');

	                sessionStorage.removeItem( 'orgUnitSelected' );
                });
            });
            
*/            




// for search ----




// ----------------------------------------------------------------
// On CatalogTypeChange  - Loading CatalogType Attributes
// ----------------------------------------------------------------
//function getCatalogTypeChange( catalogTypeId )
function getCatalogTypeChange()
{
	
	var catalogTypeId = $( '#catalogType' ).val();
	
	if( catalogTypeId == "0" )
		return;
	
	showById('selectDiv');
    disable('listAllCatalogBtn');
    
    hideById('searchCatalogDiv');
    hideById('listCatalogDiv');
    hideById('addEditCatalogFormDiv');
	hideById('resultSearchDiv');
	hideById('editEquipmentStatusDiv');
	
	jQuery('#loaderDiv').show();
	
	$.post("getCatalogTypeAttribute.action",
			{
				id:catalogTypeId
			},
			function(data)
			{
				showById('searchCatalogDiv');
				enable('listAllCatalogBtn');
				jQuery('#loaderDiv').hide();
				populateCatalogTypeAttributes( data );
			},'xml');	
}

function populateCatalogTypeAttributes( data )
{
	var searchingAttributeId = document.getElementById("searchingAttributeId");
	clearList( searchingAttributeId );
	
	var catalogTypeAttribs = data.getElementsByTagName("catalog-type-attribute");
    for ( var i = 0; i < catalogTypeAttribs.length; i++ )
    {
        var id = catalogTypeAttribs[ i ].getElementsByTagName("id")[0].firstChild.nodeValue;
        var name = catalogTypeAttribs[ i ].getElementsByTagName("name")[0].firstChild.nodeValue;
		
        var option = document.createElement("option");
        option.value = id;
        option.text = name;
        option.title = name;
        searchingAttributeId.add(option, null);
    }    	
}


//----------------------------------------------------------------
//On LoadAllCatalogs
//----------------------------------------------------------------

function loadAllCatalogs()
{
	var catalogType = document.getElementById('catalogType');
	var catalogTypeId = catalogType.options[ catalogType.selectedIndex ].value;
	
	if( catalogTypeId == 0 )
	{	
		showWarningMessage( i18n_select_please_select_catalog_type );
		return;
	}
	
	hideById('addEditCatalogFormDiv');
	hideById('resultSearchDiv');
	hideById('uploadCatalogImageDiv');
	
	showById('selectDiv');
	showById('searchCatalogDiv');

	jQuery('#loaderDiv').show();
	contentDiv = 'listCatalogDiv';
	isAjax = true;
	
	jQuery('#listCatalogDiv').load('getCatalogList.action',{
		listAll:true,
		catalogTypeId:catalogTypeId	
	},
	function(){
		statusSearching = 0;
		showById('listCatalogDiv');
		jQuery('#loaderDiv').hide();
	});
	hideLoader();
}

//----------------------------------------------------------------
//Load Equipments On Filter by catalogType Attribute
//----------------------------------------------------------------
function loadCatalogsByFilter( )
{
	var catalogType = document.getElementById('catalogType');
	var catalogTypeId = catalogType.options[ catalogType.selectedIndex ].value;
	var searchText = document.getElementById('searchText').value;
	
	if( catalogTypeId == 0 )
	{	
		showWarningMessage( i18n_select_please_select_catalog_type );
		return;
	}
	
	var catalogTypeAttribute = document.getElementById('searchingAttributeId');
	var catalogTypeAttributeId = catalogTypeAttribute.options[ catalogTypeAttribute.selectedIndex ].value;
	
	hideById('addEditCatalogFormDiv');
	hideById('resultSearchDiv');
	hideById('uploadCatalogImageDiv');
	showById('selectDiv');
	showById('searchCatalogDiv');

	jQuery('#loaderDiv').show();
	contentDiv = 'listCatalogDiv';
	isAjax = true;
	
	jQuery('#listCatalogDiv').load('getCatalogList.action',{		
		catalogTypeId:catalogTypeId,
		catalogTypeAttributeId:catalogTypeAttributeId,
		searchText:searchText
	},
	function(){
		statusSearching = 0;
		showById('listCatalogDiv');
		jQuery('#loaderDiv').hide();
	});
	hideLoader();
}



//----------------------------------------------------------------
//Add Catalog
//----------------------------------------------------------------

function showAddCatalogForm()
{
	var catalogType = document.getElementById('catalogType');
	var catalogTypeId = catalogType.options[ catalogType.selectedIndex ].value;
	
	if( catalogTypeId == 0 )
	{	
		showWarningMessage( i18n_select_please_select_catalog_type );
		return;
	}

	hideById('listCatalogDiv');
	hideById('selectDiv');
	hideById('searchCatalogDiv');
	hideById('uploadCatalogImageDiv');
	
	jQuery('#loaderDiv').show();
	jQuery('#addEditCatalogFormDiv').load('showAddCataLogForm.action',{
		catalogTypeId:catalogTypeId
		}, 
		function()
		{
			showById('addEditCatalogFormDiv');
			jQuery('#loaderDiv').hide();
		});	
}

function addCatalog()
{
	$.ajax({
    type: "POST",
    url: 'addCatalog.action',
    data: getParamsForDiv('addEditCatalogFormDiv'),
    success: function(json) {
		var type = json.response;
		jQuery('#resultSearchDiv').dialog('close');
		loadAllCatalogs();
    }
   });
  return false;
}

//----------------------------------------------------------------
//Update Catalog
//----------------------------------------------------------------

function showUpdateCatalogForm( catalogId )
{
	hideById('listCatalogDiv');
	hideById('selectDiv');
	hideById('searchCatalogDiv');
	hideById('uploadCatalogImageDiv');
	
	setInnerHTML('addEditCatalogFormDiv', '');
	
	jQuery('#loaderDiv').show();
	jQuery('#addEditCatalogFormDiv').load('showUpdateCatalogForm.action',
		{
			id:catalogId
		}, function()
		{
			showById('addEditCatalogFormDiv');
			jQuery('#searchCatalogDiv').dialog('close');
			jQuery('#loaderDiv').hide();
		});
		
	jQuery('#resultSearchDiv').dialog('close');
}

function updateCatalog()
{
	$.ajax({
      type: "POST",
      url: 'updateCatalog.action',
      data: getParamsForDiv('addEditCatalogFormDiv'),
      success: function( json ) {
		loadAllCatalogs();
      }
     });
}


/*
function showUploadCatalogImageForm( catalogId )
{
	setInnerHTML('uploadCatalogImageDiv', '');
	jQuery('#uploadCatalogImageDiv').dialog('destroy').remove();
	jQuery('<div id="uploadCatalogImageDiv">' ).load( 'showUploadImageForm.action?id='+catalogId ).dialog({
		title: i18n_upload_catalog_image,
		maximize: true,
		closable: true,
		modal:true,
		overlay:{background:'#000000', opacity:0.1},
		width: 800,
		height: 450
	});
	
}
*/
function upLoadImage()
{
	
		$( '#imageSaveDiv' ).html( ' ' );
		
		var catalogID = $( '#catalogID' ).val();
		//var sDateLB = $( '#sDateLB' ).val();
		//var eDateLB = $( '#eDateLB' ).val();
		
		//jQuery('#loaderDiv').show();
		//document.getElementById( "aggregate" ).disabled = true;
		
		jQuery('#imageSaveDiv').load('uploadCatalogImage.action',
			{
				catalogID:catalogID,
				contentType:"multipart/form-data"
				//eDateLB:eDateLB
			}, function()
			{
				showById('imageSaveDiv');
				//document.getElementById( "aggregate" ).disabled = false;
				//jQuery('#loaderDiv').hide();
			});	
}	
/*
jQuery('#imageSaveDiv').load('uploadCatalogImage.action',
		{
			catalogID:catalogID,
			contentType:"multipart/form-data"
			//eDateLB:eDateLB
		}, function()
		{
			showById('imageSaveDiv');
			//document.getElementById( "aggregate" ).disabled = false;
			//jQuery('#loaderDiv').hide();
		});	

*/
function showUploadCatalogImageForm( catalogId )
{
	hideById('listCatalogDiv');
	hideById('selectDiv');
	hideById('searchCatalogDiv');
	hideById('addEditCatalogFormDiv');
	
	setInnerHTML('uploadCatalogImageDiv', '');
	
	jQuery('#loaderDiv').show();
	jQuery('#uploadCatalogImageDiv').load('showUploadImageForm.action',
		{
			id:catalogId
		}, function()
		{
			showById('uploadCatalogImageDiv');
			jQuery('#searchCatalogDiv').dialog('close');
			jQuery('#loaderDiv').hide();
		});
		
	jQuery('#resultSearchDiv').dialog('close');
}
/*
function uploadCatalogImage()
{
	$.ajax({
      type: "POST",
      contentType: 'multipart/form-data',
      url: 'uploadCatalogImage.action',
      data: getParamsForDiv('uploadCatalogImageDiv'),
      success: function( json ) {
		loadAllCatalogs();
      }
     });
}
*/


//----------------------------------------------------------------
//Get Params form Div
//----------------------------------------------------------------

function getParamsForDiv( catalogDiv )
{
	var params = '';
	
	jQuery("#" + catalogDiv + " :input").each(function()
		{
			var elementId = $(this).attr('id');
			
			if( $(this).attr('type') == 'checkbox' )
			{
				var checked = jQuery(this).attr('checked') ? true : false;
				params += elementId + "=" + checked + "&";
			}
			else if( $(this).attr('type') != 'button' )
			{
				var value = "";
				if( jQuery(this).val() != '' )
				{
					value = htmlEncode(jQuery(this).val());
				}
				params += elementId + "="+ value + "&";
			}
			
		});
	
	//alert( params );
	
	return params;
}
