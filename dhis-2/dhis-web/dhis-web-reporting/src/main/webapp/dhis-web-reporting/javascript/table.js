// -----------------------------------------------------------------------------
// Save ReportTable
// -----------------------------------------------------------------------------

function saveTable()
{
	if ( validateCollections() )
	{
		$.postJSON( "validateTable.action", { id:getFieldValue( "tableId" ), "name":getFieldValue( "tableName" ) }, function( json )
		{
			if ( json.response == "input" )
			{
				setMessage( json.message );
				return false;
			}
			else if ( json.response == "success" )
			{
				selectTableForm();
	        	$( "#tableForm" ).submit();
			}
		} );
	}
}

function selectTableForm()
{
    selectAllById( "selectedDataElements" );

    if ( isNotEmpty( "selectedIndicators" ) )
    {
        selectAllById( "selectedIndicators" );
    }

    if ( isNotEmpty( "selectedDataSets" ) )
    {
        selectAllById( "selectedDataSets" );
    }

    selectAllById( "selectedPeriods" );
    selectAllById( "selectedOrganisationUnits" );
    
    if ( isNotEmpty( "selectedOrganisationUnitGroups" ) )
    {
        selectAllById( "selectedOrganisationUnitGroups" );
    }
}

// -----------------------------------------------------------------------------
// Remove
// -----------------------------------------------------------------------------

function removeTable( tableId, tableName )
{
    removeItem( tableId, tableName, i18n_confirm_delete, "removeTable.action" );
}

// -----------------------------------------------------------------------------
// Validation
// -----------------------------------------------------------------------------

function validateCollections()
{
    if ( isChecked( "regression" ) && selectionTreeSelection.getSelected().length > 1 )
    {
        setMessage( i18n_cannot_include_more_organisation_unit_regression );

        return false;
    }

    if ( !hasElements( "selectedDataElements" ) && !hasElements( "selectedIndicators" )
            && !hasElements( "selectedDataSets" ) )
    {
        setMessage( i18n_must_select_at_least_one_indictor_data_element_data_set );

        return false;
    }

    if ( !selectionTreeSelection.isSelected() && !hasElements( "selectedOrganisationUnitGroups" ) && !organisationUnitReportParamsChecked() )
    {
        setMessage( i18n_must_select_at_least_one_unit );

        return false;
    }

    if ( bothOrganisationUnitReportParamsChecked() )
    {
        setMessage( i18n_cannot_select_orgunit_and_parent_orgunit_param );

        return false;
    }

    if ( !hasElements( "selectedPeriods" ) && !relativePeriodsChecked() )
    {
        setMessage( i18n_must_select_at_least_one_period );

        return false;
    }

    return true;
}

function isTrue( elementId )
{
    var value = document.getElementById( elementId ).value;

    return value && value == "true" ? true : false;
}

function organisationUnitReportParamsChecked()
{
    return ( isChecked( "paramLeafParentOrganisationUnit" ) || isChecked( "paramGrandParentOrganisationUnit" ) 
    	|| isChecked( "paramParentOrganisationUnit" ) || isChecked( "paramOrganisationUnit" ) );
}

function bothOrganisationUnitReportParamsChecked()
{
    var count = 0;

    if ( isChecked( "paramLeafParentOrganisationUnit" ) )
    {
        count++;
    }
    if ( isChecked( "paramGrandParentOrganisationUnit" ) )
    {
        count++;
    }
    if ( isChecked( "paramParentOrganisationUnit" ) )
    {
        count++;
    }
    if ( isChecked( "paramOrganisationUnit" ) )
    {
        count++;
    }

    return count > 1;
}

// -----------------------------------------------------------------------------
// Details
// -----------------------------------------------------------------------------

function showTableDetails( tableId )
{
    jQuery.post( 'getTable.action', { id: tableId }, function ( json ) {
		setInnerHTML( 'nameField', json.table.name );
		setInnerHTML( 'indicatorsField', json.table.indicators );
		setInnerHTML( 'periodsField', json.table.periods );
		setInnerHTML( 'unitsField', json.table.units );
		setInnerHTML( 'doIndicatorsField', parseBool( json.table.doIndicators ) );
		setInnerHTML( 'doPeriodsField', parseBool( json.table.doPeriods ) );
		setInnerHTML( 'doUnitsField', parseBool( json.table.doUnits ) );

		showDetails();
	} );
}

function parseBool( bool )
{
    return ( bool == "true" ) ? i18n_yes : i18n_no;
}

// -----------------------------------------------------------------------------
// Regression
// -----------------------------------------------------------------------------

function toggleRegression()
{
    if ( document.getElementById( "regression" ).checked )
    {
        check( "doIndicators" );
        uncheck( "doOrganisationUnits" );
        uncheck( "doPeriods" );

        disable( "doOrganisationUnits" );
        disable( "doPeriods" );
    } 
    else
    {
        enable( "doOrganisationUnits" );
        enable( "doPeriods" );
    }
}

function toggleFixedOrganisationUnits()
{
    $( '#fixedOrganisationUnits' ).toggle( 'fast' );
}

function toggleFixedPeriods()
{
    $( '#fixedPeriods' ).toggle( 'fast' );
}

function toggleOrganisationUnitGroups()
{
	$( '#organisationUnitGroupsDiv' ).toggle( 'fast' );
}

// -----------------------------------------------------------------------------
// Dashboard
// -----------------------------------------------------------------------------

function addReportTableToDashboard( id )
{
    var dialog = window.confirm( i18n_confirm_add_report_table_to_dashboard );

    if ( dialog )
    {
        $.get( "addReportTableToDashboard.action?id=" + id );
    }
}
