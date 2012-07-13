var SUCCESS_COLOR = '#ccffcc';
var ERROR_COLOR = '#ccccff';
var SAVING_COLOR = '#ffffcc';
var SUCCESS = 'success';
var ERROR = 'error';
	
//--------------------------------------------------------------------------------------------
// Load program-stages by the selected program
//--------------------------------------------------------------------------------------------

function loadProgramStages()
{
	jQuery('#createNewEncounterDiv').dialog('close');
	hideById('dataEntryFormDiv');
	setFieldValue('executionDate','');
	setFieldValue('dueDate','');
	disableCompletedButton(true);
	disable('uncompleteBtn');
	disable('uncompleteInBelowBtn');
	disable('validationBtn');
	disable('validationInBelowBtn');
	disable('newEncounterBtn');
	hideById('inputCriteriaDiv');
	$('#programStageIdTR').html('');
	hideById('programInstanceDiv');
	hideById('colorHelpLink');
	
	var programId = jQuery('#dataRecordingSelectDiv [name=programId]').val();
	if ( programId == 0 )
	{
		return;
	}
	
	jQuery.getJSON( "loadProgramStageInstances.action",
		{
			programId: programId
		},  
		function( json ) 
		{    
			showById('programInstanceDiv');
			hideById('executionDateTB');
				
			var type = jQuery('#dataRecordingSelectDiv [name=programId] option:selected').attr('type');
			if( type == 1 && json.programStageInstances.length > 1 )
			{
				showById('colorHelpLink');
				for ( i in json.programStageInstances ) 
				{
					if( i!= 0 )
					{
						$('#programStageIdTR').append('<td><img src="images/rightarrow.png"></td>');
					}

					var status =json.programStageInstances[i].status;
					var programStageInstanceId = json.programStageInstances[i].id;
					var programStageId = json.programStageInstances[i].programStageId;
					var programStageName= json.programStageInstances[i].programStageName;
					var elementId = prefixId + programStageInstanceId;
					
					$('#programStageIdTR').append('<td><input name="programStageBtn" '
						+ 'id="' + elementId + '"' 
						+ 'type="button" class="stage-object" '
						+ 'psid="' + programStageId + '"' 
						+ 'psname="' + programStageName + '" '
						+ 'dueDate="' + json.programStageInstances[i].dueDate + '"'
						+ 'value="'+ programStageName + ' ' + json.programStageInstances[i].dueDate + '" '
						+ 'onclick="javascript:loadDataEntry(' + programStageInstanceId + ')"></td>');
					setEventColorStatus( elementId, status );
				}
				
				disableCompletedButton(true);
				disable('validationBtn');
				disable('validationInBelowBtn');
				showById('programStageIdTR');
				showById('programInstanceFlowDiv');
			}
			// Load entry form for Single-event program or normal program with only one program-stage
			else 
			{
				jQuery('#dueDateTR').attr('class','hidden');
				disableCompletedButton(false);
				hideById('programStageIdTR');
				hideById('programInstanceFlowDiv');
				var programStageInstanceId = '';
				if( json.programStageInstances.length == 1 )
				{
					programStageInstanceId = json.programStageInstances[0].id;
				}
				loadDataEntry( programStageInstanceId );
			}
	});
}

//--------------------------------------------------------------------------------------------
// Load data-entry-form
//--------------------------------------------------------------------------------------------

function loadDataEntry( programStageInstanceId )
{
	setInnerHTML('dataEntryFormDiv', '');
	showById('executionDateTB');
	showById('dataEntryFormDiv');
	setFieldValue( 'dueDate', '' );
	setFieldValue( 'executionDate', '' );
	disable('validationBtn');
	disable('validationInBelowBtn');
	disableCompletedButton(true);
	disable('uncompleteBtn');
	disable('uncompleteInBelowBtn');
	disable('newEncounterBtn');
	
	jQuery(".stage-object-selected").removeClass('stage-object-selected');
	var selectedProgramStageInstance = jQuery( '#' + prefixId + programStageInstanceId );
	selectedProgramStageInstance.addClass('stage-object-selected');
	setFieldValue( 'programStageId', selectedProgramStageInstance.attr('psid') );
	
	showLoader();	
	$( '#dataEntryFormDiv' ).load( "dataentryform.action", 
		{ 
			programStageInstanceId: programStageInstanceId
		},function( )
		{
			var executionDate = jQuery('#dataRecordingSelectDiv input[id=executionDate]').val();
			var completed = jQuery('#entryFormContainer input[id=completed]').val();
			var irregular = jQuery('#entryFormContainer input[id=irregular]').val();
			showById('inputCriteriaDiv');
			enable('validationBtn');
			enable('validationInBelowBtn');
			if( executionDate == '' )
			{
				disable('validationBtn');
				disable('validationInBelowBtn');
			}
			else if( executionDate != '' && completed == 'false' )
			{
				disableCompletedButton(false);
			}
			else if( completed == 'true' )
			{
				disableCompletedButton(true);
			}
			
			if( completed == 'true' && irregular == 'true' )
			{
				enable( 'newEncounterBtn' );
			}
			
			hideLoader();
			hideById('contentDiv'); 
		} );
}

