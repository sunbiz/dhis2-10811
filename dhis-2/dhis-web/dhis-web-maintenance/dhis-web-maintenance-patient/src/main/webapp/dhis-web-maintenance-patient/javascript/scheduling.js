// -----------------------------------------------------------------------
// Schedule Messages
// -----------------------------------------------------------------------

function scheduleTasks()
{
	$.post( 'scheduleTasks.action',{
		execute:false,
		schedule: true,
		gateWayId: getFieldValue("gatewayId"),
		timeSendingMessage: getFieldValue("timeSendingMessage")
	}, function( json ){
		var status = json.scheduleTasks.status;
		if( status=='not_started' ){
			status = i18n_not_started;
		}
		setInnerHTML('info', i18n_scheduling_is + " " + status);
		if( json.scheduleTasks.running=="true" ){
			setFieldValue('scheduledBtn', i18n_stop);
			enable('executeButton');
		}
		else{
			setFieldValue('scheduledBtn', i18n_start);
			disable('executeButton');
		}
	});
}

function executeTasks()
{
	var ok = confirm( i18n_execute_tasks_confirmation );
	setWaitMessage( i18n_executing );	
	if ( ok )
	{		
		$.post( 'executeSendMessage.action',{}
		, function( json ){
			setMessage(i18n_execute_success);
		});
	}
}

// -----------------------------------------------------------------------
// Schedule Automated Aggregate
// -----------------------------------------------------------------------

function schedulingAggCondTasks()
{
	$.post( 'scheduleCaseAggTasks.action',{
		execute:false
	}, function( json ){
		var status = json.scheduleTasks.status;
		if( status=='not_started' ){
			status = i18n_not_started;
		}
		setInnerHTML('info', i18n_scheduling_is + " " + status);
		if( json.scheduleTasks.running=="true" ){
			setFieldValue('scheduledBtn', i18n_stop);
			enable('executeButton');
		}
		else{
			setFieldValue('scheduledBtn', i18n_start);
			disable('executeButton');
		}
	});
}

function executeAggCondTasks()
{
	var ok = confirm( i18n_execute_tasks_confirmation );
	setWaitMessage( i18n_executing );	
	if ( ok )
	{
		$.post( 'scheduleCaseAggTasks.action',{
			execute:true
		},function( json ){
			setMessage(i18n_execute_success);
		});
	}
}