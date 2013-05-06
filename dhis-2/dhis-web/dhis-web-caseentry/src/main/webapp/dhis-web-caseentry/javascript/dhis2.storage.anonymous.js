/*
dhis2.storage.Store.plugin( 'online-anonymous-programs', (function () {
    return {
        call: function ( args, success, failure ) {
            return $.ajax( {
                type: 'POST',
                url: "anonymousPrograms.action",
                data: args,
                dataType: 'json'
            } ).success( function ( data ) {
                var arr = data.programs;

                for ( var i = 0; i < arr.length; i++ ) {
                    arr[i].key = arr[i].id;
                    arr[i].programStages = [];
                    arr[i].programStages[0] = {
                        id: arr[i].programStageId,
                        reportDateDescription: arr[i].reportDateDescription
                    };

                    delete arr[i].id;
                    delete arr[i].programStageId;
                    delete arr[i].reportDateDescription;
                    delete arr[i].type;
                }

                if ( success ) success( arr );
            } ).fail(failure);
        }
    };
})() );
*/