//------------------------------------------------------------------------------
// Save value
//------------------------------------------------------------------------------

function saveVal( dataElementId )
{
	if( byId('programStageId') == null) return;
	var programStageId = byId('programStageId').value;
	
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
	var programStageId = byId('programStageId').value;
	var field = byId( programStageId + '-' + dataElementId + '-val' );	
	field.style.backgroundColor = SAVING_COLOR;
	
	var valueSaver = new ValueSaver( dataElementId, field.options[field.selectedIndex].value, 'bool', SUCCESS_COLOR );
    valueSaver.save();
}

function updateProvidingFacility( dataElementId, checkField )
{
	var programStageId = byId( 'programStageId' ).value;
	var checked= checkField.checked;
	var spanField = byId( 'span_' + checkField.id );
	spanField.style.backgroundColor = SAVING_COLOR;
    
    var facilitySaver = new FacilitySaver( dataElementId, checked, SUCCESS_COLOR );
    facilitySaver.save();
    
}

function saveExecutionDate( programId, executionDateValue )
{
    var field = document.getElementById( 'executionDate' );
	
    field.style.backgroundColor = SAVING_COLOR;
	
    var executionDateSaver = new ExecutionDateSaver( programId, executionDateValue, SUCCESS_COLOR );
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
	var providedElsewhereId = getFieldValue('programStageId') + "_" + dataElementId_ + "_facility";
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
		var programStageId = getFieldValue('programStageId');
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
        if ( code == 0 )
        {
            markValue( SUCCESS );
        }
        else
        {
            markValue( ERROR );
            window.alert( i18n_saving_value_failed_status_code + '\n\n' + code );
        }
    }

    function handleHttpError( errorCode )
    {
        markValue( ERROR );
        window.alert( i18n_saving_value_failed_error_code + '\n\n' + errorCode );
    }

    function markValue( result )
    {
		var programStageId = byId( 'programStageId' ).value;
		var element = byId('span_' + programStageId + '_' + dataElementId + '_facility');
        if( result == SUCCESS )
        {
            element.style.backgroundColor = SUCCESS_COLOR;
        }
        else if( result == ERROR )
        {
            element.style.backgroundColor = ERROR_COLOR;
        }
    }
}

function ExecutionDateSaver( programId_, executionDate_, resultColor_ )
{
    var programId = programId_;
    var executionDate = executionDate_;
    var resultColor = resultColor_;

    this.save = function()
    {
		var params  = "executionDate=" + executionDate;
			params += "&programId=" + programId;
			
		$.ajax({
			   type: "POST",
			   url: "saveExecutionDate.action",
			   data: params,
			   dataType: "xml",
			   success: function( result ){
					handleResponse (result);
					
					var selectedProgramStageInstance = jQuery( '#' + prefixId + getFieldValue('programStageInstanceId') );
					jQuery(".stage-object-selected").css('border-color', COLOR_LIGHTRED);
					jQuery(".stage-object-selected").css('background-color', COLOR_LIGHT_LIGHTRED);
					disableCompletedButton(false);
					enable('validationBtn');
					enable('validationInBelowBtn');
					disable('newEncounterBtn');
					setFieldValue( 'programStageId', selectedProgramStageInstance.attr('psid') );
					
			   },
			   error: function(request,status,errorThrown) {
					handleHttpError (request);
			   }
			});
    };

    function handleResponse( rootElement )
    {
		rootElement = rootElement.getElementsByTagName( 'message' )[0];
        var codeElement = rootElement.getAttribute( 'type' );
        if ( codeElement == 'success' )
        {
            markValue( resultColor );
			if( getFieldValue('programStageInstanceId' )=='' )
			{
				var programStageInstanceId = rootElement.firstChild.nodeValue;
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

function doComplete( isCreateEvent )
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
				function (data)
				{
					jQuery(".stage-object-selected").css('border-color', COLOR_GREEN);
					jQuery(".stage-object-selected").css('background-color', COLOR_LIGHT_GREEN);

					disableCompletedButton(true);
					enable('newEncounterBtn');
					var irregular = jQuery('#entryFormContainer [name=irregular]').val();
					if( irregular == 'true' )
					{
						enable('createEventBtn');
						jQuery('#createNewEncounterDiv').dialog({
								title: i18n_create_new_event,
								maximize: true, 
								closable: true,
								modal:false,
								overlay:{background:'#000000', opacity:0.1},
								width: 300,
								height: 100
							}).show('fast');
							
						var standardInterval =  jQuery('#dataRecordingSelectDiv [name=programStageId] option:selected').attr('standardInterval');
						var date = new Date();
						var d = date.getDate() + eval(standardInterval);
						var m = date.getMonth();
						var y = date.getFullYear();
						var edate= new Date(y, m, d);
												
						jQuery('#dueDateNewEncounter').datepicker( "setDate" , edate );
					}
					
					var selectedProgram = jQuery('#dataRecordingSelectForm [name=programId] option:selected');
					if( selectedProgram.attr('type')=='2' && irregular == 'false' )
					{
						selectedProgram.remove();
						hideById('programInstanceDiv');
						hideById('entryFormContainer');
					}
					
					selection.enable();
					hideLoader();
					hideById('contentDiv');
					
					if( isCreateEvent )
					{
						showAddEventForm();
					}
				});
		}
    }
}

