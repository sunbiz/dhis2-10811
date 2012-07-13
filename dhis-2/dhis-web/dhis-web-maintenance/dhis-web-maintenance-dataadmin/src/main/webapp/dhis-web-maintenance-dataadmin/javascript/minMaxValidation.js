function generateMinMaxValue(){
	
	var datasetIds = "";
	var datasetField = byId( 'dataSetIds' ); 
	for (var i = 0; i < datasetField.options.length; i++)
	{
		if (datasetField.options[ i ].selected)
		{
		  datasetIds+= "dataSets=" + datasetField.options[ i ].value + "&";
		}
	}
	
	$.ajax({
		   type: "POST",
		   url: "generateMinMaxValue.action",
		   data: datasetIds,
		   dataType: "xml",
		   success: function(xmlObject){
				xmlObject = xmlObject.getElementsByTagName( 'message' )[0];
				showSuccessMessage (xmlObject.firstChild.nodeValue);
		   }
		});
}

//-----------------------------------------------------------------------------------
// Organisation Tree
//-----------------------------------------------------------------------------------

function removeMinMaxValue(){

	var datasetIds = "";
	var datasetField = byId( 'dataSetIds' ); 
	for (var i = 0; i < datasetField.options.length; i++)
	{
		if (datasetField.options[ i ].selected)
		{
		  datasetIds+= "dataSets=" + datasetField.options[ i ].value + "&";
		}
	}
	
	$.ajax({
		   type: "POST",
		   url: "removeMinMaxValue.action",
		   data: datasetIds,
		   dataType: "xml",
		   success: function(xmlObject){
				xmlObject = xmlObject.getElementsByTagName( 'message' )[0];
				showSuccessMessage (xmlObject.firstChild.nodeValue);
		   }
		});
}