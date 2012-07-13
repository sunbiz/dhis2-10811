function exportPDF( type )
{	
	var params = "type=" + type;
	params += "&dataDictionaryId=" + jQuery( '#dataDictionaryList' ).val();
	
	exportPdfByType( type, params );
}

function changeValueType( value )
{
    if ( value == 'int' )
    {
		showById( 'numberTypeTR' );
        hideById( 'textTypeTR' );
        enable( 'zeroIsSignificant' );
    } else
    {
        disable( 'zeroIsSignificant' );
		hideById( 'numberTypeTR' );
		hideById( 'textTypeTR' );
		if( value=='string'){
			showById( 'textTypeTR' );
		}
    }

    updateAggreationOperation( value );
}

function updateAggreationOperation( value )
{
    if ( value == 'string' || value == 'date' || value == 'trueOnly' )
    {
        hideById( "aggregationOperator" );
    } else
    {
        showById( "aggregationOperator" );
    }
}

// -----------------------------------------------------------------------------
// Change data element group and data dictionary
// -----------------------------------------------------------------------------

function criteriaChanged()
{
    var dataDictionaryId = getListValue( "dataDictionaryList" );

    var url = "dataElement.action?&dataDictionaryId=" + dataDictionaryId;

    window.location.href = url;
}

// -----------------------------------------------------------------------------
// View details
// -----------------------------------------------------------------------------

function showDataElementDetails( dataElementId )
{
	jQuery.get( '../dhis-web-commons-ajax-json/getDataElement.action', 
		{ "id": dataElementId }, function( json ) {
		setInnerHTML( 'nameField', json.dataElement.name );
		setInnerHTML( 'shortNameField', json.dataElement.shortName );

		var alternativeName = json.dataElement.alternativeName;
		setInnerHTML( 'alternativeNameField', alternativeName ? alternativeName : '[' + i18n_none + ']' );

		var description = json.dataElement.description;
		setInnerHTML( 'descriptionField', description ? description : '[' + i18n_none + ']' );

		var active = json.dataElement.active;
		setInnerHTML( 'activeField', active == 'true' ? i18n_yes : i18n_no );

		var typeMap = {
			'int' : i18n_number,
			'bool' : i18n_yes_no,
			'trueOnly' : i18n_yes_only,
			'string' : i18n_text,
			'date' : i18n_date
		};
		var type = json.dataElement.valueType;
		setInnerHTML( 'typeField', typeMap[type] );

		var domainTypeMap = {
			'aggregate' : i18n_aggregate,
			'patient' : i18n_patient
		};
		var domainType = json.dataElement.domainType;
		setInnerHTML( 'domainTypeField', domainTypeMap[domainType] );

		var aggregationOperator = json.dataElement.aggregationOperator;
		var aggregationOperatorText = i18n_none;
		if ( aggregationOperator == 'sum' )
		{
			aggregationOperatorText = i18n_sum;
		} else if ( aggregationOperator == 'average' )
		{
			aggregationOperatorText = i18n_average;
		}
		setInnerHTML( 'aggregationOperatorField', aggregationOperatorText );

		setInnerHTML( 'categoryComboField', json.dataElement.categoryCombo );

		var url = json.dataElement.url;
		setInnerHTML( 'urlField', url ? '<a href="' + url + '">' + url + '</a>' : '[' + i18n_none + ']' );

		var lastUpdated = json.dataElement.lastUpdated;
		setInnerHTML( 'lastUpdatedField', lastUpdated ? lastUpdated : '[' + i18n_none + ']' );

		var dataSets = joinNameableObjects( json.dataElement.dataSets );
		setInnerHTML( 'dataSetsField', dataSets ? dataSets : '[' + i18n_none + ']' );
	
		showDetails();
	});
}

// -----------------------------------------------------------------------------
// Remove data element
// -----------------------------------------------------------------------------

function removeDataElement( dataElementId, dataElementName )
{
    removeItem( dataElementId, dataElementName, i18n_confirm_delete, 'removeDataElement.action' );
}

