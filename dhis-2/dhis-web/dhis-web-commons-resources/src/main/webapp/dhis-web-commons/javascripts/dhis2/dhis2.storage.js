// big chunks of this is based on code from:
// http://brian.io/lawnchair

dhis2.util.namespace( 'dhis2.storage' );

dhis2.storage.Store = function ( options, callback ) {
    var Store = dhis2.storage.Store;

    this.name = options.name || 'records';
    this.dbname = options.dbname || 'dhis2';
    this.record = options.record || 'record';

    if ( arguments.length <= 2 && arguments.length > 0 ) {
        callback = (typeof arguments[0] === 'function') ? arguments[0] : arguments[1];
        options = (typeof arguments[0] === 'function') ? {} : arguments[0];
    } else {
        throw 'Incorrect # of ctor args!'
    }

    if ( !JSON ) throw 'JSON unavailable! Include http://www.json.org/json2.js to fix.';
    if ( typeof callback !== 'function' ) throw 'No callback was provided.';
    if ( Store.adapters.length == 0 ) throw 'No adapters was provided.';

    var adapter;

    if ( options.adapter ) {
        for ( var i = 0, l = Store.adapters.length; i < l; i++ ) {
            if ( options.adapter === Store.adapters[i].id ) {
                adapter = Store.adapters[i].valid() ? Store.adapters[i] : undefined;
                break;
            }
        }
    } else {
        for ( var i = 0, l = Store.adapters.length; i < l; i++ ) {
            adapter = Store.adapters[i].valid() ? Store.adapters[i] : undefined;
            if ( adapter ) break;
        }
    }

    if ( !adapter ) throw 'No valid adapter.';

    // mixin adapter functions
    for ( var i in adapter ) {
        if ( adapter.hasOwnProperty( i ) ) {
            this[i] = adapter[i];
        }
    }

    this.init( options, callback );
};

dhis2.storage.Store.adapters = [];

dhis2.storage.Store.adapter = function ( id, obj ) {
    var Store = dhis2.storage.Store;
    var adapter_interface = "init add addAll remove exists fetch fetchAll destroy".split( ' ' );

    var missing_functions = [];
    // verify adapter
    for ( var i in adapter_interface ) {
        if ( !obj.hasOwnProperty( adapter_interface[i] ) || typeof obj[adapter_interface[i]] !== 'function' ) {
            missing_functions.push( adapter_interface[i] );
        }
    }

    if ( missing_functions.length > 0 ) {
        throw 'Adapter \'' + id + '\' does not meet interface requirements, missing: ' + missing_functions.join( ' ' );
    }

    obj['id'] = id;
    Store.adapters.splice( 0, 0, obj );
};

dhis2.storage.Store.plugins = {};

dhis2.storage.Store.plugin = function ( id, obj ) {
    var Store = dhis2.storage.Store;
    var plugin_interface = "call".split( ' ' );

    var missing_functions = [];
    // verify plugin
    for ( var i in plugin_interface ) {
        if ( !obj.hasOwnProperty( plugin_interface[i] ) || typeof obj[plugin_interface[i]] !== 'function' ) {
            missing_functions.push( plugin_interface[i] );
        }
    }

    if ( missing_functions.length > 0 ) {
        throw 'Plugin\'' + id + '\' does not meet interface requirements, missing: ' + missing_functions.join( ' ' );
    }

    obj['id'] = id;
    Store.plugins[id] = obj;
};
