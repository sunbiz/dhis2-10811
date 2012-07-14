
var selected = false;

function generateResourceTable()
{
    var organisationUnit = document.getElementById( "organisationUnit" ).checked;
    var dataElementGroupSetStructure = document.getElementById( "dataElementGroupSetStructure" ).checked;
    var indicatorGroupSetStructure = document.getElementById( "indicatorGroupSetStructure" ).checked;
    var organisationUnitGroupSetStructure = document.getElementById( "organisationUnitGroupSetStructure" ).checked;
    var categoryStructure = document.getElementById( "categoryStructure" ).checked;
    var categoryOptionComboName = document.getElementById( "categoryOptionComboName" ).checked;
    var dataElementStructure = document.getElementById( "dataElementStructure" ).checked;
    var periodStructure = document.getElementById( "periodStructure" ).checked;
    
    if ( organisationUnit || dataElementGroupSetStructure || indicatorGroupSetStructure || organisationUnitGroupSetStructure || 
    		categoryStructure || categoryOptionComboName || dataElementStructure || periodStructure )
    {
        setWaitMessage( i18n_generating_resource_tables );
            
        var params = {
        	organisationUnit: organisationUnit,
            dataElementGroupSetStructure: dataElementGroupSetStructure,
            indicatorGroupSetStructure: indicatorGroupSetStructure,
            organisationUnitGroupSetStructure: organisationUnitGroupSetStructure,
            categoryStructure: categoryStructure,
            categoryOptionComboName: categoryOptionComboName,
            dataElementStructure: dataElementStructure,
            periodStructure: periodStructure
        };
            
		$.ajax({
			   type: "post",
			   url: "generateResourceTable.action",
			   data: params,
			   dataType: "xml",
			   success: function(result){
					setMessage( i18n_resource_tables_generated );
			   }
			});
    }
    else
    {
        setMessage( i18n_select_options );
    }
}

function toggleAll()
{	
	selected = !selected;
	
	document.getElementById( "organisationUnit" ).checked = selected;
	document.getElementById( "dataElementGroupSetStructure" ).checked = selected;
	document.getElementById( "indicatorGroupSetStructure" ).checked = selected;
	document.getElementById( "organisationUnitGroupSetStructure" ).checked = selected;
	document.getElementById( "categoryStructure" ).checked = selected;
	document.getElementById( "categoryOptionComboName" ).checked = selected;
	document.getElementById( "dataElementStructure" ).checked = selected;	
	document.getElementById( "periodStructure" ).checked = selected;	
}
