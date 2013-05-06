TR.conf = {
    init: {
		ajax: {
			jsonfy: function(r) {
				r = Ext.JSON.decode(r.responseText);
				var obj = { 
					system: {
						maxLevels: r.levels.length
					}
				};
				obj.system.rootnodes = [];
				for (var i = 0; i < r.user.ous.length; i++) {
					obj.system.rootnodes.push({id: r.user.ous[i].id, localid: r.user.ous[i].localid,text: r.user.ous[i].name, leaf: r.user.ous[i].leaf});
				}
				
				obj.system.program = [];
				for (var i = 0; i < r.programs.length; i++) {
					obj.system.program.push({id: r.programs[i].id, name: r.programs[i].name, type: r.programs[i].type });
				}
				
				obj.system.orgunitGroup = [];
				for (var i = 0; i < r.orgunitGroups.length; i++) {
					obj.system.orgunitGroup.push({id: r.orgunitGroups[i].id, name: r.orgunitGroups[i].name });
				}
				
				obj.system.level = [];
				for (var i = 0; i < r.levels.length; i++) {
					obj.system.level.push({value: r.levels[i].value, name: r.levels[i].name});
				}
				
				return obj;
			}
		}
    },
    finals: {
        ajax: {
			path_lib: '../../dhis-web-commons/javascripts/',
            path_root: '../',
            path_commons: '../',
            path_api: '../../api/',
            path_images: 'images/',
			initialize: 'tabularInitialize.action',
			patientproperties_get: 'loadPatientProperties.action',
			programstages_get: 'loadReportProgramStages.action',
			dataelements_get: 'loadDataElements.action',
			organisationunitchildren_get: 'getOrganisationUnitChildren.action',
			organisationunit_getbygroup: 'getOrganisationUnitPathsByGroup.action',
			generatetabularreport_get: 'generateTabularReport.action',
			casebasedfavorite_getall: 'getTabularReports.action',
			casebasedfavorite_get: 'getTabularReport.action',
			casebasedfavorite_rename: 'updateTabularReportName.action',
			casebasedfavorite_save: 'saveTabularReport.action',
            casebasedfavorite_delete: 'deleteTabularReport.action',
			suggested_dataelement_get: 'getOptions.action',
			aggregatefavorite_getall: 'getAggregateReportList.action',
			aggregatefavorite_get: 'getAggregateReport.action',
			aggregatefavorite_rename: 'updateAggregateReportName.action',
			aggregatefavorite_save: 'saveAggregateReport.action',
            aggregatefavorite_delete: 'deleteAggregateReport.action',
			generateaggregatereport_get: 'generateAggregateReport.action',
			username_dataelement_get: 'getUsernameList.action',
			organisationunit_getbyids: 'getOrganisationUnitPaths.action',
			redirect: 'index.action'
        },
        params: {
            data: {
                value: 'data',
                rawvalue: TR.i18n.regular_program,
                warning: {
					filter: TR.i18n.wm_multiple_filter_ind_de
				}
            },
            program: {
                value: 'program',
                rawvalue: TR.i18n.program
            },
            organisationunit: {
                value: 'organisationunit',
                rawvalue: TR.i18n.organisation_unit,
                warning: {
					filter: TR.i18n.wm_multiple_filter_orgunit
				}
            },
            patientProperties: {
                value: 'patientProperties',
                rawvalue: TR.i18n.patientProperties
            },
            programStage: {
                value: 'programStage',
                rawvalue: TR.i18n.program_stage
            },
            dataelement: {
                value: 'dataelement',
                rawvalue: TR.i18n.data_elements
            }
        },
        data: {
			domain: 'domain_',
		},
		root: {
			id: 'root'
		},
		download: {
            xls: 'xls',
			pdf: 'pdf',
			csv: 'csv'
        },
        cmd: {
            init: 'init_',
            none: 'none_',
			urlparam: 'id'
        }
    },
	period: {
		relativeperiodunits: {
			reportingMonth: 1,
			last3Months: 3,
			last12Months: 12,
			reportingQuarter: 1,
			last4Quarters: 4,
			lastSixMonth: 1,
			last2SixMonths: 2,
			thisYear: 1,
			lastYear: 1,
			last5Years: 5
		},
		periodtypes: [
			{id: 'Daily', name: 'Daily'},
			{id: 'Weekly', name: 'Weekly'},
			{id: 'Monthly', name: 'Monthly'},
			{id: 'BiMonthly', name: 'BiMonthly'},
			{id: 'Quarterly', name: 'Quarterly'},
			{id: 'SixMonthly', name: 'SixMonthly'},
			{id: 'Yearly', name: 'Yearly'},
			{id: 'FinancialOct', name: 'FinancialOct'},
			{id: 'FinancialJuly', name: 'FinancialJuly'},
			{id: 'FinancialApril', name: 'FinancialApril'}
		]
	},
	reportPosition: {
		POSITION_ROW_ORGUNIT_COLUMN_PERIOD: 1,
		POSITION_ROW_PERIOD_COLUMN_ORGUNIT: 2,
		POSITION_ROW_ORGUNIT_ROW_PERIOD: 3,
		POSITION_ROW_PERIOD: 4,
		POSITION_ROW_ORGUNIT: 5,
		POSITION_ROW_PERIOD_COLUMN_DATA: 6,
		POSITION_ROW_ORGUNIT_COLUMN_DATA: 7,
		POSITION_ROW_DATA: 8,
		POSITION_ROW_DATA_COLUMN_PERIOD: 9,
		POSITION_ROW_DATA_COLUMN_ORGUNIT: 10
	},
    statusbar: {
		icon: {
			error: 'error_s.png',
			warning: 'warning.png',
			ok: 'ok.png'
		}
	},
    layout: {
        west_width: 424,
        west_fieldset_width: 402,
		west_multiselect: 100,
        west_width_subtractor: 18,
        west_fill: 117,
        west_fill_accordion_organisationunit: 43,
        west_maxheight_accordion_organisationunit: 225,
        center_tbar_height: 31,
        east_gridcolumn_height: 30,
        form_label_width: 90,
		grid_row_height: 27,
		grid_favorite_width: 450,
		grid_favorite_height: 500,
        window_favorite_ypos: 100,
        window_confirm_width: 250,
		window_record_width: 450,
		window_record_height: 300,
		west_dataelements_multiselect: 120,
		west_dataelements_filter_panel: 135,
		west_dataelements_expand_filter_panel: 280,
		west_dataelements_collapse_filter_panel: 130,
		west_dataelements_expand_aggregate_filter_panel: 230,
		west_dataelements_collapse_aggregate_filter_panel: 80,
		west_properties_multiselect: 150,
		west_properties_filter_panel: 130,
		west_properties_expand_filter_panel: 280,
		west_properties_collapse_filter_panel: 135
    },
	util: {
		jsonEncodeString: function(str) {
			return typeof str === 'string' ? str.replace(/[^a-zA-Z 0-9(){}<>_!+;:?*&%#-]+/g,'') : str;
		},
		jsonEncodeArray: function(a) {
			for (var i = 0; i < a.length; i++) {
				a[i] = TR.conf.util.jsonEncodeString(a[i]);
			}
			return a;
		}
	}
};

Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', TR.conf.finals.ajax.path_lib + 'ext-ux');
Ext.require('Ext.ux.form.MultiSelect');
Ext.require([
    'Ext.ux.grid.FiltersFeature'
]);


Ext.onReady( function() {
    Ext.override(Ext.form.FieldSet,{setExpanded:function(a){var b=this,c=b.checkboxCmp,d=b.toggleCmp,e;a=!!a;if(c){c.setValue(a)}if(d){d.setType(a?"up":"down")}if(a){e="expand";b.removeCls(b.baseCls+"-collapsed")}else{e="collapse";b.addCls(b.baseCls+"-collapsed")}b.collapsed=!a;b.doComponentLayout();b.fireEvent(e,b);return b}});
    Ext.QuickTips.init();
    document.body.oncontextmenu = function(){return false;}; 
    
    Ext.Ajax.request({
        url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.initialize,
        success: function(r) {
            
    TR.init = TR.conf.init.ajax.jsonfy(r);    
    TR.init.initialize = function() {        
        TR.init.cmd = TR.util.getUrlParam(TR.conf.finals.cmd.urlparam) || TR.conf.finals.cmd.init;
    };
    
    TR.cmp = {
        region: {},
        settings: {},
        params: {
            program:{},
			programStage: {},
			patientProperty: {},
			dataelement: {},
			organisationunit: {},
			relativeperiod: {
				checkbox: []
			},
			fixedperiod: {},
			dataElementGroupBy:{}
        },
        options: {},
        toolbar: {
            menuitem: {}
        },
        statusbar: {},
        caseBasedFavorite: {
            rename: {}
        },
		aggregateFavorite: {
            rename: {}
        }
    };
    
    TR.util = {
		getCmp: function(q) {
            return TR.viewport.query(q)[0];
        },
		list:{
			addOptionToList: function( list, optionValue, optionText ){
				var option = document.createElement( "option" );
				option.value = optionValue;
				option.text = optionText;
				option.setAttribute('selected',true)
				list.add( option, null );
			},
			clearList: function( list ) {
				list.options.length = 0;
			}
		},
        getUrlParam: function(s) {
            var output = '';
            var href = window.location.href;
            if (href.indexOf('?') > -1 ) {
                var query = href.substr(href.indexOf('?') + 1);
                var query = query.split('&');
                for (var i = 0; i < query.length; i++) {
                    if (query[i].indexOf('=') > -1) {
                        var a = query[i].split('=');
                        if (a[0].toLowerCase() === s) {
							output = a[1];
							break;
						}
                    }
                }
            }
            return unescape(output);
        },
        viewport: {
            getSize: function() {
                return {x: TR.cmp.region.center.getWidth(), y: TR.cmp.region.center.getHeight()};
            },
            getXY: function() {
                return {x: TR.cmp.region.center.x + 15, y: TR.cmp.region.center.y + 43};
            },
            getPageCenterX: function(cmp) {
                return ((screen.width/2)-(cmp.width/2));
            },
            getPageCenterY: function(cmp) {
                return ((screen.height/2)-((cmp.height/2)-100));
            },
            resizeParams: function() {
				var a = [TR.cmp.params.patientProperty.panel,
						 TR.cmp.params.dataelement.panel, 
						 TR.cmp.params.organisationunit.treepanel];
				for (var i = 0; i < a.length; i++) {
					if (!a[i].collapsed) {
						a[i].fireEvent('expand');
					}
				}
			}
        },
        multiselect: {
            select: function(a, s, f) {
                var selected = a.getValue();
				if( selected.length > 0 )
				{
					var idx = a.store.findExact('id', selected);
					var name = a.store.getAt(idx).data.name;
					var valueType = a.store.getAt(idx).data.valueType;
					
					if (selected.length) {
						var array = [];
						Ext.Array.each(selected, function(item) {
							var data = a.store.findExact('id', item);
							array.push({id: item, uid:a.store.getAt(data).data.uid, name: a.store.getAt(data).data.name, compulsory: a.store.getAt(data).data.compulsory, valueType: a.store.getAt(data).data.valueType});
						});
						s.store.add(array);
					}
					this.filterAvailable(a, s);
					
					if(f!=undefined)
					{
						this.addFilterField( f, selected[0], name, valueType );
					}
				}
            },
            selectAll: function(a, s, f) {
				var array = [];
				var elements = a.boundList.all.elements;
				for( var i=0; i< elements.length; i++ )
				{
					if( elements[i].style.display != 'none' )
					{
						var id = a.store.getAt(i).data.id;
						var name = a.store.getAt(i).data.name;
						var valueType = a.store.getAt(i).data.valueType;
						
						array.push({id: id, uid:a.store.getAt(i).data.uid, name: name, compulsory: a.store.getAt(i).data.compulsory, valueType: valueType});
						if(f!=undefined)
						{
							this.addFilterField( f, id, name, valueType );
						}
					}
				}
				s.store.add(array);
                this.filterAvailable(a, s);
            },            
            unselect: function(a, s, f) {
                var selected = s.getValue();
				if( selected.length > 0 )
				{
					if (selected.length) {
						Ext.Array.each(selected, function(item) {
							s.store.remove(s.store.getAt(s.store.findExact('id', item)));
						});                    
						this.filterAvailable(a, s);
					}
					if(f!=undefined)
					{
						this.removeFilterField( f, selected[0], a.store.getAt(a.store.findExact('id', selected)).data.valueType );
					}
				}
            },
            unselectAll: function(a, s, f) {
                var elements = s.boundList.all.elements;
				var index = 0;
				var arr = [];
				Ext.Array.each(s.store.data.items, function(item) {
					if( elements[index].style.display != 'none' )
					{
					  arr.push(item.data.id);
					  if(f!=undefined)
					  {
						TR.util.multiselect.removeFilterField( f, item.data.id, item.data.valueType );
					  }
					}
					index++;
				}); 
				s.setValue(arr);
				this.unselect(a,s);
            },
            filterAvailable: function(a, s) {
				a.store.filterBy( function(r) {
                    var filter = true;
                    s.store.each( function(r2) {
                        if (r.data.id == r2.data.id) {
                            filter = false;
                        }
                    });
                    return filter;
                });
            },
			filterSelector: function(selectors, queryString) {
                var elements = selectors.boundList.all.elements;

				for( var i=0; i< elements.length; i++ )
				{
					if( elements[i].innerHTML.toLowerCase().indexOf( queryString ) != -1 )
					{
						elements[i].style.display = 'block';
					}
					else
					{
						elements[i].style.display = 'none';
					}
				}
            },
            setHeight: function(ms, panel, fill) {
				for (var i = 0; i < ms.length; i++) {
					ms[i].setHeight(panel.getHeight() - 49);
				}
			},
			addFilterField: function( p, id, name, valueType ){
				var panelid = p + '_' + id;
				var idx = 0;
				var subPanel = Ext.getCmp(panelid);
				if( subPanel == undefined )
				{
					var panel = {
						xtype: 'panel',
						id: panelid,
						layout: 'column',
						bodyStyle: 'border-style:none',
						autoScroll: true,
						overflowX: 'hidden',
						overflowY: 'auto',
						width: TR.conf.layout.west_fieldset_width - 40
					};
					Ext.getCmp(p).add(panel);
					subPanel = Ext.getCmp(panelid);
				}
				else {
					idx = subPanel.items.length/4;
				}
				
				var items = [];
				var fieldid = id + '_' + idx;
				items[0] = {
					xtype: 'label',
					id: 'filter_lb_' + fieldid,
					text:name,
					width:(TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2 - 60
				};
				items[1] = this.createOperatorField(valueType, fieldid);
				items[2] = this.createFilterField( valueType, fieldid );
				if( idx == 0 ){
					items[3] = this.addFieldBtn( p, id, name, valueType, idx );
				}
				else
				{
					items[3] = this.removeFieldBtn( panelid, fieldid );
				}
				
				subPanel.add(items);
			},
			removeFilterField: function( p, id ){
				var e1 = Ext.getCmp( p + '_' + id );
				Ext.getCmp(p).remove(e1);
			},
			createOperatorField: function( valueType, id ){
				var params = {};
				params.xtype = 'combobox';
				params.id = 'filter_opt_' + id;
				params.width = 50;
				params.queryMode = 'local';
				params.valueField = 'value';
				params.displayField = 'name';
				params.editable = false;
				params.value = '=';
				
				if(valueType == 'string' || valueType == 'list' || valueType == 'username' ){
					var fixedId = id.substring(0, id.lastIndexOf('_') );
					if( fixedId=='fixedAttr_gender' || fixedId=='fixedAttr_dobType')
					{
						params.store = new Ext.data.ArrayStore({
							fields: ['value','name'],
							data: [ ['=','='],['in',TR.i18n.in] ]
						});
						params.value = 'in';
					}
					else
					{
						params.store = new Ext.data.ArrayStore({
							fields: ['value','name'],
							data: [ ['=','='],['like',TR.i18n.like],['in',TR.i18n.in] ]
						});
						params.value = 'in';
					}
				}
				else if( valueType == 'trueOnly' || valueType == 'bool' ){
					params.store = new Ext.data.ArrayStore({
						fields: ['value','name'],
						data: [ ['=','='] ]
					});
				}
				else
				{
					params.store = new Ext.data.ArrayStore({
						fields: ['value','name'],
						data: [ ['=','='],
								['>','>'],
								['>=','>='],
								['<','<'],
								['<=','<='],
								['!=','!=' ] ]
					});
				}
				
				params.listeners={};
				params.listeners.select = function(cb)  {
					var opt = cb.getValue();
					if(opt == 'in')
					{
						Ext.getCmp('filter_' + id).multiSelect = true;
					}
					else
					{
						Ext.getCmp('filter_' + id).clearValue();
						Ext.getCmp('filter_' + id).multiSelect = false;
					}
				}
				
				return params;
			},
			createFilterField: function( valueType, id ){
				var params = {};
				var xtype = TR.value.covertXType(valueType);
				params.xtype = xtype;
				params.id = 'filter_' + id;
				params.cls = 'tr-textfield-alt1';
				params.emptyText = TR.i18n.filter_value;
				params.width = (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2 - 50;
				xtype = xtype.toLowerCase();
				if( xtype == 'datefield' )
				{
					params.format = TR.i18n.format_date;
				}
				else if( xtype == 'combobox' )
				{
					var deId = id.split('_')[1];
					var fixedId = id.substring(0, id.lastIndexOf('_') );
					params.typeAhead = true;
					params.editable = true;
					if( valueType == 'bool' || fixedId=='fixedAttr_gender' || fixedId=='fixedAttr_dobType')
					{
						params.queryMode = 'local';
						params.valueField = 'value';
						params.displayField = 'name';
						params.editable = false;
						if( fixedId=='fixedAttr_gender')
						{
							params.multiSelect = true;
							params.delimiter = ';';
							params.forceSelection = true;
							params.store = new Ext.data.ArrayStore({
								fields: ['value', 'name'],
								data: [['', TR.i18n.please_select], 
									['M', TR.i18n.male], 
									['F', TR.i18n.female],
									['T', TR.i18n.transgender]]
							});
						}
						else if( fixedId=='fixedAttr_dobType')
						{
							params.forceSelection = true;
							params.multiSelect = true;
							params.delimiter = ';';
							params.editable = false;
							params.store = new Ext.data.ArrayStore({
								fields: ['value', 'name'],
								data: [['', TR.i18n.please_select],
									['V', TR.i18n.verified], 
									['D', TR.i18n.declared],
									['A', TR.i18n.approximated]]
							});
						}
						else if (valueType == 'bool')
						{
							params.forceSelection = true;
							params.store = new Ext.data.ArrayStore({
								fields: ['value', 'name'],
								data: [['', TR.i18n.please_select], 
									['true', TR.i18n.yes], 
									['false', TR.i18n.no]]
							});
						}
					}
					else if( valueType == 'trueOnly'){
						params.queryMode = 'local';
						params.valueField = 'value';
						params.displayField = 'name';
						params.editable = false;
						params.store = new Ext.data.ArrayStore({
							fields: ['value', 'name'],
							data: [['', TR.i18n.please_select],['true', TR.i18n.yes]]
						});
					}
					else if(valueType=='username'){
						params.queryMode = 'remote';
						params.valueField = 'u';
						params.displayField = 'u';
						params.multiSelect = true;
						params.delimiter = ';';
						params.store = Ext.create('Ext.data.Store', {
							fields: ['u'],
							data:[],
							proxy: {
								type: 'ajax',
								url: TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.username_dataelement_get,
								reader: {
									type: 'json',
									root: 'usernames'
								}
							}
						});
					}
					else{
						params.queryMode = 'remote';
						params.valueField = 'o';
						params.displayField = 'o';
						params.multiSelect = true;
						params.delimiter = ';';
						var index = TR.store.dataelement.selected.findExact('id', 'de_' + deId);
						var deUid = TR.store.dataelement.selected.getAt(index).data.uid;
						params.store = Ext.create('Ext.data.Store', {
							fields: ['o'],
							data:[],
							proxy: {
								type: 'ajax',
								url: TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.suggested_dataelement_get,
								extraParams:{id: deUid},
								reader: {
									type: 'json',
									root: 'options'
								}
							}
						});
					}					
				}
				params.value = '';
				return params;
			},
			addFieldBtn: function( p, id, name, valueType, idx ){
				var params = {};
				params.xtype = 'button';
				params.text = "+";
				params.tooltip = TR.i18n.add,
				params.handler = function() {
					TR.util.multiselect.addFilterField(p, id, name, valueType);
				}
				
				return params;
			},
			removeFieldBtn: function( p, id ){
				var params = {};
				params.xtype = 'button';
				params.id = 'filter_rmv_' + id;	
				params.text = "-";
				params.tooltip = TR.i18n.remove,
				params.handler = function() {
					var e1 = Ext.getCmp( 'filter_' + id );
					var e2 = Ext.getCmp( 'filter_opt_' + id );	
					var e3 = Ext.getCmp( 'filter_lb_' + id );
					var e4 = Ext.getCmp( 'filter_rmv_' + id );
					Ext.getCmp(p).remove(e1);
					Ext.getCmp(p).remove(e2);
					Ext.getCmp(p).remove(e3);
					Ext.getCmp(p).remove(e4);
				}
				return params;
			}
        },
        positionFilter:{
			orgunit: function() {
				var o = TR.cmp.settings.positionOrgunit.value;
				
				// Orgunit is columns
				if( o==1 )
				{
					var periodStore = TR.cmp.settings.positionPeriod.store;
					periodStore.removeAll();
					periodStore.add (
						{value: 1,name: TR.i18n.rows},
						{value: 2,name: TR.i18n.columns},
						{value: 3,name: TR.i18n.filters}
					);
					Ext.getCmp('positionPeriodCbx').setValue( 1 );
				}
				// Orgunit is columns
				else if( o==2 )
				{
					var periodStore = TR.cmp.settings.positionPeriod.store;
					periodStore.removeAll();
					periodStore.add (
						{value: 1,name: TR.i18n.rows},
						{value: 3,name: TR.i18n.filter}
					);
					Ext.getCmp('positionPeriodCbx').setValue( 1 );
					
					var dataStore = TR.cmp.settings.positionData.store;
					dataStore.removeAll();
					dataStore.add ({value: 3,name: TR.i18n.filters});
					Ext.getCmp('positionDataCbx').setValue( 3 );
				}
				// Orgunit is filters
				else if( o==3)
				{
					var periodStore = TR.cmp.settings.positionPeriod.store;
					periodStore.removeAll();
					periodStore.add (
						{value: 1,name: TR.i18n.rows},
						{value: 2,name: TR.i18n.columns},
						{value: 3,name: TR.i18n.filters}
					);
					Ext.getCmp('positionPeriodCbx').setValue( 1 );
					
					var dataStore = TR.cmp.settings.positionData.store;
					dataStore.removeAll();
					dataStore.add (
						{value: 2,name: TR.i18n.columns},
						{value: 3,name: TR.i18n.filters}
					);
					Ext.getCmp('positionDataCbx').setValue( 2 );
				}
			},
			period: function(){
				// Orgunit is column
				var o = TR.cmp.settings.positionOrgunit.value;
				var p = TR.cmp.settings.positionPeriod.value;
				
				var dataStore = TR.cmp.settings.positionData.store;
				dataStore.removeAll();
					
				if( o==1 ){
					if( p==1 || p==2 ){
						dataStore.add (
							{value: 3,name: TR.i18n.filters}
						);
					}
					else if( p==3 ){
						dataStore.add (
							{value: 2,name: TR.i18n.columns},
							{value: 3,name: TR.i18n.filters}
						);
					}
					Ext.getCmp('positionDataCbx').setValue( 3 );
				}
				else if( o==2 ){
					if( p==3 ){
						dataStore.add (
							{value: 1,name: TR.i18n.rows}
						);
						Ext.getCmp('positionDataCbx').setValue( 1 );
					}
					else if( p==1 ){
						dataStore.add (
							{value: 3,name: TR.i18n.filters}
						);
						Ext.getCmp('positionDataCbx').setValue( 3 );
					}
				}
				else if( o==3 && p==1 ){
					dataStore.add (
						{value: 2,name: TR.i18n.columns},
						{value: 3,name: TR.i18n.filters}
					);
					Ext.getCmp('positionDataCbx').setValue( 2 );
				}
				else if( o==3 && ( p==2 || p==3 ) ){
					var dataStore = TR.cmp.settings.positionData.store;
					dataStore.removeAll();
					dataStore.add (
						{value: 1,name: TR.i18n.rows}
					);
					Ext.getCmp('positionDataCbx').setValue( 1 );
				}
			},
			convert: function( position )
			{
				switch( eval(position) ){
				
				case TR.conf.reportPosition.POSITION_ROW_ORGUNIT_COLUMN_PERIOD :
					Ext.getCmp('positionOrgunitCbx').setValue(1);
					Ext.getCmp('positionPeriodCbx').setValue(2);
					Ext.getCmp('positionDataCbx').setValue(3);
					break;
				case TR.conf.reportPosition.POSITION_ROW_PERIOD_COLUMN_ORGUNIT :
					Ext.getCmp('positionOrgunitCbx').setValue(2);
					Ext.getCmp('positionPeriodCbx').setValue(1);
					Ext.getCmp('positionDataCbx').setValue(3);
					break;
				case TR.conf.reportPosition.POSITION_ROW_ORGUNIT_ROW_PERIOD :
					Ext.getCmp('positionOrgunitCbx').setValue(1);
					Ext.getCmp('positionPeriodCbx').setValue(1);
					Ext.getCmp('positionDataCbx').setValue(3);
					break;
				case TR.conf.reportPosition.POSITION_ROW_PERIOD :
					Ext.getCmp('positionOrgunitCbx').setValue(3);
					Ext.getCmp('positionPeriodCbx').setValue(1);
					Ext.getCmp('positionDataCbx').setValue(3);
					break;
				case TR.conf.reportPosition.POSITION_ROW_PERIOD :
					Ext.getCmp('positionOrgunitCbx').setValue(3);
					Ext.getCmp('positionPeriodCbx').setValue(1);
					Ext.getCmp('positionDataCbx').setValue(2);
					break;
				case TR.conf.reportPosition.POSITION_ROW_ORGUNIT :
					Ext.getCmp('positionOrgunitCbx').setValue(1);
					Ext.getCmp('positionPeriodCbx').setValue(2);
					Ext.getCmp('positionDataCbx').setValue(3);
					break;	
				case TR.conf.reportPosition.POSITION_ROW_PERIOD_COLUMN_DATA :
					Ext.getCmp('positionOrgunitCbx').setValue(3);
					Ext.getCmp('positionPeriodCbx').setValue(1);
					Ext.getCmp('positionDataCbx').setValue(2);
					break;
				case TR.conf.reportPosition.POSITION_ROW_ORGUNIT_COLUMN_DATA :
					Ext.getCmp('positionOrgunitCbx').setValue(1);
					Ext.getCmp('positionPeriodCbx').setValue(3);
					Ext.getCmp('positionDataCbx').setValue(2);
					break;
				case TR.conf.reportPosition.POSITION_ROW_DATA :
					Ext.getCmp('positionOrgunitCbx').setValue(3);
					Ext.getCmp('positionPeriodCbx').setValue(3);
					Ext.getCmp('positionDataCbx').setValue(1);
					break;
				case TR.conf.reportPosition.POSITION_ROW_DATA_COLUMN_PERIOD :
					Ext.getCmp('positionOrgunitCbx').setValue(3);
					Ext.getCmp('positionPeriodCbx').setValue(2);
					Ext.getCmp('positionDataCbx').setValue(1);
					break;
				case TR.conf.reportPosition.POSITION_ROW_DATA_COLUMN_ORGUNIT :
					Ext.getCmp('positionOrgunitCbx').setValue(2);
					Ext.getCmp('positionPeriodCbx').setValue(3);
					Ext.getCmp('positionDataCbx').setValue(1);
					break;
				}
			}
		},
		store: {
            addToStorage: function(s, records) {
                s.each( function(r) {
                    if (!s.storage[r.data.id]) {
                        s.storage[r.data.id] = {id: r.data.id, name: TR.util.string.getEncodedString(r.data.name), parent: s.parent, compulsory: r.data.compulsory, valueType: r.data.valueType};
                    }
                });
                if (records) {
                    Ext.Array.each(records, function(r) {
                        if (!s.storage[r.data.id]) {
                            s.storage[r.data.id] = {id: r.data.id, name: TR.util.string.getEncodedString(r.data.name), parent: s.parent};
                        }
                    });
                }                        
            },
            loadFromStorage: function(s) {
                var items = [];
                s.removeAll();
                for (var obj in s.storage) {
                    if (s.storage[obj].parent === s.parent) {
                        items.push(s.storage[obj]);
                    }
                }
                s.add(items);
                s.sort('name', 'ASC');
            },
            containsParent: function(s) {
                for (var obj in s.storage) {
                    if (s.storage[obj].parent === s.parent) {
                        return true;
                    }
                }
                return false;
            }
        },
        notification: {
			error: function(title, text) {
				title = title || '';
				text = text || '';
				Ext.create('Ext.window.Window', {
					title: title,
					cls: 'tr-messagebox',
					iconCls: 'tr-window-title-messagebox',
					modal: true,
					width: 300,
					items: [
						{
							xtype: 'label',
							width: 40,
							text: text
						}
					]
				}).show();
				TR.cmp.statusbar.panel.setWidth(TR.cmp.region.center.getWidth());
				TR.cmp.statusbar.panel.update('<img src="' + TR.conf.finals.ajax.path_images + TR.conf.statusbar.icon.error + '" style="padding:0 5px 0 0"/>' + text);
			},
			warning: function(text) {
				text = text || '';
				TR.cmp.statusbar.panel.setWidth(TR.cmp.region.center.getWidth());
				TR.cmp.statusbar.panel.update('<img src="' + TR.conf.finals.ajax.path_images + TR.conf.statusbar.icon.warning + '" style="padding:0 5px 0 0"/>' + text);
			},
			ok: function() {
				TR.cmp.statusbar.panel.setWidth(TR.cmp.region.center.getWidth());
				TR.cmp.statusbar.panel.update('<img src="' + TR.conf.finals.ajax.path_images + TR.conf.statusbar.icon.ok + '" style="padding:0 5px 0 0"/>&nbsp;&nbsp;');
			}				
		},
        mask: {
            showMask: function(cmp, str) {
                if (TR.mask) {
                    TR.mask.destroy();
                }
                TR.mask = new Ext.LoadMask(cmp, {msg: str});
                TR.mask.show();
            },
            hideMask: function() {
				if (TR.mask) {
					TR.mask.hide();
				}
			}
        },
		/*FIXME:This is probably not going to work as intended with UNICODE?*/
        string: {
            getEncodedString: function(text) {
                return text.replace(/[^a-zA-Z 0-9(){}<>_!+;:?*&%#-]+/g,'');
            }
        },
		getValueFormula: function( value ){
			if( value.indexOf('"') != value.lastIndexOf('"') )
			{
				value = value.replace(/"/g,"'");
			}
			// if key is [xyz] && [=xyz]
			if( value.indexOf("'")==-1 ){
				var flag = value.match(/[>|>=|<|<=|=|!=]+[ ]*/);
			
				if( flag == null )
				{
					value = "='"+ value + "'";
				}
				else
				{
					value = value.replace( flag, flag + "'");
					value +=  "'";
				}
			}
			// if key is ['xyz'] && [='xyz']
			// if( value.indexOf("'") != value.lastIndexOf("'") )
			else
			{
				var flag = value.match(/[>|>=|<|<=|=|!=]+[ ]*/);
			
				if( flag == null )
				{
					value = "="+ value;
				}
			}
			return value;
		},
        crud: {
            favorite: {
                create: function(fn, isupdate) {
					var url = "";
					if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
					{
						this.caseBasedReport.create(fn, isupdate);
					}
					else
					{
						this.aggregateReport.create(fn, isupdate);
					}
                },
                update: function(fn) {
					TR.util.crud.favorite.create(fn, true);
                },
				updateName: function(id, name) {
                    if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
					{
						this.caseBasedReport.updateName(id, name);
					}
					else
					{
						this.aggregateReport.updateName(id, name);
					}
                },
                del: function(fn) {
					if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
					{
						this.caseBasedReport.del(fn);
					}
					else
					{
						this.aggregateReport.del(fn);
					}
                },
				run: function(id) {
					if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
					{
						this.caseBasedReport.run( id );
					}
					else
					{
						this.aggregateReport.run( id );
					}
				},			
				
				caseBasedReport:{
					
					updateName: function(id, name) {
						if (TR.store.caseBasedFavorite.findExact('name', name) != -1) {
							return;
						}
						TR.util.mask.showMask(TR.cmp.caseBasedFavorite.window, TR.i18n.renaming + '...');
						Ext.Ajax.request({
							url:  TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_rename,
							method: 'POST',
							params: {id: id, name: name},
							success: function() {
								TR.store.caseBasedFavorite.load({callback: function() {
									TR.cmp.caseBasedFavorite.rename.window.close();
									TR.util.mask.hideMask();
									TR.cmp.caseBasedFavorite.grid.getSelectionModel().select(TR.store.caseBasedFavorite.getAt(TR.store.caseBasedFavorite.findExact('name', name)));
									TR.cmp.caseBasedFavorite.name.setValue(name);
								}});
							}
						});
					},
					del: function(fn) {
						TR.util.mask.showMask(TR.cmp.caseBasedFavorite.window, TR.i18n.deleting + '...');
						var id = TR.cmp.caseBasedFavorite.grid.getSelectionModel().getSelection()[0].data.id;
						var baseurl =  TR.conf.finals.ajax.casebasedfavorite_delete + "?id=" + id;
						selection = TR.cmp.caseBasedFavorite.grid.getSelectionModel().getSelection();
						Ext.Array.each(selection, function(item) {
							baseurl = Ext.String.urlAppend(baseurl, 'uids=' + item.data.id);
						});
						
						Ext.Ajax.request({
							url: baseurl,
							method: 'POST',
							success: function() {
								TR.store.caseBasedFavorite.load({callback: function() {
									TR.util.mask.hideMask();
									if (fn) {
										fn();
									}
								}});
							}
						}); 
					},
					run: function(id) {
						Ext.Ajax.request({
							url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_get + '?id=' + id,
							scope: this,
							success: function(r) {
								var f = Ext.JSON.decode(r.responseText);
								
								Ext.getCmp('programCombobox').setValue( f.programId );
								TR.store.programStage.removeAll();
								TR.store.programStage.add({'id': f.programStageId, 'name': f.programStageName});
								Ext.getCmp('startDate').setValue( f.startDate );
								Ext.getCmp('endDate').setValue( f.endDate );
								Ext.getCmp('facilityLBCombobox').setValue( f.facilityLB );
								Ext.getCmp('levelCombobox').setValue( f.level );
								Ext.getCmp('userOrgunit').setValue( f.userOrganisationUnit );
								Ext.getCmp('userOrgunitChildren').setValue( f.userOrganisationUnitChildren );								
								Ext.getCmp('completedEventsOpt').setValue(f.useCompletedEvents);
								
								// Orgunits
								
								var treepanel = TR.cmp.params.organisationunit.treepanel;
								treepanel.getSelectionModel().deselectAll();
								TR.state.orgunitIds = [];
								var orgunitUids = [];
								for (var i = 0; i < f.orgunitIds.length; i++) {
									TR.state.orgunitIds.push( f.orgunitIds[i].localid );
									orgunitUids.push( f.orgunitIds[i].id );
								}
								treepanel.selectByIds(orgunitUids);
								
								 // Patient properties
								 Ext.getCmp('filterPropPanel').removeAll();
								 Ext.getCmp('filterPropPanel').doLayout(); 
								 TR.store.patientProperty.selected.removeAll();
								 
								 // programs with registration
								 TR.cmp.params.patientProperty.objects = [];
								 if (f.patientProperties && f.type != "3" ) {
									 for (var i = 0; i < f.patientProperties.length; i++) {
										var name = TR.util.string.getEncodedString(f.patientProperties[i].name);
										TR.cmp.params.patientProperty.objects.push({id: f.patientProperties[i].id, name: name});
										TR.util.multiselect.addFilterField( 'filterPropPanel', f.patientProperties[i].id, name, f.patientProperties[i].valueType );
									 }
									 TR.store.patientProperty.selected.add(TR.cmp.params.patientProperty.objects);
							 
									 var storePatientProperty = TR.store.patientProperty.available;
									 storePatientProperty.parent = f.programId;
									 if (TR.util.store.containsParent(storePatientProperty)) {
										 TR.util.store.loadFromStorage(storePatientProperty);
										 TR.util.multiselect.filterAvailable(TR.cmp.params.patientProperty.available, TR.cmp.params.patientProperty.selected);
									 }
									 else {
										 storePatientProperty.load({params: {programId: f.programId}});
									 }
								 }
                                                         
								// Data element
								
								Ext.getCmp('filterPanel').removeAll();
								Ext.getCmp('filterPanel').doLayout();
	
								TR.cmp.params.dataelement.objects = [];
								TR.store.dataelement.selected.removeAll();
								if (f.dataElements) {
									for (var i = 0; i < f.dataElements.length; i++) {
										var name = TR.util.string.getEncodedString(f.dataElements[i].name);
										var compulsory = f.dataElements[i].compulsory;
										var valueType = f.dataElements[i].valueType;
										TR.cmp.params.dataelement.objects.push({id: f.dataElements[i].id, name: name, compulsory: compulsory, valueType: valueType});
										TR.util.multiselect.addFilterField( 'filterPanel', f.dataElements[i].id, name, valueType );
									}
									TR.store.dataelement.selected.add(TR.cmp.params.dataelement.objects);
									
									if( f.singleEvent == 'false' )
									{
										var store = TR.store.dataelement.available;
										store.parent = f.programStageId;
										if (TR.util.store.containsParent(store)) {
											TR.util.store.loadFromStorage(store);
											TR.util.multiselect.filterAvailable(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected);
										}
										else {
											store.load({params: {programStageId: f.programStageId}});
										}
									}
								}
								
								// Program stage									
								var storeProgramStage = TR.store.programStage;
								storeProgramStage.parent = f.programStageId;
								storeProgramStage.isLoadFromFavorite = true;
								storeProgramStage.load({params: {programId: f.programId}});
								
								Ext.getCmp('programStageCombobox').setValue( f.programStageId );
								TR.cmp.params.organisationunit.treepanel.getSelectionModel().deselectAll();
				
								TR.exe.execute();
							}
						});
					}				
				},
				
				aggregateReport:{
					create: function(fn, isupdate) {
						// Validation
						
						if( !TR.state.aggregateReport.validation.objects() )
						{
							return;
						}
						
						// Save favorite
						
						TR.util.mask.showMask(TR.cmp.aggregateFavorite.window, TR.i18n.saving + '...');
						var p = TR.state.getParams();
						p.name = TR.cmp.aggregateFavorite.name.getValue();
						
						if (isupdate) {
							p.uid = TR.store.aggregateFavorite.getAt(TR.store.aggregateFavorite.findExact('name', p.name)).data.id;
						}
						
						Ext.Ajax.request({
							url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_save,
							method: 'POST',
							params: p,
							success: function() {
								TR.store.aggregateFavorite.load({callback: function() {
									TR.util.mask.hideMask();
									if (fn) {
										fn();
									}
								}});
							}
						});  
					},
					updateName: function(id, name) {
						if (TR.store.aggregateFavorite.findExact('name', name) != -1) {
							return;
						}
						TR.util.mask.showMask(TR.cmp.aggregateFavorite.window, TR.i18n.renaming + '...');
						Ext.Ajax.request({
							url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_rename,
							method: 'POST',
							params: {id: id, name: name},
							success: function() {
								TR.store.aggregateFavorite.load({callback: function() {
									TR.cmp.aggregateFavorite.rename.window.close();
									TR.util.mask.hideMask();
									TR.cmp.aggregateFavorite.grid.getSelectionModel().select(TR.store.aggregateFavorite.getAt(TR.store.aggregateFavorite.findExact('name', name)));
									TR.cmp.aggregateFavorite.name.setValue(name);
								}});
							}
						});
					},
					del: function(fn) {
						TR.util.mask.showMask(TR.cmp.aggregateFavorite.window, TR.i18n.deleting + '...');
						var id = TR.cmp.aggregateFavorite.grid.getSelectionModel().getSelection()[0].data.id;
						var baseurl =  TR.conf.finals.ajax.aggregatefavorite_delete + "?id=" + id;
							selection = TR.cmp.aggregateFavorite.grid.getSelectionModel().getSelection();
						Ext.Array.each(selection, function(item) {
							baseurl = Ext.String.urlAppend(baseurl, 'uids=' + item.data.id);
						});
						Ext.Ajax.request({
							url: baseurl,
							method: 'POST',
							success: function() {
								TR.store.aggregateFavorite.load({callback: function() {
									TR.util.mask.hideMask();
									if (fn) {
										fn();
									}
								}});
							}
						}); 
					},	
					run: function(id) {
						Ext.Ajax.request({
							url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_get + '?id=' + id,
							scope: this,
							success: function(r) {
								var f = Ext.JSON.decode(r.responseText);
								
								Ext.getCmp('programCombobox').setValue( f.programId );
								
								// Program-Stage
								
								TR.store.programStage.removeAll();
								TR.store.programStage.add({'id': f.programStageId, 'name': f.programStageName});
								
								Ext.getCmp('userOrgunit').setValue( f.userOrganisationUnit );
								Ext.getCmp('userOrgunitChildren').setValue( f.userOrganisationUnitChildren );								
								
								// Date period range
								
								TR.store.dateRange.removeAll();
								for( var i=0;i<f.startDates.length;i++)
								{
									TR.store.dateRange.add(
										{'startDate': f.startDates[i],'endDate': f.endDates[i]}
									);
								}						
								
								// Relative periods
								
								Ext.Array.each(TR.cmp.params.relativeperiod.checkbox, function(item) {
									if(item.getValue() && !item.hidden){
										item.setValue(false);
									}
								});
								for (var i = 0; i < f.relativePeriods.length; i++) {
									TR.util.getCmp('checkbox[paramName="' + f.relativePeriods[i] + '"]').setValue(true);
								}
								
								// Fixed periods
								
								TR.store.fixedperiod.selected.removeAll();
								
								var periods = [];
								for (var i = 0; i < f.fixedPeriods.length; i++) {
									periods[i]={
										id: f.fixedPeriods[i],
										name: f.fixedPeriodNames[i]
									};
								}
								TR.store.fixedperiod.selected.loadData(periods);
								
								// Orgunits
								
								var treepanel = TR.cmp.params.organisationunit.treepanel;
								treepanel.getSelectionModel().deselectAll();
								TR.state.orgunitIds = [];
								var orgunitUids = [];
								for (var i = 0; i < f.orgunitIds.length; i++) {
									TR.state.orgunitIds.push( f.orgunitIds[i].localid );
									orgunitUids.push( f.orgunitIds[i].id );
								}
								treepanel.selectByIds(orgunitUids);
								
								// Selected data elements
								
								Ext.getCmp('filterPanel').removeAll();
								Ext.getCmp('filterPanel').doLayout();
				
								TR.cmp.params.dataelement.objects = [];
								TR.store.dataelement.selected.removeAll();
								for (var i = 0; i < f.selectedDEs.length; i++) {
									var id = f.selectedDEs[i].id;
									TR.cmp.params.dataelement.objects.push({id: id, name: TR.util.string.getEncodedString(f.selectedDEs[i].name), compulsory: f.selectedDEs[i].compulsory, valueType:f.selectedDEs[i].valueType });
									
									// Add filter field
									TR.util.multiselect.addFilterField( 'filterPanel', id, f.selectedDEs[i].name, f.selectedDEs[i].valueType );
								}
								TR.store.dataelement.selected.add(TR.cmp.params.dataelement.objects);
								
								var availableStore = TR.store.dataelement.available;
								availableStore.parent = f.programStageId;
								if (TR.util.store.containsParent(availableStore)) {
									TR.util.store.loadFromStorage(availableStore);
									TR.util.multiselect.filterAvailable(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected);
								}
								else {
									availableStore.load({params: {programStageId: f.programStageId}});
								}
								
								// Filter values
								
								Ext.getCmp('filterPanel').removeAll();
								for (var j = 0; j < f.selectedDEs.length; j++) {
									var id = f.selectedDEs[j].id;
									var name = TR.util.string.getEncodedString(f.selectedDEs[j].name);
									var valueType = f.selectedDEs[j].valueType;
									
									for (var i = 0; i < f.filterValues.length; i++) {
										var filter = f.filterValues[i].split('_');
										var fitlerId = 'de_' + filter[0];
										if(id==fitlerId){
											TR.util.multiselect.addFilterField( 'filterPanel', fitlerId, name, valueType );
											var idx = Ext.getCmp('filterPanel_' + fitlerId).items.length/4 - 1;
											var value = filter[2].replace('(','').replace(')','').replace(/,/g, ';').replace(/'/g, '');
											
											if(valueType=='list'){
												Ext.getCmp('filter_' + fitlerId + '_' + idx ).store.add({o:value});
											}
											Ext.getCmp('filter_opt_' + fitlerId + '_' + idx ).setValue(filter[1]);
											Ext.getCmp('filter_' + fitlerId + '_' + idx ).setValue(value);
										}
									}
								}
								
								TR.util.positionFilter.convert( f.position );
								
								Ext.getCmp('completedEventsOpt').setValue(f.useCompletedEvents);
								Ext.getCmp('displayTotalsOpt').setValue(f.displayTotalsOpt);
								Ext.getCmp('facilityLBCombobox').setValue( f.facilityLB );
								Ext.getCmp('limitOption').setValue( f.limitRecords );
								Ext.getCmp('levelCombobox').setValue( f.level );
								Ext.getCmp('aggregateType').setValue( f.aggregateType );
								
								if(f.aggregateType=='sum' ){
									Ext.getCmp('aggregateType').items.items[1].setValue(true);
								}else if(f.aggregateType=='avg' ){
									Ext.getCmp('aggregateType').items.items[2].setValue(true);
								}
								else{
									Ext.getCmp('aggregateType').items.items[0].setValue(true);
								}
								
								if( TR.store.groupbyDataelement.data.items.length == 0 )
								{
									TR.store.groupbyDataelement.add(
										{'value': f.deGroupBy,'name': f.deGroupByName}
									);
								}
								Ext.getCmp('dataElementGroupByCbx').setValue( f.deGroupBy );

								if( TR.store.aggregateDataelement.data.items.length == 0 )
								{
									TR.store.aggregateDataelement.add(
										{'id': f.deSumId,'name': f.deSumName}
									);
								}
								Ext.getCmp('deSumCbx').setValue( f.deSumId );

								// Program stage									
								var storeProgramStage = TR.store.programStage;
								storeProgramStage.parent = f.programStageId;
								storeProgramStage.isLoadFromFavorite = true;
								storeProgramStage.load({params: {programId: f.programId}});
								Ext.getCmp('programStageCombobox').setValue( f.programStageId );
								
								TR.exe.execute();
							}
						});
					}				
				}
		   }
        },
		window: {
			setAnchorPosition: function(w, target) {
				var vpw = TR.viewport.getWidth(),
					targetx = target ? target.getPosition()[0] : 600,
					winw = w.getWidth(),
					y = target ? target.getPosition()[1] + target.getHeight() + 4 : 33;

				if ((targetx + winw) > vpw) {
					w.setPosition((vpw - winw - 2), y);
				}
				else {
					w.setPosition(targetx, y);
				}
			}
		}
	};
    
    TR.store = {
		program: Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'type'],
			data:TR.init.system.program
		}),
		orgunitGroup: Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'type'],
			data:TR.init.system.orgunitGroup,
			listeners: {
				load: function() {
					this.insert(0,{id:"", name: TR.i18n.none});
				}
			}
		}),
		patientProperty: {
			available: Ext.create('Ext.data.Store', {
				 fields: ['id', 'name', 'valueType'],
				 proxy: {
					 type: 'ajax',
					 url: TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.patientproperties_get,
					 reader: {
						 type: 'json',
						 root: 'patientProperties'
					 }
				 },
				 isloaded: false,
				 storage: {},
				 listeners: {
					 load: function(s) {
						 this.isloaded = true;
						 TR.util.store.addToStorage(s);
						 TR.util.multiselect.filterAvailable(TR.cmp.params.patientProperty.available, TR.cmp.params.patientProperty.selected);
					 }
				 }
			 }),
			 selected: Ext.create('Ext.data.Store', {
				 fields: ['id', 'name'],
				 data: []
			 })
		},
		programStage: Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.programstages_get,
				reader: {
					type: 'json',
					root: 'programStages'
				}
			},
			isLoadFromFavorite: false,
			listeners:{
				load: function(s) {
					Ext.override(Ext.LoadMask, {
						 onHide: function() {
							  this.callParent();
						 }
					});
					
					if( TR.store.programStage.data.items.length > 1 )
					{
						Ext.getCmp('programStageCombobox').enable();
						Ext.getCmp('programStageCombobox').setValue( "" );
					}
					else
					{
						Ext.getCmp('programStageCombobox').disable();
						var programStageId = TR.store.programStage.data.items[0].raw.id;
						Ext.getCmp('programStageCombobox').setValue( programStageId );
						
						// Load data element list
						var store = TR.store.dataelement.available;
						TR.store.dataelement.available.loadData([],false);
						if( !TR.store.programStage.isLoadFromFavorite)
						{
							TR.store.dataelement.selected.loadData([],false);
						}
						store.parent = programStageId;
						
						if (TR.util.store.containsParent(store)) {
							TR.util.store.loadFromStorage(store);
							TR.util.multiselect.filterAvailable(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected);
						}
						else {
							store.load({params: {programStageId: programStageId}});
						}
					}
				}
			}
		}),
		dataelement: {
            available: Ext.create('Ext.data.Store', {
                fields: ['id', 'uid', 'name', 'compulsory', 'valueType'],
                proxy: {
                    type: 'ajax',
                    url: TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.dataelements_get,
                    reader: {
                        type: 'json',
                        root: 'dataElements'
                    }
                },
                isloaded: false,
                storage: {},
                listeners: {
					load: function(s) {
						this.isloaded = true;
                        TR.util.store.addToStorage(s);
                        TR.util.multiselect.filterAvailable(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected);
						
						TR.store.aggregateDataelement.loadData([],false);
						TR.cmp.params.dataelement.available.store.each( function(r) {
							if(r.data.valueType == 'int'){
								TR.store.aggregateDataelement.add({
									'id': r.data.id,
									'name': r.data.name
								});
							}
						});
						
						TR.store.groupbyDataelement.loadData([],false);
						TR.store.groupbyDataelement.add({
								'id': '',
								'name': TR.i18n.please_select
							});
						
						TR.cmp.params.dataelement.available.store.each( function(r) {
							TR.store.groupbyDataelement.add({
								'id': r.data.id,
								'name': r.data.name
							});
						});
                    }
				}
            }),
            selected: Ext.create('Ext.data.Store', {
                fields: ['id', 'uid', 'name', 'compulsory', 'valueType'],
                data: []
            })
        },
        datatable: null,
        getDataTableStore: function() {
			this.datatable = Ext.create('Ext.data.ArrayStore', {
				fields: TR.value.fields,
				data: TR.value.values
			});
        },
		caseBasedFavorite: Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'lastUpdated'],
			proxy: {
				type: 'ajax',
				reader: {
					type: 'json',
					root: 'tabularReports'
				}
			},
			isloaded: false,
            pageSize: 10,
            currentPage: 1,
			defaultUrl: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_getall,
			loadStore: function(url) {
				this.proxy.url = url || this.defaultUrl;
				this.load({
					params: {
						pageSize: this.pageSize,
						currentPage: this.currentPage
					}
				});
			},
			loadFn: function(fn) {
				if (this.isLoaded) {
					fn.call();
				}
				else {
					this.load(fn);
				}
			},
			sorting: {
                field: 'name',
                direction: 'ASC'
            },
            sortStore: function() {
                this.sort(this.sorting.field, this.sorting.direction);
            },
            filtersystem: function() {
				if (!TR.init.user.isAdmin) {
					this.filterBy( function(r) {
						return r.data.userId ? true : false;
					});
				}
			},
			listeners: {
				load: function(s) {
					s.isloaded = !s.isloaded ? true : false;
					
					s.sortStore();
					s.each(function(r) {
						r.data.lastUpdated = r.data.lastUpdated.substr(0,16);
						r.data.icon = '<img src="' + TR.conf.finals.ajax.path_images + 'favorite.png" />';
						r.commit();
					});
				}
            }
		}),
		aggregateFavorite: Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'lastUpdated'],
			proxy: {
				type: 'ajax',
				reader: {
					type: 'json',
					root: 'tabularReports'
				}
			},
			isloaded: false,
			pageSize: 10,
            currentPage: 1,
			defaultUrl: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_getall,
			loadStore: function(url) {
				this.proxy.url = url || this.defaultUrl;
				this.load({
					params: {
						pageSize: this.pageSize,
						currentPage: this.currentPage
					}
				});
			},
			loadFn: function(fn) {
				if (this.isLoaded) {
					fn.call();
				}
				else {
					this.load(fn);
				}
			},
			sorting: {
                field: 'name',
                direction: 'ASC'
            },
            sortStore: function() {
                this.sort(this.sorting.field, this.sorting.direction);
            },
            filtersystem: function() {
				if (!TR.init.user.isAdmin) {
					this.filterBy( function(r) {
						return r.data.userId ? true : false;
					});
				}
			},
			listeners: {
                load: function(s) {
					s.isloaded = !s.isloaded ? true : false;
					
                    s.sortStore();
                    s.each(function(r) {
                        r.data.lastUpdated = r.data.lastUpdated.substr(0,16);
                        r.data.icon = '<img src="' + TR.conf.finals.ajax.path_images + 'favorite.png" />';
                        r.commit();
                    });
                }
            }
		}),
		periodtype: Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: TR.conf.period.periodtypes
		}),
        fixedperiod: {
            available: Ext.create('Ext.data.Store', {
                fields: ['id', 'name', 'index'],
                data: [],
                setIndex: function(periods) {
					for (var i = 0; i < periods.length; i++) {
						periods[i].index = i;
					}
				},
                sortStore: function() {
					this.sort('index', 'ASC');
				}
            }),
            selected: Ext.create('Ext.data.Store', {
                fields: ['id', 'name'],
                data: []
            })
        },
		dateRange: Ext.create('Ext.data.Store', {
			fields: ['startDate', 'endDate'],
			data: []
		}),
		aggregateDataelement: Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: []
		}),
		groupbyDataelement: Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: []
		})
	}
    
    TR.state = {
        currentPage: 1,
		total: 1,
		totalRecords: 0,
		isFilter:false,
		orderByOrgunitAsc: true,
		orderByExecutionDateByAsc: true,
		orgunitIds: [],
		generateReport: function( type, isSorted ) {
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
			{
				this.caseBasedReport.generate( type, isSorted );
			}
			else
			{
				this.aggregateReport.generate( type );
			}
		},
		filterReport: function() {
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
			{
				this.caseBasedReport.generate();
			}
		},
		getParams: function(isSorted){
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
			{
				return this.caseBasedReport.getParams(isSorted);
			}
			return this.aggregateReport.getParams();
		},
		getURLParams: function( type, isSorted ){
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true'){
				this.caseBasedReport.getURLParams( type, isSorted );
			}
			else{
				this.aggregateReport.getURLParams( type );
			}
		},
		
		caseBasedReport: {
			generate: function( type, isSorted ) {
				// Validation
				if( !TR.state.caseBasedReport.validation.objects() )
				{
					return;
				}
				// Get url
				var url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.generatetabularreport_get;
				// Export to XLS 
				if( type)
				{
					TR.state.caseBasedReport.getURLParams();
					var completedEvent='';
					if( Ext.getCmp('completedEventsOpt').getValue() == true)
					{
						completedEvent = "&completedEventsOpt=true";
					}
  				    var exportForm = document.getElementById('exportForm');
					exportForm.action = url + "?type=" + type + completedEvent;
					exportForm.submit();
				}
				// Show report on grid
				else
				{
					TR.util.mask.showMask(TR.cmp.region.center, TR.i18n.loading);
					Ext.Ajax.request({
						url: url,
						method: "POST",
						scope: this,
						params: this.getParams(isSorted),
						success: function(r) {
							var json = Ext.JSON.decode(r.responseText);
							if(json.message!=""){
								TR.util.notification.error(TR.i18n.error, json.message);
							}
							else{
								if( isSorted ){
									TR.store.datatable.loadData(TR.value.values,false);
								}
								else{
									TR.state.total = json.total;
									TR.state.totalRecords = json.totalRecords
									TR.value.columns = json.columns;
									TR.value.values = json.items;
									// Get fields
									var fields = [];
									fields[0] = 'id';
									for( var index=1; index < TR.value.columns.length; index++ )
									{
										fields[index] = 'col' + index;
									}
									TR.value.fields = fields;
									
									// Set data for grid
									TR.store.getDataTableStore();
									TR.datatable.getDataTable();
								}
								TR.datatable.setPagingToolbarStatus();
							}
							TR.util.mask.hideMask();
						}
					});
				}
				TR.util.notification.ok();
			},
			getParams: function(isSorted) {
				var p = {};
				p.startDate = TR.cmp.settings.startDate.rawValue;
				p.endDate = TR.cmp.settings.endDate.rawValue;
				p.facilityLB = TR.cmp.settings.facilityLB.getValue();
				p.level = Ext.getCmp('levelCombobox').getValue();
				
				// orders
				p.orderByOrgunitAsc = TR.state.orderByOrgunitAsc;
				p.orderByExecutionDateByAsc= TR.state.orderByExecutionDateByAsc;
				
				p.programStageId = TR.cmp.params.programStage.getValue();
				p.currentPage = TR.state.currentPage;
				
				// organisation unit
				p.orgunitIds = TR.state.orgunitIds;
				p.userOrganisationUnit = Ext.getCmp('userOrgunit').getValue();
				p.userOrganisationUnitChildren = Ext.getCmp('userOrgunitChildren').getValue();
				if( Ext.getCmp('completedEventsOpt').getValue() == true )
				{
					p.useCompletedEvents = Ext.getCmp('completedEventsOpt').getValue();
				}
				
				// Get searching values
				p.searchingValues = [];
				
				// Patient properties
				
				TR.cmp.params.patientProperty.selected.store.each( function(r) {
					var propId = r.data.id;
					var valueType = r.data.valueType;
					var length = Ext.getCmp('filterPropPanel_' + propId).items.length/4;
					var hidden = TR.state.caseBasedReport.isColHidden(propId);
					
					for(var idx=0;idx<length;idx++)
					{
						var id = propId + '_' + idx;
						var filterField = Ext.getCmp('filter_' + id);
						var filterValue = "";
						if( filterField.xtype == 'combobox' )
						{
							var values = Ext.getCmp('filter_' + id).getValue();
							for( var i in values ){
								filterValue += values[i] + ";";
							}
							filterValue = filterValue.substring(0,filterValue.length - 1  );
						}
						else{
							filterValue = filterField.rawValue;
						}
						
						var filter = propId + '_' + hidden 
						if( filterValue!=null && filterValue!=''){
							var filterOpt = Ext.getCmp('filter_opt_' + id).rawValue;
							filter += '_' + filterOpt + ' ';
							if( filterOpt == 'IN' )
							{
								var filterValues = filterValue.split(";");
								filter +="(";
								for(var i=0;i<filterValues.length;i++)
								{
									filter += "'"+ filterValues[i] +"',";
								}
								filter = filter.substr(0,filter.length - 1) + ")";
							}
							else
							{
								filter += "'" + filterValue + "'";
							}
						}
						p.searchingValues.push( filter );
					}
				});
				
				TR.cmp.params.dataelement.selected.store.each( function(r) {
					var valueType = r.data.valueType;
					var deId = r.data.id;
					var length = Ext.getCmp('filterPanel_' + deId).items.length/4;
					var hidden = TR.state.caseBasedReport.isColHidden(deId);
					
					for(var idx=0;idx<length;idx++)
					{
						var id = deId + '_' + idx;
						
						var filterOpt = Ext.getCmp('filter_opt_' + id).rawValue;						
						var filterValue = Ext.getCmp('filter_' + id).rawValue;
						var filter = deId + '_' + hidden + '_';
						
						if( filterValue!='' ){
							filterValue = filterValue.toLowerCase();
							filter += filterOpt + ' ';
							if( filterOpt == 'IN' )
							{
								var filterValues = filterValue.split(";");
								filter +="(";
								for(var i=0;i<filterValues.length;i++)
								{
									filter += "'"+ filterValues[i] +"',";
								}
								filter = filter.substr(0,filter.length - 1) + ")";
							}
							else
							{
								filter += "'" + filterValue + "'";
							}
						}
						p.searchingValues.push( filter );
					}
				});
					
				return p;
			},
			getURLParams: function( isSorted ) {
				
				document.getElementById('startDate').value = TR.cmp.settings.startDate.rawValue;
				document.getElementById('endDate').value = TR.cmp.settings.endDate.rawValue;
				document.getElementById('facilityLB').value =  TR.cmp.settings.facilityLB.getValue();
				document.getElementById('level').value = Ext.getCmp('levelCombobox').getValue();
				document.getElementById('orderByOrgunitAsc').value = this.orderByOrgunitAsc;
				document.getElementById('orderByExecutionDateByAsc').value = this.orderByExecutionDateByAsc;
				document.getElementById('programStageId').value = TR.cmp.params.programStage.getValue();				
				document.getElementById('userOrganisationUnit').value = Ext.getCmp('userOrgunit').getValue();
				document.getElementById('userOrganisationUnitChildren').value = Ext.getCmp('userOrgunitChildren').getValue();

				// orgunits
				var orgunitIdList = document.getElementById('orgunitIds');
				TR.util.list.clearList(orgunitIdList);
				for( var i in TR.state.orgunitIds){
					TR.util.list.addOptionToList(orgunitIdList, TR.state.orgunitIds[i], '');
				}
				
				// Get searching values
				
				var searchingValues = document.getElementById('searchingValues');
				TR.util.list.clearList(searchingValues);				
				
				// Patient properties
				
				TR.cmp.params.patientProperty.selected.store.each( function(r) {
					var propId = r.data.id;
					var valueType = r.data.valueType;
					var length = Ext.getCmp('filterPropPanel_' + propId).items.length/4;
					var hidden = TR.state.caseBasedReport.isColHidden(propId);
					
					for(var idx=0;idx<length;idx++)
					{
						var id = propId + '_' + idx;
						var filterValue = Ext.getCmp('filter_' + id).rawValue;
						var filter = propId + '_' + hidden 
						if( filterValue!='' && filterValue!=TR.i18n.please_select ){
							var filterOpt = Ext.getCmp('filter_opt_' + id).rawValue;
							filter += '_' + filterOpt + ' ';
							if( filterOpt == 'IN' )
							{
								var filterValues = filterValue.split(";");
								filter +="(";
								for(var i=0;i<filterValues.length;i++)
								{
									filter += "'"+ filterValues[i] +"',";
								}
								filter = filter.substr(0,filter.length - 1) + ")";
							}
							else
							{
								filter += "'" + filterValue + "'";
							}
						}
						TR.util.list.addOptionToList(searchingValues, filter, '');
					}
				});
				
				TR.cmp.params.dataelement.selected.store.each( function(r) {
					var valueType = r.data.valueType;
					var deId = r.data.id;
					var length = Ext.getCmp('filterPanel_' + deId).items.length/4;
					var hidden = TR.state.caseBasedReport.isColHidden(deId);
					
					for(var idx=0;idx<length;idx++)
					{
						var id = deId + '_' + idx;
						var filterField = Ext.getCmp('filter_' + id);
						var filterOpt = Ext.getCmp('filter_opt_' + id).rawValue;
						var filterValue = Ext.getCmp('filter_' + id).rawValue;
						
						var filter = deId + '_' + hidden + '_';
						if( filterValue!=''){
							filterValue = filterValue.toLowerCase();
							var filterOpt = Ext.getCmp('filter_opt_' + id).rawValue;
							filter += filterOpt + ' ';
						
							if( filterOpt == 'IN' )
							{
								var filterValues = filterValue.split(";");
								filter +="(";
								for(var i=0;i<filterValues.length;i++)
								{
									filter += "'"+ filterValues[i] +"',";
								}
								filter = filter.substr(0,filter.length - 1) + ")";
							}
							else
							{
								filter += "'" + filterValue + "'";
							}
						}
						TR.util.list.addOptionToList(searchingValues, filter, '');
					}
				});
			},
			isColHidden: function( colname ) {
				var grid = TR.datatable.datatable;
				if( grid != null ){
					var cols = grid.columns;
					for (var i = 0; i < cols.length; i++) {
						if (cols[i].name == colname) {
							return(cols[i].hidden==undefined)? false : cols[i].hidden;
						}
					}
				} 
				return false;
			},
			validation: {
				objects: function() {
					
					if (TR.cmp.settings.program.getValue() == null) {
						TR.util.notification.error(TR.i18n.et_no_programs, TR.i18n.et_no_programs);
						return false;
					}
					
					if( !TR.cmp.settings.startDate.isValid() )
					{
						var message = TR.i18n.start_date + " " + TR.i18n.is_not_valid;
						TR.util.notification.error( message, message);
						return false;
					}
					
					if( !TR.cmp.settings.endDate.isValid() )
					{
						var message = TR.i18n.end_date + " " + TR.i18n.is_not_valid;
						TR.util.notification.error( message, message);
						return false;
					}
				
					if (TR.state.orgunitIds.length == 0 
						&& TR.cmp.aggregateFavorite.userorganisationunit.getValue() == 'false'
						&& TR.cmp.aggregateFavorite.userorganisationunitchildren.getValue() == 'false' ) {
						TR.util.notification.error(TR.i18n.et_no_orgunits, TR.i18n.em_no_orgunits);
						return false;
					}
					
					if (Ext.getCmp('programStageCombobox').getValue() == '') {
						TR.util.notification.error(TR.i18n.em_no_program_stage, TR.i18n.em_no_program_stage);
						return false;
					}
					
					if(TR.cmp.params.dataelement.selected.store.data.items.length == 0 )
					{
						TR.util.notification.error(TR.i18n.em_no_data_element, TR.i18n.em_no_data_element);
						return false;
					}
					return true;
				},
				response: function(r) {
					if (!r.responseText) {
						TR.util.mask.hideMask();
						TR.util.notification.error(TR.i18n.et_invalid_uid, TR.i18n.em_invalid_uid);
						return false;
					}
					return true;
				},
				value: function() {
					if (!TR.value.values.length) {
						TR.util.mask.hideMask();
						TR.util.notification.error(TR.i18n.et_no_data, TR.i18n.em_no_data);
						return false;
					}
					return true;
				}
			}
		},
		
		aggregateReport: {
			generate: function( type ) {
				// Validation
				if( !TR.state.aggregateReport.validation.objects() )
				{
					return;
				}
				// Get url
				var url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.generateaggregatereport_get;
				// Export to XLS 
				if( type)
				{
					TR.state.aggregateReport.getURLParams();
  				    var completedEvent='';
					if( Ext.getCmp('completedEventsOpt').getValue() == true )
					{
						completedEvent = "&completedEventsOpt=true";
					}
					
					var displayTotals='&displayTotals=false';
					if( Ext.getCmp('displayTotalsOpt').getValue() == true )
					{
						displayTotals = "&displayTotals=true";
					}
					
  				    var exportForm = document.getElementById('exportForm');
					exportForm.action = url + "?type=" + type + completedEvent + "&" + displayTotals;
					exportForm.submit();
				}
				// Show report on grid
				else
				{
					TR.util.mask.showMask(TR.cmp.region.center, TR.i18n.loading);
					Ext.Ajax.request({
						url: url,
						method: "POST",
						scope: this,
						params: this.getParams(),
						success: function(r) {
							var json = Ext.JSON.decode(r.responseText);
							if(json.message!=""){
								TR.util.notification.error(TR.i18n.error, json.message);
							}
							else{
								TR.value.title = json.title;
								TR.value.columns = json.columns;
								TR.value.values = json.items;
								
								// Get fields
								
								var fields = [];
								for( var index=0; index < TR.value.columns.length; index++ )
								{
									fields[index] = 'col' + index;
								}
								TR.value.fields = fields;
								
								if(TR.cmp.params.dataelement.selected.store.data.length>0){
									Ext.getCmp('btnClean').enable();
								}
								else{
									Ext.getCmp('btnClean').disable();
								}
								
								// Set data for grid
								
								TR.store.getDataTableStore();
								TR.datatable.getDataTable();
								TR.datatable.hidePagingBar();
							}
							TR.util.mask.hideMask();
						}
					});
				}
				TR.util.notification.ok();
			},
			getPosition: function() {
				// 1 - Rows
				// 2 - Columns
				// 3 - Filter
				var positionOrgunit = Ext.getCmp('positionOrgunitCbx').getValue();
				var positionPeriod = Ext.getCmp('positionPeriodCbx').getValue();
				var positionData = Ext.getCmp('positionDataCbx').getValue();
				
				// 1
				if( positionOrgunit==1 && positionPeriod==2 && positionData==3 )
				{
					return TR.conf.reportPosition.POSITION_ROW_ORGUNIT_COLUMN_PERIOD;
				}
				// 2
				if( positionOrgunit==2 && positionPeriod==1 && positionData==3 )
				{
					return TR.conf.reportPosition.POSITION_ROW_PERIOD_COLUMN_ORGUNIT;
				}
				// 3
				if( positionOrgunit==1 && positionPeriod==1 && positionData==3 )
				{
					return TR.conf.reportPosition.POSITION_ROW_ORGUNIT_ROW_PERIOD;
				}
				// 4
				if( positionOrgunit==3 && positionPeriod==1 && positionData==3 )
				{
					return TR.conf.reportPosition.POSITION_ROW_PERIOD;
				}
				// 5
				if( positionOrgunit==1 && positionPeriod==3 && positionData==3 )
				{
					return TR.conf.reportPosition.POSITION_ROW_ORGUNIT;
				}
				//6
				if( positionOrgunit==3 && positionPeriod==1 && positionData==2 )
				{
					return TR.conf.reportPosition.POSITION_ROW_PERIOD_COLUMN_DATA;
				}
				//7
				if( positionOrgunit==1 && positionPeriod==3 && positionData==2 )
				{
					return TR.conf.reportPosition.POSITION_ROW_ORGUNIT_COLUMN_DATA;
				}
				// 8
				if( positionOrgunit==3 && positionPeriod==3 && positionData==1 )
				{
					return TR.conf.reportPosition.POSITION_ROW_DATA;
				}
				// 9
				if( positionOrgunit==3 && positionPeriod==2 && positionData==1 )
				{
					return TR.conf.reportPosition.POSITION_ROW_DATA_COLUMN_PERIOD;
				}
				// 10
				if( positionOrgunit==2 && positionPeriod==3 && positionData==1 )
				{
					return TR.conf.reportPosition.POSITION_ROW_DATA_COLUMN_ORGUNIT;
				}
				return '';
			},
			getParams: function() {
				var p = {};
				p.programStageId = TR.cmp.params.programStage.getValue();
				p.aggregateType = Ext.getCmp('aggregateType').getValue().aggregateType;
				if( p.aggregateType != 'count')
				{
					p.deSum = Ext.getCmp('deSumCbx').getValue().split('_')[1];
				}				
				
				// orgunits
				
				p.orgunitIds = TR.state.orgunitIds;
				p.userOrganisationUnit = Ext.getCmp('userOrgunit').getValue();
				p.userOrganisationUnitChildren = Ext.getCmp('userOrgunitChildren').getValue();
				
				p.limitRecords = Ext.getCmp('limitOption').getValue();
				
				var position = TR.state.aggregateReport.getPosition();
				if( Ext.getCmp('dataElementGroupByCbx').getValue() != null ){
					p.deGroupBy = Ext.getCmp('dataElementGroupByCbx').getValue().split('_')[1];
				}
				
				// Filter values for data-elements
				
				p.deFilters = [];
				TR.cmp.params.dataelement.selected.store.each( function(r) {
					var valueType = r.data.valueType;
					var deId = r.data.id;
					var length = Ext.getCmp('filterPanel_' + deId).items.length/4;
					
					for(var idx=0;idx<length;idx++)
					{
						var id = deId + '_' + idx;
						var filterOpt = Ext.getCmp('filter_opt_' + id).rawValue;
						var filterValue = Ext.getCmp('filter_' + id).rawValue;
						var filter = deId.split('_')[1] + "_" + filterOpt + '_';
					
						if( filterValue!=TR.i18n.please_select)
						{
							if( valueType == 'list' )
							{
								var filterValues = filterValue.split(';');
								filter +="(";
								for(var i=0;i<filterValues.length;i++)
								{
									filter += "'"+ filterValues[i] +"',";
								}
								filter = filter.substr(0,filter.length - 1) + ")";
							}
							else 
							{
								filter += "'" + filterValue + "'";
							}
							p.deFilters.push( filter );
						}
					}
				});
				
				// Period range
				
				p.startDates = [];
				p.endDates = [];
				TR.store.dateRange.data.each( function(r) {
					p.startDates.push(r.data.startDate);
					p.endDates.push(r.data.endDate);
				});
				
				// Fixed periods
				
				p.fixedPeriods = [];
				TR.cmp.params.fixedperiod.selected.store.each( function(r) {
					p.fixedPeriods.push( r.data.id );
				});
				
				// Relative periods
				
				var relativePeriodList = TR.cmp.params.relativeperiod.checkbox;
				p.relativePeriods = [];
				Ext.Array.each(relativePeriodList, function(item) {
					if(item.getValue() && !item.hidden){
						p.relativePeriods.push(item.paramName);
					}
				});
				
				p.facilityLB = TR.cmp.settings.facilityLB.getValue();
				p.position = position;
				if( Ext.getCmp('completedEventsOpt').getValue()== true )
				{
					p.useCompletedEvents = Ext.getCmp('completedEventsOpt').getValue();
				}
				
				if( Ext.getCmp('displayTotalsOpt').getValue()== true )
				{
					p.displayTotals = Ext.getCmp('displayTotalsOpt').getValue();
				}
				else
				{
					p.displayTotals = 'false';
				}
				
				return p;
			},
			getURLParams: function() {
				document.getElementById('programStageId').value = TR.cmp.params.programStage.getValue();
				document.getElementById('aggregateType').value = Ext.getCmp('aggregateType').getValue().aggregateType;
				document.getElementById('userOrganisationUnit').value = Ext.getCmp('userOrgunit').getValue();
				document.getElementById('userOrganisationUnitChildren').value = Ext.getCmp('userOrgunitChildren').getValue();
				document.getElementById('facilityLB').value = TR.cmp.settings.facilityLB.getValue();
				document.getElementById('position').value = TR.state.aggregateReport.getPosition();
				
				if( Ext.getCmp('dataElementGroupByCbx').getValue() != null 
					&& Ext.getCmp('dataElementGroupByCbx').getValue() != '' ){
					document.getElementById('deGroupBy').value = Ext.getCmp('dataElementGroupByCbx').getValue().split('_')[1];
				}
				else{
					document.getElementById('deGroupBy').value = "";
				}
				if( Ext.getCmp('limitOption').getValue() != null 
					&& Ext.getCmp('limitOption').getValue() != '' ){
					document.getElementById('limitRecords').value = Ext.getCmp('limitOption').getValue();
				}
				else{
					document.getElementById('limitRecords').value = "";
				}
				
				if( Ext.getCmp('aggregateType').getValue().aggregateType != 'count'){
					document.getElementById('deSum').value = Ext.getCmp('deSumCbx').getValue();
				}
				else{
					document.getElementById('deSum').value = '';
				}
				
				// orgunits
				
				var orgunitIdList = document.getElementById('orgunitIds');
				TR.util.list.clearList(orgunitIdList);
				for( var i in TR.state.orgunitIds){
					TR.util.list.addOptionToList(orgunitIdList, TR.state.orgunitIds[i], '');
				}
				
				// Filter values
				
				var deFiltersList = document.getElementById('deFilters');
				TR.util.list.clearList(deFiltersList);
				TR.cmp.params.dataelement.selected.store.each( function(r) {
					var valueType = r.data.valueType;
					var deId = r.data.id;
					var length = Ext.getCmp('filterPanel_' + deId).items.length/4;
					
					for(var idx=0;idx<length;idx++)
					{
						var id = deId + '_' + idx;
						var filterOpt = Ext.getCmp('filter_opt_' + id).rawValue;						
						var filterValue = Ext.getCmp('filter_' + id).rawValue;
						
						var filter = deId.split('_')[1] + "_" + filterOpt + '_';
					
						if(filterValue!=TR.i18n.please_select)
						{
							if( valueType == 'list' )
							{
								var filterValues = filterValue.split(";");
								filter +="(";
								for(var i=0;i<filterValues.length;i++)
								{
									filter += "'"+ filterValues[i] +"',";
								}
								filter = filter.substr(0,filter.length - 1) + ")";
							}
							else 
							{
								filter += "'" + filterValue + "'";
							}
							TR.util.list.addOptionToList(deFiltersList, filter, '');
						}
					}
				});
				
				// Period range
				
				var startDateList = document.getElementById('startDates');
				var endDateList = document.getElementById('endDates');
				TR.util.list.clearList(startDateList);
				TR.util.list.clearList(endDateList);
				TR.store.dateRange.data.each( function(r) {
					TR.util.list.addOptionToList(startDateList, r.data.startDate, '');
					TR.util.list.addOptionToList(endDateList, r.data.endDate, '');
				});
				
				// Fixed periods
				
				var fixedPeriodList = document.getElementById('fixedPeriods');
				TR.util.list.clearList(fixedPeriodList);
				TR.cmp.params.fixedperiod.selected.store.each( function(r) {
					TR.util.list.addOptionToList(fixedPeriodList, r.data.id, '');
				});
				
				// Relative periods
				
				var relativePeriodSelect = document.getElementById('relativePeriods');
				TR.util.list.clearList(relativePeriodSelect);
				var relativePeriodList = TR.cmp.params.relativeperiod.checkbox;
				Ext.Array.each(relativePeriodList, function(item) {
					if(item.getValue() && !item.hidden){
						TR.util.list.addOptionToList(relativePeriodSelect, item.paramName, '');
					}
				});
			},
			validation: {
				objects: function() {
					if (TR.cmp.settings.program.getValue() == null) {
						TR.util.notification.error(TR.i18n.et_no_programs, TR.i18n.et_no_programs);
						return false;
					}
					
					if (Ext.getCmp('programStageCombobox').getValue() == '') {
						TR.util.notification.error(TR.i18n.em_no_program_stage, TR.i18n.em_no_program_stage);
						return false;
					}
					
					if( TR.cmp.settings.startDate.rawValue != "" 
						&& !TR.cmp.settings.startDate.isValid() )
					{
						var message = TR.i18n.start_date + " " + TR.i18n.is_not_valid;
						TR.util.notification.error( message, message);
						return false;
					}
					
					if( TR.cmp.settings.endDate.rawValue != "" 
						&& !TR.cmp.settings.endDate.isValid() )
					{
						var message = TR.i18n.end_date + " " + TR.i18n.is_not_valid;
						TR.util.notification.error( message, message);
						return false;
					}
				
					if (TR.state.orgunitIds.length == 0 
						&& TR.cmp.aggregateFavorite.userorganisationunit.getValue() == 'false'
						&& TR.cmp.aggregateFavorite.userorganisationunitchildren.getValue() == 'false' ) {
						TR.util.notification.error(TR.i18n.et_no_orgunits, TR.i18n.em_no_orgunits);
						return false;
					}
					
					if( TR.store.dateRange.data.length==0
						&& TR.cmp.params.fixedperiod.selected.store.data.items.length == 0 )
					{
						var relativePeriodList = TR.cmp.params.relativeperiod.checkbox;
						var flag = false;
						Ext.Array.each(relativePeriodList, function(item) {
							if(item.getValue()){
								flag = true;
							}
						});
						
						if( !flag )
						{
							TR.util.notification.error(TR.i18n.em_no_period, TR.i18n.em_no_period);
							return false;
						}
					}
					
					var isValid = true;
					TR.cmp.params.dataelement.selected.store.each( function(r) {
						var deId = r.data.id;
						var length = Ext.getCmp('filterPanel_' + deId).items.length/4;
						for(var idx=0;idx<length;idx++)
						{
							var id = deId + '_' + idx;
							var filterValue = Ext.getCmp('filter_' + id).rawValue;
							if(filterValue==null || filterValue==TR.i18n.please_select)
							{
								filterValue = Ext.getCmp('filter_' + id).getValue();
							}
							if( filterValue == null || ( filterValue == '' && filterValue != 0 )
							|| filterValue==TR.i18n.please_select ){
								isValid = false;
							}
						}
					});
					if( !isValid){
						TR.util.notification.error(TR.i18n.fill_filter_values_for_all_selected_data_elements, TR.i18n.fill_filter_values_for_all_selected_data_elements);
						return false;		
					}
					
					var periodInt = 0;
					if( TR.cmp.settings.startDate.rawValue!="" 
						&& TR.cmp.settings.endDate.rawValue!="") 
					{
						periodInt++;
					}
					if( TR.cmp.params.fixedperiod.selected.store.data.items.length > 0 )
					{
						periodInt++;
					}
					var relativePeriodList = TR.cmp.params.relativeperiod.checkbox;
					Ext.Array.each(relativePeriodList, function(item) {
						if(item.getValue()){
							periodInt++;
						}
					});
					
					var position = TR.state.aggregateReport.getPosition();
					if( position==''){
						var o = TR.cmp.settings.positionOrgunit.value;
						var p = TR.cmp.settings.positionPeriod.value;
						var d = TR.cmp.settings.positionData.value;

						if( o!='1' && p!='1' && d!='1')
						{
							TR.util.notification.error(TR.i18n.please_select_one_position_for_row, TR.i18n.please_select_one_position_for_row);
							return false;
						}
						if( o!='3' && p!='3' && d!='3')
						{
							TR.util.notification.error(TR.i18n.please_select_one_position_for_filter, TR.i18n.please_select_one_position_for_filter);
							return false;
						}
						else {
							TR.util.notification.error(TR.i18n.invalid_position, TR.i18n.invalid_position);
							return false;
						}
					}
					
					if( Ext.getCmp('aggregateType').getValue().aggregateType != 'count'
						&& ( Ext.getCmp('deSumCbx').getValue() == null || Ext.getCmp('deSumCbx').getValue=='')){
						TR.util.notification.error(TR.i18n.select_a_dataelement_for_sum_avg_operator, TR.i18n.select_a_dataelement_for_sum_avg_operator );
						return false;
					}
				
					// Check orgunit by period
					if( Ext.getCmp('positionOrgunitCbx').getValue() == 3 
						&& ( TR.state.orgunitIds.length > 1 
						|| Ext.getCmp('userOrgunitChildren').getValue() ))
					{
						TR.util.notification.error(TR.i18n.multiple_orgunits_selected_as_filter, TR.i18n.multiple_orgunits_selected_as_filter);
					}
				
					// Check filter by period
					if( Ext.getCmp('positionPeriodCbx').getValue() == 3 )
					{
						var noPeriod = TR.store.dateRange.data.length + TR.cmp.params.fixedperiod.selected.store.data.length;
						
						var relativePeriodList = TR.cmp.params.relativeperiod.checkbox;
						Ext.Array.each(relativePeriodList, function(item) {
							if(item.getValue() && !item.hidden 
								&&( item.paramName=='last3Months' 
								  || item.paramName=='last12Months' 
								  || item.paramName=='last4Quarters' 
								  || item.paramName=='last2SixMonths' 
								  || item.paramName=='last5Years' ) ){
								noPeriod += 2;
							}
						});
						
						if( noPeriod > 1 ){
							TR.util.notification.error(TR.i18n.multiple_periods_selected_as_filter, TR.i18n.multiple_periods_selected_as_filter);
						}
					}
				
					return true;
				},
				response: function(r) {
					if (!r.responseText) {
						TR.util.mask.hideMask();
						TR.util.notification.error(TR.i18n.et_invalid_uid, TR.i18n.em_invalid_uid);
						return false;
					}
					return true;
				},
				value: function() {
					if (!TR.value.values.length) {
						TR.util.mask.hideMask();
						TR.util.notification.error(TR.i18n.et_no_data, TR.i18n.em_no_data);
						return false;
					}
					return true;
				}
			}
		}
   };
    
    TR.value = {
		title: '',
		columns: [],
		fields: [],
		values: [],
		covertValueType: function( type )
		{
			type = type.toLowerCase();
			if( type == 'date' )
			{
				return type;
			}
			if(type == 'number')
			{
				return 'float';
			}
			if( type == 'int' || type == 'positiveNumber'  || type == 'negativeNumber' )
			{
				return 'numeric';
			}
			if( type == 'bool' || type == 'yes/no' || type == 'trueOnly' )
			{
				return 'boolean';
			}
			if( type == 'combo' || type == 'username' )
			{
				return 'list';
			}
			return 'string';
		},
		covertXType: function( type )
		{
			if( type == 'date' )
			{
				return 'datefield';
			}
			if( type == 'number' || type == 'int' || type == 'positiveNumber'  || type == 'negativeNumber' )
			{
				return 'numberfield';
			}
			if( type == 'combo' || type == 'list' || type == 'username' || type == 'trueOnly' || type=='bool' )
			{
				return 'combobox';
			}
			return 'textfield';
		},
	};
      
    TR.datatable = {
        datatable: null,
		getDataTable: function() {
			var cols = this.createColTable();

			// title
			var title = TR.cmp.settings.program.rawValue + " - " + TR.cmp.params.programStage.rawValue + " " + TR.i18n.report;
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='false')
			{
				title = TR.value.title;
			}
			
			// grid
			this.datatable = Ext.create('Ext.grid.Panel', {
                height: TR.util.viewport.getSize().y - 58,
				id: 'gridTable',
				columns: cols,
				scroll: 'both',
				title: title,
				selType: 'cellmodel',
				bbar: [
					{
						xtype: 'button',
						icon: 'images/arrowleftdouble.png',
						id:'firstPageBtn',
						width: 22,
						handler: function() {
							TR.exe.paging(1);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowleft.png',
						id:'previousPageBtn',
						width: 22,
						handler: function() {
							TR.exe.paging( eval(TR.cmp.settings.currentPage.rawValue) - 1 );
						}
					},
					{
						xtype: 'label',
						id:'separate1Lbl',
						text: '|'
					},
					{
						xtype: 'label',
						id:'pageLbl',
						text: TR.i18n.page
					},
					{
						xtype: 'textfield',
						cls: 'tr-textfield-alt1',
						id:'currentPage',
						value: TR.state.currentPage,
						listeners: {
							added: function() {
								TR.cmp.settings.currentPage = this;
							}
						},
					},
					{
						xtype: 'label',
						id:'totalPageLbl',
						text: ' of ' + TR.state.total + ' | '
					},
					{
						xtype: 'button',
						icon: 'images/arrowright.png',
						id:'nextPageBtn',
						handler: function() {
							TR.exe.paging( eval(TR.cmp.settings.currentPage.rawValue) + 1 );
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowrightdouble.png',
						id:'lastPageBtn',
						handler: function() {
							TR.exe.paging( TR.state.total );
						}
					},
					{
						xtype: 'label',
						id:'separate2Lbl',
						text: '|'
					},
					{
						xtype: 'button',
						id:'refreshBtn',
						icon: 'images/refresh.png',
						handler: function() {
							TR.exe.paging( TR.cmp.settings.currentPage.rawValue );
						}
					},
					'->',
					{
						xtype: 'label',
						id: 'totalEventLbl',
						style: 'margin-right:18px;',
						text: TR.state.totalRecords + ' ' + TR.i18n.events
					},
				], 
				store: TR.store.datatable
			});
			
			Ext.override(Ext.grid.header.Container, { 
				sortAscText: TR.i18n.asc,
				sortDescText: TR.i18n.desc, 
				columnsText: TR.i18n.show_hide_columns });

			TR.cmp.region.center.removeAll(true);
			TR.cmp.region.center.add(this.datatable);		
          	
            return this.datatable;
            
        },
		createColTable:function(){
			var cols = [];
				
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
			{
				var orgUnitCols = ( TR.init.system.maxLevels + 1 - TR.cmp.settings.level.getValue() );
				var index = 0;
				
				// id of event
				
				cols[index] = {
					header: TR.value.columns[index].name, 
					dataIndex: 'id',
					height: TR.conf.layout.east_gridcolumn_height,
					sortable: false,
					draggable: false,
					hidden: true,
					hideable: false,
					menuDisabled: true,
					locked: true
				};
				
				// report date
				
				cols[++index] = {
					header: TR.value.columns[index].name, 
					dataIndex: 'col' + index,
					height: TR.conf.layout.east_gridcolumn_height,
					sortable: false,
					draggable: false,
					hideable: false,
					locked: true,
					menuDisabled: true
				};
							
				// Org unit level columns
				
				for( var i = 0; i <orgUnitCols; i++ )
				{
					cols[++index] = {
						header: TR.value.columns[index].name, 
						dataIndex: 'col' + index,
						height: TR.conf.layout.east_gridcolumn_height,
						name: 'meta_' + index,
						sortable: false,
						draggable: false,
						hideable: false,
						menuDisabled: true
					}
				}
				
				// Patient properties columns
			
				TR.cmp.params.patientProperty.selected.store.each( function(r) {
					cols[++index] = {
						header: r.data.name, 
						dataIndex: 'col' + index,
						height: TR.conf.layout.east_gridcolumn_height,
						name: r.data.id,
						sortable: false,
						draggable: false
					}
				});
				
				// Data element columns
				
				TR.cmp.params.dataelement.selected.store.each( function(r) {
					cols[++index] = TR.datatable.createColumn( r.data.valueType, r.data.id, r.data.compulsory, r.data.name, index );
				});
			
			}
			else
			{
				for(var i in TR.value.columns)
				{
					cols[i] = this.createColumn( "textfield","id" + i, false, TR.value.columns[i].name, i );
				}
			}
			return cols;
		},
		createColumn: function( type, id, compulsory, colname, index ){
			var objectType = id.split('_')[0];
			var objectId = id.split('_')[1];
			
			var params = {};
			params.header = colname;
			params.dataIndex = 'col' + index;
			params.name = id;
			params.hidden = eval(TR.value.columns[index].hidden );
			params.menuFilterText = TR.value.filter;
			params.sortable = false;
			params.draggable = true;
			
			if(Ext.getCmp('reportTypeGroup').getValue().reportType=='false')
			{
				params.menuDisabled = true;
				params.draggable = false;
			}
			params.isEditAllowed = true;
			params.compulsory = compulsory;
			
			type = type.toLowerCase();
			if( type == 'date' )
			{
				params.renderer = Ext.util.Format.dateRenderer( TR.i18n.format_date );
			}
			return params;
		},
        setPagingToolbarStatus: function() {
			TR.datatable.showPagingBar();
			Ext.getCmp('currentPage').enable();
			Ext.getCmp('totalEventLbl').setText( TR.state.totalRecords + ' ' + TR.i18n.events );
			Ext.getCmp('totalPageLbl').setText( ' of ' + TR.state.total + ' | ' );
			if( TR.state.totalRecords== 0 )
			{
				Ext.getCmp('currentPage').setValue('');
				Ext.getCmp('currentPage').setValue('');
				Ext.getCmp('currentPage').disable();
				Ext.getCmp('firstPageBtn').disable();
				Ext.getCmp('previousPageBtn').disable();
				Ext.getCmp('nextPageBtn').disable();
				Ext.getCmp('lastPageBtn').disable();
	
				Ext.getCmp('btnClean').disable();
				Ext.getCmp('btnSortBy').disable();
			}
			else
			{
				Ext.getCmp('btnClean').enable();
				Ext.getCmp('btnSortBy').enable();
				Ext.getCmp('currentPage').setValue(TR.state.currentPage);
				
				if( TR.state.currentPage == TR.state.total 
					&& TR.state.total== 1 )
				{
					Ext.getCmp('firstPageBtn').disable();
					Ext.getCmp('previousPageBtn').disable();
					Ext.getCmp('nextPageBtn').disable();
					Ext.getCmp('lastPageBtn').disable();
				}
				else if( TR.state.currentPage == TR.state.total )
				{
					Ext.getCmp('firstPageBtn').enable();
					Ext.getCmp('previousPageBtn').enable();
					Ext.getCmp('nextPageBtn').disable();
					Ext.getCmp('lastPageBtn').disable();
				}
				else if( TR.state.currentPage == 1 )
				{
					Ext.getCmp('firstPageBtn').disable();
					Ext.getCmp('previousPageBtn').disable();
					Ext.getCmp('nextPageBtn').enable();
					Ext.getCmp('lastPageBtn').enable();
				}
				else
				{
					Ext.getCmp('firstPageBtn').enable();
					Ext.getCmp('previousPageBtn').enable();
					Ext.getCmp('nextPageBtn').enable();
					Ext.getCmp('lastPageBtn').enable();
				} 
			}
        },
		hidePagingBar: function(){
			Ext.getCmp('currentPage').setVisible(false);
			Ext.getCmp('totalEventLbl').setVisible(false);
			Ext.getCmp('totalPageLbl').setVisible(false);
			Ext.getCmp('currentPage').setVisible(false);
			Ext.getCmp('currentPage').setVisible(false);
			Ext.getCmp('currentPage').setVisible(false);
			Ext.getCmp('firstPageBtn').setVisible(false);
			Ext.getCmp('previousPageBtn').setVisible(false);
			Ext.getCmp('nextPageBtn').setVisible(false);
			Ext.getCmp('lastPageBtn').setVisible(false);
			Ext.getCmp('refreshBtn').setVisible(false);
			Ext.getCmp('pageLbl').setVisible(false);
			Ext.getCmp('separate1Lbl').setVisible(false);
			Ext.getCmp('separate2Lbl').setVisible(false);
		},
		showPagingBar: function(){
			Ext.getCmp('currentPage').setVisible(true);
			Ext.getCmp('totalEventLbl').setVisible(true);
			Ext.getCmp('totalPageLbl').setVisible(true);
			Ext.getCmp('currentPage').setVisible(true);
			Ext.getCmp('currentPage').setVisible(true);
			Ext.getCmp('currentPage').setVisible(true);
			Ext.getCmp('firstPageBtn').setVisible(true);
			Ext.getCmp('previousPageBtn').setVisible(true);
			Ext.getCmp('nextPageBtn').setVisible(true);
			Ext.getCmp('lastPageBtn').setVisible(true);
			Ext.getCmp('refreshBtn').setVisible(true);
			Ext.getCmp('pageLbl').setVisible(true);
			Ext.getCmp('separate1Lbl').setVisible(true);
			Ext.getCmp('separate2Lbl').setVisible(true);
		}
    };
        
	TR.exe = {
		execute: function( type, isSorted ) {
			TR.state.generateReport( type, isSorted );
		},
		paging: function( currentPage )
		{
			TR.state.currentPage = currentPage;
			TR.state.filterReport();
			Ext.getCmp('currentPage').setValue( currentPage );	
			TR.datatable.setPagingToolbarStatus();
		},
		datatable: function() {
			TR.store.getDataTableStore();
			TR.datatable.getDataTable();
			TR.datatable.setPagingToolbarStatus();
		}
    };
	
	TR.app = {};
	
	TR.app.CaseFavoriteWindow = function() {

		// Objects
		var NameWindow,

		// Instances
			nameWindow,
	
		// Components
			addButton,
			searchTextfield,
			grid,
			prevButton,
			nextButton,
			tbar,
			bbar,
			info,
			nameTextfield,
			createButton,
			updateButton,
			cancelButton,
			favoriteWindow,

		// Vars
			windowWidth = 500,
			windowCmpWidth = windowWidth - 22;

		TR.store.caseBasedFavorite.on('load', function(store, records) {
			var pager = store.proxy.reader.jsonData.pager;

			info.setText( TR.i18n.page + ' ' + pager.currentPage + TR.i18n.of + ' ' + pager.pageCount );

			prevButton.enable();
			nextButton.enable();

			if (pager.currentPage === 1) {
				prevButton.disable();
			}

			if (pager.currentPage === pager.pageCount) {
				nextButton.disable();
			}
		});
		
		NameWindow = function(id) {
			var window;
			var record = TR.store.caseBasedFavorite.getById(id);
			
			nameTextfield = Ext.create('Ext.form.field.Text', {
				height: 26,
				width: 371,
				fieldStyle: 'padding-left: 6px; border-radius: 1px; border-color: #bbb; font-size:11px',
				style: 'margin-bottom:0',
				emptyText: TR.i18n.favorite_name,
				value: id ? record.data.name : '',
				listeners: {
					afterrender: function() {
						this.focus();
					}
				}
			});
			
			createButton = Ext.create('Ext.button.Button', {
				text: TR.i18n.create,
				handler: function() {
					var name = nameTextfield.getValue();

					if (name) {
						
						// Validation

						if( !TR.state.caseBasedReport.validation.objects() )
						{
							return;
						}
						
						// Save favorite
						
						TR.util.mask.showMask(TR.cmp.caseBasedFavorite.window, TR.i18n.saving + '...');
						var p = TR.state.caseBasedReport.getParams(false);
						p.name = name;
						
						Ext.Ajax.request({
							url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_save,
							method: 'POST',
							params: p,
							success: function() {
								TR.store.caseBasedFavorite.loadStore();
								window.destroy();
								TR.util.mask.hideMask();
							}
						});
					}
				}
			});
			
			updateButton = Ext.create('Ext.button.Button', {
				text: TR.i18n.update,
				handler: function() {
					var name = nameTextfield.getValue();

					if (id && name) {
						if (TR.store.caseBasedFavorite.findExact('name', name) != -1) {
							return;
						}
						TR.util.mask.showMask(TR.cmp.caseBasedFavorite.window, TR.i18n.renaming + '...');
						Ext.Ajax.request({
							url:  TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_rename,
							method: 'POST',
							params: {id: id, name: name},
							failure: function(r) {
								TR.util.mask.hideMask();
								alert(r.responseText);
							},
							success: function() {
								TR.store.caseBasedFavorite.loadStore();
								window.destroy();
								TR.util.mask.hideMask();
							}
						});
					}
				}
			});
			
			cancelButton = Ext.create('Ext.button.Button', {
				text: TR.i18n.cancel,
				handler: function() {
					window.destroy();
				}
			});
			
			window = Ext.create('Ext.window.Window', {
				title: id ? TR.i18n.rename_favorite : TR.i18n.create_new_favorite,
				bodyStyle: 'padding:2px; background:#fff',
				resizable: false,
				modal: true,
				items: nameTextfield,
				bbar: [
					cancelButton,
					'->',
					id ? updateButton : createButton
				],
				listeners: {
					show: function(w) {
						TR.util.window.setAnchorPosition(w, addButton);
					}
				}
			});

			return window;
		};

		addButton = Ext.create('Ext.button.Button', {
			text: TR.i18n.add_new,
			width: 75,
			height: 26,
			style: 'border-radius: 1px;',
			menu: {},
			handler: function() {
				nameWindow = new NameWindow(null, TR.i18n.create);
				nameWindow.show();
			}
		});

		searchTextfield = Ext.create('Ext.form.field.Text', {
			width: windowCmpWidth - addButton.width - 11,
			height: 26,
			fieldStyle: 'padding-right: 0; padding-left: 6px; border-radius: 1px; border-color: #bbb; font-size:11px',
			emptyText: TR.i18n.search_for_favorites,
			enableKeyEvents: true,
			currentValue: '',
			listeners: {
				keyup: function() {
					if (this.getValue() !== this.currentValue) {
						this.currentValue = this.getValue();

						var value = this.getValue();
						var url = ( value ) ? TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_getall + "?query=" + value : null;
						var store = TR.store.caseBasedFavorite;

						store.currentPage = 1;
						store.loadStore(url);
					}
				}
			}
		});

		prevButton = Ext.create('Ext.button.Button', {
			text: TR.i18n.prev,
			handler: function() {
				var value = searchTextfield.getValue();
				var url = ( value ) ? TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_getall + "?query=" + value : null;
				var store = TR.store.caseBasedFavorite;

				store.currentPage = store.currentPage <= 1 ? 1 : store.currentPage - 1;
				store.loadStore(url);
			}
		});

		nextButton = Ext.create('Ext.button.Button', {
			text: TR.i18n.next,
			handler: function() {
				var value = searchTextfield.getValue();
				var url = ( value ) ? TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.casebasedfavorite_getall + "?query=" + value : null;
				var store = TR.store.caseBasedFavorite;

				store.currentPage = store.currentPage + 1;
				store.loadStore(url);
			}
		});

		info = Ext.create('Ext.form.Label', {
			cls: 'tr-label-info',
			width: 300,
			height: 22
		});
		
		grid = Ext.create('Ext.grid.Panel', {
			cls: 'tr-grid',
			scroll: false,
			hideHeaders: true,
			columns: [
				{
					dataIndex: 'name',
					sortable: false,
					width: windowCmpWidth - 88,
					renderer: function(value, metaData, record) {
						var fn = function() {
							var element = Ext.get(record.data.id);
							if (element) {
								element = element.parent('td');
								element.addClsOnOver('link');
								element.load = function() {
									favoriteWindow.hide();
									TR.util.crud.favorite.run( record.data.id );
								};
								element.dom.setAttribute('onclick', 'Ext.get(this).load();');
							}
						};

						Ext.defer(fn, 100);

						return '<div id="' + record.data.id + '">' + value + '</div>';
					}
				},
				{
					xtype: 'actioncolumn',
					sortable: false,
					width: 80,
					items: [
						{
							iconCls: 'tr-grid-row-icon-edit',
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex);
								nameWindow = new NameWindow(record.data.id);
								nameWindow.show();
							}
						},
						{
							iconCls: 'tr-grid-row-icon-delete',
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex);
									
								var message = TR.i18n.confirm_delete_favorite + '\n\n' + record.data.name;
								if (confirm(message)) {
									TR.util.mask.showMask(TR.cmp.caseBasedFavorite.window, TR.i18n.deleting + '...');
									var baseurl =  TR.conf.finals.ajax.casebasedfavorite_delete + "?id=" + record.data.id;
									selection = TR.cmp.caseBasedFavorite.grid.getSelectionModel().getSelection();
									Ext.Array.each(selection, function(item) {
										baseurl = Ext.String.urlAppend(baseurl, 'uids=' + item.data.id);
									});
									
									Ext.Ajax.request({
										url: baseurl,
										method: 'POST',
										success: function() {
											TR.store.caseBasedFavorite.loadStore();
											TR.util.mask.hideMask();
										}
									});  
								}
							}
						}
					]
				},
				{
					sortable: false,
					width: 2
				}
			],
			store: TR.store.caseBasedFavorite,
			bbar: [
				info,
				'->',
				prevButton,
				nextButton
			],
			listeners: {
				added: function() {
					TR.cmp.caseBasedFavorite.grid = this;
				},
				render: function() {
					var size = Math.floor((TR.cmp.region.center.getHeight() - 155) / TR.conf.layout.grid_row_height);
					this.store.pageSize = size;
					this.store.currentPage = 1;
					this.store.loadStore();

					TR.store.caseBasedFavorite.on('load', function() {
						if (this.isVisible()) {
							this.fireEvent('afterrender');
						}
					}, this);
				},
				afterrender: function() {
					var fn = function() {
						var editArray = Ext.query('.tooltip-favorite-edit'),
							deleteArray = Ext.query('.tooltip-favorite-delete'),
							el;

						for (var i = 0; i < editArray.length; i++) {
							el = editArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: TR.i18n.rename,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}
						
						for (var i = 0; i < deleteArray.length; i++) {
							el = deleteArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: 'Delete', //i18n
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}
					};

					Ext.defer(fn, 100);
				},
				itemmouseenter: function(grid, record, item) {
					this.currentItem = Ext.get(item);
					this.currentItem.removeCls('x-grid-row-over');
				},
				select: function() {
					this.currentItem.removeCls('x-grid-row-selected');
				},
				selectionchange: function() {
					this.currentItem.removeCls('x-grid-row-focused');
				}
			}
		});
		
		favoriteWindow = Ext.create('Ext.window.Window', {
			title: TR.i18n.manage_favorites,
			bodyStyle: 'padding:5px; background-color:#fff',
			resizable: false,
			modal: true,
			width: windowWidth,
			destroyOnBlur: true,
			items: [
				{
					xtype: 'panel',
					layout: 'hbox',
					bodyStyle: 'border:0 none',
					items: [
						addButton,
						{
							height: 24,
							width: 1,
							style: 'width:1px; margin-left:5px; margin-right:5px; margin-top:1px',
							bodyStyle: 'border-left: 1px solid #aaa'
						},
						searchTextfield
					]
				},
				grid
			],
			listeners: {
				show: function(w) {
					TR.util.window.setAnchorPosition(w, TR.cmp.toolbar.favoritee);
				}
			}
		});

		return favoriteWindow;
	};
	
	TR.app.AggregateFavoriteWindow = function() {

		// Objects
		var NameWindow,

		// Instances
			nameWindow,
	
		// Components
			addButton,
			searchTextfield,
			grid,
			prevButton,
			nextButton,
			tbar,
			bbar,
			info,
			nameTextfield,
			createButton,
			updateButton,
			cancelButton,
			favoriteWindow,

		// Vars
			windowWidth = 500,
			windowCmpWidth = windowWidth - 22;

		TR.store.aggregateFavorite.on('load', function(store, records) {
			var pager = store.proxy.reader.jsonData.pager;

			info.setText( TR.i18n.page + ' ' + pager.currentPage + TR.i18n.of + ' ' + pager.pageCount );

			prevButton.enable();
			nextButton.enable();

			if (pager.currentPage === 1) {
				prevButton.disable();
			}

			if (pager.currentPage === pager.pageCount) {
				nextButton.disable();
			}
		});
		
		NameWindow = function(id) {
			var window;
			var record = TR.store.aggregateFavorite.getById(id);
			
			nameTextfield = Ext.create('Ext.form.field.Text', {
				height: 26,
				width: 371,
				fieldStyle: 'padding-left: 6px; border-radius: 1px; border-color: #bbb; font-size:11px',
				style: 'margin-bottom:0',
				emptyText: TR.i18n.favorite_name,
				value: id ? record.data.name : '',
				listeners: {
					afterrender: function() {
						this.focus();
					}
				}
			});
			
			createButton = Ext.create('Ext.button.Button', {
				text: TR.i18n.create,
				handler: function() {
					var name = nameTextfield.getValue();
					if (name) {
						
						// Validation
						
						if( !TR.state.aggregateReport.validation.objects() )
						{
							return;
						}
						
						// Save favorite
						
						TR.util.mask.showMask(TR.cmp.aggregateFavorite.window, TR.i18n.saving + '...');
						var p = TR.state.getParams();
						p.name = name;
						
						Ext.Ajax.request({
							url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_save,
							method: 'POST',
							params: p,
							success: function() {
								TR.store.aggregateFavorite.loadStore();
								window.destroy();
								TR.util.mask.hideMask();
							}
						});  
					}
				}
			});
			
			updateButton = Ext.create('Ext.button.Button', {
				text: TR.i18n.update,
				handler: function() {
					var name = nameTextfield.getValue();

					if (id && name) {
					
						if (TR.store.aggregateFavorite.findExact('name', name) != -1) {
							return;
						}
						TR.util.mask.showMask(TR.cmp.aggregateFavorite.window, TR.i18n.renaming + '...');
						var r = TR.cmp.aggregateFavorite.grid.getSelectionModel().getSelection()[0];
						Ext.Ajax.request({
							url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_rename,
							method: 'POST',
							params: {id: id, name: name},
							success: function() {
								TR.store.aggregateFavorite.loadStore();
								window.destroy();
								TR.util.mask.hideMask();
							}
						});
					}
				}
			});
			
			cancelButton = Ext.create('Ext.button.Button', {
				text: TR.i18n.cancel,
				handler: function() {
					window.destroy();
				}
			});
			
			window = Ext.create('Ext.window.Window', {
				title: id ? TR.i18n.rename_favorite : TR.i18n.create_new_favorite,
				bodyStyle: 'padding:2px; background:#fff',
				resizable: false,
				modal: true,
				items: nameTextfield,
				bbar: [
					cancelButton,
					'->',
					id ? updateButton : createButton
				],
				listeners: {
					show: function(w) {
						TR.util.window.setAnchorPosition(w, addButton);
					}
				}
			});

			return window;
		};

		addButton = Ext.create('Ext.button.Button', {
			text: TR.i18n.add_new,
			width: 75,
			height: 26,
			style: 'border-radius: 1px;',
			menu: {},
			handler: function() {
				nameWindow = new NameWindow(null, TR.i18n.create);
				nameWindow.show();
			}
		});

		searchTextfield = Ext.create('Ext.form.field.Text', {
			width: windowCmpWidth - addButton.width - 11,
			height: 26,
			fieldStyle: 'padding-right: 0; padding-left: 6px; border-radius: 1px; border-color: #bbb; font-size:11px',
			emptyText: TR.i18n.search_for_favorites,
			enableKeyEvents: true,
			currentValue: '',
			listeners: {
				keyup: function() {
					if (this.getValue() !== this.currentValue) {
						this.currentValue = this.getValue();

						var value = this.getValue();
						var url = ( value ) ? TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_getall + "?query=" + value : null;
						var store = TR.store.aggregateFavorite;

						store.currentPage = 1;
						store.loadStore(url);
					}
				}
			}
		});

		prevButton = Ext.create('Ext.button.Button', {
			text: TR.i18n.prev,
			handler: function() {
				var value = searchTextfield.getValue();
				var url = ( value ) ? TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_getall + "?query=" + value : null;
				var store = TR.store.aggregateFavorite;

				store.currentPage = store.currentPage <= 1 ? 1 : store.currentPage - 1;
				store.loadStore(url);
			}
		});

		nextButton = Ext.create('Ext.button.Button', {
			text: TR.i18n.next,
			handler: function() {
				var value = searchTextfield.getValue();
				var url = ( value ) ? TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.aggregatefavorite_getall + "?query=" + value : null;
				var store = TR.store.aggregateFavorite;

				store.currentPage = store.currentPage + 1;
				store.loadStore(url);
			}
		});

		info = Ext.create('Ext.form.Label', {
			cls: 'tr-label-info',
			width: 300,
			height: 22
		});
		
		grid = Ext.create('Ext.grid.Panel', {
			cls: 'tr-grid',
			scroll: false,
			hideHeaders: true,
			columns: [
				{
					dataIndex: 'name',
					sortable: false,
					width: windowCmpWidth - 88,
					renderer: function(value, metaData, record) {
						var fn = function() {
							var element = Ext.get(record.data.id);
							if (element) {
								element = element.parent('td');
								element.addClsOnOver('link');
								element.load = function() {
									favoriteWindow.hide();
									TR.util.crud.favorite.run( record.data.id );
								};
								element.dom.setAttribute('onclick', 'Ext.get(this).load();');
							}
						};

						Ext.defer(fn, 100);

						return '<div id="' + record.data.id + '">' + value + '</div>';
					}
				},
				{
					xtype: 'actioncolumn',
					sortable: false,
					width: 80,
					items: [
						{
							iconCls: 'tr-grid-row-icon-edit',
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex);
								nameWindow = new NameWindow(record.data.id);
								nameWindow.show();
							}
						},
						{
							iconCls: 'tr-grid-row-icon-delete',
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex);
									
								var message = TR.i18n.confirm_delete_favorite + '\n\n' + record.data.name;
								if (confirm(message)) {
									TR.util.mask.showMask(TR.cmp.aggregateFavorite.window, TR.i18n.deleting + '...');
									var baseurl =  TR.conf.finals.ajax.aggregatefavorite_delete + "?id=" + record.data.id;
									selection = TR.cmp.aggregateFavorite.grid.getSelectionModel().getSelection();
									Ext.Array.each(selection, function(item) {
										baseurl = Ext.String.urlAppend(baseurl, 'uids=' + item.data.id);
									});
									
									Ext.Ajax.request({
										url: baseurl,
										method: 'POST',
										success: function() {
											TR.store.aggregateFavorite.loadStore();
											TR.util.mask.hideMask();
										}
									});  
								}
							}
						}
					]
				},
				{
					sortable: false,
					width: 2
				}
			],
			store: TR.store.aggregateFavorite,
			bbar: [
				info,
				'->',
				prevButton,
				nextButton
			],
			listeners: {
				added: function() {
					TR.cmp.aggregateFavorite.grid = this;
				},
				render: function() {
					var size = Math.floor((TR.cmp.region.center.getHeight() - 155) / TR.conf.layout.grid_row_height);
					this.store.pageSize = size;
					this.store.currentPage = 1;
					this.store.loadStore();

					TR.store.aggregateFavorite.on('load', function() {
						if (this.isVisible()) {
							this.fireEvent('afterrender');
						}
					}, this);
				},
				afterrender: function() {
					var fn = function() {
						var editArray = Ext.query('.tooltip-favorite-edit'),
							deleteArray = Ext.query('.tooltip-favorite-delete'),
							el;

						for (var i = 0; i < editArray.length; i++) {
							el = editArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: TR.i18n.rename,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}
						
						for (var i = 0; i < deleteArray.length; i++) {
							el = deleteArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: 'Delete', //i18n
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}
					};

					Ext.defer(fn, 100);
				},
				itemmouseenter: function(grid, record, item) {
					this.currentItem = Ext.get(item);
					this.currentItem.removeCls('x-grid-row-over');
				},
				select: function() {
					this.currentItem.removeCls('x-grid-row-selected');
				},
				selectionchange: function() {
					this.currentItem.removeCls('x-grid-row-focused');
				}
			}
		});
		
		favoriteWindow = Ext.create('Ext.window.Window', {
			title: TR.i18n.manage_favorites,
			bodyStyle: 'padding:5px; background-color:#fff',
			resizable: false,
			modal: true,
			width: windowWidth,
			destroyOnBlur: true,
			items: [
				{
					xtype: 'panel',
					layout: 'hbox',
					bodyStyle: 'border:0 none',
					items: [
						addButton,
						{
							height: 24,
							width: 1,
							style: 'width:1px; margin-left:5px; margin-right:5px; margin-top:1px',
							bodyStyle: 'border-left: 1px solid #aaa'
						},
						searchTextfield
					]
				},
				grid
			],
			listeners: {
				show: function(w) {
					TR.util.window.setAnchorPosition(w, TR.cmp.toolbar.favoritee);
				}
			}
		});

		return favoriteWindow;
	};
	
	Ext.apply(Ext.form.VTypes, {
		daterange : function(val, field) {
			var date = field.parseDate(val);
	 
			if(!date){
				return;
			}
			if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
				var start = Ext.getCmp(field.startDateField);
				start.setMaxValue(date);
				start.validate();
				this.dateRangeMax = date;
			}
			else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
				var end = Ext.getCmp(field.endDateField);
				end.setMinValue(date);
				end.validate();
				this.dateRangeMin = date;
			}
			/*
			 * Always return true since we're only using this vtype to set the
			 * min/max allowed values (these are tested for after the vtype test)
			 */
			return true;
		}
	});

    TR.viewport = Ext.create('Ext.container.Viewport', {
        layout: 'border',
        renderTo: Ext.getBody(),
        isrendered: false,
        items: [
            {
                region: 'west',
                preventHeader: true,
                collapsible: true,
                collapseMode: 'mini',
                items: [
				{
					xtype: 'toolbar',
					style: 'padding-top:1px; border-style:none',
					width: TR.conf.layout.west_fieldset_width + 50,
					bodyStyle: 'border-style:none; background-color:transparent; padding:4px 0 0 8px',
                    items: [
						{
							xtype: 'panel',
							bodyStyle: 'border-style:none; background-color:transparent; padding:10px 0 0 8px',
							items: [
								Ext.create('Ext.form.Panel', {
								bodyStyle: 'border-style:none; background-color:transparent; padding:3px 30px 0 8px',
                                width: TR.conf.layout.west_fieldset_width + 50,
								items: [
								{
									xtype: 'radiogroup',
									id: 'reportTypeGroup',
									fieldLabel: TR.i18n.report_type,
									labelStyle: 'font-weight:bold',
									columns: 2,
									vertical: true,
									items: [
									{
										boxLabel: TR.i18n.case_based_report,
										name: 'reportType',
										inputValue: 'true',
										checked: true,
										listeners: {
											change: function (cb, nv, ov) {
												if(nv)
												{
													// for case-based report
													dataElementTabTitle.innerHTML = TR.i18n.data_elements;
													Ext.getCmp('limitOption').setVisible(false);
													Ext.getCmp('dataElementGroupByCbx').setVisible(false);
													Ext.getCmp('aggregateType').setVisible(false);
													Ext.getCmp('downloadPdfIcon').setVisible(false);
													Ext.getCmp('downloadCvsIcon').setVisible(false);
													Ext.getCmp('positionField').setVisible(false);
													Ext.getCmp('aggregateFavoriteBtn').setVisible(false);
													Ext.getCmp('datePeriodRangeDiv').setVisible(false);
													Ext.getCmp('deSumCbx').setVisible(false);
													Ext.getCmp('caseBasedFavoriteBtn').setVisible(true);
													Ext.getCmp('levelCombobox').setVisible(true);
													var level = Ext.getCmp('levelCombobox').getValue();
													if( level==null || level!='' ){
														Ext.getCmp('levelCombobox').setValue('1');
													}
													Ext.getCmp('dateRangeDiv').setVisible(true);
													Ext.getCmp('patientPropertiesDiv').setVisible(true);
													Ext.getCmp('btnSortBy').setVisible(true);
													Ext.getCmp('relativePeriodsDiv').setVisible(false); 
													Ext.getCmp('fixedPeriodsDiv').setVisible(false);
													Ext.getCmp('dateRangeDiv').expand();
													Ext.getCmp('filterPanel').setHeight(155);
													
												}
											}
										}
									}, 
									{
										boxLabel: TR.i18n.aggregated_report,
										name: 'reportType',
										inputValue: 'false',
										listeners: {
											change: function (cb, nv, ov) {
												if(nv)
												{
													// For aggregate report
													dataElementTabTitle.innerHTML = TR.i18n.data_filter;
													Ext.getCmp('limitOption').setVisible(true);
													Ext.getCmp('dataElementGroupByCbx').setVisible(true);
													Ext.getCmp('aggregateType').setVisible(true);
													Ext.getCmp('downloadPdfIcon').setVisible(true);
													Ext.getCmp('downloadCvsIcon').setVisible(true);
													Ext.getCmp('aggregateFavoriteBtn').setVisible(true);
													Ext.getCmp('positionField').setVisible(true);
													Ext.getCmp('deSumCbx').setVisible(true);
													Ext.getCmp('dateRangeDiv').setVisible(false);
													Ext.getCmp('levelCombobox').setVisible(false);
													Ext.getCmp('caseBasedFavoriteBtn').setVisible(false);
													Ext.getCmp('btnSortBy').setVisible(false);
													Ext.getCmp('patientPropertiesDiv').setVisible(false);
													
													Ext.getCmp('datePeriodRangeDiv').setVisible(true);
													Ext.getCmp('fixedPeriodsDiv').setVisible(true);
													Ext.getCmp('relativePeriodsDiv').setVisible(true);
													Ext.getCmp('datePeriodRangeDiv').expand();
													Ext.getCmp('filterPanel').setHeight(105);
												}
											}
										}
									}]
								}]
							}),
							{ bodyStyle: 'padding:1px 0; border-style:none;	background-color:transparent' },
							{
								xtype: 'panel',
								layout: 'column',
								bodyStyle: 'border-style:none; background-color:transparent; padding:4px 0 0 8px',
								width: TR.conf.layout.west_fieldset_width + 50,
								items:[
								{
									xtype: 'combobox',
									cls: 'tr-combo',
									name: TR.init.system.programs,
									id: 'programCombobox',
									fieldLabel: TR.i18n.program,
									labelStyle: 'font-weight:bold',
									labelAlign: 'top',
									emptyText: TR.i18n.please_select,
									queryMode: 'local',
									editable: false,
									valueField: 'id',
									displayField: 'name',
									width: TR.conf.layout.west_fieldset_width / 2 - 10,
									store: TR.store.program,
									listeners: {
										added: function() {
											TR.cmp.settings.program = this;
										},
										select: function(cb) {
											TR.state.isFilter = false;
											var pId = cb.getValue();
											
											// Registration programs
											if( cb.displayTplData[0].type !='3' )
											{
												// IDENTIFIER TYPE && PATIENT ATTRIBUTES
												var storePatientProperty = TR.store.patientProperty.available;
												TR.store.patientProperty.selected.removeAll();
												storePatientProperty.parent = pId;
												
												if (TR.util.store.containsParent(storePatientProperty)) {
													TR.util.store.loadFromStorage(storePatientProperty);
													TR.util.multiselect.filterAvailable(TR.cmp.params.patientProperty.available, TR.cmp.params.patientProperty.selected);
												}
												else {
													storePatientProperty.load({params: {programId: pId}});
												}
											}
											else
											{
												TR.store.patientProperty.available.removeAll();
												TR.store.patientProperty.selected.removeAll();
											}
										
											// PROGRAM-STAGE										
											var storeProgramStage = TR.store.programStage;
											TR.store.dataelement.available.removeAll();
											TR.store.dataelement.selected.removeAll();
											storeProgramStage.parent = pId;
											TR.store.dataelement.isLoadFromFavorite = false;
											storeProgramStage.load({params: {programId: pId}});
											
											// FILTER-VALUES FIELDS
											Ext.getCmp('filterPanel').removeAll();
											Ext.getCmp('filterPropPanel').removeAll();
										}
									}
								},
								{
									xtyle:'label',
									text: ''
								},
								{
									xtype: 'combobox',
									cls: 'tr-combo',
									id:'programStageCombobox',
									fieldLabel: TR.i18n.program_stage,
									labelStyle: 'font-weight:bold',
									labelAlign: 'top',
									emptyText: TR.i18n.please_select,
									queryMode: 'local',
									editable: false,
									valueField: 'id',
									displayField: 'name',
									width:  TR.conf.layout.west_fieldset_width / 2 - 10,
									store: TR.store.programStage,
									listeners: {
										added: function() {
											TR.cmp.params.programStage = this;
										},  
										select: function(cb) {
											TR.state.isFilter = false;
											var store = TR.store.dataelement.available;
											TR.store.dataelement.selected.loadData([],false);
											store.parent = cb.getValue();
											
											if (TR.util.store.containsParent(store)) {
												TR.util.store.loadFromStorage(store);
												TR.util.multiselect.filterAvailable(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected);
											}
											else {
												store.load({params: {programStageId: cb.getValue()}});
											}
											
											// FILTER-VALUES FIELDS
											Ext.getCmp('filterPanel').removeAll();
										} 
									}
								}
								]
							}]
						}]
					},                            
					{
						xtype: 'panel',
                        bodyStyle: 'border-style:none; border-top:2px groove #eee; padding:10px 10px 0 10px;',
                        layout: 'fit',
                        items: [
							{
								xtype: 'panel',
								layout: 'accordion',
								activeOnTop: true,
								cls: 'tr-accordion',
								bodyStyle: 'border:0 none',
								height: 475,
								items: [
							
									// DATE-RANGE
									{
										title: '<div style="height:17px; background-image:url(images/period.png); background-repeat:no-repeat; padding-left:20px">' + TR.i18n.period_range + '</div>',
										id: 'dateRangeDiv',
										hideCollapseTool: true,
										autoScroll: true,
										hidden: true,
										items: [
											{
												xtype: 'datefield',
												cls: 'tr-textfield-alt1',
												id: 'startDate',
												fieldLabel: TR.i18n.start_date,
												labelWidth: 90,
												editable: true,
												allowBlank:true,
												invalidText: TR.i18n.the_date_is_not_valid,
												style: 'margin-right:8px',
												width: TR.conf.layout.west_fieldset_width - 20,
												format: TR.i18n.format_date,
												value: new Date((new Date()).setMonth((new Date()).getMonth()-3)),
												maxValue: new Date(),
												listeners: {
													added: function() {
														TR.cmp.settings.startDate = this;
													},
													change:function(){
														var startDate =  TR.cmp.settings.startDate.getValue();
														Ext.getCmp('endDate').setMinValue(startDate);
													}
												}
											},
											{
												xtype: 'datefield',
												cls: 'tr-textfield-alt1',
												id: 'endDate',
												fieldLabel: TR.i18n.end_date,
												labelWidth: 90,
												editable: true,
												allowBlank:true,
												invalidText: TR.i18n.the_date_is_not_valid,
												width: TR.conf.layout.west_fieldset_width - 20,
												format: TR.i18n.format_date,
												value: new Date(),
												minValue: new Date((new Date()).setMonth((new Date()).getMonth()-3)),
												listeners: {
													added: function() {
														TR.cmp.settings.endDate = this;
													},
													change:function(){
														var endDate =  TR.cmp.settings.endDate.getValue();
														Ext.getCmp('startDate').setMaxValue( endDate );
													}
												}
											}
										]
									},
									
									// DATE-PERIOD RANGE
									{
										title: '<div style="height:17px; background-image:url(images/period.png); background-repeat:no-repeat; padding-left:20px">' + TR.i18n.date_period_range + '</div>',
										id: 'datePeriodRangeDiv',
										hideCollapseTool: true,
										autoScroll: true,
										items: [
											{
												xtype: 'datefield',
												cls: 'tr-textfield-alt1',
												id: 'startDateRange',
												fieldLabel: TR.i18n.start_date,
												labelWidth: 90,
												editable: true,
												allowBlank:true,
												emptyText: TR.i18n.select_from_date,
												invalidText: TR.i18n.the_date_is_not_valid,
												style: 'margin-right:8px',
												width: TR.conf.layout.west_fieldset_width - 20,
												format: TR.i18n.format_date,
												maxValue: new Date(),
												listeners: {
													added: function() {
														TR.cmp.settings.startDateRange = this;
													},
													change:function(){
														var startDate =  TR.cmp.settings.startDate.getValue();
														Ext.getCmp('endDate').setMinValue(startDate);
													}
												}
											},
											{
												xtype: 'datefield',
												cls: 'tr-textfield-alt1',
												id: 'endDateRange',
												fieldLabel: TR.i18n.end_date,
												labelWidth: 90,
												editable: true,
												allowBlank:true,
												emptyText: TR.i18n.select_to_date,
												invalidText: TR.i18n.the_date_is_not_valid,
												width: TR.conf.layout.west_fieldset_width - 20,
												format: TR.i18n.format_date,
												maxValue: new Date(),
												listeners: {
													added: function() {
														TR.cmp.settings.startDateRange = this;
													},
													change:function(){
														var endDate =  TR.cmp.settings.endDate.getValue();
														Ext.getCmp('startDate').setMaxValue( endDate );
													}
												}
											},
											{
												xtype: 'button',
												text: TR.i18n.add,
												style: 'margin-left:95px; margin-bottom: 10px;',
												width: 130,
												height: 24,
												handler: function() {	
													var startDate = Ext.getCmp('startDateRange').rawValue;
													var endDate = Ext.getCmp('endDateRange').rawValue;
													if( startDate != '' && endDate != '' 
														&& Ext.getCmp('startDateRange').isValid() 
														&& Ext.getCmp('endDateRange').isValid() )
													{
														TR.store.dateRange.add({
															'startDate': startDate,
															'endDate': endDate
														});
													}
												}
											},
											{
												xtype: 'button',
												text: TR.i18n.clear,
												style: 'margin-left:4px; margin-bottom: 10px;',
												width: 130,
												height: 24,
												handler: function() {
													if( TR.store.dateRange.data.items.length > 0 )
													{
														var result = window.confirm( TR.i18n.confirm_delete_date_range_list );
														if ( result )
														{
															TR.store.dateRange.loadData([],false);
														}
													}
												}
											},
											Ext.create('Ext.grid.Panel', {
												style: 'border: solid 1px #D0D0D0',
												width: TR.conf.layout.west_fieldset_width - 18,
												store: TR.store.dateRange,
												height: 205,
												columns: [
													{ 
														text: TR.i18n.start_date,  
														dataIndex: 'startDate', 
														width: 150, 
														menuDisabled: true,
														sortable: false,
														draggable: false
													},
													{ 
														text: TR.i18n.end_date, 
														dataIndex: 'endDate', 
														width: 150,
														menuDisabled: true,
														sortable: false,
														draggable: false 
													},
													{
														menuDisabled: true,
														sortable: false,
														xtype: 'actioncolumn',
														width: 40,
														items: [{
															icon: 'images/remove.png',
															tooltip: TR.i18n.remove,
															handler: function(grid, rowIndex, colIndex) {
																TR.store.dateRange.removeAt(rowIndex);
															}
														}]
													}
												]
											})
										]
									},
									
									// RELATIVE PERIODS
									{
										title: '<div style="height:17px; background-image:url(images/period.png); background-repeat:no-repeat; padding-left:20px">' + TR.i18n.relative_periods + '</div>',
										id: 'relativePeriodsDiv',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'panel',
														columnWidth: 0.32,
														bodyStyle: 'border-style:none; padding:0 0 0 8px',
														defaults: {
															labelSeparator: '',
															style: 'margin-bottom:2px',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		TR.cmp.params.relativeperiod.checkbox.push(chb);
																	}
																}
															}
														},
														items: [
															{
																xtype: 'label',
																text: TR.i18n.weeks,
																cls: 'tr-label-period-heading'
															},
															{
																xtype: 'checkbox',
																paramName: 'lastWeek',
																boxLabel: TR.i18n.last_week
															},
															{
																xtype: 'checkbox',
																paramName: 'last4Weeks',
																boxLabel: TR.i18n.last_4_weeks
															},
															{
																xtype: 'checkbox',
																paramName: 'last12Weeks',
																boxLabel: TR.i18n.last_12_weeks
															}
														]
													},
													{
														xtype: 'panel',
														layout: 'anchor',
														columnWidth: 0.32,
														bodyStyle: 'border-style:none; padding:0 0 0 20px',
														defaults: {
															labelSeparator: '',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		TR.cmp.params.relativeperiod.checkbox.push(chb);
																	}
																}
															}
														},
														items: [
															{
																xtype: 'label',
																text: TR.i18n.months,
																cls: 'tr-label-period-heading'
															},
															{
																xtype: 'checkbox',
																paramName: 'reportingMonth',
																boxLabel: TR.i18n.last_month,
																checked: true
															},
															{
																xtype: 'checkbox',
																paramName: 'last3Months',
																boxLabel: TR.i18n.last_3_months
															},
															{
																xtype: 'checkbox',
																paramName: 'last12Months',
																boxLabel: TR.i18n.last_12_months
															}
														]
													},
													{
														xtype: 'panel',
														layout: 'anchor',
														columnWidth: 0.32,
														bodyStyle: 'border-style:none; padding:0 0 0 32px',
														defaults: {
															labelSeparator: '',
															listeners: {
																added: function(chb) {
																	if (chb.xtype === 'checkbox') {
																		TR.cmp.params.relativeperiod.checkbox.push(chb);
																	}
																}
															}
														},
														items: [
															{
																xtype: 'label',
																text: TR.i18n.quarters,
																cls: 'tr-label-period-heading'
															},
															{
																xtype: 'checkbox',
																paramName: 'reportingQuarter',
																boxLabel: TR.i18n.last_quarter
															},
															{
																xtype: 'checkbox',
																paramName: 'last4Quarters',
																boxLabel: TR.i18n.last_4_quarters
															}
														]
													}
												]
											},
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
												{
													xtype: 'panel',
													columnWidth: 0.32,
													bodyStyle: 'border-style:none; padding:0 0 0 8px',
													defaults: {
														labelSeparator: '',
														style: 'margin-bottom:2px',
														listeners: {
															added: function(chb) {
																if (chb.xtype === 'checkbox') {
																	TR.cmp.params.relativeperiod.checkbox.push(chb);
																}
															}
														}
													},
													items: [
														{
															xtype: 'label',
															text: TR.i18n.six_months,
															cls: 'tr-label-period-heading'
														},
														{
															xtype: 'checkbox',
															paramName: 'lastSixMonth',
															boxLabel: TR.i18n.last_six_month
														},
														{
															xtype: 'checkbox',
															paramName: 'last2SixMonths',
															boxLabel: TR.i18n.last_two_six_month
														}
													]
												},
												{
													xtype: 'panel',
													columnWidth: 0.32,
													bodyStyle: 'border-style:none; padding:0 0 0 8px',
													defaults: {
														labelSeparator: '',
														style: 'margin-bottom:2px',
														listeners: {
															added: function(chb) {
																if (chb.xtype === 'checkbox') {
																	TR.cmp.params.relativeperiod.checkbox.push(chb);
																}
															}
														}
													},
													items: [
														{
															xtype: 'panel',
															layout: 'anchor',
															bodyStyle: 'border-style:none; padding:5px 0 0 10px',
															defaults: {
																labelSeparator: '',
																listeners: {
																	added: function(chb) {
																		if (chb.xtype === 'checkbox') {
																			TR.cmp.params.relativeperiod.checkbox.push(chb);
																		}
																	}
																}
															},
															items: [
																{
																	xtype: 'label',
																	text: TR.i18n.years,
																	cls: 'tr-label-period-heading'
																},
																{
																	xtype: 'checkbox',
																	paramName: 'thisYear',
																	boxLabel: TR.i18n.this_year
																},
																{
																	xtype: 'checkbox',
																	paramName: 'lastYear',
																	boxLabel: TR.i18n.last_year
																},
																{
																	xtype: 'checkbox',
																	paramName: 'last5Years',
																	boxLabel: TR.i18n.last_5_years
																}
															]
														}
													]
												}
												]
											}
										], 
										listeners: {
											added: function() {
												TR.cmp.params.relativeperiod.panel = this;
											}
										}
									},
									
									// FIXED PERIODS
									{
										title: '<div style="height:17px; background-image:url(images/period.png); background-repeat:no-repeat; padding-left:20px">' + TR.i18n.fixed_periods + '</div>',
										id: 'fixedPeriodsDiv',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'combobox',
														cls: 'tr-combo',
														style: 'margin-bottom:8px',
														width: 253,
														valueField: 'id',
														displayField: 'name',
														fieldLabel: TR.i18n.select_type,
														labelStyle: 'padding-left:7px;',
														labelWidth: 90,
														editable: false,
														queryMode: 'remote',
														store: TR.store.periodtype,
														periodOffset: 0,
														listeners: {
															select: function() {
																var pt = new PeriodType(),
																	periodType = this.getValue();
																var periods = pt.get(periodType).generatePeriods({
																	offset: this.periodOffset,
																	filterFuturePeriods: true,
																	reversePeriods: true
																});

																TR.store.fixedperiod.available.setIndex(periods);
																TR.store.fixedperiod.available.loadData(periods);
																TR.util.multiselect.filterAvailable(TR.cmp.params.fixedperiod.available, TR.cmp.params.fixedperiod.selected);
															}
														}
													},
													{
														xtype: 'button',
														text: 'Prev year',
														style: 'margin-left:4px',
														height: 24,
														handler: function() {
															var cb = this.up('panel').down('combobox');
															if (cb.getValue()) {
																cb.periodOffset--;
																cb.fireEvent('select');
															}
														}
													},
													{
														xtype: 'button',
														text: 'Next year',
														style: 'margin-left:3px',
														height: 24,
														handler: function() {
															var cb = this.up('panel').down('combobox');
															if (cb.getValue() && cb.periodOffset < 0) {
																cb.periodOffset++;
																cb.fireEvent('select');
															}
														}
													}
												]
											},
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'multiselect',
														name: 'availableFixedPeriods',
														cls: 'tr-toolbar-multiselect-left',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														height: 245,
														valueField: 'id',
														displayField: 'name',
														store: TR.store.fixedperiod.available,
														tbar: [
															{
																xtype: 'label',
																text: TR.i18n.available,
																cls: 'tr-toolbar-multiselect-left-label'
															},
															'->',
															{
																xtype: 'button',
																icon: 'images/arrowright.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.select(TR.cmp.params.fixedperiod.available, TR.cmp.params.fixedperiod.selected,'filterPropPanel');
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowrightdouble.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.selectAll(TR.cmp.params.fixedperiod.available, TR.cmp.params.fixedperiod.selected,'filterPropPanel');
																}
															},
															' '
														],
														listeners: {
															added: function() {
																TR.cmp.params.fixedperiod.available = this;
															},
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	TR.util.multiselect.select(this, TR.cmp.params.fixedperiod.selected);
																}, this);
															}
														}
													},
													{
														xtype: 'multiselect',
														name: 'selectedFixedPeriods',
														cls: 'tr-toolbar-multiselect-right',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														height: 245,
														displayField: 'name',
														valueField: 'id',
														ddReorder: false,
														queryMode: 'local',
														store: TR.store.fixedperiod.selected,
														tbar: [
															' ',
															{
																xtype: 'button',
																icon: 'images/arrowleftdouble.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.unselectAll(TR.cmp.params.fixedperiod.available, TR.cmp.params.fixedperiod.selected);
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowleft.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.unselect(TR.cmp.params.fixedperiod.available, TR.cmp.params.fixedperiod.selected);
																}
															},
															'->',
															{
																xtype: 'label',
																text: TR.i18n.selected,
																cls: 'tr-toolbar-multiselect-right-label'
															}
														],
														listeners: {
															added: function() {
																TR.cmp.params.fixedperiod.selected = this;
															},
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	TR.util.multiselect.unselect(TR.cmp.params.fixedperiod.available, this);
																}, this);
															}
														}
													}
												]
											}
										],
										listeners: {
											added: function() {
												TR.cmp.params.fixedperiod.panel = this;
											}
										}
									},
									
									// ORGANISATION UNIT
									{
										title: '<div style="height:17px;background-image:url(images/organisationunit.png); background-repeat:no-repeat; padding-left:20px">' + TR.i18n.organisation_units + '</div>',
										id: 'orgunitDiv',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'combobox',
												cls: 'tr-combo',
												name: TR.init.system.orgunitGroup,
												id: 'orgGroupCombobox',
												emptyText: TR.i18n.please_select,
												hidden: true,
												queryMode: 'local',
												editable: false,
												valueField: 'id',
												displayField: 'name',
												fieldLabel: TR.i18n.orgunit_groups,
												labelWidth: 135,
												emptyText: TR.i18n.please_select,
												width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor,
												store: TR.store.orgunitGroup,
												listeners: {
													added: function() {
														TR.cmp.settings.orgunitGroup = this;
													}
												}
											},
											{
												layout: 'column',
												bodyStyle: 'border:0 none; padding-bottom:4px',
												items: [
													{
														xtype: 'checkbox',
														id: 'userOrgunit',
														columnWidth: 0.5,
														boxLabel: TR.i18n.user_orgunit,
														labelWidth: TR.conf.layout.form_label_width,
														handler: function(chb, checked) {
															TR.cmp.params.organisationunit.toolbar.xable(checked, TR.cmp.aggregateFavorite.userorganisationunitchildren.getValue());
															TR.cmp.params.organisationunit.treepanel.xable(checked, TR.cmp.aggregateFavorite.userorganisationunitchildren.getValue());
															TR.state.orgunitIds = [];
														},
														listeners: {
															added: function() {
																TR.cmp.aggregateFavorite.userorganisationunit = this;
															}
														}
													},
													{
														xtype: 'checkbox',
														id: 'userOrgunitChildren',
														columnWidth: 0.5,
														boxLabel: TR.i18n.user_orgunit_children,
														labelWidth: TR.conf.layout.form_label_width,
														handler: function(chb, checked) {
															TR.cmp.params.organisationunit.toolbar.xable(checked, TR.cmp.aggregateFavorite.userorganisationunit.getValue());
															TR.cmp.params.organisationunit.treepanel.xable(checked, TR.cmp.aggregateFavorite.userorganisationunit.getValue());
														},
														listeners: {
															added: function() {
																TR.cmp.aggregateFavorite.userorganisationunitchildren = this;
															},
															handler: function(chb, checked) {
																TR.cmp.params.organisationunit.toolbar.xable(checked, TR.cmp.aggregateFavorite.userorganisationunitchildren.getValue());
																TR.cmp.params.organisationunit.treepanel.xable(checked, TR.cmp.aggregateFavorite.userorganisationunitchildren.getValue());
																TR.state.orgunitIds = [];
															},
														}
													}
												]
											},
											{
												xtype: 'toolbar',
												id: 'organisationunit_t',
												style: 'margin-bottom: 5px',
												width: TR.conf.layout.west_fieldset_width - 18,
												xable: function(checked, value) {
													if (checked || value) {
														this.disable();
													}
													else {
														this.enable();
													}
												},
												defaults: {
													height: 24
												},
												items: [
													{
														xtype: 'label',
														text: TR.i18n.auto_select_orgunit_by,
														style: 'padding-left:8px; color:#666; line-height:24px'
													},
													'->',
													{
														text: 'Group..',
														handler: function() {},
														listeners: {
															added: function() {
																this.menu = Ext.create('Ext.menu.Menu', {
																	shadow: false,
																	showSeparator: false,
																	width: TR.conf.layout.treepanel_toolbar_menu_width_group,
																	items: [
																		{
																			xtype: 'grid',
																			cls: 'tr-menugrid',
																			width: TR.conf.layout.treepanel_toolbar_menu_width_group,
																			scroll: 'vertical',
																			columns: [
																				{
																					dataIndex: 'name',
																					width: TR.conf.layout.treepanel_toolbar_menu_width_group,
																					style: 'display:none'
																				}
																			],
																			setHeightInMenu: function(store) {
																				var h = store.getCount() * 16,
																					sh = TR.util.viewport.getSize().y * 0.4;
																				this.setHeight(h > sh ? sh : h);
																				this.doLayout();
																				this.up('menu').doLayout();
																			},
																			store: TR.store.orgunitGroup,
																			listeners: {
																				itemclick: function(g, r) {
																					g.getSelectionModel().select([], false);
																					this.up('menu').hide();
																					TR.cmp.params.organisationunit.treepanel.selectByGroup(r.data.id);
																				}
																			}
																		}
																	],
																	listeners: {
																		show: function() {
																			if (!TR.store.orgunitGroup.isloaded) {
																				TR.store.orgunitGroup.load({scope: this, callback: function() {
																					this.down('grid').setHeightInMenu(TR.store.orgunitGroup);
																				}});
																			}
																			else {
																				this.down('grid').setHeightInMenu(TR.store.orgunitGroup);
																			}
																		}
																	}
																});
															}
														}
													}
												],
												listeners: {
													added: function() {
														TR.cmp.params.organisationunit.toolbar = this;
													}
												}
											},
											{
												xtype: 'treepanel',
												id: 'treeOrg',
												cls: 'tr-tree',
												width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor,
												rootVisible: false,
												autoScroll: true,
												multiSelect: true,
												rendered: false,
												selectRootIf: function() {
													if (this.getSelectionModel().getSelection().length < 1) {
														var node = this.getRootNode().findChild('id', TR.init.system.rootnodes[0].id, true);
														if (this.rendered) {
															this.getSelectionModel().select(node);
														}
														return node;
													}
												},
												numberOfRecords: 0,
												recordsToSelect: [],
												multipleSelectIf: function() {
													if (this.recordsToSelect.length === this.numberOfRecords) {
														this.getSelectionModel().select(this.recordsToSelect);
														TR.state.orgunitIds = [];
														for( var i in this.recordsToSelect){
															TR.state.orgunitIds.push( this.recordsToSelect[i].data.localid );
														}
														this.recordsToSelect = [];
														this.numberOfRecords = 0;
													}
												},
												multipleExpand: function(id, path) {
													this.expandPath('/' + TR.conf.finals.root.id + path, 'id', '/', function() {
														var record = this.getRootNode().findChild('id', id, true);
														this.recordsToSelect.push(record);
														this.multipleSelectIf();
													}, this);
												},
												select: function(url, params) {
													if (!params) {
														params = {};
													}
													Ext.Ajax.request({
														url: url,
														method: 'GET',
														params: params,
														scope: this,
														success: function(r) {
															var a = Ext.JSON.decode(r.responseText).organisationUnits;
															this.numberOfRecords = a.length;
															for (var i = 0; i < a.length; i++) {
																this.multipleExpand(a[i].id, a[i].path);
															}
														}
													});
												},
												selectByGroup: function(id) {
													if (id) {
														var url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.organisationunit_getbygroup,
															params = {id: id};
														this.select(url, params);
													}
												},
												selectByLevel: function(level) {
													if (level) {
														var url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.organisationunit_getbylevel,
															params = {level: level};
														this.select(url, params);
													}
												},
												selectByIds: function(ids) {
													if (ids) {
														var url = TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.organisationunit_getbyids;
														Ext.Array.each(ids, function(item) {
															url = Ext.String.urlAppend(url, 'ids=' + item);
														});
														if (!this.rendered) {
															TR.cmp.params.organisationunit.panel.expand();
														}
														this.select(url);
													}
												},
												store: Ext.create('Ext.data.TreeStore', {
													fields: ['id', 'localid', 'text'],
													proxy: {
														type: 'ajax',
														url: TR.conf.finals.ajax.path_root + TR.conf.finals.ajax.organisationunitchildren_get
													},
													root: {
														id: TR.conf.finals.root.id,
														localid: '0',
                                                        text: "/",
														expanded: true,
														children: TR.init.system.rootnodes
													},
													listeners: {
														load: function(s, node, r) {
															for (var i = 0; i < r.length; i++) {
																r[i].data.text = TR.conf.util.jsonEncodeString(r[i].data.text);
															}
														}
													}
												}),
												xable: function(checked, value) {
													if (checked || value) {
														this.disable();
													}
													else {
														this.enable();
													}
												},
												listeners: {
													added: function() {
														TR.cmp.params.organisationunit.treepanel = this;
													},
													render: function() {
														this.rendered = true;
													},
													itemclick : function(view,rec,item,index,eventObj){
														TR.state.orgunitIds = [];
														var selectedNodes = TR.cmp.params.organisationunit.treepanel.getSelectionModel().getSelection();
														for( var i=0; i<selectedNodes.length; i++ ){
															TR.state.orgunitIds.push( selectedNodes[i].data.localid);
														}
													},
													itemcontextmenu: function(v, r, h, i, e) {
														v.getSelectionModel().select(r, false);

														if (v.menu) {
															v.menu.destroy();
														}
														v.menu = Ext.create('Ext.menu.Menu', {
															id: 'treepanel-contextmenu',
															showSeparator: false
														});
														if (!r.data.leaf) {
															v.menu.add({
																id: 'treepanel-contextmenu-item',
																text: TR.i18n.select_all_children,
																icon: 'images/node-select-child.png',
																handler: function() {
																	r.expand(false, function() {
																		v.getSelectionModel().select(r.childNodes, true);
																		v.getSelectionModel().deselect(r);
																		
																		TR.state.orgunitIds = [];
																		for( var i in r.childNodes){
																			 TR.state.orgunitIds.push( r.childNodes[i].data.localid );
																		}
																	});
																}
															});
														}
														else {
															return;
														}

														v.menu.showAt(e.xy);
													}
												}
											}
										],
										listeners: {
											added: function() {
												TR.cmp.params.organisationunit.panel = this;
											},
											expand: function() {
												TR.cmp.params.organisationunit.treepanel.setHeight(TR.cmp.params.organisationunit.panel.getHeight() - TR.conf.layout.west_fill_accordion_organisationunit - 60 );
												TR.cmp.params.organisationunit.treepanel.selectRootIf();
											}
										}
									},
									
									// IDENTIFIER TYPE AND PATIENT-ATTRIBUTE
									{
										title: '<div style="height:17px;background-image:url(images/data.png); background-repeat:no-repeat; padding-left:20px">' + TR.i18n.identifiers_and_attributes + '</div>',
										id:'patientPropertiesDiv',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'multiselect',
														id: 'availablePatientProperties',
														name: 'availablePatientProperties',
														cls: 'tr-toolbar-multiselect-left',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														height: TR.conf.layout.west_properties_multiselect,
														displayField: 'name',
														valueField: 'id',
														queryMode: 'local',
														store: TR.store.patientProperty.available,
														tbar: [
															{
																xtype: 'label',
																text: TR.i18n.available,
																cls: 'tr-toolbar-multiselect-left-label'
															},
															'->',
															{
																xtype: 'button',
																icon: 'images/arrowright.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.select(TR.cmp.params.patientProperty.available, TR.cmp.params.patientProperty.selected,'filterPropPanel');
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowrightdouble.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.selectAll(TR.cmp.params.patientProperty.available, TR.cmp.params.patientProperty.selected,'filterPropPanel');
																}
															},
															' '
														],
														listeners: {
															added: function() {
																TR.cmp.params.patientProperty.available = this;
															},                                                                
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	TR.util.multiselect.select(this, TR.cmp.params.patientProperty.selected,'filterPropPanel');
																}, this);
															}
														}
													},                                            
													{
														xtype: 'multiselect',
														id: 'selectedPatientProperties',
														name: 'selectedPatientProperties',
														cls: 'tr-toolbar-multiselect-right',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														height: TR.conf.layout.west_properties_multiselect,
														displayField: 'name',
														valueField: 'id',
														ddReorder: true,
														queryMode: 'local',
														store: TR.store.patientProperty.selected,
														tbar: [
															' ',
															{
																xtype: 'button',
																icon: 'images/arrowleftdouble.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.unselectAll(TR.cmp.params.patientProperty.available, TR.cmp.params.patientProperty.selected,'filterPropPanel');
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowleft.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.unselect(TR.cmp.params.patientProperty.available, TR.cmp.params.patientProperty.selected,'filterPropPanel');
																}
															},
															'->',
															{
																xtype: 'label',
																text: TR.i18n.selected,
																cls: 'tr-toolbar-multiselect-right-label'
															}
														],
														listeners: {
															added: function() {
																TR.cmp.params.patientProperty.selected = this;
															},          
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	TR.util.multiselect.unselect(TR.cmp.params.patientProperty.available, this,'filterPropPanel');
																}, this);
															}
														}
													},
													{
														xtype: 'toolbar',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor),
														cls: 'tr-toolbar-multiselect-left',
														style: 'margin-top:10px;',
														items: [
															{
																xtype: 'label',	
																text: TR.i18n.filter_values,
																cls: 'tr-toolbar-multiselect-left-label'
															},
															'->',
															{
																xtype: 'button',
																icon: 'images/arrowup.png',
																tooltip: TR.i18n.show_hide_filter_values,
																up: true,
																width: 22,
																handler: function() {
																	if(this.up==true){
																		Ext.getCmp('availablePatientProperties').setVisible(false);
																		Ext.getCmp('selectedPatientProperties').setVisible(false);
																		if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true'){
																			Ext.getCmp('filterPropPanel').setHeight(TR.conf.layout.west_properties_expand_filter_panel);
																		}
																		else{
																			Ext.getCmp('filterPropPanel').setHeight(TR.conf.layout.west_properties_collapse_filter_panel);
																		}
																		this.setIcon('images/arrowdown.png');
																		this.up = false;
																	}
																	else{
																		Ext.getCmp('availablePatientProperties').setVisible(true);
																		Ext.getCmp('selectedPatientProperties').setVisible(true);
																		if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true'){
																			Ext.getCmp('filterPropPanel').setHeight(TR.conf.layout.west_properties_collapse_filter_panel);
																		}
																		else{
																			Ext.getCmp('filterPropPanel').setHeight(TR.conf.layout.west_properties_expand_filter_panel);
																		}
																		this.setIcon('images/arrowup.png');
																		this.up = true;
																	}
																}
															}
														]
													},
													{
														xtype: 'panel',
														layout: 'column',
														id: 'filterPropPanel',
														height: TR.conf.layout.west_properties_filter_panel,
														bodyStyle: 'background-color:transparent; padding:10px 10px 0px 3px',
														autoScroll: true,
														overflowX: 'hidden',
														overflowY: 'auto',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) ,
														items: []
													}
												]
											}
											
										],
										listeners: {
											added: function() {
												TR.cmp.params.patientProperty.panel = this;
											},
											expand: function() {
												var programId = TR.cmp.settings.program.getValue();	
												if( programId != null )
												{
													var programType = TR.cmp.settings.program.displayTplData[0].type;											
													if (programId != null && !TR.store.patientProperty.available.isloaded && programType !='3') {
														TR.store.patientProperty.available.load({params: {programId: programId}});
													}
												}
											}
										}
									},
									
									// DATA ELEMENTS
									{
										title: '<div id="dataElementTabTitle" style="height:17px;background-image:url(images/data.png); background-repeat:no-repeat; padding-left:20px;">' + TR.i18n.data_filter + '</div>',
										hideCollapseTool: true,
										items: [
											{
												xtype: 'panel',
												layout: 'column',
												bodyStyle: 'border-style:none',
												items: [
													{
														xtype: 'toolbar',
														id: 'avalableDEBar',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														cls: 'tr-toolbar-multiselect-left',
														items: [
															{
																xtype: 'label',	
																text: TR.i18n.available,
																cls: 'tr-toolbar-multiselect-left-label'
															},
															'->',
															{
																xtype: 'button',
																icon: 'images/arrowright.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.select(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected, 'filterPanel');
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowrightdouble.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.selectAll(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected, 'filterPanel');
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}
															},
															''
														]
													},
													{
														xtype: 'toolbar',
														id: 'selectedDEBar',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														cls: 'tr-toolbar-multiselect-left',
														items: [
															' ',
															{
																xtype: 'button',
																icon: 'images/arrowleftdouble.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.unselectAll(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected, 'filterPanel');
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.selected, Ext.getCmp('deFilterSelected').getValue());
																}
															},
															{
																xtype: 'button',
																icon: 'images/arrowleft.png',
																width: 22,
																handler: function() {
																	TR.util.multiselect.unselect(TR.cmp.params.dataelement.available, TR.cmp.params.dataelement.selected, 'filterPanel');
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.selected, Ext.getCmp('deFilterSelected').getValue());
																}
															},
															'->',
															{
																xtype: 'label',
																text: TR.i18n.selected,
																cls: 'tr-toolbar-multiselect-right-label'
															}
														]
													},	
													{
														xtype: 'multiselect',
														id: 'availableDataelements',
														name: 'availableDataelements',
														cls: 'tr-toolbar-multiselect-left',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														height: TR.conf.layout.west_dataelements_multiselect,
														displayField: 'name',
														valueField: 'id',
														queryMode: 'remote',
														store: TR.store.dataelement.available,
														tbar: [
															{
																xtype: 'textfield',
																emptyText: TR.i18n.filter,
																id: 'deFilterAvailable',
																width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2 - 64,
																listeners: {			
																	specialkey: function( textfield, e, eOpts ){
																		if ( e.keyCode == e.ENTER )
																		{
																			TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, textfield.rawValue.toLowerCase());	
																		}
																	}
																}
															},
															{
																xtype: 'button',
																icon: 'images/filter.png',
																tooltip: TR.i18n.filter,
																width: 24,
																handler: function() {
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}
															},
															{
																xtype: 'image',
																src: 'images/grid-split.png',
																width: 1,
																height: 14
															},
															{
																xtype: 'button',
																icon: 'images/clear-filter.png',
																tooltip: TR.i18n.clear,
																width: 24,
																handler: function() {
																	Ext.getCmp('deFilterAvailable').setValue('');
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}
															}
														],
														listeners: {
															added: function() {
																TR.cmp.params.dataelement.available = this;
															},                                                                
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	TR.util.multiselect.select(this, TR.cmp.params.dataelement.selected, 'filterPanel');
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}, this);																
															}
														}
													},											
													{
														xtype: 'multiselect',
														id: 'selectedDataelements',
														name: 'selectedDataelements',
														cls: 'tr-toolbar-multiselect-right',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2,
														height: TR.conf.layout.west_dataelements_multiselect,
														displayField: 'name',
														valueField: 'id',
														ddReorder: true,
														queryMode: 'remote',
														store: TR.store.dataelement.selected,
														tbar: [
															{
																xtype: 'textfield',
																emptyText: TR.i18n.filter,
																id: 'deFilterSelected',
																width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) / 2 - 64,
																listeners: {			
																	specialkey: function( textfield, e, eOpts ){
																		if ( e.keyCode == e.ENTER )
																		{
																			TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.selected, textfield.rawValue.toLowerCase());	
																		}
																	}
																}
															},
															{
																xtype: 'button',
																icon: 'images/filter.png',
																tooltip: TR.i18n.filter,
																width: 24,
																handler: function() {
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.selected, Ext.getCmp('deFilterSelected').getValue());
																}
															},
															{
																xtype: 'image',
																src: 'images/grid-split.png',
																width: 1,
																height: 14
															},
															{
																xtype: 'button',
																icon: 'images/clear-filter.png',
																tooltip: TR.i18n.clear,
																width: 24,
																handler: function() {
																	Ext.getCmp('deFilterSelected').setValue('');
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.selected, Ext.getCmp('deFilterSelected').getValue());
																}
															}
														],
														listeners: {
															added: function() {
																TR.cmp.params.dataelement.selected = this;
															},
															afterrender: function() {
																this.boundList.on('itemdblclick', function() {
																	TR.util.multiselect.unselect(TR.cmp.params.dataelement.available, this, 'filterPanel');
																	TR.util.multiselect.filterSelector( TR.cmp.params.dataelement.available, Ext.getCmp('deFilterAvailable').getValue());
																}, this);
															}
														}
													},
													{
														xtype: 'toolbar',
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor),
														cls: 'tr-toolbar-multiselect-left',
														style: 'margin-top:10px;',
														items: [
															{
																xtype: 'label',	
																text: TR.i18n.filter_values,
																cls: 'tr-toolbar-multiselect-left-label'
															},
															'->',
															{
																xtype: 'button',
																icon: 'images/arrowup.png',
																tooltip: TR.i18n.show_hide_filter_values,
																up: true,
																width: 22,
																handler: function() {
																	if(this.up==true){
																		Ext.getCmp('avalableDEBar').setVisible(false);
																		Ext.getCmp('selectedDEBar').setVisible(false);
																		Ext.getCmp('availableDataelements').setVisible(false);
																		Ext.getCmp('selectedDataelements').setVisible(false);
																		if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true'){
																			Ext.getCmp('filterPanel').setHeight(TR.conf.layout.west_dataelements_expand_filter_panel);
																		}
																		else{
																			Ext.getCmp('filterPanel').setHeight(TR.conf.layout.west_dataelements_expand_aggregate_filter_panel);
																		}
																		this.setIcon('images/arrowdown.png');
																		this.up = false;
																	}
																	else{
																		Ext.getCmp('avalableDEBar').setVisible(true);
																		Ext.getCmp('selectedDEBar').setVisible(true);
																		Ext.getCmp('availableDataelements').setVisible(true);
																		Ext.getCmp('selectedDataelements').setVisible(true);
																		if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true'){
																			Ext.getCmp('filterPanel').setHeight(TR.conf.layout.west_dataelements_collapse_filter_panel);
																		}
																		else{
																			Ext.getCmp('filterPanel').setHeight(TR.conf.layout.west_dataelements_collapse_aggregate_filter_panel);
																		}
																		this.setIcon('images/arrowup.png');
																		this.up = true;
																	}
																}
															}
														]
													},
													{
														xtype: 'panel',
														layout: 'column',
														id: 'filterPanel',
														bodyStyle: 'background-color:transparent; padding:10px 10px 0px 3px',
														autoScroll: true,
														overflowX: 'hidden',
														overflowY: 'auto',
														height: TR.conf.layout.west_dataelements_filter_panel,
														width: (TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor) ,
														items: []
													}
												]
											},
										],
										listeners: {
											added: function() {
												TR.cmp.params.dataelement.panel = this;
											}
										}
									},
											
									// OPTIONS
									{
										title: '<div style="height:17px;background-image:url(images/options.png); background-repeat:no-repeat; padding-left:20px;">' + TR.i18n.options + '</div>',
										hideCollapseTool: true,
										cls: 'tr-accordion-options',
										items: [
											{
												xtype: 'fieldset',
												title: TR.i18n.position,
												id: 'positionField',
												layout: 'anchor',
												collapsible: false,
												collapsed: false,
												defaults: {
													anchor: '100%',
													labelStyle: 'padding-left:4px;'
												},
												items: [
													{
														xtype: 'combobox',
														cls: 'tr-combo',
														id: 'positionOrgunitCbx',
														fieldLabel: TR.i18n.orgunit,
														labelWidth: 135,
														emptyText: TR.i18n.please_select,
														queryMode: 'local',
														editable: false,
														valueField: 'value',
														displayField: 'name',
														width: ( TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor ) - 40,
														store:  new Ext.data.ArrayStore({
															fields: ['value', 'name'],
															data: [ ['1', TR.i18n.rows], 
																	['2', TR.i18n.columns], 
																	['3', TR.i18n.filters] ]
														}),
														value: '1',
														listeners: {
															added: function() {
																TR.cmp.settings.positionOrgunit = this;
															}
														}
													},
													{
														xtype: 'combobox',
														cls: 'tr-combo',
														id: 'positionPeriodCbx',
														fieldLabel: TR.i18n.period,
														labelWidth: 135,
														emptyText: TR.i18n.please_select,
														queryMode: 'local',
														editable: false,
														valueField: 'value',
														displayField: 'name',
														width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 40,
														store:  new Ext.data.ArrayStore({
															fields: ['value', 'name'],
															data: [ ['1', TR.i18n.rows], 
																	['2', TR.i18n.columns], 
																	['3', TR.i18n.filters] ]
														}),
														value: '2',
														listeners: {
															added: function() {
																TR.cmp.settings.positionPeriod = this;
															}
														}
													},
													{
														xtype: 'combobox',
														cls: 'tr-combo',
														id: 'positionDataCbx',
														fieldLabel: TR.i18n.data,
														labelWidth: 135,
														emptyText: TR.i18n.please_select,
														queryMode: 'local',
														editable: false,
														valueField: 'value',
														displayField: 'name',
														width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 40,
														store:  new Ext.data.ArrayStore({
															fields: ['value', 'name'],
															data: [ ['1', TR.i18n.rows], 
																	['2', TR.i18n.columns], 
																	['3', TR.i18n.filters] ]
														}),
														value: '3',
														listeners: {
															added: function() {
																TR.cmp.settings.positionData = this;
															}
														}
													}
												]
											},
											{
												xtype: 'fieldset',
												layout: 'anchor',
												collapsible: false,
												collapsed: false,
												defaults: {
														anchor: '100%',
														labelStyle: 'padding-left:4px;'
												},
												items: [
												{
													xtype: 'radiogroup',
													id: 'aggregateType',
													fieldLabel: TR.i18n.aggregate_type,
													labelWidth: 135,
													columns: 3,
													vertical: true,
													items: [{
														boxLabel: TR.i18n.count,
														name: 'aggregateType',
														inputValue: 'count',
														checked: true
													}, 
													{
														boxLabel: TR.i18n.sum,
														name: 'aggregateType',
														inputValue: 'sum'
													}, 
													{
														boxLabel: TR.i18n.avg,
														name: 'aggregateType',
														inputValue: 'avg'
													}],
													listeners: {
														change : function(thisFormField, newValue, oldValue, eOpts) {
														  var opt = newValue.aggregateType[0];
														  
														  if( opt==oldValue.aggregateType && newValue.aggregateType.length > 1){
															opt = newValue.aggregateType[1];
														  }
														  
														  if (opt=='sum' || opt=='avg') {
															Ext.getCmp('deSumCbx').enable();
														  }
														  else  if (opt=='count'){
															Ext.getCmp('deSumCbx').disable();
														  }
														}
													}
												},
												{
													xtype: 'combobox',
													cls: 'tr-combo',
													id: 'deSumCbx',
													disabled: true,
													fieldLabel: TR.i18n.sum_avg_of,
													labelWidth: 135,
													emptyText: TR.i18n.please_select,
													queryMode: 'local',
													editable: true,
													typeAhead: true,
													valueField: 'id',
													displayField: 'name',
													width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 40,
													store: TR.store.aggregateDataelement,
													listeners: {
														added: function() {
															TR.cmp.settings.aggregateDataelement = this;
														}
													}
												},
												{
													xtype: 'panel',
													layout: 'column',
													bodyStyle: 'border-style:none; background-color:transparent;',
													items:[
														{
															xtype: 'checkbox',
															cls: 'tr-checkbox',
															id: 'completedEventsOpt',
															style:'padding: 0px 0px 0px 3px;',
															boxLabel: TR.i18n.use_completed_events,
															boxLabelAlign: 'before',
															labelWidth: 135
														},
														{
															xtype: 'checkbox',
															cls: 'tr-checkbox',
															id: 'displayTotalsOpt',
															style:'padding-left: 20px;',
															boxLabel: TR.i18n.display_totals,
															boxLabelAlign: 'before',
															labelWidth: 135
														},
													]
												},
												
												{
													xtype: 'combobox',
													cls: 'tr-combo',
													id: 'facilityLBCombobox',
													fieldLabel: TR.i18n.use_data_from_level,
													labelWidth: 135,
													emptyText: TR.i18n.please_select,
													queryMode: 'local',
													editable: false,
													valueField: 'value',
													displayField: 'name',
													width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 40,
													store:  new Ext.data.ArrayStore({
														fields: ['value', 'name'],
														data: [['all', TR.i18n.all], ['childrenOnly', TR.i18n.children_only], ['selected', TR.i18n.selected]],
													}),
													value: 'all',
													listeners: {
														added: function() {
															TR.cmp.settings.facilityLB = this;
														}
													}
												},
												{
													xtype: 'combobox',
													cls: 'tr-combo',
													id:'levelCombobox',
													hidden: true,
													fieldLabel: TR.i18n.show_hierachy_from_level,
													labelWidth: 135,
													name: TR.conf.finals.programs,
													emptyText: TR.i18n.please_select,
													queryMode: 'local',
													editable: false,
													valueField: 'value',
													displayField: 'name',
													width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 40,
													store: Ext.create('Ext.data.Store', {
														fields: ['value', 'name'],
														data: TR.init.system.level,
													}),
													value: '1',
													listeners: {
														added: function() {
															TR.cmp.settings.level = this;
														}
													}
												},
												{
													xtype: 'combobox',
													cls: 'tr-combo',
													id: 'dataElementGroupByCbx',
													fieldLabel: TR.i18n.group_by,
													labelWidth: 135,
													emptyText: TR.i18n.please_select,
													queryMode: 'local',
													typeAhead: true,
													editable: true,
													valueField: 'id',
													displayField: 'name',
													width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 40,
													store: TR.store.groupbyDataelement,
													listeners: {
														added: function() {
															TR.cmp.settings.dataElementGroupBy = this;
														},
														select: function(cb) {
															if( cb.getValue()!=null && cb.getValue()!='' 
																&& Ext.getCmp('positionDataCbx').getValue() !='1'){
																if( Ext.getCmp('positionOrgunitCbx').getValue() == '1' ){
																	Ext.getCmp('positionOrgunitCbx').setValue('3');
																	Ext.getCmp('positionPeriodCbx').setValue('2');
																}
																else {
																	Ext.getCmp('positionOrgunitCbx').setValue('2');
																	Ext.getCmp('positionPeriodCbx').setValue('3');
																}
																Ext.getCmp('positionDataCbx').setValue('1');
																Ext.getCmp('aggregateType').items.items[1].setValue(false);
																Ext.getCmp('aggregateType').items.items[2].setValue(false);
																Ext.getCmp('aggregateType').items.items[1].disable();
																Ext.getCmp('aggregateType').items.items[2].disable();
																Ext.getCmp('aggregateType').items.items[0].setValue(true);
															}
															else
															{
																Ext.getCmp('aggregateType').items.items[1].enable();
																Ext.getCmp('aggregateType').items.items[2].enable();
															}
														}
													}
												},
												{
													xtype: 'numberfield',
													id: 'limitOption',
													fieldLabel: TR.i18n.limit_records,
													labelSeparator: '',
													labelWidth: 135,
													editable: true,
													allowBlank:true,
													width: TR.conf.layout.west_fieldset_width - TR.conf.layout.west_width_subtractor - 30,
													minValue: 1,
													listeners: {
														added: function() {
															TR.cmp.settings.limitOption = this;
														}
													}
												}
												]
											}
										]
									}
								
								]
							}
						]
					}
				],
                listeners: {
                    added: function() {
                        TR.cmp.region.west = this;
                    },
                    collapse: function() {                    
                        this.collapsed = true;
                        TR.cmp.toolbar.resizewest.setText('>>>');
                    },
                    expand: function() {
                        this.collapsed = false;
                        TR.cmp.toolbar.resizewest.setText('<<<');
                    }
                }
            },
			// button for main form
            {
                id: 'center',
                region: 'center',
                layout: 'fit',
                bodyStyle: 'padding-top:0px, padding-bottom:0px',
                tbar: {
                    xtype: 'toolbar',
                    cls: 'tr-toolbar',
                    height: TR.conf.layout.center_tbar_height,
                    defaults: {
                        height: 26
                    },
                    items: [
					{
						xtype: 'button',
						name: 'resizewest',
						cls: 'tr-toolbar-btn-2',
						text: '<<<',
						tooltip: TR.i18n.show_hide_settings,
						handler: function() {
							var p = TR.cmp.region.west;
							if (p.collapsed) {
								p.expand();
							}
							else {
								p.collapse();
							}
						},
						listeners: {
							added: function() {
								TR.cmp.toolbar.resizewest = this;
							}
						}
					},
					{
						xtype: 'button',
						cls: 'tr-toolbar-btn-1',
						text: TR.i18n.update,
						handler: function() {
							TR.exe.execute();
						}
					},
					{
						xtype: 'button',
						text: TR.i18n.clear_filter,
						id: 'btnClean',
						disabled: true,
						handler: function() {
							if(Ext.getCmp('reportTypeGroup').getValue().reportType=='true')
							{
							
								TR.cmp.params.dataelement.selected.store.each( function(r) {
									var deId = r.data.id;
									var length = Ext.getCmp('filterPanel_' + deId).items.length/4;
									for(var idx=0;idx<length;idx++)
									{					
										var id = deId + '_' + idx;
										Ext.getCmp('filter_' + id).setValue('');
									}
								});
							}
							else
							{
								TR.store.dataelement.selected.removeAll();
								Ext.getCmp('filterPanel').removeAll();
								Ext.getCmp('filterPanel').doLayout();
							}
							TR.exe.execute();
						}
					},
					{
						xtype: 'button',
						text: TR.i18n.sort_by,
						id: 'btnSortBy',
						disabled: true,
						menu: {},
						listeners: {
							afterrender: function(b) {
								this.menu = Ext.create('Ext.menu.Menu', {
									margin: '2 0 0 0',
									shadow: false,
									showSeparator: false,
									items: [
										{
											text: TR.i18n.asc,
											iconCls: 'tr-menu-item-asc',
											minWidth: 105,
											handler: function() {
												TR.state.orderByOrgunitAsc = "true";
												TR.exe.execute(false, true );
											}
										},
										{
											text: TR.i18n.desc,
											iconCls: 'tr-menu-item-desc',
											minWidth: 105,
											handler: function() {
												TR.state.orderByOrgunitAsc = "false";
												TR.exe.execute(false, true );
											}
										}
									]                                            
								});
							}
						}
					},
					{
						xtype: 'button',
						cls: 'tr-toolbar-btn-2',
						id: 'caseBasedFavoriteBtn',
						text: TR.i18n.favorites,
						menu: {},
						hidden: true,
						handler: function() {
							TR.cmp.caseBasedFavorite.window = TR.app.CaseFavoriteWindow();
							TR.cmp.caseBasedFavorite.window.show();
						},
						listeners: {
							added: function() {
								TR.cmp.toolbar.favorite = this;
							}
						}
					},
					
					// Aggregate Favorite button
					{
						xtype: 'button',
						cls: 'tr-toolbar-btn-2',
						id: 'aggregateFavoriteBtn',
						text: TR.i18n.favorites,
						menu: {},
						handler: function() {
							if (TR.cmp.aggregateFavorite.window) {
								TR.cmp.aggregateFavorite.window.destroy();
							}
							
							TR.cmp.aggregateFavorite.window = TR.app.AggregateFavoriteWindow();
							TR.cmp.aggregateFavorite.window.show();
						},
						listeners: {
							added: function() {
								TR.cmp.toolbar.favorite = this;
							}
						}
					},
					{
						xtype: 'button',
						text: TR.i18n.download,
						menu: {},
						execute: function(type) {
							TR.exe.execute( type );
						},
						listeners: {
							afterrender: function(b) {
								this.menu = Ext.create('Ext.menu.Menu', {
									margin: '2 0 0 0',
									shadow: false,
									showSeparator: false,
									items: [
										{
											text: TR.i18n.xls,
											iconCls: 'tr-menu-item-xls',
											minWidth: 105,
											handler: function() {
												b.execute(TR.conf.finals.download.xls);
											}
										},
										{
											text: TR.i18n.pdf,
											iconCls: 'tr-menu-item-pdf',
											id: 'downloadPdfIcon',
											minWidth: 105,
											handler: function() {
												b.execute(TR.conf.finals.download.pdf);
											}
										},
										{
											text: TR.i18n.csv,
											iconCls: 'tr-menu-item-csv',
											id: 'downloadCvsIcon',
											minWidth: 105,
											handler: function() {
												b.execute(TR.conf.finals.download.csv);
											}
										}										
									]                                            
								});
							}
						}
					},
					'->',
					{
						xtype: 'button',
						cls: 'tr-toolbar-btn-2',
						text: TR.i18n.home,
						handler: function() {
							window.location.href = TR.conf.finals.ajax.path_commons + TR.conf.finals.ajax.redirect;
						}
					},]
                },
                bbar: {
					items: [
						{
							xtype: 'panel',
							cls: 'tr-statusbar',
							height: 24,
							listeners: {
								added: function() {
									TR.cmp.statusbar.panel = this;
								}
							}
						}
					]
				},					
                listeners: {
                    added: function() {
                        TR.cmp.region.center = this;
                    },
                    resize: function() {
						if (TR.cmp.statusbar.panel) {
							TR.cmp.statusbar.panel.setWidth(TR.cmp.region.center.getWidth());
						}
					}
                }
            },
            {
                region: 'east',
                preventHeader: true,
                collapsible: true,
                collapsed: true,
                collapseMode: 'mini',
                listeners: {
                    afterrender: function() {
                        TR.cmp.region.east = this;
                    }
                }
            }
        ],
        listeners: {
            afterrender: function(vp) {
                TR.init.initialize(vp);
				Ext.getCmp('reportTypeGroup').setValue(true);
				Ext.getCmp('limitOption').setVisible(false);
				dataElementTabTitle.innerHTML = TR.i18n.data_elements;
				Ext.getCmp('limitOption').setVisible(false);
				Ext.getCmp('dataElementGroupByCbx').setVisible(false);
				Ext.getCmp('deSumCbx').setVisible(false);
				Ext.getCmp('aggregateType').setVisible(false);
				Ext.getCmp('downloadPdfIcon').setVisible(false);
				Ext.getCmp('downloadCvsIcon').setVisible(false);
				Ext.getCmp('positionField').setVisible(false);
				Ext.getCmp('aggregateFavoriteBtn').setVisible(false);
				Ext.getCmp('datePeriodRangeDiv').setVisible(false);
				Ext.getCmp('caseBasedFavoriteBtn').setVisible(true);
				Ext.getCmp('levelCombobox').setVisible(true);
				Ext.getCmp('patientPropertiesDiv').setVisible(true);
				
				Ext.getCmp('dateRangeDiv').setVisible(true);
				Ext.getCmp('relativePeriodsDiv').setVisible(false); 
				Ext.getCmp('fixedPeriodsDiv').setVisible(false);
				Ext.getCmp('dateRangeDiv').expand();
				
				TR.state.orgunitIds = [];
				for( var i in TR.init.system.rootnodes){
					TR.state.orgunitIds.push( TR.init.system.rootnodes[i].localid );
				}
            },
            resize: function(vp) {
                TR.cmp.region.west.setWidth(TR.conf.layout.west_width);
                
				TR.util.viewport.resizeParams();
                
                if (TR.datatable.datatable) {
                    TR.datatable.datatable.setHeight( TR.util.viewport.getSize().y - 68 );
                }
            } 
        }
    });
    
    }});
}); 
