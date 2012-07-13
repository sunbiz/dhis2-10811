jQuery.extend( {
	
	postJSON: function( url, data, success ) {
		$.ajax( { url:url, data:data, success:success, type:'post', dataType:'json', contentType:'application/x-www-form-urlencoded;charset=utf-8' } );
	},

	postUTF8: function( url, data, success ) {
		$.ajax( { url:url, data:data, success:success, type:'post', contentType:'application/x-www-form-urlencoded;charset=utf-8' } );
	},
	
	loadNoCache: function( elementId, url, data ) {
		$.ajax( { url:url, data:data, type:'get', dataType:'html', success:function( data ) {
			$( '#' + elementId ).html( data );
		} } );
	},
	
	toggleCss: function( elementId, property, value1, value2 ) {
		var id = '#' + elementId;
		var curValue = $( id ).css( property );
		if ( curValue == value1 ) {
			$( id ).css( property, value2 );
		}
		else {
			$( id ).css( property, value1 );
		} 
	}
} );