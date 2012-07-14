
// -----------------------------------------------------------------------------
// Page init
// -----------------------------------------------------------------------------

$( document ).ready( function() { pageInit(); } );

function pageInit()
{
	setTableStyles();
	
    // Hover on rightbar close image
    
    $( "#hideRightBarImg" ).mouseover( function()
    {
    	$( this ).attr( "src", "../images/hide_active.png" );
    } );
    $( "#hideRightBarImg" ).mouseout( function()
    {
        $( this ).attr( "src", "../images/hide.png" );
    } );

	// Set links on top menu items
    
    $( "#menuLink1" ).click( function() {
    	window.location.href = "../dhis-web-commons-about/modules.action";
    } );

    $( "#menuLink2" ).click( function() {
    	window.location.href = "../dhis-web-commons-about/modules.action";
    } );

    $( "#menuLink3" ).click( function() {
    	window.location.href = "../dhis-web-commons-about/help.action";
    } );

    $( "#menuLink4" ).click( function() {
    	window.location.href = "../dhis-web-dashboard-integration/profile.action";
    } );
	
	// Set show and hide drop down events on top menu
	
	$( "#menuLink1" ).hover( function() 
	{
		showDropDown( "menuDropDown1" );
	}, 
	function() 
	{
		hideDropDownTimeout();
	} );

	$( "#menuLink2" ).hover( function() 
	{
		showDropDown( "menuDropDown2" );
	}, 
	function() 
	{
		hideDropDownTimeout();
	} );

	$( "#menuLink3" ).hover( function() 
	{
		showDropDown( "menuDropDown3" );
	}, 
	function() 
	{
		hideDropDownTimeout();
	} );

	$( "#menuLink4" ).hover( function() 
	{
		showDropDown( "menuDropDown4" );
	}, 
	function() 
	{
		hideDropDownTimeout();
	} );

	$( "#menuDropDown1, #menuDropDown2, #menuDropDown3, #menuDropDown4" ).hover( function() 
	{
		cancelHideDropDownTimeout();
	}, 
	function() 
	{
		hideDropDownTimeout();
	} );
	
	// Intro fade in
	
	$( ".introList" ).fadeIn();
}

function setTableStyles()
{
	// Zebra stripes in lists
	
	$( "table.listTable tbody tr:odd" ).addClass( "listAlternateRow" );
    $( "table.listTable tbody tr:even" ).addClass( "listRow" );

    // Hover rows in lists
    
    $( "table.listTable tbody tr" ).mouseover( function()
    {
    	$( this ).addClass( "listHoverRow" );
    } );
    $( "table.listTable tbody tr" ).mouseout( function()
    {
        $( this ).removeClass( "listHoverRow" );
    } );
}

// -----------------------------------------------------------------------------
// Menu functions
// -----------------------------------------------------------------------------

var menuTimeout = 500;
var closeTimer = null;
var dropDownId = null;

function showDropDown( id )
{
    cancelHideDropDownTimeout();
    
    var newDropDownId = "#" + id;
  
    if ( dropDownId != newDropDownId )
    {   
        hideDropDown();

        dropDownId = newDropDownId;
        
        $( dropDownId ).show();
    }
}

function hideDropDown()
{
	if ( dropDownId )
	{
	    $( dropDownId ).hide();
	    
	    dropDownId = null;
	}
}

function hideDropDownTimeout()
{
    closeTimer = window.setTimeout( "hideDropDown()", menuTimeout );
}

function cancelHideDropDownTimeout()
{
    if ( closeTimer )
    {
        window.clearTimeout( closeTimer );
        
        closeTimer = null;
    }
}

// -----------------------------------------------------------------------------
// Leftbar
// -----------------------------------------------------------------------------

var leftBar = new LeftBar();

function LeftBar()
{    
    this.showAnimated = function()
    {
        setMenuVisible();
        $( '#mainPage' ).removeAttr( 'style' );
        $( '#leftBar' ).show( 'fast' );
        $( '#showLeftBar' ).hide( 'fast' );
    };
    
    this.hideAnimated = function()
    {
        setMenuHidden();
        $( '#mainPage' ).attr( 'style', 'margin-left:20px' );
        $( '#leftBar' ).hide( 'fast' );
        $( '#showLeftBar' ).show( 'fast' );
    };
    
    this.hide = function()
    {
        setMenuHidden();
        $( '#mainPage' ).attr( 'style', 'margin-left:20px' );
        document.getElementById( 'leftBar' ).style.display = 'none';
        document.getElementById( 'showLeftBar' ).style.display = 'block';
    };

    function setMenuVisible()
    {
        $.get( '../dhis-web-commons/menu/setMenuVisible.action' );        
    }
    
    function setMenuHidden()
    {        
        $.get( '../dhis-web-commons/menu/setMenuHidden.action' );        
    }    
    
    this.openHelpForm = function( id )
    {
		window.open( "../dhis-web-commons/help/viewDynamicHelp.action?id=" + id, "Help", "width=800,height=600,scrollbars=yes" );
    }
}
