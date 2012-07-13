// -----------------------------------------------------------------------------
// Author:   Torgeir Lorange Ostby
// -----------------------------------------------------------------------------

/*
 * Usage:
 *
 * Use the selection.setListenerFunction function to register a callback
 * function to be called when an organisation unit is selected. The callback
 * function must have one argument, an array with the ids of the selected
 * organisation units.
 *
 * Multiple selection is by default turned off. Use the
 * selection.setMultipleSelectionAllowed function to change this.
 */

var organisationUnitTreePath = '../dhis-web-commons/ouwt/';
var organisationUnits = {};

var selection = new Selection();
var subtree = new Subtree();

$( document ).ready( function ()
{
    selection.load();
} );

// -----------------------------------------------------------------------------
// Selection
// -----------------------------------------------------------------------------

function Selection()
{
    var listenerFunction = undefined;
    var multipleSelectionAllowed = false;
    var unselectAllowed = false;
    var rootUnselectAllowed = false;
    var autoSelectRoot = true;

    this.setListenerFunction = function ( listenerFunction_, skipInitialCall )
    {
        listenerFunction = listenerFunction_;

        if ( !skipInitialCall )
        {
            $( "#orgUnitTree" ).one( "ouwtLoaded", function ()
            {
                selection.responseReceived();
            } );
        }
    };

    this.setMultipleSelectionAllowed = function ( allowed )
    {
        multipleSelectionAllowed = allowed;
    };

    this.setUnselectAllowed = function ( allowed )
    {
        unselectAllowed = allowed;
    };

    this.setRootUnselectAllowed = function ( allowed )
    {
        rootUnselectAllowed = allowed;
    };

    this.setAutoSelectRoot = function ( autoSelect )
    {
        autoSelectRoot = autoSelect;
    };

    this.load = function ()
    {
        function sync_and_reload()
        {
            var roots = JSON.parse( localStorage[getTagId( "Roots" )] );

            if ( sessionStorage[getTagId( "Selected" )] == null && roots.length > 0 )
            {
                if ( autoSelectRoot )
                {
                    if ( multipleSelectionAllowed )
                    {
                        sessionStorage[getTagId( "Selected" )] = roots;
                    }
                    else
                    {
                        sessionStorage[getTagId( "Selected" )] = roots[0];
                    }
                }
                else
                {
                    selection.sync( true );
                }
            }

            organisationUnits = JSON.parse( localStorage["organisationUnits"] );

            if(sessionStorage["organisationUnits"] !== undefined)
            {
                $.extend(organisationUnits, JSON.parse( sessionStorage["organisationUnits"] ))
            }

            selection.sync();
            subtree.reloadTree();

            $( "#ouwt_loader" ).hide();
        }

        function update_required( remoteVersion, remoteRoots )
        {
            var localVersion = localStorage[getTagId( "Version" )] ? localStorage[getTagId( "Version" )] : 0;
            var localRoots = localStorage[getTagId( "Roots" )] ? JSON.parse( localStorage[getTagId( "Roots" )] ) : [];

            if ( localVersion != remoteVersion )
            {
                return true;
            }

            if(localRoots == null || localRoots.length == 0)
            {
                return true;
            }

            localRoots.sort();
            remoteRoots.sort();

            for ( var i in localRoots )
            {
                if ( remoteRoots[i] == null || localRoots[i] != remoteRoots[i] )
                {
                    return true;
                }
            }

            return false;
        }

        var should_update = false;

        $.post( '../dhis-web-commons-ajax-json/getOrganisationUnitTree.action', {
                "versionOnly":true
            },
            function ( data, textStatus, jqXHR )
            {
                if ( data.indexOf( "<!DOCTYPE" ) != 0 )
                {
                    data = JSON.parse( data );
                    should_update = update_required( data.version, data.roots );
                }
            }, "text" ).complete(
            function ()
            {
                if ( should_update )
                {
                    $.post( '../dhis-web-commons-ajax-json/getOrganisationUnitTree.action',
                        function ( data, textStatus, jqXHR )
                        {
                            localStorage[getTagId( "Roots" )] = JSON.stringify( data.roots );
                            localStorage[getTagId( "Version" )] = data.version;
                            localStorage["organisationUnits"] = JSON.stringify( data.organisationUnits );
                        } ).complete( function ()
                        {
                            sync_and_reload();
                            $( "#orgUnitTree" ).trigger( "ouwtLoaded" );
                        } );
                }
                else
                {
                    sync_and_reload();
                    $( "#orgUnitTree" ).trigger( "ouwtLoaded" );
                }
            } );
    };

    // server = true : sync from server
    // server = false : sync to server
    this.sync = function ( server, fn )
    {
        if ( fn === undefined )
        {
            fn = function ()
            {
            };
        }

        if ( server )
        {
            sessionStorage.removeItem( getTagId( "Selected" ) );

            $.post( organisationUnitTreePath + "getselected.action", function ( data )
            {
                if ( data["selectedUnits"].length < 1 )
                {
                    return;
                }

                if ( multipleSelectionAllowed )
                {
                    var selected = [];
                    $.each( data["selectedUnits"], function ( i, item )
                    {
                        selected.push( item.id );
                    } );

                    sessionStorage[getTagId( "Selected" )] = JSON.stringify( selected );
                }
                else
                {
                    var ou = data["selectedUnits"][0];
                    sessionStorage[getTagId( "Selected" )] = ou.id;
                }

                subtree.reloadTree();
            } );
        }
        else
        {
            $.post( organisationUnitTreePath + "clearselected.action", function ()
            {
                if ( sessionStorage[getTagId( "Selected" )] == null )
                {
                    return;
                }

                var selected = sessionStorage[getTagId( "Selected" )];

                if ( selected != null )
                {
                    selected = JSON.parse( selected );

                    if ( multipleSelectionAllowed )
                    {
                        if ( !$.isArray( selected ) )
                        {
                            selected = [ selected ];
                        }

                        $.each( selected,
                            function ( i, item )
                            {
                                $.post( organisationUnitTreePath + "addorgunit.action", {
                                    id:item
                                } );
                            } ).complete( fn );
                    }
                    else
                    {
                        if ( $.isArray( selected ) )
                        {
                            selected = selected[0];
                        }

                        $.post( organisationUnitTreePath + "setorgunit.action", {
                            id:selected
                        } ).complete( fn );
                    }
                }
            } );
        }
    };

    this.clear = function ()
    {
        sessionStorage.removeItem( getTagId( "Selected" ) );

        var roots = JSON.parse( localStorage[getTagId( "Roots" )] );

        if ( roots.length > 1 )
        {
            sessionStorage[getTagId( "Selected" )] = roots;
        }
        else
        {
            sessionStorage[getTagId( "Selected" )] = roots[0];
        }

        subtree.reloadTree();

        $.post( organisationUnitTreePath + "clearselected.action" ).complete( this.responseReceived );
    };

    this.select = function ( unitId )
    {
        var $linkTag = $( "#" + getTagId( unitId ) ).find( "a" ).eq( 0 );

        if ( $linkTag.hasClass( "selected" ) && ( unselectAllowed || rootUnselectAllowed ) )
        {
            var selected = JSON.parse( sessionStorage[getTagId( "Selected" )] );

            if ( rootUnselectAllowed && !unselectAllowed && !multipleSelectionAllowed )
            {
                var roots = JSON.parse( localStorage[getTagId( "Roots" )] );

                if ( $.inArray(selected, roots) == -1 )
                {
                    return;
                }
            }

            if ( !!selected && $.isArray( selected ) )
            {
                var idx = undefined;

                $.each( selected, function ( i, item )
                {
                    if ( +item === unitId )
                    {
                        idx = i;
                    }
                } );

                if ( idx !== undefined )
                {
                    dhis2.array.remove( selected, idx, idx );
                }

                sessionStorage[getTagId( "Selected" )] = JSON.stringify( selected );
            }
            else
            {
                sessionStorage.removeItem( getTagId( "Selected" ) );
            }

            $.post( organisationUnitTreePath + "removeorgunit.action", {
                id:unitId
            } ).complete( this.responseReceived );

            $linkTag.removeClass( "selected" );
        }
        else
        {
            if ( multipleSelectionAllowed )
            {
                var selected = JSON.parse( sessionStorage[getTagId( "Selected" )] );

                if ( selected )
                {
                    selected = JSON.parse( selected );

                    if ( !$.isArray( selected ) )
                    {
                        selected = [ selected ];
                    }
                }
                else
                {
                    selected = [];
                }

                if ( selected.indexOf( unitId ) !== -1 )
                {
                    return;
                }

                selected.push( unitId );
                sessionStorage[getTagId( "Selected" )] = JSON.stringify( selected );

                $.post( organisationUnitTreePath + "addorgunit.action", {
                    id:unitId
                } ).complete( this.responseReceived );

                $linkTag.addClass( "selected" );
            }
            else
            {
                sessionStorage[getTagId( "Selected" )] = unitId;

                $.ajax( {
                    url:organisationUnitTreePath + "setorgunit.action",
                    data:{
                        id:unitId
                    },
                    type:'POST',
                    timeout:10000,
                    complete:this.responseReceived
                } );

                $( "#orgUnitTree" ).find( "a" ).removeClass( "selected" );
                $linkTag.addClass( "selected" );
            }
        }
    };

    this.responseReceived = function ()
    {
        if ( !listenerFunction )
        {
            return;
        }

        var selected = [];

        if ( sessionStorage[getTagId( "Selected" )] != null )
        {
            selected = JSON.parse( sessionStorage[getTagId( "Selected" )] );
        }

        var ids = [];
        var names = [];

        if ( $.isArray( selected ) )
        {
            $.each( selected, function ( i, item )
            {
                var name = organisationUnits[item].n;
                ids.push( item );
                names.push( name );
            } );
        }
        else
        {
            var name = organisationUnits[selected].n;
            ids.push( +selected );
            names.push( name );
        }

        listenerFunction( ids, names );
    };

    function getTagId( unitId )
    {
        return 'orgUnit' + unitId;
    }

    this.findByCode = function ()
    {
        var name = $( '#searchField' ).val();

        var match = undefined;

        for ( var ou in organisationUnits )
        {
            var value = organisationUnits[ou];

            if ( value.n == name )
            {
                match = value;
            }
        }

        if ( match !== undefined )
        {
            $( '#searchField' ).css( 'background-color', '#ffffff' );

            if ( multipleSelectionAllowed )
            {
                sessionStorage[getTagId( "Selected" )] = [ match.id ];
            }
            else
            {
                sessionStorage[getTagId( "Selected" )] = match.id;
            }

            subtree.reloadTree();
            selection.sync( false, selection.responseReceived );
            // selection.responseReceived();
        }
        else
        {
            $( '#searchField' ).css( 'background-color', '#ffc5c5' );
        }
    };

    this.enable = function ()
    {
        $( "#orgUnitTree" ).show();
    };

    this.unblock = function ()
    {
        $( "#orgUnitTree" ).unblock();
    };

    this.disable = function ( message )
    {
        $( "#orgUnitTree" ).hide();
    };

    this.block = function ( message )
    {
        if ( message === undefined )
        {
            $( "#orgUnitTree" ).block( {message:null} );
        }
        else
        {
            $( "#orgUnitTree" ).block( {
                message:message,
                css:{
                    border:'1px solid black',
                    margin:'4px 10px'
                }
            } );
        }
    };
}

