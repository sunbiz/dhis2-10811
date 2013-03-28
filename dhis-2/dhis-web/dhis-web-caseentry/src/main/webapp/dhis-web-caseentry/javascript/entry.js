
//------------------------------------------------------------------------------
// Save value
//------------------------------------------------------------------------------

function saveVal( dataElementId )
{

	var programStageId = jQuery('.stage-object-selected').attr('psid');
	if(programStageId==undefined){
		if( jQuery('#entryFormContainer [id=programStageId]') == null) return;
		else programStageId = jQuery('#entryFormContainer [id=programStageId]').val();
	}
        
	var fieldId = programStageId + '-' + dataElementId + '-val';
	
	var field = byId( fieldId ); 
	if( field == null) return;
	
	var fieldValue = jQuery.trim( field.value );

	var arrData = jQuery( "#" + fieldId ).attr('data').replace('{','').replace('}','').replace(/'/g,"").split(',');
	var data = new Array();
	for( var i in arrData )
	{	
		var values = arrData[i].split(':');
		var key = jQuery.trim( values[0] );
		var value = jQuery.trim( values[1] )
		data[key] = value;
	}
 
	var dataElementName = data['deName']; 
    var type = data['deType'];
 
	field.style.backgroundColor = SAVING_COLOR;
    
    if( fieldValue != '' )
    {
        if ( type == 'int' || type == 'number' || type == 'positiveNumber' || type == 'negativeNumber' )
        {
            if (  type == 'int' && !isInt( fieldValue ))
            {
                field.style.backgroundColor = '#ffcc00';

                window.alert( i18n_value_must_integer + '\n\n' + dataElementName );

                field.focus();

                return;
            }
			else if ( type == 'number' && !isRealNumber( fieldValue ) )
            {
                field.style.backgroundColor = '#ffcc00';
                window.alert( i18n_value_must_number + '\n\n' + dataElementName );
                field.focus();

                return;
            } 
			else if ( type == 'positiveNumber' && !isPositiveInt( fieldValue ) )
            {
                field.style.backgroundColor = '#ffcc00';
                window.alert( i18n_value_must_positive_integer + '\n\n' + dataElementName );
                field.focus();

                return;
            } 
			else if ( type == 'negativeNumber' && !isNegativeInt( fieldValue ) )
            {
                field.style.backgroundColor = '#ffcc00';
                window.alert( i18n_value_must_negative_integer + '\n\n' + dataElementName );
                field.focus();

                return;
            }
        }
		else if(type=='date')
		{
			field.focus();
		}
    	
    }
    
	var value = fieldValue;
	if ( type == 'trueOnly' ){
		if( field.checked ) 
			fieldValue = "true";
		else 
			fieldValue="";
	}
	var valueSaver = new ValueSaver( dataElementId, fieldValue, type, SUCCESS_COLOR );
    valueSaver.save();
}

function saveOpt( dataElementId )
{
	var programStageId = jQuery('.stage-object-selected').attr('psid');
	var field = byId( programStageId + '-' + dataElementId + '-val' );	
	field.style.backgroundColor = SAVING_COLOR;
	
	var valueSaver = new ValueSaver( dataElementId, field.options[field.selectedIndex].value, 'bool', SUCCESS_COLOR );
    valueSaver.save();
}

function updateProvidingFacility( dataElementId, checkField )
{
	var programStageId = byId( 'programStageId' ).value;
	var checked= checkField.checked;

    var facilitySaver = new FacilitySaver( dataElementId, checked, SUCCESS_COLOR );
    facilitySaver.save();    
}

function saveExecutionDate( programId, programStageInstanceId, field )
{
	field.style.backgroundColor = SAVING_COLOR;
    var executionDateSaver = new ExecutionDateSaver( programId, programStageInstanceId, field.value, SUCCESS_COLOR );
    executionDateSaver.save();
	
    if( !jQuery("#entryForm").is(":visible") )
    {
        toggleContentForReportDate(true);
    }
}

/**
* Display data element name in selection display when a value field recieves
* focus.
* XXX May want to move this to a separate function, called by valueFocus.
* @param e focus event
* @author Hans S. Tommerholt
*/
function valueFocus(e) 
{
    //Retrieve the data element id from the id of the field
    var str = e.target.id;
	
    var match = /.*\[(.*)\]/.exec( str ); //value[-dataElementId-]
	
    if ( ! match )
    {
        return;
    }

    var deId = match[1];
	
    //Get the data element name
    var nameContainer = document.getElementById('value[' + deId + '].name');
	
    if ( ! nameContainer )
    {
        return;
    }

    var name = '';
	
    var as = nameContainer.getElementsByTagName('a');

    if ( as.length > 0 )	//Admin rights: Name is in a link
    {
        name = as[0].firstChild.nodeValue;
    }
    else
    {
        name = nameContainer.firstChild.nodeValue;
    }
	
}

function keyPress( event, field )
{
    var key = 0;
    if ( event.charCode )
    {
        key = event.charCode; /* Safari2 (Mac) (and probably Konqueror on Linux, untested) */
    }
    else
    {
        if ( event.keyCode )
        {
            key = event.keyCode; /* Firefox1.5 (Mac/Win), Opera9 (Mac/Win), IE6, IE7Beta2, Netscape7.2 (Mac) */
        }
        else
        {
            if ( event.which )
            {
                key = event.which; /* Older Netscape? (No browsers triggered yet) */
            }
        }
    }
	
    if ( key == 13 )
    { 
        nextField = getNextEntryField( field );
        if ( nextField )
        {
            nextField.focus();
        }
        return true;
    }
    
    return true;
}

function getNextEntryField( field )
{
    var index = field.getAttribute( 'tabindex' );
	return $( '[name="entryfield"][tabindex="' + (++index) + '"]' );
}

//-----------------------------------------------------------------
// Save value for dataElement of type text, number, boolean, combo
//-----------------------------------------------------------------

function ValueSaver( dataElementId_, value_, dataElementType_, resultColor_  )
{
    var dataElementId = dataElementId_;
	var providedElsewhereId = getFieldValue('programStageId') + "-" + dataElementId_ + "-facility";
	var value = value_;
	var type = dataElementType_;
    var resultColor = resultColor_;
	
    this.save = function()
    {
		var params  = 'dataElementId=' + dataElementId;
			params += '&programStageInstanceId=' + getFieldValue('programStageInstanceId');
		
		params += '&providedElsewhere=';
		if( byId( providedElsewhereId ) != null )
			params += byId( providedElsewhereId ).checked;
		
		params += '&value=';
		if( value != '')
			params += htmlEncode(value);
		
		$.ajax({
			   type: "POST",
			   url: "saveValue.action",
			   data: params,
			   dataType: "xml",
			   success: function(result){
					handleResponse (result);
			   },
			   error: function(request,status,errorThrown) {
					handleHttpError (request);
			   }
			});
    };
 
    function handleResponse( rootElement )
    {
        var codeElement = rootElement.getElementsByTagName( 'code' )[0];
        var code = parseInt( codeElement.firstChild.nodeValue );
        if ( code == 0 )
        {
            markValue( resultColor );
        }
        else
        {
            if(value!="")
            {
                markValue( ERROR );
                window.alert( i18n_saving_value_failed_status_code + '\n\n' + code );
            }
            else
            {
                markValue( resultColor );
            }
        }
    }
 
    function handleHttpError( errorCode )
    {
        markValue( ERROR );
        window.alert( i18n_saving_value_failed_error_code + '\n\n' + errorCode );
    }
 
    function markValue( color )
    {
		var programStageId = jQuery('#entryFormContainer [id=programStageId]').val();
		var element = byId( programStageId + "-" + dataElementId + '-val' );
        element.style.backgroundColor = color;
    }
}

function FacilitySaver( dataElementId_, providedElsewhere_, resultColor_ )
{
    var dataElementId = dataElementId_;
	var providedElsewhere = providedElsewhere_;
    var resultColor = resultColor_;

    this.save = function()
    {
		var params  = 'dataElementId=' + dataElementId;
			params += '&providedElsewhere=' + providedElsewhere ;
		$.ajax({
			   type: "POST",
			   url: "saveProvidingFacility.action",
			   data: params,
			   dataType: "xml",
			   success: function(result){
					handleResponse (result);
			   },
			   error: function(request,status,errorThrown) {
					handleHttpError (request);
			   }
			});
    };

    function handleResponse( rootElement )
    {
        var codeElement = rootElement.getElementsByTagName( 'code' )[0];
        var code = parseInt( codeElement.firstChild.nodeValue );
		
        if ( code != 0 )
        {
            window.alert( i18n_saving_value_failed_status_code + '\n\n' + code );
        }
    }

    function handleHttpError( errorCode )
    {
        window.alert( i18n_saving_value_failed_error_code + '\n\n' + errorCode );
    }
}

function ExecutionDateSaver( programId_, programStageInstanceId_, executionDate_, resultColor_ )
{
    var programId = programId_;
    var programStageInstanceId = programStageInstanceId_;
    var executionDate = executionDate_;
    var resultColor = resultColor_;

    this.save = function()
    {
		var params  = "executionDate=" + executionDate;
			params += "&programId=" + programId;
			params += "&programStageInstanceId=" + programStageInstanceId;
			
		$.ajax({
			   type: "POST",
			   url: "saveExecutionDate.action",
			   data: params,
			   dataType: "json",
			   success: function( json ){	
					var selectedProgramStageInstance = jQuery( '#' + prefixId + getFieldValue('programStageInstanceId') );
					var box = jQuery(".stage-object-selected");
					var boxName = box.attr('psname') + "\n" + executionDate;
					box.val( boxName );
					box.attr( 'reportDate', executionDate );
					box.css('border-color', COLOR_LIGHTRED);
					box.css('background-color', COLOR_LIGHT_LIGHTRED);
					disableCompletedButton(false);
					enable('validationBtn');
					setFieldValue( 'programStageId', selectedProgramStageInstance.attr('psid') );
					
					var fieldId = "value_" + programStageInstanceId + "_date";
					jQuery("#" + fieldId).val(executionDate);
					jQuery("#" + fieldId).css('background-color', SUCCESS_COLOR);
					jQuery('#executionDate').val(executionDate);
					jQuery("#org_" + programStageInstanceId ).html(getFieldValue("orgunitName"));
					showById('inputCriteriaDiv');
					handleResponse (json);
			   },
			   error: function(request,status,errorThrown) {
					handleHttpError (request);
			   }
			});
    };

    function handleResponse( json )
    {
		if(json.response=='success')
		{
            markValue( resultColor );
			if( getFieldValue('programStageInstanceId' )=='' )
			{
				var programStageInstanceId = json.message;
				setFieldValue('programStageInstanceId', programStageInstanceId);
				loadDataEntry( programStageInstanceId );
			}
			else
			{
				showById('entryFormContainer');
				showById('dataEntryFormDiv');
				showById('entryForm');
			}
        }
        else
        {
            if( executionDate != "")
            {
                markValue( ERROR );
                showWarningMessage( i18n_invalid_date );
            }
            else
            {
                markValue( ERROR );
				showWarningMessage( i18n_please_enter_report_date );
            }
			hideById('dataEntryFormDiv');
			hideById('inputCriteriaDiv');
        }
    }

    function handleHttpError( errorCode )
    {
        markValue( ERROR );
        window.alert( i18n_saving_value_failed_error_code + '\n\n' + errorCode );
    }

    function markValue( color )
    {
        var element = document.getElementById( 'executionDate' );
           
        element.style.backgroundColor = color;
    }
}

//-----------------------------------------------------------------
//
//-----------------------------------------------------------------

function toggleContentForReportDate(show)
{
    if( show ){
        jQuery("#entryForm").show();
    }else {
        jQuery("#entryForm").hide();
    }
}

function doComplete(isCreateEvent){
	
	if(getFieldValue('validCompleteOnly')=="true")
	{
		$('#loading-bar').show();
		$('#loading-bar').dialog({
			modal:true,
			width: 330
		});
		$("#loading-bar").siblings(".ui-dialog-titlebar").hide(); 
		
		jQuery.get( 'validateProgram.action'
			, function(html){ 
				$( "#loading-bar" ).dialog( "close" );
				$('#validateProgramDiv').html(html);
				if(getFieldValue('violateValidation')=='true'){
					$('#validateProgramDiv' ).dialog({
						title: i18n_violate_validation,
						maximize: true, 
						closable: true,
						modal:true,
						overlay:{background:'#000000', opacity:0.1},
						width: 800,
						height: 450
					});
				}
				else{
					hideById('validateProgramDiv');
					runCompleteEvent( isCreateEvent );
				}
		});
	}
	else
	{
		runCompleteEvent( isCreateEvent );
	}
}

function runCompleteEvent( isCreateEvent )
{
    var flag = false;
    jQuery("#dataEntryFormDiv input[name='entryfield'],select[name='entryselect']").each(function(){
        jQuery(this).parent().removeClass("errorCell");
        
		var arrData = jQuery( this ).attr('data').replace('{','').replace('}','').replace(/'/g,"").split(',');
		var data = new Array();
		for( var i in arrData )
		{	
			var values = arrData[i].split(':');
			var key = jQuery.trim( values[0] );
			var value = jQuery.trim( values[1] )
			data[key] = value;
		}
		var compulsory = data['compulsory']; 
		if( compulsory == 'true' && 
			( !jQuery(this).val() || jQuery(this).val() == "undifined" ) ){
                flag = true;
                jQuery(this).parent().addClass("errorCell");
            }
    });
	
    if( flag ){
        alert(i18n_error_required_field);
        return;
    }else {
        if( confirm(i18n_complete_confirm_message) )
		{
			$.postJSON( "completeDataEntry.action",
				{
					programStageInstanceId: getFieldValue('programStageInstanceId')
				},
				function (json)
				{
					jQuery(".stage-object-selected").css('border-color', COLOR_GREEN);
					jQuery(".stage-object-selected").css('background-color', COLOR_LIGHT_GREEN);

					var irregular = jQuery('#entryFormContainer [name=irregular]').val();
					var displayGenerateEventBox = jQuery('#entryFormContainer [name=displayGenerateEventBox]').val();
					var programInstanceId = jQuery('#entryFormContainer [id=programInstanceId]').val();
					if( irregular == 'true' && displayGenerateEventBox=="true" )
					{
						var programStageId = jQuery(".stage-object-selected").attr('psid');
						showCreateNewEvent( programInstanceId, programStageId );
					}
					
					var selectedProgram = jQuery('.stage-object-selected');
					if( selectedProgram.attr('programType')=='2' || json.response == 'programcompleted' )
					{
						var completedRow = jQuery('#td_' + programInstanceId).html();
						jQuery('#completedList' ).append('<option value="' +  programInstanceId + '">' + getInnerHTML('infor_' + programInstanceId ) + '</option>');
					}
					
					var blocked = jQuery('#entryFormContainer [id=blockEntryForm]').val();
					if( blocked=='true' ){
						blockEntryForm();
					}
					
					disableCompletedButton(true);
					enable('validationBtn');
					var eventBox = jQuery('#ps_' + getFieldValue('programStageInstanceId'));
					eventBox.attr('status',1);
					resetActiveEvent( eventBox.attr("pi") );
					
					hideLoader();
					
					if( isCreateEvent ){
						showAddEventForm();
					}
				});
		}
    }
}

function doUnComplete( isCreateEvent )
{	
	if( confirm(i18n_incomplete_confirm_message) )
	{
		$.postJSON( "uncompleteDataEntry.action",
			{
				programStageInstanceId: getFieldValue('programStageInstanceId')
			},
			function (data)
			{
				jQuery(".stage-object-selected").css('border-color', COLOR_LIGHTRED);
				jQuery(".stage-object-selected").css('background-color', COLOR_LIGHT_LIGHTRED);
				unblockEntryForm();
				disableCompletedButton(false);
				var eventBox = jQuery('#ps_' + getFieldValue('programStageInstanceId'));
				eventBox.attr('status',2);
				resetActiveEvent( eventBox.attr("pi") );
			});
	}
    
}


function blockEntryForm()
{
	jQuery("#entryFormContainer :input").each(function()
	{
		disable($(this).attr('id'));
	});
	jQuery("#entryFormContainer").find(".ui-combobox").each(function()
	{
		jQuery(this).addClass('hidden');
	});
	jQuery('.auto-field').removeClass('optionset');
	jQuery('.date-field').each(function(){
		var id = jQuery(this).attr('id');
		jQuery('#delete_' + id ).hide();
	});
	jQuery('.date-field').removeClass('datefield');
}

function unblockEntryForm()
{
	jQuery("#entryFormContainer :input").each(function()
	{
		enable($(this).attr('id'));
	});
	jQuery("#entryFormContainer").find(".ui-combobox").each(function()
	{
		jQuery(this).removeClass('hidden');
	});
	jQuery('.auto-field').addClass('optionset');
	jQuery('.date-field').each(function(){
		var id = jQuery(this).attr('id');
		jQuery('#delete_' + id ).show();
	});
	jQuery('.date-field').addClass('datefield');
}

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
	var currentFocus = undefined;

    if( jQuery("#entryFormContainer") ) {
		
        if( jQuery("#executionDate").val() != '' )
        {
            toggleContentForReportDate(true);
        }
		
        jQuery("input[name='entryfield'],select[name='entryselect']").each(function(){
            jQuery(this).focus(function(){
                currentFocus = this;
            });
            
            jQuery(this).addClass("inputText");
        });
		
        TOGGLE.init();
				
		jQuery("#entryForm :input").each(function()
		{ 
			if( jQuery(this).attr( 'options' )!= null && jQuery(this).attr( 'options' )== 'true' )
			{
				autocompletedField(jQuery(this).attr('id'));
			}
			else if( jQuery(this).attr( 'username' )!= null && jQuery(this).attr( 'username' )== 'true' )
			{
				autocompletedUsernameField(jQuery(this).attr('id'));
			}
		});
    }
}

//------------------------------------------------------
// Run validation
//------------------------------------------------------

function runValidation()
{
	$('#loading-bar').show();
	$('#loading-bar').dialog({
		modal:true,
		width: 330
	});
	$("#loading-bar").siblings(".ui-dialog-titlebar").hide(); 
	
	$('#validateProgramDiv' ).load( 'validateProgram.action',
		function(){
			$( "#loading-bar" ).dialog( "close" );
			
			$('#validateProgramDiv' ).dialog({
				title: i18n_violate_validation,
				maximize: true, 
				closable: true,
				modal:true,
				overlay:{background:'#000000', opacity:0.1},
				width: 800,
				height: 450
			});
		});
}

function autocompletedField( idField )
{
	var input = jQuery( "#" +  idField );
	input.parent().width( input.width() + 50 );
	var dataElementId = input.attr('id').split('-')[1];
	
	input.autocomplete({
		delay: 0,
		minLength: 0,
		source: function( request, response ){
			$.ajax({
				url: "getOptions.action?id=" + dataElementId + "&query=" + input.val(),
				dataType: "json",
				cache: true,
				success: function(data) {
					response($.map(data.options, function(item) {
						return {
							label: item.o,
							id: item.o
						};
					}));
				}
			});
		},
		minLength: 0,
		select: function( event, ui ) {
			var fieldValue = ui.item.value;
			
			if ( !dhis2.trigger.invoke( "caseentry-value-selected", [dataElementId, fieldValue] ) ) {
				input.val( "" );
				return false;
			}
			
			input.val( fieldValue );			
			if ( !unSave ) {
				saveVal( dataElementId );
			}
			input.autocomplete( "close" );
		},
		change: function( event, ui ) {
			if ( !ui.item ) {
				var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( $(this).val() ) + "$", "i" ),
					valid = false;
				if ( !valid ) {
					$( this ).val( "" );
					if(!unSave)
						saveVal( dataElementId );
					input.data( "autocomplete" ).term = "";
					return false;
				}
			}
		}
	})
	.addClass( "ui-widget" );
	
	input.data( "autocomplete" )._renderItem = function( ul, item ) {
		return $( "<li></li>" )
			.data( "item.autocomplete", item )
			.append( "<a>" + item.label + "</a>" )
			.appendTo( ul );
	};
		
	var wrapper = this.wrapper = $( "<span style='width:200px'>" )
			.addClass( "ui-combobox" )
			.insertAfter( input );
						
	var button = $( "<a style='width:20px; margin-bottom:-5px;height:20px;'>" )
		.attr( "tabIndex", -1 )
		.attr( "title", i18n_show_all_items )
		.appendTo( wrapper )
		.button({
			icons: {
				primary: "ui-icon-triangle-1-s"
			},
			text: false
		})
		.addClass('small-button')
		.click(function() {
			if ( input.autocomplete( "widget" ).is( ":visible" ) ) {
				input.autocomplete( "close" );
				return;
			}
			$( this ).blur();
			input.autocomplete( "search", "" );
			input.focus();
		});
}

