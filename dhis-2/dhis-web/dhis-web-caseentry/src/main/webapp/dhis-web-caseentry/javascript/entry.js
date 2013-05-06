
var MAX_DROPDOWN_DISPLAYED = 30;

//------------------------------------------------------------------------------
// Save value
//------------------------------------------------------------------------------

function saveVal( dataElementUid )
{
    var programStageUid = getProgramStageUid();
    var fieldId = programStageUid + '-' + dataElementUid + '-val';
    var field = byId( fieldId );

    if( field == null) return;

    var fieldValue = jQuery.trim( field.value );
    var arrData = jQuery( "#" + fieldId ).attr( 'data' ).replace( '{', '' ).replace( '}', '' ).replace( /'/g, "" ).split( ',' );
    var data = new Array();

    for ( var i in arrData ) {
        var values = arrData[i].split( ':' );
        var key = jQuery.trim( values[0] );
        var value = jQuery.trim( values[1] );

        data[key] = value;
    }

    var dataElementName = data['deName'];
    var type = data['deType'];

    field.style.backgroundColor = SAVING_COLOR;

    if ( fieldValue != '' ) {
        if ( type == 'int' || type == 'number' || type == 'positiveNumber' || type == 'negativeNumber' ) {
            if ( type == 'int' && !isInt( fieldValue ) ) {
                field.style.backgroundColor = '#ffcc00';

                window.alert( i18n_value_must_integer + '\n\n' + dataElementName );

                field.focus();

                return;
            }
            else if ( type == 'number' && !isNumber( fieldValue ) ) {
                field.style.backgroundColor = '#ffcc00';
                window.alert( i18n_value_must_number + '\n\n' + dataElementName );
                field.focus();

                return;
            }
            else if ( type == 'positiveNumber' && !isPositiveInt( fieldValue ) ) {
                field.style.backgroundColor = '#ffcc00';
                window.alert( i18n_value_must_positive_integer + '\n\n' + dataElementName );
                field.focus();

                return;
            }
            else if ( type == 'negativeNumber' && !isNegativeInt( fieldValue ) ) {
                field.style.backgroundColor = '#ffcc00';
                window.alert( i18n_value_must_negative_integer + '\n\n' + dataElementName );
                field.focus();

                return;
            }
        }
        else if ( type == 'date' ) {
            field.focus();
        }

    }
    
	var value = fieldValue;

    if ( type == 'trueOnly' ) {
        if ( field.checked )
            fieldValue = "true";
        else
            fieldValue = "";
    }

    var valueSaver = new ValueSaver( dataElementUid, fieldValue, type, SUCCESS_COLOR );
    valueSaver.save();
}

function saveOpt( dataElementUid )
{
	var programStageUid = getProgramStageUid();
	var field = byId( programStageUid + '-' + dataElementUid + '-val' );	
	field.style.backgroundColor = SAVING_COLOR;
	
	var valueSaver = new ValueSaver( dataElementUid, field.options[field.selectedIndex].value, 'bool', SUCCESS_COLOR );
    valueSaver.save();
}

function updateProvidingFacility( dataElementUid, checkField )
{
	var programStageUid = byId( 'programStageUid' ).value;
	var checked= checkField.checked;

    var facilitySaver = new FacilitySaver( dataElementUid, checked, SUCCESS_COLOR );
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

function getProgramType() {
    var programType = jQuery( '.stage-object-selected' ).attr( 'programType' );

    if ( programType == undefined ) {
        programType = jQuery( '#programId option:selected' ).attr( 'programType' );
    }

    return programType;
}

function getProgramStageUid() {
    var programStageUid = jQuery( '.stage-object-selected' ).attr( 'psuid' );

    if ( programStageUid == undefined ) {
        programStageUid = jQuery( '#programId option:selected' ).attr( 'psuid' );
    }

    if ( programStageUid == undefined ) {
        programStageUid = jQuery( '#entryFormContainer [id=programStageUid]' ).val();
    }

    if ( programStageUid == undefined ) {
        programStageUid = jQuery( '#programStageUid' ).val();
    }

    return programStageUid;
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
	
    var match = /.*\[(.*)\]/.exec( str ); //value[-dataElementUid-]
	
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
    var dataElementUid = dataElementId_;
	var providedElsewhereId = getFieldValue('programStageUid') + "-" + dataElementId_ + "-facility";
	var value = value_;
	var type = dataElementType_;
    var resultColor = resultColor_;

    this.save = function()
    {
        var params = 'dataElementUid=' + dataElementUid;
            params += '&programStageInstanceId=' + getFieldValue( 'programStageInstanceId' );

        params += '&providedElsewhere=';

		if( byId( providedElsewhereId ) != null )
			params += byId( providedElsewhereId ).checked;
		
		params += '&value=';

        if ( value != '' )
            params += htmlEncode( value );

        $.ajax({
           type: "POST",
           url: "saveValue.action",
           data: params,
           dataType: "xml",
           success: function(result){
                handleResponse (result);
           },
           error: function(request) {
                handleHttpError (request);
           }
        });
    };
 
    function handleResponse( rootElement )
    {
        var codeElement = rootElement.getElementsByTagName( 'code' )[0];
        var code = parseInt( codeElement.firstChild.nodeValue );

        if ( code == 0 ) {
            markValue( resultColor );
        }
        else {
            if ( value != "" ) {
                markValue( ERROR );
                window.alert( i18n_saving_value_failed_status_code + '\n\n' + code );
            }
            else {
                markValue( resultColor );
            }
        }
    }
 
    function handleHttpError( errorCode )
    {
        if( getProgramType() == 3 && DAO.store ) {
            var data = {
                providedElsewhere: byId( providedElsewhereId ) != null ? byId( providedElsewhereId ).checked : false,
                value: value != '' ? htmlEncode( value ) : value
            };

            var dataValueKey = $( '#programStageInstanceId' ).val();
            var key = dataElementUid;

            DAO.store.get( 'dataValues', dataValueKey ).done( function ( obj ) {
                if ( !obj ) {
                    markValue( ERROR );
                    window.alert( i18n_saving_value_failed_error_code + '\n\n' + errorCode );
                    return;
                }

                if ( !obj.values ) {
                    obj.values = {};
                }

                obj.id = dataValueKey;
                obj.values[key] = data;

                this.set( 'dataValues', obj );
                markValue( resultColor );
            } );
        } else {
            markValue( ERROR );
            window.alert( i18n_saving_value_failed_error_code + '\n\n' + errorCode );
        }
    }
 
    function markValue( color )
    {
		var programStageUid = getProgramStageUid();
		var element = byId( programStageUid + "-" + dataElementUid + '-val' );
        element.style.backgroundColor = color;
    }
}

function FacilitySaver( dataElementId_, providedElsewhere_, resultColor_ )
{
    var dataElementUid = dataElementId_;
	var providedElsewhere = providedElsewhere_;
    var resultColor = resultColor_;

    this.save = function()
    {
		var params  = 'dataElementUid=' + dataElementUid;
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
					setFieldValue( 'programStageUid', selectedProgramStageInstance.attr('psuid') );
					
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
            $.ajax({
                url: 'completeDataEntry.action',
                dataType: 'json',
                data: {
                    programStageInstanceId: getFieldValue( 'programStageInstanceId' )
                },
                type: 'POST'
            } ).done(function(json) {
                jQuery(".stage-object-selected").css('border-color', COLOR_GREEN);
                jQuery(".stage-object-selected").css('background-color', COLOR_LIGHT_GREEN);

                var irregular = jQuery('#entryFormContainer [name=irregular]').val();
                var displayGenerateEventBox = jQuery('#entryFormContainer [name=displayGenerateEventBox]').val();
                var programInstanceId = jQuery('#entryFormContainer [id=programInstanceId]').val();

                if( irregular == 'true' && displayGenerateEventBox=="true" ) {
                    var programStageUid = getProgramStageUid();
                    showCreateNewEvent( programInstanceId, programStageUid );
                }

                if( getProgramType()=='2' || json.response == 'programcompleted' ) {
                    var completedRow = jQuery('#td_' + programInstanceId).html();
                    jQuery('#completedList' ).append('<option value="' +  programInstanceId + '">' + getInnerHTML('infor_' + programInstanceId ) + '</option>');
                }

                var blocked = jQuery('#entryFormContainer [id=blockEntryForm]').val();

                if( blocked=='true' ) {
                    blockEntryForm();
                }

                var remindCompleted = jQuery('#entryFormContainer [id=remindCompleted]').val();

                if( remindCompleted=='true' ) {
                    unenrollmentForm(programInstanceId, 1);
                }

                disableCompletedButton(true);

                var eventBox = jQuery('#ps_' + getFieldValue('programStageInstanceId'));
                eventBox.attr('status',1);
                resetActiveEvent( eventBox.attr("pi") );

                hideLoader();

                if ( isCreateEvent ) {
                    showAddEventForm();
                }
            } ).fail(function() {
                if ( getProgramType() == 3 ) {
                    var programStageInstanceId = getFieldValue( 'programStageInstanceId' );

                    if ( window.DAO && window.DAO.store ) {
                        jQuery(".stage-object-selected").css('border-color', COLOR_GREEN);
                        jQuery(".stage-object-selected").css('background-color', COLOR_LIGHT_GREEN);

                        DAO.store.get( 'dataValues', programStageInstanceId ).done( function ( obj ) {
                            if ( !obj ) {
                                return;
                            }

                            obj.executionDate.completed = true;
                            DAO.store.set('dataValues', obj);
                        } );

                        var blocked = jQuery('#entryFormContainer [id=blockEntryForm]').val();

                        if( blocked=='true' ) {
                            blockEntryForm();
                        }

                        disableCompletedButton(true);
                        hideLoader();
                    }
                }
            });
		}
    }
}

function doUnComplete( isCreateEvent )
{	
	if( confirm(i18n_incomplete_confirm_message) )
	{
        $.ajax({
            url: 'uncompleteDataEntry.action',
            dataType: 'json',
            data: {
                programStageInstanceId: getFieldValue( 'programStageInstanceId' )
            },
            type: 'POST'
        } ).done(function(json) {
            jQuery(".stage-object-selected").css('border-color', COLOR_LIGHTRED);
            jQuery(".stage-object-selected").css('background-color', COLOR_LIGHT_LIGHTRED);
            unblockEntryForm();
            disableCompletedButton(false);
            var eventBox = jQuery('#ps_' + getFieldValue('programStageInstanceId'));
            eventBox.attr('status',2);
            resetActiveEvent( eventBox.attr("pi") );
        } ).fail(function() {
            if ( getProgramType() == 3 ) {
                var programStageInstanceId = getFieldValue( 'programStageInstanceId' );

                if ( window.DAO && window.DAO.store ) {
                    DAO.store.get( 'dataValues', programStageInstanceId ).done( function ( obj ) {
                        if(!obj) {
                            return;
                        }

                        obj.executionDate.completed = false;
                        DAO.store.set( 'dataValues', obj );
                    } );
                }

                unblockEntryForm();
                disableCompletedButton(false);
            }
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

function loadProgramStageInstance( programStageInstanceId, always ) {
    if( programStageInstanceId.indexOf('local') != -1 ) {
        $( "#programStageInstanceId" ).val( programStageInstanceId );
        $( "#entryFormContainer input[id='programStageInstanceId']" ).val( programStageInstanceId );

        DAO.store.get( 'dataValues', programStageInstanceId ).done( function ( obj ) {
            if(obj && obj.values !== undefined ) {
                _.each( _.keys(obj.values), function(key, idx) {
                    var fieldId = getProgramStageUid() + '-' + key + '-val';
                    var field = $('#' + fieldId);

                    if ( field ) {
                        field.val( decodeURI( obj.values[key].value ) );
                    }
                });
            }

            if( always ) always();

            $('#commentInput').attr('disabled', true);
            $('#validateBtn').attr('disabled', true);
        } );
    } else {
        return $.ajax({
            url: 'getProgramStageInstance.action',
            data: {
                'programStageInstanceId': programStageInstanceId
            },
            type: 'GET',
            dataType: 'json'
        } ).done(function(data) {
            $( "#programStageInstanceId" ).val( data.id );
            $( "#entryFormContainer input[id='programStageInstanceId']" ).val( data.id );
            $( "#entryFormContainer input[id='incidentDate']" ).val( data.programInstance.dateOfIncident );
            $( "#entryFormContainer input[id='programInstanceId']" ).val( data.programInstance.id );
            $( "#entryFormContainer input[id='irregular']" ).val( data.programStage.irregular );
            $( "#entryFormContainer input[id='displayGenerateEventBox']" ).val( data.programStage.displayGenerateEventBox );
            $( "#entryFormContainer input[id='completed']" ).val( data.completed );
            $( "#entryFormContainer input[id='programStageId']" ).val( data.programStage.id  );
            $( "#entryFormContainer input[id='programStageUid']" ).val( data.programStage.uid  );
            $( "#entryFormContainer input[id='programId']" ).val( data.program.id );
            $( "#entryFormContainer input[id='validCompleteOnly']" ).val( data.programStage.validCompleteOnly );
            $( "#entryFormContainer input[id='currentUsername']" ).val( data.currentUsername );
            $( "#entryFormContainer input[id='blockEntryForm']" ).val( data.program.blockEntryForm );
            $( "#entryFormContainer input[id='remindCompleted']" ).val( data.program.remindCompleted );

            $( "input[id='dueDate']" ).val( data.dueDate );
            $( "input[id='executionDate']" ).val( data.executionDate );

            if ( data.program.type != '1' ) {
                hideById( 'newEncounterBtn' );
            }

            if ( data.program.type == '1' && data.programInstance.status == '1' ) {
                jQuery("[id=entryFormContainer] :input").prop('disabled', true);
                jQuery("[id=entryFormContainer] :input").datepicker("destroy");
                jQuery("[id=executionDate]").prop('disabled', true);
                jQuery("[id=executionDate]").datepicker("destroy");
            }

            if(data.executionDate) {
                $( '#executionDate' ).val(data.executionDate);
                $( '#entryForm' ).removeClass( 'hidden' ).addClass( 'visible' );
                $( '#inputCriteriaDiv' ).removeClass( 'hidden' );
            }

            if ( data.programStage.captureCoordinates ) {
                $( '#longitude' ).val( data.longitude );
                $( '#latitude' ).val( data.latitude );
            }

            if(data.comments.length > 0) {
                $.each(data.comments, function(idx, item) {
                    var comment = [
                        "<tr>",
                        "<td>" + item.createdDate + "</td>",
                        "<td>" + item.creator + "</td>",
                        "<td>" + item.text + "</td>",
                        "</tr>"
                    ].join(' ');

                    $( '#commentTB' ).append( comment )
                });
            }

            _.each( data.dataValues, function ( value, key ) {
                var fieldId = getProgramStageUid() + '-' + key + '-val';
                var field = $('#' + fieldId);

                if ( field ) {
                    field.val( decodeURI( value.value ));
                }
            } );

            if( always ) always();

            $('#commentInput').removeAttr('disabled');
            $('#validateBtn').removeAttr('disabled');
        } );
    }
}

function entryFormContainerOnReady()
{
	var currentFocus = undefined;
    var programStageInstanceId = getFieldValue( 'programStageInstanceId' );
	
    loadProgramStageInstance( programStageInstanceId, function() {
        if( jQuery("#entryFormContainer") ) {
            // Display entry form if excution-date is not null
            if ( jQuery( "#executionDate" ).val() == '' ) {
                hideById( 'entryForm' );
            }
            else if ( jQuery( "#executionDate" ).val() != '' ) {
                toggleContentForReportDate( true );
            }

            // Set buttons by completed-status of program-stage-instance
            var completed = $( "#entryFormContainer input[id='completed']" ).val();
            var blockEntry = $( "#entryFormContainer input[id='blockEntryForm']" ).val();

            if ( completed == 'true' ) {
                disable( 'completeBtn' );
                enable( 'uncompleteBtn' );
                if ( blockEntry == 'true' ) {
                    blockEntryForm();
                }
            }
            else {
                enable( 'completeBtn' );
                disable( 'uncompleteBtn' );
            }

            jQuery( "input[name='entryfield'],select[name='entryselect']" ).each( function () {
                jQuery( this ).focus( function () {
                    currentFocus = this;
                } );

                jQuery( this ).addClass( "inputText" );
            } );

            TOGGLE.init();

            jQuery( "#entryForm :input" ).each( function () {
                if ( jQuery( this ).attr( 'options' ) != null && jQuery( this ).attr( 'options' ) == 'true' ) {
                    autocompletedField( jQuery( this ).attr( 'id' ) );
                }
                else if ( jQuery( this ).attr( 'username' ) != null && jQuery( this ).attr( 'username' ) == 'true' ) {
                    autocompletedUsernameField( jQuery( this ).attr( 'id' ) );
                }
            } );
        }
    });
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

function searchOptionSet( uid, query, success ) {
    if(window.DAO !== undefined && window.DAO.store !== undefined ) {
        DAO.store.get( 'optionSets', uid ).done( function ( obj ) {
            if(obj) {
                var options = [];

                if(query == null || query == "") {
                    options = obj.optionSet.options.slice(0, MAX_DROPDOWN_DISPLAYED-1);
                } else {
                    query = query.toLowerCase();

                    _.each(obj.optionSet.options, function(item, idx) {
                        if ( item.toLowerCase().indexOf( query ) != -1 ) {
                            options.push(item);
                        }
                    });
                }

                success( $.map( options, function ( item ) {
                    return {
                        label: item,
                        id: item
                    };
                } ) );
            } else {
                getOptions( uid, query, success );
            }
        } );
    } else {
        getOptions( uid, query, success );
    }
}

function getOptions( uid, query, success ) {
    $.ajax( {
        url: "getOptions.action?id=" + uid + "&query=" + query,
        dataType: "json",
        cache: true,
        success: function ( data ) {
            success( $.map( data.options, function ( item ) {
                return {
                    label: item.o,
                    id: item.o
                };
            } ) );
        }
    } );
}

function autocompletedField( idField )
{
	var input = jQuery( "#" +  idField );
	input.parent().width( input.width() + 50 );
	var dataElementUid = input.attr('id').split('-')[1];
	
	input.autocomplete({
		delay: 0,
		minLength: 0,
		source: function( request, response ){
            searchOptionSet( dataElementUid, input.val(), response );
		},
		minLength: 0,
		select: function( event, ui ) {
			var fieldValue = ui.item.value;

			if ( !dhis2.trigger.invoke( "caseentry-value-selected", [dataElementUid, fieldValue] ) ) {
				input.val( "" );
				return false;
			}

			input.val( fieldValue );
			if ( !unSave ) {
				saveVal( dataElementUid );
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
						saveVal( dataElementUid );
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

function searchUsername( query, success ) {
    if(window.DAO !== undefined && window.DAO.usernames !== undefined ) {
        DAO.usernames.fetch('usernames', function(store, arr) {
            if ( arr.length > 0 ) {
                var obj = arr[0];
                var usernames = [];

                if(query == null || query == "") {
                    delete obj['key'];
                    usernames = obj.slice(0, MAX_DROPDOWN_DISPLAYED-1);
                } else {
                    query = query.toLowerCase();

                    _.each(obj, function(item, idx) {
                        if ( item.toLowerCase().indexOf( query ) != -1 ) {
                            usernames.push(item);
                        }
                    });
                }

                success( $.map( usernames, function ( item ) {
                    return {
                        label: item,
                        id: item
                    };
                } ) );
            } else {
                getUsername( query, success );
            }
        } );
    } else {
        getUsername( query, success );
    }
}

function getUsername( query, success ) {
    return $.ajax( {
        url: "getUsernameList.action?query=" + query,
        dataType: "json",
        cache: true,
        success: function ( data ) {
            success( $.map( data.usernames, function ( item ) {
                return {
                    label: item.u,
                    id: item.u
                };
            } ) );
        }
    } );
}

function autocompletedUsernameField( idField )
{
	var input = jQuery( "#" +  idField );
	input.parent().width( input.width() + 50 );
	var dataElementUid = input.attr('id').split('-')[1];
	
	input.autocomplete({
		delay: 0,
		minLength: 0,
		source: function( request, response ){
            searchUsername( input.val(), response );
		},
		minLength: 0,
		select: function( event, ui ) {
			var fieldValue = ui.item.value;
			
			if ( !dhis2.trigger.invoke( "caseentry-value-selected", [dataElementUid, fieldValue] ) ) {
				input.val( "" );
				return false;
			}
			
			input.val( fieldValue );			
			if ( !unSave ) {
				saveVal( dataElementUid );
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
						saveVal( dataElementUid );
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

function filterOnSection()
{
    var value = 'sec_' + $( '#filterDataSetSection option:selected' ).val();
    
    if ( value == 'all' )
    {
        $( '.formSection' ).show();
    }
    else
    {
        $( '.formSection' ).hide();
        $( '#' + value ).show();
    }
}