// -----------------------------------------------------------------------------
// Subtree
// -----------------------------------------------------------------------------

function Subtree()
{
    this.toggle = function ( unitId )
    {
        var children = $( "#" + getTagId( unitId ) ).find( "ul" );
        var ou = organisationUnits[unitId];

        if ( children.length < 1 || !isVisible( children[0] ) )
        {
            processExpand( ou );
        }
        else
        {
            processCollapse( ou );
        }
    };

    var selectOrgUnits = function ( ous )
    {
        $.each( ous, function ( i, item )
        {
            selectOrgUnit( item );
        } );
    };

    var selectOrgUnit = function ( ou )
    {
        $( "#" + getTagId( ou ) + " > a" ).addClass( "selected" );
    };

    var expandTreeAtOrgUnits = function ( ous )
    {
        $.each( ous, function ( i, item )
        {
            expandTreeAtOrgUnit( item );
        } );
    };

    var expandTreeAtOrgUnit = function ( ou )
    {
        if ( organisationUnits[ou] == null )
        {
            return;
        }

        var ouEl = organisationUnits[ou];

        var $rootsTag = $( "#orgUnitTree > ul" );

        if ( $rootsTag.length < 1 )
        {
            $( "#orgUnitTree" ).append( "<ul/>" );
            $rootsTag = $( "#orgUnitTree > ul" );
        }

        var array = [];

        if ( ouEl.pid !== undefined )
        {
            while ( ouEl.pid !== undefined )
            {
                if ( organisationUnits[ouEl.pid] != null )
                {
                    array.push( ouEl.pid );
                }
                else
                {
                    break;
                }

                ouEl = organisationUnits[ouEl.pid];
            }

            array.reverse();
        }

        var rootId = array.length < 1 ? ou : array[0];

        if ( $( "#" + getTagId( rootId ) ).length < 1 )
        {
            var expand = organisationUnits[rootId];
            // var $parentTag = $( "#" + getTagId( rootId ) );
            $rootsTag.append( createTreeElementTag( expand ) );
        }

        $.each( array, function ( i, item )
        {
            var expand = organisationUnits[item];
            processExpand( expand );
        } );
    };

    this.reloadTree = function ()
    {
        var $treeTag = $( "#orgUnitTree" );
        $treeTag.children().eq( 0 ).remove();

        var roots = localStorage[getTagId( "Roots" )];
        var selected = sessionStorage[getTagId( "Selected" )];

        roots = roots ? JSON.parse( roots ) : [];
        selected = selected ? JSON.parse( selected ) : [];
        selected = $.isArray( selected ) ? selected : [ selected ];

        expandTreeAtOrgUnits( roots );
        expandTreeAtOrgUnits( selected );

        selectOrgUnits( selected );
    };

    // force reload
    this.refreshTree = function ()
    {
        localStorage.removeItem( getTagId( "Version" ) );
        selection.load();
    };

    function processCollapse( parent )
    {
        var $parentTag = $( "#" + getTagId( parent.id ) );
        var child = $parentTag.find( "ul" ).eq( 0 );
        setVisible( child, false );
        setToggle( $parentTag, false );
    }

    function processExpand( parent )
    {
        var $parentTag = $( "#" + getTagId( parent.id ) );
        var $children = $parentTag.find( "ul" );

        if ( $children.length < 1 )
        {
            getAndCreateChildren( $parentTag, parent );
        }
        else
        {
            setVisible( $children.eq( 0 ), true );
            setToggle( $parentTag, true );
        }
    }

    function getAndCreateChildren(parentTag, parent)
    {
        if(parent.c !== undefined)
        {
            if(organisationUnits[parent.c[0]] !== undefined)
            {
                createChildren( parentTag, parent );
            }
            else
            {
                $.post( '../dhis-web-commons-ajax-json/getOrganisationUnitTree.action?parentId=' + parent.id,
                function ( data, textStatus, jqXHR )
                    {
                        // load additional organisationUnits into sessionStorage
                        if(sessionStorage["organisationUnits"] === undefined)
                        {
                            sessionStorage["organisationUnits"] = JSON.stringify( data.organisationUnits );
                        } else {
                            units = JSON.parse( sessionStorage["organisationUnits"] );
                            $.extend(units, data.organisationUnits);
                            sessionStorage["organisationUnits"] = JSON.stringify( units );
                        }

                        $.extend(organisationUnits, data.organisationUnits);
                        createChildren( parentTag, parent );
                    }
                );
            }
        }

    }

    function createChildren( parentTag, parent )
    {
        var $childrenTag = $( "<ul/>" );

        $.each( parent.c, function ( i, item )
        {
            var ou = organisationUnits[item];

            if(ou !== undefined)
            {
                $childrenTag.append( createTreeElementTag( ou ) );
            }
        } );

        setVisible( $childrenTag, true );
        setToggle( parentTag, true );

        $( parentTag ).append( $childrenTag );
    }

    function createTreeElementTag( ou )
    {
        var $toggleTag = $( "<span/>" );
        $toggleTag.addClass( "toggle" );

        if ( ou.c.length > 0 )
        {
            $toggleTag.bind( "click", new Function( 'subtree.toggle( ' + ou.id + ' )' ) );
            $toggleTag.append( getToggleExpand() );
        }
        else
        {
            $toggleTag.append( getToggleBlank() );
        }

        var $linkTag = $( "<a/>" );
        $linkTag.attr( "href", "javascript:void selection.select( " + ou.id + ")" );
        $linkTag.append( ou.n );

        var $childTag = $( "<li/>" );

        $childTag.attr( "id", getTagId( ou.id ) );
        $childTag.append( " " );
        $childTag.append( $toggleTag );
        $childTag.append( " " );
        $childTag.append( $linkTag );

        return $childTag;
    }

    function setToggle( unitTag, expanded )
    {
        var $toggleTag = $( unitTag ).find( "span" );
        var toggleImg = expanded ? getToggleCollapse() : getToggleExpand();

        if ( $toggleTag.children().eq( 0 ) )
        {
            $toggleTag.children().eq( 0 ).replaceWith( toggleImg );
        }
        else
        {
            $toggleTag.append( toggleImg );
        }
    }

    function setVisible( tag, visible )
    {
        if ( visible )
        {
            $( tag ).show();
        }
        else
        {
            $( tag ).hide();
        }
    }

    function isVisible( tag )
    {
        return $( tag ).is( ":visible" );
    }

    function getTagId( unitId )
    {
        return 'orgUnit' + unitId;
    }

    function getToggleExpand()
    {
        return getToggleImage().attr( "src", "../images/colapse.png" ).attr( "alt", "[+]" );
    }

    function getToggleCollapse()
    {
        return getToggleImage().attr( "src", "../images/expand.png" ).attr( "alt", "[-]" );
    }

    function getToggleBlank()
    {
        return getToggleImage().attr( "src", "../images/transparent.gif" ).removeAttr( "alt" );
    }

    function getToggleImage()
    {
        return $( "<img/>" ).attr( "width", 9 ).attr( "height", 9 );
    }
}