function autocompletedUsernameField( idField )
{
	var input = jQuery( "#" +  idField );
	input.parent().width( input.width() + 50 );
	var dataElementId = input.attr('id').split('-')[1];
	
	input.autocomplete({
		delay: 0,
		minLength: 0,
		source: function( request, response ){
			$.ajax({
				url: "getUsernameList.action?query=" + input.val(),
				dataType: "json",
				cache: true,
				success: function(data) {
					response($.map(data.usernames, function(item) {
						return {
							label: item.u,
							id: item.u
						};
					}));
				}
			});
		},
		minLength: 0,
		select: function( event, ui ) {
			var fieldValue = ui.item.value;
			
			if ( !dhis2.trigger.invoke( "caseentry-value-selected", [dataElementId, fieldValue] ) ) {
				input.val( "" );
				return false;
			}
			
			input.val( fieldValue );			
			if ( !unSave ) {
				saveVal( dataElementId );
			}
			input.autocomplete( "close" );
		},
		change: function( event, ui ) {
			if ( !ui.item ) {
				var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( $(this).val() ) + "$", "i" ),
					valid = false;
				if ( !valid ) {
					$( this ).val( "" );
					if(!unSave)
						saveVal( dataElementId );
					input.data( "autocomplete" ).term = "";
					return false;
				}
			}
		}
	})
	.addClass( "ui-widget" );
	
	input.data( "autocomplete" )._renderItem = function( ul, item ) {
		return $( "<li></li>" )
			.data( "item.autocomplete", item )
			.append( "<a>" + item.label + "</a>" )
			.appendTo( ul );
	};
		
	var wrapper = this.wrapper = $( "<span style='width:200px'>" )
			.addClass( "ui-combobox" )
			.insertAfter( input );
						
	var button = $( "<a style='width:20px; margin-bottom:-5px;height:20px;'>" )
		.attr( "tabIndex", -1 )
		.attr( "title", i18n_show_all_items )
		.appendTo( wrapper )
		.button({
			icons: {
				primary: "ui-icon-triangle-1-s"
			},
			text: false
		})
		.addClass('small-button')
		.click(function() {
			if ( input.autocomplete( "widget" ).is( ":visible" ) ) {
				input.autocomplete( "close" );
				return;
			}
			$( this ).blur();
			input.autocomplete( "search", "" );
			input.focus();
		});
}

