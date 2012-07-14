// web storage support (indexedDb)
dhis2.storage.Store.adapter( 'indexed-db', (function () {
    window.indexedDB = window.indexedDB || window.webkitIndexedDB || window.mozIndexedDB || window.oIndexedDB || window.msIndexedDB;
    window.IDBTransaction = window.IDBTransaction || window.webkitIDBTransaction || window.msIDBTransaction;
    window.IDBKeyRange = window.IDBKeyRange || window.webkitIDBKeyRange || window.msIDBKeyRange;

    var IDB = IDB || {};
    IDB.TransactionModes = {
        READ_ONLY: 'readonly',
        READ_WRITE: 'readwrite'
    };

    function getTransaction( mode ) {
        return IDB.db.transaction( [ IDB.options.name ], mode );
    }

    function getReadOnlyObjectStore() {
        return getTransaction( IDB.TransactionModes.READ_ONLY ).objectStore( IDB.options.name );
    }

    function getReadWriteObjectStore() {
        return getTransaction( IDB.TransactionModes.READ_WRITE ).objectStore( IDB.options.name );
    }

    function defaultErrorHandler() {
        console.log( "error:", e );
    }

    function defaultBlockingHandler() {
        console.log( "blocked:", e );
    }

    return {
        valid: function () {
            return false;
            // return !!(window.indexedDB && window.IDBTransaction && window.IDBKeyRange);
        },

        init: function ( options, callback ) {
            var that = this;
            IDB.options = options;
            var request = window.indexedDB.open( this.dbname, "1" );

            request.onupgradeneeded = function ( e ) {
                IDB.db = e.target.result;

                if ( IDB.db.objectStoreNames.contains( that.name ) ) {
                    IDB.db.deleteObjectStore( that.name );
                }

                IDB.db.createObjectStore( that.name );
            };

            request.onsuccess = function ( e ) {
                IDB.db = e.target.result;
                if ( callback ) callback.call( that, that );
            };

            request.onerror = defaultErrorHandler;
            request.onblocked = defaultBlockingHandler;
        },

        add: function ( key, obj, callback ) {
            var that = this;
            var request = getReadWriteObjectStore().put( obj, key );

            request.onsuccess = function ( e ) {
                if ( callback ) callback.call( that, that, obj );
            };

            request.onerror = defaultErrorHandler;
            request.onblocked = defaultBlockingHandler;
        },

        addAll: function ( keys, objs, callback ) {
            var that = this;

            if ( keys.length == 0 || objs.length == 0 ) {
                if ( callback ) callback.call( that, that );
                return;
            }

            var key = keys.pop();
            var obj = objs.pop();

            this.add( key, obj, function ( store ) {
                that.addAll( keys, objs, callback );
            } );
        },

        remove: function ( key, callback ) {
            var that = this;
            var request = getReadWriteObjectStore().delete( key );

            request.onsuccess = function ( e ) {
                if ( callback ) callback.call( that, that );
            };

            request.onerror = defaultErrorHandler;
            request.onblocked = defaultBlockingHandler;
        },

        exists: function ( key, callback ) {
            var that = this;
            var request = getReadOnlyObjectStore().get( key );

            request.onsuccess = function ( e ) {
                if ( callback ) callback.call( that, that, e.target.result != null );
            };

            request.onerror = defaultErrorHandler;
            request.onblocked = defaultBlockingHandler;
        },

        fetch: function ( key, callback ) {
            var that = this;
            var request = getReadOnlyObjectStore().get( key );

            request.onsuccess = function ( e ) {
                if ( callback ) callback.call( that, that, e.target.result );
            };

            request.onerror = defaultErrorHandler;
            request.onblocked = defaultBlockingHandler;
        },

        fetchAll: function ( callback ) {
            var that = this;
            var records = [];
            var request = getReadOnlyObjectStore().openCursor();

            request.onsuccess = function ( e ) {
                var cursor = e.target.result;

                if ( cursor ) {
                    records.push( cursor.value );
                    cursor.continue();
                } else {
                    if ( callback ) callback.call( that, that, records );
                }
            };

            request.onerror = defaultErrorHandler;
            request.onblocked = defaultBlockingHandler;
        },

        destroy: function ( callback ) {
            var that = this;
            IDB.db.close();
            var request = window.indexedDB.deleteDatabase( IDB.options.dbname );

            request.onsuccess = function ( e ) {
                if ( callback ) callback.call( that, that );
            }

            request.onerror = defaultErrorHandler;
            request.onblocked = defaultBlockingHandler;
        }
    };
})() );