function doUnComplete( isCreateEvent )
{	
	if( confirm(i18n_uncomplete_confirm_message) )
	{
		$.postJSON( "uncompleteDataEntry.action",
			{
				programStageInstanceId: getFieldValue('programStageInstanceId')
			},
			function (data)
			{
				jQuery(".stage-object-selected").css('border-color', COLOR_LIGHTRED);
				jQuery(".stage-object-selected").css('background-color', COLOR_LIGHT_LIGHTRED);
				disableCompletedButton(false);
			});
	}
    
}


function closeDueDateDiv()
{
	jQuery('#createNewEncounterDiv').dialog('close');
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
		});
    }
}

//------------------------------------------------------
// Run validation
//------------------------------------------------------

function runValidation()
{
	$('#validateProgramDiv' ).load( 'validateProgram.action' ).dialog({
			title: i18n_violate_validation,
			maximize: true, 
			closable: true,
			modal:true,
			overlay:{background:'#000000', opacity:0.1},
			width: 800,
			height: 450
		});
}

//------------------------------------------------------
// Register Irregular-encounter
//------------------------------------------------------

function registerIrregularEncounter( dueDate )
{
	jQuery.postJSON( "registerIrregularEncounter.action",{ dueDate: dueDate }, 
		function( json ) 
		{   
			var programStageInstanceId = json.message;
			jQuery('#createNewEncounterDiv').dialog('close');
			disableCompletedButton(false);
			disable('newEncounterBtn');
			
			var programStageName = jQuery(".stage-object-selected").attr('psname');
			var flag = false;
			jQuery("#programStageIdTR input[name='programStageBtn']").each(function(i,item){
				var element = jQuery(item);
				var dueDateInStage = element.attr('dueDate');
				
				if( dueDate < dueDateInStage && !flag)
				{	
					var elementId = prefixId + programStageInstanceId;
					jQuery('<td><input name="programStageBtn" '
						+ 'id="' + elementId + '" ' 
						+ 'psid="' + programStageInstanceId + '" '
						+ 'psname="' + programStageName + '" '
						+ 'dueDate="' + dueDate + '" '
						+ 'value="'+ programStageName + ' ' + dueDate + '" '
						+ 'onclick="javascript:loadDataEntry(' + programStageInstanceId + ')" '
						+ 'type="button" class="stage-object" '
						+ '></td>'
						+ '<td><img src="images/rightarrow.png"></td>')
					.insertBefore(element.parent());
					setEventColorStatus( elementId, 3 );
					flag = true;
				}
			});
		});
}

function autocompletedField( idField )
{
	var input = jQuery( "#" +  idField );
	var dataElementId = input.attr('id').split('-')[1];
	
	input.autocomplete({
		delay: 0,
		minLength: 0,
		source: function( request, response ){
			$.ajax({
				url: "getOptions.action?id=" + dataElementId + "&query=" + input.val(),
				dataType: "json",
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
			input.val(ui.item.value);
			saveVal( dataElementId );
			input.autocomplete( "close" );
		},
		change: function( event, ui ) {
			if ( !ui.item ) {
				var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( $(this).val() ) + "$", "i" ),
					valid = false;
				if ( !valid ) {
					$( this ).val( "" );
					saveVal( dataElementId );
					input.data( "autocomplete" ).term = "";
					return false;
				}
			}
		}
	})
	.addClass( "ui-widget" );
	
	var button = $( "<button>&nbsp;</button>" )
		.attr( "tabIndex", -1 )
		.attr( "title", i18n_show_all_items )
		.insertAfter( input )
		.button({
			icons: {
				primary: "ui-icon-triangle-1-s"
			},
			text: false
		})
		.addClass( "small-button" )
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

function showColorHelp()
{
	jQuery('#colorHelpDiv').dialog({
		title: i18n_color_quick_help,
		maximize: true, 
		closable: true,
		modal:false,
		width: 500,
		height: 180
	}).show('fast');
}

function disableCompletedButton( disabled )
{
	if(disabled){
		disable('completeBtn');
		disable('completeAndAddNewBtn');
		enable('uncompleteBtn');
		enable('uncompleteAndAddNewBtn');
	}
	else{
		enable('completeBtn');
		enable('completeAndAddNewBtn');
		disable('uncompleteBtn');
		disable('uncompleteAndAddNewBtn');
	}
}
