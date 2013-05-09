PT.app = {};
PT.app.init = {};

Ext.onReady( function() {
	Ext.Ajax.method = 'GET';
	Ext.QuickTips.init();

	document.body.oncontextmenu = function() {
		return false;
	};

    Ext.override(Ext.LoadMask, {
		onHide: function() {
			this.callParent();
		}
	});

	// Init

	var pt = PT.core.getInstance();

	PT.app.instances = [pt];

	PT.app.getInits = function(r) {
		var init = Ext.decode(r.responseText);

		for (var i = 0; i < init.rootNodes.length; i++) {
			init.rootNodes[i].path = '/' + pt.conf.finals.root.id + '/' + init.rootNodes[i].id;
		}

		// Ougs
		for (var i = 0, dim = pt.conf.finals.dimension, oug; i < init.ougs.length; i++) {
			oug = init.ougs[i];
			oug.dimensionName = oug.id;
			oug.objectName = pt.conf.finals.dimension.organisationUnitGroupSet.objectName;
			dim.objectNameMap[oug.id] = oug;
		}

		// Degs
		for (var i = 0, dim = pt.conf.finals.dimension, deg; i < init.degs.length; i++) {
			deg = init.degs[i];
			deg.dimensionName = deg.id;
			deg.objectName = pt.conf.finals.dimension.dataElementGroupSet.objectName;
			dim.objectNameMap[deg.id] = deg;
		}

		init.afterRender = function() {

			// Resize event handler
			pt.viewport.westRegion.on('resize', function() {
				var panel = pt.util.dimension.panel.getExpanded();

				if (panel) {
					panel.onExpand();
				}
			});

			// Left gui
			var viewportHeight = pt.viewport.westRegion.getHeight(),
				numberOfTabs = pt.init.ougs.length + pt.init.degs.length + 5,
				tabHeight = 28,
				minPeriodHeight = 380;

			if (viewportHeight > numberOfTabs * tabHeight + minPeriodHeight) {
				if (!Ext.isIE) {
					pt.viewport.accordion.setAutoScroll(false);
					pt.viewport.westRegion.setWidth(pt.conf.layout.west_width);
					pt.viewport.accordion.doLayout();
				}
			}
			else {
				pt.viewport.westRegion.hasScrollbar = true;
			}

			pt.cmp.dimension.panels[0].expand();

			// Load favorite from url
			var id = pt.util.url.getUrlParam('id');

			if (id) {
				pt.util.pivot.loadTable(id);
			}

			// Fade in
			Ext.defer( function() {
				Ext.getBody().fadeIn({
					duration: 400
				});
			}, 500 );
		};

		return init;
	};

	PT.app.getUtils = function() {
		var util = pt.util || {};

		util.dimension = {
			panel: {
				setHeight: function(mx) {
					var panelHeight = pt.cmp.dimension.panels.length * 28,
						height;

					if (pt.viewport.westRegion.hasScrollbar) {
						height = panelHeight + mx;
						pt.viewport.accordion.setHeight(pt.viewport.getHeight() - 2);
						pt.viewport.accordionBody.setHeight(height - 2);
					}
					else {
						height = pt.viewport.westRegion.getHeight() - pt.conf.layout.west_fill;
						mx += panelHeight;
						pt.viewport.accordion.setHeight((height > mx ? mx : height) - 2);
						pt.viewport.accordionBody.setHeight((height > mx ? mx : height) - 2);
					}
				},

				getExpanded: function() {
					for (var i = 0, panel; i < pt.cmp.dimension.panels.length; i++) {
						panel = pt.cmp.dimension.panels[i];

						if (!panel.collapsed) {
							return panel;
						}
					}

					return null;
				}
			}
		};

		util.window = {
			setAnchorPosition: function(w, target) {
				var vpw = pt.viewport.getWidth(),
					targetx = target ? target.getPosition()[0] : 4,
					winw = w.getWidth(),
					y = target ? target.getPosition()[1] + target.getHeight() + 4 : 33;

				if ((targetx + winw) > vpw) {
					w.setPosition((vpw - winw - 2), y);
				}
				else {
					w.setPosition(targetx, y);
				}
			},
			addHideOnBlurHandler: function(w) {
				var el = Ext.get(Ext.query('.x-mask')[0]);

				el.on('click', function() {
					if (w.hideOnBlur) {
						w.hide();
					}
				});

				w.hasHideOnBlurHandler = true;
			},
			addDestroyOnBlurHandler: function(w) {
				var el = Ext.get(Ext.query('.x-mask')[0]);

				el.on('click', function() {
					if (w.destroyOnBlur) {
						w.destroy();
					}
				});

				w.hasDestroyOnBlurHandler = true;
			}
		};

		util.pivot.getLayoutConfig = function() {
			var data = {},
				setup = pt.viewport.layoutWindow ? pt.viewport.layoutWindow.getSetup() : {},
				getData,
				extendLayout,
				config;

			config = {
				col: [],
				row: [],
				filter: [],
				objects: [],
				userOrganisationUnit: false,
				userOrganisationUnitChildren: false
			};

			getData = function() {
				var panels = pt.cmp.dimension.panels,
					dxItems = [];

				for (var i = 0, dim; i < panels.length; i++) {
					dim = panels[i].getData();

					if (dim) {
						config.objects.push(dim);

						if (dim.dimensionName === pt.conf.finals.dimension.data.dimensionName) {
							dxItems = dxItems.concat(dim.items);
						}
						else {
							data[dim.dimensionName] = dim.items;
						}
					}
				}

				if (dxItems.length) {
					data[pt.conf.finals.dimension.data.dimensionName] = dxItems;
				}
			}();

			extendLayout = function() {
				for (var i = 0, dimensionName; i < setup.col.length; i++) {
					dimensionName = setup.col[i];
					config.col.push({
						dimensionName: dimensionName,
						items: data[dimensionName]
					});
				}

				for (var i = 0, dimensionName; i < setup.row.length; i++) {
					dimensionName = setup.row[i];
					config.row.push({
						dimensionName: dimensionName,
						items: data[dimensionName]
					});
				}

				for (var i = 0, dimensionName; i < setup.filter.length; i++) {
					dimensionName = setup.filter[i];
					config.filter.push({
						dimensionName: dimensionName,
						items: data[dimensionName]
					});
				}
			}();

			config.options = pt.viewport.optionsWindow.getOptions();

			config.options.userOrganisationUnit = pt.viewport.userOrganisationUnit.getValue();
			config.options.userOrganisationUnitChildren = pt.viewport.userOrganisationUnitChildren.getValue();

			return config;
		};

		util.url = {
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
			}
		};

		return util;
	};

	PT.app.getStores = function() {
		var store = pt.store || {};

		store.indicatorAvailable = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				reader: {
					type: 'json',
					root: 'indicators'
				}
			},
			storage: {},
			sortStore: function() {
				this.sort('name', 'ASC');
			},
			listeners: {
				load: function(s) {
					//s.each( function(r) {
						//r.data.name = pt.conf.util.jsonEncodeString(r.data.name);
					//});
					pt.util.store.addToStorage(s);
					pt.util.multiselect.filterAvailable({store: s}, {store: store.indicatorSelected});
				}
			}
		});

		store.indicatorSelected = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: []
		});

		store.dataElementAvailable = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: pt.conf.finals.ajax.path_visualizer + pt.conf.finals.ajax.dataelement_get,
				reader: {
					type: 'json',
					root: 'dataElements'
				}
			},
			storage: {},
			sortStore: function() {
				this.sort('name', 'ASC');
			},
			listeners: {
				load: function(s) {
					//s.each( function(r) {
						//r.data.name = pt.conf.util.jsonEncodeString(r.data.name);
					//});
					pt.util.store.addToStorage(s);
					pt.util.multiselect.filterAvailable({store: s}, {store: store.dataElementSelected});
				}
			}
		});

		store.dataElementSelected = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: []
		});

		store.dataSetAvailable = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: pt.conf.finals.ajax.path_api + pt.conf.finals.ajax.dataset_get,
				reader: {
					type: 'json',
					root: 'dataSets'
				}
			},
			storage: {},
			sortStore: function() {
				this.sort('name', 'ASC');
			},
			isLoaded: false,
			listeners: {
				load: function(s) {
					this.isLoaded = true;
					//s.each( function(r) {
						//r.data.name = pt.conf.util.jsonEncodeString(r.data.name);
					//});
					pt.util.store.addToStorage(s);
					pt.util.multiselect.filterAvailable({store: s}, {store: store.dataSetSelected});
				}
			}
		});

		store.dataSetSelected = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: []
		});

		store.periodType = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: pt.conf.period.periodTypes
		});

		store.fixedPeriodAvailable = Ext.create('Ext.data.Store', {
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
		});

		store.fixedPeriodSelected = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			data: []
		});

		store.tables = Ext.create('Ext.data.Store', {
			fields: ['id', 'name', 'lastUpdated', 'access'],
			proxy: {
				type: 'ajax',
				reader: {
					type: 'json',
					root: 'reportTables'
				}
			},
			isLoaded: false,
			pageSize: 10,
			page: 1,
			defaultUrl: pt.baseUrl + '/api/reportTables.json?links=false',
			loadStore: function(url) {
				this.proxy.url = url || this.defaultUrl;

				this.load({
					params: {
						pageSize: this.pageSize,
						page: this.page
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
			listeners: {
				load: function(s) {
					if (!this.isLoaded) {
						this.isLoaded = true;
					}

					this.sort('name', 'ASC');
				}
			}
		});

		return store;
	};

	PT.app.getCmp = function() {
		var cmp = {};

		cmp.dimension = {
			panels: [],

			indicator: {},
			dataElement: {},
			dataSet: {},
			relativePeriod: {},
			fixedPeriod: {},
			organisationUnit: {}
		};

		cmp.dimension.relativePeriod.checkbox = [];

		cmp.favorite = {};

		return cmp;
	};

	PT.app.LayoutWindow = function() {
		var dimension,
			dimensionStore,
			row,
			rowStore,
			col,
			colStore,
			filter,
			filterStore,
			value,

			getData,
			getStore,
			getStoreKeys,
			getCmpHeight,
			getSetup,

			dimensionPanel,
			selectPanel,
			window,

			margin = 2,
			defaultWidth = 160,
			defaultHeight = 158,
			maxHeight = (pt.viewport.getHeight() - 100) / 2,

			dimConf = pt.conf.finals.dimension;

		getData = function(all) {
			var data = [];

			if (all) {
				data.push({id: dimConf.data.dimensionName, name: dimConf.data.name});
			}

			data.push({id: dimConf.category.dimensionName, name: dimConf.category.name});

			if (all) {
				data.push({id: dimConf.period.dimensionName, name: dimConf.period.name});
				data.push({id: dimConf.organisationUnit.dimensionName, name: dimConf.organisationUnit.name});
			}

			return data.concat(
				pt.util.array.sortObjectsByString(Ext.clone(pt.init.ougs)),
				pt.util.array.sortObjectsByString(Ext.clone(pt.init.degs))
			);
		};

		getStore = function(data) {
			var config = {};

			config.fields = ['id', 'name'];

			if (data) {
				config.data = data;
			}

			return Ext.create('Ext.data.Store', config);
		};

		getStoreKeys = function(store) {
			var keys = [],
				items = store.data.items;

			if (items) {
				for (var i = 0; i < items.length; i++) {
					keys.push(items[i].data.id);
				}
			}

			return keys;
		};

		dimensionStore = getStore(getData());
		dimensionStore.reset = function(all) {
			dimensionStore.removeAll();
			dimensionStore.add(getData(all));
		};
		pt.viewport.dimensionStore = dimensionStore;

		rowStore = getStore();
		pt.viewport.rowStore = rowStore;
		rowStore.add({id: dimConf.period.dimensionName, name: dimConf.period.name});

		colStore = getStore();
		pt.viewport.colStore = colStore;
		colStore.add({id: dimConf.data.dimensionName, name: dimConf.data.name});

		filterStore = getStore();
		pt.viewport.filterStore = filterStore;
		filterStore.add({id: dimConf.organisationUnit.dimensionName, name: dimConf.organisationUnit.name});

		getCmpHeight = function() {
			var size = dimensionStore.totalCount,
				expansion = 10,
				height = defaultHeight,
				diff;

			if (size > 10) {
				diff = size - 10;
				height += (diff * expansion);
			}

			height = height > maxHeight ? maxHeight : height;

			return height;
		};

		dimension = Ext.create('Ext.ux.form.MultiSelect', {
			cls: 'pt-toolbar-multiselect-leftright',
			width: defaultWidth,
			height: (getCmpHeight() * 2) + margin,
			style: 'margin-right:' + margin + 'px; margin-bottom:0px',
			valueField: 'id',
			displayField: 'name',
			dragGroup: 'layoutDD',
			dropGroup: 'layoutDD',
			ddReorder: false,
			store: dimensionStore,
			tbar: {
				height: 25,
				items: {
					xtype: 'label',
					text: PT.i18n.dimensions,
					cls: 'pt-toolbar-multiselect-leftright-label'
				}
			},
			listeners: {
				afterrender: function(ms) {
					ms.store.on('add', function() {
						Ext.defer( function() {
							ms.boundList.getSelectionModel().deselectAll();
						}, 10);
					});
				}
			}
		});

		row = Ext.create('Ext.ux.form.MultiSelect', {
			cls: 'pt-toolbar-multiselect-leftright',
			width: defaultWidth,
			height: getCmpHeight(),
			style: 'margin-bottom:0px',
			valueField: 'id',
			displayField: 'name',
			dragGroup: 'layoutDD',
			dropGroup: 'layoutDD',
			store: rowStore,
			tbar: {
				height: 25,
				items: {
					xtype: 'label',
					text: PT.i18n.row,
					cls: 'pt-toolbar-multiselect-leftright-label'
				}
			},
			listeners: {
				afterrender: function(ms) {
					ms.boundList.on('itemdblclick', function(view, record) {
						ms.store.remove(record);
						dimensionStore.add(record);
					});

					ms.store.on('add', function() {
						Ext.defer( function() {
							ms.boundList.getSelectionModel().deselectAll();
						}, 10);
					});
				}
			}
		});

		col = Ext.create('Ext.ux.form.MultiSelect', {
			cls: 'pt-toolbar-multiselect-leftright',
			width: defaultWidth,
			height: getCmpHeight(),
			style: 'margin-bottom:' + margin + 'px',
			valueField: 'id',
			displayField: 'name',
			dragGroup: 'layoutDD',
			dropGroup: 'layoutDD',
			store: colStore,
			tbar: {
				height: 25,
				items: {
					xtype: 'label',
					text: PT.i18n.column,
					cls: 'pt-toolbar-multiselect-leftright-label'
				}
			},
			listeners: {
				afterrender: function(ms) {
					ms.boundList.on('itemdblclick', function(view, record) {
						ms.store.remove(record);
						dimensionStore.add(record);
					});

					ms.store.on('add', function() {
						Ext.defer( function() {
							ms.boundList.getSelectionModel().deselectAll();
						}, 10);
					});
				}
			}
		});

		filter = Ext.create('Ext.ux.form.MultiSelect', {
			cls: 'pt-toolbar-multiselect-leftright',
			width: defaultWidth,
			height: getCmpHeight(),
			style: 'margin-right:' + margin + 'px; margin-bottom:' + margin + 'px',
			valueField: 'id',
			displayField: 'name',
			dragGroup: 'layoutDD',
			dropGroup: 'layoutDD',
			store: filterStore,
			tbar: {
				height: 25,
				items: {
					xtype: 'label',
					text: PT.i18n.filter,
					cls: 'pt-toolbar-multiselect-leftright-label'
				}
			},
			listeners: {
				afterrender: function(ms) {
					ms.boundList.on('itemdblclick', function(view, record) {
						ms.store.remove(record);
						dimensionStore.add(record);
					});

					ms.store.on('add', function() {
						Ext.defer( function() {
							ms.boundList.getSelectionModel().deselectAll();
						}, 10);
					});
				}
			}
		});

		selectPanel = Ext.create('Ext.panel.Panel', {
			bodyStyle: 'border:0 none',
			items: [
				{
					layout: 'column',
					bodyStyle: 'border:0 none',
					items: [
						filter,
						col
					]
				},
				{
					layout: 'column',
					bodyStyle: 'border:0 none',
					items: [
						row
					]
				}
			]
		});

		getSetup = function() {
			return {
				col: getStoreKeys(colStore),
				row: getStoreKeys(rowStore),
				filter: getStoreKeys(filterStore)
			};
		};

		window = Ext.create('Ext.window.Window', {
			title: PT.i18n.table_layout,
			bodyStyle: 'background-color:#fff; padding:2px',
			closeAction: 'hide',
			autoShow: true,
			modal: true,
			resizable: false,
			getSetup: getSetup,
			dimensionStore: dimensionStore,
			rowStore: rowStore,
			colStore: colStore,
			filterStore: filterStore,
			hideOnBlur: true,
			items: {
				layout: 'column',
				bodyStyle: 'border:0 none',
				items: [
					dimension,
					selectPanel
				]
			},
			bbar: [
				'->',
				{
					text: PT.i18n.hide,
					listeners: {
						added: function(b) {
							b.on('click', function() {
								window.hide();
							});
						}
					}
				},
				{
					text: '<b>' + PT.i18n.update + '</b>',
					listeners: {
						added: function(b) {
							b.on('click', function() {
								pt.viewport.updateViewport();
								window.hide();
							});
						}
					}
				}
			],
			listeners: {
				show: function(w) {
					pt.util.window.setAnchorPosition(w, pt.viewport.layoutButton);

					if (!w.hasHideOnBlurHandler) {
						pt.util.window.addHideOnBlurHandler(w);
					}
				}
			}
		});

		return window;
	};

	PT.app.OptionsWindow = function() {
		var showTotals,
			showSubTotals,
			hideEmptyRows,
			digitGroupSeparator,
			displayDensity,
			fontSize,
			reportingPeriod,
			organisationUnit,
			parentOrganisationUnit,

			data,
			style,
			parameters,

			window;

		showTotals = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: PT.i18n.show_totals,
			style: 'margin-bottom:4px',
			checked: true
		});
		pt.viewport.showTotals = showTotals;

		showSubTotals = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: PT.i18n.show_subtotals,
			style: 'margin-bottom:4px',
			checked: true
		});
		pt.viewport.showSubTotals = showSubTotals;

		hideEmptyRows = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: PT.i18n.hide_empty_rows,
			style: 'margin-bottom:4px'
		});
		pt.viewport.hideEmptyRows = hideEmptyRows;

		displayDensity = Ext.create('Ext.form.field.ComboBox', {
			cls: 'pt-combo',
			style: 'margin-bottom:3px',
			width: 250,
			labelWidth: 130,
			fieldLabel: PT.i18n.display_density,
			labelStyle: 'color:#333',
			queryMode: 'local',
			valueField: 'id',
			editable: false,
			value: 'normal',
			store: Ext.create('Ext.data.Store', {
				fields: ['id', 'text'],
				data: [
					{id: 'comfortable', text: PT.i18n.comfortable},
					{id: 'normal', text: PT.i18n.normal},
					{id: 'compact', text: PT.i18n.compact}
				]
			})
		});
		pt.viewport.displayDensity = displayDensity;

		fontSize = Ext.create('Ext.form.field.ComboBox', {
			cls: 'pt-combo',
			style: 'margin-bottom:3px',
			width: 250,
			labelWidth: 130,
			fieldLabel: PT.i18n.font_size,
			labelStyle: 'color:#333',
			queryMode: 'local',
			valueField: 'id',
			editable: false,
			value: 'normal',
			store: Ext.create('Ext.data.Store', {
				fields: ['id', 'text'],
				data: [
					{id: 'large', text: PT.i18n.large},
					{id: 'normal', text: PT.i18n.normal},
					{id: 'small', text: PT.i18n.small_}
				]
			})
		});
		pt.viewport.fontSize = fontSize;

		digitGroupSeparator = Ext.create('Ext.form.field.ComboBox', {
			labelStyle: 'color:#333',
			cls: 'pt-combo',
			style: 'margin-bottom:3px',
			width: 250,
			labelWidth: 130,
			fieldLabel: PT.i18n.digit_group_separator,
			queryMode: 'local',
			valueField: 'id',
			editable: false,
			value: 'space',
			store: Ext.create('Ext.data.Store', {
				fields: ['id', 'text'],
				data: [
					{id: 'comma', text: 'Comma'},
					{id: 'space', text: 'Space'},
					{id: 'none', text: 'None'}
				]
			})
		});
		pt.viewport.digitGroupSeparator = digitGroupSeparator;

		reportingPeriod = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: PT.i18n.reporting_period,
			style: 'margin-bottom:4px',
		});
		pt.viewport.reportingPeriod = reportingPeriod;

		organisationUnit = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: PT.i18n.organisation_unit,
			style: 'margin-bottom:4px',
		});
		pt.viewport.organisationUnit = organisationUnit;

		parentOrganisationUnit = Ext.create('Ext.form.field.Checkbox', {
			boxLabel: PT.i18n.parent_organisation_unit,
			style: 'margin-bottom:4px',
		});
		pt.viewport.parentOrganisationUnit = parentOrganisationUnit;

		data = {
			bodyStyle: 'border:0 none',
			style: 'margin-left:14px',
			items: [
				showTotals,
				showSubTotals,
				hideEmptyRows
			]
		};

		style = {
			bodyStyle: 'border:0 none',
			style: 'margin-left:14px',
			items: [
				displayDensity,
				fontSize,
				digitGroupSeparator
			]
		};

		parameters = {
			bodyStyle: 'border:0 none',
			style: 'margin-left:14px',
			items: [
				reportingPeriod,
				organisationUnit,
				parentOrganisationUnit
			]
		};

		window = Ext.create('Ext.window.Window', {
			title: PT.i18n.table_options,
			bodyStyle: 'background-color:#fff; padding:8px 8px 8px',
			closeAction: 'hide',
			autoShow: true,
			modal: true,
			resizable: false,
			hideOnBlur: true,
			getOptions: function() {
				return {
					showTotals: showTotals.getValue(),
					showSubTotals: showSubTotals.getValue(),
					hideEmptyRows: hideEmptyRows.getValue(),
					displayDensity: displayDensity.getValue(),
					fontSize: fontSize.getValue(),
					digitGroupSeparator: digitGroupSeparator.getValue(),
					reportingPeriod: reportingPeriod.getValue(),
					organisationUnit: organisationUnit.getValue(),
					parentOrganisationUnit: parentOrganisationUnit.getValue()
				};
			},
			items: [
				{
					bodyStyle: 'border:0 none; color:#222; font-size:12px; font-weight:bold',
					style: 'margin-bottom:6px',
					html: PT.i18n.data
				},
				data,
				{
					bodyStyle: 'border:0 none; padding:7px'
				},
				{
					bodyStyle: 'border:0 none; color:#222; font-size:12px; font-weight:bold',
					style: 'margin-bottom:6px',
					html: PT.i18n.style
				},
				style,
				{
					bodyStyle: 'border:0 none; padding:7px'
				},
				{
					bodyStyle: 'border:0 none; color:#222; font-size:12px',
					style: 'margin-bottom:6px',
					html: '<b>' + PT.i18n.parameters + '</b> <span style="font-size:11px"> (' + PT.i18n.for_standard_reports_only + ')</span>'
				},
				parameters
			],
			bbar: [
				'->',
				{
					text: PT.i18n.hide,
					handler: function() {
						window.hide();
					}
				},
				{
					text: '<b>' + PT.i18n.update + '</b>',
					handler: function() {
						pt.viewport.updateViewport();
						window.hide();
					}
				}
			],
			listeners: {
				show: function(w) {
					pt.util.window.setAnchorPosition(w, pt.viewport.optionsButton);

					if (!w.hasHideOnBlurHandler) {
						pt.util.window.addHideOnBlurHandler(w);
					}
				}
			}
		});

		return window;
	};

	PT.app.FavoriteWindow = function() {

		// Objects
		var NameWindow,

		// Instances
			nameWindow,

		// Functions
			getBody,

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

		pt.store.tables.on('load', function(store, records) {
			var pager = store.proxy.reader.jsonData.pager;

			info.setText('Page ' + pager.page + ' of ' + pager.pageCount);

			prevButton.enable();
			nextButton.enable();

			if (pager.page === 1) {
				prevButton.disable();
			}

			if (pager.page === pager.pageCount) {
				nextButton.disable();
			}
		});

		getBody = function() {
			var favorite;

			if (pt.xLayout) {
				favorite = Ext.clone(pt.xLayout.options);

				// Server sync
				favorite.totals = favorite.showTotals;
				delete favorite.showTotals;

				favorite.subtotals = favorite.showSubTotals;
				delete favorite.showSubTotals;

				favorite.reportParams = {
					paramReportingPeriod: favorite.reportingPeriod,
					paramOrganisationUnit: favorite.organisationUnit,
					paramParentOrganisationUnit: favorite.parentOrganisationUnit
				};
				delete favorite.reportingPeriod;
				delete favorite.organisationUnit;
				delete favorite.parentOrganisationUnit;

				// Dimensions
				for (var i = 0, obj, key, items; i < pt.xLayout.objects.length; i++) {
					obj = pt.xLayout.objects[i];

					if (obj.objectName === pt.conf.finals.dimension.period.objectName) {
						for (var j = 0, item; j < obj.items.length; j++) {
							item = obj.items[j];

							if (pt.conf.period.relativePeriodValueKeys[item]) {
								key = pt.conf.finals.dimension.relativePeriod.value;

								if (!favorite[key]) {
									favorite[key] = {};
								}

								favorite[key][pt.conf.period.relativePeriodValueKeys[item]] = true;
							}
							else {
								key = pt.conf.finals.dimension.fixedPeriod.value;

								if (!favorite[key]) {
									favorite[key] = [];
								}

								favorite[key].push({
									id: item
								});
							}
						}
					}
					else if (obj.objectName === pt.conf.finals.dimension.organisationUnitGroupSet.objectName ||
							 obj.objectName === pt.conf.finals.dimension.dataElementGroupSet.objectName) {
						key = pt.conf.finals.dimension.objectNameMap[obj.objectName].value;

						if (!favorite[key]) {
							favorite[key] = {};
						}

						favorite[key][obj.dimensionName] = [];

						for (var j = 0, item; j < obj.items.length; j++) {
							item = obj.items[j];

							favorite[key][obj.dimensionName].push({
								id: item
							});
						}
					}
					else {
						key = pt.conf.finals.dimension.objectNameMap[obj.objectName].value;
						favorite[key] = [];

						for (var j = 0, item; j < obj.items.length; j++) {
							item = obj.items[j];

							favorite[key].push({
								id: item
							});
						}
					}
				}

				// Relative periods PUT workaround
				if (!favorite.relativePeriods) {
					favorite.relativePeriods = {};
				}

				// Layout
				if (pt.xLayout.col) {
					var a = [];

					for (var i = 0; i < pt.xLayout.col.length; i++) {
						a.push(pt.xLayout.col[i].dimensionName);
					}

					favorite.columnDimensions = a;
				}

				if (pt.xLayout.row) {
					var a = [];

					for (var i = 0; i < pt.xLayout.row.length; i++) {
						a.push(pt.xLayout.row[i].dimensionName);
					}

					favorite.rowDimensions = a;
				}

				if (pt.xLayout.filter) {
					var a = [];

					for (var i = 0; i < pt.xLayout.filter.length; i++) {
						a.push(pt.xLayout.filter[i].dimensionName);
					}

					favorite.filterDimensions = a;
				}
			}

			return favorite;
		};

		NameWindow = function(id) {
			var window,
				record = pt.store.tables.getById(id);

			nameTextfield = Ext.create('Ext.form.field.Text', {
				height: 26,
				width: 371,
				fieldStyle: 'padding-left: 6px; border-radius: 1px; border-color: #bbb; font-size:11px',
				style: 'margin-bottom:0',
				emptyText: 'Favorite name',
				value: id ? record.data.name : '',
				listeners: {
					afterrender: function() {
						this.focus();
					}
				}
			});

			createButton = Ext.create('Ext.button.Button', {
				text: PT.i18n.create,
				handler: function() {
					var favorite = getBody();
					favorite.name = nameTextfield.getValue	();

					if (favorite && favorite.name) {
						Ext.Ajax.request({
							url: pt.baseUrl + '/api/reportTables/',
							method: 'POST',
							headers: {'Content-Type': 'application/json'},
							params: Ext.encode(favorite),
							failure: function(r) {
								pt.viewport.mask.show();
								alert(r.responseText);
							},
							success: function(r) {
								var id = r.getAllResponseHeaders().location.split('/').pop();

								pt.favorite = favorite;

								pt.store.tables.loadStore();

								//pt.viewport.interpretationButton.enable();

								window.destroy();
							}
						});
					}
				}
			});

			updateButton = Ext.create('Ext.button.Button', {
				text: PT.i18n.update,
				handler: function() {
					var name = nameTextfield.getValue(),
						reportTable;

					if (id && name) {
						Ext.Ajax.request({
							url: pt.baseUrl + '/api/reportTables/' + id + '.json?links=false',
							method: 'GET',
							failure: function(r) {
								pt.viewport.mask.show();
								alert(r.responseText);
							},
							success: function(r) {
								reportTable = Ext.decode(r.responseText);
								reportTable.name = name;

								Ext.Ajax.request({
									url: pt.baseUrl + '/api/reportTables/' + reportTable.id,
									method: 'PUT',
									headers: {'Content-Type': 'application/json'},
									params: Ext.encode(reportTable),
									failure: function(r) {
										pt.viewport.mask.show();
										alert(r.responseText);
									},
									success: function(r) {
										pt.store.tables.loadStore();
										window.destroy();
									}
								});
							}
						});
					}
				}
			});

			cancelButton = Ext.create('Ext.button.Button', {
				text: PT.i18n.cancel,
				handler: function() {
					window.destroy();
				}
			});

			window = Ext.create('Ext.window.Window', {
				title: id ? 'Rename favorite' : 'Create new favorite',
				//iconCls: 'pt-window-title-icon-favorite',
				bodyStyle: 'padding:2px; background:#fff',
				resizable: false,
				modal: true,
				items: nameTextfield,
				destroyOnBlur: true,
				bbar: [
					cancelButton,
					'->',
					id ? updateButton : createButton
				],
				listeners: {
					show: function(w) {
						pt.util.window.setAnchorPosition(w, addButton);

						if (!w.hasDestroyBlurHandler) {
							pt.util.window.addDestroyOnBlurHandler(w);
						}

						pt.viewport.favoriteWindow.destroyOnBlur = false;
					},
					destroy: function() {
						pt.viewport.favoriteWindow.destroyOnBlur = true;
					}
				}
			});

			return window;
		};

		addButton = Ext.create('Ext.button.Button', {
			text: PT.i18n.add_new,
			width: 67,
			height: 26,
			style: 'border-radius: 1px;',
			menu: {},
			disabled: !Ext.isObject(pt.xLayout),
			handler: function() {
				nameWindow = new NameWindow(null, 'create');
				nameWindow.show();
			}
		});

		searchTextfield = Ext.create('Ext.form.field.Text', {
			width: windowCmpWidth - addButton.width - 11,
			height: 26,
			fieldStyle: 'padding-right: 0; padding-left: 6px; border-radius: 1px; border-color: #bbb; font-size:11px',
			emptyText: PT.i18n.search_for_favorites,
			enableKeyEvents: true,
			currentValue: '',
			listeners: {
				keyup: function() {
					if (this.getValue() !== this.currentValue) {
						this.currentValue = this.getValue();

						var value = this.getValue(),
							url = value ? pt.baseUrl + '/api/reportTables/query/' + value + '.json?links=false' : null,
							store = pt.store.tables;

						store.page = 1;
						store.loadStore(url);
					}
				}
			}
		});

		prevButton = Ext.create('Ext.button.Button', {
			text: PT.i18n.prev,
			handler: function() {
				var value = searchTextfield.getValue(),
					url = value ? pt.baseUrl + '/api/reportTables/query/' + value + '.json?links=false' : null,
					store = pt.store.tables;

				store.page = store.page <= 1 ? 1 : store.page - 1;
				store.loadStore(url);
			}
		});

		nextButton = Ext.create('Ext.button.Button', {
			text: PT.i18n.next,
			handler: function() {
				var value = searchTextfield.getValue(),
					url = value ? pt.baseUrl + '/api/reportTables/query/' + value + '.json?links=false' : null,
					store = pt.store.tables;

				store.page = store.page + 1;
				store.loadStore(url);
			}
		});

		info = Ext.create('Ext.form.Label', {
			cls: 'pt-label-info',
			width: 300,
			height: 22
		});

		grid = Ext.create('Ext.grid.Panel', {
			cls: 'pt-grid',
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
									pt.util.pivot.loadTable(record.data.id);
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
							iconCls: 'pt-grid-row-icon-edit',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-edit' + (!record.data.access.update ? ' disabled' : '');
							},
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex);

								if (record.data.access.update) {
									nameWindow = new NameWindow(record.data.id);
									nameWindow.show();
								}
							}
						},
						{
							iconCls: 'pt-grid-row-icon-overwrite',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-overwrite' + (!record.data.access.update ? ' disabled' : '');
							},
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex),
									message,
									favorite;

								if (record.data.access.update) {
									message = PT.i18n.overwrite_favorite + '?\n\n' + record.data.name;
									favorite = getBody();

									if (favorite) {
										favorite.name = record.data.name;

										if (confirm(message)) {
											Ext.Ajax.request({
												url: pt.baseUrl + '/api/reportTables/' + record.data.id,
												method: 'PUT',
												headers: {'Content-Type': 'application/json'},
												params: Ext.encode(favorite),
												success: function() {
													pt.favorite = favorite;
													//pt.viewport.interpretationButton.enable();
													pt.store.tables.loadStore();
												}
											});
										}
									}
									else {
										alert(PT.i18n.please_create_a_table_first);
									}
								}
							}
						},
						{
							iconCls: 'pt-grid-row-icon-sharing',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-sharing' + (!record.data.access.manage ? ' disabled' : '');
							},
							handler: function(grid, rowIndex) {
								var record = this.up('grid').store.getAt(rowIndex);

								if (record.data.access.manage) {
									Ext.Ajax.request({
										url: pt.baseUrl + '/api/sharing?type=reportTable&id=' + record.data.id,
										method: 'GET',
										failure: function(r) {
											pt.viewport.mask.hide();
											alert(r.responseText);
										},
										success: function(r) {
											var sharing = Ext.decode(r.responseText),
												window = PT.app.SharingWindow(sharing);
											window.show();
										}
									});
								}
							}
						},
						{
							iconCls: 'pt-grid-row-icon-delete',
							getClass: function(value, metaData, record) {
								return 'tooltip-favorite-delete' + (!record.data.access['delete'] ? ' disabled' : '');
							},
							handler: function(grid, rowIndex, colIndex, col, event) {
								var record = this.up('grid').store.getAt(rowIndex),
									message;

								if (record.data.access['delete']) {
									message = PT.i18n.delete_favorite + '?\n\n' + record.data.name;

									if (confirm(message)) {
										Ext.Ajax.request({
											url: pt.baseUrl + '/api/reportTables/' + record.data.id,
											method: 'DELETE',
											success: function() {
												pt.store.tables.loadStore();
											}
										});
									}
								}
							}
						}
					]
				},
				{
					sortable: false,
					width: 6
				}
			],
			store: pt.store.tables,
			bbar: [
				info,
				'->',
				prevButton,
				nextButton
			],
			listeners: {
				added: function() {
					pt.viewport.favoriteGrid = this;
				},
				render: function() {
					var size = Math.floor((pt.viewport.centerRegion.getHeight() - 155) / pt.conf.layout.grid_row_height);
					this.store.pageSize = size;
					this.store.page = 1;
					this.store.loadStore();

					pt.store.tables.on('load', function() {
						if (this.isVisible()) {
							this.fireEvent('afterrender');
						}
					}, this);
				},
				afterrender: function() {
					var fn = function() {
						var editArray = Ext.query('.tooltip-favorite-edit'),
							overwriteArray = Ext.query('.tooltip-favorite-overwrite'),
							//dashboardArray = Ext.query('.tooltip-favorite-dashboard'),
							sharingArray = Ext.query('.tooltip-favorite-sharing'),
							deleteArray = Ext.query('.tooltip-favorite-delete'),
							el;

						for (var i = 0; i < editArray.length; i++) {
							var el = editArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: PT.i18n.rename,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}

						for (var i = 0; i < overwriteArray.length; i++) {
							el = overwriteArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: PT.i18n.overwrite,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}

						for (var i = 0; i < sharingArray.length; i++) {
							el = sharingArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: PT.i18n.share_with_other_people,
								'anchor': 'bottom',
								anchorOffset: -14,
								showDelay: 1000
							});
						}

						for (var i = 0; i < deleteArray.length; i++) {
							el = deleteArray[i];
							Ext.create('Ext.tip.ToolTip', {
								target: el,
								html: PT.i18n.delete_,
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
			title: PT.i18n.manage_favorites,
			//iconCls: 'pt-window-title-icon-favorite',
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
					pt.util.window.setAnchorPosition(w, pt.viewport.favoriteButton);

					if (!w.hasDestroyOnBlurHandler) {
						pt.util.window.addDestroyOnBlurHandler(w);
					}
				}
			}
		});

		return favoriteWindow;
	};

	PT.app.SharingWindow = function(sharing) {

		// Objects
		var UserGroupRow,

		// Functions
			getBody,

		// Components
			userGroupStore,
			userGroupField,
			userGroupButton,
			userGroupRowContainer,
			publicGroup,
			window;

		UserGroupRow = function(obj, isPublicAccess, disallowPublicAccess) {
			var getData,
				store,
				getItems,
				combo,
				getAccess,
				panel;

			getData = function() {
				var data = [
					{id: 'r-------', name: PT.i18n.can_view},
					{id: 'rw------', name: PT.i18n.can_edit_and_view}
				];

				if (isPublicAccess) {
					data.unshift({id: '-------', name: PT.i18n.none});
				}

				return data;
			}

			store = Ext.create('Ext.data.Store', {
				fields: ['id', 'name'],
				data: getData()
			});

			getItems = function() {
				var items = [];

				combo = Ext.create('Ext.form.field.ComboBox', {
					fieldLabel: isPublicAccess ? PT.i18n.public_access : obj.name,
					labelStyle: 'color:#333',
					cls: 'pt-combo',
					width: 380,
					labelWidth: 250,
					queryMode: 'local',
					valueField: 'id',
					displayField: 'name',
					labelSeparator: null,
					editable: false,
					disabled: !!disallowPublicAccess,
					value: obj.access || 'rw------',
					store: store
				});

				items.push(combo);

				if (!isPublicAccess) {
					items.push(Ext.create('Ext.Img', {
						src: 'images/grid-delete_16.png',
						style: 'margin-top:2px; margin-left:7px',
						overCls: 'pointer',
						width: 16,
						height: 16,
						listeners: {
							render: function(i) {
								i.getEl().on('click', function(e) {
									i.up('panel').destroy();
									window.doLayout();
								});
							}
						}
					}));
				}

				return items;
			};

			getAccess = function() {
				return {
					id: obj.id,
					name: obj.name,
					access: combo.getValue()
				};
			};

			panel = Ext.create('Ext.panel.Panel', {
				layout: 'column',
				bodyStyle: 'border:0 none',
				getAccess: getAccess,
				items: getItems()
			});

			return panel;
		};

		getBody = function() {
			var body = {
				object: {
					id: sharing.object.id,
					name: sharing.object.name,
					publicAccess: publicGroup.down('combobox').getValue(),
					user: {
						id: pt.init.user.id,
						name: pt.init.user.name
					}
				}
			};

			if (userGroupRowContainer.items.items.length > 1) {
				body.object.userGroupAccesses = [];
				for (var i = 1, item; i < userGroupRowContainer.items.items.length; i++) {
					item = userGroupRowContainer.items.items[i];
					body.object.userGroupAccesses.push(item.getAccess());
				}
			}

			return body;
		};

		// Initialize
		userGroupStore = Ext.create('Ext.data.Store', {
			fields: ['id', 'name'],
			proxy: {
				type: 'ajax',
				url: pt.baseUrl + '/api/sharing/search',
				reader: {
					type: 'json',
					root: 'userGroups'
				}
			}
		});

		userGroupField = Ext.create('Ext.form.field.ComboBox', {
			valueField: 'id',
			displayField: 'name',
			emptyText: PT.i18n.search_for_user_groups,
			queryParam: 'key',
			queryDelay: 200,
			minChars: 1,
			hideTrigger: true,
			fieldStyle: 'height:26px; padding-left:6px; border-radius:1px; font-size:11px',
			style: 'margin-bottom:5px',
			width: 380,
			store: userGroupStore,
			listeners: {
				beforeselect: function(cb) { // beforeselect instead of select, fires regardless of currently selected item
					userGroupButton.enable();
				},
				afterrender: function(cb) {
					cb.inputEl.on('keyup', function() {
						userGroupButton.disable();
					});
				}
			}
		});

		userGroupButton = Ext.create('Ext.button.Button', {
			text: '+',
			style: 'margin-left:2px; padding-right:4px; padding-left:4px; border-radius:1px',
			disabled: true,
			height: 26,
			handler: function(b) {
				userGroupRowContainer.add(UserGroupRow({
					id: userGroupField.getValue(),
					name: userGroupField.getRawValue(),
					access: 'r-------'
				}));

				userGroupField.clearValue();
				b.disable();
			}
		});

		userGroupRowContainer = Ext.create('Ext.container.Container', {
			bodyStyle: 'border:0 none'
		});

		publicGroup = userGroupRowContainer.add(UserGroupRow({
			id: sharing.object.id,
			name: sharing.object.name,
			access: sharing.object.publicAccess
		}, true, !sharing.meta.allowPublicAccess));

		if (Ext.isArray(sharing.object.userGroupAccesses)) {
			for (var i = 0, userGroupRow; i < sharing.object.userGroupAccesses.length; i++) {
				userGroupRow = UserGroupRow(sharing.object.userGroupAccesses[i]);
				userGroupRowContainer.add(userGroupRow);
			}
		}

		window = Ext.create('Ext.window.Window', {
			title: PT.i18n.sharing_settings,
			bodyStyle: 'padding:6px 6px 0px; background-color:#fff',
			width: 434,
			resizable: false,
			modal: true,
			destroyOnBlur: true,
			items: [
				{
					html: sharing.object.name,
					bodyStyle: 'border:0 none; font-weight:bold; color:#333',
					style: 'margin-bottom:8px'
				},
				{
					xtype: 'container',
					layout: 'column',
					bodyStyle: 'border:0 none',
					items: [
						userGroupField,
						userGroupButton
					]
				},
				userGroupRowContainer
			],
			bbar: [
				'->',
				{
					text: PT.i18n.save,
					handler: function() {
						Ext.Ajax.request({
							url: pt.baseUrl + '/api/sharing?type=reportTable&id=' + sharing.object.id,
							method: 'POST',
							headers: {
								'Content-Type': 'application/json'
							},
							params: Ext.encode(getBody())
						});

						window.destroy();
					}
				}
			],
			listeners: {
				show: function(w) {
					var pos = pt.viewport.favoriteWindow.getPosition();
					w.setPosition(pos[0] + 5, pos[1] + 5);

					if (!w.hasDestroyOnBlurHandler) {
						pt.util.window.addDestroyOnBlurHandler(w);
					}

					pt.viewport.favoriteWindow.destroyOnBlur = false;
				},
				destroy: function() {
					pt.viewport.favoriteWindow.destroyOnBlur = true;
				}
			}
		});

		return window;
	};

	PT.app.init.onInitialize = function(r) {
		var createViewport;

		createViewport = function() {
			var indicatorAvailable,
				indicatorSelected,
				indicator,
				dataElementAvailable,
				dataElementSelected,
				dataElement,
				dataSetAvailable,
				dataSetSelected,
				dataSet,
				rewind,
				relativePeriod,
				fixedPeriodAvailable,
				fixedPeriodSelected,
				period,
				userOrganisationUnit,
				userOrganisationUnitChildren,
				treePanel,
				organisationUnit,
				groupSetIdAvailableStoreMap = {},
				groupSetIdSelectedStoreMap = {},
				getGroupSetPanels,
				validateSpecialCases,
				update,

				layoutButton,
				optionsButton,
				favoriteButton,
				downloadButton,

				accordionBody,
				accordion,
				westRegion,
				centerRegion,

				setFavorite,

				viewport,
				addListeners;

			indicatorAvailable = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'pt-toolbar-multiselect-left',
				width: (pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				store: pt.store.indicatorAvailable,
				tbar: [
					{
						xtype: 'label',
						text: PT.i18n.available,
						cls: 'pt-toolbar-multiselect-left-label'
					},
					'->',
					{
						xtype: 'button',
						icon: 'images/arrowright.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.select(indicatorAvailable, indicatorSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowrightdouble.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.selectAll(indicatorAvailable, indicatorSelected);
						}
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							pt.util.multiselect.select(this, indicatorSelected);
						}, this);
					}
				}
			});

			indicatorSelected = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'pt-toolbar-multiselect-right',
				width: (pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				ddReorder: true,
				store: pt.store.indicatorSelected,
				tbar: [
					{
						xtype: 'button',
						icon: 'images/arrowleftdouble.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.unselectAll(indicatorAvailable, indicatorSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowleft.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.unselect(indicatorAvailable, indicatorSelected);
						}
					},
					'->',
					{
						xtype: 'label',
						text: PT.i18n.selected,
						cls: 'pt-toolbar-multiselect-right-label'
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							pt.util.multiselect.unselect(indicatorAvailable, this);
						}, this);
					}
				}
			});

			indicator = {
				xtype: 'panel',
				title: '<div class="pt-panel-title-data">' + PT.i18n.indicators + '</div>',
				hideCollapseTool: true,
				getData: function() {
					var data = {
						dimensionName: pt.conf.finals.dimension.indicator.dimensionName,
						objectName: pt.conf.finals.dimension.indicator.objectName,
						items: []
					};

					pt.store.indicatorSelected.each( function(r) {
						data.items.push(r.data.id);
					});

					return data.items.length ? data : null;
				},
				onExpand: function() {
					var h = pt.viewport.westRegion.hasScrollbar ?
						pt.conf.layout.west_scrollbarheight_accordion_indicator : pt.conf.layout.west_maxheight_accordion_indicator;
					pt.util.dimension.panel.setHeight(h);
					pt.util.multiselect.setHeight(
						[indicatorAvailable, indicatorSelected],
						this,
						pt.conf.layout.west_fill_accordion_indicator
					);
				},
				items: [
					{
						xtype: 'combobox',
						cls: 'pt-combo',
						style: 'margin-bottom:2px; margin-top:0px',
						width: pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding,
						valueField: 'id',
						displayField: 'name',
						emptyText: PT.i18n.select_indicator_group,
						editable: false,
						store: {
							xtype: 'store',
							fields: ['id', 'name', 'index'],
							proxy: {
								type: 'ajax',
								url: pt.conf.finals.ajax.path_api + pt.conf.finals.ajax.indicatorgroup_get,
								reader: {
									type: 'json',
									root: 'indicatorGroups'
								}
							},
							listeners: {
								load: function(s) {
									s.add({
										id: 0,
										name: PT.i18n.all_indicator_groups,
										index: -1
									});
									s.sort([
										{
											property: 'index',
											direction: 'ASC'
										},
										{
											property: 'name',
											direction: 'ASC'
										}
									]);
								}
							}
						},
						listeners: {
							select: function(cb) {
								var store = pt.store.indicatorAvailable;
								store.parent = cb.getValue();

								if (pt.util.store.containsParent(store)) {
									pt.util.store.loadFromStorage(store);
									pt.util.multiselect.filterAvailable(indicatorAvailable, indicatorSelected);
								}
								else {
									if (cb.getValue() === 0) {
										store.proxy.url = pt.conf.finals.ajax.path_api + pt.conf.finals.ajax.indicator_getall;
										store.load();
									}
									else {
										store.proxy.url = pt.conf.finals.ajax.path_api + pt.conf.finals.ajax.indicator_get + cb.getValue() + '.json';
										store.load();
									}
								}
							}
						}
					},
					{
						xtype: 'panel',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							indicatorAvailable,
							indicatorSelected
						]
					}
				],
				listeners: {
					added: function() {
						pt.cmp.dimension.panels.push(this);
					},
					expand: function(p) {
						p.onExpand();
					}
				}
			};

			dataElementAvailable = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'pt-toolbar-multiselect-left',
				width: (pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				store: pt.store.dataElementAvailable,
				tbar: [
					{
						xtype: 'label',
						text: PT.i18n.available,
						cls: 'pt-toolbar-multiselect-left-label'
					},
					'->',
					{
						xtype: 'button',
						icon: 'images/arrowright.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.select(dataElementAvailable, dataElementSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowrightdouble.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.selectAll(dataElementAvailable, dataElementSelected);
						}
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							pt.util.multiselect.select(this, dataElementSelected);
						}, this);
					}
				}
			});

			dataElementSelected = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'pt-toolbar-multiselect-right',
				width: (pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				ddReorder: true,
				store: pt.store.dataElementSelected,
				tbar: [
					{
						xtype: 'button',
						icon: 'images/arrowleftdouble.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.unselectAll(dataElementAvailable, dataElementSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowleft.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.unselect(dataElementAvailable, dataElementSelected);
						}
					},
					'->',
					{
						xtype: 'label',
						text: PT.i18n.selected,
						cls: 'pt-toolbar-multiselect-right-label'
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							pt.util.multiselect.unselect(dataElementAvailable, this);
						}, this);
					}
				}
			});

			dataElement = {
				xtype: 'panel',
				title: '<div class="pt-panel-title-data">' + PT.i18n.data_elements + '</div>',
				hideCollapseTool: true,
				getData: function() {
					var data = {
						dimensionName: pt.conf.finals.dimension.dataElement.dimensionName,
						objectName: pt.conf.finals.dimension.dataElement.objectName,
						items: []
					};

					pt.store.dataElementSelected.each( function(r) {
						data.items.push(r.data.id);
					});

					return data.items.length ? data : null;
				},
				onExpand: function() {
					var h = pt.viewport.westRegion.hasScrollbar ?
						pt.conf.layout.west_scrollbarheight_accordion_dataelement : pt.conf.layout.west_maxheight_accordion_dataelement;
					pt.util.dimension.panel.setHeight(h);
					pt.util.multiselect.setHeight(
						[dataElementAvailable, dataElementSelected],
						this,
						pt.conf.layout.west_fill_accordion_indicator
					);
				},
				items: [
					{
						xtype: 'combobox',
						cls: 'pt-combo',
						style: 'margin-bottom:2px; margin-top:0px',
						width: pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding,
						valueField: 'id',
						displayField: 'name',
						emptyText: PT.i18n.select_data_element_group,
						editable: false,
						store: {
							xtype: 'store',
							fields: ['id', 'name', 'index'],
							proxy: {
								type: 'ajax',
								url: pt.conf.finals.ajax.path_api + pt.conf.finals.ajax.dataelementgroup_get,
								reader: {
									type: 'json',
									root: 'dataElementGroups'
								}
							},
							listeners: {
								load: function(s) {
									s.add({
										id: 0,
										name: PT.i18n.all_data_element_groups,
										index: -1
									});
									s.sort([
										{
											property: 'index',
											direction: 'ASC'
										},
										{
											property: 'name',
											direction: 'ASC'
										}
									]);
								}
							}
						},
						listeners: {
							select: function(cb) {
								var store = pt.store.dataElementAvailable;
								store.parent = cb.getValue();

								if (pt.util.store.containsParent(store)) {
									pt.util.store.loadFromStorage(store);
									pt.util.multiselect.filterAvailable(dataElementAvailable, dataElementSelected);
								}
								else {
									if (cb.getValue() === 0) {
										store.proxy.url = pt.conf.finals.ajax.path_api + pt.conf.finals.ajax.dataelement_getall;
										store.load();
									}
									else {
										store.proxy.url = pt.conf.finals.ajax.path_api + pt.conf.finals.ajax.dataelement_get + cb.getValue() + '.json';
										store.load();
									}
								}
							}
						}
					},
					{
						xtype: 'panel',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							dataElementAvailable,
							dataElementSelected
						]
					}
				],
				listeners: {
					added: function() {
						pt.cmp.dimension.panels.push(this);
					},
					expand: function(p) {
						p.onExpand();
					}
				}
			};

			dataSetAvailable = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'pt-toolbar-multiselect-left',
				width: (pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				store: pt.store.dataSetAvailable,
				tbar: [
					{
						xtype: 'label',
						text: PT.i18n.available,
						cls: 'pt-toolbar-multiselect-left-label'
					},
					'->',
					{
						xtype: 'button',
						icon: 'images/arrowright.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.select(dataSetAvailable, dataSetSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowrightdouble.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.selectAll(dataSetAvailable, dataSetSelected);
						}
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							pt.util.multiselect.select(this, dataSetSelected);
						}, this);
					}
				}
			});

			dataSetSelected = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'pt-toolbar-multiselect-right',
				width: (pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding) / 2,
				valueField: 'id',
				displayField: 'name',
				ddReorder: true,
				store: pt.store.dataSetSelected,
				tbar: [
					{
						xtype: 'button',
						icon: 'images/arrowleftdouble.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.unselectAll(dataSetAvailable, dataSetSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowleft.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.unselect(dataSetAvailable, dataSetSelected);
						}
					},
					'->',
					{
						xtype: 'label',
						text: PT.i18n.selected,
						cls: 'pt-toolbar-multiselect-right-label'
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							pt.util.multiselect.unselect(dataSetAvailable, this);
						}, this);
					}
				}
			});

			dataSet = {
				xtype: 'panel',
				title: '<div class="pt-panel-title-data">' + PT.i18n.reporting_rates + '</div>',
				hideCollapseTool: true,
				getData: function() {
					var data = {
						dimensionName: pt.conf.finals.dimension.dataSet.dimensionName,
						objectName: pt.conf.finals.dimension.dataSet.objectName,
						items: []
					};

					pt.store.dataSetSelected.each( function(r) {
						data.items.push(r.data.id);
					});

					return data.items.length ? data : null;
				},
				onExpand: function() {
					var h = pt.viewport.westRegion.hasScrollbar ?
						pt.conf.layout.west_scrollbarheight_accordion_dataset : pt.conf.layout.west_maxheight_accordion_dataset;
					pt.util.dimension.panel.setHeight(h);
					pt.util.multiselect.setHeight(
						[dataSetAvailable, dataSetSelected],
						this,
						pt.conf.layout.west_fill_accordion_dataset
					);

					if (!pt.store.dataSetAvailable.isLoaded) {
						pt.store.dataSetAvailable.load();
					}
				},
				items: [
					{
						xtype: 'panel',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							dataSetAvailable,
							dataSetSelected
						]
					}
				],
				listeners: {
					added: function() {
						pt.cmp.dimension.panels.push(this);
					},
					expand: function(p) {
						p.onExpand();
					}
				}
			};

			rewind = Ext.create('Ext.form.field.Checkbox', {
				relativePeriodId: 'rewind',
				boxLabel: 'Rewind one period',
				xable: function() {
					this.setDisabled(pt.util.checkbox.isAllFalse());
				}
			});

			relativePeriod = {
				xtype: 'panel',
				hideCollapseTool: true,
				autoScroll: true,
				bodyStyle: 'border:0 none',
				valueComponentMap: {},
				items: [
					{
						xtype: 'container',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							{
								xtype: 'panel',
								columnWidth: 0.34,
								bodyStyle: 'border-style:none; padding:0 0 0 8px',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												pt.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: PT.i18n.weeks,
										cls: 'pt-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_WEEK',
										boxLabel: PT.i18n.last_week
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_4_WEEKS',
										boxLabel: PT.i18n.last_4_weeks
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_12_WEEKS',
										boxLabel: PT.i18n.last_12_weeks
									}
								]
							},
							{
								xtype: 'panel',
								columnWidth: 0.33,
								bodyStyle: 'border-style:none',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												pt.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: PT.i18n.months,
										cls: 'pt-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_MONTH',
										boxLabel: PT.i18n.last_month
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_3_MONTHS',
										boxLabel: PT.i18n.last_3_months
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_12_MONTHS',
										boxLabel: PT.i18n.last_12_months,
										checked: true
									}
								]
							},
							{
								xtype: 'panel',
								columnWidth: 0.33,
								bodyStyle: 'border-style:none',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												pt.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: PT.i18n.bimonths,
										cls: 'pt-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_BIMONTH',
										boxLabel: PT.i18n.last_bimonth
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_6_BIMONTHS',
										boxLabel: PT.i18n.last_6_bimonths
									}
								]
							}
						]
					},
					{
						xtype: 'container',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							{
								xtype: 'panel',
								columnWidth: 0.34,
								bodyStyle: 'border-style:none; padding:5px 0 0 10px',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												pt.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: PT.i18n.quarters,
										cls: 'pt-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_QUARTER',
										boxLabel: PT.i18n.last_quarter
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_4_QUARTERS',
										boxLabel: PT.i18n.last_4_quarters
									}
								]
							},
							{
								xtype: 'panel',
								columnWidth: 0.33,
								bodyStyle: 'border-style:none; padding:5px 0 0',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												pt.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: PT.i18n.sixmonths,
										cls: 'pt-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_SIX_MONTH',
										boxLabel: PT.i18n.last_sixmonth
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_2_SIXMONTHS',
										boxLabel: PT.i18n.last_2_sixmonths
									}
								]
							},
							{
								xtype: 'panel',
								columnWidth: 0.33,
								bodyStyle: 'border-style:none; padding:5px 0 0',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												pt.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: PT.i18n.financial_years,
										cls: 'pt-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_FINANCIAL_YEAR',
										boxLabel: PT.i18n.last_financial_year
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_5_FINANCIAL_YEARS',
										boxLabel: PT.i18n.last_5_financial_years
									}
								]
							}

							//{
								//xtype: 'panel',
								//layout: 'anchor',
								//bodyStyle: 'border-style:none; padding:5px 0 0 46px',
								//defaults: {
									//labelSeparator: '',
									//style: 'margin-bottom:2px',
								//},
								//items: [
									//{
										//xtype: 'label',
										//text: 'Options',
										//cls: 'pt-label-period-heading-options'
									//},
									//rewind
								//]
							//}
						]
					},
					{
						xtype: 'container',
						layout: 'column',
						bodyStyle: 'border-style:none',
						items: [
							{
								xtype: 'panel',
								columnWidth: 0.35,
								bodyStyle: 'border-style:none; padding:5px 0 0 10px',
								defaults: {
									labelSeparator: '',
									style: 'margin-bottom:2px',
									listeners: {
										added: function(chb) {
											if (chb.xtype === 'checkbox') {
												pt.cmp.dimension.relativePeriod.checkbox.push(chb);
												relativePeriod.valueComponentMap[chb.relativePeriodId] = chb;
											}
										},
										change: function() {
											rewind.xable();
										}
									}
								},
								items: [
									{
										xtype: 'label',
										text: PT.i18n.years,
										cls: 'pt-label-period-heading'
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'THIS_YEAR',
										boxLabel: PT.i18n.this_year
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_YEAR',
										boxLabel: PT.i18n.last_year
									},
									{
										xtype: 'checkbox',
										relativePeriodId: 'LAST_5_YEARS',
										boxLabel: PT.i18n.last_5_years
									}
								]
							}
						]
					}
				]
			};

			fixedPeriodAvailable = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'pt-toolbar-multiselect-left',
				width: (pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding) / 2,
				height: 180,
				valueField: 'id',
				displayField: 'name',
				store: pt.store.fixedPeriodAvailable,
				tbar: [
					{
						xtype: 'label',
						text: PT.i18n.available,
						cls: 'pt-toolbar-multiselect-left-label'
					},
					'->',
					{
						xtype: 'button',
						icon: 'images/arrowright.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.select(fixedPeriodAvailable, fixedPeriodSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowrightdouble.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.selectAll(fixedPeriodAvailable, fixedPeriodSelected, true);
						}
					},
					' '
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							pt.util.multiselect.select(fixedPeriodAvailable, fixedPeriodSelected);
						}, this);
					}
				}
			});

			fixedPeriodSelected = Ext.create('Ext.ux.form.MultiSelect', {
				cls: 'pt-toolbar-multiselect-right',
				width: (pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding) / 2,
				height: 180,
				valueField: 'id',
				displayField: 'name',
				ddReorder: true,
				store: pt.store.fixedPeriodSelected,
				tbar: [
					' ',
					{
						xtype: 'button',
						icon: 'images/arrowleftdouble.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.unselectAll(fixedPeriodAvailable, fixedPeriodSelected);
						}
					},
					{
						xtype: 'button',
						icon: 'images/arrowleft.png',
						width: 22,
						handler: function() {
							pt.util.multiselect.unselect(fixedPeriodAvailable, fixedPeriodSelected);
						}
					},
					'->',
					{
						xtype: 'label',
						text: PT.i18n.selected,
						cls: 'pt-toolbar-multiselect-right-label'
					}
				],
				listeners: {
					afterrender: function() {
						this.boundList.on('itemdblclick', function() {
							pt.util.multiselect.unselect(fixedPeriodAvailable, fixedPeriodSelected);
						}, this);
					}
				}
			});

			period = {
				xtype: 'panel',
				title: '<div class="pt-panel-title-period">Periods</div>',
				hideCollapseTool: true,
				getData: function() {
					var data = {
							dimensionName: pt.conf.finals.dimension.period.dimensionName,
							objectName: pt.conf.finals.dimension.period.objectName,
							items: []
						},
						chb = pt.cmp.dimension.relativePeriod.checkbox;

					pt.store.fixedPeriodSelected.each( function(r) {
						data.items.push(r.data.id);
					});

					for (var i = 0; i < chb.length; i++) {
						if (chb[i].getValue()) {
							data.items.push(chb[i].relativePeriodId);
						}
					}

					return data.items.length ? data : null;
				},
				onExpand: function() {
					var h = pt.viewport.westRegion.hasScrollbar ?
						pt.conf.layout.west_scrollbarheight_accordion_period : pt.conf.layout.west_maxheight_accordion_period;
					pt.util.dimension.panel.setHeight(h);
					pt.util.multiselect.setHeight(
						[fixedPeriodAvailable, fixedPeriodSelected],
						this,
						pt.conf.layout.west_fill_accordion_period
					);
				},
				items: [
					{
						xtype: 'panel',
						layout: 'column',
						bodyStyle: 'border-style:none',
						style: 'margin-top:0px',
						items: [
							{
								xtype: 'combobox',
								cls: 'pt-combo',
								style: 'margin-bottom:2px',
								width: pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding - 62 - 62 - 4,
								valueField: 'id',
								displayField: 'name',
								emptyText: PT.i18n.select_period_type,
								editable: false,
								queryMode: 'remote',
								store: pt.store.periodType,
								periodOffset: 0,
								listeners: {
									select: function() {
										var ptype = new PeriodType(),
											periodType = this.getValue();

										var periods = ptype.get(periodType).generatePeriods({
											offset: this.periodOffset,
											filterFuturePeriods: true,
											reversePeriods: true
										});

										pt.store.fixedPeriodAvailable.setIndex(periods);
										pt.store.fixedPeriodAvailable.loadData(periods);
										pt.util.multiselect.filterAvailable(fixedPeriodAvailable, fixedPeriodSelected);
									}
								}
							},
							{
								xtype: 'button',
								text: PT.i18n.prev_year,
								style: 'margin-left:2px; border-radius:2px',
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
								text: PT.i18n.next_year,
								style: 'margin-left:2px; border-radius:2px',
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
						bodyStyle: 'border-style:none; padding-bottom:2px',
						items: [
							fixedPeriodAvailable,
							fixedPeriodSelected
						]
					},
					relativePeriod
				],
				listeners: {
					added: function() {
						pt.cmp.dimension.panels.push(this);
					},
					expand: function(p) {
						p.onExpand();
					}
				}
			};

			treePanel = Ext.create('Ext.tree.Panel', {
				cls: 'pt-tree',
				style: 'border-top: 1px solid #ddd; padding-top: 1px',
				width: pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding,
				rootVisible: false,
				autoScroll: true,
				multiSelect: true,
				rendered: false,
				reset: function() {
					var rootNode = this.getRootNode().findChild('id', pt.init.rootNodes[0].id);
					this.collapseAll();
					this.expandPath(rootNode.getPath());
					this.getSelectionModel().select(rootNode);
				},
				selectRootIf: function() {
					if (this.getSelectionModel().getSelection().length < 1) {
						var node = this.getRootNode().findChild('id', pt.init.rootNodes[0].id);
						if (this.rendered) {
							this.getSelectionModel().select(node);
						}
						return node;
					}
				},
				numberOfRecords: 0,
				recordsToSelect: [],
				multipleSelectIf: function(doUpdate) {
					if (this.recordsToSelect.length === this.numberOfRecords) {
						this.getSelectionModel().select(this.recordsToSelect);
						this.recordsToSelect = [];
						this.numberOfRecords = 0;

						if (doUpdate) {
							update();
						}
					}
				},
				multipleExpand: function(id, path, doUpdate) {
					this.expandPath('/' + pt.conf.finals.root.id + path, 'id', '/', function() {
						var record = this.getRootNode().findChild('id', id, true);
						this.recordsToSelect.push(record);
						this.multipleSelectIf(doUpdate);
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
							var a = Ext.decode(r.responseText).organisationUnits;
							this.numberOfRecords = a.length;
							for (var i = 0; i < a.length; i++) {
								this.multipleExpand(a[i].id, a[i].path);
							}
						}
					});
				},
				selectByGroup: function(id) {
					if (id) {
						var url = pt.conf.finals.ajax.path_pivot + pt.conf.finals.ajax.organisationunit_getbygroup,
							params = {id: id};
						this.select(url, params);
					}
				},
				selectByLevel: function(level) {
					if (level) {
						var url = pt.conf.finals.ajax.path_pivot + pt.conf.finals.ajax.organisationunit_getbylevel,
							params = {level: level};
						this.select(url, params);
					}
				},
				selectByIds: function(ids) {
					if (ids) {
						var url = pt.conf.finals.ajax.path_pivot + pt.conf.finals.ajax.organisationunit_getbyids;
						Ext.Array.each(ids, function(item) {
							url = Ext.String.urlAppend(url, 'ids=' + item);
						});
						if (!this.rendered) {
							pt.cmp.dimension.organisationUnit.panel.expand();
						}
						this.select(url);
					}
				},
				store: Ext.create('Ext.data.TreeStore', {
					proxy: {
						type: 'ajax',
						url: pt.conf.finals.ajax.path_pivot + pt.conf.finals.ajax.organisationunitchildren_get
					},
					root: {
						id: pt.conf.finals.root.id,
						expanded: true,
						children: pt.init.rootNodes
					},
					listeners: {
						load: function(s, node, r) {
							//for (var i = 0; i < r.length; i++) {
								//r[i].data.text = pt.conf.util.jsonEncodeString(r[i].data.text);
							//}
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
						pt.cmp.dimension.organisationUnit.treepanel = this;
					},
					render: function() {
						this.rendered = true;
					},
					afterrender: function() {
						this.getSelectionModel().select(0);
					},
					itemcontextmenu: function(v, r, h, i, e) {
						v.getSelectionModel().select(r, false);

						if (v.menu) {
							v.menu.destroy();
						}
						v.menu = Ext.create('Ext.menu.Menu', {
							id: 'treepanel-contextmenu',
							showSeparator: false,
							shadow: false
						});
						if (!r.data.leaf) {
							v.menu.add({
								id: 'treepanel-contextmenu-item',
								text: PT.i18n.select_all_children,
								icon: 'images/node-select-child.png',
								handler: function() {
									r.expand(false, function() {
										v.getSelectionModel().select(r.childNodes, true);
										v.getSelectionModel().deselect(r);
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
			});

			userOrganisationUnit = Ext.create('Ext.form.field.Checkbox', {
				columnWidth: 0.5,
				boxLabel: PT.i18n.user_organisation_unit,
				labelWidth: pt.conf.layout.form_label_width,
				handler: function(chb, checked) {
					treePanel.xable(checked, userOrganisationUnitChildren.getValue());
				}
			});

			userOrganisationUnitChildren = Ext.create('Ext.form.field.Checkbox', {
				columnWidth: 0.5,
				boxLabel: PT.i18n.user_organisation_unit_children,
				labelWidth: pt.conf.layout.form_label_width,
				handler: function(chb, checked) {
					treePanel.xable(checked, userOrganisationUnit.getValue());
				}
			});

			organisationUnit = {
				xtype: 'panel',
				title: '<div class="pt-panel-title-organisationunit">' + PT.i18n.organisation_units + '</div>',
				bodyStyle: 'padding-top:5px',
				hideCollapseTool: true,
				collapsed: false,
				getData: function() {
					var records = treePanel.getSelectionModel().getSelection(),
						data = {
							dimensionName: pt.conf.finals.dimension.organisationUnit.dimensionName,
							objectName: pt.conf.finals.dimension.organisationUnit.objectName,
							items: []
						};

					for (var i = 0; i < records.length; i++) {
						data.items.push(records[i].data.id);
					}

					return data.items.length ? data : null;
				},
				onExpand: function() {
					var h = pt.viewport.westRegion.hasScrollbar ?
						pt.conf.layout.west_scrollbarheight_accordion_organisationunit : pt.conf.layout.west_maxheight_accordion_organisationunit;
					pt.util.dimension.panel.setHeight(h);
					treePanel.setHeight(this.getHeight() - pt.conf.layout.west_fill_accordion_organisationunit);
				},
				items: [
					{
						layout: 'column',
						bodyStyle: 'border:0 none; padding-bottom:3px; padding-left:7px',
						items: [
							userOrganisationUnit,
							userOrganisationUnitChildren
						]
					},
					treePanel
				],
				listeners: {
					added: function() {
						pt.cmp.dimension.panels.push(this);
					},
					expand: function(p) {
						p.onExpand();
					}
				}
			};

			getGroupSetPanels = function(groupSets, objectName, iconCls) {
				var	getAvailableStore,
					getSelectedStore,

					createPanel,
					getPanels;

				getAvailableStore = function(groupSet) {
					return Ext.create('Ext.data.Store', {
						fields: ['id', 'name'],
						data: groupSet.items,
						isLoaded: false,
						storage: {},
						sortStore: function() {
							this.sort('name', 'ASC');
						},
						reload: function() {
							this.removeAll();
							this.storage = {};
							this.loadData(groupSet.items);
						},
						listeners: {
							load: function(s) {
								s.isLoaded = true;
								//s.each( function(r) {
									//r.data.name = pt.conf.util.jsonEncodeString(r.data.name);
								//});
								pt.util.store.addToStorage(s);
							}
						}
					});
				};

				getSelectedStore = function() {
					return Ext.create('Ext.data.Store', {
						fields: ['id', 'name'],
						data: []
					});
				};

				createPanel = function(groupSet) {
					var getAvailable,
						getSelected,

						availableStore,
						selectedStore,
						available,
						selected,

						panel;

					getAvailable = function(availableStore) {
						return Ext.create('Ext.ux.form.MultiSelect', {
							cls: 'pt-toolbar-multiselect-left',
							width: (pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding) / 2,
							valueField: 'id',
							displayField: 'name',
							store: availableStore,
							tbar: [
								{
									xtype: 'label',
									text: PT.i18n.available,
									cls: 'pt-toolbar-multiselect-left-label'
								},
								'->',
								{
									xtype: 'button',
									icon: 'images/arrowright.png',
									width: 22,
									handler: function() {
										pt.util.multiselect.select(available, selected);
									}
								},
								{
									xtype: 'button',
									icon: 'images/arrowrightdouble.png',
									width: 22,
									handler: function() {
										pt.util.multiselect.selectAll(available, selected);
									}
								}
							],
							listeners: {
								afterrender: function() {
									this.boundList.on('itemdblclick', function() {
										pt.util.multiselect.select(available, selected);
									}, this);
								}
							}
						});
					};

					getSelected = function(selectedStore) {
						return Ext.create('Ext.ux.form.MultiSelect', {
							cls: 'pt-toolbar-multiselect-right',
							width: (pt.conf.layout.west_fieldset_width - pt.conf.layout.west_width_padding) / 2,
							valueField: 'id',
							displayField: 'name',
							ddReorder: true,
							store: selectedStore,
							tbar: [
								{
									xtype: 'button',
									icon: 'images/arrowleftdouble.png',
									width: 22,
									handler: function() {
										pt.util.multiselect.unselectAll(available, selected);
									}
								},
								{
									xtype: 'button',
									icon: 'images/arrowleft.png',
									width: 22,
									handler: function() {
										pt.util.multiselect.unselect(available, selected);
									}
								},
								'->',
								{
									xtype: 'label',
									text: PT.i18n.selected,
									cls: 'pt-toolbar-multiselect-right-label'
								}
							],
							listeners: {
								afterrender: function() {
									this.boundList.on('itemdblclick', function() {
										pt.util.multiselect.unselect(available, selected);
									}, this);
								}
							}
						});
					};

					availableStore = getAvailableStore(groupSet);
					selectedStore = getSelectedStore();

					groupSetIdAvailableStoreMap[groupSet.id] = availableStore;
					groupSetIdSelectedStoreMap[groupSet.id] = selectedStore;

					available = getAvailable(availableStore);
					selected = getSelected(selectedStore);

					availableStore.on('load', function() {
						pt.util.multiselect.filterAvailable(available, selected);
					});

					panel = {
						xtype: 'panel',
						title: '<div class="' + iconCls + '">' + groupSet.name + '</div>',
						hideCollapseTool: true,
						getData: function() {
							var data = {
								dimensionName: groupSet.id,
								objectName: objectName,
								items: []
							};

							selectedStore.each( function(r) {
								data.items.push(r.data.id);
							});

							return data.items.length ? data : null;
						},
						onExpand: function() {
							if (!availableStore.isLoaded) {
								availableStore.load();
							}

							var h = pt.viewport.westRegion.hasScrollbar ?
								pt.conf.layout.west_scrollbarheight_accordion_group : pt.conf.layout.west_maxheight_accordion_group;
							pt.util.dimension.panel.setHeight(h);

							pt.util.multiselect.setHeight(
								[available, selected],
								this,
								pt.conf.layout.west_fill_accordion_dataset
							);
						},
						items: [
							{
								xtype: 'panel',
								layout: 'column',
								bodyStyle: 'border-style:none',
								items: [
									available,
									selected
								]
							}
						],
						listeners: {
							added: function() {
								pt.cmp.dimension.panels.push(this);
							},
							expand: function(p) {
								p.onExpand();
							}
						}
					};

					return panel;
				};

				getPanels = function() {
					var panels = [],
						groupSet,
						last;

					for (var i = 0, panel; i < groupSets.length; i++) {
						groupSet = groupSets[i];

						panel = createPanel(groupSet);

						panels.push(panel);
					}

					return panels;
				};

				return getPanels();
			};

			validateSpecialCases = function(layout) {
				var dimConf = pt.conf.finals.dimension,
					dimensionNames = [],
					layoutObjects = [].concat(Ext.clone(layout.col || []), Ext.clone(layout.row || []), Ext.clone(layout.filter || []));

				// Layout names
				for (var i = 0; i < layoutObjects.length; i++) {
					dimensionNames.push(layoutObjects[i].dimensionName);
				}

				// Indicator as filter
				if (layout.filter && pt.store.indicatorSelected.data.length) {
					for (var i = 0; i < layout.filter.length; i++) {
						if (layout.filter[i].dimensionName === dimConf.data.dimensionName) {
							alert(PT.i18n.indicators_cannot_be_specified_as_filter);
							return;
						}
					}
				}

				// Categories as filter
				if (layout.filter && pt.viewport.layoutWindow.filterStore.getById(dimConf.category.dimensionName)) {
					alert(PT.i18n.categories_cannot_be_specified_as_filter);
					return;
				}

				// Degs and datasets in the same query
				if (Ext.Array.contains(dimensionNames, dimConf.data.dimensionName) && pt.store.dataSetSelected.data.length) {
					for (var i = 0; i < pt.init.degs.length; i++) {
						if (Ext.Array.contains(dimensionNames, pt.init.degs[i].id)) {
							alert(PT.i18n.data_element_group_sets_cannot_be_specified_together_with_data_sets);
							return;
						}
					}
				}

				return true;
			};

			update = function() {
				var config = pt.util.pivot.getLayoutConfig(),
					layout = pt.api.Layout(config);

				if (!layout) {
					return;
				}
				if (!validateSpecialCases(layout)) {
					return;
				}

				if (layout) {
					pt.util.pivot.createTable(layout, pt);
				}
			};

			accordionBody = Ext.create('Ext.panel.Panel', {
				layout: 'accordion',
				activeOnTop: true,
				cls: 'pt-accordion',
				bodyStyle: 'border:0 none; margin-bottom:2px',
				height: 700,
				items: function() {
					var panels = [
						indicator,
						dataElement,
						dataSet,
						period,
						organisationUnit
					],
					ougs = Ext.clone(pt.init.ougs),
					degs = Ext.clone(pt.init.degs);

					pt.util.array.sortObjectsByString(ougs);
					pt.util.array.sortObjectsByString(degs);

					panels = panels.concat(getGroupSetPanels(ougs, pt.conf.finals.dimension.organisationUnitGroupSet.objectName, 'pt-panel-title-organisationunitgroupset'));
					panels = panels.concat(getGroupSetPanels(degs, pt.conf.finals.dimension.dataElementGroupSet.objectName, 'pt-panel-title-dataelementgroupset'));

					last = panels[panels.length - 1];
					last.cls = 'pt-accordion-last';

					return panels;
				}()
			});

			accordion = Ext.create('Ext.panel.Panel', {
				bodyStyle: 'border-style:none; padding:2px; padding-bottom:0; overflow-y:scroll;',
				items: accordionBody,
				listeners: {
					added: function() {
						pt.cmp.dimension.accordion = this;
					}
				}
			});

			westRegion = Ext.create('Ext.panel.Panel', {
				region: 'west',
				preventHeader: true,
				collapsible: true,
				collapseMode: 'mini',
				width: function() {
					if (Ext.isWebKit) {
						return pt.conf.layout.west_width + 8;
					}
					else {
						if (Ext.isLinux && Ext.isGecko) {
							return pt.conf.layout.west_width + 13;
						}
						return pt.conf.layout.west_width + 17;
					}
				}(),
				items: accordion
			});

			layoutButton = Ext.create('Ext.button.Button', {
				text: 'Layout',
				menu: {},
				handler: function() {
					if (!pt.viewport.layoutWindow) {
						pt.viewport.layoutWindow = PT.app.LayoutWindow(pt);
					}

					pt.viewport.layoutWindow.show();
				}
			});

			optionsButton = Ext.create('Ext.button.Button', {
				text: 'Options',
				menu: {},
				handler: function() {
					if (!pt.viewport.optionsWindow) {
						pt.viewport.optionsWindow = PT.app.OptionsWindow();
					}

					pt.viewport.optionsWindow.show();
				}
			});

			favoriteButton = Ext.create('Ext.button.Button', {
				text: 'Favorites',
				menu: {},
				handler: function() {
					if (pt.viewport.favoriteWindow) {
						pt.viewport.favoriteWindow.destroy();
					}

					pt.viewport.favoriteWindow = PT.app.FavoriteWindow();
					pt.viewport.favoriteWindow.show();
				}
			});

			downloadButton = Ext.create('Ext.button.Button', {
				text: 'Download',
				disabled: true,
				menu: {
					cls: 'pt-menu',
					width: 105,
					shadow: false,
					showSeparator: false,
					items: [
						{
							text: 'Excel (XLS)',
							iconCls: 'pt-menu-item-xls',
							handler: function() {
								if (pt.baseUrl && pt.paramString) {
									window.location.href = pt.baseUrl + '/api/analytics.xls' + pt.paramString;
								}
							}
						},
						{
							text: 'CSV',
							iconCls: 'pt-menu-item-csv',
							handler: function() {
								if (pt.baseUrl && pt.paramString) {
									window.location.href = pt.baseUrl + '/api/analytics.csv' + pt.paramString;
								}
							}
						},
						{
							text: 'JSON',
							iconCls: 'pt-menu-item-csv',
							handler: function() {
								if (pt.baseUrl && pt.paramString) {
									window.open(pt.baseUrl + '/api/analytics.json' + pt.paramString);
								}
							}
						},
						{
							text: 'XML',
							iconCls: 'pt-menu-item-csv',
							handler: function() {
								if (pt.baseUrl && pt.paramString) {
									window.open(pt.baseUrl + '/api/analytics.xml' + pt.paramString);
								}
							}
						}
					],
					listeners: {
						afterrender: function() {
							this.getEl().addCls('pt-toolbar-btn-menu');
						}
					}
				}
			});

			centerRegion = Ext.create('Ext.panel.Panel', {
				region: 'center',
				bodyStyle: 'padding:1px',
				autoScroll: true,
				tbar: {
                    defaults: {
                        height: 26
                    },
					items: [
						{
							text: '<<<',
							handler: function(b) {
								var text = b.getText();
								text = text === '<<<' ? '>>>' : '<<<';
								b.setText(text);

								westRegion.toggleCollapse();
							}
						},
						{
							text: '<b>' + PT.i18n.update + '</b>',
							handler: function() {
								update();
							}
						},
						layoutButton,
						optionsButton,
						{
							xtype: 'tbseparator',
							height: 18,
							style: 'border-color:transparent; border-right-color:#d1d1d1; margin-right:4px',
						},
						favoriteButton,
						downloadButton,
                        '->',
						{
							text: PT.i18n.table,
                            toggleGroup: 'module',
							pressed: true
						},
						{
							text: PT.i18n.chart,
                            toggleGroup: 'module',
							handler: function(b) {
                                window.location.href = '../../dhis-web-visualizer/app/index.html';
							}
						},
						{
							text: PT.i18n.map,
                            toggleGroup: 'module',
							handler: function(b) {
                                window.location.href = '../../dhis-web-mapping/app/index.html';
							}
						},
						{
							xtype: 'tbseparator',
							height: 18,
							style: 'border-color:transparent; border-right-color:#d1d1d1; margin-right:4px',
						},
                        {
                            xtype: 'button',
                            text: PT.i18n.home,
                            handler: function() {
                                window.location.href = '../../dhis-web-commons-about/redirect.action';
                            }
                        }
					]
				},
				listeners: {
					afterrender: function(p) {
						var liStyle = 'padding:3px 10px; color:#333',
							html = '';

						html += '<div style="padding:20px">';
						html += '<div style="font-size:14px; padding-bottom:8px">' + PT.i18n.example1 + '</div>';
						html += '<div style="' + liStyle + '">- ' + PT.i18n.example2 + '</div>';
						html += '<div style="' + liStyle + '">- ' + PT.i18n.example3 + '</div>';
						html += '<div style="' + liStyle + '">- ' + PT.i18n.example4 + '</div>';
						html += '<div style="font-size:14px; padding-top:20px; padding-bottom:8px">' + PT.i18n.example5 + '</div>';
						html += '<div style="' + liStyle + '">- ' + PT.i18n.example6 + '</div>';
						html += '<div style="' + liStyle + '">- ' + PT.i18n.example7 + '</div>';
						html += '<div style="' + liStyle + '">- ' + PT.i18n.example8 + '</div>';
						html += '</div>';

						p.update(html);
					}
				}
			});

			setFavorite = function(r) {

				// Indicators
				pt.store.indicatorSelected.removeAll();
				if (Ext.isArray(r.indicators)) {
					pt.store.indicatorSelected.add(r.indicators);
				}

				// Data elements
				pt.store.dataElementSelected.removeAll();
				if (Ext.isArray(r.dataElements)) {
					pt.store.dataElementSelected.add(r.dataElements);
				}

				// Data sets
				pt.store.dataSetSelected.removeAll();
				if (Ext.isArray(r.dataSets)) {
					pt.store.dataSetSelected.add(r.dataSets);
				}

				// Fixed periods
				pt.store.fixedPeriodSelected.removeAll();
				if (Ext.isArray(r.periods)) {
					pt.store.fixedPeriodSelected.add(r.periods);
				}

				// Relative periods
				if (Ext.isObject(r.relativePeriods)) {
					for (var key in r.relativePeriods) {
						if (r.relativePeriods.hasOwnProperty(key) && pt.conf.period.relativePeriodParamKeys.hasOwnProperty(key)) {
							var value = pt.conf.period.relativePeriodParamKeys[key];
							relativePeriod.valueComponentMap[value].setValue(!!r.relativePeriods[key]);
						}
					}
				}

				// Organisation units: tree sync/async

				// User orgunit
				userOrganisationUnit.setValue(r.userOrganisationUnit);
				userOrganisationUnitChildren.setValue(r.userOrganisationUnitChildren);

				// Reset groupset stores
				for (var key in groupSetIdSelectedStoreMap) {
					if (groupSetIdSelectedStoreMap.hasOwnProperty(key)) {
						var a = groupSetIdAvailableStoreMap[key],
							s = groupSetIdSelectedStoreMap[key];

						if (s.getCount() > 0) {
							a.reload();
							s.removeAll();
						}
					}
				}

				// Organisation unit group sets
				if (Ext.isObject(r.organisationUnitGroupSets)) {
					for (var key in r.organisationUnitGroupSets) {
						if (r.organisationUnitGroupSets.hasOwnProperty(key)) {
							groupSetIdSelectedStoreMap[key].add(r.organisationUnitGroupSets[key]);
							pt.util.multiselect.filterAvailable({store: groupSetIdAvailableStoreMap[key]}, {store: groupSetIdSelectedStoreMap[key]});
						}
					}
				}

				// Data element group sets
				if (Ext.isObject(r.dataElementGroupSets)) {
					for (var key in r.dataElementGroupSets) {
						if (r.dataElementGroupSets.hasOwnProperty(key)) {
							groupSetIdSelectedStoreMap[key].add(r.dataElementGroupSets[key]);
							pt.util.multiselect.filterAvailable({store: groupSetIdAvailableStoreMap[key]}, {store: groupSetIdSelectedStoreMap[key]});
						}
					}
				}

				// Layout
				pt.viewport.dimensionStore.reset(true);
				pt.viewport.colStore.removeAll();
				pt.viewport.rowStore.removeAll();
				pt.viewport.filterStore.removeAll();

				if (Ext.isArray(r.columnDimensions)) {
					for (var i = 0, dim; i < r.columnDimensions.length; i++) {
						dim = pt.conf.finals.dimension.objectNameMap[r.columnDimensions[i]];

						pt.viewport.colStore.add({
							id: dim.dimensionName,
							name: dim.name
						});

						pt.viewport.dimensionStore.remove(pt.viewport.dimensionStore.getById(dim.dimensionName));

					}
				}

				if (Ext.isArray(r.rowDimensions)) {
					for (var i = 0, dim; i < r.rowDimensions.length; i++) {
						dim = pt.conf.finals.dimension.objectNameMap[r.rowDimensions[i]];

						pt.viewport.rowStore.add({
							id: dim.dimensionName,
							name: dim.name
						});

						pt.viewport.dimensionStore.remove(pt.viewport.dimensionStore.getById(dim.dimensionName));
					}
				}

				if (Ext.isArray(r.filterDimensions)) {
					for (var i = 0, dim; i < r.filterDimensions.length; i++) {
						dim = pt.conf.finals.dimension.objectNameMap[r.filterDimensions[i]];

						pt.viewport.filterStore.add({
							id: dim.dimensionName,
							name: dim.name
						});

						pt.viewport.dimensionStore.remove(pt.viewport.dimensionStore.getById(dim.dimensionName));
					}
				}

				// Options
				pt.viewport.showTotals.setValue(r.totals);
				pt.viewport.showSubTotals.setValue(r.subtotals);
				pt.viewport.hideEmptyRows.setValue(r.hideEmptyRows);
				pt.viewport.displayDensity.setValue(r.displayDensity);
				pt.viewport.fontSize.setValue(r.fontSize);
				pt.viewport.digitGroupSeparator.setValue(r.digitGroupSeparator);

				if (Ext.isObject(r.reportParams)) {
					pt.viewport.reportingPeriod.setValue(r.reportParams.paramReportingPeriod);
					pt.viewport.organisationUnit.setValue(r.reportParams.paramOrganisationUnit);
					pt.viewport.parentOrganisationUnit.setValue(r.reportParams.paramParentOrganisationUnit);
				}

				// Upgrade fixes
				if (!Ext.isArray(r.organisationUnits) || !r.organisationUnits.length) {
					if (Ext.isObject(r.reportParams) && r.reportParams.paramOrganisationUnit) {
						userOrganisationUnit.setValue(true);
					}

					if (Ext.isObject(r.reportParams) && r.reportParams.paramParentOrganisationUnit) {
						userOrganisationUnit.setValue(true);
					}
				}

				// Organisation units: If fav has organisation units, execute from tree callback instead
				if (Ext.isArray(r.organisationUnits) && Ext.isObject(r.parentGraphMap)) {
					treePanel.numberOfRecords = pt.util.object.getLength(r.parentGraphMap);
					for (var key in r.parentGraphMap) {
						if (r.parentGraphMap.hasOwnProperty(key)) {
							treePanel.multipleExpand(key, r.parentGraphMap[key], true);
						}
					}
				}
				else {
					treePanel.reset();
					update();
				}
			};

			viewport = Ext.create('Ext.container.Viewport', {
				layout: 'border',
				accordion: accordion,
				accordionBody: accordionBody,
				westRegion: westRegion,
				centerRegion: centerRegion,
				updateViewport: update,
				layoutButton: layoutButton,
				optionsButton: optionsButton,
				favoriteButton: favoriteButton,
				downloadButton: downloadButton,
				userOrganisationUnit: userOrganisationUnit,
				userOrganisationUnitChildren: userOrganisationUnitChildren,
				setFavorite: setFavorite,
				items: [
					westRegion,
					centerRegion
				],
				listeners: {
					render: function(vp) {
						pt.viewport = vp;
					},
					afterrender: function() {
						pt.init.afterRender();
					}
				}
			});

			addListeners = function() {
				pt.store.indicatorAvailable.on('load', function() {
					pt.util.multiselect.filterAvailable(indicatorAvailable, indicatorSelected);
				});

				pt.store.dataElementAvailable.on('load', function() {
					pt.util.multiselect.filterAvailable(dataElementAvailable, dataElementSelected);
				});

				pt.store.dataSetAvailable.on('load', function(s) {
					pt.util.multiselect.filterAvailable(dataSetAvailable, dataSetSelected);
					s.sort('name', 'ASC');
				});
			}();

			return viewport;
		};

		pt.init = PT.app.getInits(r);
		pt.baseUrl = pt.init.contextPath;

		pt.util = PT.app.getUtils();

		pt.store = PT.app.getStores();

		pt.cmp = PT.app.getCmp();

		pt.viewport = createViewport();

		pt.viewport.layoutWindow = PT.app.LayoutWindow();
		pt.viewport.layoutWindow.hide();

		pt.viewport.optionsWindow = PT.app.OptionsWindow();
		pt.viewport.optionsWindow.hide();
	};

	Ext.Ajax.request({
		url: pt.conf.finals.ajax.path_pivot + 'initialize.action',
		success: function(r) {
			PT.app.init.onInitialize(r);
	}});
});
